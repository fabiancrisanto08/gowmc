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
 * Thor, Dios del Trueno (Saga Nórdica)
 * Habilidades: Lanzamiento de Mjölnir Eléctrico | Aplauso Sónico + Tormenta de Asgard
 */
public class ThorBossEntity extends AbstractGodBossEntity {
    public ThorBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.thor_boss", BossEvent.BossBarColor.BLUE, 600);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 820.0D)
            .add(Attributes.ATTACK_DAMAGE, 18.0D)
            .add(Attributes.ARMOR, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.30D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Lanzamiento y retorno de Mjölnir cargado de rayo
        beamAttack(level, target, 24.0D, phaseTwo ? 16.0F : 12.0F, ParticleTypes.ELECTRIC_SPARK);
        strikeLightning(level, target.position(), true);
        target.push(0, 0.45D, 0);
        target.hurtMarked = true;
        level.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.HOSTILE, 1.5F, 0.8F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Salto eléctrico + Aplauso Sónico de Mjölnir
        dashToward(target, 1.5D, 0.55D);
        radialShockwave(level, phaseTwo ? 9.0D : 7.0D, phaseTwo ? 19.0F : 14.0F, 1.9D, 0.65D,
            ParticleTypes.ELECTRIC_SPARK, SoundEvents.LIGHTNING_BOLT_THUNDER);
        if (phaseTwo) {
            for (int i = 0; i < 3; i++) {
                double ang = (2.0D * Math.PI * i) / 3.0D;
                strikeLightning(level, this.position().add(Math.cos(ang) * 5.0D, 0, Math.sin(ang) * 5.0D), true);
            }
        }
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.ELECTRIC_SPARK; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Desata toda la Furia de Mjölnir!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "heavy_step"; }
    @Override protected String getCastAnim() { return "mjolnir_throw"; }
}
