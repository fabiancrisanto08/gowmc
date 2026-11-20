package com.spartanforge.godofwar.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caché del lado cliente para:
 * - Barra de Aturdimiento (Stun Meter) de enemigos cercanos sincronizada desde el servidor.
 * - Efectos cinemáticos de Ejecución Brutal (temblor de cámara, destello espartano, banner).
 */
public final class ClientStunState {
    public record StunSnapshot(int entityId, float stun, float maxStun, boolean stunned, int stunTicksLeft, long receivedTimeMs) {
        public float progress() {
            return maxStun <= 0.0F ? 0.0F : Math.min(1.0F, Math.max(0.0F, stun / maxStun));
        }

        public boolean isExpired(long nowMs) {
            return (nowMs - receivedTimeMs) > 8500L;
        }
    }

    private static final Map<Integer, StunSnapshot> STUN_MAP = new ConcurrentHashMap<>();

    private static float cameraShakeTicks = 0.0F;
    private static float cameraShakeIntensity = 0.0F;
    private static int executionFlashTicks = 0;
    private static int executionBannerTicks = 0;
    private static String executionBannerText = "";
    private static boolean executionWasBoss = false;

    // Orbes de Sangre (Red Orbs) del jugador sincronizados desde el servidor
    private static int playerBloodOrbs = 0;
    private static int recentBloodDelta = 0;
    private static int recentBloodTicks = 0;

    private ClientStunState() {}

    public static void updateBlood(int totalBlood, int delta) {
        playerBloodOrbs = Math.max(0, totalBlood);
        if (delta != 0) {
            if (recentBloodTicks > 0 && ((delta > 0 && recentBloodDelta > 0) || (delta < 0 && recentBloodDelta < 0))) {
                recentBloodDelta += delta;
            } else {
                recentBloodDelta = delta;
            }
            recentBloodTicks = 55;
        }
    }

    public static int getPlayerBloodOrbs() {
        return playerBloodOrbs;
    }

    public static int getRecentBloodDelta() {
        return recentBloodDelta;
    }

    public static int getRecentBloodTicks() {
        return recentBloodTicks;
    }

    public static void updateStun(int entityId, float stun, float maxStun, boolean stunned, int stunTicksLeft) {
        if (stun <= 0.01F && !stunned) {
            STUN_MAP.remove(entityId);
            return;
        }
        STUN_MAP.put(entityId, new StunSnapshot(entityId, stun, maxStun, stunned, stunTicksLeft, System.currentTimeMillis()));
    }

    public static StunSnapshot getStun(int entityId) {
        StunSnapshot snap = STUN_MAP.get(entityId);
        if (snap != null && snap.isExpired(System.currentTimeMillis())) {
            STUN_MAP.remove(entityId);
            return null;
        }
        return snap;
    }

    public static Map<Integer, StunSnapshot> getAll() {
        long now = System.currentTimeMillis();
        STUN_MAP.entrySet().removeIf(e -> e.getValue().isExpired(now));
        return STUN_MAP;
    }

    public static void clearAll() {
        STUN_MAP.clear();
        cameraShakeTicks = 0.0F;
        executionFlashTicks = 0;
        executionBannerTicks = 0;
        playerBloodOrbs = 0;
        recentBloodDelta = 0;
        recentBloodTicks = 0;
    }

    public static void triggerExecutionEffects(float shakeIntensity, int shakeDuration, String bannerText, boolean bossFinisher) {
        cameraShakeIntensity = Math.max(cameraShakeIntensity, shakeIntensity);
        cameraShakeTicks = Math.max(cameraShakeTicks, shakeDuration);
        executionFlashTicks = 14;
        executionBannerTicks = 44;
        executionBannerText = bannerText;
        executionWasBoss = bossFinisher;
    }

    public static void triggerParryShake() {
        cameraShakeIntensity = Math.max(cameraShakeIntensity, 2.2F);
        cameraShakeTicks = Math.max(cameraShakeTicks, 8.0F);
    }

    public static void clientTick() {
        if (cameraShakeTicks > 0.0F) {
            cameraShakeTicks = Math.max(0.0F, cameraShakeTicks - 1.0F);
            if (cameraShakeTicks <= 0.0F) {
                cameraShakeIntensity = 0.0F;
            }
        }
        if (executionFlashTicks > 0) {
            executionFlashTicks--;
        }
        if (executionBannerTicks > 0) {
            executionBannerTicks--;
        }
        if (recentBloodTicks > 0) {
            recentBloodTicks--;
            if (recentBloodTicks == 0) {
                recentBloodDelta = 0;
            }
        }
    }

    public static float getCurrentShakeMagnitude() {
        if (cameraShakeTicks <= 0.0F) return 0.0F;
        return cameraShakeIntensity * Math.min(1.0F, cameraShakeTicks / 12.0F);
    }

    public static int getExecutionFlashTicks() {
        return executionFlashTicks;
    }

    public static int getExecutionBannerTicks() {
        return executionBannerTicks;
    }

    public static String getExecutionBannerText() {
        return executionBannerText;
    }

    public static boolean wasExecutionBoss() {
        return executionWasBoss;
    }
}
