package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;

public final class ModVillagers {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.VILLAGER_PROFESSION);

    public static final RegistrySupplier<VillagerProfession> SPARTAN_SMITH_VILLAGER =
        PROFESSIONS.register("spartan_smith_villager", () -> new VillagerProfession(
            "spartan_smith_villager", holder -> true, holder -> true, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH));

    public static final RegistrySupplier<VillagerProfession> GREEK_ORACLE_VILLAGER =
        PROFESSIONS.register("greek_oracle_villager", () -> new VillagerProfession(
            "greek_oracle_villager", holder -> true, holder -> true, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH));

    public static final RegistrySupplier<VillagerProfession> DWARF_FORGER_VILLAGER =
        PROFESSIONS.register("dwarf_forger_villager", () -> new VillagerProfession(
            "dwarf_forger_villager", holder -> true, holder -> true, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH));

    public static final RegistrySupplier<VillagerProfession> ROMAN_QUARTERMASTER_VILLAGER =
        PROFESSIONS.register("roman_quartermaster_villager", () -> new VillagerProfession(
            "roman_quartermaster_villager", holder -> true, holder -> true, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_WEAPONSMITH));

    public static void register() {
        PROFESSIONS.register();
    }
}
