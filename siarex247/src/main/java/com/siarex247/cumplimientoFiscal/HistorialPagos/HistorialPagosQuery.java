package com.siarex247.cumplimientoFiscal.HistorialPagos;

public class HistorialPagosQuery {

    private static String lista = "select ID_REGISTRO, RFC, FECHA_PAGO, UUID_FACTURA, TIPO_MONEDA, TOTAL, ESTATUS,  CODIGO_ERROR, UUID_COMPLEMENTO  from HISTORIAL_PAGOS order by RFC";

    private static String insertar = "insert into <<esquema>>.HISTORIAL_PAGOS (RFC, FECHA_PAGO, UUID_FACTURA, TIPO_MONEDA, TOTAL, USUARIO_TRAN) values (?,?,?,?,?, ?)";

    private static String obtenerPagosHistorialRango = "select RFC from HISTORIAL_PAGOS where ESTATUS in (?, ?) and FECHA_PAGO between ? and ? group by RFC order by RFC";

   // private static String obtenerPagosHistorialDetallePorRfc =  "select FECHA_PAGO, UUID_FACTURA, TIPO_MONEDA, TOTAL from HISTORIAL_PAGOS where RFC = ? and ESTATUS in (?, ?) order by FECHA_PAGO";
    private static String obtenerPagosHistorialDetallePorRfc =
    	    "SELECT A.FECHA_PAGO, A.UUID_FACTURA, B.SERIE, B.FOLIO, " +
    	    "       A.TIPO_MONEDA, A.TOTAL " +
    	    "FROM HISTORIAL_PAGOS A " +
    	    "INNER JOIN "
    	    + "BOVEDA B ON A.UUID_FACTURA = B.UUID " +
    	    "WHERE A.RFC = ? AND A.ESTATUS IN (?, ?) " +
    	    "ORDER BY A.FECHA_PAGO";
    
    public static String getFechaUltimaActualizacion(String esquema) {
        return "SELECT MAX(FECHAALTA) AS FECHA FROM HISTORICO_PROCESOS WHERE TIPO_PROCESO = 'VCB'";
    }


    
    public static String getLista(String esquema) {
        return lista.replace("<<esquema>>", esquema);
    }

    public static String getInsertar(String esquema) {
        return insertar.replace("<<esquema>>", esquema);
    }

    
    public static String getObtenerPagosHistorialRango(String esquema) {
        return obtenerPagosHistorialRango;
    }
    
    public static String getObtenerPagosHistorialDetallePorRfc(String esquema) {
        return obtenerPagosHistorialDetallePorRfc;
    }

    
}
