package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class Fruit extends ShopItem
{
    private int sellValue;
    private int seedCount;

    public Fruit(int id, String name, Texture image, int buyValue, int sellValue)
    {
        super(id, name, image, buyValue, sellValue, false);
    }
}
