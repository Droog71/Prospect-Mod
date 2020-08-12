package com.droog71.prospect.blocks.energy;

import java.util.Random;
import com.droog71.prospect.Prospect;
import com.droog71.prospect.blocks.ProspectBlockContainer;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.tile_entity.BioGenTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BioFuelGenerator extends ProspectBlockContainer
{	
	private static boolean keepInventory;
	
    public BioFuelGenerator(String name, Material material)
    {   	
    	super(name, material);
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;
        
        if (active)
        {
            worldIn.setBlockState(pos, ProspectBlocks.bio_fuel_generator_running.getDefaultState());
            worldIn.setBlockState(pos, ProspectBlocks.bio_fuel_generator_running.getDefaultState());
        }
        else
        {
            worldIn.setBlockState(pos, ProspectBlocks.bio_fuel_generator.getDefaultState());
            worldIn.setBlockState(pos, ProspectBlocks.bio_fuel_generator.getDefaultState());
        }

        keepInventory = true;
        
        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
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
        return Item.getItemFromBlock(ProspectBlocks.bio_fuel_generator);
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

            if (tileentity instanceof BioGenTileEntity)
            {
                playerIn.openGui(Prospect.instance, 6, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new BioGenTileEntity();
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (!keepInventory)
        {
	        TileEntity tileentity = worldIn.getTileEntity(pos);
	
	        if (tileentity instanceof BioGenTileEntity)
	        {
	            InventoryHelper.dropInventoryItems(worldIn, pos, (BioGenTileEntity)tileentity);
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }   
        }
    	super.breakBlock(worldIn, pos, state);
    }

    @Override
	public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(ProspectBlocks.bio_fuel_generator));
    }
}