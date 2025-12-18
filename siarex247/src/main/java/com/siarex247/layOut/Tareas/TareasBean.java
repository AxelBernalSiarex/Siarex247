package com.siarex247.layOut.Tareas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.catalogos.Proveedores.ProveedoresQuerys;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class TareasBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	

	public ArrayList<TareasForm> detalleTareasPantalla(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasForm tareasForm = new TareasForm();
        ArrayList<TareasForm> listaDetalle = new ArrayList<>();
        
        try
        {
        	StringBuffer sbQuery = new StringBuffer(TareasQuerys.getDetalleTareasPanta(esquema));
            stmt = con.prepareStatement( sbQuery.toString());
            stmt.setString(1, "GEN");
            stmt.setString(2, "ORD");
            
            rs = stmt.executeQuery();
            String tipoEnvio = null;
            int cont = 1;
			while(rs.next()) {
				tipoEnvio = Utils.noNuloNormal(rs.getString(6));
				tareasForm.setContador(cont++);
				tareasForm.setClaveTarea(rs.getInt(1));
				tareasForm.setSubject(Utils.noNuloNormal(rs.getString(2)));
				tareasForm.setCorreoDe(Utils.noNuloNormal(rs.getString(3)));
				tareasForm.setFechaTarea(UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(5)));
				tareasForm.setTipoEnvio(evaluaTipo(tipoEnvio));
				tareasForm.setEstatus(Utils.noNuloNormal(rs.getString(7)));
				tareasForm.setEstatusDes(evaluaEstatus(Utils.noNuloNormal(rs.getString(7))));
				tareasForm.setTipoTarea(evaluaTipoTarea(Utils.noNuloNormal(rs.getString(8))));
				listaDetalle.add(tareasForm);
				tareasForm = new TareasForm();
				
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
        return listaDetalle;
    }
	
	
	public int altaTarea(Connection con, String esquema, String subject,String correoFrom, String mensaje, 
			String fechaTarea, String nombreArchivo, String tipoEnvio, String estatus, String usuario, int num_Dias1, 
			int num_Dias2, String notificacion, String tipoAlarma)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        String tipo = "0";
        try
        {
        	if ("NONE".equalsIgnoreCase(tipoEnvio)){
        		tipo = "0";
        	}else if ("ALL".equalsIgnoreCase(tipoEnvio)){
        		tipo = "1";
        	}else if ("MEX".equalsIgnoreCase(tipoEnvio)){
        		tipo = "2";
        	}else if ("USA".equalsIgnoreCase(tipoEnvio)){
        		tipo = "3";
        	}else if ("PRO".equalsIgnoreCase(tipoEnvio)){
        		tipo = "100";
        	}else if ("USR".equalsIgnoreCase(tipoEnvio)){
        		tipo = "101";
        	}
        	stmt = con.prepareStatement(TareasQuerys.getInsertTarea(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
        	stmt.setString(1, Utils.noNuloNormal(subject));
        	stmt.setString(2, correoFrom);
        	stmt.setString(3, Utils.noNuloNormal(mensaje));
        	stmt.setString(4, fechaTarea);
        	stmt.setString(5, nombreArchivo);
        	stmt.setString(6, tipo);
        	stmt.setString(7, estatus);
        	stmt.setString(8, Utils.noNuloNormal(usuario));
        	stmt.setInt(9, num_Dias1);
        	stmt.setInt(10, num_Dias2);
        	stmt.setString(11, notificacion);
        	stmt.setString(12, tipoAlarma);
        	
        	logger.info("stmt==>"+stmt);
            int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
            }
        }
        catch(SQLException sql){
        	resultado = -100;
        	Utils.imprimeLog("altaTarea ", sql);
            
        }catch(Exception e){
        	resultado = -100;
            Utils.imprimeLog("altaTarea 2 ", e);
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
	
	public int altaTareProv(Connection con, String esquema, int claveTarea, Integer arrClavesProveedor [])
    {
        PreparedStatement stmt = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(TareasQuerys.getQueryInsertTareaProv(esquema));
        	stmt.setInt(1, claveTarea);
        		
            	for (int x =0; x < arrClavesProveedor.length; x++){
            		stmt.setInt(2, arrClavesProveedor[x]);
            		stmt.executeUpdate();
            		resultado++;
            	}	
        }
        catch(SQLException sql){
        	Utils.imprimeLog("altaTareProv ", sql);
        }catch(Exception e){
            Utils.imprimeLog("altaTareProv 2 ", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
	
	
	public int eliminaTarea(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getEliminaTareas(esquema));
            stmt.setInt(1, claveTarea);
            res = stmt.executeUpdate();
            
            stmt.close();
            stmt = null;
            
            stmt = con.prepareStatement(TareasQuerys.getEliminaTareasDetalle(esquema));
            stmt.setInt(1, claveTarea);
            stmt.executeUpdate();
            
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return res;
    }
	
	public int cancelaTarea(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getActualizaTareas(esquema));
            stmt.setString(1, "1");
            stmt.setInt(2, claveTarea);
            
            // logger.info("stmtCancelar==>"+stmt);
            res = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return res;
    }
	
	
	
	public int altaTareProv(Connection con, String esquema, int claveTarea, String clavesProveedor)
    {
        PreparedStatement stmt = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(TareasQuerys.getQueryInsertTareaProv(esquema));
        	stmt.setInt(1, claveTarea);
        		String listaClaves [] = clavesProveedor.split(",");
            	for (int x =0; x < listaClaves.length; x++){
            		if ( !"".equalsIgnoreCase(listaClaves[x]) ) {
                		stmt.setInt(2, Integer.parseInt(listaClaves[x]));
                		stmt.executeUpdate();
                		resultado++;
            		}
            	}	
        }
        catch(SQLException sql){
        	Utils.imprimeLog("altaTareProv ", sql);
        }catch(Exception e){
            Utils.imprimeLog("altaTareProv 2 ", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
	
	
	public ArrayList<TareasForm> detalleTareasOrdenesOK(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasForm tareasForm = new TareasForm();
        ArrayList<TareasForm> listaDetalle = new ArrayList<>();
        
        try
        {
        	stmt = con.prepareStatement( TareasQuerys.getQueryDetalleTareasOrdenesOK(esquema));
        	stmt.setInt(1, claveTarea);
            rs = stmt.executeQuery();
            String ESTATUS = null;
            String ESTATUS_SHIPTO = null;
            final String OK = "OK";
			while(rs.next()){
				ESTATUS = Utils.noNulo(rs.getString(9));
				ESTATUS_SHIPTO = Utils.noNulo(rs.getString(10));
				if (OK.equalsIgnoreCase(ESTATUS) && OK.equalsIgnoreCase(ESTATUS_SHIPTO)) {
					
					tareasForm.setVendorId(Utils.noNulo(rs.getString(4)));
					tareasForm.setNoOrden(Utils.noNuloNormal(rs.getString(2)));
					tareasForm.setRazonSocial(Utils.noNuloNormal(rs.getString(8)));
					listaDetalle.add(tareasForm);
					tareasForm = new TareasForm();
				}
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
        return listaDetalle;
    }
	
	
	public ArrayList<TareasForm> detalleTareasOrdenesNE(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasForm tareasForm = new TareasForm();
        ArrayList<TareasForm> listaDetalle = new ArrayList<>();
        try
        {
        	stmt = con.prepareStatement( TareasQuerys.getQueryDetalleTareasOrdenesNG(esquema));
        	stmt.setInt(1, claveTarea);
            rs = stmt.executeQuery();
            String ESTATUS = null;
            String ESTATUS_SHIPTO = null;
            final String NE = "NE";
			while(rs.next()){
				ESTATUS = Utils.noNulo(rs.getString(10));
				ESTATUS_SHIPTO = Utils.noNulo(rs.getString(11));
				if (NE.equalsIgnoreCase(ESTATUS) || NE.equalsIgnoreCase(ESTATUS_SHIPTO)) {
					tareasForm.setVendorId(Utils.noNulo(rs.getString(4)));
					tareasForm.setNoOrden(Utils.noNuloNormal(rs.getString(2)));
					tareasForm.setRazonSocial(Utils.noNuloNormal(rs.getString(8)));
					tareasForm.setShipTO(Utils.noNuloNormal(rs.getString(9)));
					tareasForm.setEstatus(Utils.noNuloNormal(rs.getString(10)));
					tareasForm.setEstatusShipto(Utils.noNuloNormal(rs.getString(11)));
					tareasForm.setListaNegra(desListaNegra( Utils.noNuloNormal(rs.getString(12))));
					tareasForm.setEstatusListaNegra(Utils.noNulo(rs.getString(12)));
					listaDetalle.add(tareasForm);
					tareasForm = new TareasForm();
				}
					
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
        return listaDetalle;
    }
	
	
	public ArrayList<TareasForm> buscarTareas(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasForm tareasForm = new TareasForm();
        ArrayList<TareasForm> datos = new ArrayList<TareasForm>();
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getQueryDetalleTareas(esquema));
            stmt.setString(1, "0"); // Agendado 
            stmt.setString(2, "2");
            rs = stmt.executeQuery();
			while(rs.next())
            {
				tareasForm.setClaveTarea(rs.getInt(1));
				tareasForm.setSubject(Utils.noNuloNormal(rs.getString(2)));
				tareasForm.setCorreoDe(Utils.noNuloNormal(rs.getString(3)));
				tareasForm.setMensaje(Utils.noNuloNormal(rs.getString(4)));
				tareasForm.setFechaTarea(rs.getString(5));
				tareasForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(6)));
				tareasForm.setTipoEnvio(Utils.noNulo(rs.getString(7)));
				tareasForm.setEstatus(Utils.noNulo(rs.getString(8)));
				
				tareasForm.setNum_Dias1(rs.getInt(10));
				tareasForm.setNum_Dias2(rs.getInt(11));
				tareasForm.setNotificacion(Utils.noNulo(rs.getString(12)));
				tareasForm.setTipoTarea(Utils.noNulo(rs.getString(13)));
				datos.add(tareasForm);
				tareasForm = new TareasForm();
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
        return datos;
    }
	
	public ArrayList<TareasForm> buscarEmailProv(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasForm tareasForm = new TareasForm();
        ArrayList<TareasForm> datos = new ArrayList<TareasForm>();
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getQueryDetalleTareasPro(esquema));
            stmt.setInt(1, claveTarea);
            rs = stmt.executeQuery();
			while(rs.next()) 
            {
				tareasForm.setClaveProveedor(rs.getInt(1));
				tareasForm.setEmailProv(Utils.noNuloNormal(rs.getString(5)));
				tareasForm.setRfcProveedor(Utils.noNulo(rs.getString(2)));
				tareasForm.setRazonSocialProv(Utils.regresaCaracteresHTML(Utils.noNuloNormal(rs.getString(3))));
				tareasForm.setNotOrdenUsuario(Utils.noNulo(rs.getString(4)));
				datos.add(tareasForm);
				tareasForm = new TareasForm();
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
        return datos;
    }
	
	
	
	public ArrayList<TareasForm> buscarEmailTipo(Connection con, String esquema, String tipoProv)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasForm tareasForm = new TareasForm();
        ArrayList<TareasForm> datos = new ArrayList<TareasForm>();
        try
        {
        	stmt = con.prepareStatement(ProveedoresQuerys.getQueryProveedoresTipo(esquema));
            if ("0".equalsIgnoreCase(tipoProv)){
            	stmt.setString(1, "NONE");
            	stmt.setString(2, "NONE");
            }else if ("1".equalsIgnoreCase(tipoProv)){
            	stmt.setString(1, "MEX");
            	stmt.setString(2, "USA");
            }else if ("2".equalsIgnoreCase(tipoProv)){
            	stmt.setString(1, "MEX");
            	stmt.setString(2, "MEX");
            }else if ("3".equalsIgnoreCase(tipoProv)){
            	stmt.setString(1, "USA");
            	stmt.setString(2, "USA");
            }
            //CLAVE_PROVEEDOR, RFC, RAZON_SOCIAL, NOTIFICAR_ORDEN, EMAIL
            rs = stmt.executeQuery();
			while(rs.next()) 
            {
				tareasForm.setClaveProveedor(rs.getInt(1));
				tareasForm.setEmailProv(Utils.noNuloNormal(rs.getString(5)));
				datos.add(tareasForm);
				tareasForm = new TareasForm();
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
        return datos;
    }
	
	
	public int actualizaTarea(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getQueryActualizaTareas(esquema));
            stmt.setString(1, "3");
            stmt.setInt(2, claveTarea);
            res = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return res;
    }
	
	public String buscarTareasHistorico(Connection con, String esquema, int claveTarea)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String retorno = "N";
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getQueryBuscarHistoricoTarea(esquema));
            stmt.setInt(1, claveTarea); 
            rs = stmt.executeQuery();
			if(rs.next()) 
            {
				retorno = Utils.noNulo(rs.getString(1));
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
        return retorno;
    }
	
	public int grabarHistorico(Connection con, String esquema, int claveTarea, String fechaTarea)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(TareasQuerys.getQueryGrabarHistoricoTarea(esquema));
            stmt.setInt(1, claveTarea);
            stmt.setString(2, "S");
            stmt.setString(3, fechaTarea);
            res = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return res;
    }
	
	
	
	public ArrayList<TareasOrdenesForm> buscarOrdenesXtarea(Connection con, String esquema, int claveTarea, 
 			int claveProveedor, String estatus)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TareasOrdenesForm tareasOrdForm = new TareasOrdenesForm();
        ArrayList<TareasOrdenesForm> datos = new ArrayList<TareasOrdenesForm>();
        try
        {
        	
            stmt = con.prepareStatement(TareasQuerys.getQueryObtenerOrdXtareas(esquema));
            stmt.setInt(1, claveTarea);
            stmt.setInt(2, claveProveedor);
            stmt.setString(3, estatus);
            rs = stmt.executeQuery();
			while(rs.next()) 
            {
				tareasOrdForm.setClaveRegistro(rs.getInt(1));
				tareasOrdForm.setNumeroOrden(rs.getLong(2));
				tareasOrdForm.setClaveProveedor(rs.getInt(3));
				tareasOrdForm.setVendorID(Utils.noNuloNormal(rs.getString(4)));
				tareasOrdForm.setRfc(Utils.noNuloNormal(rs.getString(5)));
				tareasOrdForm.setTotal(rs.getDouble(6));
				tareasOrdForm.setTipoMoneda(Utils.noNuloNormal(rs.getString(7)));
				tareasOrdForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(8)));
				datos.add(tareasOrdForm);
				tareasOrdForm = new TareasOrdenesForm();
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
        return datos;
    }
	
	
	public String buscarEmpleadoOrden(Connection con, String esquema, long numeroOrden){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String numeroAsignado = "";
        try{
            stmt = con.prepareStatement(TareasQuerys.getQueryBuscarOrdenCompra(esquema));
            stmt.setLong(1, numeroOrden);
            rs = stmt.executeQuery();
			if (rs.next()){
				numeroAsignado = Utils.noNulo(rs.getString(1));
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
        return numeroAsignado;
    }
	
	
	public String evaluaEstatus(String tipoEstatus){
		if ("0".equalsIgnoreCase(tipoEstatus)){
			return "PROGRAMADO";
		}else if ("1".equalsIgnoreCase(tipoEstatus)){
			return "CANCELADO";
		}else if ("2".equalsIgnoreCase(tipoEstatus)){
			return "ENVIADO AHORA";
		}else if ("3".equalsIgnoreCase(tipoEstatus)){
			return "PROCESADO";
		}
		return tipoEstatus;
	}
	
	

	public String evaluaTipo(String tipoEnvio){
		if ("0".equalsIgnoreCase(tipoEnvio)){
			return "NINGUNO";
		}else if ("1".equalsIgnoreCase(tipoEnvio)){
			return "TODOS";
		}else if ("2".equalsIgnoreCase(tipoEnvio)){
			return "PROVEDORES DE MEXICO";
		}else if ("3".equalsIgnoreCase(tipoEnvio)){
			return "PROVEEDORES DE USA";
		}else if ("100".equalsIgnoreCase(tipoEnvio)){
			return "TODOS LOS PROVEEDORES";
		}else if ("101".equalsIgnoreCase(tipoEnvio)){
			return "USUARIO DE EMPRESA";
		}else if ("102".equalsIgnoreCase(tipoEnvio)){
			return "ALARMA DE ORDENES";
		}
		return tipoEnvio;
	}
	
	public String evaluaTipoTarea(String tipoTarea){
		if ("GEN".equalsIgnoreCase(tipoTarea)){
			return "ALARMA GENERAL";
		}else if ("PRO".equalsIgnoreCase(tipoTarea)){
			return "ALARMA PROVEEDOR";
		}else if ("USR".equalsIgnoreCase(tipoTarea)){
			return "ALARMA USUARIO";
		}else if ("ORD".equalsIgnoreCase(tipoTarea)){
			return "ALARMA ORDENES";
		}else if ("TRH".equalsIgnoreCase(tipoTarea)){
			return "ALARMA GENERAL";
		}
		return tipoTarea;
	}
	
	
	private String desListaNegra(String estatusLista) {
		if ("S".equalsIgnoreCase(estatusLista)) {
			return "PROVEEDOR ENCONTRADO EN LISTA NEGRA";
		}
		return "NO SE ENCUENTRA EN LISTA NEGRA";
	}
	
}
