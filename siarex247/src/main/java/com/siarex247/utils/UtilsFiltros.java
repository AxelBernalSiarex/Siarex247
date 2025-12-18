package com.siarex247.utils;

import org.apache.log4j.Logger;

public class UtilsFiltros {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public static String armarFiltro(String nombreFiltro, String operadorFiltro, String valorInicial, String condicionInicial){
		StringBuffer queryFiltro = new StringBuffer();
		try{
			
			logger.info("nombreFiltro===>"+nombreFiltro+", operadorFiltro==>"+operadorFiltro);
			if ("RFC".equalsIgnoreCase(nombreFiltro)) {
    			if ("IGUAL_A".equalsIgnoreCase(operadorFiltro)) {
    				queryFiltro.append(condicionInicial).append(" EMISOR_RFC = ").append("'").append(valorInicial).append("'");
    				condicionInicial = " and ";
    				
				}else if ("DIFERENTE_DE".equalsIgnoreCase(operadorFiltro)) {
					queryFiltro.append(condicionInicial).append(" EMISOR_RFC not in ( ").append("'").append(valorInicial).append("')");
    				condicionInicial = " and ";
    				
				}else if ("INICIA_CON".equalsIgnoreCase(operadorFiltro)) {
					queryFiltro.append(condicionInicial).append(" EMISOR_RFC like '").append(valorInicial).append("%' ");
					condicionInicial = " and ";
					
				}else if ("CONTIENE".equalsIgnoreCase(operadorFiltro)) {
					queryFiltro.append(condicionInicial).append(" EMISOR_RFC in ('").append(valorInicial).append("') ");
					condicionInicial = " and ";
					
				}else if ("NO_CONTIENE".equalsIgnoreCase(operadorFiltro)) {
					queryFiltro.append(condicionInicial).append(" EMISOR_RFC not in ('").append(valorInicial).append("') ");
					condicionInicial = " and ";
					
				}else if ("TERMINA_CON".equalsIgnoreCase(operadorFiltro)) {
					queryFiltro.append(condicionInicial).append(" EMISOR_RFC like '%").append(valorInicial).append("' ");
					condicionInicial = " and ";
					
				}
    			
			}
			
			
		}catch(Exception e){
			
		}
		return queryFiltro.toString();
	}
	
	
	
}
