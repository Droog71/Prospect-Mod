package com.droog71.prospect.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProspectItem extends Item 
{
	private LaunchPadItems launchPadItems = new LaunchPadItems();
	
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		launchPadItems.init();
		tooltip.add(launchPadItems.getCurrentPayout(stack.getItem())+" IGC");
    }
}