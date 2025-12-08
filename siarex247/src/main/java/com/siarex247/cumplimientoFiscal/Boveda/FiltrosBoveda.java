package com.siarex247.cumplimientoFiscal.Boveda;

/**
 * Helper para construir WHERE dinámico con operadores estilo DevExtreme/DX
 * para la pantalla de Bóveda (Recibidos).
 *
 * Uso típico desde el Bean:
 *
 *   StringBuilder sb = new StringBuilder(BovedaQuerys.getConsultaBoveda(esquema));
 *   List<Object> params = new ArrayList<>();
 *   FiltrosBoveda.Where w = new FiltrosBoveda.Where(sb, params);
 *
 *   FiltrosBoveda.aplicarFiltrosBoveda(
 *     w,
 *     // TEXTO
 *     rfc, rfcOp,
 *     razonEmisor, razonOp,
 *     serie, serieOp,
 *     tipoComprobante, tipoOp,
 *     uuid, uuidOp,
 *     // FECHAS (con fallback cabecera)
 *     dateOp, dateV1, dateV2, fechaIniCab, fechaFinCab,
 *     // NUMÉRICOS (incluye FOLIO numérico; si FOLIO viene como texto, también se filtra con textOp)
 *     folioText, folioOp, folioV1, folioV2,
 *     totalOp, totalV1, totalV2,
 *     subOp, subV1, subV2,
 *     ivaTrasOp, ivaTrasV1, ivaTrasV2,
 *     ivaRetOp, ivaRetV1, ivaRetV2,
 *     isrOp, isrV1, isrV2,
 *     impLocOp, impLocV1, impLocV2
 *   );
 *
 *   // ORDER/LIMIT y bind de params
 */
public class FiltrosBoveda {

  // =========================
  //  Logger
  // =========================
  private static final org.apache.log4j.Logger logger =
      org.apache.log4j.Logger.getLogger(FiltrosBoveda.class);

  // =========================
  //  Columnas por defecto (ajústalas si tu SELECT cambia)
  // =========================
  public static final String COL_UUID            = "UUID";
  public static final String COL_SERIE           = "SERIE";
  public static final String COL_FOLIO           = "FOLIO";               // puede ser VARCHAR en DB
  public static final String COL_FECHA           = "FECHA_FACTURA";

  public static final String COL_SUBTOTAL        = "SUB_TOTAL";
  public static final String COL_TOTAL           = "TOTAL";
  public static final String COL_IVA_TRAS        = "TOTAL_IMP_TRANSLADO";
  public static final String COL_IVA_RET         = "RETENCION_IVA";
  public static final String COL_ISR_RET         = "ISR_RET";
  // Si manejas impuestos locales en tu tabla, cambia por el nombre real; si no, déjalo vacío para ignorarlo
  public static final String COL_IMP_LOCALES     = ""; // "IMPUESTOS_LOCALES"

  public static final String COL_RFC_EMISOR      = "EMISOR_RFC";
  public static final String COL_RAZON_EMISOR    = "EMISOR_NOMBRE";
  public static final String COL_RFC_RECEPTOR    = "RECEPTOR_RFC";
  public static final String COL_RAZON_RECEPTOR  = "RECEPTOR_NOMBRE";
  public static final String COL_TIPO            = "TIPO_COMPROBANTE";

  // Para operar numéricamente sobre FOLIO (si es VARCHAR en la DB)
  public static final String EXPR_FOLIO_NUM      = "CAST(" + COL_FOLIO + " AS DECIMAL(20,0))";

//Dentro de FiltrosBoveda.java
//=====================================================
//Helper para construir WHERE de forma segura y loggable
//=====================================================
public static class Where {
   private final StringBuilder sb;
   private final java.util.List<Object> params;
   private boolean hasWhere = false; // si el SQL base ya traía WHERE

   public Where(StringBuilder sb, java.util.List<Object> params){
       this.sb = sb;
       this.params = params;
   }

   /** “Siembra” el estado: true si el SQL base YA traía WHERE, false si no. */
   public void seedHasWhere(boolean baseHasWhere){
       this.hasWhere = baseHasWhere;
   }

