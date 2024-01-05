package net.blay09.mods.waystones.client.gui;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageRemoveWaystone;
import net.blay09.mods.waystones.network.message.MessageSortWaystone;
import net.blay09.mods.waystones.network.message.MessageTeleportToWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.IOException;

public class GuiWaystoneList extends GuiScreen
{
    private final WarpMode warpMode;
    private final EnumHand hand;
    private final WaystoneEntry fromWaystone;
    private WaystoneEntry[] entries;
    private GuiButton btnPrevPage;
    private GuiButton btnNextPage;
    private int pageOffset;
    private int headerY;
    private boolean isLocationHeaderHovered;
    private int buttonsPerPage;

    public GuiWaystoneList(WaystoneEntry[] entries, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone)
    {
        this.entries = entries;
        this.warpMode = warpMode;
        this.hand = hand;
        this.fromWaystone = fromWaystone;
    }

    @Override
    public void initGui()
    {
        this.btnPrevPage = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 40, 95, 20, I18n.format("gui.waystones:warpStone.previousPage"));
        this.buttonList.add(this.btnPrevPage);

        this.btnNextPage = new GuiButton(1, this.width / 2 + 5, this.height / 2 + 40, 95, 20, I18n.format("gui.waystones:warpStone.nextPage"));
        this.buttonList.add(this.btnNextPage);

