package com.itextpdf.xmltopdf.CartaPorte30;

import java.util.*;

public class Carro {

	private String tipoCarro = "";
	private String matriculaCarro = "";
	private String guiaCarro = "";
	private double toneladasNetasCarro;
	private ArrayList<CarroContenedor> contenedor;

	public final String getTipoCarro() {
		return this.tipoCarro;
	}

	public final void setTipoCarro(String value) {
		this.tipoCarro = value;
	}

	public final String getMatriculaCarro() {
		return this.matriculaCarro;
	}

	public final void setMatriculaCarro(String value) {
		this.matriculaCarro = value;
	}

	public final String getGuiaCarro() {
		return this.guiaCarro;
	}

	public final void setGuiaCarro(String value) {
		this.guiaCarro = value;
	}

	public final double getToneladasNetasCarro() {
		return this.toneladasNetasCarro;
	}

	public final void setToneladasNetasCarro(double value) {
		this.toneladasNetasCarro = value;
	}

	public final ArrayList<CarroContenedor> getContenedor() {
		return this.contenedor;
	}

	public final void setContenedor(ArrayList<CarroContenedor> value) {
		this.contenedor = value;
	}
}
