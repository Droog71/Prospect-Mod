package com.droog71.prospect.tilentity;

import java.util.Iterator;
import java.util.NoSuchElementException;
import com.droog71.prospect.blocks.energy.Purifier;
import com.droog71.prospect.fe.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectSounds;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PurifierTileEntity extends TileEntity implements ITickable
{
	private Object ic2EnergySink;
	private int effectsTimer;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
		
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,10000,2);
			}
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}
		energyStorage.capacity = 10000;
		energyStorage.maxReceive = 20000;
		super.onLoad();
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
				ic2EnergySink = new BasicSink(this,10000,2);
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
				ic2EnergySink = new BasicSink(this,10000,2);
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
            	explode();
            }
			else
			{
				Purifier purifier = (Purifier) world.getBlockState(pos).getBlock();
				if (useEnergy())
				{		
					purifier.powered = true;
					doWork();
				}
				else
				{
					purifier.powered = false;				
				}
			}
		}
	}
    
    private void doWork()
    {   	
		effectsTimer++;
		if (effectsTimer > 40)
		{	
			WorldServer w = (WorldServer) world;
			BlockPos corner_1 = pos.add(-20, -20, -20);
			BlockPos corner_2 = pos.add(20, 20, 20);
			Iterable<BlockPos> allBlocks = BlockPos.getAllInBox(corner_1, corner_2);
			Iterator<BlockPos> iter = allBlocks.iterator();		
			while(iter.hasNext())
			{
				try
				{
					w.spawnParticle(EnumParticleTypes.TOWN_AURA, iter.next().getX(), iter.next().getY(), iter.next().getZ(), 1, 0, 0, 0, 1, null);
				}
				catch(NoSuchElementException e)
				{
					//NOOP
				}
			}
			world.playSound(null, pos, ProspectSounds.purifierSoundEvent,  SoundCategory.BLOCKS, 0.25f, 1);
			effectsTimer = 0;
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
	
	private boolean useEnergy()
    {
		if (energyStorage.getEnergyStored() >= 40)
		{
			if (Loader.isModLoaded("ic2"))
    		{
				((BasicSink) ic2EnergySink).setEnergyStored(0);
    			((BasicSink) ic2EnergySink).setCapacity(0);  			
    		}
			energyStorage.useEnergy(40);
			return true;
		}  
		else if (Loader.isModLoaded("ic2"))
		{
			((BasicSink) ic2EnergySink).setCapacity(10000);
        	if (((BasicSink) ic2EnergySink).useEnergy(10))
        	{
        		return true;
        	}   
		}
    	return false;
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