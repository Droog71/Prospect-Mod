package com.droog71.prospect.blocks.energy;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tilentity.TransformerTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Transformer extends ProspectBlock
{
	private int maxReceive;
	private int rating;
	private int capacity;
	
	public Transformer(String name, Material material,int maxReceive, int capacity, int rating) 
	{
		super(name, material);
		this.maxReceive = maxReceive;
		this.capacity = capacity;
		this.rating = rating;		
	}

	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TransformerTileEntity(maxReceive, capacity, rating);
	}
}
