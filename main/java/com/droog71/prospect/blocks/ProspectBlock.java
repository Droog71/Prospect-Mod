package com.droog71.prospect.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ProspectBlock extends Block
{
	public ProspectBlock(String name, Material material) 
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
	}
}