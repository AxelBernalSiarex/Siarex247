package com.siarex247.leerCuenta;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsAPIS;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesBean;

public class CorreoReader {

    private static final Logger logger = Logger.getLogger("siarex247");

    // ================= CÓDIGOS DE ERROR =================
    private static final String E001_ORDEN_VACIA          = "E001";
    private static final String E002_MONEDA_INVALIDA      = "E002";
    private static final String E003_EMPRESA_NO_EXISTE    = "E003";
    private static final String E004_PROV_NO_EXISTE       = "E004";
    private static final String E005_IMPORTE_INVALIDO     = "E005";
    private static final String E006_CLASIF_VACIA         = "E006";
    private static final String E007_REGISTRO_ORDEN_FALLO = "E007";
    private static final String E008_ORDEN_YA_EXISTE      = "E008";
    private static final String E999_ERROR_GENERAL        = "E999";

    // ================= VALIDACIONES =================

    private void validarNumeroOrden(OrdenCompraHtmData data) throws ValidacionHtmException {
        String orden = (data != null) ? data.getOrdenCompra() : null;
        if (orden == null || orden.trim().isEmpty()) {
            logger.error("❌ NÚMERO DE ORDEN VACÍO");
            throw new ValidacionHtmException(E001_ORDEN_VACIA, "Número de orden obligatorio");
        }
        logger.info("✔ Número de orden válido: " + orden);
    }

    private void validarMoneda(OrdenCompraHtmData data) throws ValidacionHtmException {
        String moneda = (data != null) ? data.getMoneda() : null;
        if (moneda == null || moneda.trim().isEmpty()) {
            logger.error("❌ MONEDA NULA/VACÍA");
            throw new ValidacionHtmException(E002_MONEDA_INVALIDA, "Moneda obligatoria (MXN o USD)");
        }
        if (!"MXN".equalsIgnoreCase(moneda) && !"USD".equalsIgnoreCase(moneda)) {
            logger.error("❌ MONEDA NO VÁLIDA: " + moneda);
            throw new ValidacionHtmException(E002_MONEDA_INVALIDA, "Moneda no permitida: " + moneda);
        }
        logger.info("✔ Moneda válida: " + moneda);
    }

    private void validarImporte(OrdenCompraHtmData data) throws ValidacionHtmException {
        BigDecimal monto = (data != null) ? data.getMonto() : null;
        if (monto == null) {
            logger.error("❌ IMPORTE NULO");
            throw new ValidacionHtmException(E005_IMPORTE_INVALIDO, "Importe obligatorio");
        }
        if (monto.doubleValue() <= 0) {
            logger.error("❌ IMPORTE INVÁLIDO: " + monto);
            throw new ValidacionHtmException(E005_IMPORTE_INVALIDO, "Importe debe ser mayor a 0");
        }
        logger.info("✔ Importe válido: " + monto);
    }

