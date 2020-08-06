package com.droog71.prospect.forge_energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ProspectEnergyStorage implements IEnergyStorage
{
	protected int energy;    
    public int capacity;
    public int maxReceive;
    public boolean overloaded;

    // Load the amount of energy stored.
    public void readFromNBT(NBTTagCompound compound)
    {
    	energy = compound.getInteger("energy");
    }

    // Save the amount of energy stored.
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	compound.setInteger("energy",energy);
    	return compound;
    }

    // Add energy to the buffer.
    public int generateEnergy(int amount)
    {
    	int energyAdded = Math.min(capacity - energy, amount);
        energy += energyAdded;
        return energyAdded;     
    }
    
    // Remove energy from the buffer.
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
 
    // A list of all adjacent blocks capable of receiving energy.
    public List<IEnergyStorage> receivers(World world, BlockPos pos)
	{		
    	List<IEnergyStorage> receiversFound = new ArrayList<IEnergyStorage>();
		BlockPos[] sides = {pos.add(0,1,0),pos.add(1,0,0),pos.add(0,0,1),pos.add(0,-1,0),pos.add(-1,0,0),pos.add(0,0,-1)};
		for (BlockPos p : sides)
		{
			TileEntity otherTile = world.getTileEntity(p);
			if (otherTile != null)
			{				
				EnumFacing direction = null;
				for (EnumFacing facing : EnumFacing.VALUES)
				{
					IEnergyStorage otherStorage = otherTile.getCapability(CapabilityEnergy.ENERGY,facing);
					if (otherStorage != null)
					{
						if (direction == null)
						{
							direction = facing;
						}						
					}
				}
				IEnergyStorage otherStorage = otherTile.getCapability(CapabilityEnergy.ENERGY,direction);
				if (otherStorage != null)
				{										
					receiversFound.add(otherStorage);				
				}
			}						
		}
		return receiversFound;
	}
    
    // Give energy to receivers
    public void giveEnergy(ProspectEnergyStorage source, IEnergyStorage sink, int rating)
    {
    	if (sink != null)
		{								
			if (sink.canReceive()) //The adjacent block can receive the energy.
			{	
				int demand = sink.getMaxEnergyStored() - sink.getEnergyStored(); //The energy required by the receiver.
				int potential = Math.min(demand, rating); //Use the lower value.								
				int available = Math.min(potential, source.getEnergyStored()); //Use the lower value.
				int possible = Math.max(0, ((source.getEnergyStored() - sink.getEnergyStored())/2)-1); //For conductors.
				int balanced = Math.min(possible, available); //For conductors.
				int output = source.canReceive() ? balanced : available; //Power source or conductor?
				source.useEnergy(output); //Remove energy from the source.
				sink.receiveEnergy(rating, true); //If this transaction overloads the receiver, it will explode.
				if (sink != null) //Recipient did not explode.
				{
					sink.receiveEnergy(output,false); //Add the energy to the adjacent block.
				}
			}
		}
    }
    
    //If an energy storage block's FE per tick rating is exceeded, it will explode.
    public void explode(World world, BlockPos pos)
    {   	
    	world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE,  SoundCategory.BLOCKS, 0.5f, 1);
    	WorldServer w = (WorldServer) world;
    	w.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 1, null);
    	w.spawnParticle(EnumParticleTypes.LAVA, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 1, null);
    	w.spawnParticle(EnumParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 1, null);
    	
    	Block block = world.getBlockState(pos).getBlock();
    	block.breakBlock(world, pos, block.getDefaultState());
    	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Item.getItemFromBlock(block)));
    	w.spawnEntity(item);
    	world.setBlockToAir(pos);    	
    }
    
    // Get the amount of energy in the buffer.
    @Override
    public int getEnergyStored()
    {
        return energy;
    }

    // Get the maximum capacity of the buffer.
    @Override
    public int getMaxEnergyStored()
    {
        return capacity;
    }

    @Override
    public boolean canReceive()
    {
        return maxReceive > 0;
    }
    
    // Not used.
    @Override
    public boolean canExtract()
    {
        return false;
    }

    // Not used.
    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return 0;
    }
}