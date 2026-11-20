package com.spartanforge.godofwar.entity;

import com.spartanforge.godofwar.entity.base.AbstractGodBossEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Perses, Titán Volcánico de la Destrucción (Saga Griega)
 * Habilidades: Puño de Lava | Rugido Volcánico Incandescente
 */
public class PersesTitanEntity extends AbstractGodBossEntity {
    public PersesTitanEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.perses_titan", BossEvent.BossBarColor.RED, 950);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1250.0D)
            .add(Attributes.ATTACK_DAMAGE, 19.0D)
            .add(Attributes.ARMOR, 17.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.23D)
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        radialShockwave(level, phaseTwo ? 12.5D : 9.5D, phaseTwo ? 19.0F : 14.0F, 2.0D, 0.75D,
            ParticleTypes.LAVA, SoundEvents.GENERIC_EXPLODE);
        target.setSecondsOnFire(7);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        beamAttack(level, target, 26.0D, phaseTwo ? 18.0F : 13.0F, ParticleTypes.FLAME);
        level.sendParticles(ParticleTypes.LAVA, this.getX(), this.getY() + 3.0, this.getZ(), 40, 4.0, 2.0, 4.0, 0.12);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.LAVA; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Perses entra en Erupción Volcánica!"; }
    @Override protected String getIdleAnim() { return "idle_burn"; }
    @Override protected String getMoveAnim() { return "lava_fist"; }
    @Override protected String getCastAnim() { return "volcanic_roar"; }
}
