package TFC.GUI;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import TFC.*;
import TFC.Containers.ContainerQuern;
import TFC.TileEntities.TileEntityQuern;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.World;

public class GuiQuern extends GuiContainer
{
	TileEntityQuern table;
	
    public GuiQuern(InventoryPlayer inventoryplayer, TileEntityQuern wb, World world, int i, int j, int k)
    {
        super(new ContainerQuern(inventoryplayer, wb,world, i, j, k));
        table = wb;
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();
    }

    protected void drawGuiContainerForegroundLayer()
    {
    	//drawCenteredString(fontRenderer,"Log Pile", 87, 6, 0x000000);
        //fontRenderer.drawString("Log Pile", 28, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/bioxx/querngui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }
    
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k)
    {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }
}
