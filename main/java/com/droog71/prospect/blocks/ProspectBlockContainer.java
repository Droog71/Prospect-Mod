package com.droog71.prospect.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ProspectBlockContainer extends BlockContainer
{
	public ProspectBlockContainer(String name, Material material) 
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return null;
	}
}
