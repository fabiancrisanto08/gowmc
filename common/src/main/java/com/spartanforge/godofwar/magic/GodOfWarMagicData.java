package com.spartanforge.godofwar.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Persistencia segura en mundo (Overworld SavedData) del Grimorio de Magias de cada jugador:
 * - Sobrevive muertes, cambios de dimensión y reinicios tanto en Forge como en Fabric.
 * - Almacena: Magias desbloqueadas (bitmask 24 bits), 3 slots activos, índice activo (0..2),
 *   nivel de mejora (1..5) de cada magia y barra de Maná Azul (0..100).
 */
public class GodOfWarMagicData extends SavedData {
    private static final String DATA_NAME = "godofwar_magic_grimoire";
    public static final float MAX_MANA = 100.0F;

    public static final class PlayerProfile {
        public int unlockedMask = 0;
        public final int[] slots = new int[] { -1, -1, -1 };
        public int activeSlot = 0;
        public final byte[] levels = new byte[GodOfWarMagic.values().length];
        public float mana = MAX_MANA;

        public PlayerProfile() {
            Arrays.fill(levels, (byte) 1);
        }

        public boolean isUnlocked(GodOfWarMagic magic) {
            return (unlockedMask & (1 << magic.ordinal())) != 0;
        }

        public boolean unlock(GodOfWarMagic magic) {
            if (isUnlocked(magic)) return false;
            unlockedMask |= (1 << magic.ordinal());
            if (levels[magic.ordinal()] < 1) {
                levels[magic.ordinal()] = 1;
            }
            // Auto-equipa en el primer slot vacío entre los 3 slots activos!
            for (int i = 0; i < 3; i++) {
                if (slots[i] < 0) {
                    slots[i] = magic.ordinal();
                    break;
                }
            }
            return true;
        }

        public int getLevel(GodOfWarMagic magic) {
            int lv = levels[magic.ordinal()];
            return Math.max(1, Math.min(GodOfWarMagic.MAX_LEVEL, lv));
        }

        public void setLevel(GodOfWarMagic magic, int lvl) {
            levels[magic.ordinal()] = (byte) Math.max(1, Math.min(GodOfWarMagic.MAX_LEVEL, lvl));
        }

        public int getUnlockedCount() {
            return Integer.bitCount(unlockedMask);
        }

        public GodOfWarMagic getEquipped(int slotIdx) {
            if (slotIdx < 0 || slotIdx >= 3) return null;
            return GodOfWarMagic.byOrdinal(slots[slotIdx]);
        }

        public boolean equipSlot(int slotIdx, int magicOrdinal) {
            if (slotIdx < 0 || slotIdx >= 3) return false;
            if (magicOrdinal < 0) {
                slots[slotIdx] = -1;
                return true;
            }
            GodOfWarMagic m = GodOfWarMagic.byOrdinal(magicOrdinal);
            if (m == null || !isUnlocked(m)) return false;
            // Si ya estaba equipada en otro slot, intercambia limpiamente para evitar duplicados!
            for (int i = 0; i < 3; i++) {
                if (i != slotIdx && slots[i] == magicOrdinal) {
                    slots[i] = slots[slotIdx];
                    break;
                }
            }
            slots[slotIdx] = magicOrdinal;
            activeSlot = slotIdx;
            return true;
        }

        public CompoundTag toTag(UUID uuid) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid", uuid);
            tag.putInt("unlockedMask", unlockedMask);
            tag.putIntArray("slots", slots);
            tag.putInt("activeSlot", activeSlot);
            tag.putByteArray("levels", levels);
            tag.putFloat("mana", mana);
            return tag;
        }

        public static PlayerProfile fromTag(CompoundTag tag) {
            PlayerProfile p = new PlayerProfile();
            p.unlockedMask = tag.getInt("unlockedMask");
            int[] s = tag.getIntArray("slots");
            for (int i = 0; i < Math.min(3, s.length); i++) {
                p.slots[i] = s[i];
            }
            p.activeSlot = Math.max(0, Math.min(2, tag.getInt("activeSlot")));
            byte[] lv = tag.getByteArray("levels");
            for (int i = 0; i < Math.min(p.levels.length, lv.length); i++) {
                p.levels[i] = (byte) Math.max(1, Math.min(GodOfWarMagic.MAX_LEVEL, lv[i]));
            }
            p.mana = tag.contains("mana") ? Math.max(0.0F, Math.min(MAX_MANA, tag.getFloat("mana"))) : MAX_MANA;
            return p;
        }
    }

    private final Map<UUID, PlayerProfile> profiles = new HashMap<>();

    public PlayerProfile getOrCreate(UUID uuid) {
        return profiles.computeIfAbsent(uuid, k -> {
            setDirty();
            return new PlayerProfile();
        });
    }

    public Map<UUID, PlayerProfile> getAllProfiles() {
        return profiles;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, PlayerProfile> entry : profiles.entrySet()) {
            list.add(entry.getValue().toTag(entry.getKey()));
        }
        tag.put("players", list);
        return tag;
    }

    public static GodOfWarMagicData load(CompoundTag tag) {
        GodOfWarMagicData data = new GodOfWarMagicData();
        ListTag list = tag.getList("players", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag pt = list.getCompound(i);
            if (pt.hasUUID("uuid")) {
                data.profiles.put(pt.getUUID("uuid"), PlayerProfile.fromTag(pt));
            }
        }
        return data;
    }

    public static GodOfWarMagicData get(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) {
            return new GodOfWarMagicData();
        }
        return overworld.getDataStorage().computeIfAbsent(
            GodOfWarMagicData::load,
            GodOfWarMagicData::new,
            DATA_NAME
        );
    }
}
