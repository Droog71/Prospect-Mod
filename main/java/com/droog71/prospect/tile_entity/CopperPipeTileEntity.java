package com.droog71.prospect.tile_entity;

import com.droog71.prospect.blocks.energy.ZeroPointCooler;
import com.droog71.prospect.blocks.gas.CopperPipe;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class CopperPipeTileEntity extends TileEntity implements ITickable
{	
	private ZeroPointCooler cooler;
	
	@Override
    public void onLoad() 
	{
		super.onLoad();
	}
	 
	@Override
    public void invalidate() 
    {
		super.invalidate(); // this is important for mc!
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
 
    private boolean initialPipe(CopperPipe pipe)
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};	    	
    	for (BlockPos p : positions)
    	{
    		Block b = world.getBlockState(p).getBlock();
    		if (b != null)
    		{
    			if (b instanceof ZeroPointCooler)
    			{
    				pipe.initialPipe = true;
    				cooler = (ZeroPointCooler) b;
					return pipe.initialPipe;
    			}
    		}		
    	}   
		pipe.initialPipe = false;
		cooler = null;
		return pipe.initialPipe;
    }    
    
    private void getGasFromCooler(CopperPipe pipe)
    {
    	if (cooler != null)
    	{	
    		pipe.pressurized = cooler.powered;
    	}
    }
    
    private void outputGas(CopperPipe pipe,boolean pressure)
    {
    	BlockPos[] positions = {pos.add(0,1,0),pos.add(0,-1,0),pos.add(1,0,0),pos.add(-1,0,0),pos.add(0,0,1),pos.add(0,0,-1)};	    	
    	for (BlockPos p : positions)
    	{
    		Block b = world.getBlockState(p).getBlock();
    		if (b != null)
    		{
    			if (b instanceof CopperPipe)
    			{
    				CopperPipe otherPipe = (CopperPipe) b;
    				if (otherPipe.pressurized == false && otherPipe.initialPipe == false)
					{
    					if (pipe.output == null && otherPipe.input == null && otherPipe.output != pipe)
    					{
    						pipe.output = otherPipe;	
    						otherPipe.input = pipe;
    					}
    					else
    					{
    						pipe.output.pressurized = pressure;
    					}
					}
    			}
    		}		
    	}   	
    }
    
	@Override
	public void update() 
	{
		if (!world.isRemote) //Everything is done on the server.
		{
			CopperPipe pipe = (CopperPipe) world.getBlockState(pos).getBlock();
			if (initialPipe(pipe))
			{
				getGasFromCooler(pipe);
			}		
			outputGas(pipe,pipe.pressurized);
		}
	}
}