package net.blay09.mods.waystones.util;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WaystoneActivatedEvent extends Event
{
    private final String waystoneName;
    private final BlockPos pos;
    private final int dimension;

    public WaystoneActivatedEvent(String waystoneName, BlockPos pos, int dimension)
    {
        this.waystoneName = waystoneName;
        this.pos = pos;
        this.dimension = dimension;
    }

    public String getWaystoneName()
    {
        return this.waystoneName;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public int getDimension()
    {
        return this.dimension;
    }
}
