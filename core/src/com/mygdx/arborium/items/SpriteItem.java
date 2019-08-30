package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

/*
 * An Item that contains some kind of drawable Texture
 *
 * Subclasses: ShopItem
 */

public class SpriteItem extends Item
{
    public final Texture sprite;

    public SpriteItem(int id, String name, Texture sprite)
    {
        super(id, name);
        this.sprite = sprite;
    }

    public SpriteItem(Item item, Texture sprite)
    {
        super(item.id, item.name);
        this.sprite = sprite;
    }

    public SpriteItem(int id, String name)
    {
        super(id, name);
        sprite = null;
    }
}
