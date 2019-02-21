package com.mygdx.arborium.game;

public abstract class Item
{
    final int id;

    final String itemName;

    public Item(int id, String name)
    {
        this.id = id;
        this.itemName = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return itemName;
    }
}
