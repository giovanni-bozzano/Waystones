package net.blay09.mods.waystones.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageTeleportEffect implements IMessage
{
    private BlockPos pos;

    public MessageTeleportEffect()
    {
    }

    public MessageTeleportEffect(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(this.pos.toLong());
    }

    public BlockPos getPos()
    {
        return this.pos;
    }
}
