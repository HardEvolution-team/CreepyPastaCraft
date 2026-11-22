package com.electrifiedded.cpc.entity;

import com.electrifiedded.cpc.CPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityPewds extends EntityAnimal {
    public EntityPewds(World world) {
        super(world);
        setSize(0.6F, 1.8F);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIAvoidEntity<EntityMob>(this, EntityMob.class, 8.0F, 0.6, 0.6) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                world.playSound(null, posX, posY, posZ, CPC.sound("mob.pewds.scare"), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        });
        tasks.addTask(1, new EntityAIPanic(this, 2.0));
        tasks.addTask(2, new EntityAIOpenDoor(this, true));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.5));
        tasks.addTask(6, new EntityAIWander(this, 0.5));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
    }

    @Override
    public int getTalkInterval() {
        return 160;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CPC.sound("mob.pewds.say");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource s) {
        return CPC.sound("mob.pewds.hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CPC.sound("mob.pewds.death");
    }

    @Override
    protected Item getDropItem() {
        return CPC.stephano;
    }

    @Override
    protected boolean canDespawn() {
        return true;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable other) {
        return null;
    }
}
