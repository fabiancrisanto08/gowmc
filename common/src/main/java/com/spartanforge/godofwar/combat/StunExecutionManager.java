package com.spartanforge.godofwar.combat;

import com.spartanforge.godofwar.entity.base.AbstractGodBossEntity;
import com.spartanforge.godofwar.item.BladeOfOlympusItem;
import com.spartanforge.godofwar.item.BladesOfAthenaItem;
import com.spartanforge.godofwar.item.BladesOfChaosItem;
import com.spartanforge.godofwar.item.ClawsOfHadesItem;
import com.spartanforge.godofwar.item.DraupnirSpearItem;
import com.spartanforge.godofwar.item.GuardianShieldItem;
import com.spartanforge.godofwar.item.LeviathanAxeItem;
import com.spartanforge.godofwar.item.NemeanCestusItem;
import com.spartanforge.godofwar.item.WeaponUpgradeHelper;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import com.spartanforge.godofwar.registry.ModItems;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sistema de Medidor de Aturdimiento (Stun Meter) y Ejecuciones Brutales (R3 / Tecla [V]) estilo God of War:
 * - Golpear enemigos acumula Aturdimiento (especialmente alto con Cestus de Nemea, puño limpio y Parry Dorado).
 * - Al llegar al 100%, el enemigo queda aturdido durante 7 segundos y muestra el aviso [V] R3 EJECUTAR.
 * - Al ejecutar: Kratos se lanza sobre el enemigo con i-frames, ejecuta un remate específico del arma,
 *   aniquila mobs comunes o inflige un golpe masivo a jefes y absorbe Orbes Verdes de Vida y Orbes Rojos de Furia.
 */
public final class StunExecutionManager {
    private static final int STUN_WINDOW_TICKS = 140;      // 7 segundos para rematar
    private static final int BOSS_IMMUNITY_TICKS = 200;    // 10 segundos de inmunidad en jefes tras un remate
    private static final int DECAY_DELAY_TICKS = 130;      // Comienza a decaer tras 6.5s sin recibir golpes

    private static final class StunEntry {
        float stun;
        float maxStun;
        long lastHitTick;
        long stunnedUntilTick;
        long immunityUntilTick;
        int entityId;

        StunEntry(int entityId, float maxStun) {
            this.entityId = entityId;
            this.maxStun = maxStun;
        }

        boolean isStunned(long now) {
            return stunnedUntilTick > now;
        }
    }

    private static final Map<UUID, StunEntry> ENTRIES = new ConcurrentHashMap<>();
    private static final ThreadLocal<Boolean> EXECUTING_GUARD = ThreadLocal.withInitial(() -> false);

    private StunExecutionManager() {}

