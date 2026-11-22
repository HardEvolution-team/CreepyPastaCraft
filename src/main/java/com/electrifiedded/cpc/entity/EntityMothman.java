package com.electrifiedded.cpc.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/** Mothman: bat-like flyer (original random flight target logic), attacks players. */
public class EntityMothman extends CPEntity {
    private BlockPos flightTarget;

    public EntityMothman(World world) {
        super(world);
        isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 0.7, false));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(3, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
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
    public void onUpdate() {
        super.onUpdate();
        motionY *= 0.3;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void fall(float distance, float mult) {
    }

    @Override
    protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos) {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (getAttackTarget() != null) {
            flightTarget = new BlockPos(getAttackTarget()).up();
        } else if (flightTarget != null && (!world.isAirBlock(flightTarget) || flightTarget.getY() < 1)) flightTarget = null;
        if (flightTarget == null || rand.nextInt(60) == 0 || flightTarget.distanceSq((int) posX, (int) posY, (int) posZ) < 4.0) {
            if (getAttackTarget() == null)
                flightTarget = new BlockPos((int) posX + rand.nextInt(7) - rand.nextInt(7), (int) posY + rand.nextInt(6) - 2,
                        (int) posZ + rand.nextInt(7) - rand.nextInt(7));
        }
        if (flightTarget == null) return;
        double dx = flightTarget.getX() + 0.5 - posX;
        double dy = flightTarget.getY() + 0.1 - posY;
        double dz = flightTarget.getZ() + 0.5 - posZ;
        motionX += (Math.signum(dx) * 0.5 - motionX) * 0.1;
        motionY += (Math.signum(dy) * 0.7 - motionY) * 0.1;
        motionZ += (Math.signum(dz) * 0.5 - motionZ) * 0.1;
        float yaw = (float) (Math.atan2(motionZ, motionX) * 180.0 / Math.PI) - 90.0F;
        moveForward = 0.5F;
        rotationYaw += MathHelper.wrapDegrees(yaw - rotationYaw);
    }
}
