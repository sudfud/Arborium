package com.mygdx.arborium.items;

import com.mygdx.arborium.Arborium;

import java.util.HashMap;

public class SaplingList
{
    static HashMap<Fruit.FruitType, Sapling> saplingMap;

    Sapling appleSapling;
    Sapling orangeSapling;

    String appleSapDesc = "The hallmark of any good orchard. Perfect for any teacher's pet." + "\n\n" +
                            "Matures in 60 minutes." + "\n\n" +
                            "Produces 5 apples every 10 minutes.";

    String orangeSapDesc = "The standard citrus fruit. Great at parties." + "\n\n" +
                            "Matures in 40 minutes." + "\n\n" +
                            "Produces 8 oranges every 15 minutes.";

    public SaplingList(Arborium game)
    {
        saplingMap = new HashMap<Fruit.FruitType, Sapling>();

        appleSapling = new Sapling.Builder(64, "Apple Tree", game.getTexture(Arborium.APPLE_TREE2X), appleSapDesc, 250, false)
                //.matureTime(15 * 60 * 1000)
                .matureTime(30 * 1000)
                .produce(FruitList.get(Fruit.FruitType.APPLE))
                //.produceRate(5 * 60 * 1000)
                .produceRate(30 * 1000)
                .produceAmount(5)
                .build();

        orangeSapling = new Sapling.Builder(65, "Orange Tree", game.getTexture(Arborium.ORANGE_TREE2X), orangeSapDesc, 600, false)
                //.matureTime(10 * 60 * 1000)
                .matureTime(30 * 1000)
                .produce(FruitList.get(Fruit.FruitType.ORANGE))
                //.produceRate(7 * 60 * 1000)
                .produceRate(30 * 1000)
                .produceAmount(8)
                .build();

        saplingMap.put(Fruit.FruitType.APPLE, appleSapling);
        saplingMap.put(Fruit.FruitType.ORANGE, orangeSapling);
    }

    public static Sapling get(Fruit.FruitType type)
    {
        return saplingMap.get(type);
    }
}
