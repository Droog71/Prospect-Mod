package com.droog71.prospect.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.droog71.prospect.items.LaunchPadItems;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProspectBlockContainer extends BlockContainer
{
	private LaunchPadItems launchPadItems = new LaunchPadItems();
	
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		launchPadItems.init();
		tooltip.add(launchPadItems.getCurrentPayout(stack.getItem())+" IGC");
    }
}
