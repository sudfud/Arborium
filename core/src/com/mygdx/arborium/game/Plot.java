package com.mygdx.arborium.game;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.items.Tree;
import com.mygdx.arborium.items.TreeList;

public class Plot
{
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

    public Plot(int num)
    {
        plotNumber = num;

        plantTimeKey = "Plot" + num + "PlantTime";
        harvestKey = "Plot" + num + "LastHarvest";
        emptyKey = "Plot" + num + "Empty";
        matureKey = "Plot" + num + "Mature";
        treeKey = "Plot" + num + "TreeType";

        empty = (!pref.contains(emptyKey)) || pref.getBoolean(emptyKey);

        if (empty)
        {
            plantedTree = null;
            mature = false;
            readyToHarvest = false;
        }
        else
        {
            String treeType = pref.getString(treeKey);
            plantedTree = TreeList.get(treeType);
            matureTime = plantedTree.getMatureTime();
            produceRate = plantedTree.getProduceRate();
            produceAmount = plantedTree.getProduceAmount();

            mature = pref.getBoolean(matureKey);

            plantedTime = pref.getLong(plantTimeKey);

            if (mature)
                timeSinceLastHarvest = pref.getLong(harvestKey);

            readyToHarvest = false;
        }
    }

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
        Inventory.addItem(plantedTree.getFruit().itemName, produceAmount);
        timeSinceLastHarvest = 0;
        lastHarvestTime = TimeUtils.millis();
        readyToHarvest = false;

        String key = "Plot" + plotNumber + "LastHarvest";
        pref.putLong(key, lastHarvestTime);
        pref.flush();
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

    public long getTimeSincePlanted()
    {
        return timeSincePlanted;
    }
}
