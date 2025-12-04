package com.siarex247.dashboard.Monitor360;

import java.util.Map;

public class Monitor360Form {

	private int annio;
	private String desAnnio;
	
	private String totalUniverso;
	private String porcentajeMetadata;
	private String porcentajeCancelados;
	private String porcentajeComplementos;
	
	private int totalListaNegra;
	
	private double arrIngresos [] = {0,0,0,0,0,0,0,0,0,0,0,0}; 
	private double arrEgresos  [] = {0,0,0,0,0,0,0,0,0,0,0,0};
	private String numeroMayor;
	private float intervalo;
	
	
	private String totalIngreso;
	private String subTotalIngreso;
	private String impRetenidoIngreso;
	private String impTrasladoIngreso;
	
	
	private String totalEgreso;
	private String subTotaEngreso;
	private String impRetenidoEgreso;
	private String impTrasladoEgreso;
	
	
	private String rfc;
	private String razonSocial;
	
	
	public int getAnnio() {
		return annio;
	}
	public String getDesAnnio() {
		return desAnnio;
	}
	public String getTotalUniverso() {
		return totalUniverso;
	}
	public String getPorcentajeMetadata() {
		return porcentajeMetadata;
	}
	public String getPorcentajeCancelados() {
		return porcentajeCancelados;
	}
	public double[] getArrIngresos() {
		return arrIngresos;
	}
	public double[] getArrEgresos() {
		return arrEgresos;
	}
	public String getNumeroMayor() {
		return numeroMayor;
	}
	public String getTotalIngreso() {
		return totalIngreso;
	}
	public String getSubTotalIngreso() {
		return subTotalIngreso;
	}
	public String getImpRetenidoIngreso() {
		return impRetenidoIngreso;
	}
	public String getImpTrasladoIngreso() {
		return impTrasladoIngreso;
	}
	public String getTotalEgreso() {
		return totalEgreso;
	}
	public String getSubTotaEngreso() {
		return subTotaEngreso;
	}
	public String getImpRetenidoEgreso() {
		return impRetenidoEgreso;
	}
	public String getImpTrasladoEgreso() {
		return impTrasladoEgreso;
	}
	public void setAnnio(int annio) {
		this.annio = annio;
	}
	public void setDesAnnio(String desAnnio) {
		this.desAnnio = desAnnio;
	}
	public void setTotalUniverso(String totalUniverso) {
		this.totalUniverso = totalUniverso;
	}
	public void setPorcentajeMetadata(String porcentajeMetadata) {
		this.porcentajeMetadata = porcentajeMetadata;
	}
	public void setPorcentajeCancelados(String porcentajeCancelados) {
		this.porcentajeCancelados = porcentajeCancelados;
	}
	public void setArrIngresos(double[] arrIngresos) {
		this.arrIngresos = arrIngresos;
	}
	public void setArrEgresos(double[] arrEgresos) {
		this.arrEgresos = arrEgresos;
	}
	public void setNumeroMayor(String numeroMayor) {
		this.numeroMayor = numeroMayor;
	}
	public void setTotalIngreso(String totalIngreso) {
		this.totalIngreso = totalIngreso;
	}
	public void setSubTotalIngreso(String subTotalIngreso) {
		this.subTotalIngreso = subTotalIngreso;
	}
	public void setImpRetenidoIngreso(String impRetenidoIngreso) {
		this.impRetenidoIngreso = impRetenidoIngreso;
	}
	public void setImpTrasladoIngreso(String impTrasladoIngreso) {
		this.impTrasladoIngreso = impTrasladoIngreso;
	}
	public void setTotalEgreso(String totalEgreso) {
		this.totalEgreso = totalEgreso;
	}
	public void setSubTotaEngreso(String subTotaEngreso) {
		this.subTotaEngreso = subTotaEngreso;
	}
	public void setImpRetenidoEgreso(String impRetenidoEgreso) {
		this.impRetenidoEgreso = impRetenidoEgreso;
	}
	public void setImpTrasladoEgreso(String impTrasladoEgreso) {
		this.impTrasladoEgreso = impTrasladoEgreso;
	}
	public String getRfc() {
		return rfc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public float getIntervalo() {
		return intervalo;
	}
	public void setIntervalo(float intervalo) {
		this.intervalo = intervalo;
	}
	public String getPorcentajeComplementos() {
		return porcentajeComplementos;
	}
	public void setPorcentajeComplementos(String porcentajeComplementos) {
		this.porcentajeComplementos = porcentajeComplementos;
	}
	public int getTotalListaNegra() {
		return totalListaNegra;
	}
	public void setTotalListaNegra(int totalListaNegra) {
		this.totalListaNegra = totalListaNegra;
	}
	
	
	private Map<String, double[]> mapaPorTipo;
	public Map<String, double[]> getMapaPorTipo() {
	    return mapaPorTipo;
	}
	
	public void setMapaPorTipo(Map<String, double[]> mapaPorTipo) {
	    this.mapaPorTipo = mapaPorTipo;
	}

	
	
}
