package com.siarex247.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ManipularLibros {
	private FileInputStream fiArchivoEntrada; 
	private POIFSFileSystem poiArchivo; 
	private HSSFWorkbook libro; 
	private HSSFSheet hoja; 
	private XSSFWorkbook myWorkBook = null;
	private XSSFSheet mySheet = null;
			
	 
	public static void main(String[] args) {
		XSSFSheet hojaMotivos;
		try {
			ManipularLibros librosExcel = new ManipularLibros();
			String fileXLS = "C:\\Tomcat9\\siarex247\\public_html\\DESCARGA_LOCAL\\MOTIVO_20250716.xlsx";
			librosExcel.cargarArchivoXLSX(fileXLS);
			hojaMotivos = librosExcel.obtenerHojaXLSX(0);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	 
	/*
	public static void main(String[] args) {
        String filePath = "C:\\Tomcat9\\siarex247\\public_html\\DESCARGA_LOCAL\\MOTIVO_20250716.xlsx";
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0); // Accede a la primera hoja (índice 0)

            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Obtiene el tipo de celda y lee el valor apropiado
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.print(cell.getDateCellValue() + "\t");
                            } else {
                                System.out.print(cell.getNumericCellValue() + "\t");
                            }
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t");
                            break;
                        default:
                            System.out.print("\t");
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

	public void cargarArchivo(String strRutaArchivoIn) throws FileNotFoundException, IOException { 
		StringBuilder builder; 
		if (strRutaArchivoIn == null) { 
			builder = new StringBuilder("Debe colocar una ruta valida, valor recibido["); 
			builder.append(strRutaArchivoIn).append("]"); 
			throw new IOException(builder.toString()); 
		} else if (strRutaArchivoIn.trim().length()> 0) {
			fiArchivoEntrada = new FileInputStream(strRutaArchivoIn); 
			poiArchivo = new POIFSFileSystem(fiArchivoEntrada); 
		} else { 
			builder = new StringBuilder("Debe colocar una ruta v�lida, valor recibido["); 
			builder.append(strRutaArchivoIn).append("]"); 
			throw new IOException(builder.toString()); 
		} 
	}
	
	
	public void cargarArchivoXLSX(String strRutaArchivoIn) throws FileNotFoundException, IOException { 
		StringBuilder builder; 
		if (strRutaArchivoIn == null) { 
			builder = new StringBuilder("Debe colocar una ruta v�lida, valor recibido["); 
			builder.append(strRutaArchivoIn).append("]"); 
			throw new IOException(builder.toString()); 
		} else if (strRutaArchivoIn.trim().length()> 0) {
			fiArchivoEntrada = new FileInputStream(strRutaArchivoIn); 
			myWorkBook = new XSSFWorkbook (strRutaArchivoIn); 
		} else { 
			builder = new StringBuilder("Debe colocar una ruta v�lida, valor recibido["); 
			builder.append(strRutaArchivoIn).append("]"); 
			throw new IOException(builder.toString()); 
			} 
	}
	
	
	public POIFSFileSystem obtenerPOIFS() { 
		return poiArchivo; 
	} 
	
	
	/** * <pre>* Con este m�todo obtenemos el libro basado en un * objeto v�lido de tipo POIFSFileSystem. * </pre> * @return HSSFWorkbook * @throws IOException */ 
	public HSSFWorkbook obtenerLibro() throws IOException { 
		if (poiArchivo != null) { 
			libro = new HSSFWorkbook(poiArchivo); 
		} 
		return libro; 
	} 
	
	public HSSFWorkbook obtenerLibroXLSX() throws IOException { 
		if (poiArchivo != null) { 
			libro = new HSSFWorkbook(poiArchivo); 
		} 
		return libro; 
	} 
	
	
	/** * <pre>* Con este m�todo obtenemos la hoja indicada con un indice * que inicia desde 0 seg�n la hoja que se desee procesar * </pre> * @param intIndiceIn * @return HSSFSheet * @throws IOException */ 
	public HSSFSheet obtenerHoja(int intIndiceIn) throws IOException { 
		libro = obtenerLibro(); 
		hoja = libro.getSheetAt(intIndiceIn); 
		return hoja; 
	} 

	
	public XSSFSheet obtenerHojaXLSX(int intIndiceIn) throws IOException { 
		mySheet = myWorkBook.getSheetAt(intIndiceIn);
		return mySheet; 
	} 

	
	/** * * @return */ 
	public String toString() { 
		StringBuilder b; 
		b = new StringBuilder(); 
		Iterator<Row> row = hoja.rowIterator(); 
		int i = 1, h = 1; 
		while (row.hasNext()) { 
			Row r = (Row) row.next(); 
			h = 1; 
			Iterator<Cell> cel = r.cellIterator(); 
			b.append("Registro ").append(i++).append(":\n"); 
			while (cel.hasNext()) { 
				b.append(" Celda ").append(h++).append(": "); 
				Cell c = (Cell) cel.next();
				b.append(" ").append(c.toString());
				b.append("\n"); 
			} 
			b.append("\n"); 
			} 
		return b.toString(); 
		}
 }