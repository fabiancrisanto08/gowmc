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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Magia: Ira de Poseidón (God of War I)
 * Compatible con Clic Derecho y tecla [G] en Modo Batalla de Epic Fight.
 */
public class PoseidonsRagePowerItem extends Item implements GodOfWarWeaponAbility {
    public PoseidonsRagePowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        int dispLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        float mult = WeaponUpgradeHelper.getPowerMultiplier(stack);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 2));
        player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 160 + dispLvl * 20, 0));

        AABB area = player.getBoundingBox().inflate(7.5D + (dispLvl - 1) * 0.8D);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
            t -> t != player && t.isAlive());

        int bolts = 0;
        for (LivingEntity target : targets) {
            target.hurt(serverLevel.damageSources().lightningBolt(), 18.0F * mult);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60 + dispLvl * 10, 2));
            StunExecutionManager.addStun(serverLevel, target, player, 24.0F * mult);
            target.push(0, 0.7D, 0);
            target.hurtMarked = true;
            if (bolts < 5 + dispLvl) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (bolt != null) {
                    bolt.moveTo(target.getX(), target.getY(), target.getZ());
                    bolt.setVisualOnly(true);
                    serverLevel.addFreshEntity(bolt);
                }
                bolts++;
            }
        }

        for (int i = 0; i < 40; i++) {
            double ang = (2.0D * Math.PI * i) / 40.0D;
            double r = 2.0D + (i % 4) * 1.4D;
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                player.getX() + Math.cos(ang) * r, player.getY() + 0.8 + (i % 3) * 0.5, player.getZ() + Math.sin(ang) * r,
                2, 0.15, 0.3, 0.15, 0.05);
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                player.getX() + Math.cos(ang) * r, player.getY() + 0.5, player.getZ() + Math.sin(ang) * r,
                3, 0.2, 0.4, 0.2, 0.1);
        }

        serverLevel.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.3F, 1.15F);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.2F, 0.9F);
        player.getCooldowns().addCooldown(this, Math.max(70, 130 - (dispLvl - 1) * 12));
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
            if (!MagicManager.isUnlocked(sp, GodOfWarMagic.POSEIDONS_RAGE)) {
                MagicManager.unlockMagic(sp, GodOfWarMagic.POSEIDONS_RAGE);
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
        tooltip.add(Component.literal("Magia de Poseidón — Nivel " + lvl + "/5 (Curios: [X] | Mano: [G])")
            .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Vórtice de rayos marinos que eleva, electrocuta y aturde").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Equipable en Curios (Poder Divino) y mejorable en [K] Potenciador").withStyle(ChatFormatting.GOLD));
    }
}
