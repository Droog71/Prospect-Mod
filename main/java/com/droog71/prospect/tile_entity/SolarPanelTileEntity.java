package com.droog71.prospect.tile_entity;

import com.droog71.prospect.forge_energy.ProspectEnergyStorage;

import ic2.api.energy.prefab.BasicSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SolarPanelTileEntity extends TileEntity implements ITickable
{
	private Object ic2EnergySource;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
	private int capacity;
	private int rating;
	private int tier;
	
	public SolarPanelTileEntity()
	{
		
	}
	
	public SolarPanelTileEntity(int capacity, int rating, int tier)
	{		
		this.capacity = capacity;
		this.rating = rating;
		this.tier = tier;
	}
	
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource == null))
			{
				ic2EnergySource = new BasicSource(this,energyStorage.capacity/4,tier);
			}
			((BasicSource) ic2EnergySource).onLoad(); // notify the energy sink
		}
		energyStorage.maxReceive = 0;
		energyStorage.capacity = capacity;
	}
	 
	@Override
    public void invalidate() 
    {
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource) != null)
			{
				((BasicSource) ic2EnergySource).invalidate(); // notify the energy sink
			}		
		}
		super.invalidate(); // this is important for mc!
    }
 
    @Override
    public void onChunkUnload() 
    {
    	if (Loader.isModLoaded("ic2"))
		{
    		if (((BasicSource) ic2EnergySource) != null)
			{
    			((BasicSource) ic2EnergySource).onChunkUnload(); // notify the energy sink
			}
		}
    }
 
    @Override
    public void readFromNBT(NBTTagCompound tag) 
    {
        super.readFromNBT(tag);	 
        if ((BasicSource) ic2EnergySource == null)
		{
        	ic2EnergySource = new BasicSource(this,capacity/4,tier);
		}	
        ((BasicSource) ic2EnergySource).readFromNBT(tag);
        capacity = tag.getInteger("capacity");
        rating = tag.getInteger("rating");
        tier = tag.getInteger("tier");
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) 
    {
        super.writeToNBT(tag);	
        if ((BasicSource) ic2EnergySource == null)
		{
			ic2EnergySource = new BasicSource(this,capacity/4,tier);
		}	
        ((BasicSource) ic2EnergySource).writeToNBT(tag);
        tag.setInteger("capacity", capacity);
        tag.setInteger("rating", rating);
        tag.setInteger("tier", tier);
        return tag;
    }

	@Override
	public void update() 
	{
		if (!world.isRemote) //Everything is done on the server.
		{
			addEnergy();
			distributeEnergy();			
		}
	}
	
	// Add energy to the buffer
	private void addEnergy() 
	{
		if (world.canBlockSeeSky(pos.offset(EnumFacing.UP))) 
		{			
			if (Loader.isModLoaded("ic2"))
			{
				if (((BasicSource) ic2EnergySource).getCapacity() > 0)
				{
					((BasicSource) ic2EnergySource).addEnergy(world.getSunBrightnessFactor(1F) * rating/4);
				}				
			}
			energyStorage.generateEnergy((int)world.getSunBrightnessFactor(1F) * rating);
		}
	}
	
	// Distributes energy
	private void distributeEnergy()
	{		
		boolean connectedFE = false;
		if (energyStorage.receivers(world, pos).size() > 0)
		{
			if (Loader.isModLoaded("ic2"))
			{
				((BasicSource) ic2EnergySource).setEnergyStored(0);
				((BasicSource) ic2EnergySource).setCapacity(0);
				connectedFE = true;
			}
			for (IEnergyStorage sink : energyStorage.receivers(world, pos))
			{
				energyStorage.giveEnergy(energyStorage, sink, rating);
			}
		}
		if (connectedFE == false)
		{
			((BasicSource) ic2EnergySource).setCapacity(energyStorage.capacity/4);
		}
	}
	
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