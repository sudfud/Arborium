package com.mygdx.arborium.game;

import com.badlogic.gdx.utils.TimeUtils;

public class Seed
{
    private int produceAmount;
    private long produceRate;

    private long matureTime;

    private long plantedTime;
    private long timeSincePlanted = 0;

    private long lastHarvestTime = 0;
    private long timeSinceLastHarvest = 0;

    private boolean mature = false;
    private boolean readyToHarvest = false;

    public Seed(int produceAmount, float produceRateMinutes, float matureTimeMinutes)
    {
        this.produceAmount = produceAmount;
        this.produceRate = (long)(produceRateMinutes * 1000 * 60);
        this.matureTime = (long)(matureTimeMinutes * 1000 * 60);
    }

    public void plant()
    {
        plantedTime = TimeUtils.millis();
    }

    // Call this in the render() method
    public void update()
    {
        timeSincePlanted = TimeUtils.timeSinceMillis(plantedTime);

        if (!mature && timeSincePlanted > matureTime)
        {
            mature = true;
            lastHarvestTime = System.currentTimeMillis();
        }

        else if (mature)
        {
            timeSinceLastHarvest = TimeUtils.timeSinceMillis(lastHarvestTime);

            if (timeSinceLastHarvest > produceRate)
            {
                readyToHarvest = true;
            }
        }
    }

    public void harvest()
    {
        for (int i = 0; i < produceAmount; i++)
        {

        }
        timeSinceLastHarvest = 0;
        lastHarvestTime = TimeUtils.millis();
        readyToHarvest = false;
    }

    public int getProduceAmount()
    {
        return produceAmount;
    }

    public long getProduceRate()
    {
        return produceRate;
    }

    public long getMatureTime()
    {
        return matureTime;
    }

    public long getPlantedTime() { return plantedTime; }

    public long getTimeSincePlanted() { return timeSincePlanted; }

    public long getTimeUntilMature() { return matureTime - (timeSincePlanted - plantedTime); }

    public long getTimeSinceLastHarvest() { return timeSinceLastHarvest; }

    public boolean isMature()
    {
        return mature;
    }

    public boolean isReadyToHarvest()
    {
        return readyToHarvest;
    }
}
