package com.mygdx.arborium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.items.FruitList;
import com.mygdx.arborium.items.SeedList;
import com.mygdx.arborium.items.TreeList;
import com.mygdx.arborium.screen.FarmScreen;
import com.mygdx.arborium.screen.LoadingScreen;
import com.mygdx.arborium.screen.MainMenuScreen;
import com.mygdx.arborium.screen.PlotScreen;
import com.mygdx.arborium.screen.ShopScreen;


/*
 * This class maintains all of the game's screens and is used to change between screens.
 *
 * It also contains a number of utilities used throughout the game and its screens, including:
 * resources, sprite batch for drawing, item lists, and preferences for saving game state information.
 *
 */

public class Arborium extends Game
{
	public static final String GLASSY_SKIN = "glassy-ui.json";

    public static final String BG_SKY = "background_sky.png";
    public static final String GRASS = "grass.png";
    public static final String DIRT_PATCH = "dirtpatch.png";
    public static final String DIRT_PLOT = "dirtplot.png";

    public static final String TREE_OVERWORLD = "tree-overworld.png";

    public static final String TREE_1_ADULT = "tree1_adult.png";
    public static final String TREE_1_YOUNG_ADULT = "tree1_youngadult.png";
    public static final String TREE_1_TEENAGER = "tree1_teenager.png";
    public static final String TREE_1_CHILD = "tree1_child.png";
    public static final String TREE_1_BABY = "tree1_baby.png";

    public static final String APPLE_SEED = "seed1.png";
    public static final String ORANGE_SEED = "seed2.png";

    public static final String APPLE_FRUIT = "fruits/red-apple.png";
	public static final String ORANGE_FRUIT = "fruits/orange.png";
	
	public static final String BASKET = "basket.png";

    public static final String TREE_ANIM = "Tree2/sprite_tree_2";

	public int GDX_WIDTH;
	public int GDX_HEIGHT;

	public static Preferences preferences;

	public SpriteBatch spriteBatch;

	public AssetManager assetManager;

	public FruitList fruitList;
	public TreeList treeList;
	public SeedList seedList;

	public LoadingScreen loadingScreen;
	public FarmScreen farmScreen;
	public PlotScreen plotScreen;
	public MainMenuScreen mainMenuScreen;
	public ShopScreen shopScreen;

	public Farm[] mediumFarms;
	public Farm[] smallFarms;
	public Farm largeFarm;

	private boolean notificationsEnabled = true;

	NotificationHandler notificationHandler;

	@Override
	public void create()
	{
		GDX_WIDTH = Gdx.graphics.getWidth();
		GDX_HEIGHT = Gdx.graphics.getHeight();

		preferences = Gdx.app.getPreferences("Arborium Preferences");

		spriteBatch = new SpriteBatch();

		assetManager = new AssetManager();

		Currency.initialize();
		Inventory.initialize(this);

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	public void initializeScreens()
	{
		// fruitList should be initialized first and seedList last.
		// treeList depends on fruitList and seedList depends on treeList.
		fruitList = new FruitList(this);
		treeList = new TreeList(this);
		seedList = new SeedList(this);

		mediumFarms = new Farm[2];
		for (int i = 0; i < mediumFarms.length; i++)
		{
			mediumFarms[i] = new Farm(this, "MediumFarm" + i, true, 8);
		}
		mediumFarms[0].setLock(false);

		smallFarms = new Farm[4];
		for (int i = 0; i < smallFarms.length; i++)
		{
			smallFarms[i] = new Farm(this, "SmallFarm" + i, true, 4);
		}

		largeFarm = new Farm(this, "LargeFarm0", true, 16);

		farmScreen = new FarmScreen(this);

		plotScreen = new PlotScreen(this, mediumFarms[0].getPlot(0));

		mainMenuScreen = new MainMenuScreen(this);
		shopScreen = new ShopScreen(this);
	}

	@Override
	public void render()
	{
		super.render();
	}

    @Override
    public void dispose()
    {
    	assetManager.dispose();
        spriteBatch.dispose();

        mainMenuScreen.dispose();
        shopScreen.dispose();
		farmScreen.dispose();
		plotScreen.dispose();
    }

    public void setNotificationHandler(NotificationHandler handler)
	{
		this.notificationHandler = handler;
	}

	public NotificationHandler getNotificationHandler()
	{
		return notificationHandler;
	}

	public boolean isNotificationEnabled()
	{
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(boolean enabled)
	{
		this.notificationsEnabled = enabled;
	}

	public Skin getSkin(String fileName)
    {
        return assetManager.get(fileName, Skin.class);
    }

    public Texture getTexture(String fileName)
    {
        return assetManager.get(fileName, Texture.class);
    }
}
