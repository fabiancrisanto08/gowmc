package com.spartanforge.godofwar.client.screen;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.client.ClientStunState;
import com.spartanforge.godofwar.item.WeaponUpgradeHelper;
import com.spartanforge.godofwar.menu.GodOfWarUpgradeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Pantalla a PANTALLA COMPLETA del Menú POTENCIADOR con el diseño HD original limpio del usuario:
 * - Abarca el 100% de la ventana de Minecraft como un apartado independiente de God of War II.
 * - Marco griego perimetral a pantalla completa con calaveras espartanas en las esquinas y Ω superior.
 * - Panel izquierdo limpio (sin círculos sobrantes ni medallón pequeño): escudo espartano, rueda griega continua,
 *   slot circular único sobre la barra de Sangre (77, 108) e inventario estándar 9x3 + hotbar 18x18.
 * - Panel derecho amplio con estandarte POTENCIADOR (sin L1/R1), información de mejoras, Orbes de Sangre y botón POTENCIAR.
 */
public class GodOfWarUpgradeScreen extends AbstractContainerScreen<GodOfWarUpgradeMenu> {
    private static final ResourceLocation TEXTURE =
        new ResourceLocation(GodOfWarMod.MOD_ID, "textures/gui/upgrade_menu.png");

    private static final int ATLAS = 1024;

    // Sprite HD 50x50 del Orbe Rojo de Sangre dentro del atlas 1024x1024
    private static final float ORB_U = 498.0F;
    private static final float ORB_V = 215.0F;
    private static final int ORB_SRC = 50;

    // Botón grande POTENCIAR en el panel derecho + pestaña directa a MAGIAS [M]
    private static final int BTN_X = 294;
    private static final int BTN_Y = 199;
    private static final int BTN_W = 94;
    private static final int BTN_H = 26;
    private static final int MAGIC_TAB_X = 358;
    private static final int MAGIC_TAB_Y = 14;
    private static final int MAGIC_TAB_W = 48;
    private static final int MAGIC_TAB_H = 16;