   /** Útil si necesitas garantizar WHERE antes de añadir condiciones sueltas. */
   public void ensureWhere(){
       if (!hasWhere){
           sb.append(" WHERE 1=1");
           hasWhere = true;
       }
   }

   /** Añade un fragmento condicional con bind de parámetros (usa WHERE la 1ª vez, luego AND). */
   public Where and(String fragment, Object... values){
       if (fragment == null || fragment.trim().isEmpty()) return this;
       if (!hasWhere){
           sb.append(" WHERE ");
           hasWhere = true;
       } else {
           sb.append(" AND ");
       }
       sb.append(fragment);

       if (values != null){
           for (Object v : values){
               if (v != null) params.add(v);
           }
       }

       // Log amigable (opcional)
       try {
           if (logger != null) {
               java.util.List<Object> vals = new java.util.ArrayList<>();
               if (values != null) for (Object v: values) if (v != null) vals.add(v);
           //    logger.info("      + " + fragment + "   ◀ vals=" + vals);
           }
       } catch (Throwable ignore){}

       return this;
   }

   /** Igual que and(), pero sin parámetros (fragment ya debe traer placeholders listos). */
   public Where andRaw(String fragment){
       if (fragment == null || fragment.trim().isEmpty()) return this;
       if (!hasWhere){
           sb.append(" WHERE ");
           hasWhere = true;
       } else {
           sb.append(" AND ");
       }
       sb.append(fragment);
       return this;
   }
}


  // =========================
  //  Utils / Normalizadores
  // =========================
  private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
  private static String  nz(String s){ return s == null ? "" : s.trim(); }

  private static String normTextOp(String op){
    op = nz(op).toLowerCase();
    switch (op) {
      case "equals": case "=":                        return "equals";
      case "notequals": case "notEquals":
      case "!=": case "<>":                           return "notEquals";
      case "contains": case "like":                   return "contains";
      case "notcontains": case "not like":            return "notContains";
      case "startswith": case "startsWith":
      case "start": case "sw":                        return "startsWith";
      case "endswith": case "endsWith":
      case "end": case "ew":                          return "endsWith";
      default:                                        return "contains";
    }
  }

  private static String normNumOp(String op){
    op = nz(op).toLowerCase();
    switch (op){
      case "eq": case "=": return "eq";
      case "ne": case "!=": case "<>": return "ne";
      case "lt": case "<": return "lt";
      case "gt": case ">": return "gt";
      case "le": case "<=": return "le";
      case "ge": case ">=": return "ge";
      case "between": case "bt": return "bt";
      default: return "eq";
    }
  }

  private static String normDateOp(String op){
    op = nz(op).toLowerCase();
    switch (op){
      case "eq": case "=": return "eq";
      case "ne": case "!=": case "<>": return "ne";
      case "lt": case "<": return "lt";
      case "gt": case ">": return "gt";
      case "le": case "<=": return "le";
      case "ge": case ">=": return "ge";
      case "between": case "bt": return "bt";
      default: return "eq";
    }
  }

  // =========================
  //  Builders por tipo
  // =========================
  private static void textOp(Where w, String column, String value, String opRaw){
    if (isBlank(column) || isBlank(value)) return;
    final String v  = nz(value);
    final String op = normTextOp(opRaw);

   // logger.info(String.format("[FiltrosBoveda.textOp] col=%s op=%s val=%s", column, op, v));

    switch (op){
      case "equals":
        w.and(column + " = ?", v); break;
      case "notEquals":
        w.and(column + " <> ?", v); break;
      case "startsWith":
        w.and(column + " LIKE ?", v + "%"); break;        // C%
      case "endsWith":
        w.and(column + " LIKE ?", "%" + v); break;        // %C
      case "notContains":
        w.and(column + " NOT LIKE ?", "%" + v + "%"); break;
      case "contains":
      default:
        w.and(column + " LIKE ?", "%" + v + "%"); break;  // %C%
    }
  }