    public static void register() {
        TickEvent.SERVER_LEVEL_POST.register(StunExecutionManager::onServerLevelTick);

        // Alternativa intuitiva: agacharse (Shift) + golpear a un enemigo aturdido también dispara la ejecución brutal
        PlayerEvent.ATTACK_ENTITY.register((player, level, target, hand, result) -> {
            if (!level.isClientSide && player instanceof ServerPlayer sp && level instanceof ServerLevel sl
                && player.isShiftKeyDown() && target instanceof LivingEntity living && isStunned(living)) {
                executeTarget(sl, sp, living);
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
    }

    public static boolean isExecuting() {
        return EXECUTING_GUARD.get();
    }

    public static boolean isBossEntity(LivingEntity entity) {
        return entity instanceof AbstractGodBossEntity || entity.getMaxHealth() >= 200.0F;
    }

    public static float computeMaxStun(LivingEntity target) {
        if (isBossEntity(target)) {
            return Math.min(260.0F, Math.max(130.0F, target.getMaxHealth() * 0.40F));
        }
        return Math.min(90.0F, Math.max(28.0F, target.getMaxHealth() * 1.15F));
    }

    public static float getWeaponBaseStun(Player attacker, ItemStack mainHand) {
        Item item = mainHand.getItem();
        if (item instanceof NemeanCestusItem) return 24.0F;
        if (item instanceof GuardianShieldItem) return 26.0F;
        if (item instanceof BladeOfOlympusItem) return 18.0F;
        if (item instanceof LeviathanAxeItem) return 16.0F;
        if (item instanceof BladesOfChaosItem) {
            // Bono si lleva Doble Espada del Caos (Dual Wield con Epic Fight)
            return attacker.getOffhandItem().getItem() instanceof BladesOfChaosItem ? 20.0F : 15.0F;
        }
        if (item instanceof BladesOfAthenaItem) {
            return attacker.getOffhandItem().getItem() instanceof BladesOfAthenaItem ? 21.0F : 16.0F;
        }
        if (item instanceof DraupnirSpearItem) return 14.0F;
        if (item instanceof ClawsOfHadesItem) {
            return attacker.getOffhandItem().getItem() instanceof ClawsOfHadesItem ? 18.0F : 14.0F;
        }
        if (mainHand.isEmpty()) return 17.0F; // Puño espartano desarmado acumula mucho Stun
        return 11.0F;
    }

    public static boolean isStunned(LivingEntity entity) {
        StunEntry entry = ENTRIES.get(entity.getUUID());
        return entry != null && entry.isStunned(entity.level().getGameTime());
    }

    public static void addStun(ServerLevel level, LivingEntity target, Player attacker, float baseAmount) {
        if (target == null || !target.isAlive() || target instanceof Player || target == attacker || EXECUTING_GUARD.get()) {
            return;
        }
        long now = level.getGameTime();
        StunEntry entry = ENTRIES.computeIfAbsent(target.getUUID(), id -> new StunEntry(target.getId(), computeMaxStun(target)));
        entry.entityId = target.getId();
        entry.maxStun = computeMaxStun(target);

        if (now < entry.immunityUntilTick) {
            return;
        }
        if (entry.isStunned(now)) {
            return;
        }

        float mult = attacker != null ? PlayerProgressionManager.getStunMultiplier(attacker) : 1.0F;
        if (attacker != null && GodOfWarCombatEvents.isSpartanRageActive(attacker)) {
            mult *= 1.4F;
        }
        entry.stun = Math.min(entry.maxStun, entry.stun + baseAmount * mult);
        entry.lastHitTick = now;

        boolean justStunned = false;
        if (entry.stun >= entry.maxStun) {
            entry.stun = entry.maxStun;
            entry.stunnedUntilTick = now + STUN_WINDOW_TICKS;
            justStunned = true;

            // Inmoviliza al enemigo aturdido listo para ejecución [V]
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, STUN_WINDOW_TICKS, 6, false, false));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, STUN_WINDOW_TICKS, 2, false, false));
            target.setDeltaMovement(0, target.getDeltaMovement().y * 0.2D, 0);
            target.hurtMarked = true;

            level.playSound(null, target.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.35F, 1.45F);
            level.playSound(null, target.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.9F, 1.3F);
            level.sendParticles(ParticleTypes.FLASH, target.getX(), target.getY() + target.getBbHeight() + 0.3, target.getZ(), 1, 0, 0, 0, 0);
            level.sendParticles(ParticleTypes.CRIT, target.getX(), target.getY() + target.getBbHeight() * 0.7, target.getZ(), 24, 0.6, 0.5, 0.6, 0.15);

            if (attacker instanceof ServerPlayer sp) {
                sp.displayClientMessage(
                    Component.literal("⚡ ¡ENEMIGO ATURDIDO! Presiona [V] (o Shift+Golpe) para EJECUCIÓN BRUTAL ⚡")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
            }
        }

        int left = justStunned ? STUN_WINDOW_TICKS : 0;
        GodOfWarNetwork.broadcastStun(level, target.getX(), target.getY(), target.getZ(),
            target.getId(), entry.stun, entry.maxStun, entry.isStunned(now), left);
    }

    /**
     * Busca el mejor enemigo aturdido dentro de 6 bloques y realiza el remate brutal.
     */
    public static boolean attemptExecution(ServerLevel level, ServerPlayer player) {
        if (!player.isAlive() || player.isSpectator()) return false;

        AABB box = player.getBoundingBox().inflate(6.2D);
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, box,
            e -> e != player && e.isAlive() && isStunned(e));
        if (candidates.isEmpty()) {
            return false;
        }

        Vec3 look = player.getLookAngle().normalize();
        Vec3 eye = player.getEyePosition();
        LivingEntity best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (LivingEntity c : candidates) {
            Vec3 toTarget = c.getEyePosition().subtract(eye);
            double dist = Math.max(0.5D, toTarget.length());
            double dot = toTarget.normalize().dot(look);
            double score = dot * 3.0D - dist * 0.35D;
            if (score > bestScore) {
                bestScore = score;
                best = c;
            }
        }

