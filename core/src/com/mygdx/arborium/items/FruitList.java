package com.mygdx.arborium.items;

import com.mygdx.arborium.Resources;

import java.util.HashMap;

public class FruitList
{
    private static HashMap<String, Fruit> fruitMap;

    public static Fruit apple = new Fruit(64, "Apple Fruit", Resources.appleFruit, -1, 500);
    public static Fruit orange = new Fruit(65, "Orange Fruit", Resources.orangeFruit, -1, 750);

    public static void initialize()
    {
        fruitMap = new HashMap<String, Fruit>();

        fruitMap.put(apple.itemName, apple);
        fruitMap.put(orange.itemName, orange);
    }

    public Fruit get(String name)
    {
        return fruitMap.get(name);
    }
}
