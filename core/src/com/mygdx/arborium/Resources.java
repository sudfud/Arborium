package com.mygdx.arborium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources
{
    public static Skin glassySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

    public static Texture backgroundTexture = new Texture(Gdx.files.internal("background_sky.png"));
    public static Texture grassTexture = new Texture(Gdx.files.internal("grass.png"));
    public static Texture dirtPatchTexture = new Texture(Gdx.files.internal("dirtpatch.png"));

    public static Texture seed1 = new Texture(Gdx.files.internal("seed1.png"));
    public static Texture seed2 = new Texture(Gdx.files.internal("seed2.png"));
    public static Texture seed3 = new Texture(Gdx.files.internal("seed3.png"));

    public static Texture appleFruit = new Texture(Gdx.files.internal("fruits/red-apple.png"));
    public static Texture orangeFruit = new Texture(Gdx.files.internal("fruits/orange.png"));

    public static Image backgroundImage = new Image(backgroundTexture);

    public static void initialize()
    {
        backgroundImage.setZIndex(0);

        glassySkin.getFont("font-big").getData().setScale(0.75f);
        glassySkin.getFont("font").getData().setScale(2);
    }

    public static void dispose()
    {
        backgroundTexture.dispose();
        grassTexture.dispose();
        dirtPatchTexture.dispose();
        glassySkin.dispose();
    }
}
