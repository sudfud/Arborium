package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class SpriteItem extends Item
{
    public final Texture itemImage;

    public SpriteItem(int id, String name, Texture image)
    {
        super(id, name);
        this.itemImage = image;
    }
}
