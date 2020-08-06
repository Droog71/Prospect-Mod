package com.droog71.prospect.potion;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import com.droog71.prospect.init.ProspectItems;
import ic2.core.platform.registry.Ic2Items;
import net.machinemuse.powersuits.common.MPSItems;
import techguns.TGArmors;

public class SporeArmorList 
{	
	public List<Item> protectiveArmorList()
	{
		List<Item> list = new ArrayList<Item>();
		
		list.add(ProspectItems.helmet);
		list.add(ProspectItems.suit);
		list.add(ProspectItems.pants);
		list.add(ProspectItems.boots);
		
		if (Loader.isModLoaded("ic2"))
		{
			list.add(Ic2Items.hazmatHelmet.getItem());
			list.add(Ic2Items.hazmatChest.getItem());
			list.add(Ic2Items.hazmatLeggings.getItem());
			list.add(Ic2Items.hazmatBoots.getItem());
		}
		
		if (Loader.isModLoaded("techguns"))
		{
			list.add(TGArmors.hazmat_Helmet);
			list.add(TGArmors.hazmat_Chestplate);
			list.add(TGArmors.hazmat_Leggings);
			list.add(TGArmors.hazmat_Boots);
			list.add(TGArmors.steam_Helmet);
			list.add(TGArmors.steam_Chestplate);
			list.add(TGArmors.steam_Leggings);
			list.add(TGArmors.steam_Boots);
			list.add(TGArmors.t3_miner_Helmet);
			list.add(TGArmors.t3_miner_Chestplate);
			list.add(TGArmors.t3_miner_Leggings);
			list.add(TGArmors.t3_miner_Boots);
			list.add(TGArmors.t3_power_Helmet);
			list.add(TGArmors.t3_power_Chestplate);
			list.add(TGArmors.t3_power_Leggings);
			list.add(TGArmors.t3_power_Boots);
			list.add(TGArmors.t4_power_Helmet);
			list.add(TGArmors.t4_power_Chestplate);
			list.add(TGArmors.t4_power_Leggings);
			list.add(TGArmors.t4_power_Boots);	
		}
		
		if (Loader.isModLoaded("powersuits"))
		{
			list.add(MPSItems.powerArmorHead);
			list.add(MPSItems.powerArmorTorso);
			list.add(MPSItems.powerArmorLegs);
			list.add(MPSItems.powerArmorFeet);
		}
		
		return list;
	}
}
