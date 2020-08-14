package com.droog71.prospect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import com.droog71.prospect.blocks.energy.Purifier;
import com.droog71.prospect.config.ConfigHandler;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;

public class PotionSpore extends PotionProspect
{
	private SporeArmorList sporeArmorList = new SporeArmorList();
	private int hurtTimer;
	private int message;
	
	public PotionSpore() 
	{
		super("Spores", true, 0x26ADFF);
	}

	@Override
	public boolean isReady(int duration, int amplifier) 
	{
		return true;
	}

	@Override
	public boolean shouldRender(PotionEffect effect) 
	{ 
		return false; 
	}
	
	@Override
	public boolean shouldRenderHUD(PotionEffect effect)
    {
        return false;
    }

	@Override
	public void performEffect(@Nonnull EntityLivingBase living, int amplified) 
	{
		EntityPlayer player = (EntityPlayer) living;
		if (player != null && ConfigHandler.toxicSporesEnabled())
		{					
			if (!nearPurifier(player) && (!filterInstalled(player) || !wearingProtectiveArmor(player))) 
			{
				hurtTimer++;
				if (hurtTimer >= 1200)
				{
					player.attackEntityFrom(DamageSource.GENERIC, 0.5F);									
				}	
				if (hurtTimer >= 1220)
				{
					sendMessage(player);
					hurtTimer = 0;	
				}
			}  		
			else
			{
				message = 0;
				hurtTimer = 0;	
			}
		}			
	}
	
	// Check if the player is near an energized purifier block.
	private boolean nearPurifier(EntityPlayer player) throws NoSuchElementException
	{
		BlockPos pos = player.getPosition();
		BlockPos corner_1 = pos.add(-20, -20, -20);
		BlockPos corner_2 = pos.add(20, 20, 20);
		Iterable<BlockPos> allBlocks = BlockPos.getAllInBox(corner_1, corner_2);
		Iterator<BlockPos> iter = allBlocks.iterator();
		while(iter.hasNext()) 
		{
			BlockPos found = iter.next();
			if (player.world.getBlockState(found).getBlock() == ProspectBlocks.purifier)
			{				
				Purifier purifier = (Purifier) player.world.getBlockState(found).getBlock();
				if (purifier.powered == true)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	// Check if the player has a spore filter in their hotbar.
	private boolean filterInstalled(EntityPlayer player)
	{
		for (int i = 0; i < 9; i++) 
		{
			if (player.inventory.getStackInSlot(i).getItem() == ProspectItems.filter)
			{
				return true;
			}
		}
		return false;
	}
	
	// Check if the player's armor is protecting them from toxic spores.
	private boolean wearingProtectiveArmor(EntityPlayer player)
	{
		ArrayList<ItemStack> playerArmorList = new ArrayList<ItemStack>();
		for (int slot=36; slot<40; slot++)
		{
			playerArmorList.add(player.inventory.getStackInSlot(slot));
		}				
		for (ItemStack armorItem : playerArmorList)
		{
			if (!sporeArmorList.protectiveArmorList().contains(armorItem.getItem()))
			{
				return false;
			}
		}
		return true;
	}

	private void sendMessage(EntityPlayer player)
	{
		if (message == 0)
		{
			player.sendMessage(new TextComponentString("Received poisonous spore damage!"));
		}
		if (message == 1)
		{
			player.sendMessage(new TextComponentString("You need protective armor and a spore filter in your hotbar!"));
		}
		if (message == 2)
		{
			player.sendMessage(new TextComponentString("Build a purifier to create a protected area!"));						
		}
		if (message < 2)
		{
			message++;
		}
		else
		{
			message = 0;
		}
	}
}