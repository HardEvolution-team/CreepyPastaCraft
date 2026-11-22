package com.electrifiedded.cpc.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

/** Shared AI wiring of the humanoid pastas (Jeff/Jane/Rake/Seed Eater) - 1.7 EntityAIAttackOnCollide(class) became melee + target tasks. */
final class PastaMob {
    private PastaMob() {}

    static void basics(EntityCreature e, double speed, boolean breakDoors) {
        e.tasks.addTask(0, new EntityAISwimming(e));
        if (breakDoors) e.tasks.addTask(2, new EntityAIBreakDoor(e));
        else e.tasks.addTask(2, new EntityAIOpenDoor(e, true));
        e.tasks.addTask(3, new EntityAIAttackMelee(e, speed, false));
        e.tasks.addTask(6, new EntityAIMoveTowardsRestriction(e, speed));
        e.tasks.addTask(7, new EntityAIMoveThroughVillage(e, speed, false));
        e.tasks.addTask(8, new EntityAIWander(e, speed));
        e.tasks.addTask(9, new EntityAIWatchClosest(e, EntityPlayer.class, 8.0F));
        e.tasks.addTask(9, new EntityAILookIdle(e));
        e.targetTasks.addTask(1, new EntityAIHurtByTarget(e, false));
        e.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(e, EntityPewds.class, true));
        e.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(e, EntityCry.class, true));
    }

    static void huntPlayersAndVillagers(EntityMob e) {
        e.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(e, EntityPlayer.class, true));
        e.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(e, EntityVillager.class, false));
    }
}
