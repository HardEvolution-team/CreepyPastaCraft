package com.electrifiedded.cpc.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCPCRecord extends ItemRecord {
    public ItemCPCRecord(String name, SoundEvent sound) {
        super(name, sound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getRecordNameLocal() {
        return I18n.format("item.cpc.record_lavender.desc");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.add(getRecordNameLocal());
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