    public GodOfWarUpgradeScreen(GodOfWarUpgradeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 408;
        this.imageHeight = 236;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 9999;
        this.inventoryLabelX = 9999;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // 1. FONDO A PANTALLA COMPLETA (cubre el 100% de la ventana como apartado independiente de God of War)
        graphics.fill(0, 0, this.width, this.height, 0xFF060A0B);

        // Si la ventana es más ancha que el espacio central, extiende suavemente la piedra oscura a la izquierda
        if (x > 0) {
            graphics.fill(0, 0, x + 6, this.height, 0xFF151417);
        }

        // 2. PANEL IZQUIERDO HD DEL USUARIO (recortado limpiamente sin la calavera superior-izquierda vieja)
        //    u=12, v=12, uw=438, vh=648 -> 180 x 236 (slot circular en 77,108; barra en 55,137; inv 9x3 18px en 5,153)
        graphics.blit(TEXTURE, x, y, 180, 236, 12.0F, 12.0F, 438, 648, ATLAS, ATLAS);

        // 3. MARCO GRIEGO PERIMETRAL A PANTALLA COMPLETA (repite el borde griego exacto del usuario en los 4 bordes!)
        int borderH = 10;
        int borderW = 11;
        for (int bx = 0; bx < this.width; bx += 92) {
            int segW = Math.min(92, this.width - bx);
            int srcSegW = Math.max(1, (int) (260.0F * (segW / 92.0F)));
            graphics.blit(TEXTURE, bx, 0, segW, borderH, 660.0F, 140.0F, srcSegW, 28, ATLAS, ATLAS);
            graphics.blit(TEXTURE, bx, this.height - borderH, segW, borderH, 660.0F, 140.0F, srcSegW, 28, ATLAS, ATLAS);
        }
        for (int by = 0; by < this.height; by += 92) {
            int segH = Math.min(92, this.height - by);
            int srcSegH = Math.max(1, (int) (260.0F * (segH / 92.0F)));
            graphics.blit(TEXTURE, 0, by, borderW, segH, 660.0F, 180.0F, 32, srcSegH, ATLAS, ATLAS);
            graphics.blit(TEXTURE, this.width - borderW, by, borderW, segH, 660.0F, 180.0F, 32, srcSegH, ATLAS, ATLAS);
        }

        // 4. CALAVERAS ESPARTANAS EN LAS ESQUINAS DE LA PANTALLA + EMBLEMA Ω SUPERIOR CENTRAL
        int skullSz = 22;
        graphics.blit(TEXTURE, 0, 0, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        graphics.blit(TEXTURE, this.width - skullSz, 0, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        graphics.blit(TEXTURE, this.width - skullSz, this.height - skullSz, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        if (x >= skullSz) {
            graphics.blit(TEXTURE, 0, this.height - skullSz, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        }
        // Emblema dorado Omega (Ω) en el centro superior de la pantalla
        graphics.blit(TEXTURE, this.width / 2 - 11, 0, 22, 22, 580.0F, 140.0F, 56, 56, ATLAS, ATLAS);

        // 5. ESTANDARTE ROJO "POTENCIADOR" SIN L1 NI R1 (centrado en el panel derecho rc = 294) + pestaña MAGIAS
        graphics.blit(TEXTURE, x + 226, y + 6, 136, 70, 500.0F, 0.0F, 232, 119, ATLAS, ATLAS);
        boolean hoverMagicTab = mouseX >= x + MAGIC_TAB_X && mouseX <= x + MAGIC_TAB_X + MAGIC_TAB_W
            && mouseY >= y + MAGIC_TAB_Y && mouseY <= y + MAGIC_TAB_Y + MAGIC_TAB_H;
        graphics.fill(x + MAGIC_TAB_X - 1, y + MAGIC_TAB_Y - 1, x + MAGIC_TAB_X + MAGIC_TAB_W + 1, y + MAGIC_TAB_Y + MAGIC_TAB_H + 1,
            hoverMagicTab ? 0xFFFFF0A6 : 0xFFB8862D);
        graphics.fill(x + MAGIC_TAB_X, y + MAGIC_TAB_Y, x + MAGIC_TAB_X + MAGIC_TAB_W, y + MAGIC_TAB_Y + MAGIC_TAB_H,
            hoverMagicTab ? 0xFF163B5C : 0xFF0E2033);

        Player player = this.minecraft != null ? this.minecraft.player : null;
        ItemStack stack = this.menu.getTargetItem();
        int playerBlood = ClientStunState.getPlayerBloodOrbs();

        // 6. BARRA ROJA DE PROGRESO DE SANGRE justo debajo del slot circular (55, 137, 60x6)
        int barX = x + 55;
        int barY = y + 137;
        int barW = 60;
        int barH = 6;
        if (!stack.isEmpty() && WeaponUpgradeHelper.isUpgradeableWeapon(stack.getItem()) && player != null) {
            boolean isMax = WeaponUpgradeHelper.isMaxLevel(stack);
            int cost = WeaponUpgradeHelper.getBloodCost(stack);
            float ratio = isMax || player.getAbilities().instabuild
                ? 1.0F
                : (cost <= 0 ? 1.0F : Math.min(1.0F, playerBlood / (float) cost));
            int fillW = (int) (barW * ratio);
            if (fillW > 0) {
                int fillCol = isMax ? 0xFFFFD13B : (ratio >= 1.0F ? 0xFFE6281E : 0xFFB81C18);
                int hiCol = isMax ? 0xFFFFF2A6 : 0xFFFF705E;
                graphics.fill(barX, barY, barX + fillW, barY + barH, fillCol);
                graphics.fill(barX, barY, barX + fillW, barY + 2, hiCol);
                if (fillW < barW && fillW > 2) {
                    graphics.fill(barX + fillW - 1, barY, barX + fillW + 1, barY + barH, 0xFFFFDC73);
                }
            }
        }

        // 7. BOTÓN GRANDE POTENCIAR (doble marco dorado + hierro + brillo carmesí)
        boolean canUpgrade = canUpgradeNow(player, stack, playerBlood);
        boolean hover = isMouseOverUpgradeButton(mouseX, mouseY);
        int bx = x + BTN_X;
        int by = y + BTN_Y;

        int outerBorder = canUpgrade ? (hover ? 0xFFFFF2A6 : 0xFFD9A640) : 0xFF5A452C;
        int innerBorder = canUpgrade ? 0xFF8C5B1A : 0xFF33261B;
        int fillBg = canUpgrade ? (hover ? 0xFF4F1714 : 0xFF2B1211) : 0xFF1C1717;
        int bottomGlow = canUpgrade ? (hover ? 0xFFCC2E22 : 0xFF8F1B14) : 0xFF261E1D;

        graphics.fill(bx - 2, by - 2, bx + BTN_W + 2, by + BTN_H + 2, 0xFF0A0606);
        graphics.fill(bx - 1, by - 1, bx + BTN_W + 1, by + BTN_H + 1, outerBorder);
        graphics.fill(bx, by, bx + BTN_W, by + BTN_H, innerBorder);
        graphics.fill(bx + 2, by + 2, bx + BTN_W - 2, by + BTN_H - 2, fillBg);
        graphics.fill(bx + 3, by + BTN_H - 6, bx + BTN_W - 3, by + BTN_H - 2, bottomGlow);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        Player player = this.minecraft != null ? this.minecraft.player : null;
        ItemStack stack = this.menu.getTargetItem();
        int playerBlood = ClientStunState.getPlayerBloodOrbs();
        boolean creative = player != null && player.getAbilities().instabuild;
        int rc = 294; // Centro del amplio panel derecho (x=180..408)

        // Título limpio "POTENCIADOR" sobre la cinta roja (SIN L1 ni R1!) + etiqueta de pestaña MAGIAS
        graphics.drawCenteredString(this.font, "POTENCIADOR", rc, 37, 0xFFFFE594);
        graphics.drawCenteredString(this.font, "MAGIAS", MAGIC_TAB_X + MAGIC_TAB_W / 2, MAGIC_TAB_Y + 4, 0xFF8CE6FF);

        // Leyenda de controles (abajo derecha, junto al botón POTENCIAR)
        graphics.drawString(this.font, "✕", 196, 203, 0xFFE03A3A, true);
        graphics.drawString(this.font, "= POTENCIAR", 205, 203, 0xFFE8DFC8, true);
        graphics.drawString(this.font, "△", 196, 215, 0xFF4CD964, true);
        graphics.drawString(this.font, "= ATRÁS", 205, 215, 0xFFE8DFC8, true);

        if (stack.isEmpty() || !WeaponUpgradeHelper.isUpgradeableWeapon(stack.getItem())) {
            graphics.drawCenteredString(this.font, "COLOCA TU ARMA", rc, 82, 0xFFD63229);
            graphics.drawCenteredString(this.font, "EN EL CÍRCULO DORADO", rc, 95, 0xFFFFD86B);
            graphics.drawCenteredString(this.font, "Derrota enemigos para", rc, 116, 0xFFE8DFC8);
            graphics.drawCenteredString(this.font, "obtener Orbes de Sangre", rc, 128, 0xFFE8DFC8);
            graphics.drawCenteredString(this.font, "y evolucionar tu equipo.", rc, 140, 0xFFE8DFC8);

            drawBloodLine(graphics, rc, 165, "TU SANGRE: ", playerBlood, 0xFFFF5548);
            graphics.drawCenteredString(this.font, "POTENCIAR", BTN_X + BTN_W / 2, BTN_Y + 9, 0xFF7D6B5E);
            return;
        }

        int dispLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        boolean isMax = WeaponUpgradeHelper.isMaxLevel(stack);
        int bloodCost = WeaponUpgradeHelper.getBloodCost(stack);

        // Nombre del arma en rojo-carmesí God of War
        String name = stack.getHoverName().getString().toUpperCase();
        if (name.length() > 26) {
            name = name.substring(0, 25) + "…";
        }
        graphics.drawCenteredString(this.font, name, rc, 78, 0xFFD63229);

        // NIVEL X
        String lvlLine = isMax ? "NIVEL 5 (MÁXIMO)" : ("NIVEL " + dispLvl + " / 5");
        graphics.drawCenteredString(this.font, lvlLine, rc, 91, 0xFFF2E8CE);

        // SIGUIENTE MEJORA:
        graphics.drawCenteredString(this.font, "SIGUIENTE MEJORA:", rc, 108, 0xFFFFFFFF);
        List<String> lines = WeaponUpgradeHelper.getNextUpgradeDescription(stack);
        for (int i = 0; i < Math.min(3, lines.size()); i++) {
            int col = (i < 2) ? 0xFFE0483B : 0xFFF2E6CC;
            graphics.drawCenteredString(this.font, lines.get(i), rc, 121 + i * 11, col);
        }

        // Costo en Orbes de Sangre + Sprite de Orbe Rojo HD inline
        if (isMax) {
            graphics.drawCenteredString(this.font, "✦ FORMA DIVINA MÁXIMA ✦", rc, 162, 0xFFFFD84D);
            drawBloodLine(graphics, rc, 179, "SANGRE: ", playerBlood, 0xFFFF6E60);
            graphics.drawCenteredString(this.font, "MÁXIMO", BTN_X + BTN_W / 2, BTN_Y + 9, 0xFFB09B6B);
        } else {
            boolean enough = creative || playerBlood >= bloodCost;
            if (creative) {
                graphics.drawCenteredString(this.font, "MODO CREATIVO (GRATIS)", rc, 162, 0xFFFFD84D);
            } else {
                String leftTxt = "NECESITAS " + bloodCost + " ";
                String rightTxt = " ORBES";
                int totalW = this.font.width(leftTxt) + 12 + this.font.width(rightTxt);
                int startX = rc - totalW / 2;
                int txtCol = enough ? 0xFFF5ECD6 : 0xFFFF6B60;
                graphics.drawString(this.font, leftTxt, startX, 158, txtCol, true);
                graphics.blit(TEXTURE, startX + this.font.width(leftTxt), 156, 11, 11, ORB_U, ORB_V, ORB_SRC, ORB_SRC, ATLAS, ATLAS);
                graphics.drawString(this.font, rightTxt, startX + this.font.width(leftTxt) + 12, 158, txtCol, true);
                graphics.drawCenteredString(this.font, "PARA POTENCIAR EL OBJETO.", rc, 170, 0xFFE8DFC8);
            }

            drawBloodLine(graphics, rc, 183, "SANGRE: ", playerBlood, enough ? 0xFFFF5E52 : 0xFFCC4444);

            int btnTextCol = enough ? 0xFFFF5244 : 0xFF7D6B5E;
            graphics.drawCenteredString(this.font, "POTENCIAR", BTN_X + BTN_W / 2, BTN_Y + 9, btnTextCol);
        }
    }

    private void drawBloodLine(GuiGraphics graphics, int centerX, int y, String prefix, int amount, int color) {
        String num = String.format("%,d", amount);
        int w = this.font.width(prefix) + 13 + this.font.width(num);
        int sx = centerX - w / 2;
        graphics.drawString(this.font, prefix, sx, y, 0xFFD9C7A3, true);
        graphics.blit(TEXTURE, sx + this.font.width(prefix), y - 1, 11, 11, ORB_U, ORB_V, ORB_SRC, ORB_SRC, ATLAS, ATLAS);
        graphics.drawString(this.font, num, sx + this.font.width(prefix) + 13, y, color, true);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Sin renderBackground semitransparente: nuestro renderBg ya llena el 100% de la pantalla!
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseX >= this.leftPos + MAGIC_TAB_X && mouseX <= this.leftPos + MAGIC_TAB_X + MAGIC_TAB_W
            && mouseY >= this.topPos + MAGIC_TAB_Y && mouseY <= this.topPos + MAGIC_TAB_Y + MAGIC_TAB_H) {
            if (this.minecraft != null) {
                if (this.minecraft.player != null) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.9F, 1.1F);
                }
                this.minecraft.setScreen(new GodOfWarMagicScreen());
            }
            return true;
        }
        if (button == 0 && isMouseOverUpgradeButton(mouseX, mouseY)) {
            triggerUpgradeClick();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) {
            triggerUpgradeClick();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void triggerUpgradeClick() {
        Player player = this.minecraft != null ? this.minecraft.player : null;
        int blood = ClientStunState.getPlayerBloodOrbs();
        if (canUpgradeNow(player, this.menu.getTargetItem(), blood) && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, GodOfWarUpgradeMenu.BUTTON_UPGRADE);
            if (player != null) {
                player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.9F, 1.0F);
            }
        }
    }

    private boolean isMouseOverUpgradeButton(double mouseX, double mouseY) {
        int bx = this.leftPos + BTN_X;
        int by = this.topPos + BTN_Y;
        return mouseX >= bx && mouseX <= bx + BTN_W && mouseY >= by && mouseY <= by + BTN_H;
    }

    private static boolean canUpgradeNow(Player player, ItemStack stack, int playerBlood) {
        if (player == null || stack.isEmpty() || !WeaponUpgradeHelper.isUpgradeableWeapon(stack.getItem())) {
            return false;
        }
        if (WeaponUpgradeHelper.isMaxLevel(stack)) {
            return false;
        }
        return player.getAbilities().instabuild || playerBlood >= WeaponUpgradeHelper.getBloodCost(stack);
    }
}
