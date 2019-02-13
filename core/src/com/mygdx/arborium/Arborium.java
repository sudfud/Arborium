package com.mygdx.arborium;

import com.badlogic.gdx.Game;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.screen.FarmScreen;
import com.mygdx.arborium.screen.PlotScreen;

public class Arborium extends Game
{
	public FarmScreen farmScreen;
	public PlotScreen[] plotScreens;

	public Farm farm;

	@Override
	public void create()
	{
		farm = new Farm();

		farmScreen = new FarmScreen(this);

		plotScreens = new PlotScreen[farm.getPlotSize()];
		for (int i = 0; i < farm.getPlotSize(); i++)
		{
			PlotScreen scrn = new PlotScreen(this, farm.getPlot(i));
			plotScreens[i] = scrn;
		}

		setScreen(new FarmScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}


}
