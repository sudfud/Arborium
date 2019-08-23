package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

/*
 * An Item that contains some kind of drawable Texture
 *
 * Subclasses: ShopItem
 */

public class SpriteItem extends Item
{
    public SpriteItem(int id, String name, Texture image)
    {
        super(id, name);
        properties.put("image", image);
    }

    public SpriteItem(Item item, Texture image)
    {
        super(item.id, item.itemName);
        properties.put("image", image);
    }

    public SpriteItem(int id, String name)
    {
        super(id, name);
        properties.put("image", null);
    }
}
