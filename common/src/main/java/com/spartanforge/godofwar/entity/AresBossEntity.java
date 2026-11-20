package com.spartanforge.godofwar.entity;

import com.spartanforge.godofwar.entity.base.AbstractGodBossEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Ares, Dios de la Guerra (Saga Griega)
 * Habilidades: Embestida Arácnida en Llamas | Anillo de Pilares Ígneos | Furia de Guerra (Fase 2)
 */
public class AresBossEntity extends AbstractGodBossEntity {
    public AresBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.ares_boss", BossEvent.BossBarColor.RED, 550);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 720.0D)
            .add(Attributes.ATTACK_DAMAGE, 15.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.30D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Embestida de los brazos arácnidos + ráfaga de fuego
        dashToward(target, phaseTwo ? 1.55D : 1.25D, 0.38D);
        beamAttack(level, target, 14.0D, phaseTwo ? 14.0F : 10.0F, ParticleTypes.FLAME);
        target.setSecondsOnFire(phaseTwo ? 6 : 4);
        level.playSound(null, this.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.4F, 0.75F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Explosión de pilares de fuego alrededor de Ares
        radialShockwave(level, phaseTwo ? 8.5D : 6.5D, phaseTwo ? 17.0F : 12.0F, 1.4D, 0.55D,
            ParticleTypes.FLAME, SoundEvents.GENERIC_EXPLODE);
        level.sendParticles(ParticleTypes.LAVA, this.getX(), this.getY() + 1.5, this.getZ(),
            phaseTwo ? 35 : 20, 3.5, 1.5, 3.5, 0.1);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D),
            v -> v != this && v.isAlive() && !(v instanceof AbstractGodBossEntity))) {
            e.setSecondsOnFire(6);
        }
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.FLAME; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Arde con la Furia del Dios de la Guerra!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "march"; }
    @Override protected String getCastAnim() { return "spider_arms_slash"; }
}
