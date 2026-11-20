package com.spartanforge.godofwar.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Sello Rúnico de las Nornas.
 * Protege los Cofres de las Nornas en el Templo del Olimpo, la Cámara de las Valquirias
 * y las Cadenas del Tártaro. Puede destruirse a mano o lanzando el Hacha Leviatán / Lanza Draupnir.
 */
public class RunicSealBlock extends Block {
    public RunicSealBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            shatterEffects(serverLevel, pos, player);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * Llamado cuando un proyectil de God of War (Hacha Leviatán o Lanza Draupnir) impacta el sello.
     */
    public static void shatterByProjectile(ServerLevel level, BlockPos pos, Player owner) {
        level.destroyBlock(pos, false);
        shatterEffects(level, pos, owner);
    }

    private static void shatterEffects(ServerLevel level, BlockPos pos, Player player) {
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 28, 0.45, 0.45, 0.45, 0.08);
        level.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 24, 0.5, 0.5, 0.5, 0.2);
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.4F, 1.35F);
        level.playSound(null, pos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.5F);
        if (player != null) {
            int remaining = NornirChestBlock.countNearbySeals(level, pos, 26);
            if (remaining == 0) {
                player.displayClientMessage(
                    Component.literal("✦ ¡Todos los Sellos Rúnicos destruidos! El Cofre de las Nornas se ha desbloqueado ✦")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
            } else {
                player.displayClientMessage(
                    Component.literal("⚡ ¡Sello Rúnico destruido! Quedan " + remaining + " sello(s) cercano(s)")
                        .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), true);
            }
        }
    }
}
