package com.siarex247.leerCuenta;

/**
 * Excepción de negocio para validaciones del HTM.
 * Incluye un código de error (E001..E006) para que después lo bitacorices.
 */
public class ValidacionHtmException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String codigoError;

    public ValidacionHtmException(String codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public ValidacionHtmException(String codigoError, String mensaje, Throwable cause) {
        super(mensaje, cause);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
