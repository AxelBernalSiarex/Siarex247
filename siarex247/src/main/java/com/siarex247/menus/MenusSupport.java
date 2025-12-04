package com.siarex247.menus;

import java.util.ArrayList;

import com.siarex247.bd.ActionDB;

public class MenusSupport extends ActionDB {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7942192510474175909L;

	
	private ArrayList<MenusForm> listaOpciones = null;
	private MenusForm menusForm = null;
	private int idMenu;
	private int idOpcion;
	
	private int idPerfil;
	private int idUsuario;

	private int idRegistro;
	
	public ArrayList<MenusForm> getListaOpciones() {
		return listaOpciones;
	}


	public void setListaOpciones(ArrayList<MenusForm> listaOpciones) {
		this.listaOpciones = listaOpciones;
	}


	public MenusForm getMenusForm() {
		return menusForm;
	}


	public void setMenusForm(MenusForm menusForm) {
		this.menusForm = menusForm;
	}


	public int getIdMenu() {
		return idMenu;
	}


	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}


	public int getIdOpcion() {
		return idOpcion;
	}


	public void setIdOpcion(int idOpcion) {
		this.idOpcion = idOpcion;
	}


	public int getIdPerfil() {
		return idPerfil;
	}


	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}


	public int getIdUsuario() {
		return idUsuario;
	}


	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}


	public int getIdRegistro() {
		return idRegistro;
	}


	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}



	
	
	
	
	
 }
