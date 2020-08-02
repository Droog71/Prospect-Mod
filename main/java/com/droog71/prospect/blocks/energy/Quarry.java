package com.droog71.prospect.blocks.energy;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tile_entity.QuarryTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Quarry extends ProspectBlock
{	
	public Quarry(String name, Material material) 
	{
		super(name, material);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new QuarryTileEntity();
	}
	
    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof QuarryTileEntity)
        {
        	for (BlockPos p : ((QuarryTileEntity) tileentity).quarryPositions)
			{
        		worldIn.setBlockToAir(p);
			}
        }
        super.breakBlock(worldIn, pos, state);
    }
}
