package com.electrifiedded.cpc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Hilt of a Blacksmith curse: holding it blinds and withers the holder (original CPCHiltBlackHandler). */
public class CPCEvents {
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent e) {
        if (!(e.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer p = (EntityPlayer) e.getEntityLiving();
        if (p.getHeldItemMainhand().getItem() != CPC.hiltBlack) return;
        if (!p.world.isRemote) {
            p.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 50, 1));
            p.addPotionEffect(new PotionEffect(MobEffects.WITHER, 50, 1));
        } else {
            p.world.spawnParticle(EnumParticleTypes.REDSTONE, p.posX, p.posY + 0.4, p.posZ, 0, 0, 0);
            if (p.getRNG().nextInt(100) < 25) p.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, p.posX, p.posY + 0.4, p.posZ, 0, 0, 0);
        }
    }
}
