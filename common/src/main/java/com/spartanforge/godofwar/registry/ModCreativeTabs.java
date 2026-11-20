package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> GOW_TAB = TABS.register("god_of_war_tab", () ->
        CreativeTabRegistry.create(builder -> builder
            .title(Component.translatable("itemGroup.godofwar.god_of_war_tab"))
            .icon(() -> new ItemStack(ModItems.BLADES_OF_CHAOS.get()))
            .displayItems((params, output) -> {
                // Legendary Weapons
                output.accept(ModItems.BLADES_OF_CHAOS.get());
                output.accept(ModItems.BLADES_OF_ATHENA.get());
                output.accept(ModItems.LEVIATHAN_AXE.get());
                output.accept(ModItems.BLADE_OF_OLYMPUS.get());
                output.accept(ModItems.DRAUPNIR_SPEAR.get());
                output.accept(ModItems.NEMEAN_CESTUS.get());
                output.accept(ModItems.CLAWS_OF_HADES.get());
                output.accept(ModItems.GUARDIAN_SHIELD.get());

                // Godly Powers, Complete 24 Wiki Magics & Curios Relics
                output.accept(ModItems.SPARTAN_RAGE_POWER.get());
                output.accept(ModItems.POSEIDONS_RAGE_POWER.get());
                output.accept(ModItems.MEDUSA_HEAD_POWER.get());
                output.accept(ModItems.MAGIC_ZEUS_FURY.get());
                output.accept(ModItems.ARMY_OF_HADES_POWER.get());
                output.accept(ModItems.MAGIC_TYPHONS_BANE.get());
                output.accept(ModItems.MAGIC_CRONOS_RAGE.get());
                output.accept(ModItems.MAGIC_HEAD_OF_EURYALE.get());
                output.accept(ModItems.MAGIC_ATLAS_QUAKE.get());
                output.accept(ModItems.MAGIC_DIVINE_RECKONING.get());
                output.accept(ModItems.MAGIC_ARMY_OF_SPARTA.get());
                output.accept(ModItems.MAGIC_SOUL_SUMMON.get());
                output.accept(ModItems.MAGIC_NEMEAN_ROAR.get());
                output.accept(ModItems.MAGIC_NEMESIS_RAGE.get());
                output.accept(ModItems.MAGIC_EFREET.get());
                output.accept(ModItems.MAGIC_LIGHT_OF_DAWN.get());
                output.accept(ModItems.MAGIC_CHARONS_WRATH.get());
                output.accept(ModItems.MAGIC_EYE_OF_ATLANTIS.get());
                output.accept(ModItems.MAGIC_SCOURGE_OF_ERINYS.get());
                output.accept(ModItems.MAGIC_HORN_OF_BOREAS.get());
                output.accept(ModItems.MAGIC_FIRE_OF_ARES.get());
                output.accept(ModItems.MAGIC_ICE_OF_POSEIDON.get());
                output.accept(ModItems.MAGIC_LIGHTNING_OF_ZEUS.get());
                output.accept(ModItems.MAGIC_SOUL_OF_HADES.get());
                output.accept(ModItems.MAGIC_BLADE_OF_ARES_SLASH.get());
                output.accept(ModItems.GOLDEN_FLEECE.get());
                output.accept(ModItems.WINGS_OF_ICARUS.get());
                output.accept(ModItems.AMULET_OF_OUROBOROS.get());

                // Mythic Blocks & Altars
                output.accept(ModItems.OLYMPUS_MARBLE_ITEM.get());
                output.accept(ModItems.SPARTAN_STONE_ITEM.get());
                output.accept(ModItems.CHAOS_ASH_ORE_ITEM.get());
                output.accept(ModItems.HEPHAESTUS_FORGE_ITEM.get());
                output.accept(ModItems.PANDORAS_BOX_BLOCK_ITEM.get());
                output.accept(ModItems.BIFROST_ALTAR_BLOCK_ITEM.get());
                output.accept(ModItems.NORNIR_CHEST_ITEM.get());
                output.accept(ModItems.RUNIC_SEAL_ITEM.get());

                // Progression Materials & Nornir Relics
                output.accept(ModItems.IDUNN_APPLE.get());
                output.accept(ModItems.HORN_OF_BLOOD_MEAD.get());
                output.accept(ModItems.CHAOS_ASH.get());
                output.accept(ModItems.OLYMPUS_ESSENCE.get());
                output.accept(ModItems.ASGARDIAN_STEEL.get());
                output.accept(ModItems.TITAN_CORE.get());

                // Iconic Armor Sets
                output.accept(ModItems.ARES_HELMET.get());
                output.accept(ModItems.ARES_CHESTPLATE.get());
                output.accept(ModItems.ARES_LEGGINGS.get());
                output.accept(ModItems.ARES_BOOTS.get());

                output.accept(ModItems.ZEUS_HELMET.get());
                output.accept(ModItems.ZEUS_CHESTPLATE.get());
                output.accept(ModItems.ZEUS_LEGGINGS.get());
                output.accept(ModItems.ZEUS_BOOTS.get());

                output.accept(ModItems.BERSERKER_HELMET.get());
                output.accept(ModItems.BERSERKER_CHESTPLATE.get());
                output.accept(ModItems.BERSERKER_LEGGINGS.get());
                output.accept(ModItems.BERSERKER_BOOTS.get());

                // Greek Pantheon Bosses
                output.accept(ModItems.ZEUS_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.ARES_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.ATHENA_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.POSEIDON_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.HADES_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.HERMES_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.HELIOS_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.HERCULES_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.PERSEPHONE_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.THANATOS_BOSS_SPAWN_EGG.get());

                // Norse Pantheon Bosses
                output.accept(ModItems.ODIN_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.THOR_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.BALDUR_BOSS_SPAWN_EGG.get());
                output.accept(ModItems.SIGRUN_BOSS_SPAWN_EGG.get());

                // Primordial Titans
                output.accept(ModItems.CRONOS_TITAN_SPAWN_EGG.get());
                output.accept(ModItems.GAIA_TITAN_SPAWN_EGG.get());
                output.accept(ModItems.ATLAS_TITAN_SPAWN_EGG.get());
                output.accept(ModItems.TYPHON_TITAN_SPAWN_EGG.get());
                output.accept(ModItems.PERSES_TITAN_SPAWN_EGG.get());

                // Greek & Norse Mythical Mobs
                output.accept(ModItems.MINOTAUR_MOB_SPAWN_EGG.get());
                output.accept(ModItems.SIREN_MOB_SPAWN_EGG.get());
                output.accept(ModItems.CYCLOPS_MOB_SPAWN_EGG.get());
                output.accept(ModItems.GORGON_MOB_SPAWN_EGG.get());
                output.accept(ModItems.CERBERUS_MOB_SPAWN_EGG.get());
                output.accept(ModItems.HARPY_MOB_SPAWN_EGG.get());
                output.accept(ModItems.SATYR_MOB_SPAWN_EGG.get());
                output.accept(ModItems.CENTAUR_MOB_SPAWN_EGG.get());
                output.accept(ModItems.DRAUGR_MOB_SPAWN_EGG.get());
                output.accept(ModItems.WULVER_MOB_SPAWN_EGG.get());
                output.accept(ModItems.EINHERJAR_MOB_SPAWN_EGG.get());

                // Roman Legion
                output.accept(ModItems.ROMAN_WARRIOR_SPAWN_EGG.get());
                output.accept(ModItems.ROMAN_ARCHER_SPAWN_EGG.get());
                output.accept(ModItems.ROMAN_BERSERKER_SPAWN_EGG.get());
                output.accept(ModItems.ROMAN_CENTURION_SPAWN_EGG.get());
                output.accept(ModItems.ROMAN_PRAETORIAN_SPAWN_EGG.get());

                // Mythical Creatures
                output.accept(ModItems.PHOENIX_CREATURE_SPAWN_EGG.get());
                output.accept(ModItems.PEGASUS_CREATURE_SPAWN_EGG.get());
                output.accept(ModItems.HIPPOCAMPUS_CREATURE_SPAWN_EGG.get());
                output.accept(ModItems.FENRIR_CREATURE_SPAWN_EGG.get());

                // Villagers
                output.accept(ModItems.SPARTAN_SMITH_VILLAGER_SPAWN_EGG.get());
                output.accept(ModItems.GREEK_ORACLE_VILLAGER_SPAWN_EGG.get());
                output.accept(ModItems.DWARF_FORGER_VILLAGER_SPAWN_EGG.get());
                output.accept(ModItems.ROMAN_QUARTERMASTER_VILLAGER_SPAWN_EGG.get());
            })
        )
    );

    public static void register() {
        TABS.register();
    }
}
