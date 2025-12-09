package com.siarex247.cumplimientoFiscal.HistorialPagos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.seguridad.Bitacora.BitacoraBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;

public class HistorialPagosBean {

    public static final Logger logger = Logger.getLogger("siarex247");
    private PreparedStatement stmt = null;
    
    public int insertarHistorialPago(HistorialPagosForm historialPagosForm, Connection con, String esquema) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            // =====================================================
            // 1) VALIDAR DUPLICADO SIN PROVOCAR EXCEPCIÓN SQL
            // =====================================================
            String sqlDup = HistorialPagosQuery.getValidarDuplicado(esquema);
            ps = con.prepareStatement(sqlDup);
            ps.setString(1, historialPagosForm.getUuidFactura());
            rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return 0; // 0 = duplicado
            }

            rs.close();
            ps.close();

            // =====================================================
            // 2) INSERTAR REGISTRO
            // =====================================================
            String sqlInsert = HistorialPagosQuery.getInsertar(esquema);
            ps = con.prepareStatement(sqlInsert);

            int i = 1;
            ps.setString(i++, historialPagosForm.getRfc());
            ps.setString(i++, historialPagosForm.getFechaPago());
            ps.setString(i++, historialPagosForm.getUuidFactura());
            ps.setString(i++, historialPagosForm.getTipoMoneda());
            ps.setDouble(i++, historialPagosForm.getTotal());
            ps.setString(i++, historialPagosForm.getUsuarioTran());

            int res = ps.executeUpdate();

            return (res > 0 ? 1 : -1);  // 1 = OK, -1 = error

        } catch (Exception e) {

            Utils.imprimeLog("insertarHistorialPago", e);
            return -1;

        } finally {

            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }
    }



    public ArrayList<HistorialPagosForm> listarHistorialPagos(Connection con, String esquema, String rfc) {
        ArrayList<HistorialPagosForm> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sbQuery = new StringBuffer();

        try {
        	sbQuery.append(HistorialPagosQuery.getLista(esquema));
        	
        	if ("".equals(rfc)) {
        		sbQuery.append("  order by FECHA_PAGO");
        		
        	} else {
        	   sbQuery.append(" where RFC = ? order by FECHA_PAGO");
        	}
        	
        	
            ps = con.prepareStatement(sbQuery.toString());
            
            if (!"".equals(rfc)) {
            	ps.setString(1, rfc);
            }
            
            
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
    
    public HashMap<String, String> procesarArchivoTXT(
            Connection con,
            String esquema,
            String pathArchivoCompleto,
            String nombreArchivo,
            String usuarioHTTP,
            String rfcEmpresaBD) {

        HashMap<String, String> mapaResultado = new HashMap<>();
        Integer resArreglo[] = {0, 0}; // [NG, OK]

        try {

            mapaResultado.put("BAND_MENSAJE", "OK");
            mapaResultado.put("OK", "0");
            mapaResultado.put("NG", "0");
            mapaResultado.put("ID_TAREA", "0");
            mapaResultado.put("ERROR_COLUMNAS", "false");

            BitacoraBean bitacoraBean = new BitacoraBean();
            int numBitacora = bitacoraBean.altaBitacora(
                    con, esquema, nombreArchivo, 0, "FOR", 0, 0, 0, usuarioHTTP);
            mapaResultado.put("ID_TAREA", String.valueOf(numBitacora));

            stmt = con.prepareStatement(HistorialPagosQuery.getInsertar(esquema));

            ArrayList<String> listaTXT = UtilsFile.leeArchivoTXT(pathArchivoCompleto);
            List<String> lineScan = null;
            int numRegistro = 0;

            int totColumnas = 6;

            HistorialPagosForm historialPagosForm = new HistorialPagosForm();
            ProveedoresBean provBean = new ProveedoresBean();
            ProveedoresForm provForm = null;

            for (int x = 0; x < listaTXT.size(); x++) {

                // PARSEAR SOLO UNA VEZ
                lineScan = Utils.parseLine(listaTXT.get(x), ';');

                // Ignorar líneas vacías
                if (lineScan == null || lineScan.size() == 0) {
                    continue;
                }

                numRegistro++;

                // ===== ENCABEZADO =====
                if (numRegistro == 1) {
                    if (lineScan.size() != totColumnas) {
                        mapaResultado.put("ERROR_COLUMNAS", "true");
                        break;
                    }
                    continue;
                }

                // ==========================
                //  LECTURA DE CAMPOS
                // ==========================
                String rfcEmpresaTXT = Utils.noNulo(lineScan.get(0));
                String rfcProveedor  = Utils.eliminarGuiones(Utils.noNulo(lineScan.get(1)));

                historialPagosForm.setRfc(rfcProveedor);
                historialPagosForm.setFechaPago(Utils.noNulo(lineScan.get(2)));
                historialPagosForm.setUuidFactura(Utils.noNulo(lineScan.get(3)));
                historialPagosForm.setTipoMoneda(Utils.noNulo(lineScan.get(4)));

                // === TOTAL numérico ===
                try {
                    historialPagosForm.setTotal(Utils.noNuloDouble(lineScan.get(5)));
                } catch (Exception e) {
                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " contiene un TOTAL no numérico.");
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                    continue;
                }

                historialPagosForm.setUsuarioTran(usuarioHTTP);

                // ==========================
                // VALIDACIÓN: RFC EMPRESA
                // ==========================
                logger.info("[HP-TXT][RFC-VALIDACION] Registro: " + numRegistro
                        + " | RFC_EMPRESA_BD=" + rfcEmpresaBD
                        + " | RFC_EMPRESA_TXT=" + rfcEmpresaTXT);

                if (!rfcEmpresaTXT.equalsIgnoreCase(rfcEmpresaBD)) {
                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " no coincide con el RFC de la empresa.");
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                    continue;
                }

                // ==========================
                // VALIDACIÓN: RFC PROVEEDOR LONGITUD
                // ==========================
                if (rfcProveedor.length() != 12 && rfcProveedor.length() != 13) {
                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " tiene longitud de RFC proveedor inválida.");
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                    continue;
                }

                // ==========================
                // VALIDACIÓN: FECHA VÁLIDA
                // ==========================
                if (!UtilsFechas.esFechaFormatoValido(historialPagosForm.getFechaPago())) {
                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " tiene formato de fecha inválido.");
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                    continue;
                }

                // ==========================
                // VALIDACIÓN: MONEDA
                // ==========================
                if (!"MXN".equalsIgnoreCase(historialPagosForm.getTipoMoneda()) &&
                    !"USD".equalsIgnoreCase(historialPagosForm.getTipoMoneda())) {

                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " tiene moneda inválida (solo MXN/USD).");
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                    continue;
                }

                // ==========================
                // VALIDACIÓN: RFC PROVEEDOR EXISTE
                // ==========================
                provForm = provBean.consultarProveedorXrfc(con, esquema, rfcProveedor);

                logger.info(
                    "\n[HP-TXT][PROVEEDOR]"
                    + "\n - Registro          : " + numRegistro
                    + "\n - RFC Proveedor TXT : " + rfcProveedor
                    + "\n - RFC Catalogo      : " + (provForm != null ? provForm.getRfc() : "NO ENCONTRADO")
                    + "\n - Clave Proveedor   : " + (provForm != null ? provForm.getClaveRegistro() : 0)
                    + "\n--------------------------------------------"
                );

                if (provForm == null || provForm.getClaveRegistro() <= 0) {
                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " contiene un proveedor NO registrado en el catálogo.");
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                    continue;
                }

                // ==========================
                // INSERTAR
                // ==========================
                int res = insertarHistorialPago(historialPagosForm, con, esquema);


                if (res > 0) {
                    resArreglo[1]++;
                } else {
                    resArreglo[0]++;
                    bitacoraBean.altaHistorico(con, esquema, numBitacora,
                            String.valueOf(numRegistro),
                            "El registro " + numRegistro + " contiene un UUID duplicado: "
                                    + historialPagosForm.getUuidFactura());
                    mapaResultado.put("BAND_MENSAJE", "ERROR");
                }
            }

            mapaResultado.put("OK", String.valueOf(resArreglo[1]));
            mapaResultado.put("NG", String.valueOf(resArreglo[0]));

            bitacoraBean.updateBitacora(con, esquema,
                    numBitacora, numRegistro, resArreglo[1], resArreglo[0], 1, usuarioHTTP);

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
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
    
    public String obtenerFechaUltimaActualizacion(Connection con, String esquema) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String fecha = "";

        try {
            stmt = con.prepareStatement(HistorialPagosQuery.getFechaUltimaActualizacion(esquema));
            rs = stmt.executeQuery();
            if (rs.next()) {
                fecha = Utils.noNulo(rs.getString("FECHA"));
            }
        } catch (Exception e) {
            logger.error("Error obtenerFechaUltimaActualizacion:", e);
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception ex){}
            try { if (stmt != null) stmt.close(); } catch(Exception ex){}
        }

        return fecha;
    }

}
