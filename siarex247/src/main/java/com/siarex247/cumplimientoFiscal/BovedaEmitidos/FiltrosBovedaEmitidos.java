package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

import java.math.BigDecimal;
import java.util.*;
import org.apache.log4j.Logger;

public class FiltrosBovedaEmitidos {

    public static final Logger logger = Logger.getLogger("siarex247");

    /* ===== helpers ===== */
    protected static String nz(String s){ return (s==null) ? "" : s.trim(); }

    protected static String normTxt(String s){
        String v = nz(s).toLowerCase();
        if (v.isEmpty()) return "contains";
        switch (v){
            case "=": case "==": case "equals": return "equals";
            case "!=": case "<>": case "notequals": return "notequals";
            case "startswith": case "starts-with": return "startswith";
            case "endswith":   case "ends-with":   return "endswith";
            case "notcontains": return "notcontains";
            default: return "contains";
        }
    }

    protected static String normNum(String s){
        String v = nz(s).toLowerCase();
        if (v.isEmpty()) return "eq";
        if ("between".equals(v) || "bt".equals(v) || "range".equals(v) || "entre".equals(v)) return "bt";
        if ("=".equals(v) || "==".equals(v) || "equals".equals(v)) return "eq";
        if ("!=".equals(v) || "<>".equals(v) || "notequals".equals(v)) return "ne";
        return v; // eq, ne, lt, gt, le, ge, bt
        }

    protected static BigDecimal toBD(String s){
        if (s==null) return null;
        String z = s.trim().replace(",", "");
        return z.isEmpty()? null : new BigDecimal(z);
    }

    protected static String opToSql(String op, String col){
        switch (normNum(op)){
            case "eq": return col + " = ?";
            case "ne": return col + " <> ?";
            case "lt": return col + " < ?";
            case "gt": return col + " > ?";
            case "le": return col + " <= ?";
            case "ge": return col + " >= ?";
            case "bt": return col + " BETWEEN ? AND ?";
            default:   return col + " = ?";
        }
    }

    /* ===== WHERE builder ===== */
    protected static class Where {
        public final StringBuilder sb;
        public final List<Object> params;
        private boolean hasWhere = false;

        public Where(StringBuilder sb, List<Object> params){ this.sb=sb; this.params=params; }
        public void and(String frag, Object... vals){
            if (frag==null || frag.isEmpty()) return;
            sb.append(hasWhere ? " AND " : " WHERE ").append(frag);
            if (vals!=null) for (Object v: vals) if (v!=null) params.add(v);
            hasWhere = true;
        }
    }

    /* ===== TEXT ===== */
    protected void addTextFilter(Where w, String column, String value, String operator){
        if (nz(value).isEmpty()) return;
        switch (normTxt(operator)){
            case "equals":      w.and(column + " = ?",  value); break;
            case "notequals":   w.and(column + " <> ?", value); break;
            case "startswith":  w.and(column + " LIKE ?", value + "%"); break;
            case "endswith":    w.and(column + " LIKE ?", "%" + value); break;
            case "notcontains": w.and(column + " NOT LIKE ?", "%" + value + "%"); break;
            default:            w.and(column + " LIKE ?", "%" + value + "%"); break;
        }
    }

    /* ===== FECHA ===== */
    protected void addDateFilter(Where w, String dateOperator, String d1, String d2,
                                 String fechaInicialLeg, String fechaFinalLeg){
        String op = nz(dateOperator).toLowerCase();
        String v1 = nz(d1), v2 = nz(d2);

        if (!op.isEmpty() && !v1.isEmpty()){
            switch (op) {
                case "eq": w.and("FECHA_FACTURA BETWEEN ? AND ?", v1+" 00:00:00", v1+" 23:59:59"); break;
                case "ne": w.and("NOT (FECHA_FACTURA BETWEEN ? AND ?)", v1+" 00:00:00", v1+" 23:59:59"); break;
                case "lt": w.and("FECHA_FACTURA < ?",  v1+" 00:00:00"); break;
                case "gt": w.and("FECHA_FACTURA > ?",  v1+" 23:59:59"); break;
                case "le": w.and("FECHA_FACTURA <= ?", v1+" 23:59:59"); break;
                case "ge": w.and("FECHA_FACTURA >= ?", v1+" 00:00:00"); break;
                case "bt": if (!v2.isEmpty()) w.and("FECHA_FACTURA BETWEEN ? AND ?", v1+" 00:00:00", v2+" 23:59:59"); break;
                default:   w.and("FECHA_FACTURA BETWEEN ? AND ?", v1+" 00:00:00", (v2.isEmpty()? v1 : v2)+" 23:59:59");
            }
            return;
        }
        // legado
        String fi = nz(fechaInicialLeg), ff = nz(fechaFinalLeg);
        if (!fi.isEmpty() && !ff.isEmpty()) w.and("FECHA_FACTURA BETWEEN ? AND ?", fi+" 00:00:01", ff+" 23:59:59");
    }

