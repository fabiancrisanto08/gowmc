package com.spartanforge.godofwar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Mármol del Olimpo (Saga Griega)
 */
public class OlympusMarbleBlock extends Block {
    public OlympusMarbleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            // Punto de extension para GUI / rituales del bloque (Bloque Constructivo con Vetas Doradas)
        }
        return InteractionResult.SUCCESS;
    }
}
