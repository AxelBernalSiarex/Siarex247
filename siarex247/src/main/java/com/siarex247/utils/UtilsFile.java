package com.siarex247.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import jakarta.servlet.http.Part;

public class UtilsFile {
	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char SEPARATOS_SAT = '~';
    private static final char DEFAULT_QUOTE = '"';
    
    
	public static void crearArchivo(ArrayList<String> datosTXT, String rutaDestino) {
		FileWriter flwriter = null;
		try {
			//crea el flujo para escribir en el archivo
			flwriter = new FileWriter(rutaDestino);
			//crea un buffer o flujo intermedio antes de escribir directamente en el archivo
			BufferedWriter bfwriter = new BufferedWriter(flwriter);
			for (String uuid : datosTXT) {
				//escribe los datos en el archivo
				bfwriter.write(uuid+ "\n");
			}
			//cierra el buffer intermedio
			bfwriter.close();
		} catch (IOException e) {
			Utils.imprimeLog("", e);
		} finally {
			if (flwriter != null) {
				try {//cierra el flujo principal
					flwriter.close();
				} catch (IOException e) {
					Utils.imprimeLog("", e);
				}
			}
		}
	}
	
	
	public static void crearArchivoSalto(ArrayList<String> datosTXT, String rutaDestino) {
		FileWriter flwriter = null;
		try {
			//crea el flujo para escribir en el archivo
			flwriter = new FileWriter(rutaDestino);
			//crea un buffer o flujo intermedio antes de escribir directamente en el archivo
			BufferedWriter bfwriter = new BufferedWriter(flwriter);
			for (int x = 0; x < datosTXT.size(); x++) {
				String cadena = new String(datosTXT.get(x)+ "\r\n");
				//escribe los datos en el archivo
				bfwriter.write(cadena);
			}
			//cierra el buffer intermedio
			bfwriter.close();
		} catch (IOException e) {
			Utils.imprimeLog("", e);
		} finally {
			if (flwriter != null) {
				try {//cierra el flujo principal
					flwriter.close();
				} catch (IOException e) {
					Utils.imprimeLog("", e);
				}
			}
		}
	}
	
	
	public static String moveFileDirectory(
			File sourceFile,
			File destFile,
			boolean overwrite,
			boolean preserveLastModified,
			boolean datos)
			throws IOException {
		return moveFileDirectory(sourceFile, destFile, overwrite, preserveLastModified, datos, true);
	}
	
	
	public static String moveFileDirectory(
			File sourceFile,
			File destFile,
			boolean overwrite,
			boolean preserveLastModified,
			boolean datos,
			boolean deleteFile)
			throws IOException {
//fsource,fdes, true, false, true
			try {
				if (overwrite
					|| !destFile.exists()
					|| destFile.lastModified() < sourceFile.lastModified()) {
					File parent = new File(destFile.getParent());
					if (!parent.exists()) {
						parent.mkdirs();

					}

					FileInputStream in = new FileInputStream(sourceFile);
					FileOutputStream out = new FileOutputStream(destFile);

					byte[] buffer = new byte[8 * 1024];
					int count = 0;
					do {
						if (datos) {
							out.write(buffer, 0, count);
						}
						count = in.read(buffer, 0, buffer.length);
					} while (count != -1);

					in.close();
					out.close();

					if (preserveLastModified) {
						destFile.setLastModified(sourceFile.lastModified());
						
					}
				}
				if (deleteFile) {
					sourceFile.delete();	
				}
				
			} catch (Exception ex) {
				Utils.imprimeLog("", ex);
			}
			return "";
		}
	
	
	public static boolean existe(String ruta){
		File archivo = new File(ruta);
		return archivo.exists();
	}

	
	public static boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        
        
