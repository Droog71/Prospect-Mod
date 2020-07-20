package com.droog71.prospect.potion;

import com.droog71.prospect.Prospect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionProspect extends Potion 
{
	public PotionProspect(String name, boolean badEffect, int color) 
	{
		super(badEffect, color);
		setRegistryName(new ResourceLocation(Prospect.MODID, name));
		setPotionName(name);
	}

	public boolean hasEffect(EntityLivingBase entity) 
	{
		return hasEffect(entity, this);
	}

	public boolean hasEffect(EntityLivingBase entity, Potion potion) 
	{
		return entity.getActivePotionEffect(potion) != null;
	}
}