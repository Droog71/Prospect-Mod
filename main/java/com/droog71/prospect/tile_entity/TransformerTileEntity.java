package com.droog71.prospect.tile_entity;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import com.droog71.prospect.forge_energy.ProspectEnergyStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TransformerTileEntity extends TileEntity implements ITickable
{
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
	private int maxReceive;
	private int capacity;
	private int rating;
	private int stepDownRating;
	private int stepDownMaxReceive;
		
	public TransformerTileEntity()
	{
		
	}
	
	public TransformerTileEntity(int maxReceive, int capacity, int rating)
	{		
		this.maxReceive = maxReceive;
		this.capacity = capacity;
		this.rating = rating;
	}
	
	@Override
    public void onLoad() 
	{		
		energyStorage.maxReceive = maxReceive;	
		energyStorage.capacity = capacity;
		stepDownMaxReceive = maxReceive;
		stepDownRating = rating;
	}
	
	@Override
    public void invalidate() 
    {
		super.invalidate(); // this is important for mc!
    }
	
    @Override
    public void readFromNBT(NBTTagCompound tag) 
    {       
        maxReceive = tag.getInteger("maxReceive");
        capacity = tag.getInteger("capacity");
        rating = tag.getInteger("rating");
        stepDownMaxReceive = tag.getInteger("stepDownMaxReceive");
        stepDownRating = tag.getInteger("stepDownRating");       
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) 
    {
        
        tag.setInteger("maxReceive", maxReceive);
        tag.setInteger("capacity", capacity);
        tag.setInteger("rating", rating);
        tag.setInteger("stepDownMaxReceive", stepDownMaxReceive);
        tag.setInteger("stepDownRating", stepDownRating); 
        return tag;
    }
	
	@Override
	public void update() 
	{
		if (!world.isRemote)
		{
			if (energyStorage.overloaded)
            {
				energyStorage.explode(world,pos);
            }
			else
			{
				if (world.isBlockPowered(pos))
				{
					energyStorage.maxReceive = stepDownRating;
					rating = stepDownMaxReceive;
				}
				else
				{
					energyStorage.maxReceive = stepDownMaxReceive;
					rating = stepDownRating;
				}
				
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