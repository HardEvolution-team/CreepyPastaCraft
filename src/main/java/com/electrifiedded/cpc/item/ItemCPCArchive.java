package com.electrifiedded.cpc.item;

import com.electrifiedded.cpc.CPC;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Function;

/** "CreepyPasta Archive": metadata selects a pasta, using it spawns that creature (like a spawn egg). */
public class ItemCPCArchive extends Item {
    public ItemCPCArchive() {
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    public static String name(int meta) {
        return meta >= 0 && meta < CPC.PASTA_NAMES.size() ? CPC.PASTA_NAMES.get(meta) : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        String s = name(stack.getMetadata());
        if (s != null) list.add(I18n.format("entity.cpc." + s + ".name"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hx, float hy, float hz) {
        if (world.isRemote) return EnumActionResult.SUCCESS;
        ItemStack stack = player.getHeldItem(hand);
        BlockPos p = pos.offset(facing);
        Entity e = spawnCreature(world, stack.getMetadata(), p.getX() + 0.5, p.getY(), p.getZ() + 0.5);
        if (e != null) {
            if (e instanceof EntityLiving && stack.hasDisplayName()) e.setCustomNameTag(stack.getDisplayName());
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }

    public static Entity spawnCreature(World world, int meta, double x, double y, double z) {
        String n = name(meta);
        if (n == null) return null;
        Function<World, ? extends Entity> f = CPC.PASTA.get(n);
        Entity entity = f.apply(world);
        if (entity instanceof EntityLiving) {
            EntityLiving l = (EntityLiving) entity;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            l.rotationYawHead = l.rotationYaw;
            l.renderYawOffset = l.rotationYaw;
            l.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(l)), null);
            world.spawnEntity(entity);
            l.playLivingSound();
        }
        return entity;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) for (int i = 0; i < CPC.PASTA_NAMES.size(); i++) items.add(new ItemStack(this, 1, i));
    }
}
