package com.mygdx.arborium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.items.FruitList;
import com.mygdx.arborium.items.SeedList;
import com.mygdx.arborium.items.TreeList;
import com.mygdx.arborium.screen.FarmScreen;
import com.mygdx.arborium.screen.MainMenuScreen;
import com.mygdx.arborium.screen.PlotScreen;
import com.mygdx.arborium.screen.ShopScreen;

public class Arborium extends Game
{
	public static Preferences preferences;

	public SpriteBatch spriteBatch;

	public FarmScreen farmScreen;
	public PlotScreen[] plotScreens;
	public MainMenuScreen mainMenuScreen;
	public ShopScreen shopScreen;

	public Farm farm;

	@Override
	public void create()
	{
		preferences = Gdx.app.getPreferences("Arborium Preferences");

		spriteBatch = new SpriteBatch();

		Resources.initialize();
		Currency.initialize();
		SeedList.initialize();
		TreeList.initialize();
		FruitList.initialize();
		Inventory.initialize();

		farm = new Farm();

		farmScreen = new FarmScreen(this);

		plotScreens = new PlotScreen[farm.getPlotSize()];
		for (int i = 0; i < farm.getPlotSize(); i++)
		{
			PlotScreen scrn = new PlotScreen(this, farm.getPlot(i));
			plotScreens[i] = scrn;
		}

		mainMenuScreen = new MainMenuScreen(this);
		shopScreen = new ShopScreen(this);

		setScreen(mainMenuScreen);
	}

	@Override
	public void render()
	{
		super.render();
	}

    @Override
    public void dispose()
    {
        Resources.dispose();
        spriteBatch.dispose();
    }
}
