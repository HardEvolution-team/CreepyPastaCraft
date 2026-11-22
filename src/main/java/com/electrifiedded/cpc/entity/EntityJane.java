package com.electrifiedded.cpc.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/** Jane hunts Jeff only (and Pewds/Cryotic like every pasta); neutral to players unless hurt. */
public class EntityJane extends EntityMob {
    public EntityJane(World world) {
        super(world);
        isImmuneToFire = true;
        experienceValue = 5;
        PastaMob.basics(this, 0.7, true);
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityJeff.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16.0);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0);
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.RED_FLOWER);
    }
}
