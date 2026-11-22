package com.electrifiedded.cpc.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/** Mad Squidward: drowns himself immediately (original behaviour). */
public class EntitySquidwardMad extends EntityAnimal {
    public EntitySquidwardMad(World world) {
        super(world);
        setSize(0.9F, 2.0F);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!world.isRemote) attackEntityFrom(DamageSource.DROWN, 15.0F);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable other) {
        return null;
    }
}
