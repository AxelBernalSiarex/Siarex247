package com.siarex247.estadisticas.reporteNomina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.siarex247.catalogos.sat.Catalogos.CatalogosBean;
import com.siarex247.catalogos.sat.Catalogos.CatalogosForm;
import com.siarex247.cumplimientoFiscal.BovedaNomina.FiltrosBovedaNomina;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;

public class ReporteNominaBean extends FiltrosBovedaNomina{

	
	public static final Logger logger = Logger.getLogger("siarex247");

	
	public ArrayList<ReporteNominaForm> detalleReporteNomina(
	        Connection con, String esquema,
	        // legado (valores libres que pueden venir de inputs simples)
	        String rfc,  String razonSocial, String folio, String serie,
	        String fechaInicial, String uuidBoveda, String fechaFinal,
	        // ===== operadores/valores DX-like =====
	        // texto
	        String rfcOperator,   String razonOperator, String serieOperator, String uuidOperator,
	        // fecha
	        String dateOperator,  String dateV1,        String dateV2,
	        // numéricos
	        String folioOperator, String folioV1,       String folioV2,
	        String totalOperator, String totalV1,       String totalV2,
	        String subOperator,   String subV1,         String subV2,
	        String descOperator,  String descV1,        String descV2,
	        String percOperator,  String percV1,        String percV2,
	        String dedOperator,   String dedV1,         String dedV2
	){
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<ReporteNominaForm> listaDetalle = new ArrayList<>();

	    try {
	        StringBuilder sb = new StringBuilder(ReporteNominaQuerys.getDetalleReporte(esquema));
	        ArrayList<Object> params = new ArrayList<>();

	        // ¿El SELECT base ya trae WHERE?
	        boolean baseHasWhere = sb.toString().toLowerCase().contains(" where ");

	        // Builder WHERE compatible con baseHasWhere
	        FiltrosBovedaNomina.Where w = new FiltrosBovedaNomina.Where(sb, params);
	        if (baseHasWhere){
	            try {
	                java.lang.reflect.Field f = FiltrosBovedaNomina.Where.class.getDeclaredField("hasWhere");
	                f.setAccessible(true);
	                f.setBoolean(w, true);
	            } catch (Exception ignore) {}
	        }

	        // 1) Si te llega un UUID directo por legacy (uuidBoveda), priorízalo (equivale a "=")
	       // if (uuidBoveda != null && !uuidBoveda.trim().isEmpty()){
	        //    w.and("E.UUID = ?", uuidBoveda.trim());
	       // }

	        // 2) Aplica TODOS los filtros DX-like (texto/fecha/numéricos)
	        FiltrosBovedaNomina filtros = new FiltrosBovedaNomina();
	        filtros.aplicarFiltrosNomina(
	            w,
	            // texto
	            rfc,          rfcOperator,
	            razonSocial,  razonOperator,
	            serie,        serieOperator,
	            uuidBoveda,   uuidOperator,
	            // fecha (con respaldo a fechaInicial/fechaFinal legacy)
	            dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
	            // numéricos
	            folio, folioOperator, folioV1, folioV2,
	            totalOperator, totalV1, totalV2,
	            subOperator,   subV1,   subV2,
	            descOperator,  descV1,  descV2,
	            percOperator,  percV1,  percV2,
	            dedOperator,   dedV1,   dedV2
	        );

	        sb.append(" ORDER BY E.FECHA_FACTURA DESC ");

	        stmt = con.prepareStatement(sb.toString());

	        // IMPORTANTE:
	        // - NO hardcodeo "N" como en Bóveda porque tu SELECT de ReporteNomina no lo usa
	        // - Solo bindeo params generados por Where/filtros, en el orden en que se añadieron
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof java.math.BigDecimal) {
	                stmt.setBigDecimal(idx++, (java.math.BigDecimal) p);
	            } else {
	                stmt.setString(idx++, String.valueOf(p));
	            }
	        }

	        logger.info("stmt ReporteNomina ===> " + stmt);

	        rs = stmt.executeQuery();

	        // ======= mapeo 1:1 con tu método original =======
	        ReporteNominaForm repForm = new ReporteNominaForm();
	        PercepcionesForm percepciones = new PercepcionesForm();
	        DeduccionesForm deducciones = new DeduccionesForm();
	        OtroPagosForm otroPagos = new OtroPagosForm();

