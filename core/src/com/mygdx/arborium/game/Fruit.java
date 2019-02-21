package com.mygdx.arborium.game;

public class Fruit extends ShopItem
{
    private int sellValue;
    private int seedCount;

    public Fruit(int id, String name, int buyValue, int sellValue)
    {
        super(id, name, buyValue, sellValue);
    }
}
