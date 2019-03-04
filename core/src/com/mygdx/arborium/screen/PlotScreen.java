package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.game.Tree;
import com.mygdx.arborium.game.TreeList;

import java.util.concurrent.TimeUnit;

public class PlotScreen implements Screen
{
    public static final int GDX_WIDTH = Gdx.graphics.getWidth();
    public static final int GDX_HEIGHT = Gdx.graphics.getHeight();

    Arborium game;

    private Plot plot;
    private Stage stage;

    Table table;

    Label timer;

    Skin skin;
    TextButton backButton;
    TextButton plantButton;
    TextButton harvestButton;

    private SpriteBatch batch;
    private Texture sky, grass, dirtplot, dirtpatch, adult_tree,
    youngAdult_tree, teenager_tree, child_tree, baby_tree;

    public PlotScreen(final Arborium game, Plot plot)
    {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        this.plot = plot;

        table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        timer = new Label("", skin);
        timer.setAlignment(Align.center);
        timer.setFontScale(2);

        table.add(timer).width(500).expandX().center().space(25);

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

        batch = new SpriteBatch();
        sky = new Texture(Gdx.files.internal("background_sky.png"));
        grass = new Texture(Gdx.files.internal("grass.png"));
        dirtplot = new Texture(Gdx.files.internal("dirtplot.png"));
        dirtpatch = new Texture(Gdx.files.internal("dirtpatch.png"));
        adult_tree = new Texture(Gdx.files.internal("tree1_adult.png"));
        youngAdult_tree = new Texture(Gdx.files.internal("tree1_youngadult.png"));
        teenager_tree = new Texture(Gdx.files.internal("tree1_teenager.png"));
        child_tree = new Texture(Gdx.files.internal("tree1_child.png"));
        baby_tree = new Texture(Gdx.files.internal("tree1_baby.png"));
    }

    @Override
    public void render(float delta) {
        if (!plot.isEmpty()) {
            plot.update();

            if (!plot.isEmpty() && plot.isReadyToHarvest())
                harvestButton.setVisible(true);
            batch.begin();
            batch.draw(adult_tree, -240, 265);
            batch.end();
        }

        updateLabels();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(sky, 0, 0, 0, 0, (int) stage.getWidth(), (int) stage.getHeight());
        batch.draw(grass, 0, 0);
        batch.draw(dirtplot, (int) (stage.getWidth() / 4), 0);
        if (!plot.isEmpty() && plot.isReadyToHarvest()){
            batch.draw(dirtpatch, -240, 265);
            batch.draw(adult_tree, -240, 265);
        }else if (!plot.isEmpty() && (plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() *4/5) )){
            batch.draw(dirtpatch, -240, 265);
            batch.draw(youngAdult_tree, -240, 265);
        }else if (!plot.isEmpty() && (plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() *3/5) )){
            batch.draw(dirtpatch, -240, 265);
            batch.draw(teenager_tree, -240, 265);
        }
        else if (!plot.isEmpty() && (plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() *2/5) )){
            batch.draw(dirtpatch, -240, 265);
            batch.draw(child_tree, -240, 265);
        }else if (!plot.isEmpty() && (plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() /5) )){
            batch.draw(dirtpatch, -240, 265);
            batch.draw(baby_tree, -240, 265);
        }
        else if (!plot.isEmpty() ) {
            batch.draw(dirtpatch, -240, 265);
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
    public void resume()
    {
        if (!plot.isEmpty())
        {
            plot.update();

            if (!plot.isEmpty() && plot.isReadyToHarvest())
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

        plantButton = new TextButton("Plant", skin);
        table.row();
        table.add(plantButton).width(100).expandX().space(25);

        harvestButton = new TextButton("Harvest", skin);
        harvestButton.setVisible(false);
        table.row();
        table.add(harvestButton).width(100).space(25);

        backButton = new TextButton("Back", skin);
        table.row();
        table.add(backButton).width(100).space(25);
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
                Gdx.app.log("PlotScreen", "Tree planted");
                plot.plantSeed(TreeList.appleTree);
                plantButton.setVisible(false);
                Gdx.app.log("PlotScreen", "plot empty: " + plot.isEmpty());
            }
        });

        harvestButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if (!plot.isEmpty() && plot.isReadyToHarvest())
                    plot.harvest();
                harvestButton.setVisible(false);
                return true;
            }
        });
    }

    private void updateLabels()
    {
        if (!plot.isEmpty())
        {
            Tree tree = plot.getPlantedTree();
            if (plot.isReadyToHarvest())
            {
                timer.setText("Ready to harvest");
            }
            else if (plot.isMature())
            {
                long timeToProduce = tree.getProduceRate()
                        - plot.getTimeSinceLastHarvest();

                String format = timeFormat(timeToProduce);

                timer.setText("Time to next harvest: " + format);
            }
            else
            {
                long timeToMature = (tree.getMatureTime() - plot.getTimeSincePlanted());

                String format = timeFormat(timeToMature);

                timer.setText("Time to mature: " + format);
            }
        }
        else
        {
            timer.setText("");
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
