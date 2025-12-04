/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.itextpdf.xmltopdf.Nomina.NAccionesOTitulos;
import com.itextpdf.xmltopdf.Nomina.NCompensacionSaldosAFavor;
import com.itextpdf.xmltopdf.Nomina.NDeduccion;
import com.itextpdf.xmltopdf.Nomina.NDeducciones;
import com.itextpdf.xmltopdf.Nomina.NEmisor;
import com.itextpdf.xmltopdf.Nomina.NEntidadSNCF;
import com.itextpdf.xmltopdf.Nomina.NHorasExtra;
import com.itextpdf.xmltopdf.Nomina.NIncapacidad;
import com.itextpdf.xmltopdf.Nomina.NIncapacidades;
import com.itextpdf.xmltopdf.Nomina.NJubilacionPensionRetiro;
import com.itextpdf.xmltopdf.Nomina.NOtroPago;
import com.itextpdf.xmltopdf.Nomina.NOtrosPagos;
import com.itextpdf.xmltopdf.Nomina.NPercepcion;
import com.itextpdf.xmltopdf.Nomina.NPercepciones;
import com.itextpdf.xmltopdf.Nomina.NReceptor;
import com.itextpdf.xmltopdf.Nomina.NSeparacionIndemnizacion;
import com.itextpdf.xmltopdf.Nomina.NSubContratacion;
import com.itextpdf.xmltopdf.Nomina.NSubsidioAlEmpleo;
import com.itextpdf.xmltopdf.Nomina.Nomina;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos.PImpuestos;
import com.itextpdf.xmltopdf.Pagos.PRetencion;
import com.itextpdf.xmltopdf.Pagos.PRetenciones;
import com.itextpdf.xmltopdf.Pagos.PTraslado;
import com.itextpdf.xmltopdf.Pagos.PTraslados;
import com.itextpdf.xmltopdf.Pagos.Pago;
import com.itextpdf.xmltopdf.Pagos.Pagos;
import com.siarex247.utils.Utils;

/**
 *
 * @author Faustino
 */
public final class LeerXML {

	public static final Logger logger = Logger.getLogger("siarex247");
	
    public static Comprobante ObtenerComprobante(String rutaXML) {
        try {
        	File file = new File(rutaXML);
            // an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(file);
            documento.getDocumentElement().normalize();

            if (!esXmlValido(documento)) {
                return null;
            }
            
            Comprobante comprobante = ObtenerNodoComprobante(documento);
            comprobante.setCfdiRelacionados(ObtenerNodoCfdisRelacionados(documento));
            comprobante.setInformacionGlobal(ObtenerInformacionGlobal(documento));
            comprobante.setEmisor(ObtenerNodoEmisor(documento));
            comprobante.setReceptor(ObtenerNodoReceptor(documento));
            comprobante.setConceptos(ObtenerNodoConceptos(documento));
            comprobante.setImpuestos(ObtenerNodoImpuestos(documento));
            comprobante.setComplemento(ObtenerNodoComplementos(documento));
            comprobante.setAddenda((ObtenerNodoAdenda(documento)));
            comprobante.AcuseCancelacion = ObtenerAcuseCancelacion(documento);
            // comprobante.XML = documento.InnerXml; ;
            return comprobante;
        } catch (Exception e) {
            Utils.imprimeLog("", e);
            return null;
        }
    }

