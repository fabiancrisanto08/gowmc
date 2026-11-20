package com.spartanforge.godofwar.forge;

import com.spartanforge.godofwar.compat.CuriosBridge;
import com.spartanforge.godofwar.item.GodOfWarWeaponAbility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

/**
 * Integración nativa con Curios API (Forge 1.20.1).
 * Solo se carga si ModList.get().isLoaded("curios") es verdadero.
 */
public final class ForgeCuriosCompat {
    private ForgeCuriosCompat() {}

    public static void init() {
        CuriosBridge.registerProvider(
            ForgeCuriosCompat::findCurioItem,
            ForgeCuriosCompat::findEquippedPower
        );
    }

    private static ItemStack findCurioItem(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player)
            .map(inv -> inv.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY))
            .orElse(ItemStack.EMPTY);
    }

    private static ItemStack findEquippedPower(Player player) {
        return CuriosApi.getCuriosInventory(player)
            .map(inv -> {
                Optional<SlotResult> found = inv.findFirstCurio(
                    st -> st.getItem() instanceof GodOfWarWeaponAbility && CuriosBridge.isDivinePower(st.getItem())
                );
                return found.map(SlotResult::stack).orElse(ItemStack.EMPTY);
            })
            .orElse(ItemStack.EMPTY);
    }
}
