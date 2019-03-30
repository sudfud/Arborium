package com.mygdx.arborium;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Plot;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service
{
    private AndroidAdapter adapter;
    private Timer timer;
    private Arborium game;

    public TimerService()
    {
        adapter = new AndroidAdapter(this);
        timer = new Timer();
        game = AndroidLauncher.game;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onDestroy()
    {
        timer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId)
    {
        Log.i("Service", "service started");
        final Intent finalIntent = intent;
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                checkHarvest(finalIntent);
            }
        }, 1000 * 60 * 5, 1000 * 60 * 5);

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private void checkHarvest(Intent intent)
    {
        Log.i("Service", "Checking for harvest...");
        int harvestReadyCount = 0;
        for (int i = 0; i < game.mediumFarms.length; i++)
        {
            Farm farm = game.mediumFarms[i];
            if (!farm.isLocked())
            {
                String farmTag = farm.name;
                for (int j = 0; j < farm.getPlotSize(); i++)
                {
                    Plot plot = farm.getPlot(j);

                    String plotTag = "Plot" + j;
                    //boolean mature = intent.getBooleanExtra(farmTag + plotTag + "Mature", false);
                    boolean mature = plot.isMature();

                    if (mature)
                    {
                        //long lastHarvest = intent.getLongExtra(farmTag + plotTag + "LastHarvest", -1);
                        //long produceRate = intent.getLongExtra(farmTag + plotTag + "ProduceRate", -1);

                        long lastHarvest = plot.getLastHarvestTime();
                        long produceRate = plot.getProduceRate();

                        long timeSinceHarvest = TimeUtils.timeSinceMillis(lastHarvest);
                        if (timeSinceHarvest >= produceRate)
                        {
                            harvestReadyCount++;
                        }
                    }
                }
            }
        }

        if (harvestReadyCount > 0)
        {
            String notificationDesc;

            if (harvestReadyCount == 1)
            {
                notificationDesc = "One of your trees is ready to harvest!";
            }
            else
            {
                notificationDesc = "You have trees ready to harvest!";
            }

            adapter.showNotification("Arborium", notificationDesc);
        }
    }
}
