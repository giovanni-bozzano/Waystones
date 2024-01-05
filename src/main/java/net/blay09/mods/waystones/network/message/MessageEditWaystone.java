package net.blay09.mods.waystones.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageEditWaystone implements IMessage
{
    private BlockPos pos;
    private String name;
    private boolean isGlobal;
    private boolean fromSelectionGui;

    public MessageEditWaystone()
    {
    }

    public MessageEditWaystone(BlockPos pos, String name, boolean isGlobal, boolean fromSelectionGui)
    {
        this.pos = pos;
        this.name = name;
        this.isGlobal = isGlobal;
        this.fromSelectionGui = fromSelectionGui;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.name = ByteBufUtils.readUTF8String(buf);
        this.isGlobal = buf.readBoolean();
        this.fromSelectionGui = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(this.pos.toLong());
        ByteBufUtils.writeUTF8String(buf, this.name);
        buf.writeBoolean(this.isGlobal);
        buf.writeBoolean(this.fromSelectionGui);
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isGlobal()
    {
        return this.isGlobal;
    }

    public boolean isFromSelectionGui()
    {
        return this.fromSelectionGui;
    }
}
