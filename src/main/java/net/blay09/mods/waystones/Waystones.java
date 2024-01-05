package net.blay09.mods.waystones;

import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.item.ItemBoundScroll;
import net.blay09.mods.waystones.item.ItemReturnScroll;
import net.blay09.mods.waystones.item.ItemWarpScroll;
import net.blay09.mods.waystones.item.ItemWarpStone;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.worldgen.ComponentVillageWaystone;
import net.blay09.mods.waystones.worldgen.LegacyWorldGen;
import net.blay09.mods.waystones.worldgen.VillageWaystoneCreationHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod(modid = Waystones.MOD_ID, name = "Waystones", acceptedMinecraftVersions = "[1.12]")
@Mod.EventBusSubscriber
public class Waystones
{
    public static final String MOD_ID = "waystones";
    @Mod.Instance(MOD_ID)
    public static Waystones instance;
    @SidedProxy(serverSide = "net.blay09.mods.waystones.CommonProxy", clientSide = "net.blay09.mods.waystones.client.ClientProxy")
    public static CommonProxy proxy;
    @GameRegistry.ObjectHolder(BlockWaystone.name)
    public static final Block blockWaystone = Blocks.AIR;
    @GameRegistry.ObjectHolder(ItemReturnScroll.name)
    public static final Item itemReturnScroll = Items.AIR;
    @GameRegistry.ObjectHolder(ItemBoundScroll.name)
    public static final Item itemBoundScroll = Items.AIR;
    @GameRegistry.ObjectHolder(ItemWarpScroll.name)
    public static final Item itemWarpScroll = Items.AIR;
    @GameRegistry.ObjectHolder(ItemWarpStone.name)
    public static final Item itemWarpStone = Items.AIR;
    public static final CreativeTabs creativeTab = new CreativeTabs("Sassovia")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Waystones.blockWaystone);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileWaystone.class, new ResourceLocation(MOD_ID, "waystone"));

        NetworkHandler.init();

        VillagerRegistry.instance().registerVillageCreationHandler(new VillageWaystoneCreationHandler());
        MapGenStructureIO.registerStructureComponent(ComponentVillageWaystone.class, "waystones:village_waystone");
        GameRegistry.registerWorldGenerator(new LegacyWorldGen(), 0);

        proxy.preInit(event);

        MinecraftForge.EVENT_BUS.register(new WarpDamageResetHandler());
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (MOD_ID.equals(event.getModID())) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                new BlockWaystone()
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                new ItemBlock(blockWaystone).setRegistryName(BlockWaystone.name)
        );

        event.getRegistry().registerAll(
                new ItemReturnScroll().setRegistryName(ItemReturnScroll.registryName),
                new ItemBoundScroll().setRegistryName(ItemBoundScroll.registryName),
                new ItemWarpScroll(),
                new ItemWarpStone()
        );
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Waystones.blockWaystone), 0, new ModelResourceLocation(BlockWaystone.registryName, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Waystones.itemWarpStone, 0, new ModelResourceLocation(ItemWarpStone.registryName, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Waystones.itemReturnScroll, 0, new ModelResourceLocation(ItemReturnScroll.registryName, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Waystones.itemWarpScroll, 0, new ModelResourceLocation(ItemWarpScroll.registryName, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Waystones.itemBoundScroll, 0, new ModelResourceLocation(ItemBoundScroll.registryName, "inventory"));

        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(Waystones.blockWaystone), 0, TileWaystone.class);
    }
}
