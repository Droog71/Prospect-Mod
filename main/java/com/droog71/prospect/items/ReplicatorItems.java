package com.droog71.prospect.items;

import java.util.ArrayList;
import java.util.List;
import com.droog71.prospect.tile_entity.ReplicatorTileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ReplicatorItems 
{
	public boolean replicatorItem(ReplicatorTileEntity replicator, ItemStack stack)
    {
    	Item item = stack.getItem();

    	if (item == Items.ENDER_PEARL || item == Items.GHAST_TEAR || item == Items.BLAZE_POWDER || item == Items.NETHER_WART)
    	{
    		replicator.itemTier = 5;
    		return true;
    	}
    	if (item == Items.DIAMOND || item == Items.EMERALD)
    	{
    		replicator.itemTier = 4;
    		return true;
    	}
    	if (item == Items.IRON_INGOT || item == Items.GOLD_INGOT || item == Items.REDSTONE)
    	{
    		replicator.itemTier = 3;
    		return true;
    	}
    	if (item == Items.GLOWSTONE_DUST || item == Items.CLAY_BALL || item == Items.QUARTZ || item == Items.COAL || item == Items.STRING)
    	{
    		replicator.itemTier = 2;
    		return true;
    	}
    	if (item == Item.getItemFromBlock(Blocks.PLANKS) || item == Item.getItemFromBlock(Blocks.COBBLESTONE) || item == Item.getItemFromBlock(Blocks.WOOL))
    	{
    		replicator.itemTier = 1;
    		return true;
    	}
    	
    	List<NonNullList<ItemStack>> oreDictList = new ArrayList<NonNullList<ItemStack>>();
    	String[] listNames = { "ingotCopper", "ingotTin", "ingotSilver", "ingotLead", "ingotAluminum", "silicon" };
    	for (String name : listNames)
    	{
    		oreDictList.add(OreDictionary.getOres(name));
    	}
    	
    	for (NonNullList<ItemStack> list : oreDictList)
    	{
    		for (ItemStack s : list)
        	{ 		
        		if (s.getItem().getRegistryName() == stack.getItem().getRegistryName())
        		{
        			if (s.getMetadata() == stack.getMetadata())
        			{
        				replicator.itemTier = 3;
        				return true;
        			}			
        		}
        	}
    	}
    	
    	return false;
    }
}
