package com.mygdx.arborium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.arborium.Arborium;
import com.mygdx.arborium.game.Currency;
import com.mygdx.arborium.game.Inventory;
import com.mygdx.arborium.items.Fruit;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Sapling;
import com.mygdx.arborium.items.ShopItem;
import com.mygdx.arborium.ui.PriceLabel;

public class ShopScreen implements Screen
{
    int quantity = 1;
    int totalPrice = 0;

    boolean buying = true;

    final Arborium game;

    Stage stage;
    Skin skin;

    Table shopTable;

    Button leftButton;
    Button rightButton;

    Window shopBuyWindow;
    TextButton buyButton;
    TextButton sellButton;
    PriceLabel buyPriceLabel;
    PriceLabel sellPriceLabel;
    SelectBox<Category> itemSelectBox;
    List<String> itemList;

    TextButton backButton;

    ShopItem selectItem;

    Window shopSellWindow;

    Container<Window> transactionWindowContainer;
    Window transactionWindow;
    Label quantityLabel;
    Button decrementButton;
    Button incrementButton;

    PriceLabel totalPriceLabel;

    TextButton confirmButton;
    TextButton transactionBackButton;
    TextButton sellAllButton;

    Image itemImage;
    Label itemDescription;



    enum Category
    {
        Saplings, Fruit, Upgrades
    }

