package com.spartanforge.godofwar.compat;

import dev.architectury.platform.Platform;

/**
 * Puente de compatibilidad segura (soft-dependency) con Epic Fight 1.20.1.
 * Permite detectar en tiempo de ejecución si Epic Fight está cargado sin provocar
 * errores si el jugador decide jugar solo con God of War.
 */
public final class EpicFightCompat {
    private static Boolean cachedLoaded = null;

    private EpicFightCompat() {}

    public static boolean isLoaded() {
        if (cachedLoaded == null) {
            cachedLoaded = Platform.isModLoaded("epicfight");
        }
        return cachedLoaded;
    }
}
