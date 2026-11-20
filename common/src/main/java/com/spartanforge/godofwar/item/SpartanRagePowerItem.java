package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.GodOfWarCombatEvents;
import com.spartanforge.godofwar.combat.PlayerProgressionManager;
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
 * Poder: Ira Espartana (Todas las Sagas)
 * Compatible con Clic Derecho y tecla [G] en Modo Batalla de Epic Fight.
 */
public class SpartanRagePowerItem extends Item implements GodOfWarWeaponAbility {
    public SpartanRagePowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        int dispLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        float mult = WeaponUpgradeHelper.getPowerMultiplier(stack);
        int duration = 240 + (dispLvl - 1) * 40 + PlayerProgressionManager.getExtraRageTicks(player);
        GodOfWarCombatEvents.activateSpartanRage(player, duration);

        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, dispLvl >= 4 ? 3 : 2));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, dispLvl >= 5 ? 2 : 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, 0));
        player.heal(6.0F * mult);

        AABB area = player.getBoundingBox().inflate(6.0D + (dispLvl - 1) * 0.6D);
        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area,
            t -> t != player && t.isAlive())) {
            target.hurt(serverLevel.damageSources().playerAttack(player), 14.0F * mult);
            target.setSecondsOnFire(6 + dispLvl);
            StunExecutionManager.addStun(serverLevel, target, player, 28.0F * mult);
            Vec3 away = target.position().subtract(player.position()).normalize().scale(1.4D);
            target.push(away.x, 0.5D, away.z);
            target.hurtMarked = true;
        }

        serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 1.0, player.getZ(), 70, 2.5, 1.2, 2.5, 0.14);
        serverLevel.sendParticles(ParticleTypes.LAVA, player.getX(), player.getY() + 1.0, player.getZ(), 20, 1.8, 0.8, 1.8, 0.08);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.4F, 0.75F);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 0.85F);

        int seconds = duration / 20;
        player.displayClientMessage(Component.literal("🔥 ¡IRA ESPARTANA NIV." + dispLvl + "! (" + seconds + "s — +40% Stun y Robo de Vida) 🔥")
            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
        player.getCooldowns().addCooldown(this, Math.max(200, 360 - (dispLvl - 1) * 30));
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
        tooltip.add(Component.literal("Poder Supremo de Kratos — Nivel " + lvl + "/5 (Curios: [X] | Mano: [G])")
            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Fuerza III+, Velocidad II, Resistencia II y +40% Aturdimiento").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Equipable en Curios (Poder Divino) y mejorable en [K] Potenciador").withStyle(ChatFormatting.GOLD));
    }
}
