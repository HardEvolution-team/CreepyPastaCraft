package com.electrifiedded.cpc.block;

import com.electrifiedded.cpc.CPC;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaptop extends BlockContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375);

    public BlockLaptop() {
        super(Material.IRON);
        setHardness(1.5F);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState s) {
        return s.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public void onBlockPlacedBy(World w, BlockPos pos, IBlockState s, EntityLivingBase placer, ItemStack stack) {
        // original: yaw quadrant 0..3 -> meta 2,5,3,4 (= facing towards the player)
        w.setBlockState(pos, s.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState s) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState s) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState s) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState s, IBlockAccess w, BlockPos pos) {
        return BOX;
    }

    @Override
    public boolean onBlockActivated(World w, BlockPos pos, IBlockState s, EntityPlayer p, EnumHand hand, EnumFacing side, float hx, float hy, float hz) {
        if (!(w.getTileEntity(pos) instanceof TileEntityLaptop)) return false;
        if (!w.isRemote) p.openGui(CPC.instance, 0, w, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World w, BlockPos pos, IBlockState s) {
        TileEntity te = w.getTileEntity(pos);
        if (te instanceof TileEntityLaptop) InventoryHelper.dropInventoryItems(w, pos, (TileEntityLaptop) te);
        super.breakBlock(w, pos, s);
    }

    @Override
    public TileEntity createNewTileEntity(World w, int meta) {
        return new TileEntityLaptop();
    }
}
