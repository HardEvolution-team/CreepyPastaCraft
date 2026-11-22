package com.electrifiedded.cpc.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCPCPasta extends ItemFood {
    public ItemCPCPasta() {
        super(5, 5.0F, false);
        setAlwaysEdible();
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        super.onItemUseFinish(stack, world, entity);
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) return stack;
        return new ItemStack(Items.BOWL);
    }
}
