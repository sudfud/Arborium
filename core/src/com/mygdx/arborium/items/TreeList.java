package com.mygdx.arborium.items;

import java.util.HashMap;

public class TreeList
{
    private static HashMap<String, Tree> treeMap = new HashMap<String, Tree>();

    public static final Tree appleTree = new Tree(1, "Apple Tree", 5, 60, 120, FruitList.apple);
    public static final Tree orangeTree = new Tree(2, "Orange Tree", 7, 120, 240, FruitList.orange);

    public static void initialize()
    {
        treeMap.put(appleTree.itemName, appleTree);
        treeMap.put(orangeTree.itemName, orangeTree);
    }

    public static Tree get(String id)
    {
        return treeMap.get(id);
    }
}
