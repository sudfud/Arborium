package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;

public class LoadingScreen implements Screen
{
    Arborium game;

    private Stage stage;
    private Table table;
    private Label loadingLabel;

    private Texture backgroundTexture;

    private ShapeRenderer shapeRenderer;

    public LoadingScreen(Arborium game)
    {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() 
    {
        game.assetManager.load(Arborium.GLASSY_SKIN, Skin.class);

        game.assetManager.load(Arborium.BG_SKY, Texture.class);

        game.assetManager.load(Arborium.GRASS, Texture.class);
        game.assetManager.load(Arborium.DIRT_PATCH, Texture.class);
        game.assetManager.load(Arborium.DIRT_PLOT, Texture.class);

        game.assetManager.load(Arborium.TREE_OVERWORLD, Texture.class);

        game.assetManager.load(Arborium.TREE_1_ADULT, Texture.class);
        game.assetManager.load(Arborium.TREE_1_YOUNG_ADULT, Texture.class);
        game.assetManager.load(Arborium.TREE_1_TEENAGER, Texture.class);
        game.assetManager.load(Arborium.TREE_1_CHILD, Texture.class);
        game.assetManager.load(Arborium.TREE_1_BABY, Texture.class);

        game.assetManager.load(Arborium.APPLE_SEED, Texture.class);
        game.assetManager.load(Arborium.ORANGE_SEED, Texture.class);
        game.assetManager.load("seed3.png", Texture.class);

        game.assetManager.load(Arborium.APPLE_FRUIT, Texture.class);
        game.assetManager.load(Arborium.ORANGE_FRUIT, Texture.class);

        game.assetManager.load(Arborium.BASKET, Texture.class);

        for (int i = 0; i <= 123; i++)
        {
            String formatInt = String.format("%03d", i);
            game.assetManager.load(Arborium.TREE_ANIM + formatInt + ".png", Texture.class);
        }

        game.assetManager.finishLoadingAsset(Arborium.GLASSY_SKIN);
        game.assetManager.finishLoadingAsset(Arborium.BG_SKY);

        Skin skin = game.getSkin(Arborium.GLASSY_SKIN);

        stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setFillParent(true);
        loadingLabel = new Label("Loading...", skin);
        loadingLabel.setFontScale(2);
        table.add(loadingLabel);

        stage.addActor(table);

        backgroundTexture = game.getTexture(Arborium.BG_SKY);
    }

    @Override
    public void render(float delta) 
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.assetManager.update())
        {
            game.initializeScreens();
            game.setScreen(game.mainMenuScreen);
        }

        game.spriteBatch.begin();
        game.spriteBatch.draw(backgroundTexture, 0, 0);
        game.spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0.3f, 0.8f, 1));
        shapeRenderer.rect(
                game.GDX_WIDTH/2 - game.GDX_WIDTH/4,
                game.GDX_HEIGHT/2 - game.GDX_HEIGHT/3,
                game.GDX_WIDTH/2,
                game.GDX_HEIGHT/15);

        shapeRenderer.setColor(new Color(0, 0.6f, 0.8f, 1));
        shapeRenderer.rect(
                game.GDX_WIDTH/2 - game.GDX_WIDTH/4,
                game.GDX_HEIGHT/2 - game.GDX_HEIGHT/3,
                game.GDX_WIDTH/2 * (game.assetManager.getProgress() + 0.01f),
                game.GDX_HEIGHT/15);
        shapeRenderer.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause() 
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose() 
    {

    }

}