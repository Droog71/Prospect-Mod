package com.droog71.prospect.blocks.ore;

import java.util.Random;
import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.init.ProspectItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class LivingOre extends ProspectBlock
{
	public LivingOre(String name, Material material) 
	{
		super(name, material);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int i) 
	{
		return ProspectItems.gem;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
	    return 1 + random.nextInt(3);
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
	    if (fortune > 0)
	    {
	    	return 1 + random.nextInt(5);
	    }
	    else
	    {
	       return this.quantityDropped(random);
	    }
	}
}