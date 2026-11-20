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
 * Atlas, Portador del Mundo (Saga Griega)
 * Habilidades: Temblor de Atlas | Erupción de Magma Tectónico
 */
public class AtlasTitanEntity extends AbstractGodBossEntity {
    public AtlasTitanEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.atlas_titan", BossEvent.BossBarColor.RED, 950);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1300.0D)
            .add(Attributes.ATTACK_DAMAGE, 20.0D)
            .add(Attributes.ARMOR, 18.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.22D)
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        radialShockwave(level, phaseTwo ? 12.5D : 9.5D, phaseTwo ? 20.0F : 15.0F, 2.2D, 0.85D,
            ParticleTypes.LAVA, SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        beamAttack(level, target, 26.0D, phaseTwo ? 17.0F : 12.0F, ParticleTypes.FLAME);
        target.setSecondsOnFire(5);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.LAVA; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Atlas desata el Temblor del Mundo!"; }
    @Override protected String getIdleAnim() { return "idle_hold"; }
    @Override protected String getMoveAnim() { return "ground_pound"; }
    @Override protected String getCastAnim() { return "magma_throw"; }
}
