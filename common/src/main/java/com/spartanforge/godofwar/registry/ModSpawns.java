package com.spartanforge.godofwar.registry;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

/**
 * Configura spawns naturales por bioma (multi-loader via Architectury BiomeModifications)
 * para el bestiario Griego, Nórdico, Legión Romana y Criaturas Míticas.
 */
public final class ModSpawns {
    private ModSpawns() {}

    public static void register() {
        // Biomas fríos / Taiga / Montañas nevadas -> Bestiario Nórdico
        BiomeModifications.addProperties(
            ctx -> ctx.hasTag(BiomeTags.IS_TAIGA) || ctx.hasTag(BiomeTags.IS_MOUNTAIN),
            (ctx, mutable) -> {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.DRAUGR_MOB.get(), 28, 1, 3));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.WULVER_MOB.get(), 14, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.EINHERJAR_MOB.get(), 10, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.FENRIR_CREATURE.get(), 4, 1, 1));
            }
        );

        // Biomas Overworld templados (Bosques, Colinas, Sabanas) -> Bestiario Griego y Legión Romana
        BiomeModifications.addProperties(
            ctx -> ctx.hasTag(BiomeTags.IS_OVERWORLD) && !ctx.hasTag(BiomeTags.IS_OCEAN) && !ctx.hasTag(BiomeTags.IS_RIVER),
            (ctx, mutable) -> {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.MINOTAUR_MOB.get(), 14, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.SATYR_MOB.get(), 16, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.CYCLOPS_MOB.get(), 8, 1, 1));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.CENTAUR_MOB.get(), 10, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.HARPY_MOB.get(), 14, 1, 3));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.ROMAN_WARRIOR.get(), 16, 1, 3));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.ROMAN_ARCHER.get(), 12, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.ROMAN_CENTURION.get(), 6, 1, 1));
                mutable.getSpawnProperties().addSpawn(MobCategory.CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.PEGASUS_CREATURE.get(), 5, 1, 1));
            }
        );

        // Desiertos y Badlands -> Sirenas del Desierto y Fénix de Ceniza
        BiomeModifications.addProperties(
            ctx -> ctx.hasTag(BiomeTags.HAS_DESERT_PYRAMID) || ctx.hasTag(BiomeTags.IS_BADLANDS),
            (ctx, mutable) -> {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.SIREN_MOB.get(), 22, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.PHOENIX_CREATURE.get(), 5, 1, 1));
            }
        );

        // Junglas -> Gorgonas
        BiomeModifications.addProperties(
            ctx -> ctx.hasTag(BiomeTags.IS_JUNGLE),
            (ctx, mutable) -> mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(ModEntities.GORGON_MOB.get(), 18, 1, 2))
        );

        // Océanos -> Hipocampo de Poseidón
        BiomeModifications.addProperties(
            ctx -> ctx.hasTag(BiomeTags.IS_OCEAN),
            (ctx, mutable) -> mutable.getSpawnProperties().addSpawn(MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(ModEntities.HIPPOCAMPUS_CREATURE.get(), 6, 1, 1))
        );

        // Nether -> Cerbero del Tártaro y Berserkers/Pretorianos infernales
        BiomeModifications.addProperties(
            ctx -> ctx.hasTag(BiomeTags.IS_NETHER),
            (ctx, mutable) -> {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.CERBERUS_MOB.get(), 16, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.ROMAN_BERSERKER.get(), 10, 1, 2));
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.ROMAN_PRAETORIAN.get(), 8, 1, 1));
            }
        );
    }
}
