package com.connective.android.saldo;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.RemoteViews;

public class MiWidget extends AppWidgetProvider {
	public static int cont = 0;
	public static int contRefrescar = 0;
	public static String lin = "";
	public static int contRefrescados = 0;
	public static int lay_id_progress = 0;
	public static int lay_id = 0;
	public static String EXTRA_WORD = "com.connective.android.saldo.WORD";

	

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Iteramos la lista de widgets en ejecución
		for (int i = 0; i < appWidgetIds.length; i++) {
			// ID del widget actual
			int widgetId = appWidgetIds[i];
			// Actualizamos el widget actual
			actualizarWidget(context, appWidgetManager, widgetId);
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void actualizarWidget(Context context,
			AppWidgetManager appWidgetManager, int widgetId) {
		SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs",
				Context.MODE_PRIVATE);
		RemoteViews controles;
		/*
		 * final int currentapiVersion = android.os.Build.VERSION.SDK_INT; if
		 * (currentapiVersion < 11) {
		 */
		lay_id = R.layout.miwidget;
		lay_id_progress = R.layout.progresmiwidget;
		controles = new RemoteViews(context.getPackageName(),
				R.layout.miwidget);
		controles.setTextColor(R.id.LblMensaje, context.getResources().getColor(R.color.white));
		String color = prefs.getString("color", "cualquiera");
		Log.w("color", color);
		if (color.equals("Rosado")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.pink);
		} else if (color.equals("Rojo")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.red);
		} else if (color.equals("Verde")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.green);
		} else if (color.equals("Amarillo")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.yellow);
		} else if (color.equals("Azul")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.blue);
		} else if (color.equals("Morado")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.purple);
		} else if (color.equals("Naranja")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.orange);
		} else if (color.equals("Trasparente(Blanco)")) {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.transparent);
	    } else if (color.equals("Trasparente(Negro)")) {
		controles.setTextColor(R.id.LblMensaje, context.getResources().getColor(R.color.Tblack));
		controles.setInt(R.id.fondo, "setBackgroundResource", R.color.transparent);
		} else {
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.black);
		}
		String mensaje = prefs.getString("tiempo", "3");
		int tiempo = Integer.parseInt(mensaje);
		if (cont == 0) {
			SMSSend sss = new SMSSend(context);
			sss.sssss();
			cont += 1;

		}
		// Asociamos los 'eventos' al widget
		Intent intent = new Intent(
				"com.connective.android.saldo.ACTUALIZAR_WIDGET");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent intent2 = new Intent(context, Configuracion.class);

		PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
				widgetId, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		Intent intent3 = new Intent(context, MiWidget.class);
		intent3.setAction("cambia");
		intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context,
				widgetId, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent intent4 = new Intent(context, MiWidget.class);
		intent4.setAction("refresh");
		intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context,
				widgetId, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
		// controles.setOnClickPendingIntent(R.id.BtnActualizar, pendingIntent);

		final AlarmManager m = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		final Calendar TIME = Calendar.getInstance();
		TIME.setTimeInMillis(System.currentTimeMillis());

		// Actualizamos el mensaje en el control del widget
		String bandera2 = prefs.getString("intent", "false");
		if (bandera2.equals("refrescar")) {
			if (contRefrescar == 0) {
				SMSSend.sendSMS("1150", " ");
				contRefrescar = 1;
			}
			contRefrescados += 1;
			controles = new RemoteViews(context.getPackageName(),
					lay_id_progress);
			controles.setTextColor(R.id.LblMensaje, context.getResources().getColor(R.color.white));
			if (color.equals("Rosado")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.pink);
			} else if (color.equals("Rojo")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.red);
			} else if (color.equals("Verde")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.green);
			} else if (color.equals("Amarillo")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.yellow);
			} else if (color.equals("Azul")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.blue);
			} else if (color.equals("Morado")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.purple);
			} else if (color.equals("Naranja")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.orange);
			} else if (color.equals("Trasparente(Blanco)")) {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.transparent);
		    } else if (color.equals("Trasparente(Negro)")) {
			controles.setTextColor(R.id.LblMensaje, context.getResources().getColor(R.color.Tblack));
			controles.setInt(R.id.fondo, "setBackgroundResource", R.color.transparent);
			} else {
				controles.setInt(R.id.fondo, "setBackgroundResource", R.color.black);
			}
			TIME.add(Calendar.SECOND, 1);
			m.set(AlarmManager.RTC, TIME.getTime().getTime(), pendingIntent4);

			if (SMSReceiver.sac != lin || contRefrescados == 20) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("intent", "false"); // Estado apagado
				editor.commit();
				m.cancel(pendingIntent4);
				contRefrescar = 0;
				contRefrescados = 0;
				controles = new RemoteViews(context.getPackageName(), lay_id);

				m.set(AlarmManager.RTC, TIME.getTime().getTime(), pendingIntent);
				Log.w("dato", tiempo + "son iguales");
			}
		}
		if (bandera2.equals("false")) {
			if (SMSReceiver.sac.equals("Tu saldo es 0.00 colones")) {
				TIME.add(Calendar.SECOND, 1);
			} else {
				TIME.add(Calendar.MINUTE, tiempo);

			}
			m.set(AlarmManager.RTC, TIME.getTime().getTime(), pendingIntent);
		}
		String bandera = prefs.getString("bandera", "false");
		if (bandera.equals("true")) {
			lin = SMSReceiver.totales;
			if (lin.equals("Tienes 0 min y 0 seg en llamadas o 0 mensajes")) {
				String valores = prefs.getString("valores",
						"Tienes 0 min y 0 seg en llamadas o 0 mensajes");
				lin = valores;
			}

		} else {
			lin = SMSReceiver.sac;
			if (lin.equals("Tu saldo es 0.00 colones")) {
				String sms = prefs.getString("sms", "Tu saldo es 0.00 colones");
				lin = sms;
			}
		}

		controles.setOnClickPendingIntent(R.id.fondo, pendingIntent3);
		controles.setOnClickPendingIntent(R.id.settings, pendingIntent2);
		controles.setOnClickPendingIntent(R.id.refresh, pendingIntent4);
		controles.setTextViewText(R.id.LblMensaje, lin);

		// Notificamos al manager de la actualización del widget actual
		appWidgetManager.updateAppWidget(widgetId, controles);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs",
				Context.MODE_PRIVATE);

		Log.w("dato", intent.getAction());
		if (intent.getAction().equals("refresh")) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("intent", "refrescar"); // Estado apagado
			editor.commit();
		} else {
			if (intent.getAction().equals("cambia")) {

				String bandera = prefs.getString("bandera", "error");

				if (bandera.equals("false")) {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("bandera", "true"); // Estado apagado
					editor.commit();
				} else {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("bandera", "false"); // Estado apagado
					editor.commit();
				}
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("intent", "true"); // Estado apagado
				editor.commit();
			} else {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("intent", "false"); // Estado apagado
				editor.commit();
			}
		}
		// Obtenemos el ID del widget a actualizar
		int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);

		// Obtenemos el widget manager de nuestro contexto
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

		// Actualizamos el widget
		if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
			actualizarWidget(context, widgetManager, widgetId);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		// Toast.makeText(context, "onDeleted", Toast.LENGTH_LONG).show();
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		// Toast.makeText(context, "onDisable", Toast.LENGTH_LONG).show();

		super.onDisabled(context);
	}

	@SuppressLint("UnlocalizedSms")
	@Override
	public void onEnabled(Context context) {
		// Toast.makeText(context, "onEnable", Toast.LENGTH_LONG).show();
		super.onEnabled(context);
		int[] allids = AppWidgetManager.getInstance(context).getAppWidgetIds(
				new ComponentName(context, MiWidget.class));
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		this.onUpdate(context, appWidgetManager, allids);
	}
}
