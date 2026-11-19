package com.spartanforge.godofwar.registry;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.block.OlympusMarbleBlock;
import com.spartanforge.godofwar.block.SpartanStoneBlock;
import com.spartanforge.godofwar.block.ChaosAshOreBlock;
import com.spartanforge.godofwar.block.HephaestusForgeBlock;
import com.spartanforge.godofwar.block.PandorasBoxBlockBlock;
import com.spartanforge.godofwar.block.BifrostAltarBlockBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(GodOfWarMod.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> OLYMPUS_MARBLE =
        BLOCKS.register("olympus_marble", () -> new OlympusMarbleBlock(BlockBehaviour.Properties.of()
            .strength(3.0f, 6.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> SPARTAN_STONE =
        BLOCKS.register("spartan_stone", () -> new SpartanStoneBlock(BlockBehaviour.Properties.of()
            .strength(4.0f, 8.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> CHAOS_ASH_ORE =
        BLOCKS.register("chaos_ash_ore", () -> new ChaosAshOreBlock(BlockBehaviour.Properties.of()
            .strength(2.5f, 6.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> HEPHAESTUS_FORGE =
        BLOCKS.register("hephaestus_forge", () -> new HephaestusForgeBlock(BlockBehaviour.Properties.of()
            .strength(6.0f, 12.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> PANDORAS_BOX_BLOCK =
        BLOCKS.register("pandoras_box_block", () -> new PandorasBoxBlockBlock(BlockBehaviour.Properties.of()
            .strength(7.5f, 15.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> BIFROST_ALTAR_BLOCK =
        BLOCKS.register("bifrost_altar_block", () -> new BifrostAltarBlockBlock(BlockBehaviour.Properties.of()
            .strength(5.0f, 10.0f)
            .requiresCorrectToolForDrops()));

    public static void register() {
        BLOCKS.register();
    }
}
