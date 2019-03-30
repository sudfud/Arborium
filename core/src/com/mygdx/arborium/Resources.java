package com.mygdx.arborium;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/*
 * All of the game's resources should be stored here. Resources in this case include Textures,
 * Skins, Sounds, Music, Fonts, etc. Resources are managed by the AssetManager instance.
 *
 * Ensure that resources are NOT declared static (my bad), but instead loaded into the AssetManager,
 * and then later retrieved from it using either the AM get() method or the class-specific get
 * methods below. To make this easier, a list of static String constants are provided to hold
 * resource file names and directories.
 *
 */

public class Resources
{
    public AssetManager assetManager;

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

    public Resources()
    {
        assetManager = new AssetManager();

        assetManager.load(GLASSY_SKIN, Skin.class);

        assetManager.load(BG_SKY, Texture.class);
        assetManager.load(GRASS, Texture.class);
        assetManager.load(DIRT_PATCH, Texture.class);
        assetManager.load(DIRT_PLOT, Texture.class);

        assetManager.load(TREE_OVERWORLD, Texture.class);

        assetManager.load(TREE_1_ADULT, Texture.class);
        assetManager.load(TREE_1_YOUNG_ADULT, Texture.class);
        assetManager.load(TREE_1_TEENAGER, Texture.class);
        assetManager.load(TREE_1_CHILD, Texture.class);
        assetManager.load(TREE_1_BABY, Texture.class);

        assetManager.load(APPLE_SEED, Texture.class);
        assetManager.load(ORANGE_SEED, Texture.class);
        assetManager.load("seed3.png", Texture.class);

        assetManager.load(APPLE_FRUIT, Texture.class);
        assetManager.load(ORANGE_FRUIT, Texture.class);

        assetManager.finishLoading();
    }

    public Skin getSkin(String fileName)
    {
        return assetManager.get(fileName, Skin.class);
    }

    public Texture getTexture(String fileName)
    {
        return assetManager.get(fileName, Texture.class);
    }

    public void dispose()
    {
        assetManager.dispose();
    }
}
