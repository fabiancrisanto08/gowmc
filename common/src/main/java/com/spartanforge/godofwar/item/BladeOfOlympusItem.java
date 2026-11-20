package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.StunExecutionManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hoja del Olimpo (Saga Griega)
 * Compatible con Epic Fight (Espadón / Greatsword a dos manos con Estela Celestial).
 * - Clic Derecho o [G]: Onda cortante de energía celestial azul de 22 bloques.
 * - Shift + Clic Derecho o [Shift+G]: Juicio del Olimpo (tormenta de rayos divinos).
 */
public class BladeOfOlympusItem extends SwordItem implements GodOfWarWeaponAbility {
    public BladeOfOlympusItem(Properties properties) {
        super(Tiers.NETHERITE, 16, -2.1F, properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        float bonus = WeaponUpgradeHelper.getBonusDamage(stack);
        if (secondary) {
            // JUICIO DEL OLIMPO
            AABB area = player.getBoundingBox().inflate(7.5D);
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
                t -> t != player && t.isAlive());
            for (LivingEntity t : targets) {
                t.hurt(serverLevel.damageSources().playerAttack(player), 24.0F + bonus);
                StunExecutionManager.addStun(serverLevel, t, player, 28.0F);
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (bolt != null) {
                    bolt.moveTo(t.getX(), t.getY(), t.getZ());
                    bolt.setVisualOnly(true);
                    serverLevel.addFreshEntity(bolt);
                }
                Vec3 away = t.position().subtract(player.position()).normalize().scale(1.3D);
                t.push(away.x, 0.55D, away.z);
                t.hurtMarked = true;
            }
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 1.0, player.getZ(), 60, 4.0, 1.0, 4.0, 0.12);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.4F, 1.15F);
            player.getCooldowns().addCooldown(this, 85);
        } else {
            // ONDA CORTANTE CELESTIAL
            Vec3 start = player.getEyePosition().subtract(0, 0.25, 0);
            Vec3 dir = player.getLookAngle();
            Set<Integer> hitIds = new HashSet<>();

            for (double d = 1.2D; d <= 22.0D; d += 0.65D) {
                Vec3 p = start.add(dir.scale(d));
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, p.x, p.y, p.z, 3, 0.25, 0.2, 0.25, 0.02);
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 2, 0.2, 0.2, 0.2, 0.04);

                AABB slice = new AABB(p.x - 1.3, p.y - 1.2, p.z - 1.3, p.x + 1.3, p.y + 1.2, p.z + 1.3);
                for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, slice,
                    t -> t != player && t.isAlive())) {
                    if (hitIds.add(target.getId())) {
                        target.hurt(serverLevel.damageSources().playerAttack(player), 20.0F + bonus);
                        StunExecutionManager.addStun(serverLevel, target, player, 20.0F);
                        target.push(dir.x * 0.9, 0.3, dir.z * 0.9);
                        target.hurtMarked = true;
                    }
                }
            }
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.3F, 1.35F);
            player.getCooldowns().addCooldown(this, 35);
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
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Griega (Arma Suprema)",
            "Clic Derecho / [G]: Onda Cortante del Olimpo (alcance de 22 bloques)",
            "Shift + Clic Derecho / [Shift+G]: Juicio del Olimpo (tormenta de rayos divinos)");
    }
}
