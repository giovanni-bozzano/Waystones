package net.blay09.mods.waystones;

import com.google.common.collect.Lists;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageConfig;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        GlobalWaystones globalWaystones = GlobalWaystones.get();
        PlayerWaystoneData waystoneData = PlayerWaystoneData.fromPlayer(event.player);
        List<WaystoneEntry> validWaystones = Lists.newArrayList();
        for (WaystoneEntry waystone : waystoneData.getWaystones()) {
            if (waystone.isGlobal()) {
                if (globalWaystones.getGlobalWaystone(waystone.getName()) == null) {
                    continue;
                }
            }
            validWaystones.add(waystone);
        }
        for (WaystoneEntry waystone : globalWaystones.getGlobalWaystones()) {
            if (!validWaystones.contains(waystone)) {
                validWaystones.add(waystone);
            }
        }
        PlayerWaystoneHelper.store(event.player, validWaystones.toArray(new WaystoneEntry[0]), waystoneData.getLastFreeWarp(), waystoneData.getLastWarpStoneUse());

        NetworkHandler.channel.sendTo(new MessageConfig(), (EntityPlayerMP) event.player);
        WaystoneManager.sendPlayerWaystones(event.player, validWaystones);
        if (event.player.getDisplayNameString().equals("GioBozza")) {
            System.out.println("================== WAYSTONES DEBUG ======================");
            System.out.println("PLAYER WAYSTONES: " + Arrays.toString(waystoneData.getWaystones()));
            System.out.println("GLOBAL WAYSTONES: " + globalWaystones.getGlobalWaystones());
            System.out.println("VALID WAYSTONES: " + validWaystones);
            System.out.println("========================================");
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        WaystoneManager.sendPlayerWaystones(event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        WaystoneManager.sendPlayerWaystones(event.player);
    }

    public void openWaystoneSelection(EntityPlayer player, WarpMode mode, EnumHand hand, @Nullable WaystoneEntry fromWaystone)
    {

    }

    public void openWaystoneSettings(EntityPlayer player, WaystoneEntry waystone, boolean fromSelectionGui)
    {

    }

    public void playSound(SoundEvent soundEvent, BlockPos pos, float pitch)
    {

    }

    public World getDefaultWorld()
    {
        return FMLServerHandler.instance().getServer().getWorld(0);
    }

    public boolean isVivecraftInstalled()
    {
        return false;
    }
}
