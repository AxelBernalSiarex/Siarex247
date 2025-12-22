package com.siarex247.leerCuenta;

public class BitacoraOrdenCompraHtmQuerys {

    public static String insert(String esquema) {
        return "INSERT INTO " + esquema + ".BITACORA_ORDEN_COMPRA_HTM ("
             + "NUM_ORDEN, COD_ERROR, DESC_ERROR, EMAIL_ORIGEN, ASUNTO, ARCHIVO_HTM"
             + ") VALUES (?,?,?,?,?,?)";
    }
}
