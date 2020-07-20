package com.droog71.prospect.blocks.energy;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tilentity.PurifierTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Purifier extends ProspectBlock
{
	public boolean powered;
	
	public Purifier(String name, Material material) 
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
		return new PurifierTileEntity();
	}
}
