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
 * Poseidón, Señor de los Mares (Saga Griega)
 * Habilidades: Marejada del Tridente | Ira Eléctrica de Poseidón
 */
public class PoseidonBossEntity extends AbstractGodBossEntity {
    public PoseidonBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.poseidon_boss", BossEvent.BossBarColor.BLUE, 540);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 740.0D)
            .add(Attributes.ATTACK_DAMAGE, 15.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.30D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Chorro a presión del Tridente que lanza por los aires y ralentiza
        beamAttack(level, target, 20.0D, phaseTwo ? 14.0F : 10.0F, ParticleTypes.SPLASH);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
        target.push(0, 0.65D, 0);
        target.hurtMarked = true;
        level.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.HOSTILE, 1.4F, 0.9F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Ira de Poseidón: descarga de agua y rayos en área
        strikeLightning(level, target.position(), true);
        radialShockwave(level, phaseTwo ? 8.5D : 6.5D, phaseTwo ? 17.0F : 12.0F, 1.5D, 0.7D,
            ParticleTypes.BUBBLE_POP, SoundEvents.TRIDENT_THUNDER);
        ringParticles(level, this.position().add(0, 1.2, 0), 5.0D, 36, ParticleTypes.ELECTRIC_SPARK);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.SPLASH; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Desata la Tempestad de los Siete Mares!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "swim_hover"; }
    @Override protected String getCastAnim() { return "trident_strike"; }
}
