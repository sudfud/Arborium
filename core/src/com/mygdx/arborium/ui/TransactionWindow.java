package com.mygdx.arborium.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class TransactionWindow extends Window
{
    Container<Window> windowContainer;

    Label quantityLabel;
    Button decrementButton;
    Button incrementButton;
    PriceLabel totalPriceLabel;
    TextButton confirmButton;
    TextButton transactionBackButton;
    TextButton sellAllButton;

    public TransactionWindow(String title, Skin skin, Container<Window> container)
    {
        super(title, skin);
        windowContainer = container;
        container.setActor(this);
    }

    public void show()
    {
        windowContainer.setVisible(true);
    }

    public void hide()
    {
        windowContainer.setVisible(false);
    }
}