package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.BloodOrbManager;
import com.spartanforge.godofwar.magic.GodOfWarMagic;
import com.spartanforge.godofwar.magic.MagicManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
 * Reliquia consumible de Magia de God of War:
 * - Clic derecho: consume el objeto y DESBLOQUEA PERMANENTEMENTE esta magia en el Grimorio del jugador ([M]),
 *   equipándola automáticamente si hay alguno de los 3 slots libre.
 * - Si ya la tienes desbloqueada: Shift + Clic derecho la convierte en +250 Orbes de Sangre.
 */
public class MagicRelicItem extends Item {
    private final GodOfWarMagic magic;

    public MagicRelicItem(GodOfWarMagic magic) {
        super(new Properties().stacksTo(16).rarity(Rarity.EPIC).fireResistant());
        this.magic = magic;
    }

    public GodOfWarMagic getMagic() {
        return magic;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            if (!MagicManager.isUnlocked(sp, magic)) {
                boolean unlocked = MagicManager.unlockMagic(sp, magic);
                if (unlocked && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } else if (player.isShiftKeyDown()) {
                BloodOrbManager.addBlood(sp, 250);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.2F);
                sp.displayClientMessage(
                    Component.literal("✦ Reliquia duplicada absorbida: +250 Orbes de Sangre ✦")
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
            } else {
                sp.displayClientMessage(
                    Component.literal("Ya dominas " + magic.displayName + ". Abre [M] para equiparla en tus 3 slots (o Shift+Clic para +250 Sangre).")
                        .withStyle(ChatFormatting.GOLD), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("✦ Magia Canónica — " + magic.gameOrigin)
            .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        tooltip.add(Component.literal("Elemento: " + magic.elementLabel + " | Maná: " + magic.manaCost)
            .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("• " + magic.descLine1).withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• " + magic.descLine2).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Clic Derecho: Consumir y añadir permanentemente a tu Menú [M]")
            .withStyle(ChatFormatting.GREEN));
    }
}
