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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reliquia: Cabeza de Medusa (God of War I)
 * Compatible con Clic Derecho y tecla [G] en Modo Batalla de Epic Fight.
 */
public class MedusaHeadPowerItem extends Item implements GodOfWarWeaponAbility {
    public MedusaHeadPowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        int dispLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        float mult = WeaponUpgradeHelper.getPowerMultiplier(stack);
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Set<Integer> petrified = new HashSet<>();

        double maxRange = 11.0D + dispLvl * 1.2D;
        for (double d = 1.5D; d <= maxRange; d += 0.7D) {
            double spread = 0.4D + d * 0.22D;
            Vec3 p = start.add(look.scale(d));
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, p.x, p.y, p.z, 3, spread * 0.5, spread * 0.35, spread * 0.5, 0.02);
            serverLevel.sendParticles(ParticleTypes.ASH, p.x, p.y, p.z, 2, spread * 0.4, spread * 0.3, spread * 0.4, 0.01);

            AABB coneBox = new AABB(p.x - spread, p.y - spread, p.z - spread, p.x + spread, p.y + spread, p.z + spread);
            for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, coneBox,
                t -> t != player && t.isAlive())) {
                if (petrified.add(target.getId())) {
                    boolean isBoss = target.getMaxHealth() > 200.0F;
                    int petrifyTicks = (isBoss ? 45 : 120) + (dispLvl - 1) * 18;
                    target.hurt(serverLevel.damageSources().magic(), 6.0F * mult);
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, petrifyTicks, isBoss ? 3 : 10));
                    target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, petrifyTicks, 4));
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, petrifyTicks, 2));
                    StunExecutionManager.addStun(serverLevel, target, player, 32.0F * mult);
                    target.setDeltaMovement(Vec3.ZERO);
                    target.hurtMarked = true;
                    serverLevel.sendParticles(ParticleTypes.SMOKE, target.getX(), target.getY() + 1.0, target.getZ(), 14, 0.4, 0.7, 0.4, 0.02);
                }
            }
        }

        serverLevel.playSound(null, player.blockPosition(), SoundEvents.STONE_PLACE, SoundSource.PLAYERS, 1.4F, 0.65F);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 0.7F, 1.35F);
        if (!petrified.isEmpty()) {
            player.displayClientMessage(Component.literal("🐍 ¡" + petrified.size() + " enemigo(s) petrificado(s)!").withStyle(ChatFormatting.GREEN), true);
        }
        player.getCooldowns().addCooldown(this, Math.max(65, 120 - (dispLvl - 1) * 12));
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
            if (!MagicManager.isUnlocked(sp, GodOfWarMagic.MEDUSA_GAZE)) {
                MagicManager.unlockMagic(sp, GodOfWarMagic.MEDUSA_GAZE);
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
        tooltip.add(Component.literal("Cabeza de Medusa — Nivel " + lvl + "/5 (Curios: [X] | Mano: [G])")
            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Cono de Mirada Petrificante: inmoviliza, debilita y aturde").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Equipable en Curios (Poder Divino) y mejorable en [K] Potenciador").withStyle(ChatFormatting.GOLD));
    }
}
