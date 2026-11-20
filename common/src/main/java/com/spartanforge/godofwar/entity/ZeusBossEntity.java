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
import net.minecraft.world.phys.Vec3;

/**
 * Zeus, Rey del Olimpo (Saga Griega)
 * Fase 1: Rayo del Olimpo + Onda Celestial.
 * Fase 2 (Forma Astral): Tormenta de 3 rayos + Teletransporte eléctrico con descarga.
 */
public class ZeusBossEntity extends AbstractGodBossEntity {
    public ZeusBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.zeus_boss", BossEvent.BossBarColor.BLUE, 600);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 800.0D)
            .add(Attributes.ATTACK_DAMAGE, 16.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.31D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Rayo del Olimpo dirigido (+ descarga eléctrica)
        Vec3 pos = target.position();
        strikeLightning(level, pos, true);
        target.hurt(this.damageSources().lightningBolt(), phaseTwo ? 16.0F : 12.0F);
        beamAttack(level, target, 24.0D, phaseTwo ? 10.0F : 7.0F, ParticleTypes.ELECTRIC_SPARK);
        level.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.HOSTILE, 1.4F, 1.05F);

        if (phaseTwo) {
            // Dos rayos extra alrededor del objetivo en Forma Astral
            for (int i = -1; i <= 1; i += 2) {
                Vec3 offset = pos.add(i * 3.0D, 0, (this.random.nextDouble() - 0.5) * 3.0D);
                strikeLightning(level, offset, true);
            }
        }
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        if (phaseTwo) {
            // Teletransporte Astral cerca del objetivo + Onda de Choque Celestial
            Vec3 behind = target.position().add(
                (this.random.nextDouble() - 0.5) * 5.0D,
                0.2D,
                (this.random.nextDouble() - 0.5) * 5.0D
            );
            ringParticles(level, this.position().add(0, 1.0, 0), 2.5D, 24, ParticleTypes.ELECTRIC_SPARK);
            this.teleportTo(behind.x, target.getY(), behind.z);
            strikeLightning(level, this.position(), true);
        }
        radialShockwave(level, phaseTwo ? 8.5D : 6.5D, phaseTwo ? 18.0F : 13.0F, 1.6D, 0.55D,
            ParticleTypes.ELECTRIC_SPARK, SoundEvents.LIGHTNING_BOLT_THUNDER);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.ELECTRIC_SPARK; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Desata su Forma Astral del Olimpo!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "walk"; }
    @Override protected String getCastAnim() { return "thunderbolt_cast"; }
}
