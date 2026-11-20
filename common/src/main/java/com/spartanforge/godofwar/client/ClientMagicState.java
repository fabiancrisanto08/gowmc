package com.spartanforge.godofwar.client;

import com.spartanforge.godofwar.magic.GodOfWarMagic;
import com.spartanforge.godofwar.magic.GodOfWarMagicData;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Estado sincronizado en cliente del Sistema de Magias (24 Magias + 3 Slots Activos + Maná Azul).
 */
public final class ClientMagicState {
    private static int unlockedMask = 0;
    private static final int[] slots = new int[] { -1, -1, -1 };
    private static int activeSlot = 0;
    private static final byte[] levels = new byte[GodOfWarMagic.values().length];
    private static float mana = GodOfWarMagicData.MAX_MANA;
    private static final int[] slotCooldownTicks = new int[] { 0, 0, 0 };

    static {
        Arrays.fill(levels, (byte) 1);
    }

    private ClientMagicState() {}

    public static void update(int mask, int[] newSlots, int active, byte[] newLevels, float newMana, int[] remCds) {
        unlockedMask = mask;
        for (int i = 0; i < 3; i++) {
            slots[i] = i < newSlots.length ? newSlots[i] : -1;
            slotCooldownTicks[i] = i < remCds.length ? Math.max(0, remCds[i]) : 0;
        }
        activeSlot = Math.max(0, Math.min(2, active));
        for (int i = 0; i < Math.min(levels.length, newLevels.length); i++) {
            levels[i] = (byte) Math.max(1, Math.min(GodOfWarMagic.MAX_LEVEL, newLevels[i]));
        }
        mana = Math.max(0.0F, Math.min(GodOfWarMagicData.MAX_MANA, newMana));
    }

    public static void clientTick() {
        for (int i = 0; i < 3; i++) {
            if (slotCooldownTicks[i] > 0) {
                slotCooldownTicks[i]--;
            }
        }
    }

    public static void clearAll() {
        unlockedMask = 0;
        Arrays.fill(slots, -1);
        activeSlot = 0;
        Arrays.fill(levels, (byte) 1);
        mana = GodOfWarMagicData.MAX_MANA;
        Arrays.fill(slotCooldownTicks, 0);
    }

    public static boolean hasAnyUnlocked() {
        return unlockedMask != 0;
    }

    public static int getUnlockedCount() {
        return Integer.bitCount(unlockedMask);
    }

    public static boolean isUnlocked(GodOfWarMagic magic) {
        return (unlockedMask & (1 << magic.ordinal())) != 0;
    }

    @Nullable
    public static GodOfWarMagic getEquipped(int slotIdx) {
        if (slotIdx < 0 || slotIdx >= 3) return null;
        return GodOfWarMagic.byOrdinal(slots[slotIdx]);
    }

    public static int findEquippedSlot(GodOfWarMagic magic) {
        int ord = magic.ordinal();
        for (int i = 0; i < 3; i++) {
            if (slots[i] == ord) return i;
        }
        return -1;
    }

    public static int getActiveSlot() {
        return activeSlot;
    }

    public static int getLevel(GodOfWarMagic magic) {
        return Math.max(1, Math.min(GodOfWarMagic.MAX_LEVEL, levels[magic.ordinal()]));
    }

    public static float getMana() {
        return mana;
    }

    public static int getSlotCooldownTicks(int slotIdx) {
        return (slotIdx >= 0 && slotIdx < 3) ? slotCooldownTicks[slotIdx] : 0;
    }
}
