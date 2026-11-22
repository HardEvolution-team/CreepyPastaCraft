package com.electrifiedded.cpc.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/** The Strider: huge rideable walker, steered directly by the rider (no item needed). */
public class EntityStrider extends EntityAnimal {
    public EntityStrider(World world) {
        super(world);
        setSize(2.0F, 2.5F);
        stepHeight = 1.5F;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.5));
        tasks.addTask(2, new EntityAIMate(this, 1.0));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        tasks.addTask(5, new EntityAIWander(this, 1.0));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public double getMountedYOffset() {
        return 2.7;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (super.processInteract(player, hand)) return true;
        if (!isBeingRidden() && player.getHeldItem(hand).isEmpty()) {
            if (!world.isRemote) player.startRiding(this);
            return true;
        }
        return false;
    }

    @Override
    public Entity getControllingPassenger() {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        return getControllingPassenger() instanceof EntityLivingBase;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        Entity e = getControllingPassenger();
        if (isBeingRidden() && e instanceof EntityLivingBase) {
            EntityLivingBase rider = (EntityLivingBase) e;
            rotationYaw = rider.rotationYaw;
            prevRotationYaw = rotationYaw;
            rotationPitch = rider.rotationPitch * 0.5F;
            setRotation(rotationYaw, rotationPitch);
            renderYawOffset = rotationYaw;
            rotationYawHead = rotationYaw;
            float f = rider.moveForward;
            if (f <= 0.0F) f *= 0.25F;
            setAIMoveSpeed(0.34F);
            super.travel(rider.moveStrafing * 0.5F, vertical, f);
        } else super.travel(strafe, vertical, forward);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable other) {
        return new EntityStrider(world);
    }
}
