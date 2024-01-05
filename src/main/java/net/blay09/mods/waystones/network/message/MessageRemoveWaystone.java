package net.blay09.mods.waystones.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageRemoveWaystone implements IMessage
{
    private int index;

    public MessageRemoveWaystone()
    {
    }

    public MessageRemoveWaystone(int index)
    {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.index = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(this.index);
    }

    public int getIndex()
    {
        return this.index;
    }
}
