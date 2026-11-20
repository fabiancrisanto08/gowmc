package com.spartanforge.godofwar.client.screen;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.client.ClientMagicState;
import com.spartanforge.godofwar.client.ClientStunState;
import com.spartanforge.godofwar.client.GodOfWarKeybinds;
import com.spartanforge.godofwar.magic.GodOfWarMagic;
import com.spartanforge.godofwar.magic.GodOfWarMagicData;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

/**
 * Pantalla a PANTALLA COMPLETA del Menú de Magias estilo God of War II:
 * - Reutiliza el mismo marco griego HD, calaveras espartanas, emblema Ω, estandarte rojo-oro y Orbe Rojo de Sangre
 *   del Menú POTENCIADOR para lograr una estética 100% unificada.
 * - Panel izquierdo: Rueda Espartana HD con los 3 SLOTS DE MAGIA ACTIVOS (I, II, III) en medallones dorados,
 *   barra azul de Maná y visor de lore/estadísticas de la magia seleccionada.
 * - Panel derecho: Galería completa 6×4 de las 24 Magias canónicas de la Wiki de God of War, con botón para
 *   EQUIPAR en el slot seleccionado y botón POTENCIAR (Niv. 1 → 5 con Orbes de Sangre).
 */
public class GodOfWarMagicScreen extends Screen {
    private static final ResourceLocation UPGRADE_ATLAS =
        new ResourceLocation(GodOfWarMod.MOD_ID, "textures/gui/upgrade_menu.png");
    private static final ResourceLocation WHEEL_TEX =
        new ResourceLocation(GodOfWarMod.MOD_ID, "textures/gui/magic_menu_wheel.png");

    private static final int ATLAS = 1024;
    private static final int PANEL_W = 408;
    private static final int PANEL_H = 236;

    // Centros de los 3 medallones dorados sobre magic_menu_wheel.png (escalado 438x648 -> 180x236)
    private static final int[][] SLOT_CENTERS = {
        { 112, 36 },   // Slot I (Superior)
        { 130, 78 },   // Slot II (Medio)
        {  85, 114 }   // Slot III (Inferior)
    };
    private static final int SLOT_RADIUS = 17;

    // Cuadrícula 6x4 = 24 Magias de la saga God of War (panel derecho x=191..399, y=76..184)
    private static final int GRID_X = 193;
    private static final int GRID_Y = 76;
    private static final int GRID_COLS = 6;
    private static final int CELL_W = 34;
    private static final int CELL_H = 27;

    // Botones de acción (abajo derecha) + pestaña directa al POTENCIADOR [K]
    private static final int EQUIP_X = 193, EQUIP_Y = 198, EQUIP_W = 102, EQUIP_H = 26;
    private static final int UPG_X   = 301, UPG_Y   = 198, UPG_W   = 96,  UPG_H   = 26;
    private static final int TAB_X   = 358, TAB_Y   = 14,  TAB_W   = 48,  TAB_H   = 16;

    private int leftPos;
    private int topPos;
    private int selectedSlot = 0; // Slot activo objetivo (0, 1 o 2)
    private GodOfWarMagic selectedMagic = GodOfWarMagic.POSEIDONS_RAGE;
    private long lastClickMs = 0L;
    private GodOfWarMagic lastClickedMagic = null;

    public GodOfWarMagicScreen() {
        super(Component.translatable("gui.godofwar.magic_menu"));
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - PANEL_W) / 2;
        this.topPos = (this.height - PANEL_H) / 2;
        this.selectedSlot = ClientMagicState.getActiveSlot();

