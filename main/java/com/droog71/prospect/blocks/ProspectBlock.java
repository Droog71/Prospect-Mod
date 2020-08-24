package com.droog71.prospect.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.droog71.prospect.items.LaunchPadItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProspectBlock extends Block
{
	private LaunchPadItems launchPadItems = new LaunchPadItems();
	
	public ProspectBlock(String name, Material material) 
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		launchPadItems.init();
		tooltip.add(launchPadItems.getCurrentPayout(stack.getItem())+" IGC");
    }
}