package com.electrifiedded.cpc.block;

import com.electrifiedded.cpc.CPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/** Laptop: "prints" paper (slot 0) into a random CreepyPasta Archive or, rarely, smile.jpg (slot 1). Lid opens like a chest. */
public class TileEntityLaptop extends TileEntity implements IInventory, ITickable {
    public static final int PRINT_TIME = 200;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    public int printTime;          // "fuel" left of the current sheet
    public int currentPrintTime;
    public int progress;           // original standardPrintTime
    public float lidAngle, prevLidAngle;
    public int numUsingPlayers;

    public static ItemStack printingResult(ItemStack in) {
        if (in.getItem() != Items.PAPER) return ItemStack.EMPTY;
        java.util.Random r = new com.electrifiedded.archaiclib.util.FastRandom();
        if (r.nextInt(100) < 5) return new ItemStack(CPC.smileJpg);
        return new ItemStack(CPC.archive, 1, r.nextInt(CPC.PASTA_NAMES.size()));
    }

    private boolean canPrint() {
        ItemStack in = stacks.get(0);
        return in.getItem() == Items.PAPER && stacks.get(1).isEmpty();
    }

    @Override
    public void update() {
        boolean dirty = false;
        if (printTime > 0) printTime--;
        if (!world.isRemote) {
            if (printTime == 0 && canPrint()) {
                currentPrintTime = printTime = PRINT_TIME;
                dirty = true;
            }
            if (printTime > 0 && canPrint()) {
                if (++progress >= PRINT_TIME) {
                    progress = 0;
                    stacks.set(1, printingResult(stacks.get(0)));
                    stacks.get(0).shrink(1);
                    dirty = true;
                }
            } else progress = 0;
        }
        if (dirty) markDirty();

        prevLidAngle = lidAngle;
        if (numUsingPlayers > 0 && lidAngle == 0.0F)
            world.playSound(null, pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
            float old = lidAngle;
            lidAngle += numUsingPlayers > 0 ? 0.1F : -0.1F;
            if (lidAngle > 1.0F) lidAngle = 1.0F;
            if (lidAngle < 0.5F && old >= 0.5F)
                world.playSound(null, pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            if (lidAngle < 0.0F) lidAngle = 0.0F;
        }
    }

    public int getPrintProgressScaled(int w) {
        return progress * w / PRINT_TIME;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            numUsingPlayers = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(EntityPlayer p) {
        if (numUsingPlayers < 0) numUsingPlayers = 0;
        numUsingPlayers++;
        world.addBlockEvent(pos, getBlockType(), 1, numUsingPlayers);
    }

    @Override
    public void closeInventory(EntityPlayer p) {
        numUsingPlayers--;
        world.addBlockEvent(pos, getBlockType(), 1, numUsingPlayers);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        stacks = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, stacks);
        printTime = tag.getShort("PrintTime");
        progress = tag.getShort("StandardPrintTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("PrintTime", (short) printTime);
        tag.setShort("StandardPrintTime", (short) progress);
        ItemStackHelper.saveAllItems(tag, stacks);
        return tag;
    }

    @Override public int getSizeInventory() { return 2; }
    @Override public boolean isEmpty() { return stacks.get(0).isEmpty() && stacks.get(1).isEmpty(); }
    @Override public ItemStack getStackInSlot(int i) { return stacks.get(i); }
    @Override public ItemStack decrStackSize(int i, int n) { return ItemStackHelper.getAndSplit(stacks, i, n); }
    @Override public ItemStack removeStackFromSlot(int i) { return ItemStackHelper.getAndRemove(stacks, i); }

    @Override
    public void setInventorySlotContents(int i, ItemStack s) {
        stacks.set(i, s);
        if (s.getCount() > getInventoryStackLimit()) s.setCount(getInventoryStackLimit());
    }

    @Override public int getInventoryStackLimit() { return 64; }

    @Override
    public boolean isUsableByPlayer(EntityPlayer p) {
        return world.getTileEntity(pos) == this && p.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override public boolean isItemValidForSlot(int i, ItemStack s) { return i == 0 && s.getItem() == Items.PAPER; }

    @Override
    public int getField(int id) {
        return id == 0 ? progress : id == 1 ? printTime : currentPrintTime;
    }

    @Override
    public void setField(int id, int v) {
        if (id == 0) progress = v;
        else if (id == 1) printTime = v;
        else currentPrintTime = v;
    }

    @Override public int getFieldCount() { return 3; }
    @Override public void clear() { stacks.clear(); }
    @Override public String getName() { return "container.cpc.laptop"; }
    @Override public boolean hasCustomName() { return false; }
    @Override public ITextComponent getDisplayName() { return new TextComponentTranslation(getName()); }
}
