package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Plot;

public class FarmScreen implements Screen
{
    private Arborium game;
    private Stage stage;

    private Farm farm;

    private TextButton[] testHarvestButtons;
    private Label[] plotLabels;
    private Skin skin;

    public FarmScreen(Arborium game)
    {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        farm = game.farm;

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        testHarvestButtons = new TextButton[9];
        initializeButtons();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
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
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        int xOffset = -200;
        int yOffset = 200;
        int centerX = Gdx.graphics.getWidth() / 2 - 50;
        int centerY = Gdx.graphics.getHeight() / 2 - 50;

        // Initialize buttons for each farm plot, and align in 3x3 grid
        for (int i = 0; i < testHarvestButtons.length; i++)
        {
            TextButton button = new TextButton("Plot " + (i + 1), skin);
            button.setSize(100, 100);

            button.setPosition(centerX + xOffset, centerY + yOffset);
            if (xOffset >= 200)
            {
                yOffset -= 200;
                xOffset = -200;
            }
            else
                xOffset += 200;

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

            stage.addActor(testHarvestButtons[i]);
        }
    }
}
