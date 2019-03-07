package com.mygdx.arborium.items;

import com.mygdx.arborium.Resources;

import java.util.HashMap;
import java.util.List;

public class SeedList
{
    private static HashMap<String, Seed> seedMap;

    public static final Seed apple = new Seed(128, "Apple Seed", Resources.seed2, 100, 100 * 3/4, false, TreeList.appleTree);
    public static final Seed orange = new Seed(129, "Orange Seed", Resources.seed3, 250, 250 * 3/4, false, TreeList.orangeTree);

    public static String[] seedNames = { apple.itemName, orange.itemName };

    public static void initialize()
    {
        seedMap = new HashMap<String, Seed>();
        seedMap.put(apple.itemName, apple);
        seedMap.put(orange.itemName, orange);
    }

    public static Seed get(String name)
    {
        return seedMap.get(name);
    }

    public static Seed[] getAllSeeds()
    {
        return seedMap.values().toArray(new Seed[seedMap.size()]);
    }

    public static String[] getSeedNames()
    {
        return seedMap.keySet().toArray(new String[seedMap.size()]);
    }
}
