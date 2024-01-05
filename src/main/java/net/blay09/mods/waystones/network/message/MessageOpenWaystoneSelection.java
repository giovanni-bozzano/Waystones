package net.blay09.mods.waystones.network.message;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageOpenWaystoneSelection implements IMessage
{
    private WarpMode warpMode;
    private EnumHand hand;
    private WaystoneEntry waystone;

    public MessageOpenWaystoneSelection()
    {
    }

    public MessageOpenWaystoneSelection(WarpMode warpMode, EnumHand hand, WaystoneEntry waystone)
    {
        this.warpMode = warpMode;
        this.hand = hand;
        this.waystone = waystone;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.warpMode = WarpMode.values()[buf.readByte()];
        this.hand = EnumHand.values()[buf.readByte()];
        this.waystone = WaystoneEntry.read(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(this.warpMode.ordinal());
        buf.writeByte(this.hand.ordinal());
        this.waystone.write(buf);
    }

    public WarpMode getWarpMode()
    {
        return this.warpMode;
    }

    public EnumHand getHand()
    {
        return this.hand;
    }

    public WaystoneEntry getWaystone()
    {
        return this.waystone;
    }
}
