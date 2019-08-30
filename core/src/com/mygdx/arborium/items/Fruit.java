package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class Fruit extends ShopItem
{
    public enum FruitType
    {
        APPLE(0),
        ORANGE(1),
        CHERRY(2);

        int value;

        FruitType(int value)
        {
            this.value = value;
        }
    }

    public Fruit(int id, String name, Texture image, int sellValue) 
    {
        super(id, name, image, "", -1, sellValue, false);
    }

    public Fruit(SpriteItem item)
    {
        super(item);
        setBuyValue(-1);
        setSellValue(-1);
        setLock(false);
    }

    public Fruit(SpriteItem item, int sellValue)
    {
        super(item);
        setBuyValue(-1);
        setSellValue(sellValue);
        setLock(false);
    }
}
