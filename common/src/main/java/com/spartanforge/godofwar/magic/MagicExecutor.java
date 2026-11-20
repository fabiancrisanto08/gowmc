package com.spartanforge.godofwar.magic;

import com.spartanforge.godofwar.combat.BloodOrbManager;
import com.spartanforge.godofwar.combat.StunExecutionManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Ejecuta los efectos de combate, partículas, sonidos y mecánicas fieles a la saga
 * para TODAS las 24 Magias canónicas de la Wiki de God of War.
 */
public final class MagicExecutor {

    /** Efecto sostenido multi-tick (Ira de Cronos, Ejército de Esparta, Plaga de Erinias, Ira de Caronte). */
    public static final class ActiveEffect {
        public final GodOfWarMagic magic;
        public final UUID casterId;
        public final ServerLevel level;
        public double x, y, z;
        public final int levelRank;
        public int ticksRemaining;
        public final boolean followCaster;

        public ActiveEffect(GodOfWarMagic magic, ServerPlayer caster, ServerLevel level,
                            double x, double y, double z, int levelRank, int durationTicks, boolean followCaster) {
            this.magic = magic;
            this.casterId = caster.getUUID();
            this.level = level;
            this.x = x;
            this.y = y;
            this.z = z;
            this.levelRank = levelRank;
            this.ticksRemaining = durationTicks;
            this.followCaster = followCaster;
        }
    }

    private MagicExecutor() {}

