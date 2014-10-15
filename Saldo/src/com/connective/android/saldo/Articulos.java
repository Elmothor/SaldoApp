package com.connective.android.saldo;

public class Articulos {
	protected String nombre;
	protected Boolean estado;
	protected Boolean visible;
	protected long id;

	public Articulos(String pNombre, Boolean pEstado, Boolean pVisible) {
		nombre = pNombre;
		estado = pEstado;
		visible = pVisible;
	}

	public Articulos(String pNombre, Boolean pEstado, Boolean pVisible, long pId) {
		nombre = pNombre;
		estado = pEstado;
		visible = pVisible;
		id = pId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}
