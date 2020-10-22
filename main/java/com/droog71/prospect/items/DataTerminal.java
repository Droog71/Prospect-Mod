package com.droog71.prospect.items;

import com.droog71.prospect.Prospect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class DataTerminal extends ProspectItem
{   
    public DataTerminal(String name) 
    {
		super(name);
	}

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	ItemStack itemstack = playerIn.getHeldItem(handIn);
    	playerIn.openGui(Prospect.instance, 9, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
    	return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }
}