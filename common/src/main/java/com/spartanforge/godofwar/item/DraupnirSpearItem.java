package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.entity.projectile.DraupnirSpearProjectileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Lanza Draupnir (Saga Nórdica - Ragnarök)
 * Compatible con Epic Fight (Lanza 1 mano con Escudo / 2 manos con estela de viento dorado).
 * - Clic Derecho o [G]: Arroja lanzas rúnicas ilimitadas (hasta 6 clavadas).
 * - Shift + Clic Derecho o [Shift+G]: DETONA TODAS las lanzas clavadas a la vez.
 */
public class DraupnirSpearItem extends SwordItem implements GodOfWarWeaponAbility {
    private static final int MAX_ACTIVE_SPEARS = 6;

    public DraupnirSpearItem(Properties properties) {
        super(Tiers.NETHERITE, 9, -1.8F, properties);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        List<DraupnirSpearProjectileEntity> owned = serverLevel.getEntitiesOfClass(
            DraupnirSpearProjectileEntity.class,
            player.getBoundingBox().inflate(64.0D),
            sp -> sp.isAlive() && player.equals(sp.getOwner())
        );

        if (secondary) {
            if (owned.isEmpty()) {
                player.displayClientMessage(Component.literal("No hay lanzas Draupnir clavadas para detonar.").withStyle(ChatFormatting.GRAY), true);
            } else {
                int count = owned.size();
                for (DraupnirSpearProjectileEntity spear : owned) {
                    spear.detonate(serverLevel, player);
                }
                player.displayClientMessage(Component.literal("✦ ¡" + count + " Lanza(s) Draupnir detonadas! ✦")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
                player.getCooldowns().addCooldown(this, 24);
            }
        } else {
            if (owned.size() >= MAX_ACTIVE_SPEARS) {
                owned.get(0).detonate(serverLevel, player);
            }
            float bonus = WeaponUpgradeHelper.getBonusDamage(stack);
            DraupnirSpearProjectileEntity spear = new DraupnirSpearProjectileEntity(serverLevel, player, bonus);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.75F, 0.35F);
            serverLevel.addFreshEntity(spear);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.45F);
            player.getCooldowns().addCooldown(this, 10);
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
            triggerSpecialAbility(serverLevel, sp, stack, player.isShiftKeyDown());
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Nórdica (Ragnarök)",
            "Clic Derecho / [G]: Clava lanzas de viento (hasta 6 activas, rompe Sellos Rúnicos)",
            "Shift + Clic Derecho / [Shift+G]: Detona todas las lanzas a la vez");
    }
}
