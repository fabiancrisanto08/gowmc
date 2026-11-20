package com.spartanforge.godofwar.entity.projectile;

import com.spartanforge.godofwar.block.RunicSealBlock;
import com.spartanforge.godofwar.combat.StunExecutionManager;
import com.spartanforge.godofwar.item.LeviathanAxeItem;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Proyectil del Hacha Leviatán: se clava y congela enemigos al impactar,
 * y regresa volando directamente a la mano del jugador al llamarla.
 */
public class ThrownLeviathanAxeEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> DATA_RETURNING =
        SynchedEntityData.defineId(ThrownLeviathanAxeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_STUCK =
        SynchedEntityData.defineId(ThrownLeviathanAxeEntity.class, EntityDataSerializers.BOOLEAN);

    private float bonusDamage = 0.0F;
    private int ticksAlive = 0;

    public ThrownLeviathanAxeEntity(EntityType<? extends ThrownLeviathanAxeEntity> type, Level level) {
        super(type, level);
    }

    public ThrownLeviathanAxeEntity(Level level, LivingEntity owner, float bonusDamage) {
        super(ModEntities.THROWN_LEVIATHAN_AXE.get(), owner, level);
        this.bonusDamage = bonusDamage;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_RETURNING, false);
        this.entityData.define(DATA_STUCK, false);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.LEVIATHAN_AXE.get();
    }

    public boolean isReturning() {
        return this.entityData.get(DATA_RETURNING);
    }

    public boolean isStuck() {
        return this.entityData.get(DATA_STUCK);
    }

    public void startRecall() {
        this.entityData.set(DATA_STUCK, false);
        this.entityData.set(DATA_RETURNING, true);
        this.noPhysics = true;
        this.setNoGravity(true);
        if (this.level() instanceof ServerLevel sl) {
            sl.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1.3F, 1.1F);
        }
    }

    @Override
    public void tick() {
        this.ticksAlive++;
        Entity owner = this.getOwner();

        if (!this.level().isClientSide && (owner == null || !owner.isAlive() || this.ticksAlive > 600)) {
            if (owner instanceof Player p) {
                LeviathanAxeItem.clearThrownState(p, this.getUUID());
            }
            this.discard();
            return;
        }

        if (this.isReturning() && owner != null) {
            Vec3 target = owner.getEyePosition().subtract(0, 0.3D, 0);
            Vec3 diff = target.subtract(this.position());
            double dist = diff.length();

            if (dist < 1.8D) {
                if (!this.level().isClientSide && owner instanceof Player p) {
                    LeviathanAxeItem.clearThrownState(p, this.getUUID());
                    this.level().playSound(null, p.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 1.2F, 1.4F);
                    if (this.level() instanceof ServerLevel sl) {
                        sl.sendParticles(ParticleTypes.SNOWFLAKE, p.getX(), p.getY() + 1.0, p.getZ(), 16, 0.4, 0.4, 0.4, 0.08);
                    }
                }
                this.discard();
                return;
            }

            Vec3 vel = diff.normalize().scale(1.65D);
            this.setDeltaMovement(vel);
            this.setPos(this.getX() + vel.x, this.getY() + vel.y, this.getZ() + vel.z);

            if (this.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0.15, 0.15, 0.15, 0.02);
                // Daña y congela enemigos en el camino de regreso!
                for (LivingEntity hit : sl.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.2D),
                    e -> e != owner && e.isAlive())) {
                    if (owner instanceof Player p) {
                        hit.hurt(this.damageSources().playerAttack(p), 12.0F + bonusDamage * 0.7F);
                    }
                    hit.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
                    hit.setTicksFrozen(160);
                }
            }
            return;
        }

        if (this.entityData.get(DATA_STUCK)) {
            this.setDeltaMovement(Vec3.ZERO);
            if (this.level() instanceof ServerLevel sl && this.ticksAlive % 6 == 0) {
                sl.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY() + 0.2, this.getZ(), 2, 0.25, 0.25, 0.25, 0.01);
            }
            // Auto-recall after 12 seconds if forgotten
            if (!this.level().isClientSide && this.ticksAlive > 240) {
                startRecall();
            }
            return;
        }

        super.tick();
        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 2, 0.1, 0.1, 0.1, 0.02);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.isReturning()) return;
        Entity hit = result.getEntity();
        Entity owner = this.getOwner();
        if (hit == owner) return;

        if (this.level() instanceof ServerLevel sl && hit instanceof LivingEntity living) {
            float dmg = 17.0F + this.bonusDamage;
            if (owner instanceof Player p) {
                living.hurt(this.damageSources().playerAttack(p), dmg);
                StunExecutionManager.addStun(sl, living, p, 24.0F);
            } else {
                living.hurt(this.damageSources().magic(), dmg);
            }
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 3));
            living.setTicksFrozen(220);

            // Explosión de escarcha en área al impacto
            AABB area = living.getBoundingBox().inflate(3.0D);
            for (LivingEntity splash : sl.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != owner && e != living && e.isAlive())) {
                if (owner instanceof Player p) {
                    splash.hurt(this.damageSources().playerAttack(p), 8.0F + this.bonusDamage * 0.5F);
                }
                splash.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
                splash.setTicksFrozen(140);
            }
            sl.sendParticles(ParticleTypes.SNOWFLAKE, hit.getX(), hit.getY() + 1.0, hit.getZ(), 28, 1.0, 0.8, 1.0, 0.12);
            sl.playSound(null, hit.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.2F, 1.3F);
            // Queda suspendida/clavada lista para ser llamada de vuelta (o vuelve automáticamente tras 2s si golpeó en el aire)
            this.entityData.set(DATA_STUCK, true);
            this.setNoGravity(true);
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.isReturning()) return;
        if (this.level() instanceof ServerLevel sl
            && sl.getBlockState(result.getBlockPos()).getBlock() instanceof RunicSealBlock) {
            Player p = this.getOwner() instanceof Player pl ? pl : null;
            RunicSealBlock.shatterByProjectile(sl, result.getBlockPos(), p);
            this.startRecall();
            return;
        }
        this.entityData.set(DATA_STUCK, true);
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 18, 0.5, 0.4, 0.5, 0.08);
            sl.playSound(null, this.blockPosition(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.PLAYERS, 1.2F, 0.9F);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("BonusDamage", this.bonusDamage);
        tag.putBoolean("Returning", this.isReturning());
        tag.putBoolean("Stuck", this.entityData.get(DATA_STUCK));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.entityData.set(DATA_RETURNING, tag.getBoolean("Returning"));
        this.entityData.set(DATA_STUCK, tag.getBoolean("Stuck"));
    }
}
