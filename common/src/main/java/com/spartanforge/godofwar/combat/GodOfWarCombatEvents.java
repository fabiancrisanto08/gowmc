package com.spartanforge.godofwar.combat;

import com.spartanforge.godofwar.compat.CuriosBridge;
import com.spartanforge.godofwar.item.BladesOfAthenaItem;
import com.spartanforge.godofwar.item.BladesOfChaosItem;
import com.spartanforge.godofwar.item.ClawsOfHadesItem;
import com.spartanforge.godofwar.item.GuardianShieldItem;
import com.spartanforge.godofwar.item.ModArmorMaterial;
import com.spartanforge.godofwar.item.WeaponUpgradeHelper;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import com.spartanforge.godofwar.registry.ModItems;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Eventos de combate globales (Architectury Multi-Loader):
 * - Parry real del Escudo del Guardián (ventana de 12 ticks al bloquear)
 * - Ira Espartana activa (curación por golpe + llamas + daño extra)
 * - Robo de vida de las Garras de Hades
 * - Daño adicional de la Forja de Hefesto (Niveles I-V)
 * - Bonos de set completo de las Armaduras de Ares, Zeus y Berserker
 */
public final class GodOfWarCombatEvents {
    private static final Map<UUID, Long> SPARTAN_RAGE_UNTIL = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> REALM_SHIFT_COOLDOWN = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> FLEECE_COUNTER_COOLDOWN = new ConcurrentHashMap<>();
    private static final ThreadLocal<Boolean> REENTRANT_BONUS = ThreadLocal.withInitial(() -> false);

    private GodOfWarCombatEvents() {}

    public static void activateSpartanRage(Player player, int durationTicks) {
        SPARTAN_RAGE_UNTIL.put(player.getUUID(), player.level().getGameTime() + durationTicks);
    }

    public static boolean isSpartanRageActive(Player player) {
        Long until = SPARTAN_RAGE_UNTIL.get(player.getUUID());
        return until != null && player.level().getGameTime() < until;
    }

    public static void register() {
        EntityEvent.LIVING_HURT.register(GodOfWarCombatEvents::onLivingHurt);
        TickEvent.PLAYER_POST.register(GodOfWarCombatEvents::onPlayerTick);
    }

