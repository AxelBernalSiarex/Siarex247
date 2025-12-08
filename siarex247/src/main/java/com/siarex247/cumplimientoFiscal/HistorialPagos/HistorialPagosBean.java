package com.siarex247.cumplimientoFiscal.HistorialPagos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Bitacora.BitacoraBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;

public class HistorialPagosBean {

    public static final Logger logger = Logger.getLogger("siarex247");
    private PreparedStatement stmt = null;
    
   public int insertarHistorialPago(HistorialPagosForm historialPagosForm) {
        int res = 0;
        try {
        	// logger.info("🔁 ENTRO A INSERTAR N → " + ps);
        	stmt.setString(1, historialPagosForm.getRfc());
        	stmt.setString(2, historialPagosForm.getFechaPago());
        	stmt.setString(3, historialPagosForm.getUuidFactura());
        	stmt.setString(4, historialPagosForm.getTipoMoneda());
        	stmt.setDouble(5, historialPagosForm.getTotal());
        	stmt.setString(6, historialPagosForm.getUsuarioTran());

            res = stmt.executeUpdate();

        } catch (Exception e) {
            Utils.imprimeLog("", e);
            res = -100;

        } 

        return res;
    }


    public ArrayList<HistorialPagosForm> listarHistorialPagos(Connection con, String esquema) {
        ArrayList<HistorialPagosForm> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement( HistorialPagosQuery.getLista(esquema) );
            logger.info("🔁 DetalleHistorialP N → " + ps);
            rs = ps.executeQuery();
// ID_REGISTRO, RFC, FECHA_PAGO, UUID_FACTURA, TIPO_MONEDA, TOTAL, ESTATUS,  CODIGO_ERROR, UUID_COMPLEMENTO
            String estatus = "";
            String codError = "";
            while (rs.next()) {
                HistorialPagosForm historialPagosForm = new HistorialPagosForm();
                estatus = Utils.noNulo(rs.getString(7));
                codError = Utils.noNulo(rs.getString(8));
                historialPagosForm.setIdRegistro(rs.getInt(1));
                historialPagosForm.setRfc(Utils.noNuloNormal(rs.getString(2)));
                historialPagosForm.setFechaPago(Utils.noNulo(rs.getString(3)));
                historialPagosForm.setUuidFactura(Utils.noNuloNormal(rs.getString(4)));
                historialPagosForm.setTipoMoneda(Utils.noNulo(rs.getString(5)));
                historialPagosForm.setTotal(rs.getDouble(6));
                historialPagosForm.setEstatus(estatus + " - " + desEstatus(estatus, codError));
                historialPagosForm.setUuidComplemento(Utils.noNulo(rs.getString(9)));
                lista.add(historialPagosForm);
            }

        } catch (Exception e) {
            Utils.imprimeLog("", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
        }

        return lista;
    }
    
