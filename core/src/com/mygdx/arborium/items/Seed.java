package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.arborium.game.Plot;

public class Seed extends ShopItem
{
    public final Tree treeType;

    public Seed(int id, String name, Texture image, int buyValue, int sellValue, boolean locked, Tree treeType)
    {
        super(id, name, image, buyValue, sellValue, locked);
        this.treeType = treeType;
    }

    public void plant(Plot p)
    {
        p.plantSeed(treeType);
    }
}
