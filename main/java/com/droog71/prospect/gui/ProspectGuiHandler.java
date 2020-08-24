package com.droog71.prospect.gui;

import com.droog71.prospect.inventory.BioGenContainer;
import com.droog71.prospect.inventory.ConveyorTubeContainer;
import com.droog71.prospect.inventory.ExtruderContainer;
import com.droog71.prospect.inventory.LaunchPadContainer;
import com.droog71.prospect.inventory.PressContainer;
import com.droog71.prospect.inventory.FabricatorContainer;
import com.droog71.prospect.inventory.ReplicatorContainer;
import com.droog71.prospect.inventory.ZeroPointContainer;
import com.droog71.prospect.tile_entity.BioGenTileEntity;
import com.droog71.prospect.tile_entity.ConveyorTileEntity;
import com.droog71.prospect.tile_entity.ExtruderTileEntity;
import com.droog71.prospect.tile_entity.FabricatorTileEntity;
import com.droog71.prospect.tile_entity.LaunchPadTileEntity;
import com.droog71.prospect.tile_entity.PressTileEntity;
import com.droog71.prospect.tile_entity.ReplicatorTileEntity;
import com.droog71.prospect.tile_entity.ZeroPointTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ProspectGuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
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
		if (ID == 6)
		{
			return new BioGenContainer(player.inventory, (BioGenTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 7)
		{
			return new ZeroPointContainer(player.inventory, (ZeroPointTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 8)
		{
			return new ConveyorTubeContainer(player.inventory, (ConveyorTileEntity)world.getTileEntity(new BlockPos(x,y,z)), player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if (ID == 1)
		{
			return new FabricatorGUI(player.inventory, (FabricatorTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
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
		if (ID == 6)
		{
			return new BioGenGUI(player.inventory, (BioGenTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 7)
		{
			return new ZeroPointReactorGUI(player.inventory, (ZeroPointTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		if (ID == 8)
		{
			return new ConveyorTubeGUI(player.inventory, (ConveyorTileEntity)world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

}
