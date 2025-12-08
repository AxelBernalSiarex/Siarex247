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
public class Mercancias {
        private ArrayList<Mercancia> mercancia;
        private Autotransporte autotransporte;
        private TransporteMaritimo transporteMaritimo;
        private TransporteAereo transporteAereo;
        private TransporteFerroviario transporteFerroviario;
        private double pesoBrutoTotal;
        private String unidadPeso = "";
        private double pesoNetoTotal;
        private int numTotalMercancias;
        private double cargoPorTasacion;

        public final ArrayList<Mercancia> getMercancia() {
                return this.mercancia;
        }

        public final void setMercancia(ArrayList<Mercancia> value) {
                this.mercancia = value;
        }

        public final Autotransporte getAutotransporte() {
                return this.autotransporte;
        }

        public final void setAutotransporte(Autotransporte value) {
                this.autotransporte = value;
        }

        public final TransporteMaritimo getTransporteMaritimo() {
                return this.transporteMaritimo;
        }

        public final void setTransporteMaritimo(TransporteMaritimo value) {
                this.transporteMaritimo = value;
        }

        public final TransporteAereo getTransporteAereo() {
                return this.transporteAereo;
        }

        public final void setTransporteAereo(TransporteAereo value) {
                this.transporteAereo = value;
        }

        public final TransporteFerroviario getTransporteFerroviario() {
                return this.transporteFerroviario;
        }

        public final void setTransporteFerroviario(TransporteFerroviario value) {
                this.transporteFerroviario = value;
        }

        public final double getPesoBrutoTotal() {
                return this.pesoBrutoTotal;
        }

        public final void setPesoBrutoTotal(double value) {
                this.pesoBrutoTotal = value;
        }

        public final String getUnidadPeso() {
                return this.unidadPeso;
        }

        public final void setUnidadPeso(String value) {
                this.unidadPeso = value;
        }

        public final double getPesoNetoTotal() {
                return this.pesoNetoTotal;
        }

        public final void setPesoNetoTotal(double value) {
                this.pesoNetoTotal = value;
        }

        public final int getNumTotalMercancias() {
                return this.numTotalMercancias;
        }

        public final void setNumTotalMercancias(int value) {
                this.numTotalMercancias = value;
        }

        public final double getCargoPorTasacion() {
                return this.cargoPorTasacion;
        }

        public final void setCargoPorTasacion(double value) {
                this.cargoPorTasacion = value;
        }
}
