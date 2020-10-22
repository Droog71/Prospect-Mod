package com.droog71.prospect.event;

import com.droog71.prospect.config.ConfigHandler;
import com.droog71.prospect.init.ProspectItems;
import com.droog71.prospect.init.ProspectPotions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public enum FMLEventHandler 
{
    INSTANCE;
	private static final String NBT_KEY = "prospect.firstjoin";
	
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        NBTTagCompound data = event.player.getEntityData();
        NBTTagCompound persistent;
        if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) 
        {
            data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
        } 
        else 
        {
            persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }
        if (!persistent.hasKey(NBT_KEY)) //If this is the first time onPlayerJoin is called for this player, equip armor and spore filter.
        {     
        	event.player.inventory.addItemStackToInventory(new ItemStack(ProspectItems.data_terminal));
        	if (!hasArmor(event.player))
        	{
        		event.player.inventory.addItemStackToInventory(new ItemStack(ProspectItems.filter));
                event.player.inventory.setInventorySlotContents(36, new ItemStack(ProspectItems.boots));
                event.player.inventory.setInventorySlotContents(37, new ItemStack(ProspectItems.pants));
                event.player.inventory.setInventorySlotContents(38, new ItemStack(ProspectItems.suit));
                event.player.inventory.setInventorySlotContents(39, new ItemStack(ProspectItems.helmet));
        	}	
        	else
        	{
        		ConfigHandler.disableToxicSpores();
        	}
        	persistent.setBoolean(NBT_KEY, true);
        }
        event.player.addPotionEffect(new PotionEffect(ProspectPotions.spore, 1000000, 0, false, false)); //Enable toxic spore effects.
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) 
    {
        event.player.addPotionEffect(new PotionEffect(ProspectPotions.spore, 1000000, 0, false, false)); //Enable toxic spore effects.
    }
    
    //Returns true if the mod was added to a pre-existing world in which the player already has armor.
    private boolean hasArmor(EntityPlayer player)
    {
    	for (int i = 36; i <= 39; i++)
    	{
    		if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY)
        	{
        		return true;
        	}
    	}
    	return false;
    }
}