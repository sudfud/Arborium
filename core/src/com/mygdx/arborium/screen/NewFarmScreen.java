package com.mygdx.arborium.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.game.Plot;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Sapling;
import com.mygdx.arborium.items.SaplingList;
import com.mygdx.arborium.items.Fruit.FruitType;
import com.mygdx.arborium.ui.PriceLabel;

public class NewFarmScreen implements Screen, GestureListener 
{
    private int selectedPlot = 0;

    Arborium game;

    OrthographicCamera camera;

    TiledMap map;
    OrthoCachedTiledMapRenderer mapRenderer;

    // Objects on the map used to detect when plots are tapped
    ArrayList<Rectangle> plotRects;

    Farm farm;

    // UI stuff
    Stage stage;
    Skin skin;

    Table backTable;
    PriceLabel currencyLabel;
    TextButton menuButton;
    TextButton shopButton;

    Container<Window> windowContainer;
    Window plantSeedWindow;
    Image seedImage;
    List<Sapling> saplingList;
    TextButton plantConfirmButton;
    TextButton plantBackButton;

    Table plotInfoTable;
    Window plotInfoWindow;
    Label plotInfoLabel;
    Label plotInfoTime;
    Image plotImage;
    HorizontalGroup plotInfoButtons;
    TextButton plantButton;
    TextButton harvestButton;
    TextButton clearButton;
    TextButton plotBackButton;
    Button leftButton;
    Button rightButton;

    TextureRegionDrawable plotDrawable;
    TextureRegionDrawable sproutDrawable;
    TextureRegionDrawable plainTreeDrawable;

    // Particle stuff
    ParticleEffectPool effectPool;
    Array<PooledEffect> effects;

    HashMap<Sapling, TextureAtlas> treeParticleMap;

    TextureAtlas fruitParticles;

    ParticleEffect testEffect;

    public NewFarmScreen(Arborium game, String mapName, int farmID)
    {
        this.game = game;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 10, 15);

        stage = new Stage(new ScreenViewport());
        skin = game.getSkin(Arborium.ARBOR_SKIN);

        map = new TmxMapLoader().load(mapName);

        mapRenderer = new OrthoCachedTiledMapRenderer(map, 1 / 64f);
        mapRenderer.setBlending(true);

        // Get plot objects
        MapLayers layers = map.getLayers();
        MapObjects plotBoxes = layers.get("plot_object").getObjects();
        plotRects = new ArrayList<Rectangle>();

        for (int i = 0; i < plotBoxes.getCount(); i++)
        {
            RectangleMapObject obj = (RectangleMapObject) plotBoxes.get(i);
            Rectangle rect = obj.getRectangle();
            rect.setSize(rect.width/64, rect.height/64);
            rect.setPosition(rect.x/64, rect.y/64);

            plotRects.add(rect);
        }

        farm = new Farm(this.game, "Farm1", 16, false);

        setupUI();

        plotDrawable = new TextureRegionDrawable(game.getTexture(Arborium.PLOT2X));
        sproutDrawable = new TextureRegionDrawable(game.getTexture(Arborium.PLANT));
        plainTreeDrawable = new TextureRegionDrawable(game.getTexture(Arborium.TREE_DEFAULT2X));

        treeParticleMap = new HashMap<Sapling,TextureAtlas>();
        fruitParticles = new TextureAtlas("apple.atlas");
        //orangeParticle = new TextureAtlas(Arborium.ORANGE_FRUIT);

        //treeParticleMap.put(SaplingList.get(FruitType.APPLE), appleParticle);
        //treeParticleMap.put(SaplingList.get(FruitType.ORANGE), orangeParticle);

        effects = new Array<ParticleEffectPool.PooledEffect>();

        ParticleEffect harvestEffect = new ParticleEffect();
        harvestEffect.load(Gdx.files.internal("part_fruit.p"), fruitParticles);

