package net.blay09.mods.waystones.worldgen;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.util.GenerateWaystoneNameEvent;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class NameGenerator extends WorldSavedData
{
    private static final String DATA_NAME = Waystones.MOD_ID + "_NameGenerator";
    private static final String TAG_LIST_NAME = "UsedNames";

    public NameGenerator()
    {
        super(DATA_NAME);
    }

    public NameGenerator(String name)
    {
        super(name);
    }

    // Stolen from MrPork:
    private static final String[] random1 = new String[]{
            "Kr", "Ca", "Ra",
            "Rei", "Mar", "Luk", "Cro", "Cru", "Ray", "Bre", "Zed", "Mor", "Jag", "Mer", "Jar", "Mad", "Cry", "Zur",
            "Mjol", "Zork", "Creo", "Azak", "Azur", "Mrok", "Drak",
    };
    private static final String[] random2 = new String[]{
            "ir", "mi",
            "air", "sor", "mee", "clo", "red", "cra", "ark", "arc", "mur", "zer",
            "miri", "lori", "cres", "zoir", "urak",
            "marac",
            "slamar", "salmar",
    };
    private static final String[] random3 = new String[]{
            "d",
            "ed", "es", "er",
            "ark", "arc", "der", "med", "ure", "zur", "mur",
            "tron", "cred",
    };

    private String randomName(Random rand)
    {
        return random1[rand.nextInt(random1.length)] + random2[rand.nextInt(random2.length)] + random3[rand.nextInt(random3.length)];
    }

    // ^^^^^^
    private Map<Biome, String> BIOME_NAMES;
    private final Set<String> usedNames = Sets.newHashSet();

    public void init()
    {
        this.BIOME_NAMES = Maps.newHashMap();

        this.addBiomeName(Biomes.COLD_TAIGA, "Taiga");
        this.addBiomeName(Biomes.REDWOOD_TAIGA, "Taiga");
        this.addBiomeName(Biomes.TAIGA, "Taiga");

        this.addBiomeName(Biomes.PLAINS, "Plains");

        this.addBiomeName(Biomes.MUSHROOM_ISLAND, "Island");
        this.addBiomeName(Biomes.MUSHROOM_ISLAND_SHORE, "Island");

        this.addBiomeName(Biomes.RIVER, "River");
        this.addBiomeName(Biomes.FROZEN_RIVER, "River");

        this.addBiomeName(Biomes.BEACH, "Beach");
        this.addBiomeName(Biomes.COLD_BEACH, "Beach");
        this.addBiomeName(Biomes.STONE_BEACH, "Beach");

        this.addBiomeName(Biomes.BIRCH_FOREST, "Forest");
        this.addBiomeName(Biomes.BIRCH_FOREST_HILLS, "Forest");
        this.addBiomeName(Biomes.FOREST_HILLS, "Forest");
        this.addBiomeName(Biomes.FOREST, "Forest");
        this.addBiomeName(Biomes.ROOFED_FOREST, "Forest");
        this.addBiomeName(Biomes.MUTATED_FOREST, "Forest");
        this.addBiomeName(Biomes.MUTATED_BIRCH_FOREST, "Forest");
        this.addBiomeName(Biomes.MUTATED_BIRCH_FOREST_HILLS, "Forest");
        this.addBiomeName(Biomes.MUTATED_ROOFED_FOREST, "Forest");

        this.addBiomeName(Biomes.DEEP_OCEAN, "Ocean");
        this.addBiomeName(Biomes.OCEAN, "Ocean");
        this.addBiomeName(Biomes.FROZEN_OCEAN, "Ocean");

        this.addBiomeName(Biomes.DESERT, "Desert");
        this.addBiomeName(Biomes.DESERT_HILLS, "Desert");
        this.addBiomeName(Biomes.MUTATED_DESERT, "Desert");

        this.addBiomeName(Biomes.COLD_TAIGA_HILLS, "Hills");
        this.addBiomeName(Biomes.EXTREME_HILLS, "Hills");
        this.addBiomeName(Biomes.EXTREME_HILLS_EDGE, "Hills");
        this.addBiomeName(Biomes.EXTREME_HILLS_WITH_TREES, "Hills");
        this.addBiomeName(Biomes.MUTATED_EXTREME_HILLS, "Hills");
        this.addBiomeName(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, "Hills");
        this.addBiomeName(Biomes.REDWOOD_TAIGA_HILLS, "Hills");
        this.addBiomeName(Biomes.TAIGA_HILLS, "Hills");
        this.addBiomeName(Biomes.MUTATED_REDWOOD_TAIGA_HILLS, "Hills");

        this.addBiomeName(Biomes.SWAMPLAND, "Swamps");
        this.addBiomeName(Biomes.MUTATED_SWAMPLAND, "Swamps");

        this.addBiomeName(Biomes.SAVANNA, "Savanna");
        this.addBiomeName(Biomes.SAVANNA_PLATEAU, "Plateau");
        this.addBiomeName(Biomes.MUTATED_SAVANNA, "Savanna");
        this.addBiomeName(Biomes.MUTATED_SAVANNA_ROCK, "Savanna");

        this.addBiomeName(Biomes.ICE_PLAINS, "Icelands");
        this.addBiomeName(Biomes.ICE_MOUNTAINS, "Icelands");
        this.addBiomeName(Biomes.MUTATED_ICE_FLATS, "Icelands");

        this.addBiomeName(Biomes.JUNGLE, "Jungle");
        this.addBiomeName(Biomes.JUNGLE_EDGE, "Jungle");
        this.addBiomeName(Biomes.JUNGLE_HILLS, "Jungle");
        this.addBiomeName(Biomes.MUTATED_JUNGLE, "Jungle");
        this.addBiomeName(Biomes.MUTATED_JUNGLE_EDGE, "Jungle");

        this.addBiomeName(Biomes.MESA_ROCK, "Mesa");
        this.addBiomeName(Biomes.MESA, "Mesa");
        this.addBiomeName(Biomes.MESA_CLEAR_ROCK, "Mesa");
        this.addBiomeName(Biomes.MUTATED_MESA, "Mesa");
        this.addBiomeName(Biomes.MUTATED_MESA_CLEAR_ROCK, "Mesa");
        this.addBiomeName(Biomes.MUTATED_MESA_ROCK, "Mesa");

        this.addBiomeName(Biomes.VOID, "Void");
        this.addBiomeName(Biomes.SKY, "Skies");
    }

    private void addBiomeName(Biome biome, String name)
    {
        this.BIOME_NAMES.put(biome, name);
    }

    @Deprecated
    public String getName(Biome biome, Random rand)
    {
        return this.getName(BlockPos.ORIGIN, 0, biome, rand);
    }

    public String getName(BlockPos pos, int dimension, Biome biome, Random rand)
    {
        if (this.BIOME_NAMES == null) {
            this.init();
        }

        String name = null;
        List<String> customNames = Arrays.asList(WaystoneConfig.worldGen.customNames);
        Collections.shuffle(customNames);
        for (String customName : customNames) {
            if (!this.usedNames.contains(customName)) {
                name = customName;
                break;
            }
        }

        if (name == null) {
            String biomeSuffix = this.BIOME_NAMES.get(biome);
            name = this.randomName(rand) + (biomeSuffix != null ? " " + biomeSuffix : "");
            String tryName = name;
            int i = 1;
            while (this.usedNames.contains(tryName)) {
                tryName = name + " " + RomanNumber.toRoman(i);
                i++;
            }

            name = tryName;
        }

        GenerateWaystoneNameEvent event = new GenerateWaystoneNameEvent(pos, dimension, name);
        MinecraftForge.EVENT_BUS.post(event);
        name = event.getWaystoneName();

        this.usedNames.add(name);
        this.markDirty();
        return name;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList tagList = compound.getTagList(TAG_LIST_NAME, Constants.NBT.TAG_STRING);
        for (int i = 0; i < tagList.tagCount(); i++) {
            this.usedNames.add(((NBTTagString) tagList.get(i)).getString());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagList tagList = new NBTTagList();
        for (String entry : this.usedNames) {
            tagList.appendTag(new NBTTagString(entry));
        }
        compound.setTag(TAG_LIST_NAME, tagList);
        return compound;
    }

    public static NameGenerator get(World world)
    {
        MapStorage storage = world.getMapStorage();
        if (storage != null) {
            NameGenerator instance = (NameGenerator) storage.getOrLoadData(NameGenerator.class, DATA_NAME);
            if (instance == null) {
                instance = new NameGenerator();
                storage.setData(DATA_NAME, instance);
            }
            return instance;
        }

        return new NameGenerator();
    }
}
