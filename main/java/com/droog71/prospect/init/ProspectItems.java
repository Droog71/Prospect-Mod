package com.droog71.prospect.init;

import com.droog71.prospect.Prospect;
import com.droog71.prospect.armor.ProspectArmor;
import com.droog71.prospect.items.ProspectItem;
import com.droog71.prospect.items.Schematic;
import com.droog71.prospect.items.SporeFilter;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=Prospect.MODID)
public class ProspectItems 
{
	public static Item helmet;
	public static Item suit;
	public static Item pants;
	public static Item boots;
	public static Item filter;
	public static Item suit_material;	
	public static Item gem;
	public static Item credit;
	public static Item copper_wire;
	public static Item mv_wire;
	public static Item hv_wire;
	public static Item ev_wire;
	public static Item iv_wire;
	public static Item in_lv_wire;
	public static Item in_mv_wire;
	public static Item in_hv_wire;
	public static Item in_ev_wire;
	public static Item in_iv_wire;
	public static Item lv_coil;
	public static Item mv_coil;
	public static Item hv_coil;
	public static Item ev_coil;
	public static Item iv_coil;
	public static Item circuit;
	public static Item prepared_circuit;
	public static Item quantum_circuit;
	public static Item copper_ingot;
	public static Item tin_ingot;
	public static Item silver_ingot;
	public static Item lead_ingot;
	public static Item aluminum_ingot;
	public static Item copper_plate;
	public static Item tin_plate;
	public static Item silver_plate;
	public static Item lead_plate;
	public static Item aluminum_plate;
	public static Item silicon;
	
	public static Item mv_wire_schematic;
	public static Item hv_wire_schematic;
	public static Item ev_wire_schematic;
	public static Item iv_wire_schematic;
	public static Item in_lv_wire_schematic;
	public static Item in_mv_wire_schematic;
	public static Item in_hv_wire_schematic;
	public static Item in_ev_wire_schematic;
	public static Item in_iv_wire_schematic;
	public static Item lv_coil_schematic;
	public static Item mv_coil_schematic;
	public static Item hv_coil_schematic;
	public static Item ev_coil_schematic;
	public static Item iv_coil_schematic;
	public static Item circuit_schematic;
	public static Item prepared_circuit_schematic;
	public static Item filter_schematic;	
	public static Item helmet_schematic;
	public static Item suit_schematic;
	public static Item pants_schematic;
	public static Item boots_schematic;		
	public static Item fabricator_schematic;
	public static Item quarry_schematic;
	public static Item purifier_schematic;
	public static Item launch_pad_schematic;
	public static Item replicator_schematic;
	public static Item extruder_schematic;	
	public static Item lv_cable_schematic;
	public static Item mv_cable_schematic;
	public static Item hv_cable_schematic;
	public static Item ev_cable_schematic;
	public static Item iv_cable_schematic;
	public static Item lv_transformer_schematic;
	public static Item mv_transformer_schematic;
	public static Item hv_transformer_schematic;
	public static Item ev_transformer_schematic;
	public static Item lv_solar_panel_schematic;
	public static Item mv_solar_panel_schematic;
	public static Item hv_solar_panel_schematic;
	public static Item ev_solar_panel_schematic;
	public static Item iv_solar_panel_schematic;
	
	public static Item chest_schematic;
	public static Item hopper_schematic;
	public static Item piston_schematic;
	
