package com.mygdx.arborium.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.mygdx.arborium.items.Item;

import java.util.ArrayList;
import com.mygdx.arborium.Arborium;

import java.util.HashMap;
import java.util.stream.Stream;

/*
 * The game's inventory is managed by a hashmap with String keys and Integer values.
 *
 * The keys in this case are the item names, and the values are the amounts of these Items
 * the user currently has.
 *
 * Items are 'added' to and 'removed' from the inventory by incrementing or decrementing the Item's
 * count respectively. If the 'last' of an Item is taken, the Item's key is removed from the hashmap.
 *
 * Whenever the inventory is changed in any way, it should be serialized and updated in the game's
 * preferences.
 *
 */

public class Inventory
{
    private static Arborium game;

    private ArrayList<Item> inventoryList;

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

    public Inventory()
    {
        inventoryList = new ArrayList<Item>();
    }

    public static void initialize(Arborium g)
    {
        game = g;

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

    public void add(Item item)
    {
        inventoryList.add(item);
    }

    public void remove(Item item)
    {
        inventoryList.remove(item);
    }

    public int getItemQuantity(Item item)
    {
        return (int)inventoryList.stream()
            .filter(i -> i == item)
            .count();

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
        takeItem(itemName, 1);
    }

    public static void takeItem(String itemName, int count)
    {
        if (inventory.containsKey(itemName))
        {
            int amt = inventory.get(itemName);
            if (amt - count < 1)
                inventory.remove(itemName);
            else
                inventory.put(itemName, amt - count);
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

    // Returns a list of names of all items *currently in inventory* that matches the given type
    public static String[] getItemsOfType(Class<?> type)
    {
        String[] keys = inventory.keySet().toArray(new String[inventory.size()]);
        ArrayList<String> matchingKeys = new ArrayList<String>();
        for (String key : keys)
        {
            if (type.isInstance(Item.lookup(key)))
            {
                matchingKeys.add(key);
            }
        }
        return matchingKeys.toArray(new String[matchingKeys.size()]);
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
