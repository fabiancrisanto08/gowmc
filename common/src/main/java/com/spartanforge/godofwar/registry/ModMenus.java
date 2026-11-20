package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.menu.GodOfWarUpgradeMenu;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<GodOfWarUpgradeMenu>> UPGRADE_MENU =
        MENUS.register("upgrade_menu", () -> new MenuType<>(GodOfWarUpgradeMenu::new, FeatureFlags.VANILLA_SET));

    private ModMenus() {}

    public static void register() {
        MENUS.register();
    }
}
