package net.blay09.mods.waystones.network.message;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nullable;

public class MessageTeleportToWaystone implements IMessage
{
    private WaystoneEntry waystone;
    private WarpMode warpMode;
    private EnumHand hand;
    private WaystoneEntry fromWaystone;

    public MessageTeleportToWaystone()
    {
    }

    public MessageTeleportToWaystone(WaystoneEntry waystone, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone)
    {
        this.waystone = waystone;
        this.warpMode = warpMode;
        this.hand = hand;
        this.fromWaystone = fromWaystone;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.waystone = WaystoneEntry.read(buf);
        this.warpMode = WarpMode.values()[buf.readByte()];
        this.hand = (this.warpMode == WarpMode.WARP_SCROLL || this.warpMode == WarpMode.WARP_STONE) ? EnumHand.values()[buf.readByte()] : EnumHand.MAIN_HAND;
        if (this.warpMode == WarpMode.WAYSTONE) {
            this.fromWaystone = WaystoneEntry.read(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        this.waystone.write(buf);
        buf.writeByte(this.warpMode.ordinal());
        if (this.warpMode == WarpMode.WARP_SCROLL || this.warpMode == WarpMode.WARP_STONE) {
            buf.writeByte(this.hand.ordinal());
        } else if (this.warpMode == WarpMode.WAYSTONE) {
            this.fromWaystone.write(buf);
        }
    }

    public WaystoneEntry getWaystone()
    {
        return this.waystone;
    }

    @Nullable
    public WaystoneEntry getFromWaystone()
    {
        return this.fromWaystone;
    }

    public WarpMode getWarpMode()
    {
        return this.warpMode;
    }

    public EnumHand getHand()
    {
        return this.hand;
    }
}
