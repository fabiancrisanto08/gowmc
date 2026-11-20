package com.spartanforge.godofwar.network;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.client.ClientMagicState;
import com.spartanforge.godofwar.client.ClientStunState;
import com.spartanforge.godofwar.combat.StunExecutionManager;
import com.spartanforge.godofwar.compat.CuriosBridge;
import com.spartanforge.godofwar.item.GodOfWarWeaponAbility;
import com.spartanforge.godofwar.magic.MagicManager;
import com.spartanforge.godofwar.menu.GodOfWarUpgradeMenu;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

/**
 * Red multi-loader (Architectury) de God of War:
 * - C2S_WEAPON_ABILITY: Habilidad de arma [G]/[Shift+G] en Modo Batalla Epic Fight.
 * - C2S_ATTEMPT_EXECUTION: Remate brutal [V] (R3) sobre un enemigo aturdido.
 * - C2S_OPEN_UPGRADE_MENU: Abre el Menú POTENCIADOR estilo God of War II con [K].
 * - C2S_CURIO_POWER: Activa el Poder Divino equipado en Curios API con [X].
 * - S2C_SYNC_STUN / S2C_EXECUTION_FX: Barra de Stun y efectos cinemáticos en cliente.
 */
public final class GodOfWarNetwork {
    public static final ResourceLocation C2S_WEAPON_ABILITY =
        new ResourceLocation(GodOfWarMod.MOD_ID, "weapon_ability");
    public static final ResourceLocation C2S_ATTEMPT_EXECUTION =
        new ResourceLocation(GodOfWarMod.MOD_ID, "attempt_execution");
    public static final ResourceLocation C2S_OPEN_UPGRADE_MENU =
        new ResourceLocation(GodOfWarMod.MOD_ID, "open_upgrade_menu");
    public static final ResourceLocation C2S_CURIO_POWER =
        new ResourceLocation(GodOfWarMod.MOD_ID, "curio_power");
    public static final ResourceLocation S2C_SYNC_STUN =
        new ResourceLocation(GodOfWarMod.MOD_ID, "sync_stun");
    public static final ResourceLocation S2C_EXECUTION_FX =
        new ResourceLocation(GodOfWarMod.MOD_ID, "execution_fx");
    public static final ResourceLocation S2C_SYNC_BLOOD =
        new ResourceLocation(GodOfWarMod.MOD_ID, "sync_blood");
    public static final ResourceLocation S2C_SYNC_MAGIC =
        new ResourceLocation(GodOfWarMod.MOD_ID, "sync_magic");
    public static final ResourceLocation C2S_CYCLE_MAGIC =
        new ResourceLocation(GodOfWarMod.MOD_ID, "cycle_magic");
    public static final ResourceLocation C2S_EQUIP_MAGIC =
        new ResourceLocation(GodOfWarMod.MOD_ID, "equip_magic");
    public static final ResourceLocation C2S_UPGRADE_MAGIC =
        new ResourceLocation(GodOfWarMod.MOD_ID, "upgrade_magic");

    private GodOfWarNetwork() {}

