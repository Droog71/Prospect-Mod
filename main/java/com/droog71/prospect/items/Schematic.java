package com.droog71.prospect.items;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Schematic extends ProspectItem
{   
	public int id;
	
    public Schematic(String name, int unique_id) 
    {
		super(name);
		id = unique_id;
	}
    
    public ItemStack getResult() //Returns the item the schematic produces in the fabricator.
    {
    	if (id == 0)
    	{   		
    		return new ItemStack(ProspectItems.boots);
    	}
    	if (id == 1)
    	{
    		return new ItemStack(ProspectItems.pants);
    	}
    	if (id == 2)
    	{
    		return new ItemStack(ProspectItems.suit);
    	}
    	if (id == 3)
    	{
    		return new ItemStack(ProspectItems.helmet);
    	}
    	if (id == 4)
    	{
    		return new ItemStack(ProspectItems.filter);
    	}
    	if (id == 5)
    	{
    		return new ItemStack(ProspectBlocks.launch_pad);
    	}       	
    	if (id == 6)
    	{
    		return new ItemStack(ProspectBlocks.fabricator);
    	}
    	if (id == 7)
    	{
    		return new ItemStack(ProspectBlocks.extruder);
    	}
    	if (id == 8)
    	{
    		return new ItemStack(ProspectBlocks.purifier);
    	}
    	if (id == 9)
    	{
    		return new ItemStack(ProspectBlocks.quarry);
    	}
    	if (id == 10)
    	{
    		return new ItemStack(ProspectBlocks.replicator);
    	}
    	if (id == 11)
    	{
    		return new ItemStack(ProspectItems.mv_wire);
    	}
    	if (id == 12)
    	{
    		return new ItemStack(ProspectItems.hv_wire);
    	}
    	if (id == 13)
    	{
    		return new ItemStack(ProspectItems.ev_wire);
    	}
    	if (id == 14)
    	{
    		return new ItemStack(ProspectItems.iv_wire);
    	}
    	if (id == 15)
    	{
    		return new ItemStack(ProspectItems.in_lv_wire);
    	}
    	if (id == 16)
    	{
    		return new ItemStack(ProspectItems.in_mv_wire);
    	}
    	if (id == 17)
    	{
    		return new ItemStack(ProspectItems.in_hv_wire);
    	}
    	if (id == 18)
    	{
    		return new ItemStack(ProspectItems.in_ev_wire);
    	}
    	if (id == 19)
    	{
    		return new ItemStack(ProspectItems.in_iv_wire);
    	}
    	if (id == 20)
    	{
    		return new ItemStack(ProspectItems.lv_coil);
    	}
    	if (id == 21)
    	{
    		return new ItemStack(ProspectItems.mv_coil);
    	}
    	if (id == 22)
    	{
    		return new ItemStack(ProspectItems.hv_coil);
    	}
    	if (id == 23)
    	{
    		return new ItemStack(ProspectItems.ev_coil);
    	}
    	if (id == 24)
    	{
    		return new ItemStack(ProspectItems.iv_coil);
    	}
    	if (id == 25)
    	{
    		return new ItemStack(ProspectItems.circuit);
    	}
    	if (id == 26)
    	{
    		return new ItemStack(ProspectItems.prepared_circuit);
    	}
    	if (id == 27)
    	{
    		return new ItemStack(ProspectBlocks.lv_cable);
    	}
    	if (id == 28)
    	{
    		return new ItemStack(ProspectBlocks.mv_cable);
    	}
    	if (id == 29)
    	{
    		return new ItemStack(ProspectBlocks.hv_cable);
    	}
    	if (id == 30)
    	{
    		return new ItemStack(ProspectBlocks.ev_cable);
    	}
    	if (id == 31)
    	{
    		return new ItemStack(ProspectBlocks.iv_cable);
    	}
    	if (id == 32)
    	{
    		return new ItemStack(ProspectBlocks.lv_transformer);
    	}
    	if (id == 33)
    	{
    		return new ItemStack(ProspectBlocks.mv_transformer);
    	}
    	if (id == 34)
    	{
    		return new ItemStack(ProspectBlocks.hv_transformer);
    	}
    	if (id == 35)
    	{
    		return new ItemStack(ProspectBlocks.ev_transformer);
    	}
    	if (id == 36)
    	{
    		return new ItemStack(ProspectBlocks.lv_solar_panel);
    	}
    	if (id == 37)
    	{
    		return new ItemStack(ProspectBlocks.mv_solar_panel);
    	}
    	if (id == 38)
    	{
    		return new ItemStack(ProspectBlocks.hv_solar_panel);
    	}
    	if (id == 39)
    	{
    		return new ItemStack(ProspectBlocks.ev_solar_panel);
    	}
    	if (id == 40)
    	{
    		return new ItemStack(ProspectBlocks.iv_solar_panel);
    	}
    	if (id == 41)
    	{
    		return new ItemStack(Blocks.CHEST);
    	}
    	if (id == 42)
    	{
    		return new ItemStack(Blocks.HOPPER);
    	}
    	if (id == 43)
    	{
    		return new ItemStack(Blocks.PISTON);
    	}
    	if (id == 44)
    	{
    		return new ItemStack(ProspectBlocks.bio_fuel_generator);
    	}
    	if (id == 45)
    	{
    		return new ItemStack(ProspectBlocks.zero_point_reactor);
    	}
    	if (id == 46)
    	{
    		return new ItemStack(ProspectBlocks.zero_point_cooler);
    	}
    	if (id == 47)
    	{
    		return new ItemStack(ProspectBlocks.conveyor_tube);
    	}
    	if (id == 48)
    	{
    		return new ItemStack(ProspectItems.bio_fuel);
    	}
    	if (id == 49)
    	{
    		return new ItemStack(ProspectItems.refrigerant);
    	}
    	if (id == 50)
    	{
    		return new ItemStack(ProspectItems.motor);
    	}
    	return null;
    }
    
    public ItemStack[] getIngredients() //Returns the ingredients for the crafting recipe the schematic produces.
    {
    	if (id == 0)
    	{   		
    		ItemStack suit_material = new ItemStack(ProspectItems.suit_material,4);
    		ItemStack[] required = {suit_material};
    		return required;
    	}
    	if (id == 1)
    	{
    		ItemStack suit_material = new ItemStack(ProspectItems.suit_material,7);
    		ItemStack[] required = {suit_material};
    		return required;
    	}
    	if (id == 2)
    	{
    		ItemStack suit_material = new ItemStack(ProspectItems.suit_material,8);
    		ItemStack[] required = {suit_material};
    		return required;
    	}
    	if (id == 3)
    	{
    		ItemStack suit_material = new ItemStack(ProspectItems.suit_material,8);
    		ItemStack glass = new ItemStack(Blocks.GLASS,1);
    		ItemStack[] required = {suit_material,glass};
    		return required;
    	}
    	if (id == 4)
    	{
    		ItemStack tin_plate = new ItemStack(ProspectItems.tin_plate,6);
    		ItemStack suit_material = new ItemStack(ProspectItems.suit_material,2);
    		ItemStack charcoal = new ItemStack(Items.COAL,1,1);
    		ItemStack[] required = {tin_plate,suit_material,charcoal};
    		return required;
    	}
    	if (id == 5)
    	{
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,5);
    		ItemStack quantum_circuit = new ItemStack(ProspectItems.quantum_circuit,2);
    		ItemStack hopper = new ItemStack(Blocks.HOPPER,1);
    		ItemStack[] required = {lead_plate,quantum_circuit,hopper};
    		return required;
    	}       	
    	if (id == 6)
    	{
    		ItemStack tin_plate = new ItemStack(ProspectItems.tin_plate,4);
    		ItemStack quantum_circuit = new ItemStack(ProspectItems.quantum_circuit,4);
    		ItemStack replicator = new ItemStack(ProspectBlocks.replicator,1);
    		ItemStack[] required = {tin_plate,quantum_circuit,replicator};
    		return required;
    	}
    	if (id == 7)
    	{
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,6);
    		ItemStack lv_coil = new ItemStack(ProspectItems.lv_coil,1);
    		ItemStack hopper = new ItemStack(Blocks.HOPPER,1);   		
    		ItemStack piston = new ItemStack(Blocks.PISTON,1);
    		ItemStack[] required = {lead_plate,lv_coil,hopper,piston};
    		return required;
    	}
    	if (id == 8)
    	{
    		ItemStack tin_plate = new ItemStack(ProspectItems.tin_plate,4);
    		ItemStack hopper = new ItemStack(Blocks.HOPPER,4);   		
    		ItemStack filter = new ItemStack(ProspectItems.filter,1);
    		ItemStack[] required = {tin_plate,hopper,filter};
    		return required;
    	}
    	if (id == 9)
    	{
    		ItemStack in_iv_wire = new ItemStack(ProspectItems.in_iv_wire,2);
    		ItemStack ev_transformer = new ItemStack(ProspectBlocks.ev_transformer,2);    		 		
    		ItemStack piston = new ItemStack(Blocks.PISTON,2);
    		ItemStack quantum_circuit = new ItemStack(ProspectItems.quantum_circuit,1);  
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,2);
    		ItemStack[] required = {in_iv_wire,ev_transformer,piston,quantum_circuit,lead_plate};
    		return required;
    	}
    	if (id == 10)
    	{
    		ItemStack tin_plate = new ItemStack(ProspectItems.tin_plate,4);
    		ItemStack ev_transformer = new ItemStack(ProspectBlocks.ev_transformer,1);   	
    		ItemStack quantum_circuit = new ItemStack(ProspectItems.quantum_circuit,3); 
    		ItemStack diamond = new ItemStack(Items.DIAMOND,1);
    		ItemStack[] required = {tin_plate,ev_transformer,quantum_circuit,diamond};
    		return required;
    	}
    	if (id == 11)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,2); 
    		ItemStack[] required = {copper_wire};
    		return required;
    	}
    	if (id == 12)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,3); 
    		ItemStack[] required = {copper_wire};
    		return required;
    	}
    	if (id == 13)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,4); 
    		ItemStack[] required = {copper_wire};
    		return required;
    	}
    	if (id == 14)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,5); 
    		ItemStack[] required = {copper_wire};
    		return required;
    	}
    	if (id == 15)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,1);
    		ItemStack wool = new ItemStack(Blocks.WOOL,1,0);
    		ItemStack[] required = {copper_wire,wool};
    		return required;
    	}
    	if (id == 16)
    	{
    		ItemStack mv_wire = new ItemStack(ProspectItems.mv_wire,1);
    		ItemStack wool = new ItemStack(Blocks.WOOL,1,0);
    		ItemStack[] required = {mv_wire,wool};
    		return required;
    	}
    	if (id == 17)
    	{
    		ItemStack hv_wire = new ItemStack(ProspectItems.hv_wire,1);
    		ItemStack wool = new ItemStack(Blocks.WOOL,1,0);
    		ItemStack[] required = {hv_wire,wool};
    		return required;
    	}
    	if (id == 18)
    	{
    		ItemStack ev_wire = new ItemStack(ProspectItems.ev_wire,1);
    		ItemStack wool = new ItemStack(Blocks.WOOL,1,0);
    		ItemStack[] required = {ev_wire,wool};
    		return required;
    	}
    	if (id == 19)
    	{
    		ItemStack iv_wire = new ItemStack(ProspectItems.iv_wire,1);
    		ItemStack wool = new ItemStack(Blocks.WOOL,1,0);
    		ItemStack[] required = {iv_wire,wool};
    		return required;
    	}
    	if (id == 20)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,8); 
    		ItemStack[] required = {copper_wire};
    		return required;
    	}
    	if (id == 21)
    	{
    		ItemStack mv_wire = new ItemStack(ProspectItems.mv_wire,8); 
    		ItemStack[] required = {mv_wire};
    		return required;
    	}
    	if (id == 22)
    	{
    		ItemStack hv_wire = new ItemStack(ProspectItems.hv_wire,8); 
    		ItemStack[] required = {hv_wire};
    		return required;
    	}
    	if (id == 23)
    	{
    		ItemStack ev_wire = new ItemStack(ProspectItems.ev_wire,8); 
    		ItemStack[] required = {ev_wire};
    		return required;
    	}
    	if (id == 24)
    	{
    		ItemStack iv_wire = new ItemStack(ProspectItems.iv_wire,8); 
    		ItemStack[] required = {iv_wire};
    		return required;
    	}
    	if (id == 25)
    	{
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,4);
    		ItemStack silver_plate = new ItemStack(ProspectItems.silver_plate,4);   	
    		ItemStack silicon = new ItemStack(ProspectItems.silicon,1); 
    		ItemStack[] required = {copper_wire,silver_plate,silicon};
    		return required;
    	}
    	if (id == 26)
    	{
    		ItemStack redstone = new ItemStack(Items.REDSTONE,4);
    		ItemStack glowstone_dust = new ItemStack(Items.GLOWSTONE_DUST,4);   	
    		ItemStack circuit = new ItemStack(ProspectItems.circuit,1); 
    		ItemStack[] required = {redstone,glowstone_dust,circuit};
    		return required;
    	}
    	if (id == 27)
    	{
    		ItemStack in_lv_wire = new ItemStack(ProspectItems.in_lv_wire,1); 
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,8); 
    		ItemStack[] required = {in_lv_wire,aluminum_plate};
    		return required;
    	}
    	if (id == 28)
    	{
    		ItemStack in_mv_wire = new ItemStack(ProspectItems.in_mv_wire,1); 
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,8); 
    		ItemStack[] required = {in_mv_wire,aluminum_plate};
    		return required;
    	}
    	if (id == 29)
    	{
    		ItemStack in_hv_wire = new ItemStack(ProspectItems.in_hv_wire,1); 
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,8); 
    		ItemStack[] required = {in_hv_wire,aluminum_plate};
    		return required;
    	}
    	if (id == 30)
    	{
    		ItemStack in_ev_wire = new ItemStack(ProspectItems.in_ev_wire,1); 
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,8); 
    		ItemStack[] required = {in_ev_wire,aluminum_plate};
    		return required;
    	}
    	if (id == 31)
    	{
    		ItemStack in_iv_wire = new ItemStack(ProspectItems.in_iv_wire,1); 
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,8); 
    		ItemStack[] required = {in_iv_wire,aluminum_plate};
    		return required;
    	}
    	if (id == 32)
    	{
    		ItemStack lv_coil = new ItemStack(ProspectItems.lv_coil,1); 
    		ItemStack mv_coil = new ItemStack(ProspectItems.mv_coil,1); 
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,6); 
    		ItemStack[] required = {lv_coil,mv_coil,lead_plate};
    		return required;
    	}
    	if (id == 33)
    	{
    		ItemStack hv_coil = new ItemStack(ProspectItems.hv_coil,1); 
    		ItemStack mv_coil = new ItemStack(ProspectItems.mv_coil,1); 
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,6); 
    		ItemStack[] required = {hv_coil,mv_coil,lead_plate};
    		return required;
    	}
    	if (id == 34)
    	{
    		ItemStack hv_coil = new ItemStack(ProspectItems.hv_coil,1); 
    		ItemStack ev_coil = new ItemStack(ProspectItems.ev_coil,1); 
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,6); 
    		ItemStack[] required = {hv_coil,ev_coil,lead_plate};
    		return required;
    	}
    	if (id == 35)
    	{
    		ItemStack ev_coil = new ItemStack(ProspectItems.ev_coil,1); 
    		ItemStack iv_coil = new ItemStack(ProspectItems.iv_coil,1); 
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,6); 
    		ItemStack[] required = {ev_coil,iv_coil,lead_plate};
    		return required;
    	}
    	if (id == 36)
    	{
    		ItemStack glass = new ItemStack(Blocks.GLASS,3);
    		ItemStack tin_plate = new ItemStack(ProspectItems.tin_plate,4);   	
    		ItemStack copper_wire = new ItemStack(ProspectItems.copper_wire,1); 
    		ItemStack lv_transformer = new ItemStack(ProspectBlocks.lv_transformer,1); 
    		ItemStack[] required = {glass,tin_plate,copper_wire,lv_transformer};
    		return required;
    	}
    	if (id == 37)
    	{
    		ItemStack lv_transformer = new ItemStack(ProspectBlocks.lv_transformer,1); 	
    		ItemStack lv_solar_panel = new ItemStack(ProspectBlocks.lv_solar_panel,8);
    		ItemStack[] required = {lv_transformer,lv_solar_panel};
    		return required;
    	}
    	if (id == 38)
    	{
    		ItemStack mv_transformer = new ItemStack(ProspectBlocks.mv_transformer,1);   	
    		ItemStack mv_solar_panel = new ItemStack(ProspectBlocks.mv_solar_panel,8);
    		ItemStack[] required = {mv_transformer,mv_solar_panel};
    		return required;
    	}
    	if (id == 39)
    	{
    		ItemStack hv_transformer = new ItemStack(ProspectBlocks.hv_transformer,1);  		
    		ItemStack hv_solar_panel = new ItemStack(ProspectBlocks.hv_solar_panel,8);
    		ItemStack[] required = {hv_transformer,hv_solar_panel};
    		return required;
    	}
    	if (id == 40)
    	{
    		ItemStack ev_transformer = new ItemStack(ProspectBlocks.ev_transformer,1);  		
    		ItemStack ev_solar_panel = new ItemStack(ProspectBlocks.ev_solar_panel,8);
    		ItemStack[] required = {ev_transformer,ev_solar_panel};
    		return required;
    	}
    	if (id == 41)
    	{
    		ItemStack wood = new ItemStack(Blocks.PLANKS,8); 
    		ItemStack[] required = {wood};
    		return required;
    	}
    	if (id == 42)
    	{
    		ItemStack chest = new ItemStack(Blocks.CHEST,1); 
    		ItemStack iron_ingot = new ItemStack(Items.IRON_INGOT,5); 
    		ItemStack[] required = {chest,iron_ingot};
    		return required;
    	}
    	if (id == 43)
    	{
    		ItemStack iron_ingot = new ItemStack(Items.IRON_INGOT,1); 
    		ItemStack redstone = new ItemStack(Items.REDSTONE,1);
    		ItemStack cobblestone = new ItemStack(Blocks.COBBLESTONE,4); 
    		ItemStack wood = new ItemStack(Blocks.PLANKS,3); 
    		ItemStack[] required = {iron_ingot,redstone,cobblestone,wood};
    		return required;
    	}
    	if (id == 44)
    	{
    		ItemStack iron_ingot = new ItemStack(Items.IRON_INGOT,4); 
    		ItemStack flint_and_steel = new ItemStack(Items.FLINT_AND_STEEL,1);
    		ItemStack cauldron = new ItemStack(Blocks.CAULDRON,1); 
    		ItemStack piston = new ItemStack(Blocks.PISTON,1); 
    		ItemStack copper_ingot = new ItemStack(ProspectItems.copper_ingot,2);
    		ItemStack[] required = {iron_ingot,flint_and_steel,cauldron,piston,copper_ingot};
    		return required;
    	}
    	if (id == 45)
    	{
    		ItemStack quantum_circuit = new ItemStack(ProspectItems.quantum_circuit,2); 
    		ItemStack motor = new ItemStack(ProspectItems.motor,2);
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,4); 
    		ItemStack replicator = new ItemStack(ProspectBlocks.replicator,1); 
    		ItemStack[] required = {quantum_circuit,motor,aluminum_plate,replicator};
    		return required;
    	}
    	if (id == 46)
    	{
    		ItemStack refrigerant = new ItemStack(ProspectItems.refrigerant,1); 
    		ItemStack motor = new ItemStack(ProspectItems.motor,1);
    		ItemStack aluminum_plate = new ItemStack(ProspectItems.aluminum_plate,6);
    		ItemStack piston = new ItemStack(Blocks.PISTON,1); 
    		ItemStack[] required = {refrigerant,motor,aluminum_plate,piston};
    		return required;
    	}
    	if (id == 47)
    	{
    		ItemStack motor = new ItemStack(ProspectItems.motor,1); 
    		ItemStack tin_plate = new ItemStack(ProspectItems.tin_plate,2);
    		ItemStack[] required = {motor,tin_plate};
    		return required;
    	}
    	if (id == 48)
    	{
    		ItemStack snowball = new ItemStack(Items.SNOWBALL,1); 
    		ItemStack coal = new ItemStack(Items.COAL,1);
    		ItemStack bucket = new ItemStack(Items.BUCKET,1); 
    		ItemStack ghast_tear = new ItemStack(Items.GHAST_TEAR,1); 
    		ItemStack[] required = {snowball,coal,bucket,ghast_tear};
    		return required;
    	}
    	if (id == 49)
    	{
    		ItemStack apple = new ItemStack(Items.APPLE,1); 
    		ItemStack rotten_flesh = new ItemStack(Items.ROTTEN_FLESH,1);
    		ItemStack bucket = new ItemStack(Items.BUCKET,1); 
    		ItemStack[] required = {apple,rotten_flesh,bucket};
    		return required;
    	}
    	if (id == 50)
    	{
    		ItemStack piston = new ItemStack(Blocks.PISTON,2);
    		ItemStack mv_coil = new ItemStack(ProspectItems.mv_coil,1);
    		ItemStack hv_coil = new ItemStack(ProspectItems.hv_coil,2);
    		ItemStack lead_plate = new ItemStack(ProspectItems.lead_plate,4);
    		ItemStack[] required = {piston,mv_coil,hv_coil,lead_plate};
    		return required;
    	}
    	return null;
    }
}