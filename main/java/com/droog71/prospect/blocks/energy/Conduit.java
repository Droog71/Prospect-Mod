package com.droog71.prospect.blocks.energy;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tile_entity.ConduitTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Conduit extends ProspectBlock
{
	private int rating;
	private int capacity;
	
	public Conduit(String name, Material material, int rating, int capacity) 
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
		return new ConduitTileEntity(this.rating,this.capacity);
	}
}
