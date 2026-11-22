package com.electrifiedded.cpc.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemCPCDiaper extends ItemArmor {
    public ItemCPCDiaper(ArmorMaterial m) {
        super(m, 0, EntityEquipmentSlot.LEGS);
    }

    @Override
    public boolean getIsRepairable(ItemStack a, ItemStack b) {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "cpc:textures/models/armor/baby_layer_2.png";
    }
}
