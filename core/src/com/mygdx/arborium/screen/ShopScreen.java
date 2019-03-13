package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Seed;
import com.mygdx.arborium.items.ShopItem;

import java.util.ArrayList;

public class ShopScreen implements Screen
{
    private String selectedItemName;
    private int selectedItemPrice;

    // Keeps track of which shop section we're in; either Buy or Sell
    private String currentSection = "Buy";

    private Arborium game;

    // Used to select between buying and selling items
    SelectBox shopSelectBox;

    List<String> itemList;
    Label currencyLabel;
    Label priceLabel;
    TextButton buyButton;
    TextButton backButton;
    Table table;
    Stage stage;
    Skin skin;

    Texture background;

    public ShopScreen(Arborium game)
    {
        this.game = game;

        stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setFillParent(true);
        // table.setDebug(true);
        stage.addActor(table);

        skin = game.resources.getSkin(Resources.GLASSY_SKIN);

        // Set up selection box for buying and selling items (maybe easier to split the two?)
        shopSelectBox = new SelectBox(skin);
        shopSelectBox.setItems("Buy", "Sell");
        shopSelectBox.setSelected("Buy");
        shopSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (shopSelectBox.getSelected().equals("Buy"))
                {
                    setBuyItems();
                }
                else
                {
                    itemList.setItems(Inventory.getItems());
                }

                buyButton.setText((String)shopSelectBox.getSelected());
                updateLabels();
            }
        });
        table.add(shopSelectBox).width(200);
        table.row();

        // Set up label to show current currency amt
        String currentCurrency = "Currency: " + Currency.getAmount();
        currencyLabel = new Label(currentCurrency, skin);
        currencyLabel.setFontScale(2);
        table.add(currencyLabel).expandX().top().height(100);
        table.row();

        itemList = new List<String>(skin);
        itemList.getStyle().selection.setTopHeight(16);
        itemList.getStyle().selection.setBottomHeight(16);
        itemList.setItems(game.seedList.getSeedNames());
        itemList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                updateLabels();
            }
        });
        table.add(itemList).height(750).width(500).center();
        table.row();

        priceLabel = new Label("Price: ", skin);
        priceLabel.setFontScale(2);
        updateLabels();
        table.add(priceLabel).left();

        buyButton = new TextButton("Buy", skin);
        buyButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if (shopSelectBox.getSelected().equals("Buy") && Currency.subtract(selectedItemPrice))
                    Inventory.addItem(selectedItemName, 1);

                else
                {
                    Currency.add(selectedItemPrice);
                    Inventory.takeItem(selectedItemName);
                }

                updateLabels();
                return true;
            }
        });
        table.add();
        table.add(buyButton).width(150).height(100);

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                back();
                return true;
            }
        });
        table.row();
        table.add(backButton);

        background = game.resources.getTexture(Resources.BG_SKY);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw Background
        game.spriteBatch.begin();
        game.spriteBatch.draw(background, 0, 0);
        game.spriteBatch.end();

        // Draw UI stuff
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

    private void updateLabels()
    {
        if (itemList.getItems().size > 0)
        {
            selectedItemName = itemList.getSelected();
            if (shopSelectBox.getSelected().equals("Sell"))
            {
                int cutoff = selectedItemName.indexOf(':');
                if (cutoff != -1)
                    selectedItemName = selectedItemName.substring(0, cutoff);
            }
            ShopItem item = (ShopItem) Item.lookup(selectedItemName);

            if (shopSelectBox.getSelected().equals("Buy"))
                selectedItemPrice = item.buyValue;
            else
                selectedItemPrice = item.sellValue;

            priceLabel.setText("Price: " + selectedItemPrice);

            int currency = Currency.getAmount();
            currencyLabel.setText("Currency: " + currency);

            if (shopSelectBox.getSelected().equals("Buy"))
            {
                String[] shopItems = Item.getItemsOfType(Seed.class);
                ArrayList<String> buyableItems = new ArrayList<String>();

                itemList.setItems(shopItems);
            }
            else
            {
                String[] itemNames = Inventory.getItemsOfType(ShopItem.class);
                itemList.setItems(itemNames);
            }
        }
    }

    private void setBuyItems()
    {
        itemList.setItems(game.seedList.getSeedNames());
    }

    private void setSellItems()
    {

    }

    private void back()
    {
        game.setScreen(game.farmScreen);
    }
}
