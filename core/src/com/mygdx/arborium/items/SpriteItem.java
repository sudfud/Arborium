package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

/*
 * An Item that contains some kind of drawable Texture
 *
 * Subclasses: ShopItem
 */

public class SpriteItem extends Item
{
    public final Texture itemImage;

    public SpriteItem(int id, String name, Texture image)
    {
        super(id, name);
        this.itemImage = image;
    }
}
