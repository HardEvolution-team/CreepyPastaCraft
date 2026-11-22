package com.electrifiedded.cpc.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerLaptop extends Container {
    private final TileEntityLaptop laptop;
    private final int[] last = {-1, -1, -1};

    public ContainerLaptop(InventoryPlayer inv, TileEntityLaptop te) {
        laptop = te;
        addSlotToContainer(new Slot(te, 0, 56, 34));
        addSlotToContainer(new Slot(te, 1, 116, 35) {
            @Override
            public boolean isItemValid(ItemStack s) {
                return false;
            }
        });
        te.openInventory(inv.player);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++) addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int j = 0; j < 9; j++) addSlotToContainer(new Slot(inv, j, 8 + j * 18, 142));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int id = 0; id < 3; id++) {
            int v = laptop.getField(id);
            if (v != last[id]) for (IContainerListener l : listeners) l.sendWindowProperty(this, id, v);
            last[id] = v;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int v) {
        laptop.setField(id, v);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p) {
        return laptop.isUsableByPlayer(p);
    }

    @Override
    public void onContainerClosed(EntityPlayer p) {
        super.onContainerClosed(p);
        laptop.closeInventory(p);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack s = slot.getStack();
            copy = s.copy();
            if (index == 1) {
                if (!mergeItemStack(s, 2, 38, true)) return ItemStack.EMPTY;
                slot.onSlotChange(s, copy);
            } else if (index != 0) {
                if (s.getItem() == Items.PAPER) {
                    if (!mergeItemStack(s, 0, 1, false)) return ItemStack.EMPTY;
                } else if (index < 29) {
                    if (!mergeItemStack(s, 29, 38, false)) return ItemStack.EMPTY;
                } else if (!mergeItemStack(s, 2, 29, false)) return ItemStack.EMPTY;
            } else if (!mergeItemStack(s, 2, 38, false)) return ItemStack.EMPTY;
            if (s.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
            if (s.getCount() == copy.getCount()) return ItemStack.EMPTY;
            slot.onTake(p, s);
        }
        return copy;
    }
}
