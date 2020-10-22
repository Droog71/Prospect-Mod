package com.droog71.prospect.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SporeFilter extends ProspectItem
{   
    public SporeFilter(String name) 
    {
		super(name);
	}

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }
    
    @Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
    {
        return false;
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    	if(itemSlot < 9) //Spore filters must be placed in the hotbar when in use.
    	{  		
    		if (stack.getItemDamage() >= stack.getMaxDamage())
    		{    			   			
    			stack.shrink(1);
    		}
    		else
    		{  			
    			stack.getItem().setDamage(stack, stack.getItemDamage()+1);	
    		}
    		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    	}
    }
}