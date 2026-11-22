package com.electrifiedded.cpc.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/** Squidward (happy). Right click -> turns into the "suicide" Squidward, which later goes mad and dies. */
public class EntitySquidward extends EntityAnimal {
    public EntitySquidward(World world) {
        super(world);
        setSize(0.9F, 2.0F);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(5, new EntityAIWander(this, 1.0));
        tasks.addTask(7, new EntityAILookIdle(this));
        tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
    }

    static void replace(EntityAnimal from, EntityAnimal to) {
        from.setDead();
        to.setLocationAndAngles(from.posX, from.posY, from.posZ, from.rotationYaw, from.rotationPitch);
        to.setHealth(to.getMaxHealth());
        to.renderYawOffset = from.renderYawOffset;
        from.world.spawnEntity(to);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (super.processInteract(player, hand)) return true;
        if (!world.isRemote) replace(this, new EntitySquidwardSuicide(world));
        return true;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable other) {
        return new EntitySquidward(world);
    }
}
