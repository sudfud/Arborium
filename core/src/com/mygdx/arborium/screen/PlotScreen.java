package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.items.Seed;
import com.mygdx.arborium.items.SeedList;
import com.mygdx.arborium.items.Tree;

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
    private Texture sky, grass, dirtplot, dirtpatch, adult_tree,
    youngAdult_tree, teenager_tree, child_tree, baby_tree;

    public PlotScreen(final Arborium game, Plot plot)
    {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        stage.addActor(Resources.backgroundImage);
        this.plot = plot;

        stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stack.add(table);
        stack.setDebug(true);

        skin = Resources.glassySkin;

        timer = new Label("", skin);
        timer.setAlignment(Align.center);
        timer.setFontScale(2);

        table.add(timer).width(500).expandX().center().space(25);

        seedSelectTable = new Table();
        seedSelectTable.setFillParent(true);

        seedSelectList = new List<String>(skin);

        seedSelectScrollPane = new ScrollPane(seedSelectList, skin);
        seedSelectTable.add(seedSelectScrollPane).size(250, 500);

        seedImage = new Image(Resources.seed2);
        seedImage.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                seedTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                seedTouched = false;
                seedImage.setY(GDX_HEIGHT * 3/4);
            }
        });
        seedImage.setVisible(false);
        stage.addActor(seedImage);

        initializeButtons();
        addButtonListeners();
    }

    @Override
    public void show()
    {
        Gdx.app.log("PlotScreen", "show()");
        Gdx.app.log("PlotScreen", "plot empty: " + plot.isEmpty());

        seedSelectList.setItems(Inventory.getItems(Inventory.InventoryCategory.SEED));
        Gdx.app.log("PlotScreen", "" + seedSelectList.getItems().size);

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

        game.spriteBatch.begin();
        Texture background = Resources.backgroundTexture;
        Texture grass = Resources.grassTexture;
        Texture dirtPatch = Resources.dirtPatchTexture;
        game.spriteBatch.draw(background, -background.getWidth()/2, 0);
        game.spriteBatch.draw(grass, 0, -grass.getHeight()/4);
        game.spriteBatch.draw(dirtPatch, Gdx.graphics.getWidth()/2 - dirtPatch.getWidth()/2, 200);
        game.spriteBatch.end();

        if (seedTouched)
        {
            int y = GDX_HEIGHT - Gdx.input.getY();

            if (y <= 200)
            {
                seedImage.setVisible(false);
                String seedName = seedSelectList.getSelected();
                Seed seed = SeedList.get(seedName);
                plot.plantSeed(seed.treeType);
                Inventory.takeItem(seedName);
                stack.add(table);
                plantButton.setVisible(false);
                backButton.setVisible(true);
                seedTouched = false;
            }

            seedImage.setY(y);
        }

        batch.begin();
        batch.draw(sky, 0, 0, 0, 0, (int) stage.getWidth(), (int) stage.getHeight());
        batch.draw(grass, 0, 0);

        int centerX = GDX_WIDTH/2;

        batch.draw(dirtplot, centerX - dirtplot.getWidth()/2, 200);
        batch.draw(dirtpatch, centerX - dirtPatch.getWidth()/2, 165);

        if(!plot.isEmpty()) {

            if (plot.isReadyToHarvest() || plot.isMature())
            batch.draw(adult_tree, centerX - dirtPatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() * 4 / 5)))
                batch.draw(youngAdult_tree, centerX - dirtPatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() * 3 / 5)))
                batch.draw(teenager_tree, centerX - dirtPatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() * 2 / 5)))
                batch.draw(child_tree, centerX - dirtPatch.getWidth()/2, 165);

            else if ((plot.getTimeSincePlanted() > (plot.getPlantedTree().getMatureTime() / 5)))
                batch.draw(baby_tree, centerX - dirtPatch.getWidth()/2, 165);
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
                    Seed seed = SeedList.get(seedName);
                    seedImage.setDrawable(new TextureRegionDrawable(seed.itemImage));
                    seedImage.setScale(0.5f);
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
