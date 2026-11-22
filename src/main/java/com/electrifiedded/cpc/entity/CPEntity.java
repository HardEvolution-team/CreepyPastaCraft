package com.electrifiedded.cpc.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/** Base of the original "CPEntity" monsters: hunts Pewds/Cryotic and hits with a flat attackStrength. */
public abstract class CPEntity extends EntityMob {
    protected int attackStrength = 2;

    public CPEntity(World world) {
        super(world);
        experienceValue = 5;
    }

    /** Adds the melee task + the Pewds/Cryotic target tasks every pasta monster has. */
    protected void addPastaHunting(double speed) {
        tasks.addTask(1, new EntityAIAttackMelee(this, speed, false));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityPewds.class, true));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityCry.class, true));
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        int dmg = attackStrength;
        if (isPotionActive(MobEffects.STRENGTH)) dmg += 3 << getActivePotionEffect(MobEffects.STRENGTH).getAmplifier();
        if (isPotionActive(MobEffects.WEAKNESS)) dmg -= 2 << getActivePotionEffect(MobEffects.WEAKNESS).getAmplifier();
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), dmg);
    }
}