        File parent = new File(destination.getParent());
		if (!parent.exists()) {
			parent.mkdirs();
		}
		
        
        if (origin.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
	
	/*
	public static String leeArchivo(String nombreArchivo){
		File f = new File(nombreArchivo);
		//File aux = null;
		RandomAccessFile raf = null;
		String linea = "";	
		StringBuffer sbArchivo = new StringBuffer();
		int i = 0;		
		try{
			raf = new RandomAccessFile(f, "r");
			//int x = 1;
			while(raf.read() != -1){
				//if (x > 1){
					raf.seek(i);
					linea = raf.readLine();
					sbArchivo.append(linea);
					i = i + linea.length() + 1;	
				//}
				//x++;
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				raf.close();
				raf = null;
			}catch (IOException ex){
				raf = null;
			}
		}
		return sbArchivo.toString();
	}
	*/
	
	public static String leeArchivo(String nombreArchivo){
		File f = new File(nombreArchivo);
		//File aux = null;
		RandomAccessFile raf = null;
		String linea = "";
		String UTF8 = "";
		StringBuffer sbArchivo = new StringBuffer();
		int i = 0;		
		try{
			raf = new RandomAccessFile(f, "r");
			while(raf.read() != -1){
				//if (x > 1){
					raf.seek(i);
					linea = raf.readLine();
					UTF8 = new String(linea.getBytes("ISO-8859-1"), "UTF-8");
					sbArchivo.append(UTF8);
					i = i + linea.length() + 1;	
				//}
				//x++;
			}
		}catch(FileNotFoundException e){
			Utils.imprimeLog("", e);
		}catch(IOException e){
			Utils.imprimeLog("", e);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				raf.close();
				raf = null;
			}catch (IOException ex){
				raf = null;
			}
		}
		return sbArchivo.toString();
	}
	
	
	public static ArrayList<String> leeArchivoTXT(String nombreArchivo){
		File f = new File(nombreArchivo);
		RandomAccessFile raf = null;
		String linea = "";	
		ArrayList<String> respuesta = new ArrayList<String>();
		int i = 0;		
		String UTF8 = "";
		try{
			raf = new RandomAccessFile(f, "r");
			while(raf.read() != -1){
				raf.seek(i);
				linea = raf.readLine();
				UTF8 = new String(linea.getBytes("ISO-8859-1"), "UTF-8");
				if (!"".equals(linea)){
					if (UTF8.indexOf("|") > -1){
						respuesta.add(UTF8.replace("|", ";"));
					}else{
						respuesta.add(UTF8);	
					}
					
				}
				i = i + linea.length() + 1;
				
			}
		}catch(FileNotFoundException e){
			Utils.imprimeLog("", e);
		}catch(IOException e){
			Utils.imprimeLog("", e);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				raf.close();
				raf = null;
			}catch (IOException ex){
				raf = null;
			}
		}
		return respuesta;
	}

	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

	
	 public static List<String> parseLineSAT(String cvsLine) {
	        return parseLine(cvsLine, SEPARATOS_SAT, DEFAULT_QUOTE);
	  }
	 
	 
	public static void eliminaArchivo(String rutaArchivo){
		File file = new File(rutaArchivo);
		file.delete();
	}
	
	
	public static void deleteDirectoryContentA(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryContentA(file); // Recursión para subdirectorios
                }
                file.delete();
            }
        }
    }
	

	public static File getFileFromPart (Part filePart) throws IOException {
		String fileName = filePart.getSubmittedFileName();
    	
    	InputStream inputStream = filePart.getInputStream();
    	File file = new File("/home/tempDir/" + fileName);
    	FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        return file;
       }
    	
  
	public static ArrayList<File> getFilesFromPart (Collection<Part> colectionPart) throws IOException {
        ArrayList<File> listFiles = new ArrayList<File>(); 
        for (Part filePart : colectionPart) {
        	try {
        		//logger.info("headerName==>"+filePart.getSubmittedFileName());
            	String fileName = filePart.getSubmittedFileName();
            	InputStream inputStream = filePart.getInputStream();
            	File file = new File("/home/tempDir/" + fileName);
            	FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                if (fileName == null || "null".equalsIgnoreCase(fileName)) {
                }else {
                  	listFiles.add(file);
                }
                outputStream.close();
        	}catch(Exception e) {
        		
        	}
        }
        
        return listFiles;
       }
	
	public static String getContentType (File filePath) throws IOException {
		Path pathFile = Paths.get(filePath.getAbsolutePath());
		return Files.probeContentType(pathFile);
	}


	
	public static List<String> obtenerArchivosRecursivamente(String directorioPath) {
        List<String> archivos = new ArrayList<>();
        File directorio = new File(directorioPath);

        if (directorio.exists() && directorio.isDirectory()) {
            File[] contenido = directorio.listFiles();

            if (contenido != null) {
                for (File elemento : contenido) {
                    if (elemento.isFile()) {
                        archivos.add(elemento.getAbsolutePath());
                    } else if (elemento.isDirectory()) {
                        archivos.addAll(obtenerArchivosRecursivamente(elemento.getAbsolutePath()));
                    }
                }
            }
        } else {
            System.err.println("El directorio no existe o no es válido: " + directorioPath);
        }

        return archivos;
    }
	
	public static void main(String[] args) {
		try {
			Long.parseLong("MULTIPLE");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
