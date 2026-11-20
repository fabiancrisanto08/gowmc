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
 * Baldur, El Invulnerable (Saga Nórdica)
 * Habilidades: Alternancia Elemental (Fuego de Muspelheim / Escarcha de Helheim) + Embestida Relámpago
 */
public class BaldurBossEntity extends AbstractGodBossEntity {
    private boolean fireStance = false;

    public BaldurBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.baldur_boss", BossEvent.BossBarColor.BLUE, 520);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 680.0D)
            .add(Attributes.ATTACK_DAMAGE, 14.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.36D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D);
    }

    @Override protected int getPrimaryCooldownBase() { return 70; }
    @Override protected int getPrimaryCooldownPhaseTwo() { return 48; }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        dashToward(target, 1.65D, 0.35D);
        ParticleOptions p = fireStance ? ParticleTypes.FLAME : ParticleTypes.SNOWFLAKE;
        radialShockwave(level, 4.5D, phaseTwo ? 14.0F : 10.0F, 1.5D, 0.4D, p, SoundEvents.PLAYER_ATTACK_STRONG);
        if (fireStance) {
            target.setSecondsOnFire(4);
        } else {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 2));
        }
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Cambio elemental entre Fuego y Hielo con onda explosiva
        fireStance = !fireStance;
        ParticleOptions p = fireStance ? ParticleTypes.FLAME : ParticleTypes.SNOWFLAKE;
        radialShockwave(level, 7.0D, phaseTwo ? 15.0F : 11.0F, 1.3D, 0.45D, p, SoundEvents.GLASS_BREAK);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0));
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return fireStance ? ParticleTypes.FLAME : ParticleTypes.SNOWFLAKE; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Siente el Dolor y Desata Hielo y Fuego!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "brawler_stance"; }
    @Override protected String getCastAnim() { return "elemental_shift"; }
}
