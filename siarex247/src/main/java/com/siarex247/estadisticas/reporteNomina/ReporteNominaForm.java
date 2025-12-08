package com.siarex247.estadisticas.reporteNomina;

import java.util.ArrayList;

public class ReporteNominaForm {

	private int idRegistro;
	private String uuid;
	private String tipoNomina;
	private String fechaPago;
	private String fechaInicialPago;
	private String fechaFinalPago;
	private double numDiasPagados;
	private String subsidioCausado;
	private double subsidioCausadoDouble;
	private String totalPercepciones;
	private double totalPercepcionesDouble;
	private String totalDeducciones;
	private double totalDeduccionesDouble;
	private String totalOtroPagos;
	private double totalOtroPagosDouble;
	private String total;
	private double totalDouble;
	private String OrigenRecurso;
	private String estatusSAT;
	private String serie;
	private String folio;
	private String fechaFactura;
	private String fechaTimbrado;
	private String emisorRFC;
	private String emisorNombre;
	private String registroPatronal;
	private String receptorRFC;
	private String receptorNombre;
	private String curp;
	private String numeroSeguroSocial;
	private String fechaInicioLaboral;
	private String antiguedad;
	private String tipoContrato;
	private String tipoJornada;
	private String tipoRegimen;
	private String numEmpleado;
	private String riesgoPuesto;
	private String departamento;
	private String puesto;
	private String sindicalizado;
	private String periodicidadPago;
	private String salarioBaseCotApor;
	private double salarioBaseCotAporDouble;
	private String salarioDiarioIntegrado;
	private double salarioDiarioIntegradoDouble;
	private String claveEntFed;
	private String ingresoAcumulable;
	private double ingresoAcumulableDouble;
	private String ingresoNoAcumulable;
	private double ingresoNoAcumulableDouble;
	private String ultimoSueldoMensual;
	private double ultimoSueldoMensualDouble;
	private double aniosServicio;
	private String totalPagado;
	private double totalPagadoDouble;
	
	
	private PercepcionesForm percepciones;
	private DeduccionesForm deducciones;
	private OtroPagosForm otrosPagos;
	public int getIdRegistro() {
		return idRegistro;
	}
	public String getUuid() {
		return uuid;
	}
	public String getTipoNomina() {
		return tipoNomina;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public String getFechaInicialPago() {
		return fechaInicialPago;
	}
	public String getFechaFinalPago() {
		return fechaFinalPago;
	}
	public double getNumDiasPagados() {
		return numDiasPagados;
	}
	public String getSubsidioCausado() {
		return subsidioCausado;
	}
	public double getSubsidioCausadoDouble() {
		return subsidioCausadoDouble;
	}
	public String getTotalPercepciones() {
		return totalPercepciones;
	}
	public double getTotalPercepcionesDouble() {
		return totalPercepcionesDouble;
	}
	public String getTotalDeducciones() {
		return totalDeducciones;
	}
	public double getTotalDeduccionesDouble() {
		return totalDeduccionesDouble;
	}
	public String getTotalOtroPagos() {
		return totalOtroPagos;
	}
	public double getTotalOtroPagosDouble() {
		return totalOtroPagosDouble;
	}
	public String getTotal() {
		return total;
	}
	public double getTotalDouble() {
		return totalDouble;
	}
	public String getOrigenRecurso() {
		return OrigenRecurso;
	}
	public String getEstatusSAT() {
		return estatusSAT;
	}
	public String getSerie() {
		return serie;
	}
	public String getFolio() {
		return folio;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public String getFechaTimbrado() {
		return fechaTimbrado;
	}
	public String getEmisorRFC() {
		return emisorRFC;
	}
	public String getEmisorNombre() {
		return emisorNombre;
	}
	public String getRegistroPatronal() {
		return registroPatronal;
	}
	public String getReceptorRFC() {
		return receptorRFC;
	}
	public String getReceptorNombre() {
		return receptorNombre;
	}
	public String getCurp() {
		return curp;
	}
	public String getNumeroSeguroSocial() {
		return numeroSeguroSocial;
	}
	public String getFechaInicioLaboral() {
		return fechaInicioLaboral;
	}
	public String getAntiguedad() {
		return antiguedad;
	}
	public String getTipoContrato() {
		return tipoContrato;
	}
	public String getTipoJornada() {
		return tipoJornada;
	}
	public String getTipoRegimen() {
		return tipoRegimen;
	}
	public String getNumEmpleado() {
		return numEmpleado;
	}
	public String getRiesgoPuesto() {
		return riesgoPuesto;
	}
	public String getDepartamento() {
		return departamento;
	}
	public String getPuesto() {
		return puesto;
	}
	public String getSindicalizado() {
		return sindicalizado;
	}
	public String getPeriodicidadPago() {
		return periodicidadPago;
	}
	public String getSalarioBaseCotApor() {
		return salarioBaseCotApor;
	}
	public double getSalarioBaseCotAporDouble() {
		return salarioBaseCotAporDouble;
	}
	public String getSalarioDiarioIntegrado() {
		return salarioDiarioIntegrado;
	}
	public double getSalarioDiarioIntegradoDouble() {
		return salarioDiarioIntegradoDouble;
	}
	public String getClaveEntFed() {
		return claveEntFed;
	}
	public String getIngresoAcumulable() {
		return ingresoAcumulable;
	}
	public double getIngresoAcumulableDouble() {
		return ingresoAcumulableDouble;
	}
	public String getIngresoNoAcumulable() {
		return ingresoNoAcumulable;
	}
	public double getIngresoNoAcumulableDouble() {
		return ingresoNoAcumulableDouble;
	}
	public String getUltimoSueldoMensual() {
		return ultimoSueldoMensual;
	}
	public double getUltimoSueldoMensualDouble() {
		return ultimoSueldoMensualDouble;
	}
	public double getAniosServicio() {
		return aniosServicio;
	}
	public String getTotalPagado() {
		return totalPagado;
	}
	public double getTotalPagadoDouble() {
		return totalPagadoDouble;
	}
	public PercepcionesForm getPercepciones() {
		return percepciones;
	}
	public DeduccionesForm getDeducciones() {
		return deducciones;
	}
	public OtroPagosForm getOtrosPagos() {
		return otrosPagos;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public void setTipoNomina(String tipoNomina) {
		this.tipoNomina = tipoNomina;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public void setFechaInicialPago(String fechaInicialPago) {
		this.fechaInicialPago = fechaInicialPago;
	}
	public void setFechaFinalPago(String fechaFinalPago) {
		this.fechaFinalPago = fechaFinalPago;
	}
	public void setNumDiasPagados(double numDiasPagados) {
		this.numDiasPagados = numDiasPagados;
	}
	public void setSubsidioCausado(String subsidioCausado) {
		this.subsidioCausado = subsidioCausado;
	}
	public void setSubsidioCausadoDouble(double subsidioCausadoDouble) {
		this.subsidioCausadoDouble = subsidioCausadoDouble;
	}
	public void setTotalPercepciones(String totalPercepciones) {
		this.totalPercepciones = totalPercepciones;
	}
	public void setTotalPercepcionesDouble(double totalPercepcionesDouble) {
		this.totalPercepcionesDouble = totalPercepcionesDouble;
	}
	public void setTotalDeducciones(String totalDeducciones) {
		this.totalDeducciones = totalDeducciones;
	}
	public void setTotalDeduccionesDouble(double totalDeduccionesDouble) {
		this.totalDeduccionesDouble = totalDeduccionesDouble;
	}
	public void setTotalOtroPagos(String totalOtroPagos) {
		this.totalOtroPagos = totalOtroPagos;
	}
	public void setTotalOtroPagosDouble(double totalOtroPagosDouble) {
		this.totalOtroPagosDouble = totalOtroPagosDouble;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public void setTotalDouble(double totalDouble) {
		this.totalDouble = totalDouble;
	}
	public void setOrigenRecurso(String origenRecurso) {
		OrigenRecurso = origenRecurso;
	}
	public void setEstatusSAT(String estatusSAT) {
		this.estatusSAT = estatusSAT;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}
	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}
	public void setRegistroPatronal(String registroPatronal) {
		this.registroPatronal = registroPatronal;
	}
	public void setReceptorRFC(String receptorRFC) {
		this.receptorRFC = receptorRFC;
	}
	public void setReceptorNombre(String receptorNombre) {
		this.receptorNombre = receptorNombre;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public void setNumeroSeguroSocial(String numeroSeguroSocial) {
		this.numeroSeguroSocial = numeroSeguroSocial;
	}
	public void setFechaInicioLaboral(String fechaInicioLaboral) {
		this.fechaInicioLaboral = fechaInicioLaboral;
	}
	public void setAntiguedad(String antiguedad) {
		this.antiguedad = antiguedad;
	}
	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	public void setTipoJornada(String tipoJornada) {
		this.tipoJornada = tipoJornada;
	}
	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}
	public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}
	public void setRiesgoPuesto(String riesgoPuesto) {
		this.riesgoPuesto = riesgoPuesto;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public void setSindicalizado(String sindicalizado) {
		this.sindicalizado = sindicalizado;
	}
	public void setPeriodicidadPago(String periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
	}
	public void setSalarioBaseCotApor(String salarioBaseCotApor) {
		this.salarioBaseCotApor = salarioBaseCotApor;
	}
	public void setSalarioBaseCotAporDouble(double salarioBaseCotAporDouble) {
		this.salarioBaseCotAporDouble = salarioBaseCotAporDouble;
	}
	public void setSalarioDiarioIntegrado(String salarioDiarioIntegrado) {
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}
	public void setSalarioDiarioIntegradoDouble(double salarioDiarioIntegradoDouble) {
		this.salarioDiarioIntegradoDouble = salarioDiarioIntegradoDouble;
	}
	public void setClaveEntFed(String claveEntFed) {
		this.claveEntFed = claveEntFed;
	}
	public void setIngresoAcumulable(String ingresoAcumulable) {
		this.ingresoAcumulable = ingresoAcumulable;
	}
	public void setIngresoAcumulableDouble(double ingresoAcumulableDouble) {
		this.ingresoAcumulableDouble = ingresoAcumulableDouble;
	}
	public void setIngresoNoAcumulable(String ingresoNoAcumulable) {
		this.ingresoNoAcumulable = ingresoNoAcumulable;
	}
	public void setIngresoNoAcumulableDouble(double ingresoNoAcumulableDouble) {
		this.ingresoNoAcumulableDouble = ingresoNoAcumulableDouble;
	}
	public void setUltimoSueldoMensual(String ultimoSueldoMensual) {
		this.ultimoSueldoMensual = ultimoSueldoMensual;
	}
	public void setUltimoSueldoMensualDouble(double ultimoSueldoMensualDouble) {
		this.ultimoSueldoMensualDouble = ultimoSueldoMensualDouble;
	}
	public void setAniosServicio(double aniosServicio) {
		this.aniosServicio = aniosServicio;
	}
	public void setTotalPagado(String totalPagado) {
		this.totalPagado = totalPagado;
	}
	public void setTotalPagadoDouble(double totalPagadoDouble) {
		this.totalPagadoDouble = totalPagadoDouble;
	}
	public void setPercepciones(PercepcionesForm percepciones) {
		this.percepciones = percepciones;
	}
	public void setDeducciones(DeduccionesForm deducciones) {
		this.deducciones = deducciones;
	}
	public void setOtrosPagos(OtroPagosForm otrosPagos) {
		this.otrosPagos = otrosPagos;
	}
	

	
	
	
	
}
