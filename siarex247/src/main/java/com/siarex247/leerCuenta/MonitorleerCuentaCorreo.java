package com.siarex247.leerCuenta;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;

public class MonitorleerCuentaCorreo {

    public static final Logger logger = Logger.getLogger("siarex247");

    public void MonitorleerCuentaCorreo(int diaProceso) {

        Connection con = null;
        ResultadoConexion rc = null;

        try {
            AccesoBean accesoBean = new AccesoBean();
            ConexionDB connPool = new ConexionDB();

            rc = connPool.getConnectionSiarex();
            con = rc.getCon();

            ArrayList<EmpresasForm> listaEmpresas =
                accesoBean.listaEmpresas(con, rc.getEsquema());

            CorreoReader reader = new CorreoReader();

            for (EmpresasForm empresa : listaEmpresas) {

                if (!"A".equalsIgnoreCase(empresa.getEstatus())) continue;

                logger.info("==============================================");
                logger.info(" MONITOREO CUENTA CORREO ");
                logger.info(" Empresa: " + empresa.getEsquema());
                logger.info(" Email: " + empresa.getEmailDominio());
                logger.info("==============================================");

                reader.leerCuenta(empresa);
            }

        } catch (Exception e) {
            Utils.imprimeLog("MonitorleerCuentaCorreo", e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }
}
