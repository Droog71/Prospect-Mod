package com.droog71.prospect.blocks.conveyor;

import java.util.Random;
import com.droog71.prospect.Prospect;
import com.droog71.prospect.blocks.ProspectBlock;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.tile_entity.ConveyorTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConveyorTube extends ProspectBlock
{
	public ConveyorTube(String name, Material material) 
	{
		super(name, material);
	}
	
    /**
     * Called when the block is right clicked by a player.
     */
    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof ConveyorTileEntity)
            {
            	if (worldIn.getStrongPower(pos) >= 15)
            	{
            		playerIn.openGui(Prospect.instance, 8, worldIn, pos.getX(), pos.getY(), pos.getZ());
            		return true;
            	}
            } 
        }
        return false;
    }
	
    @Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ProspectBlocks.conveyor_tube);
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new ConveyorTileEntity();
	}
}
