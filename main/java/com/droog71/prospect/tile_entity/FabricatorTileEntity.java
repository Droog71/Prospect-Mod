package com.droog71.prospect.tile_entity;

import java.util.ArrayList;
import java.util.List;

import com.droog71.prospect.forge_energy.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.inventory.FabricatorContainer;
import com.droog71.prospect.items.Schematic;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FabricatorTileEntity extends TileEntity implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> fabricatorItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int energyStored;
    private int energyCapacity;
    private int fabricateTime;
    private int totalfabricateTime;
    private int effectsTimer;
    private boolean itemsConsumed;
    private Object ic2EnergySink;	
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
    
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,64000,3);
			}	
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}
		energyStorage.capacity = 45000;
		energyStorage.maxReceive = 9000;
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
    }
    
	
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return fabricatorItemStacks.size();
    }

    @Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : fabricatorItemStacks)
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
        return fabricatorItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(fabricatorItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(fabricatorItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = fabricatorItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        fabricatorItemStacks.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            totalfabricateTime = getfabricateTime(stack);
            fabricateTime = 0;
            markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
	public String getName()
    {
        return "Fabricator";
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
        fabricatorItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, fabricatorItemStacks);
        energyStored = compound.getInteger("EnergyStored");
        fabricateTime = compound.getInteger("fabricateTime");
        totalfabricateTime = compound.getInteger("fabricateTimeTotal");      
        energyCapacity = compound.getInteger("EnergyCapacity");
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,64000,3);
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
        compound.setInteger("fabricateTime", (short)fabricateTime);
        compound.setInteger("fabricateTimeTotal", (short)totalfabricateTime);
        ItemStackHelper.saveAllItems(compound, fabricatorItemStacks);   
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,64000,3);
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
            	if (itemsConsumed && useEnergy())
            	{
            		++fabricateTime;
                	
                    if (fabricateTime == totalfabricateTime)
                    {
                    	fabricateTime = 0;
                    	totalfabricateTime = getfabricateTime(fabricatorItemStacks.get(0));
                        fabricateItem();
                        needsNetworkUpdate = true;
                    }
            		
            		effectsTimer++;
            		if (effectsTimer > 40)
            		{
            			world.playSound(null, pos, ProspectSounds.fabricatorSoundEvent,  SoundCategory.BLOCKS, 0.25f, 1);
            			effectsTimer = 0;
            		}
            	}
            	else if (canFabricate() && useEnergy())
            	{
            		consumeItems();
            	}
            	else if (fabricateTime > 0)
                {
                	fabricateTime = MathHelper.clamp(fabricateTime - 1, 0, totalfabricateTime);
                }            
        	}      	
        }
        
        if (needsNetworkUpdate)
        {
            markDirty();
        }
    }
    
    // Fabricate items and plays the sound effect
    private void doWork()
    {
    	
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
    			((BasicSink) ic2EnergySink).setCapacity(80000);
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
        	if (((BasicSink) ic2EnergySink).useEnergy(64))
        	{
        		return true;
        	}   
        	else if (energyStorage != null)
        	{
    			if (energyStorage.getEnergyStored() >= 256)
        		{
        			energyStorage.useEnergy(256);
        			return true;
        		}                		
        	}
		}
    	else if (energyStorage != null)
    	{
			if (energyStorage.getEnergyStored() >= 256)
    		{
				energyStorage.useEnergy(256);
    			return true;
    		}                		
    	}
    	return false;
    }
    
    // How long it takes to fabricate the item
    public int getfabricateTime(ItemStack stack)
    {
    	return 50;
    }
  
    /**
     * Returns true if all ingredients relevant to the schematic are present in an adjacent inventory
     */
    private boolean canCraft(ItemStack[] stacks, IInventory iinventory)
    {
        if (iinventory != null)
        {
        	int stacksFound = 0;
			int size = iinventory.getSizeInventory();
			for (ItemStack requiredStack : stacks)          
            {      
            	boolean foundStack = false;
            	for (int index = 0; index < size; ++index)
                {
                	ItemStack containerStack = iinventory.getStackInSlot(index);                       
                    if (containerStack.getItem() == requiredStack.getItem())
                    {
                    	if (containerStack.getCount() >= requiredStack.getCount())
                    	{
                    		if (foundStack == false)
                    		{
                    			foundStack = true;
                    			stacksFound++;
                    		}                  		
                    	}
                    }
                }
            } 
            if (stacksFound >= stacks.length)
            {
            	return true;
            }
        }
        return false;
    }
    
    // Consumes ingredients in adjacent inventory when crafting an item
    private void consumeItems()
    {
    	ItemStack[] stacks = null;
        Item item = fabricatorItemStacks.get(0).getItem();
        
        if (item instanceof Schematic)
        {
        	ItemStack[] required = ((Schematic) item).getIngredients();           
            IInventory inventoryToUse = getInventoryForCrafting(required);      	
            if (inventoryToUse != null)
    		{
            	stacks = required;
    		}
        } 
        
        if (stacks != null)
        {
        	IInventory iinventory = getInventoryForCrafting(stacks);
            if (iinventory != null)
            {
    			int size = iinventory.getSizeInventory();
    			for (ItemStack requiredStack : stacks)
                {      
                	boolean foundStack = false;
                	for (int index = 0; index < size; ++index)
                    {
                    	ItemStack containerStack = iinventory.getStackInSlot(index);                       
                        if (containerStack.getItem() == requiredStack.getItem())
                        {
                        	if (containerStack.getCount() >= requiredStack.getCount())
                        	{
                        		if (foundStack == false)
                        		{
                        			foundStack = true;
                        			containerStack.shrink(requiredStack.getCount());
                        		}
                        	}
                        }
                    }
                } 
    			itemsConsumed = true;
            	iinventory.markDirty();
            }
        }
    }
    
    // Returns an adjacent inventory containing the necessary ingredients for the current schematic
    public IInventory getInventoryForCrafting(ItemStack[] stacks)
    {
    	List<IInventory> invList = new ArrayList<IInventory>();
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};    	
    	for (BlockPos p : positions)
    	{
    		invList.add(getInventoryAtPosition(world,p.getX(),p.getY(),p.getZ()));
    	}   	
    	for (IInventory inventory : invList)
    	{
    		if (inventory != null)
    		{  		
    			if (canCraft(stacks,inventory))
    			{
    				return inventory;
    			} 			
    		}
    	}
    	return null;
    }
    
    // Returns IInventory instance at a given position
    public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z)
    {
        IInventory iinventory = null;
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos);
        Block block = state.getBlock();

        if (block.hasTileEntity(state))
        {
            TileEntity tileentity = worldIn.getTileEntity(blockpos);

            if (tileentity instanceof IInventory)
            {
                iinventory = (IInventory)tileentity;

                if (iinventory instanceof TileEntityChest && block instanceof BlockChest)
                {
                    iinventory = ((BlockChest)block).getContainer(worldIn, blockpos, true);
                }
            }
        }

        if (iinventory == null)
        {
            List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelectors.HAS_INVENTORY);

            if (!list.isEmpty())
            {
                iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
            }
        }

        return iinventory;
    }
    
    /**
     * Returns true if the fabricator can craft an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canFabricate()
    {    	
        if (fabricatorItemStacks.get(0).isEmpty() || !(fabricatorItemStacks.get(0).getItem() instanceof Schematic))
        {
            return false;
        }
        	
        ItemStack result = ((Schematic) fabricatorItemStacks.get(0).getItem()).getResult();
        ItemStack output = fabricatorItemStacks.get(2);

        if (output.isEmpty())
        {
            return true;
        }
        else if (!output.isItemEqual(result))
        {
            return false;
        }
        else if (output.getCount() + result.getCount() <= getInventoryStackLimit() && output.getCount() + result.getCount() <= output.getMaxStackSize())
        {
            return true;
        }
        else
        {
            return output.getCount() + result.getCount() <= result.getMaxStackSize();
        }
    }

    // Creates the resulting item for the current schematic
    public void fabricateItem()
    {        
        ItemStack result = new ItemStack(Items.AIR);   
        ItemStack output = fabricatorItemStacks.get(2);
        Item item = fabricatorItemStacks.get(0).getItem();
             
        if (item instanceof Schematic)
        {
        	result = ((Schematic) item).getResult();                  	
        }                

        if (output.isEmpty())
        {
            fabricatorItemStacks.set(2, result.copy());
            itemsConsumed = false;
        }
        else if (output.getItem() == result.getItem())
        {
        	output.grow(result.getCount());
            itemsConsumed = false;
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
        if (direction == EnumFacing.DOWN && index == 1)
        {
        	return false;
        }

        return true;
    }

    // Not used
    public String getGuiID()
    {
        return null;
    }

    // Creates the container
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new FabricatorContainer(playerInventory, this);
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
                return fabricateTime;
            case 3:
                return totalfabricateTime;
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
                energyStored = value;
                break;
            case 1:
                energyCapacity = value;
                break;
            case 2:
            	fabricateTime = value;
                break;
            case 3:
            	totalfabricateTime = value;
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
        fabricatorItemStacks.clear();
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