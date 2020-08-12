package com.droog71.prospect.tile_entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.droog71.prospect.forge_energy.ProspectEnergyStorage;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectSounds;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class QuarryTileEntity extends TileEntity implements ITickable
{	
	private int quarryTimer;
	private int soundTimer;
	private int level = 1;
	private int miningX = 100000000;
	private int miningZ = 100000000;
	private boolean quarryFinished;
	private boolean overflow;
	private IInventory currentInventory;
	private int energyStored;
	private boolean miningBlock;
	private Object ic2EnergySink;
	private ProspectEnergyStorage energyStorage = new ProspectEnergyStorage();
	public List<BlockPos> quarryPositions = new ArrayList<BlockPos>();
		
	@Override
    public void onLoad() 
	{
		if (Loader.isModLoaded("ic2"))
		{
			if (((BasicSink) ic2EnergySink == null))
			{
				ic2EnergySink = new BasicSink(this,256000,4);
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
 
    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        
        quarryTimer = compound.getInteger("quarryTimer");
        level = compound.getInteger("level");
        miningX = compound.getInteger("miningX");
        miningZ = compound.getInteger("miningZ");      
        quarryFinished = compound.getBoolean("quarryFinished");
        energyStored = compound.getInteger("energyStored");
        
        int[] quarryPosX = compound.getIntArray("quarryPosX");
        int[] quarryPosY = compound.getIntArray("quarryPosY");
        int[] quarryPosZ = compound.getIntArray("quarryPosZ");
        for (int i=0; i<quarryPosX.length; i++)
        {
        	quarryPositions.add(new BlockPos(quarryPosX[i],quarryPosY[i],quarryPosZ[i]));
        }  
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,256000,4);
			}	
	        ((BasicSink) ic2EnergySink).readFromNBT(compound);
		}
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        
        compound.setInteger("quarryTimer", (short)quarryTimer);
        compound.setInteger("level", (short)level);
        compound.setInteger("miningX", (short)miningX);
        compound.setInteger("miningZ", (short)miningZ);
        compound.setBoolean("quarryFinished", quarryFinished);
        compound.setInteger("energyStored", energyStored);
        
        int[] quarryPosX = new int[quarryPositions.size()];
        int[] quarryPosY = new int[quarryPositions.size()];
        int[] quarryPosZ = new int[quarryPositions.size()];
        for (int i=0; i<quarryPositions.size(); i++)
        {
        	quarryPosX[i] = quarryPositions.get(i).getX();
        	quarryPosY[i] = quarryPositions.get(i).getY();
        	quarryPosZ[i] = quarryPositions.get(i).getZ();
        }
        compound.setIntArray("quarryPosX", quarryPosX);
        compound.setIntArray("quarryPosY", quarryPosY);
        compound.setIntArray("quarryPosZ", quarryPosZ);
        if (Loader.isModLoaded("ic2"))
		{
	        if ((BasicSink) ic2EnergySink == null)
			{
	        	ic2EnergySink = new BasicSink(this,256000,4);
			}	
	        ((BasicSink) ic2EnergySink).writeToNBT(compound);
		}
        
        return compound;
    }
 
    // Puts mined blocks and items into adjacent storage
    private void transferItemOut(ItemStack stack)
    {
        IInventory iinventory = getInventoryForTransfer();
        if (iinventory != null)
        {
        	if (!isInventoryFull(iinventory))
            {
        		if (!isLiquid(stack))
            	{
        			int size = iinventory.getSizeInventory();
        			boolean flag = false;
                    for (int index = 0; index < size; ++index)
                    {
                    	ItemStack itemstack = iinventory.getStackInSlot(index);                       
                        if (itemstack.isEmpty())
                        {
                    		iinventory.setInventorySlotContents(index, stack);
                            stack = ItemStack.EMPTY;                     	
                            flag = true;
                        }
                        else if (canCombine(itemstack, stack))
                        {
                            int i = stack.getMaxStackSize() - itemstack.getCount();
                            int j = Math.min(stack.getCount(), i);
                        	stack.shrink(j);
                            itemstack.grow(j);                        
                            flag = j > 0;
                        }                        
                    } 
                    if (flag)
                    {
                    	overflow = false;
                		iinventory.markDirty();
                    }
                    else
                    {
                    	overflow = true;
                    	WorldServer w = (WorldServer) world;
                    	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), stack);
                    	w.spawnEntity(item);
                    }
            	}  
            }
        	else
            {
            	WorldServer w = (WorldServer) world;
            	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), stack);
            	w.spawnEntity(item);
            }
        }
        else
        {
        	WorldServer w = (WorldServer) world;
        	EntityItem item = new EntityItem(w, pos.getX(), pos.getY(), pos.getZ(), stack);
        	w.spawnEntity(item);
        }
    }

    // Checks if the block being mined is a liquid
    private boolean isLiquid(ItemStack stack)
    {
    	Block b = Block.getBlockFromItem(stack.getItem());
    	if (b instanceof BlockLiquid || b instanceof BlockFluidBase || b instanceof IFluidBlock)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
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

    // Checks if given inventory is full
    private boolean isInventoryFull(IInventory inventoryIn)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            return true;
        }
        else
        {
            int i = inventoryIn.getSizeInventory();
            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Checks if given inventory has any empty slots
    private boolean inventoryHasEmptySlot(IInventory inventoryIn)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            return false;
        }
        else
        {
            int i = inventoryIn.getSizeInventory();
            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty())
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    // The adjacent inventory the quarry will use to transfer items out
    public IInventory getInventoryForTransfer()
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
        		if (!isInventoryFull(inventory))
        		{
        			if (currentInventory != null)
        			{
        				if (overflow)
        				{       					
    		    			if (currentInventory != inventory)
    		    			{	
    		    				if (inventoryHasEmptySlot(inventory))
    		    				{
    		    					currentInventory = inventory;
        		    				return inventory;
    		    				}   		    				
    		    			}
        				}
        				else
        				{
        					if (currentInventory == inventory)
    		    			{	
        						currentInventory = inventory;
    		    				return inventory;
    		    			}
        				}
        			}
        			else
        			{
        				currentInventory = inventory;
        				return inventory;
        			}
        		}			
    		}
    	}
    	return null;
    }
    
    // Returns instance of IInventory at a given position
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
    
    // Sets the starting position for the quarry
    private void initPos()
    {
    	if (miningX == 100000000)
		{
			miningX = pos.getX() - 10;	
		}
		if (miningZ == 100000000)
		{
			miningZ = pos.getZ() - 10; 
		}
    }
    
    // Sound played when a block is mined
    private void playMiningSound(BlockPos p)
    {
    	IBlockState state = world.getBlockState(p);
    	Block b = state.getBlock();
    	if (b != Blocks.AIR)
    	{
    		world.playSound(null, p, b.getSoundType(state,world,p,null).getBreakSound(), SoundCategory.BLOCKS, 1.0f, 1);
    	}	
    }

    // Amount of energy stored
    private int getEnergyStored()
    {
    	if (energyStorage.getEnergyStored() > 0)
    	{   		
    		if (Loader.isModLoaded("ic2")) //If FE is in use, EU is disabled.
    		{
				((BasicSink) ic2EnergySink).setEnergyStored(0);
    			((BasicSink) ic2EnergySink).setCapacity(0);  			
    		}
    		energyStored = energyStorage.getEnergyStored()/4; //Return FE scaled to EU equivalent.		
    	}
    	else
    	{
    		if (Loader.isModLoaded("ic2"))
    		{
    			((BasicSink) ic2EnergySink).setCapacity(256000); //FE is not in use, IC2 is installed, so EU is enabled.
        		if (((BasicSink) ic2EnergySink).getEnergyStored() > 0)
        		{
        			energyStored = (int) ((BasicSink) ic2EnergySink).getEnergyStored(); //Return EU stored.
        		}
    		}  
    	}   
    	return energyStored;
    }
    
    // Attempts to consume energy and returns true if successful
    private boolean useEnergy(int amount)
    {
    	if (Loader.isModLoaded("ic2"))
		{
        	if (((BasicSink) ic2EnergySink).useEnergy(amount)) //Attempt to consume EU.
        	{
        		return true;
        	}   
        	else if (energyStorage != null)
        	{
    			if (energyStorage.getEnergyStored() >= amount*4)
        		{
        			energyStorage.useEnergy(amount*4); //Attempt to consume FE.
        			return true;
        		}                		
        	}
		}
    	else if (energyStorage != null)
    	{
			if (energyStorage.getEnergyStored() >= amount*4)
    		{
				energyStorage.useEnergy(amount*4); //Attempt to consume FE.
    			return true;
    		}                		
    	}
    	return false;
    }
    
    // Builds the quarry frame and tube 1 block below the quarry.
    private void buildLevelOne(BlockPos p)
    {
    	if (p.getX() == pos.getX() && p.getZ() == pos.getZ())
		{
			world.setBlockState(p, ProspectBlocks.quarry_tube.getDefaultState());
			world.getBlockState(p).getBlock().setHardness(1000.0f);
			quarryPositions.add(p);
		}
		else if (p.getX() == pos.getX() - 10 || p.getX() == pos.getX() + 10 || p.getZ() == pos.getZ() - 10 || p.getZ() == pos.getZ() + 10) 
		{
			world.setBlockState(p, ProspectBlocks.quarry_frame.getDefaultState());
			world.getBlockState(p).getBlock().setHardness(1000.0f);
			quarryPositions.add(p);
		}	
		else if (p.getX() == pos.getX() && p.getZ() != pos.getZ())
		{
			world.setBlockState(p, ProspectBlocks.quarry_frame.getDefaultState());
			world.getBlockState(p).getBlock().setHardness(1000.0f);
			quarryPositions.add(p);
		}
		else if (p.getX() != pos.getX() && p.getZ() == pos.getZ())
		{
			world.setBlockState(p, ProspectBlocks.quarry_frame.getDefaultState());
			world.getBlockState(p).getBlock().setHardness(1000.0f);
			quarryPositions.add(p);
		}
    }
    
    // Mines blocks, places quarry frames, tubes and drills
    private void mineBlock(Block b, BlockPos p)
    {
    	ItemStack stack = new ItemStack(Items.AIR);
		Item itemDropped = b.getItemDropped(b.getDefaultState(), new Random(), 0);
		if (itemDropped != Item.getItemFromBlock(b))
		{
			stack = new ItemStack(itemDropped); //Used for blocks that drop items; diamonds, coal, etc.
		}
		else
		{
			stack = new ItemStack(Item.getItemFromBlock(b)); //All other blocks.
		}	
		if (stack.getItem() != Items.AIR)
		{
			transferItemOut(stack); //Put the item collected in an adjacent storage container.
		}									
		
		if (level == 1)
		{
			buildLevelOne(p);
		}
		else
		{
			if (p.getX() == pos.getX() && p.getZ() == pos.getZ())
			{
				world.setBlockState(p, ProspectBlocks.quarry_tube.getDefaultState());
				world.getBlockState(p).getBlock().setHardness(1000.0f);
				quarryPositions.add(p);
			}
			else if (p.getX() == pos.getX() - 10 && p.getZ() == pos.getZ() - 10 || p.getX() == pos.getX() + 10 && p.getZ() == pos.getZ() + 10 || p.getX() == pos.getX() + 10 && p.getZ() == pos.getZ() - 10 || p.getX() == pos.getX() - 10 && p.getZ() == pos.getZ() + 10) 
			{
				world.setBlockState(p, ProspectBlocks.quarry_frame.getDefaultState());
				world.getBlockState(p).getBlock().setHardness(1000.0f);
				quarryPositions.add(p);
			}
			else
			{											
				if (level == 2) //Prevents the frame on level 1 from being destroyed by the quarry.
				{
					if (p.getX() != pos.getX() && p.getZ() != pos.getZ())
					{
						if (p.getX() != pos.getX() - 10 && p.getX() != pos.getX() + 10 && p.getZ() != pos.getZ() - 10 && p.getZ() != pos.getZ() + 10) 
						{
							miningBlock = true; //A block will be removed at this position.
						}
						else
						{
							miningBlock = false;
						}
					}
				}
				else
				{
					miningBlock = true; //A block will be removed at this position.
					if (world.getBlockState(p).getBlock() != Blocks.AIR)
					{
						//Play sound and spawn particles at each block mined.
						playMiningSound(p);
						WorldServer w = (WorldServer) world;
						w.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, p.getX(), p.getY(), p.getZ(), 1, 0, 0, 0, 1, null);
					}											
				}	
				world.setBlockState(p, ProspectBlocks.quarry_drill.getDefaultState()); //Move the drill down to the next level.
				world.getBlockState(p).getBlock().setHardness(1000.0f);
				quarryPositions.add(p);	
				if (miningBlock == true)
				{
					//Remove the old drill block.
					world.setBlockToAir(p.add(0,1,0));
					miningBlock = false;
				}											
			}	
		}
    }
    
    // Speed of quarry scales with the amount of power received.
    private void increaseQuarryTimer()
    {
    	if (getEnergyStored() >= 512)
		{
			useEnergy(512);
			quarryTimer += 32;
		}
		else if (getEnergyStored() >= 256)
		{
			useEnergy(256);
			quarryTimer += 16;
		}
		else if (getEnergyStored() >= 128)
		{
			useEnergy(128);
			quarryTimer += 8;
		}
		else if (getEnergyStored() >= 32)
		{
			useEnergy(32);
			quarryTimer += 4;
		}
		else if (getEnergyStored() >= 5)
		{
			useEnergy(5);
			quarryTimer += 2;
		}	
		else if (getEnergyStored() >= 1)
		{
			useEnergy(1);
			quarryTimer += 1;
		}
    }
    
	@Override
	public void update() 
	{
		if (!world.isRemote) //Everything is done on the server
		{		
			if (energyStorage.overloaded)
            {
				energyStorage.explode(world,pos);
            }
			else
			{
				if (quarryFinished == false) //The quarry has not hit bedrock.
				{	
					if (getEnergyStored() > 0)
					{		
						soundTimer++;
						if (soundTimer >= 60)
						{	
							//Looping sound effect played at the quarry block.
							world.playSound(null, pos, ProspectSounds.quarrySoundEvent,  SoundCategory.BLOCKS, 1.0f, 1);
							soundTimer = 0;
						}					
					}
										
					increaseQuarryTimer();
					
					if (quarryTimer >= 32)
					{
						initPos();
						if (miningX < pos.getX() + 11) //After one row on the Z axis is mined, X position will increase.
						{
							if (miningZ < pos.getZ() + 11) //Mines one block per tick until a row along the Z axis is complete.
							{
								BlockPos p = new BlockPos(miningX,pos.getY()-level,miningZ); //Level variable controls depth.
								Block b = world.getBlockState(p).getBlock();
								if (b != Blocks.BEDROCK) //Quarry stops when it hits bedrock.
								{
									mineBlock(b,p);														
								}
								else
								{
									quarryFinished = true; //The quarry hit bedrock.
								}
								miningZ++;
							}	
							else
							{
								//Reset the Z axis position and move on to the next row on the X axis.
								miningZ = pos.getZ() - 10;
								miningX++;
							}												
						}
						else
						{
							//Reset the X axis position and increase depth to move to the next layer.
							miningX = pos.getX() - 10;																						
							level++;
							markDirty();
						}					
						quarryTimer = 0;	
					}
				}
			}					
		}
	}
	
	@SuppressWarnings("unchecked")
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
