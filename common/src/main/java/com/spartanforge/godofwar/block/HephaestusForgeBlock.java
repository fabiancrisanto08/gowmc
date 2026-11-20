package com.spartanforge.godofwar.block;

import com.spartanforge.godofwar.menu.GodOfWarUpgradeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Forja de Hefesto (Saga Griega):
 * Al interactuar abre directamente el Menú POTENCIADOR estilo God of War II
 * para evolucionar armas, poderes divinos y reliquias Curios usando Experiencia (XP).
 */
public class HephaestusForgeBlock extends Block {
    public HephaestusForgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.85F, 1.15F);
            GodOfWarUpgradeMenu.openFor(serverPlayer);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
