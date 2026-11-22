package com.electrifiedded.cpc.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/** Thrown Stephano statue: burns and hurts what it hits, then pops in a small explosion. */
public class EntityStephano extends EntityThrowable {
    public EntityStephano(World world) {
        super(world);
    }

    public EntityStephano(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.03F;
    }

    @Override
    protected void onImpact(RayTraceResult hit) {
        if (world.isRemote) return;
        if (hit.entityHit != null && hit.entityHit != getThrower()) {
            hit.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()), 6.0F);
            hit.entityHit.setFire(6);
        }
        world.createExplosion(this, posX, posY, posZ, 0.5F, true);
        world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        setDead();
    }
}
