package com.siarex247.menus;

public class MenusQuerys {

	
	private static String queryOpciones = "select A.CLAVEMENU, B.NOMBRECORTO, C.DESCRIPCION, C.URL, C.ALTO, A.FRAME from MENUOPCION A, MENUS B, OPCIONES C where A.CLAVEMENU in (select CLAVEMENU from PERFILMENU where CLAVEPERFIL = ?) and A.CLAVEMENU = B.CLAVEMENU and A.CLAVEOPCION = C.CLAVEOPCION order by A.CLAVEMENU, A.ORDEN";
	
	private static String menusPrincipal = "select B.CLAVEMENU , B.NOMBRECORTO, B.DESCRIPCION, B.JAVASCRIPT, B.IMAGEN, B.CLASSNAME1, B.CLASSNAME2, B.CLASSNAME3, B.TIPO_MENU from PERFILMENU A inner join MENUS B on A.CLAVEMENU = B.CLAVEMENU where A.CLAVEPERFIL = ? order by A.ORDEN";

	private static String opcionesxMenu = "select A.CLAVEMENU, A.CLAVEOPCION, B.NOMBRECORTO, B.DESCRIPCION, B.URL, B.ALTO, A.FRAME, B.ANCHO, C.NOMBRECORTO, B.CLASSNAME1, B.JAVASCRIPT from MENUOPCION A inner join OPCIONES B on A.CLAVEOPCION = B.CLAVEOPCION inner join MENUS C on A.CLAVEMENU = C.CLAVEMENU inner join PERFILMENU D on A.CLAVEMENU = D.CLAVEMENU and D.CLAVEPERFIL = ? where A.CLAVEMENU = ? order by A.ORDEN";
//	private static String opcionesxMenu = "select A.CLAVEMENU, A.CLAVEOPCION, B.NOMBRECORTO, B.DESCRIPCION, B.URL, B.ALTO, A.FRAME, B.ANCHO, C.NOMBRECORTO, B.CLASSNAME1 from MENUOPCION A inner join OPCIONES B on A.CLAVEOPCION = B.CLAVEOPCION inner join MENUS C on A.CLAVEMENU = C.CLAVEMENU inner join OPCION_USUARIO D on B.CLAVEOPCION = D.CLAVEOPCION and D.ID_USUARIO = ? where A.CLAVEMENU = ? order by A.ORDEN";
	
	private static String opcionesxMenuOpcion = "select A.CLAVEMENU, A.CLAVEOPCION, B.NOMBRECORTO, B.DESCRIPCION, B.URL, B.ALTO, A.FRAME, B.ANCHO, C.NOMBRECORTO from MENUOPCION A inner join OPCIONES B on A.CLAVEOPCION = B.CLAVEOPCION inner join MENUS C on A.CLAVEMENU = C.CLAVEMENU  where A.CLAVEMENU = ? and A.CLAVEOPCION = ? order by A.ORDEN";	

	private static String tienePermiso = "select CLAVEREGISTRO from PERFILMENU where CLAVEPERFIL = ? and CLAVEMENU = ?";

	

	private static String detalleMenus       = "select CLAVEMENU, NOMBRECORTO, DESCRIPCION, IMAGEN, TIPO_MENU, JAVASCRIPT, classname1, classname2, classname3 from MENUS";
	private static String menusDisponibles   = "select CLAVEMENU, DESCRIPCION from MENUS where CLAVEMENU not in (select CLAVEMENU from MENU_USUARIO where ID_USUARIO = ?) ";
	private static String menusAsignados     = "select CLAVEMENU, DESCRIPCION from MENUS where CLAVEMENU in (select CLAVEMENU from MENU_USUARIO where ID_USUARIO = ?) ";
	
	
	private static String menusDisponiblesPerfil   = "select CLAVEMENU, NOMBRECORTO from MENUS where CLAVEMENU not in (select CLAVEMENU from PERFILMENU where CLAVEPERFIL = ?) ";
	private static String menusAsignadosPerfil   = "select CLAVEMENU, NOMBRECORTO from MENUS where CLAVEMENU in (select CLAVEMENU from PERFILMENU where CLAVEPERFIL = ?) ";

	private static String comboMenus       = "select A.CLAVEMENU, NOMBRECORTO, DESCRIPCION from MENUS A inner join PERFILMENU B on A.CLAVEMENU = B.CLAVEMENU where B.CLAVEPERFIL = ?";	

	
// configuracion de opciones por usuario
	private static String opcionesDisponiblesUsuario   = "select A.CLAVEOPCION, A.DESCRIPCION from OPCIONES A inner join MENUOPCION B on A.CLAVEOPCION = B.CLAVEOPCION where B.CLAVEMENU = ? and  A.CLAVEOPCION not in (select CLAVEOPCION from OPCION_USUARIO where ID_USUARIO = ?)";
	private static String opcionesAsignadosUsuario     = "select A.CLAVEOPCION, A.DESCRIPCION from OPCIONES A inner join MENUOPCION B on A.CLAVEOPCION = B.CLAVEOPCION where B.CLAVEMENU = ? and  A.CLAVEOPCION in (select CLAVEOPCION from OPCION_USUARIO where ID_USUARIO = ?)";

	private static String guardarOpcionAsignadosUsuario     = "insert into OPCION_USUARIO (ID_USUARIO, CLAVEOPCION, ORDEN) values (?,?,?)";
	private static String eliminarOpcionAsignadosUsuario     = "delete from OPCION_USUARIO where ID_USUARIO = ? and CLAVEOPCION = ?";
	
	
	public static String getDetalleMenus(String esquema) {
		return detalleMenus.replaceAll("<<esquema>>", esquema);
	}

	public static String getMenusDisponibles(String esquema) {
		return menusDisponibles.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getComboMenus(String esquema) {
		return comboMenus.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getMenusAsignados(String esquema) {
		return menusAsignados.replaceAll("<<esquema>>", esquema);
	}

	public static String getMenusDisponiblesPerfil(String esquema) {
		return menusDisponiblesPerfil.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getMenusAsignadosPerfil(String esquema) {
		return menusAsignadosPerfil.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getOpcionesDisponiblesUsuario(String esquema) {
		return opcionesDisponiblesUsuario.replaceAll("<<esquema>>", esquema);
	}

	public static String getOpcionesAsignadosUsuario(String esquema) {
		return opcionesAsignadosUsuario.replaceAll("<<esquema>>", esquema);
	}


	public static String getGuardarOpcionAsignadosUsuario(String esquema) {
		return guardarOpcionAsignadosUsuario.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminarOpcionAsignadosUsuario(String esquema) {
		return eliminarOpcionAsignadosUsuario.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getQueryOpciones(String esquema) {
		return queryOpciones.replaceAll("<<esquema>>", esquema);
	}

	public static String getMenusPrincipal(String esquema) {
		return menusPrincipal.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getOpcionesxMenu(String esquema) {
		return opcionesxMenu.replaceAll("<<esquema>>", esquema);
	}

	public static String getOpcionesxMenuOpcion(String esquema) {
		return opcionesxMenuOpcion.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getTienePermiso(String esquema) {
		return tienePermiso.replaceAll("<<esquema>>", esquema);
	}
	
}
