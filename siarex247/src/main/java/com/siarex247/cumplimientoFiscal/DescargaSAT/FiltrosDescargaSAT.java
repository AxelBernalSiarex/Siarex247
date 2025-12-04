package com.siarex247.cumplimientoFiscal.DescargaSAT;

/**
 * Helper para construir WHERE dinámico con operadores DX-like
 * en la pantalla de Descarga Masiva (Recibidos).
 *
 * Uso típico desde el Bean:
 *
 *   StringBuilder sb = new StringBuilder(DescargaSATQuerys.getDetalle(esquema));
 *   java.util.List<Object> params = new java.util.ArrayList<>();
 *   FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
 *     @Override public void and(String frag, Object... vals) {
 *       if (frag == null || frag.isEmpty()) return;
 *       sb.append(" AND ").append(frag);
 *       if (vals != null) for (Object v : vals) if (v != null) params.add(v);
 *     }
 *   };
 *
 *   FiltrosDescargaSAT.aplicarFiltrosRecibidos(
 *       w,
 *       // TEXTO (valor + operador)
 *       uuid,        uuidOp,
 *       rfcEmisor,   rfcEmiOp,
 *       razonEmisor, nomEmiOp,
 *       rfcReceptor, rfcRecOp,
 *       razonReceptor, nomRecOp,
 *       pacVal,      pacOp,
 *       efectoVal,   efectoOp,
 *       estatusVal,  estatusOp,
 *       bovedaVal,   bovedaOp,
 *       // FECHAS (emisión con fallback a globales)
 *       emiOp, emiV1, emiV2, fechaIniGlobal, fechaFinGlobal,
 *       // certificación
 *       cerOp, cerV1, cerV2,
 *       // cancelación
 *       canOp, canV1, canV2,
 *       // NUMÉRICO
 *       montoOp, montoV1, montoV2
 *   );
 *
 *   // luego ORDER/LIMIT y bind de params
 */
public class FiltrosDescargaSAT {

  /* =========================
   *  Columnas por defecto (coinciden con tu SELECT)
   * ========================= */
  public static final String COL_UUID           = "UUID";
  public static final String COL_RFC_EMISOR     = "EMISOR_RFC";
  public static final String COL_RAZON_EMISOR   = "EMISOR_NOMBRE";
  public static final String COL_RFC_RECEPTOR   = "RECEPTOR_RFC";
  public static final String COL_RAZON_RECEPTOR = "RECEPTOR_NOMBRE";
  public static final String COL_PAC            = "RECEPTOR_PAC";
  public static final String COL_EFECTO         = "EFECTO_COMPROBANTE";
  public static final String COL_ESTATUS        = "ESTATUS";
  public static final String COL_BOVEDA         = "EXISTE_BOVEDA";

  public static final String COL_TOTAL          = "MONTO";                // numérico
  public static final String COL_FECHA_EMISION  = "FECHA_EMISION";        // date/datetime
  public static final String COL_FECHA_CERT     = "FECHA_CERTIFICACION";  // date/datetime
  public static final String COL_FECHA_CANCEL   = "FECHA_CANCELACION";    // date/datetime

  /** Interfaz compatible con tu patrón (StringBuilder + params). */
  public static abstract class Where {
    public abstract void and(String frag, Object... vals);
  }

  /* =========================
   *  Normalizadores / utils
   * ========================= */
  private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
  private static String  nz(String s){ return (s == null) ? "" : s.trim(); }

  private static String normTextOp(String opRaw){
	  String op = nz(opRaw).trim();
	  String opl = op.toLowerCase();
	  if ("equals".equals(opl))                       return "equals";
	  if ("notequals".equals(opl) || "notEquals".equals(op))     return "notEquals";
	  if ("contains".equals(opl))                     return "contains";
	  if ("notcontains".equals(opl) || "notContains".equals(op)) return "notContains";
	  if ("startswith".equals(opl) || "startsWith".equals(op))   return "startsWith";
	  if ("endswith".equals(opl)  || "endsWith".equals(op))      return "endsWith";
	  return "contains";
	}

