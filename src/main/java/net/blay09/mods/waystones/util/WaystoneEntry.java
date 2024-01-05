package net.blay09.mods.waystones.util;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.block.TileWaystone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class WaystoneEntry
{
    private final String name;
    private final int dimensionId;
    private final BlockPos pos;
    private boolean isGlobal;

    public WaystoneEntry(String name, int dimensionId, BlockPos pos, boolean isGlobal)
    {
        this.name = name;
        this.dimensionId = dimensionId;
        this.pos = pos;
        this.isGlobal = isGlobal;
    }

    public WaystoneEntry(TileWaystone tileWaystone)
    {
        this.name = tileWaystone.getWaystoneName();
        this.dimensionId = tileWaystone.getWorld().provider.getDimension();
        this.pos = tileWaystone.getPos();
        this.isGlobal = tileWaystone.isGlobal();
    }

    public String getName()
    {
        return this.name;
    }

    public int getDimensionId()
    {
        return this.dimensionId;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public boolean isGlobal()
    {
        return this.isGlobal;
    }

    public void setGlobal(boolean isGlobal)
    {
        this.isGlobal = isGlobal;
    }

    public static WaystoneEntry read(ByteBuf buf)
    {
        return new WaystoneEntry(ByteBufUtils.readUTF8String(buf), buf.readInt(), BlockPos.fromLong(buf.readLong()), buf.readBoolean());
    }

    public static WaystoneEntry read(NBTTagCompound tagCompound)
    {
        return new WaystoneEntry(tagCompound.getString("Name"), tagCompound.getInteger("Dimension"), BlockPos.fromLong(tagCompound.getLong("Position")), tagCompound.getBoolean("IsGlobal"));
    }

    public void write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.name);
        buf.writeInt(this.dimensionId);
        buf.writeLong(this.pos.toLong());
        buf.writeBoolean(this.isGlobal);
    }

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("Name", this.name);
        tagCompound.setInteger("Dimension", this.dimensionId);
        tagCompound.setLong("Position", this.pos.toLong());
        tagCompound.setBoolean("IsGlobal", this.isGlobal);
        return tagCompound;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        WaystoneEntry that = (WaystoneEntry) o;
        return this.dimensionId == that.dimensionId && this.pos.equals(that.pos) && this.isGlobal == that.isGlobal;
    }

    @Override
    public int hashCode()
    {
        int result = this.dimensionId;
        result = 31 * result + this.pos.hashCode();
        return result;
    }
}
