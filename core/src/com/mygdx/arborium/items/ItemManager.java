package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.arborium.Arborium;

import java.util.ArrayList;
import java.util.List;

public class ItemManager
{
    public Texture[] itemTextures;

    String[] saplingNames = 
        {
            "Apple Sapling", 
            "Orange Sapling", 
            "Cherry Sapling",
            "Peach Sapling"
        };
        
        String[] saplingSpriteDirs = 
        {
            Arborium.APPLE_TREE2X, 
            Arborium.ORANGE_TREE2X, 
            Arborium.CHERRY_TREE,
            Arborium.PEACH_TREE
        };

        int[] saplingPrices = 
        {
            250,        // Apple
            1000,       // Orange
            5000,       // Cherry
            40000       // Peach
        };

        boolean[] saplingLocks = 
        {
            false,      // Apple
            false,      // Orange
            true,       // Cherry
            true        // Peach
        };

        long[] matureTimeMinutes = 
        {
            15,     // Apple
            35,     // Orange
            50,     // Cherry
            20,     // Peach
        };

        long[] produceTimeMinutes = 
        {
            7,      // Apple
            35,     // Orange
            45,     // Cherry
            90      // Peach
        };

        int[] produceAmounts = 
        {
            1,      // Apple
            2,      // Orange
            7,      // Cherry
            5,      // Peach
        };

        
        // Fruit properties
        private String[] fruitNames = 
        {
            "Apple",
            "Orange",
            "Cherry",
            "Peach"
        };

        private String[] fruitSpriteDirs = 
        {
            Arborium.APPLE_FRUIT,
            Arborium.ORANGE_FRUIT,
            Arborium.CHERRY_FRUIT,
            Arborium.PEACH_4X
        };

        private int[] fruitSellPrices = 
        {
            10,     // Apple
            30,     // Orange
            25,     // Cherry
            100     // Peach
        };


    private ArrayList<Sapling> saplings = new ArrayList<Sapling>();

    public ItemManager(Arborium game)
    {
        itemTextures = new Texture[128];

        for (int id = 1; id < saplingNames.length; id++)
        {
            Sapling sapling = new Sapling(
                id, saplingNames[id], game.getTexture(saplingSpriteDirs[id]),saplingPrices[id], saplingLocks[id],
                matureTimeMinutes[id] * 60 * 1000,
                produceTimeMinutes[id] * 60 * 1000,
                produceAmounts[id]);

            saplings.add(sapling);
        }


    }
}
