package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.game.Transaction;
import com.mygdx.arborium.items.Fruit;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Sapling;
import com.mygdx.arborium.items.ShopItem;
import com.mygdx.arborium.ui.PriceLabel;

public class ShopScreen implements Screen
{
    Transaction transaction;

    String[] itemList;
    int itemSelectIndex = 0;

    final Arborium game;

    OrthographicCamera camera;

    Stage stage;
    Skin skin;

    Table shopTable;

    Button leftButton;
    Button rightButton;

    // Shop window UI Elements
    Window shopWindow;
    PriceLabel currencyLabel;
    TextButton buyButton;
    TextButton sellButton;
    PriceLabel buyPriceLabel;
    PriceLabel sellPriceLabel;
    SelectBox<Category> itemSelectBox;

    ShopItem selectItem;

    // Transaction window UI elements
    Container<Window> transactionWindowContainer;
    Window transactionWindow;
    Label quantityLabel;
    Button decrementButton;
    Button incrementButton;
    PriceLabel totalPriceLabel;
    TextButton confirmButton;
    TextButton transactionBackButton;
    TextButton sellAllButton;
    TextButton[] quantityButtons;

    Image itemImage;
    Label itemDescription;

    TextButton backButton;

    enum Category
    {
        Saplings, Fruit, Upgrades
    }

