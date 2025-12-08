package com.siarex247.utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.util.Collections;

public class GoogleDriveUploader {

    private static final String APPLICATION_NAME = "Siarex ZIP Uploader";
    private static final String SERVICE_ACCOUNT_JSON = "C:/Users/AXELS/ProyectosRelease/RestJR/src/main/resources/siarexuploader-e918550f4e7d.json";
    private static final String FOLDER_ID = "1t9Mwecv_vG79GVeij7gD5nqlsKwZT3hN";

    public static void subirArchivo(String rutaArchivoLocal, String nombreArchivoEnDrive) throws Exception {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        var jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        Drive driveService = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        File metadata = new File();
        metadata.setName(nombreArchivoEnDrive);
        metadata.setParents(Collections.singletonList(FOLDER_ID));

        java.io.File archivoLocal = new java.io.File(rutaArchivoLocal);
        FileContent contenido = new FileContent("application/zip", archivoLocal);

        File archivoSubido = driveService.files().create(metadata, contenido)
                .setFields("id")
                .execute();

       // System.out.println("✅ Archivo subido: " + archivoSubido.getId());
    }
}
