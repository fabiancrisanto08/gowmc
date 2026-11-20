package com.spartanforge.godofwar.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Reliquia Curios: Alas de Ícaro (God of War II & III)
 * Se equipa en el slot "Reliquia Olímpica/Nórdica" de Curios API.
 * - Anula el daño por caída.
 * - Permite planear en el aire (Caída Lenta controlada + Salto potenciado según Nivel 1-5).
 */
public class WingsOfIcarusItem extends Item {
    public WingsOfIcarusItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int lvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        tooltip.add(Component.literal("✦ Reliquia Curios — Nivel " + lvl + "/5 (Mejorable con [K])")
            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Equipada en Curios: Inmunidad total al daño por caída").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Mantén Shift o salta para planear con plumas doradas (+Salto Niv. " + (lvl >= 3 ? "II" : "I") + ")").withStyle(ChatFormatting.AQUA));
    }
}
