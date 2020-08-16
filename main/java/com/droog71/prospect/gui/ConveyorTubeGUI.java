package com.droog71.prospect.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;

public class ConveyorTubeGUI extends GuiChest
{
	public ConveyorTubeGUI(IInventory upperInv, IInventory lowerInv) 
	{
		super(upperInv, lowerInv);
	}
}