    public static void registerServerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_WEAPON_ABILITY, (buf, ctx) -> {
            boolean secondary = buf.readBoolean();
            ctx.queue(() -> {
                if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
                if (!(player.level() instanceof ServerLevel serverLevel)) return;

                ItemStack main = player.getMainHandItem();
                if (main.getItem() instanceof GodOfWarWeaponAbility ability) {
                    if (player.getCooldowns().isOnCooldown(main.getItem())) return;
                    ability.triggerSpecialAbility(serverLevel, player, main, secondary);
                    return;
                }
                ItemStack off = player.getOffhandItem();
                if (off.getItem() instanceof GodOfWarWeaponAbility ability) {
                    if (player.getCooldowns().isOnCooldown(off.getItem())) return;
                    ability.triggerSpecialAbility(serverLevel, player, off, secondary);
                }
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_ATTEMPT_EXECUTION, (buf, ctx) -> {
            ctx.queue(() -> {
                if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
                if (!(player.level() instanceof ServerLevel serverLevel)) return;
                boolean executed = StunExecutionManager.attemptExecution(serverLevel, player);
                if (!executed) {
                    player.displayClientMessage(
                        Component.literal("No hay enemigos aturdidos (100% Stun) a tu alcance.")
                            .withStyle(ChatFormatting.DARK_GRAY), true);
                }
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_OPEN_UPGRADE_MENU, (buf, ctx) -> {
            ctx.queue(() -> {
                if (ctx.getPlayer() instanceof ServerPlayer player) {
                    GodOfWarUpgradeMenu.openFor(player);
                }
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_CURIO_POWER, (buf, ctx) -> {
            ctx.queue(() -> {
                if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
                if (!(player.level() instanceof ServerLevel serverLevel)) return;

                // Prioridad 1: Lanzar la Magia equipada en el Slot Activo del Menú de Magias [M]!
                if (MagicManager.castActiveMagic(player)) {
                    return;
                }

                // Prioridad 2: Fallback a Poder Divino equipado en Curios API
                ItemStack powerStack = CuriosBridge.getEquippedPower(player);
                if (powerStack.isEmpty() || !(powerStack.getItem() instanceof GodOfWarWeaponAbility ability)) {
                    player.displayClientMessage(
                        Component.literal("Equipa una Magia en el Menú [M] (o un Poder en Curios) para lanzarla con [X].")
                            .withStyle(ChatFormatting.GRAY), true);
                    return;
                }
                if (player.getCooldowns().isOnCooldown(powerStack.getItem())) {
                    return;
                }
                ability.triggerSpecialAbility(serverLevel, player, powerStack, player.isShiftKeyDown());
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_CYCLE_MAGIC, (buf, ctx) -> {
            ctx.queue(() -> {
                if (ctx.getPlayer() instanceof ServerPlayer player) {
                    MagicManager.cycleActiveSlot(player);
                }
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_EQUIP_MAGIC, (buf, ctx) -> {
            int slotIdx = buf.readVarInt();
            int magicOrd = buf.readVarInt();
            ctx.queue(() -> {
                if (ctx.getPlayer() instanceof ServerPlayer player) {
                    MagicManager.equipSlot(player, slotIdx, magicOrd);
                }
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, C2S_UPGRADE_MAGIC, (buf, ctx) -> {
            int magicOrd = buf.readVarInt();
            ctx.queue(() -> {
                if (ctx.getPlayer() instanceof ServerPlayer player) {
                    MagicManager.upgradeMagic(player, magicOrd);
                }
            });
        });
    }

    public static void registerClientReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2C_SYNC_STUN, (buf, ctx) -> {
            int entityId = buf.readVarInt();
            float stun = buf.readFloat();
            float maxStun = buf.readFloat();
            boolean stunned = buf.readBoolean();
            int ticksLeft = buf.readVarInt();
            ctx.queue(() -> ClientStunState.updateStun(entityId, stun, maxStun, stunned, ticksLeft));
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2C_EXECUTION_FX, (buf, ctx) -> {
            float intensity = buf.readFloat();
            int duration = buf.readVarInt();
            boolean boss = buf.readBoolean();
            String banner = buf.readUtf(128);
            ctx.queue(() -> ClientStunState.triggerExecutionEffects(intensity, duration, banner, boss));
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2C_SYNC_BLOOD, (buf, ctx) -> {
            int totalBlood = buf.readVarInt();
            int delta = buf.readVarInt();
            ctx.queue(() -> ClientStunState.updateBlood(totalBlood, delta));
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2C_SYNC_MAGIC, (buf, ctx) -> {
            int mask = buf.readInt();
            int[] slots = new int[] { buf.readVarInt(), buf.readVarInt(), buf.readVarInt() };
            int active = buf.readVarInt();
            byte[] lvls = buf.readByteArray(64);
            float mana = buf.readFloat();
            int[] cds = new int[] { buf.readVarInt(), buf.readVarInt(), buf.readVarInt() };
            ctx.queue(() -> ClientMagicState.update(mask, slots, active, lvls, mana, cds));
        });
    }

    // --- Envíos Cliente -> Servidor ---

    public static void sendWeaponAbility(boolean secondary) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(secondary);
        NetworkManager.sendToServer(C2S_WEAPON_ABILITY, buf);
    }

    public static void sendAttemptExecution() {
        NetworkManager.sendToServer(C2S_ATTEMPT_EXECUTION, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public static void sendOpenUpgradeMenu() {
        NetworkManager.sendToServer(C2S_OPEN_UPGRADE_MENU, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public static void sendCurioPower() {
        NetworkManager.sendToServer(C2S_CURIO_POWER, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public static void sendCycleMagicSlot() {
        NetworkManager.sendToServer(C2S_CYCLE_MAGIC, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public static void sendEquipMagicSlot(int slotIdx, int magicOrdinal) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(slotIdx);
        buf.writeVarInt(magicOrdinal);
        NetworkManager.sendToServer(C2S_EQUIP_MAGIC, buf);
    }

    public static void sendUpgradeMagic(int magicOrdinal) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(magicOrdinal);
        NetworkManager.sendToServer(C2S_UPGRADE_MAGIC, buf);
    }

    // --- Envíos Servidor -> Clientes ---

    public static void broadcastStun(ServerLevel level, double x, double y, double z,
                                     int entityId, float stun, float maxStun, boolean stunned, int ticksLeft) {
        AABB range = new AABB(x - 36, y - 24, z - 36, x + 36, y + 24, z + 36);
        for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, range)) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            buf.writeFloat(stun);
            buf.writeFloat(maxStun);
            buf.writeBoolean(stunned);
            buf.writeVarInt(ticksLeft);
            NetworkManager.sendToPlayer(player, S2C_SYNC_STUN, buf);
        }
    }

    public static void sendExecutionFx(ServerPlayer player, float intensity, int duration, boolean boss, String banner) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeFloat(intensity);
        buf.writeVarInt(duration);
        buf.writeBoolean(boss);
        buf.writeUtf(banner, 128);
        NetworkManager.sendToPlayer(player, S2C_EXECUTION_FX, buf);
    }

    public static void sendBloodSync(ServerPlayer player, int totalBlood, int delta) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(totalBlood);
        buf.writeVarInt(delta);
        NetworkManager.sendToPlayer(player, S2C_SYNC_BLOOD, buf);
    }

    public static void sendMagicSync(ServerPlayer player, int unlockedMask, int[] slots, int activeSlot,
                                     byte[] levels, float mana, int[] remCds) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(unlockedMask);
        for (int i = 0; i < 3; i++) buf.writeVarInt(slots[i]);
        buf.writeVarInt(activeSlot);
        buf.writeByteArray(levels);
        buf.writeFloat(mana);
        for (int i = 0; i < 3; i++) buf.writeVarInt(remCds[i]);
        NetworkManager.sendToPlayer(player, S2C_SYNC_MAGIC, buf);
    }
}
