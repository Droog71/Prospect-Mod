package com.droog71.prospect.blocks.energy;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tilentity.CableTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Cable extends ProspectBlock
{
	private int rating;
	private int capacity;
	
	public Cable(String name, Material material, int rating, int capacity) 
	{
		super(name, material);
		this.rating = rating;
		this.capacity = capacity;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new CableTileEntity(this.rating,this.capacity);
	}
}