  /** Uso con columnas verdaderamente numéricas (TOTAL, SUB_TOTAL, etc.)
   *  y también con expresiones numéricas (p.ej., CAST(FOLIO AS DECIMAL(20,0))) */
  private static void numOp(Where w, String columnOrExpr, String v1, String v2, String opRaw){
    if (isBlank(columnOrExpr)) return;
    final String op  = normNumOp(opRaw);
    final boolean h1 = !isBlank(v1);
    final boolean h2 = !isBlank(v2);

    switch (op){
      case "ne": if (h1) w.and(columnOrExpr + " <> ?", v1); break;
      case "lt": if (h1) w.and(columnOrExpr + " <  ?", v1); break;
      case "gt": if (h1) w.and(columnOrExpr + " >  ?", v1); break;
      case "le": if (h1) w.and(columnOrExpr + " <= ?", v1); break;
      case "ge": if (h1) w.and(columnOrExpr + " >= ?", v1); break;
      case "bt":
        if (h1 && h2)      w.and(columnOrExpr + " BETWEEN ? AND ?", v1, v2);
        else if (h1)       w.and(columnOrExpr + " >= ?", v1);
        else if (h2)       w.and(columnOrExpr + " <= ?", v2);
        break;
      case "eq":
      default:
        if (h1) w.and(columnOrExpr + " = ?", v1);
        break;
    }
  }

  private static void dateOp(Where w, String column, String d1, String d2, String opRaw){
    if (isBlank(column)) return;
    final String op  = normDateOp(opRaw);
    final boolean h1 = !isBlank(d1);
    final boolean h2 = !isBlank(d2);

    switch (op){
      case "ne": if (h1) w.and("DATE(" + column + ") <> ?", d1); break;
      case "lt": if (h1) w.and("DATE(" + column + ") <  ?", d1); break;
      case "gt": if (h1) w.and("DATE(" + column + ") >  ?", d1); break;
      case "le": if (h1) w.and("DATE(" + column + ") <= ?", d1); break;
      case "ge": if (h1) w.and("DATE(" + column + ") >= ?", d1); break;
      case "bt":
        if (h1 && h2)      w.and("DATE(" + column + ") BETWEEN ? AND ?", d1, d2);
        else if (h1)       w.and("DATE(" + column + ") >= ?", d1);
        else if (h2)       w.and("DATE(" + column + ") <= ?", d2);
        break;
      case "eq":
      default:
        if (h1) w.and("DATE(" + column + ") =  ?", d1);
        break;
    }
  }

  // =========================
  //  API principal (columnas por defecto)
  // =========================
  public static void aplicarFiltrosBoveda(
      Where w,
      // TEXTO
      String rfcEmisor,     String rfcOp,
      String razonEmisor,   String razonOp,
      String serie,         String serieOp,
      String tipoComprobante, String tipoOp,
      String uuid,          String uuidOp,

      // FECHAS (con fallback a cabecera)
      String dateOpRaw,     String dateV1, String dateV2,
      String fechaIniFallback, String fechaFinFallback,

      // NUMÉRICOS (incluye FOLIO numérico) + FOLIO como texto (si se llena)
      String folioText,     String folioNumOp, String folioV1, String folioV2,
      String totalOp,       String totalV1, String totalV2,
      String subOp,         String subV1, String subV2,
      String ivaTrasOp,     String ivaTrasV1, String ivaTrasV2,
      String ivaRetOp,      String ivaRetV1, String ivaRetV2,
      String isrOp,         String isrV1, String isrV2,
      String impLocOp,      String impLocV1, String impLocV2
  ){
    // ===== TEXTO =====
    textOp(w, COL_RFC_EMISOR,     rfcEmisor,     rfcOp);
    textOp(w, COL_RAZON_EMISOR,   razonEmisor,   razonOp);
    textOp(w, COL_SERIE,          serie,         serieOp);
    textOp(w, COL_TIPO,           tipoComprobante, tipoOp);
    textOp(w, COL_UUID,           uuid,          uuidOp);

    // FOLIO como texto (si usuario teclea algo en el input de texto)
    textOp(w, COL_FOLIO,          folioText,     "contains");

    // ===== FECHAS =====
    final String d1 = !isBlank(dateV1) ? dateV1 : nz(fechaIniFallback);
    final String d2 = !isBlank(dateV2) ? dateV2 : nz(fechaFinFallback);
    dateOp(w, COL_FECHA, d1, d2, dateOpRaw);

    // ===== NUMÉRICOS =====
    // FOLIO numérico (si tu columna es VARCHAR en DB se castea con EXPR_FOLIO_NUM)
    numOp(w, EXPR_FOLIO_NUM, folioV1, folioV2, folioNumOp);

    numOp(w, COL_TOTAL,     totalV1,    totalV2,    totalOp);
    numOp(w, COL_SUBTOTAL,  subV1,      subV2,      subOp);
    numOp(w, COL_IVA_TRAS,  ivaTrasV1,  ivaTrasV2,  ivaTrasOp);
    numOp(w, COL_IVA_RET,   ivaRetV1,   ivaRetV2,   ivaRetOp);
    numOp(w, COL_ISR_RET,   isrV1,      isrV2,      isrOp);

    if (!isBlank(COL_IMP_LOCALES)) {
      numOp(w, COL_IMP_LOCALES, impLocV1, impLocV2, impLocOp);
    }
  }

