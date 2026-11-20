package com.spartanforge.godofwar.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.client.ClientStunState;
import com.spartanforge.godofwar.client.GodOfWarKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

/**
 * Eventos visuales exclusivos del bus FORGE en cliente:
 * 1. Temblor dinámico de cámara (Camera Shake) al realizar Ejecuciones Brutales y Parries Dorados.
 * 2. Indicador flotante 3D en el mundo sobre la cabeza de los enemigos aturdidos ([V] R3 EJECUTAR / barra de Stun).
 */
@Mod.EventBusSubscriber(modid = GodOfWarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GodOfWarForgeClientEvents {
    private GodOfWarForgeClientEvents() {}

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        float mag = ClientStunState.getCurrentShakeMagnitude();
        if (mag <= 0.01F) return;

        double t = (System.nanoTime() * 1.0e-8D);
        float yawOffset = (float) (Math.sin(t * 9.3D) * mag * 0.65D);
        float pitchOffset = (float) (Math.cos(t * 11.7D) * mag * 0.55D);
        float rollOffset = (float) (Math.sin(t * 7.1D) * mag * 0.85D);

        event.setYaw(event.getYaw() + yawOffset);
        event.setPitch(event.getPitch() + pitchOffset);
        event.setRoll(event.getRoll() + rollOffset);
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        ClientStunState.StunSnapshot snap = ClientStunState.getStun(entity.getId());
        if (snap == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity == mc.player) return;
        if (mc.player.distanceToSqr(entity) > 900.0D) return; // Máximo 30 bloques

        PoseStack poseStack = event.getPoseStack();
        Font font = mc.font;

        poseStack.pushPose();
        double yOffset = entity.getBbHeight() + 0.65D;
        poseStack.translate(0.0D, yOffset, 0.0D);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        float scale = snap.stunned() ? 0.032F : 0.024F;
        poseStack.scale(-scale, -scale, scale);

        Matrix4f matrix = poseStack.last().pose();
        String label;
        int textColor;
        int bgColor;

        if (snap.stunned()) {
            boolean pulse = ((System.currentTimeMillis() / 150L) % 2L) == 0L;
            String key = GodOfWarKeybinds.KEY_EXECUTE.getTranslatedKeyMessage().getString().toUpperCase();
            label = "⚔ [" + key + "] R3 EJECUTAR ⚔";
            textColor = pulse ? 0xFFFFEA61 : 0xFFFF4A3D;
            bgColor = pulse ? 0xCC700C0C : 0xCC2B1206;
        } else {
            int pct = Math.min(99, Math.max(1, Math.round(snap.progress() * 100.0F)));
            int filledBlocks = Math.max(1, Math.round(snap.progress() * 8.0F));
            StringBuilder bar = new StringBuilder("STUN [");
            for (int i = 0; i < 8; i++) {
                bar.append(i < filledBlocks ? "█" : "░");
            }
            bar.append("] ").append(pct).append("%");
            label = bar.toString();
            textColor = 0xFFFFC247;
            bgColor = 0x88140F0A;
        }

        float x = -font.width(label) / 2.0F;
        font.drawInBatch(
            label,
            x,
            0.0F,
            textColor,
            false,
            matrix,
            event.getMultiBufferSource(),
            Font.DisplayMode.SEE_THROUGH,
            bgColor,
            LightTexture.FULL_BRIGHT
        );
        poseStack.popPose();
    }
}
