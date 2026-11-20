package com.spartanforge.godofwar.client;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.compat.EpicFightCompat;
import com.spartanforge.godofwar.item.GodOfWarWeaponAbility;
import com.spartanforge.godofwar.magic.GodOfWarMagic;
import com.spartanforge.godofwar.magic.GodOfWarMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

/**
 * HUD estilo God of War:
 * 1. Barra de Aturdimiento (Stun Meter) del enemigo más cercano en combate o bajo la mira.
 * 2. Aviso pulsante "[V] ¡EJECUCIÓN BRUTAL! (R3)" cuando un enemigo está listo para rematar.
 * 3. Destello espartano carmesí/dorado y banner cinemático al ejecutar un remate o Parry Dorado.
 * 4. Indicador discreto de tecla [G]/[Shift+G] para habilidades en Modo Batalla de Epic Fight.
 */
public final class GodOfWarClientHud {
    private static final ResourceLocation GUI_ATLAS =
        new ResourceLocation(GodOfWarMod.MOD_ID, "textures/gui/upgrade_menu.png");

    private GodOfWarClientHud() {}

    public static void render(GuiGraphics graphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null || mc.options.hideGui) {
            return;
        }

        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        Font font = mc.font;

        // 1. Destello carmesí-dorado tras Ejecución Brutal
        int flashTicks = ClientStunState.getExecutionFlashTicks();
        if (flashTicks > 0) {
            int alpha = Math.min(115, flashTicks * 8);
            int edgeColor = ClientStunState.wasExecutionBoss()
                ? ((alpha << 24) | 0xD49B1A)
                : ((alpha << 24) | 0xB81818);
            int border = 14;
            graphics.fill(0, 0, screenW, border, edgeColor);
            graphics.fill(0, screenH - border, screenW, screenH, edgeColor);
            graphics.fill(0, border, border, screenH - border, edgeColor);
            graphics.fill(screenW - border, border, screenW, screenH - border, edgeColor);
        }

        // 2. Banner cinemático de Ejecución Brutal
        int bannerTicks = ClientStunState.getExecutionBannerTicks();
        if (bannerTicks > 0 && !ClientStunState.getExecutionBannerText().isEmpty()) {
            String text = ClientStunState.getExecutionBannerText();
            int alpha = bannerTicks < 10 ? (int) (255 * (bannerTicks / 10.0F)) : 255;
            int rgb = ClientStunState.wasExecutionBoss() ? 0xFFD84D : 0xFF453A;
            int color = (Math.max(16, alpha) << 24) | rgb;
            int textW = font.width(text);
            int bx = (screenW - textW) / 2;
            int by = (int) (screenH * 0.23F);
            graphics.fill(bx - 8, by - 4, bx + textW + 8, by + 13, (Math.max(12, alpha / 2) << 24) | 0x100808);
            graphics.drawString(font, text, bx, by, color, true);
        }

        // 3. Buscar objetivo principal para mostrar Barra de Stun + Aviso [V] Ejecutar
        LivingEntity focusTarget = findFocusStunEntity(mc, player);
        if (focusTarget != null) {
            ClientStunState.StunSnapshot snap = ClientStunState.getStun(focusTarget.getId());
            if (snap != null) {
                int barW = 144;
                int barH = 8;
                int x = (screenW - barW) / 2;
                int y = (int) (screenH * 0.62F);

                String targetName = focusTarget.getDisplayName().getString();
                if (targetName.length() > 26) {
                    targetName = targetName.substring(0, 26) + "...";
                }
                String header = snap.stunned() ? ("⚡ " + targetName + " — ATURDIDO ⚡") : (targetName + " — Aturdimiento");
                int headerColor = snap.stunned() ? 0xFFFFD43B : 0xFFE8C87A;
                graphics.drawCenteredString(font, header, screenW / 2, y - 11, headerColor);

                // Marco de hierro oscuro + relleno dorado/carmesí
                graphics.fill(x - 2, y - 2, x + barW + 2, y + barH + 2, 0xCC120D0A);
                graphics.fill(x, y, x + barW, y + barH, 0xCC2E2219);
                int fillW = (int) (barW * snap.progress());
                if (fillW > 0) {
                    boolean pulse = ((System.currentTimeMillis() / 140L) % 2L) == 0L;
                    int fillColor = snap.stunned()
                        ? (pulse ? 0xFFFF3B2F : 0xFFFFC107)
                        : 0xFFE5A024;
                    graphics.fill(x, y, x + fillW, y + barH, fillColor);
                }

                // Aviso pulsante [V] R3 EJECUTAR si está al alcance
                double dist = player.distanceTo(focusTarget);
                if (snap.stunned() && dist <= 7.5D) {
                    boolean pulse = ((System.currentTimeMillis() / 160L) % 2L) == 0L;
                    String keyName = GodOfWarKeybinds.KEY_EXECUTE.getTranslatedKeyMessage().getString().toUpperCase();
                    String prompt = "⚔ [" + keyName + "] ¡EJECUCIÓN BRUTAL! (R3) ⚔";
                    int pw = font.width(prompt);
                    int px = (screenW - pw) / 2;
                    int py = y + 13;
                    graphics.fill(px - 6, py - 2, px + pw + 6, py + 11, pulse ? 0xDD7A0C0C : 0xCC3D1608);
                    graphics.drawString(font, prompt, px, py, pulse ? 0xFFFFEA70 : 0xFFFFFFFF, true);
                }
            }
        }

