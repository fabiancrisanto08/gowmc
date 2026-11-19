package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.item.BladesOfChaosItem;
import com.spartanforge.godofwar.item.LeviathanAxeItem;
import com.spartanforge.godofwar.item.BladeOfOlympusItem;
import com.spartanforge.godofwar.item.DraupnirSpearItem;
import com.spartanforge.godofwar.item.NemeanCestusItem;
import com.spartanforge.godofwar.item.ClawsOfHadesItem;
import com.spartanforge.godofwar.item.GuardianShieldItem;
import com.spartanforge.godofwar.item.SpartanRagePowerItem;
import com.spartanforge.godofwar.item.PoseidonsRagePowerItem;
import com.spartanforge.godofwar.item.ArmyOfHadesPowerItem;
import com.spartanforge.godofwar.item.MedusaHeadPowerItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> BLADES_OF_CHAOS =
        ITEMS.register("blades_of_chaos", () -> new BladesOfChaosItem(new Item.Properties().stacksTo(1)));

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

    public static final RegistrySupplier<Item> SPARTAN_RAGE_POWER =
        ITEMS.register("spartan_rage_power", () -> new SpartanRagePowerItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> POSEIDONS_RAGE_POWER =
        ITEMS.register("poseidons_rage_power", () -> new PoseidonsRagePowerItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> ARMY_OF_HADES_POWER =
        ITEMS.register("army_of_hades_power", () -> new ArmyOfHadesPowerItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> MEDUSA_HEAD_POWER =
        ITEMS.register("medusa_head_power", () -> new MedusaHeadPowerItem(new Item.Properties().stacksTo(1)));

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

    public static void register() {
        ITEMS.register();
    }
}
