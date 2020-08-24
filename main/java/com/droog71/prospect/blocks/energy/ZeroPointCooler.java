package com.droog71.prospect.blocks.energy;

import java.util.Random;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.tile_entity.CoolerTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ZeroPointCooler extends ProspectBlock
{	
	public ZeroPointCooler(String name, Material material) 
	{
		super(name, material);
	}

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        
        if (active)
        {
            worldIn.setBlockState(pos, ProspectBlocks.zero_point_cooler_running.getDefaultState());
            worldIn.setBlockState(pos, ProspectBlocks.zero_point_cooler_running.getDefaultState());
        }
        else
        {
            worldIn.setBlockState(pos, ProspectBlocks.zero_point_cooler.getDefaultState());
            worldIn.setBlockState(pos, ProspectBlocks.zero_point_cooler.getDefaultState());
        }
        
        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
	
    @Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ProspectBlocks.zero_point_cooler);
    }
    
	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new CoolerTileEntity();
	}
}
