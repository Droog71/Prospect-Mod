package com.droog71.prospect.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DataTerminalGUI extends GuiScreen
{
	private int index;
	private float zoom = 1;
	
    private static final ResourceLocation[] pages = 
	{
		new ResourceLocation("prospect:textures/gui/data_terminal_intro_1.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_intro_2.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_spores_1.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_spores_2.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_spores_3.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_igc_1.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_igc_2.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_igc_3.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_machines.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_schematics.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_solar.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_zero_point_reactor.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_transformers.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_conduits.png"),
		new ResourceLocation("prospect:textures/gui/data_terminal_conveyor_tubes.png")
	};

    public DataTerminalGUI() 
    {
		
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		drawImage();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(0, width - 200, (int)(height * 0.75f), "Zoom"));
        this.buttonList.add(new GuiButton(1, width - 200, (int)(height * 0.8f), "Next"));
        this.buttonList.add(new GuiButton(2, width - 200, (int)(height * 0.85f), "Previous"));
        this.buttonList.add(new GuiButton(3, width - 200, (int)(height * 0.9f), "Close"));
    }
    
    public void drawImage()
    {
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(pages[index]);
        int size = (int) (height * zoom);
        int offset = (int) ((zoom - 1) * height);
        int height_offset = zoom == 1 ? 0 : (int) ((offset / 2) * -1);
        drawModalRectWithCustomSizedTexture((size / 2) - (size / 2), height_offset, 0, 0, size, size, size, size);
    }
    
    protected void actionPerformed(GuiButton button)
    {
    	if (button.id == 0)
    	{
    		zoom = zoom < 2 ? zoom + 0.25f : 1;
    	}
    	else if (button.id == 1)
    	{
    		if (index < pages.length - 1)
    		{
    			index++;
    		}
    		else
        	{
        		index = 0;
        	} 
    	}
    	else if (button.id == 2)
    	{
    		if (index > 0)
    		{
    			index--;
    		}
    		else
        	{
        		index = pages.length - 1;
        	} 
    	}
    	else
    	{
    		this.mc.displayGuiScreen((GuiScreen)null);
    	} 
    }
}