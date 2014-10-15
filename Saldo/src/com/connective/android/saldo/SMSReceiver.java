package com.connective.android.saldo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private String str = "";
	private String name = "";
	public static String sac = "Tu saldo es 0.00 colones";
	private String minutos = "";
	private String mensajes = "";
	public static String totales = "Tienes 0 min y 0 seg en llamadas o 0 mensajes";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;

		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];

			for (int i = 0; i < msgs.length; i++) {

				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				name = msgs[i].getOriginatingAddress().toString();
				str += msgs[i].getMessageBody().toString();
				str += "\n";

			}
			SharedPreferences prefs = context.getSharedPreferences(
					"WidgetPrefs", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			String fechaString = prefs.getString("bonoSmsFecha",
					"nada-nada-nada");
			String[] fech = fechaString.split("-");
			if (!fech[0].equals("nada")) {
				Calendar cal1 = new GregorianCalendar();
				Calendar cal2 = Calendar.getInstance();
				cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1, cal1.get(Calendar.DAY_OF_MONTH));
				cal2.set(Integer.parseInt(fech[2]), Integer.parseInt(fech[1]), Integer.parseInt(fech[0]));
				 long milis1 = cal1.getTimeInMillis();
				 long milis2 = cal2.getTimeInMillis();
				 long diff = milis2 - milis1;
				 long diffDays = diff / (24 * 60 * 60 * 1000);
				 if(diffDays < 1){
					 prefs.edit().remove("bonoSms").commit();
					 prefs.edit().remove("bonoSmsFecha").commit();
					 prefs.edit().remove("Meganet").commit();
					 prefs.edit().remove("fechaMeganet").commit();
					 prefs.edit().remove("bonollamadas").commit();
				 }
				
			}
			String fechaStringConecta = prefs.getString("fechaMeganet",
					"nada-nada-nada");
			String[] fechConecta = fechaStringConecta.split("-");
			if (!fechConecta[0].equals("nada")) {
				Calendar cal1 = new GregorianCalendar();
				Calendar cal2 = Calendar.getInstance();
				cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1, cal1.get(Calendar.DAY_OF_MONTH));
				cal2.set(Integer.parseInt(fechConecta[2]), Integer.parseInt(fechConecta[1]), Integer.parseInt(fechConecta[0]));
				 long milis1 = cal1.getTimeInMillis();
				 long milis2 = cal2.getTimeInMillis();
				 long diff = milis2 - milis1;
				 long diffDays = diff / (24 * 60 * 60 * 1000);
				 if(diffDays < 1){
					 prefs.edit().remove("bonoSms").commit();
					 prefs.edit().remove("bonoSmsFecha").commit();
					 prefs.edit().remove("Meganet").commit();
					 prefs.edit().remove("fechaMeganet").commit();
					 prefs.edit().remove("bonollamadas").commit();
				 }
				
			}
			if (name.equals("1150")) {
				if (str.indexOf("El saldo de tu bono de SMS es de") != -1) {
					this.abortBroadcast();
					String bonoSms = "";
					String bonoSmsFecha = "";
					for (int i = 33; i < str.length(); i++) {
						if (!String.valueOf(str.charAt(i)).equals(" ")) {
							bonoSms = bonoSms + str.charAt(i);
						} else {
							i = str.length();
						}
					}

					for (int i = str.indexOf("vence el ") + 9; i < str.length(); i++) {
						if (!String.valueOf(str.charAt(i)).equals(",")) {
							bonoSmsFecha = bonoSmsFecha + str.charAt(i);
						} else {
							i = str.length();
						}
					}
					editor.putString("bonoSms", bonoSms);
					editor.putString("bonoSmsFecha", bonoSmsFecha);
					editor.commit();
				}
			}

			if (name.equals("1150")) {
				if (str.indexOf("El saldo de tu bono de internet es de") != -1) {
					this.abortBroadcast();
					String bonoSms = "";
					String bonoSmsFecha = "";
					for (int i = str
							.indexOf("El saldo de tu bono de internet es de ") + 38; i < str
							.length(); i++) {
						if (!String.valueOf(str.charAt(i)).equals(" ")) {
							bonoSms = bonoSms + str.charAt(i);
						} else {
							i = str.length();
						}
					}

					for (int i = str.indexOf("vence el ") + 9; i < str.length(); i++) {
						if (!String.valueOf(str.charAt(i)).equals(",")) {
							bonoSmsFecha = bonoSmsFecha + str.charAt(i);
						} else {
							i = str.length();
						}
					}
					editor.putString("Meganet", bonoSms);
					editor.putString("fechaMeganet", bonoSmsFecha);
					editor.commit();
				}
			}
			if (name.equals("1150")) {
				if (str.indexOf("El saldo de tu bono llamadas es de") != -1) {
					this.abortBroadcast();
					String bonoSms = "";
					for (int i = str
							.indexOf("El saldo de tu bono llamadas es de ") + 35; i < str
							.length(); i++) {
						if (!String.valueOf(str.charAt(i)).equals(" ")) {
							bonoSms = bonoSms + str.charAt(i);
						} else {
							i = str.length();
						}
					}
					editor.putString("bonollamadas", bonoSms);
					editor.commit();
				}
			}
			if (name.equals("1150")) {
				if (str.indexOf("El saldo de tu Internet Movil es de ") != -1) {
					this.abortBroadcast();

					editor.putString("Meganet",
							str.substring(36, str.length() - 43));
					editor.putString("fechaMeganet",
							str.substring(55, str.length() - 17));
					editor.commit();
				}
			}

			if (name.equals("1150")) {
				if (str.indexOf("Tu recarga de") == -1
						|| str.indexOf("Buenas noticias con kolbi ahora tenes") == -1) {
					this.abortBroadcast();
				}
				if (str.indexOf("Tu saldo") != -1) {
					this.abortBroadcast();

					editor.putString("fechaBono", "");
					editor.putString("fechaBonoSaldo", "");
					editor.putString("saldoOriginal", "");
					editor.putString("FechaMovistar", "");
					editor.putString("saldoBono", "");
					editor.putString("saldoPromocional", "");
					editor.commit();
					// str =
					// "Tu saldo es 856.00 colones, con kolbi tu saldo no vence mientras tu linea este activa.Tu Bono kolbi Te Salva es de 250.00 colones, vence el17/07/2013 a las 00:00 am. Tu Bono es de 500.00 colones, vence el18/07/2013 a las 00:00 am.";
					sac = "";
					float saldo = 0;
					String[] partes = str.split("colones");
					if (partes.length == 2) {
						sac = partes[0].toString() + " colones.";
						if (sac.indexOf("-") == -1) {
							saldo = Float
									.parseFloat(partes[0].substring(partes[0]
											.indexOf("saldo es ") + 9));
							editor.putString("saldoOriginal", saldo + "");
							editor.commit();
						}
					} else {
						for (int i = 0; i < partes.length; i++) {

							if (partes[i].indexOf("Tu saldo es 0.00") == -1
									&& partes[i].indexOf("-") == -1) {
								if (partes[i].indexOf("Tu saldo es ") != -1) {

									saldo = Float.parseFloat(partes[i]
											.substring(partes[i]
													.indexOf("saldo es ") + 9));
									editor.putString("saldoOriginal", saldo
											+ "");
									editor.commit();
								}
							} else {
								editor.putString("saldoOriginal", partes[i]
										.substring(partes[i]
												.indexOf("saldo es ") + 9));
							}
							if (partes[i]
									.indexOf("Tu Bono kolbi Te Salva es de 0.00") == -1) {
								if (partes[i].indexOf("Tu Bono kolbi") != -1) {

									editor.putString(
											"fechaBonoSaldo",
											"expira el "
													+ partes[i + 1].substring(
															partes[i + 1]
																	.indexOf("vence el") + 8,
															partes[i + 1]
																	.indexOf("vence el") + 19));
									editor.putString(
											"saldoPromocional",
											partes[i].substring(partes[i]
													.indexOf("Salva es de ") + 12));
									editor.commit();
									saldo += Float
											.parseFloat(partes[i].substring(partes[i]
													.indexOf("Salva es de ") + 12));
								}
							} else if (partes[i]
									.indexOf("Tu Bono kolbi Te Salva es de 0.00") != -1
									&& partes[0].indexOf("Tu saldo es -") != -1) {
								sac = partes[0].toString() + " colones.";
								editor.putString("saldoOriginal", partes[i]
										.substring(partes[i]
												.indexOf("Tu saldo es ") + 12));
								editor.commit();
							}
							if (partes[i].indexOf("Bono es de") != -1) {
								if (partes[i].indexOf("Tu Bono es de 0.00") == -1) {
									editor.putString(
											"fechaBono",
											"expira el "
													+ partes[i + 1].substring(
															partes[i + 1]
																	.indexOf("vence el") + 8,
															partes[i + 1]
																	.indexOf("vence el") + 19));
									editor.putString(
											"saldoBono",
											partes[i].substring(partes[i]
													.indexOf("Tu Bono es de ") + 14));
									editor.commit();
									saldo += Float
											.parseFloat(partes[i].substring(partes[i]
													.indexOf("Tu Bono es de ") + 14));
								} else if (partes[i]
										.indexOf("Tu Bono es de 0.00") != -1
										&& partes[0].indexOf("Tu saldo es -") != -1) {
									sac = partes[0].toString() + " colones.";
									editor.putString(
											"saldoOriginal",
											partes[0].substring(partes[i]
													.indexOf("Tu saldo es ") + 12));
									editor.commit();
								}
							}

						}
					}
					DecimalFormat fmt = new DecimalFormat();
					DecimalFormatSymbols fmts = new DecimalFormatSymbols();
					fmts.setGroupingSeparator('.');
					fmt.setGroupingSize(3);
					fmt.setGroupingUsed(true);
					fmt.setDecimalFormatSymbols(fmts);
					String nuevoSaldo = fmt.format(saldo);
					if (saldo > 0) {
						sac = "Tu saldo es " + nuevoSaldo + " colones";
					}
					if (sac.equals("")) {
						sac = "Tu saldo es 0.00 colones";
					}
					this.calculoKolbi(saldo, context);
					editor.putString("sms", sac);
					editor.putString("valores", totales);
					editor.commit();
				}
			} else if (name.equals("651") || name.equals("movistar")) {
				this.abortBroadcast();

				editor.putString("fechaBono", "");
				editor.putString("fechaBonoSaldo", "");
				editor.putString("FechaMovistar", "");
				editor.putString("saldoOriginal", "");
				editor.putString("saldoBono", "");
				editor.putString("saldoPromocional", "");
				editor.commit();
				sac = "";
				float saldo = 0;
				String[] partes = str.split("colones");
				if (partes.length == 2) {
					sac = "Tu "
							+ partes[0].substring(partes[0]
									.indexOf("saldo es de")) + "colones";
					saldo = Float.parseFloat(partes[0].substring(
							(partes[0].indexOf("saldo es de ") + 12),
							(partes[0].length() - 1)));
					editor.putString("saldoOriginal", saldo + "");
					editor.commit();
					editor.putString(
							"FechaMovistar",
							" y expira el "
									+ partes[0 + 1].substring(
											partes[0 + 1].indexOf("vence ") + 6,
											partes[0 + 1].length() - 1));
					editor.commit();
				} else {
					for (int i = 0; i < partes.length; i++) {
						if (partes[i].indexOf("saldo es de 0.00") == -1) {
							if (partes[i].indexOf("saldo es de") != -1) {
								saldo = Float
										.parseFloat(partes[i].substring(
												(partes[i]
														.indexOf("saldo es de ") + 12),
												(partes[i].length() - 1)));
								editor.putString(
										"FechaMovistar",
										"El Saldo expira el "
												+ partes[i + 1].substring(
														partes[i + 1]
																.indexOf("vence ") + 6,
														partes[i + 1]
																.indexOf(". Su") - 1));
								editor.putString("saldoOriginal", saldo + "");
								editor.commit();
								editor.commit();
							}
						}
						if (partes[i]
								.indexOf("Su saldo promocional es de 0.00") == -1) {
							if (partes[i].indexOf("Su saldo promocional es de") != -1) {
								saldo += Float
										.parseFloat(partes[i].substring(
												(partes[i]
														.indexOf("saldo promocional es de ") + 24),
												(partes[i].length() - 1)));
								editor.putString(
										"fechaBono",
										"El Bono expira el "
												+ partes[i + 1].substring(
														partes[i + 1]
																.indexOf("vence ") + 6,
														partes[i + 1].length() - 2));
								editor.putString(
										"saldoBono",
										partes[i].substring(
												(partes[i]
														.indexOf("saldo promocional es de ") + 24),
												(partes[i].length() - 1)));
								editor.commit();
							}
						}
					}
				}
				calculoKolbi(saldo, context);
				DecimalFormat fmt = new DecimalFormat();
				DecimalFormatSymbols fmts = new DecimalFormatSymbols();
				fmts.setGroupingSeparator('.');
				fmt.setGroupingSize(3);
				fmt.setGroupingUsed(true);
				fmt.setDecimalFormatSymbols(fmts);
				String nuevoSaldo = fmt.format(saldo);
				sac = "Tu saldo es" + nuevoSaldo + "Colones";
				editor.putString("sms", sac);
				editor.putString("valores", totales);
				editor.commit();
			}
		}
	}

	public void calculoKolbi(float saldo, Context contex) {
		// String valorMi = "minutos";
		// String valorSe = "segundos";
		SharedPreferences prefs = contex.getSharedPreferences("WidgetPrefs",
				Context.MODE_PRIVATE);
		float bonoLlamadas = Float.parseFloat(prefs.getString("bonollamadas",
				"0.00"));
		saldo = saldo + bonoLlamadas;
		int segundos = (int) (saldo / 0.57);
		int minuto = segundos / 60;
		// if (minuto == 1) {
		// valorMi = "minuto";
		// }
		segundos = segundos % 60;
		// if (segundos == 1) {
		// valorSe = "segundo";
		// }
		minutos = "Tienes " + minuto + " min y " + segundos
				+ " seg en llamadas o ";
		saldo = saldo - bonoLlamadas;
		String valorMe = "mensajes";
		int mensaje = (int) (saldo / 3);
		if (mensaje == 1) {
			valorMe = "mensaje";
		}

		int mmsm = Integer.parseInt(prefs.getString("bonoSms", "0"));

		mensaje = mensaje + mmsm;
		mensajes = mensaje + " " + valorMe;

		totales = minutos + mensajes;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		Log.w("finalize", "El broadcast Receiver de mensajes finalizo");
	}

}
