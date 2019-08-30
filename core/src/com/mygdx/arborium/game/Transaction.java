package com.mygdx.arborium.game;

import com.mygdx.arborium.items.ShopItem;

public class Transaction
{
    private ShopItem transactionItem;
    private boolean isPurchase = false;
    private int quantity = 0;
    private int totalPrice = 0;

    public Transaction(ShopItem item, boolean purchase)
    {
        transactionItem = item;
        isPurchase = purchase;
    }

    public boolean isPurchase()
    {
        return isPurchase;
    }

    public void setPurchase(boolean purchase)
    {
        isPurchase = purchase;
    }

    public void setTransactionItem(ShopItem item)
    {
        transactionItem = item;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public int getTotalPrice()
    {
        return totalPrice;
    }

    public boolean changeQuantity(int offset)
    {
        int price = isPurchase? transactionItem.getBuyValue() : transactionItem.getSellValue();
        price *= offset;

        int offQuantity = quantity + offset;

        if (offQuantity < 0)
            return false;

        if (!isPurchase && offQuantity <= Inventory.getCount(transactionItem.name) || 
            (isPurchase && totalPrice + price <= Currency.getAmount()))
        {
            quantity = offQuantity;
            totalPrice += price;
            return true;
        }

        else 
            return false;
    }

    public void apply()
    {
        if (isPurchase)
        {
            Currency.subtract(totalPrice);
            Inventory.addItem(transactionItem.name, quantity);
        }
        else
        {
            Currency.add(totalPrice);
            Inventory.takeItem(transactionItem.name, quantity);
        }
    }

    public void reset()
    {
        quantity = 0;
        totalPrice = 0;
    }
}