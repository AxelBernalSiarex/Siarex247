package com.siarex247.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


public class GoogleDriveDownloader {

    private static final String APPLICATION_NAME = "Siarex Descarga Masiva";
    private static final String SERVICE_ACCOUNT_JSON = "C:/Xml/siarexuploader-e918550f4e7d.json";
    private static final String CARPETA_DRIVE_ID = "1t9Mwecv_vG79GVeij7gD5nqlsKwZT3hN";
    private static final String DESTINO_LOCAL = "C:/Xml/";

    
    public static void main(String[] args) {
		try {
			descargarTodo();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    
    public static void descargarTodo() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        Drive driveService = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Buscar archivos en la carpeta
        String query = "'" + CARPETA_DRIVE_ID + "' in parents and trashed=false";
        FileList result = driveService.files().list()
                .setQ(query)
                .setFields("files(id, name)")
                .execute();

        List<File> archivos = result.getFiles();
        if (archivos == null || archivos.isEmpty()) {
            System.out.println("📭 No hay archivos en la carpeta.");
            return;
        }

        for (File archivo : archivos) {
            String nombre = archivo.getName();
            String destino = Paths.get(DESTINO_LOCAL, nombre).toString();

            java.io.File destinoLocal = new java.io.File(destino);
            if (destinoLocal.exists()) {
               // System.out.println("🟡 Ya existe: " + nombre);
                continue;
            }

            System.out.println("⬇️ Descargando: " + nombre);
            OutputStream out = new FileOutputStream(destinoLocal);
            driveService.files().get(archivo.getId()).executeMediaAndDownloadTo(out);
            out.close();
            System.out.println("✅ Guardado en: " + destino);
        }
    }
}
