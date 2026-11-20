package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.item.AmuletOfOuroborosItem;
import com.spartanforge.godofwar.item.BladesOfAthenaItem;
import com.spartanforge.godofwar.item.BladesOfChaosItem;
import com.spartanforge.godofwar.item.GoldenFleeceItem;
import com.spartanforge.godofwar.item.LeviathanAxeItem;
import com.spartanforge.godofwar.item.WingsOfIcarusItem;
import com.spartanforge.godofwar.item.BladeOfOlympusItem;
import com.spartanforge.godofwar.item.DraupnirSpearItem;
import com.spartanforge.godofwar.item.NemeanCestusItem;
import com.spartanforge.godofwar.item.ClawsOfHadesItem;
import com.spartanforge.godofwar.item.GuardianShieldItem;
import com.spartanforge.godofwar.item.SpartanRagePowerItem;
import com.spartanforge.godofwar.item.PoseidonsRagePowerItem;
import com.spartanforge.godofwar.item.ArmyOfHadesPowerItem;
import com.spartanforge.godofwar.item.MedusaHeadPowerItem;
import com.spartanforge.godofwar.item.GodArmorItem;
import com.spartanforge.godofwar.item.HornOfBloodMeadItem;
import com.spartanforge.godofwar.item.IdunnAppleItem;
import com.spartanforge.godofwar.item.MagicRelicItem;
import com.spartanforge.godofwar.item.ModArmorMaterial;
import com.spartanforge.godofwar.magic.GodOfWarMagic;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.ITEM);

    // Legendary Weapons
    public static final RegistrySupplier<Item> BLADES_OF_CHAOS =
        ITEMS.register("blades_of_chaos", () -> new BladesOfChaosItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> BLADES_OF_ATHENA =
        ITEMS.register("blades_of_athena", () -> new BladesOfAthenaItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> LEVIATHAN_AXE =
        ITEMS.register("leviathan_axe", () -> new LeviathanAxeItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> BLADE_OF_OLYMPUS =
        ITEMS.register("blade_of_olympus", () -> new BladeOfOlympusItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> DRAUPNIR_SPEAR =
        ITEMS.register("draupnir_spear", () -> new DraupnirSpearItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> NEMEAN_CESTUS =
        ITEMS.register("nemean_cestus", () -> new NemeanCestusItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> CLAWS_OF_HADES =
        ITEMS.register("claws_of_hades", () -> new ClawsOfHadesItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> GUARDIAN_SHIELD =
        ITEMS.register("guardian_shield", () -> new GuardianShieldItem(new Item.Properties().stacksTo(1)));

    // Godly Powers & Relics
    public static final RegistrySupplier<Item> SPARTAN_RAGE_POWER =
        ITEMS.register("spartan_rage_power", () -> new SpartanRagePowerItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> POSEIDONS_RAGE_POWER =
        ITEMS.register("poseidons_rage_power", () -> new PoseidonsRagePowerItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> ARMY_OF_HADES_POWER =
        ITEMS.register("army_of_hades_power", () -> new ArmyOfHadesPowerItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> MEDUSA_HEAD_POWER =
        ITEMS.register("medusa_head_power", () -> new MedusaHeadPowerItem(new Item.Properties().stacksTo(1)));

    // Complete God of War Saga Wiki Magics (Consume to Permanently Unlock in [M] Magic Menu!)
    public static final RegistrySupplier<Item> MAGIC_ZEUS_FURY =
        ITEMS.register("magic_zeus_fury", () -> new MagicRelicItem(GodOfWarMagic.ZEUS_FURY));
    public static final RegistrySupplier<Item> MAGIC_TYPHONS_BANE =
        ITEMS.register("magic_typhons_bane", () -> new MagicRelicItem(GodOfWarMagic.TYPHONS_BANE));
    public static final RegistrySupplier<Item> MAGIC_CRONOS_RAGE =
        ITEMS.register("magic_cronos_rage", () -> new MagicRelicItem(GodOfWarMagic.CRONOS_RAGE));
    public static final RegistrySupplier<Item> MAGIC_HEAD_OF_EURYALE =
        ITEMS.register("magic_head_of_euryale", () -> new MagicRelicItem(GodOfWarMagic.HEAD_OF_EURYALE));
    public static final RegistrySupplier<Item> MAGIC_ATLAS_QUAKE =
        ITEMS.register("magic_atlas_quake", () -> new MagicRelicItem(GodOfWarMagic.ATLAS_QUAKE));
    public static final RegistrySupplier<Item> MAGIC_DIVINE_RECKONING =
        ITEMS.register("magic_divine_reckoning", () -> new MagicRelicItem(GodOfWarMagic.DIVINE_RECKONING));
    public static final RegistrySupplier<Item> MAGIC_ARMY_OF_SPARTA =
        ITEMS.register("magic_army_of_sparta", () -> new MagicRelicItem(GodOfWarMagic.ARMY_OF_SPARTA));
    public static final RegistrySupplier<Item> MAGIC_SOUL_SUMMON =
        ITEMS.register("magic_soul_summon", () -> new MagicRelicItem(GodOfWarMagic.SOUL_SUMMON));
    public static final RegistrySupplier<Item> MAGIC_NEMEAN_ROAR =
        ITEMS.register("magic_nemean_roar", () -> new MagicRelicItem(GodOfWarMagic.NEMEAN_ROAR));
    public static final RegistrySupplier<Item> MAGIC_NEMESIS_RAGE =
        ITEMS.register("magic_nemesis_rage", () -> new MagicRelicItem(GodOfWarMagic.NEMESIS_RAGE));
    public static final RegistrySupplier<Item> MAGIC_EFREET =
        ITEMS.register("magic_efreet", () -> new MagicRelicItem(GodOfWarMagic.EFREET));
    public static final RegistrySupplier<Item> MAGIC_LIGHT_OF_DAWN =
        ITEMS.register("magic_light_of_dawn", () -> new MagicRelicItem(GodOfWarMagic.LIGHT_OF_DAWN));
    public static final RegistrySupplier<Item> MAGIC_CHARONS_WRATH =
        ITEMS.register("magic_charons_wrath", () -> new MagicRelicItem(GodOfWarMagic.CHARONS_WRATH));
    public static final RegistrySupplier<Item> MAGIC_EYE_OF_ATLANTIS =
        ITEMS.register("magic_eye_of_atlantis", () -> new MagicRelicItem(GodOfWarMagic.EYE_OF_ATLANTIS));
    public static final RegistrySupplier<Item> MAGIC_SCOURGE_OF_ERINYS =
        ITEMS.register("magic_scourge_of_erinys", () -> new MagicRelicItem(GodOfWarMagic.SCOURGE_OF_ERINYS));
    public static final RegistrySupplier<Item> MAGIC_HORN_OF_BOREAS =
        ITEMS.register("magic_horn_of_boreas", () -> new MagicRelicItem(GodOfWarMagic.HORN_OF_BOREAS));
    public static final RegistrySupplier<Item> MAGIC_FIRE_OF_ARES =
        ITEMS.register("magic_fire_of_ares", () -> new MagicRelicItem(GodOfWarMagic.FIRE_OF_ARES));
    public static final RegistrySupplier<Item> MAGIC_ICE_OF_POSEIDON =
        ITEMS.register("magic_ice_of_poseidon", () -> new MagicRelicItem(GodOfWarMagic.ICE_OF_POSEIDON));
    public static final RegistrySupplier<Item> MAGIC_LIGHTNING_OF_ZEUS =
        ITEMS.register("magic_lightning_of_zeus", () -> new MagicRelicItem(GodOfWarMagic.LIGHTNING_OF_ZEUS));
    public static final RegistrySupplier<Item> MAGIC_SOUL_OF_HADES =
        ITEMS.register("magic_soul_of_hades", () -> new MagicRelicItem(GodOfWarMagic.SOUL_OF_HADES));
    public static final RegistrySupplier<Item> MAGIC_BLADE_OF_ARES_SLASH =
        ITEMS.register("magic_blade_of_ares_slash", () -> new MagicRelicItem(GodOfWarMagic.BLADE_OF_ARES_SLASH));

    // Curios Equippable Mythic Relics
    public static final RegistrySupplier<Item> GOLDEN_FLEECE =
        ITEMS.register("golden_fleece", () -> new GoldenFleeceItem(new Item.Properties()));

    public static final RegistrySupplier<Item> WINGS_OF_ICARUS =
        ITEMS.register("wings_of_icarus", () -> new WingsOfIcarusItem(new Item.Properties()));

    public static final RegistrySupplier<Item> AMULET_OF_OUROBOROS =
        ITEMS.register("amulet_of_ouroboros", () -> new AmuletOfOuroborosItem(new Item.Properties()));

    // Mythic Blocks & Altars
    public static final RegistrySupplier<Item> OLYMPUS_MARBLE_ITEM =
        ITEMS.register("olympus_marble", () -> new BlockItem(ModBlocks.OLYMPUS_MARBLE.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> SPARTAN_STONE_ITEM =
        ITEMS.register("spartan_stone", () -> new BlockItem(ModBlocks.SPARTAN_STONE.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> CHAOS_ASH_ORE_ITEM =
        ITEMS.register("chaos_ash_ore", () -> new BlockItem(ModBlocks.CHAOS_ASH_ORE.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> HEPHAESTUS_FORGE_ITEM =
        ITEMS.register("hephaestus_forge", () -> new BlockItem(ModBlocks.HEPHAESTUS_FORGE.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> PANDORAS_BOX_BLOCK_ITEM =
        ITEMS.register("pandoras_box_block", () -> new BlockItem(ModBlocks.PANDORAS_BOX_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> BIFROST_ALTAR_BLOCK_ITEM =
        ITEMS.register("bifrost_altar_block", () -> new BlockItem(ModBlocks.BIFROST_ALTAR_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> NORNIR_CHEST_ITEM =
        ITEMS.register("nornir_chest", () -> new BlockItem(ModBlocks.NORNIR_CHEST.get(), new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistrySupplier<Item> RUNIC_SEAL_ITEM =
        ITEMS.register("runic_seal", () -> new BlockItem(ModBlocks.RUNIC_SEAL.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    // Nornir Chest Permanent Progression Relics
    public static final RegistrySupplier<Item> IDUNN_APPLE =
        ITEMS.register("idunn_apple", () -> new IdunnAppleItem(new Item.Properties()));

    public static final RegistrySupplier<Item> HORN_OF_BLOOD_MEAD =
        ITEMS.register("horn_of_blood_mead", () -> new HornOfBloodMeadItem(new Item.Properties()));

    // Mythic Progression Materials (Forge, Boss Drops & Rituals)
    public static final RegistrySupplier<Item> CHAOS_ASH =
        ITEMS.register("chaos_ash", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistrySupplier<Item> OLYMPUS_ESSENCE =
        ITEMS.register("olympus_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistrySupplier<Item> ASGARDIAN_STEEL =
        ITEMS.register("asgardian_steel", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistrySupplier<Item> TITAN_CORE =
        ITEMS.register("titan_core", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    // Iconic God of War Armor Sets
    private static final String ARES_BONUS = "Fuerza I, Resistencia al Fuego y quema a quienes te atacan";
    public static final RegistrySupplier<Item> ARES_HELMET = ITEMS.register("ares_helmet",
        () -> new GodArmorItem(ModArmorMaterial.ARES, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ARES_BONUS));
    public static final RegistrySupplier<Item> ARES_CHESTPLATE = ITEMS.register("ares_chestplate",
        () -> new GodArmorItem(ModArmorMaterial.ARES, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ARES_BONUS));
    public static final RegistrySupplier<Item> ARES_LEGGINGS = ITEMS.register("ares_leggings",
        () -> new GodArmorItem(ModArmorMaterial.ARES, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ARES_BONUS));
    public static final RegistrySupplier<Item> ARES_BOOTS = ITEMS.register("ares_boots",
        () -> new GodArmorItem(ModArmorMaterial.ARES, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ARES_BONUS));

    private static final String ZEUS_BONUS = "Velocidad I, Salto I y 25% de invocar Rayo del Olimpo al golpear";
    public static final RegistrySupplier<Item> ZEUS_HELMET = ITEMS.register("zeus_helmet",
        () -> new GodArmorItem(ModArmorMaterial.ZEUS, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ZEUS_BONUS));
    public static final RegistrySupplier<Item> ZEUS_CHESTPLATE = ITEMS.register("zeus_chestplate",
        () -> new GodArmorItem(ModArmorMaterial.ZEUS, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ZEUS_BONUS));
    public static final RegistrySupplier<Item> ZEUS_LEGGINGS = ITEMS.register("zeus_leggings",
        () -> new GodArmorItem(ModArmorMaterial.ZEUS, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ZEUS_BONUS));
    public static final RegistrySupplier<Item> ZEUS_BOOTS = ITEMS.register("zeus_boots",
        () -> new GodArmorItem(ModArmorMaterial.ZEUS, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().rarity(Rarity.EPIC), ZEUS_BONUS));

    private static final String BERSERKER_BONUS = "Resistencia I y hasta +10 de daño adicional al bajar tu vida";
    public static final RegistrySupplier<Item> BERSERKER_HELMET = ITEMS.register("berserker_helmet",
        () -> new GodArmorItem(ModArmorMaterial.BERSERKER, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().rarity(Rarity.EPIC), BERSERKER_BONUS));
    public static final RegistrySupplier<Item> BERSERKER_CHESTPLATE = ITEMS.register("berserker_chestplate",
        () -> new GodArmorItem(ModArmorMaterial.BERSERKER, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().rarity(Rarity.EPIC), BERSERKER_BONUS));
    public static final RegistrySupplier<Item> BERSERKER_LEGGINGS = ITEMS.register("berserker_leggings",
        () -> new GodArmorItem(ModArmorMaterial.BERSERKER, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().rarity(Rarity.EPIC), BERSERKER_BONUS));
    public static final RegistrySupplier<Item> BERSERKER_BOOTS = ITEMS.register("berserker_boots",
        () -> new GodArmorItem(ModArmorMaterial.BERSERKER, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().rarity(Rarity.EPIC), BERSERKER_BONUS));

    // Spawn Eggs - Greek Pantheon Bosses
    public static final RegistrySupplier<Item> ZEUS_BOSS_SPAWN_EGG = registerEgg("zeus_boss_spawn_egg", ModEntities.ZEUS_BOSS, 0xE6ECF5, 0x3CC8FF);
    public static final RegistrySupplier<Item> ARES_BOSS_SPAWN_EGG = registerEgg("ares_boss_spawn_egg", ModEntities.ARES_BOSS, 0x5C1010, 0xFF6A00);
    public static final RegistrySupplier<Item> ATHENA_BOSS_SPAWN_EGG = registerEgg("athena_boss_spawn_egg", ModEntities.ATHENA_BOSS, 0xD8C79A, 0x4EC3E0);
    public static final RegistrySupplier<Item> POSEIDON_BOSS_SPAWN_EGG = registerEgg("poseidon_boss_spawn_egg", ModEntities.POSEIDON_BOSS, 0x0E3D66, 0x35E6FF);
    public static final RegistrySupplier<Item> HADES_BOSS_SPAWN_EGG = registerEgg("hades_boss_spawn_egg", ModEntities.HADES_BOSS, 0x26172E, 0xC84BFF);
    public static final RegistrySupplier<Item> HERMES_BOSS_SPAWN_EGG = registerEgg("hermes_boss_spawn_egg", ModEntities.HERMES_BOSS, 0xB8933B, 0xFFF2B2);
    public static final RegistrySupplier<Item> HELIOS_BOSS_SPAWN_EGG = registerEgg("helios_boss_spawn_egg", ModEntities.HELIOS_BOSS, 0xD98A1A, 0xFFF066);
    public static final RegistrySupplier<Item> HERCULES_BOSS_SPAWN_EGG = registerEgg("hercules_boss_spawn_egg", ModEntities.HERCULES_BOSS, 0x6E4926, 0xE0A832);
    public static final RegistrySupplier<Item> PERSEPHONE_BOSS_SPAWN_EGG = registerEgg("persephone_boss_spawn_egg", ModEntities.PERSEPHONE_BOSS, 0x381A40, 0xB5367C);
    public static final RegistrySupplier<Item> THANATOS_BOSS_SPAWN_EGG = registerEgg("thanatos_boss_spawn_egg", ModEntities.THANATOS_BOSS, 0x1A1822, 0x8F1D2C);

    // Spawn Eggs - Norse Pantheon Bosses
    public static final RegistrySupplier<Item> ODIN_BOSS_SPAWN_EGG = registerEgg("odin_boss_spawn_egg", ModEntities.ODIN_BOSS, 0x2C3545, 0xD4AF37);
    public static final RegistrySupplier<Item> THOR_BOSS_SPAWN_EGG = registerEgg("thor_boss_spawn_egg", ModEntities.THOR_BOSS, 0x5A3628, 0x52B6FF);
    public static final RegistrySupplier<Item> BALDUR_BOSS_SPAWN_EGG = registerEgg("baldur_boss_spawn_egg", ModEntities.BALDUR_BOSS, 0xC4B4A2, 0x3E7BFA);
    public static final RegistrySupplier<Item> SIGRUN_BOSS_SPAWN_EGG = registerEgg("sigrun_boss_spawn_egg", ModEntities.SIGRUN_BOSS, 0x262630, 0xF5C542);

    // Spawn Eggs - Primordial Titans
    public static final RegistrySupplier<Item> CRONOS_TITAN_SPAWN_EGG = registerEgg("cronos_titan_spawn_egg", ModEntities.CRONOS_TITAN, 0x5C4D3C, 0xC49A45);
    public static final RegistrySupplier<Item> GAIA_TITAN_SPAWN_EGG = registerEgg("gaia_titan_spawn_egg", ModEntities.GAIA_TITAN, 0x4A3F32, 0x4B8B3B);
    public static final RegistrySupplier<Item> ATLAS_TITAN_SPAWN_EGG = registerEgg("atlas_titan_spawn_egg", ModEntities.ATLAS_TITAN, 0x4F4844, 0xE86A17);
    public static final RegistrySupplier<Item> TYPHON_TITAN_SPAWN_EGG = registerEgg("typhon_titan_spawn_egg", ModEntities.TYPHON_TITAN, 0x5B7B94, 0xC7ECFF);
    public static final RegistrySupplier<Item> PERSES_TITAN_SPAWN_EGG = registerEgg("perses_titan_spawn_egg", ModEntities.PERSES_TITAN, 0x2E1A16, 0xFF4500);

    // Spawn Eggs - Greek & Norse Mobs
    public static final RegistrySupplier<Item> MINOTAUR_MOB_SPAWN_EGG = registerEgg("minotaur_mob_spawn_egg", ModEntities.MINOTAUR_MOB, 0x4F3124, 0xA88A64);
    public static final RegistrySupplier<Item> SIREN_MOB_SPAWN_EGG = registerEgg("siren_mob_spawn_egg", ModEntities.SIREN_MOB, 0x7A6D5D, 0x5CFFE1);
    public static final RegistrySupplier<Item> CYCLOPS_MOB_SPAWN_EGG = registerEgg("cyclops_mob_spawn_egg", ModEntities.CYCLOPS_MOB, 0x6E5D4E, 0xD93829);
    public static final RegistrySupplier<Item> GORGON_MOB_SPAWN_EGG = registerEgg("gorgon_mob_spawn_egg", ModEntities.GORGON_MOB, 0x265432, 0x7CFF4D);
    public static final RegistrySupplier<Item> CERBERUS_MOB_SPAWN_EGG = registerEgg("cerberus_mob_spawn_egg", ModEntities.CERBERUS_MOB, 0x291512, 0xFF551A);
    public static final RegistrySupplier<Item> HARPY_MOB_SPAWN_EGG = registerEgg("harpy_mob_spawn_egg", ModEntities.HARPY_MOB, 0x4D2A2E, 0xC75B39);
    public static final RegistrySupplier<Item> SATYR_MOB_SPAWN_EGG = registerEgg("satyr_mob_spawn_egg", ModEntities.SATYR_MOB, 0x5E3E2A, 0xC9A043);
    public static final RegistrySupplier<Item> CENTAUR_MOB_SPAWN_EGG = registerEgg("centaur_mob_spawn_egg", ModEntities.CENTAUR_MOB, 0x593420, 0xC9362B);
    public static final RegistrySupplier<Item> DRAUGR_MOB_SPAWN_EGG = registerEgg("draugr_mob_spawn_egg", ModEntities.DRAUGR_MOB, 0x36302E, 0xFF7B1C);
    public static final RegistrySupplier<Item> WULVER_MOB_SPAWN_EGG = registerEgg("wulver_mob_spawn_egg", ModEntities.WULVER_MOB, 0x464B52, 0xB82929);
    public static final RegistrySupplier<Item> EINHERJAR_MOB_SPAWN_EGG = registerEgg("einherjar_mob_spawn_egg", ModEntities.EINHERJAR_MOB, 0x2D3142, 0xB266FF);

    // Spawn Eggs - Roman Legion
    public static final RegistrySupplier<Item> ROMAN_WARRIOR_SPAWN_EGG = registerEgg("roman_warrior_spawn_egg", ModEntities.ROMAN_WARRIOR, 0x7A1C1C, 0xB8BDC4);
    public static final RegistrySupplier<Item> ROMAN_ARCHER_SPAWN_EGG = registerEgg("roman_archer_spawn_egg", ModEntities.ROMAN_ARCHER, 0x6B3B2A, 0xC49B54);
    public static final RegistrySupplier<Item> ROMAN_BERSERKER_SPAWN_EGG = registerEgg("roman_berserker_spawn_egg", ModEntities.ROMAN_BERSERKER, 0x541818, 0xD96B27);
    public static final RegistrySupplier<Item> ROMAN_CENTURION_SPAWN_EGG = registerEgg("roman_centurion_spawn_egg", ModEntities.ROMAN_CENTURION, 0x8C1414, 0xE6BE4C);
    public static final RegistrySupplier<Item> ROMAN_PRAETORIAN_SPAWN_EGG = registerEgg("roman_praetorian_spawn_egg", ModEntities.ROMAN_PRAETORIAN, 0x3D1452, 0xF0C848);

    // Spawn Eggs - Mythical Creatures
    public static final RegistrySupplier<Item> PHOENIX_CREATURE_SPAWN_EGG = registerEgg("phoenix_creature_spawn_egg", ModEntities.PHOENIX_CREATURE, 0xB82A14, 0xFFD23F);
    public static final RegistrySupplier<Item> PEGASUS_CREATURE_SPAWN_EGG = registerEgg("pegasus_creature_spawn_egg", ModEntities.PEGASUS_CREATURE, 0xEBEFF5, 0xE6C667);
    public static final RegistrySupplier<Item> HIPPOCAMPUS_CREATURE_SPAWN_EGG = registerEgg("hippocampus_creature_spawn_egg", ModEntities.HIPPOCAMPUS_CREATURE, 0x155E8A, 0x6BF2FF);
    public static final RegistrySupplier<Item> FENRIR_CREATURE_SPAWN_EGG = registerEgg("fenrir_creature_spawn_egg", ModEntities.FENRIR_CREATURE, 0x343B45, 0x6BCBFF);

    // Spawn Eggs - Villagers
    public static final RegistrySupplier<Item> SPARTAN_SMITH_VILLAGER_SPAWN_EGG = registerEgg("spartan_smith_villager_spawn_egg", ModEntities.SPARTAN_SMITH_VILLAGER, 0x8C2318, 0xD4A24C);
    public static final RegistrySupplier<Item> GREEK_ORACLE_VILLAGER_SPAWN_EGG = registerEgg("greek_oracle_villager_spawn_egg", ModEntities.GREEK_ORACLE_VILLAGER, 0xDDD6C5, 0x4EC3E0);
    public static final RegistrySupplier<Item> DWARF_FORGER_VILLAGER_SPAWN_EGG = registerEgg("dwarf_forger_villager_spawn_egg", ModEntities.DWARF_FORGER_VILLAGER, 0x3B5E8C, 0xE0B44A);
    public static final RegistrySupplier<Item> ROMAN_QUARTERMASTER_VILLAGER_SPAWN_EGG = registerEgg("roman_quartermaster_villager_spawn_egg", ModEntities.ROMAN_QUARTERMASTER_VILLAGER, 0x7A2020, 0xD9C282);

    private static <T extends Mob> RegistrySupplier<Item> registerEgg(
        String id,
        RegistrySupplier<EntityType<T>> entityType,
        int primaryColor,
        int secondaryColor
    ) {
        return ITEMS.register(id, () -> new ArchitecturySpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Properties()));
    }

    public static void register() {
        ITEMS.register();
    }
}
