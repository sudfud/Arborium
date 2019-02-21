package com.mygdx.arborium.game;

import com.badlogic.gdx.utils.TimeUtils;

public class Tree extends Item
{
    private int produceAmount;
    private long produceRate;

    private long matureTime;

    private Fruit fruit;

    public Tree(int id, String name, int produceAmount, float produceRateMinutes, float matureTimeMinutes, Fruit fruit)
    {
        super(id, name);
        this.produceAmount = produceAmount;
        this.produceRate = (long)(produceRateMinutes * 1000 * 60);
        this.matureTime = (long)(matureTimeMinutes * 1000 * 60);
        this.fruit = fruit;
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

    public Fruit getFruit()
    {
        return fruit;
    }
}
