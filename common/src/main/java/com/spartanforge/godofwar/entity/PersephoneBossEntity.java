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
 * Perséfone, Reina del Inframundo (Saga Griega)
 * Habilidades: Orbe de Oscuridad del Tártaro | Vuelo Siniestro con Onda Maldita
 */
public class PersephoneBossEntity extends AbstractGodBossEntity {
    public PersephoneBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.persephone_boss", BossEvent.BossBarColor.PURPLE, 480);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 600.0D)
            .add(Attributes.ATTACK_DAMAGE, 14.0D)
            .add(Attributes.ARMOR, 12.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.32D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.82D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        beamAttack(level, target, 22.0D, phaseTwo ? 14.0F : 10.0F, ParticleTypes.DRAGON_BREATH);
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1));
        level.playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 1.3F, 0.9F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        dashToward(target, 1.3D, 0.55D);
        radialShockwave(level, 7.0D, phaseTwo ? 15.0F : 11.0F, 1.4D, 0.45D,
            ParticleTypes.WITCH, SoundEvents.WITHER_AMBIENT);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.DRAGON_BREATH; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Despliega la Oscuridad del Inframundo!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "wing_hover"; }
    @Override protected String getCastAnim() { return "dark_orb_cast"; }
}
