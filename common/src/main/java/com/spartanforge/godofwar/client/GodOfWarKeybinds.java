package com.spartanforge.godofwar.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * Controles configurables de God of War (en Opciones -> Controles):
 * - [G] / [Shift+G]: Habilidad Especial del Arma (100% compatible con Modo Batalla de Epic Fight).
 * - [V]: Ejecución Brutal (R3) sobre enemigos aturdidos.
 * - [K]: Abrir Menú POTENCIADOR estilo God of War II (mejoras con XP).
 * - [X]: Activar Poder Divino equipado en Curios API.
 */
public final class GodOfWarKeybinds {
    public static final String CATEGORY = "key.categories.godofwar";

    public static final KeyMapping KEY_WEAPON_ABILITY = new KeyMapping(
        "key.godofwar.weapon_ability",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        CATEGORY
    );

    public static final KeyMapping KEY_EXECUTE = new KeyMapping(
        "key.godofwar.execute",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_V,
        CATEGORY
    );

    public static final KeyMapping KEY_UPGRADE_MENU = new KeyMapping(
        "key.godofwar.upgrade_menu",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_K,
        CATEGORY
    );

    public static final KeyMapping KEY_CURIO_POWER = new KeyMapping(
        "key.godofwar.curio_power",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_X,
        CATEGORY
    );

    public static final KeyMapping KEY_MAGIC_MENU = new KeyMapping(
        "key.godofwar.magic_menu",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_M,
        CATEGORY
    );

    public static final KeyMapping KEY_CYCLE_MAGIC = new KeyMapping(
        "key.godofwar.cycle_magic",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_C,
        CATEGORY
    );

    private GodOfWarKeybinds() {}
}
