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
 * Tifón, Titán de los Vientos y la Escarcha (Saga Griega)
 * Habilidades: Vendaval Glacial (empuje masivo + congelación) | Fragmentos de Hielo
 */
public class TyphonTitanEntity extends AbstractGodBossEntity {
    public TyphonTitanEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.typhon_titan", BossEvent.BossBarColor.BLUE, 900);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1150.0D)
            .add(Attributes.ATTACK_DAMAGE, 18.0D)
            .add(Attributes.ARMOR, 16.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.24D)
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        beamAttack(level, target, 28.0D, phaseTwo ? 16.0F : 12.0F, ParticleTypes.SNOWFLAKE);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 2));
        target.setTicksFrozen(180);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        radialShockwave(level, phaseTwo ? 14.0D : 11.0D, phaseTwo ? 17.0F : 13.0F, 2.8D, 0.75D,
            ParticleTypes.CLOUD, SoundEvents.ENDER_DRAGON_FLAP);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.SNOWFLAKE; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Desata la Tormenta Ártica de Tifón!"; }
    @Override protected String getIdleAnim() { return "idle_inhale"; }
    @Override protected String getMoveAnim() { return "gale_blow"; }
    @Override protected String getCastAnim() { return "ice_shatter"; }
}
