package com.mygdx.arborium.game;

import com.mygdx.arborium.Arborium;

/*
 * Container for some number of plots (Current default is 9).
 */

public class Farm
{
    // This will be used for saving plot data in preferences
    public final String name;

    private boolean locked;

    Plot[] plots;

    public Farm(Arborium game, String name, boolean locked)
    {
        this.name = name;
        this.locked = locked;

        plots = new Plot[9];
        for (int i = 0; i < plots.length; i++)
            plots[i] = new Plot(game, i, this);
    }

    public Farm(Arborium game, String name, boolean locked, int plotCount)
    {
        this.name = name;
        this.locked = locked;

        plots = new Plot[plotCount];
        for (int i = 0; i < plots.length; i++)
            plots[i] = new Plot(game, i, this);
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

    public boolean isLocked()
    {
        return locked;
    }

    public void setLock(boolean lock)
    {
        locked = lock;
    }
}
