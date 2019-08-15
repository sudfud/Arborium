package com.mygdx.arborium.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.arborium.Arborium;

public class PriceLabel extends HorizontalGroup
{
    Image coin;
    Label price;

    public PriceLabel(Texture coinTexture, Skin skin)
    {
        super();
        coin = new Image(new TextureRegionDrawable(coinTexture));
        price = new Label("", skin);

        this.addActor(coin);
        this.addActor(price);
    }

    public void setText(String text)
    {
        price.setText(text);
    }
}
