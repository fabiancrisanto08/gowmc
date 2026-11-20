package com.spartanforge.godofwar.magic;

import com.spartanforge.godofwar.combat.BloodOrbManager;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestor central del Sistema de Magias de la saga God of War:
 * - Desbloqueo permanente al consumir una Reliquia Mágica (con auto-equipado en los 3 slots).
 * - Equipar/intercambiar magias en cualquiera de los 3 slots activos.
 * - Mejorar cualquier magia desbloqueada del Nivel 1 al 5 usando Orbes de Sangre.
 * - Regeneración fluida de Maná Azul (+ recarga al derrotar enemigos / ejecuciones) y ejecución de las 24 magias.
 */
public final class MagicManager {
    private static final List<MagicExecutor.ActiveEffect> ACTIVE_EFFECTS = new CopyOnWriteArrayList<>();
    // Enfriamiento por jugador y por magia (timestamp en server ticks)
    private static final Map<UUID, long[]> COOLDOWNS = new ConcurrentHashMap<>();
    private static int tickCounter = 0;

    private MagicManager() {}

    public static void register() {
        PlayerEvent.PLAYER_JOIN.register(MagicManager::syncToClient);
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd) -> syncToClient(player));
        PlayerEvent.CHANGE_DIMENSION.register((player, oldL, newL) -> syncToClient(player));

        // Recarga de Maná Azul al derrotar enemigos en combate (estilo Orbes Azules de GoW!)
        EntityEvent.LIVING_DEATH.register((LivingEntity victim, DamageSource source) -> {
            if (!victim.level().isClientSide && source.getEntity() instanceof ServerPlayer killer) {
                addMana(killer, victim.getMaxHealth() >= 60.0F ? 22.0F : 9.0F);
            }
            return EventResult.pass();
        });

        TickEvent.SERVER_POST.register(MagicManager::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;

        // 1. Tick de efectos sostenidos (Cronos, Ejército de Esparta, Plaga de Erinias, Caronte)
        Iterator<MagicExecutor.ActiveEffect> it = ACTIVE_EFFECTS.iterator();
        while (it.hasNext()) {
            MagicExecutor.ActiveEffect ef = it.next();
            ServerPlayer caster = server.getPlayerList().getPlayer(ef.casterId);
            if (!MagicExecutor.tickActiveEffect(ef, caster)) {
                ACTIVE_EFFECTS.remove(ef);
            }
        }

        // 2. Regeneración suave de Maná Azul para jugadores con al menos 1 magia desbloqueada (cada 10 ticks = 0.5s)
        if (tickCounter % 10 == 0) {
            GodOfWarMagicData data = GodOfWarMagicData.get(server);
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());
                if (prof.unlockedMask != 0 && prof.mana < GodOfWarMagicData.MAX_MANA) {
                    prof.mana = Math.min(GodOfWarMagicData.MAX_MANA, prof.mana + 1.85F);
                    data.setDirty();
                    syncToClient(player);
                }
            }
        }
    }

    public static void addMana(ServerPlayer player, float amount) {
        MinecraftServer server = player.getServer();
        if (server == null || amount <= 0.0F) return;
        GodOfWarMagicData data = GodOfWarMagicData.get(server);
        GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());
        if (prof.unlockedMask == 0) return;
        float next = Math.min(GodOfWarMagicData.MAX_MANA, prof.mana + amount);
        if (next != prof.mana) {
            prof.mana = next;
            data.setDirty();
            syncToClient(player);
        }
    }

    /**
     * Desbloquea permanentemente una magia al consumir su reliquia/ítem.
     * Devuelve true si era nueva; false si ya estaba desbloqueada.
     */
    public static boolean unlockMagic(ServerPlayer player, GodOfWarMagic magic) {
        MinecraftServer server = player.getServer();
        if (server == null) return false;
        GodOfWarMagicData data = GodOfWarMagicData.get(server);
        GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());

        if (!prof.unlock(magic)) {
            return false;
        }
        prof.mana = GodOfWarMagicData.MAX_MANA;
        data.setDirty();
        syncToClient(player);

        // Fanfarria y espiral divina de desbloqueo!
        if (player.level() instanceof ServerLevel sl) {
            for (int i = 0; i < 36; i++) {
                double ang = (2.0D * Math.PI * i) / 36.0D;
                sl.sendParticles(ParticleTypes.END_ROD,
                    player.getX() + Math.cos(ang) * 1.4D, player.getY() + 0.3D + (i / 36.0D) * 2.0D,
                    player.getZ() + Math.sin(ang) * 1.4D, 1, 0.05, 0.05, 0.05, 0.03);
            }
            sl.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 1.1F, 1.15F);
            sl.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.9F, 1.25F);
        }

        player.displayClientMessage(
            Component.literal("✦ MAGIA DESBLOQUEADA: " + magic.displayName.toUpperCase()
                    + " (" + magic.gameOrigin + ") — Menú [M] | Lanzar [X] ✦")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
            true
        );
        return true;
    }

    public static boolean isUnlocked(ServerPlayer player, GodOfWarMagic magic) {
        MinecraftServer server = player.getServer();
        if (server == null) return false;
        return GodOfWarMagicData.get(server).getOrCreate(player.getUUID()).isUnlocked(magic);
    }

    public static void equipSlot(ServerPlayer player, int slotIdx, int magicOrdinal) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        GodOfWarMagicData data = GodOfWarMagicData.get(server);
        GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());
        if (prof.equipSlot(slotIdx, magicOrdinal)) {
            data.setDirty();
            syncToClient(player);
            GodOfWarMagic eq = GodOfWarMagic.byOrdinal(magicOrdinal);
            if (eq != null) {
                player.displayClientMessage(
                    Component.literal("Slot " + (slotIdx + 1) + " equipado: " + eq.displayName)
                        .withStyle(ChatFormatting.GOLD), true);
            }
        }
    }

    public static void cycleActiveSlot(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        GodOfWarMagicData data = GodOfWarMagicData.get(server);
        GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());
        if (prof.unlockedMask == 0) {
            player.displayClientMessage(
                Component.literal("Consume una Magia de God of War para desbloquearla y equiparla en [M].")
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        // Avanza al siguiente slot (prioriza slots ocupados, pero permite rotar libremente 0->1->2)
        int next = (prof.activeSlot + 1) % 3;
        for (int tries = 0; tries < 3; tries++) {
            int cand = (prof.activeSlot + 1 + tries) % 3;
            if (prof.slots[cand] >= 0) {
                next = cand;
                break;
            }
        }
        prof.activeSlot = next;
        data.setDirty();
        syncToClient(player);

        GodOfWarMagic cur = prof.getEquipped(next);
        String msg = cur != null
            ? ("✦ Magia Activa [Slot " + (next + 1) + "]: " + cur.displayName + " (Niv. " + prof.getLevel(cur) + ") ✦")
            : ("Slot " + (next + 1) + " vacío — Equipa magias en [M]");
        player.displayClientMessage(Component.literal(msg).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), true);
        player.level().playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.7F, 1.35F);
    }

    public static void upgradeMagic(ServerPlayer player, int magicOrdinal) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        GodOfWarMagic magic = GodOfWarMagic.byOrdinal(magicOrdinal);
        if (magic == null) return;

        GodOfWarMagicData data = GodOfWarMagicData.get(server);
        GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());
        if (!prof.isUnlocked(magic)) return;

        int curLvl = prof.getLevel(magic);
        if (curLvl >= GodOfWarMagic.MAX_LEVEL) return;

        int cost = magic.getBloodUpgradeCost(curLvl);
        if (!player.getAbilities().instabuild && !BloodOrbManager.spendBlood(player, cost)) {
            player.displayClientMessage(
                Component.literal("Necesitas " + cost + " Orbes de Sangre para potenciar " + magic.displayName + ".")
                    .withStyle(ChatFormatting.RED), true);
            return;
        }
        int nextLvl = curLvl + 1;
        prof.setLevel(magic, nextLvl);
        data.setDirty();
        syncToClient(player);

        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.25F);
        player.displayClientMessage(
            Component.literal("✦ " + magic.displayName + " potenciada al Nivel " + nextLvl + "/5 ✦")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
    }

    /**
     * Lanza la magia equipada en el slot activo (0..2).
     * Devuelve true si había magia equipada y se intentó lanzar; false si no hay magia equipada en el slot activo.
     */
    public static boolean castActiveMagic(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null || !(player.level() instanceof ServerLevel serverLevel)) return false;

        GodOfWarMagicData data = GodOfWarMagicData.get(server);
        GodOfWarMagicData.PlayerProfile prof = data.getOrCreate(player.getUUID());
        GodOfWarMagic magic = prof.getEquipped(prof.activeSlot);
        if (magic == null) {
            return false;
        }

        int lvl = prof.getLevel(magic);
        long now = server.getTickCount();
        long[] cds = COOLDOWNS.computeIfAbsent(player.getUUID(), u -> new long[GodOfWarMagic.values().length]);
        long readyAt = cds[magic.ordinal()];
        if (!player.getAbilities().instabuild && now < readyAt) {
            float sec = (readyAt - now) / 20.0F;
            player.displayClientMessage(
                Component.literal(String.format("%s recargando (%.1fs)...", magic.displayName, sec))
                    .withStyle(ChatFormatting.GRAY), true);
            return true;
        }

        int manaCost = magic.getEffectiveManaCost(lvl);
        if (!player.getAbilities().instabuild && prof.mana < manaCost) {
            player.displayClientMessage(
                Component.literal("Maná insuficiente (" + (int) prof.mana + "/" + manaCost + ") para " + magic.displayName)
                    .withStyle(ChatFormatting.BLUE), true);
            return true;
        }

        if (!player.getAbilities().instabuild) {
            prof.mana = Math.max(0.0F, prof.mana - manaCost);
            cds[magic.ordinal()] = now + magic.getEffectiveCooldown(lvl);
            data.setDirty();
        }

        MagicExecutor.execute(serverLevel, player, magic, lvl, ACTIVE_EFFECTS);
        syncToClient(player);
        return true;
    }

    public static void syncToClient(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        GodOfWarMagicData.PlayerProfile prof = GodOfWarMagicData.get(server).getOrCreate(player.getUUID());
        long now = server.getTickCount();
        long[] cds = COOLDOWNS.get(player.getUUID());
        int[] remCd = new int[3];
        for (int i = 0; i < 3; i++) {
            int ord = prof.slots[i];
            if (ord >= 0 && cds != null && cds[ord] > now) {
                remCd[i] = (int) (cds[ord] - now);
            }
        }
        GodOfWarNetwork.sendMagicSync(player, prof.unlockedMask, prof.slots, prof.activeSlot, prof.levels, prof.mana, remCd);
    }
}
