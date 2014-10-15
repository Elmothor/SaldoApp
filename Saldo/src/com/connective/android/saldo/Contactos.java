package com.connective.android.saldo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Contactos extends Activity {

	MyCustomAdapter dataAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listado_contacto);

		// Generate list View from ArrayList
		mostrarLista();

		checkButtonClick();

	}

	private void mostrarLista() {

		// Array list of countries
		ArrayList<Numeros> listaNumeros = new ArrayList<Numeros>();
		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {

			String name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			Numeros _states = new Numeros(phoneNumber, name, false);

			listaNumeros.add(_states);
		}
		phones.close();

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.contacto_info,
				listaNumeros);
		ListView vistaLista = (ListView) findViewById(R.id.listaNumeros);
		// Assign adapter to ListView
		vistaLista.setAdapter(dataAdapter);

		vistaLista.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				/*
				 * Numeros numero = (Numeros)
				 * parent.getItemAtPosition(position);
				 * Toast.makeText(getApplicationContext(),"Fila cliqueada: " +
				 * numero.getName(), Toast.LENGTH_LONG).show();
				 */
			}
		});
	}

	private class MyCustomAdapter extends ArrayAdapter<Numeros> {

		private ArrayList<Numeros> listNumeros;

		public MyCustomAdapter(Context context, int textViewResourceId,

		ArrayList<Numeros> listNumeros) {
			super(context, textViewResourceId, listNumeros);
			this.listNumeros = new ArrayList<Numeros>();
			this.listNumeros.addAll(listNumeros);
		}

		private class ViewHolder {
			TextView tvPhoneNumber;
			CheckBox cbNombre;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.contacto_info, null);

				holder = new ViewHolder();
				holder.tvPhoneNumber = (TextView) convertView
						.findViewById(R.id.tvNombre);
				holder.cbNombre = (CheckBox) convertView
						.findViewById(R.id.cbNombre);

				convertView.setTag(holder);

				holder.cbNombre.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Numeros _contacto = (Numeros) cb.getTag();
						_contacto.setSelected(cb.isChecked());
					}
				});

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Numeros contacto = listNumeros.get(position);

			holder.tvPhoneNumber.setText(" (" + contacto.getPhoneNo() + ")");
			holder.cbNombre.setText(contacto.getName());
			holder.cbNombre.setChecked(contacto.isSelected());

			holder.cbNombre.setTag(contacto);

			return convertView;
		}

	}

	private void checkButtonClick() {

		Button bEnviar = (Button) findViewById(R.id.bEnviarNumeros);

		bEnviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Thread t = new Thread(new Runnable() {
					public void run() {
						ArrayList<Numeros> listillaNumeros = dataAdapter.listNumeros;
						for (int i = 0; i < listillaNumeros.size(); i++) {
							Numeros contacto = listillaNumeros.get(i);

							if (contacto.isSelected()) {
								String mensaje = "Hola, me gustaría compartir contigo esta genial aplicación llamada 'Saldo'. https://play.google.com/store/apps/details?id=com.connective.android.saldo";
								try {
									SmsManager sms = SmsManager.getDefault();
									ArrayList<String> parts = sms.divideMessage(mensaje); 
									if(contacto.getPhoneNo().indexOf("+506") == -1){
										sms.sendMultipartTextMessage(contacto.getPhoneNo(),
												null, parts, null, null);
									}else{
									sms.sendMultipartTextMessage("506"+contacto.getPhoneNo(),
											null, parts, null, null);
									}
									Thread.sleep(4000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					}
				});
				t.start();
				Toast.makeText(getApplicationContext(),
						"Aplicación compartida", Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}
}