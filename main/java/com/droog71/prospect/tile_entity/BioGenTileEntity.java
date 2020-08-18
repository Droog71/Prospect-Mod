package com.droog71.prospect.tile_entity;

import com.droog71.prospect.blocks.energy.BioFuelGenerator;
import com.droog71.prospect.forge_energy.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.inventory.BioGenContainer;
import ic2.api.energy.prefab.BasicSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BioGenTileEntity extends TileEntity implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> bioGenItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int energyStored;
    private int energyCapacity;
    private int burnTime;
    private int totalburnTime;
    private int effectsTimer = 250;
    private Object ic2EnergySource;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
    
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource == null))
			{
				ic2EnergySource = new BasicSource(this,8,1);
			}
			((BasicSource) ic2EnergySource).onLoad(); // notify the energy sink
		}	
		energyStorage.capacity = 32;
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
				ic2EnergySource = new BasicSource(this,8,1);
			}
			((BasicSource) ic2EnergySource).onLoad(); // notify the energy sink
		}
	}
	
	@Override
    public void invalidate() 
    {
		super.invalidate(); // this is important for mc!
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSource) ic2EnergySource != null))
			{
				((BasicSource) ic2EnergySource).invalidate(); // notify the energy sink
			}
		}
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
        return bioGenItemStacks.size();
    }

    @Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : bioGenItemStacks)
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
        return bioGenItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(bioGenItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(bioGenItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = bioGenItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        bioGenItemStacks.set(index, stack);

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
        return "Bio Fuel Generator";
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
        bioGenItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, bioGenItemStacks);
        energyStored = compound.getInteger("EnergyStored");
        burnTime = compound.getInteger("burnTime");
        totalburnTime = compound.getInteger("TotalburnTime");        
        energyCapacity = compound.getInteger("EnergyCapacity");  
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSource) ic2EnergySource == null)
			{
				ic2EnergySource = new BasicSource(this,8,1);
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
        ItemStackHelper.saveAllItems(compound, bioGenItemStacks);
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSource) ic2EnergySource == null)
			{
				ic2EnergySource = new BasicSource(this,8,1);
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
        			world.playSound(null, pos, ProspectSounds.bioFuelGeneratorSoundEvent,  SoundCategory.BLOCKS, 0.5f, 1);
        			effectsTimer = 0;
        		}
        		needsNetworkUpdate = true;
            }	
        	
        	if (burnTime == totalburnTime)
    	    {
    	    	burnTime = 0;
    	    	totalburnTime = getburnTime(bioGenItemStacks.get(0));
    	    	needsNetworkUpdate = true;
    	    }
    	    
        	if (clientIsGenerating != burnTime > 0 && burnTime < totalburnTime)
        	{
        		needsNetworkUpdate = true;
        	}
        	
        	boolean active = burnTime > 0 && burnTime < totalburnTime;
        	if (active)
        	{
        		if (world.getBlockState(pos) != ProspectBlocks.bio_fuel_generator_running.getDefaultState())
				{
        			BioFuelGenerator.setState(true, world, pos);
				}
        	}
        	else
        	{
        		if (world.getBlockState(pos) != ProspectBlocks.bio_fuel_generator.getDefaultState())
				{
        			BioFuelGenerator.setState(false, world, pos);
				}
        	}
        } 
        
        if (needsNetworkUpdate)
        {
            markDirty();
        }
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
    			((BasicSource) ic2EnergySource).setCapacity(8);
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
				((BasicSource) ic2EnergySource).addEnergy(4);
			}
		}
		energyStorage.generateEnergy(16);
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
				energyStorage.giveEnergy(energyStorage, sink, 16);
			}
		}
		if (connectedFE == false)
		{
			((BasicSource) ic2EnergySource).setCapacity(8);
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
    	return stack.getItem() == ProspectItems.bio_fuel;
    }
    
    /**
     * Returns true if the generator can consume fuel, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canConsumeFuel()
    {
        if (bioGenItemStacks.get(0).isEmpty() || !isFuel(bioGenItemStacks.get(0)))
        {
            return false;
        }
        else
        {
            ItemStack itemstack = new ItemStack(Items.BUCKET);

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = bioGenItemStacks.get(2);

                if (itemstack1.isEmpty())
                {
                    return true;
                }
                else if (!itemstack1.isItemEqual(itemstack))
                {
                    return false;
                }
                else if (itemstack1.getCount() + itemstack.getCount() <= getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: respect stack sizes
                {
                    return true;
                }
                else
                {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: respect stack sizes
                }
            }
        }
    }

    /**
     * Turn one item from the generator source stack into the appropriate resulting item in the generator result stack
     */
    public void consumeFuel()
    {
        ItemStack itemstack = bioGenItemStacks.get(0);
        ItemStack itemstack1 = new ItemStack(Items.BUCKET);
        ItemStack itemstack2 = bioGenItemStacks.get(2);

        if (itemstack2.isEmpty())
        {
            bioGenItemStacks.set(2, itemstack1.copy());
        }
        else if (itemstack2.getItem() == itemstack1.getItem())
        {
            itemstack2.grow(itemstack1.getCount());
        }

        itemstack.shrink(1);
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
        return new BioGenContainer(playerInventory, this);
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
        bioGenItemStacks.clear();
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