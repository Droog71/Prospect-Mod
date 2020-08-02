package com.droog71.prospect.gui;

import com.droog71.prospect.inventory.ExtruderContainer;
import com.droog71.prospect.inventory.LaunchPadContainer;
import com.droog71.prospect.inventory.PressContainer;
import com.droog71.prospect.inventory.FabricatorContainer;
import com.droog71.prospect.inventory.ReplicatorContainer;
import com.droog71.prospect.tile_entity.ExtruderTileEntity;
import com.droog71.prospect.tile_entity.FabricatorTileEntity;
import com.droog71.prospect.tile_entity.LaunchPadTileEntity;
import com.droog71.prospect.tile_entity.PressTileEntity;
import com.droog71.prospect.tile_entity.ReplicatorTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ProspectGuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if (ID == 0)
		{
			return null;
		}
		if (ID == 1)
		{
			return new FabricatorContainer(player.inventory, (FabricatorTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 2)
		{
			return new ReplicatorContainer(player.inventory, (ReplicatorTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 3)
		{
			return new LaunchPadContainer(player.inventory, (LaunchPadTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 4)
		{
			return new ExtruderContainer(player.inventory, (ExtruderTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 5)
		{
			return new PressContainer(player.inventory, (PressTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if (ID == 0)
		{
			return null;
		}
		if (ID == 1)
		{
			return new PrinterGUI(player.inventory, (FabricatorTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 2)
		{
			return new ReplicatorGUI(player.inventory, (ReplicatorTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 3)
		{
			return new LaunchPadGUI(player.inventory, (LaunchPadTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 4)
		{
			return new ExtruderGUI(player.inventory, (ExtruderTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 5)
		{
			return new PressGUI(player.inventory, (PressTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

}
