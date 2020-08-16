package com.droog71.prospect.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;

public class ConveyorTubeContainer extends ContainerChest
{
	public ConveyorTubeContainer(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) 
	{
		super(playerInventory, chestInventory, player);
	}
}
