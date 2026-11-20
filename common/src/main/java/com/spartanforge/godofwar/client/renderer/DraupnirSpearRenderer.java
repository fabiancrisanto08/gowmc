package com.spartanforge.godofwar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.spartanforge.godofwar.entity.projectile.DraupnirSpearProjectileEntity;
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
 * Renderizador 3D para la Lanza Draupnir proyectil:
 * Se orienta en dirección de la trayectoria de vuelo y vibra con energía dorada cuando queda clavada.
 */
public class DraupnirSpearRenderer extends EntityRenderer<DraupnirSpearProjectileEntity> {
    private final ItemRenderer itemRenderer;

    public DraupnirSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(DraupnirSpearProjectileEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.translate(0.0D, 0.2D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch - 90.0F));

        if (entity.isStuck()) {
            float vib = Mth.sin((entity.tickCount + partialTicks) * 1.4F) * 2.5F;
            poseStack.mulPose(Axis.XP.rotationDegrees(vib));
        }

        poseStack.scale(1.55F, 1.55F, 1.55F);
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
    public ResourceLocation getTextureLocation(DraupnirSpearProjectileEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
