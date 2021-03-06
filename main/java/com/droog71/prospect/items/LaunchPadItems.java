package com.droog71.prospect.items;

import java.util.ArrayList;
import java.util.List;
import com.droog71.prospect.config.ConfigHandler;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import techguns.TGBlocks;
import techguns.TGItems;

public class LaunchPadItems 
{	
	List<Item> itemTier5 = new ArrayList<Item>();
	List<Item> itemTier4 = new ArrayList<Item>();
	List<Item> itemTier3 = new ArrayList<Item>();
	List<Item> itemTier2 = new ArrayList<Item>();
	List<Item> itemTier1 = new ArrayList<Item>();
	ReplicatorItems replicatorItems = new ReplicatorItems();
	
	public void init()
	{
		generateTier5ItemList();	
		generateTier4ItemList();
		generateTier3ItemList();
		generateTier2ItemList();
		generateTier1ItemList();
		replicatorItems.init();
	}
	
	public int getCurrentPayout(Item item)
    { 
		if (ConfigHandler.launchPadItems() != null)
		{
			for (LaunchPadItem lpItem : ConfigHandler.launchPadItems())
			{
				ResourceLocation location = new ResourceLocation(lpItem.name);
				Item foundItem = Item.REGISTRY.getObject(location);
				if (foundItem == item)
				{
					return lpItem.worth;
				}
			}
		}
		
		if (itemTier5.contains(item))
		{
			return 64;
		}
		if (itemTier4.contains(item))
		{
			return 32;
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
			return 4;
		}

		return replicatorItems.getItemWorth(new ItemStack(item));
    }

	private void generateTier5ItemList()
	{
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.zero_point_reactor));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.zero_point_cooler)); 
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.replicator));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.iv_solar_panel));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.ev_solar_panel));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.hv_solar_panel));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.mv_solar_panel)); 
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.quarry));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.fabricator));
		itemTier5.add(Item.getItemFromBlock(ProspectBlocks.purifier));		
			
		if (Loader.isModLoaded("techguns"))
		{
			itemTier5.add(TGBlocks.BASIC_MACHINE.getItemblock());
			itemTier5.add(TGItems.PLASMA_GENERATOR.getItem());
		}
	}
	
	private void generateTier4ItemList()
	{
		itemTier4.add(ProspectItems.quantum_circuit);
		itemTier4.add(ProspectItems.motor);
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.bio_fuel_generator)); 
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.extruder)); 
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.lv_solar_panel));	
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.hv_transformer));
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.ev_transformer));
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.mv_transformer));
		itemTier4.add(Item.getItemFromBlock(ProspectBlocks.lv_transformer));				
		
		if (Loader.isModLoaded("techguns"))
		{
			itemTier4.add(TGItems.CIRCUIT_BOARD_ELITE.getItem());
		}
	}
	
	private void generateTier3ItemList()
	{	
		itemTier3.add(Item.getItemFromBlock(ProspectBlocks.press));					
		
		if (Loader.isModLoaded("techguns"))
		{
			itemTier3.add(TGItems.ELECTRIC_ENGINE.getItem());
		}
	}
	
	private void generateTier2ItemList()
	{					
		if (Loader.isModLoaded("techguns"))
		{
			itemTier2.add(TGItems.CIRCUIT_BOARD_BASIC.getItem());
		}
	}
	
	private void generateTier1ItemList()
	{
		itemTier1.add(ProspectItems.gem);
		itemTier1.add(Item.getItemFromBlock(ProspectBlocks.conveyor_tube));		
		itemTier1.add(ProspectItems.in_iv_wire);
		itemTier1.add(ProspectItems.in_ev_wire);
		itemTier1.add(ProspectItems.in_hv_wire);
		itemTier1.add(ProspectItems.in_mv_wire);
		itemTier1.add(ProspectItems.in_lv_wire);
		itemTier1.add(Item.getItemFromBlock(ProspectBlocks.iv_conduit));
		itemTier1.add(Item.getItemFromBlock(ProspectBlocks.ev_conduit));
		itemTier1.add(Item.getItemFromBlock(ProspectBlocks.hv_conduit));
		itemTier1.add(Item.getItemFromBlock(ProspectBlocks.mv_conduit));
		itemTier1.add(Item.getItemFromBlock(ProspectBlocks.lv_conduit));
		
		if (Loader.isModLoaded("techguns"))
		{
			itemTier1.add(TGItems.ENERGY_CELL.getItem());
		}
	}
}
