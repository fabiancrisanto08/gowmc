package com.spartanforge.godofwar.block;

import com.spartanforge.godofwar.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Cofre de las Nornas (God of War):
 * Permanece sellado mientras existan bloques RunicSealBlock en un radio de 24 bloques.
 * Al hacer clic derecho mientras está sellado, lanza rayos guía hacia los sellos restantes.
 * Al destruir todos los sellos, se abre y otorga una Manzana de Iðunn o un Cuerno de Hidromiel de Sangre
 * junto con tesoros míticos.
 */
public class NornirChestBlock extends Block {
    public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");
    private static final int SEAL_RADIUS = 24;

    public NornirChestBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(UNLOCKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNLOCKED);
    }

    public static List<BlockPos> findNearbySeals(Level level, BlockPos center, int radius) {
        List<BlockPos> seals = new ArrayList<>();
        BlockPos min = center.offset(-radius, -12, -radius);
        BlockPos max = center.offset(radius, 18, radius);
        for (BlockPos p : BlockPos.betweenClosed(min, max)) {
            if (level.getBlockState(p).getBlock() instanceof RunicSealBlock) {
                seals.add(p.immutable());
            }
        }
        return seals;
    }

    public static int countNearbySeals(Level level, BlockPos center, int radius) {
        return findNearbySeals(level, center, radius).size();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.CONSUME;
        }

        if (state.getValue(UNLOCKED)) {
            player.displayClientMessage(
                Component.literal("Este Cofre de las Nornas ya fue reclamado.").withStyle(ChatFormatting.GRAY), true);
            return InteractionResult.CONSUME;
        }

        List<BlockPos> seals = findNearbySeals(serverLevel, pos, SEAL_RADIUS);
        if (!seals.isEmpty()) {
            // Traza rastros de energía rúnica hacia cada sello restante para ayudar al jugador
            Vec3 from = Vec3.atCenterOf(pos).add(0, 0.6, 0);
            for (BlockPos seal : seals) {
                Vec3 to = Vec3.atCenterOf(seal);
                for (int i = 1; i <= 14; i++) {
                    double t = i / 14.0D;
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        from.x + (to.x - from.x) * t,
                        from.y + (to.y - from.y) * t + Math.sin(t * Math.PI) * 0.8D,
                        from.z + (to.z - from.z) * t,
                        1, 0.04, 0.04, 0.04, 0.0);
                }
            }
            serverLevel.playSound(null, pos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 0.7F);
            player.displayClientMessage(
                Component.literal("🔒 Cofre de las Nornas sellado — Destruye " + seals.size()
                    + " Sello(s) Rúnico(s) cercano(s) (¡usa tu Hacha o Lanza!)")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), true);
            return InteractionResult.CONSUME;
        }

        // ¡DESBLOQUEADO!
        serverLevel.setBlock(pos, state.setValue(UNLOCKED, true), 3);
        serverLevel.playSound(null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 1.1F, 1.15F);
        serverLevel.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1.2F, 0.85F);
        serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 45, 0.6, 0.7, 0.6, 0.2);

        // Recompensa principal: alterna entre Manzana de Iðunn y Cuerno de Hidromiel de Sangre
        boolean giveApple = serverLevel.random.nextBoolean();
        ItemStack relic = giveApple
            ? new ItemStack(ModItems.IDUNN_APPLE.get())
            : new ItemStack(ModItems.HORN_OF_BLOOD_MEAD.get());
        spawnReward(serverLevel, pos, relic);

        // Recompensa secundaria de materiales míticos
        spawnReward(serverLevel, pos, new ItemStack(ModItems.CHAOS_ASH.get(), 4 + serverLevel.random.nextInt(4)));
        spawnReward(serverLevel, pos, new ItemStack(
            serverLevel.random.nextBoolean() ? ModItems.OLYMPUS_ESSENCE.get() : ModItems.ASGARDIAN_STEEL.get(),
            2 + serverLevel.random.nextInt(2)));

        String relicName = giveApple ? "Manzana de Iðunn (+Vida Máxima)" : "Cuerno de Hidromiel de Sangre (+Furia/Stun)";
        player.displayClientMessage(
            Component.literal("✦ ¡Cofre de las Nornas abierto! Obtuviste: " + relicName + " ✦")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), false);

        return InteractionResult.CONSUME;
    }

    private static void spawnReward(ServerLevel level, BlockPos pos, ItemStack stack) {
        ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 1.15D, pos.getZ() + 0.5D, stack);
        drop.setDeltaMovement(
            (level.random.nextDouble() - 0.5D) * 0.16D,
            0.28D,
            (level.random.nextDouble() - 0.5D) * 0.16D);
        level.addFreshEntity(drop);
    }
}
