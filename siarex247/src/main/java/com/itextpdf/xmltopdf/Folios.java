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
public class Folios {
        private String _UUID = "";
        private String _estatusUUID = "";

        public final String getUUID() {
                return _UUID;
        }

        public final void setUUID(String value) {
                _UUID = value;
        }

        public final String getEstatusUUID() {
                return _estatusUUID;
        }

        public final void setEstatusUUID(String value) {
                _estatusUUID = value;
        }
}
