package net.blay09.mods.waystones.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSortWaystone implements IMessage
{
    private int index;
    private int otherIndex;

    public MessageSortWaystone()
    {
    }

    public MessageSortWaystone(int index, int otherIndex)
    {
        this.index = index;
        this.otherIndex = otherIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.index = buf.readByte();
        this.otherIndex = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(this.index);
        buf.writeByte(this.otherIndex);
    }

    public int getIndex()
    {
        return this.index;
    }

    public int getOtherIndex()
    {
        return this.otherIndex;
    }
}
