package com.spartanforge.godofwar.entity.projectile;

import com.spartanforge.godofwar.block.RunicSealBlock;
import com.spartanforge.godofwar.combat.StunExecutionManager;
import com.spartanforge.godofwar.registry.ModEntities;
import com.spartanforge.godofwar.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Proyectil de la Lanza Draupnir: se clava en enemigos o bloques brillando con viento dorado
 * y explota simultáneamente cuando el jugador detona sus lanzas (Shift + Clic Derecho).
 */
public class DraupnirSpearProjectileEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> DATA_STUCK =
        SynchedEntityData.defineId(DraupnirSpearProjectileEntity.class, EntityDataSerializers.BOOLEAN);

    private float bonusDamage = 0.0F;
    private int ticksAlive = 0;
    private int stuckEntityId = -1;

    public DraupnirSpearProjectileEntity(EntityType<? extends DraupnirSpearProjectileEntity> type, Level level) {
        super(type, level);
    }

    public DraupnirSpearProjectileEntity(Level level, LivingEntity owner, float bonusDamage) {
        super(ModEntities.DRAUPNIR_SPEAR_PROJECTILE.get(), owner, level);
        this.bonusDamage = bonusDamage;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STUCK, false);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DRAUPNIR_SPEAR.get();
    }

    public boolean isStuck() {
        return this.entityData.get(DATA_STUCK);
    }

    @Override
    protected float getGravity() {
        return this.ticksAlive < 10 ? 0.005F : 0.03F;
    }

    @Override
    public void tick() {
        this.ticksAlive++;
        if (!this.level().isClientSide && this.ticksAlive > 400) {
            this.discard();
            return;
        }

        if (this.entityData.get(DATA_STUCK)) {
            this.setDeltaMovement(Vec3.ZERO);
            if (this.stuckEntityId != -1) {
                Entity pinned = this.level().getEntity(this.stuckEntityId);
                if (pinned != null && pinned.isAlive()) {
                    this.setPos(pinned.getX(), pinned.getY() + pinned.getBbHeight() * 0.65D, pinned.getZ());
                } else {
                    this.stuckEntityId = -1;
                }
            }
            if (this.level() instanceof ServerLevel sl && this.ticksAlive % 5 == 0) {
                sl.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 0.1, this.getZ(), 2, 0.2, 0.2, 0.2, 0.04);
            }
            return;
        }

        super.tick();
        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 1, 0.05, 0.05, 0.05, 0.01);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.entityData.get(DATA_STUCK)) return;
        Entity hit = result.getEntity();
        Entity owner = this.getOwner();
        if (hit == owner) return;

        if (this.level() instanceof ServerLevel sl && hit instanceof LivingEntity living) {
            float dmg = 10.0F + this.bonusDamage * 0.6F;
            if (owner instanceof Player p) {
                living.hurt(this.damageSources().playerAttack(p), dmg);
                StunExecutionManager.addStun(sl, living, p, 14.0F);
            } else {
                living.hurt(this.damageSources().magic(), dmg);
            }
            this.stuckEntityId = living.getId();
            this.entityData.set(DATA_STUCK, true);
            this.setNoGravity(true);
            this.setDeltaMovement(Vec3.ZERO);
            sl.playSound(null, living.blockPosition(), SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 1.0F, 1.4F);
            sl.sendParticles(ParticleTypes.CRIT, living.getX(), living.getY() + 1.0, living.getZ(), 12, 0.4, 0.4, 0.4, 0.1);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.entityData.get(DATA_STUCK)) return;
        if (this.level() instanceof ServerLevel sl
            && sl.getBlockState(result.getBlockPos()).getBlock() instanceof RunicSealBlock) {
            Player p = this.getOwner() instanceof Player pl ? pl : null;
            RunicSealBlock.shatterByProjectile(sl, result.getBlockPos(), p);
            this.discard();
            return;
        }
        this.entityData.set(DATA_STUCK, true);
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
        if (this.level() instanceof ServerLevel sl) {
            sl.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.PLAYERS, 1.0F, 1.5F);
        }
    }

    public void detonate(ServerLevel level, Player owner) {
        AABB blast = this.getBoundingBox().inflate(4.5D);
        float dmg = 15.0F + this.bonusDamage;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, blast,
            e -> e != owner && e.isAlive())) {
            target.hurt(this.damageSources().playerAttack(owner), dmg);
            StunExecutionManager.addStun(level, target, owner, 24.0F);
            target.push(0, 0.55D, 0);
            target.hurtMarked = true;
        }
        level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.3, this.getZ(), 3, 0.5, 0.5, 0.5, 0.02);
        level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 0.4, this.getZ(), 24, 1.2, 0.8, 1.2, 0.25);
        level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.35F);
        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("BonusDamage", this.bonusDamage);
        tag.putBoolean("Stuck", this.entityData.get(DATA_STUCK));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.entityData.set(DATA_STUCK, tag.getBoolean("Stuck"));
    }
}
