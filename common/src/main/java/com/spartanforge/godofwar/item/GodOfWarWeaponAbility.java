package com.spartanforge.godofwar.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Interfaz unificada para todas las armas y reliquias de God of War.
 * Permite ejecutar sus habilidades especiales tanto con Clic Derecho clásico
 * como con la tecla dedicada [G] / [Shift + G] dentro del Modo Batalla de Epic Fight.
 */
public interface GodOfWarWeaponAbility {
    /**
     * Ejecuta la habilidad especial del arma/reliquia en el servidor.
     *
     * @param level     Mundo servidor actual
     * @param player    Jugador que activa la habilidad
     * @param stack     ItemStack del arma/reliquia
     * @param secondary true si el jugador mantiene agachado (Shift) para la habilidad secundaria
     * @return true si la habilidad se activó con éxito
     */
    boolean triggerSpecialAbility(ServerLevel level, ServerPlayer player, ItemStack stack, boolean secondary);
}
