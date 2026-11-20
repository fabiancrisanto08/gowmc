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
 * Atenea, Diosa de la Sabiduría (Saga Griega)
 * Habilidades: Estocada de Lanza Astral | Escudo Égida (Resistencia + Onda de Sabiduría)
 */
public class AthenaBossEntity extends AbstractGodBossEntity {
    public AthenaBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.athena_boss", BossEvent.BossBarColor.WHITE, 480);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 620.0D)
            .add(Attributes.ATTACK_DAMAGE, 13.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.33D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Estocada veloz con rayo astral
        dashToward(target, 1.35D, 0.25D);
        beamAttack(level, target, 15.0D, phaseTwo ? 13.0F : 10.0F, ParticleTypes.GLOW);
        level.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_THROW, SoundSource.HOSTILE, 1.3F, 1.3F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Égida de Atenea: se protege temporalmente y repele enemigos con luz divina
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, phaseTwo ? 1 : 0));
        radialShockwave(level, 6.5D, phaseTwo ? 14.0F : 10.0F, 1.7D, 0.45D,
            ParticleTypes.END_ROD, SoundEvents.BEACON_ACTIVATE);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.END_ROD; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Alza la Égida de la Sabiduría Divina!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "glide"; }
    @Override protected String getCastAnim() { return "spear_thrust"; }
}
