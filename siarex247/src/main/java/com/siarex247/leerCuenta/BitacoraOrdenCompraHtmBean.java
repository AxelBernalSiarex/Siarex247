package com.siarex247.leerCuenta;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class BitacoraOrdenCompraHtmBean {

    private static final Logger logger = Logger.getLogger("siarex247");

    /**
     * Inserta registro en contrare_<empresa>.
     * @param form datos a guardar
     * @param nombreEsquemaEmpresa ej: "mario" (NO "contrare_mario")
     */
    public boolean insertar(BitacoraOrdenCompraHtmForm form, String nombreEsquemaEmpresa) {

        ResultadoConexion rc = null;
        ConexionDB connPool = new ConexionDB();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            rc = connPool.getConnection(nombreEsquemaEmpresa);
            con = rc.getCon();

            String esquema = rc.getEsquema(); // "contrare_"+empresa
            String sql = BitacoraOrdenCompraHtmQuerys.insert(esquema);

            stmt = con.prepareStatement(sql);

            int i = 1;
            stmt.setString(i++, form.getNumOrden());
            stmt.setString(i++, form.getCodError());
            stmt.setString(i++, form.getDescError());
            stmt.setString(i++, form.getEmailOrigen());
            stmt.setString(i++, form.getAsunto());
            stmt.setString(i++, form.getArchivoHtm());

            logger.info("insertBitacoraOrdenCompraHtm -> " + stmt);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            Utils.imprimeLog("insertarBitacoraOrdenCompraHtm()", e);
            return false;

        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }
}
