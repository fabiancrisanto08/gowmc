package com.spartanforge.godofwar.combat;

import com.spartanforge.godofwar.entity.AtlasTitanEntity;
import com.spartanforge.godofwar.entity.CronosTitanEntity;
import com.spartanforge.godofwar.entity.GaiaTitanEntity;
import com.spartanforge.godofwar.entity.PersesTitanEntity;
import com.spartanforge.godofwar.entity.TyphonTitanEntity;
import com.spartanforge.godofwar.entity.BloodOrbEntity;
import com.spartanforge.godofwar.entity.base.AbstractGodBossEntity;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sistema completo de Orbes de Sangre (Red Orbs) estilo God of War:
 * - Cada entidad derrotada por un jugador suelta EXACTAMENTE 1 BloodOrbEntity (cero lag).
 * - El valor del orbe escala según el tipo/vida del enemigo (de 15 en mobs pequeños hasta 9,000 en Titanes).
 * - Ejecutar brutalmente [V] otorga +75% de Sangre adicional.
 * - La sangre acumulada se guarda permanentemente en el Scoreboard del mundo ("godofwar_blood")
 *   sobreviendo muertes, reinicios y cambios de dimensión en Forge y Fabric.
 */
public final class BloodOrbManager {
    public static final String OBJECTIVE_NAME = "godofwar_blood";
    public static final int MAX_BLOOD = 999999;

    // Marca temporal (timestamp ms) de entidades ejecutadas brutalmente para dar bono de +75% Sangre
    private static final Map<UUID, Long> RECENTLY_EXECUTED = new ConcurrentHashMap<>();

    private BloodOrbManager() {}

    public static void register() {
        EntityEvent.LIVING_DEATH.register(BloodOrbManager::onLivingDeath);

        PlayerEvent.PLAYER_JOIN.register(player -> syncToClient(player, 0));
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd) -> syncToClient(player, 0));
        PlayerEvent.CHANGE_DIMENSION.register((player, oldLvl, newLvl) -> syncToClient(player, 0));
    }

    public static void markExecuted(UUID entityUuid) {
        RECENTLY_EXECUTED.put(entityUuid, System.currentTimeMillis());
    }

    private static boolean consumeExecutedFlag(UUID entityUuid) {
        Long ts = RECENTLY_EXECUTED.remove(entityUuid);
        return ts != null && (System.currentTimeMillis() - ts) < 3500L;
    }

    private static EventResult onLivingDeath(LivingEntity victim, DamageSource source) {
        if (victim.level().isClientSide || !(victim.level() instanceof ServerLevel serverLevel)) {
            return EventResult.pass();
        }
        if (victim instanceof Player) {
            return EventResult.pass();
        }

        // Solo suelta Orbe de Sangre si intervino un jugador (evita lag de muertes naturales lejos del jugador)
        boolean killedByPlayer = source.getEntity() instanceof Player
            || victim.getKillCredit() instanceof Player
            || victim.getLastHurtByMob() instanceof Player;
        if (!killedByPlayer) {
            return EventResult.pass();
        }

        int value = computeBloodValue(victim);
        if (consumeExecutedFlag(victim.getUUID())) {
            value = Math.round(value * 1.75F);
        }

        // Exactamente UN solo orbe por mob derrotado (rendimiento óptimo!)
        BloodOrbEntity orb = new BloodOrbEntity(
            serverLevel,
            victim.getX(),
            victim.getY() + Math.min(1.4D, victim.getBbHeight() * 0.55D),
            victim.getZ(),
            value
        );
        serverLevel.addFreshEntity(orb);
        return EventResult.pass();
    }

    /**
     * Escala la cantidad de Orbes de Sangre según el poder y rango mítico del enemigo derrotado.
     */
    public static int computeBloodValue(LivingEntity entity) {
        // Titanes Primordiales: recompensa colosal
        if (entity instanceof CronosTitanEntity || entity instanceof GaiaTitanEntity
            || entity instanceof AtlasTitanEntity || entity instanceof TyphonTitanEntity
            || entity instanceof PersesTitanEntity) {
            return 7500;
        }
        // Dioses Jefes de God of War (Zeus, Ares, Thor, Odin, Sigrun, etc.)
        if (entity instanceof AbstractGodBossEntity) {
            return (int) Math.min(6000, Math.max(3200, Math.round(entity.getMaxHealth() * 10.5F)));
        }
        // Jefes Vanilla (Wither, Ender Dragon, Warden)
        if (entity instanceof EnderDragon) return 3000;
        if (entity instanceof WitherBoss) return 2200;
        if (entity instanceof Warden) return 2000;

        float hp = entity.getMaxHealth();
        if (hp >= 180.0F) return Math.min(2500, Math.round(hp * 6.5F));
        if (hp >= 60.0F)  return Math.round(140 + (hp - 60.0F) * 2.2F);  // Minotauros, Cíclopes, Gorgonas, Cerberos, Einherjar
        if (hp >= 26.0F)  return Math.round(80 + (hp - 26.0F) * 1.8F);   // Legión Romana, Draugr, Endermen, Blazes
        if (entity instanceof Enemy) {
            return Math.max(40, Math.round(hp * 2.4F));                  // Zombies, Esqueletos, Creepers (~48)
        }
        return Math.max(15, Math.round(hp * 1.6F));                      // Animales / criaturas pasivas (~15-30)
    }

    private static Objective getOrCreateObjective(MinecraftServer server) {
        Scoreboard sb = server.getScoreboard();
        Objective obj = sb.getObjective(OBJECTIVE_NAME);
        if (obj == null) {
            obj = sb.addObjective(
                OBJECTIVE_NAME,
                ObjectiveCriteria.DUMMY,
                Component.literal("Orbes de Sangre"),
                ObjectiveCriteria.RenderType.INTEGER
            );
        }
        return obj;
    }

    public static int getBlood(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) return 0;
        Objective obj = getOrCreateObjective(server);
        Score score = server.getScoreboard().getOrCreatePlayerScore(player.getScoreboardName(), obj);
        return Math.max(0, score.getScore());
    }

    public static void setBlood(ServerPlayer player, int amount, int deltaForClient) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        Objective obj = getOrCreateObjective(server);
        Score score = server.getScoreboard().getOrCreatePlayerScore(player.getScoreboardName(), obj);
        int clamped = Math.max(0, Math.min(MAX_BLOOD, amount));
        score.setScore(clamped);
        GodOfWarNetwork.sendBloodSync(player, clamped, deltaForClient);
    }

    public static void addBlood(ServerPlayer player, int delta) {
        if (delta <= 0) return;
        int current = getBlood(player);
        setBlood(player, current + delta, delta);
    }

    public static boolean spendBlood(ServerPlayer player, int cost) {
        if (cost <= 0) return true;
        int current = getBlood(player);
        if (current < cost) return false;
        setBlood(player, current - cost, -cost);
        return true;
    }

    public static void syncToClient(ServerPlayer player, int delta) {
        GodOfWarNetwork.sendBloodSync(player, getBlood(player), delta);
    }
}
