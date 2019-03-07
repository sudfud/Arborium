package com.mygdx.arborium.items;

import java.util.HashMap;

public abstract class Item
{
    public final int id;

    public final String itemName;

    private static HashMap<String, Item> itemLookup = new HashMap<String, Item>();

    public Item(int id, String name)
    {
        this.id = id;
        this.itemName = name;

        itemLookup.put(this.itemName, this);
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

    public static Item lookup(String name)
    {
        return itemLookup.get(name);
    }
}
