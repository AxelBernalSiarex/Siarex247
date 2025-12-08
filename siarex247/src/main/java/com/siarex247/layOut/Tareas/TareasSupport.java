package com.siarex247.layOut.Tareas;

import java.io.File;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class TareasSupport extends ActionDB{

	private static final long serialVersionUID = -6844303820498601713L;
	
	private int claveTarea;
	private String subject;
	private String mensaje;
	private String enviarAhora;
	private String correoDe;
	private String fechaTarea;
	private String tipoEnvio;
	private String estatus;
	private String estatusDes;
	private String tipoTarea;
	private String preValida;
	private String tipoAlarma;
	private String requieroCopia;
	private int num_Dias1;
	private int num_Dias2;
	private String claveProveedor;
	
	
	
	public int getClaveTarea() {
		return claveTarea;
	}

	@StrutsParameter
	public void setClaveTarea(int claveTarea) {
		this.claveTarea = claveTarea;
	}

	public String getSubject() {
		return subject;
	}

	@StrutsParameter
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCorreoDe() {
		return correoDe;
	}

	@StrutsParameter
	public void setCorreoDe(String correoDe) {
		this.correoDe = correoDe;
	}

	public String getFechaTarea() {
		return fechaTarea;
	}

	@StrutsParameter
	public void setFechaTarea(String fechaTarea) {
		this.fechaTarea = fechaTarea;
	}

	public String getTipoEnvio() {
		return tipoEnvio;
	}

	@StrutsParameter
	public void setTipoEnvio(String tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}

	public String getEstatus() {
		return estatus;
	}
	@StrutsParameter
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatusDes() {
		return estatusDes;
	}

	@StrutsParameter
	public void setEstatusDes(String estatusDes) {
		this.estatusDes = estatusDes;
	}

	public String getTipoTarea() {
		return tipoTarea;
	}

	@StrutsParameter
	public void setTipoTarea(String tipoTarea) {
		this.tipoTarea = tipoTarea;
	}


	public String getMensaje() {
		return mensaje;
	}
	@StrutsParameter
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getEnviarAhora() {
		return enviarAhora;
	}
	@StrutsParameter
	public void setEnviarAhora(String enviarAhora) {
		this.enviarAhora = enviarAhora;
	}

	public String getPreValida() {
		return preValida;
	}
	@StrutsParameter
	public void setPreValida(String preValida) {
		this.preValida = preValida;
	}

	public String getTipoAlarma() {
		return tipoAlarma;
	}
	@StrutsParameter
	public void setTipoAlarma(String tipoAlarma) {
		this.tipoAlarma = tipoAlarma;
	}

	public String getRequieroCopia() {
		return requieroCopia;
	}
	@StrutsParameter
	public void setRequieroCopia(String requieroCopia) {
		this.requieroCopia = requieroCopia;
	}

	public int getNum_Dias1() {
		return num_Dias1;
	}
	@StrutsParameter
	public void setNum_Dias1(int num_Dias1) {
		this.num_Dias1 = num_Dias1;
	}

	public int getNum_Dias2() {
		return num_Dias2;
	}
	@StrutsParameter
	public void setNum_Dias2(int num_Dias2) {
		this.num_Dias2 = num_Dias2;
	}

	public String getClaveProveedor() {
		return claveProveedor;
	}
	@StrutsParameter
	public void setClaveProveedor(String claveProveedor) {
		this.claveProveedor = claveProveedor;
	}

	
	
}
