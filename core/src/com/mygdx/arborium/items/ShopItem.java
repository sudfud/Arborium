package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public abstract class ShopItem extends SpriteItem
{
    public final int buyValue;
    public final int sellValue;

    private boolean locked;

    public ShopItem(int id, String name, Texture image, int buyValue, int sellValue, boolean locked)
    {
        super(id, name, image);
        this.buyValue = buyValue;
        this.sellValue = sellValue;
        this.locked = locked;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void unlock()
    {
        locked = false;
    }
}
