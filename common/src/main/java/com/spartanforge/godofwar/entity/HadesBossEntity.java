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
 * Hades, Señor del Inframundo (Saga Griega)
 * Habilidades: Cadenas de Almas (Atrae y drena vida) | Ejército del Tártaro (Onda espectral + Wither)
 */
public class HadesBossEntity extends AbstractGodBossEntity {
    public HadesBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.hades_boss", BossEvent.BossBarColor.PURPLE, 560);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 780.0D)
            .add(Attributes.ATTACK_DAMAGE, 16.0D)
            .add(Attributes.ARMOR, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.29D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.92D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Gancho de las Garras de Hades: atrae al jugador y drena un poco de vida
        pullTarget(level, target, 1.55D, ParticleTypes.SOUL_FIRE_FLAME);
        target.hurt(this.damageSources().magic(), phaseTwo ? 12.0F : 9.0F);
        this.heal(phaseTwo ? 12.0F : 6.0F);
        level.playSound(null, this.blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.HOSTILE, 1.5F, 0.65F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Explosión de Almas del Inframundo
        radialShockwave(level, phaseTwo ? 8.5D : 6.5D, phaseTwo ? 16.0F : 12.0F, 1.2D, 0.4D,
            ParticleTypes.SOUL, SoundEvents.WITHER_SHOOT);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D),
            v -> v != this && v.isAlive() && !(v instanceof AbstractGodBossEntity))) {
            e.addEffect(new MobEffectInstance(MobEffects.WITHER, phaseTwo ? 100 : 60, 1));
        }
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.SOUL_FIRE_FLAME; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Reclama tu Alma para el Inframundo!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "heavy_walk"; }
    @Override protected String getCastAnim() { return "hook_throw"; }
}
