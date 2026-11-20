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
 * Reliquia Curios: Amuleto de Uróboros (God of War Ascension / Cambio de Reino)
 * Se equipa en el slot "Reliquia Olímpica/Nórdica" de Curios API.
 * - Regeneración pasiva constante (escala del Nivel 1 al 5).
 * - Cambio de Reino automático: cuando bajas del 60% de vida al ser golpeado,
 *   ralentiza drásticamente el tiempo de todos los enemigos cercanos.
 */
public class AmuletOfOuroborosItem extends Item {
    public AmuletOfOuroborosItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int lvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        tooltip.add(Component.literal("✦ Reliquia Curios — Nivel " + lvl + "/5 (Mejorable con [K])")
            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• Equipada en Curios: Regeneración pasiva y reducción de enfriamientos").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("• Cambio de Reino: ralentiza el tiempo alrededor al recibir golpes críticos").withStyle(ChatFormatting.AQUA));
    }
}
