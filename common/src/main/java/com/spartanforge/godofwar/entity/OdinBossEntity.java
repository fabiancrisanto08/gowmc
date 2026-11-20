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
import net.minecraft.world.phys.Vec3;

/**
 * Odín, Padre de Todos (Saga Nórdica)
 * Habilidades: Estocada Rúnica de Gungnir | Explosión del Bifrost y Teletransporte de Cuervos
 */
public class OdinBossEntity extends AbstractGodBossEntity {
    public OdinBossEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "entity.godofwar.odin_boss", BossEvent.BossBarColor.BLUE, 620);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 820.0D)
            .add(Attributes.ATTACK_DAMAGE, 16.0D)
            .add(Attributes.ARMOR, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.32D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }

    @Override
    protected void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Estocada de Gungnir con energía rúnica del Bifrost (no falla su objetivo)
        beamAttack(level, target, 26.0D, phaseTwo ? 15.0F : 11.0F, ParticleTypes.ENCHANT);
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 0));
        level.playSound(null, this.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 1.5F, 0.8F);
    }

    @Override
    protected void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo) {
        // Teletransporte de Cuervos de Hugin y Munin + Descarga del Bifrost
        Vec3 dest = target.position().add(
            (this.random.nextDouble() - 0.5) * 6.0D, 0.2D, (this.random.nextDouble() - 0.5) * 6.0D);
        level.sendParticles(ParticleTypes.SQUID_INK, this.getX(), this.getY() + 1.2, this.getZ(), 24, 0.8, 1.0, 0.8, 0.08);
        this.teleportTo(dest.x, target.getY(), dest.z);
        radialShockwave(level, phaseTwo ? 8.5D : 6.5D, phaseTwo ? 17.0F : 13.0F, 1.4D, 0.5D,
            ParticleTypes.PORTAL, SoundEvents.ENDERMAN_TELEPORT);
        if (phaseTwo) {
            strikeLightning(level, target.position(), true);
        }
    }

    @Override protected ParticleOptions getPhaseTwoParticle() { return ParticleTypes.PORTAL; }
    @Override protected String getPhaseTwoAnnouncement() { return "¡Canaliza la Magia Prohibida de los Nueve Reinos!"; }
    @Override protected String getIdleAnim() { return "idle"; }
    @Override protected String getMoveAnim() { return "levitate"; }
    @Override protected String getCastAnim() { return "gungnir_combo"; }
}
