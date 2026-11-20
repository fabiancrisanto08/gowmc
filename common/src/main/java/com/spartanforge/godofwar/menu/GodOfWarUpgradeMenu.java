package com.spartanforge.godofwar.menu;

import com.spartanforge.godofwar.combat.BloodOrbManager;
import com.spartanforge.godofwar.item.BladesOfAthenaItem;
import com.spartanforge.godofwar.item.BladesOfChaosItem;
import com.spartanforge.godofwar.item.WeaponUpgradeHelper;
import com.spartanforge.godofwar.registry.ModMenus;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

/**
 * Contenedor del Menú POTENCIADOR estilo God of War II (Rediseño 320x226 con Orbes de Sangre):
 * - Slot 0: Medallón dorado Omega (120, 102) — acepta armas, poderes divinos y reliquias Curios.
 * - Inventario 9x3 + Barra rápida 9x1 integrados en la esquina inferior izquierda (x=9, y=144 / 202).
 * - Botón 0: Potencia el objeto del Nivel 1 al 5 consumiendo Orbes de Sangre (Red Orbs).
 */
public class GodOfWarUpgradeMenu extends AbstractContainerMenu {
    public static final int BUTTON_UPGRADE = 0;
    public static final int OMEGA_SLOT_X = 77;
    public static final int OMEGA_SLOT_Y = 108;
    public static final int INV_START_X = 5;
    public static final int INV_START_Y = 153;
    public static final int INV_COL_STEP = 18;
    public static final int INV_ROW_STEP = 18;
    public static final int HOTBAR_Y = 212;

    private static final DustParticleOptions BLOOD_BURST =
        new DustParticleOptions(new Vector3f(0.95F, 0.1F, 0.1F), 1.3F);

    private final Container upgradeSlotContainer = new SimpleContainer(1) {
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    public GodOfWarUpgradeMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.UPGRADE_MENU.get(), containerId);

        // Slot 0: Medallón Dorado Omega
        this.addSlot(new Slot(upgradeSlotContainer, 0, OMEGA_SLOT_X, OMEGA_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return WeaponUpgradeHelper.isUpgradeableWeapon(stack.getItem());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        // Inventario principal del jugador (3 filas x 9 columnas — parte inferior izquierda)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, INV_START_X + col * INV_COL_STEP, INV_START_Y + row * INV_ROW_STEP));
            }
        }

        // Barra rápida del jugador (9 slots — parte inferior izquierda)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, INV_START_X + col * INV_COL_STEP, HOTBAR_Y));
        }
    }

    public static void openFor(ServerPlayer player) {
        BloodOrbManager.syncToClient(player, 0);
        player.openMenu(new SimpleMenuProvider(
            (id, inv, p) -> new GodOfWarUpgradeMenu(id, inv),
            Component.translatable("gui.godofwar.upgrade_menu")
        ));
    }

    public ItemStack getTargetItem() {
        return this.upgradeSlotContainer.getItem(0);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId != BUTTON_UPGRADE) {
            return false;
        }
        ItemStack stack = this.upgradeSlotContainer.getItem(0);
        if (stack.isEmpty() || !WeaponUpgradeHelper.isUpgradeableWeapon(stack.getItem())) {
            return false;
        }
        if (WeaponUpgradeHelper.isMaxLevel(stack)) {
            return false;
        }

        int bloodCost = WeaponUpgradeHelper.getBloodCost(stack);
        boolean creative = player.getAbilities().instabuild;

        if (!player.level().isClientSide && player instanceof ServerPlayer sp && player.level() instanceof ServerLevel serverLevel) {
            if (!creative && !BloodOrbManager.spendBlood(sp, bloodCost)) {
                sp.displayClientMessage(
                    Component.literal("Necesitas " + bloodCost + " Orbes de Sangre para potenciar este objeto.")
                        .withStyle(ChatFormatting.RED),
                    true
                );
                return false;
            }

            int nextInternal = WeaponUpgradeHelper.getLevel(stack) + 1;
            WeaponUpgradeHelper.setLevel(stack, nextInternal);
            this.upgradeSlotContainer.setChanged();
            this.broadcastChanges();

            int dispLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
            boolean isAthena = stack.getItem() instanceof BladesOfAthenaItem;
            boolean isAres = stack.getItem() instanceof BladesOfChaosItem;

            serverLevel.sendParticles(BLOOD_BURST, player.getX(), player.getY() + 1.1D, player.getZ(), 28, 0.65, 0.7, 0.65, 0.12);
            serverLevel.sendParticles(
                isAthena ? ParticleTypes.ELECTRIC_SPARK : ParticleTypes.FLAME,
                player.getX(), player.getY() + 1.1D, player.getZ(),
                30, 0.6, 0.65, 0.6, 0.12);

            serverLevel.playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.1F, 0.9F + dispLvl * 0.1F);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.2F, 1.15F);

            String extra = (isAres || isAthena) ? " (¡Apariencia 3D evolucionada!)" : "";
            sp.displayClientMessage(
                Component.literal("✦ " + stack.getHoverName().getString() + " potenciado al NIVEL " + dispLvl + "/5" + extra + " ✦")
                    .withStyle(dispLvl >= 5 ? ChatFormatting.GOLD : ChatFormatting.RED, ChatFormatting.BOLD),
                true
            );
            return true;
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            result = current.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(current, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (WeaponUpgradeHelper.isUpgradeableWeapon(current.getItem())) {
                    if (!this.moveItemStackTo(current, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 28) {
                    if (!this.moveItemStackTo(current, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(current, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (current.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, this.upgradeSlotContainer);
    }
}
