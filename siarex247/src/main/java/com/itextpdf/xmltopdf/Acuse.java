/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

/**
 *
 * @author frack
 */
import java.time.*;

public class Acuse {
        private LocalDateTime _fecha = LocalDateTime.MIN;
        private String _rfcEmisor = "";
        private String _selloDigitalSAT = "";
        private Folios _folios = new Folios();

        public final LocalDateTime getFecha() {
                return _fecha;
        }

        public final void setFecha(LocalDateTime value) {
                _fecha = value;
        }

        public final String getRfcEmisor() {
                return _rfcEmisor;
        }

        public final void setRfcEmisor(String value) {
                _rfcEmisor = value;
        }

        public final String getSelloDigitalSAT() {
                return _selloDigitalSAT;
        }

        public final void setSelloDigitalSAT(String value) {
                _selloDigitalSAT = value;
        }

        public final Folios getFolios() {
                return _folios;
        }

        public final void setFolios(Folios value) {
                _folios = value;
        }
}
