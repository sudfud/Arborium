package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class Sapling extends ShopItem
{
    long matureTime;
    long produceRate;
    int produceAmount;

    Fruit produce;

    public Sapling(int id, String name, String description, Texture image, int buyValue, boolean locked)
    {
        super(id, name, image, description, buyValue, buyValue / 3, locked);
    }

    public long getMatureTime()
    {
        return matureTime;
    }

    public long getProduceRate()
    {
        return produceRate;
    }

    public int getProduceAmount()
    {
        return produceAmount;
    }

    public Fruit getProduce()
    {
        return produce;
    }
}
