package com.spartanforge.godofwar.entity.base;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

/**
 * Clase base para todos los Dioses Jefes y Titanes de God of War.
 * Gestiona barra de jefe, transición a Fase 2 al 50% de vida, habilidades especiales con cooldown
 * y sincronización de animaciones con GeckoLib.
 */
public abstract class AbstractGodBossEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Boolean> DATA_PHASE_TWO =
        SynchedEntityData.defineId(AbstractGodBossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_CAST_TICKS =
        SynchedEntityData.defineId(AbstractGodBossEntity.class, EntityDataSerializers.INT);

    private static final UUID PHASE_TWO_SPEED_UUID = UUID.fromString("9e7c2f34-1b2a-4c3d-8e5f-112233445566");
    private static final UUID PHASE_TWO_DAMAGE_UUID = UUID.fromString("9e7c2f34-1b2a-4c3d-8e5f-665544332211");

    protected final ServerBossEvent bossEvent;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected int primaryAbilityCooldown = 60;
    protected int secondaryAbilityCooldown = 130;
    protected int ambientTick = 0;

    protected AbstractGodBossEntity(
        EntityType<? extends Monster> type,
        Level level,
        String translationKey,
        BossEvent.BossBarColor barColor,
        int xpReward
    ) {
        super(type, level);
        this.xpReward = xpReward;
        this.bossEvent = new ServerBossEvent(
            Component.translatable(translationKey),
            barColor,
            BossEvent.BossBarOverlay.NOTCHED_10
        );
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE_TWO, false);
        this.entityData.define(DATA_CAST_TICKS, 0);
    }

    public boolean isPhaseTwo() {
        return this.entityData.get(DATA_PHASE_TWO);
    }

    public boolean isCasting() {
        return this.entityData.get(DATA_CAST_TICKS) > 0;
    }

    protected void triggerCastAnim(int ticks) {
        this.entityData.set(DATA_CAST_TICKS, ticks);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.85D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(Math.max(0.0F, Math.min(1.0F, this.getHealth() / this.getMaxHealth())));

        int cast = this.entityData.get(DATA_CAST_TICKS);
        if (cast > 0) {
            this.entityData.set(DATA_CAST_TICKS, cast - 1);
        }

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        // Check Phase 2 transition at 50% HP
        if (!this.isPhaseTwo() && this.getHealth() <= this.getMaxHealth() * 0.5F) {
            this.enterPhaseTwo(serverLevel);
        }

        // Ambient Phase 2 aura particles
        this.ambientTick++;
        if (this.isPhaseTwo() && this.ambientTick % 6 == 0) {
            serverLevel.sendParticles(
                getPhaseTwoParticle(),
                this.getX(), this.getY() + this.getBbHeight() * 0.55D, this.getZ(),
                4, this.getBbWidth() * 0.5D, this.getBbHeight() * 0.35D, this.getBbWidth() * 0.5D, 0.02D
            );
        }

        // Boss AI abilities when fighting a valid target
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || this.distanceToSqr(target) > 42.0D * 42.0D) {
            return;
        }

        if (this.primaryAbilityCooldown > 0) {
            this.primaryAbilityCooldown--;
        }
        if (this.secondaryAbilityCooldown > 0) {
            this.secondaryAbilityCooldown--;
        }

        boolean p2 = this.isPhaseTwo();
        if (this.secondaryAbilityCooldown <= 0) {
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.triggerCastAnim(18);
            this.performSecondaryAbility(serverLevel, target, p2);
            this.secondaryAbilityCooldown = p2 ? getSecondaryCooldownPhaseTwo() : getSecondaryCooldownBase();
        } else if (this.primaryAbilityCooldown <= 0) {
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.triggerCastAnim(16);
            this.performPrimaryAbility(serverLevel, target, p2);
            this.primaryAbilityCooldown = p2 ? getPrimaryCooldownPhaseTwo() : getPrimaryCooldownBase();
        }
    }

    protected void enterPhaseTwo(ServerLevel serverLevel) {
        this.entityData.set(DATA_PHASE_TWO, true);
        this.bossEvent.setDarkenScreen(true);
        this.triggerCastAnim(22);

        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null && speed.getModifier(PHASE_TWO_SPEED_UUID) == null) {
            speed.addTransientModifier(new AttributeModifier(PHASE_TWO_SPEED_UUID, "Phase Two Speed", 0.05D, AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance dmg = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (dmg != null && dmg.getModifier(PHASE_TWO_DAMAGE_UUID) == null) {
            dmg.addTransientModifier(new AttributeModifier(PHASE_TWO_DAMAGE_UUID, "Phase Two Power", 4.0D, AttributeModifier.Operation.ADDITION));
        }

        serverLevel.playSound(null, this.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.4F, 0.9F);
        ringParticles(serverLevel, this.position().add(0, 1.0, 0), 4.5D, 36, getPhaseTwoParticle());

        Component msg = Component.literal("⚔ ")
            .withStyle(ChatFormatting.GOLD)
            .append(this.getDisplayName().copy().withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
            .append(Component.literal(" — " + getPhaseTwoAnnouncement()).withStyle(ChatFormatting.YELLOW));
        for (Player player : serverLevel.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(36.0D))) {
            player.displayClientMessage(msg, true);
        }

        onEnterPhaseTwo(serverLevel);
    }

    protected void onEnterPhaseTwo(ServerLevel serverLevel) {
        radialShockwave(serverLevel, 6.0D, 10.0F, 1.4D, 0.5D, getPhaseTwoParticle(), SoundEvents.GENERIC_EXPLODE);
    }

    protected int getPrimaryCooldownBase() { return 90; }
    protected int getPrimaryCooldownPhaseTwo() { return 60; }
    protected int getSecondaryCooldownBase() { return 170; }
    protected int getSecondaryCooldownPhaseTwo() { return 115; }

    protected abstract void performPrimaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo);
    protected abstract void performSecondaryAbility(ServerLevel level, LivingEntity target, boolean phaseTwo);
    protected abstract ParticleOptions getPhaseTwoParticle();
    protected abstract String getPhaseTwoAnnouncement();
    protected abstract String getIdleAnim();
    protected abstract String getMoveAnim();
    protected abstract String getCastAnim();

    // ==================== SHARED COMBAT HELPERS ====================

    protected void strikeLightning(ServerLevel level, Vec3 pos, boolean visualOnly) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(pos.x, pos.y, pos.z);
            bolt.setVisualOnly(visualOnly);
            level.addFreshEntity(bolt);
        }
    }

    protected void radialShockwave(
        ServerLevel level,
        double radius,
        float damage,
        double knockback,
        double upward,
        ParticleOptions particle,
        SoundEvent sound
    ) {
        AABB box = this.getBoundingBox().inflate(radius);
        List<LivingEntity> victims = level.getEntitiesOfClass(LivingEntity.class, box,
            e -> e != this && e.isAlive() && !(e instanceof AbstractGodBossEntity));
        for (LivingEntity v : victims) {
            v.hurt(this.damageSources().mobAttack(this), damage);
            Vec3 push = v.position().subtract(this.position());
            double len = Math.max(0.1D, push.horizontalDistance());
            v.push((push.x / len) * knockback, upward, (push.z / len) * knockback);
            v.hurtMarked = true;
        }
        ringParticles(level, this.position().add(0, 0.5, 0), radius * 0.75D, 32, particle);
        level.playSound(null, this.blockPosition(), sound, SoundSource.HOSTILE, 1.3F, 0.9F);
    }

    protected void dashToward(LivingEntity target, double horizontalSpeed, double upward) {
        Vec3 dir = target.position().subtract(this.position());
        double horiz = Math.max(0.1D, dir.horizontalDistance());
        this.setDeltaMovement((dir.x / horiz) * horizontalSpeed, upward, (dir.z / horiz) * horizontalSpeed);
        this.hurtMarked = true;
    }

    protected void pullTarget(ServerLevel level, LivingEntity target, double strength, ParticleOptions trail) {
        Vec3 from = this.getEyePosition();
        Vec3 to = target.getEyePosition();
        for (int i = 1; i <= 12; i++) {
            double t = i / 12.0D;
            level.sendParticles(trail,
                from.x + (to.x - from.x) * t,
                from.y + (to.y - from.y) * t,
                from.z + (to.z - from.z) * t,
                2, 0.1D, 0.1D, 0.1D, 0.01D);
        }
        Vec3 pull = this.position().subtract(target.position()).normalize().scale(strength);
        target.setDeltaMovement(pull.x, 0.35D, pull.z);
        target.hurtMarked = true;
    }

    protected void beamAttack(ServerLevel level, LivingEntity target, double maxRange, float damage, ParticleOptions particle) {
        Vec3 start = this.getEyePosition();
        Vec3 dir = target.getEyePosition().subtract(start).normalize();
        double dist = Math.min(maxRange, start.distanceTo(target.getEyePosition()) + 2.0D);
        for (double d = 1.0D; d <= dist; d += 0.6D) {
            Vec3 p = start.add(dir.scale(d));
            level.sendParticles(particle, p.x, p.y, p.z, 3, 0.15D, 0.15D, 0.15D, 0.02D);
            AABB hitBox = new AABB(p.x - 0.9, p.y - 0.9, p.z - 0.9, p.x + 0.9, p.y + 0.9, p.z + 0.9);
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, hitBox,
                v -> v != this && v.isAlive() && !(v instanceof AbstractGodBossEntity))) {
                e.hurt(this.damageSources().mobAttack(this), damage);
            }
        }
    }

    protected void ringParticles(ServerLevel level, Vec3 center, double radius, int count, ParticleOptions particle) {
        for (int i = 0; i < count; i++) {
            double ang = (2.0D * Math.PI * i) / count;
            level.sendParticles(
                particle,
                center.x + Math.cos(ang) * radius,
                center.y,
                center.z + Math.sin(ang) * radius,
                2, 0.15D, 0.2D, 0.15D, 0.04D
            );
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, state -> {
            if (this.isCasting() || this.swinging) {
                return state.setAndContinue(RawAnimation.begin().thenPlay(getCastAnim()));
            }
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop(getMoveAnim()));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop(getIdleAnim()));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
