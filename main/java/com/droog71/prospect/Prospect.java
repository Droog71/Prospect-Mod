package com.droog71.prospect;

import com.droog71.prospect.common.CommonProxy;
import com.droog71.prospect.config.ConfigHandler;
import com.droog71.prospect.event.FMLEventHandler;
import com.droog71.prospect.gui.ProspectGuiHandler;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.worldgen.OreGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = Prospect.MODID, name = Prospect.NAME, version = Prospect.VERSION)
public class Prospect
{
    public static final String MODID = "prospect";
    public static final String NAME = "Prospect";
    public static final String VERSION = "1.0";

    @Instance
	public static Prospect instance;
    
    @SidedProxy(clientSide = "com.droog71.prospect.common.ClientProxy", serverSide = "com.droog71.prospect.common.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	ProspectBlocks.init();
    	ProspectItems.init();
    	ProspectSounds.init();
    	ConfigHandler.createConfigFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	GameRegistry.registerWorldGenerator(new OreGen(), 0);
    	GameRegistry.addSmelting(ProspectBlocks.copper_ore, new ItemStack(ProspectItems.copper_ingot), 0.7f);
    	GameRegistry.addSmelting(ProspectBlocks.tin_ore, new ItemStack(ProspectItems.tin_ingot), 0.7f);
    	GameRegistry.addSmelting(ProspectBlocks.aluminum_ore, new ItemStack(ProspectItems.aluminum_ingot), 0.7f);
    	GameRegistry.addSmelting(ProspectBlocks.silver_ore, new ItemStack(ProspectItems.silver_ingot), 0.7f);
    	GameRegistry.addSmelting(ProspectBlocks.lead_ore, new ItemStack(ProspectItems.lead_ingot), 0.7f);
    	GameRegistry.addSmelting(ProspectBlocks.silicon_ore, new ItemStack(ProspectItems.silicon), 0.7f);
    	GameRegistry.addSmelting(ProspectItems.prepared_circuit, new ItemStack(ProspectItems.quantum_circuit), 0.7f);
    	OreDictionary.registerOre("ingotCopper", ProspectItems.copper_ingot);
    	OreDictionary.registerOre("ingotTin", ProspectItems.tin_ingot);
    	OreDictionary.registerOre("ingotAluminum", ProspectItems.aluminum_ingot);
    	OreDictionary.registerOre("ingotSilver", ProspectItems.silver_ingot);
    	OreDictionary.registerOre("ingotLead", ProspectItems.lead_ingot);
    	OreDictionary.registerOre("silicon", ProspectItems.silicon);
    	MinecraftForge.EVENT_BUS.register(FMLEventHandler.INSTANCE);
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ProspectGuiHandler());
    }

    public static final CreativeTabs tabProspect = new CreativeTabs("Prospect") 
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ProspectBlocks.purifier);
		}
	};
}