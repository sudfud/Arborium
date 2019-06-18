package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Sapling;
import com.mygdx.arborium.items.ShopItem;

public class ShopScreen implements Screen
{
    Arborium game;

    String[] buyItemCategories = {"Saplings"};
    String[] sellItemCategories = {"Fruit"};

    Stage stage;
    Skin skin;

    Table shopTable;

    Window shopBuyWindow;
    TextButton buyButton;
    TextButton sellButton;
    SelectBox<String> itemSelectBox;
    List<String> itemList;

    ShopItem selectItem;

    Window shopSellWindow;

    Image itemImage;
    Label itemDescription;

    Image coinIcon;
    Label priceLabel;

    enum Category
    {
        SAPLINGS, FRUIT, UPGRADES
    }

    public ShopScreen(Arborium game)
    {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        skin = game.getSkin(Arborium.ARBOR_SKIN);

        shopTable = new Table();
        shopBuyWindow = new Window("Buy", skin);
        shopSellWindow = new Window("Sell", skin);
        buyButton = new TextButton("Buy", skin);
        sellButton = new TextButton("Sell", skin);
        itemSelectBox = new SelectBox<String>(skin);
        itemList = new List<String>(skin);

        itemImage = new Image();
        itemDescription = new Label("Test description! :)", skin);

        coinIcon = new Image(new TextureRegionDrawable(game.getTexture(Arborium.COIN)));
        priceLabel = new Label("", skin);

        shopTable.setFillParent(true);

        itemDescription.setWrap(true);

        buyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buyButton.setDisabled(true);
                sellButton.setDisabled(false);
            }
        });

        sellButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                sellButton.setDisabled(true);
                buyButton.setDisabled(false);
            }
        });

        itemSelectBox.setItems("Saplings", "Fruit", "Upgrades");

        String[] saplings = Inventory.getItemsOfType(Sapling.class);
        itemList.setItems(saplings);

        itemList.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                updateShopWindow();
            }
        });

        itemList.setSelectedIndex(0);
        updateShopWindow();

        shopBuyWindow.add(itemSelectBox).space(10).width(300).colspan(2).left();
        shopBuyWindow.row();
        shopBuyWindow.add(itemList).space(10).width(300).colspan(2).left();

        shopBuyWindow.row();
        shopBuyWindow.add(itemImage).space(25);
        shopBuyWindow.add(itemDescription).width(500);
        shopBuyWindow.row();
        shopBuyWindow.add(buyButton);
        shopBuyWindow.add(coinIcon);
        shopBuyWindow.add(priceLabel);
        shopBuyWindow.pack();
        shopTable.add(shopBuyWindow).maxWidth(750).minHeight(500);

        stage.addActor(shopTable);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        itemList.setSelectedIndex(0);
        updateShopWindow();
    }

    @Override
    public void render(float delta)
    {
        // Clear screen
        Gdx.gl.glClearColor(100/255f, 1, 244/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
    public void dispose() {

    }

    private void updateShopWindow()
    {
        selectItem = (ShopItem) Item.lookup(itemList.getSelected());
        itemImage.setDrawable(new TextureRegionDrawable(selectItem.itemImage));
        itemDescription.setText(selectItem.description);
        priceLabel.setText("" + selectItem.buyValue);
        //shopBuyWindow.invalidate();
    }
}
