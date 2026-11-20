package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.compat.EpicFightCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Utilidad compartida para el sistema de mejora estilo God of War (Niveles 1 a 5),
 * costos de Experiencia (Orbes XP), textos de desbloqueo del menú POTENCIADOR [K],
 * evolución visual 3D y tooltips temáticos.
 */
public final class WeaponUpgradeHelper {
    public static final String TAG_FORGE_LEVEL = "GowForgeLevel";
    /** 0 = Nivel 1 (Base), 1 = Nivel 2, 2 = Nivel 3, 3 = Nivel 4, 4 = Nivel 5 (MÁX). */
    public static final int MAX_LEVEL = 4;
    public static final int DISPLAY_MAX_LEVEL = 5;

    private WeaponUpgradeHelper() {}

    public static int getLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? Math.min(MAX_LEVEL, Math.max(0, tag.getInt(TAG_FORGE_LEVEL))) : 0;
    }

    /** Nivel visible estilo God of War: de 1 (base) hasta 5 (Máximo). */
    public static int getDisplayLevel(ItemStack stack) {
        return getLevel(stack) + 1;
    }

    public static boolean isMaxLevel(ItemStack stack) {
        return getLevel(stack) >= MAX_LEVEL;
    }

    public static void setLevel(ItemStack stack, int level) {
        stack.getOrCreateTag().putInt(TAG_FORGE_LEVEL, Math.min(MAX_LEVEL, Math.max(0, level)));
    }

    /** Valor normalizado 0.0 .. 1.0 usado por ItemProperties para cambiar el modelo 3D de las Espadas. */
    public static float getModelStage(ItemStack stack) {
        return getLevel(stack) / (float) MAX_LEVEL;
    }

    /** Costo en Orbes de Sangre (Red Orbs) para subir del nivel actual al siguiente. */
    public static int getBloodCost(ItemStack stack) {
        return switch (getLevel(stack)) {
            case 0 -> 500;   // Nivel 1 -> Nivel 2
            case 1 -> 1500;  // Nivel 2 -> Nivel 3
            case 2 -> 3500;  // Nivel 3 -> Nivel 4
            case 3 -> 7000;  // Nivel 4 -> Nivel 5 (FORMA FINAL)
            default -> 0;
        };
    }

    /** Compatibilidad heredada si algo consulta getXpLevelCost. */
    public static int getXpLevelCost(ItemStack stack) {
        return getBloodCost(stack);
    }

    /** Bono de daño progresivo según el nivel (Nivel 1: +0, Niv 2: +3, Niv 3: +6, Niv 4: +9, Niv 5: +13). */
    public static float getBonusDamage(ItemStack stack) {
        int lvl = getLevel(stack);
        return lvl >= 4 ? 13.0F : lvl * 3.0F;
    }

    /** Multiplicador de potencia para Poderes Divinos y Reliquias (1.0x hasta 1.8x). */
    public static float getPowerMultiplier(ItemStack stack) {
        return 1.0F + getLevel(stack) * 0.20F;
    }

    public static boolean isUpgradeableWeapon(Item item) {
        return item instanceof BladesOfChaosItem
            || item instanceof BladesOfAthenaItem
            || item instanceof LeviathanAxeItem
            || item instanceof BladeOfOlympusItem
            || item instanceof DraupnirSpearItem
            || item instanceof NemeanCestusItem
            || item instanceof ClawsOfHadesItem
            || item instanceof GuardianShieldItem
            || item instanceof SpartanRagePowerItem
            || item instanceof PoseidonsRagePowerItem
            || item instanceof ArmyOfHadesPowerItem
            || item instanceof MedusaHeadPowerItem
            || item instanceof GoldenFleeceItem
            || item instanceof WingsOfIcarusItem
            || item instanceof AmuletOfOuroborosItem;
    }

    public static String toRoman(int displayLevel) {
        return switch (displayLevel) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> "I";
        };
    }

    /** Descripción estilo God of War II ("SIGUIENTE MEJORA:") mostrada en el menú POTENCIADOR. */
    public static List<String> getNextUpgradeDescription(ItemStack stack) {
        if (stack.isEmpty() || !isUpgradeableWeapon(stack.getItem())) {
            return List.of("Coloca un arma, poder", "o reliquia en el", "medallón dorado.");
        }
        int next = getDisplayLevel(stack) + 1;
        if (next > DISPLAY_MAX_LEVEL) {
            return List.of("PODER MÁXIMO ALCANZADO", "Forma Divina Completa", "(Nivel 5 / 5)");
        }

        Item item = stack.getItem();
        if (item instanceof BladesOfChaosItem) {
            return switch (next) {
                case 2 -> List.of("Evolución Visual Fase 2", "Filo de Bronce Ígneo", "+3 Daño y +Alcance");
                case 3 -> List.of("Evolución Visual Fase 3", "Runas de Magma Activas", "+6 Daño y +Ciclón");
                case 4 -> List.of("Evolución Visual Fase 4", "Cadenas Volcánicas", "Ciclón Explosivo Lava");
                default -> List.of("FORMA FINAL DE ARES", "Llama Infernal Máxima", "+13 Daño y Ciclón Total");
            };
        }
        if (item instanceof BladesOfAthenaItem) {
            return switch (next) {
                case 2 -> List.of("Evolución Visual Fase 2", "Filigrana de Atenea", "+3 Daño y +Velocidad");
                case 3 -> List.of("Evolución Visual Fase 3", "Runas Celestes Activas", "Ciclón invoca Rayos");
                case 4 -> List.of("Evolución Visual Fase 4", "Látigo Encadena Luz", "+9 Daño Olímpico");
                default -> List.of("FORMA FINAL DE ATENEA", "Oro Celestial Divino", "Juicio de Atenea Máx.");
            };
        }
        if (item instanceof LeviathanAxeItem) {
            return switch (next) {
                case 2 -> List.of("Filo de Escarcha II", "+3 Daño de Hielo", "Lanzamiento más letal");
                case 3 -> List.of("Runas de Brok y Sindri", "+6 Daño y +Stun", "Congelación extendida");
                case 4 -> List.of("Ira de Fimbulwinter", "+9 Daño Glacial", "Impacto explosivo");
                default -> List.of("HACHA LEVIATÁN MÁX.", "Escarcha Primordial", "+13 Daño Divino");
            };
        }
        if (item instanceof BladeOfOlympusItem) {
            return List.of("Potencia Olímpica Niv. " + next, "Onda Celestial y Juicio", "+" + (next == 5 ? 13 : (next - 1) * 3) + " Daño Divino");
        }
        if (item instanceof DraupnirSpearItem) {
            return List.of("Viento de Draupnir Niv. " + next, "Detonación más letal", "+" + (next == 5 ? 13 : (next - 1) * 3) + " Daño y +Stun");
        }
        if (item instanceof NemeanCestusItem) {
            return List.of("Fuerza de Nemea Niv. " + next, "Rugido Sísmico Mayor", "+" + (next == 5 ? 13 : (next - 1) * 3) + " Daño y +Stun");
        }
        if (item instanceof ClawsOfHadesItem) {
            return List.of("Almas de Hades Niv. " + next, "Invocar Almas más Letales", "+" + (next == 5 ? 13 : (next - 1) * 3) + " Daño y Robo Vida");
        }
        if (item instanceof GuardianShieldItem) {
            return List.of("Escudo de Faye Niv. " + next, "Parry y Golpe Mayor", "+" + (next == 5 ? 13 : (next - 1) * 3) + " Daño de Impacto");
        }
        if (item instanceof GoldenFleeceItem) {
            int pct = 12 + (next - 1) * 5;
            return List.of("Vellocino Nivel " + next, "Mitiga " + pct + "% del daño", "Contraataque más fuerte");
        }
        if (item instanceof WingsOfIcarusItem) {
            return List.of("Alas de Ícaro Nivel " + next, "Planeo Dorado Mejorado", next >= 3 ? "Salto Potenciado II" : "Impulso Aéreo");
        }
        if (item instanceof AmuletOfOuroborosItem) {
            return List.of("Uróboros Nivel " + next, "Regeneración más rápida", "Cambio de Reino Mayor");
        }
        // Poderes Divinos
        return List.of("Poder Divino Nivel " + next, "Aumenta Daño y Radio", "+20% Potencia Mágica");
    }

    public static void appendWeaponTooltip(ItemStack stack, List<Component> tooltip, String saga, String primaryAbility, String secondaryAbility) {
        int dispLvl = getDisplayLevel(stack);
        float bonus = getBonusDamage(stack);
        if (dispLvl > 1) {
            tooltip.add(Component.literal("★ Potenciador: Nivel " + dispLvl + "/5 (+" + bonus + " Daño Divino)")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        } else {
            tooltip.add(Component.literal("☆ Nivel 1/5 — Pulsa [K] o usa la Forja para Potenciar con Orbes de Sangre")
                .withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.literal("Saga: " + saga).withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("• " + primaryAbility).withStyle(ChatFormatting.YELLOW));
        if (secondaryAbility != null && !secondaryAbility.isEmpty()) {
            tooltip.add(Component.literal("• " + secondaryAbility).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        if (EpicFightCompat.isLoaded()) {
            tooltip.add(Component.literal("⚔ Epic Fight Activo — [G]/[Shift+G] Habilidad | [V] Ejecutar | [K] Mejorar")
                .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.literal("⚔ [G]/[Shift+G] Habilidad | [V] Ejecutar | [K] Menú Potenciador")
                .withStyle(ChatFormatting.DARK_AQUA));
        }
    }
}
