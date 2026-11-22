package com.electrifiedded.cpc.item;

import com.electrifiedded.cpc.entity.EntityStephano;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCPCStephano extends Item {
    public ItemCPCStephano() {
        setMaxStackSize(8);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.capabilities.isCreativeMode) stack.shrink(1);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
                0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            EntityStephano s = new EntityStephano(world, player);
            // 1.7 EntityThrowable defaults with the overridden speed 0.5 and pitch offset -20
            s.shoot(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.spawnEntity(s);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
