package com.droog71.prospect.tilentity;

import com.droog71.prospect.fe.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.inventory.ReplicatorContainer;
import ic2.api.energy.prefab.BasicSink;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ReplicatorTileEntity extends TileEntity implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> replicatorItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int energyStored;
    private int energyCapacity;
    private int replicatorSpendTime;
    private int currentCreditSpendTime;
    private int replicateTime;
    private int totalreplicateTime;
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
        		ic2EnergySink = new BasicSink(this,1000000,5);       		
			}	
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}
		energyStorage.capacity = 125000;
		energyStorage.maxReceive = 25000;
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
			super.invalidate(); // this is important for mc!
		}
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
    }
    
	
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return replicatorItemStacks.size();
    }

    @Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : replicatorItemStacks)
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
        return replicatorItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(replicatorItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(replicatorItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = replicatorItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        replicatorItemStacks.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            totalreplicateTime = getreplicateTime(stack);
            replicateTime = 0;
            markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
	public String getName()
    {
        return "Replicator";
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
        replicatorItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, replicatorItemStacks);
        energyStored = compound.getInteger("EnergyStored");
        replicatorSpendTime = compound.getInteger("SpendTime");
        replicateTime = compound.getInteger("replicateTime");
        totalreplicateTime = compound.getInteger("replicateTimeTotal");
        currentCreditSpendTime = getCreditSpendTime(replicatorItemStacks.get(1));
        energyCapacity = compound.getInteger("EnergyCapacity");
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,1000000,5);
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
        compound.setInteger("SpendTime", (short)replicatorSpendTime);
        compound.setInteger("replicateTime", (short)replicateTime);
        compound.setInteger("replicateTimeTotal", (short)totalreplicateTime);
        ItemStackHelper.saveAllItems(compound, replicatorItemStacks);   
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,1000000,5);
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
        return inventory.getField(5) > 0;
    }
    
    public boolean isReplicating()
    {
        return replicatorSpendTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isReplicating(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
	public void update()
    {
        boolean flag1 = false;

        if (!world.isRemote)
        {
        	if (energyStorage.overloaded)
            {
            	explode();
            }
			else
			{
	        	updateEnergy();
	        	
	            ItemStack itemstack = replicatorItemStacks.get(1);
	
	            if (!itemstack.isEmpty() && !replicatorItemStacks.get(0).isEmpty())
	            {
	                if (isEnergized() && canReplicate())
	                {
	                	if (useEnergy())
	                	{
	                		--replicatorSpendTime;
	                		if (replicatorSpendTime <= 0)
	                		{
	                            itemstack.shrink(1);
	                            replicatorSpendTime = getCreditSpendTime(itemstack);
	                            flag1 = true;
	                		}	                		
                           
		                    ++replicateTime;
		                    if (replicateTime == totalreplicateTime)
		                    {
		                        replicateTime = 0;
		                        totalreplicateTime = getreplicateTime(replicatorItemStacks.get(0));
		                        replicateItem();
		                        flag1 = true;
		                    }
		                    
		                    effectsTimer++;
		    				if (effectsTimer > 40)
		    				{
		    					world.playSound(null, pos, ProspectSounds.replicatorSoundEvent,  SoundCategory.BLOCKS, 0.25f, 1);
		    					effectsTimer = 0;
		    				}
	                	}
	                }
	                else
	                {
	                    replicateTime = 0;
	                }
	            }
	            
	            if (flag1)
	            {
	                markDirty();
	            }
			}
        }
        
        if (flag1)
        {
            markDirty();
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
    			((BasicSink) ic2EnergySink).setCapacity(1000000);
        		if (((BasicSink) ic2EnergySink).getEnergyStored() > 0)
        		{
        			energyStored = (int) ((BasicSink) ic2EnergySink).getEnergyStored();
        			energyCapacity = (int) ((BasicSink) ic2EnergySink).getCapacity();
        		}
    		}  
    	}   	 
    }
    
    private boolean useEnergy()
    {
    	if (Loader.isModLoaded("ic2"))
		{
        	if (((BasicSink) ic2EnergySink).useEnergy(4096))
        	{
        		return true;
        	}   
        	else if (energyStorage != null)
        	{
    			if (energyStorage.getEnergyStored() >= 16384)
        		{
        			energyStorage.useEnergy(16384);
        			return true;
        		}                		
        	}
		}
    	else if (energyStorage != null)
    	{
			if (energyStorage.getEnergyStored() >= 16384)
    		{
				energyStorage.useEnergy(16384);
    			return true;
    		}                		
    	}
    	return false;
    }
    
    public int getreplicateTime(ItemStack stack)
    {
    	Item i = stack.getItem();
    	if (i == Items.EMERALD || i == Items.DIAMOND)
    	{
    		return 400;
    	}
    	if (Loader.isModLoaded("ic2"))
    	{
    		if (stack == Ic2Items.uraniumDrop)
    		{
    			return 400;
    		}
    	}
    	return 200;
    }

    private boolean invalidReplicatorItem(ItemStack stack)
    {
    	Item i = stack.getItem();
    	if (i == Item.getItemFromBlock(Blocks.WOOL) || i == Items.GLOWSTONE_DUST || i == Items.IRON_INGOT || i == Items.GOLD_INGOT || i == Items.REDSTONE || i == Items.DIAMOND || i == Items.CLAY_BALL || i == Items.QUARTZ || i == Items.COAL || i == Items.EMERALD)
    	{
    		return false;
    	}
    	if (Loader.isModLoaded("ic2"))
    	{
    		if (stack == Ic2Items.stickyResin || stack == Ic2Items.uraniumDrop)
    		{
    			return false;
    		}
    	}
    	NonNullList<ItemStack> copper = OreDictionary.getOres("ingotCopper");
    	for (ItemStack s : copper)
    	{ 		
    		if (s.getDisplayName() == stack.getDisplayName())
    		{
    			return false;
    		}
    	}
    	NonNullList<ItemStack> tin = OreDictionary.getOres("ingotTin");
    	for (ItemStack s : tin)
    	{
    		if (s.getDisplayName() == stack.getDisplayName())
    		{
    			return false;
    		}
    	}
    	NonNullList<ItemStack> silver = OreDictionary.getOres("ingotSilver");
    	for (ItemStack s : silver)
    	{
    		if (s.getDisplayName() == stack.getDisplayName())
    		{
    			return false;
    		}
    	}
    	NonNullList<ItemStack> lead = OreDictionary.getOres("ingotLead");
    	for (ItemStack s : lead)
    	{
    		if (s.getDisplayName() == stack.getDisplayName())
    		{
    			return false;
    		}
    	}
    	NonNullList<ItemStack> aluminum = OreDictionary.getOres("ingotAluminum");
    	for (ItemStack s : aluminum)
    	{
    		if (s.getDisplayName() == stack.getDisplayName())
    		{
    			return false;
    		}
    	}
    	NonNullList<ItemStack> silicon = OreDictionary.getOres("silicon");
    	for (ItemStack s : silicon)
    	{
    		if (s.getDisplayName() == stack.getDisplayName())
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Returns true if the transmitter can transmit an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canReplicate()
    {
    	Item item = replicatorItemStacks.get(0).getItem();
    	ItemStack itemstack = new ItemStack(item);

        if (itemstack.isEmpty())
        {
            return false;
        }
        else if (invalidReplicatorItem(itemstack))
        {
        	return false;
        }
        else
        {
            ItemStack itemstack1 = replicatorItemStacks.get(2);

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

    public void replicateItem()
    {
        if (canReplicate())
        {           
            ItemStack itemstack = replicatorItemStacks.get(0);
            ItemStack itemstack1 = new ItemStack(itemstack.getItem());
            ItemStack itemstack2 = replicatorItemStacks.get(2);

            if (itemstack2.isEmpty())
            {
                replicatorItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(itemstack1.getCount());
            }
        }
    }

    public static int getCreditSpendTime(ItemStack stack) //Could eventually be used for differing denominations of currency.
    {
    	return 10; 
    }

    public static boolean isCredit(ItemStack stack)
    {
        return stack.getItem() == ProspectItems.credit;
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
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            return stack.getItem() == ProspectItems.credit;
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
        if (direction == EnumFacing.DOWN && index == 1)
        {
        	return false;
        }

        return true;
    }

    public String getGuiID()
    {
        return null;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ReplicatorContainer(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return replicatorSpendTime;
            case 1:
                return currentCreditSpendTime;
            case 2:
                return replicateTime;
            case 3:
                return totalreplicateTime;
            case 4:
                return energyCapacity;
            case 5:
                return energyStored;
            default:
                return 0;
        }
    }

    @Override
	public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                replicatorSpendTime = value;
                break;
            case 1:
                currentCreditSpendTime = value;
                break;
            case 2:
                replicateTime = value;
                break;
            case 3:
                totalreplicateTime = value;
                break;
            case 4:
            	energyCapacity = value;
                break;
            case 5:
            	energyStored = value;
                break;
        }
    }

    @Override
	public int getFieldCount()
    {
        return 6;
    }

    @Override
	public void clear()
    {
        replicatorItemStacks.clear();
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
