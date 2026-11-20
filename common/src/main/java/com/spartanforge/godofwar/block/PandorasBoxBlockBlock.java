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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Caja de Pandora (Saga Griega)
 * - Clic Derecho con Esencia del Olimpo: Desata la Llama del Olimpo (Bendición Divina de 60s).
 * - Clic Derecho con Núcleo de Titán: Forja y entrega la mítica Hoja del Olimpo.
 */
public class PandorasBoxBlockBlock extends Block {
    public PandorasBoxBlockBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            ItemStack held = player.getMainHandItem();
            boolean creative = player.getAbilities().instabuild;

            if (held.is(ModItems.TITAN_CORE.get())) {
                if (!creative) held.shrink(1);
                ItemStack blade = new ItemStack(ModItems.BLADE_OF_OLYMPUS.get());
                if (!player.addItem(blade)) {
                    player.drop(blade, false);
                }
                triggerOlympusBurst(serverLevel, pos);
                player.displayClientMessage(
                    Component.literal("⚡ ¡Has abierto la Caja de Pandora y obtenido la HOJA DEL OLIMPO! ⚡")
                        .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
                    true
                );
                return InteractionResult.SUCCESS;
            }

            if (held.is(ModItems.OLYMPUS_ESSENCE.get()) || creative) {
                if (!creative) held.shrink(1);
                int dur = 1200; // 60s
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, dur, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, dur, 1));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, dur, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, dur, 3));
                triggerOlympusBurst(serverLevel, pos);
                player.displayClientMessage(
                    Component.literal("✦ ¡La Llama del Olimpo imbuye tu cuerpo con el Poder de la Esperanza! ✦")
                        .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD),
                    true
                );
                return InteractionResult.SUCCESS;
            }

            player.displayClientMessage(
                Component.literal("Caja de Pandora: Ofrece 1x Esencia del Olimpo (Bendición Divina) o 1x Núcleo de Titán (Hoja del Olimpo).")
                    .withStyle(ChatFormatting.GOLD),
                true
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void triggerOlympusBurst(ServerLevel level, BlockPos pos) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 45, 0.8, 1.4, 0.8, 0.08);
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.4F, 1.1F);
    }
}
