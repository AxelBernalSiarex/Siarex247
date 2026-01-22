package com.siarex247.descargaMasivaSat;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;

public class monitorDescargaMasivaSat {

    public static final Logger logger = Logger.getLogger("siarex247");

    public void monitorDescargaMasivaSat(int diaProceso) {

        Connection con = null;
        ResultadoConexion rc = null;

        try {
            AccesoBean accesoBean = new AccesoBean();
            ConexionDB connPool = new ConexionDB();

            // ===== 1) conexión SOLO para listaEmpresas =====
            rc = connPool.getConnectionSiarex();
            con = rc.getCon();

            ArrayList<EmpresasForm> listaEmpresas =
                    accesoBean.listaEmpresas(con, rc.getEsquema());
            // OJO: listaEmpresas() CIERRA la conexión por dentro (por tu diseño)

            DescargaSATBean bean = new DescargaSATBean();

            for (EmpresasForm empresa : listaEmpresas) {

                if (!"A".equalsIgnoreCase(Utils.noNulo(empresa.getEstatus()))) {
                    continue;
                }

                logger.info("==============================================");
                logger.info(" MONITOREO DESCARGA MASIVA SAT ");
                logger.info(" Empresa: " + empresa.getEsquema());
                logger.info(" RFC: " + empresa.getRfc());
                logger.info("==============================================");

                // ===== 2) Primera consulta: Metadata HOY (trae N registros) =====
                ArrayList<HistoricoProcesoSATForm> historicoHoy =
                        obtenerHistoricoMetadataHoy(bean, empresa.getEsquema());

                logger.info("Metadata HOY total=" + (historicoHoy == null ? 0 : historicoHoy.size()));

                // ===== 3) Comparar uno por uno: solo los que estén en INI =====
                ArrayList<HistoricoProcesoSATForm> historicoIni =
                        filtrarPorEstatus(historicoHoy, "INI");

                logger.info("Metadata HOY en INI total=" + historicoIni.size());

                // (opcional) ver detalle de los INI
                for (HistoricoProcesoSATForm h : historicoIni) {
                    logger.info("INI -> ID=" + h.getClaveHistorico()
                            + " SOL=" + h.getSolicitudSat()
                            + " PAQ=" + h.getPaqueteSat()
                            + " EST_DESC=" + h.getEstatusDescarga()
                            + " EST=" + h.getEstatus()
                            + " FECHA=" + h.getFechaDescarga());
                }

                // AHORITA HASTA AHI:
                // si hay INI, ya sabes cuáles son (historicoIni) y puedes decidir qué hacer después
                // ejemplo:
                // if (!historicoIni.isEmpty()) { continue; }

            }

        } catch (Exception e) {
            Utils.imprimeLog("monitorDescargaMasivaSat", e);
        } finally {
            // aquí ya casi siempre 'con' ya viene cerrado por listaEmpresas()
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    /**
     * Abre su propia conexión (porque listaEmpresas() cerró la anterior).
     * Trae TODOS los registros Metadata de HOY (N registros).
     */
    private ArrayList<HistoricoProcesoSATForm> obtenerHistoricoMetadataHoy(
            DescargaSATBean bean, String esquema) {

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        ArrayList<HistoricoProcesoSATForm> lista = new ArrayList<>();

        try {
            rc = connPool.getConnectionSiarex();
            con = rc.getCon();

            if (con == null) {
                logger.info("obtenerHistoricoMetadataHoy -> con == null (no se pudo abrir conexión)");
                return lista;
            }

            lista = bean.consultarHistoricoMetadataHoy(con, esquema);

            int total = (lista == null ? 0 : lista.size());
            logger.info("Histórico Metadata HOY - total registros: " + total);

            // (opcional) ver el detalle de TODO lo de hoy
            if (lista != null) {
                for (HistoricoProcesoSATForm h : lista) {
                    logger.info("ID=" + h.getClaveHistorico()
                            + " SOL=" + h.getSolicitudSat()
                            + " PAQ=" + h.getPaqueteSat()
                            + " EST_DESC=" + h.getEstatusDescarga()
                            + " EST=" + h.getEstatus());
                }
            }

        } catch (Exception e) {
            Utils.imprimeLog("obtenerHistoricoMetadataHoy", e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }

        return (lista == null ? new ArrayList<HistoricoProcesoSATForm>() : lista);
    }

    /**
     * Recorre N registros y regresa solo los que cumplan ESTATUS = estatusBuscado.
     * (Comparación uno por uno, como lo pediste)
     */
    private ArrayList<HistoricoProcesoSATForm> filtrarPorEstatus(
            ArrayList<HistoricoProcesoSATForm> lista, String estatusBuscado) {

        ArrayList<HistoricoProcesoSATForm> out = new ArrayList<>();
        String est = Utils.noNulo(estatusBuscado).trim();

        if (lista == null || lista.isEmpty()) return out;

        for (HistoricoProcesoSATForm h : lista) {
            String estatusDescarga = Utils.noNulo(h.getEstatusDescarga()).trim(); // CHAR(3) -> TRIM
            if (estatusDescarga.equalsIgnoreCase(est)) {
                out.add(h);
            }
        }
        return out;
    }
}
