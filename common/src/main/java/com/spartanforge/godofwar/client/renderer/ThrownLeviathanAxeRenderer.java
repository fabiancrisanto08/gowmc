package com.spartanforge.godofwar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.spartanforge.godofwar.entity.projectile.ThrownLeviathanAxeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Renderizador 3D para el Hacha Leviatán lanzada:
 * Gira vertiginosamente sobre su eje durante el vuelo y el retorno,
 * y queda clavada en ángulo épico al impactar en un bloque o enemigo.
 */
public class ThrownLeviathanAxeRenderer extends EntityRenderer<ThrownLeviathanAxeEntity> {
    private final ItemRenderer itemRenderer;

    public ThrownLeviathanAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownLeviathanAxeEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.translate(0.0D, 0.25D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch));

        if (!entity.isStuck()) {
            float spinSpeed = entity.isReturning() ? -46.0F : 38.0F;
            float spin = (entity.tickCount + partialTicks) * spinSpeed;
            poseStack.mulPose(Axis.ZP.rotationDegrees(spin));
        } else {
            poseStack.mulPose(Axis.ZP.rotationDegrees(-35.0F));
        }

        poseStack.scale(1.45F, 1.45F, 1.45F);
        this.itemRenderer.renderStatic(
            entity.getItem(),
            ItemDisplayContext.FIXED,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            buffer,
            entity.level(),
            entity.getId()
        );
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownLeviathanAxeEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
