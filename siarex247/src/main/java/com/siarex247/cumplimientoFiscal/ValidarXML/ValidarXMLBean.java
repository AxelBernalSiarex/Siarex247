package com.siarex247.cumplimientoFiscal.ValidarXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;

public class ValidarXMLBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleXML(Connection con, String esquema, String usuarioHTTP, String tipoGrupo)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        int totReg = 0;
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        JSONArray jsonArrayTotales = new JSONArray();
        JSONObject jsonobjTotal = new JSONObject();
        
        StringBuffer sbQuery = null;
        try
        {
        	
        	if ("GPO_IDV".equalsIgnoreCase(tipoGrupo)){
        		sbQuery = new StringBuffer(ValidarXMLQuerys.getQueryDetalleXMLInd(esquema));
        	}else{
        		sbQuery = new StringBuffer(ValidarXMLQuerys.getQueryDetalleXMLGpo(esquema));
        	}

            stmt = con.prepareStatement( sbQuery.toString());
            stmt.setString(1, usuarioHTTP);
            
            rs = stmt.executeQuery();
            logger.info("Detalle Xml -> "+stmt);
            
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            String rfcProveedor = null;
            String rfcProveedorTemp = null;
            int x = 0;
            int rowsPanFin = 1;
            int rowsPanIni = 0;
            HashMap<String, Double> mapaTotales = sumaTotales(con, esquema, usuarioHTTP);
			while(rs.next()) 
            {
				rfcProveedor = Utils.noNulo(rs.getString(1));
				if (rfcProveedorTemp != null && !rfcProveedor.equalsIgnoreCase(rfcProveedorTemp) ){
					//rowsPanIni = x - 1;
					jsonobjTotal.put("index",rowsPanIni);
					jsonobjTotal.put("rowspan",rowsPanFin-1);
					jsonArrayTotales.add(jsonobjTotal); 
		        	jsonobjTotal = new JSONObject();
		        	rowsPanFin = 1;
		        	rowsPanIni = x;
				}
				
				jsonobj.put("RFC",rfcProveedor);
				jsonobj.put("RAZON_SOCIAL",Utils.noNulo(rs.getString(2)));
				jsonobj.put("SUBTOTAL",decimal.format(rs.getDouble(3)));
				jsonobj.put("TOTAL",decimal.format(rs.getDouble(4)));
				jsonobj.put("GRAND_TOTAL",decimal.format(mapaTotales.get(rfcProveedor)));
				jsonobj.put("TIPO_MONEDA",Utils.noNulo(rs.getString(5)));
				jsonobj.put("ESTADO_SAT",Utils.noNuloNormal(rs.getString(6)));
				jsonobj.put("ESTATUS_SAT", Utils.noNuloNormal(rs.getString(7)));
				jsonobj.put("SERIE", Utils.noNuloNormal(rs.getString(8)));
				jsonobj.put("FOLIO", Utils.noNuloNormal(rs.getString(9)));
				jsonobj.put("TIPO_COMPROBANTE", Utils.noNulo(rs.getString(10))); // Nuevo índice (basado en detalleXMLInd modificado)
	        	jsonArray.add(jsonobj);  
	        	jsonobj = new JSONObject();
	        	
	        	rfcProveedorTemp = rfcProveedor;
	        	x++;
	        	rowsPanFin++;
            }
			
			jsonobjTotal.put("index",rowsPanIni);
			jsonobjTotal.put("rowspan",rowsPanFin -1);
			jsonArrayTotales.add(jsonobjTotal); 
        	jsonobjTotal = new JSONObject();
        	rowsPanFin = 1;
        	
			mapaRes.put("detalle", jsonArray);
	        mapaRes.put("totReg", totReg);
	        mapaRes.put("granTotal", jsonArrayTotales);
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
        return mapaRes;
    }
	
	
	
	public HashMap<String, Double> sumaTotales(Connection con, String esquema, String usuarioHTTP)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, Double> mapaTotales = new HashMap<String, Double>();
        try
        {
        	StringBuffer sbQuery = new StringBuffer(ValidarXMLQuerys.getQuerySumaTotales(esquema));
            stmt = con.prepareStatement( sbQuery.toString());
            stmt.setString(1, usuarioHTTP);
            rs = stmt.executeQuery();

			while(rs.next()) 
            {
				mapaTotales.put(Utils.noNulo(rs.getString(1)), rs.getDouble(2));
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
        return mapaTotales;
    }
	
	
	
	
	public int eliminaXML(Connection con, String esquema, String idEmpleado)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(ValidarXMLQuerys.getQueryEliminarXML(esquema));
        	stmt.setString(1, idEmpleado);
        	resultado = stmt.executeUpdate();
        }
        catch(SQLException sql){
        	resultado = sql.getErrorCode();
        	Utils.imprimeLog("eliminaXML ", sql);
            
        }catch(Exception e){
        	resultado = 100;
            Utils.imprimeLog("eliminaXML 2 ", e);
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
        return resultado;
    }
	
	
	
	public int altaXML(Connection con, String esquema, ValidarXMLForm cargasXMLForm, String idEmpleado)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(ValidarXMLQuerys.getQueryAgregaXML(esquema));
        	stmt.setString(1, cargasXMLForm.getUuid());
        	stmt.setString(2, cargasXMLForm.getSerie());
        	stmt.setString(3, cargasXMLForm.getFolio());
        	stmt.setString(4, cargasXMLForm.getFechaFactura());
        	stmt.setString(5, cargasXMLForm.getFormaPago());
        	stmt.setString(6, cargasXMLForm.getMetodoPago());
        	stmt.setString(7, cargasXMLForm.getTipoMoneda());
        	stmt.setString(8, cargasXMLForm.getDesTipoMoneda());
        	stmt.setString(9, cargasXMLForm.getNumeroCuentaPago());
        	stmt.setDouble(10, cargasXMLForm.getSubTotal());
        	stmt.setDouble(11, cargasXMLForm.getDescuento());
        	stmt.setDouble(12, cargasXMLForm.getTotalImpuestoRet());
        	stmt.setDouble(13, cargasXMLForm.getTotalImpuestoTranslado());
        	stmt.setDouble(14, cargasXMLForm.getTotal());
        	stmt.setString(15, cargasXMLForm.getEmisorRFC());
        	stmt.setString(16, cargasXMLForm.getEmisorNombre());
        	stmt.setString(17, cargasXMLForm.getReceptorRFC());
        	stmt.setString(18, cargasXMLForm.getReceptorNombre());
        	stmt.setDouble(19, cargasXMLForm.getRetencionIVA());
        	stmt.setDouble(20, cargasXMLForm.getTransladoIVA());
        	stmt.setDouble(21, cargasXMLForm.getRetencionISR());
        	stmt.setDouble(22, cargasXMLForm.getTransladoIEPS());
        	stmt.setString(23, idEmpleado);
        	stmt.setString(24, cargasXMLForm.getEstadoSAT());
        	stmt.setString(25, cargasXMLForm.getEstatusSAT());
        	stmt.setString(26, cargasXMLForm.getTipoComprobante()); // Nuevo campo
        
        	 logger.info("stmtGuardar===>"+stmt);
        	resultado = stmt.executeUpdate();
        }
        catch(SQLException sql){
        	resultado = sql.getErrorCode();
        	Utils.imprimeLog("", sql);
            
        }catch(Exception e){
        	resultado = 100;
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
        return resultado;
    }
}
