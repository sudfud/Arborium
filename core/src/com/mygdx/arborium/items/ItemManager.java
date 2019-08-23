package com.mygdx.arborium.items;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.arborium.Arborium;

import java.util.ArrayList;
import java.util.List;

public class ItemManager
{
    public Texture[] itemTextures;

    public ArrayList<Sapling> saplings = new ArrayList<Sapling>();

    public ItemManager(Arborium game)
    {
        itemTextures = new Texture[128];

        Texture[] saplingSprites =
                {
                        game.getTexture(Arborium.APPLE_TREE2X),
                        game.getTexture(Arborium.ORANGE_TREE2X),
                        game.getTexture(Arborium.CHERRY_TREE)
                };

        Sapling appleSapling = new Sapling()
    }
}
