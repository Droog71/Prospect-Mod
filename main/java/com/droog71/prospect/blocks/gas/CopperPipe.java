package com.droog71.prospect.blocks.gas;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tile_entity.CopperPipeTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CopperPipe extends ProspectBlock
{
	public boolean pressurized;
	public boolean initialPipe;
	public CopperPipe input;
	public CopperPipe output;
	
	public CopperPipe(String name, Material material) 
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
		return new CopperPipeTileEntity();
	}
}
