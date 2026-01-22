package com.siarex247.leerCuenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class BitacoraOrdenCompraHtmBean {

    private static final Logger logger = Logger.getLogger("siarex247");

    // Límites EXACTOS según tu DDL
    private static final int MAX_NUM_ORDEN   = 50;
    private static final int MAX_COD_ERROR   = 20;
    private static final int MAX_DESC_ERROR  = 500;
    private static final int MAX_EMAIL       = 255;
    private static final int MAX_ASUNTO      = 255;
    private static final int MAX_ARCHIVO_HTM = 500;

    /**
     * Inserta registro en contrare_<empresa>.
     * @param form datos a guardar
     * @param nombreEsquemaEmpresa ej: "mario" (NO "contrare_mario")
     */
    public boolean insertar(BitacoraOrdenCompraHtmForm form, String nombreEsquemaEmpresa) {

        ResultadoConexion rc = null;
        ConexionDB connPool = new ConexionDB();
        Connection con = null;

        try {
            rc = connPool.getConnection(nombreEsquemaEmpresa);
            con = rc.getCon();

            String esquema = rc.getEsquema(); // "contrare_"+empresa
            String sql = BitacoraOrdenCompraHtmQuerys.insert(esquema);

            // Truncar SIEMPRE a tamaños reales (evita Data too long)
            String numOrden  = truncNotNull(form.getNumOrden(), MAX_NUM_ORDEN);
            String codError  = truncNotNull(form.getCodError(), MAX_COD_ERROR);
            String descError = truncNotNull(form.getDescError(), MAX_DESC_ERROR);
            String email     = truncNullable(form.getEmailOrigen(), MAX_EMAIL);
            String asunto    = truncNullable(form.getAsunto(), MAX_ASUNTO);
            String htm       = truncNullable(form.getArchivoHtm(), MAX_ARCHIVO_HTM);

            return ejecutarInsert(con, sql, numOrden, codError, descError, email, asunto, htm, "NORMAL");

        } catch (SQLException sqle) {

            // Si fue data-too-long, reintenta (pero con los mismos límites reales)
            if (esDataTooLong(sqle)) {
                try {
                    String esquema = (rc != null) ? rc.getEsquema() : "desconocido";
                    String sql = BitacoraOrdenCompraHtmQuerys.insert(esquema);

                    String numOrden  = truncNotNull(form.getNumOrden(), MAX_NUM_ORDEN);
                    String codError  = truncNotNull(form.getCodError(), MAX_COD_ERROR);
                    String descError = truncNotNull(form.getDescError(), MAX_DESC_ERROR);
                    String email     = truncNullable(form.getEmailOrigen(), MAX_EMAIL);
                    String asunto    = truncNullable(form.getAsunto(), MAX_ASUNTO);
                    String htm       = truncNullable(form.getArchivoHtm(), MAX_ARCHIVO_HTM);

                    logger.warn("Reintentando INSERT bitácora por data-too-long (truncado real). "
                            + "numOrden=" + numOrden
                            + " codError=" + codError
                            + " descLen=" + len(descError)
                            + " htmLen=" + len(htm));

                    return ejecutarInsert(con, sql, numOrden, codError, descError, email, asunto, htm, "RETRY_TRUNC");

                } catch (Exception retryEx) {
                    logger.error("Fallo reintento INSERT bitácora (truncado).", retryEx);
                    Utils.imprimeLog("insertarBitacoraOrdenCompraHtm(RETRY)", retryEx);
                    return false;
                }
            }

            logger.error("Error SQL insertando BITACORA_ORDEN_COMPRA_HTM. "
                    + "sqlState=" + sqle.getSQLState()
                    + " vendorCode=" + sqle.getErrorCode(),
                    sqle);

            Utils.imprimeLog("insertarBitacoraOrdenCompraHtm(SQL)", sqle);
            return false;

        } catch (Exception e) {
            logger.error("Error insertando BITACORA_ORDEN_COMPRA_HTM", e);
            Utils.imprimeLog("insertarBitacoraOrdenCompraHtm()", e);
            return false;

        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    private boolean ejecutarInsert(Connection con, String sql,
                                  String numOrden,
                                  String codError,
                                  String descError,
                                  String emailOrigen,
                                  String asunto,
                                  String archivoHtm,
                                  String modo) throws SQLException {

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);

            int i = 1;
            stmt.setString(i++, numOrden);
            stmt.setString(i++, codError);
            stmt.setString(i++, descError);
            stmt.setString(i++, emailOrigen);
            stmt.setString(i++, asunto);
            stmt.setString(i++, archivoHtm);

            // NO loguear el stmt completo porque puede traer HTML/ruta grande
            logger.info("insertBitacoraOrdenCompraHtm[" + modo + "] numOrden=" + numOrden
                    + " codError=" + codError
                    + " descLen=" + len(descError)
                    + " htmLen=" + len(archivoHtm));
            
            logger.info(stmt);

            int rows = stmt.executeUpdate();

            // por si el pool trae autocommit=false
            if (con != null && !con.getAutoCommit()) {
                con.commit();
            }

            return rows > 0;

        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
        }
    }

    private boolean esDataTooLong(SQLException e) {
        // MySQL: errorCode 1406 = Data too long for column
        if (e == null) return false;
        if (e.getErrorCode() == 1406) return true;
        String state = e.getSQLState();
        return state != null && "22001".equals(state); // data truncation
    }

    private String truncNotNull(String s, int max) {
        if (s == null) s = "";
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }

    private String truncNullable(String s, int max) {
        if (s == null) return null;
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }

    private int len(String s) {
        return (s == null) ? 0 : s.length();
    }
}
