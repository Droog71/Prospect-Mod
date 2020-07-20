package com.droog71.prospect.tilentity;

import com.droog71.prospect.fe.ProspectEnergyStorage;
import ic2.api.energy.prefab.BasicSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
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
	private int rating;
	private int tier;
	
	public SolarPanelTileEntity()
	{
		
	}
	
	public SolarPanelTileEntity(int capacity, int rating, int tier)
	{		
		energyStorage.capacity = capacity;		
		this.rating = rating;
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
			ic2EnergySource = new BasicSource(this,10,1);
		}	
        ((BasicSource) ic2EnergySource).readFromNBT(tag);
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) 
    {
        super.writeToNBT(tag);	
        if ((BasicSource) ic2EnergySource == null)
		{
			ic2EnergySource = new BasicSource(this,10,1);
		}	
        ((BasicSource) ic2EnergySource).writeToNBT(tag);
        return tag;
    }

	@Override
	public void update() 
	{
		if (!world.isRemote) //Everything is done on the server.
		{
			addEnergy();
			giveEnergy();
		}
	}
	
	private void addEnergy() 
	{
		if (world.canBlockSeeSky(pos.offset(EnumFacing.UP))) 
		{			
			if (Loader.isModLoaded("ic2"))
			{
				if (((BasicSource) ic2EnergySource).getCapacity() > 0)
				{
					((BasicSource) ic2EnergySource).addEnergy(world.getSunBrightnessFactor(1F) * 5);
				}				
			}
			energyStorage.generateEnergy((int)world.getSunBrightnessFactor(1F) * rating);
		}
	}
	
	private void giveEnergy()
	{		
		boolean connectedFE = false;
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
					if (Loader.isModLoaded("ic2"))
					{
						((BasicSource) ic2EnergySource).setEnergyStored(0);
						((BasicSource) ic2EnergySource).setCapacity(0);
						connectedFE = true;
					}
					if (energyStorage.getEnergyStored() >= rating)
					{
						if (otherStorage.canReceive())
						{
							if (otherStorage.getEnergyStored() <= otherStorage.getMaxEnergyStored() - rating)
							{
								otherStorage.receiveEnergy(rating,false);
								energyStorage.useEnergy(rating);
							}	
							else
							{
								otherStorage.receiveEnergy(otherStorage.getMaxEnergyStored() - otherStorage.getEnergyStored(),false);
								energyStorage.useEnergy(otherStorage.getMaxEnergyStored() - otherStorage.getEnergyStored());								
							}
						}					
					}
					else if (energyStorage.getEnergyStored() > 0)
					{
						if (otherStorage.canReceive())
						{
							if (otherStorage.getEnergyStored() <= otherStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())
							{
								otherStorage.receiveEnergy(energyStorage.getEnergyStored(),false);
								energyStorage.useEnergy(energyStorage.getEnergyStored());
							}	
							else
							{
								otherStorage.receiveEnergy(otherStorage.getMaxEnergyStored() - otherStorage.getEnergyStored(),false);
								energyStorage.useEnergy(otherStorage.getMaxEnergyStored() - otherStorage.getEnergyStored());
							}
						}
					}					
				}
			}						
		}
		if (connectedFE == false)
		{
			((BasicSource) ic2EnergySource).setCapacity(10);
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