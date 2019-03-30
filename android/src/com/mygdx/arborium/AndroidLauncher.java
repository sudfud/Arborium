package com.mygdx.arborium;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.arborium.game.Farm;
import com.mygdx.arborium.game.Plot;

public class AndroidLauncher extends AndroidApplication
{
	static Arborium game;

	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		AndroidAdapter adapter = new AndroidAdapter(this);

		game = new Arborium();
		game.setNotificationHandler(adapter);

		initialize(game, config);
	}

	@Override
	protected void onPause()
	{
		if (game.isNotificationEnabled())
		{
			Intent intent = new Intent(this, TimerService.class);

			for (int i = 0; i < game.mediumFarms.length; i++)
			{
				Farm farm = game.mediumFarms[i];
				if (!farm.isLocked())
				{
					String farmTag = farm.name;

					for (int j = 0; j < game.mediumFarms[i].getPlotSize(); j++)
					{
						Plot plot = farm.getPlot(j);

						String plotTag = "Plot" + j;

						String matureTag = farmTag + plotTag + "Mature";
						String lastHarvestTag = farmTag + plotTag + "LastHarvest";
						String produceRateTag = farmTag + plotTag + "ProduceRate";

						boolean mature = plot.isMature();
						intent.putExtra(matureTag, mature);

						if (mature) {
							long lastHarvest = plot.getLastHarvestTime();
							long produceRate = plot.getProduceRate();
							intent.putExtra(lastHarvestTag, lastHarvest);
							intent.putExtra(produceRateTag, produceRate);
						}
					}
				}
			}

			startService(intent);
		}

		super.onPause();
	}

	@Override
	protected void onResume()
	{
		Intent intent = new Intent(this, TimerService.class);
		stopService(intent);

		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
