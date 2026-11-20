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
 * Cuerno de Hidromiel de Sangre (Cofre de las Nornas):
 * Aumenta permanentemente la Maestría Espartana (Nivel I a V):
 * +15% acumulación de Aturdimiento, +2s Ira Espartana y +1.5 HP curación por Ejecución.
 */
public class HornOfBloodMeadItem extends Item {
    public HornOfBloodMeadItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
            if (PlayerProgressionManager.consumeBloodMead(serverPlayer)) {
                int lvl = PlayerProgressionManager.getMeadLevel(serverPlayer);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 1.1, player.getZ(), 32, 0.6, 0.7, 0.6, 0.08);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 0.9F, 1.25F);
                serverPlayer.displayClientMessage(
                    Component.literal("🔥 ¡Hidromiel de Sangre bebida! Maestría de Furia/Stun Nivel " + lvl
                        + "/" + PlayerProgressionManager.MAX_MEAD_LEVEL + " (+Aturdimiento y +Ira Espartana)")
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), false);
            } else {
                serverPlayer.displayClientMessage(
                    Component.literal("Ya alcanzaste la Maestría máxima de Hidromiel de Sangre (5/5).")
                        .withStyle(ChatFormatting.GOLD), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Reliquia de Furia de las Nornas").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Clic Derecho: +15% Aturdimiento, +2s Ira Espartana y más curación en Ejecuciones").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Acumulable hasta Nivel V").withStyle(ChatFormatting.GRAY));
    }
}
