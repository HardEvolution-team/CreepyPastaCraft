package com.electrifiedded.cpc.entity;

import com.electrifiedded.cpc.CPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityJeff extends EntityMob {
    public EntityJeff(World world) {
        super(world);
        isImmuneToFire = true;
        experienceValue = 5;
        PastaMob.basics(this, 0.7, false);
        PastaMob.huntPlayersAndVillagers(this);
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityJane.class, true));
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
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance d) {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CPC.jeffKnife));
        setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
    }

    @Override
    public void onKillEntity(EntityLivingBase victim) {
        super.onKillEntity(victim);
        world.playSound(null, posX, posY, posZ, CPC.sound("mob.jeff.kill"), SoundCategory.HOSTILE, 1.0F, 1.0F);
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote && cause.getTrueSource() instanceof EntityJane) {
            dropItem(CPC.jeffKnife, 1);
            for (EntityPlayer p : world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().grow(32.0)))
                p.sendMessage(new TextComponentString("§lJane The Killer: §rSleep well."));
        }
    }
}
