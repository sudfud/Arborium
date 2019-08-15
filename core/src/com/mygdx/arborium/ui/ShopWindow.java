package com.mygdx.arborium.ui;

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
import com.mygdx.arborium.items.Fruit;
import com.mygdx.arborium.items.Item;
import com.mygdx.arborium.items.Sapling;
import com.mygdx.arborium.items.ShopItem;
import com.mygdx.arborium.ui.PriceLabel;

public class ShopWindow extends Window
{
    int quantity = 1;
    int totalPrice = 0;

    boolean buying = true;

    String[] itemList;
    int itemSelectIndex = 0;

    OrthographicCamera camera;

    Stage stage;
    Skin skin;

    Table shopTable;

    Button leftButton;
    Button rightButton;

    PriceLabel currencyLabel;

    Window shopBuyWindow;
    TextButton buyButton;
    TextButton sellButton;
    PriceLabel buyPriceLabel;
    PriceLabel sellPriceLabel;
    SelectBox<Category> itemSelectBox;

    TextButton backButton;

    ShopItem selectItem;

    HorizontalGroup imageGroup;
    Image itemImage;
    Label itemDescription;

    enum Category
    {
        Saplings, Fruit, Upgrades
    }

    public ShopWindow(String title, Skin skin, Arborium game)
    {
        super(title, skin);

        Texture coin = game.getTexture(Arborium.COIN);

        shopTable = new Table();
        shopTable.setFillParent(true);

        currencyLabel = new PriceLabel(coin, skin);
        currencyLabel.setText("" + Currency.getAmount());

        leftButton = new Button(skin, "left");
        rightButton = new Button(skin, "right");

        shopBuyWindow = new Window("Buy", skin);
        //shopBuyWindow.setDebug(true);

        buyButton = new TextButton("Buy", skin);
        sellButton = new TextButton("Sell", skin);

        itemSelectBox = new SelectBox<Category>(skin);
        itemSelectBox.setItems(Category.values());

        itemList = new String[0];

        itemImage = new Image(new TextureRegionDrawable(coin));
        imageGroup = new HorizontalGroup();
        itemDescription = new Label("Test description! :)", skin);
        itemDescription.setWrap(true);

        backButton = new TextButton("Back", skin);

        buyPriceLabel = new PriceLabel(coin, skin);
        sellPriceLabel = new PriceLabel(coin, skin);

        build();
    }

    private void build()
    {
        shopBuyWindow.add(currencyLabel).expandX().colspan(2).space(25);
        shopBuyWindow.row();
        shopBuyWindow.add(itemSelectBox).top().expandX().colspan(2).space(25);
        shopBuyWindow.row();
        //itemImage.setScale(2);
        //shopBuyWindow.add(imageGroup).center().colspan(2);
        shopBuyWindow.add(itemImage).expandX().colspan(2);
        shopBuyWindow.row();
        shopBuyWindow.add(buyPriceLabel).space(25);
        shopBuyWindow.add(buyButton).space(25).fill();
        shopBuyWindow.row();
        shopBuyWindow.add(sellPriceLabel).space(25);
        shopBuyWindow.add(sellButton).fill();
        shopBuyWindow.pack();

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
                buying = true;
                //updateTransactionWindow();
                //showTransactionWindow();
            }
        });
    }

    private void updateShopWindow()
    {
        updateItemList();

        if (itemList.length > 0)
        {
            leftButton.setVisible(itemSelectIndex > 0);
            rightButton.setVisible(itemSelectIndex < itemList.length - 1);

            selectItem = (ShopItem) Item.lookup(itemList[itemSelectIndex]);
            itemImage.setDrawable(new TextureRegionDrawable(selectItem.itemImage));
            itemDescription.setText(selectItem.description);

            int buyPrice = selectItem.buyValue;
            int sellPrice = selectItem.sellValue;

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
        shopBuyWindow.invalidate();
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