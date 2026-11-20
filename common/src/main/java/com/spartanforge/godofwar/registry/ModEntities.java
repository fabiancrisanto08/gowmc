package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.ZeusBossEntity;
import com.spartanforge.godofwar.entity.AresBossEntity;
import com.spartanforge.godofwar.entity.AthenaBossEntity;
import com.spartanforge.godofwar.entity.PoseidonBossEntity;
import com.spartanforge.godofwar.entity.HadesBossEntity;
import com.spartanforge.godofwar.entity.HermesBossEntity;
import com.spartanforge.godofwar.entity.HeliosBossEntity;
import com.spartanforge.godofwar.entity.HerculesBossEntity;
import com.spartanforge.godofwar.entity.PersephoneBossEntity;
import com.spartanforge.godofwar.entity.ThanatosBossEntity;
import com.spartanforge.godofwar.entity.OdinBossEntity;
import com.spartanforge.godofwar.entity.ThorBossEntity;
import com.spartanforge.godofwar.entity.BaldurBossEntity;
import com.spartanforge.godofwar.entity.SigrunBossEntity;
import com.spartanforge.godofwar.entity.CronosTitanEntity;
import com.spartanforge.godofwar.entity.GaiaTitanEntity;
import com.spartanforge.godofwar.entity.AtlasTitanEntity;
import com.spartanforge.godofwar.entity.TyphonTitanEntity;
import com.spartanforge.godofwar.entity.PersesTitanEntity;
import com.spartanforge.godofwar.entity.MinotaurMobEntity;
import com.spartanforge.godofwar.entity.SirenMobEntity;
import com.spartanforge.godofwar.entity.CyclopsMobEntity;
import com.spartanforge.godofwar.entity.GorgonMobEntity;
import com.spartanforge.godofwar.entity.CerberusMobEntity;
import com.spartanforge.godofwar.entity.HarpyMobEntity;
import com.spartanforge.godofwar.entity.SatyrMobEntity;
import com.spartanforge.godofwar.entity.CentaurMobEntity;
import com.spartanforge.godofwar.entity.DraugrMobEntity;
import com.spartanforge.godofwar.entity.WulverMobEntity;
import com.spartanforge.godofwar.entity.EinherjarMobEntity;
import com.spartanforge.godofwar.entity.RomanWarriorEntity;
import com.spartanforge.godofwar.entity.RomanArcherEntity;
import com.spartanforge.godofwar.entity.RomanBerserkerEntity;
import com.spartanforge.godofwar.entity.RomanCenturionEntity;
import com.spartanforge.godofwar.entity.RomanPraetorianEntity;
import com.spartanforge.godofwar.entity.PhoenixCreatureEntity;
import com.spartanforge.godofwar.entity.PegasusCreatureEntity;
import com.spartanforge.godofwar.entity.HippocampusCreatureEntity;
import com.spartanforge.godofwar.entity.FenrirCreatureEntity;
import com.spartanforge.godofwar.entity.SpartanSmithVillagerEntity;
import com.spartanforge.godofwar.entity.GreekOracleVillagerEntity;
import com.spartanforge.godofwar.entity.DwarfForgerVillagerEntity;
import com.spartanforge.godofwar.entity.RomanQuartermasterVillagerEntity;
import com.spartanforge.godofwar.entity.projectile.ThrownLeviathanAxeEntity;
import com.spartanforge.godofwar.entity.BloodOrbEntity;
import com.spartanforge.godofwar.entity.projectile.DraupnirSpearProjectileEntity;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ZeusBossEntity>> ZEUS_BOSS =
        ENTITIES.register("zeus_boss", () -> EntityType.Builder.of(ZeusBossEntity::new, MobCategory.MONSTER)
            .sized(1.16f, 2.90f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "zeus_boss").toString()));

    public static final RegistrySupplier<EntityType<AresBossEntity>> ARES_BOSS =
        ENTITIES.register("ares_boss", () -> EntityType.Builder.of(AresBossEntity::new, MobCategory.MONSTER)
            .sized(1.44f, 3.60f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "ares_boss").toString()));

    public static final RegistrySupplier<EntityType<AthenaBossEntity>> ATHENA_BOSS =
        ENTITIES.register("athena_boss", () -> EntityType.Builder.of(AthenaBossEntity::new, MobCategory.MONSTER)
            .sized(0.96f, 2.40f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "athena_boss").toString()));

    public static final RegistrySupplier<EntityType<PoseidonBossEntity>> POSEIDON_BOSS =
        ENTITIES.register("poseidon_boss", () -> EntityType.Builder.of(PoseidonBossEntity::new, MobCategory.MONSTER)
            .sized(1.32f, 3.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "poseidon_boss").toString()));

    public static final RegistrySupplier<EntityType<HadesBossEntity>> HADES_BOSS =
        ENTITIES.register("hades_boss", () -> EntityType.Builder.of(HadesBossEntity::new, MobCategory.MONSTER)
            .sized(1.24f, 3.10f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "hades_boss").toString()));

    public static final RegistrySupplier<EntityType<HermesBossEntity>> HERMES_BOSS =
        ENTITIES.register("hermes_boss", () -> EntityType.Builder.of(HermesBossEntity::new, MobCategory.MONSTER)
            .sized(0.84f, 2.10f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "hermes_boss").toString()));

    public static final RegistrySupplier<EntityType<HeliosBossEntity>> HELIOS_BOSS =
        ENTITIES.register("helios_boss", () -> EntityType.Builder.of(HeliosBossEntity::new, MobCategory.MONSTER)
            .sized(1.00f, 2.50f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "helios_boss").toString()));

    public static final RegistrySupplier<EntityType<HerculesBossEntity>> HERCULES_BOSS =
        ENTITIES.register("hercules_boss", () -> EntityType.Builder.of(HerculesBossEntity::new, MobCategory.MONSTER)
            .sized(1.12f, 2.80f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "hercules_boss").toString()));

    public static final RegistrySupplier<EntityType<PersephoneBossEntity>> PERSEPHONE_BOSS =
        ENTITIES.register("persephone_boss", () -> EntityType.Builder.of(PersephoneBossEntity::new, MobCategory.MONSTER)
            .sized(0.96f, 2.40f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "persephone_boss").toString()));

    public static final RegistrySupplier<EntityType<ThanatosBossEntity>> THANATOS_BOSS =
        ENTITIES.register("thanatos_boss", () -> EntityType.Builder.of(ThanatosBossEntity::new, MobCategory.MONSTER)
            .sized(1.20f, 3.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "thanatos_boss").toString()));

    public static final RegistrySupplier<EntityType<OdinBossEntity>> ODIN_BOSS =
        ENTITIES.register("odin_boss", () -> EntityType.Builder.of(OdinBossEntity::new, MobCategory.MONSTER)
            .sized(0.92f, 2.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "odin_boss").toString()));

    public static final RegistrySupplier<EntityType<ThorBossEntity>> THOR_BOSS =
        ENTITIES.register("thor_boss", () -> EntityType.Builder.of(ThorBossEntity::new, MobCategory.MONSTER)
            .sized(1.08f, 2.70f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "thor_boss").toString()));

    public static final RegistrySupplier<EntityType<BaldurBossEntity>> BALDUR_BOSS =
        ENTITIES.register("baldur_boss", () -> EntityType.Builder.of(BaldurBossEntity::new, MobCategory.MONSTER)
            .sized(0.88f, 2.20f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "baldur_boss").toString()));

    public static final RegistrySupplier<EntityType<SigrunBossEntity>> SIGRUN_BOSS =
        ENTITIES.register("sigrun_boss", () -> EntityType.Builder.of(SigrunBossEntity::new, MobCategory.MONSTER)
            .sized(1.04f, 2.60f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "sigrun_boss").toString()));

    public static final RegistrySupplier<EntityType<CronosTitanEntity>> CRONOS_TITAN =
        ENTITIES.register("cronos_titan", () -> EntityType.Builder.of(CronosTitanEntity::new, MobCategory.MONSTER)
            .sized(3.83f, 7.65f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "cronos_titan").toString()));

    public static final RegistrySupplier<EntityType<GaiaTitanEntity>> GAIA_TITAN =
        ENTITIES.register("gaia_titan", () -> EntityType.Builder.of(GaiaTitanEntity::new, MobCategory.MONSTER)
            .sized(3.38f, 6.75f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "gaia_titan").toString()));

    public static final RegistrySupplier<EntityType<AtlasTitanEntity>> ATLAS_TITAN =
        ENTITIES.register("atlas_titan", () -> EntityType.Builder.of(AtlasTitanEntity::new, MobCategory.MONSTER)
            .sized(3.15f, 6.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "atlas_titan").toString()));

    public static final RegistrySupplier<EntityType<TyphonTitanEntity>> TYPHON_TITAN =
        ENTITIES.register("typhon_titan", () -> EntityType.Builder.of(TyphonTitanEntity::new, MobCategory.MONSTER)
            .sized(2.70f, 5.40f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "typhon_titan").toString()));

    public static final RegistrySupplier<EntityType<PersesTitanEntity>> PERSES_TITAN =
        ENTITIES.register("perses_titan", () -> EntityType.Builder.of(PersesTitanEntity::new, MobCategory.MONSTER)
            .sized(3.06f, 6.12f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "perses_titan").toString()));

    public static final RegistrySupplier<EntityType<MinotaurMobEntity>> MINOTAUR_MOB =
        ENTITIES.register("minotaur_mob", () -> EntityType.Builder.of(MinotaurMobEntity::new, MobCategory.MONSTER)
            .sized(1.08f, 2.70f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "minotaur_mob").toString()));

    public static final RegistrySupplier<EntityType<SirenMobEntity>> SIREN_MOB =
        ENTITIES.register("siren_mob", () -> EntityType.Builder.of(SirenMobEntity::new, MobCategory.MONSTER)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "siren_mob").toString()));

    public static final RegistrySupplier<EntityType<CyclopsMobEntity>> CYCLOPS_MOB =
        ENTITIES.register("cyclops_mob", () -> EntityType.Builder.of(CyclopsMobEntity::new, MobCategory.MONSTER)
            .sized(1.68f, 4.20f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "cyclops_mob").toString()));

    public static final RegistrySupplier<EntityType<GorgonMobEntity>> GORGON_MOB =
        ENTITIES.register("gorgon_mob", () -> EntityType.Builder.of(GorgonMobEntity::new, MobCategory.MONSTER)
            .sized(0.92f, 2.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "gorgon_mob").toString()));

    public static final RegistrySupplier<EntityType<CerberusMobEntity>> CERBERUS_MOB =
        ENTITIES.register("cerberus_mob", () -> EntityType.Builder.of(CerberusMobEntity::new, MobCategory.MONSTER)
            .sized(1.32f, 3.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "cerberus_mob").toString()));

    public static final RegistrySupplier<EntityType<HarpyMobEntity>> HARPY_MOB =
        ENTITIES.register("harpy_mob", () -> EntityType.Builder.of(HarpyMobEntity::new, MobCategory.MONSTER)
            .sized(0.76f, 1.90f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "harpy_mob").toString()));

    public static final RegistrySupplier<EntityType<SatyrMobEntity>> SATYR_MOB =
        ENTITIES.register("satyr_mob", () -> EntityType.Builder.of(SatyrMobEntity::new, MobCategory.MONSTER)
            .sized(0.92f, 2.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "satyr_mob").toString()));

    public static final RegistrySupplier<EntityType<CentaurMobEntity>> CENTAUR_MOB =
        ENTITIES.register("centaur_mob", () -> EntityType.Builder.of(CentaurMobEntity::new, MobCategory.MONSTER)
            .sized(1.20f, 3.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "centaur_mob").toString()));

    public static final RegistrySupplier<EntityType<DraugrMobEntity>> DRAUGR_MOB =
        ENTITIES.register("draugr_mob", () -> EntityType.Builder.of(DraugrMobEntity::new, MobCategory.MONSTER)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "draugr_mob").toString()));

    public static final RegistrySupplier<EntityType<WulverMobEntity>> WULVER_MOB =
        ENTITIES.register("wulver_mob", () -> EntityType.Builder.of(WulverMobEntity::new, MobCategory.MONSTER)
            .sized(1.00f, 2.50f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "wulver_mob").toString()));

    public static final RegistrySupplier<EntityType<EinherjarMobEntity>> EINHERJAR_MOB =
        ENTITIES.register("einherjar_mob", () -> EntityType.Builder.of(EinherjarMobEntity::new, MobCategory.MONSTER)
            .sized(0.92f, 2.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "einherjar_mob").toString()));

    public static final RegistrySupplier<EntityType<RomanWarriorEntity>> ROMAN_WARRIOR =
        ENTITIES.register("roman_warrior", () -> EntityType.Builder.of(RomanWarriorEntity::new, MobCategory.MONSTER)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "roman_warrior").toString()));

    public static final RegistrySupplier<EntityType<RomanArcherEntity>> ROMAN_ARCHER =
        ENTITIES.register("roman_archer", () -> EntityType.Builder.of(RomanArcherEntity::new, MobCategory.MONSTER)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "roman_archer").toString()));

    public static final RegistrySupplier<EntityType<RomanBerserkerEntity>> ROMAN_BERSERKER =
        ENTITIES.register("roman_berserker", () -> EntityType.Builder.of(RomanBerserkerEntity::new, MobCategory.MONSTER)
            .sized(0.96f, 2.40f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "roman_berserker").toString()));

    public static final RegistrySupplier<EntityType<RomanCenturionEntity>> ROMAN_CENTURION =
        ENTITIES.register("roman_centurion", () -> EntityType.Builder.of(RomanCenturionEntity::new, MobCategory.MONSTER)
            .sized(0.92f, 2.30f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "roman_centurion").toString()));

    public static final RegistrySupplier<EntityType<RomanPraetorianEntity>> ROMAN_PRAETORIAN =
        ENTITIES.register("roman_praetorian", () -> EntityType.Builder.of(RomanPraetorianEntity::new, MobCategory.MONSTER)
            .sized(0.96f, 2.40f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "roman_praetorian").toString()));

    public static final RegistrySupplier<EntityType<PhoenixCreatureEntity>> PHOENIX_CREATURE =
        ENTITIES.register("phoenix_creature", () -> EntityType.Builder.of(PhoenixCreatureEntity::new, MobCategory.CREATURE)
            .sized(1.20f, 3.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "phoenix_creature").toString()));

    public static final RegistrySupplier<EntityType<PegasusCreatureEntity>> PEGASUS_CREATURE =
        ENTITIES.register("pegasus_creature", () -> EntityType.Builder.of(PegasusCreatureEntity::new, MobCategory.CREATURE)
            .sized(1.08f, 2.70f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "pegasus_creature").toString()));

    public static final RegistrySupplier<EntityType<HippocampusCreatureEntity>> HIPPOCAMPUS_CREATURE =
        ENTITIES.register("hippocampus_creature", () -> EntityType.Builder.of(HippocampusCreatureEntity::new, MobCategory.CREATURE)
            .sized(1.04f, 2.60f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "hippocampus_creature").toString()));

    public static final RegistrySupplier<EntityType<FenrirCreatureEntity>> FENRIR_CREATURE =
        ENTITIES.register("fenrir_creature", () -> EntityType.Builder.of(FenrirCreatureEntity::new, MobCategory.CREATURE)
            .sized(1.76f, 4.40f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "fenrir_creature").toString()));

    public static final RegistrySupplier<EntityType<SpartanSmithVillagerEntity>> SPARTAN_SMITH_VILLAGER =
        ENTITIES.register("spartan_smith_villager", () -> EntityType.Builder.of(SpartanSmithVillagerEntity::new, MobCategory.CREATURE)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "spartan_smith_villager").toString()));

    public static final RegistrySupplier<EntityType<GreekOracleVillagerEntity>> GREEK_ORACLE_VILLAGER =
        ENTITIES.register("greek_oracle_villager", () -> EntityType.Builder.of(GreekOracleVillagerEntity::new, MobCategory.CREATURE)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "greek_oracle_villager").toString()));

    public static final RegistrySupplier<EntityType<DwarfForgerVillagerEntity>> DWARF_FORGER_VILLAGER =
        ENTITIES.register("dwarf_forger_villager", () -> EntityType.Builder.of(DwarfForgerVillagerEntity::new, MobCategory.CREATURE)
            .sized(0.68f, 1.70f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "dwarf_forger_villager").toString()));

    public static final RegistrySupplier<EntityType<RomanQuartermasterVillagerEntity>> ROMAN_QUARTERMASTER_VILLAGER =
        ENTITIES.register("roman_quartermaster_villager", () -> EntityType.Builder.of(RomanQuartermasterVillagerEntity::new, MobCategory.CREATURE)
            .sized(0.80f, 2.00f)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "roman_quartermaster_villager").toString()));

    // Custom Weapon Projectiles
    public static final RegistrySupplier<EntityType<ThrownLeviathanAxeEntity>> THROWN_LEVIATHAN_AXE =
        ENTITIES.register("thrown_leviathan_axe", () -> EntityType.Builder.<ThrownLeviathanAxeEntity>of(ThrownLeviathanAxeEntity::new, MobCategory.MISC)
            .sized(0.55f, 0.55f)
            .clientTrackingRange(6)
            .updateInterval(2)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "thrown_leviathan_axe").toString()));

    public static final RegistrySupplier<EntityType<DraupnirSpearProjectileEntity>> DRAUPNIR_SPEAR_PROJECTILE =
        ENTITIES.register("draupnir_spear_projectile", () -> EntityType.Builder.<DraupnirSpearProjectileEntity>of(DraupnirSpearProjectileEntity::new, MobCategory.MISC)
            .sized(0.45f, 0.45f)
            .clientTrackingRange(6)
            .updateInterval(2)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "draupnir_spear_projectile").toString()));

    public static final RegistrySupplier<EntityType<BloodOrbEntity>> BLOOD_ORB =
        ENTITIES.register("blood_orb", () -> EntityType.Builder.<BloodOrbEntity>of(BloodOrbEntity::new, MobCategory.MISC)
            .sized(0.45f, 0.45f)
            .clientTrackingRange(6)
            .updateInterval(2)
            .build(new ResourceLocation(GodOfWarMod.MOD_ID, "blood_orb").toString()));

    public static void register() {
        ENTITIES.register();
        EntityAttributeRegistry.register(ZEUS_BOSS, ZeusBossEntity::createAttributes);
        EntityAttributeRegistry.register(ARES_BOSS, AresBossEntity::createAttributes);
        EntityAttributeRegistry.register(ATHENA_BOSS, AthenaBossEntity::createAttributes);
        EntityAttributeRegistry.register(POSEIDON_BOSS, PoseidonBossEntity::createAttributes);
        EntityAttributeRegistry.register(HADES_BOSS, HadesBossEntity::createAttributes);
        EntityAttributeRegistry.register(HERMES_BOSS, HermesBossEntity::createAttributes);
        EntityAttributeRegistry.register(HELIOS_BOSS, HeliosBossEntity::createAttributes);
        EntityAttributeRegistry.register(HERCULES_BOSS, HerculesBossEntity::createAttributes);
        EntityAttributeRegistry.register(PERSEPHONE_BOSS, PersephoneBossEntity::createAttributes);
        EntityAttributeRegistry.register(THANATOS_BOSS, ThanatosBossEntity::createAttributes);
        EntityAttributeRegistry.register(ODIN_BOSS, OdinBossEntity::createAttributes);
        EntityAttributeRegistry.register(THOR_BOSS, ThorBossEntity::createAttributes);
        EntityAttributeRegistry.register(BALDUR_BOSS, BaldurBossEntity::createAttributes);
        EntityAttributeRegistry.register(SIGRUN_BOSS, SigrunBossEntity::createAttributes);
        EntityAttributeRegistry.register(CRONOS_TITAN, CronosTitanEntity::createAttributes);
        EntityAttributeRegistry.register(GAIA_TITAN, GaiaTitanEntity::createAttributes);
        EntityAttributeRegistry.register(ATLAS_TITAN, AtlasTitanEntity::createAttributes);
        EntityAttributeRegistry.register(TYPHON_TITAN, TyphonTitanEntity::createAttributes);
        EntityAttributeRegistry.register(PERSES_TITAN, PersesTitanEntity::createAttributes);
        EntityAttributeRegistry.register(MINOTAUR_MOB, MinotaurMobEntity::createAttributes);
        EntityAttributeRegistry.register(SIREN_MOB, SirenMobEntity::createAttributes);
        EntityAttributeRegistry.register(CYCLOPS_MOB, CyclopsMobEntity::createAttributes);
        EntityAttributeRegistry.register(GORGON_MOB, GorgonMobEntity::createAttributes);
        EntityAttributeRegistry.register(CERBERUS_MOB, CerberusMobEntity::createAttributes);
        EntityAttributeRegistry.register(HARPY_MOB, HarpyMobEntity::createAttributes);
        EntityAttributeRegistry.register(SATYR_MOB, SatyrMobEntity::createAttributes);
        EntityAttributeRegistry.register(CENTAUR_MOB, CentaurMobEntity::createAttributes);
        EntityAttributeRegistry.register(DRAUGR_MOB, DraugrMobEntity::createAttributes);
        EntityAttributeRegistry.register(WULVER_MOB, WulverMobEntity::createAttributes);
        EntityAttributeRegistry.register(EINHERJAR_MOB, EinherjarMobEntity::createAttributes);
        EntityAttributeRegistry.register(ROMAN_WARRIOR, RomanWarriorEntity::createAttributes);
        EntityAttributeRegistry.register(ROMAN_ARCHER, RomanArcherEntity::createAttributes);
        EntityAttributeRegistry.register(ROMAN_BERSERKER, RomanBerserkerEntity::createAttributes);
        EntityAttributeRegistry.register(ROMAN_CENTURION, RomanCenturionEntity::createAttributes);
        EntityAttributeRegistry.register(ROMAN_PRAETORIAN, RomanPraetorianEntity::createAttributes);
        EntityAttributeRegistry.register(PHOENIX_CREATURE, PhoenixCreatureEntity::createAttributes);
        EntityAttributeRegistry.register(PEGASUS_CREATURE, PegasusCreatureEntity::createAttributes);
        EntityAttributeRegistry.register(HIPPOCAMPUS_CREATURE, HippocampusCreatureEntity::createAttributes);
        EntityAttributeRegistry.register(FENRIR_CREATURE, FenrirCreatureEntity::createAttributes);
        EntityAttributeRegistry.register(SPARTAN_SMITH_VILLAGER, SpartanSmithVillagerEntity::createAttributes);
        EntityAttributeRegistry.register(GREEK_ORACLE_VILLAGER, GreekOracleVillagerEntity::createAttributes);
        EntityAttributeRegistry.register(DWARF_FORGER_VILLAGER, DwarfForgerVillagerEntity::createAttributes);
        EntityAttributeRegistry.register(ROMAN_QUARTERMASTER_VILLAGER, RomanQuartermasterVillagerEntity::createAttributes);
    }
}
