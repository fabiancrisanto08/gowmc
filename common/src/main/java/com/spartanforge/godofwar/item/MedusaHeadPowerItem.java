package com.spartanforge.godofwar.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.List;

/**
 * Reliquia: Cabeza de Medusa (Saga Griega)
 * Reliquia clásica que convierte temporalmente a enemigos normales en estatuas frágiles.
 */
public class MedusaHeadPowerItem extends Item {
    public MedusaHeadPowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            AABB area = player.getBoundingBox().inflate(6.0D);
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
                target -> target != player && target.isAlive());

            for (LivingEntity target : targets) {
                target.hurt(level.damageSources().playerAttack(player), 6.0F);
                
                
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140, 10));
            }

            serverLevel.sendParticles(
                ParticleTypes.FLAME,
                player.getX(), player.getY() + 1.0D, player.getZ(),
                40, 2.5D, 0.8D, 2.5D, 0.08D
            );
            level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.0F, 1.0F);
            player.getCooldowns().addCooldown(this, 120);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
