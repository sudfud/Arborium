package com.mygdx.arborium.game;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.items.Fruit;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Sapling;
import com.mygdx.arborium.items.SaplingList;

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
    String plotStateKey;
    String fruitKey;

    private int plotNumber;

    int produceAmount = -1;
    long produceRate = -1;

    long matureTime = -1;

    long plantedTime;
    long timeSincePlanted = 0;

    long lastHarvestTime = 0;
    long timeSinceLastHarvest = 0;

    Sapling plantedTree = null;

    Farm farm;

    public enum PlotState
    {
        EMPTY(0),
        PREMATURE(1),
        MATURE(2),
        HARVESTABLE(3);

        public final int value;
        private PlotState(int value)
        {
            this.value = value;
        }
    }

    PlotState currentState;

    public Plot(Arborium game, int num, Farm farm)
    {
        plotNumber = num;
        this.game = game;
        this.farm = farm;

        String farmTag = farm.name;

        updateState(PlotState.EMPTY);

        plantTimeKey = farmTag + "Plot" + num + "PlantTime";
        harvestKey = farmTag + "Plot" + num + "LastHarvest";
        plotStateKey = farmTag + "Plot" + num + "State";
        fruitKey = farmTag + "Plot" + num + "Sapling";

        int savedState = pref.getInteger(plotStateKey, 0);
        updateState(PlotState.values()[savedState]);

        // If it was, retrieve the plot's state information from the game's preferences.
        if (currentState != PlotState.EMPTY)
        {
            String saplingName = pref.getString(fruitKey);
            plantedTree = (Sapling)Item.lookup(saplingName);
            matureTime = plantedTree.getMatureTime();
            produceRate = plantedTree.getProduceRate();
            produceAmount = plantedTree.getProduceAmount();

            // mature = pref.getBoolean(matureKey);

            plantedTime = pref.getLong(plantTimeKey);

            if (currentState == PlotState.PREMATURE)
            {
                timeSincePlanted = TimeUtils.timeSinceMillis(plantedTime);
                if (timeSincePlanted > matureTime)
                    updateState(PlotState.MATURE);
            }

            if (currentState == PlotState.MATURE)
            {
                lastHarvestTime = pref.getLong(harvestKey);
                timeSinceLastHarvest = TimeUtils.timeSinceMillis(lastHarvestTime);
                if (timeSinceLastHarvest >= produceRate)
                    updateState(PlotState.HARVESTABLE);
            }
        }
    }

    //
    public void plantSapling(Sapling tree)
    {
        produceAmount = tree.getProduceAmount();
        produceRate = tree.getProduceRate();
        matureTime = tree.getMatureTime();
        plantedTime = TimeUtils.millis();
        updateState(PlotState.PREMATURE);

        plantedTree = tree;

        pref.putLong(plantTimeKey, plantedTime);
        pref.putLong(harvestKey, plantedTime);
        pref.putString(fruitKey, tree.itemName);
        pref.flush();
    }

    // Call this in the render() method
    public void update()
    {
        switch(currentState)
        {
            case EMPTY:
                break;

            case PREMATURE:
                timeSincePlanted = TimeUtils.timeSinceMillis(plantedTime);
                if (timeSincePlanted > matureTime)
                {
                    //mature = true;
                    updateState(PlotState.MATURE);
                    lastHarvestTime = TimeUtils.millis();
                }
                break;

            case MATURE:
            case HARVESTABLE:
                timeSinceLastHarvest = TimeUtils.timeSinceMillis(lastHarvestTime);
                if (currentState != PlotState.HARVESTABLE && timeSinceLastHarvest > produceRate)
                {
                    updateState(PlotState.HARVESTABLE);
                }
                break;
        }
    }

    public void harvest()
    {
        if (currentState == PlotState.HARVESTABLE)
        {
            timeSinceLastHarvest = 0;
            lastHarvestTime = TimeUtils.millis();

            updateState(PlotState.MATURE);
            Inventory.addItem(plantedTree.getProduce().itemName, plantedTree.getProduceAmount());
            pref.putLong(harvestKey, lastHarvestTime);
            pref.flush();
        }
    }

    private void updateState(PlotState state)
    {
        currentState = state;
        pref.putInteger(plotStateKey, currentState.value);
        pref.flush();
    }

    public PlotState getCurrentState()
    {
        return currentState;
    }

    public long getMatureTime()
    {
        return matureTime;
    }

    public Sapling getPlantedTree()
    {
        return plantedTree;
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
