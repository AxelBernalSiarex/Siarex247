/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte30;

/**
 *
 * @author frack
 */
public class Domicilio {
        private String calle = "";
        private String numeroExterior = "";
        private String numeroInterior = "";
        private String colonia = "";
        private String localidad = "";
        private String referencia = "";
        private String municipio = "";
        private String estado = "";
        private String pais = "";
        private String codigoPostal = "";

        public final String getCalle() {
                return this.calle;
        }

        public final void setCalle(String value) {
                this.calle = value;
        }

        public final String getNumeroExterior() {
                return this.numeroExterior;
        }

        public final void setNumeroExterior(String value) {
                this.numeroExterior = value;
        }

        public final String getNumeroInterior() {
                return this.numeroInterior;
        }

        public final void setNumeroInterior(String value) {
                this.numeroInterior = value;
        }

        public final String getColonia() {
                return this.colonia;
        }

        public final void setColonia(String value) {
                this.colonia = value;
        }

        public final String getLocalidad() {
                return this.localidad;
        }

        public final void setLocalidad(String value) {
                this.localidad = value;
        }

        public final String getReferencia() {
                return this.referencia;
        }

        public final void setReferencia(String value) {
                this.referencia = value;
        }

        public final String getMunicipio() {
                return this.municipio;
        }

        public final void setMunicipio(String value) {
                this.municipio = value;
        }

        public final String getEstado() {
                return this.estado;
        }

        public final void setEstado(String value) {
                this.estado = value;
        }

        public final String getPais() {
                return this.pais;
        }

        public final void setPais(String value) {
                this.pais = value;
        }

        public final String getCodigoPostal() {
                return this.codigoPostal;
        }

        public final void setCodigoPostal(String value) {
                this.codigoPostal = value;
        }
}
