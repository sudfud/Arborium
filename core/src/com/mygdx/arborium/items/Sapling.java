package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class Sapling extends ShopItem
{
    private long matureTime;
    private long produceRate;
    private int produceAmount;

    Fruit produce;

    public Sapling(int id, String name, Texture image, int buyValue, boolean locked, long matureTime, long produceRate, int produceAmount)
    {
        super(id, name, image, "", buyValue, buyValue / 3, locked);
    }

    public Sapling(int id, String name, Texture sprite)
    {
        super(id, name, sprite);

        matureTime = -1;
        produceRate = -1;
        produceAmount = -1;
    }

    public Sapling(SpriteItem item, int buyValue)
    {
        super(item);
        setBuyValue(buyValue);
        setSellValue(-1);
        setLock(false);

        matureTime = -1;
        produceRate = -1;
        produceAmount = -1;
    }

    public Sapling(SpriteItem item, int buyValue, boolean locked)
    {
        super(item);
        setBuyValue(buyValue);
        setLock(locked);
    }

    public Sapling(SpriteItem item, long matureTime, long produceRate, int produceAmount, Fruit produce)
    {
        super(item);

    }

    public void setMatureTime(long millis)
    {
        matureTime = millis;
    }
    public long getMatureTime()
    {
        return matureTime;
    }


    public void setProduceRate(long millis)
    {
        produceRate = millis;
    }
    public long getProduceRate()
    {
        return produceRate;
    }


    public void setProduceAmount(int amount)
    {
        produceAmount = amount;
    }
    public int getProduceAmount()
    {
        return produceAmount;
    }

    public void setProduce(Fruit fruit)
    {
        produce = fruit;
    }
    public Fruit getProduce()
    {
        return produce;
    }
}
