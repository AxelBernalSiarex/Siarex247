package com.siarex247.configSistema.AlertaConciliacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class AlertaConciliacionBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public AlertaConciliacionForm buscarConfProceso (Connection con, String esquema, String proceso){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AlertaConciliacionForm outForm = new AlertaConciliacionForm();
		try {
			stmt = con.prepareStatement(AlertaConciliacionQuerys.getQueryConfProceso(esquema));
			stmt.setString(1, proceso);
			
			rs  = stmt.executeQuery();
			if (rs.next()) {
				outForm.setDiaEjecucion(Utils.noNulo(rs.getString(1)));
				outForm.setSubject(Utils.noNuloNormal(rs.getString(2)));
				outForm.setMensajeError(Utils.noNuloNormal(rs.getString(3)));
				outForm.setDestinatario1(Utils.noNuloNormal(rs.getString(4)));
				outForm.setDestinatario2(Utils.noNuloNormal(rs.getString(5)));
				outForm.setDestinatario3(Utils.noNuloNormal(rs.getString(6)));
				outForm.setDestinatario4(Utils.noNuloNormal(rs.getString(7)));
				outForm.setDestinatario5(Utils.noNuloNormal(rs.getString(8)));
				outForm.setAdjuntar(Utils.noNulo(rs.getString(9)));
				outForm.setActivar(Utils.noNulo(rs.getString(10)));
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
		return outForm;
	}
	
	public int configuracionProceso(Connection con, String esquema, String tipoProceso, String  subject, String mensaje,
            String destinatario1, String destinatario2, String destinatario3, String destinatario4, String destinatario5,
            String adjuntarArchivo, String activarProceso, String usuarioHTTP, String proceso){
		PreparedStatement stmt = null;
		int resultado = 0;
		try{

			stmt = con.prepareStatement(AlertaConciliacionQuerys.getQueryUpdProceso(esquema));
			stmt.setString(1, tipoProceso);
			stmt.setString(2, subject);
			stmt.setString(3, mensaje);
			stmt.setString(4, destinatario1);
			stmt.setString(5, destinatario2);
			stmt.setString(6, destinatario3);
			stmt.setString(7, destinatario4);
			stmt.setString(8, destinatario5);
			stmt.setString(9, adjuntarArchivo);
			stmt.setString(10, activarProceso);
			stmt.setString(11, usuarioHTTP);
			stmt.setString(12, proceso);
		
		resultado = stmt.executeUpdate();
		if(resultado == 0) {
			try{
				if(stmt != null) {
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e){
				stmt = null;
			}

				stmt = con.prepareStatement(AlertaConciliacionQuerys.getQueryInsProceso(esquema));
				stmt.setString(1, "PRO01");
				stmt.setString(2, tipoProceso);
				stmt.setString(3, subject);
				stmt.setString(4, mensaje);
				stmt.setString(5, destinatario1);
				stmt.setString(6, destinatario2);
				stmt.setString(7, destinatario3);
				stmt.setString(8, destinatario4);
				stmt.setString(9, destinatario5);
				stmt.setString(10, adjuntarArchivo);
				stmt.setString(11, activarProceso);
				stmt.setString(12, usuarioHTTP);
				resultado = stmt.executeUpdate();
			
			
		}
		}
		catch(Exception e){
			Utils.imprimeLog("configuracionProceso(): ", e);
		}
		finally{
			try{
				if(stmt != null) {
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e){
				stmt = null;
			}
		}
		return resultado;
	}
	
}
