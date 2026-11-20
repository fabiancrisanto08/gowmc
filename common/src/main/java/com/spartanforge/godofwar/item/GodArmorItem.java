package com.spartanforge.godofwar.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Piezas de armadura míticas de God of War con descripción de bonos de set completo.
 */
public class GodArmorItem extends ArmorItem {
    private final String setBonusDescription;

    public GodArmorItem(ModArmorMaterial material, Type type, Properties properties, String setBonusDescription) {
        super(material, type, properties);
        this.setBonusDescription = setBonusDescription;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Bono de Set Completo:").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        tooltip.add(Component.literal("• " + this.setBonusDescription).withStyle(ChatFormatting.YELLOW));
    }
}
