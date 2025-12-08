package com.siarex247.visor.VisorOrdenes;

public class FiltrosVisorOrdenesFiltros {

  // ===== Texto
  public static final String COL_RAZON_SOCIAL   = "B.RAZON_SOCIAL";
  public static final String COL_ORDEN_COMPRA   = "A.FOLIO_EMPRESA";
  public static final String COL_DESCRIPCION    = "A.CONCEPTO";
  public static final String COL_SERIE_FOLIO    = "A.SERIE";
  public static final String COL_ASIGNAR_A      = "A.ASIGNAR_A";
  public static final String COL_ESTADO_CFDI    = "A.ESTADO_SAT";
  public static final String COL_USO_CFDI       = "A.USO_CFDI";
  public static final String COL_CLAVE_PRODSERV = "A.CLAVE_PRODUCTO_SERVICIO";

  // ===== Select / catálogos
  public static final String COL_TIPO_MONEDA    = "A.TIPO_MONEDA";
  public static final String COL_SERV_RECIBO    = "A.SERVICIO_PRODUCTO"; // S / N
  public static final String COL_ESTATUS_PAGO   = "A.ESTATUS_PAGO";
  public static final String COL_ESTATUS_SAT    = "A.ESTATUS_SAT";       // S / N

  // ===== Numéricos
  public static final String COL_MONTO          = "A.MONTO";
  public static final String COL_TOTAL          = "A.TOTAL";
  public static final String COL_SUBTOTAL       = "A.SUB_TOTAL";
  public static final String COL_IVA            = "A.IVA";
  public static final String COL_IVA_RET        = "A.IVA_RET";
  public static final String COL_ISR_RET        = "A.ISR_RET";
  public static final String COL_IMP_LOCALES    = "A.IMP_LOCALES";
  public static final String COL_TOTAL_NC       = "D.TOTAL_PAGADO";
  public static final String COL_PAGO_TOTAL     = "A.TOTAL - IFNULL(D.TOTAL_PAGADO,0)";
  public static final String COL_IVA_RET_NC     = "D.ISR_RET"; // ajusta si corresponde a otro campo

  // ===== Fechas
  public static final String COL_FECHA_PAGO     = "A.FECHAPAGO";
  public static final String COL_ULT_MOV        = "A.FECHAULTMOV";

  public static abstract class Where { public abstract void and(String frag, Object... vals); }

  private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
  private static String  nz(String s){ return (s == null) ? "" : s.trim(); }

  private static String normTextOp(String opRaw){
    String op = nz(opRaw); String l = op.toLowerCase();
    if ("equals".equals(l)) return "equals";
    if ("notequals".equals(l) || "notEquals".equals(op)) return "notEquals";
    if ("contains".equals(l)) return "contains";
    if ("notcontains".equals(l) || "notContains".equals(op)) return "notContains";
    if ("startswith".equals(l) || "startsWith".equals(op)) return "startsWith";
    if ("endswith".equals(l) || "endsWith".equals(op)) return "endsWith";
    return "contains";
  }
  private static String normNumOp(String op){ op = nz(op).toLowerCase(); return
    ("between".equals(op) ? "bt" : op.matches("eq|ne|lt|gt|le|ge|bt") ? op : "eq"); }
  private static String normDateOp(String op){ op = nz(op).toLowerCase(); return
    ("between".equals(op) ? "bt" : op.matches("eq|ne|lt|gt|le|ge|bt") ? op : "eq"); }

  private static void textOp(Where w, String column, String value, String opRaw){
    if (isBlank(column) || isBlank(value)) return;
    final String v = nz(value); final String op = normTextOp(opRaw);
    switch (op){
      case "equals":      w.and(column + " = ?"      , v);           break;
      case "notEquals":   w.and(column + " <> ?"     , v);           break;
      case "startsWith":  w.and(column + " like ?"   , v + "%");     break;
      case "endsWith":    w.and(column + " like ?"   , "%" + v);     break;
      case "notContains": w.and(column + " not like ?", "%" + v + "%"); break;
      case "contains":
      default:            w.and(column + " like ?"   , "%" + v + "%");   break;
    }
  }

  private static void numOp(Where w, String column, String v1, String v2, String opRaw){
    if (isBlank(column)) return;
    final String op = normNumOp(opRaw);
    final boolean h1 = !isBlank(v1), h2 = !isBlank(v2);
    switch (op){
      case "ne": if (h1) w.and(column + " <> ?", v1); break;
      case "lt": if (h1) w.and(column + " <  ?", v1); break;
      case "gt": if (h1) w.and(column + " >  ?", v1); break;
      case "le": if (h1) w.and(column + " <= ?", v1); break;
      case "ge": if (h1) w.and(column + " >= ?", v1); break;
      case "bt":
        if (h1 && h2) w.and(column + " between ? AND ?", v1, v2);
        else if (h1)  w.and(column + " >= ?", v1);
        else if (h2)  w.and(column + " <= ?", v2);
        break;
      case "eq":
      default: if (h1)  w.and(column + " = ?", v1); break;
    }
  }