        // 4. Guía discreta de teclas de combate, Potenciador [K] y Poder Curios [X]
        boolean holdingGowWeapon = player.getMainHandItem().getItem() instanceof GodOfWarWeaponAbility
            || player.getOffhandItem().getItem() instanceof GodOfWarWeaponAbility;
        if (holdingGowWeapon) {
            String abKey = GodOfWarKeybinds.KEY_WEAPON_ABILITY.getTranslatedKeyMessage().getString().toUpperCase();
            String exKey = GodOfWarKeybinds.KEY_EXECUTE.getTranslatedKeyMessage().getString().toUpperCase();
            String upKey = GodOfWarKeybinds.KEY_UPGRADE_MENU.getTranslatedKeyMessage().getString().toUpperCase();
            String pwKey = GodOfWarKeybinds.KEY_CURIO_POWER.getTranslatedKeyMessage().getString().toUpperCase();
            String hint = EpicFightCompat.isLoaded()
                ? ("⚔ Epic Fight | [" + abKey + "] Hab. [" + exKey + "] Ejec. [" + pwKey + "] Poder [" + upKey + "] Mejorar")
                : ("⚔ [" + abKey + "] Habilidad [" + exKey + "] Ejecutar [" + pwKey + "] Curio [" + upKey + "] Potenciador");
            int hw = font.width(hint);
            int hx = screenW - hw - 8;
            int hy = screenH - 16;
            graphics.fill(hx - 4, hy - 2, hx + hw + 4, hy + 10, 0x880B0B10);
            graphics.drawString(font, hint, hx, hy, 0xFFD9C9A3, true);
        }

        // 5. Contador de Orbes de Sangre (Red Orbs) estilo God of War en la esquina superior izquierda
        int blood = ClientStunState.getPlayerBloodOrbs();
        int deltaTicks = ClientStunState.getRecentBloodTicks();
        if (holdingGowWeapon || blood > 0 || deltaTicks > 0) {
            String bloodStr = String.format("%,d", blood);
            int boxW = 24 + font.width(bloodStr);
            int bx = 8;
            int by = 8;
            graphics.fill(bx - 1, by - 1, bx + boxW + 1, by + 16, 0xCC7A561E);
            graphics.fill(bx, by, bx + boxW, by + 15, 0xBB140707);
            // Sprite HD brillante del Orbe Rojo de Sangre (atlas 1024x1024, UV 498, 215, 50x50)
            graphics.blit(GUI_ATLAS, bx + 3, by + 2, 11, 11, 498.0F, 215.0F, 50, 50, 1024, 1024);
            graphics.drawString(font, bloodStr, bx + 18, by + 4, 0xFFFF5C4D, true);

            if (deltaTicks > 0 && ClientStunState.getRecentBloodDelta() != 0) {
                int d = ClientStunState.getRecentBloodDelta();
                String pop = (d > 0 ? "+" : "") + String.format("%,d", d);
                int popColor = d > 0 ? 0xFFFFD84D : 0xFFFF4D4D;
                graphics.drawString(font, pop, bx + boxW + 5, by + 4, popColor, true);
            }
        }