        if (best == null) return false;
        executeTarget(level, player, best);
        return true;
    }

    public static void executeTarget(ServerLevel level, ServerPlayer player, LivingEntity target) {
        if (!target.isAlive()) return;
        long now = level.getGameTime();
        boolean boss = isBossEntity(target);
        ItemStack weapon = player.getMainHandItem();

        // 1. Invulnerabilidad cinemática breve (i-frames estilo R3 de God of War)
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 28, 4, false, false));

        // 2. Acercamiento dinámico (lunge) frente al objetivo
        Vec3 diff = target.position().subtract(player.position());
        double horizDist = Math.hypot(diff.x, diff.z);
        if (horizDist > 2.0D && horizDist < 6.5D) {
            Vec3 dir = new Vec3(diff.x, 0, diff.z).normalize();
            Vec3 lungeDest = target.position().subtract(dir.scale(1.55D));
            if (level.noCollision(player, player.getBoundingBox().move(lungeDest.subtract(player.position())))) {
                player.teleportTo(lungeDest.x, target.getY(), lungeDest.z);
            } else {
                player.setDeltaMovement(dir.scale(0.9D).add(0, 0.25D, 0));
                player.hurtMarked = true;
            }
        }

        // 3. Efectos visuales y sonoros únicos según el arma empuñada
        String banner = spawnWeaponExecutionVisuals(level, player, target, weapon, boss);

        // 4. Daño de Ejecución Brutal (+75% Orbes de Sangre en ejecuciones letales!)
        float forgeBonus = WeaponUpgradeHelper.getBonusDamage(weapon);
        BloodOrbManager.markExecuted(target.getUUID());
        EXECUTING_GUARD.set(true);
        try {
            target.invulnerableTime = 0;
            if (boss) {
                float bossDamage = Math.min(115.0F, Math.max(48.0F, target.getMaxHealth() * 0.18F)) + forgeBonus * 1.5F;
                target.hurt(level.damageSources().playerAttack(player), bossDamage);
                Vec3 knock = target.position().subtract(player.position()).normalize().scale(1.1D);
                target.push(knock.x, 0.4D, knock.z);
                target.hurtMarked = true;
            } else {
                // Aniquilación brutal instantánea en enemigos comunes
                target.hurt(level.damageSources().playerAttack(player), Math.max(999.0F, target.getMaxHealth() * 10.0F));
                if (target.isAlive()) {
                    target.kill();
                }
            }
        } finally {
            EXECUTING_GUARD.set(false);
        }

        // Onda expansiva que empuja a otros enemigos cercanos
        AABB shockArea = target.getBoundingBox().inflate(4.5D);
        for (LivingEntity nearby : level.getEntitiesOfClass(LivingEntity.class, shockArea,
            e -> e != player && e != target && e.isAlive())) {
            Vec3 push = nearby.position().subtract(target.position()).normalize().scale(1.25D);
            nearby.push(push.x, 0.42D, push.z);
            nearby.hurtMarked = true;
        }

        // 5. Recompensa de Orbes Verdes de Vida y Orbes Rojos de Furia Espartana
        float extraHeal = PlayerProgressionManager.getExtraExecutionHeal(player);
        player.heal((boss ? 10.0F : 6.0F) + extraHeal);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 1, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 0, false, true));

        // Reduce enfriamiento de Ira Espartana si está en cooldown
        if (player.getCooldowns().isOnCooldown(ModItems.SPARTAN_RAGE_POWER.get())) {
            player.getCooldowns().removeCooldown(ModItems.SPARTAN_RAGE_POWER.get());
        }

        // Estela visual de Orbes Verdes y Rojos absorbiéndose en Kratos
        Vec3 from = target.position().add(0, target.getBbHeight() * 0.6D, 0);
        Vec3 to = player.position().add(0, 1.1D, 0);
        for (int i = 0; i <= 10; i++) {
            double t = i / 10.0D;
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t + Math.sin(t * Math.PI) * 0.5, from.z + (to.z - from.z) * t,
                1, 0.1, 0.1, 0.1, 0.01);
            level.sendParticles(ParticleTypes.FLAME,
                from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t, from.z + (to.z - from.z) * t,
                1, 0.08, 0.08, 0.08, 0.01);
        }

        // Extra Chaos Ash al ejecutar brutalmente
        if (level.random.nextFloat() < (boss ? 1.0F : 0.45F)) {
            ItemEntity ash = new ItemEntity(level, target.getX(), target.getY() + 0.5D, target.getZ(),
                new ItemStack(ModItems.CHAOS_ASH.get(), boss ? 2 : 1));
            level.addFreshEntity(ash);
        }

        // 6. Limpiar estado de stun y aplicar inmunidad temporal en jefes
        StunEntry entry = ENTRIES.get(target.getUUID());
        if (entry != null) {
            entry.stun = 0.0F;
            entry.stunnedUntilTick = 0L;
            if (boss) {
                entry.immunityUntilTick = now + BOSS_IMMUNITY_TICKS;
            } else {
                ENTRIES.remove(target.getUUID());
            }
        }
        GodOfWarNetwork.broadcastStun(level, target.getX(), target.getY(), target.getZ(),
            target.getId(), 0.0F, 100.0F, false, 0);

        // 7. Efecto de cámara y destello en el cliente del jugador
        GodOfWarNetwork.sendExecutionFx(player, boss ? 4.8F : 3.4F, boss ? 18 : 13, boss, banner);
        player.displayClientMessage(Component.literal(banner).withStyle(boss ? ChatFormatting.GOLD : ChatFormatting.RED, ChatFormatting.BOLD), true);
    }

    private static String spawnWeaponExecutionVisuals(ServerLevel level, ServerPlayer player, LivingEntity target, ItemStack weapon, boolean boss) {
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.55D;
        double z = target.getZ();
        Item item = weapon.getItem();

        level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);

        if (item instanceof BladesOfChaosItem) {
            level.sendParticles(ParticleTypes.FLAME, x, y, z, 55, 0.9, 0.8, 0.9, 0.18);
            level.sendParticles(ParticleTypes.LAVA, x, y, z, 22, 0.6, 0.6, 0.6, 0.12);
            level.playSound(null, target.blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.PLAYERS, 1.5F, 0.7F);
            level.playSound(null, target.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.1F, 1.1F);
            return boss ? "🔥 ¡DESTROZO DE LAS CADENAS DEL CAOS! 🔥" : "🔥 ¡EJECUCIÓN DEL CAOS! 🔥";
        }
        if (item instanceof BladesOfAthenaItem) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(x, target.getY(), z);
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 65, 1.0, 0.9, 1.0, 0.22);
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 35, 0.8, 0.8, 0.8, 0.12);
            level.playSound(null, target.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.4F, 1.25F);
            return boss ? "✦ ¡SENTENCIA DIVINA DE ATENEA! ✦" : "✦ ¡EJECUCIÓN DE ATENEA! ✦";
        }
        if (item instanceof LeviathanAxeItem) {
            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 65, 1.0, 0.9, 1.0, 0.2);
            level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 3, 0.4, 0.4, 0.4, 0.02);
            level.playSound(null, target.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.5F, 0.8F);
            level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.4F, 0.75F);
            return boss ? "❄ ¡TAJO GÉLIDO DEL LEVIATÁN! ❄" : "❄ ¡EJECUCIÓN DE ESCARCHA! ❄";
        }
        if (item instanceof BladeOfOlympusItem) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(x, target.getY(), z);
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 60, 1.0, 1.0, 1.0, 0.16);
            level.playSound(null, target.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.5F, 1.0F);
            return boss ? "⚡ ¡SENTENCIA DE LA HOJA DEL OLIMPO! ⚡" : "⚡ ¡EJECUCIÓN OLÍMPICA! ⚡";
        }
        if (item instanceof DraupnirSpearItem) {
            level.sendParticles(ParticleTypes.CRIT, x, y, z, 60, 1.1, 0.9, 1.1, 0.25);
            level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 4, 0.5, 0.5, 0.5, 0.02);
            level.playSound(null, target.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.2F, 1.35F);
            return boss ? "✦ ¡IMPLOSIÓN DE DRAUPNIR! ✦" : "✦ ¡EJECUCIÓN DE DRAUPNIR! ✦";
        }
        if (item instanceof NemeanCestusItem) {
            level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 6, 0.8, 0.6, 0.8, 0.05);
            level.sendParticles(ParticleTypes.CRIT, x, y, z, 55, 1.2, 0.8, 1.2, 0.25);
            level.playSound(null, target.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.3F, 0.7F);
            level.playSound(null, target.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.3F, 0.65F);
            return boss ? "🦁 ¡APLASTAMIENTO DEL LEÓN DE NEMEA! 🦁" : "🦁 ¡DEMOLICIÓN DE NEMEA! 🦁";
        }
        if (item instanceof ClawsOfHadesItem) {
            level.sendParticles(ParticleTypes.SOUL, x, y, z, 55, 0.9, 0.9, 0.9, 0.14);
            level.sendParticles(ParticleTypes.WITCH, x, y, z, 35, 0.8, 0.8, 0.8, 0.1);
            level.playSound(null, target.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.6F, 0.7F);
            level.playSound(null, target.blockPosition(), SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 1.1F, 0.85F);
            return boss ? "💀 ¡DESGARRO DE ALMAS DE HADES! 💀" : "💀 ¡COSECHA DEL INFRAMUNDO! 💀";
        }

        level.sendParticles(ParticleTypes.CRIT, x, y, z, 45, 0.8, 0.8, 0.8, 0.22);
        level.sendParticles(ParticleTypes.FLAME, x, y, z, 28, 0.7, 0.7, 0.7, 0.1);
        level.playSound(null, target.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.1F, 0.85F);
        level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.4F, 0.7F);
        return boss ? "⚔ ¡REMATE DE DIOSES! ⚔" : "⚔ ¡EJECUCIÓN ESPARTANA! ⚔";
    }

    private static void onServerLevelTick(ServerLevel level) {
        long now = level.getGameTime();
        if (now % 4 != 0 || ENTRIES.isEmpty()) return;

        Iterator<Map.Entry<UUID, StunEntry>> it = ENTRIES.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, StunEntry> pair = it.next();
            UUID uuid = pair.getKey();
            StunEntry entry = pair.getValue();
            Entity ent = level.getEntity(uuid);

            if (ent == null) {
                // Podría estar en otra dimensión; limpia solo si pasó mucho tiempo sin actividad
                if (now - entry.lastHitTick > 600 && entry.stunnedUntilTick < now) {
                    it.remove();
                }
                continue;
            }
            if (!(ent instanceof LivingEntity living) || !living.isAlive()) {
                GodOfWarNetwork.broadcastStun(level, ent.getX(), ent.getY(), ent.getZ(), entry.entityId, 0, entry.maxStun, false, 0);
                it.remove();
                continue;
            }

            if (entry.isStunned(now)) {
                int left = (int) (entry.stunnedUntilTick - now);
                // Halo dorado-carmesí girando sobre la cabeza del enemigo aturdido
                double ang = (now * 0.35D) % (Math.PI * 2.0D);
                double r = Math.max(0.55D, living.getBbWidth() * 0.65D);
                double headY = living.getY() + living.getBbHeight() + 0.35D;
                level.sendParticles(ParticleTypes.CRIT,
                    living.getX() + Math.cos(ang) * r, headY, living.getZ() + Math.sin(ang) * r,
                    1, 0, 0, 0, 0.0);
                level.sendParticles(ParticleTypes.FLAME,
                    living.getX() - Math.cos(ang) * r, headY, living.getZ() - Math.sin(ang) * r,
                    1, 0, 0, 0, 0.0);
                if (now % 8 == 0) {
                    GodOfWarNetwork.broadcastStun(level, living.getX(), living.getY(), living.getZ(),
                        living.getId(), entry.stun, entry.maxStun, true, left);
                }
            } else if (entry.stunnedUntilTick > 0L && now >= entry.stunnedUntilTick) {
                // Terminó la ventana de ejecución sin ser ejecutado
                entry.stunnedUntilTick = 0L;
                entry.stun = 0.0F;
                entry.immunityUntilTick = now + 40;
                GodOfWarNetwork.broadcastStun(level, living.getX(), living.getY(), living.getZ(),
                    living.getId(), 0.0F, entry.maxStun, false, 0);
            } else if (entry.stun > 0.0F && now - entry.lastHitTick > DECAY_DELAY_TICKS) {
                // Decaimiento gradual fuera de combate
                entry.stun = Math.max(0.0F, entry.stun - (entry.maxStun * 0.06F));
                if (now % 8 == 0 || entry.stun <= 0.01F) {
                    GodOfWarNetwork.broadcastStun(level, living.getX(), living.getY(), living.getZ(),
                        living.getId(), entry.stun, entry.maxStun, false, 0);
                }
                if (entry.stun <= 0.01F && now >= entry.immunityUntilTick) {
                    it.remove();
                }
            }
        }
    }
}
