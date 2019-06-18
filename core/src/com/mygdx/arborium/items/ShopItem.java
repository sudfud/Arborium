package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

/*
 * An Item that can be bought and/or sold in the game's shop. Has a buy and sell value, and can be
 * either be locked or unlocked. If an Item can only be bought, the sell value should be -1, and
 * vice versa.
 *
 */

public abstract class ShopItem extends SpriteItem
{
    public final int buyValue;
    public final int sellValue;

    private boolean locked;

    public final String description;

    public ShopItem(int id, String name, Texture image, String description, int buyValue, int sellValue, boolean locked)
    {
        super(id, name, image);
        this.description = description;
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
