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
 * Gaia, Madre de la Tierra (Saga Griega)
 * Habilidades: Terremoto de Raíces | Regeneración de la Tierra (Fase 2)
 */
public class GaiaTitanEntity extends AbstractGodBossEntity {
    public GaiaTitanEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.gaia_titan", BossEvent.BossBarColor.GREEN, 950);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1300.0D)
            .add(Attributes.ATTACK_DAMAGE, 19.0D)
            .add(Attributes.ARMOR, 18.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.22D)
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        radialShockwave(level, phaseTwo ? 12.0D : 9.5D, phaseTwo ? 18.0F : 14.0F, 1.8D, 0.75D,
            ParticleTypes.COMPOSTER, SoundEvents.GENERIC_EXPLODE);
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1));
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        this.heal(phaseTwo ? 35.0F : 20.0F);
        ringParticles(level, this.position().add(0, 1.5, 0), 8.0D, 40, ParticleTypes.HAPPY_VILLAGER);
        radialShockwave(level, 10.0D, 12.0F, 1.5D, 0.65D, ParticleTypes.SPORE_BLOSSOM_AIR, SoundEvents.GRASS_BREAK);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.HAPPY_VILLAGER; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡La Madre Tierra despierta su furia tectónica!"; }
    @Override protected String getIdleAnim() { return "idle_breathe"; }
    @Override protected String getMoveAnim() { return "climb_slam"; }
    @Override protected String getCastAnim() { return "vine_eruption"; }
}
