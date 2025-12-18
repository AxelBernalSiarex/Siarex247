package com.siarex247.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CrtToPem {
	
	/*
    public static void main(String[] args) throws Exception {
    	
    	String fileEntrada = "C:\\PERSONAL\\SAT\\bumg8008188a4.cer";
    	String fileSalida = "C:\\PERSONAL\\SAT\\bumg8008188a4.pem";
    	
    	
        Path in = Path.of(fileEntrada);
        Path out = Path.of(fileSalida);

        byte[] content = Files.readAllBytes(in);
        String text = new String(content);
        boolean looksPem = text.contains("-----BEGIN CERTIFICATE-----");

        byte[] derBytes;

        if (looksPem) {
            // Limpia el PEM (por si trae líneas o comentarios raros) → bytes DER
            String base64 = text
                    .replaceAll("-----BEGIN CERTIFICATE-----", "")
                    .replaceAll("-----END CERTIFICATE-----", "")
                    .replaceAll("\\s", "");
            derBytes = Base64.getMimeDecoder().decode(base64);
        } else {
            // Es DER binario
            derBytes = content;
        }

        // Valida que realmente es un X.509
        X509Certificate cert = (X509Certificate) CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new java.io.ByteArrayInputStream(derBytes));

        // Re-empaqueta a PEM canónico (líneas de 64 chars)
        String pem = toPem("CERTIFICATE", cert.getEncoded()); // usa la codificación DER del propio cert
        Files.writeString(out, pem);
        System.out.println("Escrito PEM en: " + out.toAbsolutePath());
    } */

    
    
    
    
    private static String toPem1(String type, byte[] der) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(der);
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(type).append("-----\n");
        for (int i = 0; i < base64.length(); i += 64) {
            int end = Math.min(i + 64, base64.length());
            sb.append(base64, i, end).append("\n");
        }
        sb.append("-----END ").append(type).append("-----\n");
        return sb.toString();
    }
    
    
    /**
     * Convierte un certificado X.509 en formato DER (.cer)
     * a un archivo PEM de texto plano (.pem).
     */
    public static void convertCertificateToPem(File cerFile, File pemFile) throws Exception {
        if (cerFile == null || !cerFile.exists()) {
            throw new IllegalArgumentException("El archivo CER no existe: " + cerFile);
        }

        byte[] derBytes = Files.readAllBytes(cerFile.toPath());

        // Validación básica del certificado
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(derBytes));

        // Generamos el PEM a partir de la representación DER del certificado
        String pem = toPem("CERTIFICATE", cert.getEncoded());

        if (pemFile == null) {
            throw new IllegalArgumentException("El archivo PEM destino es nulo");
        }
        if (pemFile.getParentFile() != null && !pemFile.getParentFile().exists()) {
            pemFile.getParentFile().mkdirs();
        }

        Files.write(pemFile.toPath(), pem.getBytes(StandardCharsets.US_ASCII));
    }

    // Método de apoyo para formatear PEM en líneas de 64 caracteres
    private static String toPem(String type, byte[] der) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(der);
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(type).append("-----\n");

        int index = 0;
        while (index < base64.length()) {
            int end = Math.min(index + 64, base64.length());
            sb.append(base64, index, end).append("\n");
            index = end;
        }

        sb.append("-----END ").append(type).append("-----\n");
        return sb.toString();
    }

    // Opcional: para pruebas manuales
    public static void main(String[] args) throws Exception {
        if (args != null && args.length == 2) {
            File in = new File(args[0]);
            File out = new File(args[1]);
            convertCertificateToPem(in, out);
        }
    }
}