	        while (rs.next()) {
	            int numCol = 1;
	            repForm.setIdRegistro(rs.getInt(numCol++));
	            repForm.setUuid(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setTipoNomina(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFechaPago(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFechaInicialPago(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFechaFinalPago(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setNumDiasPagados(rs.getDouble(numCol++));
	            repForm.setSubsidioCausadoDouble(rs.getDouble(numCol++));
	            repForm.setTotalPercepcionesDouble(rs.getDouble(numCol++));
	            repForm.setTotalDeduccionesDouble(rs.getDouble(numCol++));
	            repForm.setTotalOtroPagosDouble(rs.getDouble(numCol++));
	            repForm.setTotalDouble(rs.getDouble(numCol++));
	            repForm.setOrigenRecurso(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setEstatusSAT(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setSerie(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFolio(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFechaFactura(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFechaTimbrado(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setEmisorRFC(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setEmisorNombre(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setRegistroPatronal(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setReceptorRFC(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setReceptorNombre(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setCurp(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setNumeroSeguroSocial(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setFechaInicioLaboral(Utils.noNulo(rs.getString(numCol++)));

	            repForm.setAntiguedad(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setTipoContrato(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setTipoJornada(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setTipoRegimen(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setNumEmpleado(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setRiesgoPuesto(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setDepartamento(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setPuesto(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setSindicalizado(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setPeriodicidadPago(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setSalarioBaseCotAporDouble(rs.getDouble(numCol++));
	            repForm.setSalarioDiarioIntegradoDouble(rs.getDouble(numCol++));
	            repForm.setClaveEntFed(Utils.noNulo(rs.getString(numCol++)));
	            repForm.setIngresoAcumulableDouble(rs.getDouble(numCol++));
	            repForm.setIngresoNoAcumulableDouble(rs.getDouble(numCol++));
	            repForm.setUltimoSueldoMensualDouble(rs.getDouble(numCol++));
	            repForm.setAniosServicio(rs.getDouble(numCol++));
	            repForm.setTotalPagadoDouble(rs.getDouble(numCol++));

	            // ===== Percepciones 001..052 (gravado/exento) =====
	            percepciones.setPercepcionGravado001(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento001(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado002(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento002(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado003(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento003(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado004(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento004(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado005(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento005(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado006(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento006(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado007(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento007(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado008(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento008(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado009(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento009(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado010(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento010(rs.getDouble(numCol++));

	            percepciones.setPercepcionGravado011(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento011(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado012(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento012(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado013(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento013(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado014(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento014(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado015(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento015(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado016(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento016(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado017(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento017(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado018(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento018(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado019(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento019(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado020(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento020(rs.getDouble(numCol++));

	            percepciones.setPercepcionGravado021(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento021(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado022(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento022(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado023(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento023(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado024(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento024(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado025(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento025(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado026(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento026(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado027(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento027(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado028(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento028(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado029(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento029(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado030(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento030(rs.getDouble(numCol++));

	            percepciones.setPercepcionGravado031(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento031(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado032(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento032(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado033(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento033(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado034(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento034(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado035(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento035(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado036(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento036(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado037(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento037(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado038(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento038(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado039(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento039(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado040(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento040(rs.getDouble(numCol++));

	            percepciones.setPercepcionGravado041(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento041(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado042(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento042(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado043(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento043(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado044(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento044(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado045(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento045(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado046(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento046(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado047(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento047(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado048(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento048(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado049(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento049(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado050(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento050(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado051(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento051(rs.getDouble(numCol++));
	            percepciones.setPercepcionGravado052(rs.getDouble(numCol++));
	            percepciones.setPercepcionExento052(rs.getDouble(numCol++));

	            // ===== Deducciones 001..110 =====
	            DeduccionesForm d = deducciones;
	            d.setDeducciones001(rs.getDouble(numCol++));  d.setDeducciones002(rs.getDouble(numCol++));
	            d.setDeducciones003(rs.getDouble(numCol++));  d.setDeducciones004(rs.getDouble(numCol++));
	            d.setDeducciones005(rs.getDouble(numCol++));  d.setDeducciones006(rs.getDouble(numCol++));
	            d.setDeducciones007(rs.getDouble(numCol++));  d.setDeducciones008(rs.getDouble(numCol++));
	            d.setDeducciones009(rs.getDouble(numCol++));  d.setDeducciones010(rs.getDouble(numCol++));
	            d.setDeducciones011(rs.getDouble(numCol++));  d.setDeducciones012(rs.getDouble(numCol++));
	            d.setDeducciones013(rs.getDouble(numCol++));  d.setDeducciones014(rs.getDouble(numCol++));
	            d.setDeducciones015(rs.getDouble(numCol++));  d.setDeducciones016(rs.getDouble(numCol++));
	            d.setDeducciones017(rs.getDouble(numCol++));  d.setDeducciones018(rs.getDouble(numCol++));
	            d.setDeducciones019(rs.getDouble(numCol++));  d.setDeducciones020(rs.getDouble(numCol++));
	            d.setDeducciones021(rs.getDouble(numCol++));  d.setDeducciones022(rs.getDouble(numCol++));
	            d.setDeducciones023(rs.getDouble(numCol++));  d.setDeducciones024(rs.getDouble(numCol++));
	            d.setDeducciones025(rs.getDouble(numCol++));  d.setDeducciones026(rs.getDouble(numCol++));
	            d.setDeducciones027(rs.getDouble(numCol++));  d.setDeducciones028(rs.getDouble(numCol++));
	            d.setDeducciones029(rs.getDouble(numCol++));  d.setDeducciones030(rs.getDouble(numCol++));
	            d.setDeducciones031(rs.getDouble(numCol++));  d.setDeducciones032(rs.getDouble(numCol++));
	            d.setDeducciones033(rs.getDouble(numCol++));  d.setDeducciones034(rs.getDouble(numCol++));
	            d.setDeducciones035(rs.getDouble(numCol++));  d.setDeducciones036(rs.getDouble(numCol++));
	            d.setDeducciones037(rs.getDouble(numCol++));  d.setDeducciones038(rs.getDouble(numCol++));
	            d.setDeducciones039(rs.getDouble(numCol++));  d.setDeducciones040(rs.getDouble(numCol++));
	            d.setDeducciones041(rs.getDouble(numCol++));  d.setDeducciones042(rs.getDouble(numCol++));
	            d.setDeducciones043(rs.getDouble(numCol++));  d.setDeducciones044(rs.getDouble(numCol++));
	            d.setDeducciones045(rs.getDouble(numCol++));  d.setDeducciones046(rs.getDouble(numCol++));
	            d.setDeducciones047(rs.getDouble(numCol++));  d.setDeducciones048(rs.getDouble(numCol++));
	            d.setDeducciones049(rs.getDouble(numCol++));  d.setDeducciones050(rs.getDouble(numCol++));
	            d.setDeducciones051(rs.getDouble(numCol++));  d.setDeducciones052(rs.getDouble(numCol++));
	            d.setDeducciones053(rs.getDouble(numCol++));  d.setDeducciones054(rs.getDouble(numCol++));
	            d.setDeducciones055(rs.getDouble(numCol++));  d.setDeducciones056(rs.getDouble(numCol++));
	            d.setDeducciones057(rs.getDouble(numCol++));  d.setDeducciones058(rs.getDouble(numCol++));
	            d.setDeducciones059(rs.getDouble(numCol++));  d.setDeducciones060(rs.getDouble(numCol++));
	            d.setDeducciones061(rs.getDouble(numCol++));  d.setDeducciones062(rs.getDouble(numCol++));
	            d.setDeducciones063(rs.getDouble(numCol++));  d.setDeducciones064(rs.getDouble(numCol++));
	            d.setDeducciones065(rs.getDouble(numCol++));  d.setDeducciones066(rs.getDouble(numCol++));
	            d.setDeducciones067(rs.getDouble(numCol++));  d.setDeducciones068(rs.getDouble(numCol++));
	            d.setDeducciones069(rs.getDouble(numCol++));  d.setDeducciones070(rs.getDouble(numCol++));
	            d.setDeducciones071(rs.getDouble(numCol++));  d.setDeducciones072(rs.getDouble(numCol++));
	            d.setDeducciones073(rs.getDouble(numCol++));  d.setDeducciones074(rs.getDouble(numCol++));
	            d.setDeducciones075(rs.getDouble(numCol++));  d.setDeducciones076(rs.getDouble(numCol++));
	            d.setDeducciones077(rs.getDouble(numCol++));  d.setDeducciones078(rs.getDouble(numCol++));
	            d.setDeducciones079(rs.getDouble(numCol++));  d.setDeducciones080(rs.getDouble(numCol++));
	            d.setDeducciones081(rs.getDouble(numCol++));  d.setDeducciones082(rs.getDouble(numCol++));
	            d.setDeducciones083(rs.getDouble(numCol++));  d.setDeducciones084(rs.getDouble(numCol++));
	            d.setDeducciones085(rs.getDouble(numCol++));  d.setDeducciones086(rs.getDouble(numCol++));
	            d.setDeducciones087(rs.getDouble(numCol++));  d.setDeducciones088(rs.getDouble(numCol++));
	            d.setDeducciones089(rs.getDouble(numCol++));  d.setDeducciones090(rs.getDouble(numCol++));
	            d.setDeducciones091(rs.getDouble(numCol++));  d.setDeducciones092(rs.getDouble(numCol++));
	            d.setDeducciones093(rs.getDouble(numCol++));  d.setDeducciones094(rs.getDouble(numCol++));
	            d.setDeducciones095(rs.getDouble(numCol++));  d.setDeducciones096(rs.getDouble(numCol++));
	            d.setDeducciones097(rs.getDouble(numCol++));  d.setDeducciones098(rs.getDouble(numCol++));
	            d.setDeducciones099(rs.getDouble(numCol++));  d.setDeducciones100(rs.getDouble(numCol++));
	            d.setDeducciones101(rs.getDouble(numCol++));  d.setDeducciones102(rs.getDouble(numCol++));
	            d.setDeducciones103(rs.getDouble(numCol++));  d.setDeducciones104(rs.getDouble(numCol++));
	            d.setDeducciones105(rs.getDouble(numCol++));  d.setDeducciones106(rs.getDouble(numCol++));
	            d.setDeducciones107(rs.getDouble(numCol++));  d.setDeducciones108(rs.getDouble(numCol++));
	            d.setDeducciones109(rs.getDouble(numCol++));  d.setDeducciones110(rs.getDouble(numCol++));

	            // ===== Otros pagos 001..020 + 999 =====
	            otroPagos.setOtroPago001(rs.getDouble(numCol++));  otroPagos.setOtroPago002(rs.getDouble(numCol++));
	            otroPagos.setOtroPago003(rs.getDouble(numCol++));  otroPagos.setOtroPago004(rs.getDouble(numCol++));
	            otroPagos.setOtroPago005(rs.getDouble(numCol++));  otroPagos.setOtroPago006(rs.getDouble(numCol++));
	            otroPagos.setOtroPago007(rs.getDouble(numCol++));  otroPagos.setOtroPago008(rs.getDouble(numCol++));
	            otroPagos.setOtroPago009(rs.getDouble(numCol++));  otroPagos.setOtroPago010(rs.getDouble(numCol++));
	            otroPagos.setOtroPago011(rs.getDouble(numCol++));  otroPagos.setOtroPago012(rs.getDouble(numCol++));
	            otroPagos.setOtroPago013(rs.getDouble(numCol++));  otroPagos.setOtroPago014(rs.getDouble(numCol++));
	            otroPagos.setOtroPago015(rs.getDouble(numCol++));  otroPagos.setOtroPago016(rs.getDouble(numCol++));
	            otroPagos.setOtroPago017(rs.getDouble(numCol++));  otroPagos.setOtroPago018(rs.getDouble(numCol++));
	            otroPagos.setOtroPago019(rs.getDouble(numCol++));  otroPagos.setOtroPago020(rs.getDouble(numCol++));
	            otroPagos.setOtroPago999(rs.getDouble(numCol++));

	            repForm.setPercepciones(percepciones);
	            repForm.setDeducciones(deducciones);
	            repForm.setOtrosPagos(otroPagos);

	            listaDetalle.add(repForm);

	            // Reiniciar contenedores para la siguiente fila
	            repForm = new ReporteNominaForm();
	            percepciones = new PercepcionesForm();
	            deducciones = new DeduccionesForm();
	            otroPagos   = new OtroPagosForm();
	        }
	    } catch (Exception e) {
	        Utils.imprimeLog("detalleReporteNominaDX", e);
	    } finally {
	        try { if (rs != null)   rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return listaDetalle;
	}


	
	
	public ArrayList<String> percepcionesReporte(Connection con, String esquema) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaPercepciones = new ArrayList<>();
		try {
			
			stmt = con.prepareStatement(ReporteNominaQuerys.getTipoPercepcionesExcel(esquema));
        	rs = stmt.executeQuery();
			// DecimalFormat decimal = new DecimalFormat("###,###.##");
			while (rs.next()) {
				listaPercepciones.add(Utils.noNulo(rs.getString(1)));
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
				if (stmt != null)
					stmt.close();
				stmt = null;
			} catch (Exception e) {
				stmt = null;
			}
		}
		return listaPercepciones;
	}
	
	
	public ArrayList<String> deduccionesReporte(Connection con, String esquema) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaDeducciones = new ArrayList<>();
		try {
			
			stmt = con.prepareStatement(ReporteNominaQuerys.getTipoDeduccionesExcel(esquema));
        	rs = stmt.executeQuery();
			while (rs.next()) {
				listaDeducciones.add(Utils.noNulo(rs.getString(1)));
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
				if (stmt != null)
					stmt.close();
				stmt = null;
			} catch (Exception e) {
				stmt = null;
			}
		}
		return listaDeducciones;
	}
	
	
	public ArrayList<String> otroPagosReporte(Connection con, String esquema) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaOtroPagos = new ArrayList<>();
		try {
			
			stmt = con.prepareStatement(ReporteNominaQuerys.getTipoOtroPagosExcel(esquema));
        	rs = stmt.executeQuery();
			while (rs.next()) {
				listaOtroPagos.add(Utils.noNulo(rs.getString(1)));
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
				if (stmt != null)
					stmt.close();
				stmt = null;
			} catch (Exception e) {
				stmt = null;
			}
		}
		return listaOtroPagos;
	}
	
	
	public void toExcelNomina(Connection conSAT,
            SXSSFSheet hoja1,
            ArrayList<ReporteNominaForm> datosBoveda,
            ArrayList<String> listaPercepciones,
            ArrayList<String> listaDeducciones,
            ArrayList<String> listaOtroPagos,
            SXSSFWorkbook objLibro,
            int regInicial,
            int regFinal) {
			try {
			Row header  = hoja1.createRow(0); // título superior
			Row header2 = null;               // fila de grupos (2)
			Row header3 = null;               // subtítulos (3)
			header.setHeightInPoints(18);
			
			// ====== Estilos ======
			CellStyle styleTitulo               = objLibro.createCellStyle();
			Font headerFont                     = objLibro.createFont();
			headerFont.setFontHeightInPoints((short)10);
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerFont.setFontName("Calibri");
			headerFont.setBold(true);
			styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
			styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			styleTitulo.setFont(headerFont);
			styleTitulo.setAlignment(HorizontalAlignment.CENTER);
			
			CellStyle styleSubTitulo            = objLibro.createCellStyle();
			Font fontSub                        = objLibro.createFont();
			fontSub.setFontHeightInPoints((short)10);
			fontSub.setFontName("Calibri");
			fontSub.setBold(true);
			styleSubTitulo.setFont(fontSub);
			styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
			
			CellStyle styleDatosNomina          = estiloColumnaDatosNomina(objLibro);
			CellStyle styleDatosEmision         = estiloColumnaDatosEmision(objLibro);
			CellStyle styleDatosTrabajador      = estiloColumnaDatosTrabajador(objLibro);
			CellStyle styleSeparacion           = estiloColumnaSeparacion(objLibro);
			CellStyle stylePercepciones         = estiloColumnaPercepciones(objLibro);
			CellStyle stylePercepcionesExcento  = estiloColumnaPercepcionesExcento(objLibro);
			CellStyle styleDeducciones          = estiloColumnaDeducciones(objLibro);
			CellStyle styleOtroPagos            = estiloColumnaOtroPagos(objLibro);
			CellStyle styleDetalle              = estiloColumnaDetalle(objLibro);
			
			// ====== Título y subtítulo (texto, merge después) ======
			Cell monthCell = header.createCell(0);
			monthCell.setCellValue("Sistema de Recepcion de XML");
			
			header = hoja1.createRow(1);
			header.setHeightInPoints(18);
			Cell monthCell2 = header.createCell(0);
			monthCell2.setCellValue("Detalle Nomina XML");
			monthCell2.setCellStyle(styleSubTitulo);
			
			// ====== Fila 2 (grupos) y 3 (subtítulos) ======
			header2 = hoja1.createRow(2);
			header2.setHeightInPoints(30);
			header3 = hoja1.createRow(3);
			
			// Catálogos
			CatalogosBean catalogosBean = new CatalogosBean();
			HashMap<String, CatalogosForm> mapaTipoNomina       = catalogosBean.detalleTipoNomina(conSAT, "");
			HashMap<String, CatalogosForm> mapaTipoDeducciones  = catalogosBean.detalleTipoDeducciones(conSAT, "");
			HashMap<String, CatalogosForm> mapaTipoOtroPagos    = catalogosBean.detalleOtroPagos(conSAT, "");
			
			// ====== Secciones fijas (0..44) ======
			final int FIXED_LAST_COL = 44;
			int lastCol = FIXED_LAST_COL;
			
			Cell monthCell3 = header2.createCell(0);
			setMerge(hoja1, 2, 3, 0, 10, false);
			monthCell3.setCellValue("Datos de Nómina");
			monthCell3.setCellStyle(styleDatosNomina);
			
			monthCell3 = header2.createCell(11);
			setMerge(hoja1, 2, 3, 11, 21, false);
			monthCell3.setCellValue("Datos de Emision");
			monthCell3.setCellStyle(styleDatosEmision);
			
			monthCell3 = header2.createCell(22);
			setMerge(hoja1, 2, 3, 22, 39, false);
			monthCell3.setCellValue("Datos de Trabajador");
			monthCell3.setCellStyle(styleDatosTrabajador);
			
			monthCell3 = header2.createCell(40);
			setMerge(hoja1, 2, 3, 40, 44, false);
			monthCell3.setCellValue("Separación Indemnizacion");
			monthCell3.setCellStyle(styleSeparacion);
			
			// ====== Secciones dinámicas ======
			int perceCols = (listaPercepciones != null) ? (listaPercepciones.size() * 2) : 0;
			int deducCols = (listaDeducciones != null) ? listaDeducciones.size() : 0;
			int otrosCols = (listaOtroPagos != null) ? listaOtroPagos.size() : 0;
			
			int perceStart = lastCol + 1;              // 45
			int perceEnd   = perceStart + perceCols - 1;
			
			int deducStart = (perceCols > 0) ? (perceEnd + 1) : (lastCol + 1);
			int deducEnd   = deducStart + deducCols - 1;
			
			int otrosStart = (deducCols > 0) ? (deducEnd + 1)
			                           : ((perceCols > 0) ? (perceEnd + 1) : (lastCol + 1));
			int otrosEnd   = otrosStart + otrosCols - 1;
			
			if (perceCols > 0) {
			monthCell3 = header2.createCell(perceStart);
			setMerge(hoja1, 2, 2, perceStart, perceEnd, false);
			monthCell3.setCellValue("PERCEPCIONES");
			monthCell3.setCellStyle(stylePercepciones);
			lastCol = perceEnd;
			}
			
			if (deducCols > 0) {
			monthCell3 = header2.createCell(deducStart);
			setMerge(hoja1, 2, 3, deducStart, deducEnd, true);
			monthCell3.setCellValue("DEDUCCIONES");
			monthCell3.setCellStyle(styleDeducciones);
			lastCol = deducEnd;
			}
			
			if (otrosCols > 0) {
			monthCell3 = header2.createCell(otrosStart);
			setMerge(hoja1, 2, 3, otrosStart, otrosEnd, true);
			monthCell3.setCellValue("OTROS PAGOS");
			monthCell3.setCellStyle(styleOtroPagos);
			lastCol = otrosEnd;
			}
			
			// Merge de títulos ahora que sabemos lastCol
			setMerge(hoja1, 0, 0, 0, lastCol, true);
			setMerge(hoja1, 1, 1, 0, lastCol, true);
			hoja1.getRow(0).getCell(0).setCellStyle(styleTitulo);
			hoja1.getRow(1).getCell(0).setCellStyle(styleSubTitulo);
			
			// ====== Fila 4: encabezados de columnas ======
			Row header4 = hoja1.createRow(4);
			header4.setHeightInPoints(50);
			
			String columnNamesNomina[][] = nombreColumnasNomina();
			int numCol = 0;
			for (int i = 0; i < columnNamesNomina.length; i++) {
			String[] datoColumna = columnNamesNomina[i];
			Cell c = header4.createCell(numCol);
			c.setCellValue(datoColumna[0]);
			c.setCellStyle(styleDatosNomina);
			hoja1.setColumnWidth(numCol, Utils.noNuloINT(datoColumna[1]));
			numCol++;
			}
			
			String columnNamesEmision[][] = nombreColumnasEmision();
			for (int i = 0; i < columnNamesEmision.length; i++) {
			String[] datoColumna = columnNamesEmision[i];
			Cell c = header4.createCell(numCol);
			c.setCellValue(datoColumna[0]);
			c.setCellStyle(styleDatosEmision);
			hoja1.setColumnWidth(numCol, Utils.noNuloINT(datoColumna[1]));
			numCol++;
			}
			
			String columnNamesTrabajador[][] = nombreColumnasTrabajador();
			for (int i = 0; i < columnNamesTrabajador.length; i++) {
			String[] datoColumna = columnNamesTrabajador[i];
			Cell c = header4.createCell(numCol);
			c.setCellValue(datoColumna[0]);
			c.setCellStyle(styleDatosTrabajador);
			hoja1.setColumnWidth(numCol, Utils.noNuloINT(datoColumna[1]));
			numCol++;
			}
			
			String columnNamesSeparacion[][] = nombreColumnasSeparacion();
			for (int i = 0; i < columnNamesSeparacion.length; i++) {
			String[] datoColumna = columnNamesSeparacion[i];
			Cell c = header4.createCell(numCol);
			c.setCellValue(datoColumna[0]);
			c.setCellStyle(styleSeparacion);
			hoja1.setColumnWidth(numCol, Utils.noNuloINT(datoColumna[1]));
			numCol++;
			}
			
			// Sub-encabezados percepciones (fila 3) + título por código (fila 4)
			if (perceCols > 0) {
			boolean bandGravado = true;
			int rowLista = 0;
			for (int x = perceStart; x <= perceEnd; x++) {
			  CatalogosForm tipoNominaForm = mapaTipoNomina.get(listaPercepciones.get(rowLista));
			  if (tipoNominaForm == null) {
			      tipoNominaForm = new CatalogosForm();
			      tipoNominaForm.setDescripcion("No existe en Catalogo");
			  }
			  Cell sub = header3.createCell(x);
			  if (bandGravado) {
			      sub.setCellValue("Gravado");
			      sub.setCellStyle(stylePercepciones);
			      bandGravado = false;
			
			      Cell top = header4.createCell(x);
			      top.setCellValue("(" + listaPercepciones.get(rowLista) + " -> " + tipoNominaForm.getDescripcion() + ")");
			      top.setCellStyle(stylePercepciones);
			      hoja1.setColumnWidth(x, 4000);
			  } else {
			      sub.setCellValue("Excento");
			      sub.setCellStyle(stylePercepcionesExcento);
			      bandGravado = true;
			
			      Cell top = header4.createCell(x);
			      top.setCellValue("(" + listaPercepciones.get(rowLista) + " -> " + tipoNominaForm.getDescripcion() + ")");
			      top.setCellStyle(stylePercepcionesExcento);
			      hoja1.setColumnWidth(x, 4000);
			
			      rowLista++;
			  }
			}
			}
			
			// Encabezados deducciones (fila 4)
			if (deducCols > 0) {
			int col = deducStart;
			for (int i = 0; i < listaDeducciones.size(); i++, col++) {
			  CatalogosForm tipoDed = mapaTipoDeducciones.get(listaDeducciones.get(i));
			  if (tipoDed == null) {
			      tipoDed = new CatalogosForm();
			      tipoDed.setDescripcion("No existe en Catalogo");
			  }
			  Cell c = header4.createCell(col);
			  c.setCellValue("(" + listaDeducciones.get(i) + "-> " + tipoDed.getDescripcion() + ")");
			  c.setCellStyle(styleDeducciones);
			  hoja1.setColumnWidth(col, 4000);
			}
			}
			
			// Encabezados otros pagos (fila 4)
			if (otrosCols > 0) {
			int col = otrosStart;
			for (int i = 0; i < listaOtroPagos.size(); i++, col++) {
			  CatalogosForm tipoOP = mapaTipoOtroPagos.get(listaOtroPagos.get(i));
			  if (tipoOP == null) {
			      tipoOP = new CatalogosForm();
			      tipoOP.setDescripcion("");
			  }
			  Cell c = header4.createCell(col);
			  c.setCellValue("(" + listaOtroPagos.get(i) + "-> " + tipoOP.getDescripcion() + ")");
			  c.setCellStyle(styleOtroPagos);
			  hoja1.setColumnWidth(col, 4000);
			}
			}
			
			// ====== Datos ======
			int numRowDatos = 5;
			for (int x = regInicial; x < regFinal; x++) {
			ReporteNominaForm repForm = datosBoveda.get(x);
			Row fila = hoja1.createRow(numRowDatos++);
			
			if (x % 100 == 0 && x != 0) {
			  ((SXSSFSheet) hoja1).flushRows(100);
			}
			
			int col = 0;
			Cell celda;
			
			// Nómina
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTipoNomina());               celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaPago());                celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaInicialPago());         celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaFinalPago());           celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getNumDiasPagados());           celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getSubsidioCausadoDouble());    celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTotalPercepcionesDouble());  celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTotalDeduccionesDouble());   celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTotalOtroPagosDouble());     celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getOrigenRecurso());            celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTotalDouble());              celda.setCellStyle(styleDetalle);
			
			// Emisión
			celda = fila.createCell(col++); celda.setCellValue(repForm.getUuid());                     celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getSerie());                    celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFolio());                    celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaFactura().substring(0, 4)); celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaFactura().substring(5, 7)); celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaFactura().substring(8, 10));celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaFactura());             celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaTimbrado());            celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getEmisorRFC());                celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getEmisorNombre());             celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getRegistroPatronal());         celda.setCellStyle(styleDetalle);
			
			// Trabajador
			celda = fila.createCell(col++); celda.setCellValue(repForm.getReceptorRFC());              celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getReceptorNombre());           celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getCurp());                     celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getNumeroSeguroSocial());       celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getFechaInicioLaboral());       celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getAntiguedad());               celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTipoContrato());             celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTipoJornada());              celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTipoRegimen());              celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getNumEmpleado());              celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getRiesgoPuesto());             celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getDepartamento());             celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getPuesto());                   celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getSindicalizado());            celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getPeriodicidadPago());         celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getSalarioBaseCotAporDouble()); celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getSalarioDiarioIntegradoDouble()); celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getClaveEntFed());              celda.setCellStyle(styleDetalle);
			
			// Separación
			celda = fila.createCell(col++); celda.setCellValue(repForm.getIngresoAcumulableDouble());  celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getIngresoNoAcumulableDouble());celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getUltimoSueldoMensualDouble());celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getAniosServicio());            celda.setCellStyle(styleDetalle);
			celda = fila.createCell(col++); celda.setCellValue(repForm.getTotalPagadoDouble());        celda.setCellStyle(styleDetalle);
			
			// Percepciones
			if (perceCols > 0) {
			  for (String tipoPercepcion : listaPercepciones) {
			      pintarPercepcion(fila, styleDetalle, col, tipoPercepcion, repForm.getPercepciones());
			      col += 2;
			  }
			}
			// Deducciones
			if (deducCols > 0) {
			  for (String tipoDeduccion : listaDeducciones) {
			      pintarDeduccion(fila, styleDetalle, col, tipoDeduccion, repForm.getDeducciones());
			      col++;
			  }
			}
			// Otros pagos
			if (otrosCols > 0) {
			  for (String tipoOP : listaOtroPagos) {
			      pintarOtroPagos(fila, styleDetalle, col, tipoOP, repForm.getOtrosPagos());
			      col++;
			  }
			}
			}
			
			} catch (Exception e) {
			Utils.imprimeLog("", e);
			}
			}

	
	private void pintarPercepcion(Row fila, CellStyle styleDetalle, int numCol, String tipoPercepcion, PercepcionesForm percepciones) {
		try {
			
			Cell celdaGravado = fila.createCell(numCol++);
			celdaGravado.setCellStyle(styleDetalle);
			
			Cell celdaExcento = fila.createCell(numCol++);
			celdaExcento.setCellStyle(styleDetalle);

			
			if ("001".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado001());
				celdaExcento.setCellValue(percepciones.getPercepcionExento001());
			}
			if ("002".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado002());
				celdaExcento.setCellValue(percepciones.getPercepcionExento002());
			}
			if ("003".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado003());
				celdaExcento.setCellValue(percepciones.getPercepcionExento003());
			}
			if ("004".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado004());
				celdaExcento.setCellValue(percepciones.getPercepcionExento004());
			}
			if ("005".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado005());
				celdaExcento.setCellValue(percepciones.getPercepcionExento005());
			}
			if ("006".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado006());
				celdaExcento.setCellValue(percepciones.getPercepcionExento006());
			}
			if ("007".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado007());
				celdaExcento.setCellValue(percepciones.getPercepcionExento007());
			}
			if ("008".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado008());
				celdaExcento.setCellValue(percepciones.getPercepcionExento008());
			}
			if ("009".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado009());
				celdaExcento.setCellValue(percepciones.getPercepcionExento009());
			}
			if ("010".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado010());
				celdaExcento.setCellValue(percepciones.getPercepcionExento010());
			}
			
			if ("011".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado011());
				celdaExcento.setCellValue(percepciones.getPercepcionExento011());
			}
			if ("012".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado012());
				celdaExcento.setCellValue(percepciones.getPercepcionExento012());
			}
			if ("013".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado013());
				celdaExcento.setCellValue(percepciones.getPercepcionExento013());
			}
			if ("014".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado014());
				celdaExcento.setCellValue(percepciones.getPercepcionExento014());
			}
			if ("015".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado015());
				celdaExcento.setCellValue(percepciones.getPercepcionExento015());
			}
			if ("016".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado016());
				celdaExcento.setCellValue(percepciones.getPercepcionExento016());
			}
			if ("017".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado017());
				celdaExcento.setCellValue(percepciones.getPercepcionExento017());
			}
			if ("018".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado018());
				celdaExcento.setCellValue(percepciones.getPercepcionExento018());
			}
			if ("019".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado019());
				celdaExcento.setCellValue(percepciones.getPercepcionExento019());
			}
			if ("020".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado020());
				celdaExcento.setCellValue(percepciones.getPercepcionExento020());
			}

			if ("021".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado021());
				celdaExcento.setCellValue(percepciones.getPercepcionExento021());
			}
			if ("022".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado022());
				celdaExcento.setCellValue(percepciones.getPercepcionExento022());
			}
			if ("023".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado023());
				celdaExcento.setCellValue(percepciones.getPercepcionExento023());
			}
			if ("024".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado024());
				celdaExcento.setCellValue(percepciones.getPercepcionExento024());
			}
			if ("025".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado025());
				celdaExcento.setCellValue(percepciones.getPercepcionExento025());
			}
			if ("026".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado026());
				celdaExcento.setCellValue(percepciones.getPercepcionExento026());
			}
			if ("027".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado027());
				celdaExcento.setCellValue(percepciones.getPercepcionExento027());
			}
			if ("028".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado028());
				celdaExcento.setCellValue(percepciones.getPercepcionExento028());
			}
			if ("029".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado029());
				celdaExcento.setCellValue(percepciones.getPercepcionExento029());
			}
			if ("030".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado030());
				celdaExcento.setCellValue(percepciones.getPercepcionExento030());
			}
			if ("031".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado031());
				celdaExcento.setCellValue(percepciones.getPercepcionExento031());
			}
			if ("032".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado032());
				celdaExcento.setCellValue(percepciones.getPercepcionExento032());
			}
			if ("033".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado033());
				celdaExcento.setCellValue(percepciones.getPercepcionExento033());
			}
			if ("034".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado034());
				celdaExcento.setCellValue(percepciones.getPercepcionExento034());
			}
			if ("035".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado035());
				celdaExcento.setCellValue(percepciones.getPercepcionExento035());
			}
			if ("036".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado036());
				celdaExcento.setCellValue(percepciones.getPercepcionExento036());
			}
			if ("037".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado037());
				celdaExcento.setCellValue(percepciones.getPercepcionExento037());
			}
			if ("038".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado038());
				celdaExcento.setCellValue(percepciones.getPercepcionExento038());
			}
			if ("039".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado039());
				celdaExcento.setCellValue(percepciones.getPercepcionExento039());
			}
			if ("040".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado040());
				celdaExcento.setCellValue(percepciones.getPercepcionExento040());
			}
			if ("041".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado041());
				celdaExcento.setCellValue(percepciones.getPercepcionExento041());
			}
			if ("042".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado042());
				celdaExcento.setCellValue(percepciones.getPercepcionExento042());
			}
			if ("043".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado043());
				celdaExcento.setCellValue(percepciones.getPercepcionExento043());
			}
			if ("044".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado044());
				celdaExcento.setCellValue(percepciones.getPercepcionExento044());
			}
			if ("045".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado045());
				celdaExcento.setCellValue(percepciones.getPercepcionExento045());
			}
			if ("046".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado046());
				celdaExcento.setCellValue(percepciones.getPercepcionExento046());
			}
			if ("047".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado047());
				celdaExcento.setCellValue(percepciones.getPercepcionExento047());
			}
			if ("048".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado048());
				celdaExcento.setCellValue(percepciones.getPercepcionExento048());
			}
			if ("049".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado049());
				celdaExcento.setCellValue(percepciones.getPercepcionExento049());
			}
			if ("050".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado050());
				celdaExcento.setCellValue(percepciones.getPercepcionExento050());
			}
			if ("051".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado051());
				celdaExcento.setCellValue(percepciones.getPercepcionExento051());
			}
			if ("052".equalsIgnoreCase(tipoPercepcion)) {
				celdaGravado.setCellValue(percepciones.getPercepcionGravado052());
				celdaExcento.setCellValue(percepciones.getPercepcionExento052());
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	private void pintarDeduccion(Row fila, CellStyle styleDetalle, int numCol, String tipoDeduccion, DeduccionesForm deducciones) {
		try {
			
			Cell celdaDeduccion = fila.createCell(numCol++);
			celdaDeduccion.setCellStyle(styleDetalle);
			
			if ("001".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones001());
			}
			if ("002".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones002());
			}
			if ("003".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones003());
			}
			if ("004".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones004());
			}
			if ("005".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones005());
			}
			if ("006".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones006());
			}
			if ("007".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones007());
			}
			if ("008".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones008());
			}
			if ("009".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones009());
			}
			if ("010".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones010());
			}
			
			if ("011".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones011());
			}
			if ("012".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones012());
			}
			if ("013".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones013());
			}
			if ("014".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones014());
			}
			if ("015".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones015());
			}
			if ("016".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones016());
			}
			if ("017".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones017());
			}
			if ("018".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones018());
			}
			if ("019".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones019());
			}
			if ("020".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones020());
			}
			
			if ("021".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones021());
			}
			if ("022".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones022());
			}
			if ("023".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones023());
			}
			if ("024".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones024());
			}
			if ("025".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones025());
			}
			if ("026".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones026());
			}
			if ("027".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones027());
			}
			if ("028".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones028());
			}
			if ("029".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones029());
			}
			if ("030".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones030());
			}
			
			if ("031".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones031());
			}
			if ("032".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones032());
			}
			if ("033".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones033());
			}
			if ("034".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones034());
			}
			if ("035".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones035());
			}
			if ("036".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones036());
			}
			if ("037".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones037());
			}
			if ("038".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones038());
			}
			if ("039".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones039());
			}
			if ("040".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones040());
			}
			
			if ("041".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones041());
			}
			if ("042".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones042());
			}
			if ("043".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones043());
			}
			if ("044".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones044());
			}
			if ("045".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones045());
			}
			if ("046".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones046());
			}
			if ("047".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones047());
			}
			if ("048".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones048());
			}
			if ("049".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones049());
			}
			if ("050".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones050());
			}
			
			if ("051".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones051());
			}
			if ("052".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones052());
			}
			if ("053".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones053());
			}
			if ("054".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones054());
			}
			if ("055".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones055());
			}
			if ("056".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones056());
			}
			if ("057".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones057());
			}
			if ("058".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones058());
			}
			if ("059".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones059());
			}
			if ("060".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones060());
			}
			
			if ("061".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones061());
			}
			if ("062".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones062());
			}
			if ("063".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones063());
			}
			if ("064".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones064());
			}
			if ("065".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones065());
			}
			if ("066".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones066());
			}
			if ("067".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones067());
			}
			if ("068".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones068());
			}
			if ("069".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones069());
			}
			if ("070".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones070());
			}
			
			if ("071".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones071());
			}
			if ("072".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones072());
			}
			if ("073".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones073());
			}
			if ("074".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones074());
			}
			if ("075".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones075());
			}
			if ("076".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones076());
			}
			if ("077".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones077());
			}
			if ("078".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones078());
			}
			if ("079".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones079());
			}
			if ("080".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones080());
			}
			
			if ("081".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones081());
			}
			if ("082".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones082());
			}
			if ("083".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones083());
			}
			if ("084".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones084());
			}
			if ("085".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones085());
			}
			if ("086".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones086());
			}
			if ("087".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones087());
			}
			if ("088".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones088());
			}
			if ("089".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones089());
			}
			if ("090".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones090());
			}
			
			if ("091".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones091());
			}
			if ("092".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones092());
			}
			if ("093".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones093());
			}
			if ("094".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones094());
			}
			if ("095".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones095());
			}
			if ("096".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones096());
			}
			if ("097".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones097());
			}
			if ("098".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones098());
			}
			if ("099".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones099());
			}
			if ("100".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones100());
			}
			if ("101".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones101());
			}
			if ("102".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones102());
			}
			if ("103".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones103());
			}
			if ("104".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones104());
			}
			if ("105".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones105());
			}
			if ("106".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones106());
			}
			if ("107".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones107());
			}
			if ("108".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones108());
			}
			if ("109".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones109());
			}
			if ("110".equalsIgnoreCase(tipoDeduccion)) {
				celdaDeduccion.setCellValue(deducciones.getDeducciones110());
			}

			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
			
	private void pintarOtroPagos(Row fila, CellStyle styleDetalle, int numCol, String tipoOtroPagos, OtroPagosForm otroPagos) {
		try {
			
			Cell celdaOtroPago = fila.createCell(numCol++);
			celdaOtroPago.setCellStyle(styleDetalle);
			
			if ("001".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago001());
			}
			if ("002".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago002());
			}
			if ("003".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago003());
			}
			if ("004".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago004());
			}
			if ("005".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago005());
			}
			if ("006".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago006());
			}
			if ("007".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago007());
			}
			if ("008".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago008());
			}
			if ("009".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago009());
			}
			if ("010".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago010());
			}
			if ("011".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago011());
			}
			if ("012".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago012());
			}
			if ("013".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago013());
			}
			if ("014".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago014());
			}
			if ("015".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago015());
			}
			if ("016".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago016());
			}
			if ("017".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago017());
			}
			if ("018".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago018());
			}
			if ("019".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago019());
			}
			if ("020".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago020());
			}
			if ("999".equalsIgnoreCase(tipoOtroPagos)) {
				celdaOtroPago.setCellValue(otroPagos.getOtroPago999());
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		
	}	
		
	private String[] [] nombreColumnasNomina() {
		String columnNames [] [] = { {"Tipo de Nómina", "4500"},
									 {"Fecha de Pago", "4500"},
									 {"Fecha Inicial de Pago", "5000"},
									 {"Fecha Final de Pago", "5000"},
									 {"Dias Pagados", "5000"},
									 {"Subsidio Causado", "5000"},
									 {"Total de Percepciones", "5000"},
									 {"Total de Deducciones", "5000"},
									 {"Total de Otros Pagos", "5000"},
									 {"Origen del Recurso", "5000"},
									 {"Total CFDI", "5000"}
							   };
			
		return columnNames;
	}
	
	
	private String[] [] nombreColumnasEmision() {
		String columnNames [] [] = { {"UUID", "10000"},
									 {"Serie", "4500"},
									 {"Folio", "4500"},
									 {"Año", "3000"},
									 {"Mes", "3000"},
									 {"Dia", "3000"},
									 {"Fecha de Emisión", "5000"},
									 {"Fecha de Timbrado", "5000"},
									 {"RFC Emisor", "5000"},
									 {"Nombre Emisor", "10000"},
									 {"Registro Patronal", "7000"}
							   };
			
		return columnNames;
	}
	
	private String[] [] nombreColumnasTrabajador() {
		String columnNames [] [] = { {"RFC Trabajador", "5000"},
									 {"Nombre del Trabajador", "10000"},
									 {"CURP", "7000"},
									 {"NumSeguridadSocial", "7000"},
									 {"FechaInicioRelLaboral", "5000"},
									 {"Antigüedad", "5000"},
									 {"TipoContrato", "5000"},
									 {"TipoJornada", "5000"},
									 {"TipoRegimen", "5000"},
									 {"NumEmpleado", "5000"},
									 {"RiesgoPuesto", "5000"},
									 {"Departamento", "8000"},
									 {"Puesto", "7000"},
									 {"Sindicalizado", "5000"},
									 {"PeriodicidadPago", "5000"},
									 {"SalarioBaseCotApor", "5000"},
									 {"SalarioDiarioIntegrado", "5000"},
									 {"ClaveEntFed", "5000"}
							   };
			
		return columnNames;
	}
	
	private String[] [] nombreColumnasSeparacion() {
		String columnNames [] [] = { {"Ingreso Acumulable", "5000"},
									 {"Ingreso No Acumulable", "5000"},
									 {"Ultimo Sueldo Mensual Ordinario", "5000"},
									 {"Num Años Servicio", "5000"},
									 {"Total Pagado", "5000"}
							   };
			
		return columnNames;
	}
	
	private CellStyle estiloColumnaDatosNomina(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);// new java.awt.Color(252, 228, 214)
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(252, 228, 214)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			   
			   
			      
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		        
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    
			   
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	
	private CellStyle estiloColumnaDatosEmision(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   //style.setFillForegroundColor(new XSSFColor(new java.awt.Color(221, 235, 247)));
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(221, 235, 247)));
			   
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    
			   
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	
	private CellStyle estiloColumnaDatosTrabajador(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   //style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 242, 204)));
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(255, 242, 204)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	
	private CellStyle estiloColumnaSeparacion(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
				style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   //style.setFillForegroundColor(new XSSFColor(new java.awt.Color(196, 214, 237)));
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(196, 214, 237)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			   style.setWrapText(true);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			   
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	private CellStyle estiloColumnaPercepciones(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   //style.setFillForegroundColor(new XSSFColor(new java.awt.Color(169, 208, 142)));
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(169, 208, 142)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			   style.setWrapText(true);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	private CellStyle estiloColumnaPercepcionesExcento(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
				style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   //style.setFillForegroundColor(new XSSFColor(new java.awt.Color(226, 239, 218)));
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(226, 239, 218)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			   style.setWrapText(true);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	private CellStyle estiloColumnaDeducciones(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   //style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 230, 153)));
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(255, 230, 153)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			   style.setWrapText(true);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			   
			    
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	private CellStyle estiloColumnaOtroPagos(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);// new java.awt.Color(217, 225, 242)
			   style.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(217, 255, 242)));
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       style.setFont(fontStyle);
			   style.setAlignment(HorizontalAlignment.CENTER);
			   style.setVerticalAlignment(VerticalAlignment.CENTER);
			   style.setWrapText(true);
			    
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
		       
			   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	private CellStyle estiloColumnaDetalle(SXSSFWorkbook objLibro) {
		CellStyle style = null;
		try {
			   style = objLibro.createCellStyle();
			   Font fontStyle = objLibro.createFont();
			   fontStyle.setFontHeightInPoints((short)10);
			   fontStyle.setFontName("Calibri");
			   fontStyle.setBold(false);
			   style.setFont(fontStyle);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return style;
	}
	
	protected void setMerge(SXSSFSheet sheet, int numRow, int untilRow, int numCol, int untilCol, boolean border) {
	    // Evita "Invalid cell range" de Apache POI
	    if (sheet == null) return;
	    if (numRow > untilRow || numCol > untilCol) return;
	    if (numRow < 0 || untilRow < 0 || numCol < 0 || untilCol < 0) return;

	    CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
	    sheet.addMergedRegion(cellMerge);
	    if (border) {
	        setBordersToMergedCells(sheet, cellMerge);
	    }
	}
 

	protected void setBordersToMergedCells(SXSSFSheet sheet, CellRangeAddress rangeAddress) {
		RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
	    RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
	    RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
	}	
}