  private static String normNumOp(String op){
    op = nz(op).toLowerCase();
    switch (op){
      case "eq": case "ne": case "lt": case "gt": case "le": case "ge":
      case "between": case "bt":
        return op;
      default: return "eq";
    }
  }

  private static String normDateOp(String op){
    op = nz(op).toLowerCase();
    switch (op){
      case "eq": case "ne": case "lt": case "gt": case "le": case "ge":
      case "between": case "bt":
        return (op.equals("between") ? "bt" : op);
      default: return "eq";
    }
  }

  /* =========================
   *  Builders por tipo
   * ========================= */
  private static void textOp(Where w, String column, String value, String opRaw){
	// Evita LIKE redundante sobre RECEPTOR_RFC cuando parece RFC completo
	  if ("RECEPTOR_RFC".equalsIgnoreCase(column)) {
		  final String v = nz(value);
		  final String opNorm = normTextOp(opRaw); // contains (default), equals, notcontains, etc.
		  // Sólo auto-forzar '=' cuando NO viene operador explícito
		  final boolean sinOperador = isBlank(opRaw);
		  if (sinOperador && (v.length() == 12 || v.length() == 13)) {
		    if (!isBlank(v)) { w.and(column + " = ?", v); }
		    return; // auto-equals sólo en modo "sin operador"
		  }
		}

    if (isBlank(value) || isBlank(column)) return;
    final String v  = nz(value);
    final String op = normTextOp(opRaw);

    switch (op){
      case "equals":
        w.and(column + " = ?", v); break;
      case "notEquals":
        w.and(column + " <> ?", v); break;
      case "startsWith":
      case "startswith":
        w.and(column + " LIKE ?", v + "%"); break;
      case "endsWith":
      case "endswith":
        w.and(column + " LIKE ?", "%" + v); break;
      case "notContains":
        w.and(column + " NOT LIKE ?", "%" + v + "%"); break;
      case "contains":
      default:
        w.and(column + " LIKE ?", "%" + v + "%"); break;
    }
  }

  private static void numOp(Where w, String column, String v1, String v2, String opRaw){
    if (isBlank(column)) return;
    final String op   = normNumOp(opRaw);
    final boolean h1  = !isBlank(v1);
    final boolean h2  = !isBlank(v2);

    switch (op){
      case "ne": if (h1) w.and(column + " <> ?", v1); break;
      case "lt": if (h1) w.and(column + " <  ?", v1); break;
      case "gt": if (h1) w.and(column + " >  ?", v1); break;
      case "le": if (h1) w.and(column + " <= ?", v1); break;
      case "ge": if (h1) w.and(column + " >= ?", v1); break;
      case "between":
      case "bt":
        if (h1 && h2)      w.and(column + " BETWEEN ? AND ?", v1, v2);
        else if (h1)       w.and(column + " >= ?", v1);
        else if (h2)       w.and(column + " <= ?", v2);
        break;
      case "eq":
      default:
        if (h1) w.and(column + " = ?", v1);
        break;
    }
  }

