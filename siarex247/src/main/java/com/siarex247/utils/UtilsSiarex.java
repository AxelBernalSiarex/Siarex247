package com.siarex247.utils;

import java.util.HashMap;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;

public class UtilsSiarex {

	
	
	public static String desEstatus(String claveEstatus, String lenguaje){
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(lenguaje, "VISOR");
		
		if ("A1".equalsIgnoreCase(claveEstatus)){
			// return "SERVICIO SIN RECIBO Y CON FACTURA";
			 return Utils.noNuloNormal(mapaLenguaje.get("ETQ6"));
		}
		if ("A2".equalsIgnoreCase(claveEstatus)){
			// return "SERVICIO CON RECIBO Y SIN FACTURA";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ7"));
		}
		if ("A3".equalsIgnoreCase(claveEstatus)){
			// return "LISTO PARA PAGO";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ8"));
		}
		if ("A4".equalsIgnoreCase(claveEstatus)){
			// return "PAGADO";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ9"));
		}
		if ("A5".equalsIgnoreCase(claveEstatus)){
			// return "SERVICIO SIN RECIBO Y SIN FACTURA";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ10"));
		}
		if ("A6".equalsIgnoreCase(claveEstatus)){
			// return "SERVICIO SIN RECIBO Y SIN FACTURA";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ105"));
		}
		if ("A9".equalsIgnoreCase(claveEstatus)){
			// return "BAJO VALIDACION";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ101"));
		}
		if ("A10".equalsIgnoreCase(claveEstatus)){
			// return "VALIDAR CLAVES CFDI";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ102"));
		}
		if ("A11".equalsIgnoreCase(claveEstatus)){
			//return "BAJO VALIDACION NOTA DE CREDITO";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ103"));
		}
		if ("A12".equalsIgnoreCase(claveEstatus)){
			//return "VALIDAR CLAVES CFDI CARTA PORTE";
			return Utils.noNuloNormal(mapaLenguaje.get("ETQ104"));
		}
	   return "";	
	}
	
	
}
