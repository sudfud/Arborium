package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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

public class PlotScreen implements Screen, GestureListener {
    // Indeces used for Box2D collision filtering
    final int FRUIT_INDEX = -1;
    final int BASKET_INDEX = -2;

    final float SCALE = 25.0f;

    final float BASKET_WIDTH;
    final float BASKET_HEIGHT;

    float basketX;
    float basketY = 0;

    boolean basketTouched;

    Texture basketTexture;
    Image basketImage;
    Body basketBody;

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
    Array<Body> fruitDeleteList;

    private Timer fruitDropTimer;

    private HashMap<Plot, Vector2[]> fruitPositionMap;

    public PlotScreen(final Arborium game, Plot plot) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        this.plot = plot;

        BASKET_WIDTH = game.GDX_WIDTH / 3 / SCALE;
        BASKET_HEIGHT = game.GDX_WIDTH / 6 / SCALE;

        basketX = game.GDX_WIDTH / 2 / SCALE - BASKET_WIDTH / 2;
        Gdx.app.log("PlotScreen", "" + basketX);

        basketTexture = game.getTexture(Arborium.BASKET);
        basketImage = new Image(basketTexture);
        basketImage.setSize(BASKET_WIDTH * SCALE, BASKET_HEIGHT * SCALE);
        basketImage.setPosition(basketX * SCALE, basketY * SCALE);
        basketImage.setVisible(false);
        basketImage.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                basketTouched = true;
                return true;
            }

            // Send the seed image to its original position
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                basketTouched = false;
            }
        });
        stage.addActor(basketImage);

        // Setup Stack and add to stage
        stack = new Stack();
        stack.setFillParent(true);
        stage.addActor(stack);

        // Setup main table and add to stack
        table = new Table();
        table.setFillParent(true);
        stack.add(table);
        stack.setDebug(true);

        skin = game.getSkin(Arborium.GLASSY_SKIN);

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

        random = new Random();

        initializeButtons();
        addButtonListeners();

        physWorld = new World(new Vector2(0, -9.81f), true);
        
        physDebug = new Box2DDebugRenderer();
        fruitBodies = new Array<Body>();
        fruitDeleteList = new Array<Body>();

        fruitDropTimer = new Timer();

        fruitPositionMap = new HashMap<Plot, Vector2[]>();

        camera = new OrthographicCamera(game.GDX_WIDTH / SCALE, game.GDX_HEIGHT / SCALE);
        camera.position.set(game.GDX_WIDTH / 2 / SCALE, game.GDX_HEIGHT / 2 / SCALE, 0);
        camera.update();

        Texture tree = game.getTexture("Tree2/sprite_tree_2000.png");
        treeWidth = tree.getWidth();
        treeHeight = tree.getHeight();
    }

    @Override
    public void show()
    {
        seedSelectList.setItems(Inventory.getItemsOfType(Seed.class));

        addButtonListeners();

        updateLabels();

        plantButton.setVisible(plot.isEmpty());

        InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(this);

        im.addProcessor(gd);
        im.addProcessor(stage);

        Gdx.input.setInputProcessor(im);

        batch = game.spriteBatch;

        physWorld.setContactListener(new ContactListener() 
        {
            @Override
            public void beginContact(Contact contact) 
            {
                Fixture fruit = contact.getFixtureA().getFilterData().groupIndex == FRUIT_INDEX ?
                    contact.getFixtureA() :
                    contact.getFixtureB();

                Body fruitBody = fruit.getBody();
                fruitDeleteList.add(fruitBody);
                Inventory.addItem(plot.getPlantedTree().getFruit().itemName, 1);
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

        });

        Texture seedTexture = game.getTexture(Arborium.APPLE_SEED);
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
                seedImage.setY(game.GDX_HEIGHT * 3 / 4);
            }
        });
        seedImage.setVisible(false);
        stage.addActor(seedImage);

        if (!plot.isEmpty())
        {
            initializeBodies();
        }

        // Grab all the textures needed from the game's resource manager
        sky = game.getTexture(Arborium.BG_SKY);
        grass = game.getTexture(Arborium.GRASS);
        dirtplot = game.getTexture(Arborium.DIRT_PLOT);
        dirtpatch = game.getTexture(Arborium.DIRT_PATCH);
    }

    @Override
    public void render(float delta)
    {
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

        if (basketTouched)
        {
            int x = Gdx.input.getX();
            basketImage.setX(x);
        }

        // This should only be true if there's currently a seed selected to plant
        if (seedTouched)
        {
            int y = game.GDX_HEIGHT - Gdx.input.getY();

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

        physWorld.step(1 / 60f, 6, 2);

        if (harvesting)
         {
            // Harvest the tree and stop drawing the fruit when the phone
            // is shaken.
            float shakeAmt = Math.abs(Gdx.input.getAccelerometerX());
            if (shakeAmt >= 25) 
            {
                plot.harvest();
                physWorld.getBodies(fruitBodies);
                for (int i = 0; i < fruitBodies.size; i++) 
                {
                    Fixture bodyFixture = fruitBodies.get(i).getFixtureList().get(0);
                    if (bodyFixture.getFilterData().groupIndex == FRUIT_INDEX)
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
            }

            int fruitOffScreenCount = 0;

            if (fruitDeleteList.size > 0)
            {
                for (Body body : fruitDeleteList)
                {
                    physWorld.destroyBody(body);
                }
                fruitDeleteList.clear();
            }

            physWorld.getBodies(fruitBodies);
            for (Body b : fruitBodies)
            {
                Fixture bodyFixture = b.getFixtureList().first();
                if (bodyFixture.getFilterData().groupIndex == FRUIT_INDEX && b.getPosition().y < -50/SCALE)
                {
                    //fruitOffScreenCount++;
                    fruitDeleteList.add(b);
                }

                else if (bodyFixture.getFilterData().groupIndex == BASKET_INDEX)
                {
                    b.getPosition().set(basketX + BASKET_WIDTH/2, basketY);
                }
            }

            if(physWorld.getBodyCount() <= 1)
            {
                harvesting = false;
                initializeBodies();

                // For some reason the harvest button won't work when the screen's unchanged unless the listener is re-added
                addButtonListeners();
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

        // If the plot has a tree, draw it based on how long the tree has
        // to wait to mature.
        if (!plot.isEmpty())
        {
            float timeSincePlanted = plot.getTimeSincePlanted();
            float matureTime = plot.getPlantedTree().getMatureTime();
            int treeFrameIndex = MathUtils.clamp((int)(timeSincePlanted / matureTime * 123), 0, 123);

            String formatIndex = String.format("%03d", treeFrameIndex);
            Texture treeFrame = game.getTexture(Arborium.TREE_ANIM + formatIndex + ".png");

            float treeCenterX = treeFrame.getWidth()/2/SCALE;

            batch.draw(treeFrame, (float)game.GDX_WIDTH/8/SCALE, (float)game.GDX_HEIGHT/4/SCALE, (float)game.GDX_WIDTH*3/4/SCALE, (float)game.GDX_HEIGHT*3/4/SCALE);
           
            Texture fruitTexture = plot.getPlantedTree().getFruit().itemImage;

            // Draw fruit onto tree
            if (plot.isReadyToHarvest() || harvesting)
            {
                for (int i = 0; i < fruitBodies.size; i++)
                {
                    Body body = fruitBodies.get(i);
                    Fixture fixture = body.getFixtureList().first();
                    if (fixture.getFilterData().groupIndex == FRUIT_INDEX)
                    {
                        Vector2 position = body.getPosition();
                        float radius = game.GDX_WIDTH / 20 / SCALE;
                        batch.draw(fruitTexture, position.x - radius, position.y - radius, radius * 2, radius * 2);
                    }
                }

                if (harvesting)
                {
                    batch.draw(basketTexture, basketX, basketY, BASKET_WIDTH, BASKET_HEIGHT);
                }
            }
        }

        //batch.draw(dirtpatch, (float)game.GDX_WIDTH/4/SCALE, game.GDX_HEIGHT/4/SCALE - dirtPatchCenterY, dirtpatch.getWidth()/SCALE, dirtpatch.getHeight()/SCALE);
        
        batch.end();

        // Update and draw stage
        stage.act();
        stage.draw();

        physDebug.render(physWorld, camera.combined);
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
                    seedImage.setSize(seed.itemImage.getWidth()/5, seed.itemImage.getHeight()/5);
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
        circle.setRadius(game.GDX_WIDTH/20/SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.friction = 0.5f;   
        fixtureDef.filter.groupIndex = FRUIT_INDEX;

        for (int i = 0; i < plot.getPlantedTree().getProduceAmount(); i++)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.allowSleep = true;
            bodyDef.awake = false;

            float x = (random.nextInt(game.GDX_WIDTH/2) + game.GDX_WIDTH/4) / SCALE;
            float y = (random.nextInt(game.GDX_WIDTH*2/3) + game.GDX_HEIGHT/2) / SCALE;

            bodyDef.position.set(x, y);

            Body body = physWorld.createBody(bodyDef);

            Fixture fixture = body.createFixture(fixtureDef);
        } 

        PolygonShape baskRect = new PolygonShape();
        baskRect.setAsBox(BASKET_WIDTH/2, BASKET_HEIGHT/2);
        FixtureDef baskFixture = new FixtureDef();
        baskFixture.shape = baskRect;
        baskFixture.filter.groupIndex = BASKET_INDEX;

        BodyDef baskBody = new BodyDef();
        baskBody.type = BodyType.KinematicBody;
        baskBody.position.set(basketX + BASKET_WIDTH/2, basketY);

        basketBody = physWorld.createBody(baskBody);
        Fixture fix = basketBody.createFixture(baskFixture);

        physWorld.getBodies(fruitBodies);

        circle.dispose();
        baskRect.dispose();
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

    private boolean basketTouched(float x, float y)
    {
        Rectangle rect = new Rectangle(basketX, basketY, BASKET_WIDTH, BASKET_HEIGHT);
        return rect.contains(x, y);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) 
    {
        Gdx.app.log("PlotScreen", "Panning...");
        Gdx.app.log("PlotScreen", "Delta X: " + deltaX);
        if (harvesting)
        {
            float scaledDeltaX = deltaX * (camera.viewportWidth / game.GDX_WIDTH);
            Gdx.app.log("PlotScreen", "Scaled Delta X: " + scaledDeltaX);
            basketX += scaledDeltaX;
            basketBody.setTransform(basketX + BASKET_WIDTH/2, basketY, 0f);
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