    private static Boolean esXmlValido(Document documento) {
        try {
            if (documento.getElementsByTagName("Acuse").getLength() > 0) {
                return true;
            } else if (documento.getElementsByTagName("cfdi:Comprobante").getLength() > 0) {
                NodeList lComprobante = documento.getElementsByTagName("cfdi:Comprobante");
                if (lComprobante.getLength() > 0) {
                    Element nComprobante = (Element) lComprobante.item(0);
                    String version = "";
                    if (nComprobante.hasAttribute("Version")) {
                        version = nComprobante.getAttribute("Version");
                    }
                    // Para la version 3.2
                    if (nComprobante.hasAttribute("version")) {
                        version = nComprobante.getAttribute("version");
                    }
                    if (version.equals("4.0") || version.equals("3.3") || version.equals("3.2")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static Comprobante ObtenerNodoComprobante(Document documento) {
        Comprobante comprobante = new Comprobante();
        NodeList lComprobante = documento.getElementsByTagName("cfdi:Comprobante");
        if (lComprobante.getLength() == 0) {
            return new Comprobante();
        }
        Element nComprobante = (Element) lComprobante.item(0);
        if (nComprobante.hasAttribute("Version")) {
            comprobante.setVersion(nComprobante.getAttribute("Version"));
        }
        if (nComprobante.hasAttribute("Serie")) {
            comprobante.setSerie(nComprobante.getAttribute("Serie"));
        }
        if (nComprobante.hasAttribute("Folio")) {
            comprobante.setFolio(nComprobante.getAttribute("Folio"));
        }
        if (nComprobante.hasAttribute("Fecha")) {
            comprobante.setFecha(LocalDateTime.parse(nComprobante.getAttribute("Fecha")));
        }
        if (nComprobante.hasAttribute("Sello")) {
            comprobante.setSello(nComprobante.getAttribute("Sello"));
        }
        if (nComprobante.hasAttribute("FormaPago")) {
            comprobante.setFormaPago(nComprobante.getAttribute("FormaPago"));
        }
        if (nComprobante.hasAttribute("NoCertificado")) {
            comprobante.setNoCertificado(nComprobante.getAttribute("NoCertificado"));
        }
        if (nComprobante.hasAttribute("Certificado")) {
            comprobante.setCertificado(nComprobante.getAttribute("Certificado"));
        }
        if (nComprobante.hasAttribute("CondicionesDePago")) {
            comprobante.setCondicionesDePago(nComprobante.getAttribute("CondicionesDePago"));
        }
        if (nComprobante.hasAttribute("SubTotal")) {
            comprobante.setSubTotal(Double.parseDouble(nComprobante.getAttribute("SubTotal")));
        }
        if (nComprobante.hasAttribute("Descuento")) {
            comprobante.setDescuento(Double.parseDouble(nComprobante.getAttribute("Descuento")));
        }
        if (nComprobante.hasAttribute("Moneda")) {
            comprobante.setMoneda(nComprobante.getAttribute("Moneda"));
        }
        if (nComprobante.hasAttribute("TipoCambio")) {
            comprobante.setTipoCambio(Double.parseDouble(nComprobante.getAttribute("TipoCambio")));
        }
        if (nComprobante.hasAttribute("Total")) {
            comprobante.setTotal(Double.parseDouble(nComprobante.getAttribute("Total")));
        }
        if (nComprobante.hasAttribute("Descuento")) {
            comprobante.setDescuento(Double.parseDouble(nComprobante.getAttribute("Descuento")));
        }
        if (nComprobante.hasAttribute("TipoDeComprobante")) {
            comprobante.setTipoDeComprobante(nComprobante.getAttribute("TipoDeComprobante"));
        }
        if (nComprobante.hasAttribute("Exportacion")) {
            comprobante.setExportacion(nComprobante.getAttribute("Exportacion"));
        }
        if (nComprobante.hasAttribute("MetodoPago")) {
            comprobante.setMetodoPago(nComprobante.getAttribute("MetodoPago"));
        }
        if (nComprobante.hasAttribute("LugarExpedicion")) {
            comprobante.setLugarExpedicion(nComprobante.getAttribute("LugarExpedicion"));
        }
        if (nComprobante.hasAttribute("Confirmacion")) {
            comprobante.setConfirmacion(nComprobante.getAttribute("Confirmacion"));
        }
        // Version 3.2
        if (nComprobante.hasAttribute("version")) {
            comprobante.setVersion(nComprobante.getAttribute("version"));
        }
        if (nComprobante.hasAttribute("serie")) {
            comprobante.setSerie(nComprobante.getAttribute("serie"));
        }
        if (nComprobante.hasAttribute("folio")) {
            comprobante.setFolio(nComprobante.getAttribute("folio"));
        }
        if (nComprobante.hasAttribute("fecha")) {
            comprobante.setFecha(LocalDateTime.parse(nComprobante.getAttribute("fecha")));
        }
        if (nComprobante.hasAttribute("sello")) {
            comprobante.setSello(nComprobante.getAttribute("sello"));
        }
        if (nComprobante.hasAttribute("formaDePago")) {
            comprobante.setFormaPago(nComprobante.getAttribute("formaDePago"));
        }
        if (nComprobante.hasAttribute("noCertificado")) {
            comprobante.setNoCertificado(nComprobante.getAttribute("NoCertificado"));
        }
        if (nComprobante.hasAttribute("certificado")) {
            comprobante.setCertificado(nComprobante.getAttribute("certificado"));
        }
        if (nComprobante.hasAttribute("condicionesDePago")) {
            comprobante.setCondicionesDePago(nComprobante.getAttribute("condicionesDePago"));
        }
        if (nComprobante.hasAttribute("subTotal")) {
            comprobante.setSubTotal(Double.parseDouble(nComprobante.getAttribute("subTotal")));
        }
        if (nComprobante.hasAttribute("descuento")) {
            comprobante.setDescuento(Double.parseDouble(nComprobante.getAttribute("descuento")));
        }
        if (nComprobante.hasAttribute("motivoDescuento")) {
            comprobante.setMotivoDescuento(nComprobante.getAttribute("motivoDescuento"));
        }
        if (nComprobante.hasAttribute("total")) {
            comprobante.setTotal(Double.parseDouble(nComprobante.getAttribute("total")));
        }
        if (nComprobante.hasAttribute("tipoDeComprobante")) {
            comprobante.setTipoDeComprobante(nComprobante.getAttribute("tipoDeComprobante"));
        }
        if (nComprobante.hasAttribute("metodoDePago")) {
            comprobante.setMetodoPago(nComprobante.getAttribute("metodoDePago"));
        }
        if (nComprobante.hasAttribute("NumCtaPago")) {
            comprobante.setNumCtaPago(nComprobante.getAttribute("NumCtaPago"));
        }
        if (nComprobante.hasAttribute("FolioFiscalOrig")) {
            comprobante.setFolioFiscalOrig(nComprobante.getAttribute("FolioFiscalOrig"));
        }
        if (nComprobante.hasAttribute("SerieFolioFiscalOrig")) {
            comprobante.setSerieFolioFiscalOrig(nComprobante.getAttribute("SerieFolioFiscalOrig"));
        }
        if (nComprobante.hasAttribute("FechaFolioFiscalOrig")) {
            comprobante.setFechaFolioFiscalOrig(LocalDateTime.parse(nComprobante.getAttribute("FechaFolioFiscalOrig")));
        }
        if (nComprobante.hasAttribute("MontoFolioFiscalOrig")) {
            comprobante.setMontoFolioFiscalOrig(Double.parseDouble(nComprobante.getAttribute("MontoFolioFiscalOrig")));
        }
        return comprobante;
    }

    private static InformacionGlobal ObtenerInformacionGlobal(Document documento) {
        NodeList lInformacionGlobal = documento.getElementsByTagName("cfdi:InformacionGlobal");
        if (lInformacionGlobal.getLength() == 0) {
            return null;
        }
        Element nInformacionGlobal = (Element) lInformacionGlobal.item(0);
        InformacionGlobal informacionGlobal = new InformacionGlobal();
        if (nInformacionGlobal.hasAttribute("Periodicidad")) {
            informacionGlobal.setPeriodicidad(nInformacionGlobal.getAttribute("Periodicidad"));
        }
        if (nInformacionGlobal.hasAttribute("Meses")) {
            informacionGlobal.setMeses(nInformacionGlobal.getAttribute("Meses"));
        }
        if (nInformacionGlobal.hasAttribute("Año")) {
            informacionGlobal.setAno(Integer.parseInt(nInformacionGlobal.getAttribute("Año")));
        }
        return informacionGlobal;
    }

    private static CfdiRelacionados ObtenerNodoCfdisRelacionados(Document documento) {
        if (documento.getElementsByTagName("cfdi:CfdiRelacionados") == null
                || documento.getElementsByTagName("cfdi:CfdiRelacionados").getLength() == 0) {
            return null;
        }
        CfdiRelacionados cfdiRelacionados = new CfdiRelacionados();
        NodeList lCfdiRelacionados = documento.getElementsByTagName("cfdi:CfdiRelacionados");
        if (((Element) lCfdiRelacionados.item(0)).getAttribute("TipoRelacion") != null) {
            cfdiRelacionados.setTipoRelacion(((Element) lCfdiRelacionados.item(0)).getAttribute("TipoRelacion"));
        }
        if (((Element) lCfdiRelacionados.item(0)).getElementsByTagName("cfdi:Relacionado") == null) {
            return cfdiRelacionados;
        }
        NodeList ListaCfdiRelacionados = ((Element) lCfdiRelacionados.item(0))
                .getElementsByTagName("cfdi:CfdiRelacionado");

        for (int i = 0; i < ListaCfdiRelacionados.getLength(); i++) {
            Element nodo = (Element) ListaCfdiRelacionados.item(i);
            CfdiRelacionado c = new CfdiRelacionado();
            if (nodo.getAttribute("UUID") != null) {
                c.setUUID(nodo.getAttribute("UUID"));
            }
            cfdiRelacionados.getCfdiRelacionado().add(c);
        }
        return cfdiRelacionados;
    }

    private static Emisor ObtenerNodoEmisor(Document documento) {
        NodeList lEmisores = documento.getElementsByTagName("cfdi:Emisor");
        if (lEmisores.getLength() == 0) {
            return null;
        }
        Element nEmisor = (Element) lEmisores.item(0);
        Emisor emisor = new Emisor();
        if (nEmisor.hasAttribute("Rfc")) {
            emisor.setRfc(nEmisor.getAttribute("Rfc"));
        }
        if (nEmisor.hasAttribute("Nombre")) {
            emisor.setNombre(nEmisor.getAttribute("Nombre"));
        }
        if (nEmisor.hasAttribute("RegimenFiscal")) {
            emisor.setRegimenFiscal(nEmisor.getAttribute("RegimenFiscal"));
        }
        if (nEmisor.hasAttribute("FacAtrAdquirente")) {
            emisor.setFacAtrAdquirente(nEmisor.getAttribute("FacAtrAdquirente"));
        }
        // Leo datos para la version 3.2
        if (nEmisor.hasAttribute("rfc")) {
            emisor.setRfc(nEmisor.getAttribute("rfc"));
        }
        if (nEmisor.hasAttribute("nombre")) {
            emisor.setNombre(nEmisor.getAttribute("nombre"));
        }
        emisor.setDomicilioFiscal(ObtenerDomicilioFiscal(nEmisor));
        emisor.setExpedidoEn(ObtenerExpedidoEn(nEmisor));
        NodeList lnEmisor = nEmisor.getElementsByTagName("cfdi:RegimenFiscal");
        if (lnEmisor.getLength() > 0) {
            Element nRegimenFiscal = (Element) lnEmisor.item(0);
            emisor.setRegimenFiscal(nRegimenFiscal.getAttribute("Regimen"));
        }
        return emisor;
    }

    private static DomicilioFiscal ObtenerDomicilioFiscal(Element nEmisor) {
        DomicilioFiscal domicilioFiscal;
        NodeList lDomicilioFiscal = nEmisor.getElementsByTagName("cfdi:DomicilioFiscal");
        if (lDomicilioFiscal.getLength() > 0) {
            domicilioFiscal = new DomicilioFiscal();
            Element nDomicilioFiscal = (Element) lDomicilioFiscal.item(0);
            if (nDomicilioFiscal.hasAttribute("calle")) {
                domicilioFiscal.setCalle(nDomicilioFiscal.getAttribute("calle"));
            }
            if (nDomicilioFiscal.hasAttribute("noExterior")) {
                domicilioFiscal.setNoExterior(nDomicilioFiscal.getAttribute("noExterior"));
            }
            if (nDomicilioFiscal.hasAttribute("noInterior")) {
                domicilioFiscal.setNoInterior(nDomicilioFiscal.getAttribute("noInterior"));
            }
            if (nDomicilioFiscal.hasAttribute("colonia")) {
                domicilioFiscal.setColonia(nDomicilioFiscal.getAttribute("colonia"));
            }
            if (nDomicilioFiscal.hasAttribute("localidad")) {
                domicilioFiscal.setLocalidad(nDomicilioFiscal.getAttribute("localidad"));
            }
            if (nDomicilioFiscal.hasAttribute("referencia")) {
                domicilioFiscal.setReferencia(nDomicilioFiscal.getAttribute("referencia"));
            }
            if (nDomicilioFiscal.hasAttribute("municipio")) {
                domicilioFiscal.setMunicipio(nDomicilioFiscal.getAttribute("municipio"));
            }
            if (nDomicilioFiscal.hasAttribute("estado")) {
                domicilioFiscal.setEstado(nDomicilioFiscal.getAttribute("estado"));
            }
            if (nDomicilioFiscal.hasAttribute("pais")) {
                domicilioFiscal.setPais(nDomicilioFiscal.getAttribute("pais"));
            }
            if (nDomicilioFiscal.hasAttribute("codigoPostal")) {
                domicilioFiscal.setCodigoPostal(nDomicilioFiscal.getAttribute("codigoPostal"));
            }
            return domicilioFiscal;
        }
        return null;
    }

    private static ExpedidoEn ObtenerExpedidoEn(Element nEmisor) {
        ExpedidoEn expedidoEn;
        NodeList lExpedidoEn = nEmisor.getElementsByTagName("cfdi:ExpedidoEn");
        if (lExpedidoEn.getLength() > 0) {
            expedidoEn = new ExpedidoEn();
            Element nExpedidoEn = (Element) lExpedidoEn.item(0);
            if (nExpedidoEn.hasAttribute("calle")) {
                expedidoEn.setCalle(nExpedidoEn.getAttribute("calle"));
            }
            if (nExpedidoEn.hasAttribute("noExterior")) {
                expedidoEn.setNoExterior(nExpedidoEn.getAttribute("noExterior"));
            }
            if (nExpedidoEn.hasAttribute("noInterior")) {
                expedidoEn.setNoInterior(nExpedidoEn.getAttribute("noInterior"));
            }
            if (nExpedidoEn.hasAttribute("colonia")) {
                expedidoEn.setColonia(nExpedidoEn.getAttribute("colonia"));
            }
            if (nExpedidoEn.hasAttribute("localidad")) {
                expedidoEn.setLocalidad(nExpedidoEn.getAttribute("localidad"));
            }
            if (nExpedidoEn.hasAttribute("referencia")) {
                expedidoEn.setReferencia(nExpedidoEn.getAttribute("referencia"));
            }
            if (nExpedidoEn.hasAttribute("municipio")) {
                expedidoEn.setMunicipio(nExpedidoEn.getAttribute("municipio"));
            }
            if (nExpedidoEn.hasAttribute("estado")) {
                expedidoEn.setEstado(nExpedidoEn.getAttribute("estado"));
            }
            if (nExpedidoEn.hasAttribute("pais")) {
                expedidoEn.setPais(nExpedidoEn.getAttribute("pais"));
            }
            if (nExpedidoEn.hasAttribute("codigoPostal")) {
                expedidoEn.setCodigoPostal(nExpedidoEn.getAttribute("codigoPostal"));
            }
            return expedidoEn;
        }
        return null;
    }

    private static Acuse ObtenerAcuseCancelacion(Document documento) {
        NodeList lCancelacion = documento.getElementsByTagName("Acuse");
        if (lCancelacion.getLength() == 0) {
            return null;
        }
        Element nCancelacion = (Element) lCancelacion.item(0);
        Acuse cancelacion = new Acuse();
        cancelacion.setRfcEmisor(nCancelacion.getAttribute("RfcEmisor"));
        cancelacion.setFecha(LocalDateTime.parse(nCancelacion.getAttribute("Fecha")));
        NodeList lFolios = nCancelacion.getElementsByTagName("Folios");
        if (lFolios.getLength() > 0) {
            Folios folios = new Folios();
            Element nFolios = (Element) lFolios.item(0);

            NodeList lUUIDS = nFolios.getElementsByTagName("UUID");
            if (lUUIDS.getLength() > 0) {
                Element nUUID = (Element) lUUIDS.item(0);
                folios.setUUID(nUUID.getTextContent());
            }

            NodeList lEstatusUUID = nFolios.getElementsByTagName("EstatusUUID");
            if (lEstatusUUID.getLength() > 0) {
                Element nEstatusUUID = (Element) lEstatusUUID.item(0);
                folios.setEstatusUUID(nEstatusUUID.getTextContent());
            }
            cancelacion.setFolios(folios);
        }
        return cancelacion;
    }

    private static Receptor ObtenerNodoReceptor(Document documento) {
        NodeList lReceptor = documento.getElementsByTagName("cfdi:Receptor");
        if (lReceptor.getLength() == 0) {
            return null;
        }
        Element nReceptor = (Element) lReceptor.item(0);
        Receptor receptor = new Receptor();
        if (nReceptor.hasAttribute("Rfc")) {
            receptor.setRfc(nReceptor.getAttribute("Rfc"));
        }
        if (nReceptor.hasAttribute("Nombre")) {
            receptor.setNombre(nReceptor.getAttribute("Nombre"));
        }
        if (nReceptor.hasAttribute("DomicilioFiscalReceptor")) {
            receptor.setDomicilioFiscalReceptor(nReceptor.getAttribute("DomicilioFiscalReceptor"));
        }
        if (nReceptor.hasAttribute("ResidenciaFiscal")) {
            receptor.setResidenciaFiscal(nReceptor.getAttribute("ResidenciaFiscal"));
        }
        if (nReceptor.hasAttribute("NumRegIdTrib")) {
            receptor.setNumRegIdTrib(nReceptor.getAttribute("NumRegIdTrib"));
        }
        if (nReceptor.hasAttribute("RegimenFiscalReceptor")) {
            receptor.setRegimenFiscalReceptor(nReceptor.getAttribute("RegimenFiscalReceptor"));
        }
        if (nReceptor.hasAttribute("UsoCFDI")) {
            receptor.setUsoCFDI(nReceptor.getAttribute("UsoCFDI"));
        }
        // Leo datos para la version 3.2
        if (nReceptor.hasAttribute("rfc")) {
            receptor.setRfc(nReceptor.getAttribute("rfc"));
        }
        if (nReceptor.hasAttribute("nombre")) {
            receptor.setNombre(nReceptor.getAttribute("nombre"));
        }
        return receptor;
    }

    private static Conceptos ObtenerNodoConceptos(Document documento) {
        NodeList lConceptos = documento.getElementsByTagName("cfdi:Conceptos");
        if (lConceptos.getLength() == 0) {
            return null;
        }
        Conceptos conceptos = new Conceptos();
        conceptos.setConcepto(new ArrayList<>());
        Element nConceptos = (Element) lConceptos.item(0);
        NodeList lConcepto = nConceptos.getElementsByTagName("cfdi:Concepto");
        for (int i = 0; i < lConcepto.getLength(); i++) {
            Element nConcepto = (Element) lConcepto.item(i);
            Concepto concepto = new Concepto();
            if (nConcepto.hasAttribute("ClaveProdServ")) {
                concepto.setClaveProdServ(nConcepto.getAttribute("ClaveProdServ"));
            }
            if (nConcepto.hasAttribute("NoIdentificacion")) {
                concepto.setNoIdentificacion(nConcepto.getAttribute("NoIdentificacion"));
            }
            if (nConcepto.hasAttribute("Cantidad")) {
                concepto.setCantidad(Float.parseFloat(nConcepto.getAttribute("Cantidad")));
            }
            if (nConcepto.hasAttribute("ClaveUnidad")) {
                concepto.setClaveUnidad(nConcepto.getAttribute("ClaveUnidad"));
            }
            if (nConcepto.hasAttribute("Unidad")) {
                concepto.setUnidad(nConcepto.getAttribute("Unidad"));
            }
            if (nConcepto.hasAttribute("Descripcion")) {
                concepto.setDescripcion(nConcepto.getAttribute("Descripcion"));
            }
            if (nConcepto.hasAttribute("ValorUnitario")) {
                concepto.setValorUnitario(Double.parseDouble(nConcepto.getAttribute("ValorUnitario")));
            }
            if (nConcepto.hasAttribute("Importe")) {
                concepto.setImporte(Double.parseDouble(nConcepto.getAttribute("Importe")));
            }
            if (nConcepto.hasAttribute("Descuento")) {
                concepto.setDescuento(Double.parseDouble(nConcepto.getAttribute("Descuento")));
            }
            if (nConcepto.hasAttribute("ObjetoImp")) {
                concepto.setObjetoImp(nConcepto.getAttribute("ObjetoImp"));
            }
            concepto.setImpuestos(ObtenerImpuestosConcepto(nConcepto));
            concepto.setInformacionAduanera(ObtenerInformacionAduaneraConcepto(nConcepto));
            concepto.setCuentaPredial(ObtenerCuentaPredialConcepto(nConcepto));
            concepto.setParte(ObtenerParteC(nConcepto));
            conceptos.getConcepto().add(concepto);
            // Leo datos para la version 3.2
            if (nConcepto.hasAttribute("cantidad")) {
                concepto.setCantidad(Float.parseFloat(nConcepto.getAttribute("cantidad")));
            }
            if (nConcepto.hasAttribute("unidad")) {
                concepto.setUnidad(nConcepto.getAttribute("unidad"));
            }
            if (nConcepto.hasAttribute("noIdentificacion")) {
                concepto.setNoIdentificacion(nConcepto.getAttribute("noIdentificacion"));
            }
            if (nConcepto.hasAttribute("descripcion")) {
                concepto.setDescripcion(nConcepto.getAttribute("descripcion"));
            }
            if (nConcepto.hasAttribute("valorUnitario")) {
                concepto.setValorUnitario(Double.parseDouble(nConcepto.getAttribute("valorUnitario")));
            }
            if (nConcepto.hasAttribute("importe")) {
                concepto.setImporte(Double.parseDouble(nConcepto.getAttribute("importe")));
            }
        }
        return conceptos;
    }

    private static ImpuestosC ObtenerImpuestosConcepto(Element nodoConcepto) {
        NodeList lImpuestos = nodoConcepto.getElementsByTagName("cfdi:Impuestos");
        if (lImpuestos.getLength() == 0) {
            return null;
        }
        Element nImpuestos = (Element) lImpuestos.item(0);
        NodeList lTraslados = nImpuestos.getElementsByTagName("cfdi:Traslados");
        ImpuestosC impuestos = new ImpuestosC();
        if (lTraslados.getLength() > 0) {
            ArrayList<TrasladoC> traslados = new ArrayList<>();
            Element nTraslados = (Element) lTraslados.item(0);
            NodeList lTraslado = nTraslados.getElementsByTagName("cfdi:Traslado");
            for (int i = 0; i < lTraslado.getLength(); i++) {
                Element nTraslado = (Element) lTraslado.item(i);
                TrasladoC traslado = new TrasladoC();
                if (nTraslado.hasAttribute("Base")) {
                    traslado.setBase(Double.parseDouble(nTraslado.getAttribute("Base")));
                }
                if (nTraslado.hasAttribute("Impuesto")) {
                    traslado.setImpuesto(nTraslado.getAttribute("Impuesto"));
                }
                if (nTraslado.hasAttribute("TipoFactor")) {
                    traslado.setTipoFactor(nTraslado.getAttribute("TipoFactor"));
                }
                if (nTraslado.hasAttribute("TasaOCuota")) {
                    traslado.setTasaOCuota(Double.parseDouble(nTraslado.getAttribute("TasaOCuota")));
                }
                if (nTraslado.hasAttribute("Importe")) {
                    traslado.setImporte(Double.parseDouble(nTraslado.getAttribute("Importe")));
                }
                traslados.add(traslado);
            }
            impuestos.setTraslados(traslados);
        }
        NodeList lRetenciones = nImpuestos.getElementsByTagName("cfdi:Retenciones");
        if (lRetenciones.getLength() > 0) {
            ArrayList<RetencionC> retenciones = new ArrayList<>();
            Element nRetenciones = (Element) lRetenciones.item(0);
            NodeList lRetencion = nRetenciones.getElementsByTagName("cfdi:Retencion");
            for (int i = 0; i < lRetencion.getLength(); i++) {
                Element nRetencion = (Element) lRetencion.item(i);

                RetencionC retencion = new RetencionC();
                if (nRetencion.hasAttribute("Base")) {
                    retencion.setBase(Double.parseDouble(nRetencion.getAttribute("Base")));
                }
                if (nRetencion.hasAttribute("Impuesto")) {
                    retencion.setImpuesto(nRetencion.getAttribute("Impuesto"));
                }
                if (nRetencion.hasAttribute("TipoFactor")) {
                    retencion.setTipoFactor(nRetencion.getAttribute("TipoFactor"));
                }
                if (nRetencion.hasAttribute("TasaOCuota")) {
                    retencion.setTasaOCuota(Double.parseDouble(nRetencion.getAttribute("TasaOCuota")));
                }
                if (nRetencion.hasAttribute("Importe")) {
                    retencion.setImporte(Double.parseDouble(nRetencion.getAttribute("Importe")));
                }
                retenciones.add(retencion);
            }
            impuestos.setRetenciones(retenciones);
        }
        return impuestos;

    }

    private static ArrayList<InformacionAduaneraC> ObtenerInformacionAduaneraConcepto(Element nodoConcepto) {
        NodeList lInformacionAduanera = nodoConcepto.getElementsByTagName("cfdi:InformacionAduanera");
        ArrayList<InformacionAduaneraC> lista = new ArrayList<>();
        for (int i = 0; i < lInformacionAduanera.getLength(); i++) {
            Element nInformacionA = (Element) lInformacionAduanera.item(i);
            if (nInformacionA.getParentNode().getNodeName().equals("cfdi:Concepto")) {
                InformacionAduaneraC informacionAduanera = new InformacionAduaneraC();
                if (nInformacionA.hasAttribute("NumeroPedimento")) {
                    informacionAduanera.setNumeroPedimento(nInformacionA.getAttribute("NumeroPedimento"));
                }
                lista.add(informacionAduanera);
            }
        }
        return lista;
    }

    private static CuentaPredialC ObtenerCuentaPredialConcepto(Element nodoConcepto) {
        NodeList lCuentaPredial = nodoConcepto.getElementsByTagName("cfdi:CuentaPredial");
        if (lCuentaPredial.getLength() == 0) {
            return null;
        }
        Element nCuentaPredial = (Element) lCuentaPredial.item(0);
        CuentaPredialC cuentaPredial = new CuentaPredialC();
        if (nCuentaPredial.hasAttribute("Numero")) {
            cuentaPredial.setNumero(nCuentaPredial.getAttribute("Numero"));
        }
        return cuentaPredial;
    }

    private static ArrayList<ParteC> ObtenerParteC(Element nodoConcepto) {
        NodeList lParte = nodoConcepto.getElementsByTagName("cfdi:Parte");
        ArrayList<ParteC> lista = new ArrayList<>();
        for (int i = 0; i < lParte.getLength(); i++) {
            Element nParte = (Element) lParte.item(i);
            ParteC parte = new ParteC();
            if (nParte.hasAttribute("ClaveProdServ")) {
                parte.setClaveProdServ(nParte.getAttribute("ClaveProdServ"));
            }
            if (nParte.hasAttribute("NoIdentificacion")) {
                parte.setNoIdentificacion(nParte.getAttribute("NoIdentificacion"));
            }
            if (nParte.hasAttribute("Cantidad")) {
                parte.setCantidad(Double.parseDouble(nParte.getAttribute("Cantidad")));
            }
            if (nParte.hasAttribute("Unidad")) {
                parte.setUnidad(nParte.getAttribute("Unidad"));
            }
            if (nParte.hasAttribute("Descripcion")) {
                parte.setDescripcion(nParte.getAttribute("Descripcion"));
            }
            if (nParte.hasAttribute("ValorUnitario")) {
                parte.setValorUnitario(Double.parseDouble(nParte.getAttribute("ValorUnitario")));
            }
            if (nParte.hasAttribute("Importe")) {
                parte.setImporte(Double.parseDouble(nParte.getAttribute("Importe")));
            }
            parte.setInformacionAduanera(ObtenerInformacionAduaneraParte(nParte));
            // InformacionAduaneraC preguntar si no va como lista.
            lista.add(parte);
        }
        return lista;
    }

    private static ArrayList<InformacionAduaneraC> ObtenerInformacionAduaneraParte(Element nodoParte) {
        NodeList lInformacionAduanera = nodoParte.getElementsByTagName("cfdi:InformacionAduanera");
        ArrayList<InformacionAduaneraC> lista = new ArrayList<>();
        for (int i = 0; i < lInformacionAduanera.getLength(); i++) {
            Element nInformacionA = (Element) lInformacionAduanera.item(i);
            InformacionAduaneraC informacionAduanera = new InformacionAduaneraC();
            if (nInformacionA.hasAttribute("NumeroPedimento")) {
                informacionAduanera.setNumeroPedimento(nInformacionA.getAttribute("NumeroPedimento"));
            }
            lista.add(informacionAduanera);
        }
        return lista;
    }

    
    
    
    
    private static Impuestos ObtenerNodoImpuestos(Document documento) {
        NodeList lImpuestos = documento.getElementsByTagName("cfdi:Impuestos");
        if (lImpuestos.getLength() == 0) {
            return null;
        }
        boolean encontrado = false;
        int indice = 0;
        for (int i = 0; i < lImpuestos.getLength(); i++) {
            Element nImpuesto = (Element) lImpuestos.item(i);
            if ("cfdi:Comprobante".equals(nImpuesto.getParentNode().getNodeName())) {
                encontrado = true;
                break;
            }
            indice++;
        }
        if (!encontrado) {
            return null;
        }
        Element nImpuestos = (Element) lImpuestos.item(indice);
        Impuestos impuestos = new Impuestos();
        if (nImpuestos.hasAttribute("TotalImpuestosRetenidos")) {
            impuestos
                    .setTotalImpuestosRetenidos(Double.parseDouble(nImpuestos.getAttribute("TotalImpuestosRetenidos")));
        }
        if (nImpuestos.hasAttribute("TotalImpuestosTrasladados")) {
            impuestos.setTotalImpuestosTrasladados(
                    Double.parseDouble(nImpuestos.getAttribute("TotalImpuestosTrasladados")));
        }
        
        
        // Leo para la version 3.2
        if (nImpuestos.hasAttribute("totalImpuestosRetenidos")) {
            impuestos
                    .setTotalImpuestosRetenidos(Double.parseDouble(nImpuestos.getAttribute("totalImpuestosRetenidos")));
        }
        if (nImpuestos.hasAttribute("totalImpuestosTrasladados")) {
            impuestos.setTotalImpuestosTrasladados(
                    Double.parseDouble(nImpuestos.getAttribute("totalImpuestosTrasladados")));
        }
        NodeList lRetenciones = nImpuestos.getElementsByTagName("cfdi:Retenciones");
        if (lRetenciones.getLength() > 0) {
            ArrayList<Retencion> retenciones = new ArrayList<>();
            Element nRetenciones = (Element) lRetenciones.item(0);
            NodeList nlRetencion = nRetenciones.getElementsByTagName("cfdi:Retencion");
            for (int i = 0; i < nlRetencion.getLength(); i++) {
                Element nRetencion = (Element) nlRetencion.item(i);
                Retencion retencion = new Retencion();
                if (nRetencion.hasAttribute("Impuesto")) {
                    retencion.setImpuesto(nRetencion.getAttribute("Impuesto"));
                }
                if (nRetencion.hasAttribute("Importe")) {
                    retencion.setImporte(Double.parseDouble(nRetencion.getAttribute("Importe")));
                }
                // Leo datos version 3.2
                if (nRetencion.hasAttribute("impuesto")) {
                    retencion.setImpuesto(nRetencion.getAttribute("impuesto"));
                }
                if (nRetencion.hasAttribute("importe")) {
                    retencion.setImporte(Double.parseDouble(nRetencion.getAttribute("importe")));
                }
                retenciones.add(retencion);
            }
            impuestos.setRetenciones(retenciones);
        }
        
        
        NodeList lTraslados = nImpuestos.getElementsByTagName("cfdi:Traslados");
        if (lTraslados.getLength() > 0) {
            ArrayList<Traslado> traslados = new ArrayList<>();
            Element nTraslados = (Element) lTraslados.item(0);
            NodeList nlTraslado = nTraslados.getElementsByTagName("cfdi:Traslado");
            
            if (nlTraslado.getLength() == 0) {
                nlTraslado = nTraslados.getElementsByTagName("cfdi:Traslado");
                if (nlTraslado.getLength() == 0) {
                    nlTraslado = nTraslados.getElementsByTagName("Traslado");
                }
            }

            /*
            if (nlTraslado == null) {
            	nlTraslado = nTraslados.getElementsByTagName("Traslado");
            }
            */
            
         //  Element nTraslado = null;
           //for (int i = 0; i < lTraslados.getLength(); i++) {
           	for (int i = 0; i < nlTraslado.getLength(); i++) {
        	   // nTraslado = (Element) lTraslados.item(i);
        	   Element nTraslado = (Element) nlTraslado.item(i);
        	   
               Traslado traslado = new Traslado();
               
                if (nTraslado.hasAttribute("Impuesto")) {
                    traslado.setImpuesto(nTraslado.getAttribute("Impuesto"));
                }
                if (nTraslado.hasAttribute("TipoFactor")) {
                    traslado.setTipoFactor(nTraslado.getAttribute("TipoFactor"));
                }
                if (nTraslado.hasAttribute("TasaOCuota")) {
                    traslado.setTasaOCuota(Double.parseDouble(nTraslado.getAttribute("TasaOCuota")));
                }
                if (nTraslado.hasAttribute("Base")) {
                    traslado.setBase(Double.parseDouble(nTraslado.getAttribute("Base")));
                }
                if (nTraslado.hasAttribute("Importe")) {
                    traslado.setImporte(Double.parseDouble(nTraslado.getAttribute("Importe")));
                }
                // Leo datos version 3.2
                if (nTraslado.hasAttribute("impuesto")) {
                    traslado.setImpuesto(nTraslado.getAttribute("impuesto"));
                }
                if (nTraslado.hasAttribute("tasa")) {
                    traslado.setTasaOCuota(Double.parseDouble(nTraslado.getAttribute("tasa")));
                }
                if (nTraslado.hasAttribute("base")) {
                    traslado.setBase(Double.parseDouble(nTraslado.getAttribute("base")));
                }
                if (nTraslado.hasAttribute("importe")) {
                    traslado.setImporte(Double.parseDouble(nTraslado.getAttribute("importe")));
                }
                traslados.add(traslado);
            }
            impuestos.setTraslados(traslados);
        }
        return impuestos;
    }

    
    /*
    private static Impuestos ObtenerNodoImpuestos(Document documento) {
        NodeList lImpuestos = documento.getElementsByTagName("cfdi:Impuestos");
        if (lImpuestos.getLength() == 0) {
            return null;
        }
        boolean encontrado = false;
        int indice = 0;
        for (int i = 0; i < lImpuestos.getLength(); i++) {
            Element nImpuesto = (Element) lImpuestos.item(i);
            if ("cfdi:Comprobante".equals(nImpuesto.getParentNode().getNodeName())) {
                encontrado = true;
                break;
            }
            indice++;
        }
        if (!encontrado) {
            return null;
        }
        Element nImpuestos = (Element) lImpuestos.item(indice);
        Impuestos impuestos = new Impuestos();
        if (nImpuestos.hasAttribute("TotalImpuestosRetenidos")) {
            impuestos
                    .setTotalImpuestosRetenidos(Double.parseDouble(nImpuestos.getAttribute("TotalImpuestosRetenidos")));
        }
        if (nImpuestos.hasAttribute("TotalImpuestosTrasladados")) {
            impuestos.setTotalImpuestosTrasladados(
                    Double.parseDouble(nImpuestos.getAttribute("TotalImpuestosTrasladados")));
        }
        
        
        // Leo para la version 3.2
        if (nImpuestos.hasAttribute("totalImpuestosRetenidos")) {
            impuestos
                    .setTotalImpuestosRetenidos(Double.parseDouble(nImpuestos.getAttribute("totalImpuestosRetenidos")));
        }
        if (nImpuestos.hasAttribute("totalImpuestosTrasladados")) {
            impuestos.setTotalImpuestosTrasladados(
                    Double.parseDouble(nImpuestos.getAttribute("totalImpuestosTrasladados")));
        }
        NodeList lRetenciones = nImpuestos.getElementsByTagName("cfdi:Retenciones");
        if (lRetenciones.getLength() > 0) {
            ArrayList<Retencion> retenciones = new ArrayList<>();
            Element nRetenciones = (Element) lRetenciones.item(0);
            NodeList nlRetencion = nRetenciones.getElementsByTagName("cfdi:Retencion");
            for (int i = 0; i < nlRetencion.getLength(); i++) {
                Element nRetencion = (Element) nlRetencion.item(i);
                Retencion retencion = new Retencion();
                if (nRetencion.hasAttribute("Impuesto")) {
                    retencion.setImpuesto(nRetencion.getAttribute("Impuesto"));
                }
                if (nRetencion.hasAttribute("Importe")) {
                    retencion.setImporte(Double.parseDouble(nRetencion.getAttribute("Importe")));
                }
                // Leo datos version 3.2
                if (nRetencion.hasAttribute("impuesto")) {
                    retencion.setImpuesto(nRetencion.getAttribute("impuesto"));
                }
                if (nRetencion.hasAttribute("importe")) {
                    retencion.setImporte(Double.parseDouble(nRetencion.getAttribute("importe")));
                }
                retenciones.add(retencion);
            }
            impuestos.setRetenciones(retenciones);
        }
        
        
        NodeList lTraslados = nImpuestos.getElementsByTagName("cfdi:Traslados");
        if (lTraslados.getLength() > 0) {
            ArrayList<Traslado> traslados = new ArrayList<>();
            Element nTraslados = (Element) lTraslados.item(0);
            NodeList nlTraslado = nTraslados.getElementsByTagName("cfdi:Traslado");
            if (nlTraslado == null) {
            	nlTraslado = nTraslados.getElementsByTagName("Traslado");
            }
            
           Element nTraslado = null;
           logger.info("total traslados....."+lTraslados.getLength());
           for (int i = 0; i < lTraslados.getLength(); i++) {
        	   nTraslado = (Element) lTraslados.item(i);
        	   
        	   logger.info("atributos....."+nTraslado.getAttributes());
        	   
               Traslado traslado = new Traslado();
               
                if (nTraslado.hasAttribute("Impuesto")) {
                	logger.info("Entro a Impuestos.....");
                    traslado.setImpuesto(nTraslado.getAttribute("Impuesto"));
                }
                logger.info("Impuesto==>"+traslado.getImpuesto());
                if (nTraslado.hasAttribute("TipoFactor")) {
                	logger.info("Entro a TipoFactor.....");
                    traslado.setTipoFactor(nTraslado.getAttribute("TipoFactor"));
                }
                logger.info("TipoFactor==>"+traslado.getTipoFactor());
                if (nTraslado.hasAttribute("TasaOCuota")) {
                	logger.info("Entro a TasaOCuota.....");
                    traslado.setTasaOCuota(Double.parseDouble(nTraslado.getAttribute("TasaOCuota")));
                }
                logger.info("TasaOCuota==>"+traslado.getTasaOCuota());
                if (nTraslado.hasAttribute("Base")) {
                	logger.info("Entro a Base.....");
                    traslado.setBase(Double.parseDouble(nTraslado.getAttribute("Base")));
                }
                logger.info("Base==>"+traslado.getBase());
                if (nTraslado.hasAttribute("Importe")) {
                	logger.info("Entro a Importe.....");
                    traslado.setImporte(Double.parseDouble(nTraslado.getAttribute("Importe")));
                }
                logger.info("Importe==>"+traslado.getImporte());
                // Leo datos version 3.2
                if (nTraslado.hasAttribute("impuesto")) {
                    traslado.setImpuesto(nTraslado.getAttribute("impuesto"));
                }
                logger.info("Impuesto 3.2 ==>"+traslado.getImpuesto());
                if (nTraslado.hasAttribute("tasa")) {
                    traslado.setTasaOCuota(Double.parseDouble(nTraslado.getAttribute("tasa")));
                }
                logger.info("tasa 3.2 ==>"+traslado.getTasaOCuota());
                if (nTraslado.hasAttribute("base")) {
                    traslado.setBase(Double.parseDouble(nTraslado.getAttribute("base")));
                }
                logger.info("base 3.2 ==>"+traslado.getBase());
                if (nTraslado.hasAttribute("importe")) {
                    traslado.setImporte(Double.parseDouble(nTraslado.getAttribute("importe")));
                }
                logger.info("Importe 3.2 ==>"+traslado.getImporte());
                traslados.add(traslado);
            }
            impuestos.setTraslados(traslados);
        }
        return impuestos;
    }
*/
    
    private static Complemento ObtenerNodoComplementos(Document documento) {
        Complemento complemento = new Complemento();
        NodeList listaComplemento = documento.getElementsByTagName("cfdi:Complemento");
        if (listaComplemento.getLength() == 0) {
            return null;
        }
        Element nodoComplemento = (Element) listaComplemento.item(0);
        complemento.CartaPorte10 = com.itextpdf.xmltopdf.CartaPorte10.Leer
                .ObtenerNodoComplementoCartaPorte(nodoComplemento);
        complemento.CartaPorte20 = com.itextpdf.xmltopdf.CartaPorte20.Leer
                .ObtenerNodoComplementoCartaPorte(nodoComplemento);
        complemento.CartaPorte30 = com.itextpdf.xmltopdf.CartaPorte30.Leer
                .ObtenerNodoComplementoCartaPorte(nodoComplemento);
        
        complemento.CartaPorte31 = com.itextpdf.xmltopdf.CartaPorte31.Leer
                .ObtenerNodoComplementoCartaPorte(nodoComplemento);
        
        complemento.TimbreFiscalDigital = ObtenerNodoComplementoTimbreFiscalDigital(nodoComplemento);
        complemento.Nomina = ObtenerNodoComplementoNomina(nodoComplemento);
        complemento.Pagos = ObtenerNodoPagos(nodoComplemento);
        complemento.Pagos20 = ObtenerNodoPagos20(nodoComplemento);

        return complemento;
    }

    private static TimbreFiscalDigital ObtenerNodoComplementoTimbreFiscalDigital(Element nodoComplemento) {
        NodeList lTimbreFiscalDigital = nodoComplemento.getElementsByTagName("tfd:TimbreFiscalDigital");
        if (lTimbreFiscalDigital.getLength() == 0) {
            return null;
        }
        Element nTimbreFiscalDigital = (Element) lTimbreFiscalDigital.item(0);
        TimbreFiscalDigital timbreFiscalDigital = new TimbreFiscalDigital();
        if (nTimbreFiscalDigital.hasAttribute("Version")) {
            timbreFiscalDigital.setVersion(nTimbreFiscalDigital.getAttribute("Version"));
        }
        if (nTimbreFiscalDigital.hasAttribute("UUID")) {
            timbreFiscalDigital.setUUID(nTimbreFiscalDigital.getAttribute("UUID"));
        }
        if (nTimbreFiscalDigital.hasAttribute("FechaTimbrado")) {
            timbreFiscalDigital
                    .setFechaTimbrado(LocalDateTime.parse(nTimbreFiscalDigital.getAttribute("FechaTimbrado")));
        }
        if (nTimbreFiscalDigital.hasAttribute("RfcProvCertif")) {
            timbreFiscalDigital.setRfcProvCertif(nTimbreFiscalDigital.getAttribute("RfcProvCertif"));
        }
        if (nTimbreFiscalDigital.hasAttribute("Leyenda")) {
            timbreFiscalDigital.setLeyenda(nTimbreFiscalDigital.getAttribute("Leyenda"));
        }
        if (nTimbreFiscalDigital.hasAttribute("SelloCFD")) {
            timbreFiscalDigital.setSelloCFD(nTimbreFiscalDigital.getAttribute("SelloCFD"));
        }
        if (nTimbreFiscalDigital.hasAttribute("NoCertificadoSAT")) {
            timbreFiscalDigital.setNoCertificadoSAT(nTimbreFiscalDigital.getAttribute("NoCertificadoSAT"));
        }
        if (nTimbreFiscalDigital.hasAttribute("SelloSAT")) {
            timbreFiscalDigital.setSelloSAT(nTimbreFiscalDigital.getAttribute("SelloSAT"));
        }
        // Leo version 3.2
        if (nTimbreFiscalDigital.hasAttribute("version")) {
            timbreFiscalDigital.setVersion(nTimbreFiscalDigital.getAttribute("version"));
        }
        if (nTimbreFiscalDigital.hasAttribute("UUID")) {
            timbreFiscalDigital.setUUID(nTimbreFiscalDigital.getAttribute("UUID"));
        }
        if (nTimbreFiscalDigital.hasAttribute("fechaTimbrado")) {
            timbreFiscalDigital
                    .setFechaTimbrado(LocalDateTime.parse(nTimbreFiscalDigital.getAttribute("fechaTimbrado")));
        }
        if (nTimbreFiscalDigital.hasAttribute("selloCFD")) {
            timbreFiscalDigital.setSelloCFD(nTimbreFiscalDigital.getAttribute("selloCFD"));
        }
        if (nTimbreFiscalDigital.hasAttribute("noCertificadoSAT")) {
            timbreFiscalDigital.setNoCertificadoSAT(nTimbreFiscalDigital.getAttribute("noCertificadoSAT"));
        }
        if (nTimbreFiscalDigital.hasAttribute("selloSAT")) {
            timbreFiscalDigital.setSelloSAT(nTimbreFiscalDigital.getAttribute("selloSAT"));
        }
        return timbreFiscalDigital;
    }

    // <editor-fold defaultstate="collapsed" desc="Nomina">
    private static Nomina ObtenerNodoComplementoNomina(Element nodoComplemento) {
        NodeList lNomina = nodoComplemento.getElementsByTagName("nomina12:Nomina");
        if (lNomina.getLength() <= 0) {
            return null;
        }
        Element nNomina = (Element) lNomina.item(0);
        Nomina nomina = new Nomina();
        if (nNomina.hasAttribute("Version")) {
            nomina.setVersion(nNomina.getAttribute("Version"));
        }
        if (nNomina.hasAttribute("TipoNomina")) {
            nomina.setTipoNomina(nNomina.getAttribute("TipoNomina"));
        }
        if (nNomina.hasAttribute("FechaPago")) {
            nomina.setFechaPago(LocalDate.parse(nNomina.getAttribute("FechaPago")));
        }
        if (nNomina.hasAttribute("FechaInicialPago")) {
            nomina.setFechaInicialPago(LocalDate.parse(nNomina.getAttribute("FechaInicialPago")));
        }
        if (nNomina.hasAttribute("FechaFinalPago")) {
            nomina.setFechaFinalPago(LocalDate.parse(nNomina.getAttribute("FechaFinalPago")));
        }
        if (nNomina.hasAttribute("NumDiasPagados")) {
            nomina.setNumDiasPagados(Double.parseDouble(nNomina.getAttribute("NumDiasPagados")));
        }
        if (nNomina.hasAttribute("TotalPercepciones")) {
            nomina.setTotalPercepciones(Double.parseDouble(nNomina.getAttribute("TotalPercepciones")));
        }
        if (nNomina.hasAttribute("TotalDeducciones")) {
            nomina.setTotalDeducciones(Double.parseDouble(nNomina.getAttribute("TotalDeducciones")));
        }
        if (nNomina.hasAttribute("TotalOtrosPagos")) {
            nomina.setTotalOtrosPagos(Double.parseDouble(nNomina.getAttribute("TotalOtrosPagos")));
        }

        nomina.setEmisor(ObtenerNodoEmisorNomina(nNomina));
        nomina.setReceptor(ObtenerNodoReceptorNomina(nNomina));
        nomina.setPercepciones(ObtenerNodoPercepcionesNomina(nNomina));
        nomina.setDeducciones(ObtenerNodoDeduccionesNomina(nNomina));
        nomina.setOtrosPagos(ObtenerNodoOtrosPagosNomina(nNomina));
        nomina.setIncapacidades(ObtenerNodoIncapacidadesNomina(nNomina));
        nomina.setHorasExtra(ObtenerNodoHorasExtraNomina(nNomina));
        return nomina;
    }

    private static NEmisor ObtenerNodoEmisorNomina(Element nodoNomina) {
        NodeList lNEmisores = nodoNomina.getElementsByTagName("nomina12:Emisor");
        if (lNEmisores.getLength() == 0) {
            return null;
        }
        Element nNEmisor = (Element) lNEmisores.item(0);
        NEmisor nEmisor = new NEmisor();
        if (nNEmisor.hasAttribute("Curp")) {
            nEmisor.setCurp(nNEmisor.getAttribute("Curp"));
        }
        if (nNEmisor.hasAttribute("RegistroPatronal")) {
            nEmisor.setRegistroPatronal(nNEmisor.getAttribute("RegistroPatronal"));
        }
        if (nNEmisor.hasAttribute("RfcPatronOrigen")) {
            nEmisor.setRfcPatronOrigen(nNEmisor.getAttribute("RfcPatronOrigen"));
        }
        nEmisor.setEntidadSNCF(ObtenerNodoEntidadSNCFEmisorNomina(nNEmisor));
        return nEmisor;
    }

    private static NEntidadSNCF ObtenerNodoEntidadSNCFEmisorNomina(Element nodoEmisorNomina) {
        NodeList lNEntidadSNCF = nodoEmisorNomina.getElementsByTagName("nomina12:EntidadSNCF");
        if (lNEntidadSNCF.getLength() == 0) {
            return null;
        }
        Element nNEntidadSNCF = (Element) lNEntidadSNCF.item(0);
        NEntidadSNCF entidadSNCF = new NEntidadSNCF();
        if (nNEntidadSNCF.hasAttribute("OrigenRecurso")) {
            entidadSNCF.setOrigenRecurso(nNEntidadSNCF.getAttribute("OrigenRecurso"));
        }
        if (nNEntidadSNCF.hasAttribute("MontoRecursoPropio")) {
            entidadSNCF.setMontoRecursoPropio(Double.parseDouble(nNEntidadSNCF.getAttribute("Cantidad")));
        }
        return entidadSNCF;
    }

    private static NReceptor ObtenerNodoReceptorNomina(Element nodoNomina) {
        NodeList lNReceptor = nodoNomina.getElementsByTagName("nomina12:Receptor");
        if (lNReceptor.getLength() == 0) {
            return null;
        }
        Element nNReceptor = (Element) lNReceptor.item(0);
        NReceptor nReceptor = new NReceptor();
        if (nNReceptor.hasAttribute("Curp")) {
            nReceptor.setCurp(nNReceptor.getAttribute("Curp"));
        }
        if (nNReceptor.hasAttribute("NumSeguridadSocial")) {
            nReceptor.setNumSeguridadSocial(nNReceptor.getAttribute("NumSeguridadSocial"));
        }
        if (nNReceptor.hasAttribute("FechaInicioRelLaboral")) {
            nReceptor.setFechaInicioRelLaboral(LocalDate.parse(nNReceptor.getAttribute("FechaInicioRelLaboral")));
        }
        if (nNReceptor.hasAttribute("Antigüedad")) {
            nReceptor.setAntiguedad(nNReceptor.getAttribute("Antigüedad"));
        }
        if (nNReceptor.hasAttribute("TipoContrato")) {
            nReceptor.setTipoContrato(nNReceptor.getAttribute("TipoContrato"));
        }
        if (nNReceptor.hasAttribute("Sindicalizado")) {
            nReceptor.setSindicalizado(nNReceptor.getAttribute("Sindicalizado"));
        }
        if (nNReceptor.hasAttribute("TipoJornada")) {
            nReceptor.setTipoJornada(nNReceptor.getAttribute("TipoJornada"));
        }
        if (nNReceptor.hasAttribute("TipoRegimen")) {
            nReceptor.setTipoRegimen(nNReceptor.getAttribute("TipoRegimen"));
        }
        if (nNReceptor.hasAttribute("NumEmpleado")) {
            nReceptor.setNumEmpleado(nNReceptor.getAttribute("NumEmpleado"));
        }
        if (nNReceptor.hasAttribute("Departamento")) {
            nReceptor.setDepartamento(nNReceptor.getAttribute("Departamento"));
        }
        if (nNReceptor.hasAttribute("Puesto")) {
            nReceptor.setPuesto(nNReceptor.getAttribute("Puesto"));
        }
        if (nNReceptor.hasAttribute("RiesgoPuesto")) {
            nReceptor.setRiesgoPuesto(nNReceptor.getAttribute("RiesgoPuesto"));
        }
        if (nNReceptor.hasAttribute("PeriodicidadPago")) {
            nReceptor.setPeriodicidadPago(nNReceptor.getAttribute("PeriodicidadPago"));
        }
        if (nNReceptor.hasAttribute("Banco")) {
            nReceptor.setBanco(nNReceptor.getAttribute("Banco"));
        }
        if (nNReceptor.hasAttribute("CuentaBancaria")) {
            nReceptor.setCuentaBancaria(nNReceptor.getAttribute("CuentaBancaria"));
        }
        if (nNReceptor.hasAttribute("SalarioBaseCotApor")) {
            nReceptor.setSalarioBaseCotApor(Double.parseDouble(nNReceptor.getAttribute("SalarioBaseCotApor")));
        }
        if (nNReceptor.hasAttribute("SalarioDiarioIntegrado")) {
            nReceptor.setSalarioDiarioIntegrado(Double.parseDouble(nNReceptor.getAttribute("SalarioDiarioIntegrado")));
        }
        if (nNReceptor.hasAttribute("ClaveEntFed")) {
            nReceptor.setClaveEntFed(nNReceptor.getAttribute("ClaveEntFed"));
        }
        nReceptor.setSubContratacion(ObtenerNodoSubContratacionReceptorNomina(nNReceptor));
        return nReceptor;
    }

    private static ArrayList<NSubContratacion> ObtenerNodoSubContratacionReceptorNomina(Element nodoReceptorNomina) {
        NodeList lNSubContratacion = nodoReceptorNomina.getElementsByTagName("nomina12:SubContratacion");
        if (lNSubContratacion.getLength() == 0) {
            return null;
        }
        ArrayList<NSubContratacion> lista = new ArrayList<>();
        for (int i = 0; i < lNSubContratacion.getLength(); i++) {
            Element nNSubContratacion = ((Element) lNSubContratacion.item(i));
            NSubContratacion subcontratacion = new NSubContratacion();
            if (nNSubContratacion.hasAttribute("RfcLabora")) {
                subcontratacion.setRfcLabora(nNSubContratacion.getAttribute("RfcLabora"));
            }
            if (nNSubContratacion.hasAttribute("PorcentajeTiempo")) {
                subcontratacion
                        .setPorcentajeTiempo(Double.parseDouble(nNSubContratacion.getAttribute("PorcentajeTiempo")));
            }
            lista.add(subcontratacion);
        }
        return lista;
    }

    private static NPercepciones ObtenerNodoPercepcionesNomina(Element nodoNomina) {
        NodeList nPercepciones = nodoNomina.getElementsByTagName("nomina12:Percepciones");
        if (nPercepciones.getLength() == 0) {
            return null;
        }
        Element nPercepcion = (Element) nPercepciones.item(0);
        NPercepciones percepciones = new NPercepciones();
        if (nPercepcion.hasAttribute("TotalSueldos")) {
            percepciones.setTotalSueldos(Double.parseDouble(nPercepcion.getAttribute("TotalSueldos")));
        }
        if (nPercepcion.hasAttribute("TotalSeparacionIndemnizacion")) {
            percepciones.setTotalSeparacionIndemnizacion(
                    Double.parseDouble(nPercepcion.getAttribute("TotalSeparacionIndemnizacion")));
        }
        if (nPercepcion.hasAttribute("TotalJubilacionPensionRetiro")) {
            percepciones.setTotalJubilacionPensionRetiro(
                    Double.parseDouble(nPercepcion.getAttribute("TotalJubilacionPensionRetiro")));
        }
        if (nPercepcion.hasAttribute("TotalGravado")) {
            percepciones.setTotalGravado(Double.parseDouble(nPercepcion.getAttribute("TotalGravado")));
        }
        if (nPercepcion.hasAttribute("TotalExento")) {
            percepciones.setTotalExento(Double.parseDouble(nPercepcion.getAttribute("TotalExento")));
        }
        percepciones.setPercepcion(new ArrayList<>());

        NodeList lPercepciones = nPercepcion.getElementsByTagName("nomina12:Percepcion");
        for (int i = 0; i < lPercepciones.getLength(); i++) {
            Element p = (Element) lPercepciones.item(i);
            NPercepcion percepcion = new NPercepcion();
            if (p.hasAttribute("TipoPercepcion")) {
                percepcion.setTipoPercepcion(p.getAttribute("TipoPercepcion"));
            }
            if (p.hasAttribute("Clave")) {
                percepcion.setClave(p.getAttribute("Clave"));
            }
            if (p.hasAttribute("Concepto")) {
                percepcion.setConcepto(p.getAttribute("Concepto"));
            }
            if (p.hasAttribute("ImporteGravado")) {
                percepcion.setImporteGravado(Double.parseDouble(p.getAttribute("ImporteGravado")));
            }
            if (p.hasAttribute("ImporteExento")) {
                percepcion.setImporteExento(Double.parseDouble(p.getAttribute("ImporteExento")));
            }

            percepcion.setAccionesOTitulos(ObtenerNodoAccionesOTitulosPercepcionNomina(p));
            percepcion.setHorasExtras(ObtenerNodoHorasExtraNomina(p));
            percepciones.getPercepcion().add(percepcion);
        }
        percepciones.setJubilacionPensionRetiro(ObtenerNodoJubilacionPensionRetiroNomina(nPercepcion));
        percepciones.setSeparacionIndeminzacion(ObtenerNodoSeparacionIndemnizacion(nPercepcion));
        return percepciones;
    }

    // NAccionesOTitulos
    private static NAccionesOTitulos ObtenerNodoAccionesOTitulosPercepcionNomina(Element nodoPercepcionNomina) {
        NodeList lNAccionesOTitulos = nodoPercepcionNomina.getElementsByTagName("nomina12:AccionesOTitulos");
        if (lNAccionesOTitulos.getLength() == 0) {
            return null;
        }
        Element nNAccionesOTitulos = (Element) lNAccionesOTitulos.item(0);
        NAccionesOTitulos accionesOTitulos = new NAccionesOTitulos();
        accionesOTitulos.setValorMercado(nNAccionesOTitulos.getAttribute("ValorMercado"));
        accionesOTitulos.setPrecioAlOtorgarse(Double.parseDouble(nNAccionesOTitulos.getAttribute("PrecioAlOtorgarse")));
        return accionesOTitulos;
    }

    private static ArrayList<NHorasExtra> ObtenerNodoHorasExtraNomina(Element nodoNomina) {
        NodeList lNHorasExtra = nodoNomina.getElementsByTagName("nomina12:HorasExtra");
        if (lNHorasExtra.getLength() == 0) {
            return null;
        }
        ArrayList<NHorasExtra> lista = new ArrayList<>();
        for (int i = 0; i < lNHorasExtra.getLength(); i++) {
            Element h = (Element) lNHorasExtra.item(i);
            NHorasExtra horasExtra = new NHorasExtra();
            horasExtra.setDias(Integer.parseInt(h.getAttribute("Dias")));
            horasExtra.setTipoHoras(Integer.parseInt(h.getAttribute("TipoHoras")));
            horasExtra.setHorasExtra(Integer.parseInt(h.getAttribute("HorasExtra")));
            horasExtra.setImportePagado(Double.parseDouble(h.getAttribute("ImportePagado")));
            lista.add(horasExtra);

        }
        return lista;
    }

    // NJubilacionPensionRetiro
    private static NJubilacionPensionRetiro ObtenerNodoJubilacionPensionRetiroNomina(Element nodoPercepcionesNomina) {
        NodeList lNJubilacionPensionRetiro = nodoPercepcionesNomina
                .getElementsByTagName("nomina12:JubilacionPensionRetiro");
        if (lNJubilacionPensionRetiro.getLength() == 0) {
            return null;
        }
        Element nNJubilacionPensionRetiro = (Element) lNJubilacionPensionRetiro.item(0);
        NJubilacionPensionRetiro jubilacionPensionRetiro = new NJubilacionPensionRetiro();
        if (nNJubilacionPensionRetiro.hasAttribute("TotalUnaExhibicion")) {
            jubilacionPensionRetiro.setTotalUnaExhibicion(
                    Double.parseDouble(nNJubilacionPensionRetiro.getAttribute("TotalUnaExhibicion")));
        }
        if (nNJubilacionPensionRetiro.hasAttribute("TotalParcialidad")) {
            jubilacionPensionRetiro
                    .setTotalParcialidad(
                            Double.parseDouble(nNJubilacionPensionRetiro.getAttribute("TotalParcialidad")));
        }
        if (nNJubilacionPensionRetiro.hasAttribute("MontoDiario")) {
            jubilacionPensionRetiro
                    .setMontoDiario(Double.parseDouble(nNJubilacionPensionRetiro.getAttribute("MontoDiario")));
        }
        jubilacionPensionRetiro
                .setIngresoAcumulable(Double.parseDouble(nNJubilacionPensionRetiro.getAttribute("IngresoAcumulable")));
        jubilacionPensionRetiro.setIngresoNoAcumulable(
                Double.parseDouble(nNJubilacionPensionRetiro.getAttribute("IngresoNoAcumulable")));
        return jubilacionPensionRetiro;
    }

    // NSeparacionIndemnizacion
    private static NSeparacionIndemnizacion ObtenerNodoSeparacionIndemnizacion(Element nodoPercepcionesNomina) {
        NodeList lNSeparacionIndeminzacion = nodoPercepcionesNomina
                .getElementsByTagName("nomina12:SeparacionIndemnizacion");
        if (lNSeparacionIndeminzacion.getLength() == 0) {
            return null;
        }
        Element nNSeparacionIndeminzacion = (Element) lNSeparacionIndeminzacion.item(0);
        NSeparacionIndemnizacion separacionIndeminzacion = new NSeparacionIndemnizacion();
        separacionIndeminzacion
                .setTotalPagado(Double.parseDouble(nNSeparacionIndeminzacion.getAttribute("TotalPagado")));
        //separacionIndeminzacion .setNumAnosServicio(Integer.parseInt(nNSeparacionIndeminzacion.getAttribute("NumAnosServicio")));
        separacionIndeminzacion .setNumAnosServicio(Integer.parseInt(nNSeparacionIndeminzacion.getAttribute("NumAñosServicio")));
        separacionIndeminzacion.setUltimoSueldoMensOrd(
                Double.parseDouble(nNSeparacionIndeminzacion.getAttribute("UltimoSueldoMensOrd")));
        separacionIndeminzacion
                .setIngresoAcumulable(Double.parseDouble(nNSeparacionIndeminzacion.getAttribute("IngresoAcumulable")));
        separacionIndeminzacion.setIngresoNoAcumulable(
                Double.parseDouble(nNSeparacionIndeminzacion.getAttribute("IngresoNoAcumulable")));
        return separacionIndeminzacion;
    }

    private static NDeducciones ObtenerNodoDeduccionesNomina(Element nodoNomina) {
        NodeList nNDeducciones = nodoNomina.getElementsByTagName("nomina12:Deducciones");
        if (nNDeducciones.getLength() == 0) {
            return null;
        }
        Element nNDeduccion = (Element) nNDeducciones.item(0);
        NDeducciones deducciones = new NDeducciones();
        if (nNDeduccion.hasAttribute("TotalOtrasDeducciones")) {
            deducciones.setTotalOtrasDeducciones(Double.parseDouble(nNDeduccion.getAttribute("TotalOtrasDeducciones")));
        }
        if (nNDeduccion.hasAttribute("TotalImpuestosRetenidos")) {
            deducciones
                    .setTotalImpuestosRetenidos(
                            Double.parseDouble(nNDeduccion.getAttribute("TotalImpuestosRetenidos")));
        }
        deducciones.setDeduccion(new ArrayList<NDeduccion>());

        NodeList listaDeduccion = nNDeduccion.getElementsByTagName("nomina12:Deduccion");
        for (int i = 0; i < listaDeduccion.getLength(); i++) {
            Element d = (Element) listaDeduccion.item(i);
            NDeduccion deduccion = new NDeduccion();
            if (d.hasAttribute("TipoDeduccion")) {
                deduccion.setTipoDeduccion(d.getAttribute("TipoDeduccion"));
            }
            if (d.hasAttribute("Clave")) {
                deduccion.setClave(d.getAttribute("Clave"));
            }
            if (d.hasAttribute("Concepto")) {
                deduccion.setConcepto(d.getAttribute("Concepto"));
            }
            if (d.hasAttribute("Importe")) {
                deduccion.setImporte(Double.parseDouble(d.getAttribute("Importe")));
            }
            deducciones.getDeduccion().add(deduccion);
        }
        return deducciones;
    }

    // NOtrosPagos
    private static NOtrosPagos ObtenerNodoOtrosPagosNomina(Element nodoNomina) {
        NodeList lOtrosPagos = nodoNomina.getElementsByTagName("nomina12:OtrosPagos");
        if (lOtrosPagos.getLength() == 0) {
            return null;
        }
        NOtrosPagos otrosPagos = new NOtrosPagos();
        otrosPagos.setOtroPago(new ArrayList<>());
        Element nOtrosPagos = (Element) lOtrosPagos.item(0);
        NodeList lOtroPago = nOtrosPagos.getElementsByTagName("nomina12:OtroPago");
        for (int i = 0; i < lOtroPago.getLength(); i++) {
            Element op = (Element) lOtroPago.item(i);
            NOtroPago otroPago = new NOtroPago();
            if (op.hasAttribute("TipoOtroPago")) {
                otroPago.setTipoOtroPago(op.getAttribute("TipoOtroPago"));
            }
            if (op.hasAttribute("Clave")) {
                otroPago.setClave(op.getAttribute("Clave"));
            }
            if (op.hasAttribute("Concepto")) {
                otroPago.setConcepto(op.getAttribute("Concepto"));
            }
            if (op.hasAttribute("Importe")) {
                otroPago.setImporte(Double.parseDouble(op.getAttribute("Importe")));
            }
            otroPago.setSubsidioAlEmpleo(ObtenerNodoSubsidioAlEmpleo(op));
            otroPago.setCompensacionSaldosAFavor(ObtenerNodoCompensacionSaldosAFavor(op));
            otrosPagos.getOtroPago().add(otroPago);
        }
        return otrosPagos;
    }

    // NSubsidioAlEmpleo
    private static NSubsidioAlEmpleo ObtenerNodoSubsidioAlEmpleo(Element nodoOtroPago) {
        NodeList lNSubsidioAlEmpleo = nodoOtroPago.getElementsByTagName("nomina12:SubsidioAlEmpleo");
        if (lNSubsidioAlEmpleo.getLength() == 0) {
            return null;
        }
        Element nSubsidioAlEmpleado = (Element) lNSubsidioAlEmpleo.item(0);
        NSubsidioAlEmpleo subsidioAlEmpleo = new NSubsidioAlEmpleo();
        if (nSubsidioAlEmpleado.hasAttribute("SubsidioCausado")) {
            subsidioAlEmpleo
                    .setSubsidioCausado(Double.parseDouble(nSubsidioAlEmpleado.getAttribute("SubsidioCausado")));
        }
        return subsidioAlEmpleo;
    }

    // NCompensacionSaldosAFavor
    private static NCompensacionSaldosAFavor ObtenerNodoCompensacionSaldosAFavor(Element nodoOtroPagoNomina) {
        NodeList lNCompensacionSaldosAFavor = nodoOtroPagoNomina
                .getElementsByTagName("nomina12:CompensacionSaldosAFavor");
        if (lNCompensacionSaldosAFavor.getLength() == 0) {
            return null;
        }
        Element nNCompensacionSaldosAFavor = (Element) lNCompensacionSaldosAFavor.item(0);
        NCompensacionSaldosAFavor compensacionSaldosAFavor = new NCompensacionSaldosAFavor();
        compensacionSaldosAFavor
                .setSaldoAFavor(Double.parseDouble(nNCompensacionSaldosAFavor.getAttribute("SaldoAFavor")));
        compensacionSaldosAFavor.setAno(nNCompensacionSaldosAFavor.getAttribute("Año"));
        compensacionSaldosAFavor
                .setRemanenteSalFav(Double.parseDouble(nNCompensacionSaldosAFavor.getAttribute("RemanenteSalFav")));
        return compensacionSaldosAFavor;
    }

    private static NIncapacidades ObtenerNodoIncapacidadesNomina(Element nodoNomina) {
        NodeList nIncapacidades = nodoNomina.getElementsByTagName("nomina12:Incapacidades");
        if (nIncapacidades.getLength() == 0) {
            return null;
        }
        Element nIncapacidad = (Element) nIncapacidades.item(0);
        NIncapacidades incapacidades = new NIncapacidades();
        incapacidades.setIncapacidad(new ArrayList<>());

        NodeList lIncapacidad = nIncapacidad.getElementsByTagName("nomina12:Incapacidad");
        for (int j = 0; j < lIncapacidad.getLength(); j++) {
            Element i = (Element) lIncapacidad.item(j);
            NIncapacidad incapacidad = new NIncapacidad();
            if (i.hasAttribute("DiasIncapacidad")) {
                incapacidad.setDiasIncapacidad(Integer.parseInt(i.getAttribute("DiasIncapacidad")));
            }
            if (i.hasAttribute("TipoIncapacidad")) {
                incapacidad.setTipoIncapacidad(i.getAttribute("Concepto"));
            }
            incapacidades.getIncapacidad().add(incapacidad);
        }
        return incapacidades;

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Complemento de pagos 20">

    private static com.itextpdf.xmltopdf.Pagos20.Pagos20 ObtenerNodoPagos20(Element nodoComplemento) {
        NodeList lPagos = nodoComplemento.getElementsByTagName("pago20:Pagos");
        if (lPagos.getLength() == 0) {
            return null;
        }
        com.itextpdf.xmltopdf.Pagos20.Pagos20 pagos = new com.itextpdf.xmltopdf.Pagos20.Pagos20();
        // pagos.getPago() = new ArrayList<Pago>();
        Element nPagos = (Element) lPagos.item(0);
        if (nPagos.hasAttribute("Version")) {
            pagos.setVersion(nPagos.getAttribute("Version"));
        }
        pagos.setTotales(ObtenerTotalesP20(nPagos));
        NodeList lPago = nPagos.getElementsByTagName("pago20:Pago");
        for (int i = 0; i < lPago.getLength(); i++) {
            Element nPago = (Element) lPago.item(i);
            com.itextpdf.xmltopdf.Pagos20.Pago pago = new com.itextpdf.xmltopdf.Pagos20.Pago();
            if (nPago.hasAttribute("FechaPago")) {
                pago.setFechaPago(LocalDateTime.parse(nPago.getAttribute("FechaPago").trim()));
            }
            if (nPago.hasAttribute("FormaDePagoP")) {
                pago.setFormaDePagoP(nPago.getAttribute("FormaDePagoP"));
            }
            if (nPago.hasAttribute("MonedaP")) {
                pago.setMonedaP(nPago.getAttribute("MonedaP"));
            }
            if (nPago.hasAttribute("TipoCambioP")) {
                pago.setTipoCambioP(Double.parseDouble(nPago.getAttribute("TipoCambioP")));
            }
            if (nPago.hasAttribute("Monto")) {
                pago.setMonto(Double.parseDouble(nPago.getAttribute("Monto")));
            }
            if (nPago.hasAttribute("NumOperacion")) {
                pago.setNumOperacion(nPago.getAttribute("NumOperacion"));
            }
            if (nPago.hasAttribute("RfcEmisorCtaOrd")) {
                pago.setRfcEmisorCtaOrd(nPago.getAttribute("RfcEmisorCtaOrd"));
            }
            if (nPago.hasAttribute("NomBancoOrdExt")) {
                pago.setNomBancoOrdExt(nPago.getAttribute("NomBancoOrdExt"));
            }
            if (nPago.hasAttribute("CtaOrdenante")) {
                pago.setCtaOrdenante(nPago.getAttribute("CtaOrdenante"));
            }
            if (nPago.hasAttribute("RfcEmisorCtaBen")) {
                pago.setRfcEmisorCtaBen(nPago.getAttribute("RfcEmisorCtaBen"));
            }
            if (nPago.hasAttribute("CtaBeneficiario")) {
                pago.setCtaBeneficiario(nPago.getAttribute("CtaBeneficiario"));
            }
            if (nPago.hasAttribute("TipoCadPago")) {
                pago.setTipoCadPago(nPago.getAttribute("TipoCadPago"));
            }
            if (nPago.hasAttribute("CertPago")) {
                pago.setCertPago(nPago.getAttribute("CertPago"));
            }
            if (nPago.hasAttribute("CadPago")) {
                pago.setCadPago(nPago.getAttribute("CadPago"));
            }
            if (nPago.hasAttribute("SelloPago")) {
                pago.setSelloPago(nPago.getAttribute("SelloPago"));
            }
            pago.setDoctoRelacionado(ObtenerDoctoRelacionadoPago20(nPago));
            pago.setImpuestos(obtenerImpuestosPago20(nPago));
            pagos.getPago().add(pago);
        }
        return pagos;
    }

    private static com.itextpdf.xmltopdf.Pagos20.Totales ObtenerTotalesP20(Element nodoPagos) {
        NodeList lnTotales = nodoPagos.getElementsByTagName("pago20:Totales");
        com.itextpdf.xmltopdf.Pagos20.Totales totales = new com.itextpdf.xmltopdf.Pagos20.Totales();
        if (lnTotales.getLength() == 0) {
            return totales;
        }
        Element nTotales = (Element) lnTotales.item(0);

        if (nTotales.hasAttribute("MontoTotalPagos")) {
            totales.setMontoTotalPagos(Double.parseDouble(nTotales.getAttribute("MontoTotalPagos")));
        }
        if (nTotales.hasAttribute("TotalRetencionesIEPS")) {
            totales.setTotalRetencionesIEPS(Double.parseDouble(nTotales.getAttribute("TotalRetencionesIEPS")));
        }
        if (nTotales.hasAttribute("TotalRetencionesISR")) {
            totales.setTotalRetencionesISR(Double.parseDouble(nTotales.getAttribute("TotalRetencionesISR")));
        }
        if (nTotales.hasAttribute("TotalRetencionesIVA")) {
            totales.setTotalRetencionesIVA(Double.parseDouble(nTotales.getAttribute("TotalRetencionesIVA")));
        }
        if (nTotales.hasAttribute("TotalTrasladosBaseIVA0")) {
            totales.setTotalTrasladosBaseIVA0(Double.parseDouble(nTotales.getAttribute("TotalTrasladosBaseIVA0")));
        }
        if (nTotales.hasAttribute("TotalTrasladosBaseIVA16")) {
            totales.setTotalTrasladosBaseIVA16(Double.parseDouble(nTotales.getAttribute("TotalTrasladosBaseIVA16")));
        }
        if (nTotales.hasAttribute("TotalTrasladosBaseIVA8")) {
            totales.setTotalTrasladosBaseIVA8(Double.parseDouble(nTotales.getAttribute("TotalTrasladosBaseIVA8")));
        }
        if (nTotales.hasAttribute("TotalTrasladosBaseIVAExento")) {
            totales.setTotalTrasladosBaseIVAExento(
                    Double.parseDouble(nTotales.getAttribute("TotalTrasladosBaseIVAExento")));
        }
        if (nTotales.hasAttribute("TotalTrasladosImpuestoIVA0")) {
            totales.setTotalTrasladosImpuestoIVA0(
                    Double.parseDouble(nTotales.getAttribute("TotalTrasladosImpuestoIVA0")));
        }
        if (nTotales.hasAttribute("TotalTrasladosImpuestoIVA16")) {
            totales.setTotalTrasladosImpuestoIVA16(
                    Double.parseDouble(nTotales.getAttribute("TotalTrasladosImpuestoIVA16")));
        }
        if (nTotales.hasAttribute("TotalTrasladosImpuestoIVA8")) {
            totales.setTotalTrasladosImpuestoIVA8(
                    Double.parseDouble(nTotales.getAttribute("TotalTrasladosImpuestoIVA8")));
        }
        return totales;
    }

    private static ArrayList<com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado> ObtenerDoctoRelacionadoPago20(
            Element nodoPago) {
        NodeList lnDoctoRelacionado = nodoPago.getElementsByTagName("pago20:DoctoRelacionado");
        ArrayList<com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado> listaDoctosRelacionados = new ArrayList<>();
        for (int i = 0; i < lnDoctoRelacionado.getLength(); i++) {
            Element nDoctoRelacionado = (Element) lnDoctoRelacionado.item(i);
            com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado pDoctoRelacionado = new com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado();
            if (nDoctoRelacionado.hasAttribute("IdDocumento")) {
                pDoctoRelacionado.setIdDocumento(nDoctoRelacionado.getAttribute("IdDocumento"));
            }
            if (nDoctoRelacionado.hasAttribute("Serie")) {
                pDoctoRelacionado.setSerie(nDoctoRelacionado.getAttribute("Serie"));
            }
            if (nDoctoRelacionado.hasAttribute("Folio")) {
                pDoctoRelacionado.setFolio(nDoctoRelacionado.getAttribute("Folio"));
            }
            if (nDoctoRelacionado.hasAttribute("MonedaDR")) {
                pDoctoRelacionado.setMonedaDR(nDoctoRelacionado.getAttribute("MonedaDR"));
            }
            if (nDoctoRelacionado.hasAttribute("EquivalenciaDR")) {
                pDoctoRelacionado
                        .setEquivalenciaDR(Double.parseDouble(nDoctoRelacionado.getAttribute("EquivalenciaDR")));
            }
            if (nDoctoRelacionado.hasAttribute("NumParcialidad")) {
                pDoctoRelacionado.setNumParcialidad(nDoctoRelacionado.getAttribute("NumParcialidad"));
            }
            if (nDoctoRelacionado.hasAttribute("ImpSaldoAnt")) {
                pDoctoRelacionado.setImpSaldoAnt(Double.parseDouble(nDoctoRelacionado.getAttribute("ImpSaldoAnt")));
            }
            if (nDoctoRelacionado.hasAttribute("ImpPagado")) {
                pDoctoRelacionado.setImpPagado(Double.parseDouble(nDoctoRelacionado.getAttribute("ImpPagado")));
            }
            if (nDoctoRelacionado.hasAttribute("ImpSaldoInsoluto")) {
                pDoctoRelacionado
                        .setImpSaldoInsoluto(Double.parseDouble(nDoctoRelacionado.getAttribute("ImpSaldoInsoluto")));
            }
            if (nDoctoRelacionado.hasAttribute("ObjetoImpDR")) {
                pDoctoRelacionado.setObjetoImpDR(nDoctoRelacionado.getAttribute("ObjetoImpDR"));
            }
            pDoctoRelacionado.setImpuestosDR(ObtenerImpuestosDR20(nDoctoRelacionado));
            listaDoctosRelacionados.add(pDoctoRelacionado);

        }
        return listaDoctosRelacionados;
    }

    private static com.itextpdf.xmltopdf.Pagos20.ImpuestosDR ObtenerImpuestosDR20(Element nodoDoctoRelacionado) {
        NodeList lnImpuestosDR = nodoDoctoRelacionado.getElementsByTagName("pago20:ImpuestosDR");
        if (lnImpuestosDR.getLength() == 0) {
            return null;
        }
        com.itextpdf.xmltopdf.Pagos20.ImpuestosDR impuestos = new com.itextpdf.xmltopdf.Pagos20.ImpuestosDR();
        Element nImpuestosDR = (Element) lnImpuestosDR.item(0);

        NodeList lPRetenciones = nImpuestosDR.getElementsByTagName("pago20:RetencionesDR");
        if (lPRetenciones.getLength() > 0) {
            com.itextpdf.xmltopdf.Pagos20.RetencionesDR retenciones = new com.itextpdf.xmltopdf.Pagos20.RetencionesDR();
            Element nPRetenciones = (Element) lPRetenciones.item(0);
            NodeList lPRetencion = nPRetenciones.getElementsByTagName("pago20:RetencionDR");
            for (int i = 0; i < lPRetenciones.getLength(); i++) {
                Element nRetencionDR = (Element) lPRetencion.item(i);
                com.itextpdf.xmltopdf.Pagos20.RetencionDR retencion = new com.itextpdf.xmltopdf.Pagos20.RetencionDR();
                if (nRetencionDR.hasAttribute("BaseDR")) {
                    retencion.setBaseDR(Double.parseDouble(nRetencionDR.getAttribute("BaseDR")));
                }
                if (nRetencionDR.hasAttribute("ImpuestoDR")) {
                    retencion.setImpuestoDR(nRetencionDR.getAttribute("ImpuestoDR"));
                }

                if (nRetencionDR.hasAttribute("TipoFactorDR")) {
                    retencion.setTipoFactorDR(nRetencionDR.getAttribute("TipoFactorDR"));
                }

                if (nRetencionDR.hasAttribute("TasaOCuotaDR")) {
                    retencion.setTasaOCuotaDR(Double.parseDouble(nRetencionDR.getAttribute("TasaOCuotaDR")));
                }

                if (nRetencionDR.hasAttribute("ImporteDR")) {
                    retencion.setImporteDR(Double.parseDouble(nRetencionDR.getAttribute("ImporteDR")));
                }

                retenciones.getRetencionDR().add(retencion);
            }
            impuestos.setRetencionesDR(retenciones);
        }
        NodeList lPTraslados = nImpuestosDR.getElementsByTagName("pago20:TrasladosDR");
        if (lPTraslados.getLength() > 0) {
            com.itextpdf.xmltopdf.Pagos20.TrasladosDR traslados = new com.itextpdf.xmltopdf.Pagos20.TrasladosDR();
            // traslados.TrasladoDR = new ArrayList<TrasladoDR>();
            Element nPTraslados = (Element) lPTraslados.item(0);
            NodeList lPTraslado = nPTraslados.getElementsByTagName("pago20:TrasladoDR");
            for (int i = 0; i < lPTraslado.getLength(); i++) {
                Element nTrasladoDR = (Element) lPTraslado.item(i);
                com.itextpdf.xmltopdf.Pagos20.TrasladoDR traslado = new com.itextpdf.xmltopdf.Pagos20.TrasladoDR();
                if (nTrasladoDR.hasAttribute("BaseDR")) {
                    traslado.setBaseDR(Double.parseDouble(nTrasladoDR.getAttribute("BaseDR")));
                }
                if (nTrasladoDR.hasAttribute("ImpuestoDR")) {
                    traslado.setImpuestoDR(nTrasladoDR.getAttribute("ImpuestoDR"));
                }
                if (nTrasladoDR.hasAttribute("TipoFactorDR")) {
                    traslado.setTipoFactorDR(nTrasladoDR.getAttribute("TipoFactorDR"));
                }
                if (nTrasladoDR.hasAttribute("TasaOCuotaDR")) {
                    traslado.setTasaOCuotaDR(Double.parseDouble(nTrasladoDR.getAttribute("TasaOCuotaDR")));
                }
                if (nTrasladoDR.hasAttribute("ImporteDR")) {
                    traslado.setImporteDR(Double.parseDouble(nTrasladoDR.getAttribute("ImporteDR")));
                }
                traslados.getTrasladoDR().add(traslado);
            }
            impuestos.setTrasladosDR(traslados);
        }
        return impuestos;
    }

    private static com.itextpdf.xmltopdf.Pagos20.ImpuestosP obtenerImpuestosPago20(Element nodoPago) {

        NodeList lnImpuestos = nodoPago.getElementsByTagName("pago20:ImpuestosP");
        if (lnImpuestos.getLength() == 0) {
            return null;
        }
        com.itextpdf.xmltopdf.Pagos20.ImpuestosP impuestos = new com.itextpdf.xmltopdf.Pagos20.ImpuestosP();
        Element nImpuestos = (Element) lnImpuestos.item(0);
        NodeList lPRetenciones = nImpuestos.getElementsByTagName("pago20:RetencionesP");
        if (lPRetenciones.getLength() > 0) {
            com.itextpdf.xmltopdf.Pagos20.RetencionesP retenciones = new com.itextpdf.xmltopdf.Pagos20.RetencionesP();
            // retenciones.RetencionP = new ArrayList<RetencionP>();
            Element nPRetenciones = (Element) lPRetenciones.item(0);
            NodeList lPRetencion = nPRetenciones.getElementsByTagName("pago20:RetencionP");
            for (int i = 0; i < lPRetencion.getLength(); i++) {
                Element nPRetencion = (Element) lPRetencion.item(i);
                com.itextpdf.xmltopdf.Pagos20.RetencionP retencion = new com.itextpdf.xmltopdf.Pagos20.RetencionP();
                if (nPRetencion.hasAttribute("ImpuestoP")) {
                    retencion.setImpuestoP(nPRetencion.getAttribute("ImpuestoP"));
                }
                if (nPRetencion.hasAttribute("ImporteP")) {
                    retencion.setImporteP(Double.parseDouble(nPRetencion.getAttribute("ImporteP")));
                }
                retenciones.getRetencionP().add(retencion);
            }
            impuestos.setRetencionesP(retenciones);
        }
        NodeList lPTraslados = nImpuestos.getElementsByTagName("pago20:TrasladosP");
        if (lPTraslados.getLength() > 0) {
            com.itextpdf.xmltopdf.Pagos20.TrasladosP traslados = new com.itextpdf.xmltopdf.Pagos20.TrasladosP();
            // traslados.TrasladoP = new ArrayList<TrasladoP>();
            Element nPTraslados = (Element) lPTraslados.item(0);
            NodeList lPTraslado = nPTraslados.getElementsByTagName("pago20:TrasladoP");
            for (int i = 0; i < lPTraslado.getLength(); i++) {
                Element nPTraslado = (Element) lPTraslado.item(i);
                com.itextpdf.xmltopdf.Pagos20.TrasladoP traslado = new com.itextpdf.xmltopdf.Pagos20.TrasladoP();
                if (nPTraslado.hasAttribute("BaseP")) {
                    traslado.setBaseP(Double.parseDouble(nPTraslado.getAttribute("BaseP")));
                }
                if (nPTraslado.hasAttribute("ImpuestoP")) {
                    traslado.setImpuestoP(nPTraslado.getAttribute("ImpuestoP"));
                }
                if (nPTraslado.hasAttribute("TipoFactorP")) {
                    traslado.setTipoFactorP(nPTraslado.getAttribute("TipoFactorP"));
                }
                if (nPTraslado.hasAttribute("TasaOCuotaP")) {
                    traslado.setTasaOCuotaP(Double.parseDouble(nPTraslado.getAttribute("TasaOCuotaP")));
                }
                if (nPTraslado.hasAttribute("ImporteP")) {
                    traslado.setImporteP(Double.parseDouble(nPTraslado.getAttribute("ImporteP")));
                }
                traslados.getTrasladoP().add(traslado);
            }
            impuestos.setTrasladosP(traslados);
        }
        return impuestos;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Complemento de pagos">
    private static Pagos ObtenerNodoPagos(Element nodoComplemento) {
        NodeList lPagos = nodoComplemento.getElementsByTagName("pago10:Pagos");
        if (lPagos.getLength() == 0) {
            return null;
        }
        Pagos pagos = new Pagos();
        pagos.setPago(new ArrayList<>());
        Element nPagos = (Element) lPagos.item(0);
        if (nPagos.hasAttribute("Version")) {
            pagos.setVersion(nPagos.getAttribute("Version"));
        }
        NodeList lPago = nPagos.getElementsByTagName("pago10:Pago");
        for (int i = 0; i < lPago.getLength(); i++) {
            Element nPago = (Element) lPago.item(i);
            Pago pago = new Pago();
            if (nPago.hasAttribute("FechaPago")) {
                pago.setFechaPago(LocalDateTime.parse(nPago.getAttribute("FechaPago")));
            }
            if (nPago.hasAttribute("FormaDePagoP")) {
                pago.setFormaDePagoP(nPago.getAttribute("FormaDePagoP"));
            }
            if (nPago.hasAttribute("MonedaP")) {
                pago.setMonedaP(nPago.getAttribute("MonedaP"));
            }
            if (nPago.hasAttribute("TipoCambioP")) {
                pago.setTipoCambioP(Double.parseDouble(nPago.getAttribute("TipoCambioP")));
            }
            if (nPago.hasAttribute("Monto")) {
                pago.setMonto(Double.parseDouble(nPago.getAttribute("Monto")));
            }
            if (nPago.hasAttribute("NumOperacion")) {
                pago.setNumOperacion(nPago.getAttribute("NumOperacion"));
            }
            if (nPago.hasAttribute("RfcEmisorCtaOrd")) {
                pago.setRfcEmisorCtaOrd(nPago.getAttribute("RfcEmisorCtaOrd"));
            }
            if (nPago.hasAttribute("NomBancoOrdExt")) {
                pago.setNomBancoOrdExt(nPago.getAttribute("NomBancoOrdExt"));
            }
            if (nPago.hasAttribute("CtaOrdenante")) {
                pago.setCtaOrdenante(nPago.getAttribute("CtaOrdenante"));
            }
            if (nPago.hasAttribute("RfcEmisorCtaBen")) {
                pago.setRfcEmisorCtaBen(nPago.getAttribute("RfcEmisorCtaBen"));
            }
            if (nPago.hasAttribute("CtaBeneficiario")) {
                pago.setCtaBeneficiario(nPago.getAttribute("CtaBeneficiario"));
            }
            if (nPago.hasAttribute("TipoCadPago")) {
                pago.setTipoCadPago(nPago.getAttribute("TipoCadPago"));
            }
            if (nPago.hasAttribute("CertPago")) {
                pago.setCertPago(nPago.getAttribute("CertPago"));
            }
            if (nPago.hasAttribute("CadPago")) {
                pago.setCadPago(nPago.getAttribute("CadPago"));
            }
            if (nPago.hasAttribute("SelloPago")) {
                pago.setSelloPago(nPago.getAttribute("SelloPago"));
            }
            pago.setDoctoRelacionado(ObtenerDoctoRelacionadoPago(nPago));
            pago.setImpuestos(obtenerImpuestosPago(nPago));
            pagos.getPago().add(pago);
        }
        return pagos;
    }

    private static ArrayList<PDoctoRelacionado> ObtenerDoctoRelacionadoPago(Element nodoPago) {
        NodeList lnDoctoRelacionado = nodoPago.getElementsByTagName("pago10:DoctoRelacionado");
        if (lnDoctoRelacionado.getLength() == 0) {
            return null;
        }
        ArrayList<PDoctoRelacionado> listaDoctosRelacionados = new ArrayList<>();
        for (int i = 0; i < lnDoctoRelacionado.getLength(); i++) {
            Element nDoctoRelacionado = (Element) lnDoctoRelacionado.item(i);
            PDoctoRelacionado pDoctoRelacionado = new PDoctoRelacionado();
            if (nDoctoRelacionado.hasAttribute("IdDocumento")) {
                pDoctoRelacionado.setIdDocumento(nDoctoRelacionado.getAttribute("IdDocumento"));
            }
            if (nDoctoRelacionado.hasAttribute("Serie")) {
                pDoctoRelacionado.setSerie(nDoctoRelacionado.getAttribute("Serie"));
            }
            if (nDoctoRelacionado.hasAttribute("Folio")) {
                pDoctoRelacionado.setFolio(nDoctoRelacionado.getAttribute("Folio"));
            }
            if (nDoctoRelacionado.hasAttribute("MonedaDR")) {
                pDoctoRelacionado.setMonedaDR(nDoctoRelacionado.getAttribute("MonedaDR"));
            }
            if (nDoctoRelacionado.hasAttribute("TipoCambioDR")) {
                pDoctoRelacionado.setTipoCambioDR(Double.parseDouble(nDoctoRelacionado.getAttribute("TipoCambioDR")));
            }
            if (nDoctoRelacionado.hasAttribute("MetodoDePagoDR")) {
                pDoctoRelacionado.setMetodoDePagoDR(nDoctoRelacionado.getAttribute("MetodoDePagoDR"));
            }
            if (nDoctoRelacionado.hasAttribute("NumParcialidad")) {
                pDoctoRelacionado.setNumParcialidad(nDoctoRelacionado.getAttribute("NumParcialidad"));
            }
            if (nDoctoRelacionado.hasAttribute("ImpSaldoAnt")) {
                pDoctoRelacionado.setImpSaldoAnt(Double.parseDouble(nDoctoRelacionado.getAttribute("ImpSaldoAnt")));
            }
            if (nDoctoRelacionado.hasAttribute("ImpPagado")) {
                pDoctoRelacionado.setImpPagado(Double.parseDouble(nDoctoRelacionado.getAttribute("ImpPagado")));
            }
            if (nDoctoRelacionado.hasAttribute("ImpSaldoInsoluto")) {
                pDoctoRelacionado
                        .setImpSaldoInsoluto(Double.parseDouble(nDoctoRelacionado.getAttribute("ImpSaldoInsoluto")));
            }
            listaDoctosRelacionados.add(pDoctoRelacionado);
        }
        return listaDoctosRelacionados;
    }

    private static ArrayList<PImpuestos> obtenerImpuestosPago(Element nodoPago) {
        PImpuestos impuestos = null;
        NodeList lnImpuestos = nodoPago.getElementsByTagName("pago10:Impuestos");
        if (lnImpuestos.getLength() == 0) {
            return null;
        }
        ArrayList<PImpuestos> listaImpuestos = new ArrayList<>();
        for (int i = 0; i < lnImpuestos.getLength(); i++) {
            impuestos = new PImpuestos();
            Element nImpuestos = (Element) (lnImpuestos.item(i));
            if (nImpuestos.hasAttribute("TotalImpuestosRetenidos")) {
                impuestos.setTotalImpuestosRetenidos(
                        Double.parseDouble(nImpuestos.getAttribute("TotalImpuestosRetenidos")));
            }
            if (nImpuestos.hasAttribute("TotalImpuestosTrasladados")) {
                impuestos.setTotalImpuestosTrasladados(
                        Double.parseDouble(nImpuestos.getAttribute("TotalImpuestosTrasladados")));
            }
            NodeList lPRetenciones = nImpuestos.getElementsByTagName("pago10:Retenciones");
            if (lPRetenciones.getLength() > 0) {
                PRetenciones retenciones = new PRetenciones();
                retenciones.setRetencion(new ArrayList<PRetencion>());
                Element nPRetenciones = (Element) lPRetenciones.item(0);

                NodeList lPRetencion = (NodeList) nPRetenciones.getElementsByTagName("pago10:Retencion");
                for (int j = 0; j < lPRetencion.getLength(); j++) {
                    Element nPRetencion = (Element) lPRetencion.item(j);
                    PRetencion retencion = new PRetencion();
                    if (nPRetencion.hasAttribute("Impuesto")) {
                        retencion.setImpuesto(nPRetencion.getAttribute("Impuesto"));
                    }
                    if (nPRetencion.hasAttribute("Importe")) {
                        retencion.setImporte(Double.parseDouble(nPRetencion.getAttribute("Importe")));
                    }
                    retenciones.getRetencion().add(retencion);
                }
                impuestos.setRetenciones(retenciones);
            }
            NodeList lPTraslados = nImpuestos.getElementsByTagName("pago10:Traslados");
            if (lPTraslados.getLength() > 0) {
                PTraslados traslados = new PTraslados();
                traslados.setTraslado(new ArrayList<>());
                Element nPTraslados = (Element) lPTraslados.item(0);

                NodeList lPTraslado = nPTraslados.getElementsByTagName("pago10:Traslado");
                for (int k = 0; k < lPTraslado.getLength(); k++) {
                    Element nPTraslado = (Element) lPTraslado.item(k);
                    PTraslado traslado = new PTraslado();
                    if (nPTraslado.hasAttribute("Impuesto")) {
                        traslado.setImpuesto(nPTraslado.getAttribute("Impuesto"));
                    }
                    if (nPTraslado.hasAttribute("TipoFactor")) {
                        traslado.setTipoFactor(nPTraslado.getAttribute("TipoFactor"));
                    }
                    if (nPTraslado.hasAttribute("TasaOCuota")) {
                        traslado.setTasaOCuota(Double.parseDouble(nPTraslado.getAttribute("TasaOCuota")));
                    }
                    if (nPTraslado.hasAttribute("Importe")) {
                        traslado.setImporte(Double.parseDouble(nPTraslado.getAttribute("Importe")));
                    }
                    traslados.getTraslado().add(traslado);
                }
                impuestos.setTraslados(traslados);
            }
            listaImpuestos.add(impuestos);
        }
        return listaImpuestos;
    }

    // </editor-fold>
    /**
     * ********************************************
     */
    /**
     * Falta implementar la clase de hidrocarburos*******
     */
    private static Addenda ObtenerNodoAdenda(Document documento) {
        NodeList lAdenda = documento.getElementsByTagName("cfdi:Addendas");
        if (lAdenda.getLength() == 0) {
            return null;
        }
        Element nAdenda = (Element) lAdenda.item(0);
        Addenda adenda = new Addenda();
        if (nAdenda.hasAttribute("Direccion1")) {
            adenda.setDireccion(nAdenda.getAttribute("Direccion1"));
        }
        return adenda;
    }
}
