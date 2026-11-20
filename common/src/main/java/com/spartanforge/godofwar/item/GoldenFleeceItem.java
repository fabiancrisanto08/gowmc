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
 * Reliquia Curios: Vellocino de Oro (God of War II & III)
 * Se equipa en el slot "Reliquia Olímpica/Nórdica" de Curios API.
 * - Reduce daño recibido (12% al Niv. 1 -> 32% al Niv. 5).
 * - Amplía la ventana de Parry Dorado del Escudo del Guardián.
 * - Al recibir golpes fuertes libera un contraataque dorado que aturde al atacante.
 */
public class GoldenFleeceItem extends Item {
    public GoldenFleeceItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int lvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        int redPct = 12 + (lvl - 1) * 5;
        tooltip.add(Component.literal("✦ Reliquia Curios — Nivel " + lvl + "/5 (Mejorable con [K])")
            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Equipada en Curios: -" + redPct + "% daño recibido y contraataque dorado").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Amplía la ventana de Parry Dorado del Escudo del Guardián").withStyle(ChatFormatting.AQUA));
    }
}