  private static void dateOp(Where w, String column, String d1, String d2, String opRaw){
    if (isBlank(column)) return;
    final String op   = normDateOp(opRaw);
    final boolean h1  = !isBlank(d1);
    final boolean h2  = !isBlank(d2);

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

  /* =========================
   *  API principal (columnas por defecto)
   * ========================= */
  public static void aplicarFiltrosRecibidos(
      Where w,
      // TEXTO
      String uuid,          String uuidOp,
      String rfcEmisor,     String rfcEmiOp,
      String razonEmisor,   String nomEmiOp,
      String rfcReceptor,   String rfcRecOp,
      String razonReceptor, String nomRecOp,
      String pacVal,        String pacOp,
      String efectoVal,     String efectoOp,
      String estatusVal,    String estatusOp,
      String bovedaVal,     String bovedaOp,

      // FECHAS (emisión con fallback global)
      String emiOp, String emiV1, String emiV2, String fechaIniFallback, String fechaFinFallback,
      // certificación
      String cerOp, String cerV1, String cerV2,
      // cancelación
      String canOp, String canV1, String canV2,

      // NUMÉRICO
      String montoOp, String montoV1, String montoV2
  ){
    // ===== TEXTO =====
    textOp(w, COL_UUID,           uuid,          uuidOp);
    textOp(w, COL_RFC_EMISOR,     rfcEmisor,     rfcEmiOp);
    textOp(w, COL_RAZON_EMISOR,   razonEmisor,   nomEmiOp);
    textOp(w, COL_RFC_RECEPTOR,   rfcReceptor,   rfcRecOp);
    textOp(w, COL_RAZON_RECEPTOR, razonReceptor, nomRecOp);
    textOp(w, COL_PAC,            pacVal,        pacOp);
    textOp(w, COL_EFECTO,         efectoVal,     efectoOp);
    textOp(w, COL_ESTATUS,        estatusVal,    estatusOp);
    textOp(w, COL_BOVEDA,         bovedaVal,     bovedaOp);

    // ===== NUMÉRICO =====
    numOp(w, COL_TOTAL, montoV1, montoV2, montoOp);

    // ===== FECHAS =====
    // Emisión con respaldo a globales de cabecera
    final String d1Emi = !isBlank(emiV1) ? emiV1 : nz(fechaIniFallback);
    final String d2Emi = !isBlank(emiV2) ? emiV2 : nz(fechaFinFallback);
    dateOp(w, COL_FECHA_EMISION, d1Emi, d2Emi, normDateOp(emiOp));

    // Certificación / Cancelación tal cual vengan del menú
    dateOp(w, COL_FECHA_CERT,   cerV1, cerV2, normDateOp(cerOp));
    dateOp(w, COL_FECHA_CANCEL, canV1, canV2, normDateOp(canOp));
  }

  /* =========================
   *  Overload (columnas personalizadas)
   * ========================= */
  public static void aplicarFiltrosRecibidos(
      Where w,
      // columnas
      String colUuid, String colRfcEmi, String colRazEmi, String colRfcRec, String colRazRec,
      String colPac, String colEfecto, String colEstatus, String colBoveda,
      String colTotal, String colFechaEmi, String colFechaCert, String colFechaCanc,

      // TEXTO
      String uuid,          String uuidOp,
      String rfcEmisor,     String rfcEmiOp,
      String razonEmisor,   String nomEmiOp,
      String rfcReceptor,   String rfcRecOp,
      String razonReceptor, String nomRecOp,
      String pacVal,        String pacOp,
      String efectoVal,     String efectoOp,
      String estatusVal,    String estatusOp,
      String bovedaVal,     String bovedaOp,

      // FECHAS (emisión con fallback)
      String emiOp, String emiV1, String emiV2, String fechaIniFallback, String fechaFinFallback,
      // certificación
      String cerOp, String cerV1, String cerV2,
      // cancelación
      String canOp, String canV1, String canV2,

      // NUMÉRICO
      String montoOp, String montoV1, String montoV2
  ){
    // Texto
    textOp(w, nz(colUuid),    uuid,          uuidOp);
    textOp(w, nz(colRfcEmi),  rfcEmisor,     rfcEmiOp);
    textOp(w, nz(colRazEmi),  razonEmisor,   nomEmiOp);
    textOp(w, nz(colRfcRec),  rfcReceptor,   rfcRecOp);
    textOp(w, nz(colRazRec),  razonReceptor, nomRecOp);
    textOp(w, nz(colPac),     pacVal,        pacOp);
    textOp(w, nz(colEfecto),  efectoVal,     efectoOp);
    textOp(w, nz(colEstatus), estatusVal,    estatusOp);
    textOp(w, nz(colBoveda),  bovedaVal,     bovedaOp);

    // Numérico
    numOp(w, nz(colTotal), montoV1, montoV2, montoOp);

    // Fechas
    final String d1Emi = !isBlank(emiV1) ? emiV1 : nz(fechaIniFallback);
    final String d2Emi = !isBlank(emiV2) ? emiV2 : nz(fechaFinFallback);
    dateOp(w, nz(colFechaEmi),  d1Emi,  d2Emi, normDateOp(emiOp));
    dateOp(w, nz(colFechaCert), cerV1,  cerV2, normDateOp(cerOp));
    dateOp(w, nz(colFechaCanc), canV1,  canV2, normDateOp(canOp));
  }
}
