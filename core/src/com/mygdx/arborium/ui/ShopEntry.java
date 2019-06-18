package com.mygdx.arborium.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.arborium.Arborium;

public class ShopEntry extends HorizontalGroup
{
    Image shopItemImage;
    Label shopItemLabel;

    public ShopEntry(Texture itemTexture, String itemName, Skin skin)
    {
        super();

        shopItemImage = new Image(itemTexture);
        shopItemLabel = new Label(itemName, skin);
        shopItemLabel.setScale(2);

        space(25);
        pad(25);
        
    }
}