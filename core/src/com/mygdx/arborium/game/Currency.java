package com.mygdx.arborium.game;

import com.badlogic.gdx.Preferences;
import com.mygdx.arborium.Arborium;


public class Currency
{
    private static int amount;
    private static String key = "CurrencyValue";

    private static Preferences pref;

    public static void initialize()
    {
        pref = Arborium.preferences;

        // Set currency to last updated value, or 500 if none is found
        if (pref.contains(key))
            amount = pref.getInteger(key);
        else
            amount = 500;

        updatePreferences();
    }

    public static void add(int amt)
    {
        amount += amt;
        updatePreferences();
    }

    // Will return false if there's not enough money
    public static boolean subtract(int amt)
    {
        if (amount - amt < 0)
            return false;
        else
        {
            amount -= amt;
            updatePreferences();
            return true;
        }
    }

    public static int getAmount()
    {
        return amount;
    }

    private static void updatePreferences()
    {
        pref.putInteger(key, amount);
        pref.flush();
    }
}
