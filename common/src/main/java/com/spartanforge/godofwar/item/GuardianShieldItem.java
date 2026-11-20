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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Escudo del Guardián (Saga Nórdica)
 * Compatible con Epic Fight (Escudo en mano secundaria + Parry Dorado + Golpe de Escudo vía [G]).
 * - Mantener Clic Derecho: Bloquea golpes con ventana de Parry Dorado (0.7s) que aturde masivamente.
 * - Shift + Clic Derecho o [G]: Golpe de Escudo Dorado (enorme acumulación de Stun y empuje).
 */
public class GuardianShieldItem extends Item implements GodOfWarWeaponAbility {
    public GuardianShieldItem(Properties properties) {
        super(properties.durability(900));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        float bonus = WeaponUpgradeHelper.getBonusDamage(stack);
        Vec3 center = player.position().add(player.getLookAngle().scale(2.5D));
        AABB area = new AABB(center.x - 3.5, center.y - 1.5, center.z - 3.5,
            center.x + 3.5, center.y + 2.5, center.z + 3.5);
        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area,
            t -> t != player && t.isAlive())) {
            target.hurt(serverLevel.damageSources().playerAttack(player), 10.0F + bonus);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            StunExecutionManager.addStun(serverLevel, target, player, 34.0F);
            Vec3 away = target.position().subtract(player.position()).normalize().scale(1.5D);
            target.push(away.x, 0.45D, away.z);
            target.hurtMarked = true;
        }
        serverLevel.sendParticles(ParticleTypes.CRIT, center.x, center.y + 1.0, center.z, 26, 1.5, 0.6, 1.5, 0.2);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 1.3F);
        player.getCooldowns().addCooldown(this, 45);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
                triggerSpecialAbility(serverLevel, sp, stack, true);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Nórdica (Regalo de Faye)",
            "Mantener Clic Derecho: Bloqueo real con ventana de Parry Dorado (0.7s + mucho Stun)",
            "Shift + Clic Derecho / [G]: Golpe de Escudo expansivo");
    }
}
