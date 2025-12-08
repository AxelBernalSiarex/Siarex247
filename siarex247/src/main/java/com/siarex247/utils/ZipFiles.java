package com.siarex247.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipFiles {
	
	 public static final Logger logger = Logger.getLogger("siarex247");
	 
	static final int BUFFER = 2048;
	List<String> filesListInDir = new ArrayList<String>();
	
	
	public static void main(String[] args) {
		ZipFiles z = new ZipFiles();
		try {
			//File dir = new File("C:\\Tomcat9\\siarex247\\public_html\\mvs\\DESCARGA_SAT\\CFDI_TIMBRADO\\2023-09-07\\Todos\\");
			String destFilePath = "C:\\Tomcat9\\siarex247\\public_html\\mvs\\DESCARGA_SAT\\CFDI_TIMBRADO\\2023-09-07\\Todos\\";
			String zipDirName = "C:\\Tomcat9\\siarex247\\public_html\\mvs\\DESCARGA_SAT\\CFDI_TIMBRADO\\2023-09-07\\Todos\\descargaMasivaTimbrado.zip";
			
			// z.zipDirectory(dir, zipDirName);
			ZipFiles.unzip(zipDirName, destFilePath);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public static boolean unzip(String ZipFilePath, String DestFilePath) {
		boolean bandRegresa = false;
		try {
	        File Destination_Directory = new File(DestFilePath);
	        if (!Destination_Directory.exists()) {
	        	Destination_Directory.mkdir();
	        }
	        ZipInputStream Zip_Input_Stream = new ZipInputStream(new FileInputStream(ZipFilePath));
	        ZipEntry Zip_Entry = Zip_Input_Stream.getNextEntry();

	        while (Zip_Entry != null) {
	            String File_Path = DestFilePath + File.separator + Zip_Entry.getName();
	            if (!Zip_Entry.isDirectory()) {

	                extractFile(Zip_Input_Stream, File_Path);
	            } else {

	                File directory = new File(File_Path);
	                directory.mkdirs();
	            }
	            Zip_Input_Stream.closeEntry();
	            Zip_Entry = Zip_Input_Stream.getNextEntry();
	        }
	        Zip_Input_Stream.close();
	        bandRegresa  = true;
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return bandRegresa;
    }
*/
	
	public static void unzip(String zipFilePath, String destDir) throws IOException {
        File destDirFile = new File(destDir);
        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];

            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        }
    }
	
    private static void extractFile(ZipInputStream Zip_Input_Stream, String File_Path) throws IOException {
        BufferedOutputStream Buffered_Output_Stream = new BufferedOutputStream(new FileOutputStream(File_Path));
        byte[] Bytes = new byte[BUFFER];
        int Read_Byte = 0;
        while ((Read_Byte = Zip_Input_Stream.read(Bytes)) != -1) {
        	Buffered_Output_Stream.write(Bytes, 0, Read_Byte);
        }
        Buffered_Output_Stream.close();
    }
	
	
	public ByteArrayOutputStream zipFiles(ArrayList<String> alFiles) {
	    ByteArrayOutputStream dest = new ByteArrayOutputStream();
	    zipFiles(dest, alFiles);
	    return dest;
	}

	
	public void zipFiles(OutputStream dest, ArrayList<String> alFiles) {
		try {
			ZipOutputStream out = new ZipOutputStream(dest);
			//out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];

			BufferedInputStream origin = null;
			File file = null;
			for (int i = 0; i < alFiles.size(); i++) {
			  try {
					try{
						file = new File(alFiles.get(i));	
					}catch(Exception e){
						file = null;
					}
					if (file != null && file.exists()){
						// System.out.println("Adding: " + file.getAbsolutePath() +" - "+ file.exists());
						FileInputStream fi = new FileInputStream(file);
						origin = new BufferedInputStream(fi, BUFFER);
						ZipEntry entry = new ZipEntry(file.getName());
						out.putNextEntry(entry);
						int count;
						while ((count = origin.read(data, 0, BUFFER)) != -1) {
							out.write(data, 0, count);
						}
						origin.close();
					}
			  }catch(Exception e) {
				    origin.close();
					//Utils.imprimeLog("", e);
			  }
			}
			out.close();
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	 
	 /**
	     * This method zips the directory
	     * @param dir
	     * @param zipDirName
	     */
	    public int zipDirectory(File dir, String zipDirName) {
	    	int totReg = 0;
	        try {
	        	populateFilesList(dir);
	            //now zip files one by one
	            //create ZipOutputStream to write to the zip file
	            FileOutputStream fos = new FileOutputStream(zipDirName);
	            ZipOutputStream zos = new ZipOutputStream(fos);
	            for(String filePath : filesListInDir){
	            	try {
	            		// logger.info("filePath===>"+filePath);
	            		ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
		                zos.putNextEntry(ze);
		                //read the file and write to ZipOutputStream
		                FileInputStream fis = new FileInputStream(filePath);
		                byte[] buffer = new byte[1024];
		                int len;
		                while ((len = fis.read(buffer)) > 0) {
		                    zos.write(buffer, 0, len);
		                }
		                zos.closeEntry();
		                fis.close();
		                totReg++;
	            	}catch(Exception e) {
	            		Utils.imprimeLog("", e);
	            	}
	                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
	                
	            }
	            zos.close();
	            fos.close();
	        } catch (IOException e) {
	            Utils.imprimeLog("", e);
	        }
	        return totReg;
	    }
	     
	    
	    public int generaZIP(List<String>  datos, String zipDirName) {
	    	int totReg = 0;
	        try {
	        	
	            //now zip files one by one
	            //create ZipOutputStream to write to the zip file
	            FileOutputStream fos = new FileOutputStream(zipDirName);
	            ZipOutputStream zos = new ZipOutputStream(fos);
	            for(String filePath : datos){
	            	File file = new File(filePath);
	                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
	              //  ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
	            	//ZipEntry ze = new ZipEntry(filePath);
	            	//logger.info("Agregando...."+file.getName());
	            	//logger.info("filePath...."+filePath);
	            	
	            	
	            	ZipEntry ze = new ZipEntry(file.getName());
	                zos.putNextEntry(ze);
	                //read the file and write to ZipOutputStream
	                FileInputStream fis = new FileInputStream(filePath);
	                byte[] buffer = new byte[1024];
	                int len;
	                while ((len = fis.read(buffer)) > 0) {
	                    zos.write(buffer, 0, len);
	                }
	                zos.closeEntry();
	                fis.close();
	                totReg++;
	            }
	            zos.close();
	            fos.close();
	        } catch (IOException e) {
	            Utils.imprimeLog("", e);
	        }
	        return totReg;
	    }
	    
	    
	    
	    public void zipArrayFiles(List<String>  sourceFiles, FileOutputStream fileOutputStream ){
	    	try {
	    	                 //create byte buffer
	    	                 
	    	                  FileOutputStream fout = fileOutputStream;
	    	                  ZipOutputStream zout = new ZipOutputStream(fout);
	    	                  for(int i=0; i < sourceFiles.size(); i++){
	    	                         FileInputStream fin = new FileInputStream(sourceFiles.get(i));
	    	                         zout.putNextEntry(new ZipEntry(sourceFiles.get(i)));
	    	                         int length;
	    	                         byte[] buffer = new byte[1024];
	    	                         while((length = fin.read(buffer)) > 0)
	    	                         {
	    	                            zout.write(buffer, 0, length);
	    	                         }
	    	                         zout.closeEntry();
	    	                         fin.close();
	    	                  }
	    	                   zout.close();
	    	                   fout.close();
	    	         }
	    	         catch(IOException ioe){
	    	        	 Utils.imprimeLog("", ioe);
	    	         }
	    	catch(Exception e){
	    		Utils.imprimeLog("", e);
	    	}
	    }
	    
	    
	    public static boolean descomprimeZIP(String archivoZip, String rutaCarpeta) {
			byte[] buffer = new byte[1024];
			boolean descomprime = false;
	        try {
	            File folder = new File(rutaCarpeta);
	            if (!folder.exists()) {
	                folder.mkdir();
	            }
	            ZipInputStream zis = new ZipInputStream(new FileInputStream(archivoZip));
	            ZipEntry ze = zis.getNextEntry();
	            while (ze != null) {
	                String nombreArchivo = ze.getName();
	                File archivoNuevo = new File(rutaCarpeta + File.separator + nombreArchivo);
	                //System.out.println("archivo descomprimido : " + archivoNuevo.getAbsoluteFile());
	                new File(archivoNuevo.getParent()).mkdirs();
	                FileOutputStream fos = new FileOutputStream(archivoNuevo);
	                int len;
	                while ((len = zis.read(buffer)) > 0) {
	                    fos.write(buffer, 0, len);
	                }
	                fos.close();
	                ze = zis.getNextEntry();
	            }
	            zis.closeEntry();
	            zis.close();
	            descomprime = true;
	            //System.out.println("Listo");
	        } catch (Exception ex) {
	            //ex.printStackTrace();
	            descomprime = false;
	        }
	        return descomprime;
		}
	    
	    
	    /**
	     * This method populates all the files in a directory to a List
	     * @param dir
	     * @throws IOException
	     */
	    private void populateFilesList(File dir) throws IOException {
	        File[] files = dir.listFiles();
	        for(File file : files){
	            if(file.isFile()) 
	            	filesListInDir.add(file.getAbsolutePath());
	            else 
	            	populateFilesList(file);
	        }
	    }
	 
	    /**
	     * This method compresses the single file to zip format
	     * @param file
	     * @param zipFileName
	     */
	    private static void zipSingleFile(File file, String zipFileName) {
	        try {
	            //create ZipOutputStream to write to the zip file
	            FileOutputStream fos = new FileOutputStream(zipFileName);
	            ZipOutputStream zos = new ZipOutputStream(fos);
	            //add a new Zip Entry to the ZipOutputStream
	            ZipEntry ze = new ZipEntry(file.getName());
	            zos.putNextEntry(ze);
	            //read the file and write to ZipOutputStream
	            FileInputStream fis = new FileInputStream(file);
	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = fis.read(buffer)) > 0) {
	                zos.write(buffer, 0, len);
	            }
	             
	            //Close the zip entry to write to zip file
	            zos.closeEntry();
	            //Close resources
	            zos.close();
	            fis.close();
	            fos.close();
	            // System.out.println(file.getCanonicalPath()+" is zipped to "+zipFileName);
	             
	        } catch (IOException e) {
	            Utils.imprimeLog("", e);
	        }
	 
	    }
	
	
}
