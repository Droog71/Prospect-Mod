package com.droog71.prospect.inventory;

import com.droog71.prospect.init.ProspectItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ReplicatorContainer extends Container
{
    private final IInventory tileReplicator;
    private int replicateTime;
    private int totalreplicateTime;
    private int replicatorSpendTime;
    private int currentCreditSpendTime;
    private int energyCapacity;
    private int energyStored;

    public ReplicatorContainer(InventoryPlayer playerInventory, IInventory replicatorInventory)
    {
        this.tileReplicator = replicatorInventory;
        this.addSlotToContainer(new Slot(replicatorInventory, 0, 56, 17));
        this.addSlotToContainer(new Slot(replicatorInventory, 1, 56, 53));
        this.addSlotToContainer(new Slot(replicatorInventory, 2, 116, 35));

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
        listener.sendAllWindowProperties(this, this.tileReplicator);
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

            if (this.replicateTime != this.tileReplicator.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileReplicator.getField(2));
            }

            if (this.replicatorSpendTime != this.tileReplicator.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileReplicator.getField(0));
            }

            if (this.currentCreditSpendTime != this.tileReplicator.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileReplicator.getField(1));
            }

            if (this.totalreplicateTime != this.tileReplicator.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileReplicator.getField(3));
            }
            
            if (this.energyCapacity != this.tileReplicator.getField(4))
            {
                icontainerlistener.sendWindowProperty(this, 4, this.tileReplicator.getField(4));
            }
            
            if (this.energyStored != this.tileReplicator.getField(5))
            {
                icontainerlistener.sendWindowProperty(this, 5, this.tileReplicator.getField(5));
            }
        }

        this.replicateTime = this.tileReplicator.getField(2);
        this.replicatorSpendTime = this.tileReplicator.getField(0);
        this.currentCreditSpendTime = this.tileReplicator.getField(1);
        this.totalreplicateTime = this.tileReplicator.getField(3);
        this.energyCapacity = this.tileReplicator.getField(4);
        this.energyStored = this.tileReplicator.getField(5);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileReplicator.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileReplicator.isUsableByPlayer(playerIn);
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
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (itemstack1.getItem() == ProspectItems.credit)
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return ItemStack.EMPTY;
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }               
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
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