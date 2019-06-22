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
import com.mygdx.arborium.items.SaplingList;
import com.mygdx.arborium.screen.LoadingScreen;
import com.mygdx.arborium.screen.MainMenuScreen;
import com.mygdx.arborium.screen.NewFarmScreen;
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
	public static final String ARBOR_SKIN = "arborium_ui.json";

	public static final String CLOUD = "cloud (1).png";

    public static final String BG_SKY = "background_sky.png";
    public static final String GRASS = "grass.png";
    public static final String DIRT_PATCH = "dirtpatch.png";
	public static final String PLOT = "plot.png";
	public static final String PLOT2X = "plot2x.png";

    public static final String TREE_OVERWORLD = "tree-overworld.png";

	public static final String PLANT = "plant2x.png";
	public static final String TREE_DEFAULT = "tree.png";
	public static final String TREE_DEFAULT2X = "tree2x.png";

	public static final String APPLE_TREE = "apple_tree.png";
	public static final String APPLE_TREE2X = "apple_tree2x.png";

	public static final String ORANGE_TREE = "orange_tree.png";
	public static final String ORANGE_TREE2X = "orange_tree2x.png";

    public static final String APPLE_SEED = "seed1.png";
    public static final String ORANGE_SEED = "seed2.png";

    public static final String APPLE_FRUIT = "apple2x.png";
	public static final String ORANGE_FRUIT = "orange2x.png";
	
	public static final String BASKET = "basket.png";

	public static final String TREE_ANIM = "Tree2/sprite_tree_2";
	
	public static final String COIN = "coin.png";

	public int GDX_WIDTH;
	public int GDX_HEIGHT;

	public static Preferences preferences;

	public SpriteBatch spriteBatch;

	public AssetManager assetManager;

	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public NewFarmScreen farmScreen;
	public ShopScreen shopScreen;

	public Farm[] mediumFarms;
	public Farm[] smallFarms;
	public Farm largeFarm;

	FruitList fruitList;
	SaplingList saplingList;

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
		fruitList = new FruitList(this);
		saplingList = new SaplingList(this);

		mainMenuScreen = new MainMenuScreen(this);
		farmScreen = new NewFarmScreen(this, "arborMap2.tmx", 0);
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
        //shopScreen.dispose();
		//farmScreen.dispose();
		//plotScreen.dispose();
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
