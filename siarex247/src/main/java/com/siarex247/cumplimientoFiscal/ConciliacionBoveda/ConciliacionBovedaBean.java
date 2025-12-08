package com.siarex247.cumplimientoFiscal.ConciliacionBoveda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.cumplimientoFiscal.Conciliacion.ConciliacionQuerys;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class ConciliacionBovedaBean {

	
	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	public ArrayList<ConciliacionBovedaForm> detalleConsiliados(Connection con, String esquema, String fechaIni, String fechaFin, String tipoComple)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ConciliacionBovedaForm> listaDetalle = new ArrayList<>();
        ConciliacionBovedaForm conciForm = new ConciliacionBovedaForm();
        StringBuffer sbQuery = new StringBuffer();
        try{
        	
        	if ("CON_SIN_COMPLE".equalsIgnoreCase(tipoComple)) {
            	sbQuery.append(ConciliacionBovedaQuerys.getDetalleConciliacion_ConSinComplemento(esquema));
            }else if ("CON_COMPLE".equalsIgnoreCase(tipoComple)) {
            	sbQuery.append(ConciliacionBovedaQuerys.getDetalleConciliacion_Complemento(esquema));
            }else {
            	sbQuery.append(ConciliacionBovedaQuerys.getDetalleConciliacion_SinComplemento(esquema));
            }
            
        	/*
        	logger.info("anio===>"+anio);
        	logger.info("fechaIni===>"+fechaIni);
        	logger.info("fechaFin===>"+fechaFin);
        	logger.info("tipoComple===>"+tipoComple);
        	*/
        	
            //sbQuery.append(ConciliacionBovedaQuerys.getDetalleConciliacion(esquema));
        	sbQuery.append(" order by A.EMISOR_RFC, C.UUID_FACTURA ");
        	
       		stmt = con.prepareStatement( sbQuery.toString());	
            
       		stmt.setString(1, fechaIni);
       		stmt.setString(2, fechaFin);
       		
       		logger.info("stmtConciliados===>"+stmt);
       		
       		rs = stmt.executeQuery();
            
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            Timestamp fechaPago = null;
			while(rs.next())  {
				conciForm.setIdRegistro(rs.getInt(1));
				conciForm.setRfc(Utils.noNulo(rs.getString(2)));
				conciForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));	
				conciForm.setTipoMoneda(Utils.noNulo(rs.getString(4)));
				conciForm.setSerieFolio(Utils.noNulo(rs.getString(5)));
				fechaPago = rs.getTimestamp(6);
				if (fechaPago == null) {
					conciForm.setFechaPago("");
				}else {
					conciForm.setFechaPago(Utils.noNulo(rs.getString(6)));
				}
				
				conciForm.setFechaTimbrado(Utils.noNulo(rs.getString(7)));
				conciForm.setUuidOrden(Utils.noNulo(rs.getString(8)));
				conciForm.setSubTotal(Utils.noNulo(decimal.format(rs.getDouble(9))));
				conciForm.setIva(Utils.noNulo(decimal.format(rs.getDouble(10))));
				conciForm.setIvaRet(Utils.noNulo(decimal.format(rs.getDouble(11))));
				conciForm.setImpLocales("0");
				conciForm.setTotal(Utils.noNulo(decimal.format(rs.getDouble(12))));
				conciForm.setUuidComplemento(Utils.noNulo(rs.getString(13)));
				conciForm.setTotalFactura(Utils.noNulo(decimal.format(rs.getDouble(14))));
				conciForm.setTotalComplemento(Utils.noNulo(decimal.format(rs.getDouble(15))));
				conciForm.setEstatusComplemento(Utils.noNuloNormal(rs.getString(16)));
				// logger.info("NombreXML==>"+Utils.noNuloNormal(rs.getString(17)));
				conciForm.setNombreXML(Utils.noNuloNormal(rs.getString(17)));
				conciForm.setNombrePDF(Utils.noNuloNormal(rs.getString(18)));
				conciForm.setEstatusConciliacion(getEstatus(Utils.noNuloNormal(rs.getString(19))));
				conciForm.setFechaFactura(Utils.noNulo(rs.getString(20)));
				listaDetalle.add(conciForm);
				conciForm = new ConciliacionBovedaForm();
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
        return listaDetalle;
    }
	
	
	
	
	 @SuppressWarnings("unchecked")
		public JSONObject consultaBovedaRegistro(Connection con, String esquema, int idRegistro)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        try{
	        	
	        	StringBuffer sbQuery = new StringBuffer(ConciliacionBovedaQuerys.getConsultaBovedaRegistro(esquema));
	        	stmt = con.prepareStatement(sbQuery.toString());
	        	stmt.setInt(1, idRegistro);
	        	rs = stmt.executeQuery();
	            DecimalFormat decimal = new DecimalFormat("###,###.##");
	            if(rs.next()){
					
						jsonobj.put("ID_REGISTRO", rs.getInt(1));
						jsonobj.put("UUID", Utils.noNuloNormal(rs.getString(2)));
						jsonobj.put("RFC", Utils.noNulo(rs.getString(15)));
						jsonobj.put("RAZON_SOCIAL", Utils.noNulo(rs.getString(16)));
						jsonobj.put("SERIE", Utils.noNulo(rs.getString(3)));
						jsonobj.put("FOLIO", Utils.noNulo(rs.getString(4)));
						jsonobj.put("TOTAL", decimal.format(rs.getDouble(14)));
						jsonobj.put("SUB-TOTAL", decimal.format(rs.getDouble(10)));
						jsonobj.put("IVA", decimal.format(rs.getDouble(13)));
						jsonobj.put("ISR RET", decimal.format(rs.getDouble(21)));
						jsonobj.put("IMP_LOCALES", 0);
						jsonobj.put("XML", Utils.noNuloNormal(rs.getString(23)));
						jsonobj.put("FECHA_FACTURA", Utils.noNulo(rs.getString(5)));
						jsonobj.put("TIPO_COMPROBANTE", Utils.noNulo(rs.getString(24)));
	            }
	        }
	        catch(Exception e){
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
	        return jsonobj;
	    }
	 
	 
	 
	 @SuppressWarnings("unchecked")
		public JSONObject consultaBovedaUUID(Connection con, String esquema, String uuid)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        try{
	        	
	        	StringBuffer sbQuery = new StringBuffer(ConciliacionBovedaQuerys.getConsultaBovedaUUID(esquema));
	        	stmt = con.prepareStatement(sbQuery.toString());
	        	stmt.setString(1, uuid);
	        	rs = stmt.executeQuery();
	            DecimalFormat decimal = new DecimalFormat("###,###.##");
	            if(rs.next()){
					
						jsonobj.put("ID_REGISTRO", rs.getInt(1));
						jsonobj.put("UUID", Utils.noNuloNormal(rs.getString(2)));
						jsonobj.put("RFC", Utils.noNulo(rs.getString(15)));
						jsonobj.put("RAZON_SOCIAL", Utils.noNulo(rs.getString(16)));
						jsonobj.put("SERIE", Utils.noNulo(rs.getString(3)));
						jsonobj.put("FOLIO", Utils.noNulo(rs.getString(4)));
						jsonobj.put("TOTAL", decimal.format(rs.getDouble(14)));
						jsonobj.put("SUB-TOTAL", decimal.format(rs.getDouble(10)));
						jsonobj.put("IVA", decimal.format(rs.getDouble(13)));
						jsonobj.put("ISR RET", decimal.format(rs.getDouble(21)));
						jsonobj.put("IMP_LOCALES", 0);
						jsonobj.put("XML", Utils.noNuloNormal(rs.getString(23)));
						jsonobj.put("FECHA_FACTURA", Utils.noNulo(rs.getString(5)));
						jsonobj.put("TIPO_COMPROBANTE", Utils.noNulo(rs.getString(24)));
	            }
	        }
	        catch(Exception e){
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
	        return jsonobj;
	    }
	 
	 

	private String getEstatus(String estatusCon) {
		if ("OK".equalsIgnoreCase(estatusCon)) {
			return "CONCILIACIÓN EXITOSA.";
		}else if ( "NG".equalsIgnoreCase(estatusCon) ) {
			return "ERROR EN CONCILIACIÓN.";
		}else {
			return "SIN COMPLEMENTO.";	
		}
		
	}

	
}
