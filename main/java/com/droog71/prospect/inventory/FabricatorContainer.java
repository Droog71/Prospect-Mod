package com.droog71.prospect.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FabricatorContainer extends Container
{
    private final IInventory tilePrinter;
    private int printTime;
    private int totalPrintTime;
    private int energyCapacity;
    private int energyStored;

    public FabricatorContainer(InventoryPlayer playerInventory, IInventory printerInventory)
    {
        this.tilePrinter = printerInventory;
        this.addSlotToContainer(new Slot(printerInventory, 0, 56, 17));
        this.addSlotToContainer(new Slot(printerInventory, 2, 116, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
	public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tilePrinter);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.energyStored != this.tilePrinter.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tilePrinter.getField(0));
            }

            if (this.energyCapacity != this.tilePrinter.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tilePrinter.getField(1));
            }

            if (this.printTime != this.tilePrinter.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tilePrinter.getField(2));
            }
            
            if (this.totalPrintTime != this.tilePrinter.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tilePrinter.getField(3));
            }
        }
        this.energyStored = this.tilePrinter.getField(0);
        this.energyCapacity = this.tilePrinter.getField(1);
        this.printTime = this.tilePrinter.getField(2);
        this.totalPrintTime = this.tilePrinter.getField(3);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tilePrinter.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tilePrinter.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 38, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!itemstack1.isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 38, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 38, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}