        this.updateList();
    }

    public void updateList()
    {
        final int maxContentHeight = (int) (this.height * 0.8f);
        final int headerHeight = 40;
        final int footerHeight = 25;
        final int entryHeight = 25;
        final int maxButtonsPerPage = (maxContentHeight - headerHeight - footerHeight) / entryHeight;

        this.buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, this.entries.length));
        final int contentHeight = headerHeight + this.buttonsPerPage * entryHeight + footerHeight;
        this.headerY = this.height / 2 - contentHeight / 2;

        this.btnPrevPage.enabled = this.pageOffset > 0;
        this.btnNextPage.enabled = this.pageOffset < (this.entries.length - 1) / this.buttonsPerPage;

        this.buttonList.removeIf(button -> button instanceof GuiButtonWaystoneEntry || button instanceof GuiButtonSortWaystone || button instanceof GuiButtonRemoveWaystone);

        int id = 2;
        int y = headerHeight;
        for (int i = 0; i < this.buttonsPerPage; i++) {
            int entryIndex = this.pageOffset * this.buttonsPerPage + i;
            if (entryIndex >= 0 && entryIndex < this.entries.length) {
                GuiButtonWaystoneEntry btnWaystone = new GuiButtonWaystoneEntry(id, this.width / 2 - 100, this.headerY + y, this.entries[entryIndex], this.warpMode);
                if (this.entries[entryIndex].equals(this.fromWaystone)) {
                    btnWaystone.enabled = false;
                }
                this.buttonList.add(btnWaystone);
                id++;

                GuiButtonSortWaystone sortUp = new GuiButtonSortWaystone(id, this.width / 2 + 108, this.headerY + y + 2, btnWaystone, -1);
                if (entryIndex == 0) {
                    sortUp.visible = false;
                }
                this.buttonList.add(sortUp);
                id++;

                GuiButtonSortWaystone sortDown = new GuiButtonSortWaystone(id, this.width / 2 + 108, this.headerY + y + 11, btnWaystone, 1);
                if (entryIndex == this.entries.length - 1) {
                    sortDown.visible = false;
                }
                this.buttonList.add(sortDown);
                id++;

                y += 22;
            }
        }

        this.btnPrevPage.y = this.headerY + headerHeight + this.buttonsPerPage * 22 + (this.entries.length > 0 ? 10 : 0);
        this.btnNextPage.y = this.headerY + headerHeight + this.buttonsPerPage * 22 + (this.entries.length > 0 ? 10 : 0);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button == this.btnNextPage) {
            this.pageOffset = GuiScreen.isShiftKeyDown() ? (this.entries.length - 1) / this.buttonsPerPage : this.pageOffset + 1;
            this.updateList();
        } else if (button == this.btnPrevPage) {
            this.pageOffset = GuiScreen.isShiftKeyDown() ? 0 : this.pageOffset - 1;
            this.updateList();
        } else if (button instanceof GuiButtonWaystoneEntry) {
            NetworkHandler.channel.sendToServer(new MessageTeleportToWaystone(((GuiButtonWaystoneEntry) button).getWaystone(), this.warpMode, this.hand, this.fromWaystone));
            this.mc.displayGuiScreen(null);
        } else if (button instanceof GuiButtonSortWaystone) {
            WaystoneEntry waystoneEntry = ((GuiButtonSortWaystone) button).getWaystone();
            int index = ArrayUtils.indexOf(this.entries, waystoneEntry);
            int sortDir = ((GuiButtonSortWaystone) button).getSortDir();
            int otherIndex = index + sortDir;
            if (GuiScreen.isShiftKeyDown()) {
                otherIndex = sortDir == -1 ? 0 : this.entries.length - 1;
            }
            if (index == -1 || otherIndex < 0 || otherIndex >= this.entries.length) {
                return;
            }

            WaystoneEntry swap = this.entries[index];
            this.entries[index] = this.entries[otherIndex];
            this.entries[otherIndex] = swap;
            NetworkHandler.channel.sendToServer(new MessageSortWaystone(index, otherIndex));
            this.updateList();
        } else if (button instanceof GuiButtonRemoveWaystone) {
            WaystoneEntry waystoneEntry = ((GuiButtonRemoveWaystone) button).getWaystone();
            int index = ArrayUtils.indexOf(this.entries, waystoneEntry);
            this.entries = ArrayUtils.remove(this.entries, index);
            NetworkHandler.channel.sendToServer(new MessageRemoveWaystone(index));
            this.updateList();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (this.isLocationHeaderHovered && this.fromWaystone != null) {
            Waystones.proxy.openWaystoneSettings(this.mc.player, this.fromWaystone, true);

            this.mouseHandled = true;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawWorldBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.waystones:warpStone.selectDestination"), this.width / 2, this.headerY + (this.fromWaystone != null ? 20 : 0), 0xFFFFFF);
        if (this.fromWaystone != null) {
            this.drawLocationHeader(this.fromWaystone.getName(), mouseX, mouseY, this.width / 2, this.headerY);
        }

        if (this.entries.length == 0) {
            this.drawCenteredString(this.fontRenderer, TextFormatting.RED + I18n.format("waystones:scrollNotBound"), this.width / 2, this.height / 2 - 20, 0xFFFFFF);
        }
    }

    public void drawLocationHeader(String locationName, int mouseX, int mouseY, int x, int y)
    {
        String locationPrefix = TextFormatting.YELLOW + I18n.format("gui.waystones:current_location") + " ";
        int locationPrefixWidth = this.fontRenderer.getStringWidth(locationPrefix);

        int locationWidth = this.fontRenderer.getStringWidth(locationName);

        int fullWidth = locationPrefixWidth + locationWidth;

        int startX = x - fullWidth / 2 + locationPrefixWidth;
        this.isLocationHeaderHovered = mouseX >= startX && mouseX < startX + locationWidth + 16
                && mouseY >= y && mouseY < y + this.fontRenderer.FONT_HEIGHT;

        String fullText = locationPrefix + TextFormatting.WHITE;
        if (this.isLocationHeaderHovered) {
            fullText += TextFormatting.UNDERLINE;
        }
        fullText += locationName;

        this.drawString(this.fontRenderer, TextFormatting.UNDERLINE + fullText, x - fullWidth / 2, y, 0xFFFFFF);

        if (this.isLocationHeaderHovered) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + fullWidth / 2 + 4, y, 0f);
            float scale = 0.5f;
            GlStateManager.scale(scale, scale, scale);
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.WRITABLE_BOOK), 0, 0);
            GlStateManager.popMatrix();
        }
    }
}
