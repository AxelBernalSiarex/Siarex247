package com.siarex247.utils;

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.Logger;

public class CertificadoUtils {
	public static final Logger logger = Logger.getLogger("siarex247");

    public static class InfoCertificado {
        public String numeroSerie;
        public String validoDesde;
        public String validoHasta;
        public boolean porVencer;   // ← si faltan menos de 3 meses
        public long diasRestantes;
    }

    public static InfoCertificado leerCertificado(String rutaCer) {
        InfoCertificado info = new InfoCertificado();

        try (FileInputStream fis = new FileInputStream(rutaCer)) {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(fis);

            info.numeroSerie = cert.getSerialNumber().toString();
            info.validoDesde = cert.getNotBefore().toString();
            info.validoHasta = cert.getNotAfter().toString();

            LocalDate hoy = LocalDate.now();
            LocalDate fechaVencimiento = cert.getNotAfter()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long dias = ChronoUnit.DAYS.between(hoy, fechaVencimiento);
            logger.info("Dias por vencer -> "+dias);
            info.diasRestantes = dias;
            info.porVencer = dias <= 90;  // ← menos de 3 meses

        } catch (Exception e) {
            Utils.imprimeLog("leerCertificado()", e);
        }

        return info;
    }
}
