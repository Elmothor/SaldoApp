package com.connective.android.saldo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SMSSend {
	private final SharedPreferences sharedPrefs;
	Context contest;

	public SMSSend(Context context) {
		sharedPrefs = context.getSharedPreferences("WidgetPrefs",
				Context.MODE_PRIVATE);
		contest = context;
		Intent intent = new Intent();
		intent.setAction("com.connective.android.saldo.SMSReceiver");
		context.sendBroadcast(intent);

	}

	public void sssss() {
		final Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {

					try {
						TelephonyManager telephonyManager = (TelephonyManager) contest
								.getSystemService(Context.TELEPHONY_SERVICE);
						String operador = telephonyManager
								.getNetworkOperatorName();
						if (operador.equals("Kolbi ICE")
								|| operador.equals("ICE")
								|| operador.equals("I.C.E.")
								|| operador.equals("K lbi ICE")
								|| operador
										.equals("Instituto Costarricense de Electricidad")
								|| operador.equals("ICE Celular")
								|| operador.equals("K?lbi ICE")
								|| operador.equals("I.C.E.,I.C.E.")
								|| operador.equals("null,I.C.E.")
								|| operador.equals("Kolbi ICE,null")) {
							sendSMS("1150", " ");
						} else if (operador.equals("71204")
								|| operador.equals("movistar")
								|| operador.equals("Movistar")
								|| operador.equals("CRI TC")) {
							sendSMS("651", " ");
						}
						Log.w("dada", operador);

						// ejecutarTarea();
						Thread.sleep(tiempo());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();

	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub

	}

	/*
	 * @SuppressWarnings("null") public static void ejecutarTarea() {
	 * 
	 * String tiempo; Date ahora = new Date(); SimpleDateFormat formateador =
	 * new SimpleDateFormat("hh:mm:ss");
	 * 
	 * tiempo = formateador.format(ahora); SharedPreferences settings =
	 * getSharedPreferences("WidgetPrefs", MODE_PRIVATE);
	 * SharedPreferences.Editor editor = settings.edit();
	 * editor.putString("tiempo", tiempo); editor.commit();
	 * 
	 * }
	 */

	public static void sendSMS(String phoneNumbre, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumbre, null, message, null, null);

	}

	private int tiempo() {

		String mensaje = sharedPrefs.getString("tiempo", "180000");
		int tiemp = Integer.parseInt(mensaje);
		switch (tiemp) {
		case 1:
			tiemp = 60000;
			break;
		case 3:
			tiemp = 180000;
			break;
		case 5:
			tiemp = 300000;
			break;
		case 10:
			tiemp = 600000;
			break;
		case 15:
			tiemp = 900000;
			break;
		default:
			break;
		}
		Log.w("sms", "" + tiemp);
		return tiemp;
	}

}