    /* ===== TIPO COMPROBANTE ===== */
    protected void addTipoComprobante(Where w, String tipoComprobante, String tipoOperator){
        String t = nz(tipoComprobante);
        if (t.isEmpty() || "ALL".equalsIgnoreCase(t)) return;

        String op = nz(tipoOperator).isEmpty()? "equals" : tipoOperator.trim().toLowerCase();
        switch (op){
            case "equals":    w.and("TIPO_COMPROBANTE = ?", t); break;
            case "notequals": w.and("TIPO_COMPROBANTE <> ?", t); break;
            case "startswith":  w.and("TIPO_COMPROBANTE LIKE ?", t + "%"); break;
            case "endswith":    w.and("TIPO_COMPROBANTE LIKE ?", "%" + t); break;
            case "notcontains": w.and("TIPO_COMPROBANTE NOT LIKE ?", "%" + t + "%"); break;
            default:            w.and("TIPO_COMPROBANTE LIKE ?", "%" + t + "%"); break;
        }
    }

    /* ===== NUMÉRICOS ===== */
    protected void addNumericFilter(Where w, String column, String operator, String v1, String v2){
        String op = normNum(operator);
        BigDecimal b1 = toBD(v1), b2 = toBD(v2);
        String frag = opToSql(op, column);
        if ("bt".equals(op)){ if (b1!=null && b2!=null) w.and(frag, b1, b2); }
        else { if (b1!=null) w.and(frag, b1); }
    }

    /* Folio: si hay op/v1/v2 → numérico; si no → exacto por texto */
    protected void addFolio(Where w, String folioTxt, String folioOperator, String folioV1, String folioV2, boolean castToDecimal){
        boolean folioNum = !nz(folioOperator).isEmpty() && (!nz(folioV1).isEmpty() || !nz(folioV2).isEmpty());
        if (folioNum){
            String col = castToDecimal ? "CAST(FOLIO AS DECIMAL(20,6))" : "FOLIO";
            addNumericFilter(w, col, folioOperator, folioV1, folioV2);
        } else if (!nz(folioTxt).isEmpty()){
            w.and("FOLIO = ?", folioTxt);
        }
    }

    /* IN helpers por si los usas en Excel/ZIP */
    protected void addUuidIn(Where w, List<String> uuids){
        if (uuids==null || uuids.isEmpty()) return;
        String ph = String.join(",", Collections.nCopies(uuids.size(), "?"));
        w.and("UUID IN ("+ph+")", uuids.toArray());
    }

    /* ===== Paquete de filtros para EMITIDOS =====
       OJO: RFC/Razón → RECEPTOR_*                      */
    protected void aplicarFiltrosBoveda(Where w,
                                        String rfc,   String rfcOp,
                                        String razon, String razonOp,
                                        String serie, String serieOp,
                                        String tipoComprobante, String tipoOp,
                                        String uuid,  String uuidOp,
                                        // fecha
                                        String dateOp, String dateV1, String dateV2,
                                        String fechaInicialLeg, String fechaFinalLeg,
                                        // numéricos
                                        String folio, String folioOp, String folioV1, String folioV2,
                                        String totalOp, String totalV1, String totalV2,
                                        String subOp,   String subV1,   String subV2,
                                        String ivaOp,   String ivaV1,   String ivaV2,
                                        String ivaRetOp,String ivaRetV1,String ivaRetV2,
                                        String isrOp,   String isrV1,   String isrV2,
                                        String impLocOp,String impLocV1,String impLocV2){
        addTextFilter(w, "RECEPTOR_RFC",    rfc,   rfcOp);
        addTextFilter(w, "RECEPTOR_NOMBRE", razon, razonOp);
        addTextFilter(w, "SERIE",           serie, serieOp);
        addTextFilter(w, "UUID",            uuid,  uuidOp);

        addDateFilter(w, dateOp, dateV1, dateV2, fechaInicialLeg, fechaFinalLeg);
        addTipoComprobante(w, tipoComprobante, tipoOp);

        addFolio(w, folio, folioOp, folioV1, folioV2, true);
        addNumericFilter(w, "TOTAL",               totalOp,   totalV1,   totalV2);
        addNumericFilter(w, "SUB_TOTAL",           subOp,     subV1,     subV2);
        addNumericFilter(w, "TOTAL_IMP_TRANSLADO", ivaOp,     ivaV1,     ivaV2);
        addNumericFilter(w, "TOTAL_IMP_RETENIDO",       ivaRetOp,  ivaRetV1,  ivaRetV2);
        addNumericFilter(w, "ISR_RET",             isrOp,     isrV1,     isrV2);
        addNumericFilter(w, "IMP_LOCALES",         impLocOp,  impLocV1,  impLocV2);
        
    }
}
