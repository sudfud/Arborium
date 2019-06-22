package com.mygdx.arborium.items;

import com.mygdx.arborium.Arborium;

import java.util.HashMap;

public class FruitList
{
    static HashMap<Fruit.FruitType, Fruit> fruitMap;

    final Fruit apple;
    final Fruit orange;

    public FruitList(Arborium game)
    {
        fruitMap = new HashMap<Fruit.FruitType, Fruit>();

        apple = new Fruit(1, "Apple", game.getTexture(Arborium.APPLE_FRUIT), 50, false);
        orange = new Fruit(2, "Orange", game.getTexture(Arborium.ORANGE_FRUIT), 75, false);

        fruitMap.put(Fruit.FruitType.APPLE, apple);
        fruitMap.put(Fruit.FruitType.ORANGE, orange);
    }

    public static Fruit get(Fruit.FruitType type)
    {
        return fruitMap.get(type);
    }
}
