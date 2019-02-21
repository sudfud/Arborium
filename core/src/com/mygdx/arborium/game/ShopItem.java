package com.mygdx.arborium.game;

public abstract class ShopItem extends Item
{
    public int buyValue;
    public int sellValue;

    public ShopItem(int id, String name, int buyValue, int sellValue)
    {
        super(id, name);
        this.buyValue = buyValue;
        this.sellValue = sellValue;
    }
}