        effectPool = new ParticleEffectPool(harvestEffect, 1, 10);
    }

    @Override
    public void show() 
    {
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(stage);
        GestureDetector gd = new GestureDetector(this);
        im.addProcessor(gd);
        Gdx.input.setInputProcessor(im);
        //Gdx.input.setInputProcessor(stage);

        updateSaplingList();

        // Center camera on the farm map
        camera.position.set(map.getProperties().get("width", Integer.class) / 2f,
                map.getProperties().get("height", Integer.class) / 2f, 0);
        camera.update();

        mapRenderer.setView(camera);
    }

    @Override
    public void render(float delta)
    {
        farm.update();

        // Clear screen
        Gdx.gl.glClearColor(100/255f, 1, 244/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mapRenderer.render();
       
        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();
        for (int i = 0; i < farm.getPlotSize(); i++)
        {
            Plot plot = farm.getPlot(i);
            Rectangle rect = plotRects.get(i);
            switch(plot.getCurrentState())
            {
                case PREMATURE:
                    Texture plant = game.getTexture(Arborium.PLANT);
                    game.spriteBatch.draw(plant, rect.x, rect.y, plant.getWidth()/128f, plant.getHeight()/128f);
                    break;
                case MATURE:
                    Texture tree = game.getTexture(Arborium.TREE_DEFAULT);
                    game.spriteBatch.draw(tree, rect.x, rect.y, tree.getWidth()/64f, tree.getHeight()/64f);
                    break;
                case HARVESTABLE:
                    Texture fruityTree = plot.getPlantedTree().itemImage;
                    game.spriteBatch.draw(fruityTree, rect.x, rect.y, fruityTree.getWidth()/128f, fruityTree.getHeight()/128f);
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < effects.size; i++)
        {
            PooledEffect effect = effects.get(i);
            effect.draw(game.spriteBatch, Gdx.graphics.getDeltaTime());
            if (effect.isComplete())
            {
                effect.free();
                effects.removeIndex(i);
            }
        }

        game.spriteBatch.end();

        if (plotInfoWindow.hasParent())
            updateLabels();

        stage.act();
        stage.draw(); 
    }

    private void setupUI()
    {
        // Background table initialization

        backTable = new Table();
        currencyLabel = new PriceLabel(game);
        menuButton = new TextButton("Menu", skin);
        shopButton = new TextButton("Shop", skin);

        backTable.setFillParent(true);

        menuButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(game.mainMenuScreen);
            }
        });

        shopButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(game.shopScreen);
            }
        });

        backTable.add(currencyLabel).colspan(2).expand().top().right();
        backTable.row();
        backTable.add(menuButton).expand().bottom().left().width(200).height(100);
        backTable.add(shopButton).expand().bottom().right().width(200).height(100);

        stage.addActor(backTable);

        // Seed planting window initialization

        windowContainer = new Container<Window>();
        plantSeedWindow = new Window("Plant seed", skin);
        seedImage = new Image(game.getTexture(Arborium.APPLE_FRUIT));
        saplingList = new List<Sapling>(skin);
        plantConfirmButton = new TextButton("Confirm", skin);
        plantBackButton = new TextButton("Back", skin);
        
        windowContainer.setFillParent(true);
        
        saplingList.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (saplingList.getItems().size > 0)
                {
                    Sapling selectedSeed = saplingList.getSelected();
                    seedImage.setDrawable(new TextureRegionDrawable(selectedSeed.itemImage));
                }
                else
                    seedImage.clear();
            }
        });

        updateSaplingList();

        plantConfirmButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (!saplingList.getItems().isEmpty())
                {
                    Sapling selectSapling = saplingList.getSelected();
                    Plot plot = farm.getPlot(selectedPlot);
                    plot.plantSapling(selectSapling);
                    Inventory.takeItem(selectSapling.itemName);
                    windowContainer.remove();
                    updateSaplingList();
                }
            }
        });

        plantBackButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                windowContainer.remove();
            }
        });

        plantSeedWindow.add(seedImage).pad(12.5f);
        plantSeedWindow.add(saplingList).pad(12.5f);
        plantSeedWindow.row();
        plantSeedWindow.add(plantConfirmButton);
        plantSeedWindow.add(plantBackButton);
        plantSeedWindow.pack();
        windowContainer.setActor(plantSeedWindow);

        // Plot info window initialization

        plotInfoTable = new Table();
        plotInfoTable.setFillParent(true);
        plotInfoWindow = new Window("Test", skin);
        plotInfoLabel = new Label("Test", skin);
        plotInfoTime = new Label("", skin);
        plotImage = new Image(game.getTexture(Arborium.PLOT2X));
        plotInfoButtons = new HorizontalGroup();
        plantButton = new TextButton("Plant", skin);
        harvestButton = new TextButton("Harvest", skin);
        clearButton = new TextButton("Clear", skin);
        plotBackButton = new TextButton("Back", skin);
        leftButton = new Button(skin, "left");
        rightButton = new Button(skin, "right");

        plantButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                stage.addActor(windowContainer);
                plotInfoTable.remove();
            }
        });

        plotBackButton.addListener(new ClickListener()
        {   
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                plotInfoTable.remove();
            }
        });

        leftButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                selectedPlot--;
                updatePlotInfo();
            }
        });

        rightButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                selectedPlot++;
                updatePlotInfo();
            }
        });

        plotInfoWindow.add(plotInfoLabel);
        plotInfoWindow.row();
        plotInfoWindow.add(plotInfoTime);
        plotInfoWindow.row();
        plotInfoWindow.add(plotImage);
        plotInfoWindow.row();

        plotInfoButtons.space(25);
        plotInfoButtons.pad(25);

        plotInfoButtons.addActor(plantButton);
        plotInfoButtons.addActor(clearButton);
        plotInfoButtons.addActor(plotBackButton);
        
        plotInfoWindow.add(plotInfoButtons);
        plotInfoWindow.pack();

        plotInfoTable.add(leftButton).space(25);
        plotInfoTable.add(plotInfoWindow);
        plotInfoTable.add(rightButton).space(25);

        //stage.addActor(plotInfoTable);
    }

    private void updatePlotInfo()
    {
        if (selectedPlot == 0)
            leftButton.setVisible(false);
        else if (!leftButton.isVisible())
            leftButton.setVisible(true);

        if (selectedPlot == 15)
            rightButton.setVisible(false);
        else if (!rightButton.isVisible())
            rightButton.setVisible(true);

        Plot plot = farm.getPlot(selectedPlot);
        plotInfoWindow.getTitleLabel().setText("Plot " + (selectedPlot + 1));


        switch(plot.getCurrentState())
        {
            case EMPTY:
                plotImage.setDrawable(plotDrawable);
                break;

            case PREMATURE:
                plotImage.setDrawable(sproutDrawable);
                break;

            case MATURE:
                plotImage.setDrawable(plainTreeDrawable);
                break;

            case HARVESTABLE:
                Texture treeImage = plot.getPlantedTree().itemImage;
                plotImage.setDrawable(new TextureRegionDrawable(treeImage));
                break;
        }

        plotInfoWindow.invalidate();
    }

    private void updateLabels()
    {
        Plot plot = farm.getPlot(selectedPlot);
        plotInfoWindow.getTitleLabel().setText("Plot " + (selectedPlot + 1));

        String info = "";
        String time = "";

        switch(plot.getCurrentState())
        {
            case EMPTY:
                info = "Empty";
                time = "";
                break;

            case PREMATURE:
                info = "Premature";
                time = "Matures in: " + timeFormat(plot.getMatureTime() - plot.getTimeSincePlanted());
                break;

            case MATURE:
                info = "Mature";
                time = timeFormat(plot.getProduceRate() - plot.getTimeSinceLastHarvest());
                break;

            case HARVESTABLE:
                info = "Harvestable";
                time = "Ready to harvest!";
                break;
        }

        currencyLabel.setText("" + Currency.getAmount());

        plotInfoLabel.setText(info);
        plotInfoTime.setText(time);

        plotInfoWindow.invalidate();
    }

    private void updateSaplingList()
    {
        String[] seedStrings = Inventory.getItemsOfType(Sapling.class);
        Sapling[] seeds = new Sapling[seedStrings.length];

        for (int i = 0; i < seeds.length; i++)
        {
            seeds[i] = (Sapling)Item.lookup(seedStrings[i]);
        }

        saplingList.setItems(seeds);
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
        fruitParticles.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 temp = new Vector3(x, y, 0);
        Gdx.app.log("FarmScreen", "" + plotRects.get(0).x + ", " + plotRects.get(0).y);
        camera.unproject(temp);
        Gdx.app.log("FarmScreen", "Tapped..." + temp.x + ", " + temp.y);

        // Check if the plot info table is already displaying
        if (!plotInfoTable.hasParent())
        {
            // Find the plot that was tapped, if any
            for (int i = 0; i < plotRects.size(); i++)
            {
                Rectangle rect = plotRects.get(i);
                if (rect.contains(temp.x, temp.y))
                {
                    selectedPlot = i;
                    Plot plot = farm.getPlot(i);
                    if (plot.getCurrentState() == Plot.PlotState.HARVESTABLE)
                    {
                        plot.harvest();
                        PooledEffect effect = effectPool.obtain();
                        //effect.load(Gdx.files.internal("part_fruit.p"), treeParticleMap.get(plot.getPlantedTree()));
                        Sapling tree = plot.getPlantedTree();
                        if (tree == SaplingList.get(FruitType.APPLE))
                        {
                            effect.load(Gdx.files.internal("part_fruit.p"), fruitParticles);
                        }
                        else if (tree == SaplingList.get(FruitType.ORANGE))
                        {
                            effect.load(Gdx.files.internal("part_orange.p"), fruitParticles);
                        }

                        effect.setPosition(rect.x + 0.75f, rect.y + 1);
                        effects.add(effect);
                        effect.start();
                    }

                    else
                    {
                        updatePlotInfo();
                        stage.addActor(plotInfoTable);
                    }
                    return true;
                }
            }
        }

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
        return false;
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