        // 6. HUD de Magias God of War (Barra Azul de Maná + 3 Slots Activos I/II/III) debajo de los Orbes de Sangre
        if (ClientMagicState.hasAnyUnlocked()) {
            int mx = 8;
            int my = 28;
            int manaBarW = 76;
            int manaBarH = 6;
            float manaRatio = ClientMagicState.getMana() / GodOfWarMagicData.MAX_MANA;
            int fillW = (int) (manaBarW * Math.max(0.0F, Math.min(1.0F, manaRatio)));

            // Barra Azul de Maná estilo GoW
            graphics.fill(mx - 1, my - 1, mx + manaBarW + 1, my + manaBarH + 1, 0xCC9E7428);
            graphics.fill(mx, my, mx + manaBarW, my + manaBarH, 0xCC0A1320);
            if (fillW > 0) {
                graphics.fill(mx, my, mx + fillW, my + manaBarH, 0xFF1888FF);
                graphics.fill(mx, my, mx + fillW, my + 2, 0xFF99F0FF);
            }

            // Los 3 Slots Activos compactos (I, II, III)
            int sy = my + 10;
            int active = ClientMagicState.getActiveSlot();
            String[] roman = { "1", "2", "3" };
            for (int i = 0; i < 3; i++) {
                int sx = mx + i * 26;
                boolean isAct = (i == active);
                int border = isAct ? 0xFFFFEA70 : 0xAA785C28;
                int bg = isAct ? 0xCC1B2838 : 0xAA0C1018;
                graphics.fill(sx - 1, sy - 1, sx + 23, sy + 23, border);
                graphics.fill(sx, sy, sx + 22, sy + 22, bg);

                GodOfWarMagic eq = ClientMagicState.getEquipped(i);
                if (eq != null) {
                    graphics.blit(eq.iconTexture, sx + 2, sy + 2, 18, 18, 0.0F, 0.0F, 32, 32, 32, 32);
                    int cd = ClientMagicState.getSlotCooldownTicks(i);
                    if (cd > 0) {
                        int maxCd = Math.max(1, eq.getEffectiveCooldown(ClientMagicState.getLevel(eq)));
                        int shadeH = Math.min(18, Math.max(1, Math.round(18.0F * (cd / (float) maxCd))));
                        graphics.fill(sx + 2, sy + 20 - shadeH, sx + 20, sy + 20, 0xAA090C14);
                    }
                } else {
                    graphics.drawCenteredString(font, roman[i], sx + 11, sy + 7, 0xFF4D596B);
                }
            }

            GodOfWarMagic activeMagic = ClientMagicState.getEquipped(active);
            String castKey = GodOfWarKeybinds.KEY_CURIO_POWER.getTranslatedKeyMessage().getString().toUpperCase();
            String cycKey  = GodOfWarKeybinds.KEY_CYCLE_MAGIC.getTranslatedKeyMessage().getString().toUpperCase();
            String menuKey = GodOfWarKeybinds.KEY_MAGIC_MENU.getTranslatedKeyMessage().getString().toUpperCase();
            String label = activeMagic != null
                ? ("[" + castKey + "] " + activeMagic.displayName + " | [" + cycKey + "] [" + menuKey + "]")
                : ("[" + menuKey + "] Abrir Menú de Magias");
            graphics.drawString(font, label, mx, sy + 26, 0xFF9CE8FF, true);
        }
    }

    private static LivingEntity findFocusStunEntity(Minecraft mc, Player player) {
        // Prioridad 1: enemigo aturdido al alcance inmediato
        AABB near = player.getBoundingBox().inflate(10.0D);
        List<LivingEntity> nearby = mc.level.getEntitiesOfClass(LivingEntity.class, near,
            e -> e != player && e.isAlive());

        LivingEntity closestStunned = null;
        double closestStunnedDist = Double.MAX_VALUE;
        for (LivingEntity e : nearby) {
            ClientStunState.StunSnapshot snap = ClientStunState.getStun(e.getId());
            if (snap != null && snap.stunned()) {
                double d = player.distanceToSqr(e);
                if (d < closestStunnedDist) {
                    closestStunnedDist = d;
                    closestStunned = e;
                }
            }
        }
        if (closestStunned != null) {
            return closestStunned;
        }

        // Prioridad 2: entidad bajo el cursor del jugador
        if (mc.hitResult instanceof EntityHitResult ehr) {
            Entity hit = ehr.getEntity();
            if (hit instanceof LivingEntity living && ClientStunState.getStun(living.getId()) != null) {
                return living;
            }
        }

        // Prioridad 3: enemigo más cercano con barra de Stun activa
        LivingEntity bestActive = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity e : nearby) {
            ClientStunState.StunSnapshot snap = ClientStunState.getStun(e.getId());
            if (snap != null && snap.stun() > 0.01F) {
                double d = player.distanceToSqr(e);
                if (d < bestDist) {
                    bestDist = d;
                    bestActive = e;
                }
            }
        }
        return bestActive;
    }
}
