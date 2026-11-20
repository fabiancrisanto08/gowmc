package com.spartanforge.godofwar.magic;

import com.spartanforge.godofwar.GodOfWarMod;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Catálogo completo y canónico de TODAS las 24 Magias de la saga God of War
 * según la Wiki oficial (GoW I, GoW II, GoW III, Chains of Olympus, Ghost of Sparta, Ascension y Betrayal).
 */
public enum GodOfWarMagic {
    // --- GOD OF WAR (2005) ---
    POSEIDONS_RAGE("poseidons_rage", "Ira de Poseidón", "God of War (2005)", "Rayo Marino",
        0xFF38D6FF, 30, 100,
        "Vórtice eléctrico circular 360°", "Electrocuta, eleva y aturde enemigos."),
    MEDUSA_GAZE("medusa_gaze", "Mirada de Medusa", "God of War (2005)", "Petrificación",
        0xFF5EE874, 28, 110,
        "Rayo frontal de Gorgona esmeralda", "Petrifica enemigos (daño x2 al golpear)."),
    ZEUS_FURY("zeus_fury", "Furia de Zeus", "God of War (2005)", "Rayo Olímpico",
        0xFFFFD84A, 22, 65,
        "Lanza un rayo divino a larga distancia", "Explota con relámpago real al impactar."),
    ARMY_OF_HADES("army_of_hades", "Ejército de Hades", "God of War (2005)", "Almas del Inframundo",
        0xFFC95CFF, 38, 140,
        "Invoca almas torturadas del Tártaro", "Persiguen, desgarran y drenan vida."),

    // --- GOD OF WAR II ---
    TYPHONS_BANE("typhons_bane", "Perdición de Tifón", "God of War II", "Viento Titánico",
        0xFF88ECFF, 20, 55,
        "Ráfaga de flechas de viento huracanado", "Perfora en línea y empuja enemigos."),
    CRONOS_RAGE("cronos_rage", "Ira de Cronos", "God of War II", "Tormenta Titánica",
        0xFF66B2FF, 36, 145,
        "Orbe eléctrico estático durante 5s", "Fríe continuamente a enemigos cercanos."),
    HEAD_OF_EURYALE("head_of_euryale", "Cabeza de Euríale", "God of War II", "Petrificación 360°",
        0xFFB56BFF, 36, 135,
        "Pulso petrificante en 360° de Gorgona", "Congela en piedra y detona esquirlas."),
    ATLAS_QUAKE("atlas_quake", "Temblor de Atlas", "God of War II", "Terremoto Volcánico",
        0xFFFF7A29, 38, 140,
        "Golpe titánico que agrieta el suelo", "Levanta columnas de magma y roca."),

    // --- GOD OF WAR III ---
    DIVINE_RECKONING("divine_reckoning", "Desagravio Divino", "God of War III", "Torbellino Celeste",
        0xFF4CD9FF, 40, 150,
        "Ciclón de la Hoja del Olimpo", "Atrae rivales al centro y estalla en luz."),
    ARMY_OF_SPARTA("army_of_sparta", "Ejército de Esparta", "God of War III", "Falange Espartana",
        0xFFE6392E, 42, 155,
        "Falange espectral defensiva 360°", "Escudo espartano + lluvia de lanzas."),
    SOUL_SUMMON("soul_summon", "Invocación de Almas", "God of War III", "Bestias Espectrales",
        0xFFD966FF, 34, 125,
        "Libera bestias de las Garras de Hades", "Embestida espectral + Wither masivo."),
    NEMEAN_ROAR("nemean_roar", "Rugido de Nemea", "God of War III", "Onda Sísmica",
        0xFF7AE0FF, 32, 115,
        "3 anillos de choque de los Cestus", "Rompe defensas e inflige +55 Aturdimiento."),
    NEMESIS_RAGE("nemesis_rage", "Furia de Némesis", "God of War III", "Rayo en Cadena",
        0xFF3BFFAA, 30, 105,
        "Descargas eléctricas del Látigo", "Salta en cadena entre hasta 8 rivales."),

    // --- CHAINS OF OLYMPUS ---
    EFREET("efreet", "Efrit (El Genio)", "Chains of Olympus", "Fuego Persa",
        0xFFFF661A, 34, 120,
        "Invoca al espíritu ígneo Efrit", "Pilares de fuego e incineración 360°."),
    LIGHT_OF_DAWN("light_of_dawn", "Luz del Alba", "Chains of Olympus", "Luz Solar de Eos",
        0xFFFFE66B, 22, 65,
        "Orbe solar dorado de alta velocidad", "Ciega enemigos y hace daño x2 a no-muertos."),
    CHARONS_WRATH("charons_wrath", "Ira de Caronte", "Chains of Olympus", "Fuego Verde",
        0xFF47FF6B, 34, 130,
        "Llamas verdes letales de Caronte", "Paraliza y quema durante 4s contagiosos."),

