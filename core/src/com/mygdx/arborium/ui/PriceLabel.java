package com.mygdx.arborium.ui;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.arborium.Arborium;

public class PriceLabel extends HorizontalGroup
{
    Image coin;
    Label price;

    public PriceLabel(Arborium game)
    {
        super();
        coin = new Image(new TextureRegionDrawable(game.getTexture(Arborium.COIN)));
        price = new Label("", game.getSkin(Arborium.ARBOR_SKIN));

        this.addActor(coin);
        this.addActor(price);
    }

    public void setText(String text)
    {
        price.setText(text);
    }
}
