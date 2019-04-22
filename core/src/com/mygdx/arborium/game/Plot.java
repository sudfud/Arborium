package com.mygdx.arborium.game;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.items.Tree;
import com.mygdx.arborium.items.TreeList;

/*
 * The Plot class handles Tree growth, maturity, and production. Plots can be empty, or they can
 * contain a single planted Tree.
 *
 * If a Tree is planted, it is this class' job to keep track of the tree's state as well as the time
 * relative to when the Tree was planted, when it will mature, and subsequently when it will be
 * ready to be harvested.
 *
 */

public class Plot
{
    private Arborium game;

    private Preferences pref = Arborium.preferences;
    String plantTimeKey;
    String harvestKey;
    String emptyKey;
    String matureKey;
    String treeKey;

    private int plotNumber;

    boolean empty;

    int produceAmount = -1;
    long produceRate = -1;

    long matureTime = -1;

    long plantedTime;
    long timeSincePlanted = 0;

    long lastHarvestTime = 0;
    long timeSinceLastHarvest = 0;

    boolean mature;
    boolean readyToHarvest;

    Tree plantedTree;
    Farm farm;

    public Plot(Arborium game, int num, Farm farm)
    {
        plotNumber = num;
        this.game = game;
        this.farm = farm;

        String farmTag = farm.name;

        plantTimeKey = farmTag + "Plot" + num + "PlantTime";
        harvestKey = farmTag + "Plot" + num + "LastHarvest";
        emptyKey = farmTag + "Plot" + num + "Empty";
        matureKey = farmTag + "Plot" + num + "Mature";
        treeKey = farmTag + "Plot" + num + "TreeType";

        // Check to see if this plot was already occupied in a previous run.
        empty = (!pref.contains(emptyKey)) || pref.getBoolean(emptyKey);

        if (empty)
        {
            plantedTree = null;
            mature = false;
            readyToHarvest = false;
        }

        // If it was, retrieve the plot's state information from the game's preferences.
        else
        {
            String treeType = pref.getString(treeKey);
            plantedTree = game.treeList.get(treeType);
            matureTime = plantedTree.getMatureTime();
            produceRate = plantedTree.getProduceRate();
            produceAmount = plantedTree.getProduceAmount();

            mature = pref.getBoolean(matureKey);

            plantedTime = pref.getLong(plantTimeKey);

            if (mature)
            {
                lastHarvestTime = pref.getLong(harvestKey);
            }

            readyToHarvest = false;
        }
    }

    //
    public void plantSeed(Tree tree)
    {
        plantedTree = tree;
        produceAmount = plantedTree.getProduceAmount();
        produceRate = plantedTree.getProduceRate();
        matureTime = plantedTree.getMatureTime();
        plantedTime = TimeUtils.millis();
        empty = false;

        pref.putBoolean(emptyKey, false);
        pref.putBoolean(matureKey, false);
        pref.putLong(plantTimeKey, plantedTime);
        pref.putString(treeKey, plantedTree.itemName);
        pref.putLong(harvestKey, plantedTime);
        pref.flush();
    }

    // Call this in the render() method
    public void update()
    {
        if (!empty)
        {
            timeSincePlanted = TimeUtils.timeSinceMillis(plantedTime);

            if (!mature && timeSincePlanted > matureTime)
            {
                mature = true;
                lastHarvestTime = TimeUtils.millis();
                pref.putBoolean(matureKey, true);
                pref.flush();
            }

            else if (mature)
            {
                timeSinceLastHarvest = TimeUtils.timeSinceMillis(lastHarvestTime);

                if (timeSinceLastHarvest > produceRate)
                    readyToHarvest = true;
            }
        }
    }

    public void harvest()
    {
        if (readyToHarvest)
        {
            timeSinceLastHarvest = 0;
            lastHarvestTime = TimeUtils.millis();
            readyToHarvest = false;

            pref.putLong(harvestKey, lastHarvestTime);
            pref.flush();
        }
    }

    public boolean isEmpty()
    {
        return empty;
    }

    public Tree getPlantedTree()
    {
        return plantedTree;
    }

    public boolean isReadyToHarvest()
    {
        return readyToHarvest;
    }

    public boolean isMature()
    {
        return mature;
    }

    public long getTimeSinceLastHarvest()
    {
        return timeSinceLastHarvest;
    }

    public long getLastHarvestTime()
    {
        return lastHarvestTime;
    }

    public long getTimeSincePlanted()
    {
        return timeSincePlanted;
    }

    public long getProduceRate()
    {
        return produceRate;
    }

    public Farm getFarm()
    {
        return farm;
    }
}
