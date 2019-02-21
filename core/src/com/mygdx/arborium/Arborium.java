package com.mygdx.arborium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.game.TreeList;
import com.mygdx.arborium.screen.FarmScreen;
import com.mygdx.arborium.screen.PlotScreen;

public class Arborium extends Game
{
	public static Preferences preferences;

	public FarmScreen farmScreen;
	public PlotScreen[] plotScreens;

	public Farm farm;

	@Override
	public void create()
	{
		preferences = Gdx.app.getPreferences("Arborium Preferences");
		TreeList.initialize();

		farm = new Farm();

		Inventory inv = new Inventory();

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
