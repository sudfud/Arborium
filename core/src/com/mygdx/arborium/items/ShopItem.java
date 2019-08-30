package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;

public abstract class ShopItem extends SpriteItem
{
   private int buyValue;
   private int sellValue;

   private boolean locked;

   private String description;

    public ShopItem(int id, String name, Texture image, String description, int buyValue, int sellValue, boolean locked)
    {
        super(id, name, image);
        this.description = description;
        this.buyValue = buyValue;
        this.sellValue = sellValue;
        this.locked = locked;
    }

    public ShopItem(int id, String name, Texture image)
    {
        super(id, name, image);
        this.description = "";
        this.buyValue = -1;
        this.sellValue = -1;
        this.locked = false;
    }

    public ShopItem(SpriteItem item)
    {
        super(item.id, item.name, item.sprite);
        this.description = "";
        this.buyValue = -1;
        this.sellValue = -1;
        this.locked = false;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String desc)
    {
        description = desc;
    }

    public void setBuyValue(int buy)
    {
        this.buyValue = buy;
    }

    public int getBuyValue()
    {
        return buyValue;
    }

    public void setSellValue(int sell)
    {
        sellValue = sell;
    }

    public int getSellValue()
    {
        return sellValue;
    }

    public void setLock(boolean lock)
    {
        locked = lock;
    }

    public boolean isLocked()
    {
        return locked;
    }
}
