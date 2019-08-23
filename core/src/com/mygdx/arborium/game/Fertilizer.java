package com.mygdx.arborium.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.arborium.items.ShopItem;

public abstract class Fertilizer extends ShopItem
{
    public Fertilizer(int id, String name, Texture image, String description, int buyValue, boolean locked)
    {
        super(id, name, image, description, buyValue, buyValue / 3, locked);
    }
}
