package com.connective.android.saldo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetChanges extends BroadcastReceiver {

	private boolean haveConnectedMobile = false;


	@Override
	public void onReceive(final Context context, final Intent intent) {
		SharedPreferences settings = context.getSharedPreferences(
				"WidgetPrefs", Context.MODE_PRIVATE);
		haveConnectedMobile = false;
		Intent intento = new Intent(context,WidgetService.class);
		String entrar = settings.getString("interChequeado", "false");

		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] current = connec.getAllNetworkInfo();
		for (NetworkInfo ni : current) {
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		
		if (haveConnectedMobile) {
			
			if (entrar.equals("true")) {
				context.startService(intento);
				Log.w("context", "inicio");
			}
		} else {
			context.stopService(intento);
			Log.w("context", "finalizo");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub

		Log.w("finalize", "El broadcast Receiver de internet finalizo");
	}
	
}
