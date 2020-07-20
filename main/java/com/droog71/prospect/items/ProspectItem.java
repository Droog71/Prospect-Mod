package com.droog71.prospect.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ProspectItem extends Item 
{
	public ProspectItem(String name) 
	{
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	@Override
    public int getItemEnchantability()
    {
        return 0;
    }
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }
}