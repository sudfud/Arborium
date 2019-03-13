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

	public Resources resources;

	public FruitList fruitList;
	public TreeList treeList;
	public SeedList seedList;

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

		resources = new Resources();

		// fruitList should be initialized first and seedList last.
		// treeList depends on fruitList and seedList depends on treeList.
		fruitList = new FruitList(this);
		treeList = new TreeList(this);
		seedList = new SeedList(this);

		Currency.initialize();
		Inventory.initialize(this);

		farm = new Farm(this);

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
    	resources.dispose();
        spriteBatch.dispose();

        mainMenuScreen.dispose();
        shopScreen.dispose();
		farmScreen.dispose();
		for (int i = 0; i < plotScreens.length; i++)
		{
			plotScreens[i].dispose();
		}
    }
}
