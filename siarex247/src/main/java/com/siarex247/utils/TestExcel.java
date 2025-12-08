package com.siarex247.utils;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestExcel {

	public static void generaExcel() {
		try {
				
			XSSFWorkbook workbook = new XSSFWorkbook(); 
		
            // Create a new sheet
            Sheet sheet = workbook.createSheet("Sheet1");

            // Create a row
            Row row = sheet.createRow(0);

            // Create cells and add data
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("Name");

            Cell cell2 = row.createCell(1);
            cell2.setCellValue("Age");

            // Create another row
            Row row2 = sheet.createRow(1);

            Cell cell3 = row2.createCell(0);
            cell3.setCellValue("John Doe");

            Cell cell4 = row2.createCell(1);
            cell4.setCellValue(30);

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream("C:\\testPDF\\my_excel_file.xlsx")) {
                workbook.write(fileOut);
            }
            System.out.println("Excel file created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		try {
			TestExcel.generaExcel();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
