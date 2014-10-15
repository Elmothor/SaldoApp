package com.connective.android.saldo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

@SuppressLint("SetJavaScriptEnabled")
public class Configuracion extends Activity {
	private static boolean internet = false;
	private static final int DIALOGO_SELECCION = 3;
	private static final int DIALOGO_SPINNER = 4;
	private AdView adView;
	private LinearLayout lytMain;
	private String MI_ID_ADMOB = "a152bea79851940";
	ArrayList<Articulos> arrayList = new ArrayList<Articulos>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuracion);
		lytMain = (LinearLayout) findViewById(R.id.lytMain);
		adView = new AdView(this, AdSize.BANNER, MI_ID_ADMOB);
		lytMain.addView(adView);
		adView.bringToFront();
		adView.loadAd(new AdRequest());
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String operador = telephonyManager.getNetworkOperatorName();
		// Toast.makeText(getApplicationContext(), operador,
		// Toast.LENGTH_LONG).show();

		final SharedPreferences settings = getSharedPreferences("WidgetPrefs",
				MODE_PRIVATE);
		final ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo current = connec.getActiveNetworkInfo();
		final String modeloPhone = getDeviceName();
		if (current != null) {

			String internet = settings.getString("internet", "noListo");
			if (internet.equals("noListo")) {
				Thread thr = new Thread(new Runnable() {
					@Override
					public void run() {
						// Enviamos el texto escrito a la funcion
						enviarDatos(modeloPhone, operador + "");
					}
				});
				// Arrancamos el Hilo
				thr.start();
			}
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("internet", "listo");
			editor.commit();
		}
		String tuto = settings.getString("tutorial", "noVisto");
		if (tuto.equals("noVisto")) {
			Intent tutorial = new Intent(Configuracion.this,
					SwipeScreenExample.class);
			startActivity(tutorial);
		}
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("tutorial", "visto");
		editor.commit();
		final ListView lstConfiguracion = (ListView) findViewById(R.id.lstConfiguracion);

		String entrar = settings.getString("interChequeado", "false");
		Boolean entra = Boolean.valueOf(entrar);
		Articulos articulos;
		articulos = new Articulos("Color de fondo", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Detalle de saldo", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Comparte esta aplicacion", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Visualizar consumo de internet", entra, true);
		arrayList.add(articulos);
		articulos = new Articulos("Configuración", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Tutorial", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Síguenos", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Acerca de", false, false);
		arrayList.add(articulos);
		articulos = new Articulos("Salir", false, false);
		arrayList.add(articulos);
		final BaseAdapterCustom adapter = new BaseAdapterCustom(this, arrayList);
		lstConfiguracion.setAdapter(adapter);
		lstConfiguracion.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					showDialog(DIALOGO_SPINNER);
					break;
				case 1:
					String fechaBono = settings.getString("fechaBono", "");
					String fechaBonoSaldo = settings.getString(
							"fechaBonoSaldo", "");
					String fechaMovistar = settings.getString("FechaMovistar",
							"");
					String saldoOriginal = settings.getString("saldoOriginal",
							"");
					String saldoBono = settings.getString("saldoBono", "");
					String saldoPromocional = settings.getString(
							"saldoPromocional", "");
					Dialog dialog = new Dialog(Configuracion.this);
					dialog.setTitle("Fecha de expiración");
					dialog.setCanceledOnTouchOutside(true);
					dialog.setContentView(R.layout.alerta_vencimiento);
					TextView tvFechaBono = (TextView) dialog
							.findViewById(R.id.dpFechaBono);
					TextView tvFechaBonoSaldo = (TextView) dialog
							.findViewById(R.id.dpFechaBonoSaldo);
					TextView tvFechaOriginal = (TextView) dialog
							.findViewById(R.id.dpFechaOriginal);
					tvFechaOriginal.setText("Tu saldo es " + saldoOriginal
							+ fechaMovistar);
					if (!fechaBono.equals("") || !fechaBonoSaldo.equals("")) {

						if (!fechaBono.equals("") && !fechaBonoSaldo.equals("")) {
							tvFechaBono.setText("Tu bono promocional es "
									+ saldoBono + " y " + fechaBono);
							tvFechaBonoSaldo
									.setText("Tu sálvame es "
											+ saldoPromocional + " y "
											+ fechaBonoSaldo);
						} else if (!fechaBono.equals("")
								&& fechaBonoSaldo.equals("")) {
							tvFechaBonoSaldo.setText("Tu bono promocional es "
									+ saldoBono + " y " + fechaBono);
						} else if (fechaBono.equals("")
								&& !fechaBonoSaldo.equals("")) {
							tvFechaBonoSaldo
									.setText("Tu sálvame es "
											+ saldoPromocional + " y "
											+ fechaBonoSaldo);
						} else {
							tvFechaBono.setText("");
							tvFechaBonoSaldo.setText("");
						}
						dialog.show();
					} else {
						Toast.makeText(getApplicationContext(),
								"No tienes ningun bono en este momento.",
								Toast.LENGTH_LONG).show();
					}
					break;
				case 2:
					Intent contactos = new Intent(Configuracion.this,
							Contactos.class);
					startActivity(contactos);
					break;
				case 3:
					/*
					 * Toast.makeText(Configuracion.this,
					 * "Función inhabilitada, disculpa los inconvenientes.",
					 * Toast.LENGTH_LONG) .show();
					 */
					NetworkInfo current = connec.getActiveNetworkInfo();
					current = connec.getActiveNetworkInfo();
					String algo = current + "";
					if (!algo.equals("null")) {
						if (!current.getTypeName().toString().equals("mobile")) {
							if (operador.equals("Kolbi ICE")
									|| operador.equals("ICE")
									|| operador.equals("I.C.E.")
									|| operador.equals("K lbi ICE")
									|| operador
											.equals("Instituto Costarricense de Electricidad")
									|| operador.equals("ICE Celular")
									|| operador.equals("K?lbi ICE")
									|| operador.equals("I.C.E.,I.C.E.")) {
								String entrar = settings.getString(
										"interChequeado", "false");
								Boolean entra = Boolean.valueOf(entrar);

								if (entra) {
									internet = false;
								} else {
									internet = true;
								}
								SharedPreferences.Editor editor = settings
										.edit();
								Articulos arti = arrayList.get(position);
								adapter.notifyDataSetChanged();
								arti.setEstado(internet);
								if (internet) {
									Toast.makeText(Configuracion.this,
											"Función activada",
											Toast.LENGTH_LONG).show();
									editor.putString("interChequeado", "true");
								} else {
									Toast.makeText(Configuracion.this,
											"Función desactivada",
											Toast.LENGTH_LONG).show();
									editor.putString("interChequeado", "false");
								}
								editor.commit();
							} else {
								Toast.makeText(
										Configuracion.this,
										"Esta función solo esta disponible para lineas kolbi",
										Toast.LENGTH_LONG).show();
							}

						} else {
							Toast.makeText(
									Configuracion.this,
									"Para activar o desactivar esta función asegurate de no estar conectado a internet usando el paquete de datos",
									Toast.LENGTH_LONG).show();

						}
					} else {
						if (operador.equals("Kolbi ICE")
								|| operador.equals("ICE")
								|| operador.equals("I.C.E.")
								|| operador.equals("K lbi ICE")
								|| operador
										.equals("Instituto Costarricense de Electricidad")
								|| operador.equals("ICE Celular")
								|| operador.equals("K?lbi ICE")
								|| operador.equals("I.C.E.,I.C.E.")) {
							String entrar = settings.getString(
									"interChequeado", "false");
							Boolean entra = Boolean.valueOf(entrar);

							if (entra) {
								internet = false;
							} else {
								internet = true;
							}
							SharedPreferences.Editor editor = settings.edit();
							Articulos arti = arrayList.get(position);
							adapter.notifyDataSetChanged();
							arti.setEstado(internet);
							if (internet) {
								Toast.makeText(Configuracion.this,
										"Función activada", Toast.LENGTH_LONG)
										.show();
								editor.putString("interChequeado", "true");
							} else {
								Toast.makeText(Configuracion.this,
										"Función desactivada",
										Toast.LENGTH_LONG).show();
								editor.putString("interChequeado", "false");
							}
							editor.commit();
						} else {
							Toast.makeText(
									Configuracion.this,
									"Esta función solo esta disponible para lineas kolbi",
									Toast.LENGTH_LONG).show();
						}
					}

					ArrayList<Articulos> articu = new ArrayList<Articulos>();
					String entrar = settings.getString("interChequeado",
							"false");
					Boolean entra = Boolean.valueOf(entrar);
					Articulos articulos;
					articulos = new Articulos("Color de fondo", false, false);
					articu.add(articulos);
					articulos = new Articulos("Detalle de saldo", false, false);
					articu.add(articulos);
					articulos = new Articulos("Comparte esta aplicacion",
							false, false);
					articu.add(articulos);
					articulos = new Articulos("Visualizar consumo de internet",
							entra, true);
					articu.add(articulos);
					articulos = new Articulos("Configuración", false, false);
					articu.add(articulos);
					articulos = new Articulos("Tutorial", false, false);
					articu.add(articulos);
					articulos = new Articulos("Síguenos", false, false);
					articu.add(articulos);
					articulos = new Articulos("Acerca de", false, false);
					articu.add(articulos);
					articulos = new Articulos("Salir", false, false);
					articu.add(articulos);
					adapter.updateResults(articu);
					break;
				case 4:
					showDialog(DIALOGO_SELECCION);
					break;
				case 5:
					Intent tutorial = new Intent(Configuracion.this,
							SwipeScreenExample.class);
					startActivity(tutorial);
					break;
				case 6:
					try {
						getPackageManager().getPackageInfo(
								"com.facebook.katana", 0);
						Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/575087102559813"));
					startActivity(fb);
					} catch (Exception e) {
						Intent fb2 = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("https://www.facebook.com/connectiveSaldo"));
						startActivity(fb2);
					}
					
					break;
				case 7:

					acercaDe();
					break;
				case 8:

					finish();
					break;
				default:
					break;
				}
			}
		});
	}

	private void acercaDe() {
		AlertDialog alertDialog = new AlertDialog.Builder(Configuracion.this)
				.create();
		TextView message = new TextView(Configuracion.this);
		SpannableString s = new SpannableString(
				"Este widget fue creado por Connective para tu comodidad, cualquier sugerencia o reporte de bugs realizarlo al correo connective.inc@gmail.com");
		Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
		alertDialog.setTitle("Saldo");
		message.setText(s);
		message.setTextSize(22);
		message.setMovementMethod(LinkMovementMethod.getInstance());
		alertDialog.setView(message);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setIcon(R.drawable.moneda_icon);
		alertDialog.show();

	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialogo = null;
		switch (id) {
		case DIALOGO_SELECCION:
			dialogo = crearDialogoSeleccion();
			break;
		case DIALOGO_SPINNER:
			dialogo = crearDialogoColor2();
			break;
		default:
			dialogo = null;
			break;
		}

		return dialogo;
	}

	private Dialog crearDialogoSeleccion() {
		final String[] items = { "1 Minuto", "3 Minutos", "5 Minutos",
				"10 Minutos", "15 Minutos" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.actualizacion);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String tiempo = "";
				Log.i("Dialogos", "Opción elegida: " + items[item]);
				if (items[item].equals("1 Minuto")) {
					tiempo += 1;
				} else if (items[item].equals("3 Minutos")) {
					tiempo += 3;
				} else if (items[item].equals("5 Minutos")) {
					tiempo += 5;
				} else if (items[item].equals("10 Minutos")) {
					tiempo += 10;
				} else if (items[item].equals("15 Minutos")) {
					tiempo += 15;
				}
				SharedPreferences settings = getSharedPreferences(
						"WidgetPrefs", MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("tiempo", tiempo);
				editor.commit();
			}
		});
		return builder.create();
	}

	private Dialog crearDialogoColor2() {
		final String[] items = { "Rosado", "Rojo", "Verde", "Negro",
				"Amarillo", "Azul", "Morado", "Naranja", "Trasparente(Blanco)",
				"Trasparente(Negro)" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.sColor);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				SharedPreferences settings = getSharedPreferences(
						"WidgetPrefs", MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("color", items[item]);
				editor.commit();

			}
		});
		return builder.create();
	}

	public void enviarDatos(String dato, String dato2) {
		// Utilizamos la clase Httpclient para conectar
		HttpClient httpclient = new DefaultHttpClient();
		// Utilizamos la HttpPost para enviar lso datos
		// A la url donde se encuentre nuestro archivo receptor
		HttpPost httppost = new HttpPost("http://inextcloud.tk/agregra.php");
		try {
			// Añadimos los datos a enviar en este caso solo uno
			// que le llamamos de nombre 'a'
			// La segunda linea podría repetirse tantas veces como queramos
			// siempre cambiando el nombre ('a')
			List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);
			postValues.add(new BasicNameValuePair("a", dato));
			postValues.add(new BasicNameValuePair("b", dato2));
			// Encapsulamos
			httppost.setEntity(new UrlEncodedFormEntity(postValues));
			// Lanzamos la petición
			HttpResponse respuesta = httpclient.execute(httppost);
			// Conectamos para recibir datos de respuesta
			HttpEntity entity = respuesta.getEntity();
			// Creamos el InputStream como su propio nombre indica
			InputStream is = entity.getContent();
			// Limpiamos el codigo obtenido atraves de la funcion
			// StreamToString explicada más abajo
			String resultado = StreamToString(is);

			// Enviamos el resultado LIMPIO al Handler para mostrarlo
			Message sms = new Message();
			sms.obj = resultado;
			puente.sendMessage(sms);
		} catch (IOException e) {

		}
	}

	// Funcion para 'limpiar' el codigo recibido
	public String StreamToString(InputStream is) {
		// Creamos el Buffer
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			// Bucle para leer todas las líneas
			// En este ejemplo al ser solo 1 la respuesta
			// Pues no haría falta
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// retornamos el codigo límpio
		return sb.toString();
	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}

	@SuppressLint("HandlerLeak")
	private Handler puente = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Mostramos el mensage recibido del servido en pantall
		}
	};

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}

	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

}
