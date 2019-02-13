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
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.game.Seed;

import java.util.concurrent.TimeUnit;

public class PlotScreen implements Screen
{
    public static final int GDX_WIDTH = Gdx.graphics.getWidth();
    public static final int GDX_HEIGHT = Gdx.graphics.getHeight();

    Arborium game;

    private Plot plot;
    private Stage stage;

    Label matureTime;
    Label produceTime;

    Skin skin;
    TextButton backButton;
    TextButton plantButton;
    TextButton harvestButton;

    public PlotScreen(final Arborium game, Plot plot)
    {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        this.plot = plot;

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        matureTime = new Label("", skin);
        matureTime.setSize(250, 150);
        matureTime.setFontScale(2);
        matureTime.setPosition(GDX_WIDTH/2 - matureTime,
                                GDX_HEIGHT/2 + GDX_WIDTH/2);

        produceTime = new Label("", skin);
        produceTime.setSize(250, 150);
        produceTime.setFontScale(2);
        produceTime.setPosition(GDX_WIDTH/2 - produceTime.getWidth()/2,
                GDX_HEIGHT/2 + GDX_WIDTH/2);

        stage.addActor(matureTime);
        stage.addActor(produceTime);

        initializeButtons();
        addButtonListeners();
    }

    @Override
    public void show()
    {
        Gdx.app.log("PlotScreen", "show()");
        Gdx.app.log("PlotScreen", "plot empty: " + plot.isEmpty());

        addButtonListeners();

        updateLabels();

        plantButton.setVisible(plot.isEmpty());

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        if (!plot.isEmpty())
        {
            plot.update();

            Seed tree = plot.getPlantedTree();
            if (tree != null && tree.isReadyToHarvest())
                harvestButton.setVisible(true);
        }

        updateLabels();

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
    public void resume()
    {
        if (!plot.isEmpty())
        {
            plot.update();

            Seed tree = plot.getPlantedTree();
            if (tree != null && tree.isReadyToHarvest())
                harvestButton.setVisible(true);
        }
        else
            plantButton.setVisible(true);

        Gdx.app.log("PlotScreen", "resume()");
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        stage.dispose();
        skin.dispose();
    }

    public void setPlot(Plot p)
    {
        plot = p;
    }


    // Helper methods

    private void initializeButtons()
    {
        int centerX = Gdx.graphics.getWidth() / 2;
        int centerY = Gdx.graphics.getHeight() / 2;

        backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50);
        backButton.setPosition(centerX - 50, centerY - 200);
        stage.addActor(backButton);

        plantButton = new TextButton("Plant", skin);
        plantButton.setSize(100, 50);
        plantButton.setPosition(centerX - 50, centerY + 200);
        stage.addActor(plantButton);

        harvestButton = new TextButton("Harvest", skin);
        harvestButton.setSize(100, 50);
        harvestButton.setPosition(centerX - 50, centerY);
        harvestButton.setVisible(false);
        stage.addActor(harvestButton);
    }

    private void addButtonListeners()
    {
        backButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("PlotScreen", "Back");
                game.setScreen(game.farmScreen);
            }
        });

        plantButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("PlotScreen", "Seed planted");
                plot.plantSeed(new Seed(5, 1, 5));
                plantButton.setVisible(false);
                Gdx.app.log("PlotScreen", "plot empty: " + plot.isEmpty());
            }
        });

        harvestButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Seed tree = plot.getPlantedTree();
                if (tree != null && tree.isReadyToHarvest())
                    tree.harvest();
                harvestButton.setVisible(false);
                return true;
            }
        });
    }

    private void updateLabels()
    {
        if (!plot.isEmpty())
        {
            Seed tree = plot.getPlantedTree();
            if (tree.isReadyToHarvest())
            {
                produceTime.setText("Ready to harvest");
                produceTime.setVisible(true);
                matureTime.setVisible(false);
            }
            else if (tree.isMature())
            {
                long timeToProduce = tree.getProduceRate()
                        - tree.getTimeSinceLastHarvest();

                String format = timeFormat(timeToProduce);

                produceTime.setText("Time to next harvest: " + format);

                produceTime.setVisible(true);
                matureTime.setVisible(false);
            }
            else
            {
                long timeToMature = (tree.getMatureTime() - tree.getTimeSincePlanted());

                String format = timeFormat(timeToMature);

                matureTime.setText("Time to mature: " + format);

                matureTime.setVisible(true);
                produceTime.setVisible(false);
            }
        }
        else
        {
            matureTime.setVisible(false);
            produceTime.setVisible(false);
        }
    }

    private String timeFormat(long millis)
    {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
