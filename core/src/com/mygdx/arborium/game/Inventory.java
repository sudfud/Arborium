package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.SeedList;

import java.util.ArrayList;
import com.mygdx.arborium.Arborium;

import java.util.HashMap;
import java.util.List;

public class Inventory
{
    private static HashMap<String, Integer> inventory;

    private static Preferences pref = Arborium.preferences;
    private static Json json;

    public enum InventoryCategory
    {
        SEED,
        FRUIT
    }

    int maxCapacity = 50;
    int inventoryCount = 0;

    public static void initialize()
    {
        json = new Json();
        Gdx.app.log("Inventory", pref.get().toString());

        if (!pref.contains("Inventory"))
            inventory = new HashMap<String, Integer>();
        else
        {
            String serializedInventory;
            serializedInventory = pref.getString("Inventory");
            inventory = json.fromJson(HashMap.class, serializedInventory);
            Gdx.app.log("Inventory", inventory.keySet().toString());
        }
        pref = Arborium.preferences;
    }

    public static void addItem(String itemName, int count)
    {

        if (inventory.containsKey(itemName))
        {
            int amt = inventory.get(itemName);
            inventory.put(itemName, amt + count);
        }
        else
        {
            inventory.put(itemName, count);
        }

        updateInventory();
    }

    public static void takeItem(String itemName)
    {
        if (inventory.containsKey(itemName))
        {
            int amt = inventory.get(itemName);
            if (amt == 1)
                inventory.remove(itemName);
            else
                inventory.put(itemName, amt - 1);
        }
        updateInventory();
    }

    public static boolean containsItem(Item item)
    {
        return inventory.containsKey(item);
    }

    public static int getCount(String name)
    {
        return inventory.get(name);
    }

    public static String[] getItems()
    {
        return inventory.keySet().toArray(new String[inventory.size()]);
    }

    public static String[] getItems(InventoryCategory cat)
    {
        ArrayList<String> records = new ArrayList<String>();
        Gdx.app.log("Inventory", inventory.keySet().toString());
        switch(cat)
        {
            case SEED:
                for (String seed : SeedList.getSeedNames())
                {
                    if (inventory.containsKey(seed))
                        records.add(seed);
                }
                break;
        }

        return records.toArray(new String[records.size()]);
    }

    public static boolean isEmpty()
    {
        return inventory.isEmpty();
    }

    private static void updateInventory()
    {
        String serialized = json.toJson(inventory);
        pref.putString("Inventory", serialized);
        pref.flush();
    }
}
