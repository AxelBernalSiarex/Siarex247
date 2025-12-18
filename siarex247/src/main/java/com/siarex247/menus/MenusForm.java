package com.siarex247.menus;

import java.util.List;

/**
 * @author eidon
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class MenusForm {

	private int claveMenu = 0;
	private int claveOpcion = 0;
	
	private String nombreCorto;
	private String nombreCortoMenu;
	private String descripcion;
	private String url;
	private int orden = 0;
	private int alto = 0;
	private int ancho = 0;
	private String frame;
	private String javascript;
	private String active;
	private String classname1;
	private String classname2;
	private String classname3;
	private String imagen;
	private String visible;

	private String tipoMenu;
	private boolean selected;

	private List<OpcionesForm> dataOpciones;
	
	/**
	 * @return Devuelve claveMenu.
	 */
	public int getClaveMenu() {
		return claveMenu;
	}
	/**
	 * @param claveMenu El claveMenu a establecer.
	 */
	public void setClaveMenu(int claveMenu) {
		this.claveMenu = claveMenu;
	}
	/**
	 * @return Devuelve descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion El descripcion a establecer.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Devuelve nombreCorto.
	 */
	public String getNombreCorto() {
		return nombreCorto;
	}
	/**
	 * @param nombreCorto El nombreCorto a establecer.
	 */
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}
	
	
	
	/**
	 * @return Devuelve url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url El url a establecer.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return Devuelve orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden El orden a establecer.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public int getAlto() {
		return alto;
	}
	public void setAlto(int alto) {
		this.alto = alto;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public int getAncho() {
		return ancho;
	}
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	public String getJavascript() {
		return javascript;
	}
	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getNombreCortoMenu() {
		return nombreCortoMenu;
	}
	public void setNombreCortoMenu(String nombreCortoMenu) {
		this.nombreCortoMenu = nombreCortoMenu;
	}
	public int getClaveOpcion() {
		return claveOpcion;
	}
	public void setClaveOpcion(int claveOpcion) {
		this.claveOpcion = claveOpcion;
	}
	public String getClassname1() {
		return classname1;
	}
	public void setClassname1(String classname1) {
		this.classname1 = classname1;
	}
	public String getClassname2() {
		return classname2;
	}
	public void setClassname2(String classname2) {
		this.classname2 = classname2;
	}
	public String getClassname3() {
		return classname3;
	}
	public void setClassname3(String classname3) {
		this.classname3 = classname3;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getTipoMenu() {
		return tipoMenu;
	}
	public void setTipoMenu(String tipoMenu) {
		this.tipoMenu = tipoMenu;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	
	
	
	
 }
