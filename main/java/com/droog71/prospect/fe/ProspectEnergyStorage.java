package com.droog71.prospect.fe;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class ProspectEnergyStorage implements IEnergyStorage
{
	protected int energy;    
    public int capacity;
    public int maxReceive;
    public boolean overloaded;

    public void readFromNBT(NBTTagCompound compound)
    {
    	energy = compound.getInteger("energy");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	compound.setInteger("energy",energy);
    	return compound;
    }

    public int generateEnergy(int amount)
    {
    	int energyAdded = Math.min(capacity - energy, amount);
        energy += energyAdded;
        return energyAdded;     
    }
    
    public int useEnergy(int energyToUse)
    {
        int energyUsed = Math.min(energy, energyToUse);
        energy -= energyUsed;
        return energyUsed;
    }
    
    @Override
    public int receiveEnergy(int amount, boolean simulate)
    {
		if (amount > maxReceive)
    	{
    		overloaded = true;
    		return 0;
    	}
		else
		{
			if (!canReceive())
                return 0;

            int energyReceived = Math.min(capacity - energy, Math.min(maxReceive, amount));
            if (!simulate)
                energy += energyReceived;
            return energyReceived;    			
		}  	
    }
 
    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return 0;
    }

    @Override
    public int getEnergyStored()
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return capacity;
    }

    @Override
    public boolean canExtract()
    {
        return false;
    }

    @Override
    public boolean canReceive()
    {
        return maxReceive > 0;
    }
}