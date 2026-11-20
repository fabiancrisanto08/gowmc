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
 * Tánatos, Dios Primordial de la Muerte (Saga Griega)
 * Habilidades: Corte de la Guadaña | Vórtice Mortal que Marchita
 */
public class ThanatosBossEntity extends AbstractGodBossEntity {
    public ThanatosBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.thanatos_boss", BossEvent.BossBarColor.RED, 540);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 700.0D)
            .add(Attributes.ATTACK_DAMAGE, 16.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.31D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        dashToward(target, 1.5D, 0.35D);
        beamAttack(level, target, 16.0D, phaseTwo ? 15.0F : 11.0F, ParticleTypes.SMOKE);
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, phaseTwo ? 2 : 1));
        level.playSound(null, this.blockPosition(), SoundEvents.WITHER_HURT, SoundSource.HOSTILE, 1.3F, 0.65F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        radialShockwave(level, phaseTwo ? 8.5D : 6.5D, phaseTwo ? 17.0F : 13.0F, 1.5D, 0.5D,
            ParticleTypes.SOUL, SoundEvents.WARDEN_SONIC_BOOM);
        this.heal(phaseTwo ? 16.0F : 8.0F);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.SOUL; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Despierta la Ira Primordial de la Muerte!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "stalk"; }
    @Override protected String getCastAnim() { return "scythe_cleave"; }
}
