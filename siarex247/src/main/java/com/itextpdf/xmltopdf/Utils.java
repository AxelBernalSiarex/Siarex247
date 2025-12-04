package com.itextpdf.xmltopdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;



public class Utils {

	
	public static Object noNulo (Object cadena){
		return validaNulo(cadena).toString().trim();
	}
	
	
	public static String noNulo (String cadena){
		return validaNulo(cadena).toString().trim().toUpperCase();
	}
	
	public static String noNuloNormal (String cadena){
		return validaNulo(cadena).toString().trim();
	}
	
	public static Object noNuloUpperCase (Object cadena){
		return validaNulo(cadena).toString().trim().toUpperCase();
	}
	
	
	
	private static Object validaNulo (Object cadena){
		if (cadena == null){
			cadena = "";
		}
		return cadena;
	}
	
	public static int noNuloINT(String cadena){
		if (cadena == null || "".equals(cadena)){
			cadena = "0";
		}
		return Integer.parseInt(cadena.trim());
	}

	public static int noNuloINT(Object cadena){
		if (cadena == null){
			cadena = "0";
		}
		return Integer.parseInt(cadena.toString());
	}

	
	public static double noNuloDouble(String cadena){
		if (cadena == null || "".equals(cadena)){
			cadena = "0";
		}
		return Double.parseDouble(cadena.trim());
	}

	public static double noNuloDouble(Object cadena){
		try {
			if (cadena == null){
				cadena = "0";
			}
			return Double.parseDouble(cadena.toString());	
		}catch(Exception e) {
			cadena = "0";
		}
		return Double.parseDouble(cadena.toString());
	}
	

	public static float noNuloFloat(String cadena){
		if (cadena == null || "".equals(cadena)){
			cadena = "0";
		}
		return Float.parseFloat(cadena.trim());
	}

	public static float noNuloFloat(Object cadena){
		try {
			if (cadena == null){
				cadena = "0";
			}
			return Float.parseFloat(cadena.toString());	
		}catch(Exception e) {
			cadena = "0";
		}
		return Float.parseFloat(cadena.toString());
	}

	}