  // =========================
  //  Overload (columnas personalizadas)
  // =========================
  public static void aplicarFiltrosBoveda(
      Where w,
      // columnas personalizadas
      String colRfcEmi, String colRazEmi, String colSerie,
      String colTipo, String colUuid, String colFecha,
      String colFolio, String exprFolioNum,
      String colTotal, String colSub, String colIvaTras, String colIvaRet,
      String colIsr, String colImpLocales,

      // TEXTO
      String rfcEmisor,     String rfcOp,
      String razonEmisor,   String razonOp,
      String serie,         String serieOp,
      String tipoComprobante, String tipoOp,
      String uuid,          String uuidOp,

      // FECHAS (con fallback)
      String dateOpRaw,     String dateV1, String dateV2,
      String fechaIniFallback, String fechaFinFallback,

      // NUMÉRICOS + FOLIO numérico + FOLIO texto
      String folioText,     String folioNumOp, String folioV1, String folioV2,
      String totalOp,       String totalV1, String totalV2,
      String subOp,         String subV1, String subV2,
      String ivaTrasOp,     String ivaTrasV1, String ivaTrasV2,
      String ivaRetOp,      String ivaRetV1, String ivaRetV2,
      String isrOp,         String isrV1, String isrV2,
      String impLocOp,      String impLocV1, String impLocV2
  ){
    // Texto
    textOp(w, nz(colRfcEmi),  rfcEmisor,     rfcOp);
    textOp(w, nz(colRazEmi),  razonEmisor,   razonOp);
    textOp(w, nz(colSerie),   serie,         serieOp);
    textOp(w, nz(colTipo),    tipoComprobante, tipoOp);
    textOp(w, nz(colUuid),    uuid,          uuidOp);
    textOp(w, nz(colFolio),   folioText,     "contains");

    // Fechas
    final String d1 = !isBlank(dateV1) ? dateV1 : nz(fechaIniFallback);
    final String d2 = !isBlank(dateV2) ? dateV2 : nz(fechaFinFallback);
    dateOp(w, nz(colFecha), d1, d2, dateOpRaw);

    // Numéricos
    final String folioExpr = isBlank(exprFolioNum) ? ("CAST(" + nz(colFolio) + " AS DECIMAL(20,0))") : exprFolioNum;
    numOp(w, folioExpr, folioV1, folioV2, folioNumOp);

    numOp(w, nz(colTotal),   totalV1,    totalV2,    totalOp);
    numOp(w, nz(colSub),     subV1,      subV2,      subOp);
    numOp(w, nz(colIvaTras), ivaTrasV1,  ivaTrasV2,  ivaTrasOp);
    numOp(w, nz(colIvaRet),  ivaRetV1,   ivaRetV2,   ivaRetOp);
    numOp(w, nz(colIsr),     isrV1,      isrV2,      isrOp);

    if (!isBlank(colImpLocales)) {
      numOp(w, nz(colImpLocales), impLocV1, impLocV2, impLocOp);
    }
  }

}
