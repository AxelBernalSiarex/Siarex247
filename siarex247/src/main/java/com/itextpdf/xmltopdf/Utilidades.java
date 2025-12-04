package com.itextpdf.xmltopdf;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public final class Utilidades {
        public static float cmToFloat(float centimetro) {
                return centimetro * 28.3464567f;
        }

        public static float[] cmToFloat(float[] centimetro) {
                for (int i = 0; i < centimetro.length; i++) {
                        centimetro[i] = centimetro[i] * 28.3464567f;
                }
                return centimetro;
        }

        public static String convertir(double valor) {
                return NumberFormat.getCurrencyInstance(Locale.US).format(valor);
        }

        public static String ToDate(String fecha) {
                try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date dia = formatter.parse(fecha);
                        return formatter.format(dia);
                } catch (Exception e) {
                        return "";
                }
        }

        public static String ToString(LocalDate fecha) {
                String sFecha = "";
                try {
                        return sFecha = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception error) {
                        sFecha = "";
                }
                return sFecha;
        }

        public static String ToString(LocalDateTime fecha) {
                String sFecha = "";
                try {
                        sFecha = fecha.format(DateTimeFormatter.ISO_DATE_TIME);
                        return sFecha;
                } catch (Exception error) {
                        sFecha = "";
                }
                return sFecha;
        }

}
