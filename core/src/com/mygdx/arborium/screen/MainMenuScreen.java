package com.mygdx.arborium.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.arborium.Arborium;

public class MainMenuScreen implements Screen
{
    Arborium game;

    // Camera and Images
    Texture background;
    Texture cloud;
    OrthographicCamera camera;

    //UI
    Skin skin;

    Stage stage;
    Table table;

    ImageTextButton title;

    Image cloudImage;

    TextButton startButton;
    TextButton optionButton;
    TextButton quitButton;

    // Clouds! :)
    ParticleEffect cloudParticles;
    TextureAtlas arborAtlas;

    public MainMenuScreen(Arborium game)
    {
        this.game = game;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 10, 15);

        // Initialize background images
        background = game.getTexture(Arborium.BG_SKY);
        cloud = game.getTexture(Arborium.CLOUD);
        cloudImage = new Image(cloud);
        cloudImage.setScale(2);

        skin = game.getSkin(Arborium.ARBOR_SKIN);
        //skin.getFont("comic").getData().setScale(2);

        // Setup stage and table
        stage = new Stage(new ExtendViewport(800, 480));
        table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);
        //stage.addActor(cloudImage);

        title = new ImageTextButton("Arborium", skin, "title");
        //title.setPosition(0, 0);
        table.add(title).center();

        // Setup start button
        startButton = new TextButton("Start", skin);
        startButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                toFarmScreen();
                return true;
            }
        });
        startButton.setPosition(game.GDX_WIDTH/2, -200);
        table.row();
        table.add(startButton).width(200).height(100).space(25).expandX();


        // Setup quit button
        quitButton = new TextButton("Exit", skin);
        quitButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.exit();
                return true;
            }
        });
        table.row();
        table.add(quitButton).width(200).height(100).space(25).expandX();

        arborAtlas = new TextureAtlas(Gdx.files.internal("arborium.atlas"));
        cloudParticles = new ParticleEffect();
        cloudParticles.load(Gdx.files.internal("falling_fruits.p"), arborAtlas);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);

        // Move cloud and start its movement
        cloudImage.setPosition(0, game.GDX_HEIGHT*4/5);
        MoveToAction action = new MoveToAction();
        action.setPosition(game.GDX_WIDTH, game.GDX_HEIGHT*4/5);
        action.setDuration(10);
        cloudImage.addAction(action);
        cloudParticles.setPosition(0, 10);
        cloudParticles.start();
    }

    @Override
    public void render(float delta)
    {
        // Clear screen
        Gdx.gl.glClearColor(100/255f, 1, 244/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Draw background

        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();
        // cloudParticles.draw(game.spriteBatch, Gdx.graphics.getDeltaTime());
        cloudParticles.draw(game.spriteBatch, Gdx.graphics.getDeltaTime());
         game.spriteBatch.end();

        // Update and draw UI
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        float aspectRatio = (float) width / (float) height;
        camera = new OrthographicCamera(2f * aspectRatio, 2f);

        camera.zoom = 7.5f;
        camera.update();
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
    public void dispose() {

    }

    private void toFarmScreen()
    {
        game.setScreen(game.farmScreen);
    }
}
