package com.spartanforge.godofwar.entity;

import com.spartanforge.godofwar.combat.BloodOrbManager;
import com.spartanforge.godofwar.registry.ModEntities;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Orbe de Sangre (Red Orb) estilo God of War:
 * - Solo aparece 1 orbe por cada enemigo derrotado (cero lag).
 * - Lleva un valor escalado según la dificultad del enemigo o jefe.
 * - Flota con estela carmesí, se magnetiza suavemente hacia el jugador y suma Sangre al recogerlo.
 */
public class BloodOrbEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_BLOOD_VALUE =
        SynchedEntityData.defineId(BloodOrbEntity.class, EntityDataSerializers.INT);

    private static final DustParticleOptions BLOOD_DUST =
        new DustParticleOptions(new Vector3f(0.92F, 0.08F, 0.08F), 0.9F);

    private int age = 0;
    private Player followingPlayer;

    public BloodOrbEntity(EntityType<? extends BloodOrbEntity> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    public BloodOrbEntity(Level level, double x, double y, double z, int bloodValue) {
        this(ModEntities.BLOOD_ORB.get(), level);
        this.setPos(x, y, z);
        this.setBloodValue(Math.max(1, bloodValue));
        double vx = (level.random.nextDouble() - 0.5D) * 0.22D;
        double vz = (level.random.nextDouble() - 0.5D) * 0.22D;
        this.setDeltaMovement(vx, 0.28D, vz);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_BLOOD_VALUE, 50);
    }

    public int getBloodValue() {
        return this.entityData.get(DATA_BLOOD_VALUE);
    }

    public void setBloodValue(int value) {
        this.entityData.set(DATA_BLOOD_VALUE, Math.max(1, value));
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;

        // Desaparece tras 3 minutos para evitar acumulación de entidades
        if (!this.level().isClientSide && this.age >= 3600) {
            this.discard();
            return;
        }

        // Busca al jugador más cercano dentro de 11 bloques para atraerse hacia Kratos
        if (this.age > 6) {
            if (this.followingPlayer == null || this.followingPlayer.isSpectator()
                || !this.followingPlayer.isAlive() || this.followingPlayer.distanceToSqr(this) > 144.0D) {
                this.followingPlayer = this.level().getNearestPlayer(this, 11.0D);
            }
        }

        if (this.followingPlayer != null && !this.followingPlayer.isSpectator() && this.followingPlayer.isAlive()) {
            Vec3 target = new Vec3(
                this.followingPlayer.getX(),
                this.followingPlayer.getY() + this.followingPlayer.getBbHeight() * 0.55D,
                this.followingPlayer.getZ()
            );
            Vec3 diff = target.subtract(this.position());
            double distSqr = diff.lengthSqr();
            if (distSqr < 144.0D && distSqr > 0.001D) {
                double strength = 1.0D - Math.sqrt(distSqr) / 12.0D;
                Vec3 pull = diff.normalize().scale(0.14D + strength * strength * 0.22D);
                this.setDeltaMovement(this.getDeltaMovement().scale(0.84D).add(pull));
            }
        } else {
            // Gravedad suave cuando no sigue a nadie
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.025D, 0.0D));
            }
            this.setDeltaMovement(this.getDeltaMovement().scale(0.96D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.82D, -0.5D, 0.82D));
        }

        // Estela ligera de sangre carmesí en el cliente (1 partícula cada 3 ticks -> mínimo impacto de FPS)
        if (this.level().isClientSide && this.age % 3 == 0) {
            this.level().addParticle(BLOOD_DUST,
                this.getX() + (this.random.nextDouble() - 0.5D) * 0.2D,
                this.getY() + 0.22D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 0.2D,
                0.0D, 0.01D, 0.0D);
        }

        // Recogida automática al acercarse a un jugador
        if (!this.level().isClientSide && this.age >= 8) {
            Player picker = this.level().getNearestPlayer(this, 1.45D);
            if (picker instanceof ServerPlayer sp && !sp.isSpectator() && sp.isAlive()) {
                collectBy(sp);
            }
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide && this.age >= 8 && player instanceof ServerPlayer sp && !sp.isSpectator()) {
            collectBy(sp);
        }
    }

    private void collectBy(ServerPlayer player) {
        if (this.isRemoved()) return;
        int amount = this.getBloodValue();
        BloodOrbManager.addBlood(player, amount);

        if (this.level() instanceof ServerLevel sl) {
            sl.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS,
                0.55F, 0.65F + this.random.nextFloat() * 0.25F);
            sl.sendParticles(BLOOD_DUST, player.getX(), player.getY() + 1.0D, player.getZ(),
                amount >= 1000 ? 18 : 6, 0.35, 0.45, 0.35, 0.04);
        }
        this.discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.age = tag.getInt("Age");
        this.setBloodValue(Math.max(1, tag.getInt("BloodValue")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.age);
        tag.putInt("BloodValue", this.getBloodValue());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
