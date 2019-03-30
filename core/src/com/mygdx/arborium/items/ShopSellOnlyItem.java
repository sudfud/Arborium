package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

import com.mygdx.arborium.items.ShopItem;

public abstract class ShopSellOnlyItem extends ShopItem
{
    public ShopSellOnlyItem(int id, String name, Texture image, int sellValue, boolean locked)
    {
        super(id, name, image, -1, sellValue, locked);
    }
}