    public static void execute(ServerLevel level, ServerPlayer player, GodOfWarMagic magic, int lvl,
                               List<ActiveEffect> activeEffects) {
        float mult = magic.getDamageMultiplier(lvl);
        double radBonus = (lvl - 1) * 0.85D;

        switch (magic) {
            // ================= GOD OF WAR (2005) =================
            case POSEIDONS_RAGE -> {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 45, 2));
                player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 160 + lvl * 25, 0));
                List<LivingEntity> targets = nearbyEnemies(level, player, 7.5D + radBonus);
                int bolts = 0;
                for (LivingEntity t : targets) {
                    t.hurt(level.damageSources().lightningBolt(), 15.0F * mult);
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                    StunExecutionManager.addStun(level, t, player, 24.0F * mult);
                    t.push(0, 0.72D, 0);
                    t.hurtMarked = true;
                    if (bolts++ < 5 + lvl) spawnVisualLightning(level, t.getX(), t.getY(), t.getZ());
                }
                ringParticles(level, player.getX(), player.getY() + 0.8D, player.getZ(), 2.5D, 7.0D, ParticleTypes.ELECTRIC_SPARK, 44);
                ringParticles(level, player.getX(), player.getY() + 0.5D, player.getZ(), 2.0D, 6.5D, ParticleTypes.SPLASH, 36);
                level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.3F, 1.15F);
                level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.2F, 0.9F);
            }

            case MEDUSA_GAZE -> {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 eye = player.getEyePosition();
                double range = 11.0D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, range)) {
                    Vec3 dir = t.getEyePosition().subtract(eye).normalize();
                    if (look.dot(dir) > 0.68D) {
                        int dur = 100 + lvl * 25;
                        t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur, 6));
                        t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, dur, 2));
                        t.addEffect(new MobEffectInstance(MobEffects.GLOWING, dur, 0));
                        t.hurt(level.damageSources().magic(), 8.0F * mult);
                        StunExecutionManager.addStun(level, t, player, 42.0F * mult);
                        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, t.getX(), t.getY() + 1.0D, t.getZ(), 18, 0.5, 0.8, 0.5, 0.04);
                    }
                }
                beamParticles(level, eye, look, range, ParticleTypes.TOTEM_OF_UNDYING, 26);
                level.playSound(null, player.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 0.95F, 1.35F);
            }

            case ZEUS_FURY -> {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 eye = player.getEyePosition();
                double range = 26.0D + lvl * 3.0D;
                LivingEntity hitTarget = findLineTarget(level, player, eye, look, range, 1.6D);
                Vec3 impact = hitTarget != null ? hitTarget.position() : eye.add(look.scale(18.0D));
                beamParticles(level, eye, look, eye.distanceTo(impact), ParticleTypes.ELECTRIC_SPARK, 34);
                spawnVisualLightning(level, impact.x, impact.y, impact.z);
                for (LivingEntity t : enemiesAround(level, player, impact.x, impact.y, impact.z, 4.2D + lvl * 0.5D)) {
                    t.hurt(level.damageSources().lightningBolt(), 16.0F * mult);
                    StunExecutionManager.addStun(level, t, player, 22.0F * mult);
                }
                level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.2F, 1.35F);
            }

            case ARMY_OF_HADES -> {
                List<LivingEntity> targets = nearbyEnemies(level, player, 10.5D + radBonus);
                float healed = 0.0F;
                for (LivingEntity t : targets) {
                    t.hurt(level.damageSources().magic(), 14.5F * mult);
                    t.addEffect(new MobEffectInstance(MobEffects.WITHER, 90 + lvl * 15, 1));
                    t.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
                    StunExecutionManager.addStun(level, t, player, 18.0F * mult);
                    level.sendParticles(ParticleTypes.SOUL, t.getX(), t.getY() + 1.0D, t.getZ(), 18, 0.5, 0.8, 0.5, 0.08);
                    level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, t.getX(), t.getY() + 0.6D, t.getZ(), 10, 0.4, 0.5, 0.4, 0.05);
                    healed += 1.6F;
                }
                if (healed > 0.0F) player.heal(Math.min(10.0F, healed));
                ringParticles(level, player.getX(), player.getY() + 1.0D, player.getZ(), 2.0D, 8.5D, ParticleTypes.SOUL, 48);
                level.playSound(null, player.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.7F, 1.45F);
            }

            // ================= GOD OF WAR II =================
            case TYPHONS_BANE -> {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 eye = player.getEyePosition();
                double range = 22.0D + lvl * 2.5D;
                for (LivingEntity t : nearbyEnemies(level, player, range)) {
                    Vec3 toT = t.getEyePosition().subtract(eye);
                    double proj = toT.dot(look);
                    if (proj > 0.5D && proj < range) {
                        double perp = toT.subtract(look.scale(proj)).length();
                        if (perp <= 2.3D + lvl * 0.2D) {
                            t.hurt(level.damageSources().magic(), 11.5F * mult);
                            t.setTicksFrozen(Math.min(260, t.getTicksFrozen() + 90));
                            t.push(look.x * 1.45D, 0.45D, look.z * 1.45D);
                            t.hurtMarked = true;
                            StunExecutionManager.addStun(level, t, player, 16.0F * mult);
                        }
                    }
                }
                beamParticles(level, eye, look, range * 0.85D, ParticleTypes.CLOUD, 36);
                beamParticles(level, eye, look, range * 0.85D, ParticleTypes.SNOWFLAKE, 26);
                level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.25F, 1.4F);
                level.playSound(null, player.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.1F, 0.75F);
            }

            case CRONOS_RAGE -> {
                Vec3 spot = player.position().add(player.getLookAngle().scale(3.5D));
                activeEffects.add(new ActiveEffect(magic, player, level, spot.x, spot.y + 1.3D, spot.z, lvl, 90 + lvl * 15, false));
                ringParticles(level, spot.x, spot.y + 1.3D, spot.z, 0.6D, 2.2D, ParticleTypes.ELECTRIC_SPARK, 32);
                level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.1F, 1.35F);
            }

            case HEAD_OF_EURYALE -> {
                double r = 8.5D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    int dur = 120 + lvl * 25;
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur, 6));
                    t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, dur, 3));
                    t.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1));
                    t.hurt(level.damageSources().magic(), 12.0F * mult);
                    StunExecutionManager.addStun(level, t, player, 48.0F * mult);
                    level.sendParticles(ParticleTypes.WITCH, t.getX(), t.getY() + 1.0D, t.getZ(), 16, 0.4, 0.8, 0.4, 0.05);
                }
                ringParticles(level, player.getX(), player.getY() + 1.0D, player.getZ(), 1.5D, r, ParticleTypes.TOTEM_OF_UNDYING, 48);
                level.playSound(null, player.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.15F, 0.85F);
            }

            case ATLAS_QUAKE -> {
                double r = 8.5D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().lava(), 17.5F * mult);
                    t.setSecondsOnFire(4 + lvl);
                    StunExecutionManager.addStun(level, t, player, 36.0F * mult);
                    Vec3 out = t.position().subtract(player.position()).normalize();
                    t.push(out.x * 1.1D, 0.95D, out.z * 1.1D);
                    t.hurtMarked = true;
                }
                ringParticles(level, player.getX(), player.getY() + 0.3D, player.getZ(), 1.5D, r, ParticleTypes.LAVA, 48);
                ringParticles(level, player.getX(), player.getY() + 0.6D, player.getZ(), 2.0D, r, ParticleTypes.EXPLOSION, 14);
                level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.4F, 0.65F);
            }

            // ================= GOD OF WAR III =================
            case DIVINE_RECKONING -> {
                double r = 9.0D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    Vec3 pull = player.position().subtract(t.position()).normalize();
                    t.push(pull.x * 0.9D, 0.85D, pull.z * 0.9D);
                    t.hurtMarked = true;
                    t.hurt(level.damageSources().magic(), 19.0F * mult);
                    StunExecutionManager.addStun(level, t, player, 32.0F * mult);
                }
                ringParticles(level, player.getX(), player.getY() + 1.2D, player.getZ(), 1.5D, r, ParticleTypes.END_ROD, 52);
                ringParticles(level, player.getX(), player.getY() + 0.8D, player.getZ(), 1.5D, r, ParticleTypes.ELECTRIC_SPARK, 42);
                level.playSound(null, player.blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.4F, 0.85F);
                level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.1F, 1.25F);
            }

            case ARMY_OF_SPARTA -> {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 65 + lvl * 10, 3));
                activeEffects.add(new ActiveEffect(magic, player, level, player.getX(), player.getY(), player.getZ(), lvl, 55, true));
                ringParticles(level, player.getX(), player.getY() + 1.0D, player.getZ(), 2.2D, 3.2D, ParticleTypes.FLAME, 40);
                level.playSound(null, player.blockPosition(), SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2).value(), SoundSource.PLAYERS, 1.3F, 0.9F);
            }

            case SOUL_SUMMON -> {
                double r = 9.5D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().wither(), 16.0F * mult);
                    t.addEffect(new MobEffectInstance(MobEffects.WITHER, 100 + lvl * 20, 2));
                    t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                    StunExecutionManager.addStun(level, t, player, 28.0F * mult);
                    level.sendParticles(ParticleTypes.DRAGON_BREATH, t.getX(), t.getY() + 1.0D, t.getZ(), 18, 0.5, 0.7, 0.5, 0.06);
                }
                ringParticles(level, player.getX(), player.getY() + 1.0D, player.getZ(), 2.0D, r, ParticleTypes.SOUL, 44);
                level.playSound(null, player.blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.PLAYERS, 0.9F, 1.25F);
            }

            case NEMEAN_ROAR -> {
                double r = 8.0D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().playerAttack(player), 15.0F * mult);
                    StunExecutionManager.addStun(level, t, player, 55.0F * mult); // Aturdimiento brutal fiel a los Cestus!
                    Vec3 out = t.position().subtract(player.position()).normalize();
                    t.push(out.x * 1.45D, 0.55D, out.z * 1.45D);
                    t.hurtMarked = true;
                }
                for (double rr : new double[]{2.5D, 5.0D, r}) {
                    ringParticles(level, player.getX(), player.getY() + 0.5D, player.getZ(), rr, rr, ParticleTypes.SONIC_BOOM, 8);
                    ringParticles(level, player.getX(), player.getY() + 0.7D, player.getZ(), rr, rr, ParticleTypes.END_ROD, 24);
                }
                level.playSound(null, player.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.4F, 0.75F);
            }

            case NEMESIS_RAGE -> {
                List<LivingEntity> chain = nearbyEnemies(level, player, 10.0D + radBonus);
                chain.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
                int maxChain = Math.min(chain.size(), 5 + lvl);
                Vec3 prev = player.getEyePosition();
                for (int i = 0; i < maxChain; i++) {
                    LivingEntity t = chain.get(i);
                    t.hurt(level.damageSources().lightningBolt(), 13.5F * mult);
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 75, 4));
                    StunExecutionManager.addStun(level, t, player, 26.0F * mult);
                    Vec3 cur = t.getEyePosition();
                    beamParticles(level, prev, cur.subtract(prev).normalize(), prev.distanceTo(cur), ParticleTypes.GLOW, 16);
                    prev = cur;
                }
                level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1.2F, 1.55F);
            }

            // ================= CHAINS OF OLYMPUS =================
            case EFREET -> {
                double r = 7.8D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().inFire(), 16.5F * mult);
                    t.setSecondsOnFire(6 + lvl);
                    StunExecutionManager.addStun(level, t, player, 26.0F * mult);
                    t.push(0, 0.75D, 0);
                    t.hurtMarked = true;
                    level.sendParticles(ParticleTypes.FLAME, t.getX(), t.getY() + 1.0D, t.getZ(), 22, 0.4, 1.1, 0.4, 0.08);
                }
                ringParticles(level, player.getX(), player.getY() + 0.8D, player.getZ(), 1.8D, r, ParticleTypes.FLAME, 56);
                ringParticles(level, player.getX(), player.getY() + 1.6D, player.getZ(), 2.0D, r, ParticleTypes.LAVA, 24);
                level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.4F, 0.65F);
                level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1.2F, 0.75F);
            }

            case LIGHT_OF_DAWN -> {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 eye = player.getEyePosition();
                double range = 24.0D + lvl * 2.5D;
                LivingEntity hit = findLineTarget(level, player, eye, look, range, 1.8D);
                Vec3 impact = hit != null ? hit.position() : eye.add(look.scale(16.0D));
                beamParticles(level, eye, look, eye.distanceTo(impact), ParticleTypes.END_ROD, 34);
                for (LivingEntity t : enemiesAround(level, player, impact.x, impact.y, impact.z, 4.5D + lvl * 0.4D)) {
                    float dmg = (t.getMobType() == MobType.UNDEAD ? 24.0F : 13.0F) * mult;
                    t.hurt(level.damageSources().magic(), dmg);
                    t.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0));
                    t.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 0));
                    StunExecutionManager.addStun(level, t, player, 22.0F * mult);
                }
                ringParticles(level, impact.x, impact.y + 0.8D, impact.z, 0.8D, 4.0D, ParticleTypes.FLASH, 3);
                level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5F, 1.25F);
            }

            case CHARONS_WRATH -> {
                Vec3 spot = player.position().add(player.getLookAngle().scale(4.5D));
                activeEffects.add(new ActiveEffect(magic, player, level, spot.x, spot.y + 0.6D, spot.z, lvl, 80 + lvl * 12, false));
                ringParticles(level, spot.x, spot.y + 0.8D, spot.z, 1.0D, 5.0D, ParticleTypes.HAPPY_VILLAGER, 36);
                level.playSound(null, player.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.4F, 0.75F);
            }

            // ================= GHOST OF SPARTA =================
            case EYE_OF_ATLANTIS -> {
                List<LivingEntity> chain = nearbyEnemies(level, player, 11.5D + radBonus);
                chain.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
                int maxTargets = Math.min(chain.size(), 4 + lvl);
                for (int i = 0; i < maxTargets; i++) {
                    LivingEntity t = chain.get(i);
                    t.hurt(level.damageSources().lightningBolt(), 15.0F * mult);
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 3));
                    StunExecutionManager.addStun(level, t, player, 25.0F * mult);
                    beamParticles(level, player.getEyePosition(), t.getEyePosition().subtract(player.getEyePosition()).normalize(),
                        player.distanceTo(t), ParticleTypes.ELECTRIC_SPARK, 18);
                }
                level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.2F, 1.3F);
            }

            case SCOURGE_OF_ERINYS -> {
                Vec3 spot = player.position().add(player.getLookAngle().scale(5.5D));
                activeEffects.add(new ActiveEffect(magic, player, level, spot.x, spot.y + 1.2D, spot.z, lvl, 65 + lvl * 10, false));
                ringParticles(level, spot.x, spot.y + 1.2D, spot.z, 0.8D, 4.5D, ParticleTypes.PORTAL, 48);
                level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.1F, 0.65F);
            }

            case HORN_OF_BOREAS -> {
                double r = 9.0D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().freeze(), 14.5F * mult);
                    t.setTicksFrozen(Math.min(400, t.getTicksFrozen() + 220 + lvl * 35));
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 110 + lvl * 15, 5));
                    StunExecutionManager.addStun(level, t, player, 32.0F * mult);
                }
                ringParticles(level, player.getX(), player.getY() + 1.2D, player.getZ(), 1.5D, r, ParticleTypes.SNOWFLAKE, 64);
                level.playSound(null, player.blockPosition(), SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(0).value(), SoundSource.PLAYERS, 1.4F, 0.72F);
                level.playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.3F);
            }

            // ================= ASCENSION =================
            case FIRE_OF_ARES -> {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 160 + lvl * 25, 1));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
                double r = 7.5D + radBonus;
                int hits = 0;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().inFire(), 15.0F * mult);
                    t.setSecondsOnFire(5 + lvl);
                    StunExecutionManager.addStun(level, t, player, 24.0F * mult);
                    hits++;
                }
                if (hits > 0) {
                    BloodOrbManager.addBlood(player, hits * (15 + lvl * 8)); // Bonus de Sangre de Ares!
                }
                ringParticles(level, player.getX(), player.getY() + 0.9D, player.getZ(), 1.5D, r, ParticleTypes.FLAME, 52);
                level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.35F, 0.8F);
            }

            case ICE_OF_POSEIDON -> {
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 400 + lvl * 80, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 240 + lvl * 40, 0));
                double r = 7.5D + radBonus;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().freeze(), 14.0F * mult);
                    t.setTicksFrozen(Math.min(340, t.getTicksFrozen() + 160));
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 4));
                    StunExecutionManager.addStun(level, t, player, 28.0F * mult);
                }
                ringParticles(level, player.getX(), player.getY() + 0.9D, player.getZ(), 1.5D, r, ParticleTypes.SNOWFLAKE, 48);
                level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1.3F, 0.85F);
            }

            case LIGHTNING_OF_ZEUS -> {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160 + lvl * 25, 1));
                Vec3 look = player.getLookAngle().normalize();
                Vec3 eye = player.getEyePosition();
                double range = 13.0D + radBonus;
                int bolts = 0;
                for (LivingEntity t : nearbyEnemies(level, player, range)) {
                    if (look.dot(t.getEyePosition().subtract(eye).normalize()) > 0.55D) {
                        t.hurt(level.damageSources().lightningBolt(), 15.5F * mult);
                        StunExecutionManager.addStun(level, t, player, 26.0F * mult);
                        if (bolts++ < 3 + lvl) spawnVisualLightning(level, t.getX(), t.getY(), t.getZ());
                    }
                }
                beamParticles(level, eye, look, range * 0.85D, ParticleTypes.ELECTRIC_SPARK, 32);
                level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.15F, 1.4F);
            }

            case SOUL_OF_HADES -> {
                double r = 8.5D + radBonus;
                float drain = 0.0F;
                for (LivingEntity t : nearbyEnemies(level, player, r)) {
                    t.hurt(level.damageSources().wither(), 13.5F * mult);
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 5));
                    t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 2));
                    StunExecutionManager.addStun(level, t, player, 24.0F * mult);
                    level.sendParticles(ParticleTypes.SOUL, t.getX(), t.getY() + 0.4D, t.getZ(), 16, 0.3, 0.9, 0.3, 0.06);
                    drain += 1.5F;
                }
                if (drain > 0.0F) player.heal(Math.min(8.0F, drain));
                level.playSound(null, player.blockPosition(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 0.85F, 1.15F);
            }

            // ================= BETRAYAL =================
            case BLADE_OF_ARES_SLASH -> {
                Vec3 look = player.getLookAngle().normalize();
                Vec3 eye = player.getEyePosition();
                double range = 18.0D + lvl * 2.0D;
                for (LivingEntity t : nearbyEnemies(level, player, range)) {
                    Vec3 toT = t.getEyePosition().subtract(eye);
                    double proj = toT.dot(look);
                    if (proj > 0.5D && proj < range) {
                        double perp = toT.subtract(look.scale(proj)).length();
                        if (perp <= 2.4D) {
                            t.hurt(level.damageSources().playerAttack(player), 17.5F * mult);
                            t.setSecondsOnFire(4);
                            StunExecutionManager.addStun(level, t, player, 26.0F * mult);
                        }
                    }
                }
                beamParticles(level, eye, look, range * 0.88D, ParticleTypes.SWEEP_ATTACK, 10);
                beamParticles(level, eye, look, range * 0.88D, ParticleTypes.FLAME, 32);
                level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.4F, 0.65F);
            }
        }
    }

    /** Tick periódico para las magias sostenidas (Cronos, Ejército de Esparta, Erinias, Caronte). */
    public static boolean tickActiveEffect(ActiveEffect ef, ServerPlayer caster) {
        ef.ticksRemaining--;
        if (ef.ticksRemaining <= 0) return false;
        if (caster == null || !caster.isAlive() || caster.level() != ef.level) return false;

        if (ef.followCaster) {
            ef.x = caster.getX();
            ef.y = caster.getY();
            ef.z = caster.getZ();
        }
        float mult = ef.magic.getDamageMultiplier(ef.levelRank);

        switch (ef.magic) {
            case CRONOS_RAGE -> {
                if (ef.ticksRemaining % 4 == 0) {
                    ef.level.sendParticles(ParticleTypes.ELECTRIC_SPARK, ef.x, ef.y, ef.z, 8, 0.45, 0.45, 0.45, 0.06);
                }
                if (ef.ticksRemaining % 12 == 0) {
                    for (LivingEntity t : enemiesAround(ef.level, caster, ef.x, ef.y, ef.z, 6.5D + ef.levelRank * 0.6D)) {
                        t.hurt(ef.level.damageSources().lightningBolt(), 4.8F * mult);
                        t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 22, 2));
                        StunExecutionManager.addStun(ef.level, t, caster, 8.0F * mult);
                        beamParticles(ef.level, new Vec3(ef.x, ef.y, ef.z),
                            t.getEyePosition().subtract(ef.x, ef.y, ef.z).normalize(),
                            t.position().distanceTo(new Vec3(ef.x, ef.y, ef.z)), ParticleTypes.ELECTRIC_SPARK, 10);
                    }
                    ef.level.playSound(null, caster.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.45F, 1.7F);
                }
            }

            case ARMY_OF_SPARTA -> {
                if (ef.ticksRemaining % 5 == 0) {
                    ringParticles(ef.level, ef.x, ef.y + 1.0D, ef.z, 2.4D, 2.6D, ParticleTypes.FLAME, 18);
                }
                if (ef.ticksRemaining % 14 == 0) {
                    // Lluvia de lanzas/flechas espectrales en 360° alrededor de la falange espartana!
                    int spears = 8 + ef.levelRank * 2;
                    for (int i = 0; i < spears; i++) {
                        double ang = (2.0D * Math.PI * i) / spears + (ef.ticksRemaining * 0.15D);
                        Arrow spear = new Arrow(ef.level, caster);
                        spear.setPos(ef.x + Math.cos(ang) * 1.4D, ef.y + 1.5D, ef.z + Math.sin(ang) * 1.4D);
                        spear.shoot(Math.cos(ang), 0.12D, Math.sin(ang), 2.1F, 2.0F);
                        spear.setBaseDamage(4.5D * mult);
                        spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        spear.setSecondsOnFire(4);
                        ef.level.addFreshEntity(spear);
                    }
                    for (LivingEntity t : enemiesAround(ef.level, caster, ef.x, ef.y, ef.z, 4.5D)) {
                        t.hurt(ef.level.damageSources().playerAttack(caster), 4.5F * mult);
                        Vec3 out = t.position().subtract(ef.x, ef.y, ef.z).normalize();
                        t.push(out.x * 0.65D, 0.25D, out.z * 0.65D);
                        t.hurtMarked = true;
                    }
                    ef.level.playSound(null, caster.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.9F, 0.85F);
                }
            }

            case SCOURGE_OF_ERINYS -> {
                if (ef.ticksRemaining % 3 == 0) {
                    ringParticles(ef.level, ef.x, ef.y, ef.z, 0.6D, 3.6D, ParticleTypes.CRIMSON_SPORE, 14);
                    ef.level.sendParticles(ParticleTypes.PORTAL, ef.x, ef.y, ef.z, 10, 0.6, 0.6, 0.6, 0.15);
                }
                if (ef.ticksRemaining % 8 == 0) {
                    float heal = 0.0F;
                    for (LivingEntity t : enemiesAround(ef.level, caster, ef.x, ef.y, ef.z, 7.5D + ef.levelRank * 0.7D)) {
                        Vec3 pull = new Vec3(ef.x, ef.y, ef.z).subtract(t.position()).normalize();
                        t.push(pull.x * 0.48D, 0.12D, pull.z * 0.48D);
                        t.hurtMarked = true;
                        t.hurt(ef.level.damageSources().wither(), 4.2F * mult);
                        heal += 0.75F;
                    }
                    if (heal > 0.0F) caster.heal(Math.min(3.5F, heal));
                }
            }

            case CHARONS_WRATH -> {
                if (ef.ticksRemaining % 4 == 0) {
                    ringParticles(ef.level, ef.x, ef.y + 0.3D, ef.z, 0.8D, 5.2D, ParticleTypes.HAPPY_VILLAGER, 14);
                    ringParticles(ef.level, ef.x, ef.y + 0.5D, ef.z, 1.0D, 4.8D, ParticleTypes.SOUL_FIRE_FLAME, 10);
                }
                if (ef.ticksRemaining % 14 == 0) {
                    for (LivingEntity t : enemiesAround(ef.level, caster, ef.x, ef.y, ef.z, 6.0D + ef.levelRank * 0.6D)) {
                        t.hurt(ef.level.damageSources().magic(), 5.0F * mult);
                        t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 4));
                        t.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
                        StunExecutionManager.addStun(ef.level, t, caster, 9.0F * mult);
                    }
                }
            }

            default -> { return false; }
        }
        return true;
    }

    // ================= UTILIDADES DE COMBATE Y VFX =================
    private static List<LivingEntity> nearbyEnemies(ServerLevel level, ServerPlayer player, double radius) {
        return enemiesAround(level, player, player.getX(), player.getY(), player.getZ(), radius);
    }

    private static List<LivingEntity> enemiesAround(ServerLevel level, ServerPlayer caster,
                                                    double x, double y, double z, double radius) {
        AABB box = new AABB(x - radius, y - radius * 0.6D, z - radius, x + radius, y + radius * 0.8D, z + radius);
        return new ArrayList<>(level.getEntitiesOfClass(LivingEntity.class, box,
            e -> e != caster && e.isAlive() && !e.isAlliedTo(caster)));
    }

    private static LivingEntity findLineTarget(ServerLevel level, ServerPlayer caster, Vec3 start, Vec3 dir,
                                               double maxRange, double hitRadius) {
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity e : nearbyEnemies(level, caster, maxRange)) {
            Vec3 toE = e.getEyePosition().subtract(start);
            double proj = toE.dot(dir);
            if (proj > 0.5D && proj < maxRange) {
                double perp = toE.subtract(dir.scale(proj)).length();
                if (perp <= hitRadius && proj < bestDist) {
                    bestDist = proj;
                    best = e;
                }
            }
        }
        return best;
    }

    private static void spawnVisualLightning(ServerLevel level, double x, double y, double z) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(x, y, z);
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
    }

    private static void ringParticles(ServerLevel level, double cx, double cy, double cz,
                                      double minR, double maxR, ParticleOptions type, int count) {
        for (int i = 0; i < count; i++) {
            double ang = (2.0D * Math.PI * i) / count;
            double r = minR + ((i % 4) / 3.0D) * (maxR - minR);
            level.sendParticles(type, cx + Math.cos(ang) * r, cy + (i % 3) * 0.25D, cz + Math.sin(ang) * r,
                1, 0.12, 0.18, 0.12, 0.04);
        }
    }

    private static void beamParticles(ServerLevel level, Vec3 start, Vec3 dir, double dist,
                                      ParticleOptions type, int steps) {
        for (int i = 1; i <= steps; i++) {
            double d = (dist * i) / steps;
            Vec3 p = start.add(dir.scale(d));
            level.sendParticles(type, p.x, p.y, p.z, 2, 0.12, 0.12, 0.12, 0.02);
        }
    }
}
