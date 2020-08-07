package com.droog71.prospect.init;

import com.droog71.prospect.Prospect;
import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.blocks.energy.BioFuelGenerator;
import com.droog71.prospect.blocks.energy.Cable;
import com.droog71.prospect.blocks.energy.Extruder;
import com.droog71.prospect.blocks.energy.LaunchPad;
import com.droog71.prospect.blocks.energy.Press;
import com.droog71.prospect.blocks.energy.Fabricator;
import com.droog71.prospect.blocks.energy.Purifier;
import com.droog71.prospect.blocks.energy.Quarry;
import com.droog71.prospect.blocks.energy.Replicator;
import com.droog71.prospect.blocks.energy.SolarPanel;
import com.droog71.prospect.blocks.energy.Transformer;
import com.droog71.prospect.blocks.ore.LivingOre;
import com.droog71.prospect.tile_entity.BioGenTileEntity;
import com.droog71.prospect.tile_entity.CableTileEntity;
import com.droog71.prospect.tile_entity.ExtruderTileEntity;
import com.droog71.prospect.tile_entity.FabricatorTileEntity;
import com.droog71.prospect.tile_entity.LaunchPadTileEntity;
import com.droog71.prospect.tile_entity.PressTileEntity;
import com.droog71.prospect.tile_entity.PurifierTileEntity;
import com.droog71.prospect.tile_entity.QuarryTileEntity;
import com.droog71.prospect.tile_entity.ReplicatorTileEntity;
import com.droog71.prospect.tile_entity.SolarPanelTileEntity;
import com.droog71.prospect.tile_entity.TransformerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Prospect.MODID)
public class ProspectBlocks 
{
	public static Block living_ore;
	public static Block copper_ore;
	public static Block tin_ore;
	public static Block silver_ore;
	public static Block lead_ore;
	public static Block aluminum_ore;
	public static Block silicon_ore;
	public static Block purifier;
	public static Block quarry;
	public static Block fabricator;
	public static Block replicator;
	public static Block quarry_frame;
	public static Block quarry_tube;
	public static Block quarry_drill;
	public static Block launch_pad;
	public static Block capsule;
	public static Block extruder;
	public static Block press;
	public static Block lv_cable;
	public static Block mv_cable;
	public static Block hv_cable;
	public static Block ev_cable;
	public static Block iv_cable;
	public static Block lv_transformer;
	public static Block mv_transformer;
	public static Block hv_transformer;
	public static Block ev_transformer;
	public static Block bio_fuel_generator;
	public static Block lv_solar_panel;
	public static Block mv_solar_panel;
	public static Block hv_solar_panel;
	public static Block ev_solar_panel;
	public static Block iv_solar_panel;
	static ExtruderTileEntity extruderTileEntity;
	static PressTileEntity pressTileEntity;
	static PurifierTileEntity purifierTileEntity;
	static FabricatorTileEntity fabricatorTileEntity;
	static QuarryTileEntity quarryTileEntity;
	static ReplicatorTileEntity replicatorTileEntity;
	static BioGenTileEntity bioGenTileEntity;
	static SolarPanelTileEntity solarPanelTileEntity;
	static LaunchPadTileEntity launchPadTileEntity;
	static CableTileEntity cableTileEntity;
	static TransformerTileEntity transformerTileEntity;
	
