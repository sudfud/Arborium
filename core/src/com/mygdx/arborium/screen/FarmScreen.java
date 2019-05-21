package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.GameUtils;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Plot;

import java.util.ArrayList;
import java.util.HashMap;

public class FarmScreen implements Screen, GestureDetector.GestureListener
{
    private float currentZoom;

    private float cameraX, cameraY;

    private Arborium game;

    private OrthographicCamera camera;

    private TiledMap farmMap;
    private TiledMapRenderer mapRenderer;

    private Stage stage;

    private Table table;
    private TextButton[] testHarvestButtons;
    private TextButton mainMenuButton;
    private TextButton shopButton;
    private Skin skin;

    private TextButton testButton;

    private Texture sky, grass, dirtplot;
    private Texture tree;

    private ShapeRenderer shapeRenderer;

    private ArrayList<Rectangle> plotRects;

    // Use this to connect clickable rectangles to plots
    private HashMap<Rectangle, Plot> plotMap;

    public FarmScreen(Arborium game)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 10, 15);

        stage = new Stage(new ScreenViewport());

        farmMap = new TmxMapLoader().load("farm1.tmx");
        mapRenderer = new OrthoCachedTiledMapRenderer(farmMap, 1 / 64f);
        mapRenderer.setView(camera);

        // Setup table
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        skin = game.getSkin(Arborium.GLASSY_SKIN);

        testHarvestButtons = new TextButton[9];
        initializeButtons();

        plotRects = new ArrayList<Rectangle>();
        plotMap = new HashMap<Rectangle, Plot>();
        MapLayers medFarmLayers = farmMap.getLayers();
        MapObjects[] medFarmObjects = new MapObjects[] 
        { 
            medFarmLayers.get("Med Farm 1").getObjects(),
            medFarmLayers.get("Med Farm 2").getObjects()
        };

        for (int i = 0; i < game.mediumFarms.length; i++)
        {
            MapObjects rects = medFarmObjects[i];
            for (int j = 0; j < rects.getCount(); j++)
            {
                RectangleMapObject rectObject = (RectangleMapObject) rects.get(j);
                Rectangle rect = rectObject.getRectangle();
                Rectangle scaledRect = new Rectangle(rect.x / 32, rect.y / 32, rect.width / 32, rect.height / 32);
                plotRects.add(scaledRect);
                plotMap.put(scaledRect, game.mediumFarms[i].getPlot(j));
            }
        }

        centerCamera();
    }

    @Override
    public void show()
    {
        InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(this);
        im.addProcessor(gd);
        im.addProcessor(stage);
        Gdx.input.setInputProcessor(im);

        // Grab needed textures from the game's resource manager.
        sky = game.getTexture(Arborium.BG_SKY);
        grass = game.getTexture(Arborium.GRASS);
        dirtplot = game.getTexture(Arborium.DIRT_PLOT);
        tree = game.getTexture(Arborium.TREE_OVERWORLD);

        //centerCamera();
    }

    @Override
    public void render(float delta)
    {
        for (Farm farm : game.mediumFarms)
            farm.update();

        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.update();

        //mapRenderer.setView(camera);
        mapRenderer.render();

        //game.spriteBatch.setProjectionMatrix(camera.combined);
        //game.spriteBatch.begin();
        for (Rectangle rect : plotRects)
        {
            Plot plot = plotMap.get(rect);
            if (!plot.isEmpty())
            {
          //      game.spriteBatch.draw(tree, rect.x, rect.y, rect.width, rect.height);
            }
        }
        //game.spriteBatch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause()
    {
        //centerCamera();
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {
        //centerCamera();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        farmMap.dispose();
    }

    private void initializeButtons()
    {
        // Setup main menu button
        mainMenuButton = new TextButton("Menu", skin);
        mainMenuButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                GameUtils.delaySetScreen(game, 0.15f, game.mainMenuScreen);
                return true;
            }
        });
        table.row();
        table.add(mainMenuButton).expand().bottom().left().width(150).height(100);


        // Setup shop button
        shopButton = new TextButton("Shop", skin);
        shopButton.addListener(new ClickListener()
        {
           @Override
           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
           {
               GameUtils.delaySetScreen(game, 0.15f, game.shopScreen);
               return true;
           }
        });
        table.add();
        table.add(shopButton).expand().bottom().right().width(150).height(100);
    }

    private void centerCamera()
    {
        camera.position.set(farmMap.getProperties().get("width", Integer.class)/2,
                farmMap.getProperties().get("height", Integer.class)/2, 0);
        camera.zoom = 2f;
        currentZoom = camera.zoom;
        camera.update();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        Gdx.app.log("FarmScreen", "Tapped...");
        Vector3 tmp = new Vector3(x, y, 0f);
        camera.unproject(tmp);
        for (int i = 0; i < plotRects.size(); i++)
        {
            Rectangle rect = plotRects.get(i);
            if (rect.contains(tmp.x, tmp.y))
            {
                Plot plot = plotMap.get(rect);
                if (!plot.getFarm().isLocked())
                {
                    game.plotScreen.setPlot(plotMap.get(rect));
                    game.setScreen(game.plotScreen);
                    break;
                }
            }
        }
        return true;
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
        float scaledDeltaX = deltaX * (camera.viewportWidth / game.GDX_WIDTH);
        float scaledDeltaY = deltaY * (camera.viewportHeight / game.GDX_WIDTH);
        camera.translate(-scaledDeltaX * currentZoom, scaledDeltaY * currentZoom);

        float viewportXHalf = camera.viewportWidth/2;
        float viewportYHalf = camera.viewportHeight/2;

        float mapWidth = farmMap.getProperties().get("width", Integer.class);
        float mapHeight = farmMap.getProperties().get("height", Integer.class);

        camera.position.x = MathUtils.clamp(camera.position.x, viewportXHalf, mapWidth - viewportXHalf);
        camera.position.y = MathUtils.clamp(camera.position.y, viewportYHalf, mapHeight - viewportYHalf);
        
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        currentZoom = camera.zoom;
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        camera.zoom = MathUtils.clamp((initialDistance / distance) * currentZoom, 0.25f, 2.5f);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
