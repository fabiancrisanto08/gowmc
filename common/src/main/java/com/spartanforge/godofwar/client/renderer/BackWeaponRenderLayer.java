package com.spartanforge.godofwar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.spartanforge.godofwar.compat.EpicFightCompat;
import com.spartanforge.godofwar.item.BladeOfOlympusItem;
import com.spartanforge.godofwar.item.BladesOfAthenaItem;
import com.spartanforge.godofwar.item.BladesOfChaosItem;
import com.spartanforge.godofwar.item.DraupnirSpearItem;
import com.spartanforge.godofwar.item.GuardianShieldItem;
import com.spartanforge.godofwar.item.LeviathanAxeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Renderiza hasta 2 armas legendarias de God of War enfundadas en la espalda del jugador en 3D
 * (por ejemplo, el Hacha Leviatán y las Espadas del Caos cruzadas en la espalda como Kratos).
 * Cuando Epic Fight está activo, cede el control al rig animado de Epic Fight para evitar solapamientos.
 */
public class BackWeaponRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public BackWeaponRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.isInvisible() || EpicFightCompat.isLoaded()) {
            return;
        }

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        ItemStack primaryBack = ItemStack.EMPTY;
        ItemStack secondaryBack = ItemStack.EMPTY;

        for (int i = 0; i < 9; i++) {
            ItemStack st = player.getInventory().getItem(i);
            if (st.isEmpty() || st == main || st == off) continue;
            if (!isBackHolsterable(st.getItem())) continue;
            if (primaryBack.isEmpty()) {
                primaryBack = st;
            } else if (secondaryBack.isEmpty() && st.getItem() != primaryBack.getItem()) {
                secondaryBack = st;
                break;
            }
        }

        if (primaryBack.isEmpty()) return;

        // Render arma principal en la espalda en diagonal derecha
        renderHolstered(poseStack, buffer, packedLight, player, primaryBack, 0.04F, 0.34F, 0.22F, -142.0F);

        // Si lleva una segunda arma icónica en la barra rápida, se cruza en X en la espalda
        if (!secondaryBack.isEmpty()) {
            renderHolstered(poseStack, buffer, packedLight, player, secondaryBack, -0.04F, 0.34F, 0.26F, 142.0F);
        }
    }

    private void renderHolstered(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                 AbstractClientPlayer player, ItemStack stack,
                                 float tx, float ty, float tz, float zAngle) {
        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        poseStack.translate(tx, ty, tz);
        if (stack.getItem() instanceof GuardianShieldItem) {
            poseStack.scale(0.85F, 0.85F, 0.85F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        } else {
            poseStack.mulPose(Axis.ZP.rotationDegrees(zAngle));
            poseStack.scale(0.88F, 0.88F, 0.88F);
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(
            stack,
            ItemDisplayContext.FIXED,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            buffer,
            player.level(),
            player.getId()
        );
        poseStack.popPose();
    }

    private static boolean isBackHolsterable(Item item) {
        return item instanceof LeviathanAxeItem
            || item instanceof BladesOfChaosItem
            || item instanceof BladesOfAthenaItem
            || item instanceof BladeOfOlympusItem
            || item instanceof DraupnirSpearItem
            || item instanceof GuardianShieldItem;
    }
}
