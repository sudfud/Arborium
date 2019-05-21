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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.GameUtils;
import com.mygdx.arborium.Resources;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Seed;
import com.mygdx.arborium.items.ShopItem;

import java.util.ArrayList;

public class ShopScreen implements Screen
{
    // Keeps track of which shop section we're in; either Buy or Sell

    private Arborium game;

    Label columnLabel;

    ScrollPane scrollPane;
    Table itemTable;

    // Used to select between buying and selling items
    SelectBox<String> shopSelectBox;

    //List<String> itemList;
    Label currencyLabel;

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

        skin = game.getSkin(Arborium.GLASSY_SKIN);

        // Set up selection box for buying and selling items (maybe easier to split the two?)
        shopSelectBox = new SelectBox<String>(skin);
        shopSelectBox.setItems("Buy", "Sell");
        shopSelectBox.setSelected("Buy");
        shopSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (shopSelectBox.getSelected().equals("Buy"))
                {
                    //setBuyItems();
                    setupBuyItemList();
                }
                else
                {
                    setupSellItemList();
                }
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
        //table.add(itemList).height(750).width(500).center();
        //table.row();

        String columns = String.format("%-15s%-7s%-7s", "Name", "Count", "Price");
        columnLabel = new Label(columns, skin);
        columnLabel.setFontScale(2);
        table.add(columnLabel).width(game.GDX_WIDTH - 100);
        table.row();

        itemTable = new Table(skin);
        scrollPane = new ScrollPane(itemTable);
        table.add(scrollPane).height(750).width(game.GDX_WIDTH).center();
        table.row();

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

        background = game.getTexture(Arborium.BG_SKY);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        shopSelectBox.setSelected("Buy");
        setupBuyItemList();
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
    public void resume()
    {
        shopSelectBox.setSelectedIndex(0);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    private void back()
    {
        GameUtils.delaySetScreen(game, 0.15f, game.farmScreen);
    }

    private void setupBuyItemList()
    {
        itemTable.clear();
        String[] items = game.seedList.getSeedNames();
        for (String item : items)
        {
            final ShopItem i = game.seedList.get(item);
            final String name = i.itemName;
            final int count = Inventory.containsItem(i) ? Inventory.getCount(name) : 0;
            final int price = i.buyValue;

            String labelString = String.format("%-15s%-7d%-7d", name, count, price);
            Label itemLabel = new Label(labelString, skin);
            itemLabel.setFontScale(2);
            itemTable.add(itemLabel).width(game.GDX_WIDTH - 100);

            TextButton buyButton = new TextButton("Buy", skin);
            buyButton.getLabel().setFontScale(0.5f);
            buyButton.addListener(new ClickListener()
            {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    if (Currency.subtract(price))
                    {
                        Inventory.addItem(i.itemName, 1);
                    }
                    return true;
                }
            });
            itemTable.add(buyButton).width(100);

            itemTable.row();
        }
    }

    private void setupSellItemList()
    {
        itemTable.clear();
        String[] invItems = Inventory.getItemsOfType(ShopItem.class);
        for (String itemName : invItems)
        {
            final ShopItem item = (ShopItem)Item.lookup(itemName);

            final int itemCount = Inventory.getCount(itemName);
            final int sellValue = item.sellValue;

            String itemInfo = itemName + "\t" + sellValue + "\t" + itemCount;
            Label itemLabel = new Label(itemInfo, skin);
            itemLabel.setFontScale(2);
            itemTable.add(itemLabel).width(game.GDX_WIDTH - 100);

            TextButton sellButton = new TextButton("Sell", skin);
            sellButton.getLabel().setFontScale(0.5f);
            sellButton.addListener(new ClickListener()
            {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    Currency.add(sellValue);
                    Inventory.takeItem(item.itemName);
                    setupSellItemList();
                    return true;
                }
            });

            itemTable.add(sellButton).width(100);
            itemTable.row();
        }
    }
}
