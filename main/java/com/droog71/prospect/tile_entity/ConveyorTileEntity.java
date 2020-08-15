package com.droog71.prospect.tile_entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

final class ConveyorWithdrawl
{
	public IInventory inventory;
	public ItemStack withdrawlStack;
	public int withdrawlIndex;
	
	public ConveyorWithdrawl(IInventory inventory, ItemStack withdrawlStack, int withdrawlIndex)
	{
		this.inventory = inventory;
		this.withdrawlStack = withdrawlStack;
		this.withdrawlIndex = withdrawlIndex;
	}
}

final class ConveyorStorage
{
	public IInventory inventory;
	public ItemStack depositStack;
	public int depositIndex;
	public boolean combining;
	public int amount;
	
	public ConveyorStorage(IInventory inventory, ItemStack depositStack, int depositIndex, boolean combining, int amount)
	{
		this.inventory = inventory;
		this.depositStack = depositStack;
		this.depositIndex = depositIndex;
		this.combining = combining;
		this.amount = amount;
	}
}

public class ConveyorTileEntity extends TileEntity implements ITickable
{	
	private int actionTimer;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private ItemStack currentItemStack;
	public boolean withdrawlConveyor;
	public ConveyorTileEntity input;
	public ConveyorTileEntity output;
	
	@Override
    public void onLoad() 
	{
		super.onLoad();
	}
	 
	@Override
    public void invalidate() 
    {
		super.invalidate();
    }
 
    @Override
    public void onChunkUnload() 
    {
    	super.onChunkUnload();
    }
 
    @Override
    public void readFromNBT(NBTTagCompound tag) 
    {
        super.readFromNBT(tag);
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) 
    {
        super.writeToNBT(tag);
        return tag;
    }   
    
    @Override
	public void update() 
	{
		if (!world.isRemote)
		{
			actionTimer++;
			if (actionTimer >= 60)
			{
				actionTimer = 0;
				
				if (getConveyorWithdrawl() == null)
				{
					if (input != null)
					{
						handleInput();
					}					
				}
				
				if (output == null)
				{
					if (withdrawlConveyor || input != null)
					{
						connectConveyor();
					}				
					if (output == null)
					{
						if (currentItemStack != null && currentItemStack != ItemStack.EMPTY)
						{
							transferItemOut(currentItemStack);
							currentItemStack = ItemStack.EMPTY;
						}		
					}
					else
					{
						handleOutput();
					}
				}
				else
				{
					handleOutput();
				}	
			}
		}
	}
    