    public ShopScreen(final Arborium game)
    {
        this.game = game;

        initialize();
        addUIListeners();

        updateShopWindow();

        buildUI();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
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

    private void initialize()
    {
        Texture coin = game.getTexture(Arborium.COIN);

        camera = new OrthographicCamera();
        //camera.setToOrtho();

        stage = new Stage(new ExtendViewport(800, 480));
        skin = game.getSkin(Arborium.ARBOR_SKIN);

        shopTable = new Table();
        shopTable.setFillParent(true);

        currencyLabel = new PriceLabel(coin, skin);
        currencyLabel.setText("" + Currency.getAmount());

        leftButton = new Button(skin, "left");
        rightButton = new Button(skin, "right");

        shopWindow = new Window("Buy", skin);
        //shopBuyWindow.setDebug(true);

        buyButton = new TextButton("Buy", skin);
        sellButton = new TextButton("Sell", skin);

        itemSelectBox = new SelectBox<Category>(skin);
        itemSelectBox.setItems(Category.values());

        itemList = new String[0];
        updateItemList();

        itemImage = new Image(new TextureRegionDrawable(game.getTexture(Arborium.COIN)));
        itemDescription = new Label("Test description! :)", skin);
        itemDescription.setWrap(true);

        backButton = new TextButton("Back", skin);

        buyPriceLabel = new PriceLabel(coin, skin);
        sellPriceLabel = new PriceLabel(coin, skin);

        transactionWindowContainer = new Container<Window>();
        transactionWindowContainer.setFillParent(true);

        transactionWindow = new Window("", skin);

        quantityLabel = new Label("", skin);
        decrementButton = new Button(skin, "left");
        incrementButton = new Button(skin, "right");
        confirmButton = new TextButton("OK", skin);
        transactionBackButton = new TextButton("Back", skin);

        totalPriceLabel = new PriceLabel(coin, skin);   

        selectItem = (ShopItem) Item.lookup(itemList[itemSelectIndex]);
        transaction = new Transaction(selectItem, true);
    }

    private void addUIListeners()
    {
        itemSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                itemSelectIndex = 0;
                updateShopWindow();
            }
        });

        leftButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                itemSelectIndex--;
                updateShopWindow();
            }
        });

        rightButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                itemSelectIndex++;
                updateShopWindow();
            }
        });


        buyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //buying = true;
                transaction.setPurchase(true);
                updateTransactionWindow();
                showTransactionWindow();
            }
        });

        sellButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //buying = false;
                transaction.setPurchase(false);
                updateTransactionWindow();
                showTransactionWindow();
            }
        });

        confirmButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                transaction.apply();

                currencyLabel.setText("" + Currency.getAmount());
                itemSelectIndex = 0;

                hideTransactionWindow();
            }
        });

        decrementButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                transaction.changeQuantity(-1);
                updateTransactionWindow();
            }
        });

        incrementButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                transaction.changeQuantity(1);
                updateTransactionWindow();
            }
        });

        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(game.farmScreen);
            }
        });

        transactionBackButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                hideTransactionWindow();
                shopWindow.setTouchable(Touchable.enabled);
            }
        });
    }

    private void buildUI()
    {
        transactionWindow.row();
        transactionWindow.add(decrementButton).pad(25);
        transactionWindow.add(quantityLabel);
        transactionWindow.add(incrementButton).pad(25);
        transactionWindow.row();
        transactionWindow.add(totalPriceLabel).colspan(2).left();
        transactionWindow.row();
        transactionWindow.add(confirmButton);
        transactionWindow.add(transactionBackButton);
        transactionWindow.pack();

        transactionWindowContainer.setActor(transactionWindow);

        shopWindow.add(currencyLabel).expandX().colspan(2).space(25);
        shopWindow.row();
        shopWindow.add(itemSelectBox).top().expandX().colspan(2).space(25);
        shopWindow.row();
        shopWindow.add(itemImage).expandX().colspan(2);
        shopWindow.row();
        shopWindow.add(buyPriceLabel).space(25);
        shopWindow.add(buyButton).space(25).fill();
        shopWindow.row();
        shopWindow.add(sellPriceLabel).space(25);
        shopWindow.add(sellButton).fill();
        shopWindow.pack();

        shopTable.add(leftButton);
        shopTable.add(shopWindow).expand().minWidth(500).minHeight(500);
        shopTable.add(rightButton);

        shopTable.row();
        shopTable.add(backButton);

        stage.addActor(shopTable);
        stage.addActor(transactionWindowContainer);
        transactionWindowContainer.setVisible(false);
    }

    private void updateShopWindow()
    {
        updateItemList();

        if (itemList.length > 0)
        {
            leftButton.setVisible(itemSelectIndex > 0);
            rightButton.setVisible(itemSelectIndex < itemList.length - 1);

            selectItem = (ShopItem) Item.lookup(itemList[itemSelectIndex]);
            itemImage.setDrawable(new TextureRegionDrawable(selectItem.sprite));
            itemDescription.setText(selectItem.getDescription());

            int buyPrice = selectItem.getBuyValue();
            int sellPrice = selectItem.getSellValue();

            if (buyPrice > 0)
            {
                buyPriceLabel.setVisible(true);
                buyPriceLabel.setText("" + buyPrice);
                buyButton.setVisible(true);
            }
            else
            {
                buyPriceLabel.setVisible(false);
                buyButton.setVisible(false);
            }

            if (sellPrice > 0)
            {
                sellPriceLabel.setVisible(true);
                sellPriceLabel.setText("" + sellPrice);
                sellButton.setVisible(true);
            }
            else
            {
                sellPriceLabel.setVisible(false);
                sellButton.setVisible(false);
            }
        }
        else
        {
            itemImage.setDrawable(null);
            itemDescription.setText("Hey, you don't have any of these items yet!");
            buyPriceLabel.setVisible(false);
            sellPriceLabel.setVisible(false);
        }
        shopWindow.invalidate();
    }

    private void updateTransactionWindow()
    {
        transaction.setTransactionItem(selectItem);
        quantityLabel.setText(transaction.getQuantity());
        totalPriceLabel.setText("" + transaction.getTotalPrice());
    }

    private void showTransactionWindow()
    {
        //stage.addActor(transactionWindowContainer);
        transactionWindowContainer.setVisible(true);
        shopWindow.setTouchable(Touchable.disabled);
    }

    private void hideTransactionWindow()
    {
        //transactionWindowContainer.remove();
        transactionWindowContainer.setVisible(false);
        shopWindow.setTouchable(Touchable.enabled);
        transaction.reset();
        updateShopWindow();
    }

    private void updateItemList()
    {
        switch(itemSelectBox.getSelected())
        {
            case Upgrades:
            case Saplings:
            {
                itemList = Item.getItemsOfType(Sapling.class);
                break;
            }

            case Fruit:
            {
                itemList = Inventory.getItemsOfType(Fruit.class);
                break;
            }
        }
    }
}
