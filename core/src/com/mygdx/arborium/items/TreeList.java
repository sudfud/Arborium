package com.mygdx.arborium.items;

import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;

import java.util.HashMap;

public class TreeList
{
    private HashMap<String, Tree> treeMap = new HashMap<String, Tree>();

    public final Tree appleTree;
    public final Tree orangeTree;

    public TreeList(Arborium game)
    {
        appleTree = new Tree(1, "Apple Tree", 5, 3f, 12f, game.fruitList.apple);
        orangeTree = new Tree(2, "Orange Tree", 7, 9, 6, game.fruitList.orange);

        treeMap.put(appleTree.itemName, appleTree);
        treeMap.put(orangeTree.itemName, orangeTree);
    }

    public Tree get(String id)
    {
        return treeMap.get(id);
    }
}
