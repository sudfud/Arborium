package com.mygdx.arborium.items;

import com.mygdx.arborium.Arborium;

import java.util.HashMap;

public class SaplingList
{
    static HashMap<Fruit.FruitType, Sapling> saplingMap;

    Sapling appleSapling;
    Sapling orangeSapling;
    Sapling cherrySapling;

    String appleSapDesc = "The hallmark of any good orchard. Perfect for any teacher's pet." + "\n\n" +
                            "Matures in 60 minutes." + "\n\n" +
                            "Produces 2 apples every 40 minutes.";

    String orangeSapDesc = "The standard citrus fruit. Great at parties." + "\n\n" +
                            "Matures in 90 minutes." + "\n\n" +
                            "Produces 4 oranges every 60 minutes.";

    String cherrySapDesc = "Sweet, succulent, and there's two of 'em!" + "\n\n" +
                            "Matures in 120 minutes." + "\n\n" +
                            "Produces 8 cherries every 20 minutes.";

    public SaplingList(Arborium game)
    {
        saplingMap = new HashMap<Fruit.FruitType, Sapling>();

        appleSapling = new Sapling.Builder(64, "Apple Tree", game.getTexture(Arborium.APPLE_TREE2X), appleSapDesc, 250, false)
                .matureTime(60 * 60 * 1000)
                .produce(FruitList.get(Fruit.FruitType.APPLE))
                .produceRate(20 * 60 * 1000)
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

        cherrySapling = new Sapling.Builder(66, "Cherry Tree", game.getTexture(Arborium.CHERRY_TREE), cherrySapDesc, 1500, false)
                .matureTime(30 * 1000)
                .produce(FruitList.get(Fruit.FruitType.CHERRY))
                .produceRate(30 * 1000)
                .produceAmount(1)
                .build();

        saplingMap.put(Fruit.FruitType.APPLE, appleSapling);
        saplingMap.put(Fruit.FruitType.ORANGE, orangeSapling);
        saplingMap.put(Fruit.FruitType.CHERRY, cherrySapling);
    }

    public static Sapling get(Fruit.FruitType type)
    {
        return saplingMap.get(type);
    }
}
