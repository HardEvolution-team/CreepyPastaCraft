package com.electrifiedded.cpc.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class EntityRake extends EntityMob {
    public EntityRake(World world) {
        super(world);
        setSize(0.7F, 0.9F);
        experienceValue = 5;
        PastaMob.basics(this, 1.0, false);
        PastaMob.huntPlayersAndVillagers(this);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(48.0);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16.0);
        // original: 7.0 (unplayable warp speed in 1.12) - kept "very fast"
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45);
    }

    @Override
    protected void dropFewItems(boolean byPlayer, int looting) {
        int n = rand.nextInt(3) + 1 + rand.nextInt(1 + looting);
        for (int i = 0; i < n; i++) dropItem(isBurning() ? Items.COOKED_PORKCHOP : Items.PORKCHOP, 1);
    }
}