    public ShopScreen(final Arborium game)
    {
        this.game = game;

        initialize();
        addUIListeners();

        // Setup item list
        String[] saplings = Item.getItemsOfType(Sapling.class);
        itemList.setItems(saplings);

        itemList.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                updateShopWindow();
                if (!itemList.getItems().isEmpty())
                {
                    ShopItem item = (ShopItem) Item.lookup(itemList.getSelected());
                    buyButton.setVisible(item.buyValue > 0);
                    sellButton.setVisible(item.sellValue > 0);
                }
                else
                {
                    buyButton.setVisible(false);
                    sellButton.setVisible(false);
                }
            }
        });

        itemList.setSelectedIndex(0);
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
        stage = new Stage(new ScreenViewport());
        skin = game.getSkin(Arborium.ARBOR_SKIN);

        shopTable = new Table();
        shopTable.setFillParent(true);

        leftButton = new Button(skin, "left");
        rightButton = new Button(skin, "right");

        shopBuyWindow = new Window("Buy", skin);

        shopSellWindow = new Window("Sell", skin);
        buyButton = new TextButton("Buy", skin);
        sellButton = new TextButton("Sell", skin);

        itemSelectBox = new SelectBox<Category>(skin);
        itemSelectBox.setItems(Category.values());

        itemList = new List<String>(skin);

        itemImage = new Image(new TextureRegionDrawable(game.getTexture(Arborium.COIN)));
        itemDescription = new Label("Test description! :)", skin);
        itemDescription.setWrap(true);

        backButton = new TextButton("Back", skin);

        buyPriceLabel = new PriceLabel(game);
        sellPriceLabel = new PriceLabel(game);

        transactionWindowContainer = new Container<Window>();
        transactionWindowContainer.setFillParent(true);

        transactionWindow = new Window("", skin);

        quantityLabel = new Label("", skin);
        decrementButton = new Button(skin, "left");
        incrementButton = new Button(skin, "right");
        confirmButton = new TextButton("OK", skin);
        transactionBackButton = new TextButton("Back", skin);

        totalPriceLabel = new PriceLabel(game);
    }

    private void addUIListeners()
    {
        itemSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                updateItemList();
            }
        });

        leftButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buying = !buying;

                if (buying)
                {
                    shopBuyWindow.getTitleLabel().setText("Sell");

                }
            }
        });

        buyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buying = true;
                updateTransactionWindow();
                showTransactionWindow();
            }
        });

        sellButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buying = false;
                updateTransactionWindow();
                showTransactionWindow();
            }
        });

        confirmButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                int currency = Currency.getAmount();
                if (buying && currency >= totalPrice)
                {
                    Currency.subtract(totalPrice);
                    Inventory.addItem(selectItem.itemName, quantity);
                    hideTransactionWindow();
                }
                else if (!buying)
                {
                    Currency.add(totalPrice);
                    Inventory.takeItem(selectItem.itemName, quantity);
                    hideTransactionWindow();
                }
            }
        });

        decrementButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (quantity > 1)
                    quantity--;
                updateTransactionWindow();
            }
        });

        incrementButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (!buying)
                {
                    int itemCount = Inventory.getCount(selectItem.itemName);
                    if (quantity < itemCount)
                        quantity++;
                }
                else if ((quantity + 1) * selectItem.buyValue <= Currency.getAmount())
                {
                    quantity++;
                }
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
                transactionWindowContainer.remove();
                shopBuyWindow.setTouchable(Touchable.enabled);
            }
        });
    }

    private void buildUI()
    {
        shopBuyWindow.add(itemSelectBox).space(10).width(300).colspan(2).left();
        shopBuyWindow.row();
        shopBuyWindow.add(itemList).space(10).width(300).colspan(2).left();
        shopBuyWindow.row();
        shopBuyWindow.add(itemImage);
        shopBuyWindow.add(itemDescription).width(500);
        shopBuyWindow.row();
        shopBuyWindow.add(buyButton);
        shopBuyWindow.add(sellButton);
        shopBuyWindow.row();
        shopBuyWindow.add(buyPriceLabel);
        shopBuyWindow.add(sellPriceLabel);
        shopBuyWindow.pack();

        shopTable.add(shopBuyWindow).maxWidth(750).minHeight(500);
        shopTable.row();
        shopTable.add(backButton);

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

        stage.addActor(shopTable);
    }

    private void updateShopWindow()
    {
        updateItemList();

        if (!itemList.getItems().isEmpty())
        {
            selectItem = (ShopItem) Item.lookup(itemList.getSelected());
            itemImage.setDrawable(new TextureRegionDrawable(selectItem.itemImage));
            itemDescription.setText(selectItem.description);

            int buyPrice = selectItem.buyValue;
            int sellPrice = selectItem.sellValue;

            if (buyPrice > 0)
            {
                buyPriceLabel.setVisible(true);
                buyPriceLabel.setText("" + buyPrice);
            }
            else
                buyPriceLabel.setVisible(false);

            if (sellPrice > 0)
            {
                sellPriceLabel.setVisible(true);
                sellPriceLabel.setText("" + sellPrice);
            }
            else
                sellPriceLabel.setVisible(false);
        }
        else
        {
            itemImage.clear();
            itemDescription.setText("Hey, you don't have any of these items yet!");
            buyPriceLabel.setVisible(false);
            sellPriceLabel.setVisible(false);
        }
        shopBuyWindow.invalidate();
    }

    private void updateTransactionWindow()
    {
        quantityLabel.setText(quantity);

        int price = buying? selectItem.buyValue : selectItem.sellValue;

        totalPrice = price * quantity;
        totalPriceLabel.setText("" + totalPrice);
    }

    private void showTransactionWindow()
    {
        stage.addActor(transactionWindowContainer);
        shopBuyWindow.setTouchable(Touchable.disabled);
    }

    private void hideTransactionWindow()
    {
        transactionWindowContainer.remove();
        shopBuyWindow.setTouchable(Touchable.enabled);
        quantity = 1;
        updateShopWindow();
    }

    private void updateItemList()
    {
        switch(itemSelectBox.getSelected())
        {
            case Upgrades:
            case Saplings:
            {
                String[] saplings = Item.getItemsOfType(Sapling.class);
                itemList.setItems(saplings);
                break;
            }

            case Fruit:
            {
                String[] fruits = Inventory.getItemsOfType(Fruit.class);
                itemList.setItems(fruits);
                break;
            }
        }
    }
}
