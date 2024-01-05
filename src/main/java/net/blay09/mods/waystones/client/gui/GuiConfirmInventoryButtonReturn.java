package net.blay09.mods.waystones.client.gui;

import net.blay09.mods.waystones.PlayerWaystoneHelper;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageFreeWarpReturn;
import net.blay09.mods.waystones.network.message.MessageTeleportToGlobal;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GuiConfirmInventoryButtonReturn extends GuiYesNo implements GuiYesNoCallback
{
    private final String waystoneName;

    public GuiConfirmInventoryButtonReturn()
    {
        this("");
    }

    public GuiConfirmInventoryButtonReturn(String targetWaystone)
    {
        super((result, id) -> {
            if (result) {
                if (targetWaystone.isEmpty()) {
                    NetworkHandler.channel.sendToServer(new MessageFreeWarpReturn());
                } else {
                    NetworkHandler.channel.sendToServer(new MessageTeleportToGlobal(targetWaystone));
                }
            }
            Minecraft.getMinecraft().displayGuiScreen(null);
        }, I18n.format("gui.waystones:confirmReturn"), "", 0);

        this.waystoneName = getWaystoneName(targetWaystone);
    }

    private static String getWaystoneName(String targetWaystone)
    {
        if (!targetWaystone.isEmpty()) {
            return TextFormatting.GRAY + I18n.format("gui.waystones:confirmReturn.boundTo", targetWaystone);
        }

        WaystoneEntry lastEntry = PlayerWaystoneHelper.getLastWaystone(FMLClientHandler.instance().getClientPlayerEntity());
        if (lastEntry != null) {
            return TextFormatting.GRAY + I18n.format("gui.waystones:confirmReturn.boundTo", lastEntry.getName());
        }

        return TextFormatting.GRAY + I18n.format("gui.waystones:confirmReturn.noWaystoneActive");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.waystoneName, this.width / 2, 100, 0xFFFFFF);
    }
}
