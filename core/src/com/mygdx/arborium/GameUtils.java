package com.mygdx.arborium;

import com.badlogic.gdx.Screen;

import java.util.Timer;
import java.util.TimerTask;

public class GameUtils
{
    private static Timer timer = new Timer();

    public static void delaySetScreen(final Arborium game, float seconds, final Screen screenTo)
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                game.setScreen(screenTo);
            }
        }, (long) (seconds * 1000));
    }
}
