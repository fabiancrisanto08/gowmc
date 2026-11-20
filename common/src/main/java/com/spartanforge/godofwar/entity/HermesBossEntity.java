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
import net.minecraft.world.phys.Vec3;

/**
 * Hermes, Mensajero Olímpico (Saga Griega)
 * Habilidades: Estela Supersónica | Patada Relámpago con Flanqueo Veloz
 */
public class HermesBossEntity extends AbstractGodBossEntity {
    public HermesBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.hermes_boss", BossEvent.BossBarColor.YELLOW, 420);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 480.0D)
            .add(Attributes.ATTACK_DAMAGE, 11.0D)
            .add(Attributes.ARMOR, 10.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.42D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.65D);
    }

    @Override protected int getPrimaryCooldownBase() { return 65; }
    @Override protected int getPrimaryCooldownPhaseTwo() { return 40; }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        dashToward(target, 1.85D, 0.32D);
        level.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.5, this.getZ(), 18, 0.6, 0.4, 0.6, 0.12);
        level.playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.HOSTILE, 1.2F, 1.6F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Flanqueo veloz a la espalda del jugador + patada de viento
        Vec3 behind = target.position().subtract(target.getLookAngle().scale(2.5D));
        this.teleportTo(behind.x, target.getY(), behind.z);
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, phaseTwo ? 2 : 1));
        radialShockwave(level, 4.5D, phaseTwo ? 13.0F : 9.0F, 1.4D, 0.4D,
            ParticleTypes.SWEEP_ATTACK, SoundEvents.PLAYER_ATTACK_SWEEP);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.CLOUD; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Rompe la Barrera del Sonido!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "sprint_blur"; }
    @Override protected String getCastAnim() { return "dash_kick"; }
}
