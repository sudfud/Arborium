package com.mygdx.arborium.items;

public abstract class Item
{
    public final int id;

    public final String itemName;

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

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Item))
            return false;
        else
            return (id == ((Item) o).getId());
    }
}
