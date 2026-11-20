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
 * Cestus de Nemea (Saga Griega - God of War III)
 * Compatible con Epic Fight (Guanteletes / Fist de enorme impacto y aturdimiento).
 * - Golpes pesados con gran empuje y acumulación masiva de Stun.
 * - Clic Derecho o [G]: Rugido Sísmico del León que rompe defensas y dispara Stun en área.
 */
public class NemeanCestusItem extends SwordItem implements GodOfWarWeaponAbility {
    public NemeanCestusItem(Properties properties) {
        super(Tiers.NETHERITE, 13, -2.7F, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Vec3 push = target.position().subtract(attacker.position()).normalize().scale(0.95D);
        target.push(push.x, 0.35D, push.z);
        target.hurtMarked = true;
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        float bonus = WeaponUpgradeHelper.getBonusDamage(stack);
        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(look.x * 0.9D, 0.35D, look.z * 0.9D);
        player.hurtMarked = true;

        AABB area = player.getBoundingBox().inflate(6.5D);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
            t -> t != player && t.isAlive());
        for (LivingEntity target : targets) {
            target.hurt(serverLevel.damageSources().playerAttack(player), 18.0F + bonus);
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1));
            StunExecutionManager.addStun(serverLevel, target, player, 36.0F);
            Vec3 away = target.position().subtract(player.position()).normalize().scale(1.65D);
            target.push(away.x, 0.72D, away.z);
            target.hurtMarked = true;
        }

        serverLevel.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY() + 0.5, player.getZ(), 5, 2.2, 0.5, 2.2, 0.05);
        serverLevel.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY() + 1.0, player.getZ(), 35, 3.0, 0.6, 3.0, 0.25);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.1F, 0.8F);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.1F, 0.7F);
        player.getCooldowns().addCooldown(this, 60);
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
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Griega (God of War III)",
            "Pasiva: Puñetazos demoledores con empuje y máximo Aturdimiento (Stun)",
            "Clic Derecho / [G]: Rugido Sísmico de Nemea (eleva, debilita y aturde)");
    }
}
