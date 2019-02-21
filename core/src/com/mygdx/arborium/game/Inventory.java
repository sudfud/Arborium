package com.mygdx.arborium.game;

import java.util.HashMap;

public class Inventory
{
    static HashMap<Item, Integer> inventory;

    public Inventory()
    {
        inventory = new HashMap<Item, Integer>();
    }

    public static void addItem(Item item)
    {
        if (inventory.containsKey(item))
        {
            int amt = inventory.get(item);
            inventory.put(item, amt + 1);
        }
        else
        {
            inventory.put(item, 1);
        }
    }

    public static void takeItem(Item item)
    {
        if (inventory.containsKey(item))
        {
            int amt = inventory.get(item);
            if (amt == 1)
                inventory.remove(item);
            else
                inventory.put(item, amt - 1);
        }
    }

    public static boolean containsItem(Item item)
    {
        return inventory.containsKey(item);
    }
}
