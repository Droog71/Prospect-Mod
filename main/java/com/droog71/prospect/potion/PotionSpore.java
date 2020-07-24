package com.droog71.prospect.potion;

import net.machinemuse.powersuits.item.armor.ItemElectricArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import techguns.TGArmors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;

import com.droog71.prospect.armor.ProspectArmor;
import com.droog71.prospect.blocks.energy.Purifier;
import com.droog71.prospect.config.ConfigHandler;
import com.droog71.prospect.init.ProspectBlocks;
import com.droog71.prospect.init.ProspectItems;

import ic2.core.platform.registry.Ic2Items;


public class PotionSpore extends PotionProspect
{
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
	
	private boolean isArmorProtective(ItemStack stack) //Checks if the player's armor will protect them from toxic spores.
	{
		if (stack.getItem() instanceof ProspectArmor)
		{
			return true;
		}
		if (Loader.isModLoaded("ic2"))
		{
			if (stack == Ic2Items.hazmatHelmet || stack == Ic2Items.hazmatChest || stack == Ic2Items.hazmatLeggings || stack == Ic2Items.hazmatBoots)
			{
				return true;
			}
		}
		if (Loader.isModLoaded("techguns"))
		{
			if (stack.getItem() == TGArmors.hazmat_Helmet || stack.getItem() == TGArmors.hazmat_Chestplate || stack.getItem() == TGArmors.hazmat_Leggings || stack.getItem() == TGArmors.hazmat_Boots)
			{
				return true;
			}
			if (stack.getItem() == TGArmors.t3_miner_Helmet || stack.getItem() == TGArmors.t3_miner_Chestplate || stack.getItem() == TGArmors.t3_miner_Leggings || stack.getItem() == TGArmors.t3_miner_Boots)
			{
				return true;
			}
			if (stack.getItem() == TGArmors.steam_Helmet || stack.getItem() == TGArmors.steam_Chestplate || stack.getItem() == TGArmors.steam_Leggings || stack.getItem() == TGArmors.steam_Boots)
			{
				return true;
			}
			if (stack.getItem() == TGArmors.t3_power_Helmet || stack.getItem() == TGArmors.t3_power_Chestplate || stack.getItem() == TGArmors.t3_power_Leggings || stack.getItem() == TGArmors.t3_power_Boots)
			{
				return true;
			}
			if (stack.getItem() == TGArmors.t4_power_Helmet || stack.getItem() == TGArmors.t4_power_Chestplate || stack.getItem() == TGArmors.t4_power_Leggings || stack.getItem() == TGArmors.t4_power_Boots)
			{
				return true;
			}
		}
		if (Loader.isModLoaded("powersuits"))
		{
			if (stack.getItem() instanceof ItemElectricArmor)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void performEffect(@Nonnull EntityLivingBase living, int amplified) 
	{
		EntityPlayer player = (EntityPlayer) living;
		if (player != null && ConfigHandler.toxicSporesEnabled())
		{
			boolean isPoisoned = false;
			boolean nearPurifier = false;
			BlockPos pos = player.getPosition();
			BlockPos corner_1 = pos.add(-20, -20, -20);
			BlockPos corner_2 = pos.add(20, 20, 20);
			Iterable<BlockPos> allBlocks = BlockPos.getAllInBox(corner_1, corner_2);
			Iterator<BlockPos> iter = allBlocks.iterator();		
			while(iter.hasNext()) //Check if the player is near an energized purifier block which will protect them from toxic spores.
			{
				try
				{
					BlockPos found = iter.next();
					if (player.world.getBlockState(found).getBlock() == ProspectBlocks.purifier)
					{				
						Purifier purifier = (Purifier) player.world.getBlockState(found).getBlock();
						if (purifier.powered == true)
						{
							nearPurifier = true;
						}
					}
				}
				catch(NoSuchElementException e)
				{
					//NOOP
				}
			}
			if (!nearPurifier)
			{
				boolean filterInstalled = false;
				int i = 0;
				while (i < 9) //Check if the player has a spore filter in their hotbar.
				{
					if (player.inventory.getStackInSlot(i).getItem() == ProspectItems.filter)
					{
						filterInstalled = true;
					}
					i++;
				}
				if (!filterInstalled) 
				{
					isPoisoned = true; //The player is not near a purifier and has no spore filter in their hotbar and is therefore vulnerable to toxic spores.
				}  
				ArrayList<ItemStack> armorList = new ArrayList<ItemStack>();
				for (int slot=36; slot<40; slot++)
				{
					armorList.add(player.inventory.getStackInSlot(slot));
				}				
				for (ItemStack armorItem : armorList)
				{
					if (!(isArmorProtective(armorItem)))
					{
						isPoisoned = true; //The player is not near a purifier and is not wearing protective armor and is therefore vulnerable to toxic spores.
					}
				}
			}			
			if (isPoisoned)
			{
				hurtTimer++;
				if (hurtTimer >= 200)
				{
					player.attackEntityFrom(DamageSource.GENERIC, 0.5F);											
				}	
				if (hurtTimer >= 240)
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
}