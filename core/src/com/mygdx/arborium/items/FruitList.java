package com.mygdx.arborium.items;

import com.mygdx.arborium.Arborium;

import java.util.HashMap;

public class FruitList
{
    static HashMap<Fruit.FruitType, Fruit> fruitMap;

    // final Fruit apple;
    // final Fruit orange;
    // final Fruit cherry;

    public FruitList(Arborium game)
    {
        fruitMap = new HashMap<Fruit.FruitType, Fruit>();

        // apple = new Fruit(1, "Apple", game.getTexture(Arborium.APPLE_LARGE), 50, false);
        // orange = new Fruit(2, "Orange", game.getTexture(Arborium.ORANGE_LARGE), 75, false);
        // cherry = new Fruit(3, "Cherry", game.getTexture(Arborium.CHERRY_FRUIT), 15, false);

        // fruitMap.put(Fruit.FruitType.APPLE, apple);
        // fruitMap.put(Fruit.FruitType.ORANGE, orange);
        // fruitMap.put(Fruit.FruitType.CHERRY, cherry);
    }

    public static Fruit get(Fruit.FruitType type)
    {
        return fruitMap.get(type);
    }
}
