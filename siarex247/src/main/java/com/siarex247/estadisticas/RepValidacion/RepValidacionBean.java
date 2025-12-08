package com.siarex247.estadisticas.RepValidacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class RepValidacionBean {
	public static final Logger logger = Logger.getLogger("siarex247");
	
	public ArrayList<RepValidacionForm>  detalleBitacora (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<RepValidacionForm> listaDetalle = new ArrayList<RepValidacionForm>();
		RepValidacionForm repBitForm = new RepValidacionForm();
        try {
			stmt = con.prepareStatement(RepValidacionQuerys.getListaBitacora(esquema));
			rs = stmt.executeQuery();

			while (rs.next()) {
					repBitForm.setIdRegistro(rs.getInt(1));
					repBitForm.setFolioEmpresa(Utils.noNuloDouble(rs.getString(2)));
					repBitForm.setRazonSocial(Utils.noNulo(rs.getString(3)));
					repBitForm.setEtiqueta(Utils.noNuloNormal(rs.getString(4)));
					repBitForm.setValorXML(Utils.noNulo(rs.getString(5)));
					repBitForm.setEstatus(Utils.noNulo(rs.getString(6)));
					repBitForm.setResultado(desResultado(Utils.noNulo(rs.getString(7))));
					repBitForm.setFechaTrans(Utils.noNulo(rs.getString(8)));
					
					listaDetalle.add(repBitForm);
					repBitForm = new RepValidacionForm();	
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
		return listaDetalle;
	}
	
	
	private String desResultado(String resultado) {
		if ("S".equalsIgnoreCase(resultado)) {
			return "CUMPLE";
		}else if ("N".equalsIgnoreCase(resultado)) {
			return "NO CUMPLE";
		}else {
			return "";
		}
	}
}
