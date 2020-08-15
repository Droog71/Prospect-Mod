package com.droog71.prospect.tile_entity;

import com.droog71.prospect.forge_energy.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.inventory.ExtruderContainer;

import ic2.api.energy.prefab.BasicSink;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ExtruderTileEntity extends TileEntity implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> extruderItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int energyStored;
    private int energyCapacity;
    private int extrudeTime;
    private int totalextrudeTime;
    private int effectsTimer = 250;
    private Object ic2EnergySink;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
    
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,1000,1);
			}
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}	
		energyStorage.capacity = 5000;
		energyStorage.maxReceive = 1000;
	}
	 
	@Override
    public void invalidate() 
    {
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink != null))
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
    		if (((BasicSink) ic2EnergySink != null))
			{
    			((BasicSink) ic2EnergySink).onChunkUnload(); // notify the energy sink
			}
		}
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return extruderItemStacks.size();
    }

    @Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : extruderItemStacks)
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
        return extruderItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(extruderItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(extruderItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = extruderItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        extruderItemStacks.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            totalextrudeTime = getextrudeTime(stack);
            extrudeTime = 0;
            markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
	public String getName()
    {
        return "Extruder";
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
        extruderItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, extruderItemStacks);
        energyStored = compound.getInteger("EnergyStored");
        extrudeTime = compound.getInteger("extrudeTime");
        totalextrudeTime = compound.getInteger("TotalextrudeTime");        
        energyCapacity = compound.getInteger("EnergyCapacity");  
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
				ic2EnergySink = new BasicSink(this,1000,1);
			}	
	        ((BasicSink) ic2EnergySink).readFromNBT(compound);
		}
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);       
        compound.setInteger("EnergyStored", (short)energyStored);
        compound.setInteger("EnergyCapacity", (short)energyCapacity);
        compound.setInteger("extrudeTime", (short)extrudeTime);
        compound.setInteger("TotalextrudeTime", (short)totalextrudeTime);
        ItemStackHelper.saveAllItems(compound, extruderItemStacks);
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
				ic2EnergySink = new BasicSink(this,1000,1);
			}	
	        ((BasicSink) ic2EnergySink).writeToNBT(compound);
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
    		if (((BasicSink) ic2EnergySink).getEnergyStored() > 0)
			{
				return ((BasicSink) ic2EnergySink).getEnergyStored() > 0;
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
    	
        if (!world.isRemote)
        {
            if (energyStorage.overloaded)
            {
            	energyStorage.explode(world,pos);
            }
            else
            {
            	updateEnergy();            	
            	if (canExtrude() && useEnergy())
                {            	
            		++extrudeTime;    	
                    if (extrudeTime == totalextrudeTime)
                    {
                        extrudeTime = 0;
                        totalextrudeTime = getextrudeTime(extruderItemStacks.get(0));
                        extrudeItem();
                        needsNetworkUpdate = true;
                    }
                    
                    effectsTimer++;
            		if (effectsTimer > 200)
            		{	
            			world.playSound(null, pos, ProspectSounds.extruderSoundEvent,  SoundCategory.BLOCKS, 0.5f, 1);
            			effectsTimer = 0;
            		}
                }
                else if (extrudeTime > 0)
                {
                    extrudeTime = MathHelper.clamp(extrudeTime - 1, 0, totalextrudeTime);
                }
            }                   
        } 
        
        if (needsNetworkUpdate)
        {
            markDirty();
        }
    }
    
    // Get values from the energy storage or ic2 energy sink
    private void updateEnergy()
    {
    	if (energyStorage.getEnergyStored() > 0)
    	{
    		energyStored = energyStorage.getEnergyStored();
    		energyCapacity = energyStorage.getMaxEnergyStored();
    		if (Loader.isModLoaded("ic2"))
    		{
				((BasicSink) ic2EnergySink).setEnergyStored(0);
    			((BasicSink) ic2EnergySink).setCapacity(0);  			
    		}
    	}
    	else
    	{
    		if (Loader.isModLoaded("ic2"))
    		{
    			((BasicSink) ic2EnergySink).setCapacity(1000);
        		if (((BasicSink) ic2EnergySink).getEnergyStored() > 0)
        		{
        			energyStored = (int) ((BasicSink) ic2EnergySink).getEnergyStored();
        			energyCapacity = (int) ((BasicSink) ic2EnergySink).getCapacity();
        		}
    		}  
    	}   	 
    }
    
    // Remove energy from the buffer
    private boolean useEnergy()
    {
    	if (Loader.isModLoaded("ic2"))
		{
        	if (((BasicSink) ic2EnergySink).useEnergy(2))
        	{
        		return true;
        	}   
        	else if (energyStorage != null)
        	{
    			if (energyStorage.getEnergyStored() >= 8)
        		{
        			energyStorage.useEnergy(8);
        			return true;
        		}                		
        	}
		}
    	else if (energyStorage != null)
    	{
			if (energyStorage.getEnergyStored() >= 8)
    		{
    			energyStorage.useEnergy(8);
    			return true;
    		}                		
    	}
    	return false;
    }
    
    // How long it takes to extrude the ingot
    public int getextrudeTime(ItemStack stack)
    {
        return 50;
    }

    // Checks if the item in question is registered as a copper ingot
    private boolean isCopperIngot(ItemStack stack)
    {
    	NonNullList<ItemStack> copper = OreDictionary.getOres("ingotCopper");
    	for (ItemStack s : copper)
    	{ 		   	
    		if (s.getItem().getRegistryName() == stack.getItem().getRegistryName())
    		{
    			if (s.getMetadata() == stack.getMetadata())
    			{
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * Returns true if the extruder can extrude an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canExtrude()
    {
        if (extruderItemStacks.get(0).isEmpty() || !isCopperIngot(extruderItemStacks.get(0)))
        {
            return false;
        }
        else
        {
        	ItemStack itemstack = ItemStack.EMPTY;
        	if (world.isBlockPowered(pos))
        	{
        		itemstack = new ItemStack(ProspectItems.copper_wire,6);
        	}
        	else
        	{
        		itemstack = new ItemStack(ProspectBlocks.copper_pipe);
        	}      

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = extruderItemStacks.get(2);

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
     * Turn one item from the extruder source stack into the appropriate resulting item in the extruder result stack
     */
    public void extrudeItem()
    {
        if (canExtrude())
        {
            ItemStack itemstack = extruderItemStacks.get(0);
            ItemStack itemstack1 = ItemStack.EMPTY; 
            ItemStack itemstack2 = extruderItemStacks.get(2);
            
            if (world.isBlockPowered(pos))
        	{
        		itemstack1 = new ItemStack(ProspectItems.copper_wire,6);
        	}
        	else
        	{
        		itemstack1 = new ItemStack(ProspectBlocks.copper_pipe);
        	} 

            if (itemstack2.isEmpty())
            {
                extruderItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
        }
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
        return new ExtruderContainer(playerInventory, this);
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
                return extrudeTime;
            case 3:
                return totalextrudeTime;
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
                extrudeTime = value;
                break;
            case 3:
                totalextrudeTime = value;
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
        extruderItemStacks.clear();
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
