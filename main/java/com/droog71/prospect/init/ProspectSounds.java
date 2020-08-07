package com.droog71.prospect.init;

import com.droog71.prospect.Prospect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Prospect.MODID)
public class ProspectSounds
{
    static ResourceLocation fabricatorSoundLocation;
	static ResourceLocation purifierSoundLocation;    
    static ResourceLocation quarrySoundLocation;
    static ResourceLocation replicatorSoundLocation;
    static ResourceLocation transmitterSoundLocation;
    static ResourceLocation capsuleSoundLocation;
    static ResourceLocation extruderSoundLocation;
    static ResourceLocation pressSoundLocation;
    static ResourceLocation bioFuelGeneratorSoundLocation;
	public static SoundEvent fabricatorSoundEvent;
    public static SoundEvent purifierSoundEvent;
    public static SoundEvent quarrySoundEvent;
    public static SoundEvent replicatorSoundEvent;
    public static SoundEvent transmitterSoundEvent;
    public static SoundEvent capsuleSoundEvent;
    public static SoundEvent extruderSoundEvent;
    public static SoundEvent pressSoundEvent;
    public static SoundEvent bioFuelGeneratorSoundEvent;
	
	public static void init() 
	{
        fabricatorSoundLocation = new ResourceLocation("prospect", "fabricator");
		purifierSoundLocation = new ResourceLocation("prospect", "purifier");
        quarrySoundLocation = new ResourceLocation("prospect", "quarry");
        replicatorSoundLocation = new ResourceLocation("prospect", "replicator");
        transmitterSoundLocation = new ResourceLocation("prospect", "transmitter");
        capsuleSoundLocation = new ResourceLocation("prospect", "capsule");
        extruderSoundLocation = new ResourceLocation("prospect", "extruder");
        pressSoundLocation = new ResourceLocation("prospect", "press");
        bioFuelGeneratorSoundLocation = new ResourceLocation("prospect", "bio_fuel_generator");
		fabricatorSoundEvent = new SoundEvent(fabricatorSoundLocation);
        purifierSoundEvent = new SoundEvent(purifierSoundLocation);
        quarrySoundEvent = new SoundEvent(quarrySoundLocation);
        replicatorSoundEvent = new SoundEvent(replicatorSoundLocation);
        transmitterSoundEvent = new SoundEvent(transmitterSoundLocation);
        capsuleSoundEvent = new SoundEvent(capsuleSoundLocation);
        extruderSoundEvent = new SoundEvent(extruderSoundLocation);
        pressSoundEvent = new SoundEvent(pressSoundLocation);
        bioFuelGeneratorSoundEvent = new SoundEvent(bioFuelGeneratorSoundLocation);
        
	}
	
	public static void registerSoundEvent(String name, SoundEvent event)
	{
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
	}
}
