package com.connective.android.saldo;

public class Numeros {
	private String name;
	private String phoneNo;
	boolean selected = false;

	public Numeros(String phoneNo, String name, boolean selected) {
		super();
		this.phoneNo = phoneNo;
		this.name = name;
		this.selected = selected;
	}
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPhoneNo() {
			return phoneNo;
		}
		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
		
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}