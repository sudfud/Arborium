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



    public Resources()
    {
        assetManager = new AssetManager();

        
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
