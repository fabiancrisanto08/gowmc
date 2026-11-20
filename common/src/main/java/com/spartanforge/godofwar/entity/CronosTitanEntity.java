package com.spartanforge.godofwar.entity;

import com.spartanforge.godofwar.entity.base.AbstractGodBossEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
 * Cronos, Titán del Tiempo (Saga Griega)
 * Habilidades: Palmada Colosal Sísmica | Distorsión del Tiempo (Ralentiza enemigos en área)
 */
public class CronosTitanEntity extends AbstractGodBossEntity {
    public CronosTitanEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.cronos_titan", BossEvent.BossBarColor.YELLOW, 1000);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1400.0D)
            .add(Attributes.ATTACK_DAMAGE, 20.0D)
            .add(Attributes.ARMOR, 18.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.22D)
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        radialShockwave(level, phaseTwo ? 13.0D : 10.0D, phaseTwo ? 20.0F : 15.0F, 2.2D, 0.85D,
            ParticleTypes.EXPLOSION, SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Distorsión Temporal de Cronos
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(18.0D),
            v -> v != this && v.isAlive() && !(v instanceof AbstractGodBossEntity))) {
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 2));
            e.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 90, 1));
        }
        ringParticles(level, this.position().add(0, 2.0, 0), 10.0D, 48, ParticleTypes.PORTAL);
        strikeLightning(level, target.position(), true);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.PORTAL; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡El Tiempo mismo tiembla ante Cronos!"; }
    @Override protected String getIdleAnim() { return "idle_breathe"; }
    @Override protected String getMoveAnim() { return "palm_slam"; }
    @Override protected String getCastAnim() { return "finger_pinch"; }
}
