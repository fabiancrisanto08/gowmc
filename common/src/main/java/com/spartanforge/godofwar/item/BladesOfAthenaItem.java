package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.combat.StunExecutionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Espadas de Atenea (God of War I & II — Forjadas por Atenea en el Olimpo)
 * Evoluciona visual y mecánicamente del Nivel 1 al Nivel 5 en el menú POTENCIADOR [K]:
 * - Niv. 1: Acero olímpico plateado-bronce.
 * - Niv. 2: Filigrana dorada y daño eléctrico adicional.
 * - Niv. 3: Runas celestes encendidas + Ciclón de Atenea invoca rayos olímpicos.
 * - Niv. 4: Hoja dorada radiante + el Látigo encadena luz divina a enemigos cercanos.
 * - Niv. 5 (FORMA FINAL): Oro divino celestial puro + Juicio de Atenea (tormenta dorada + Velocidad II).
 */
public class BladesOfAthenaItem extends SwordItem implements GodOfWarWeaponAbility {
    public BladesOfAthenaItem(Properties properties) {
        super(Tiers.NETHERITE, 10, -1.75F, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int displayLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        if (target.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + 1.0, target.getZ(), 4 + displayLvl * 2, 0.3, 0.4, 0.3, 0.05);
            if (displayLvl >= 4) {
                sl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, target.getX(), target.getY() + 1.0, target.getZ(), 4, 0.25, 0.35, 0.25, 0.02);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean triggerSpecialAbility(ServerLevel serverLevel, ServerPlayer player, ItemStack stack, boolean secondary) {
        int displayLvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        float bonus = WeaponUpgradeHelper.getBonusDamage(stack);

        if (secondary) {
            // CICLÓN DE ATENEA (escala del Nivel 1 al 5)
            double radius = 5.2D + (displayLvl - 1) * 0.65D;
            AABB area = player.getBoundingBox().inflate(radius);
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area,
                t -> t != player && t.isAlive());

            int lightningCount = 0;
            int maxBolts = displayLvl >= 5 ? 6 : (displayLvl >= 3 ? 3 : 0);

            for (LivingEntity target : targets) {
                target.hurt(serverLevel.damageSources().playerAttack(player), 14.0F + bonus);
                StunExecutionManager.addStun(serverLevel, target, player, 18.0F + displayLvl * 3.0F);
                Vec3 away = target.position().subtract(player.position()).normalize().scale(0.8D + displayLvl * 0.1D);
                target.push(away.x, 0.4D, away.z);
                target.hurtMarked = true;

                if (lightningCount < maxBolts) {
                    LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                    if (bolt != null) {
                        bolt.moveTo(target.getX(), target.getY(), target.getZ());
                        bolt.setVisualOnly(true);
                        serverLevel.addFreshEntity(bolt);
                    }
                    lightningCount++;
                }
            }

            if (displayLvl >= 5) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, true));
                player.displayClientMessage(Component.literal("✦ ¡JUICIO CELESTIAL DE ATENEA! ✦").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), true);
            }

            for (int i = 0; i < 40; i++) {
                double ang = (2.0D * Math.PI * i) / 40.0D;
                double r = 3.2D + (displayLvl * 0.45D);
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    player.getX() + Math.cos(ang) * r, player.getY() + 1.0D, player.getZ() + Math.sin(ang) * r,
                    2, 0.15, 0.25, 0.15, 0.08);
                if (displayLvl >= 3) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        player.getX() + Math.cos(ang) * (r * 0.8D), player.getY() + 1.2D, player.getZ() + Math.sin(ang) * (r * 0.8D),
                        1, 0.1, 0.2, 0.1, 0.02);
                }
            }

            serverLevel.playSound(null, player.blockPosition(),
                displayLvl >= 3 ? SoundEvents.TRIDENT_THUNDER : SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS, 1.3F, 1.25F);
            player.getCooldowns().addCooldown(this, Math.max(36, 56 - displayLvl * 3));
        } else {
            // LÁTIGO DORADO DE ATENEA
            double reach = 15.0D + (displayLvl - 1) * 1.5D;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            LivingEntity hooked = null;
            double bestDist = reach;

            for (double d = 1.2D; d <= reach; d += 0.65D) {
                Vec3 p = start.add(look.scale(d));
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 2, 0.08, 0.08, 0.08, 0.02);
                if (displayLvl >= 3) {
                    serverLevel.sendParticles(ParticleTypes.WAX_ON, p.x, p.y, p.z, 1, 0.06, 0.06, 0.06, 0.01);
                }
                AABB probe = new AABB(p.x - 1.0, p.y - 1.0, p.z - 1.0, p.x + 1.0, p.y + 1.0, p.z + 1.0);
                List<LivingEntity> found = serverLevel.getEntitiesOfClass(LivingEntity.class, probe,
                    t -> t != player && t.isAlive());
                if (!found.isEmpty()) {
                    hooked = found.get(0);
                    bestDist = d;
                    break;
                }
            }

            if (hooked != null) {
                hooked.hurt(serverLevel.damageSources().playerAttack(player), 12.0F + bonus);
                StunExecutionManager.addStun(serverLevel, hooked, player, 22.0F + displayLvl * 2.5F);

                // Nivel 4+: Encadena arco de luz divina a enemigos adyacentes
                if (displayLvl >= 4) {
                    final LivingEntity primaryTarget = hooked;
                    for (LivingEntity chainTarget : serverLevel.getEntitiesOfClass(LivingEntity.class,
                        primaryTarget.getBoundingBox().inflate(4.5D), t -> t != player && t != primaryTarget && t.isAlive())) {
                        chainTarget.hurt(serverLevel.damageSources().magic(), 8.0F + bonus * 0.5F);
                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, chainTarget.getX(), chainTarget.getY() + 1.0, chainTarget.getZ(), 10, 0.3, 0.4, 0.3, 0.08);
                    }
                }

                if (hooked.getMaxHealth() > 150.0F) {
                    Vec3 dash = hooked.position().subtract(player.position()).normalize().scale(1.75D);
                    player.setDeltaMovement(dash.x, 0.45D, dash.z);
                    player.hurtMarked = true;
                } else {
                    Vec3 pull = player.position().subtract(hooked.position()).normalize().scale(Math.min(1.85D, bestDist * 0.22D));
                    hooked.setDeltaMovement(pull.x, 0.42D, pull.z);
                    hooked.hurtMarked = true;
                }
                serverLevel.playSound(null, hooked.blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.PLAYERS, 1.3F, 1.35F);
                player.getCooldowns().addCooldown(this, 26);
            } else {
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.25F);
                player.getCooldowns().addCooldown(this, 14);
            }
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
        int lvl = WeaponUpgradeHelper.getDisplayLevel(stack);
        tooltip.add(Component.literal("✦ Forma Visual: Espadas de Atenea — Fase " + lvl + "/5 (Cambia al mejorar)")
            .withStyle(ChatFormatting.GOLD));
        WeaponUpgradeHelper.appendWeaponTooltip(stack, tooltip, "Griega (Regalo de Atenea — GoW I/II)",
            "Clic Derecho / [G]: Látigo Dorado de Atenea (encadena luz al Niv. 4+)",
            "Shift + Clic Derecho / [Shift+G]: Ciclón de Atenea (rayos divinos al Niv. 3+)");
    }
}
