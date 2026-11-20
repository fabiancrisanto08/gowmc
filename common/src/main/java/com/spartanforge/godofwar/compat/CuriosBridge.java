package com.spartanforge.godofwar.compat;

import com.spartanforge.godofwar.item.GodOfWarWeaponAbility;
import dev.architectury.platform.Platform;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Puente seguro multi-loader hacia Curios API (Forge 1.20.1).
 * Cuando Curios API está instalado en Forge, ForgeCuriosCompat registra implementaciones reales
 * sobre CuriosApi.getCuriosInventory(player). Si no está instalado, busca de forma segura en el inventario.
 */
public final class CuriosBridge {
    private static BiFunction<Player, Item, ItemStack> findCurioFn = (p, item) -> ItemStack.EMPTY;
    private static Function<Player, ItemStack> findEquippedPowerFn = p -> ItemStack.EMPTY;
    private static boolean providerRegistered = false;

    private CuriosBridge() {}

    public static void registerProvider(
        BiFunction<Player, Item, ItemStack> findCurio,
        Function<Player, ItemStack> findPower
    ) {
        findCurioFn = findCurio;
        findEquippedPowerFn = findPower;
        providerRegistered = true;
    }

    public static boolean isCuriosLoaded() {
        return providerRegistered || Platform.isModLoaded("curios");
    }

    /**
     * Busca una reliquia equipada en los slots de Curios (o como fallback en la mano secundaria si Curios no está activo).
     */
    public static ItemStack getEquippedRelic(Player player, Item item) {
        if (providerRegistered) {
            ItemStack curio = findCurioFn.apply(player, item);
            if (!curio.isEmpty()) {
                return curio;
            }
        }
        // Fallback amigable si el usuario juega sin Curios instalado
        if (!Platform.isModLoaded("curios")) {
            if (player.getOffhandItem().is(item)) return player.getOffhandItem();
            for (int i = 0; i < 9; i++) {
                ItemStack st = player.getInventory().getItem(i);
                if (st.is(item)) return st;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasEquippedRelic(Player player, Item item) {
        return !getEquippedRelic(player, item).isEmpty();
    }

    /**
     * Devuelve el Poder Divino equipado en el slot Curios "gow_power" (para activar con [X]).
     * Si Curios no está instalado, busca el primer poder de God of War en la barra rápida.
     */
    public static ItemStack getEquippedPower(Player player) {
        if (providerRegistered) {
            ItemStack curio = findEquippedPowerFn.apply(player);
            if (!curio.isEmpty()) {
                return curio;
            }
        }
        for (int i = 0; i < 9; i++) {
            ItemStack st = player.getInventory().getItem(i);
            if (st.getItem() instanceof GodOfWarWeaponAbility && isDivinePower(st.getItem())) {
                return st;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isDivinePower(Item item) {
        String name = item.getClass().getSimpleName();
        return name.endsWith("PowerItem");
    }
}
