package com.droog71.prospect.tile_entity;

import com.droog71.prospect.blocks.gas.CopperPipe;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class CopperPipeTileEntity extends TileEntity implements ITickable
{	
	private CoolerTileEntity cooler;
	private int particleTimer;
	private BlockPos inputPos;
	private BlockPos outputPos;
	public boolean pressurized;
	public boolean initialPipe;
	public CopperPipeTileEntity input;
	public CopperPipeTileEntity output;
	
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
			if (initialPipe())
			{
				getGasFromCooler();
			}	
			else
			{
				if (input != null)
				{
					handleInput();	
				}
				else
				{
					pressurized = false;
				}								
			}
			
			if (initialPipe == true || input != null)
			{
				if (output == null)
				{
					connectPipe();
				}			
			}	
			
			if (output != null)
			{
				handleOutput();
			}
			
			if (pressurized == true)
			{
				particleTimer++;
				if (particleTimer >= 20)
				{
					particleTimer = 0;
					WorldServer w = (WorldServer) world;
					w.spawnParticle(EnumParticleTypes.DRIP_WATER, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 1, null);
				}
			}
			CopperPipe.setState(pressurized, world, pos);
		}
	}
    
	// This pipe is next to the cooler
    private boolean initialPipe()
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};	    	
    	for (BlockPos p : positions)
    	{
    		TileEntity t = world.getTileEntity(p);
    		if (t != null)
    		{
    			if (t instanceof CoolerTileEntity)
    			{
    				cooler = (CoolerTileEntity) t;
    				initialPipe = true;
					return true;
    			}
    		}		
    	}   
		initialPipe = false;
		cooler = null;
		return false;
    }    
    
    // If the cooler has power, pressurize the pipe
    private void getGasFromCooler()
    {
    	if (cooler != null)
    	{	
    		pressurized = cooler.energized;
    	}
    }
    
    // Find an adjacent pipe for output
    private void connectPipe()
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};	    	
    	for (BlockPos p : positions)
    	{
    		TileEntity t = world.getTileEntity(p);
    		if (t != null)
    		{
    			if (t instanceof CopperPipeTileEntity)
    			{
    				CopperPipeTileEntity otherPipe = (CopperPipeTileEntity) t;
    				if (otherPipe.initialPipe == false && otherPipe.input == null && otherPipe.output != this)
					{
    					if (output == null)
    					{
    						output = otherPipe;
    						outputPos = p;
    						otherPipe.input = this;
    						otherPipe.inputPos = pos;
    					}    					
					}    				
    			}
    		}		
    	}   	
    }

    // Checks if input is still valid
    private void handleInput()
    {
    	TileEntity t = world.getTileEntity(inputPos);
		if (t != null)
		{
			if (t instanceof CopperPipeTileEntity)
			{
				input = (CopperPipeTileEntity) t;
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
    
    // Outputs gas if output pipe is still valid
    private void handleOutput()
    {
    	TileEntity t = world.getTileEntity(outputPos);
		if (t != null)
		{
			if (t instanceof CopperPipeTileEntity)
			{
				output = (CopperPipeTileEntity) t;
				output.pressurized = pressurized;
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