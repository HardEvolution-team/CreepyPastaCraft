package com.electrifiedded.cpc.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCPCKnife extends ItemSword {
    public ItemCPCKnife(ToolMaterial m) {
        super(m);
    }

    @Override
    public boolean getIsRepairable(ItemStack a, ItemStack b) {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.add(I18n.format(getTranslationKey() + ".desc"));
    }
}
