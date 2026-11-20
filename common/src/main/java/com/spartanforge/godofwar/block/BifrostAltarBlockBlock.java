package com.spartanforge.godofwar.block;

import com.spartanforge.godofwar.registry.ModEntities;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Altar del Bifrost (Saga Nórdica)
 * - Mano vacía: Purifica efectos negativos y recarga todos los enfriamientos de armas y magias del mod.
 * - Con Acero de Asgard (1-4): Invoca a un Dios Nórdico (1=Baldur, 2=Sigrun, 3=Thor, 4=Odín).
 * - Con Esencia del Olimpo (1-4): Invoca a un Dios Griego (1=Ares, 2=Poseidón, 3=Hades, 4=Zeus).
 * - Con Núcleo de Titán: Invoca un Titán Primordial aleatorio.
 */
public class BifrostAltarBlockBlock extends Block {
    public BifrostAltarBlockBlock(Properties properties) {
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

            // 1. Invocación de Dioses Nórdicos con Acero de Asgard
            if (held.is(ModItems.ASGARDIAN_STEEL.get())) {
                int tier = Math.min(4, held.getCount());
                EntityType<?> bossType = switch (tier) {
                    case 1 -> ModEntities.BALDUR_BOSS.get();
                    case 2 -> ModEntities.SIGRUN_BOSS.get();
                    case 3 -> ModEntities.THOR_BOSS.get();
                    default -> ModEntities.ODIN_BOSS.get();
                };
                if (!creative) held.shrink(tier);
                summonAtAltar(serverLevel, pos, bossType, player, "⚡ ¡El Bifrost se abre! Has invocado a ");
                return InteractionResult.SUCCESS;
            }

            // 2. Invocación de Dioses Griegos con Esencia del Olimpo
            if (held.is(ModItems.OLYMPUS_ESSENCE.get())) {
                int tier = Math.min(4, held.getCount());
                EntityType<?> bossType = switch (tier) {
                    case 1 -> ModEntities.ARES_BOSS.get();
                    case 2 -> ModEntities.POSEIDON_BOSS.get();
                    case 3 -> ModEntities.HADES_BOSS.get();
                    default -> ModEntities.ZEUS_BOSS.get();
                };
                if (!creative) held.shrink(tier);
                summonAtAltar(serverLevel, pos, bossType, player, "⚡ ¡El Olimpo desciende! Has invocado a ");
                return InteractionResult.SUCCESS;
            }

            // 3. Invocación de Titán Primordial con Núcleo de Titán
            if (held.is(ModItems.TITAN_CORE.get())) {
                EntityType<?>[] titans = {
                    ModEntities.CRONOS_TITAN.get(), ModEntities.GAIA_TITAN.get(),
                    ModEntities.ATLAS_TITAN.get(), ModEntities.TYPHON_TITAN.get(),
                    ModEntities.PERSES_TITAN.get()
                };
                EntityType<?> chosen = titans[serverLevel.random.nextInt(titans.length)];
                if (!creative) held.shrink(1);
                summonAtAltar(serverLevel, pos, chosen, player, "🌋 ¡Un Titán Primordial despierta: ");
                return InteractionResult.SUCCESS;
            }

            // 4. Purificación y recarga instantánea de todos los poderes y armas del mod!
            Item[] modCooldownItems = {
                ModItems.BLADES_OF_CHAOS.get(), ModItems.LEVIATHAN_AXE.get(), ModItems.BLADE_OF_OLYMPUS.get(),
                ModItems.DRAUPNIR_SPEAR.get(), ModItems.NEMEAN_CESTUS.get(), ModItems.CLAWS_OF_HADES.get(),
                ModItems.GUARDIAN_SHIELD.get(), ModItems.SPARTAN_RAGE_POWER.get(), ModItems.POSEIDONS_RAGE_POWER.get(),
                ModItems.ARMY_OF_HADES_POWER.get(), ModItems.MEDUSA_HEAD_POWER.get()
            };
            for (Item it : modCooldownItems) {
                player.getCooldowns().removeCooldown(it);
            }
            player.removeEffect(MobEffects.WITHER);
            player.removeEffect(MobEffects.POISON);
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            player.removeEffect(MobEffects.BLINDNESS);
            player.heal(6.0F);

            serverLevel.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 40, 0.8, 1.0, 0.8, 0.15);
            serverLevel.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 20, 0.6, 0.8, 0.6, 0.08);
            serverLevel.playSound(null, pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.2F, 1.2F);
            player.displayClientMessage(
                Component.literal("✦ ¡Energía del Bifrost restaurada! (Ofrece Acero de Asgard o Esencia del Olimpo para invocar jefes)")
                    .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD),
                true
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void summonAtAltar(ServerLevel level, BlockPos pos, EntityType<?> type, Player player, String prefix) {
        Entity entity = type.create(level);
        if (entity != null) {
            entity.moveTo(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, player.getYRot() + 180.0F, 0.0F);
            level.addFreshEntity(entity);
            level.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, 60, 1.2, 1.8, 1.2, 0.2);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, 30, 1.0, 1.5, 1.0, 0.1);
            level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.3F, 1.0F);
            player.displayClientMessage(
                Component.literal(prefix).withStyle(ChatFormatting.GOLD)
                    .append(entity.getDisplayName().copy().withStyle(ChatFormatting.RED, ChatFormatting.BOLD)),
                true
            );
        }
    }
}
