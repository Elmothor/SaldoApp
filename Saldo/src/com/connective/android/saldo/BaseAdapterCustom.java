package com.connective.android.saldo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseAdapterCustom extends BaseAdapter {
	protected Activity activity;
	protected ArrayList<Articulos> items;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	public BaseAdapterCustom(Activity activity, ArrayList<Articulos> items) {
		this.activity = activity;
		this.items = items;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0).getId();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		if (arg1 == null) {
			LayoutInflater inf = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.row, null);
		}
		Articulos art = items.get(arg0);
		TextView nombre = (TextView) v.findViewById(R.id.tvNombreRow);
		nombre.setText(art.getNombre());
		CheckBox check = (CheckBox) v.findViewById(R.id.chkCuadroRow);
		check.setChecked(art.getEstado());
		if (art.getVisible()) {
			check.setVisibility(View.VISIBLE);
			check.setClickable(false);
		} else {
			check.setVisibility(View.GONE);
		}
		return v;
	}
	  public void updateResults(ArrayList<Articulos> results) {
	        items = results;
	        //Triggers the list update
	        notifyDataSetChanged();
	    }

}
