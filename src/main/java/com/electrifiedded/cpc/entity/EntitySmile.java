package com.electrifiedded.cpc.entity;

import com.electrifiedded.archaiclib.legacy.entity.LegacyStare;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/** Smile.dog: looking at it curses the player ("Spread The Word.") and makes it angry. */
public class EntitySmile extends CPEntity {
    private static final DataParameter<Boolean> ANGRY = EntityDataManager.createKey(EntitySmile.class, DataSerializers.BOOLEAN);
    private int stareTicks;

    public EntitySmile(World world) {
        super(world);
        setSize(0.6F, 0.8F);
        stepHeight = 1.0F;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, true));
        tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ANGRY, false);
    }

    public boolean isAngry() {
        return dataManager.get(ANGRY);
    }

    public void setAngry(boolean b) {
        dataManager.set(ANGRY, b);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
        // original: 7.0 - capped to a fast but sane value
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (world.isRemote || !isEntityAlive() || ticksExisted % 4 != 0) return;
        EntityPlayer p = world.getClosestPlayerToEntity(this, 64.0);
        if (p == null || p.isCreative() || p.isSpectator()) return;
        if (LegacyStare.isStaredAt(p, this, 0.025)) {
            setAngry(true);
            if (stareTicks++ == 5) {
                stareTicks = 0;
                setAttackTarget(p);
            } else {
                p.sendMessage(new TextComponentString("Spread The Word."));
                p.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 700, 50));
                p.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 250, 50));
                p.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
                p.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, 2));
            }
        } else stareTicks = 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("Angry", isAngry());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        setAngry(tag.getBoolean("Angry"));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WOLF_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource s) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
}
