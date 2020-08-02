package com.droog71.prospect.tile_entity;

import com.droog71.prospect.fe.ProspectEnergyStorage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class CableTileEntity extends TileEntity implements ITickable
{
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
	private int rating;
	private int capacity;
		
	public CableTileEntity()
	{
		
	}
	
	public CableTileEntity(int rating, int capacity)
	{			
		this.rating = rating;
		this.capacity = capacity;
	}
	
	@Override
    public void onLoad() 
	{
		energyStorage.maxReceive = rating;
		energyStorage.capacity = capacity;	
	}
	
	@Override
    public void invalidate() 
    {
		super.invalidate(); // this is important for mc!
    }
	
    @Override
    public void readFromNBT(NBTTagCompound tag) 
    {
        rating = tag.getInteger("rating");
        capacity = tag.getInteger("capacity");
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) 
    {
        tag.setInteger("rating", rating);
        tag.setInteger("capacity", capacity);
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
				for (IEnergyStorage sink : energyStorage.receivers(world, pos))
				{
					energyStorage.giveEnergy(energyStorage, sink, rating);
				}
			}			
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