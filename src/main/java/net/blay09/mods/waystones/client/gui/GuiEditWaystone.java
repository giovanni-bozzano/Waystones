package net.blay09.mods.waystones.client.gui;

import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageEditWaystone;
import net.blay09.mods.waystones.util.WaystoneActivatedEvent;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiEditWaystone extends GuiScreen
{
    private final WaystoneEntry tileWaystone;
    private GuiTextField textField;
    private GuiButton btnDone;
    private GuiCheckBox chkGlobal;
    private final boolean fromSelectionGui;

    public GuiEditWaystone(WaystoneEntry waystone, boolean fromSelectionGui)
    {
        this.tileWaystone = waystone;
        this.fromSelectionGui = fromSelectionGui;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        String oldText = this.tileWaystone.getName();
        if (this.textField != null) {
            oldText = this.textField.getText();
        }

        this.textField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 20, 200, 20);
        this.textField.setMaxStringLength(128);
        this.textField.setText(oldText);
        this.textField.setFocused(true);
        this.btnDone = new GuiButton(0, this.width / 2, this.height / 2 + 10, 100, 20, I18n.format("gui.done"));
        this.buttonList.add(this.btnDone);

        this.chkGlobal = new GuiCheckBox(1, this.width / 2 - 100, this.height / 2 + 15, " " + I18n.format("gui.waystones:editWaystone.isGlobal"), this.tileWaystone.isGlobal());
        if (!WaystoneConfig.general.allowEveryoneGlobal && (!Minecraft.getMinecraft().player.capabilities.isCreativeMode)) {
            this.chkGlobal.visible = false;
        }

        this.buttonList.add(this.chkGlobal);

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button == this.btnDone) {
            if (this.textField.getText().isEmpty()) {
                this.textField.setFocused(true);
                return;
            }

            NetworkHandler.channel.sendToServer(new MessageEditWaystone(this.tileWaystone.getPos(), this.textField.getText(), this.chkGlobal.isChecked(), this.fromSelectionGui));

            if (!this.fromSelectionGui) {
                FMLClientHandler.instance().getClientPlayerEntity().closeScreen();
            }

            if (this.tileWaystone.getName().isEmpty()) {
                MinecraftForge.EVENT_BUS.post(new WaystoneActivatedEvent(this.textField.getText(), this.tileWaystone.getPos(), this.tileWaystone.getDimensionId()));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.textField.mouseClicked(mouseX, mouseY, mouseButton)) {
            this.mouseHandled = true;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == Keyboard.KEY_RETURN) {
            this.actionPerformed(this.btnDone);
            return;
        }

        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            this.keyHandled = true;
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen()
    {
        this.textField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawWorldBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRenderer.drawString(I18n.format("gui.waystones:editWaystone.enterName"), this.width / 2 - 100, this.height / 2 - 35, 0xFFFFFF);
        this.textField.drawTextBox();
    }
}
