package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

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

    private static HashMap<String, Item> nameItemLookup = new HashMap<String, Item>();
    private static HashMap<Integer, Item> idItemLookup = new HashMap<Integer, Item>();

    protected HashMap<String, Object> properties;

    public Item(int id, String name)
    {
        this.id = id;
        this.itemName = name;

        properties = new HashMap<String, Object>();
        properties.put("id", id);
        properties.put("name", name);

        nameItemLookup.put(this.itemName, this);
        idItemLookup.put(this.id, this);
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

    public Object getProperty(String key)
    {
        return properties.get(key);
    }

    public Item setProperty(String key, Object value)
    {
        properties.put(key, value);
        return this;
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
        return itemName;
    }
}
