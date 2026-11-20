package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.StunExecutionManager;
import com.spartanforge.godofwar.magic.GodOfWarMagic;
import com.spartanforge.godofwar.magic.MagicManager;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Magia: Ejército de Hades (God of War I)
 * Compatible con Clic Derecho y tecla [G] en Modo Batalla de Epic Fight.
 */
public class ArmyOfHadesPowerItem extends Item implements GodOfWarWeaponAbility {
    public ArmyOfHadesPowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        int dispLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        float mult = WeaponUpgradeHelper.getPowerMultiplier(stack);
        AABB area = player.getBoundingBox().inflate(13.0D + dispLvl);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
            t -> t != player && t.isAlive());

        int maxSouls = 6 + dispLvl * 2;
        int count = 0;
        for (LivingEntity target : targets) {
            if (count >= maxSouls) break;
            Vec3 from = player.getEyePosition();
            Vec3 to = target.getEyePosition();
            for (int i = 1; i <= 12; i++) {
                double t = i / 12.0D;
                serverLevel.sendParticles(ParticleTypes.SOUL,
                    from.x + (to.x - from.x) * t,
                    from.y + (to.y - from.y) * t + Math.sin(t * Math.PI) * 1.2D,
                    from.z + (to.z - from.z) * t,
                    2, 0.15, 0.15, 0.15, 0.02);
                serverLevel.sendParticles(ParticleTypes.WITCH,
                    from.x + (to.x - from.x) * t,
                    from.y + (to.y - from.y) * t,
                    from.z + (to.z - from.z) * t,
                    1, 0.1, 0.1, 0.1, 0.01);
            }
            target.hurt(serverLevel.damageSources().magic(), 15.0F * mult);
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100 + dispLvl * 20, 1));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            StunExecutionManager.addStun(serverLevel, target, player, 18.0F * mult);
            count++;
        }

        if (count > 0) {
            player.heal(Math.min(12.0F, count * 2.0F));
        }
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 1.0, player.getZ(), 30, 1.5, 0.8, 1.5, 0.06);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.3F, 0.75F);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.5F, 0.85F);
        player.getCooldowns().addCooldown(this, Math.max(75, 140 - (dispLvl - 1) * 14));
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
            if (!MagicManager.isUnlocked(sp, GodOfWarMagic.ARMY_OF_HADES)) {
                MagicManager.unlockMagic(sp, GodOfWarMagic.ARMY_OF_HADES);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } else {
                triggerSpecialAbility(serverLevel, sp, stack, player.isShiftKeyDown());
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int lvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        tooltip.add(Component.literal("Ejército de Hades — Nivel " + lvl + "/5 (Curios: [X] | Mano: [G])")
            .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Invoca almas buscadoras que marchitan y drenan vida").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("• Equipable en Curios (Poder Divino) y mejorable en [K] Potenciador").withStyle(ChatFormatting.GOLD));
    }
}