    private static EventResult onLivingHurt(LivingEntity victim, DamageSource source, float amount) {
        if (victim.level().isClientSide || !(victim.level() instanceof ServerLevel serverLevel)) {
            return EventResult.pass();
        }

        // 0. RELIQUIAS CURIOS DEFENSIVAS (Alas de Ícaro, Vellocino de Oro, Amuleto de Uróboros)
        if (victim instanceof Player defender) {
            // Alas de Ícaro: Inmunidad total al daño por caída
            if (source.is(DamageTypeTags.IS_FALL) && CuriosBridge.hasEquippedRelic(defender, ModItems.WINGS_OF_ICARUS.get())) {
                defender.fallDistance = 0.0F;
                serverLevel.sendParticles(ParticleTypes.CLOUD, defender.getX(), defender.getY() + 0.2, defender.getZ(), 10, 0.4, 0.1, 0.4, 0.04);
                return EventResult.interruptFalse();
            }

            // Vellocino de Oro: Mitigación + contraataque dorado reactivo
            ItemStack fleece = CuriosBridge.getEquippedRelic(defender, ModItems.GOLDEN_FLEECE.get());
            if (!fleece.isEmpty() && amount > 1.0F) {
                int fleeceLvl = WeaponUpgradeHelper.getDisplayLevel(fleece);
                float mitigation = 0.12F + (fleeceLvl - 1) * 0.05F;
                defender.heal(Math.min(amount * mitigation, 6.0F));
                long now = serverLevel.getGameTime();
                long cdUntil = FLEECE_COUNTER_COOLDOWN.getOrDefault(defender.getUUID(), 0L);
                if (now >= cdUntil && source.getEntity() instanceof LivingEntity attacker && attacker != defender && !REENTRANT_BONUS.get()) {
                    FLEECE_COUNTER_COOLDOWN.put(defender.getUUID(), now + Math.max(50, 110 - fleeceLvl * 12));
                    StunExecutionManager.addStun(serverLevel, attacker, defender, 16.0F + fleeceLvl * 4.0F);
                    REENTRANT_BONUS.set(true);
                    try {
                        attacker.hurt(serverLevel.damageSources().magic(), 4.0F + fleeceLvl * 1.5F);
                    } finally {
                        REENTRANT_BONUS.set(false);
                    }
                    serverLevel.sendParticles(ParticleTypes.WAX_ON, defender.getX(), defender.getY() + 1.0, defender.getZ(), 16, 0.8, 0.6, 0.8, 0.1);
                    serverLevel.playSound(null, defender.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 0.9F, 1.55F);
                }
            }

            // Amuleto de Uróboros: Cambio de Reino (ralentiza el tiempo a tu alrededor si bajas del 60% HP)
            ItemStack amulet = CuriosBridge.getEquippedRelic(defender, ModItems.AMULET_OF_OUROBOROS.get());
            if (!amulet.isEmpty() && (defender.getHealth() - amount) < defender.getMaxHealth() * 0.65F) {
                long now = serverLevel.getGameTime();
                long cdUntil = REALM_SHIFT_COOLDOWN.getOrDefault(defender.getUUID(), 0L);
                if (now >= cdUntil) {
                    int amLvl = WeaponUpgradeHelper.getDisplayLevel(amulet);
                    REALM_SHIFT_COOLDOWN.put(defender.getUUID(), now + Math.max(160, 280 - amLvl * 20));
                    int shiftTicks = 60 + amLvl * 14;
                    AABB area = defender.getBoundingBox().inflate(7.0D + amLvl);
                    for (LivingEntity enemy : serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != defender && e.isAlive())) {
                        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, shiftTicks, 5));
                        enemy.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, shiftTicks, 4));
                        enemy.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, shiftTicks, 1));
                    }
                    serverLevel.sendParticles(ParticleTypes.ENCHANT, defender.getX(), defender.getY() + 1.0, defender.getZ(), 45, 3.0, 1.2, 3.0, 0.25);
                    serverLevel.playSound(null, defender.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.1F, 0.7F);
                    defender.displayClientMessage(Component.literal("⏳ ¡CAMBIO DE REINO DE URÓBOROS! ⏳").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), true);
                }
            }
        }

        // 1. PARRY REAL DEL ESCUDO DEL GUARDIÁN (ampliado a 22 ticks si llevas el Vellocino de Oro en Curios!)
        if (victim instanceof Player player && player.isUsingItem() && player.getUseItem().getItem() instanceof GuardianShieldItem) {
            int ticksBlocking = player.getUseItem().getUseDuration() - player.getUseItemRemainingTicks();
            int parryWindow = CuriosBridge.hasEquippedRelic(player, ModItems.GOLDEN_FLEECE.get()) ? 22 : 14;
            if (ticksBlocking <= parryWindow) {
                // ¡PARRY PERFECTO!
                player.heal(2.5F);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.1F, 1.65F);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.0F, 1.3F);
                serverLevel.sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 1.2, player.getZ(), 1, 0, 0, 0, 0);
                serverLevel.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY() + 1.1, player.getZ(), 24, 1.2, 0.7, 1.2, 0.25);

                Entity attacker = source.getEntity();
                float bonus = WeaponUpgradeHelper.getBonusDamage(player.getUseItem());
                if (attacker instanceof LivingEntity livingAttacker && livingAttacker != player) {
                    livingAttacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 4));
                    livingAttacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 70, 2));
                    StunExecutionManager.addStun(serverLevel, livingAttacker, player, 48.0F);
                    if (!REENTRANT_BONUS.get()) {
                        REENTRANT_BONUS.set(true);
                        try {
                            livingAttacker.hurt(serverLevel.damageSources().playerAttack(player), 10.0F + bonus);
                        } finally {
                            REENTRANT_BONUS.set(false);
                        }
                    }
                    Vec3 knock = livingAttacker.position().subtract(player.position()).normalize().scale(1.6D);
                    livingAttacker.push(knock.x, 0.45D, knock.z);
                    livingAttacker.hurtMarked = true;
                }

                // Onda expansiva dorada alrededor
                AABB area = player.getBoundingBox().inflate(4.5D);
                for (LivingEntity e : serverLevel.getEntitiesOfClass(LivingEntity.class, area, v -> v != player && v.isAlive())) {
                    Vec3 push = e.position().subtract(player.position()).normalize().scale(1.1D);
                    e.push(push.x, 0.3D, push.z);
                    e.hurtMarked = true;
                }

                if (player instanceof ServerPlayer sp) {
                    GodOfWarNetwork.sendExecutionFx(sp, 2.2F, 8, false, "✦ ¡PARRY DORADO! ✦");
                }
                player.displayClientMessage(Component.literal("✦ ¡PARRY DORADO! ✦").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
                return EventResult.interruptFalse();
            } else {
                // Bloqueo normal del Escudo del Guardián reduce el golpe y evita knockback
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                return EventResult.interruptFalse();
            }
        }

        // 2. BONO DEFENSIVO DE ARMADURA DE ARES (prende fuego al atacante)
        if (victim instanceof Player defender && hasFullSet(defender, ModArmorMaterial.ARES)) {
            if (source.getEntity() instanceof LivingEntity attacker && attacker != defender) {
                attacker.setSecondsOnFire(4);
            }
        }

        // 3. EFECTOS OFENSIVOS DEL JUGADOR (Stun Meter, Ira Espartana, Doble Empuñadura Epic Fight, Garras de Hades, Forja, Armaduras)
        if (!REENTRANT_BONUS.get() && !StunExecutionManager.isExecuting() && source.getEntity() instanceof Player attacker && victim != attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            float extraDamage = 0.0F;

            // Acumulación del Medidor de Aturdimiento (Stun Meter) para Ejecuciones Brutales [V]
            float baseStun = StunExecutionManager.getWeaponBaseStun(attacker, weapon);
            StunExecutionManager.addStun(serverLevel, victim, attacker, baseStun);

            // Sinergia de Doble Espada del Caos o Doble Espada de Atenea (Dual Wield con Epic Fight)
            if (weapon.getItem() instanceof BladesOfChaosItem && attacker.getOffhandItem().getItem() instanceof BladesOfChaosItem) {
                extraDamage += 3.5F;
                victim.setSecondsOnFire(5);
                serverLevel.sendParticles(ParticleTypes.FLAME, victim.getX(), victim.getY() + 1.0, victim.getZ(), 6, 0.3, 0.4, 0.3, 0.04);
            } else if (weapon.getItem() instanceof BladesOfAthenaItem && attacker.getOffhandItem().getItem() instanceof BladesOfAthenaItem) {
                extraDamage += 4.0F;
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, victim.getX(), victim.getY() + 1.0, victim.getZ(), 8, 0.3, 0.4, 0.3, 0.06);
            }

            // Bono de la Forja de Hefesto
            int forgeLvl = WeaponUpgradeHelper.getLevel(weapon);
            if (forgeLvl > 0) {
                extraDamage += WeaponUpgradeHelper.getBonusDamage(weapon);
            }

            // Ira Espartana activa: robo de vida + fuego + daño adicional
            if (isSpartanRageActive(attacker)) {
                attacker.heal(3.5F);
                victim.setSecondsOnFire(4);
                extraDamage += 5.0F;
                serverLevel.sendParticles(ParticleTypes.FLAME, victim.getX(), victim.getY() + 1.0, victim.getZ(), 10, 0.35, 0.5, 0.35, 0.06);
            }

            // Garras de Hades: robo de vida pasivo en cada golpe cuerpo a cuerpo
            if (weapon.getItem() instanceof ClawsOfHadesItem) {
                attacker.heal(2.0F);
                serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY() + 1.0, victim.getZ(), 6, 0.3, 0.4, 0.3, 0.04);
            }

            // Set Completo de Zeus: probabilidad de invocar rayo olímpico en golpes
            if (hasFullSet(attacker, ModArmorMaterial.ZEUS) && attacker.getRandom().nextFloat() < 0.25F) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (bolt != null) {
                    bolt.moveTo(victim.getX(), victim.getY(), victim.getZ());
                    bolt.setVisualOnly(true);
                    serverLevel.addFreshEntity(bolt);
                }
                extraDamage += 6.0F;
            }

            // Set Completo de Berserker: daño adicional cuando el jugador está bajo de vida
            if (hasFullSet(attacker, ModArmorMaterial.BERSERKER)) {
                float missingRatio = 1.0F - (attacker.getHealth() / attacker.getMaxHealth());
                extraDamage += missingRatio * 10.0F;
            }

            if (extraDamage > 0.0F && victim.isAlive()) {
                REENTRANT_BONUS.set(true);
                try {
                    victim.invulnerableTime = 0;
                    victim.hurt(serverLevel.damageSources().magic(), extraDamage);
                } finally {
                    REENTRANT_BONUS.set(false);
                }
            }
        }

        return EventResult.pass();
    }

    private static void onPlayerTick(Player player) {
        if (player.level().isClientSide || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        // Partículas de aura durante la Ira Espartana
        if (isSpartanRageActive(player) && player.tickCount % 4 == 0) {
            serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 0.9, player.getZ(), 5, 0.4, 0.5, 0.4, 0.03);
        }

        // Reliquia Curios: Alas de Ícaro — planeo suave dorado al caer o saltar + impulso
        ItemStack wings = CuriosBridge.getEquippedRelic(player, ModItems.WINGS_OF_ICARUS.get());
        if (!wings.isEmpty()) {
            int wLvl = WeaponUpgradeHelper.getDisplayLevel(wings);
            if (!player.onGround() && player.getDeltaMovement().y < -0.18D) {
                player.fallDistance = 0.0F;
                double maxDrop = Math.max(-0.22D, -0.34D + wLvl * 0.03D);
                Vec3 dm = player.getDeltaMovement();
                player.setDeltaMovement(dm.x * 1.02D, Math.max(dm.y, maxDrop), dm.z * 1.02D);
                player.hurtMarked = true;
                if (player.tickCount % 3 == 0) {
                    serverLevel.sendParticles(ParticleTypes.WAX_ON, player.getX(), player.getY() + 0.8, player.getZ(), 3, 0.5, 0.2, 0.5, 0.02);
                }
            }
            if (player.tickCount % 30 == 0) {
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, wLvl >= 3 ? 1 : 0, true, false));
            }
        }

        // Reliquia Curios: Amuleto de Uróboros — regeneración pasiva según Nivel 1-5
        ItemStack amulet = CuriosBridge.getEquippedRelic(player, ModItems.AMULET_OF_OUROBOROS.get());
        if (!amulet.isEmpty()) {
            int aLvl = WeaponUpgradeHelper.getDisplayLevel(amulet);
            int interval = Math.max(35, 100 - (aLvl - 1) * 15);
            if (player.tickCount % interval == 0 && player.getHealth() < player.getMaxHealth()) {
                player.heal(1.0F + (aLvl >= 4 ? 0.5F : 0.0F));
            }
        }

        // Bonos pasivos de Set Completo de Armadura cada segundo
        if (player.tickCount % 20 == 0) {
            if (hasFullSet(player, ModArmorMaterial.ARES)) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, true, false));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 0, true, false));
            } else if (hasFullSet(player, ModArmorMaterial.ZEUS)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 0, true, false));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 0, true, false));
            } else if (hasFullSet(player, ModArmorMaterial.BERSERKER)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0, true, false));
            }
        }
    }

    public static boolean hasFullSet(Player player, ModArmorMaterial material) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack piece = player.getItemBySlot(slot);
            if (!(piece.getItem() instanceof ArmorItem armor) || armor.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }
}
