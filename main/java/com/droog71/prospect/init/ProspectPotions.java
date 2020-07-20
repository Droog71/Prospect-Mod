package com.droog71.prospect.init;

import com.droog71.prospect.Prospect;
import com.droog71.prospect.potion.PotionSpore;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Prospect.MODID)
public class ProspectPotions {

	public static final Potion spore = new PotionSpore();

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> evt)
	{
		evt.getRegistry().register(spore);
	}
}