	public static void init() 
	{		
		// Armor
		helmet = new ProspectArmor("helmet",Prospect.tabProspect,ProspectArmor.PROSPECTOR_ARMOR, 0, EntityEquipmentSlot.HEAD);
		suit = new ProspectArmor("suit",Prospect.tabProspect,ProspectArmor.PROSPECTOR_ARMOR, 0, EntityEquipmentSlot.CHEST);
		pants = new ProspectArmor("pants",Prospect.tabProspect,ProspectArmor.PROSPECTOR_ARMOR, 1, EntityEquipmentSlot.LEGS);
		boots = new ProspectArmor("boots",Prospect.tabProspect,ProspectArmor.PROSPECTOR_ARMOR, 0, EntityEquipmentSlot.FEET);
		
		// Items
		filter = new SporeFilter("filter").setMaxDamage(100000).setCreativeTab(Prospect.tabProspect).setMaxStackSize(1);
		gem = new ProspectItem("gem").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);		
		credit = new ProspectItem("credit").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);	
		suit_material = new ProspectItem("suit_material").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		copper_wire = new ProspectItem("copper_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		mv_wire = new ProspectItem("mv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_wire = new ProspectItem("hv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_wire = new ProspectItem("ev_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		iv_wire = new ProspectItem("iv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_lv_wire = new ProspectItem("in_lv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_mv_wire = new ProspectItem("in_mv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_hv_wire = new ProspectItem("in_hv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_ev_wire = new ProspectItem("in_ev_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_iv_wire = new ProspectItem("in_iv_wire").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		lv_coil = new ProspectItem("lv_coil").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		mv_coil = new ProspectItem("mv_coil").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_coil = new ProspectItem("hv_coil").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_coil = new ProspectItem("ev_coil").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		iv_coil = new ProspectItem("iv_coil").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		circuit = new ProspectItem("circuit").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		prepared_circuit = new ProspectItem("prepared_circuit").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);		
		quantum_circuit = new ProspectItem("quantum_circuit").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		copper_ingot = new ProspectItem("copper_ingot").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		tin_ingot = new ProspectItem("tin_ingot").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		silver_ingot = new ProspectItem("silver_ingot").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		lead_ingot = new ProspectItem("lead_ingot").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		aluminum_ingot = new ProspectItem("aluminum_ingot").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		copper_plate = new ProspectItem("copper_plate").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		tin_plate = new ProspectItem("tin_plate").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		silver_plate = new ProspectItem("silver_plate").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		lead_plate = new ProspectItem("lead_plate").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		aluminum_plate = new ProspectItem("aluminum_plate").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		silicon = new ProspectItem("silicon").setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
			
		// Schematics
		boots_schematic = new Schematic("boots_schematic",0).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		pants_schematic = new Schematic("pants_schematic",1).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		suit_schematic = new Schematic("suit_schematic",2).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		helmet_schematic = new Schematic("helmet_schematic",3).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);	
		filter_schematic = new Schematic("filter_schematic",4).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		launch_pad_schematic = new Schematic("launch_pad_schematic",5).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);		
		fabricator_schematic = new Schematic("fabricator_schematic",6).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		extruder_schematic = new Schematic("extruder_schematic",7).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		purifier_schematic = new Schematic("purifier_schematic",8).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		quarry_schematic = new Schematic("quarry_schematic",9).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		replicator_schematic = new Schematic("replicator_schematic",10).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);								
		mv_wire_schematic = new Schematic("mv_wire_schematic",11).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_wire_schematic = new Schematic("hv_wire_schematic",12).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_wire_schematic = new Schematic("ev_wire_schematic",13).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		iv_wire_schematic = new Schematic("iv_wire_schematic",14).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_lv_wire_schematic = new Schematic("in_lv_wire_schematic",15).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_mv_wire_schematic = new Schematic("in_mv_wire_schematic",16).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_hv_wire_schematic = new Schematic("in_hv_wire_schematic",17).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_ev_wire_schematic = new Schematic("in_ev_wire_schematic",18).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		in_iv_wire_schematic = new Schematic("in_iv_wire_schematic",19).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		lv_coil_schematic = new Schematic("lv_coil_schematic",20).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		mv_coil_schematic = new Schematic("mv_coil_schematic",21).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_coil_schematic = new Schematic("hv_coil_schematic",22).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_coil_schematic = new Schematic("ev_coil_schematic",23).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		iv_coil_schematic = new Schematic("iv_coil_schematic",24).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		circuit_schematic = new Schematic("circuit_schematic",25).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		prepared_circuit_schematic = new Schematic("prepared_circuit_schematic",26).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);		
		lv_cable_schematic = new Schematic("lv_cable_schematic",27).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		mv_cable_schematic = new Schematic("mv_cable_schematic",28).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_cable_schematic = new Schematic("hv_cable_schematic",29).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_cable_schematic = new Schematic("ev_cable_schematic",30).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		iv_cable_schematic = new Schematic("iv_cable_schematic",31).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);		
		lv_transformer_schematic = new Schematic("lv_transformer_schematic",32).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		mv_transformer_schematic = new Schematic("mv_transformer_schematic",33).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_transformer_schematic = new Schematic("hv_transformer_schematic",34).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_transformer_schematic = new Schematic("ev_transformer_schematic",35).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		lv_solar_panel_schematic = new Schematic("lv_solar_panel_schematic",36).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		mv_solar_panel_schematic = new Schematic("mv_solar_panel_schematic",37).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hv_solar_panel_schematic = new Schematic("hv_solar_panel_schematic",38).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		ev_solar_panel_schematic = new Schematic("ev_solar_panel_schematic",39).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		iv_solar_panel_schematic = new Schematic("iv_solar_panel_schematic",40).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		chest_schematic = new Schematic("chest_schematic",41).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		hopper_schematic = new Schematic("hopper_schematic",42).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
		piston_schematic = new Schematic("piston_schematic",43).setCreativeTab(Prospect.tabProspect).setMaxStackSize(64);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) 
	{	
		// Armor
		event.getRegistry().registerAll(helmet);
		event.getRegistry().registerAll(suit);
		event.getRegistry().registerAll(pants);
		event.getRegistry().registerAll(boots);	
		
		// Items
		event.getRegistry().registerAll(filter);
		event.getRegistry().registerAll(gem);
		event.getRegistry().registerAll(credit);
		event.getRegistry().registerAll(suit_material);
		event.getRegistry().registerAll(copper_wire);
		event.getRegistry().registerAll(mv_wire);
		event.getRegistry().registerAll(hv_wire);
		event.getRegistry().registerAll(ev_wire);
		event.getRegistry().registerAll(iv_wire);
		event.getRegistry().registerAll(in_lv_wire);
		event.getRegistry().registerAll(in_mv_wire);
		event.getRegistry().registerAll(in_hv_wire);
		event.getRegistry().registerAll(in_ev_wire);
		event.getRegistry().registerAll(in_iv_wire);
		event.getRegistry().registerAll(lv_coil);
		event.getRegistry().registerAll(mv_coil);
		event.getRegistry().registerAll(hv_coil);
		event.getRegistry().registerAll(ev_coil);
		event.getRegistry().registerAll(iv_coil);
		event.getRegistry().registerAll(circuit);
		event.getRegistry().registerAll(prepared_circuit);
		event.getRegistry().registerAll(quantum_circuit);
		event.getRegistry().registerAll(copper_ingot);
		event.getRegistry().registerAll(tin_ingot);
		event.getRegistry().registerAll(silver_ingot);
		event.getRegistry().registerAll(lead_ingot);
		event.getRegistry().registerAll(aluminum_ingot);
		event.getRegistry().registerAll(copper_plate);
		event.getRegistry().registerAll(tin_plate);
		event.getRegistry().registerAll(silver_plate);
		event.getRegistry().registerAll(lead_plate);
		event.getRegistry().registerAll(aluminum_plate);
		event.getRegistry().registerAll(silicon);
		
		// Schematics
		event.getRegistry().registerAll(purifier_schematic);
		event.getRegistry().registerAll(launch_pad_schematic);
		event.getRegistry().registerAll(fabricator_schematic);
		event.getRegistry().registerAll(helmet_schematic);
		event.getRegistry().registerAll(suit_schematic);
		event.getRegistry().registerAll(pants_schematic);
		event.getRegistry().registerAll(boots_schematic);		
		event.getRegistry().registerAll(filter_schematic);
		event.getRegistry().registerAll(quarry_schematic);
		event.getRegistry().registerAll(replicator_schematic);		
		event.getRegistry().registerAll(extruder_schematic);		
		event.getRegistry().registerAll(lv_cable_schematic);
		event.getRegistry().registerAll(mv_cable_schematic);
		event.getRegistry().registerAll(hv_cable_schematic);
		event.getRegistry().registerAll(ev_cable_schematic);
		event.getRegistry().registerAll(iv_cable_schematic);		
		event.getRegistry().registerAll(lv_transformer_schematic);
		event.getRegistry().registerAll(mv_transformer_schematic);
		event.getRegistry().registerAll(hv_transformer_schematic);
		event.getRegistry().registerAll(ev_transformer_schematic);	
		event.getRegistry().registerAll(lv_solar_panel_schematic);
		event.getRegistry().registerAll(mv_solar_panel_schematic);
		event.getRegistry().registerAll(hv_solar_panel_schematic);
		event.getRegistry().registerAll(ev_solar_panel_schematic);
		event.getRegistry().registerAll(iv_solar_panel_schematic);
		event.getRegistry().registerAll(mv_wire_schematic);
		event.getRegistry().registerAll(hv_wire_schematic);
		event.getRegistry().registerAll(ev_wire_schematic);
		event.getRegistry().registerAll(iv_wire_schematic);
		event.getRegistry().registerAll(in_lv_wire_schematic);
		event.getRegistry().registerAll(in_mv_wire_schematic);
		event.getRegistry().registerAll(in_hv_wire_schematic);
		event.getRegistry().registerAll(in_ev_wire_schematic);
		event.getRegistry().registerAll(in_iv_wire_schematic);
		event.getRegistry().registerAll(lv_coil_schematic);
		event.getRegistry().registerAll(mv_coil_schematic);
		event.getRegistry().registerAll(hv_coil_schematic);
		event.getRegistry().registerAll(ev_coil_schematic);
		event.getRegistry().registerAll(iv_coil_schematic);
		event.getRegistry().registerAll(circuit_schematic);
		event.getRegistry().registerAll(prepared_circuit_schematic);
		event.getRegistry().registerAll(chest_schematic);
		event.getRegistry().registerAll(hopper_schematic);
		event.getRegistry().registerAll(piston_schematic);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) 
	{
		// Armor
		registerRender(filter);
		registerRender(helmet);
		registerRender(suit);
		registerRender(pants);
		registerRender(boots);	
		
		// Items
		registerRender(gem);
		registerRender(credit);
		registerRender(suit_material);
		registerRender(copper_wire);
		registerRender(mv_wire);
		registerRender(hv_wire);
		registerRender(ev_wire);
		registerRender(iv_wire);
		registerRender(in_lv_wire);
		registerRender(in_mv_wire);
		registerRender(in_hv_wire);
		registerRender(in_ev_wire);
		registerRender(in_iv_wire);
		registerRender(lv_coil);
		registerRender(mv_coil);
		registerRender(hv_coil);
		registerRender(ev_coil);
		registerRender(iv_coil);
		registerRender(circuit);
		registerRender(prepared_circuit);
		registerRender(quantum_circuit);
		registerRender(copper_ingot);
		registerRender(tin_ingot);
		registerRender(silver_ingot);
		registerRender(lead_ingot);
		registerRender(aluminum_ingot);
		registerRender(copper_plate);
		registerRender(tin_plate);
		registerRender(silver_plate);
		registerRender(lead_plate);
		registerRender(aluminum_plate);
		registerRender(silicon);
			
		// Schematics
		registerRender(helmet_schematic);
		registerRender(suit_schematic);
		registerRender(pants_schematic);
		registerRender(boots_schematic);		
		registerRender(quarry_schematic);
		registerRender(replicator_schematic);		
		registerRender(extruder_schematic);	
		registerRender(purifier_schematic);
		registerRender(launch_pad_schematic);
		registerRender(fabricator_schematic);		
		registerRender(lv_transformer_schematic);
		registerRender(mv_transformer_schematic);
		registerRender(hv_transformer_schematic);
		registerRender(ev_transformer_schematic);
		registerRender(lv_solar_panel_schematic);
		registerRender(mv_solar_panel_schematic);
		registerRender(hv_solar_panel_schematic);
		registerRender(ev_solar_panel_schematic);
		registerRender(iv_solar_panel_schematic);
		registerRender(lv_cable_schematic);
		registerRender(mv_cable_schematic);
		registerRender(hv_cable_schematic);
		registerRender(ev_cable_schematic);
		registerRender(iv_cable_schematic);		
		registerRender(filter_schematic);
		registerRender(mv_wire_schematic);
		registerRender(hv_wire_schematic);
		registerRender(ev_wire_schematic);
		registerRender(iv_wire_schematic);
		registerRender(in_lv_wire_schematic);
		registerRender(in_mv_wire_schematic);
		registerRender(in_hv_wire_schematic);
		registerRender(in_ev_wire_schematic);
		registerRender(in_iv_wire_schematic);
		registerRender(lv_coil_schematic);
		registerRender(mv_coil_schematic);
		registerRender(hv_coil_schematic);
		registerRender(ev_coil_schematic);
		registerRender(iv_coil_schematic);
		registerRender(circuit_schematic);
		registerRender(prepared_circuit_schematic);		
		registerRender(chest_schematic);
		registerRender(hopper_schematic);
		registerRender(piston_schematic);
	}
	
	private static void registerRender(Item item) 
	{
		Prospect.proxy.registerRenderInformation(item);
	}
}