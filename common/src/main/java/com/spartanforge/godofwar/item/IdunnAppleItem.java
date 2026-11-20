package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.PlayerProgressionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Manzana de Iðunn (Cofre de las Nornas):
 * Al consumirla aumenta permanentemente la Vida Máxima en +2 HP (+1 corazón), hasta +20 HP.
 */
public class IdunnAppleItem extends Item {
    public IdunnAppleItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
            if (PlayerProgressionManager.consumeIdunnApple(serverPlayer)) {
                int lvl = PlayerProgressionManager.getIdunnLevel(serverPlayer);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.2, player.getZ(), 28, 0.6, 0.7, 0.6, 0.08);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.1F, 1.3F);
                serverPlayer.displayClientMessage(
                    Component.literal("🍏 ¡Manzana de Iðunn consumida! Vida Máxima +" + (lvl * 2)
                        + " HP (" + lvl + "/" + PlayerProgressionManager.MAX_IDUNN_LEVEL + ")")
                        .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), false);
            } else {
                player.heal(10.0F);
                serverPlayer.displayClientMessage(
                    Component.literal("Ya alcanzaste el máximo poder de las Manzanas de Iðunn ("
                        + PlayerProgressionManager.MAX_IDUNN_LEVEL + "/" + PlayerProgressionManager.MAX_IDUNN_LEVEL + ").")
                        .withStyle(ChatFormatting.GOLD), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Fruto Sagrado de las Nornas").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Clic Derecho: Aumenta permanentemente +1 Corazón (+2 HP)").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Acumulable hasta 10 Manzanas (+20 HP permanentes)").withStyle(ChatFormatting.GRAY));
    }
}
