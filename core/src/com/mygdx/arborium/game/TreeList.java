package com.mygdx.arborium.game;

import java.util.HashMap;

public class TreeList
{
    private static HashMap<String, Tree> treeMap = new HashMap<String, Tree>();

    public static Tree appleTree = new Tree(1, "Apple Tree", 5, 1, 5, FruitList.apple);

    public static void initialize()
    {
        treeMap.put(appleTree.itemName, appleTree);
    }

    public static Tree get(String id)
    {
        return treeMap.get(id);
    }
}
