package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Plot;

public class FarmScreen implements Screen
{
    private Arborium game;
    private Stage stage;

    private Farm farm;

    private Table table;
    private TextButton[] testHarvestButtons;
    private TextButton mainMenuButton;
    private TextButton shopButton;
    private Skin skin;

    private SpriteBatch batch;
    private Texture sky, grass, dirtplot;

    public FarmScreen(Arborium game)
    {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        stage.addActor(Resources.backgroundImage);

        farm = game.farm;

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        skin = Resources.glassySkin;

        testHarvestButtons = new TextButton[9];
        initializeButtons();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        sky = new Texture(Gdx.files.internal("background_sky.png"));
        grass = new Texture(Gdx.files.internal("grass.png"));
        dirtplot = new Texture(Gdx.files.internal("dirtplot.png"));
    }

    @Override
    public void render(float delta)
    {
        farm.update();

        for (int i = 0; i < farm.getPlotSize(); i++)
        {
            String buttonText = "Plot " + i;
            Plot p = farm.getPlot(i);
            if (p.isEmpty())
                buttonText += "\nEmpty";

            testHarvestButtons[i].setText(buttonText);
            batch.begin();
            batch.draw(dirtplot, testHarvestButtons[i].getX(), testHarvestButtons[i].getY());
            batch.end();
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        Texture background = Resources.backgroundTexture;
        game.spriteBatch.draw(Resources.backgroundTexture, - background.getWidth()/2, 0);
        game.spriteBatch.end();

        batch.begin();
        batch.draw(grass, 0, 0, 0, 0, (int)stage.getWidth() , (int)stage.getHeight()*2);
        for (int i = 0; i < farm.getPlotSize(); i++)
        {
            batch.draw(dirtplot, testHarvestButtons[i].getX() - dirtplot.getWidth()/2, testHarvestButtons[i].getY() - dirtplot.getWidth()/2);
        }
        batch.end();

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
    public void dispose()
    {
        stage.dispose();
    }

    private void initializeButtons()
    {
        int column = 0;

        // Initialize buttons for each farm plot, and align in 3x3 grid
        for (int i = 0; i < testHarvestButtons.length; i++)
        {
            TextButton button = new TextButton("Plot " + (i + 1), skin);

            table.add(button).width(150).height(100).expand();
            column++;
            if (column >= 3)
            {
                table.row();
                column = 0;
            }
            final int index = i;

            // Send to plot screen on click
            button.addListener(new ClickListener()
            {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    final PlotScreen plotScreen = game.plotScreens[index];
                    // Delay the screen change to let button animations finish
                    Timer.schedule(new Task()
                    {
                        @Override
                       public void run()
                        {
                            game.setScreen(plotScreen);
                        }
                    }, 0.25f);
                    return true;
                }
            });

            testHarvestButtons[i] = button;
        }

        mainMenuButton = new TextButton("Menu", skin);
        mainMenuButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(game.mainMenuScreen);
                return true;
            }
        });

        table.row();
        table.add(mainMenuButton).bottom().width(150).height(100);

        shopButton = new TextButton("Shop", skin);
        shopButton.addListener(new ClickListener()
        {
           @Override
           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
           {
               game.setScreen(game.shopScreen);
               return true;
           }
        });

        table.add();
        table.add(shopButton).width(150).height(100);
    }
}