    // Returns the slot to withdraw from
    public ConveyorWithdrawl getConveyorWithdrawl()
    {
    	if (!world.isBlockPowered(pos))
    	{
    		withdrawlConveyor = false;
    		return null;
    	}
    	List<IInventory> inventoryList = new ArrayList<IInventory>();
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};    	
    	for (BlockPos p : positions)
    	{
    		IInventory inventory = getInventoryAtPosition(p);
    		if (inventory != null)
    		{
    			inventoryList.add(inventory);
    		}
    	}   	
    	for (IInventory inventory : inventoryList)
    	{ 		
    		int size = inventory.getSizeInventory();
            for (int index = 0; index < size; ++index)
            {
            	ItemStack itemstack = inventory.getStackInSlot(index);                       
                if (!itemstack.isEmpty())
                {
                	inventory.setInventorySlotContents(index, ItemStack.EMPTY);
                	withdrawlConveyor = true;
                	currentItemStack = itemstack;
                	return new ConveyorWithdrawl(inventory,itemstack,index);
                }                      
            } 			
    	}
    	withdrawlConveyor = false;
    	return null;
    }
    
    // Puts mined blocks and items into adjacent storage
    private void transferItemOut(ItemStack stack)
    {
        ConveyorStorage conveyorStorage = getConveyorStorage(stack);
        if (conveyorStorage != null)
        {                      
            if (conveyorStorage.combining == true)
            {
            	stack.shrink(conveyorStorage.amount);
            	conveyorStorage.depositStack.grow(conveyorStorage.amount); 
            	conveyorStorage.inventory.markDirty();
            	//System.out.println("Combining stacks!");
            }
            else
            {
            	conveyorStorage.inventory.setInventorySlotContents(conveyorStorage.depositIndex, stack);
            	conveyorStorage.inventory.markDirty();
            	//System.out.println("Depositing "+stack.getCount()+" "+stack.getDisplayName()+" to slot "+conveyorStorage.depositIndex+" of "+conveyorStorage.inventory);
            	stack = ItemStack.EMPTY;               
            }                        
        }
        else
        {
        	WorldServer w = (WorldServer) world;
        	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), stack);
        	w.spawnEntity(item);
        }
    }
    
    // Returns IInventory instance at a given position
    public IInventory getInventoryAtPosition(BlockPos blockpos)
    {
        TileEntity tileentity = world.getTileEntity(blockpos);
        if (tileentity != null)
        {
            if (tileentity instanceof IInventory)
            {
                return (IInventory)tileentity;  
            }
        }
        return null;
    }
    
    // The adjacent inventory the quarry will use to transfer items out
    public ConveyorStorage getConveyorStorage(ItemStack stack)
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};
    	List<IInventory> inventoryList = new ArrayList<IInventory>();  	    	
    	for (BlockPos p : positions)
    	{
    		IInventory inventory = getInventoryAtPosition(p);
    		if (inventory != null)
    		{
    			inventoryList.add(inventory);
    		}		
    	}   	
    	for (IInventory inventory : inventoryList)
    	{
			int size = inventory.getSizeInventory();
            for (int index = 0; index < size; ++index)
            {
            	ItemStack itemstack = inventory.getStackInSlot(index);                       
                if (itemstack.isEmpty())
                {
                	return new ConveyorStorage(inventory, itemstack, index, false, 0);
                }
                else if (canCombine(itemstack, stack))
                {
                    int i = stack.getMaxStackSize() - itemstack.getCount();
                    int j = Math.min(stack.getCount(), i);                        
                    if (j > 0)
                    {
                    	return new ConveyorStorage(inventory, itemstack, 0, true, j);
                    }
                }                        
            } 
    	}
    	return null;
    }
    
    // Checks if an itemstack can be combined with another
    private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        if (stack1.getItem() != stack2.getItem())
        {
            return false;
        }
        else if (stack1.getMetadata() != stack2.getMetadata())
        {
            return false;
        }
        else if (stack1.getCount() > stack1.getMaxStackSize())
        {
            return false;
        }
        else
        {
            return ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }
    
    private void connectConveyor()
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};	    	
    	for (BlockPos p : positions)
    	{
    		TileEntity t = world.getTileEntity(p);
    		if (t != null)
    		{
    			if (t instanceof ConveyorTileEntity)
    			{
    				ConveyorTileEntity otherConveyor = (ConveyorTileEntity) t;
    				if (otherConveyor.withdrawlConveyor == false && otherConveyor.input == null && otherConveyor.output != this)
					{
    					if (output == null)
    					{
    						output = otherConveyor;
    						outputPos = p;
    						otherConveyor.input = this;
    						otherConveyor.inputPos = pos;
    					}    					
					}    				
    			}
    		}		
    	}   	
    }

    private void handleInput()
    {
    	TileEntity t = world.getTileEntity(inputPos);
		if (t != null)
		{
			if (t instanceof ConveyorTileEntity)
			{
				if (input.withdrawlConveyor || input.input != null)
				{
					input = (ConveyorTileEntity) t;
				}
				else
				{
					input = null;
				}
			}
			else
			{
				input = null;
			}
		}
		else
		{
			input = null;
		}
    }
    
    private void handleOutput()
    {
    	TileEntity t = world.getTileEntity(outputPos);
		if (t != null)
		{
			if (t instanceof ConveyorTileEntity)
			{
				if (withdrawlConveyor || input != null)
				{
					output = (ConveyorTileEntity) t;
					if (currentItemStack != ItemStack.EMPTY)
					{
						output.currentItemStack = currentItemStack;
						currentItemStack = ItemStack.EMPTY;
					}				
				}
				else
				{
					output = null;
				}
			}
			else
			{
				output = null;
			}
		}
		else
		{
			output = null;
		}
    }
}