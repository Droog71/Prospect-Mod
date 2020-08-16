package com.droog71.prospect.tile_entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

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

public class ConveyorTileEntity extends TileEntityChest implements ITickable
{	
	private int actionTimer;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private ItemStack currentItemStack = ItemStack.EMPTY;
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
    public String getName()
    {
        return "Conveyor Tube";
    }
    
    @Override
    public void checkForAdjacentChests()
    {
    	return;
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
				
				if (!isWithdrawlConveyor())
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
						if (currentItemStack != ItemStack.EMPTY)
						{
							transferItemOut();
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
    
    private List<Item> filterList()
    {
    	List<Item> itemlist = new ArrayList<Item>();
    	IInventory inventory = getInventoryAtPosition(pos);
    	if (inventory != null)
    	{
    		int size = inventory.getSizeInventory();
            for (int index = 0; index < size; ++index)
            {
            	ItemStack itemstack = inventory.getStackInSlot(index);                       
                if (!itemstack.isEmpty())
                {
                	itemlist.add(itemstack.getItem());
                }                      
            } 
            return itemlist;
    	}
    	return null;
    }
    
    // Returns the slot to withdraw from
    private boolean isWithdrawlConveyor()
    {
    	if (!(world.getStrongPower(pos) >= 15))
    	{
    		withdrawlConveyor = false;
    		return false;
    	}
    	List<IInventory> inventoryList = new ArrayList<IInventory>();
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};    	
    	for (BlockPos p : positions)
    	{
    		IInventory inventory = getInventoryAtPosition(p);
    		if (inventory != null)
    		{
    			if (!(inventory instanceof ConveyorTileEntity))
    			{
    				inventoryList.add(inventory);
    			}
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
            		if (filterList().contains(itemstack.getItem()))
					{
                		inventory.setInventorySlotContents(index, ItemStack.EMPTY);
                		currentItemStack = itemstack;
                		withdrawlConveyor = true;
                    	return true;
					}
                	else
                	{
                		currentItemStack = ItemStack.EMPTY;
                		withdrawlConveyor = true;
                    	return true;
                	}  
                }
            } 			
    	}
    	withdrawlConveyor = false;
    	return false;
    }
    
    // Puts mined blocks and items into adjacent storage
    private void transferItemOut()
    {
        ConveyorStorage conveyorStorage = getConveyorStorage();
        if (conveyorStorage != null)
        {                      
            if (conveyorStorage.combining == true)
            {
            	conveyorStorage.depositStack.grow(conveyorStorage.amount);
            	currentItemStack = ItemStack.EMPTY;  
            	conveyorStorage.inventory.markDirty();
            }
            else
            {
            	conveyorStorage.inventory.setInventorySlotContents(conveyorStorage.depositIndex, currentItemStack);
            	conveyorStorage.inventory.markDirty();
            	currentItemStack = ItemStack.EMPTY;               
            }                        
        }
        else
        {
        	ejectItem();
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
    public ConveyorStorage getConveyorStorage()
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};
    	List<IInventory> inventoryList = new ArrayList<IInventory>();  	    	
    	for (BlockPos p : positions)
    	{
    		IInventory inventory = getInventoryAtPosition(p);
    		if (inventory != null)
    		{
    			if (!(inventory instanceof ConveyorTileEntity))
    			{
    				inventoryList.add(inventory);
    			}
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
                else if (canCombine(itemstack, currentItemStack))
                {
                    int i = currentItemStack.getMaxStackSize() - itemstack.getCount();
                    int j = Math.min(currentItemStack.getCount(), i);                        
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
        else if (stack1.getCount() > stack2.getMaxStackSize() - stack2.getCount())
        {
        	return false;
        }
        else if (stack2.getCount() > stack1.getMaxStackSize() - stack1.getCount())
        {
        	return false;
        }
        else
        {
            return ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }
    
    // Finds an adjacent conveyor tube to designate as the output
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

    // Ejects the current itemstack into the world
    private void ejectItem()
    {
    	if (currentItemStack != ItemStack.EMPTY)
        {
	    	WorldServer w = (WorldServer) world;
	    	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), currentItemStack);
	    	w.spawnEntity(item);
	    	currentItemStack = ItemStack.EMPTY;
        }
    }
    
    // Checks for valid input at the location it was last found
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
    
    
    // Sends itemstack to the next conveyor tube or ejects it into the world
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
					if (output.withdrawlConveyor == true)
					{
						ejectItem();
					}
					else if (currentItemStack != ItemStack.EMPTY)
					{
						if (output.currentItemStack == ItemStack.EMPTY)
						{
							output.currentItemStack = currentItemStack;
							currentItemStack = ItemStack.EMPTY;
						}
						else
				        {
							ejectItem();
				        }
					}				
				}
				else
				{
					ejectItem();
					output = null;
				}
			}
			else
			{
				ejectItem();
				output = null;
			}
		}
		else
		{
			ejectItem();
			output = null;
		}
    }
}