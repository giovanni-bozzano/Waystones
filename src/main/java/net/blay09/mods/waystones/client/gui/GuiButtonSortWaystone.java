package net.blay09.mods.waystones.client.gui;

import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiButtonSortWaystone extends GuiButtonExt
{
    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    private final GuiButtonWaystoneEntry parentButton;
    private final int sortDir;

    public GuiButtonSortWaystone(int id, int x, int y, GuiButtonWaystoneEntry parentButton, int sortDir)
    {
        super(id, x, y, "");
        this.width = 11;
        this.height = 7;
        this.parentButton = parentButton;
        this.sortDir = sortDir;
    }

    public WaystoneEntry getWaystone()
    {
        return this.parentButton.getWaystone();
    }

    public int getSortDir()
    {
        return this.sortDir;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
    {
        if (this.visible && mouseY >= this.parentButton.y && mouseY < this.parentButton.y + this.parentButton.height) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (this.hovered) {
                Gui.drawModalRectWithCustomSizedTexture(this.x - 5, this.y - 5 - (this.sortDir == 1 ? 15 : 0), 96f - (this.sortDir == 1 ? 32f : 0f), 32f, 32, 32, 256f, 256f);
            } else {
                Gui.drawModalRectWithCustomSizedTexture(this.x - 5, this.y - 5 - (this.sortDir == 1 ? 15 : 0), 96f - (this.sortDir == 1 ? 32f : 0f), 0f, 32, 32, 256f, 256f);
            }
        }
    }
}
