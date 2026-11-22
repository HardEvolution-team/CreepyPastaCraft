package com.electrifiedded.cpc;

import com.electrifiedded.cpc.block.ContainerLaptop;
import com.electrifiedded.cpc.block.TileEntityLaptop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CPCGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer p, World w, int x, int y, int z) {
        TileEntity te = w.getTileEntity(new BlockPos(x, y, z));
        return te instanceof TileEntityLaptop ? new ContainerLaptop(p.inventory, (TileEntityLaptop) te) : null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer p, World w, int x, int y, int z) {
        TileEntity te = w.getTileEntity(new BlockPos(x, y, z));
        return te instanceof TileEntityLaptop ? ClientProxy.laptopGui(p, (TileEntityLaptop) te) : null;
    }
}
