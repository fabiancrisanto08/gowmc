package com.spartanforge.godofwar.combat;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestiona las mejoras permanentes estilo God of War obtenidas de los Cofres de las Nornas:
 * - Manzanas de Iðunn: aumentan la Vida Máxima permanentemente (+2 HP por manzana, hasta +20 HP / +10 corazones).
 * - Cuernos de Hidromiel de Sangre: aumentan Maestría de Furia y Aturdimiento (Nivel 1 a 5).
 *
 * Los valores se guardan en tags persistentes del jugador (scoreboard tags) para sobrevivir
 * reinicios de mundo, muertes y cambios de dimensión tanto en Forge como en Fabric.
 */
public final class PlayerProgressionManager {
    private static final UUID IDUNN_HEALTH_MODIFIER_UUID = UUID.fromString("e3f14b2a-9a11-4c91-a2b7-4e0f90a21c77");
    private static final String IDUNN_TAG_PREFIX = "gow_idunn_lvl_";
    private static final String MEAD_TAG_PREFIX = "gow_mead_lvl_";

    public static final int MAX_IDUNN_LEVEL = 10;
    public static final int MAX_MEAD_LEVEL = 5;

    private static final Map<UUID, Integer> IDUNN_CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> MEAD_CACHE = new ConcurrentHashMap<>();

    private PlayerProgressionManager() {}

    public static void register() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            int idunn = readTagLevel(player, IDUNN_TAG_PREFIX, MAX_IDUNN_LEVEL);
            int mead = readTagLevel(player, MEAD_TAG_PREFIX, MAX_MEAD_LEVEL);
            IDUNN_CACHE.put(player.getUUID(), idunn);
            MEAD_CACHE.put(player.getUUID(), mead);
            applyHealthModifier(player, idunn);
        });

        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            int idunn = Math.max(getIdunnLevel(oldPlayer), readTagLevel(oldPlayer, IDUNN_TAG_PREFIX, MAX_IDUNN_LEVEL));
            int mead = Math.max(getMeadLevel(oldPlayer), readTagLevel(oldPlayer, MEAD_TAG_PREFIX, MAX_MEAD_LEVEL));
            writeTagLevel(newPlayer, IDUNN_TAG_PREFIX, idunn, MAX_IDUNN_LEVEL);
            writeTagLevel(newPlayer, MEAD_TAG_PREFIX, mead, MAX_MEAD_LEVEL);
            IDUNN_CACHE.put(newPlayer.getUUID(), idunn);
            MEAD_CACHE.put(newPlayer.getUUID(), mead);
            applyHealthModifier(newPlayer, idunn);
            if (!wonGame) {
                newPlayer.setHealth(newPlayer.getMaxHealth());
            }
        });

        PlayerEvent.PLAYER_RESPAWN.register((newPlayer, conqueredEnd) -> {
            int idunn = getIdunnLevel(newPlayer);
            applyHealthModifier(newPlayer, idunn);
            newPlayer.setHealth(newPlayer.getMaxHealth());
        });

        PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> {
            applyHealthModifier(player, getIdunnLevel(player));
        });
    }

    public static int getIdunnLevel(Player player) {
        int cached = IDUNN_CACHE.getOrDefault(player.getUUID(), -1);
        if (cached >= 0) {
            return cached;
        }
        int parsed = readTagLevel(player, IDUNN_TAG_PREFIX, MAX_IDUNN_LEVEL);
        IDUNN_CACHE.put(player.getUUID(), parsed);
        return parsed;
    }

    public static int getMeadLevel(Player player) {
        int cached = MEAD_CACHE.getOrDefault(player.getUUID(), -1);
        if (cached >= 0) {
            return cached;
        }
        int parsed = readTagLevel(player, MEAD_TAG_PREFIX, MAX_MEAD_LEVEL);
        MEAD_CACHE.put(player.getUUID(), parsed);
        return parsed;
    }

    public static boolean consumeIdunnApple(ServerPlayer player) {
        int current = getIdunnLevel(player);
        if (current >= MAX_IDUNN_LEVEL) {
            return false;
        }
        int next = current + 1;
        writeTagLevel(player, IDUNN_TAG_PREFIX, next, MAX_IDUNN_LEVEL);
        IDUNN_CACHE.put(player.getUUID(), next);
        applyHealthModifier(player, next);
        player.heal(6.0F);
        return true;
    }

    public static boolean consumeBloodMead(ServerPlayer player) {
        int current = getMeadLevel(player);
        if (current >= MAX_MEAD_LEVEL) {
            return false;
        }
        int next = current + 1;
        writeTagLevel(player, MEAD_TAG_PREFIX, next, MAX_MEAD_LEVEL);
        MEAD_CACHE.put(player.getUUID(), next);
        return true;
    }

    /** Multiplicador de acumulación de Aturdimiento según Cuernos de Hidromiel bebidos. */
    public static float getStunMultiplier(Player player) {
        return 1.0F + getMeadLevel(player) * 0.15F;
    }

    /** Duración adicional (en ticks) para la Ira Espartana según Cuernos de Hidromiel. */
    public static int getExtraRageTicks(Player player) {
        return getMeadLevel(player) * 40; // +2 segundos por nivel
    }

    /** Curación extra en Ejecuciones Brutales según Cuernos de Hidromiel. */
    public static float getExtraExecutionHeal(Player player) {
        return getMeadLevel(player) * 1.5F;
    }

    public static void applyHealthModifier(Player player, int idunnLevel) {
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr == null) return;
        attr.removeModifier(IDUNN_HEALTH_MODIFIER_UUID);
        if (idunnLevel > 0) {
            attr.addPermanentModifier(new AttributeModifier(
                IDUNN_HEALTH_MODIFIER_UUID,
                "GodOfWar Idunn Apples Bonus",
                idunnLevel * 2.0D,
                AttributeModifier.Operation.ADDITION
            ));
        }
    }

    private static int readTagLevel(Player player, String prefix, int max) {
        int best = 0;
        for (String tag : player.getTags()) {
            if (tag.startsWith(prefix)) {
                try {
                    int val = Integer.parseInt(tag.substring(prefix.length()));
                    if (val > best && val <= max) {
                        best = val;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return best;
    }

    private static void writeTagLevel(Player player, String prefix, int level, int max) {
        for (int i = 1; i <= max; i++) {
            player.removeTag(prefix + i);
        }
        if (level > 0) {
            player.addTag(prefix + Math.min(level, max));
        }
    }
}
