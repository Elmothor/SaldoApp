/*package com.connective.android.saldo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WidgetConfig extends Activity {

	private Button btnColor;
	private Button btnCancelar;
	private Button btnAyudar;
	private Button btnCompartir;
	private Button btnConfigurar;
	private Button btnEstado;
	private static boolean internet = false;
	private static final int DIALOGO_SELECCION = 3;
	private static final int DIALOGO_SPINNER = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_config);
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String operador = telephonyManager.getNetworkOperatorName();
		// Toast.makeText(getApplicationContext(), operador,
		// Toast.LENGTH_LONG).show();
		SharedPreferences settings = getSharedPreferences("WidgetPrefs",
				MODE_PRIVATE);
		String fechaBono = settings.getString("fechaBono", "");
		String fechaBonoSaldo = settings.getString("fechaBonoSaldo", "");
		TextView tvFechaBono = (TextView) findViewById(R.id.tvFechaBono);
		TextView tvFechaBonoSaldo = (TextView) findViewById(R.id.tvFechaBonoSaldo);
		if (!fechaBono.equals("") && !fechaBonoSaldo.equals("")) {
			//tvFechaValidez.setText("Fecha de expiración de bonos");
			tvFechaBono.setText(fechaBono);
			tvFechaBonoSaldo.setText(fechaBonoSaldo);
		} else if (!fechaBono.equals("") && fechaBonoSaldo.equals("")) {
			//tvFechaValidez.setText("Fecha de expiración de bonos");
			tvFechaBono.setText(fechaBono);
		} else if (fechaBono.equals("") && !fechaBonoSaldo.equals("")) {
			//tvFechaValidez.setText("Fecha de expiración de bonos");
			tvFechaBono.setText(fechaBonoSaldo);
		} else {
			//tvFechaValidez.setText("");
			tvFechaBono.setText("");
			tvFechaBonoSaldo.setText("");
		}
		final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		final String modeloPhone = getDeviceName();
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo current = connec.getActiveNetworkInfo();
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
		btnCompartir = (Button) findViewById(R.id.BtnCompartir);
		btnCompartir.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent contactos = new Intent(WidgetConfig.this,
						Contactos.class);
				startActivity(contactos);
			}
		});
		btnEstado = (Button) findViewById(R.id.BtnEstado);
		btnEstado.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				internet = !internet;
				final long inicioBytes = TrafficStats.getTotalRxBytes();
				final long inicioBytesT = TrafficStats.getTotalTxBytes();
				final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				if (internet) {

					final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
							getApplicationContext());
					mBuilder.setContentTitle("Consumo ¢0").setAutoCancel(true)
							.setContentText("0,0kb/r     0,0kb/e")
							.setOngoing(true).setSmallIcon(R.drawable.refresh);
					// Start a lengthy operation in a background thread
					new Thread(new Runnable() {
						@Override
						public void run() {
							int incr;
							// Do the "lengthy" operation 20 times
							while (internet) {
								// Sets the progress indicator to a max value,
								// the
								// current completion percentage, and
								// "determinate"
								// state
								mNotifyManager.notify(1, mBuilder.build());
								// Displays the progress bar for the first time.

								// Sleeps the thread, simulating an operation
								// that takes time
								DecimalFormat fmt = new DecimalFormat();
								DecimalFormatSymbols fmts = new DecimalFormatSymbols();

								fmts.setGroupingSeparator('.');

								fmt.setGroupingSize(3);
								fmt.setGroupingUsed(true);
								fmt.setDecimalFormatSymbols(fmts);
								String consumo = fmt.format((((TrafficStats
										.getTotalTxBytes() - inicioBytesT) / 1024) * 0.0085));
								String recibido = fmt.format((((TrafficStats
										.getTotalRxBytes() - inicioBytes) / 1024)));
								String enviado = fmt.format((((TrafficStats
										.getTotalTxBytes() - inicioBytesT) / 1024)));
								mBuilder.setContentText(
										(recibido + "kb/recibido     "
												+ enviado + "kb/enviado"))
										.setContentTitle("Consumo ¢" + consumo);
								try {
									// Sleep for 5 seconds
									Thread.sleep(1 * 1000);
								} catch (InterruptedException e) {
									Log.d("", "sleep failure");
								}
							}
							// When the loop is finished, updates the
							// notification

						}
					}
					// Starts the thread by calling the run() method in its
					// Runnable
					).start();
				} else {

					mNotifyManager.cancelAll();
				}

			}
		});
		btnColor = (Button) findViewById(R.id.BtnColor);
		btnColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOGO_SPINNER);
			}
		});
		btnConfigurar = (Button) findViewById(R.id.BtnConfigurar);
		new AlertDialog.Builder(this);
		btnConfigurar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOGO_SELECCION);
			}
		});

		btnCancelar = (Button) findViewById(R.id.BtnCancelar);
		btnCancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnAyudar = (Button) findViewById(R.id.BtnAyuda);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		final TextView message = new TextView(this);
		btnAyudar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final SpannableString s = new SpannableString(
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
		});
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
			// TODO Auto-generated catch block

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

	private Handler puente = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Mostramos el mensage recibido del servido en pantall
		}
	};

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
}
*/