    // --- GHOST OF SPARTA ---
    EYE_OF_ATLANTIS("eye_of_atlantis", "Ojo de la Atlántida", "Ghost of Sparta", "Relámpago Atlante",
        0xFF29FFE8, 30, 105,
        "Rayos turquesa de la Atlántida", "Electrocuta en cadena a múltiples rivales."),
    SCOURGE_OF_ERINYS("scourge_of_erinys", "Plaga de Erinias", "Ghost of Sparta", "Vórtice Oscuro",
        0xFFFF386B, 38, 145,
        "Esfera oscura que succiona enemigos", "Desgarra en vórtice y roba vida 3s."),
    HORN_OF_BOREAS("horn_of_boreas", "Cuerno de Bóreas", "Ghost of Sparta", "Ventisca Ártica",
        0xFF8CE6FF, 32, 120,
        "Tormenta helada del Cuerno de Bóreas", "Congela por completo a todos alrededor."),

    // --- ASCENSION ---
    FIRE_OF_ARES("fire_of_ares", "Fuego de Ares", "GoW: Ascension", "Elemento Fuego",
        0xFFFF521A, 32, 120,
        "Explosión volcánica + Fuerza II", "Incineración y +50 Orbes de Sangre extra."),
    ICE_OF_POSEIDON("ice_of_poseidon", "Hielo de Poseidón", "GoW: Ascension", "Elemento Hielo",
        0xFF42C8FF, 28, 105,
        "Picos glaciares congelantes 360°", "Concede Respiración y poder acuático."),
    LIGHTNING_OF_ZEUS("lightning_of_zeus", "Relámpago de Zeus", "GoW: Ascension", "Elemento Rayo",
        0xFF70CCFF, 28, 95,
        "Descarga veloz de rayos en cono", "Sobrecarga eléctrica + Velocidad II."),
    SOUL_OF_HADES("soul_of_hades", "Almas de Hades", "GoW: Ascension", "Elemento Inframundo",
        0xFFB866FF, 30, 115,
        "Brazos del Inframundo desde el suelo", "Atrapa rivales, debilita y drena salud."),

    // --- BETRAYAL ---
    BLADE_OF_ARES_SLASH("blade_of_ares_slash", "Corte de Ares", "GoW: Betrayal", "Tajo Divino",
        0xFFFF3B30, 24, 75,
        "Onda cortante carmesí perforante", "Lacera a todos los enemigos en línea recta.");

    public static final int MAX_LEVEL = 5;
    private static final int[] UPGRADE_BLOOD_COSTS = { 450, 1200, 2600, 5200 };
    private static final Map<String, GodOfWarMagic> BY_ID = new HashMap<>();

    static {
        for (GodOfWarMagic m : values()) {
            BY_ID.put(m.id, m);
        }
    }

    public final String id;
    public final String displayName;
    public final String gameOrigin;
    public final String elementLabel;
    public final int accentColor;
    public final int manaCost;
    public final int cooldownTicks;
    public final String descLine1;
    public final String descLine2;
    public final ResourceLocation iconTexture;

    GodOfWarMagic(String id, String displayName, String gameOrigin, String elementLabel,
                  int accentColor, int manaCost, int cooldownTicks, String descLine1, String descLine2) {
        this.id = id;
        this.displayName = displayName;
        this.gameOrigin = gameOrigin;
        this.elementLabel = elementLabel;
        this.accentColor = accentColor;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
        this.descLine1 = descLine1;
        this.descLine2 = descLine2;
        this.iconTexture = new ResourceLocation(GodOfWarMod.MOD_ID, "textures/magic/" + id + ".png");
    }

    @Nullable
    public static GodOfWarMagic byId(String id) {
        return BY_ID.get(id);
    }

    @Nullable
    public static GodOfWarMagic byOrdinal(int ord) {
        GodOfWarMagic[] vals = values();
        return (ord >= 0 && ord < vals.length) ? vals[ord] : null;
    }

    public int getBloodUpgradeCost(int currentLevel) {
        int clamped = Math.max(1, Math.min(MAX_LEVEL, currentLevel));
        if (clamped >= MAX_LEVEL) return 0;
        return UPGRADE_BLOOD_COSTS[clamped - 1];
    }

    public float getDamageMultiplier(int level) {
        return 1.0F + (Math.max(1, level) - 1) * 0.30F; // Lv1=1.0x, Lv5=2.2x!
    }

    public int getEffectiveCooldown(int level) {
        float reduction = 1.0F - (Math.max(1, level) - 1) * 0.11F;
        return Math.max(24, Math.round(cooldownTicks * reduction));
    }

    public int getEffectiveManaCost(int level) {
        return Math.max(12, manaCost - (Math.max(1, level) - 1) * 2);
    }
}
