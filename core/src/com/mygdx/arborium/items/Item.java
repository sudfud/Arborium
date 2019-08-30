package com.mygdx.arborium.items;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Item
{
    private static HashMap<String, Item> nameItemLookup = new HashMap<String, Item>();
    private static HashMap<Integer, Item> idItemLookup = new HashMap<Integer, Item>();

    public final int id;
    public final String name;

    public Item(int id, String name)
    {
        this.id = id;
        this.name = name;

        nameItemLookup.put(name, this);
        idItemLookup.put(id, this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Item))
            return false;
        else
            return (id == ((Item) o).id);
    }
    
    public static Item lookup(String name)
    {
        return nameItemLookup.get(name);
    }

    // Returns the list of names of *all* items matching the given type.
    public static String[] getItemsOfType(Class<?> type)
    {
        String[] allItems = nameItemLookup.keySet().toArray(new String[nameItemLookup.size()]);
        ArrayList<String> matchingItems = new ArrayList<String>();

        for (String key : allItems)
        {
            Item item = lookup(key);
            if (type.isInstance(item))
            {
                matchingItems.add(key);
            }
        }

        return matchingItems.toArray(new String[matchingItems.size()]);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
