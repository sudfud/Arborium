package com.mygdx.arborium.game;

import com.mygdx.arborium.Arborium;

public class Farm
{
    Plot[] plots;

    public Farm(Arborium game)
    {
        plots = new Plot[9];
        for (int i = 0; i < plots.length; i++)
            plots[i] = new Plot(i, game);
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
