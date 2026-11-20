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
 * Garras de Hades (Saga Griega)
 * Compatible con Epic Fight (Dagas Dobles ultrarrápidas con Estela del Inframundo).
 * - Pasiva: Drena +2 HP en cada golpe cuerpo a cuerpo.
 * - Clic Derecho o [G]: Invoca almas buscadoras del Inframundo que persiguen y marchitan enemigos.
 */
public class ClawsOfHadesItem extends SwordItem implements GodOfWarWeaponAbility {
    public ClawsOfHadesItem(Properties properties) {
        super(Tiers.NETHERITE, 10, -2.0F, properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        float bonus = WeaponUpgradeHelper.getBonusDamage(stack);
        AABB area = player.getBoundingBox().inflate(10.0D);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
            t -> t != player && t.isAlive());

        int drained = 0;
        for (LivingEntity target : targets) {
            Vec3 from = player.getEyePosition();
            Vec3 to = target.getEyePosition();
            for (int i = 1; i <= 8; i++) {
                double t = i / 8.0D;
                serverLevel.sendParticles(ParticleTypes.SOUL,
                    from.x + (to.x - from.x) * t,
                    from.y + (to.y - from.y) * t,
                    from.z + (to.z - from.z) * t,
                    2, 0.12, 0.12, 0.12, 0.01);
            }
            target.hurt(serverLevel.damageSources().magic(), 12.0F + bonus * 0.8F);
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
            StunExecutionManager.addStun(serverLevel, target, player, 16.0F);
            drained++;
        }
        if (drained > 0) {
            player.heal(Math.min(8.0F, drained * 2.0F));
        }
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.5F, 0.75F);
        player.getCooldowns().addCooldown(this, 55);
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
            "Pasiva: Drena +2 HP al golpear enemigos",
            "Clic Derecho / [G]: Libera almas buscadoras del Inframundo (+Wither y curación)");
    }
}
