package com.droog71.prospect.tilentity;

import java.util.ArrayList;
import java.util.List;

import com.droog71.prospect.fe.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectSounds;
import com.droog71.prospect.inventory.PrinterContainer;
import com.droog71.prospect.items.Schematic;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PrinterTileEntity extends TileEntity implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> printerItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int energyStored;
    private int energyCapacity;
    private int printTime;
    private int totalPrintTime;
    private int effectsTimer;
    private Object ic2EnergySink;	
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
    
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,400000,4);      		
			}	
			((BasicSink) ic2EnergySink).onLoad(); // notify the energy sink
		}
		energyStorage.capacity = 80000;
		energyStorage.maxReceive = 16000;
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
        return printerItemStacks.size();
    }

    @Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : printerItemStacks)
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
        return printerItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(printerItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(printerItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = printerItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        printerItemStacks.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            totalPrintTime = getPrintTime(stack);
            printTime = 0;
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
        printerItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, printerItemStacks);
        energyStored = compound.getInteger("EnergyStored");
        printTime = compound.getInteger("PrintTime");
        totalPrintTime = compound.getInteger("PrintTimeTotal");      
        energyCapacity = compound.getInteger("EnergyCapacity");
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,400000,4);
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
        compound.setInteger("PrintTime", (short)printTime);
        compound.setInteger("PrintTimeTotal", (short)totalPrintTime);
        ItemStackHelper.saveAllItems(compound, printerItemStacks);   
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,400000,4);
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
        if (!world.isRemote)
        {
        	if (energyStorage.overloaded)
            {
            	explode();
            }
        	else
        	{
        		updateEnergy();           	
                if (isEnergized())
                {
                    if (canPrint())
                    {               	
                    	if (useEnergy())
                    	{
                    		doWork();
                    	}
                    }
                }
        	}      	
        }
    }
    
    private void doWork()
    {
    	boolean flag1 = false;   
    	++printTime;
    	
        if (printTime == totalPrintTime)
        {
        	printTime = 0;
        	totalPrintTime = getPrintTime(printerItemStacks.get(0));
            printItem();
            flag1 = true;
        }
        
		if (flag1)
        {
            markDirty();
        }
		
		effectsTimer++;
		if (effectsTimer > 40)
		{
			world.playSound(null, pos, ProspectSounds.printerSoundEvent,  SoundCategory.BLOCKS, 0.25f, 1);
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
    
    private boolean useEnergy()
    {
    	if (Loader.isModLoaded("ic2"))
		{
        	if (((BasicSink) ic2EnergySink).useEnergy(512))
        	{
        		return true;
        	}   
        	else if (energyStorage != null)
        	{
    			if (energyStorage.getEnergyStored() >= 2048)
        		{
        			energyStorage.useEnergy(2048);
        			return true;
        		}                		
        	}
		}
    	else if (energyStorage != null)
    	{
			if (energyStorage.getEnergyStored() >= 2048)
    		{
				energyStorage.useEnergy(2048);
    			return true;
    		}                		
    	}
    	return false;
    }
    
    public int getPrintTime(ItemStack stack)
    {
    	return 200;
    }
  
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
    
    private void consumeItems(ItemStack[] stacks)
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
        	iinventory.markDirty();
        }
    }
    
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
     * Returns true if the printer can print an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canPrint()
    {    	
        if (printerItemStacks.get(0).isEmpty() || !(printerItemStacks.get(0).getItem() instanceof Schematic))
        {
            return false;
        }
        
        ItemStack itemstack = null;  	
        Item item = printerItemStacks.get(0).getItem();  	
    	ItemStack[] required = ((Schematic) item).getIngredients();
        ItemStack result = ((Schematic) item).getResult();
        
        IInventory inventoryToUse = getInventoryForCrafting(required);
    	
        if (inventoryToUse != null)
		{
			itemstack = result;
		}
    	
        if (itemstack == null)
        {
            return false;
        }

        ItemStack itemstack1 = printerItemStacks.get(2);

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

    public void printItem()
    {
        if (canPrint())
        {           
            ItemStack itemstack1 = new ItemStack(Items.AIR);
            
            Item item = printerItemStacks.get(0).getItem();  
                 
            if (item instanceof Schematic)
            {
            	ItemStack[] required = ((Schematic) item).getIngredients();
                ItemStack result = ((Schematic) item).getResult();
                 
                IInventory inventoryToUse = getInventoryForCrafting(required);
            	
                if (inventoryToUse != null)
        		{
        			consumeItems(required);
        			itemstack1 = result;
        		}
            } 
            
            ItemStack itemstack2 = printerItemStacks.get(2);

            if (itemstack2.isEmpty())
            {
                printerItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(itemstack1.getCount());
            }
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

    public String getGuiID()
    {
        return null;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new PrinterContainer(playerInventory, this);
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
                return printTime;
            case 3:
                return totalPrintTime;
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
            	printTime = value;
                break;
            case 3:
            	totalPrintTime = value;
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
        printerItemStacks.clear();
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