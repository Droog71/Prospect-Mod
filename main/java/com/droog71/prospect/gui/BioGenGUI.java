package com.droog71.prospect.gui;

import com.droog71.prospect.inventory.BioGenContainer;
import com.droog71.prospect.tile_entity.BioGenTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BioGenGUI extends GuiContainer
{
    private static final ResourceLocation BIO_GEN_GUI_TEXTURES = new ResourceLocation("prospect:textures/gui/bio_gen.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final IInventory tileBioGen;

    public BioGenGUI(InventoryPlayer playerInv, IInventory bioGenInv)
    {
        super(new BioGenContainer(playerInv, bioGenInv));
        playerInventory = playerInv;
        tileBioGen = bioGenInv;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = tileBioGen.getName();
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BIO_GEN_GUI_TEXTURES);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        if (BioGenTileEntity.isEnergized(tileBioGen))
        {
            int k = getPowerScaled(13);
            drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int h = getBurnProgressScaled(16);
        drawTexturedModalRect(i + 79, j + 34, 176, 14, 24, h + 1);
    }

    private int getBurnProgressScaled(int pixels)
    {
        int i = tileBioGen.getField(2);
        int j = tileBioGen.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 16;
    }

    private int getPowerScaled(int pixels)
    {
        return tileBioGen.getField(0) * pixels / tileBioGen.getField(1);
    }
}