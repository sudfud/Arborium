package com.mygdx.arborium.items;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * This base class represents all items used in the game. An "Item" can be considered as anything
 * that holds some form of data to be utilized by the user.
 *
 * Subclasses: SpriteItem, Tree
 *
 */

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

    // Returns the list of names of *all* items matching the given type.
    public static String[] getItemsOfType(Class<?> type)
    {
        String[] allItems = itemLookup.keySet().toArray(new String[itemLookup.size()]);
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
}
