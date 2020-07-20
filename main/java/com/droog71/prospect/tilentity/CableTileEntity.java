package com.droog71.prospect.tilentity;

import com.droog71.prospect.fe.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class CableTileEntity extends TileEntity implements ITickable
{
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
	private int rating;
		
	public CableTileEntity()
	{
		
	}
	
	public CableTileEntity(int rating, int capacity)
	{		
		energyStorage.maxReceive = rating;
		energyStorage.capacity = capacity;	
		this.rating = rating;
	}
	
	@Override
	public void update() 
	{
		if (!world.isRemote) //Everything is done on the server.
		{
			if (energyStorage.overloaded)
            {
            	explode();
            }
			else
			{
				giveEnergy();
			}			
		}
	}

	private void giveEnergy()
	{		
		BlockPos[] sides = {pos.add(0,1,0),pos.add(1,0,0),pos.add(0,0,1),pos.add(0,-1,0),pos.add(-1,0,0),pos.add(0,0,-1)};
		for (BlockPos p : sides) //Iterate over all adjacent blocks.
		{
			TileEntity otherTile = world.getTileEntity(p);
			if (otherTile != null) //TileEntity exists at adjacent block position.
			{				
				EnumFacing direction = null;
				for (EnumFacing facing : EnumFacing.VALUES) 
				{
					IEnergyStorage otherStorage = otherTile.getCapability(CapabilityEnergy.ENERGY,facing);
					if (otherStorage != null) //The TileEntity has the required forge energy capability.
					{
						if (direction == null)
						{
							direction = facing; //The cable will transfer energy in this direction if possible.
						}						
					}
				}
				IEnergyStorage otherStorage = otherTile.getCapability(CapabilityEnergy.ENERGY,direction);
				if (otherStorage != null)
				{								
					if (energyStorage.getEnergyStored() > 0) //The cable has the potential to transfer energy to the adjacent block.
					{
						if (otherStorage.canReceive()) //The adjacent block can receive the energy.
						{							
							if (otherStorage.getEnergyStored() < energyStorage.getEnergyStored()) //Potential exists between the two energy storage instances.
							{
								int potential = (energyStorage.getEnergyStored() - otherStorage.getEnergyStored())/2; //Calculate the potential.
								int output = Math.min(potential, rating); //If potential is greater than this cable's maximum transfer rate, use the lower value.							
								energyStorage.useEnergy(output); //Remove energy from this cable.
								otherStorage.receiveEnergy(rating, true); //Simulate the transfer. If this transaction overloads the receiver, it will explode.
								if (otherStorage != null) //Recipient did not explode.
								{
									otherStorage.receiveEnergy(output,false); //Add the energy to the adjacent block.
								}
							}	
						}
					}					
				}
			}						
		}
	}
	
    private void explode()
    {
    	world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE,  SoundCategory.BLOCKS, 0.5f, 1);
    	WorldServer w = (WorldServer) world;
    	w.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 1, null);
    	w.spawnParticle(EnumParticleTypes.LAVA, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 1, null);
    	w.spawnParticle(EnumParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 1, null);   	
    	world.getBlockState(pos).getBlock().breakBlock(world, pos, ProspectBlocks.extruder.getDefaultState());
    	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ProspectBlocks.extruder));
    	w.spawnEntity(item);
    	world.setBlockToAir(pos);    	
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