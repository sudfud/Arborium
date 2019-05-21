package com.mygdx.arborium.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.GameUtils;
import com.mygdx.arborium.Resources;

public class MainMenuScreen implements Screen
{
    Arborium game;

    Skin skin;

    Stage stage;
    Table table;

    TextButton startButton;
    TextButton optionButton;
    TextButton quitButton;

    Texture background;

    public MainMenuScreen(Arborium game)
    {
        this.game = game;

        skin = game.getSkin(Arborium.GLASSY_SKIN);
        skin.getFont("font-big").getData().setScale(0.75f);

        // Setup stage and table
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        // Setup start button
        startButton = new TextButton("Start", skin);
        startButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                toFarmScreen();
                return true;
            }
        });
        startButton.setSize(200, 100);
        startButton.setPosition(game.GDX_WIDTH/2, -200);
        MoveToAction moveAction = Actions.action(MoveToAction.class);
        moveAction.setPosition(game.GDX_WIDTH/2 - 100, game.GDX_HEIGHT/2 - 50);
        moveAction.setDuration(2);
        moveAction.setInterpolation(Interpolation.sine);
       // startButton.addAction(moveAction);
        table.add(startButton).width(200).height(100).space(50);


        // Setup quit button
        quitButton = new TextButton("Exit", skin);
        quitButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.exit();
                return true;
            }
        });
        table.row();
        table.add(quitButton).width(200).height(100);

        background = game.getTexture(Arborium.BG_SKY);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        game.spriteBatch.begin();
        game.spriteBatch.draw(background, 0, 0);
        game.spriteBatch.end();

        // Update and draw UI
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void toFarmScreen()
    {
        GameUtils.delaySetScreen(game, 0.15f, game.farmScreen);
    }
}
