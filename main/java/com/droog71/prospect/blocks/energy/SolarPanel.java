package com.droog71.prospect.blocks.energy;

import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.tile_entity.SolarPanelTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SolarPanel extends ProspectBlock
{	
	private int rating;
	private int capacity;
	private int tier;
	
	public SolarPanel(String name, Material material, int capacity, int rating, int tier) 
	{
		super(name, material);
		this.capacity = capacity;
		this.rating = rating;	
		this.tier = tier;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new SolarPanelTileEntity(capacity, rating, tier);
	}
}
