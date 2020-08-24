package com.droog71.prospect.tile_entity;

import com.droog71.prospect.blocks.energy.ZeroPointReactor;
import com.droog71.prospect.forge_energy.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectItems;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.inventory.ZeroPointContainer;
import ic2.api.energy.prefab.BasicSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ZeroPointTileEntity extends TileEntity implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {0};
    private static final int[] SLOTS_SIDES = new int[] {0};
    private NonNullList<ItemStack> zeroPointItemStacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private int energyStored;
    private int energyCapacity;
    private int overHeatTime;
    private int burnTime;
    private int totalburnTime;
    private int effectsTimer = 250;
    private int alarmSoundLoopTimer;
    private Object ic2EnergySource;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
    
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource == null))
			{
				ic2EnergySource = new BasicSource(this,4096,5);
			}
			((BasicSource) ic2EnergySource).onLoad(); // notify the energy sink
		}	
		energyStorage.capacity = 16384;
		energyStorage.maxReceive = 0;
	}
	 
	@Override
	public void validate()
	{
		super.validate();
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource == null))
			{
				ic2EnergySource = new BasicSource(this,4096,5);
			}
			((BasicSource) ic2EnergySource).onLoad(); // notify the energy sink
		}
	}
	
	@Override
    public void invalidate() 
    {
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource != null))
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
    		if (((BasicSource) ic2EnergySource != null))
			{
    			((BasicSource) ic2EnergySource).onChunkUnload(); // notify the energy sink
			}
		}
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return zeroPointItemStacks.size();
    }

    @Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : zeroPointItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return zeroPointItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(zeroPointItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(zeroPointItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = zeroPointItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        zeroPointItemStacks.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            totalburnTime = getburnTime(stack);
            markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
	public String getName()
    {
        return "Zero Point Reactor";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return false;
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        //NOOP
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);       
        zeroPointItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, zeroPointItemStacks);
        energyStored = compound.getInteger("EnergyStored");
        burnTime = compound.getInteger("burnTime");
        totalburnTime = compound.getInteger("TotalburnTime");        
        energyCapacity = compound.getInteger("EnergyCapacity");  
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSource) ic2EnergySource == null)
			{
				ic2EnergySource = new BasicSource(this,4096,5);
			}	
	        ((BasicSource) ic2EnergySource).readFromNBT(compound);
		}
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);       
        compound.setInteger("EnergyStored", (short)energyStored);
        compound.setInteger("EnergyCapacity", (short)energyCapacity);
        compound.setInteger("burnTime", (short)burnTime);
        compound.setInteger("TotalburnTime", (short)totalburnTime);
        ItemStackHelper.saveAllItems(compound, zeroPointItemStacks);
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSource) ic2EnergySource == null)
			{
				ic2EnergySource = new BasicSource(this,4096,5);
			}	
	        ((BasicSource) ic2EnergySource).writeToNBT(compound);
		}
        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
	public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Machine isEnergized
     */
    public boolean isEnergized()
    {
    	if (Loader.isModLoaded("ic2"))
		{
    		if (((BasicSource) ic2EnergySource).getEnergyStored() > 0)
			{
				return ((BasicSource) ic2EnergySource).getEnergyStored() > 0;
			}
			else
	    	{
	    		return energyStorage.getEnergyStored() > 0;
	    	}
		}
    	else
    	{
    		return energyStorage.getEnergyStored() > 0;
    	}
    }

    @SideOnly(Side.CLIENT)
    public static boolean isEnergized(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
	public void update()
    {      
    	boolean needsNetworkUpdate = false;
    	boolean clientIsGenerating = burnTime > 0 && burnTime < totalburnTime;
        if (!world.isRemote)
        {          	
        	if (burnTime == 0)
    		{
        		if (canConsumeFuel())
        		{
        			burnTime = 1;
        			consumeFuel();
        	        needsNetworkUpdate = true;
        		}     		
    		}	
        	
        	if (burnTime > 0 && burnTime < totalburnTime)
            {   
        		handleEnergy();
    			burnTime++;
        		effectsTimer++;
        		if (effectsTimer > 200)
        		{	
        			world.playSound(null, pos, ProspectSounds.zeroPointReactorSoundEvent,  SoundCategory.BLOCKS, 0.5f, 1);
        			effectsTimer = 0;
        		}         		            		
        		needsNetworkUpdate = true;
            }	
        	
        	if (burnTime == totalburnTime)
    	    {
    	    	burnTime = 0;
    	    	totalburnTime = getburnTime(zeroPointItemStacks.get(0));
    	    	needsNetworkUpdate = true;
    	    }
    	    
        	if (clientIsGenerating != burnTime > 0 && burnTime < totalburnTime)
        	{
        		needsNetworkUpdate = true;
        	}
        	
        	ZeroPointReactor.setState(burnTime > 0 && burnTime < totalburnTime, world, pos);	
        } 
        
        if (needsNetworkUpdate)
        {
            markDirty();
        }
    }
    
    private void soundAlarm()
	{
		alarmSoundLoopTimer++;
		if (alarmSoundLoopTimer >= 200)
		{
			world.playSound(null, pos, ProspectSounds.alarmSoundEvent,  SoundCategory.BLOCKS, 1, 1);
			alarmSoundLoopTimer = 0;
		}
	}
    
    // Returns true if a pressurized refrigerant pipe is adjacent to the reactor
    private boolean hasCooler()
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};	    	
    	for (BlockPos p : positions)
    	{
    		TileEntity t = world.getTileEntity(p);
    		if (t != null)
    		{
    			if (t instanceof CopperPipeTileEntity)
    			{
    				if (((CopperPipeTileEntity)t).pressurized)
					{
						return true;
					}
    			}
    		}		
    	}   	
    	return false;
    }
    
    // Get values from the energy storage or ic2 energy sink
    private void handleEnergy()
    {
    	if (energyStorage.receivers(world, pos).size() > 0)
    	{
    		energyStored = energyStorage.getEnergyStored();
    		energyCapacity = energyStorage.getMaxEnergyStored();
    		if (Loader.isModLoaded("ic2"))
    		{
				((BasicSource) ic2EnergySource).setEnergyStored(0);
    			((BasicSource) ic2EnergySource).setCapacity(0);  			
    		}
    	}
    	else
    	{
    		if (Loader.isModLoaded("ic2"))
    		{
    			((BasicSource) ic2EnergySource).setCapacity(4096);
        		if (((BasicSource) ic2EnergySource).getEnergyStored() > 0)
        		{
        			energyStored = (int) ((BasicSource) ic2EnergySource).getEnergyStored();
        			energyCapacity = (int) ((BasicSource) ic2EnergySource).getCapacity();
        		}
    		}  
    	}   
    	if (energyCapacity == 0)
    	{
    		energyCapacity = energyStorage.getMaxEnergyStored();
    	}
    	generateEnergy();
    }
    
    // Add energy to the buffer
 	private void generateEnergy()
 	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource).getCapacity() > 0)
			{
				((BasicSource) ic2EnergySource).addEnergy(2048);
			}
		}
		energyStorage.generateEnergy(8192);		
		distributeEnergy();
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
				energyStorage.giveEnergy(energyStorage, sink, 8192);
			}
		}
		if (connectedFE == false)
		{
			((BasicSource) ic2EnergySource).setCapacity(4096);
		}
		checkForCooler();
	}
    
	private void checkForCooler()
	{
		if (hasCooler() == false)
		{
    		soundAlarm();
    		overHeatTime++;
    		if (overHeatTime >= 2000)
    		{
    			energyStorage.explode(world, pos);
    		}			
		}
		else
		{
			overHeatTime = 0;
		}
	}
	
    // How long it takes to burn fuel
    public int getburnTime(ItemStack stack)
    {
        return 24000;
    }

    // Checks if the item in question is registered as a copper ingot
    private boolean isFuel(ItemStack stack)
    {
    	return stack.getItem() == ProspectItems.gem;
    }
    
    /**
     * Returns true if the generator can consume fuel, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canConsumeFuel()
    {
        return !zeroPointItemStacks.get(0).isEmpty() && isFuel(zeroPointItemStacks.get(0));
    }

    /**
     * Turn one item from the generator source stack into the appropriate resulting item in the generator result stack
     */
    public void consumeFuel()
    {
        zeroPointItemStacks.get(0).shrink(1);
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
	public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (world.getTileEntity(pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
	public void openInventory(EntityPlayer player)
    {
    	
    }

    @Override
	public void closeInventory(EntityPlayer player)
    {
    	
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    @Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == 0)
        {
            return true;
        }
        else
        {
        	return false;
        }
    }

    @Override
	public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    @Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }

    // Create the container
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ZeroPointContainer(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return energyStored;
            case 1:
                return energyCapacity;
            case 2:
                return burnTime;
            case 3:
                return totalburnTime;
            default:
                return 0;
        }
    }

    // Not used
    public String getGuiID()
    {
        return null;
    }
    
    @Override
	public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                energyStored = value;
                break;
            case 1:
                energyCapacity = value;
                break;
            case 2:
                burnTime = value;
                break;
            case 3:
                totalburnTime = value;
        }
    }

    @Override
	public int getFieldCount()
    {
        return 4;
    }

    @Override
	public void clear()
    {
        zeroPointItemStacks.clear();
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
        	if (facing == EnumFacing.DOWN)
        	{
        		return (T) handlerBottom;
        	}
                
            else if (facing == EnumFacing.UP)
            {
            	return (T) handlerTop;
            }
                
            else
            {
            	return (T) handlerSide;
            }               
        } 
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
