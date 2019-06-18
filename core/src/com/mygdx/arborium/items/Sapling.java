package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public class Sapling extends ShopItem
{
    public static class Builder
    {
        private int id;
        private String name;
        private Texture image;
        private String description;
        private int buyValue;
        private boolean locked;

        private long matureTime;

        private Fruit produce;
        private long produceRate;
        private int produceAmount;

        public Builder(int id, String name, Texture image, String description, int buyValue, boolean locked)
        {
            this.id = id;
            this.name = name;
            this.image = image;
            this.description = description;
            this.buyValue = buyValue;
            this.locked = locked;
        }

        public Builder matureTime(long matureTime)
        {
            this.matureTime = matureTime;
            return this;
        }

        public Builder produce(Fruit produce)
        {
            this.produce = produce;
            return this;
        }

        public Builder produceRate(long produceRate)
        {
            this.produceRate = produceRate;
            return this;
        }

        public Builder produceAmount(int produceAmount)
        {
            this.produceAmount = produceAmount;
            return this;
        }

        public Sapling build()
        {
            Sapling sapling = new Sapling(id, name, description, image, buyValue, locked);
            sapling.matureTime = this.matureTime;
            sapling.produce = this.produce;
            sapling.produceAmount = this.produceAmount;
            sapling.produceRate = this.produceRate;

            return sapling;
        }
    }

    long matureTime;
    long produceRate;
    int produceAmount;

    Fruit produce;

    public Sapling(int id, String name, String description, Texture image, int buyValue, boolean locked)
    {
        super(id, name, image, description, buyValue, buyValue / 3, locked);
    }

    public long getMatureTime()
    {
        return matureTime;
    }

    public long getProduceRate()
    {
        return produceRate;
    }

    public int getProduceAmount()
    {
        return produceAmount;
    }

    public Fruit getProduce()
    {
        return produce;
    }
}
