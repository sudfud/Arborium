package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class Fruit extends ShopItem
{
    public enum FruitType
    {
        APPLE(0),
        ORANGE(1);

        int value;

        FruitType(int value)
        {
            this.value = value;
        }
    }

    public Fruit(int id, String name, Texture image, int sellValue, boolean locked) {
        super(id, name, image, "", -1, sellValue, locked);
    }
}
