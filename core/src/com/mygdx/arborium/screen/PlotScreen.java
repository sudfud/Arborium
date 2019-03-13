package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.items.Seed;
import com.mygdx.arborium.items.Tree;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlotScreen implements Screen
{
    public static final int GDX_WIDTH = Gdx.graphics.getWidth();
    public static final int GDX_HEIGHT = Gdx.graphics.getHeight();

    Arborium game;

    private Plot plot;
    private Stage stage;

    Stack stack;
    Table table;

    Label plantedTreeLabel;
    Label timer;

    Skin skin;
    TextButton backButton;
    TextButton plantButton;
    TextButton harvestButton;

    Table seedSelectTable;
    ScrollPane seedSelectScrollPane;
    List<String> seedSelectList;
    TextButton seedSelectButton;
    TextButton seedSelectBackButton;

    Image seedImage;
    private boolean seedTouched = false;
    private SpriteBatch batch;
    private Texture sky, dirtplot, dirtpatch, adult_tree,
    youngAdult_tree, teenager_tree, child_tree, baby_tree;

    Texture background;
    Texture grass;
    Texture dirt;

    Random random;
    private boolean fruitDrawn = false;
    private Vector2[] fruitPositions;

    public PlotScreen(final Arborium game, Plot plot)
    {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        this.plot = plot;

        // Setup Stack and add to stage
        stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        // Setup main table and add to stack
        table = new Table();
        table.setFillParent(true);
        stack.add(table);
        stack.setDebug(true);

        skin = game.resources.getSkin(Resources.GLASSY_SKIN);

        // Setup tree label, add to table
        plantedTreeLabel = new Label("", skin);
        plantedTreeLabel.setAlignment(Align.center);
        plantedTreeLabel.setFontScale(2);
        table.add(plantedTreeLabel).width(500);
        table.row();

        // Setup mature/harvest timer, add to main table.
        timer = new Label("", skin);
        timer.setAlignment(Align.center);
        timer.setFontScale(2);
        table.add(timer).width(500).expandX().center().space(25);

        // Setup seed selection table, added to stack later
        seedSelectTable = new Table();
        seedSelectTable.setFillParent(true);

        seedSelectList = new List<String>(skin);

        seedSelectScrollPane = new ScrollPane(seedSelectList, skin);
        seedSelectTable.add(seedSelectScrollPane).size(250, 500);

        // Setup a dummy seed image, to be modified later when the user selects a
        // seed to plant
        Texture seedTexture = game.resources.getTexture(Resources.APPLE_SEED);
        seedImage = new Image(seedTexture);
        seedImage.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                seedTouched = true;
                return true;
            }

            // Send the seed image to its original position
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                seedTouched = false;
                seedImage.setY(GDX_HEIGHT * 3/4);
            }
        });
        seedImage.setVisible(false);
        stage.addActor(seedImage);

        random = new Random();

        initializeButtons();
        addButtonListeners();
    }

    @Override
    public void show()
    {
        Gdx.app.log("PlotScreen", "show()");
        Gdx.app.log("PlotScreen", "plot empty: " + plot.isEmpty());

        seedSelectList.setItems(Inventory.getItemsOfType(Seed.class));
        Gdx.app.log("PlotScreen", "" + seedSelectList.getItems().size);

        addButtonListeners();

        updateLabels();

        plantButton.setVisible(plot.isEmpty());

        Gdx.input.setInputProcessor(stage);

        batch = game.spriteBatch;

        // Grab all the textures needed from the game's resource manager
        sky = game.resources.getTexture(Resources.BG_SKY);
        grass = game.resources.getTexture(Resources.GRASS);
        dirtplot = game.resources.getTexture(Resources.DIRT_PLOT);
        dirtpatch = game.resources.getTexture(Resources.DIRT_PATCH);
        adult_tree = game.resources.getTexture(Resources.TREE_1_ADULT);
        youngAdult_tree = game.resources.getTexture(Resources.TREE_1_YOUNG_ADULT);
        teenager_tree = game.resources.getTexture(Resources.TREE_1_TEENAGER);
        child_tree = game.resources.getTexture(Resources.TREE_1_CHILD);
        baby_tree = game.resources.getTexture(Resources.TREE_1_BABY);
    }

    @Override
    public void render(float delta)
    {
        // Update the plot if it's not currently empty
        if (!plot.isEmpty())
        {
            plot.update();

            if (!plot.isEmpty() && plot.isReadyToHarvest())
                harvestButton.setVisible(true);
            batch.begin();
            //batch.draw(adult_tree, -240, 265);
            batch.end();
        }

        updateLabels();

        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // This should only be true if there's currently a seed selected to plant
        if (seedTouched)
        {
            int y = GDX_HEIGHT - Gdx.input.getY();

            // If the seed is currently on or under the bottom quarter of
            // the screen, plant the seed and bring back the main table
            if (y <= 200)
            {
                seedImage.setVisible(false);
                String seedName = seedSelectList.getSelected();
                Seed seed = game.seedList.get(seedName);
                plot.plantSeed(seed.treeType);
                Inventory.takeItem(seedName);
                stack.add(table);
                plantButton.setVisible(false);
                backButton.setVisible(true);
                seedTouched = false;
            }

            // Set the seed's y position to where the screen was touched
            seedImage.setY(y);
        }

        if (plot.isReadyToHarvest())
        {
            // If the fruit has not already been drawn, draw it so that the
            // correct # of fruits appear on the tree
            if (!fruitDrawn)
            {
                fruitPositions = new Vector2[plot.getPlantedTree().getProduceAmount()];
                for (int i = 0; i < fruitPositions.length; i++)
                {
                    int x = random.nextInt(GDX_WIDTH) - 100;
                    int y = random.nextInt(GDX_HEIGHT/2) + GDX_HEIGHT/2 - 100;

                    fruitPositions[i] = new Vector2(x, y);
                }
                fruitDrawn = true;
            }

            // Harvest the tree and stop drawing the fruit when the phone
            // is shaken.
            float shakeAmt = Math.abs(Gdx.input.getAccelerometerX());
            if (shakeAmt >= 25)
            {
                plot.harvest();
                fruitDrawn = false;
            }
        }

        batch.begin();

        // Draw the static (unchanging) textures
        batch.draw(sky, 0, 0, 0, 0, (int) stage.getWidth(), (int) stage.getHeight());
        batch.draw(grass, 0, 0);

        int centerX = GDX_WIDTH/2;
        batch.draw(dirtplot, centerX - dirtplot.getWidth()/2, 200);
        batch.draw(dirtpatch, centerX - dirtpatch.getWidth()/2, 165);

        // If the plot has a tree, draw it based on how long the tree has
        // to wait to mature.
        if (!plot.isEmpty())
        {
            if (plot.isReadyToHarvest() || plot.isMature())
            batch.draw(adult_tree, centerX - dirtpatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() * 4 / 5)))
                batch.draw(youngAdult_tree, centerX - dirtpatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() * 3 / 5)))
                batch.draw(teenager_tree, centerX - dirtpatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() * 2 / 5)))
                batch.draw(child_tree, centerX - dirtpatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() / 5)))
                batch.draw(baby_tree, centerX - dirtpatch.getWidth()/2, 165);
        }

        // Draw the fruits
        if (fruitDrawn)
        {
            Texture fruitTexture = plot.getPlantedTree().getFruit().itemImage;
            for (int i = 0; i < fruitPositions.length; i++)
            {
                int x = (int)fruitPositions[i].x;
                int y = (int)fruitPositions[i].y;

                batch.draw(fruitTexture, x, y, 100, 100);
            }
        }
        batch.end();

        // Update and draw stage
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
        plantButton = new TextButton("Plant", skin);
        table.row();
        table.add(plantButton).width(150).height(100).expandX().space(25);

        harvestButton = new TextButton("Harvest", skin);
        harvestButton.setVisible(false);
        table.row();
        table.add(harvestButton).width(150).height(100).space(25);

        backButton = new TextButton("Back", skin);
        table.row();
        table.add(backButton).width(150).height(100).pad(25);

        seedSelectButton = new TextButton("Select", skin);
        seedSelectTable.row();
        seedSelectTable.add(seedSelectButton).size(150, 100).space(25);

        seedSelectBackButton = new TextButton("Back", skin);
        seedSelectTable.row();
        seedSelectTable.add(seedSelectBackButton).size(150, 100).space(25);
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
                // plot.plantSeed(TreeList.appleTree);
                stack.add(seedSelectTable);
                backButton.setVisible(false);
                // plantButton.setVisible(false);
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

        seedSelectButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //stack.remove();
                if (seedSelectList.getItems().size > 0)
                {
                    stack.removeActor(seedSelectTable);
                    stack.removeActor(table);
                    String seedName = seedSelectList.getSelected();
                    Seed seed = game.seedList.get(seedName);
                    seedImage.setDrawable(new TextureRegionDrawable(seed.itemImage));
                    seedImage.setSize(100, 100);
                    seedImage.setPosition(GDX_WIDTH / 2 - seedImage.getWidth() / 4, GDX_HEIGHT * 3 / 4);
                    seedImage.setVisible(true);
                    stage.addActor(seedImage);
                }

                return true;
            }
        });

        seedSelectBackButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                backButton.setVisible(true);
                stack.removeActor(seedSelectTable);
                addButtonListeners();
                return true;
            }
        });
    }

    private void updateLabels()
    {
        if (!plot.isEmpty())
        {
            Tree tree = plot.getPlantedTree();
            plantedTreeLabel.setText("Currently planted: " + tree.itemName);

            // Set timer text based on the current state of the plot/tree.
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
            plantedTreeLabel.setText("Currently planted: None");
            timer.setText("");
        }
    }

    // Use this to convert time in milliseconds to a more human-readable format
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
