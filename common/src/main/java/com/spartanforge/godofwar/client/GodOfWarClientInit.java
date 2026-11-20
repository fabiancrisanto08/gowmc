package com.spartanforge.godofwar.client;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.client.screen.GodOfWarMagicScreen;
import com.spartanforge.godofwar.client.screen.GodOfWarUpgradeScreen;
import com.spartanforge.godofwar.item.WeaponUpgradeHelper;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import com.spartanforge.godofwar.registry.ModItems;
import com.spartanforge.godofwar.registry.ModMenus;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Inicialización común del lado Cliente (usada por Forge y Fabric):
 * - Registra la pantalla POTENCIADOR estilo God of War II.
 * - Registra la propiedad visual "godofwar:upgrade_level" para que las Espadas del Caos (Ares)
 *   y las Espadas de Atenea cambien de modelo 3D y textura del Nivel 1 al 5.
 * - Escucha las teclas [G], [V], [K] y [X].
 */
public final class GodOfWarClientInit {
    private static boolean initialized = false;

    private GodOfWarClientInit() {}

    public static void init() {
        if (initialized) return;
        initialized = true;

        GodOfWarNetwork.registerClientReceivers();

        MenuRegistry.registerScreenFactory(ModMenus.UPGRADE_MENU.get(), GodOfWarUpgradeScreen::new);

        ResourceLocation upgradeLevelProp = new ResourceLocation(GodOfWarMod.MOD_ID, "upgrade_level");
        ItemPropertiesRegistry.register(ModItems.BLADES_OF_CHAOS.get(), upgradeLevelProp,
            (stack, level, entity, seed) -> WeaponUpgradeHelper.getModelStage(stack));
        ItemPropertiesRegistry.register(ModItems.BLADES_OF_ATHENA.get(), upgradeLevelProp,
            (stack, level, entity, seed) -> WeaponUpgradeHelper.getModelStage(stack));

        ClientTickEvent.CLIENT_POST.register(mc -> {
            if (mc.player == null || mc.level == null) {
                ClientStunState.clearAll();
                ClientMagicState.clearAll();
                return;
            }
            ClientStunState.clientTick();
            ClientMagicState.clientTick();

            while (GodOfWarKeybinds.KEY_WEAPON_ABILITY.consumeClick()) {
                GodOfWarNetwork.sendWeaponAbility(mc.player.isShiftKeyDown());
            }
            while (GodOfWarKeybinds.KEY_EXECUTE.consumeClick()) {
                GodOfWarNetwork.sendAttemptExecution();
            }
            while (GodOfWarKeybinds.KEY_UPGRADE_MENU.consumeClick()) {
                if (mc.screen == null) {
                    GodOfWarNetwork.sendOpenUpgradeMenu();
                }
            }
            while (GodOfWarKeybinds.KEY_CURIO_POWER.consumeClick()) {
                GodOfWarNetwork.sendCurioPower();
            }
            while (GodOfWarKeybinds.KEY_MAGIC_MENU.consumeClick()) {
                if (mc.screen == null) {
                    mc.setScreen(new GodOfWarMagicScreen());
                }
            }
            while (GodOfWarKeybinds.KEY_CYCLE_MAGIC.consumeClick()) {
                GodOfWarNetwork.sendCycleMagicSlot();
            }
        });

        ClientGuiEvent.RENDER_HUD.register(GodOfWarClientHud::render);
    }
}
