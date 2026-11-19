package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> GOW_TAB = TABS.register("god_of_war_tab", () ->
        CreativeTabRegistry.create(
            Component.translatable("itemGroup.godofwar.god_of_war_tab"),
            () -> new ItemStack(ModItems.BLADES_OF_CHAOS.get())
        )
    );

    public static void register() {
        TABS.register();
    }
}
