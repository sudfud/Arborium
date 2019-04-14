package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;

import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.GameUtils;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.items.Seed;
import com.mygdx.arborium.items.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlotScreen implements Screen {
    final float SCALE = 15.0f;

    Arborium game;

    private Plot plot;
    private Stage stage;

    Stack stack;
    Table table;

    Label plantedTreeLabel;
    Label harvestTimer;

    Skin skin;
    TextButton backButton;
    TextButton plantButton;

    TextButton harvestButton;
    private boolean harvesting = false;

    Table seedSelectTable;
    ScrollPane seedSelectScrollPane;
    List<String> seedSelectList;
    TextButton seedSelectButton;
    TextButton seedSelectBackButton;

    Image seedImage;
    private boolean seedTouched = false;
    private SpriteBatch batch;
    private Texture sky, dirtplot, dirtpatch;

    Texture grass;

    Random random;
    private Vector2[] fruitPositions;

    private OrthographicCamera camera;

    private int treeHeight;
    private int treeWidth;

    // Used for physics, i.e. falling fruits
    World physWorld;
    Box2DDebugRenderer physDebug;
    Array<Body> fruitBodies;

    private Timer fruitDropTimer;

    private HashMap<Plot, Vector2[]> fruitPositionMap;

    public PlotScreen(final Arborium game, Plot plot) {
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
        harvestTimer = new Label("", skin);
        harvestTimer.setAlignment(Align.center);
        harvestTimer.setFontScale(2);
        table.add(harvestTimer).width(500).expandX().center().space(25);

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
        seedImage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                seedTouched = true;
                return true;
            }

            // Send the seed image to its original position
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                seedTouched = false;
                seedImage.setY(game.GDX_HEIGHT * 3 / 4);
            }
        });
        seedImage.setVisible(false);
        stage.addActor(seedImage);

        random = new Random();

        initializeButtons();
        addButtonListeners();

        Texture tree = game.resources.getTexture("Tree2/sprite_tree_2000.png");
        treeWidth = tree.getWidth();
        treeHeight = tree.getHeight();

        physWorld = new World(new Vector2(0, -9.81f), true);
        physDebug = new Box2DDebugRenderer();
        fruitBodies = new Array<Body>();

        fruitDropTimer = new Timer();

        fruitPositionMap = new HashMap<Plot, Vector2[]>();

        camera = new OrthographicCamera(game.GDX_WIDTH / SCALE, game.GDX_HEIGHT / SCALE);
        camera.position.set(game.GDX_WIDTH / 2 / SCALE, game.GDX_HEIGHT / 2 / SCALE, 0);
        camera.update();
    }

    @Override
    public void show() {
        seedSelectList.setItems(Inventory.getItemsOfType(Seed.class));

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
    }

    @Override
    public void render(float delta) {
        // Update the plot if it's not currently empty
        if (!plot.isEmpty()) {
            plot.update();

            batch.begin();
            // batch.draw(adult_tree, -240, 265);
            batch.end();
        }

        updateLabels();

        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        harvestButton.setVisible(plot.isReadyToHarvest() && !harvesting);
        backButton.setVisible(!harvesting);

        // This should only be true if there's currently a seed selected to plant
        if (seedTouched) {
            int y = game.GDX_HEIGHT - Gdx.input.getY();

            // If the seed is currently on or under the bottom quarter of
            // the screen, plant the seed and bring back the main table
            if (y <= 200) {
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

        physWorld.step(1 / 60f, 6, 2);

        if (harvesting)
         {
            // Harvest the tree and stop drawing the fruit when the phone
            // is shaken.
            float shakeAmt = Math.abs(Gdx.input.getAccelerometerX());
            if (shakeAmt >= 25) {
                plot.harvest();
                physWorld.getBodies(fruitBodies);
                for (int i = 0; i < fruitBodies.size; i++) 
                {
                    final int seconds = i;
                    fruitDropTimer.scheduleTask(new Task()
                    {
                        @Override
                        public void run()
                        {
                            fruitBodies.get(seconds).setAwake(true);
                        }
                    }, seconds);
                }
            }

            int fruitOffScreenCount = 0;

            physWorld.getBodies(fruitBodies);
            for (Body b : fruitBodies)
            {
                if (b.getPosition().y < -50/SCALE)
                {
                    fruitOffScreenCount++;
                }
            }

            if(physWorld.getBodyCount() == fruitOffScreenCount)
            {
                harvesting = false;
                initializeBodies();
            }
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Draw the static (unchanging) textures
        batch.draw(sky, 0, 0, 0, 0, (int) stage.getWidth(), (int) stage.getHeight());
        batch.draw(grass, 0, 0, grass.getWidth()/SCALE, grass.getHeight()/SCALE);

        float centerX = game.GDX_WIDTH/2/SCALE;
        float dirtPatchCenterX = dirtpatch.getWidth()/2/SCALE;
        float dirtPatchCenterY = dirtpatch.getHeight()/2/SCALE;
        batch.draw(dirtplot, centerX - dirtplot.getWidth()/2/SCALE, game.GDX_WIDTH/4/SCALE, dirtplot.getWidth()/SCALE, dirtplot.getHeight()/SCALE);
        batch.draw(dirtpatch, centerX - dirtPatchCenterX, game.GDX_HEIGHT/4/SCALE - dirtPatchCenterY, dirtpatch.getWidth()/SCALE, dirtpatch.getHeight()/SCALE);

        // If the plot has a tree, draw it based on how long the tree has
        // to wait to mature.
        if (!plot.isEmpty())
        {
            float timeSincePlanted = plot.getTimeSincePlanted();
            float matureTime = plot.getPlantedTree().getMatureTime();
            int treeFrameIndex = MathUtils.clamp((int)(timeSincePlanted / matureTime * 123), 0, 123);

            String formatIndex = String.format("%03d", treeFrameIndex);
            Texture treeFrame = game.resources.getTexture(Resources.TREE_ANIM + formatIndex + ".png");

            float treeCenterX = treeFrame.getWidth()/2/SCALE;

            batch.draw(treeFrame, centerX - treeCenterX + treeCenterX/6, game.GDX_HEIGHT/4/SCALE, treeFrame.getWidth()/SCALE, treeFrame.getHeight()/SCALE);
            
           
            Texture fruitTexture = plot.getPlantedTree().getFruit().itemImage;

            if (plot.isReadyToHarvest() || harvesting)
            {
                for (int i = 0; i < fruitBodies.size; i++)
                {
                    Vector2 position = fruitBodies.get(i).getPosition();

                    batch.draw(fruitTexture, position.x - 25/SCALE, position.y - 25/SCALE, 50/SCALE, 50/SCALE);
                }
            }
        }

        
        batch.end();

        // Update and draw stage
        stage.act();
        stage.draw();

        //physDebug.render(physWorld, camera.combined);
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
        }
        else
            plantButton.setVisible(true);
    }

    @Override
    public void hide()
    {
        harvesting = false;
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        skin.dispose();
    }

    public void setPlot(Plot p)
    {
        if (plot == p)
            return;

        plot = p;
        if (!plot.isEmpty())
            initializeBodies();
    }


    // Helper methods

    private void initializeButtons()
    {
        plantButton = new TextButton("Plant", skin);
        table.row();
        table.add(plantButton).width(150).height(100).expandX().space(25);

        harvestButton = new TextButton("Harvest", skin);
        table.row();
        table.add(harvestButton).width(200).height(100);

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
                GameUtils.delaySetScreen(game, 0.15f, game.farmScreen);
            }
        });

        plantButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                stack.add(seedSelectTable);
                backButton.setVisible(false);
            }
        });

        harvestButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                harvesting = true;
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
                    seedImage.setPosition(game.GDX_WIDTH / 2  - seedImage.getWidth() / 4, game.GDX_HEIGHT * 3 / 4);
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
            if (harvesting)
            {
                harvestTimer.setText("Shake to harvest!");
            }

            else if (plot.isReadyToHarvest())
            {
                harvestTimer.setText("Ready to harvest");
            }

            else if (plot.isMature())
            {
                long timeToProduce = tree.getProduceRate() - plot.getTimeSinceLastHarvest();
                String format = timeFormat(timeToProduce);
                harvestTimer.setText("Time to next harvest: " + format);
            }

            else
            {
                long timeToMature = (tree.getMatureTime() - plot.getTimeSincePlanted());
                String format = timeFormat(timeToMature);
                harvestTimer.setText("Time to mature: " + format);
            }
        }
        else
        {
            plantedTreeLabel.setText("Currently planted: None");
            harvestTimer.setText("");
        }
    }

    private void initializeBodies()
    {
        physWorld.getBodies(fruitBodies);
        for (Body b : fruitBodies)
        {
            physWorld.destroyBody(b);
        }

        CircleShape circle = new CircleShape();
        circle.setRadius(2.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.friction = 0.5f;   
        fixtureDef.filter.groupIndex = -1;

        for (int i = 0; i < plot.getPlantedTree().getProduceAmount(); i++)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.allowSleep = true;
            bodyDef.awake = false;

            float x = (game.GDX_WIDTH/2 + random.nextInt(treeWidth * 2/3) - treeWidth/3) / SCALE;
            float y = (game.GDX_HEIGHT/2 + random.nextInt(game.GDX_HEIGHT/3) - game.GDX_HEIGHT/6) / SCALE;

            bodyDef.position.set(x, y);

            Body body = physWorld.createBody(bodyDef);

            Fixture fixture = body.createFixture(fixtureDef);
        } 

        physWorld.getBodies(fruitBodies);

        circle.dispose();
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
