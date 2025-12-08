package com.siarex247.cumplimientoFiscal.ConciliacionCP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class ConciliacionCPBean {

	
	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleCartaPorte(Connection con, String esquema, String fechaIni, String fechaFin, String razonSocial, int claveProveedor, String tipoComple) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        StringBuffer sbQuery = new StringBuffer();
        int siguiente = 1;

        try{
            if ("CON_SIN_COMPLE".equalsIgnoreCase(tipoComple)) {
            	sbQuery.append(ConciliacionCPQuerys.getDetalleConSinCP(esquema));
            }
            else if ("CON_COMPLE".equalsIgnoreCase(tipoComple)) {
            	sbQuery.append(ConciliacionCPQuerys.getDetalleConCP(esquema));
            }
            else {
            	sbQuery.append(ConciliacionCPQuerys.getDetalleSinCP(esquema));
            }
            
        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQuery.append(" and P.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
        	}
        	
        	if (claveProveedor  > 0  && sbQuery.indexOf("where") > -1 ){
        		sbQuery.append(" and P.CLAVE_PROVEEDOR = ? ");
        	}
        	else {
        		if (claveProveedor  > 0 ){
        			sbQuery.append(" where P.CLAVE_PROVEEDOR = ? ");
        		}
        	}
        	
        	sbQuery.append(" order by A.CLAVE_PROVEEDOR, A.FECHAULTMOV ");
       		stmt = con.prepareStatement( sbQuery.toString());	
        	if ("CON_SIN_COMPLE".equalsIgnoreCase(tipoComple)) {
	        	stmt.setString(siguiente++, "S");
	        	stmt.setString(siguiente++, "A3");
	        	stmt.setString(siguiente++, "A4");
	        	stmt.setString(siguiente++, fechaIni);
	            stmt.setString(siguiente++, fechaFin);
        	}
        	else if ("CON_COMPLE".equalsIgnoreCase(tipoComple)) {
        		stmt.setString(siguiente++, "A3");
            	stmt.setString(siguiente++, "A4");
            	stmt.setString(siguiente++, fechaIni);
                stmt.setString(siguiente++, fechaFin);
        	}
        	else {
        		stmt.setString(siguiente++, "A3");
            	stmt.setString(siguiente++, "A4");
            	stmt.setString(siguiente++, fechaIni);
                stmt.setString(siguiente++, fechaFin);
        	}
           
            if (claveProveedor > 0){
            	stmt.setInt(siguiente++, claveProveedor);
        	}

           // logger.info("stmt ........... " + stmt );
            
            rs = stmt.executeQuery();
            
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            
			while(rs.next()) {
					jsonobj.put("RFC",Utils.noNulo(rs.getString(1)));
					jsonobj.put("RAZON_SOCIAL",Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(2))));	
					jsonobj.put("FOLIO_ORDEN",rs.getLong(3));
					jsonobj.put("FOLIO_EMPRESA",rs.getLong(4));
					jsonobj.put("TIPO_MONEDA",Utils.noNulo(rs.getString(5)));
					jsonobj.put("SERIE_FOLIO",Utils.noNulo(rs.getString(6)));
					
					jsonobj.put("FECHAPAGO",UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(7)));
					jsonobj.put("FECHAPAGO_XML",rs.getString(8));
					jsonobj.put("FECHATIMBRADO",rs.getString(9));
					
					jsonobj.put("UUID_ORDEN",Utils.noNulo(rs.getString(10)));
					jsonobj.put("SUB_TOTAL",Utils.noNulo(decimal.format(rs.getDouble(11))));
					jsonobj.put("IVA",Utils.noNulo(decimal.format(rs.getDouble(12))));
					jsonobj.put("IVA_RET",Utils.noNulo(decimal.format(rs.getDouble(13))));
					jsonobj.put("IMP_LOCALES",Utils.noNulo(decimal.format(rs.getDouble(14))));
					jsonobj.put("TOTAL",Utils.noNulo(decimal.format(rs.getDouble(15))));

					jsonobj.put("UUID_CARTA_PORTE",Utils.noNulo(rs.getString(16)));
					jsonobj.put("TOTAL_FACTURA",Utils.noNulo(decimal.format(rs.getDouble(17))));
					jsonobj.put("TOTAL_CARTA_PORTE",0);
					jsonobj.put("ESTATUS_FACTURA",Utils.noNuloNormal(rs.getString(18)));
					jsonobj.put("ESTATUS_CARTA_PORTE",Utils.noNuloNormal(rs.getString(19)));
					jsonobj.put("NOMBRE_XML",Utils.noNuloNormal(rs.getString(20)));
					jsonobj.put("NOMBRE_PDF",Utils.noNuloNormal(rs.getString(21)));
					jsonobj.put("ESTATUS_CONCILIACION",getEstatus(Utils.noNuloNormal(rs.getString(22))));
					jsonobj.put("ESTATUS_CONCILIACION_EXCEL",getEstatusExcel(Utils.noNuloNormal(rs.getString(22))));
					jsonobj.put("CLAVE_PROVEEDOR",Utils.noNulo(rs.getString(23)));
					jsonArray.add(jsonobj);
					jsonobj = new JSONObject();
				
            }
			mapaRes.put("detalle", jsonArray);
        }
        catch(Exception e){
        	Utils.imprimeLog("detalleCartaPorte(): ", e);
        }
        finally{
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
	
	
	@SuppressWarnings("unchecked")
	public JSONObject buscarDocumentoFolioEmpresa(Connection con, String esquema, long folioOrden){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        
        try{
        	stmt = con.prepareStatement(ConciliacionCPQuerys.getBuscarDocumentosFolioEmpresa(esquema));
            stmt.setLong(1, folioOrden);
            //logger.info("stmt==>"+stmt);
            rs = stmt.executeQuery();
            String estatus = null;

			if (rs.next()){
				estatus = Utils.noNulo(rs.getString(3));
				if ("OK".equalsIgnoreCase(estatus)) {
					jsonobj.put("NOMBRE_XML",Utils.noNuloNormal(rs.getString(1)));
					jsonobj.put("NOMBRE_PDF",Utils.noNuloNormal(rs.getString(2)));	
					jsonobj.put("UUID_COMPLEMENTO",Utils.noNuloNormal(rs.getString(4)));
				}
            }
        }
        catch(Exception e){
        	Utils.imprimeLog("buscarDocumentoFolioEmpresa(): ", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return jsonobj;
    }
	
	
	private String getEstatus(String estatusCon) {
		if ("OK".equalsIgnoreCase(estatusCon)) {
			return "CONCILIACI&Oacute;N EXITOSA.";
		}
		else if ( "NG".equalsIgnoreCase(estatusCon) ) {
			return "ERROR EN CONCILIACI&Oacute;N.";
		}
		else {
			return "SIN COMPLEMENTO.";	
		}
	}
	
	private String getEstatusExcel(String estatusCon) {
		if ("OK".equalsIgnoreCase(estatusCon)) {
			return "CONCILIACIÓN EXITOSA.";
		}
		else if ( "NG".equalsIgnoreCase(estatusCon) ) {
			return "ERROR EN CONCILIACIÓN.";
		}
		else {
			return "SIN COMPLEMENTO.";	
		}
	}

}
