package com.spartanforge.godofwar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.BloodOrbEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * Renderizador brillante siempre orientado hacia la cámara para el Orbe de Sangre (Red Orb).
 * Su tamaño escala según la cantidad de sangre que contiene (pequeño en mobs, enorme en Dioses y Titanes).
 */
public class BloodOrbRenderer extends EntityRenderer<BloodOrbEntity> {
    private static final ResourceLocation TEXTURE =
        new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/blood_orb.png");

    public BloodOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.14F;
        this.shadowStrength = 0.45F;
    }

    @Override
    public void render(BloodOrbEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        int val = entity.getBloodValue();
        float baseScale = val >= 3000 ? 0.85F : (val >= 1000 ? 0.65F : (val >= 200 ? 0.50F : 0.38F));
        float pulse = 1.0F + Mth.sin((entity.tickCount + partialTicks) * 0.22F) * 0.10F;
        float bob = Mth.sin((entity.tickCount + partialTicks) * 0.14F) * 0.08F;

        poseStack.translate(0.0D, 0.25D + bob, 0.0D);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        float s = baseScale * pulse;
        poseStack.scale(s, s, s);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f mat = pose.pose();
        Matrix3f norm = pose.normal();

        int light = LightTexture.FULL_BRIGHT;
        vertex(vc, mat, norm, -0.5F, -0.5F, 0.0F, 1.0F, light);
        vertex(vc, mat, norm,  0.5F, -0.5F, 1.0F, 1.0F, light);
        vertex(vc, mat, norm,  0.5F,  0.5F, 1.0F, 0.0F, light);
        vertex(vc, mat, norm, -0.5F,  0.5F, 0.0F, 0.0F, light);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer vc, Matrix4f mat, Matrix3f norm,
                               float x, float y, float u, float v, int light) {
        vc.vertex(mat, x, y, 0.0F)
            .color(255, 255, 255, 245)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(light)
            .normal(norm, 0.0F, 1.0F, 0.0F)
            .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(BloodOrbEntity entity) {
        return TEXTURE;
    }
}
