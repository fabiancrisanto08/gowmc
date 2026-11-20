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
 * Helios, Dios del Sol (Saga Griega)
 * Habilidades: Rayo Solar Incandescente | Destello Cegador del Carro Solar
 */
public class HeliosBossEntity extends AbstractGodBossEntity {
    public HeliosBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.helios_boss", BossEvent.BossBarColor.YELLOW, 460);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 560.0D)
            .add(Attributes.ATTACK_DAMAGE, 13.0D)
            .add(Attributes.ARMOR, 12.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.31D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        beamAttack(level, target, 24.0D, phaseTwo ? 14.0F : 10.0F, ParticleTypes.FLAME);
        target.setSecondsOnFire(5);
        level.playSound(null, this.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 1.4F, 1.1F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Destello Solar Cegador en área
        radialShockwave(level, 7.5D, phaseTwo ? 14.0F : 10.0F, 1.1D, 0.35D,
            ParticleTypes.FLASH, SoundEvents.BEACON_POWER_SELECT);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.0D),
            v -> v != this && v.isAlive() && !(v instanceof AbstractGodBossEntity))) {
            e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, phaseTwo ? 70 : 45, 0));
            e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 0));
        }
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.FLAME; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Arde con la Corona del Sol!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "hover_chariot"; }
    @Override protected String getCastAnim() { return "solar_flare"; }
}
