package com.siarex247.cumplimientoFiscal.HistorialPagos;

import java.util.ArrayList;

public class HistorialPagosModel {

    private ArrayList<HistorialPagosForm> data;
    private String codError;
    private String mensajeError;
    private int recordsTotal;
    private int draw;
    private int recordsFiltered;
    private int claveRegistro;
    private int idBitacora;
    private int noExitosos;
    
    
    public ArrayList<HistorialPagosForm> getData() {
        return data;
    }

    public void setData(ArrayList<HistorialPagosForm> data) {
        this.data = data;
    }

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getClaveRegistro() {
        return claveRegistro;
    }

    public void setClaveRegistro(int claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

	public int getIdBitacora() {
		return idBitacora;
	}

	public void setIdBitacora(int idBitacora) {
		this.idBitacora = idBitacora;
	}

	public int getNoExitosos() {
		return noExitosos;
	}

	public void setNoExitosos(int noExitosos) {
		this.noExitosos = noExitosos;
	}
    
    
}
