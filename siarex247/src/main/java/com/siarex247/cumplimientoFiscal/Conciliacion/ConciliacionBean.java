package com.siarex247.cumplimientoFiscal.Conciliacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class ConciliacionBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleConsiliados(Connection con, String esquema, int anio, String fechaIni, String fechaFin, String razonSocial, int claveProveedor, String tipoComple)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        StringBuffer sbQuery = new StringBuffer();
        try{
        	
            if ("CON_SIN_COMPLE".equalsIgnoreCase(tipoComple)) {
            	sbQuery.append(ConciliacionQuerys.getDetalleConSinComple(esquema));
            }else if ("CON_COMPLE".equalsIgnoreCase(tipoComple)) {
            	sbQuery.append(ConciliacionQuerys.getDetalleConComple(esquema));
            }else {
            	sbQuery.append(ConciliacionQuerys.getDetalleSinComple(esquema));
            }
            
        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQuery.append(" and P.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
        	}
        	
        	if (claveProveedor  > 0  && sbQuery.indexOf("where") > -1 ){
        		sbQuery.append(" and P.CLAVE_PROVEEDOR = ? ");
        	}else {
        		if (claveProveedor  > 0 ){
        			sbQuery.append(" where P.CLAVE_PROVEEDOR = ? ");
        		}
        		
        	}
        	
        	sbQuery.append(" order by A.CLAVE_PROVEEDOR, A.FECHAULTMOV ");
        	
       		stmt = con.prepareStatement( sbQuery.toString());
       		
       		int numParam=1;
       		
       		if ("CON_SIN_COMPLE".equalsIgnoreCase(tipoComple)) {
       			stmt.setString(numParam++, "A4");
       			stmt.setString(numParam++, "A6");
            }else if ("CON_COMPLE".equalsIgnoreCase(tipoComple)) {
            	stmt.setString(numParam++, "A6");
            }else {
            	stmt.setString(numParam++, "A4");
            }
       		
            stmt.setString(numParam++, fechaIni);
            stmt.setString(numParam++, fechaFin);
            stmt.setString(numParam++, "MEX");
            
            
            if (claveProveedor > 0){
            	stmt.setInt(5, claveProveedor);
            	//cont++;
        	}
            
            logger.info("stmt==>"+stmt);
            rs = stmt.executeQuery();
            
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            String tipoOrden = null;
            // final String TMBCS = "TMBCS";
            // final String TMBCA = "TMBCA";

			String TMBCS = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "LABEL_LAYOUT_ORDEN");
			String TMBCA = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "LABEL_LAYOUT_MULTIPLE");
			
			

			while(rs.next())
            {
				
					tipoOrden = Utils.noNulo(rs.getString(24));
					
					if (tipoOrden.length() > 1 && 
							tipoOrden.substring(0, 1).equals("M")){
						jsonobj.put("BUSINESS_UNIT",TMBCS);
					}else{
						jsonobj.put("BUSINESS_UNIT", TMBCA);
					}
					jsonobj.put("RFC",Utils.noNulo(rs.getString(1)));
					
					jsonobj.put("RAZON_SOCIAL",Utils.regresaCaracteresNormales( Utils.noNuloNormal(rs.getString(2))));
					
					
					jsonobj.put("FOLIO_ORDEN",rs.getLong(3));
					jsonobj.put("FOLIO_EMPRESA",rs.getLong(4));
					jsonobj.put("TIPO_MONEDA",Utils.noNulo(rs.getString(5)));
					jsonobj.put("SERIE_FOLIO",Utils.noNulo(rs.getString(6)));
					
					jsonobj.put("FECHAPAGO",UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(7)));
					jsonobj.put("FECHAPAGO_XML",rs.getString(8));
					jsonobj.put("FECHATIMBRADO",rs.getString(9));
					
					jsonobj.put("UUID_ORDEN",Utils.noNuloNormal(rs.getString(10)));
					jsonobj.put("SUB_TOTAL",Utils.noNulo(decimal.format(rs.getDouble(11))));
					jsonobj.put("IVA",Utils.noNulo(decimal.format(rs.getDouble(12))));
					jsonobj.put("IVA_RET",Utils.noNulo(decimal.format(rs.getDouble(13))));
					jsonobj.put("IMP_LOCALES",Utils.noNulo(decimal.format(rs.getDouble(14))));
					jsonobj.put("TOTAL",Utils.noNulo(decimal.format(rs.getDouble(15))));
					
					jsonobj.put("UUID_COMPLEMENTO",Utils.noNuloNormal(rs.getString(16)));
					jsonobj.put("TOTAL_FACTURA",Utils.noNulo(decimal.format(rs.getDouble(17))));
					jsonobj.put("TOTAL_COMPLEMENTO",Utils.noNulo(decimal.format(rs.getDouble(18))));
					jsonobj.put("ESTATUS_FACTURA",Utils.noNuloNormal(rs.getString(19)));
					jsonobj.put("ESTATUS_COMPLEMENTO",Utils.noNuloNormal(rs.getString(20)));
					jsonobj.put("NOMBRE_XML",Utils.noNuloNormal(rs.getString(21)));
					jsonobj.put("NOMBRE_PDF",Utils.noNuloNormal(rs.getString(22)));
					jsonobj.put("ESTATUS_CONCILIACION",getEstatus(Utils.noNuloNormal(rs.getString(23))));
					jsonobj.put("ESTATUS_CONCILIACION_EXCEL",getEstatusExcel(Utils.noNuloNormal(rs.getString(23))));
					jsonobj.put("CLAVE_PROVEEDOR",Utils.noNulo(rs.getString(24)));
					
					jsonArray.add(jsonobj);
					jsonobj = new JSONObject();
				
				
            }
			mapaRes.put("detalle", jsonArray);
        }catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return mapaRes;
    }
	
	
	
	public ArrayList<ConciliacionForm> detalleExportarZIP(Connection con, String esquema, String fechaIni, String fechaFin, String razonSocial,
			int claveProveedor, String tipoComple, String idRegistro)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ConciliacionForm complementoForm = new ConciliacionForm();
        ArrayList<ConciliacionForm> lista = new ArrayList<ConciliacionForm>();
        StringBuffer sbQuery = new StringBuffer();
        try{

           	sbQuery.append(ConciliacionQuerys.getExportarZipConComple(esquema));
            

           	if (!"0".equalsIgnoreCase(idRegistro)) {
           		String cadFolios [] = idRegistro.split(";");
           		StringBuffer sbIn = new StringBuffer(" and C.FOLIO_EMPRESA in (");
           		for (int z = 0; z < cadFolios.length; z++) {
           			sbIn.append("?,");
           		}
           		sbQuery.append(sbIn.toString().substring(0, sbIn.length() - 1));
           		sbQuery.append(") ");
           		sbIn.setLength(0);
           		sbIn = null;
           	}
           	
           	
           	
        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQuery.append(" and P.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
        	}
        	
        	if (claveProveedor  > 0  && sbQuery.indexOf("where") > -1 ){
        		sbQuery.append(" and P.CLAVE_PROVEEDOR = ? ");
        	}else {
        		if (claveProveedor  > 0 ){
        			sbQuery.append(" where P.CLAVE_PROVEEDOR = ? ");
        		}
        		
        	}
        	
        	sbQuery.append(" group by UUID_COMPLEMENTO ");
        	
            stmt = con.prepareStatement( sbQuery.toString());
            stmt.setString(1, "A4");
            stmt.setString(2, fechaIni);
            stmt.setString(3, fechaFin);
            stmt.setString(4, "MEX");
            
            int contador = 5;
            if (!"0".equalsIgnoreCase(idRegistro)) {
            	String cadFolios [] = idRegistro.split(";");
           		for (int z = 0; z < cadFolios.length; z++) {
           			stmt.setString(contador++, cadFolios[z]);
           		}
           	}
            
            
            if (claveProveedor > 0){
            	stmt.setInt(contador++, claveProveedor);
        	}
            
            logger.info("stmtDescarga===>"+stmt);
            rs = stmt.executeQuery();
			while(rs.next()){
					complementoForm.setNombreXML(Utils.noNuloNormal(rs.getString(1)));
					complementoForm.setNombrePDF(Utils.noNuloNormal(rs.getString(2)));
					complementoForm.setClaveProveedor(rs.getInt(3));
					lista.add(complementoForm);
					complementoForm = new ConciliacionForm();
            }
        }catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return lista;
    }
	
	
	
	private String getEstatus(String estatusCon) {
		if ("OK".equalsIgnoreCase(estatusCon)) {
			return "CONCILIACI&Oacute;N EXITOSA.";
		}else if ( "NG".equalsIgnoreCase(estatusCon) ) {
			return "ERROR EN CONCILIACI&Oacute;N.";
		}else {
			return "SIN COMPLEMENTO.";	
		}
		
	}

	
	private String getEstatusExcel(String estatusCon) {
		if ("OK".equalsIgnoreCase(estatusCon)) {
			return "CONCILIACIÓN EXITOSA.";
		}else if ( "NG".equalsIgnoreCase(estatusCon) ) {
			return "ERROR EN CONCILIACIÓN.";
		}else {
			return "SIN COMPLEMENTO.";	
		}
		
	}

	
}
