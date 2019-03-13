package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;

import java.util.HashMap;

public class FruitList
{
    private HashMap<String, Fruit> fruitMap;

    public Fruit apple;
    public Fruit orange;

    public FruitList(Arborium game)
    {
        Texture appleTexture = game.resources.getTexture(Resources.APPLE_FRUIT);
        Texture orangeTexture = game.resources.getTexture(Resources.ORANGE_FRUIT);

        apple = new Fruit(64, "Apple Fruit", appleTexture, -1, 10);
        orange = new Fruit(65, "Orange Fruit", orangeTexture, -1, 25);

        fruitMap = new HashMap<String, Fruit>();

        fruitMap.put(apple.itemName, apple);
        fruitMap.put(orange.itemName, orange);
    }

    public Fruit get(String name)
    {
        return fruitMap.get(name);
    }
}
