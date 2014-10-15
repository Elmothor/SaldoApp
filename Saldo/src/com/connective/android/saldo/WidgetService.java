package com.connective.android.saldo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class WidgetService extends Service {
	Timer mTimer = new Timer();
	final NotifyMannager notify = new NotifyMannager();
	final NotifyMannagerMega notifyMega = new NotifyMannagerMega();
	int can = 0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.w("context", "inicio-notification");
		this.mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				SharedPreferences settings = getSharedPreferences(
						"WidgetPrefs", Context.MODE_PRIVATE);
				String megas = settings.getString("Meganet", "");
				
				if (!megas.equals("")) {
					can = 1;
					String fechaMega = settings.getString("fechaMeganet", "");
					notifyMega.playNotification(getApplicationContext(),
							Configuracion.class,
							"Visualizador de consumo de internet", "Tienes "
									+ megas + " Mb", R.drawable.moneda_icon,
							"vence el " + fechaMega);
				} else {
					can = 2;
					notify.playNotification(getApplicationContext(),
							Configuracion.class,
							"Visualizador de consumo de internet",
							"Consumo ¢0", R.drawable.moneda_icon);
				}

			}
		}, 0, 1000 * 1);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mTimer.cancel();
		
		if (can == 2) {
			notify.cancelar();
		} else {
			notifyMega.cancelar();
		}

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
