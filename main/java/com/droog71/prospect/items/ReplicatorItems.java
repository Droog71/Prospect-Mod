package com.droog71.prospect.items;

import java.util.ArrayList;
import java.util.List;
import com.droog71.prospect.config.ConfigHandler;

import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class ReplicatorItems 
{
	List<Item> itemTier3 = new ArrayList<Item>();
	List<Item> itemTier2 = new ArrayList<Item>();
	List<Item> itemTier1 = new ArrayList<Item>();
	
	public void init()
	{
		generateTier3ItemList();
		generateTier2ItemList();
		generateTier1ItemList();
	}
	
	public int getItemWorth(ItemStack stack)
    {
    	Item item = stack.getItem();

    	if (ConfigHandler.replicatorItems() != null)
    	{
    		for (ReplicatorItem replicatorItem : ConfigHandler.replicatorItems())
    		{
    			ResourceLocation location = new ResourceLocation(replicatorItem.name);
    			Item foundItem = Item.REGISTRY.getObject(location);
    			if (foundItem == item)
    			{
    				return replicatorItem.worth;
    			}
    		}
    	}
    	
    	if (itemTier3.contains(item))
    	{
    		return 16;
    	}
    	if (itemTier2.contains(item))
    	{
    		return 8;
    	}
    	if (itemTier1.contains(item))
    	{
    		return 1;
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
        				return 1;
        			}			
        		}
        	}
    	}

    	return 0;
    }
	
	private void generateTier3ItemList()
	{
		itemTier3.add(Items.ENDER_PEARL); 
		itemTier3.add(Items.GHAST_TEAR);
		itemTier3.add(Items.BLAZE_POWDER);
		itemTier3.add(Items.NETHER_WART);
	}
	
	private void generateTier2ItemList()
	{
		itemTier2.add(Items.DIAMOND);
		itemTier2.add(Items.EMERALD);
		if (Loader.isModLoaded("ic2"))
		{
			itemTier2.add(Ic2Items.uraniumDrop.getItem());
		}
	}
	
	private void generateTier1ItemList()
	{
		itemTier1.add(Items.IRON_INGOT); 
		itemTier1.add(Items.GOLD_INGOT);
		itemTier1.add(Items.REDSTONE);
		itemTier1.add(Items.GLOWSTONE_DUST);
		itemTier1.add(Items.CLAY_BALL);
		itemTier1.add(Items.QUARTZ);
		itemTier1.add(Items.COAL);
		itemTier1.add(Items.STRING);
		itemTier1.add(Item.getItemFromBlock(Blocks.PLANKS));
		itemTier1.add(Item.getItemFromBlock(Blocks.COBBLESTONE));
		itemTier1.add(Item.getItemFromBlock(Blocks.WOOL));
	}
}