    public HashMap<String, String> procesarArchivoTXT( Connection con, String esquema, String pathArchivoCompleto, String nombreArchivo, String usuarioHTTP) {
        HashMap<String, String> mapaResultado = new HashMap<>();
        Integer resArreglo [] = {0,0};
        try {
        	mapaResultado.put("BAND_MENSAJE", "OK");
			mapaResultado.put("OK", "0");
			mapaResultado.put("NG", "0");
			mapaResultado.put("ID_TAREA", "0");
			mapaResultado.put("ERROR_COLUMNAS", "false");
			
			BitacoraBean bitacoraBean = new BitacoraBean();
			int numBitacora = bitacoraBean.altaBitacora(con, esquema, nombreArchivo, 0, "FOR", 0, 0, 0, usuarioHTTP);
			mapaResultado.put("ID_TAREA", String.valueOf(numBitacora));
			
			stmt = con.prepareStatement( HistorialPagosQuery.getInsertar(esquema) );
			
			
        	ArrayList<String> listaTXT = UtilsFile.leeArchivoTXT(pathArchivoCompleto);
        	List<String> lineScan = null;
        	int numRegistro=0;
        	int totColumnas = 5;
        	HistorialPagosForm historialPagosForm = new HistorialPagosForm();
        	
        	for (int x = 0; x < listaTXT.size(); x++) {
        		lineScan = Utils.parseLine(listaTXT.get(x), ';');
        		numRegistro++;
				if (numRegistro == 1) {
					if (totColumnas == lineScan.size()) {
						
					}else {
						mapaResultado.put("ERROR_COLUMNAS", "true");
						break;
					}
				}else {
				
					historialPagosForm.setRfc(Utils.eliminarGuiones(Utils.noNulo(lineScan.get(0))));
					historialPagosForm.setFechaPago(Utils.noNulo(lineScan.get(1)));
					historialPagosForm.setUuidFactura(Utils.noNulo(lineScan.get(2)));
					historialPagosForm.setTipoMoneda(Utils.noNulo(lineScan.get(3)));
					historialPagosForm.setTotal(Utils.noNuloDouble(lineScan.get(4)));
					historialPagosForm.setUsuarioTran(usuarioHTTP);
					
					if (historialPagosForm.getRfc().length() != 12 && historialPagosForm.getRfc().length() != 13) {
						resArreglo[0]++;
						bitacoraBean.altaHistorico(con, esquema, numBitacora, String.valueOf(numRegistro), "El registro "+numRegistro+", no cumple con la longitud del RFC" ) ;
						mapaResultado.put("BAND_MENSAJE", "ERROR");
						
					}else if (!UtilsFechas.esFechaFormatoValido(historialPagosForm.getFechaPago())) {
						resArreglo[0]++;
						bitacoraBean.altaHistorico(con, esquema, numBitacora, String.valueOf(numRegistro), "El registro "+numRegistro+", no cumple con el formato de la fecha de pago") ;
						mapaResultado.put("BAND_MENSAJE", "ERROR");
					}else if (!"MXN".equalsIgnoreCase(historialPagosForm.getTipoMoneda()) && !"USD".equalsIgnoreCase(historialPagosForm.getTipoMoneda())) {
						resArreglo[0]++;
						bitacoraBean.altaHistorico(con, esquema, numBitacora, String.valueOf(numRegistro), "El registro "+numRegistro+", no cumple con el tipo de moneda (MXN/USD)") ;
						mapaResultado.put("BAND_MENSAJE", "ERROR");
					}else {
						int res = insertarHistorialPago(historialPagosForm);

	                    if (res > 0) {
	                    	resArreglo[1]++;
	                    } else {
	                    	resArreglo[0]++;
	                    	bitacoraBean.altaHistorico(con, esquema, numBitacora, String.valueOf(numRegistro), "El registro "+numRegistro+", con folio fiscal "+historialPagosForm.getUuidFactura()+", ya existe en nuestra base de datos") ;
							mapaResultado.put("BAND_MENSAJE", "ERROR");
	                        //throw new Exception("La BD no guardó el registro (Posible duplicado).");
	                    }
					}
				}
        	}
        	
			logger.info("El usuario "+usuarioHTTP + " ha importado "+resArreglo.length+" registros de historico de pagos de los cuales "+resArreglo[1] +" fueron exitosos y "+resArreglo[0] + " No exitosos");
			mapaResultado.put("OK", String.valueOf(resArreglo[1]));
			mapaResultado.put("NG", String.valueOf(resArreglo[0]));
			bitacoraBean.updateBitacora(con, esquema, numBitacora, numRegistro, resArreglo[1], resArreglo[0], 1, usuarioHTTP); // se actualiza la bitacora

        } catch (Exception e) {
            logger.error("", e);
            // jsonResponse.put("codError", "999");
            /// jsonResponse.put("mensajeError", "Error crítico leyendo el archivo: " + e.getMessage());
        }finally {
            try { 
            	if (stmt != null) 
            		stmt.close();
            	stmt = null;
            } catch (Exception ex) {
            	stmt = null;
            }
        }
        return mapaResultado;
    }
    
    private String desEstatus(String estatus, String codError) {
    	try {
    		if ("C01".equalsIgnoreCase(estatus)) {
    			return "Sin Complemento de Pago";
    		}else if ("C02".equalsIgnoreCase(estatus)) {
    			return "Con Complemento de Pago";
    		}else if ("C20".equalsIgnoreCase(estatus)) {
    			if ("001".equalsIgnoreCase(codError)) {
    				return "RFC Emisor del complemento es diferente al de la factura";
    			}else if ("002".equalsIgnoreCase(codError)) {
    				return "Tipo de Moneda de su factura, es diferente al de su complemento de pago";
    			}else if ("003".equalsIgnoreCase(codError)) {
    				return "Total Pagado de su factura, es diferente al de su complemento de pago";
    			}else if ("004".equalsIgnoreCase(codError)) {
    				return "No coincide la fecha de pago de su factura vs fecha de pago del complemento";
    			}else if ("005".equalsIgnoreCase(codError)) {
    				return "El folio de su factura ya fue asignado a otro complemento de pago";
    			}else if ("006".equalsIgnoreCase(codError)) {
    				return "Complemento de pago, se encuentra cancelado en el SAT Servicio de Administraci(o)n Tributaria";
    			}else if ("007".equalsIgnoreCase(codError)) {
    				return "Sin Complemento de Pago";
    			}
    		}else {
    			return "Sin Complemento de Pago";
    		}
    	}catch(Exception e) {
    		Utils.imprimeLog("", e);
    	}
    	return "";
    }
}