  private static void dateOp(Where w, String column, String d1, String d2, String opRaw){
    if (isBlank(column)) return;
    final String op = normDateOp(opRaw);
    final boolean h1 = !isBlank(d1), h2 = !isBlank(d2);
    switch (op){
      case "ne": if (h1) w.and("date(" + column + ") <> ?", d1); break;
      case "lt": if (h1) w.and("date(" + column + ") <  ?", d1); break;
      case "gt": if (h1) w.and("date(" + column + ") >  ?", d1); break;
      case "le": if (h1) w.and("date(" + column + ") <= ?", d1); break;
      case "ge": if (h1) w.and("date(" + column + ") >= ?", d1); break;
      case "bt":
        if (h1 && h2) w.and("date(" + column + ") between ? and ?", d1, d2);
        else if (h1)  w.and("date(" + column + ") >= ?", d1);
        else if (h2)  w.and("date(" + column + ") <= ?", d2);
        break;
      case "eq":
      default: if (h1)  w.and("date(" + column + ") = ?", d1); break;
    }
  }

  /** API con columnas por defecto (coinciden con tu SELECT de Visor). */
  public static void aplicarFiltrosVisor(
		    Where w,

		    // ======== TEXTO / SELECTS ========
		    String razonSocial,   String rsOp,

		    // ======== ORDEN DE COMPRA (NUM) ========
		    String ocOp, String ocV1, String ocV2,

		    // ======== resto TEXTO / SELECTS ========
		    String descripcion,   String descOp,
		    String tipoMoneda,    String monedaOp,
		    String servRecibo,    String reciboOp,
		    String estatusPago,   String estPagoOp,
		    String serieFolio,    String serieFolioOp,
		    String asignarA,      String asignarOp,
		    String estadoCfdi,    String estadoCfdiOp,
		    String estatusSat,    String estSatOp,
		    String usoCfdi,       String usoCfdiOp,
		    String cps,           String cpsOp,

		    // ======== NUMÉRICOS ========
		    String montoOp,     String montoV1,     String montoV2,
		    String totalOp,     String totalV1,     String totalV2,
		    String subtotalOp,  String subtotalV1,  String subtotalV2,
		    String ivaOp,       String ivaV1,       String ivaV2,
		    String ivaRetOp,    String ivaRetV1,    String ivaRetV2,
		    String isrRetOp,    String isrRetV1,    String isrRetV2,
		    String impLocOp,    String impLocV1,    String impLocV2,
		    String totalNcOp,   String totalNcV1,   String totalNcV2,
		    String pagoTotOp,   String pagoTotV1,   String pagoTotV2,
		    String ivaRetNcOp,  String ivaRetNcV1,  String ivaRetNcV2,

		    // ======== FECHAS ========
		    String fechaPagoOp, String fechaPagoV1, String fechaPagoV2,
		    String ultMovOp,   String ultMovV1,    String ultMovV2
		){
		    // Texto / selects
		    textOp(w, COL_RAZON_SOCIAL, razonSocial, rsOp);

		    // ✅ OC como número
		    numOp(w, COL_ORDEN_COMPRA, ocV1, ocV2, ocOp);

		    // Resto texto/select
		    textOp(w, COL_DESCRIPCION , descripcion, descOp);
		    textOp(w, COL_TIPO_MONEDA , tipoMoneda,  monedaOp);
		    textOp(w, COL_SERV_RECIBO , servRecibo,  reciboOp);
		    textOp(w, COL_ESTATUS_PAGO, estatusPago, estPagoOp);
		    textOp(w, COL_SERIE_FOLIO , serieFolio,  serieFolioOp);
		    textOp(w, COL_ASIGNAR_A   , asignarA,    asignarOp);
		    textOp(w, COL_ESTADO_CFDI , estadoCfdi,  estadoCfdiOp);
		    textOp(w, COL_ESTATUS_SAT , estatusSat,  estSatOp);
		    textOp(w, COL_USO_CFDI    , usoCfdi,     usoCfdiOp);
		    textOp(w, COL_CLAVE_PRODSERV, cps,       cpsOp);

		    // Numéricos (igual que ya estaban)
		    numOp(w, COL_MONTO,       montoV1,    montoV2,    montoOp);
		    numOp(w, COL_TOTAL,       totalV1,    totalV2,    totalOp);
		    numOp(w, COL_SUBTOTAL,    subtotalV1, subtotalV2, subtotalOp);
		    numOp(w, COL_IVA,         ivaV1,      ivaV2,      ivaOp);
		    numOp(w, COL_IVA_RET,     ivaRetV1,   ivaRetV2,   ivaRetOp);
		    numOp(w, COL_ISR_RET,     isrRetV1,   isrRetV2,   isrRetOp);
		    numOp(w, COL_IMP_LOCALES, impLocV1,   impLocV2,   impLocOp);
		    numOp(w, COL_TOTAL_NC,    totalNcV1,  totalNcV2,  totalNcOp);
		    numOp(w, COL_PAGO_TOTAL,  pagoTotV1,  pagoTotV2,  pagoTotOp);
		    numOp(w, COL_IVA_RET_NC,  ivaRetNcV1, ivaRetNcV2, ivaRetNcOp);

		    // Fechas
		    dateOp(w, COL_FECHA_PAGO, fechaPagoV1, fechaPagoV2, fechaPagoOp);
		    dateOp(w, COL_ULT_MOV,    ultMovV1,    ultMovV2,    ultMovOp);
		}

}
