package com.mygdx.arborium.game;

public class Farm
{
    Plot[] plots;

    public Farm()
    {
        plots = new Plot[9];
        for (int i = 0; i < plots.length; i++)
            plots[i] = new Plot(i);
    }

    public void update()
    {
        for (int i = 0; i < plots.length; i++)
            plots[i].update();
    }

    public Plot getPlot(int index)
    {
        return plots[index];
    }

    public int getPlotSize()
    {
        return plots.length;
    }
}
