package com.droog71.prospect.tile_entity;

import com.droog71.prospect.blocks.energy.ZeroPointCooler;
import com.droog71.prospect.forge_energy.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectSounds;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class CoolerTileEntity extends TileEntity implements ITickable
{
	private Object ic2EnergySink;
	private int effectsTimer;
	public boolean energized;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
		
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,5000,2);
			}
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}
		energyStorage.capacity = 20000;
		energyStorage.maxReceive = 4000;
		super.onLoad();
	}
	 
	@Override
	public void validate()
	{
		super.validate();
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,5000,2);
			}
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}
	}
	
	@Override
    public void invalidate() 
    {
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink) != null)
			{
				((BasicSink) ic2EnergySink).invalidate(); // notify the energy sink
			}			
		}
		super.invalidate(); // this is important for mc!
    }
 
    @Override
    public void onChunkUnload() 
    {
    	if (Loader.isModLoaded("ic2"))
		{
    		if (((BasicSink) ic2EnergySink) != null)
    		{
    			((BasicSink) ic2EnergySink).onChunkUnload(); // notify the energy sink
    		}  		
		}  
    	super.onChunkUnload();
    }
 
    @Override
    public void readFromNBT(NBTTagCompound tag) 
    {
        super.readFromNBT(tag);
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
				ic2EnergySink = new BasicSink(this,5000,2);
			}	
	        ((BasicSink) ic2EnergySink).readFromNBT(tag);
		}
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) 
    {
        super.writeToNBT(tag);
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
				ic2EnergySink = new BasicSink(this,5000,2);
			}	
	        ((BasicSink) ic2EnergySink).writeToNBT(tag);
		}
        return tag;
    }
 
	@Override
	public void update() 
	{
		if (!world.isRemote) //Everything is done on the server.
		{
			if (energyStorage.overloaded)
            {
				energyStorage.explode(world,pos);
            }
			else
			{
				if (useEnergy())
				{		
					energized = true;
					ZeroPointCooler.setState(true, world, pos);
					effectsTimer++;
					if (effectsTimer > 40)
					{	
						world.playSound(null, pos, ProspectSounds.zeroPointCoolerSoundEvent,  SoundCategory.BLOCKS, 0.5f, 1);
						effectsTimer = 0;
					}
				}
				else
				{
					energized = false;	
					ZeroPointCooler.setState(false, world, pos);
				}
			}
		}
	}
    
    //Remove energy from the buffer
	private boolean useEnergy()
    {
		if (energyStorage.getEnergyStored() >= 32)
		{
			if (Loader.isModLoaded("ic2"))
    		{
				((BasicSink) ic2EnergySink).setEnergyStored(0);
    			((BasicSink) ic2EnergySink).setCapacity(0);  			
    		}
			energyStorage.useEnergy(32);
			return true;
		}  
		else if (Loader.isModLoaded("ic2"))
		{
			((BasicSink) ic2EnergySink).setCapacity(5000);
        	if (((BasicSink) ic2EnergySink).useEnergy(8))
        	{
        		return true;
        	}   
		}
    	return false;
    }
	
	@SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
        	return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
    	if (capability == CapabilityEnergy.ENERGY)
    	{
    		return true;
    	}
    	return super.hasCapability(capability, facing);
    }
}