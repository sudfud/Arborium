package com.mygdx.arborium.game;

public class Plot
{
    boolean empty;

    Seed plantedTree;

    public Plot()
    {
        empty = true;
        plantedTree = null;
    }

    public void plantSeed(Seed seed)
    {
        plantedTree = seed;
        seed.plant();
        empty = false;
    }

    public void update()
    {
        if (plantedTree != null)
            plantedTree.update();
    }

    public boolean isEmpty()
    {
        return empty;
    }

    public Seed getPlantedTree()
    {
        return plantedTree;
    }
}
