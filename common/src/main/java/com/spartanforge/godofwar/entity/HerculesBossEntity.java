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
 * Hércules, Semidiós de la Fuerza (Saga Griega)
 * Habilidades: Embestida de los Cestus de Nemea | Terremoto del León
 */
public class HerculesBossEntity extends AbstractGodBossEntity {
    public HerculesBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.hercules_boss", BossEvent.BossBarColor.YELLOW, 520);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 700.0D)
            .add(Attributes.ATTACK_DAMAGE, 17.0D)
            .add(Attributes.ARMOR, 14.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.29D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        dashToward(target, 1.45D, 0.35D);
        radialShockwave(level, 5.0D, phaseTwo ? 16.0F : 12.0F, 1.8D, 0.6D,
            ParticleTypes.CRIT, SoundEvents.ANVIL_LAND);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Doble impacto sísmico con los Cestus de Nemea
        radialShockwave(level, phaseTwo ? 9.0D : 7.0D, phaseTwo ? 20.0F : 15.0F, 2.1D, 0.75D,
            ParticleTypes.EXPLOSION, SoundEvents.GENERIC_EXPLODE);
        level.playSound(null, this.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.HOSTILE, 1.4F, 0.8F);
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.CRIT; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Ruge con el Poder de los Cestus de Nemea!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "stomp_walk"; }
    @Override protected String getCastAnim() { return "cestus_slam"; }
}
