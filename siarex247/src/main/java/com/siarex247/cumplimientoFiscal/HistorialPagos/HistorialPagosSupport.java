package com.siarex247.cumplimientoFiscal.HistorialPagos;

import com.siarex247.bd.ActionDB;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

public class HistorialPagosSupport extends ActionDB {

    private static final long serialVersionUID = 1L;

    private int claveRegistro;
    private String fechaPago;
    private String tipoReg;
    private String bandProcesar;
    private int idBitacora;

    public int getClaveRegistro() { return claveRegistro; }

    @StrutsParameter
    public void setClaveRegistro(int claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    public String getFechaPago() { return fechaPago; }

    @StrutsParameter
    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getTipoReg() { return tipoReg; }

    @StrutsParameter
    public void setTipoReg(String tipoReg) {
        this.tipoReg = tipoReg;
    }

    public String getBandProcesar() { return bandProcesar; }

    @StrutsParameter
    public void setBandProcesar(String bandProcesar) {
        this.bandProcesar = bandProcesar;
    }

	public int getIdBitacora() {
		return idBitacora;
	}
	
	@StrutsParameter
	public void setIdBitacora(int idBitacora) {
		this.idBitacora = idBitacora;
	}
    
    
}
