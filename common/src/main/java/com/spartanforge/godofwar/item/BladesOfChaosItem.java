package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.StunExecutionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Espadas del Caos (Forjadas por Ares — Saga Griega / Nórdica)
 * Evoluciona visual y mecánicamente del Nivel 1 al Nivel 5 en el menú POTENCIADOR [K]:
 * - Niv. 1: Acero ennegrecido crudo de Ares.
 * - Niv. 2: Grabados de bronce y primeras brasas.
 * - Niv. 3: Runas de magma encendidas + mayor alcance de cadenas.
 * - Niv. 4: Filo volcánico ardiente + Ciclón explosivo de lava.
 * - Niv. 5 (FORMA FINAL DE ARES): Llama infernal dorada-carmesí + Ciclón Infernal de Ares.
 */
public class BladesOfChaosItem extends SwordItem implements GodOfWarWeaponAbility {
    public BladesOfChaosItem(Properties properties) {
        super(Tiers.NETHERITE, 10, -2.0F, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int displayLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        target.setSecondsOnFire(2 + displayLvl * 2);
        if (displayLvl >= 3 && target.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.FLAME, target.getX(), target.getY() + 1.0, target.getZ(), 4 + displayLvl * 2, 0.3, 0.4, 0.3, 0.04);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        int displayLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        float bonus = WeaponUpgradeHelper.getBonusDamage(stack);

        if (secondary) {
            // CICLÓN DEL CAOS -> evoluciona a CICLÓN INFERNAL DE ARES al Niv. 4-5
            double radius = 5.0D + (displayLvl - 1) * 0.65D;
            AABB area = player.getBoundingBox().inflate(radius);
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
                t -> t != player && t.isAlive());
            for (LivingEntity target : targets) {
                target.hurt(serverLevel.damageSources().playerAttack(player), 15.0F + bonus);
                target.setSecondsOnFire(4 + displayLvl * 2);
                StunExecutionManager.addStun(serverLevel, target, player, 18.0F + displayLvl * 4.0F);
                Vec3 away = target.position().subtract(player.position()).normalize().scale(0.85D + displayLvl * 0.14D);
                target.push(away.x, 0.35D + displayLvl * 0.06D, away.z);
                target.hurtMarked = true;
            }
            for (int i = 0; i < 36 + displayLvl * 4; i++) {
                double ang = (2.0D * Math.PI * i) / (36.0D + displayLvl * 4.0D);
                double r = 3.5D + displayLvl * 0.45D;
                serverLevel.sendParticles(ParticleTypes.FLAME,
                    player.getX() + Math.cos(ang) * r,
                    player.getY() + 1.0D,
                    player.getZ() + Math.sin(ang) * r,
                    2, 0.15, 0.25, 0.15, 0.06);
                if (displayLvl >= 4 && i % 3 == 0) {
                    serverLevel.sendParticles(ParticleTypes.LAVA,
                        player.getX() + Math.cos(ang) * (r * 0.85D),
                        player.getY() + 1.1D,
                        player.getZ() + Math.sin(ang) * (r * 0.85D),
                        1, 0.2, 0.2, 0.2, 0.05);
                }
            }
            if (displayLvl >= 5) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY() + 1.0, player.getZ(), 3, 1.5, 0.4, 1.5, 0.02);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 0.9F);
                player.displayClientMessage(Component.literal("🔥 ¡CICLÓN INFERNAL DE ARES! 🔥").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
            }
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.3F, 0.85F);
            player.getCooldowns().addCooldown(this, Math.max(38, 56 - displayLvl * 3));
        } else {
            // LÁTIGO DE CADENAS DEL TÁRTARO (alcance escala de 14 a 20 bloques)
            double reach = 14.0D + (displayLvl - 1) * 1.5D;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            LivingEntity hooked = null;
            double bestDist = reach;

            for (double d = 1.2D; d <= reach; d += 0.65D) {
                Vec3 p = start.add(look.scale(d));
                serverLevel.sendParticles(ParticleTypes.FLAME, p.x, p.y, p.z, displayLvl >= 4 ? 3 : 2, 0.08, 0.08, 0.08, 0.01);
                if (displayLvl >= 4 && (int) (d * 2) % 3 == 0) {
                    serverLevel.sendParticles(ParticleTypes.LAVA, p.x, p.y, p.z, 1, 0.05, 0.05, 0.05, 0.01);
                } else if ((int) (d * 2) % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.SMOKE, p.x, p.y, p.z, 1, 0.05, 0.05, 0.05, 0.01);
                }
                AABB probe = new AABB(p.x - 1.0, p.y - 1.0, p.z - 1.0, p.x + 1.0, p.y + 1.0, p.z + 1.0);
                List<LivingEntity> found = serverLevel.getEntitiesOfClass(LivingEntity.class, probe,
                    t -> t != player && t.isAlive());
                if (!found.isEmpty()) {
                    hooked = found.get(0);
                    bestDist = d;
                    break;
                }
            }

            if (hooked != null) {
                hooked.hurt(serverLevel.damageSources().playerAttack(player), 12.0F + bonus);
                hooked.setSecondsOnFire(4 + displayLvl * 2);
                StunExecutionManager.addStun(serverLevel, hooked, player, 22.0F + displayLvl * 2.5F);
                if (hooked.getMaxHealth() > 150.0F) {
                    Vec3 dash = hooked.position().subtract(player.position()).normalize().scale(1.65D);
                    player.setDeltaMovement(dash.x, 0.45D, dash.z);
                    player.hurtMarked = true;
                } else {
                    Vec3 pull = player.position().subtract(hooked.position()).normalize().scale(Math.min(1.8D, bestDist * 0.22D));
                    hooked.setDeltaMovement(pull.x, 0.42D, pull.z);
                    hooked.hurtMarked = true;
                }
                serverLevel.playSound(null, hooked.blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.PLAYERS, 1.3F, 0.85F);
                player.getCooldowns().addCooldown(this, 28);
            } else {
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 0.7F);
                player.getCooldowns().addCooldown(this, 16);
            }
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
            triggerSpecialAbility(serverLevel, sp, stack, player.isShiftKeyDown());
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int lvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        tooltip.add(Component.literal("🔥 Forma Visual: Espadas de Ares — Fase " + lvl + "/5 (Cambia al mejorar)")
            .withStyle(ChatFormatting.RED));
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Griega (Forjadas por Ares)",
            "Clic Derecho / [G]: Cadena del Caos (atrae enemigos o te impulsa a jefes)",
            "Shift + Clic Derecho / [Shift+G]: Ciclón del Caos (explosivo al Niv. 4-5)");
    }
}