    private void validarClasificacion(OrdenCompraHtmData data) throws ValidacionHtmException {
        String clasificacion = (data != null) ? data.getClasificacionCodigo() : null;
        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            logger.error("❌ CLASIFICACIÓN VACÍA");
            throw new ValidacionHtmException(E006_CLASIF_VACIA, "Clasificación obligatoria");
        }
        logger.info("✔ Clasificación válida: " + clasificacion);
    }

    /**
     * 3) Valida que la razón social (DESDE) del HTM exista en EMPRESAS.NOMBRE_LARGO
     */
    private void validarRazonSocialHTM(OrdenCompraHtmData data) throws ValidacionHtmException {
        String razonHtm = (data != null) ? data.getDesde() : null;
        if (razonHtm == null || razonHtm.trim().isEmpty()) {
            throw new ValidacionHtmException(E003_EMPRESA_NO_EXISTE, "Razón social (DESDE) vacía");
        }
        String razonNormalizada = normalizarBasica(razonHtm);
        try {
            AccesoBean accesoBean = new AccesoBean();
            EmpresasForm empresaBd = accesoBean.consultaEmpresaPorNombreLargo(razonNormalizada);
            if (empresaBd == null) {
                logger.error("❌ RAZÓN SOCIAL NO REGISTRADA EN EMPRESAS: " + razonHtm);
                throw new ValidacionHtmException(E003_EMPRESA_NO_EXISTE, "Empresa emisora no registrada: " + razonHtm);
            }
            logger.info("✔ Empresa emisora válida: " + empresaBd.getNombreLargo());
        } catch (ValidacionHtmException vex) {
            throw vex;
        } catch (Exception e) {
            throw new ValidacionHtmException(E003_EMPRESA_NO_EXISTE,
                    "Error validando EMPRESA (DESDE): " + e.getMessage(), e);
        }
    }

    /**
     * 4) Valida proveedor por RAZON SOCIAL del "PARA"
     */
    private void validarProveedorPara(OrdenCompraHtmData data, EmpresasForm empresaSesion)
            throws ValidacionHtmException {

        String proveedorRaw = (data != null) ? data.getPara() : null;
        if (proveedorRaw == null || proveedorRaw.trim().isEmpty()) {
            throw new ValidacionHtmException(E004_PROV_NO_EXISTE, "Proveedor (PARA) vacío");
        }

        try {
            ProveedoresBean proveedoresBean = new ProveedoresBean();
            logger.info("Validando proveedor (PARA): [" + proveedorRaw + "] esquema=" + empresaSesion.getEsquema());
            boolean existe = proveedoresBean.existeProveedorPorRazonSocial(
                    proveedorRaw, empresaSesion.getEsquema());
            if (!existe) {
                logger.error("❌ PROVEEDOR NO REGISTRADO: [" + proveedorRaw + "]");
                throw new ValidacionHtmException(E004_PROV_NO_EXISTE, "Proveedor no registrado: " + proveedorRaw);
            }
            logger.info("✔ Proveedor válido (PARA): " + proveedorRaw);
        } catch (ValidacionHtmException vex) {
            throw vex;
        } catch (Exception e) {
            throw new ValidacionHtmException(E004_PROV_NO_EXISTE,
                    "Error validando PROVEEDOR (PARA): " + e.getMessage(), e);
        }
    }

    // ================= EXTRACCIONES =================

    private BigDecimal extraerCantidadUnidad(String html) {
        try {
            Pattern p = Pattern.compile(
                "<span[^>]*>\\s*(\\d+(?:\\.\\d+)?)\\s*</span>\\s*<span>\\s*\\(EA\\)\\s*</span>",
                Pattern.CASE_INSENSITIVE
            );
            Matcher m = p.matcher(html);
            if (m.find()) return new BigDecimal(m.group(1));
        } catch (Exception e) {
            logger.error("Error extrayendo Cant. (Unidad)", e);
        }
        return BigDecimal.ONE;
    }

    private String leerArchivoComoString(File archivo) throws Exception {
        return Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
    }

    // ================= HELPERS =================

    private String normalizarBasica(String texto) {
        return texto == null ? null :
            texto.toLowerCase()
                 .replace(".", "")
                 .replace(",", "")
                 .replaceAll("\\s+", " ")
                 .trim();
    }

    private String obtenerRfcProveedorPorRazonSocial(Connection con, String esquema, String razonSocial)
            throws ValidacionHtmException {
        try {
            ProveedoresBean bean = new ProveedoresBean();
            ProveedoresForm form = bean.obtenerProveedorPorRazonSocial(con, esquema, razonSocial);
            if (form == null || form.getRfc() == null || form.getRfc().trim().isEmpty()) {
                logger.error("❌ Proveedor sin RFC: " + razonSocial);
                throw new ValidacionHtmException(E004_PROV_NO_EXISTE,
                        "Proveedor sin RFC registrado: " + razonSocial);
            }
            logger.info("✔ RFC proveedor obtenido: " + form.getRfc()
                    + " razonSocial=" + razonSocial);
            return form.getRfc();
        } catch (ValidacionHtmException vex) {
            throw vex;
        } catch (Exception e) {
            logger.error("Error obteniendo RFC proveedor: " + razonSocial, e);
            throw new ValidacionHtmException(E004_PROV_NO_EXISTE,
                    "Error obteniendo RFC proveedor: " + razonSocial, e);
        }
    }

    // ================= USO EXTERNO =================

    public void procesarHtmArchivo(File archivoHtm,
                                   EmpresasForm empresaSesion,
                                   String emailOrigen,
                                   String asunto) throws ValidacionHtmException {

        if (archivoHtm == null || !archivoHtm.exists()) {
            throw new ValidacionHtmException(E999_ERROR_GENERAL,
                    "Archivo HTM no existe: " + (archivoHtm != null ? archivoHtm.getAbsolutePath() : "null"));
        }

        OrdenCompraHtmData data = null;
        String fileName = archivoHtm.getName();

        try {
            AribaHtmParser parser = new AribaHtmParser();
            data = parser.parse(archivoHtm);

            validarNumeroOrden(data);
            validarMoneda(data);
            validarImporte(data);
            validarClasificacion(data);
            validarRazonSocialHTM(data);
            validarProveedorPara(data, empresaSesion);

            registrarNuevaOrdenDesdeHtm(empresaSesion, data, archivoHtm, emailOrigen, asunto);

            llamarServicioDoRegister(
                empresaSesion,
                data,
                Long.parseLong(data.getOrdenCompra()),
                emailOrigen,
                asunto,
                archivoHtm
            );

        } catch (ValidacionHtmException vex) {
            throw vex;
        } catch (Exception ex) {
            throw new ValidacionHtmException(E999_ERROR_GENERAL,
                    "Error general procesando HTM: " + ex.getMessage(), ex);
        }
    }

    // ================= REGISTRO ORDEN =================

    private void registrarNuevaOrdenDesdeHtm(EmpresasForm empresaSesion,
            OrdenCompraHtmData data,
            File archivoHtm,
            String emailOrigen,
            String asunto) throws ValidacionHtmException {

        ResultadoConexion rc = null;
        Connection con = null;

        try {
            long folioEmpresa = Long.parseLong(data.getOrdenCompra().trim());

            ConexionDB connPool = new ConexionDB();
            rc = connPool.getConnection(empresaSesion.getEsquema());
            con = rc.getCon();

            int claveProveedor = obtenerClaveProveedorPorRazonSocial(
                    con, rc.getEsquema(), data.getPara());

            if (claveProveedor <= 0) {
                throw new ValidacionHtmException(E004_PROV_NO_EXISTE,
                        "Proveedor no registrado: " + data.getPara());
            }

            VisorOrdenesBean visorBean = new VisorOrdenesBean();
            int resultado = visorBean.nuevaOrden(
                con,
                rc.getEsquema(),
                folioEmpresa,
                "Orden recibida por correo (HTM)",
                data.getMoneda(),
                data.getMonto().doubleValue(),
                claveProveedor,
                "0",
                "",
                "A5",
                "",
                "",
                "PROCESO_CORREO",
                archivoHtm != null ? archivoHtm.getName() : null
            );

            if (resultado == 1 || resultado == 1062 || resultado == -1062) return;

            throw new ValidacionHtmException(E007_REGISTRO_ORDEN_FALLO,
                    "No se pudo registrar orden. Resultado=" + resultado);

        } catch (Exception e) {
            throw new ValidacionHtmException(E007_REGISTRO_ORDEN_FALLO,
                    "Error registrando orden: " + e.getMessage(), e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    private int obtenerClaveProveedorPorRazonSocial(Connection con, String esquema, String razonSocial) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                "SELECT CLAVE_PROVEEDOR FROM " + esquema + ".PROVEEDORES " +
                "WHERE UPPER(TRIM(RAZON_SOCIAL)) = UPPER(TRIM(?)) " +
                "AND ESTATUS_REGISTRO = 'A' LIMIT 1";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, razonSocial);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            logger.error("Error obteniendo CLAVE_PROVEEDOR: " + razonSocial, e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignore) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
        }
        return 0;
    }

    // ================= DoRegister =================

    private void llamarServicioDoRegister(EmpresasForm empresaSesion,
            OrdenCompraHtmData data,
            long folioEmpresa,
            String emailOrigen,
            String asunto,
            File archivoHtm) {

        try {
            String token = UtilsAPIS.generarToken("");
            if (token == null || token.trim().isEmpty()) return;

            DoRegisterModel model = new DoRegisterModel();

            String html = leerArchivoComoString(archivoHtm);
            model.setCantidad(extraerCantidadUnidad(html));

            ConexionDB connPool = new ConexionDB();
            ResultadoConexion rc = connPool.getConnection(empresaSesion.getEsquema());
            Connection con = rc.getCon();

            try {
                String rfcCliente = obtenerRfcProveedorPorRazonSocial(
                        con, rc.getEsquema(), data.getPara());

                model.setRfcCliente(rfcCliente);
                model.setRazonSocial(data.getPara());
                model.setRfcProveedor(data.getDesde());
                model.setClaveProducto(data.getClasificacionCodigo());
                model.setMonto(data.getMonto());
                model.setNumeroOrden(String.valueOf(folioEmpresa));
                model.setTipoMoneda(data.getMoneda());

            } finally {
                try { con.close(); } catch (Exception ignore) {}
            }

            JSONObject respuesta = UtilsAPIS.doRegister(model, token);
            logger.info("✔ DoRegister OK folio=" + folioEmpresa + " resp=" + respuesta);

        } catch (Exception e) {
            logger.error("❌ Error llamando DoRegister", e);
        }
    }
}