	public static void init() 
	{		
		extruder = new Extruder("extruder",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		press = new Press("press",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		purifier = new Purifier("purifier",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		quarry = new Quarry("quarry",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		fabricator = new Fabricator("fabricator",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		replicator = new Replicator("replicator",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		quarry_frame = new ProspectBlock("quarry_frame",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		quarry_tube = new ProspectBlock("quarry_tube",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		quarry_drill = new ProspectBlock("quarry_drill",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		launch_pad = new LaunchPad("launch_pad",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		capsule = new ProspectBlock("capsule",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		copper_ore = new ProspectBlock("copper_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		tin_ore = new ProspectBlock("tin_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		silver_ore = new ProspectBlock("silver_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		lead_ore = new ProspectBlock("lead_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		aluminum_ore = new ProspectBlock("aluminum_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		silicon_ore = new ProspectBlock("silicon_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		living_ore = new LivingOre("living_ore",Material.ROCK).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);			
		lv_cable = new Cable("lv_cable",Material.IRON,1000,5000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);	
		mv_cable = new Cable("mv_cable",Material.IRON,4000,20000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		hv_cable = new Cable("hv_cable",Material.IRON,9000,45000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		ev_cable = new Cable("ev_cable",Material.IRON,16000,80000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		iv_cable = new Cable("iv_cable",Material.IRON,25000,125000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		lv_transformer = new Transformer("lv_transformer",Material.IRON,4000,20000,1000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		mv_transformer = new Transformer("mv_transformer",Material.IRON,9000,45000,4000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		hv_transformer = new Transformer("hv_transformer",Material.IRON,16000,80000,9000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		ev_transformer = new Transformer("ev_transformer",Material.IRON,25000,125000,16000).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		bio_fuel_generator = new BioFuelGenerator("bio_fuel_generator",Material.IRON).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		lv_solar_panel = new SolarPanel("lv_solar_panel",Material.IRON,64,32,1).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		mv_solar_panel = new SolarPanel("mv_solar_panel",Material.IRON,256,128,2).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		hv_solar_panel = new SolarPanel("hv_solar_panel",Material.IRON,1024,512,3).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		ev_solar_panel = new SolarPanel("ev_solar_panel",Material.IRON,4096,2048,4).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		iv_solar_panel = new SolarPanel("iv_solar_panel",Material.IRON,16384,8192,5).setHardness(1.0f).setCreativeTab(Prospect.tabProspect);
		extruderTileEntity = new ExtruderTileEntity();
		pressTileEntity = new PressTileEntity();
		purifierTileEntity = new PurifierTileEntity();
		fabricatorTileEntity = new FabricatorTileEntity();
		quarryTileEntity = new QuarryTileEntity();
		replicatorTileEntity = new ReplicatorTileEntity();
		bioGenTileEntity = new BioGenTileEntity();
		solarPanelTileEntity = new SolarPanelTileEntity();
		launchPadTileEntity = new LaunchPadTileEntity();
		cableTileEntity = new CableTileEntity(0,0);
		transformerTileEntity = new TransformerTileEntity(0,0,0);
		GameRegistry.registerTileEntity(pressTileEntity.getClass(),"prospect:pressTileEntity");
		GameRegistry.registerTileEntity(extruderTileEntity.getClass(),"prospect:extruderTileEntity");
		GameRegistry.registerTileEntity(purifierTileEntity.getClass(), "prospect:purifierTileEntity");
		GameRegistry.registerTileEntity(fabricatorTileEntity.getClass(), "prospect:fabricatorTileEntity");
		GameRegistry.registerTileEntity(quarryTileEntity.getClass(), "prospect:quarryTileEntity");
		GameRegistry.registerTileEntity(replicatorTileEntity.getClass(), "prospect:replicatorTileEntity");
		GameRegistry.registerTileEntity(bioGenTileEntity.getClass(), "prospect:bioGenTileEntity");
		GameRegistry.registerTileEntity(solarPanelTileEntity.getClass(), "prospect:solarPanelTileEntity");
		GameRegistry.registerTileEntity(launchPadTileEntity.getClass(), "prospect:launchPadTileEntity");
		GameRegistry.registerTileEntity(cableTileEntity.getClass(), "prospect:cableTileEntity");
		GameRegistry.registerTileEntity(transformerTileEntity.getClass(), "prospect:transformerTileEntity");
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) 
	{
		event.getRegistry().registerAll(extruder);
		event.getRegistry().registerAll(press);
		event.getRegistry().registerAll(living_ore);
		event.getRegistry().registerAll(purifier);
		event.getRegistry().registerAll(quarry);
		event.getRegistry().registerAll(quarry_frame);
		event.getRegistry().registerAll(quarry_tube);
		event.getRegistry().registerAll(quarry_drill);
		event.getRegistry().registerAll(fabricator);
		event.getRegistry().registerAll(replicator);
		event.getRegistry().registerAll(launch_pad);
		event.getRegistry().registerAll(capsule);
		event.getRegistry().registerAll(copper_ore);
		event.getRegistry().registerAll(tin_ore);
		event.getRegistry().registerAll(silver_ore);
		event.getRegistry().registerAll(lead_ore);
		event.getRegistry().registerAll(aluminum_ore);
		event.getRegistry().registerAll(silicon_ore);
		event.getRegistry().registerAll(lv_cable);
		event.getRegistry().registerAll(mv_cable);
		event.getRegistry().registerAll(hv_cable);
		event.getRegistry().registerAll(ev_cable);
		event.getRegistry().registerAll(iv_cable);
		event.getRegistry().registerAll(lv_transformer);
		event.getRegistry().registerAll(mv_transformer);
		event.getRegistry().registerAll(hv_transformer);
		event.getRegistry().registerAll(ev_transformer);
		event.getRegistry().registerAll(bio_fuel_generator);
		event.getRegistry().registerAll(lv_solar_panel);
		event.getRegistry().registerAll(mv_solar_panel);
		event.getRegistry().registerAll(hv_solar_panel);
		event.getRegistry().registerAll(ev_solar_panel);
		event.getRegistry().registerAll(iv_solar_panel);
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) 
	{
		event.getRegistry().registerAll(new ItemBlock(extruder).setRegistryName(extruder.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(press).setRegistryName(press.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(living_ore).setRegistryName(living_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(purifier).setRegistryName(purifier.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(fabricator).setRegistryName(fabricator.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(replicator).setRegistryName(replicator.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(quarry).setRegistryName(quarry.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(quarry_frame).setRegistryName(quarry_frame.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(quarry_tube).setRegistryName(quarry_tube.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(quarry_drill).setRegistryName(quarry_drill.getRegistryName()));	
		event.getRegistry().registerAll(new ItemBlock(launch_pad).setRegistryName(launch_pad.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(capsule).setRegistryName(capsule.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(copper_ore).setRegistryName(copper_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(tin_ore).setRegistryName(tin_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(silver_ore).setRegistryName(silver_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(lead_ore).setRegistryName(lead_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(aluminum_ore).setRegistryName(aluminum_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(silicon_ore).setRegistryName(silicon_ore.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(lv_cable).setRegistryName(lv_cable.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(mv_cable).setRegistryName(mv_cable.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(hv_cable).setRegistryName(hv_cable.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(ev_cable).setRegistryName(ev_cable.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(iv_cable).setRegistryName(iv_cable.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(lv_transformer).setRegistryName(lv_transformer.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(mv_transformer).setRegistryName(mv_transformer.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(hv_transformer).setRegistryName(hv_transformer.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(ev_transformer).setRegistryName(ev_transformer.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(bio_fuel_generator).setRegistryName(bio_fuel_generator.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(lv_solar_panel).setRegistryName(lv_solar_panel.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(mv_solar_panel).setRegistryName(mv_solar_panel.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(hv_solar_panel).setRegistryName(hv_solar_panel.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(ev_solar_panel).setRegistryName(ev_solar_panel.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(iv_solar_panel).setRegistryName(iv_solar_panel.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) 
	{
		registerRender(Item.getItemFromBlock(extruder));
		registerRender(Item.getItemFromBlock(press));
		registerRender(Item.getItemFromBlock(living_ore));
		registerRender(Item.getItemFromBlock(purifier));
		registerRender(Item.getItemFromBlock(quarry));
		registerRender(Item.getItemFromBlock(quarry_frame));
		registerRender(Item.getItemFromBlock(quarry_tube));
		registerRender(Item.getItemFromBlock(quarry_drill));
		registerRender(Item.getItemFromBlock(fabricator));
		registerRender(Item.getItemFromBlock(replicator));	
		registerRender(Item.getItemFromBlock(launch_pad));
		registerRender(Item.getItemFromBlock(capsule));
		registerRender(Item.getItemFromBlock(copper_ore));
		registerRender(Item.getItemFromBlock(tin_ore));
		registerRender(Item.getItemFromBlock(silver_ore));
		registerRender(Item.getItemFromBlock(lead_ore));
		registerRender(Item.getItemFromBlock(aluminum_ore));
		registerRender(Item.getItemFromBlock(silicon_ore));
		registerRender(Item.getItemFromBlock(lv_cable));
		registerRender(Item.getItemFromBlock(mv_cable));
		registerRender(Item.getItemFromBlock(hv_cable));
		registerRender(Item.getItemFromBlock(ev_cable));
		registerRender(Item.getItemFromBlock(iv_cable));
		registerRender(Item.getItemFromBlock(lv_transformer));
		registerRender(Item.getItemFromBlock(mv_transformer));
		registerRender(Item.getItemFromBlock(hv_transformer));
		registerRender(Item.getItemFromBlock(ev_transformer));
		registerRender(Item.getItemFromBlock(bio_fuel_generator));
		registerRender(Item.getItemFromBlock(lv_solar_panel));
		registerRender(Item.getItemFromBlock(mv_solar_panel));
		registerRender(Item.getItemFromBlock(hv_solar_panel));
		registerRender(Item.getItemFromBlock(ev_solar_panel));
		registerRender(Item.getItemFromBlock(iv_solar_panel));
	}
	
	public static void registerRender(Item item) 
	{
		Prospect.proxy.registerRenderInformation(item);
	}
}
