/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte20;

import java.util.*;

/**
 *
 * @author frack
 */
public class Carro {
	private ArrayList<CarroContenedor> contenedor;
	private String tipoCarro = "";
	private String matriculaCarro = "";
	private String guiaCarro = "";
	private double toneladasNetasCarro;

	public final ArrayList<CarroContenedor> getContenedor() {
		return this.contenedor;
	}

	public final void setContenedor(ArrayList<CarroContenedor> value) {
		this.contenedor = value;
	}

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
}
