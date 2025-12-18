package com.siarex247.menus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.utils.Utils;


public class MenusBean {

	public static final Logger logger = Logger.getLogger("manifest247");
	
	public ArrayList<MenusForm> obtenerOpcionesxMenu(Connection con, String esquema, String lenguaje, int idPerfil, int claveMenu){
		ArrayList<MenusForm> resultado = new ArrayList<MenusForm>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MenusForm menuForm = new MenusForm();
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		try{
			HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(lenguaje, "MENU_PRINCIPAL");
			
			stmt = con.prepareStatement(MenusQuerys.getOpcionesxMenu(esquema));
			stmt.setInt(1, idPerfil);
			stmt.setInt(2, claveMenu);
			
			rs = stmt.executeQuery();
			int numReg = 1;
			
			//logger.info("stmt===>"+stmt);
			
			while (rs.next()){
				if (numReg == 1) {
					menuForm.setActive("active");
				}
				numReg++;
				menuForm.setClaveMenu(rs.getInt(1));
				menuForm.setClaveOpcion(rs.getInt(2));
				menuForm.setNombreCorto(rs.getString(3));
				//menuForm.setDescripcion(rs.getString(4));
				menuForm.setDescripcion(mapaLenguaje.get(Utils.noNuloNormal(rs.getString(3))));
				menuForm.setUrl(rs.getString(5));
				menuForm.setAlto(rs.getInt(6));
				menuForm.setFrame(rs.getString(7));
				menuForm.setAncho(rs.getInt(8));
				menuForm.setNombreCortoMenu(rs.getString(9));
				menuForm.setClassname1(Utils.noNuloNormal(rs.getString(10)));
				
				resultado.add(menuForm);
				menuForm = new MenusForm();
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;

	}
	
	/*
	
	public ArrayList<OpcionesForm> obtenerOpcionesxMenu(Connection con, String esquema, int idPerfil, int claveMenu){
		ArrayList<MenusForm> resultado = new ArrayList<MenusForm>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OpcionesForm opcionesForm = new OpcionesForm();
		try{
			stmt = con.prepareStatement(MenusQuerys.getOpcionesxMenu(esquema));
			stmt.setInt(1, idPerfil);
			stmt.setInt(2, claveMenu);
			
			rs = stmt.executeQuery();
			while (rs.next()){
				opcionesForm.setClaveOpcion((rs.getInt(2));
				opcionesForm.setNombreCorto(rs.getString(3));
				opcionesForm.setDescripcion(rs.getString(4));
				opcionesForm.setUrlOpcion(rs.getString(5));
				
				resultado.add(opcionesForm);
				opcionesForm = new OpcionesForm();
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;

	}
	*/
	
	/*
	public ArrayList<MenusForm> obtenerOpcionesxMenu(Connection con, String esquema, int idUsuario, int claveMenu){
		ArrayList<MenusForm> resultado = new ArrayList<MenusForm>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MenusForm menuForm = new MenusForm();
		try{
			stmt = con.prepareStatement(MenusQuerys.getOpcionesxMenu(esquema));
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, claveMenu);
			
			rs = stmt.executeQuery();
			int numReg = 1;
			while (rs.next()){
				if (numReg == 1) {
					menuForm.setActive("active");
				}
				numReg++;
				menuForm.setClaveMenu(rs.getInt(1));
				menuForm.setClaveOpcion(rs.getInt(2));
				menuForm.setNombreCorto(rs.getString(3));
				menuForm.setDescripcion(rs.getString(4));
				menuForm.setUrl(rs.getString(5));
				menuForm.setAlto(rs.getInt(6));
				menuForm.setFrame(rs.getString(7));
				menuForm.setAncho(rs.getInt(8));
				menuForm.setNombreCortoMenu(rs.getString(9));
				menuForm.setClassname1(Utils.noNuloNormal(rs.getString(10)));
				
				resultado.add(menuForm);
				menuForm = new MenusForm();
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;

	}
	*/
	public ArrayList<MenusForm> obtenerOpciones(Connection con, String esquema, int perfilUsuario){
		ArrayList<MenusForm> resultado = new ArrayList<MenusForm>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MenusForm menuForm = new MenusForm();
		try{
			stmt = con.prepareStatement(MenusQuerys.getQueryOpciones(esquema));
			stmt.setInt(1, perfilUsuario);
			rs = stmt.executeQuery();
			while (rs.next()){
				menuForm.setClaveMenu(rs.getInt(1));
				menuForm.setNombreCorto(rs.getString(2));
				menuForm.setDescripcion(rs.getString(3));
				menuForm.setUrl(rs.getString(4));
				menuForm.setAlto(rs.getInt(5));
				menuForm.setFrame(rs.getString(6));
				resultado.add(menuForm);
				menuForm = new MenusForm();
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;
	}
	
	
	
	public boolean tienePermisoPerfil(Connection con, String esquema, int perfilUsuario, int idMenu){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean bandPermiso = false;
		try{
			stmt = con.prepareStatement(MenusQuerys.getTienePermiso(esquema));
			stmt.setInt(1, perfilUsuario);
			stmt.setInt(2, idMenu);
			rs = stmt.executeQuery();
			if (rs.next()){
				bandPermiso = true;
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return bandPermiso;
	}
	
	public ArrayList<ArrayList<String>> obtenerOpcionesxMenu2(Connection con, String esquema, int idPerfil, int claveMenu){
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		ArrayList<String> datos = new ArrayList <String>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(MenusQuerys.getOpcionesxMenu(esquema));
			stmt.setInt(1, idPerfil);
			stmt.setInt(2, claveMenu);
			rs = stmt.executeQuery();
			int numReg = 1;

			while (rs.next()){
				if (numReg == 1) {
					datos.add("active");
				}
				else {
					datos.add("");
				}
				numReg++;
				datos.add(String.valueOf(rs.getInt(1)));
				datos.add(String.valueOf(rs.getInt(2)));
				datos.add(rs.getString(3));
				datos.add(rs.getString(4));
				datos.add(rs.getString(5));
				datos.add(String.valueOf(rs.getInt(6)));
				datos.add(rs.getString(7));
				datos.add(String.valueOf(rs.getInt(8)));
				datos.add(rs.getString(9));
				datos.add(Utils.noNuloNormal(rs.getString(10)));
				datos.add(Utils.noNuloNormal(rs.getString(11)));
				
				resultado.add((ArrayList<String>) datos.clone());
				datos.clear();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("obtenerOpcionesxMenu2(): ", e);
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;
	}
	
	public ArrayList<ArrayList<String>> obtenerMenuPrincipal2(Connection con, String esquema, int idPerfil){
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		ArrayList<String> datos = new ArrayList <String>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		final String VISIBLE = "visible";

		try{
			stmt = con.prepareStatement(MenusQuerys.getMenusPrincipal(esquema));
			stmt.setInt(1, idPerfil);
			rs = stmt.executeQuery();

			while (rs.next()){
				datos.add(String.valueOf(rs.getInt(1)));
				datos.add(Utils.noNuloNormal(rs.getString(2)));
				datos.add(Utils.noNuloNormal(rs.getString(3)));
				datos.add(Utils.noNuloNormal(rs.getString(4)));
				datos.add(Utils.noNuloNormal(rs.getString(5)));
				datos.add(Utils.noNuloNormal(rs.getString(6)));
				datos.add(Utils.noNuloNormal(rs.getString(7)));
				datos.add(Utils.noNuloNormal(rs.getString(8)));
				datos.add(VISIBLE);
				
				resultado.add((ArrayList<String>) datos.clone());
				datos.clear();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("obtenerMenuPrincipal2(): ", e);
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;
	}
	
	public ArrayList<MenusForm> obtenerMenuPrincipal(Connection con, String esquema, String lenguaje, int idPerfil){
		PreparedStatement stmt = null;
		MenusForm menuForm = new MenusForm();
		ResultSet rs = null;
		ArrayList<MenusForm> resultado = new ArrayList <MenusForm>();
		final String VISIBLE = "visible";
		//final String HIDDEN = "hidden";
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		try{
			HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(lenguaje, "MENU_PRINCIPAL");
			
			stmt = con.prepareStatement(MenusQuerys.getMenusPrincipal(esquema));
			stmt.setInt(1, idPerfil);
			// logger.info("stmtMenus===>"+stmt);
			rs = stmt.executeQuery();
			while (rs.next()){
				menuForm.setClaveMenu(rs.getInt(1));
				menuForm.setNombreCorto( Utils.noNuloNormal(rs.getString(2)));
				//menuForm.setDescripcion(Utils.noNuloNormal(rs.getString(3)));
				menuForm.setDescripcion(Utils.noNuloNormal(mapaLenguaje.get(rs.getString(2))));
				menuForm.setJavascript(Utils.noNuloNormal(rs.getString(4)));
				menuForm.setImagen(Utils.noNuloNormal(rs.getString(5)));
				menuForm.setClassname1(Utils.noNuloNormal(rs.getString(6)));
				menuForm.setClassname2(Utils.noNuloNormal(rs.getString(7)));
				menuForm.setClassname3(Utils.noNuloNormal(rs.getString(8)));
				menuForm.setTipoMenu(Utils.noNuloNormal(rs.getString(9)));
				menuForm.setVisible(VISIBLE);
				
				resultado.add(menuForm);
				menuForm = new MenusForm();
			}
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resultado;
	}
	
	
	public MenusForm obtenerInfoMenuOpcion(Connection con, String esquema, int idMenu, int idOpcion){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MenusForm menuForm = new MenusForm();
		try{
			stmt = con.prepareStatement(MenusQuerys.getOpcionesxMenuOpcion(esquema));
			stmt.setInt(1, idMenu);
			stmt.setInt(2, idOpcion);
			rs = stmt.executeQuery();
			if (rs.next()){
				menuForm.setClaveMenu(rs.getInt(1));
				menuForm.setClaveOpcion(rs.getInt(2));
				menuForm.setNombreCorto(rs.getString(3));
				menuForm.setDescripcion(rs.getString(4));
				menuForm.setUrl(rs.getString(5));
				menuForm.setAlto(rs.getInt(6));
				menuForm.setFrame(rs.getString(7));
				menuForm.setAncho(rs.getInt(8));
				menuForm.setNombreCortoMenu(rs.getString(9));
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return menuForm;

	}
	
	
	
	public ArrayList<MenusForm> detalleMenus (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getDetalleMenus(esquema));
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				menusForm.setDescripcion(Utils.noNulo(rs.getString(3)));
				menusForm.setImagen(Utils.noNulo(rs.getString(4)));
				menusForm.setTipoMenu(Utils.noNulo(rs.getString(5)));
				menusForm.setJavascript(Utils.noNuloNormal(rs.getString(6)));
				menusForm.setClassname1(Utils.noNuloNormal(rs.getString(7)));
				menusForm.setClassname2(Utils.noNuloNormal(rs.getString(8)));
				menusForm.setClassname3(Utils.noNuloNormal(rs.getString(9)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	
	public ArrayList<MenusForm> comboMenus (Connection con, String esquema, int idPerfil){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {	
			stmt = con.prepareStatement(MenusQuerys.getComboMenus(esquema));
			stmt.setInt(1, idPerfil);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNulo(rs.getString(2)) + " - " + Utils.noNulo(rs.getString(3)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	
	
	public ArrayList<MenusForm> getMenusDisponibles (Connection con, String esquema, int idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getMenusDisponibles(esquema));
			stmt.setInt(1, idUsuario);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNulo(rs.getString(2)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	
	public ArrayList<MenusForm> getMenusAsignados (Connection con, String esquema, int idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getMenusAsignados(esquema));
			stmt.setInt(1, idUsuario);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNulo(rs.getString(2)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	
	
	public ArrayList<MenusForm> getMenusDisponiblesPerfil (Connection con, String esquema, int idPerfil){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getMenusDisponiblesPerfil(esquema));
			stmt.setInt(1, idPerfil);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	
	
	public ArrayList<MenusForm> getMenusAsignadosPerfil (Connection con, String esquema, int idPerfil){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getMenusAsignadosPerfil(esquema));
			stmt.setInt(1, idPerfil);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}

	
	
	
	public ArrayList<MenusForm> getOpcionesDisponiblesUsuario (Connection con, String esquema, int idMenu, int idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getOpcionesDisponiblesUsuario(esquema));
			stmt.setInt(1, idMenu);
			stmt.setInt(2, idUsuario);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	public ArrayList<MenusForm> getOpcionesAsignadosUsuario (Connection con, String esquema, int idMenu, int idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<MenusForm> listaMenus = new ArrayList<MenusForm>();
		MenusForm menusForm = new MenusForm();
		try {
			stmt = con.prepareStatement(MenusQuerys.getOpcionesAsignadosUsuario(esquema));
			stmt.setInt(1, idMenu);
			stmt.setInt(2, idUsuario);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				menusForm.setClaveMenu(rs.getInt(1));
				menusForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				listaMenus.add(menusForm);
				menusForm = new MenusForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaMenus;
	}
	
	
	
	public int altaOpcionUsuario (Connection con, String esquema, int idOpcion, int idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(MenusQuerys.getGuardarOpcionAsignadosUsuario(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idOpcion);
			stmt.setInt(3, 1);
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return resultado;
	}
	
	
	public int eliminaOpcionUsuario (Connection con, String esquema, int idOpcion, int idUsuario){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(MenusQuerys.getEliminarOpcionAsignadosUsuario(esquema));
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idOpcion);
			resultado = stmt.executeUpdate();
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return resultado;
	}
 }
