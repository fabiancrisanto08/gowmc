package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.entity.projectile.ThrownLeviathanAxeEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Hacha Leviatán (Saga Nórdica)
 * Compatible con Epic Fight (Hacha Nórdica + Escudo del Guardián + Estela de Escarcha).
 * - Golpe cuerpo a cuerpo: aplica escarcha y lentitud.
 * - Clic Derecho o [G]: lanza el Hacha Leviatán congelando enemigos y destruyendo Sellos Rúnicos.
 * - Segundo Clic Derecho o [G]: LLAMA EL HACHA DE VUELTA A TU MANO (cortando todo a su paso).
 */
public class LeviathanAxeItem extends SwordItem implements GodOfWarWeaponAbility {
    private static final String TAG_THROWN = "GowAxeThrown";
    private static final String TAG_AXE_UUID = "GowThrownAxeUUID";

    public LeviathanAxeItem(Properties properties) {
        super(Tiers.NETHERITE, 11, -2.3F, properties);
    }

    public static void clearThrownState(Player player, UUID axeUuid) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack st = player.getInventory().getItem(i);
            if (st.getItem() instanceof LeviathanAxeItem && st.hasTag()) {
                CompoundTag tag = st.getTag();
                if (tag != null && tag.getBoolean(TAG_THROWN)) {
                    if (!tag.hasUUID(TAG_AXE_UUID) || tag.getUUID(TAG_AXE_UUID).equals(axeUuid)) {
                        tag.putBoolean(TAG_THROWN, false);
                        tag.remove(TAG_AXE_UUID);
                    }
                }
            }
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
        target.setTicksFrozen(Math.min(200, target.getTicksFrozen() + 60));
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getBoolean(TAG_THROWN)) {
            // RECUPERAR EL HACHA LEVIATÁN
            Entity found = tag.hasUUID(TAG_AXE_UUID) ? serverLevel.getEntity(tag.getUUID(TAG_AXE_UUID)) : null;
            if (found instanceof ThrownLeviathanAxeEntity axe && axe.isAlive()) {
                axe.startRecall();
                player.displayClientMessage(Component.literal("❄ Llamando al Hacha Leviatán...").withStyle(ChatFormatting.AQUA), true);
                player.getCooldowns().addCooldown(this, 10);
            } else {
                tag.putBoolean(TAG_THROWN, false);
                tag.remove(TAG_AXE_UUID);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1.0F, 1.2F);
            }
        } else {
            // LANZAR EL HACHA LEVIATÁN
            float bonus = WeaponUpgradeHelper.getBonusDamage(stack);
            ThrownLeviathanAxeEntity axe = new ThrownLeviathanAxeEntity(serverLevel, player, bonus);
            axe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.35F, 0.5F);
            serverLevel.addFreshEntity(axe);

            tag.putBoolean(TAG_THROWN, true);
            tag.putUUID(TAG_AXE_UUID, axe.getUUID());
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY() + 1.4, player.getZ(), 12, 0.3, 0.3, 0.3, 0.05);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.2F, 0.9F);
            player.getCooldowns().addCooldown(this, 12);
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
        if (stack.hasTag() && stack.getTag() != null && stack.getTag().getBoolean(TAG_THROWN)) {
            tooltip.add(Component.literal("❄ [Lanzada — Clic Derecho o [G] para llamar de vuelta]").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        }
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Nórdica",
            "Clic Derecho / [G]: Lanza el hacha, congela enemigos y rompe Sellos Rúnicos",
            "2º Clic Derecho / [G]: Regresa volando a tu mano cortando enemigos");
    }
}