        // Selecciona por defecto la magia del slot activo, o la primera magia desbloqueada
        GodOfWarMagic eq = ClientMagicState.getEquipped(this.selectedSlot);
        if (eq != null) {
            this.selectedMagic = eq;
        } else {
            for (GodOfWarMagic m : GodOfWarMagic.values()) {
                if (ClientMagicState.isUnlocked(m)) {
                    this.selectedMagic = m;
                    break;
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = this.leftPos;
        int y = this.topPos;
        Player player = this.minecraft != null ? this.minecraft.player : null;
        boolean creative = player != null && player.getAbilities().instabuild;
        int playerBlood = ClientStunState.getPlayerBloodOrbs();

        // 1. FONDO A PANTALLA COMPLETA (mismo estilo God of War II independiente)
        graphics.fill(0, 0, this.width, this.height, 0xFF060A0B);
        if (x > 0) {
            graphics.fill(0, 0, x + 6, this.height, 0xFF151417);
        }

        // 2. PANEL IZQUIERDO HD CON RUEDA ESPARTANA Y 3 MEDALLONES DE MAGIA (438x648 -> 180x236)
        graphics.blit(WHEEL_TEX, x, y, 180, 236, 0.0F, 0.0F, 438, 648, 438, 648);

        // 3. MARCO GRIEGO PERIMETRAL A PANTALLA COMPLETA (idéntico al Menú POTENCIADOR!)
        int borderH = 10;
        int borderW = 11;
        for (int bx = 0; bx < this.width; bx += 92) {
            int segW = Math.min(92, this.width - bx);
            int srcSegW = Math.max(1, (int) (260.0F * (segW / 92.0F)));
            graphics.blit(UPGRADE_ATLAS, bx, 0, segW, borderH, 660.0F, 140.0F, srcSegW, 28, ATLAS, ATLAS);
            graphics.blit(UPGRADE_ATLAS, bx, this.height - borderH, segW, borderH, 660.0F, 140.0F, srcSegW, 28, ATLAS, ATLAS);
        }
        for (int by = 0; by < this.height; by += 92) {
            int segH = Math.min(92, this.height - by);
            int srcSegH = Math.max(1, (int) (260.0F * (segH / 92.0F)));
            graphics.blit(UPGRADE_ATLAS, 0, by, borderW, segH, 660.0F, 180.0F, 32, srcSegH, ATLAS, ATLAS);
            graphics.blit(UPGRADE_ATLAS, this.width - borderW, by, borderW, segH, 660.0F, 180.0F, 32, srcSegH, ATLAS, ATLAS);
        }

        // 4. CALAVERAS ESPARTANAS EN LAS ESQUINAS + EMBLEMA Ω SUPERIOR CENTRAL
        int skullSz = 22;
        graphics.blit(UPGRADE_ATLAS, 0, 0, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        graphics.blit(UPGRADE_ATLAS, this.width - skullSz, 0, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        graphics.blit(UPGRADE_ATLAS, this.width - skullSz, this.height - skullSz, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        if (x >= skullSz) {
            graphics.blit(UPGRADE_ATLAS, 0, this.height - skullSz, skullSz, skullSz, 500.0F, 140.0F, 60, 60, ATLAS, ATLAS);
        }
        graphics.blit(UPGRADE_ATLAS, this.width / 2 - 11, 0, 22, 22, 580.0F, 140.0F, 56, 56, ATLAS, ATLAS);

        // 5. ESTANDARTE ROJO-ORO SUPERIOR DERECHO ("MAGIAS") + PESTAÑA DIRECTA A POTENCIADOR [K]
        graphics.blit(UPGRADE_ATLAS, x + 224, y + 4, 136, 68, 500.0F, 0.0F, 232, 119, ATLAS, ATLAS);
        graphics.drawCenteredString(this.font, "MAGIAS", x + 292, y + 34, 0xFFFFE594);

        boolean hoverTab = isInside(mouseX, mouseY, x + TAB_X, y + TAB_Y, TAB_W, TAB_H);
        graphics.fill(x + TAB_X - 1, y + TAB_Y - 1, x + TAB_X + TAB_W + 1, y + TAB_Y + TAB_H + 1, hoverTab ? 0xFFFFF0A6 : 0xFFB8862D);
        graphics.fill(x + TAB_X, y + TAB_Y, x + TAB_X + TAB_W, y + TAB_Y + TAB_H, hoverTab ? 0xFF4F1714 : 0xFF210F0E);
        graphics.drawCenteredString(this.font, "ARMAS", x + TAB_X + TAB_W / 2, y + TAB_Y + 4, 0xFFFFDF80);

        // 6. LOS 3 SLOTS DE MAGIA ACTIVOS EN LA RUEDA IZQUIERDA (I, II, III)
        String[] roman = { "I", "II", "III" };
        for (int i = 0; i < 3; i++) {
            int sx = x + SLOT_CENTERS[i][0];
            int sy = y + SLOT_CENTERS[i][1];
            boolean isTarget = (this.selectedSlot == i);
            boolean isActiveInGame = (ClientMagicState.getActiveSlot() == i);
            boolean hoverSlot = Math.hypot(mouseX - sx, mouseY - sy) <= SLOT_RADIUS + 2;

            // Resplandor dorado si está seleccionado o activo
            if (isTarget || hoverSlot) {
                int ringCol = isTarget ? 0xFFFFEA70 : 0x99D9A640;
                graphics.fill(sx - 15, sy - 15, sx + 15, sy - 13, ringCol);
                graphics.fill(sx - 15, sy + 13, sx + 15, sy + 15, ringCol);
                graphics.fill(sx - 15, sy - 13, sx - 13, sy + 13, ringCol);
                graphics.fill(sx + 13, sy - 13, sx + 15, sy + 13, ringCol);
            }

            GodOfWarMagic eq = ClientMagicState.getEquipped(i);
            if (eq != null) {
                graphics.blit(eq.iconTexture, sx - 12, sy - 12, 24, 24, 0.0F, 0.0F, 32, 32, 32, 32);
            } else {
                graphics.drawCenteredString(this.font, roman[i], sx, sy - 4, 0xFF5A6D8A);
            }

            // Etiqueta compacta del slot (I / II / III) a la derecha del medallón
            int tagX = sx + 16;
            int tagY = sy - 5;
            String tag = isActiveInGame ? ("★" + roman[i]) : roman[i];
            graphics.drawString(this.font, tag, tagX, tagY, isTarget ? 0xFFFFEB7A : 0xFFC7B28A, true);
        }

        // 7. BARRA AZUL DE MANÁ (justo debajo del Slot III, en x+55, y+137, 60x6)
        float manaRatio = ClientMagicState.getMana() / GodOfWarMagicData.MAX_MANA;
        int manaW = (int) (60 * Math.max(0.0F, Math.min(1.0F, manaRatio)));
        if (manaW > 0) {
            graphics.fill(x + 55, y + 137, x + 55 + manaW, y + 143, 0xFF1A8CFF);
            graphics.fill(x + 55, y + 137, x + 55 + manaW, y + 139, 0xFF99EFFF);
        }

        // 8. PANEL INFERIOR IZQUIERDO: INFORMACIÓN DETALLADA DE LA MAGIA SELECCIONADA
        renderSelectedMagicInfo(graphics, x, y, playerBlood);

        // 9. CUADRÍCULA PANTHEON 6×4 CON LAS 24 MAGIAS CANÓNICAS DE LA WIKI
        GodOfWarMagic[] all = GodOfWarMagic.values();
        GodOfWarMagic hoveredMagic = null;
        for (int idx = 0; idx < all.length; idx++) {
            GodOfWarMagic m = all[idx];
            int col = idx % GRID_COLS;
            int row = idx / GRID_COLS;
            int cx = x + GRID_X + col * CELL_W;
            int cy = y + GRID_Y + row * CELL_H;
            boolean unlocked = ClientMagicState.isUnlocked(m);
            boolean isSel = (this.selectedMagic == m);
            boolean hover = isInside(mouseX, mouseY, cx, cy, CELL_W - 2, CELL_H - 2);
            if (hover) hoveredMagic = m;

            int frameCol = isSel ? 0xFFFFE570 : (hover ? 0xFFD9A640 : (unlocked ? 0xFF7D5C28 : 0xFF2B2524));
            int bgCol = isSel ? 0xFF2E1E12 : (unlocked ? 0xFF141820 : 0xFF0C0E12);

            graphics.fill(cx, cy, cx + CELL_W - 2, cy + CELL_H - 2, frameCol);
            graphics.fill(cx + 1, cy + 1, cx + CELL_W - 3, cy + CELL_H - 3, bgCol);

            if (unlocked) {
                graphics.blit(m.iconTexture, cx + 5, cy + 2, 22, 22, 0.0F, 0.0F, 32, 32, 32, 32);
                // Insignia de slot equipado (I / II / III) en esquina superior izquierda
                int eqSlot = ClientMagicState.findEquippedSlot(m);
                if (eqSlot >= 0) {
                    graphics.fill(cx + 1, cy + 1, cx + 11, cy + 10, 0xDD7A1412);
                    graphics.drawString(this.font, roman[eqSlot], cx + 3, cy + 2, 0xFFFFEA70, false);
                }
                // Nivel (1..5) en esquina inferior derecha
                int lv = ClientMagicState.getLevel(m);
                graphics.drawString(this.font, String.valueOf(lv), cx + CELL_W - 9, cy + CELL_H - 11,
                    lv >= GodOfWarMagic.MAX_LEVEL ? 0xFFFFD84D : 0xFFD9F2FF, true);
            } else {
                // Silueta oscura bloqueada con símbolo rúnico "?"
                graphics.fill(cx + 6, cy + 4, cx + 26, cy + 22, 0xFF151921);
                graphics.drawCenteredString(this.font, "?", cx + 16, cy + 9, 0xFF474E5E);
            }
        }

        // Contador X/24 magias desbloqueadas sobre la cuadrícula
        String prog = "DESBLOQUEADAS: " + ClientMagicState.getUnlockedCount() + " / " + all.length;
        graphics.drawString(this.font, prog, x + GRID_X, y + 66, 0xFFD9C7A3, true);

        // 10. BOTONES DE ACCIÓN: EQUIPAR EN SLOT X + POTENCIAR CON SANGRE
        boolean selUnlocked = ClientMagicState.isUnlocked(this.selectedMagic);
        int curLvl = ClientMagicState.getLevel(this.selectedMagic);
        boolean isMax = curLvl >= GodOfWarMagic.MAX_LEVEL;
        int bloodCost = this.selectedMagic.getBloodUpgradeCost(curLvl);
        boolean canEquip = selUnlocked;
        boolean canUpgrade = selUnlocked && !isMax && (creative || playerBlood >= bloodCost);

        // Botón EQUIPAR EN SLOT I / II / III
        drawGowButton(graphics, x + EQUIP_X, y + EQUIP_Y, EQUIP_W, EQUIP_H,
            canEquip, isInside(mouseX, mouseY, x + EQUIP_X, y + EQUIP_Y, EQUIP_W, EQUIP_H),
            "EQUIPAR SLOT " + roman[this.selectedSlot],
            canEquip ? 0xFF7AE8FF : 0xFF6B625A);

        // Botón POTENCIAR (Niv. 1 -> 5 con Orbes de Sangre)
        String upgTxt = !selUnlocked ? "BLOQUEADA"
            : (isMax ? "NIVEL MÁX." : (creative ? "POTENCIAR" : "SUBIR (" + bloodCost + ")"));
        drawGowButton(graphics, x + UPG_X, y + UPG_Y, UPG_W, UPG_H,
            canUpgrade, isInside(mouseX, mouseY, x + UPG_X, y + UPG_Y, UPG_W, UPG_H),
            upgTxt,
            canUpgrade ? 0xFFFF5244 : (isMax && selUnlocked ? 0xFFFFD84D : 0xFF6B625A));

        // Tooltip limpio al pasar el ratón por cualquier celda de la cuadrícula
        if (hoveredMagic != null) {
            boolean hu = ClientMagicState.isUnlocked(hoveredMagic);
            String tip = hu
                ? (hoveredMagic.displayName + " (Niv. " + ClientMagicState.getLevel(hoveredMagic) + "/5 — " + hoveredMagic.gameOrigin + ")")
                : (hoveredMagic.displayName + " [" + hoveredMagic.gameOrigin + "] — Bloqueada (Consume su Reliquia)");
            graphics.renderTooltip(this.font, Component.literal(tip), mouseX, mouseY);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderSelectedMagicInfo(GuiGraphics graphics, int x, int y, int playerBlood) {
        int ix = x + 12;
        int iy = y + 157;
        int ic = x + 90;
        GodOfWarMagic m = this.selectedMagic;
        boolean unlocked = ClientMagicState.isUnlocked(m);

        String title = m.displayName.toUpperCase();
        if (title.length() > 21) title = title.substring(0, 20) + "…";
        graphics.drawCenteredString(this.font, title, ic, iy, unlocked ? m.accentColor : 0xFF7D7872);

        String sub = m.gameOrigin + (unlocked ? (" • NIV " + ClientMagicState.getLevel(m) + "/5") : " • BLOQUEADA");
        graphics.drawCenteredString(this.font, sub, ic, iy + 11, unlocked ? 0xFFFFE082 : 0xFF94443E);

        if (unlocked) {
            int lv = ClientMagicState.getLevel(m);
            graphics.drawCenteredString(this.font, m.descLine1, ic, iy + 23, 0xFFEFE6D0);
            graphics.drawCenteredString(this.font, m.descLine2, ic, iy + 33, 0xFFBFB59E);
            String stats = "Maná: " + m.getEffectiveManaCost(lv) + "  |  CD: "
                + String.format("%.1fs", m.getEffectiveCooldown(lv) / 20.0F);
            graphics.drawCenteredString(this.font, stats, ic, iy + 45, 0xFF6BE0FF);
        } else {
            graphics.drawCenteredString(this.font, "Consume la Reliquia de", ic, iy + 25, 0xFFBFB59E);
            graphics.drawCenteredString(this.font, m.displayName, ic, iy + 36, 0xFFFFC966);
            graphics.drawCenteredString(this.font, "para añadirla a tu Grimorio.", ic, iy + 47, 0xFF9E9484);
        }

        // Pie: Orbes de Sangre + teclas rápidas
        String bloodStr = String.format("%,d", playerBlood);
        graphics.blit(UPGRADE_ATLAS, ix + 2, iy + 57, 9, 9, 498.0F, 215.0F, 50, 50, ATLAS, ATLAS);
        graphics.drawString(this.font, bloodStr, ix + 13, iy + 58, 0xFFFF5C4D, true);

        String castKey = GodOfWarKeybinds.KEY_CURIO_POWER.getTranslatedKeyMessage().getString().toUpperCase();
        String cycKey = GodOfWarKeybinds.KEY_CYCLE_MAGIC.getTranslatedKeyMessage().getString().toUpperCase();
        String keys = "[" + castKey + "] Usar  [" + cycKey + "] Slot";
        graphics.drawString(this.font, keys, x + 168 - this.font.width(keys), iy + 58, 0xFFD9C7A3, true);
    }

    private void drawGowButton(GuiGraphics g, int bx, int by, int bw, int bh,
                               boolean active, boolean hover, String label, int textColor) {
        int outer = active ? (hover ? 0xFFFFF2A6 : 0xFFD9A640) : 0xFF5A452C;
        int inner = active ? 0xFF8C5B1A : 0xFF33261B;
        int bg    = active ? (hover ? 0xFF4F1714 : 0xFF2B1211) : 0xFF1A1616;
        int glow  = active ? (hover ? 0xFFCC2E22 : 0xFF8F1B14) : 0xFF241D1C;

        g.fill(bx - 1, by - 1, bx + bw + 1, by + bh + 1, outer);
        g.fill(bx, by, bx + bw, by + bh, inner);
        g.fill(bx + 2, by + 2, bx + bw - 2, by + bh - 2, bg);
        g.fill(bx + 3, by + bh - 5, bx + bw - 3, by + bh - 2, glow);
        g.drawCenteredString(this.font, label, bx + bw / 2, by + 9, textColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = this.leftPos;
        int y = this.topPos;

        // 1. Pestaña directa al Menú POTENCIADOR [K]
        if (button == 0 && isInside(mouseX, mouseY, x + TAB_X, y + TAB_Y, TAB_W, TAB_H)) {
            playClick();
            this.onClose();
            GodOfWarNetwork.sendOpenUpgradeMenu();
            return true;
        }

        // 2. Clic en cualquiera de los 3 Slots de la rueda izquierda
        for (int i = 0; i < 3; i++) {
            int sx = x + SLOT_CENTERS[i][0];
            int sy = y + SLOT_CENTERS[i][1];
            if (Math.hypot(mouseX - sx, mouseY - sy) <= SLOT_RADIUS + 3) {
                playClick();
                if (button == 1) {
                    // Clic derecho: vaciar slot
                    GodOfWarNetwork.sendEquipMagicSlot(i, -1);
                } else {
                    this.selectedSlot = i;
                    GodOfWarMagic eq = ClientMagicState.getEquipped(i);
                    if (eq != null) {
                        this.selectedMagic = eq;
                    }
                }
                return true;
            }
        }

        // 3. Clic en la cuadrícula 6x4 de las 24 Magias (doble clic equipa inmediatamente!)
        GodOfWarMagic[] all = GodOfWarMagic.values();
        for (int idx = 0; idx < all.length; idx++) {
            int col = idx % GRID_COLS;
            int row = idx / GRID_COLS;
            int cx = x + GRID_X + col * CELL_W;
            int cy = y + GRID_Y + row * CELL_H;
            if (isInside(mouseX, mouseY, cx, cy, CELL_W - 2, CELL_H - 2)) {
                GodOfWarMagic clicked = all[idx];
                this.selectedMagic = clicked;
                playClick();

                long now = System.currentTimeMillis();
                if (button == 0 && ClientMagicState.isUnlocked(clicked)
                    && this.lastClickedMagic == clicked && (now - this.lastClickMs) < 350L) {
                    GodOfWarNetwork.sendEquipMagicSlot(this.selectedSlot, clicked.ordinal());
                }
                this.lastClickMs = now;
                this.lastClickedMagic = clicked;
                return true;
            }
        }

        // 4. Botón EQUIPAR EN SLOT
        if (button == 0 && isInside(mouseX, mouseY, x + EQUIP_X, y + EQUIP_Y, EQUIP_W, EQUIP_H)) {
            if (ClientMagicState.isUnlocked(this.selectedMagic)) {
                playClick();
                GodOfWarNetwork.sendEquipMagicSlot(this.selectedSlot, this.selectedMagic.ordinal());
            }
            return true;
        }

        // 5. Botón POTENCIAR CON ORBES DE SANGRE
        if (button == 0 && isInside(mouseX, mouseY, x + UPG_X, y + UPG_Y, UPG_W, UPG_H)) {
            if (ClientMagicState.isUnlocked(this.selectedMagic)) {
                playClick();
                GodOfWarNetwork.sendUpgradeMagic(this.selectedMagic.ordinal());
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (GodOfWarKeybinds.KEY_MAGIC_MENU.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void playClick() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.85F, 1.1F);
        }
    }

    private static boolean isInside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
