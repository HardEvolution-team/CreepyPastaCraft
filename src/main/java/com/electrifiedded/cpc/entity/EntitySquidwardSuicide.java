package com.electrifiedded.cpc.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntitySquidwardSuicide extends EntityAnimal {
    public int madTime;

    public EntitySquidwardSuicide(World world) {
        super(world);
        setSize(0.9F, 2.0F);
        madTime = rand.nextInt(200);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(5, new EntityAIWander(this, 1.0));
        tasks.addTask(7, new EntityAILookIdle(this));
        tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!onGround && motionY < 0.0) motionY *= 0.6;
        if (!isChild() && --madTime <= 0) {
            if (!world.isRemote) {
                EntitySquidward.replace(this, new EntitySquidwardMad(world));
                world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_WOLF_GROWL, SoundCategory.NEUTRAL, 1.0F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
            madTime = rand.nextInt(200);
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable other) {
        return new EntitySquidwardSuicide(world);
    }
}
