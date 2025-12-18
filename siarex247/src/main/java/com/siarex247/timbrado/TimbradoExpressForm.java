package com.siarex247.timbrado;

import java.util.ArrayList;

public class TimbradoExpressForm {

	private String code;
	private String message;
	private String statusSat;
	private String statusCodeSat;
	private String status;
	
	private ArrayList<DetailError> listaErrores = new ArrayList<>();
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatusSat() {
		return statusSat;
	}
	public void setStatusSat(String statusSat) {
		this.statusSat = statusSat;
	}
	public String getStatusCodeSat() {
		return statusCodeSat;
	}
	public void setStatusCodeSat(String statusCodeSat) {
		this.statusCodeSat = statusCodeSat;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<DetailError> getListaErrores() {
		return listaErrores;
	}
	public void setListaErrores(ArrayList<DetailError> listaErrores) {
		this.listaErrores = listaErrores;
	}
	
}
