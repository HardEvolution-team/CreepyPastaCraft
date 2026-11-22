package com.electrifiedded.cpc.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/** Seed Eater: hunts villager children (original ChildHunt AI) and attacks players that come close. */
public class EntitySeed extends EntityMob {
    public EntitySeed(World world) {
        super(world);
        isImmuneToFire = true;
        PastaMob.basics(this, 0.6, true);
        targetTasks.taskEntries.removeIf(t -> t.action instanceof EntityAINearestAttackableTarget);
        targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, 10, true, false,
                (EntityVillager v) -> v != null && v.isChild()));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, true, false,
                (EntityPlayer p) -> p != null && p.getDistanceSq(EntitySeed.this) < 36.0));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0);
    }

    @Override
    public boolean attackEntityAsMob(net.minecraft.entity.Entity e) {
        return (!(e instanceof EntityVillager) || ((EntityLivingBase) e).isChild()) && super.attackEntityAsMob(e);
    }

    @Override
    protected Item getDropItem() {
        return Items.WHEAT_SEEDS;
    }
}
