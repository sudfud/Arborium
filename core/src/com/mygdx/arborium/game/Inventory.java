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

    private static Preferences pref = Gdx.app.getPreferences("Arborium Inventory");
    private static Json json;

    public enum InventoryCategory
    {
        SEED,
        FRUIT
    }
    public class InventoryItemEntry
    {
        Item item;
        int count;

        public InventoryItemEntry(Item item)
        {
            this.item = item;
            count = 0;
        }

        public void incCount()
        {
            count++;
        }

        public void decCount()
        {
            count--;
        }
    }

    int maxCapacity = 50;
    int inventoryCount = 0;

    public static void initialize()
    {
        json = new Json();

        if (!pref.contains("Inventory"))
            inventory = new HashMap<String, Integer>();
        else
        {
            String serializedInventory;
            serializedInventory = pref.getString("Inventory");
            inventory = json.fromJson(HashMap.class, serializedInventory);
            Gdx.app.log("Inventory", inventory.keySet().toString());
        }
        inventory = new HashMap<String, Integer>();
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
        Gdx.app.log("Inventory", "" + inventory.get(itemName));

        pref.putInteger("InvCount" + itemName, inventory.get(itemName));
        pref.flush();
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
        Gdx.app.log("Inventory", "AAAAAAA" + inventory.get(itemName));
        String serialized = json.toJson(inventory);
        pref.putString("Inventory", serialized);
        pref.flush();
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
