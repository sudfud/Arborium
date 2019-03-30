package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;

import java.util.HashMap;

public class SeedList
{
    private HashMap<String, Seed> seedMap;

    public final Seed apple;
    public final Seed orange;

    public SeedList(Arborium game)
    {
        Texture appleSeedTexture = game.resources.getTexture(Resources.APPLE_SEED);
        Texture orangeSeedTexture = game.resources.getTexture(Resources.ORANGE_SEED);

        apple = new Seed(128, "Apple Seed", appleSeedTexture, 100, 100 * 3/4, false, game.treeList.appleTree);
        orange = new Seed(129, "Orange Seed", orangeSeedTexture, 250, 250 * 3/4, false, game.treeList.orangeTree);

        seedMap = new HashMap<String, Seed>();
        seedMap.put(apple.itemName, apple);
        seedMap.put(orange.itemName, orange);
    }

    public Seed get(String name)
    {
        return seedMap.get(name);
    }

    public Seed[] getAllSeeds()
    {
        return seedMap.values().toArray(new Seed[seedMap.size()]);
    }

    public String[] getSeedNames()
    {
        return seedMap.keySet().toArray(new String[seedMap.size()]);
    }
}
