package com.spartanforge.godofwar.entity;

import com.spartanforge.godofwar.entity.base.AbstractGodBossEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Sigrun, Reina de las Valquirias (Saga Nórdica)
 * Habilidades: ¡VALHALLA! (Salto aéreo + pisotón devastador) | Giro de Alas Cuchilla | Destello Cegador
 */
public class SigrunBossEntity extends AbstractGodBossEntity {
    public SigrunBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.sigrun_boss", BossEvent.BossBarColor.YELLOW, 700);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 880.0D)
            .add(Attributes.ATTACK_DAMAGE, 17.0D)
            .add(Attributes.ARMOR, 16.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.36D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }

    @Override protected int getPrimaryCooldownBase() { return 75; }
    @Override protected int getPrimaryCooldownPhaseTwo() { return 50; }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Giro de Alas Cuchilla con embestida veloz
        dashToward(target, 1.7D, 0.35D);
        radialShockwave(level, 5.5D, phaseTwo ? 16.0F : 12.0F, 1.4D, 0.4D,
            ParticleTypes.SWEEP_ATTACK, SoundEvents.ENDER_DRAGON_FLAP);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // "¡VALHALLA!": Teletransporte sobre la cabeza del jugador + pisotón de choque + destello
        this.teleportTo(target.getX(), target.getY() + 3.5D, target.getZ());
        this.setDeltaMovement(0, -1.2D, 0);
        this.hurtMarked = true;
        radialShockwave(level, phaseTwo ? 8.0D : 6.0D, phaseTwo ? 19.0F : 14.0F, 1.7D, 0.55D,
            ParticleTypes.FLAME, SoundEvents.ANVIL_LAND);
        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 45, 0));
        level.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.HOSTILE, 1.4F, 1.3F);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.END_ROD; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡VALHALLA! La Reina de las Valquirias desata su furia!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "wing_hover"; }
    @Override protected String getCastAnim() { return "valhalla_stomp"; }
}
