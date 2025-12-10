package com.siarex247.catalogos.Proveedores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.utils.Gramatica;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;


public class ProveedoresBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public ArrayList<ProveedoresForm> detalleProveedores(Connection con, String esquema){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ProveedoresForm> listaProveedores = new ArrayList<>();
        ProveedoresForm proveedoresForm = new ProveedoresForm();
        
        
        try{
        	StringBuffer sbQuery = new StringBuffer(ProveedoresQuerys.getQueryDetalleProveedor(esquema));
        	stmt = con.prepareStatement(sbQuery.toString());
            rs = stmt.executeQuery();
            while(rs.next()) {
            	proveedoresForm.setClaveRegistro(rs.getInt(1));
            	proveedoresForm.setRazonSocial(Utils.noNulo(rs.getString(2)));
            	proveedoresForm.setRfc(Utils.noNulo(rs.getString(3)));
            	proveedoresForm.setDomicilio("");
            	proveedoresForm.setCalle(Utils.noNulo(rs.getString(4)));
            	proveedoresForm.setNumeroInt(Utils.noNulo(rs.getString(5)));
            	proveedoresForm.setNumeroExt(Utils.noNulo(rs.getString(6)));
            	proveedoresForm.setColonia(Utils.noNulo(rs.getString(7)));
            	proveedoresForm.setDelegacion(Utils.noNulo(rs.getString(8)));
            	proveedoresForm.setCiudad(Utils.noNulo(rs.getString(9)));
            	proveedoresForm.setEstado(Utils.noNulo(rs.getString(10)));
            	proveedoresForm.setCodigoPostal(rs.getInt(11));
            	proveedoresForm.setTelefono(Utils.noNulo(rs.getString(12)));
            	proveedoresForm.setExtencion(Utils.noNulo(rs.getString(13)));
            	proveedoresForm.setNombreContacto(Utils.noNulo(rs.getString(14)));
            	proveedoresForm.setTipoProveedor(Utils.noNulo(rs.getString(15)));
            	proveedoresForm.setBanco(Utils.noNulo(rs.getString(16)));
            	proveedoresForm.setSucursal(Utils.noNulo(rs.getString(17)));
            	proveedoresForm.setNombreSucursal(Utils.noNulo(rs.getString(18)));
            	proveedoresForm.setNumeroCuenta(Utils.noNulo(rs.getString(19)));
            	proveedoresForm.setCuentaClabe(Utils.noNulo(rs.getString(20)));
            	proveedoresForm.setNumeroConvenio(Utils.noNulo(rs.getString(21)));
            	proveedoresForm.setMoneda(Utils.noNulo(rs.getString(22)));
            	proveedoresForm.setBancoDollar(Utils.noNulo(rs.getString(23)));
            	proveedoresForm.setSucursalDollar(Utils.noNulo(rs.getString(24)));
            	proveedoresForm.setNombreSucursalDollar(Utils.noNulo(rs.getString(25)));
            	proveedoresForm.setNumeroCuentaDollar(Utils.noNulo(rs.getString(26)));
            	proveedoresForm.setCuentaClabeDollar(Utils.noNulo(rs.getString(27)));
            	proveedoresForm.setNumeroConvenioDollar(Utils.noNulo(rs.getString(28)));
            	proveedoresForm.setMonedaDollar(Utils.noNulo(rs.getString(29)));
            	proveedoresForm.setAbaDollar(Utils.noNulo(rs.getString(30)));
            	proveedoresForm.setSwitfCodeDollar(Utils.noNulo(rs.getString(31)));
            	proveedoresForm.setBancoOtro(Utils.noNulo(rs.getString(32)));
            	proveedoresForm.setSucursalOtro(Utils.noNulo(rs.getString(33)));
            	proveedoresForm.setNombreSucursalOtro(Utils.noNulo(rs.getString(34)));
            	proveedoresForm.setNumeroCuentaOtro(Utils.noNulo(rs.getString(35)));
            	proveedoresForm.setCuentaClabeOtro(Utils.noNulo(rs.getString(36)));
            	proveedoresForm.setNumeroConvenioOtro(Utils.noNulo(rs.getString(37)));
            	proveedoresForm.setMonedaOtro(Utils.noNulo(rs.getString(38)));
            	proveedoresForm.setAbaOtro(Utils.noNulo(rs.getString(39)));
            	proveedoresForm.setSwitfCodeOtro(Utils.noNulo(rs.getString(40)));
            	proveedoresForm.setUsuarioGeneracion(Utils.noNulo(rs.getString(41)));
            	proveedoresForm.setFechaAlta(UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(42)));
            	proveedoresForm.setTipoConfirmacion(Utils.noNulo(rs.getString(43)));
            	proveedoresForm.setAnexo24(getAnexo24(Utils.noNulo(rs.getString(45))));
            	proveedoresForm.setIdProveedor(Utils.noNulo(rs.getString(46)));
            	String notificaOrdenCompra = Utils.noNulo(rs.getString(48));
            	proveedoresForm.setNotificaOrdenCompra("N");
            	if (notificaOrdenCompra.equals("1")){
            		proveedoresForm.setNotificaOrdenCompra("S");
				}
            	String notificaOrdenPago = Utils.noNulo(rs.getString(49));
            	proveedoresForm.setNotificaOrdenPago("N");
            	if (notificaOrdenPago.equals("1")){
            		proveedoresForm.setNotificaOrdenPago("S");
				}
            	
            	String bandDescuento = Utils.noNulo(rs.getString(50));
            	proveedoresForm.setBandDescuento("N");
            	if (bandDescuento.equals("1")){
            		proveedoresForm.setBandDescuento("S");
				}
            	
            	proveedoresForm.setConceptoServicio(Utils.noNulo(rs.getString(51)));
            	proveedoresForm.setFormaPago(getFormaPago(Utils.noNulo(rs.getString(52))));
            	proveedoresForm.setNumEstrellas(rs.getInt(53));
            	proveedoresForm.setNumeroCuentaProveedor(Utils.noNulo(rs.getString(54)));
            	proveedoresForm.setCentroCostos(Utils.noNulo(rs.getString(55)));
            	
            	String tieneImss = Utils.noNulo(rs.getString(60));
            	if (tieneImss.equals("")){
            		proveedoresForm.setTieneIMSS("N");
				}else {
					proveedoresForm.setTieneIMSS(Utils.noNulo(rs.getString(60)));
				}
            	
            	String tieneSAT = Utils.noNulo(rs.getString(61));
            	if (tieneSAT.equals("")){
            		proveedoresForm.setTieneSAT("N");
				}else {
					proveedoresForm.setTieneSAT(Utils.noNulo(rs.getString(61)));
				}
            	
            	proveedoresForm.setServEsp(Utils.noNulo(rs.getString(62)));
            	proveedoresForm.setNumRegistro(Utils.noNulo(rs.getString(63)));
            	
            	proveedoresForm.setEstatusRegistro(Utils.noNulo(rs.getString(66)));
            	
            	listaProveedores.add(proveedoresForm);
            	proveedoresForm = new ProveedoresForm();
				
            }
        }
        catch(Exception e){
        	Utils.imprimeLog("detalleProveedoresJSON(): ", e);
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
        return listaProveedores;
    }
	
	
	
	public  ProveedoresForm consultarProveedor(Connection con, String esquemaEmpresa, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProveedoresForm proveedoresForm = new ProveedoresForm();;
		try{

			stmt = con.prepareStatement(ProveedoresQuerys.getQueryInfoProvee(esquemaEmpresa));
			stmt.setInt(1, claveProveedor);
			rs = stmt.executeQuery();
			if (rs.next()){
				int numParam = 1;
				proveedoresForm.setClaveRegistro(claveProveedor);
				proveedoresForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(numParam++))));
	            proveedoresForm.setRfc(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCalle(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroInt(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroExt(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setColonia(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setDelegacion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCiudad(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setEstado(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCodigoPostal(Utils.noNuloINT(rs.getInt(numParam++)));
	            proveedoresForm.setTelefono(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setExtencion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreContacto(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTipoProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBanco(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSucursal(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreSucursal(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuenta(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCuentaClabe(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroConvenio(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setMoneda(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBancoDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSucursalDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreSucursalDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuentaDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCuentaClabeDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroConvenioDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setMonedaDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAbaDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSwitfCodeDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBancoOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSucursalOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreSucursalOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuentaOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCuentaClabeOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroConvenioOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setMonedaOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAbaOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSwitfCodeOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setUsuarioGeneracion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTipoConfirmacion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setEmail(Utils.noNuloNormal(rs.getString(numParam++)));
	            proveedoresForm.setAnexo24(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setIdProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setLimiteTolerancia(Utils.noNulo(rs.getString(numParam++)));
	            numParam++;
	            // SE BRINCA
	            proveedoresForm.setNotComUsuario(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNotPagoUsuario(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBandDescuento(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setConServicio(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setFormaPago(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumEstrellas(Utils.noNuloINT(rs.getString(numParam++)));
	            proveedoresForm.setLimiteComplemento(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuentaProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCentroCostos(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setRazonProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setPagoDolares(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setPagoPesos(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAMERICANOS_SERIE(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAMERICANOS_FOLIO(Utils.noNuloINT(rs.getInt(numParam++)));
	            proveedoresForm.setAMERICANOS_ACCESO(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBandIMSS(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBandSAT(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTieneIMSS(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTieneSAT(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTieneConfidencial(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setServEsp(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumRegistro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCartaPorte(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setRegimenFiscal(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setConfirmarIMSS(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setConfirmarSAT(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setEstatusRegistro(Utils.noNulo(rs.getString(numParam++)));
				
				
				
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
			}catch(Exception e){
				stmt = null;
			}
		}
		return proveedoresForm;
	}
	
	
	
	
	public  ProveedoresForm consultarProveedorXrfc(Connection con, String esquemaEmpresa, String rfc){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProveedoresForm proveedoresForm = new ProveedoresForm();
		try{

			stmt = con.prepareStatement(ProveedoresQuerys.getInfoProveeXrfc(esquemaEmpresa));
			stmt.setString(1, rfc);
			stmt.setString(2, "A");
			
			//logger.info("stmt===>"+stmt);
			
			rs = stmt.executeQuery();
			if (rs.next()){
				int numParam = 1;
				proveedoresForm.setClaveRegistro(rs.getInt(numParam++));
				proveedoresForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(numParam++))));
	            proveedoresForm.setRfc(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCalle(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroInt(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroExt(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setColonia(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setDelegacion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCiudad(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setEstado(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCodigoPostal(Utils.noNuloINT(rs.getInt(numParam++)));
	            proveedoresForm.setTelefono(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setExtencion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreContacto(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTipoProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBanco(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSucursal(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreSucursal(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuenta(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCuentaClabe(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroConvenio(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setMoneda(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBancoDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSucursalDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreSucursalDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuentaDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCuentaClabeDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroConvenioDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setMonedaDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAbaDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSwitfCodeDollar(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBancoOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSucursalOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNombreSucursalOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuentaOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCuentaClabeOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroConvenioOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setMonedaOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAbaOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setSwitfCodeOtro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setUsuarioGeneracion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTipoConfirmacion(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setEmail(Utils.noNuloNormal(rs.getString(numParam++)));
	            proveedoresForm.setAnexo24(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setIdProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setLimiteTolerancia(Utils.noNulo(rs.getString(numParam++)));
	            numParam++;
	            // SE BRINCA
	            proveedoresForm.setNotComUsuario(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNotPagoUsuario(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBandDescuento(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setConServicio(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setFormaPago(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumEstrellas(Utils.noNuloINT(rs.getString(numParam++)));
	            proveedoresForm.setLimiteComplemento(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumeroCuentaProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCentroCostos(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setRazonProveedor(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setPagoDolares(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setPagoPesos(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAMERICANOS_SERIE(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setAMERICANOS_FOLIO(Utils.noNuloINT(rs.getInt(numParam++)));
	            proveedoresForm.setAMERICANOS_ACCESO(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBandIMSS(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setBandSAT(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTieneIMSS(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTieneSAT(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setTieneConfidencial(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setServEsp(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setNumRegistro(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setCartaPorte(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setRegimenFiscal(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setConfirmarIMSS(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setConfirmarSAT(Utils.noNulo(rs.getString(numParam++)));
	            proveedoresForm.setEstatusRegistro(Utils.noNulo(rs.getString(numParam++)));
				
				
				
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
			}catch(Exception e){
				stmt = null;
			}
		}
		return proveedoresForm;
	}
	
	
	public ProveedoresForm infoProveedoresMail(Connection con, String esquema, int claveProveedor, ProveedoresForm proveedoresForm)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = con.prepareStatement(ProveedoresQuerys.getQueryInfoProveedorMail(esquema));
            stmt.setInt(1, claveProveedor);
            stmt.setString(2, "S");
            rs = stmt.executeQuery();
            int x = 1;
			while(rs.next()) {
				switch (x) {
				case 1:
					proveedoresForm.setEmail1(Utils.noNuloNormal(rs.getString(2)));
					proveedoresForm.setTipoEmail1(validaTipoCorreo(Utils.noNuloNormal(rs.getString(4))));
					proveedoresForm.setTipoEmail2(validaTipoCorreo(Utils.noNuloNormal(rs.getString(5))));
					break;
				case 2:
					proveedoresForm.setEmail2(Utils.noNuloNormal(rs.getString(2)));
					proveedoresForm.setTipoEmail3(validaTipoCorreo(Utils.noNuloNormal(rs.getString(4))));
					proveedoresForm.setTipoEmail4(validaTipoCorreo(Utils.noNuloNormal(rs.getString(5))));
					break;
				case 3:
					proveedoresForm.setEmail3(Utils.noNuloNormal(rs.getString(2)));
					proveedoresForm.setTipoEmail5(validaTipoCorreo(Utils.noNuloNormal(rs.getString(4))));
					proveedoresForm.setTipoEmail6(validaTipoCorreo(Utils.noNuloNormal(rs.getString(5))));
					break;
				case 4:
					proveedoresForm.setEmail4(Utils.noNuloNormal(rs.getString(2)));
					proveedoresForm.setTipoEmail7(validaTipoCorreo(Utils.noNuloNormal(rs.getString(4))));
					proveedoresForm.setTipoEmail8(validaTipoCorreo(Utils.noNuloNormal(rs.getString(5))));
					break;
				case 5:
					proveedoresForm.setEmail5(Utils.noNuloNormal(rs.getString(2)));
					proveedoresForm.setTipoEmail9(validaTipoCorreo(Utils.noNuloNormal(rs.getString(4))));
					proveedoresForm.setTipoEmail10(validaTipoCorreo(Utils.noNuloNormal(rs.getString(5))));
					break;
				default:
					break;
				}
				x++;
            }
        }
        catch(Exception e){
        	Utils.imprimeLog("infoProveedoresMail ", e);
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
        return proveedoresForm;
    }
	
	
	public int altaProveedores(Connection con, String esquema, ProveedoresForm proveedoresForm ){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try{
        	stmt = con.prepareStatement(ProveedoresQuerys.getQueryAltaProveedor(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
        	stmt.setString(1, proveedoresForm.getRazonSocial().toUpperCase());
            stmt.setString(2, proveedoresForm.getRfc().toUpperCase());
            stmt.setString(3, proveedoresForm.getCalle().toUpperCase());
            stmt.setString(4, proveedoresForm.getNumeroInt().toUpperCase());
            stmt.setString(5, proveedoresForm.getNumeroExt().toUpperCase());
            stmt.setString(6, proveedoresForm.getColonia().toUpperCase());
            stmt.setString(7, proveedoresForm.getDelegacion().toUpperCase());
            stmt.setString(8, proveedoresForm.getCiudad().toUpperCase());
            stmt.setString(9, proveedoresForm.getEstado().toUpperCase());
            stmt.setInt(10, proveedoresForm.getCodigoPostal());
            stmt.setString(11, proveedoresForm.getTelefono().toUpperCase());
            stmt.setString(12, proveedoresForm.getExtencion().toUpperCase());
            stmt.setString(13, proveedoresForm.getNombreContacto().toUpperCase());
            stmt.setString(14, proveedoresForm.getTipoProveedor().toUpperCase());
            stmt.setString(15, proveedoresForm.getBanco().toUpperCase());
            stmt.setString(16, proveedoresForm.getSucursal().toUpperCase());
            stmt.setString(17, proveedoresForm.getNombreSucursal().toUpperCase());
            stmt.setString(18, proveedoresForm.getNumeroCuenta().toUpperCase());
            stmt.setString(19, proveedoresForm.getCuentaClabe().toUpperCase());
            stmt.setString(20, proveedoresForm.getNumeroConvenio().toUpperCase());
            stmt.setString(21, proveedoresForm.getMoneda().toUpperCase());
            stmt.setString(22, proveedoresForm.getBancoDollar().toUpperCase());
            stmt.setString(23, proveedoresForm.getSucursalDollar().toUpperCase());
            stmt.setString(24, proveedoresForm.getNombreSucursalDollar().toUpperCase());
            stmt.setString(25, proveedoresForm.getNumeroCuentaDollar().toUpperCase());
            stmt.setString(26, proveedoresForm.getCuentaClabeDollar().toUpperCase());
            stmt.setString(27, proveedoresForm.getNumeroConvenioDollar().toUpperCase());
            stmt.setString(28, proveedoresForm.getMonedaDollar().toUpperCase());
            stmt.setString(29, proveedoresForm.getAbaDollar().toUpperCase());
            stmt.setString(30, proveedoresForm.getSwitfCodeDollar().toUpperCase());
            stmt.setString(31, proveedoresForm.getBancoOtro().toUpperCase());
            stmt.setString(32, proveedoresForm.getSucursalOtro().toUpperCase());
            stmt.setString(33, proveedoresForm.getNombreSucursalOtro().toUpperCase());
            stmt.setString(34, proveedoresForm.getNumeroCuentaOtro().toUpperCase());
            stmt.setString(35, proveedoresForm.getCuentaClabeOtro().toUpperCase());
            stmt.setString(36, proveedoresForm.getNumeroConvenioOtro().toUpperCase());
            stmt.setString(37, proveedoresForm.getMonedaOtro().toUpperCase());
            stmt.setString(38, proveedoresForm.getAbaOtro().toUpperCase());
            stmt.setString(39, proveedoresForm.getSwitfCodeOtro().toUpperCase());
            stmt.setString(40, proveedoresForm.getUsuarioGeneracion());
            stmt.setString(41, proveedoresForm.getTipoConfirmacion().toUpperCase());
            stmt.setString(42, proveedoresForm.getEmail());
            stmt.setString(43, proveedoresForm.getAnexo24().toUpperCase());
            stmt.setString(44, proveedoresForm.getIdProveedor().toUpperCase());
            stmt.setString(45, proveedoresForm.getLimiteTolerancia().toUpperCase());
            
            String razonGramatica = Gramatica.aplicarGramatica(proveedoresForm.getRazonSocial().toUpperCase());
            stmt.setString(46, razonGramatica);
            stmt.setString(47, proveedoresForm.getNotComUsuario());
            stmt.setString(48, proveedoresForm.getNotPagoUsuario());
            stmt.setString(49, proveedoresForm.getBandDescuento());
            stmt.setString(50, proveedoresForm.getConServicio());
            stmt.setString(51, proveedoresForm.getFormaPago());
            stmt.setInt(52, proveedoresForm.getNumEstrellas());
            stmt.setString(53, proveedoresForm.getLimiteComplemento());
            stmt.setString(54, proveedoresForm.getNumeroCuentaProveedor());
            stmt.setString(55, proveedoresForm.getCentroCostos());
            stmt.setString(56, proveedoresForm.getRazonProveedor());
            stmt.setString(57, proveedoresForm.getPagoDolares());
            stmt.setString(58, proveedoresForm.getPagoPesos());
            stmt.setString(59, proveedoresForm.getAMERICANOS_SERIE());
            stmt.setInt(60, proveedoresForm.getAMERICANOS_FOLIO());
            stmt.setString(61, proveedoresForm.getAMERICANOS_ACCESO());
            
            stmt.setString(62, proveedoresForm.getBandIMSS());
            stmt.setString(63, proveedoresForm.getBandSAT());
            stmt.setString(64, proveedoresForm.getTieneIMSS());
            stmt.setString(65, proveedoresForm.getTieneSAT());
            stmt.setString(66, proveedoresForm.getTieneConfidencial());
            stmt.setString(67, proveedoresForm.getServEsp());
            stmt.setString(68, proveedoresForm.getNumRegistro());
            
            stmt.setString(69, proveedoresForm.getCartaPorte());
            stmt.setString(70, proveedoresForm.getRegimenFiscal());
            
            int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
            }
        }
        catch(SQLException sql){
        	resultado = sql.getErrorCode();
        	Utils.imprimeLog("altaProveedores ", sql);
            
        }catch(Exception e){
        	resultado = 100;
            Utils.imprimeLog("altaProveedores 2 ", e);
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

	
	
	public int actualizaProveedores(Connection con, String esquema, ProveedoresForm proveedoresForm, String usuarioTrans ) {
        PreparedStatement stmt = null;
        int resultado = 0;
        try{
        	stmt = con.prepareStatement(ProveedoresQuerys.getQueryactualizaProveedor(esquema));

        	int numParam=1;
            stmt.setString(numParam++, proveedoresForm.getRazonSocial().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getCalle().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroInt().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroExt().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getColonia().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getDelegacion().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getCiudad().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getEstado().toUpperCase());
            stmt.setInt(numParam++, proveedoresForm.getCodigoPostal());
            stmt.setString(numParam++, proveedoresForm.getTelefono().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getExtencion().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNombreContacto().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getTipoProveedor().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getBanco().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getSucursal().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNombreSucursal().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroCuenta().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getCuentaClabe().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroConvenio().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getMoneda().toUpperCase());

            stmt.setString(numParam++, proveedoresForm.getBancoDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getSucursalDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNombreSucursalDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroCuentaDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getCuentaClabeDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroConvenioDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getMonedaDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getAbaDollar().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getSwitfCodeDollar().toUpperCase());

            stmt.setString(numParam++, proveedoresForm.getBancoOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getSucursalOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNombreSucursalOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroCuentaOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getCuentaClabeOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getNumeroConvenioOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getMonedaOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getAbaOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getSwitfCodeOtro().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getUsuarioGeneracion().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getTipoConfirmacion().toUpperCase());
            stmt.setString(numParam++, proveedoresForm.getEmail());
            stmt.setString(numParam++, proveedoresForm.getAnexo24().toUpperCase());
            //stmt.setString(44, Utils.rellenaCeros(proveedoresForm.getIdProveedor().toUpperCase(), proveedoresForm.getIdProveedor().length()));
            stmt.setString(numParam++, proveedoresForm.getLimiteTolerancia().toUpperCase());
            String razonGramatica = Gramatica.aplicarGramatica(proveedoresForm.getRazonSocial().toUpperCase());
            stmt.setString(numParam++, razonGramatica);
            stmt.setString(numParam++, proveedoresForm.getNotComUsuario());
            stmt.setString(numParam++, proveedoresForm.getNotPagoUsuario());
            stmt.setString(numParam++, proveedoresForm.getBandDescuento());
            stmt.setString(numParam++, proveedoresForm.getConServicio());
            stmt.setString(numParam++, proveedoresForm.getFormaPago());
            stmt.setInt(numParam++, proveedoresForm.getNumEstrellas());
            stmt.setString(numParam++, proveedoresForm.getLimiteComplemento());
            stmt.setString(numParam++, proveedoresForm.getNumeroCuentaProveedor());
            stmt.setString(numParam++, proveedoresForm.getCentroCostos());
            stmt.setString(numParam++, proveedoresForm.getRazonProveedor());
            stmt.setString(numParam++, proveedoresForm.getPagoDolares());
            stmt.setString(numParam++, proveedoresForm.getPagoPesos());
            stmt.setString(numParam++, proveedoresForm.getAMERICANOS_SERIE());
            stmt.setInt(numParam++, proveedoresForm.getAMERICANOS_FOLIO());
            stmt.setString(numParam++, proveedoresForm.getAMERICANOS_ACCESO());
            
            stmt.setString(numParam++, proveedoresForm.getBandIMSS());
            stmt.setString(numParam++, proveedoresForm.getBandSAT());
            stmt.setString(numParam++, proveedoresForm.getTieneIMSS());
            stmt.setString(numParam++, proveedoresForm.getTieneSAT());
            stmt.setString(numParam++, proveedoresForm.getTieneConfidencial());
            stmt.setString(numParam++, proveedoresForm.getConfirmarIMSS());
            stmt.setString(numParam++, proveedoresForm.getConfirmarSAT());
            stmt.setString(numParam++, proveedoresForm.getServEsp());
            stmt.setString(numParam++, proveedoresForm.getNumRegistro());
            
            stmt.setString(numParam++, proveedoresForm.getCartaPorte());
            stmt.setString(numParam++, proveedoresForm.getRegimenFiscal());
            stmt.setString(numParam++, usuarioTrans);
            stmt.setInt(numParam++, proveedoresForm.getClaveRegistro());
            
            logger.info("stmt==>"+stmt);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("actualizaProveedores(): ", e);
        }
        finally{
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
	
	
	
	
	public int actualizaEstatus(Connection con, String esquema, int claveRegistro, String usuarioTrans ) {
        PreparedStatement stmt = null;
        int resultado = 0;
        try{
        	stmt = con.prepareStatement(ProveedoresQuerys.getActualizaEstatus(esquema));

        	stmt.setString(1, "A");
            stmt.setString(2, usuarioTrans);
            stmt.setInt(3, claveRegistro);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("actualizaEstatus(): ", e);
        }
        finally{
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
	
	
	public int eliminaProveedores(Connection con, String esquema, int claveProveedor, String usuarioTran )
    {
        PreparedStatement stmt = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(ProveedoresQuerys.getQueryEliminaProveedor(esquema));
        	stmt.setString(1, "D");
        	stmt.setString(2, usuarioTran);
        	stmt.setInt(3, claveProveedor);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("eliminaProveedores ", e);
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
	
	
	public int altaProveedoresMail(Connection con, String esquema, ProveedoresForm proveedoresForm ) {
        PreparedStatement stmt = null;
        int resultado = 0;
        try {
        	stmt = con.prepareStatement(ProveedoresQuerys.getQueryAltaProveedorMail(esquema));
        	stmt.setInt(1, proveedoresForm.getClaveRegistro());
        	stmt.setString(3, "S");
        	
        	if (!"".equals(proveedoresForm.getEmail1())){
        		stmt.setString(2, proveedoresForm.getEmail1());
        		stmt.setString(4, validaTipoCorreo(proveedoresForm.getTipoEmail1()));
        		stmt.setString(5, validaTipoCorreo(proveedoresForm.getTipoEmail2()));
        		stmt.executeUpdate();
        	}
        	if (!"".equals(proveedoresForm.getEmail2())){
        		stmt.setString(2, proveedoresForm.getEmail2());
        		stmt.setString(4, validaTipoCorreo(proveedoresForm.getTipoEmail3()));
        		stmt.setString(5, validaTipoCorreo(proveedoresForm.getTipoEmail4()));
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail3())){
        		stmt.setString(2, proveedoresForm.getEmail3());
        		stmt.setString(4, validaTipoCorreo(proveedoresForm.getTipoEmail5()));
        		stmt.setString(5, validaTipoCorreo(proveedoresForm.getTipoEmail6()));
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail4())){
        		stmt.setString(2, proveedoresForm.getEmail4());
        		stmt.setString(4, validaTipoCorreo(proveedoresForm.getTipoEmail7()));
        		stmt.setString(5, validaTipoCorreo(proveedoresForm.getTipoEmail8()));
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail5())){
        		stmt.setString(2, proveedoresForm.getEmail5());
        		stmt.setString(4, validaTipoCorreo(proveedoresForm.getTipoEmail9()));
        		stmt.setString(5, validaTipoCorreo(proveedoresForm.getTipoEmail10()));
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail6())){
        		stmt.setString(2, proveedoresForm.getEmail6());
        		stmt.setString(4, "N");
        		stmt.setString(5, "N");
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail7())){
        		stmt.setString(2, proveedoresForm.getEmail7());
        		stmt.setString(4, "N");
        		stmt.setString(5, "N");
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail8())){
        		stmt.setString(2, proveedoresForm.getEmail8());
        		stmt.setString(4, "N");
        		stmt.setString(5, "N");
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail9())){
        		stmt.setString(2, proveedoresForm.getEmail9());
        		stmt.setString(4, "N");
        		stmt.setString(5, "N");
        		stmt.executeUpdate();	
        	}
        	if (!"".equals(proveedoresForm.getEmail10())){
        		stmt.setString(2, proveedoresForm.getEmail10());
        		stmt.setString(4, "N");
        		stmt.setString(5, "N");
        		stmt.executeUpdate();	
        	}
        }
        catch(Exception e){
        	Utils.imprimeLog("altaProveedoresMail ", e);
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
	
	
	public int eliminaProveedoresMail(Connection con, String esquema, int claveProveedor )
	{
	    PreparedStatement stmt = null;
	    int resultado = 0;
	    try
	    {
	    	stmt = con.prepareStatement(ProveedoresQuerys.getQueryEliminaProveedorMail(esquema));
	    	stmt.setInt(1, claveProveedor);
	    	stmt.setString(2, "P");
	    	stmt.executeUpdate();
	    }
	    catch(Exception e){
	    	Utils.imprimeLog("eliminaProveedoresMail ", e);
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

	
	
	public ArrayList<ProveedoresForm> exportarCertificados (Connection con, String esquema, String [] listaProveedores)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ProveedoresForm> listaDetalle = new ArrayList<>();
        ProveedoresForm proveedoresForm = new ProveedoresForm();
        try
        {
        	StringBuffer sbQuery = new StringBuffer(ProveedoresQuerys.getQueryListaExportarZIP(esquema));
        	
        	for (int x = 0; x < listaProveedores.length; x++) {
        		sbQuery.append(" ?,");
        	}
        	String queryFinal = sbQuery.substring(0, sbQuery.length() - 1) + ")";
        	
            stmt = con.prepareStatement(queryFinal);
            
            int contador = 1;
            for (int x = 0; x < listaProveedores.length; x++) {
        		stmt.setInt(contador++, Integer.parseInt( listaProveedores[x]));
        	}
            
            logger.info("stmt===>"+stmt);
            rs = stmt.executeQuery();
            String tieneDocumentoIMSS = null;
            String tieneDocumentoSAT = null;
            final String S = "S";
            boolean tieneDocumento = false;
			while(rs.next()){
				tieneDocumento = false;
				tieneDocumentoIMSS = Utils.noNulo(rs.getString(3));
				tieneDocumentoSAT = Utils.noNulo(rs.getString(4));
				
				if (S.equalsIgnoreCase(tieneDocumentoIMSS)) {
					tieneDocumento = true;
				}
				
				if (S.equalsIgnoreCase(tieneDocumentoSAT)) {
					tieneDocumento = true;
				}
				
				if (tieneDocumento) {
					proveedoresForm.setClaveRegistro(rs.getInt(1));
					proveedoresForm.setRfc(Utils.noNulo(rs.getString(2)));
					listaDetalle.add(proveedoresForm);
					proveedoresForm = new ProveedoresForm();
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
	

	
	public int eliminarCertificados (Connection con, String esquema, String [] listaProveedores)
    {
        PreparedStatement stmt = null;
        int resultado = 0;
        final String N = "N";
        try{
        	StringBuffer sbQuery = new StringBuffer(ProveedoresQuerys.getQueryEliminarCertificados(esquema));
        	
        	for (int x = 0; x < listaProveedores.length; x++) {
        		sbQuery.append(" ?,");
        	}
        	String queryFinal = sbQuery.substring(0, sbQuery.length() - 1) + ")";
        	stmt = con.prepareStatement(queryFinal);
            
        	stmt.setString(1, N);
        	stmt.setString(2, N);
        	stmt.setString(3, N);
        	stmt.setString(4, N);
        	
            int contador = 5;
            for (int x = 0; x < listaProveedores.length; x++) {
        		stmt.setInt(contador++, Integer.parseInt( listaProveedores[x]));
        	}
            resultado =  stmt.executeUpdate();
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
        return resultado;
    }
	
	
	
	public ProveedoresForm infoProveedoresCertificados(Connection con, String esquema, int claveRegistro, String tipoCertificado)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ProveedoresForm provForm = new ProveedoresForm();
        StringBuffer sbQuery = new StringBuffer();
        try
        {
        	sbQuery.append(ProveedoresQuerys.getQueryInfoProveedorCertificado(esquema));
        	stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, claveRegistro);
            
            rs = stmt.executeQuery();
			
            String TIENE_DOCUMENTO_IMSS = null;
            String TIENE_DOCUMENTO_SAT = null;
            String CONFIRMAR_DOCUMENTO_IMSS = null;
            String CONFIRMAR_DOCUMENTO_SAT = null;
            
            if(rs.next()){
            	if ("IMSS".equalsIgnoreCase(tipoCertificado)) {
            		TIENE_DOCUMENTO_IMSS = Utils.noNulo(rs.getString(4));	
            		CONFIRMAR_DOCUMENTO_IMSS = Utils.noNulo(rs.getString(6));
            		if ("".equals(CONFIRMAR_DOCUMENTO_IMSS)) {
            			CONFIRMAR_DOCUMENTO_IMSS = "N";
            		}
            		if ("S".equalsIgnoreCase(TIENE_DOCUMENTO_IMSS) && "N".equalsIgnoreCase(CONFIRMAR_DOCUMENTO_IMSS)) {
            			provForm.setClaveRegistro(rs.getInt(1));
            			provForm.setRazonSocial(Utils.noNuloNormal(rs.getString(2)));
            			provForm.setRfc(Utils.noNuloNormal(rs.getString(3)));
            			provForm.setNombreArchivo("RFC"+Utils.noNulo(rs.getString(3)) +"_" + "Cert_IMSS.pdf");
            			provForm.setEstatusRegistro("true");
            			
            			/*
        				jsonobj.put("claveRegistro",rs.getInt(1));
        				jsonobj.put("razonSocial",Utils.noNulo(rs.getString(2)));
        				jsonobj.put("rfc",Utils.noNulo(rs.getString(3)));
        				jsonobj.put("nombrePDF","RFC"+Utils.noNulo(rs.getString(3)) +"_" + "Cert_IMSS.pdf");
        				jsonobj.put("VALIDO","true");
        				*/
            		}
            	}
            	

            	if ("SAT".equalsIgnoreCase(tipoCertificado)) {
            		TIENE_DOCUMENTO_SAT = Utils.noNulo(rs.getString(5));	
            		CONFIRMAR_DOCUMENTO_SAT = Utils.noNulo(rs.getString(7));
            		if ("".equals(CONFIRMAR_DOCUMENTO_SAT)) {
            			CONFIRMAR_DOCUMENTO_SAT = "N";
            		}
            		if ("S".equalsIgnoreCase(TIENE_DOCUMENTO_SAT) && "N".equalsIgnoreCase(CONFIRMAR_DOCUMENTO_SAT)) {
            			provForm.setClaveRegistro(rs.getInt(1));
            			provForm.setRazonSocial(Utils.noNuloNormal(rs.getString(2)));
            			provForm.setRfc(Utils.noNuloNormal(rs.getString(3)));
            			provForm.setNombreArchivo("RFC"+Utils.noNulo(rs.getString(3)) +"_" + "Cert_SAT.pdf");
            			provForm.setEstatusRegistro("true");
            			/*
        				jsonobj.put("claveRegistro",rs.getInt(1));
        				jsonobj.put("razonSocial",Utils.noNulo(rs.getString(2)));
        				jsonobj.put("rfc",Utils.noNulo(rs.getString(3)));
        				jsonobj.put("nombrePDF","RFC"+Utils.noNulo(rs.getString(3)) +"_" + "Cert_SAT.pdf");
        				jsonobj.put("VALIDO","true");
        				*/
            		}
            	}
            }
            
        	if (provForm.getClaveRegistro() == 0) {
        		provForm.setEstatusRegistro("false");
        	}

        	
        }
        catch(Exception e){
        	Utils.imprimeLog("infoProveedores ", e);
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
        return provForm;
    }
	
	
	public int confirmarCertificadoCompras(Connection con, String esquema, ProveedoresForm provForm, String tipoCertificado)
    {
        PreparedStatement stmt = null;
        int resultado = 0;
        try{
        	
        	if ("IMSS".equalsIgnoreCase(tipoCertificado)) {
        		stmt = con.prepareStatement(ProveedoresQuerys.getQueryActualizaCertificadoComprasIMSS(esquema));	
        	}else {
        		stmt = con.prepareStatement(ProveedoresQuerys.getQueryActualizaCertificadoComprasSAT(esquema));	
        	}
        	
        	if ("IMSS".equalsIgnoreCase(tipoCertificado)) {
        		stmt.setString(1, provForm.getConfirmarIMSS());
        		stmt.setString(2, provForm.getTieneIMSS());
        	}else {
        		stmt.setString(1, provForm.getConfirmarSAT());
        		stmt.setString(2, provForm.getTieneSAT());
        	}
        	stmt.setInt(3, provForm.getClaveRegistro());
        	
        	logger.info("stmtActualiza===>"+stmt);
            resultado = stmt.executeUpdate();
        }catch(Exception e){
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
        return resultado;
    }

	

	public ArrayList<ProveedoresForm> comboProveedores(Connection con, String esquema, String bandTareas, String tipoProveedor, String idLengueje) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ProveedoresForm proveedoresForm = new ProveedoresForm();
        ArrayList<ProveedoresForm> listaProveedores = new ArrayList<>();
        LenguajeBean lenguajeBean = LenguajeBean.instance();
        
        try{
        	
        	if ("".equalsIgnoreCase(Utils.noNulo(bandTareas)) ) {
        		HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(idLengueje, "PAN_MSG_GENERALES");
    			String msgSeleccione = null;
    			if ("".equalsIgnoreCase(Utils.noNulo(mapaLenguaje.get("MSG_SELECCION_PROVEE")))) {
    				msgSeleccione = "Seleccione un Proveedor";
    			}else {
    				msgSeleccione = mapaLenguaje.get("MSG_SELECCION_PROVEE");
    			}
    			
    			proveedoresForm.setClaveRegistro(0);
    			proveedoresForm.setRfc("");
    	        proveedoresForm.setRazonSocial(msgSeleccione);
    	        listaProveedores.add(proveedoresForm);
    	        proveedoresForm = new ProveedoresForm();
        	}
        	
	        
			
        	StringBuffer sbQuery = new StringBuffer(ProveedoresQuerys.getComboProveedores(esquema));
        	
        	if ("MEX".equalsIgnoreCase(tipoProveedor) || "USA".equalsIgnoreCase(tipoProveedor)) {
        		sbQuery.append(" and TIPO_PROVEEDOR = ?");
        	}
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setString(1, "A");
	        
            if ("MEX".equalsIgnoreCase(tipoProveedor) || "USA".equalsIgnoreCase(tipoProveedor)) {
            	stmt.setString(2, tipoProveedor);
        	}
            sbQuery.append(" order by RAZON_SOCIAL");
            
            rs = stmt.executeQuery();
	        
	        while (rs.next()){
	        	
	        	proveedoresForm.setClaveRegistro(rs.getInt(1));
		        proveedoresForm.setRfc(Utils.noNuloNormal(rs.getString(3)));
		        proveedoresForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(2))));
		        // proveedoresForm.setRazonSocial(Utils.noNuloNormal(rs.getString(3)) + " - " +Utils.noNuloNormal(rs.getString(2)));
		        listaProveedores.add(proveedoresForm);
		        proveedoresForm = new ProveedoresForm();
	        }
	        
	        
        } catch(Exception e){
        	Utils.imprimeLog("comboProveedores(): ", e);
        } finally{
        	try{
   	        	 if(rs != null) {
   	                rs.close();
   	            }
   	            rs = null;
   	            if(stmt != null) {
   	                stmt.close();
   	            }
   	            stmt = null;
        	}
        	catch(Exception e){
        		rs = null;
        		stmt = null;
        	}
        }
        return listaProveedores;
    }
	
	
	public ArrayList<ProveedoresForm> comboProveedoresExt(Connection con, String esquema) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ProveedoresForm proveedoresForm = new ProveedoresForm();
        ArrayList<ProveedoresForm> listaProveedores = new ArrayList<>();

        try{
            stmt = con.prepareStatement(ProveedoresQuerys.getQueryProveedoresCP(esquema));
            stmt.setString(1, "S");
	        rs = stmt.executeQuery();
	        
	        proveedoresForm.setClaveRegistro(0);
	        proveedoresForm.setRazonSocial("Seleccione proveedor");
	        listaProveedores.add(proveedoresForm);
	        proveedoresForm = new ProveedoresForm();
	        
	        while (rs.next()){
	        	
	        	proveedoresForm.setClaveRegistro(rs.getInt(1));
		        proveedoresForm.setRazonSocial(Utils.noNuloNormal(rs.getString(3)) + " - " +Utils.noNuloNormal(rs.getString(2)));
		        listaProveedores.add(proveedoresForm);
		        proveedoresForm = new ProveedoresForm();
		        
	        }
        }
        catch(Exception e){
        	Utils.imprimeLog("comboProveedores(): ", e);
        }
        finally{
        	try{
   	        	 if(rs != null) {
   	                rs.close();
   	            }
   	            rs = null;
   	            if(stmt != null) {
   	                stmt.close();
   	            }
   	            stmt = null;
        	}
        	catch(Exception e){
        		rs = null;
        		stmt = null;
        	}
        }
        return listaProveedores;
    }
	
	
	
	public ArrayList<String> buscarRFCxNumero(Connection con, String esquema, String numProveedor)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> datos = new ArrayList<String>();
        try
        {
            stmt = con.prepareStatement(ProveedoresQuerys.getQueryBuscarNumeroProveedor(esquema));
            stmt.setString(1, numProveedor);
            rs = stmt.executeQuery();
			if (rs.next()) 
            {
				datos.add(Utils.noNulo(rs.getString(1))); // CLAVE
				datos.add(Utils.noNulo(rs.getString(2))); // RFC
				datos.add(Utils.noNulo(rs.getString(3))); // RIPO
            }
        }catch(Exception e){
        	Utils.imprimeLog("buscarRFCxNumero ", e);
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
	
	
	
	private String getAnexo24(String anexo24){
		if ("1".equals(anexo24)){
			return "S";
		}
		return "N";
	}


	public String getFormaPago(String formaPago){
		if (formaPago.equalsIgnoreCase("CHK")){
			return "CHEQUE";
		}else if (formaPago.equalsIgnoreCase("WIR")){
			return "TRANSFERENCIA";
		}else{
			return "";
		}
		
	}

	private String validaTipoCorreo(String bandTipo){
		if ("S".equalsIgnoreCase(bandTipo)){
			return "S";
		}
		return "N";
	}
	
	public void obtenerCertificados(Connection con, String esquema, int claveProveedor, ProveedoresForm form) {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        stmt = con.prepareStatement(ProveedoresQuerys.GET_CERTIFICADO_PROVEEDOR);
	        stmt.setInt(1, claveProveedor);
	        logger.info("obtenerCertificados -> " + stmt);

	        rs = stmt.executeQuery();
	       
	        if (rs.next()) {

	            
	            form.setPasswordSat(Utils.noNulo(rs.getString(1)));  // PASSWORD_SAT
	            form.setArchivoCer(Utils.noNulo(rs.getString(2)));   // ARCHIVO_CER
	            form.setArchivoKey(Utils.noNulo(rs.getString(3)));   // ARCHIVO_KEY
	            form.setNumeroCertificado(Utils.noNulo(rs.getString(4))); // NUMERO_CERTIFICADO

	            // Tiene certificado si ARCHIVO_CER no está vacío
	            form.setTieneCertificado(
	                !Utils.noNulo(rs.getString(2)).isEmpty() ? "S" : "N"
	            );
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("obtenerCertificados()", e);

	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ex) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
	    }
	}


	
	public int guardarCertificadosProveedor(
	        Connection con,
	        int claveProveedor,
	        String passwordSat,
	        String archivoCer,
	        String archivoKey,
	        String numeroCertificado
	) {
	    PreparedStatement stmt = null;

	    try {
	        stmt = con.prepareStatement(ProveedoresQuerys.UPDATE_CERTIFICADOS_PROVEEDOR);
	        stmt.setString(1, passwordSat);
	        stmt.setString(2, archivoCer);
	        stmt.setString(3, archivoKey);
	        stmt.setString(4, numeroCertificado);
	        stmt.setInt(5, claveProveedor);
	        
	        
	        logger.info("guardarCertificadosProveedor -> " + stmt);
	        return stmt.executeUpdate();

	    } catch (Exception e) {
	        Utils.imprimeLog("guardarCertificadosProveedor()", e);
	        return -1;

	    } finally {
	        try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
	    }
	}



}
