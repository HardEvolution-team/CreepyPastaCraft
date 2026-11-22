package com.electrifiedded.cpc.entity;

import com.electrifiedded.archaiclib.legacy.entity.LegacyStare;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/** Eyeless Jack: teleports around a player who stares at him and visits sleeping players. */
public class EntityJack extends CPEntity {
    private int stareTicks;
    private boolean targetWasSleeping;
    private int sleepDelay;

    public EntityJack(World world) {
        super(world);
        attackStrength = 6;
        isImmuneToFire = true;
        PastaMob.basics(this, 1.0, true);
        PastaMob.huntPlayersAndVillagers(this);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean hit = super.attackEntityAsMob(target);
        int diff = world.getDifficulty().getId();
        if (hit && getHeldItemMainhand().isEmpty() && rand.nextFloat() < diff * 0.3F) target.setFire(2 * diff);
        return hit;
    }

    @Override
    public void onLivingUpdate() {
        if (!world.isRemote && isEntityAlive()) {
            EntityPlayer p = world.getClosestPlayerToEntity(this, 64.0);
            if (p != null && !p.isCreative() && !p.isSpectator()) {
                if (ticksExisted % 4 == 0 && LegacyStare.isStaredAt(p, this, 0.025)) {
                    if (stareTicks++ == 5) {
                        stareTicks = 0;
                        setAttackTarget(p);
                    } else teleportToEntity(p);
                } else stareTicks = 0;
                // original: after the player wakes up, Jack appears next to him
                if (p.isPlayerSleeping()) targetWasSleeping = true;
                else if (targetWasSleeping) {
                    targetWasSleeping = false;
                    sleepDelay = 20;
                }
                if (sleepDelay > 0 && --sleepDelay == 0)
                    for (int i = 0; i <= 5 && !teleportToEntity(p); i++) ;
            }
        }
        super.onLivingUpdate();
    }

    protected boolean teleportToEntity(Entity e) {
        Vec3d v = new Vec3d(posX - e.posX, getEntityBoundingBox().minY + height / 2.0F - e.posY + e.getEyeHeight(), posZ - e.posZ).normalize();
        double x = posX + (rand.nextDouble() - 0.5) * 8.0 - v.x * 16.0;
        double y = posY + (rand.nextInt(16) - 8) - v.y * 16.0;
        double z = posZ + (rand.nextDouble() - 0.5) * 8.0 - v.z * 16.0;
        double ox = posX, oy = posY, oz = posZ;
        net.minecraftforge.event.entity.living.EnderTeleportEvent ev = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(ev)) return false;
        boolean ok = attemptTeleport(ev.getTargetX(), ev.getTargetY(), ev.getTargetZ());
        if (ok) {
            world.playSound(null, ox, oy, oz, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 1.0F, 1.0F);
            playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
        return ok;
    }
}
