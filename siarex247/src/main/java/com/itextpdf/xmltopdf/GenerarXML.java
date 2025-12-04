/// *
// * To change this license header, choose License Headers in Project
/// Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
// package com.itextpdf.xmltopdf;
//
// import org.w3c.dom.Document;
// import org.w3c.dom.Element;
// import org.w3c.dom.NodeList;
// import javax.xml.parsers.DocumentBuilder;
// import javax.xml.parsers.DocumentBuilderFactory;
//
/// **
// *
// * @author frack
// */
// public class GenerarXML {
//
// private static String NAMESPACE_CFD = "http://www.sat.gob.mx/cfd/4";
// private static String NAMESPACE_NOMINA = "http://www.sat.gob.mx/nomina12";
// private static String NAMESPACE_PAGOS = "http://www.sat.gob.mx/Pagos20";
// private static String NAMESPACE_CARTA_PORTE =
/// "http://www.sat.gob.mx/CartaPorte20";
// private static String NAMESPACE_IMPUESTOLOCAL =
/// "http://www.sat.gob.mx/implocal";
// private static String NAMESPACE_HINGRESOSHIDROCARBUROS =
/// "http://www.sat.gob.mx/IngresosHidrocarburos10";
// private static String NAMESPACE_GASTOSHIDROCARBUROS = "
/// http://www.sat.gob.mx/GastosHidrocarburos10";
// private static String SCHEMALOCATION_CFD = "http://www.sat.gob.mx/cfd/4
/// http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd";
// private static String SCHEMALOCATION_CARTA_PORTE =
/// "http://www.sat.gob.mx/CartaPorte20
/// http://www.sat.gob.mx/sitio_internet/cfd/CartaPorte/CartaPorte20.xsd";
// private static String SCHEMALOCATION_NOMINA = "http://www.sat.gob.mx/nomina12
/// http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina12.xsd";
// private static String SCHEMALOCATION_PAGOS = "http://www.sat.gob.mx/Pagos20
/// http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos20.xsd";
// private static String SCHEMALOCATION_CFD_NOMINA =
/// "http://www.sat.gob.mx/cfd/4
/// http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd
/// http://www.sat.gob.mx/nomina12
/// http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina12.xsd";
// private static String SCHEMALOCATION_IMPUESTOSLOCALES =
/// "http://www.sat.gob.mx/cfd/2
/// http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv2.xsd
/// http://www.sat.gob.mx/implocal
/// http://www.sat.gob.mx/sitio_internet/cfd/implocal/implocal.xsd";
// private static String SCHEMALOCATION_INGRESOSHIDROCARBUROS =
/// "http://www.sat.gob.mx/cfd/4
/// http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd
/// http://www.sat.gob.mx/IngresosHidrocarburos10
/// http://www.sat.gob.mx/sitio_internet/cfd/ IngresosHidrocarburos10/
/// IngresosHidrocarburos.xsd";
// private static String SCHEMALOCATION_GASTOSHIDROCARBUROS =
/// "http://www.sat.gob.mx/cfd/
/// http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd
/// http://www.sat.gob.mx/GastosHidrocarburos10
/// http://www.sat.gob.mx/sitio_internet/cfd/GastosHidrocarburos10/GastosHidrocarburos10
/// .xsd";
//
// public static void GuardarXMLPorCertificado(Comprobante c, String rutaXML,
/// String rutaCertificado, String rutaArchivoClavePrivada, String
/// passwordClavePrivada) {
// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// DocumentBuilder builder = factory.newDocumentBuilder();
// Document documento = builder.newDocument();
//
//
// //XmlDocument documento = new XmlDocument();
//
// documento.AppendChild(documento.CreateProcessingInstruction("xml",
/// "version='1.0' encoding='UTF-8'"));
// documento.AppendChild(AgregarNodoComprobante(c, documento,
/// documento.CreateElement("cfdi", "Comprobante", NAMESPACE_CFD),
/// rutaArchivoClavePrivada, passwordClavePrivada, rutaCertificado));
// XmlNodeList elementos = documento.GetElementsByTagName("cfdi:Comprobante");
// (elementos.getItemOf(0) instanceof XmlElement ? (XmlElement)
/// elementos.getItemOf(0) : null).SetAttribute("Sello",
/// ObtenerSelloPorCertificado(documento, rutaArchivoClavePrivada,
/// passwordClavePrivada));
// documento.Save(rutaXML);
//
// //XmlDocument documento = new XmlDocument();
// //documento.AppendChild(documento.CreateProcessingInstruction("xml",
/// "version='1.0' encoding='utf-8'"));
// //documento.AppendChild(AgregarNodoComprobante(c, documento,
/// documento.CreateElement("cfdi", "Comprobante", NAMESPACE_CFD), rutaPFX,
/// passwordPFX, rutaCertificado));
// //XmlNodeList elementos = documento.GetElementsByTagName("cfdi:Comprobante");
// //(elementos[0] as XmlElement).SetAttribute("Sello",
/// ObtenerSelloPorPFX(documento, rutaPFX, passwordPFX));
// }
//
// private static Element AgregarNodoComprobante(Comprobante c, XmlDocument
/// documento, Element nComprobante, String rutaPFX, String passwordPFX, String
/// rutaCertificado) {
// Element nodoComprobante = nComprobante;
// nodoComprobante.SetAttribute("Version", c.Version);
// if (!c.Serie.equals("")) {
// nodoComprobante.SetAttribute("Serie", c.Serie);
// }
// if (!c.Folio.equals("")) {
// nodoComprobante.SetAttribute("Folio", c.Folio);
// }
// nodoComprobante.SetAttribute("Fecha", c.Fecha.toString("s"));
// nodoComprobante.SetAttribute("Sello", c.Sello);
// if (!c.FormaPago.equals("")) {
// nodoComprobante.SetAttribute("FormaPago", c.FormaPago);
// }
// String certificado, NoCertificado;
// tangible.OutObject<String> tempOut_certificado = new
/// tangible.OutObject<String>();
// tangible.OutObject<String> tempOut_NoCertificado = new
/// tangible.OutObject<String>();
// ObtenerCertificadoYNoCertificado(rutaCertificado, tempOut_certificado,
/// tempOut_NoCertificado);
// NoCertificado = tempOut_NoCertificado.outArgValue;
// certificado = tempOut_certificado.outArgValue;
// nodoComprobante.SetAttribute("NoCertificado", NoCertificado);
// nodoComprobante.SetAttribute("Certificado", certificado);
// if (!c.CondicionesDePago.equals("")) {
// nodoComprobante.SetAttribute("CondicionesDePago", c.CondicionesDePago);
// }
// nodoComprobante.SetAttribute("SubTotal", String.format("%1$.2f",
/// c.SubTotal));
// if (c.Descuento > 0) {
// nodoComprobante.SetAttribute("Descuento", String.format("%1$.2f",
/// c.Descuento));
// }
// nodoComprobante.SetAttribute("Moneda", c.Moneda);
// if (c.TipoCambio > 0) {
// nodoComprobante.SetAttribute("TipoCambio", String.format("%1$.2f",
/// c.TipoCambio));
// }
// nodoComprobante.SetAttribute("Total", String.format("%1$.2f", c.Total));
// nodoComprobante.SetAttribute("TipoDeComprobante", c.TipoDeComprobante);
// if (!c.MetodoPago.equals("")) {
// nodoComprobante.SetAttribute("MetodoPago", c.MetodoPago);
// }
// nodoComprobante.SetAttribute("LugarExpedicion", c.LugarExpedicion);
// if (!c.Confirmacion.equals("")) {
// nodoComprobante.SetAttribute("Confirmacion", c.Confirmacion);
// }
// if (!c.Exportacion.equals("")) {
// nodoComprobante.SetAttribute("Exportacion", c.Exportacion);
// }
//
// XmlAttribute schemaLocation = documento.CreateAttribute("xsi",
/// "schemaLocation", "http://www.w3.org/2001/XMLSchema-instance");
//
// schemaLocation.Value = SCHEMALOCATION_CFD;
// nodoComprobante.SetAttribute("xmlns:cfdi", NAMESPACE_CFD);
//
// if (c.Complemento != null) {
// if (c.Complemento.Nomina != null) {
// nodoComprobante.SetAttribute("xmlns:nomina12", NAMESPACE_NOMINA);
// schemaLocation.Value += " " + SCHEMALOCATION_CFD_NOMINA;
// }
// if (c.Complemento.CartaPorte != null) {
// nodoComprobante.SetAttribute("xmlns:cartaporte20", NAMESPACE_CARTA_PORTE);
// schemaLocation.Value += " " + SCHEMALOCATION_CARTA_PORTE;
// }
// if (c.Complemento.Pagos != null) {
// nodoComprobante.SetAttribute("xmlns:pagos20", NAMESPACE_PAGOS);
// schemaLocation.Value += " " + SCHEMALOCATION_PAGOS;
// }
// }
// nodoComprobante.SetAttributeNode(schemaLocation);
//
// nodoComprobante.AppendChild(AgregarNodoEmisor(c.Emisor,
/// documento.CreateElement("cfdi", "Emisor", NAMESPACE_CFD)));
// nodoComprobante.AppendChild(AgregarNodoReceptor(c.Receptor,
/// documento.CreateElement("cfdi", "Receptor", NAMESPACE_CFD)));
// nodoComprobante.AppendChild(AgregarNodoConceptos(c.Conceptos, documento));
//
// Element informacionGlobal = AgregarNodoInformacionGlobal(c.InformacionGlobal,
/// documento);
// if (informacionGlobal != null) {
// nodoComprobante.AppendChild(informacionGlobal);
// }
// Element impuestos = AgregarNodoImpuestos(c.Impuestos, documento);
// if (impuestos != null) {
// nodoComprobante.AppendChild(impuestos);
// }
// Element complemento = AgregarNodoComplemento(c.Complemento, documento);
// if (complemento != null) {
// nodoComprobante.AppendChild(complemento);
// }
// return nodoComprobante;
// }
//
// private static Element AgregarNodoEmisor(Emisor emisor, Element nEmisor) {
// Element nodoEmisor = nEmisor;
// nodoEmisor.SetAttribute("Rfc", emisor.Rfc);
// if (!tangible.StringHelper.isNullOrEmpty(emisor.Nombre)) {
// nodoEmisor.SetAttribute("Nombre", emisor.Nombre);
// }
// nodoEmisor.SetAttribute("RegimenFiscal", emisor.RegimenFiscal);
// if (!tangible.StringHelper.isNullOrEmpty(emisor.FacAtrAdquirente)) {
// nodoEmisor.SetAttribute("FacAtrAdquirente", emisor.FacAtrAdquirente);
// }
// return nodoEmisor;
// }
//
// private static Element AgregarNodoReceptor(Receptor receptor, Element
/// nReceptor) {
// Element nodoReceptor = nReceptor;
// nodoReceptor.SetAttribute("Rfc", receptor.Rfc);
// if (!tangible.StringHelper.isNullOrEmpty(receptor.Nombre)) {
// nodoReceptor.SetAttribute("Nombre", receptor.Nombre);
// }
// if (!tangible.StringHelper.isNullOrEmpty(receptor.DomicilioFiscalReceptor)) {
// nodoReceptor.SetAttribute("DomicilioFiscalReceptor",
/// receptor.DomicilioFiscalReceptor);
// }
// if (!tangible.StringHelper.isNullOrEmpty(receptor.ResidenciaFiscal)) {
// nodoReceptor.SetAttribute("ResidenciaFiscal", receptor.ResidenciaFiscal);
// }
// if (!tangible.StringHelper.isNullOrEmpty(receptor.NumRegIdTrib)) {
// nodoReceptor.SetAttribute("NumRegIdTrib", receptor.NumRegIdTrib);
// }
// if (!tangible.StringHelper.isNullOrEmpty(receptor.RegimenFiscalReceptor)) {
// nodoReceptor.SetAttribute("RegimenFiscalReceptor",
/// receptor.RegimenFiscalReceptor);
// }
// if (!tangible.StringHelper.isNullOrEmpty(receptor.UsoCFDI)) {
// nodoReceptor.SetAttribute("UsoCFDI", receptor.UsoCFDI);
// }
// return nodoReceptor;
// }
//
// private static Element AgregarNodoConceptos(Conceptos conceptos, XmlDocument
/// documento) {
// if (conceptos.Concepto.Count() == 0) {
// return null;
// }
// Element nodoConceptos = documento.CreateElement("cfdi", "Conceptos",
/// NAMESPACE_CFD);
// for (Concepto concepto : conceptos.Concepto) {
// Element nodoConcepto = documento.CreateElement("cfdi", "Concepto",
/// NAMESPACE_CFD);
// if (!tangible.StringHelper.isNullOrEmpty(concepto.ClaveProdServ)) {
// nodoConcepto.SetAttribute("ClaveProdServ", concepto.ClaveProdServ);
// }
// if (!tangible.StringHelper.isNullOrEmpty(concepto.NoIdentificacion)) {
// nodoConcepto.SetAttribute("NoIdentificacion", concepto.NoIdentificacion);
// }
// nodoConcepto.SetAttribute("Cantidad", String.format("%1$.2f",
/// concepto.Cantidad));
// nodoConcepto.SetAttribute("ClaveUnidad", concepto.ClaveUnidad);
// if (!concepto.Unidad.equals("")) {
// nodoConcepto.SetAttribute("Unidad", concepto.Unidad);
// }
// nodoConcepto.SetAttribute("Descripcion", concepto.Descripcion);
// nodoConcepto.SetAttribute("ValorUnitario", String.format("%1$.6f",
/// concepto.ValorUnitario));
// nodoConcepto.SetAttribute("Importe", String.format("%1$.2f",
/// concepto.Importe));
// if (concepto.Descuento > 0) {
// nodoConcepto.SetAttribute("Descuento", String.format("%1$.2f",
/// concepto.Descuento));
// }
// if (!tangible.StringHelper.isNullOrEmpty(concepto.ObjetoImp)) {
// nodoConcepto.SetAttribute("ObjetoImp", concepto.ObjetoImp);
// }
// Element impuestos = ObtenerNodoImpuestosConcepto(concepto.Impuestos,
/// documento);
// if (impuestos != null) {
// nodoConcepto.AppendChild(impuestos);
// }
// for (var item : concepto.CuentaPredial) {
// Element cuentaPredial = ObtenerNodoCuentaPredialC(item, documento);
// if (cuentaPredial != null) {
// nodoConcepto.AppendChild(cuentaPredial);
// }
// }
//
// Element informacionAduanera =
/// ObtenerNodoInformacionAduaneraC(concepto.InformacionAduanera, documento);
// if (informacionAduanera != null) {
// nodoConcepto.AppendChild(informacionAduanera);
// }
// Element parte = ObtenerNodoParteC(concepto.Parte, documento);
// if (parte != null) {
// nodoConcepto.AppendChild(parte);
// }
// nodoConceptos.AppendChild(nodoConcepto);
// }
// return nodoConceptos;
// }
//
// private static Element ObtenerNodoImpuestosConcepto(ImpuestosC impuestos,
/// XmlDocument documento) {
// if (impuestos == null) {
// return null;
// }
// if (impuestos.Retenciones.Count == 0 && impuestos.Traslados.Count == 0) {
// return null;
// }
// Element nodoImpuestos = documento.CreateElement("cfdi", "Impuestos",
/// NAMESPACE_CFD);
// if (impuestos.Traslados != null && impuestos.Traslados.Count > 0) {
// Element nodoTraslados = documento.CreateElement("cfdi", "Traslados",
/// NAMESPACE_CFD);
// for (TrasladoC traslado : impuestos.Traslados) {
// Element nodoTraslado = documento.CreateElement("cfdi", "Traslado",
/// NAMESPACE_CFD);
// nodoTraslado.SetAttribute("Base", String.format("%1$.6f", traslado.Base));
// nodoTraslado.SetAttribute("Impuesto", traslado.Impuesto);
// nodoTraslado.SetAttribute("TipoFactor", traslado.TipoFactor);
// if (traslado.TasaOCuota > 0) {
// nodoTraslado.SetAttribute("TasaOCuota", String.format("%1$.6f",
/// traslado.TasaOCuota));
// }
// if (traslado.Importe > 0) {
// nodoTraslado.SetAttribute("Importe", String.format("%1$.2f",
/// traslado.Importe));
// }
// nodoTraslados.AppendChild(nodoTraslado);
// }
// nodoImpuestos.AppendChild(nodoTraslados);
// }
//
// if (impuestos.Retenciones != null && impuestos.Retenciones.Count > 0) {
// Element nodoRetenciones = documento.CreateElement("cfdi", "Retenciones",
/// NAMESPACE_CFD);
// for (RetencionC retencion : impuestos.Retenciones) {
// Element nodoRetencion = documento.CreateElement("cfdi", "Retencion",
/// NAMESPACE_CFD);
// nodoRetencion.SetAttribute("Base", String.format("%1$.6f", retencion.Base));
// nodoRetencion.SetAttribute("Impuesto", retencion.Impuesto);
// nodoRetencion.SetAttribute("TipoFactor", retencion.TipoFactor);
// if (retencion.TasaOCuota > 0) {
// nodoRetencion.SetAttribute("TasaOCuota", String.format("%1$.6f",
/// retencion.TasaOCuota));
// }
// if (retencion.Importe > 0) {
// nodoRetencion.SetAttribute("Importe", String.format("%1$.2f",
/// retencion.Importe));
// }
// nodoRetenciones.AppendChild(nodoRetencion);
// }
// nodoImpuestos.AppendChild(nodoRetenciones);
// }
// return nodoImpuestos;
// }
//
// private static Element AgregarNodoInformacionGlobal(InformacionGlobal
/// informacionGlobal, XmlDocument documento) {
// if (informacionGlobal == null) {
// return null;
// }
// Element nodoInformacionGlobal = documento.CreateElement("cfdi",
/// "InformacionGlobal", NAMESPACE_CFD);
// if (!tangible.StringHelper.isNullOrEmpty(informacionGlobal.Periodicidad)) {
// nodoInformacionGlobal.SetAttribute("Periodicidad",
/// informacionGlobal.Periodicidad);
// }
// if (!tangible.StringHelper.isNullOrEmpty(informacionGlobal.Meses)) {
// nodoInformacionGlobal.SetAttribute("Meses", informacionGlobal.Meses);
// }
// if (informacionGlobal.Ano > 0) {
// nodoInformacionGlobal.SetAttribute("Año", informacionGlobal.Ano.toString());
// }
// return nodoInformacionGlobal;
// }
//
// private static Element
/// ObtenerNodoInformacionAduaneraC(ArrayList<InformacionAduaneraC>
/// informacionAduanera, XmlDocument documento) {
// Element nodoInformacionAduanera = documento.CreateElement("cfdi",
/// "InformacionAduanera", NAMESPACE_CFD);
// if (informacionAduanera == null || informacionAduanera.size() == 0) {
// return null;
// }
// for (InformacionAduaneraC ia : informacionAduanera) {
// nodoInformacionAduanera.SetAttribute("NumeroPedimento", ia.NumeroPedimento);
// nodoInformacionAduanera.AppendChild(nodoInformacionAduanera);
// }
// return nodoInformacionAduanera;
// }
//
// private static Element ObtenerNodoCuentaPredialC(CuentaPredialC
/// cuentaPredial, XmlDocument documento) {
// if (cuentaPredial == null) {
// return null;
// }
// Element nodoCuentaPredialC = documento.CreateElement("cfdi", "CuentaPredial",
/// NAMESPACE_CFD);
// nodoCuentaPredialC.SetAttribute("NumeroPedimento", cuentaPredial.Numero);
// nodoCuentaPredialC.AppendChild(nodoCuentaPredialC);
// return nodoCuentaPredialC;
// }
//
// private static Element ObtenerNodoParteC(ArrayList<ParteC> parte, XmlDocument
/// documento) {
// if (parte == null || parte.size() == 0) {
// return null;
// }
// Element nodoParte = documento.CreateElement("cfdi", "Parte", NAMESPACE_CFD);
// for (ParteC p : parte) {
// nodoParte.SetAttribute("ClaveProdServ", p.ClaveProdServ);
// if (!p.NoIdentificacion.equals("")) {
// nodoParte.SetAttribute("NoIdentificacion", p.NoIdentificacion);
// }
// nodoParte.SetAttribute("Cantidad", String.format("%1$.1f", p.Cantidad));
// if (!p.Unidad.equals("")) {
// nodoParte.SetAttribute("Unidad", p.Unidad);
// }
// nodoParte.SetAttribute("Descripcion", p.Descripcion);
// nodoParte.SetAttribute("ValorUnitario", String.format("%1$.2f",
/// p.ValorUnitario));
// if (p.Importe > 0) {
// nodoParte.SetAttribute("Importe", String.format("%1$.2f", p.Importe));
// }
// nodoParte.AppendChild(ObtenerNodoInformacionAduaneraC(p.InformacionAduanera,
/// documento));
// }
// return nodoParte;
// }
//
// private static Element AgregarNodoImpuestos(Impuestos impuestos, XmlDocument
/// documento) {
// if (impuestos == null) {
// return null;
// }
// if (impuestos.Retenciones.Count == 0 && impuestos.Traslados.Count == 0) {
// return null;
// }
// Element nodoImpuestos = documento.CreateElement("cfdi", "Impuestos",
/// NAMESPACE_CFD);
// if (impuestos.TotalImpuestosRetenidos != -1) {
// nodoImpuestos.SetAttribute("TotalImpuestosRetenidos", String.format("%1$.2f",
/// impuestos.TotalImpuestosRetenidos));
// }
// if (impuestos.TotalImpuestosTrasladados != -1) {
// nodoImpuestos.SetAttribute("TotalImpuestosTrasladados",
/// String.format("%1$.2f", impuestos.TotalImpuestosTrasladados));
// }
// if (impuestos.Traslados != null && impuestos.Traslados.Count > 0) {
// Element nodoTraslados = documento.CreateElement("cfdi", "Traslados",
/// NAMESPACE_CFD);
// for (Traslado traslado : impuestos.Traslados) {
// Element nodoTraslado = documento.CreateElement("cfdi", "Traslado",
/// NAMESPACE_CFD);
// nodoTraslado.SetAttribute("Base", String.format("%1$.2f", traslado.Base));
// nodoTraslado.SetAttribute("Impuesto", traslado.Impuesto);
// nodoTraslado.SetAttribute("TipoFactor", traslado.TipoFactor);
// if (traslado.TasaOCuota > 0) {
// nodoTraslado.SetAttribute("TasaOCuota", String.format("%1$.6f",
/// traslado.TasaOCuota));
// }
// if (traslado.Importe > 0) {
// nodoTraslado.SetAttribute("Importe", String.format("%1$.2f",
/// traslado.Importe));
// }
// nodoTraslados.AppendChild(nodoTraslado);
// }
// nodoImpuestos.AppendChild(nodoTraslados);
// }
// if (impuestos.Retenciones != null && impuestos.Retenciones.Count > 0) {
// Element nodoRetenciones = documento.CreateElement("cfdi", "Retenciones",
/// NAMESPACE_CFD);
// for (Retencion retencion : impuestos.Retenciones) {
// Element nodoRetencion = documento.CreateElement("cfdi", "Retencion",
/// NAMESPACE_CFD);
// nodoRetencion.SetAttribute("Impuesto", retencion.Impuesto);
// if (retencion.Importe > 0) {
// nodoRetencion.SetAttribute("Importe", String.format("%1$.2f",
/// retencion.Importe));
// }
// nodoRetenciones.AppendChild(nodoRetencion);
// }
// nodoImpuestos.AppendChild(nodoRetenciones);
// }
// return nodoImpuestos;
// }
//
// private static Element AgregarNodoComplemento(Complemento complemento,
/// XmlDocument documento) {
// if (complemento == null) {
// return null;
// }
// if (complemento.Nomina == null && complemento.Pagos == null &&
/// complemento.TimbreFiscalDigital == null && complemento.CartaPorte == null) {
// return null;
// }
// Element nodoComplemento = documento.CreateElement("cfdi", "Complemento",
/// NAMESPACE_CFD);
// Element nomina = AgregarNodoNominaComplemento(complemento.Nomina, documento);
// if (nomina != null) {
// nodoComplemento.AppendChild(nomina);
// }
// Element pagos = AgregarNodoPagos(complemento.Pagos, documento);
// if (pagos != null) {
// nodoComplemento.AppendChild(pagos);
// }
// Element cartaPorte20 = GenerarElement.ObtenerNodo(complemento.CartaPorte,
/// documento);
// if (cartaPorte20 != null) {
// nodoComplemento.AppendChild(cartaPorte20);
// }
// return nodoComplemento;
// }
//
// private static Element AgregarNodoNominaComplemento(Nomina nomina,
/// XmlDocument documento) {
// if (nomina == null) {
// return null;
// }
// Element nodoNomina = documento.CreateElement("nomina12", "Nomina",
/// NAMESPACE_NOMINA);
// nodoNomina.SetAttribute("Version", nomina.Version);
// nodoNomina.SetAttribute("TipoNomina", nomina.TipoNomina);
// nodoNomina.SetAttribute("FechaPago",
/// nomina.FechaPago.toString("yyyy-MM-dd"));
// nodoNomina.SetAttribute("FechaInicialPago",
/// nomina.FechaInicialPago.toString("yyyy-MM-dd"));
// nodoNomina.SetAttribute("FechaFinalPago",
/// nomina.FechaFinalPago.toString("yyyy-MM-dd"));
// if (nomina.NumDiasPagados > 0) {
// nodoNomina.SetAttribute("NumDiasPagados", String.format("%1$.3f",
/// nomina.NumDiasPagados));
// }
// if (nomina.TotalPercepciones > 0) {
// nodoNomina.SetAttribute("TotalPercepciones", String.format("%1$.2f",
/// nomina.TotalPercepciones));
// }
// if (nomina.TotalDeducciones > 0) {
// nodoNomina.SetAttribute("TotalDeducciones", String.format("%1$.2f",
/// nomina.TotalDeducciones));
// }
// if (nomina.TotalOtrosPagos > 0) {
// nodoNomina.SetAttribute("TotalOtrosPagos", String.format("%1$.2f",
/// nomina.TotalOtrosPagos));
// }
// Element emisor = AgregarNodoNEmisor(nomina.Emisor, documento);
// if (emisor != null) {
// nodoNomina.AppendChild(emisor);
// }
// Element receptor = AgregarNodoNReceptor(nomina.Receptor, documento);
// if (receptor != null) {
// nodoNomina.AppendChild(receptor);
// }
// Element percepciones = AgregarNodoNPercepciones(nomina.Percepciones,
/// documento);
// if (percepciones != null) {
// nodoNomina.AppendChild(percepciones);
// }
// Element deducciones = AgregarNodoNDeducciones(nomina.Deducciones, documento);
// if (deducciones != null) {
// nodoNomina.AppendChild(deducciones);
// }
// Element otrosPagos = AgregarNodoNOtrosPagos(nomina.OtrosPagos, documento);
// if (otrosPagos != null) {
// nodoNomina.AppendChild(otrosPagos);
// }
// Element incapacidades = AgregarNodoNIncapacidades(nomina.Incapacidades,
/// documento);
// if (incapacidades != null) {
// nodoNomina.AppendChild(otrosPagos);
// }
// XmlAttribute schemaLocation = documento.CreateAttribute("xsi",
/// "schemaLocation", "http://www.w3.org/2001/XMLSchema-instance");
// schemaLocation.Value = SCHEMALOCATION_NOMINA;
// nodoNomina.SetAttributeNode(schemaLocation);
// return nodoNomina;
// }
//
// private static Element AgregarNodoNEmisor(NEmisor emisor, XmlDocument
/// documento) {
// if (emisor == null) {
// return null;
// }
// Element nodoNEmisor = documento.CreateElement("nomina12", "Emisor",
/// NAMESPACE_NOMINA);
// if (!emisor.Curp.equals("")) {
// nodoNEmisor.SetAttribute("Curp", emisor.Curp);
// }
// if (!emisor.RegistroPatronal.equals("")) {
// nodoNEmisor.SetAttribute("RegistroPatronal", emisor.RegistroPatronal);
// }
// if (!emisor.RfcPatronOrigen.equals("")) {
// nodoNEmisor.SetAttribute("RfcPatronOrigen", emisor.RfcPatronOrigen);
// }
// Element entidadSNCF = ObtenerNodoNEntidadSNCF(emisor.EntidadSNCF, documento);
// if (entidadSNCF != null) {
// nodoNEmisor.AppendChild(entidadSNCF);
// }
// return nodoNEmisor;
// }
//
// private static Element ObtenerNodoNEntidadSNCF(NEntidadSNCF entidadSNCF,
/// XmlDocument documento) {
// if (entidadSNCF == null) {
// return null;
// }
// Element nodoNEntidadSNCF = documento.CreateElement("Nomina12", "EntidadSNCF",
/// NAMESPACE_NOMINA);
// nodoNEntidadSNCF.SetAttribute("OrigenRecurso", entidadSNCF.OrigenRecurso);
// if (entidadSNCF.MontoRecursoPropio > 0) {
// nodoNEntidadSNCF.SetAttribute("MontoRecursoPropio", String.format("%1$.6f",
/// entidadSNCF.MontoRecursoPropio));
// }
// return nodoNEntidadSNCF;
// }
//
// private static Element AgregarNodoNReceptor(NReceptor receptor, XmlDocument
/// documento) {
// if (receptor == null) {
// return null;
// }
// Element nodoNReceptor = documento.CreateElement("nomina12", "Receptor",
/// NAMESPACE_NOMINA);
// nodoNReceptor.SetAttribute("Curp", receptor.Curp);
// if (!receptor.NumSeguridadSocial.equals("")) {
// nodoNReceptor.SetAttribute("NumSeguridadSocial",
/// receptor.NumSeguridadSocial);
// }
// if (!LocalDateTime.now().equals(receptor.FechaInicioRelLaboral)) {
// nodoNReceptor.SetAttribute("FechaInicioRelLaboral",
/// receptor.FechaInicioRelLaboral.toString("yyyy-MM-dd"));
// }
// if (!receptor.Antiguedad.equals("")) {
// nodoNReceptor.SetAttribute("Antigüedad", receptor.Antiguedad);
// }
// nodoNReceptor.SetAttribute("TipoContrato", receptor.TipoContrato);
// if (!receptor.Sindicalizado.equals("")) {
// nodoNReceptor.SetAttribute("Sindicalizado", receptor.Sindicalizado);
// }
// if (!receptor.TipoJornada.equals("")) {
// nodoNReceptor.SetAttribute("TipoJornada", receptor.TipoJornada);
// }
// nodoNReceptor.SetAttribute("TipoRegimen", receptor.TipoRegimen);
// nodoNReceptor.SetAttribute("NumEmpleado", receptor.NumEmpleado);
// if (!receptor.Departamento.equals("")) {
// nodoNReceptor.SetAttribute("Departamento", receptor.Departamento);
// }
// if (!receptor.Puesto.equals("")) {
// nodoNReceptor.SetAttribute("Puesto", receptor.Puesto);
// }
// if (!receptor.RiesgoPuesto.equals("")) {
// nodoNReceptor.SetAttribute("RiesgoPuesto", receptor.RiesgoPuesto);
// }
// nodoNReceptor.SetAttribute("PeridiocidadPago", receptor.PeriodicidadPago);
// if (!receptor.Banco.equals("")) {
// nodoNReceptor.SetAttribute("Banco", receptor.Banco);
// }
// if (!receptor.CuentaBancaria.equals("")) {
// nodoNReceptor.SetAttribute("CuentaBancaria", receptor.CuentaBancaria);
// }
// if (receptor.SalarioBaseCotApor > 0) {
// nodoNReceptor.SetAttribute("SalarioBaseCotApor", String.format("%1$.2f",
/// receptor.SalarioBaseCotApor));
// }
// if (receptor.SalarioDiarioIntegrado > 0) {
// nodoNReceptor.SetAttribute("SalarioDiarioIntegrado", String.format("%1$.2f",
/// receptor.SalarioDiarioIntegrado));
// }
// nodoNReceptor.SetAttribute("ClaveEntFed", receptor.ClaveEntFed);
// Element subContratacion =
/// ObtenerNodoNSubContratacion(receptor.SubContratacion, documento);
// if (subContratacion != null) {
// nodoNReceptor.AppendChild(subContratacion);
// }
// return nodoNReceptor;
// }
//
// private static Element
/// ObtenerNodoNSubContratacion(ArrayList<NSubContratacion> subContratacion,
/// XmlDocument documento) {
// if (subContratacion == null || subContratacion.size() == 0) {
// return null;
// }
// Element nodoNSubContratacion = documento.CreateElement("nomina12",
/// "SubContratacion", NAMESPACE_NOMINA);
// for (NSubContratacion sc : subContratacion) {
// nodoNSubContratacion.SetAttribute("RfcLabora", sc.RfcLabora);
// if (sc.PorcentajeTiempo > 0) {
// nodoNSubContratacion.SetAttribute("PorcentajeTiempo", String.format("%1$.6f",
/// sc.PorcentajeTiempo));
// }
// }
// return nodoNSubContratacion;
// }
//
// private static Element AgregarNodoNPercepciones(NPercepciones percepciones,
/// XmlDocument documento) {
// if (percepciones == null) {
// return null;
// }
// Element nodoNPercepciones = documento.CreateElement("nomina12",
/// "Percepciones", NAMESPACE_NOMINA);
// if (percepciones.TotalSueldos > 0) {
// nodoNPercepciones.SetAttribute("TotalSueldos", String.format("%1$.2f",
/// percepciones.TotalSueldos));
// }
// if (percepciones.TotalSeparacionIndemnizacion > 0) {
// nodoNPercepciones.SetAttribute("TotalSeparacionIndemnizacion",
/// String.format("%1$.2f", percepciones.TotalSeparacionIndemnizacion));
// }
// if (percepciones.TotalJubilacionPensionRetiro > 0) {
// nodoNPercepciones.SetAttribute("TotalJubilacionPensionRetiro",
/// String.format("%1$.2f", percepciones.TotalJubilacionPensionRetiro));
// }
// nodoNPercepciones.SetAttribute("TotalGravado", String.format("%1$.2f",
/// percepciones.TotalGravado));
// nodoNPercepciones.SetAttribute("TotalExento", String.format("%1$.2f",
/// percepciones.TotalExento));
//
// if (percepciones.Percepcion != null && percepciones.Percepcion.Count() > 0) {
// for (NPercepcion p : percepciones.Percepcion) {
// Element nodoNPercepcion = documento.CreateElement("nomina12", "Percepcion",
/// NAMESPACE_NOMINA);
// nodoNPercepcion.SetAttribute("TipoPercepcion", p.TipoPercepcion);
// nodoNPercepcion.SetAttribute("Clave", p.Clave);
// nodoNPercepcion.SetAttribute("Concepto", p.Concepto);
// nodoNPercepcion.SetAttribute("ImporteGravado", String.format("%1$.2f",
/// p.ImporteGravado));
// nodoNPercepcion.SetAttribute("ImporteExento", String.format("%1$.2f",
/// p.ImporteExento));
// Element accionesOTitulos = ObtenerNodoNAccionesOTitulos(p.AccionesOTitulos,
/// documento);
// if (accionesOTitulos != null) {
// nodoNPercepcion.AppendChild(accionesOTitulos);
// }
//
// Element horasExtra = ObtenerNodoNHorasExtras(p.HorasExtras, documento);
// if (horasExtra != null) {
// nodoNPercepcion.AppendChild(horasExtra);
// }
// nodoNPercepciones.AppendChild(nodoNPercepcion);
// }
// }
// Element jubilacionPensionRetiro =
/// ObtenerNodoNJubilacionPensionRetiro(percepciones.JubilacionPensionRetiro,
/// documento);
// if (jubilacionPensionRetiro != null) {
// nodoNPercepciones.AppendChild(jubilacionPensionRetiro);
// }
// Element separacionIndemizacion =
/// ObtenerNodoNSeparacionIndemnizacion(percepciones.SeparacionIndeminzacion,
/// documento);
// if (separacionIndemizacion != null) {
// nodoNPercepciones.AppendChild(separacionIndemizacion);
// }
// return nodoNPercepciones;
// }
//
// private static Element ObtenerNodoNAccionesOTitulos(NAccionesOTitulos
/// accionesOTitulos, XmlDocument documento) {
// if (accionesOTitulos == null) {
// return null;
// }
// Element nodoNAccionesOTitulos = documento.CreateElement("nomina12",
/// "AccionesOTitulos", NAMESPACE_NOMINA);
// nodoNAccionesOTitulos.SetAttribute("ValorMercado",
/// accionesOTitulos.ValorMercado);
// nodoNAccionesOTitulos.SetAttribute("PrecioAlOtorgarse",
/// String.format("%1$.6f", accionesOTitulos.PrecioAlOtorgarse));
// return nodoNAccionesOTitulos;
// }
//
// private static Element ObtenerNodoNHorasExtras(ArrayList<NHorasExtra>
/// horasExtra, XmlDocument documento) {
// if (horasExtra == null || horasExtra.size() == 0) {
// return null;
// }
// Element nodoNHorasExtra = documento.CreateElement("nomina12", "HoraExtra",
/// NAMESPACE_NOMINA);
// for (NHorasExtra he : horasExtra) {
//
// nodoNHorasExtra.SetAttribute("Dias", String.format("%1$.2f", he.Dias));
// nodoNHorasExtra.SetAttribute("TipoHoras", String.format("%1$.2f",
/// he.TipoHoras));
// nodoNHorasExtra.SetAttribute("HorasExtra", String.format("%1$.2f",
/// he.HorasExtra));
// nodoNHorasExtra.SetAttribute("ImportePagado", String.format("%1$.2f",
/// he.ImportePagado));
// }
// return nodoNHorasExtra;
// }
//
// private static Element
/// ObtenerNodoNJubilacionPensionRetiro(NJubilacionPensionRetiro
/// jubilacionPensionRetiro, XmlDocument documento) {
// if (jubilacionPensionRetiro == null) {
// return null;
// }
// Element nodoNJubilacionPesionRetiro = documento.CreateElement("nomina12",
/// "JubulacionesPensionRetiro", NAMESPACE_NOMINA);
// if (jubilacionPensionRetiro.TotalUnaExhibicion > 0) {
// nodoNJubilacionPesionRetiro.SetAttribute("TotalUnaExhibicion",
/// String.format("%1$.2f", jubilacionPensionRetiro.TotalUnaExhibicion));
// }
// if (jubilacionPensionRetiro.TotalParcialidad > 0) {
// nodoNJubilacionPesionRetiro.SetAttribute("TotalParcialidad",
/// String.format("%1$.2f", jubilacionPensionRetiro.TotalParcialidad));
// }
// if (jubilacionPensionRetiro.MontoDiario > 0) {
// nodoNJubilacionPesionRetiro.SetAttribute("MontoDiario",
/// String.format("%1$.2f", jubilacionPensionRetiro.MontoDiario));
// }
// nodoNJubilacionPesionRetiro.SetAttribute("IngresoAcumulable",
/// String.format("%1$.2f", jubilacionPensionRetiro.IngresoAcumulable));
// nodoNJubilacionPesionRetiro.SetAttribute("IngresoNoAcumulable",
/// String.format("%1$.2f", jubilacionPensionRetiro.IngresoNoAcumulable));
// return nodoNJubilacionPesionRetiro;
// }
//
// private static Element
/// ObtenerNodoNSeparacionIndemnizacion(NSeparacionIndemnizacion
/// separacionIndemnizacion, XmlDocument documento) {
// if (separacionIndemnizacion == null) {
// return null;
// }
// Element nodoNSeparacionIndemnizacion = documento.CreateElement("nomina12",
/// "SeparacionIndemnizacion", NAMESPACE_NOMINA);
// nodoNSeparacionIndemnizacion.SetAttribute("SeparacionIndemnizacion",
/// String.format("%1$.2f", separacionIndemnizacion.TotalPagado));
// nodoNSeparacionIndemnizacion.SetAttribute("NumAnosServicio",
/// String.format("%1$.2f", separacionIndemnizacion.NumAnosServicio));
// nodoNSeparacionIndemnizacion.SetAttribute("UltimoSueldoMensOrd",
/// String.format("%1$.2f", separacionIndemnizacion.UltimoSueldoMensOrd));
// nodoNSeparacionIndemnizacion.SetAttribute("IngresoAcumulado",
/// String.format("%1$.2f", separacionIndemnizacion.IngresoAcumulable));
// nodoNSeparacionIndemnizacion.SetAttribute("IngresoNoAcumulable",
/// String.format("%1$.2f", separacionIndemnizacion.IngresoNoAcumulable));
// return nodoNSeparacionIndemnizacion;
// }
//
// private static Element AgregarNodoNDeducciones(NDeducciones deducciones,
/// XmlDocument documento) {
// if (deducciones == null) {
// return null;
// }
// Element nodoNDeducciones = documento.CreateElement("nomina12:Deducciones",
/// NAMESPACE_NOMINA);
// if (deducciones.TotalOtrasDeducciones > 0) {
// nodoNDeducciones.SetAttribute("TotalOtrasDeducciones",
/// String.format("%1$.2f", deducciones.TotalOtrasDeducciones));
// }
// if (deducciones.TotalImpuestosRetenidos > 0) {
// nodoNDeducciones.SetAttribute("TotalImpuestosRetenidos",
/// String.format("%1$.2f", deducciones.TotalImpuestosRetenidos));
// }
// if (deducciones.Deduccion != null && deducciones.Deduccion.Count() > 0) {
// for (NDeduccion d : deducciones.Deduccion) {
// Element nodoDeduccion = documento.CreateElement("nomina12", "Deduccion",
/// NAMESPACE_NOMINA);
// nodoDeduccion.SetAttribute("TipoDeduccion", d.TipoDeduccion);
// nodoDeduccion.SetAttribute("Clave", d.Clave);
// nodoDeduccion.SetAttribute("Concepto", d.Concepto);
// nodoDeduccion.SetAttribute("Importe", String.format("%1$.2f", d.Importe));
// nodoNDeducciones.AppendChild(nodoDeduccion);
// }
// }
// return nodoNDeducciones;
// }
//
// private static Element AgregarNodoNOtrosPagos(NOtrosPagos otrosPagos,
/// XmlDocument documento) {
// if (otrosPagos == null) {
// return null;
// }
// Element nodoNOtrosPagos = documento.CreateElement("nomina12", "OtrosPagos",
/// NAMESPACE_NOMINA);
// for (NOtroPago op : otrosPagos.OtroPago) {
// Element nodoNOtroPago = documento.CreateElement("nomina12", "OtroPago",
/// NAMESPACE_NOMINA);
// nodoNOtroPago.SetAttribute("TipoOtroPago", op.TipoOtroPago);
// nodoNOtroPago.SetAttribute("Clave", op.Clave);
// nodoNOtroPago.SetAttribute("Concepto", op.Concepto);
// nodoNOtroPago.SetAttribute("Importe", String.format("%1$.2f", op.Importe));
// Element nsubsidioAlEmpleo = ObtenerNodoNSubsidioAlEmpleo(op.SubsidioAlEmpleo,
/// documento);
// if (nsubsidioAlEmpleo != null) {
// nodoNOtroPago.AppendChild(nsubsidioAlEmpleo);
// }
// Element ncompensacionSaldosAFavor =
/// ObtenerNodoNCompensacionSaldosAFavor(op.CompensacionSaldosAFavor,
/// documento);
// if (ncompensacionSaldosAFavor != null) {
// nodoNOtroPago.AppendChild(ncompensacionSaldosAFavor);
// }
// nodoNOtrosPagos.AppendChild(nodoNOtroPago);
// }
// return nodoNOtrosPagos;
// }
//
// private static Element ObtenerNodoNSubsidioAlEmpleo(NSubsidioAlEmpleo
/// nSubsidioAlEmpleo, XmlDocument documento) {
// if (nSubsidioAlEmpleo == null) {
// return null;
// }
// Element nodoNSubsidioAlEmpleo = documento.CreateElement("nomina12",
/// "SubsidioAlEmpleo", NAMESPACE_NOMINA);
// nodoNSubsidioAlEmpleo.SetAttribute("SubsidioCausado", String.format("%1$.2f",
/// nSubsidioAlEmpleo.SubsidioCausado));
// return nodoNSubsidioAlEmpleo;
// }
//
// private static Element
/// ObtenerNodoNCompensacionSaldosAFavor(NCompensacionSaldosAFavor
/// ncompensacionSaldosAFavor, XmlDocument documento) {
// if (ncompensacionSaldosAFavor == null) {
// return null;
// }
// Element nodoNCompensacionSaldosAFavor = documento.CreateElement("nomina12",
/// "CompensacionSaldosAFavor", NAMESPACE_NOMINA);
// nodoNCompensacionSaldosAFavor.SetAttribute("SaldoAFavor",
/// String.format("%1$.2f", ncompensacionSaldosAFavor.SaldoAFavor));
// nodoNCompensacionSaldosAFavor.SetAttribute("Año",
/// ncompensacionSaldosAFavor.Ano);
// nodoNCompensacionSaldosAFavor.SetAttribute("RemanenteSalFav",
/// String.format("%1$.2f", ncompensacionSaldosAFavor.RemanenteSalFav));
// return nodoNCompensacionSaldosAFavor;
// }
//
// private static Element AgregarNodoNIncapacidades(NIncapacidades
/// incapacidades, XmlDocument documento) {
// if (incapacidades == null) {
// return null;
// }
// Element nodoNIncapacidades = documento.CreateElement("nomina12",
/// "Incapacidades", NAMESPACE_NOMINA);
// for (NIncapacidad i : incapacidades.Incapacidad) {
// Element nodoNIncapacidad = documento.CreateElement("nomina12", "Incapacidad",
/// NAMESPACE_NOMINA);
// nodoNIncapacidad.SetAttribute("DiasIncapacidad", String.format("%1$.2f",
/// i.DiasIncapacidad));
// nodoNIncapacidad.SetAttribute("TipoIncapacidad", i.TipoIncapacidad);
// nodoNIncapacidad.SetAttribute("ImporteMonetario", String.format("%1$.2f",
/// i.ImporteMonetario));
// nodoNIncapacidad.AppendChild(nodoNIncapacidad);
// }
// return nodoNIncapacidades;
// }
//
// private static Element AgregarNodoPagos(Pagos pagos, XmlDocument documento) {
// if (pagos == null) {
// return null;
// }
// Element nodoPagos = documento.CreateElement("pago20", "Pagos",
/// NAMESPACE_PAGOS);
// nodoPagos.SetAttribute("Version", pagos.Version);
// nodoPagos.AppendChild(ObteneNodoPTotales(pagos.Totales, documento));
// if (pagos.Pago != null && pagos.Pago.Count() > 0) {
// for (Pago pg : pagos.Pago) {
// Element nodoPago = documento.CreateElement("pago20", "Pago",
/// NAMESPACE_PAGOS);
// nodoPago.SetAttribute("FechaPago", pg.FechaPago.toString("s"));
// nodoPago.SetAttribute("FormaDePagoP", pg.FormaDePagoP);
// nodoPago.SetAttribute("MonedaP", pg.MonedaP);
// if (pg.TipoCambioP > 0) {
// nodoPago.SetAttribute("TipoCambioP", String.format("%1$.2f",
/// pg.TipoCambioP));
// }
// nodoPago.SetAttribute("Monto", String.format("%1$.2f", pg.Monto));
// if (!tangible.StringHelper.isNullOrEmpty(pg.NumOperacion)) {
// nodoPago.SetAttribute("NumOperacion", pg.NumOperacion);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.RfcEmisorCtaOrd)) {
// nodoPago.SetAttribute("RfcEmisorCtaOrd", pg.RfcEmisorCtaOrd);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.NomBancoOrdExt)) {
// nodoPago.SetAttribute("NomBancoOrdExt", pg.NomBancoOrdExt);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.CtaOrdenante)) {
// nodoPago.SetAttribute("CtaOrdenante", pg.CtaOrdenante);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.RfcEmisorCtaBen)) {
// nodoPago.SetAttribute("RfcEmisorCtaBen", pg.RfcEmisorCtaBen);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.CtaBeneficiario)) {
// nodoPago.SetAttribute("CtaBeneficiario", pg.CtaBeneficiario);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.TipoCadPago)) {
// nodoPago.SetAttribute("TipoCadPago", pg.TipoCadPago);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.CertPago)) {
// nodoPago.SetAttribute("CertPago", pg.CertPago);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.CadPago)) {
// nodoPago.SetAttribute("CadPago", pg.CadPago);
// }
// if (!tangible.StringHelper.isNullOrEmpty(pg.SelloPago)) {
// nodoPago.SetAttribute("SelloPago", pg.SelloPago);
// }
// Element doctoRelacionado = ObtenerNodoPDoctoRelacionado(pg.DoctoRelacionado,
/// documento);
// if (doctoRelacionado != null) {
// nodoPago.AppendChild(doctoRelacionado);
// }
// Element pImpuestos = ObtenerNodoImpuestosP(pg.Impuestos, documento);
// if (pImpuestos != null) {
// nodoPago.AppendChild(pImpuestos);
// }
// nodoPagos.AppendChild(nodoPago);
// }
// }
//
// return nodoPagos;
// }
//
// private static Element ObteneNodoPTotales(PTotales totales, XmlDocument
/// documento) {
// Element nodoTotales = documento.CreateElement("pago20", "Totales",
/// NAMESPACE_PAGOS);
// if (totales.MontoTotalPagos > 0) {
// nodoTotales.SetAttribute("MontoTotalPagos", String.format("%1$.2f",
/// totales.MontoTotalPagos));
// }
// if (totales.TotalRetencionesIEPS > 0) {
// nodoTotales.SetAttribute("TotalRetencionesIEPS", String.format("%1$.2f",
/// totales.TotalRetencionesIEPS));
// }
// if (totales.TotalRetencionesISR > 0) {
// nodoTotales.SetAttribute("TotalRetencionesISR", String.format("%1$.2f",
/// totales.TotalRetencionesISR));
// }
// if (totales.TotalRetencionesIVA > 0) {
// nodoTotales.SetAttribute("TotalRetencionesIVA", String.format("%1$.2f",
/// totales.TotalRetencionesIVA));
// }
// if (totales.TotalTrasladosBaseIVA0 > 0) {
// nodoTotales.SetAttribute("TotalTrasladosBaseIVA0", String.format("%1$.2f",
/// totales.TotalTrasladosBaseIVA0));
// }
// if (totales.TotalTrasladosBaseIVA16 > 0) {
// nodoTotales.SetAttribute("TotalTrasladosBaseIVA16", String.format("%1$.2f",
/// totales.TotalTrasladosBaseIVA16));
// }
// if (totales.TotalTrasladosBaseIVA8 > 0) {
// nodoTotales.SetAttribute("TotalTrasladosBaseIVA8", String.format("%1$.2f",
/// totales.TotalTrasladosBaseIVA8));
// }
// if (totales.TotalTrasladosBaseIVAExento > 0) {
// nodoTotales.SetAttribute("TotalTrasladosBaseIVAExento",
/// String.format("%1$.2f", totales.TotalTrasladosBaseIVAExento));
// }
// if (totales.TotalTrasladosImpuestoIVA0 > 0) {
// nodoTotales.SetAttribute("TotalTrasladosImpuestoIVA0",
/// String.format("%1$.2f", totales.TotalTrasladosImpuestoIVA0));
// }
// if (totales.TotalTrasladosImpuestoIVA16 > 0) {
// nodoTotales.SetAttribute("TotalTrasladosImpuestoIVA16",
/// String.format("%1$.2f", totales.TotalTrasladosImpuestoIVA16));
// }
// if (totales.TotalTrasladosImpuestoIVA8 > 0) {
// nodoTotales.SetAttribute("TotalTrasladosImpuestoIVA8",
/// String.format("%1$.2f", totales.TotalTrasladosImpuestoIVA8));
// }
//
// return nodoTotales;
// }
//
// private static Element
/// ObtenerNodoPDoctoRelacionado(ArrayList<PDoctoRelacionado> doctoRelacionado,
/// XmlDocument documento) {
// if (doctoRelacionado == null && doctoRelacionado.size() == 0) {
// return null;
// }
// Element nodoPDoctoRelacionado = documento.CreateElement("pago20",
/// "DoctoRelacionado", NAMESPACE_PAGOS);
// for (PDoctoRelacionado dr : doctoRelacionado) {
// nodoPDoctoRelacionado.SetAttribute("IdDocumento", dr.IdDocumento);
// if (!dr.Serie.equals("")) {
// nodoPDoctoRelacionado.SetAttribute("Serie", dr.Serie);
// }
// if (!dr.Folio.equals("")) {
// nodoPDoctoRelacionado.SetAttribute("Folio", dr.Folio);
// }
// nodoPDoctoRelacionado.SetAttribute("MonedaDR", dr.MonedaDR);
// if (dr.EquivalenciaDR > 0) {
// nodoPDoctoRelacionado.SetAttribute("EquivalenciaDR", String.format("%1$.2f",
/// dr.EquivalenciaDR));
// }
// if (!dr.NumParcialidad.equals("")) {
// nodoPDoctoRelacionado.SetAttribute("NumParcialidad", dr.NumParcialidad);
// }
// if (dr.ImpSaldoAnt > 0) {
// nodoPDoctoRelacionado.SetAttribute("ImpSaldoAnt", String.format("%1$.2f",
/// dr.ImpSaldoAnt));
// }
// if (dr.ImpPagado > 0) {
// nodoPDoctoRelacionado.SetAttribute("ImpPagado", String.format("%1$.2f",
/// dr.ImpPagado));
// }
// if (dr.ImpSaldoInsoluto > 0 || dr.ImpSaldoInsoluto == 0) {
// nodoPDoctoRelacionado.SetAttribute("ImpSaldoInsoluto",
/// String.format("%1$.2f", dr.ImpSaldoInsoluto));
// }
// if (!tangible.StringHelper.isNullOrEmpty(dr.ObjetoImpDR)) {
// nodoPDoctoRelacionado.SetAttribute("ObjetoImpDR", dr.ObjetoImpDR);
// }
// Element impuestosDR = ObtenerNodoImpuestosDR(dr.ImpuestosDR, documento);
// if (impuestosDR != null) {
// nodoPDoctoRelacionado.AppendChild(impuestosDR);
// }
// }
// return nodoPDoctoRelacionado;
// }
//
// private static Element ObtenerNodoImpuestosDR(ImpuestosDR impuestosDR,
/// XmlDocument documento) {
// if (impuestosDR == null) {
// return null;
// }
// Element nodoPImpuestosDR = documento.CreateElement("pago20", "ImpuestosDR",
/// NAMESPACE_PAGOS);
// if (impuestosDR.RetencionesDR != null) {
// Element nodoPRetenciones = documento.CreateElement("pago20", "RetencionesDR",
/// NAMESPACE_PAGOS);
// for (RetencionDR retencion : impuestosDR.RetencionesDR.RetencionDR) {
// Element nodoPRetencion = documento.CreateElement("pago20", "RetencionDR",
/// NAMESPACE_PAGOS);
// nodoPRetencion.SetAttribute("BaseDR", String.format("%1$.2f",
/// retencion.BaseDR));
// nodoPRetencion.SetAttribute("ImpuestoDR", retencion.ImpuestoDR);
// nodoPRetencion.SetAttribute("TipoFactorDR", retencion.TipoFactorDR);
// nodoPRetencion.SetAttribute("TasaOCuota", String.format("%1$.6f",
/// retencion.TasaOCuotaDR));
// nodoPRetencion.SetAttribute("ImporteDR", String.format("%1$.2f",
/// retencion.ImporteDR));
// nodoPRetenciones.AppendChild(nodoPRetencion);
// }
// nodoPImpuestosDR.AppendChild(nodoPRetenciones);
// }
//
// if (impuestosDR.TrasladosDR != null) {
// Element nodoPTraslados = documento.CreateElement("pago20", "TrasladosDR",
/// NAMESPACE_CFD);
// for (TrasladoDR traslado : impuestosDR.TrasladosDR.TrasladoDR) {
// Element nodoPTraslado = documento.CreateElement("pago20", "TrasladoDR",
/// NAMESPACE_CFD);
// nodoPTraslado.SetAttribute("BaseDR", String.format("%1$.2f",
/// traslado.BaseDR));
// nodoPTraslado.SetAttribute("ImpuestoDR", traslado.ImpuestoDR);
// nodoPTraslado.SetAttribute("TipoFactorDR", traslado.TipoFactorDR);
// nodoPTraslado.SetAttribute("TasaOCuota", String.format("%1$.6f",
/// traslado.TasaOCuotaDR));
// nodoPTraslado.SetAttribute("ImporteDR", String.format("%1$.2f",
/// traslado.ImporteDR));
// nodoPTraslados.AppendChild(nodoPTraslado);
// }
// nodoPImpuestosDR.AppendChild(nodoPTraslados);
// }
//
// return nodoPImpuestosDR;
// }
//
// private static Element ObtenerNodoImpuestosP(ImpuestosP impuestosP,
/// XmlDocument documento) {
// if (impuestosP == null) {
// return null;
// }
// Element nodoPImpuestos = documento.CreateElement("pago20", "ImpuestosP",
/// NAMESPACE_PAGOS);
//
// if (impuestosP.RetencionesP != null) {
// Element nodoPRetenciones = documento.CreateElement("pago20", "RetencionesP",
/// NAMESPACE_PAGOS);
// for (RetencionP retencion : impuestosP.RetencionesP.RetencionP) {
// Element nodoPRetencion = documento.CreateElement("pago20", "RetencionP",
/// NAMESPACE_PAGOS);
// nodoPRetencion.SetAttribute("ImpuestoP", retencion.ImpuestoP);
// nodoPRetencion.SetAttribute("ImporteP", String.format("%1$.2f",
/// retencion.ImporteP));
// nodoPRetenciones.AppendChild(nodoPRetencion);
// }
// nodoPImpuestos.AppendChild(nodoPRetenciones);
// }
//
// if (impuestosP.TrasladosP != null) {
// Element nodoPTraslados = documento.CreateElement("pago20", "Traslados",
/// NAMESPACE_CFD);
// for (TrasladoP traslado : impuestosP.TrasladosP.TrasladoP) {
// Element nodoPTraslado = documento.CreateElement("pago20", "Traslado",
/// NAMESPACE_CFD);
// nodoPTraslado.SetAttribute("BaseP", String.format("%1$.2f", traslado.BaseP));
// nodoPTraslado.SetAttribute("ImpuestoP", traslado.ImpuestoP);
// nodoPTraslado.SetAttribute("TipoFactorP", traslado.TipoFactorP);
// nodoPTraslado.SetAttribute("TasaOCuotaP", String.format("%1$.6f",
/// traslado.TasaOCuotaP));
// nodoPTraslado.SetAttribute("ImporteP", String.format("%1$.2f",
/// traslado.ImporteP));
// nodoPTraslados.AppendChild(nodoPTraslado);
// }
// nodoPImpuestos.AppendChild(nodoPTraslados);
// }
//
// return nodoPImpuestos;
// }
//
// private static Element AgregarImpuestosLocales(ImpuestosLocales implocal,
/// XmlDocument documento) {
// if (implocal == null) {
// return null;
// }
// Element nodoImpuestosLocales = documento.CreateElement("implocal",
/// "ImpuestosLocales", NAMESPACE_IMPUESTOLOCAL);
// nodoImpuestosLocales.SetAttribute("Version", implocal.Version);
// nodoImpuestosLocales.SetAttribute("TotaldeRetenciones",
/// String.format("%1$.2f", implocal.TotaldeRetenciones));
// nodoImpuestosLocales.SetAttribute("TotaldeTraslados", String.format("%1$.2f",
/// implocal.TotalTraslasdos));
// Element retencionesLocales =
/// ObtenerNodoRetencionesLocales(implocal.RetencionesLocales, documento);
// if (retencionesLocales != null) {
// nodoImpuestosLocales.AppendChild(retencionesLocales);
// }
// Element trasladosLocales =
/// ObtenerNodoTrasladosLocales(implocal.TrasladosLocales, documento);
// if (trasladosLocales != null) {
// nodoImpuestosLocales.AppendChild(trasladosLocales);
// }
// XmlAttribute schemalocation = documento.CreateAttribute("xsi",
/// "schemaLocation", "http://www.w3.org/2001/XMLSchema-instance");
// schemalocation.Value = SCHEMALOCATION_IMPUESTOSLOCALES;
// nodoImpuestosLocales.SetAttributeNode(schemalocation);
// return nodoImpuestosLocales;
// }
//
// private static Element
/// ObtenerNodoRetencionesLocales(ArrayList<RetencionesLocales> retenlocal,
/// XmlDocument documento) {
// if (retenlocal == null && retenlocal.size() == 0) {
// return null;
// }
// Element nodoRetenLocales = documento.CreateElement("implocal",
/// "RetencionesLocales", NAMESPACE_IMPUESTOLOCAL);
// for (RetencionesLocales rl : retenlocal) {
// nodoRetenLocales.SetAttribute("ImpLocRetenido", rl.ImpLocRetenido);
// nodoRetenLocales.SetAttribute("TasadeRetencion", String.format("%1$.2f",
/// rl.TasadeRetencion));
// nodoRetenLocales.SetAttribute("Importe", String.format("%1$.2f",
/// rl.Importe));
// }
// return nodoRetenLocales;
// }
//
// private static Element
/// ObtenerNodoTrasladosLocales(ArrayList<TrasladosLocales> traslocal,
/// XmlDocument documento) {
// if (traslocal == null && traslocal.size() == 0) {
// return null;
// }
// Element nodoTrasLocales = documento.CreateElement("implocal",
/// "TrasladosLocales", NAMESPACE_IMPUESTOLOCAL);
// for (TrasladosLocales tl : traslocal) {
// nodoTrasLocales.SetAttribute("ImpLocTrasladado ", tl.ImpLocTraslado);
// nodoTrasLocales.SetAttribute("TasadeTraslado", String.format("%1$.2f",
/// tl.TasaTraslado));
// nodoTrasLocales.SetAttribute("Importe", String.format("%1$.2f", tl.Importe));
// }
// return nodoTrasLocales;
// }
//
// private static Element AgregarNodoIngresosHidrocarburos(IngresosHidrocarburos
/// ingresosHidro, XmlDocument documento) {
// if (ingresosHidro == null) {
// return null;
// }
// Element NodoIngresosHidro = documento.CreateElement("ieeh",
/// "IngresosHidrocarburos", NAMESPACE_HINGRESOSHIDROCARBUROS);
// NodoIngresosHidro.SetAttribute("Version", ingresosHidro.Version);
// NodoIngresosHidro.SetAttribute("NumeroContrato",
/// ingresosHidro.NumeroContrato);
// NodoIngresosHidro.SetAttribute("ContraprestacionPagadaOperador",
/// String.format("%1$.2f", ingresosHidro.ContraPrestacionPagadaOperador));
// NodoIngresosHidro.SetAttribute("Porcentaje", String.format("%1$.3f",
/// ingresosHidro.Porcentaje));
// Element ihDocumentoRelacionado =
/// ObtenerNodoIHDocumentoRelacionado(ingresosHidro.DocumentoRelacionado,
/// documento);
// if (ihDocumentoRelacionado != null) {
// NodoIngresosHidro.AppendChild(ihDocumentoRelacionado);
// }
// XmlAttribute schemalocation = documento.CreateAttribute("xsi",
/// "schemaLocation", "http://www.w3.org/2001/XMLSchema-instance");
// schemalocation.Value = SCHEMALOCATION_INGRESOSHIDROCARBUROS;
// NodoIngresosHidro.SetAttributeNode(schemalocation);
// return NodoIngresosHidro;
// }
//
// private static Element
/// ObtenerNodoIHDocumentoRelacionado(ArrayList<IHDocumentoRelacionado>
/// ihDoctoRela, XmlDocument documento) {
// if (ihDoctoRela == null && ihDoctoRela.size() == 0) {
// return null;
// }
// Element nodoihDoctoRelacionado = documento.CreateElement("ieeh",
/// "DocumentoRelacionado", NAMESPACE_HINGRESOSHIDROCARBUROS);
// for (IHDocumentoRelacionado dr : ihDoctoRela) {
// nodoihDoctoRelacionado.SetAttribute("FolioFiscalVinculado",
/// dr.FolioFiscalVinculado);
// nodoihDoctoRelacionado.SetAttribute("FechaFolioFiscalVinculado",
/// dr.FechaFolioFiscalVinculado);
// nodoihDoctoRelacionado.SetAttribute("Mes", dr.Mes);
// }
// return nodoihDoctoRelacionado;
// }
//
// private static Element AgregarNodoGastosHidrocarburos(GastosHidrocarburos
/// gastosHidro, XmlDocument documento) {
// if (gastosHidro == null) {
// return null;
// }
// Element nodoGastosHidro = documento.CreateElement("gceh",
/// "GastosHidrocarburos", NAMESPACE_GASTOSHIDROCARBUROS);
// nodoGastosHidro.SetAttribute("Version", gastosHidro.Version);
// nodoGastosHidro.SetAttribute("NumeroContrato", gastosHidro.NumeroContrato);
// nodoGastosHidro.SetAttribute("AreaContractual", gastosHidro.AreaContractual);
// Element erogacion = ObtenerNodoErogacion(gastosHidro.Erogacion, documento);
// if (nodoGastosHidro != null) {
// nodoGastosHidro.AppendChild(erogacion);
// }
// XmlAttribute schemalocation = documento.CreateAttribute("xsi",
/// "schemaLocation", "http://www.w3.org/2001/XMLSchema-instance");
// schemalocation.Value = SCHEMALOCATION_GASTOSHIDROCARBUROS;
// nodoGastosHidro.SetAttributeNode(schemalocation);
// return nodoGastosHidro;
// }
//
// private static Element ObtenerNodoErogacion(ArrayList<Erogacion> erogacion,
/// XmlDocument documento) {
// if (erogacion == null && erogacion.size() == 0) {
// return null;
// }
// Element nodoErogacion = documento.CreateElement("gceh", "Erocion",
/// NAMESPACE_GASTOSHIDROCARBUROS);
// for (Erogacion e : erogacion) {
// nodoErogacion.SetAttribute("TipoErogacion", e.TipoErogacion);
// nodoErogacion.SetAttribute("MontocuErogacion", String.format("%1$.2f",
/// e.MontocuErogacion));
// nodoErogacion.SetAttribute("Porcentaje", String.format("%1$.2f",
/// e.Porcentaje));
// Element documentoRelacionado =
/// AgregarNodoEDocumentoRelacionado(e.DocumentoRelacionado, documento);
// if (nodoErogacion != null) {
// nodoErogacion.AppendChild(documentoRelacionado);
// }
// Element actividades = AgegarNodoActividades(e.Actividades, documento);
// if (nodoErogacion != null) {
// nodoErogacion.AppendChild(actividades);
// }
// Element centroCostos = AgregarNodoCentroCostos(e.CentroCostos, documento);
// if (nodoErogacion != null) {
// nodoErogacion.AppendChild(centroCostos);
// }
// }
// return nodoErogacion;
// }
//
// private static Element
/// AgregarNodoEDocumentoRelacionado(ArrayList<EDocumentoRelacionado>
/// documentoRelacionado, XmlDocument documento) {
// if (documentoRelacionado == null && documentoRelacionado.size() == 0) {
// return null;
// }
// Element nodoDocRelacionado = documento.CreateElement("gceh",
/// "DocumentoRelacionado");
// for (EDocumentoRelacionado edr : documentoRelacionado) {
// nodoDocRelacionado.SetAttribute("OrigenErogacion ", edr.OrigenErogacion);
// if (!edr.FolioFiscalVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("FolioFiscalVinculado",
/// edr.FolioFiscalVinculado);
// }
// if (!edr.RFCProveedor.equals("")) {
// nodoDocRelacionado.SetAttribute("RFCProveedor", edr.RFCProveedor);
// }
// if (edr.MontoTotalIVA > 0) {
// nodoDocRelacionado.SetAttribute("MontoTotalIVA", String.format("%1$.2f",
/// edr.MontoTotalIVA));
// }
// if (edr.MontoRetencionOtrosImpuestos > 0) {
// nodoDocRelacionado.SetAttribute("MontoRetencionOtrosImpuestos ",
/// String.format("%1$.2f", edr.MontoRetencionOtrosImpuestos));
// }
// if (!edr.NumeroPedimentoVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("NumeroPedimentoVinculado",
/// edr.NumeroPedimentoVinculado);
// }
// if (!edr.ClavePedimentoVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("ClavePedimentoVinculado ",
/// edr.ClavePedimentoVinculado);
// }
// if (!edr.ClavePagoPedimentoVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("ClavePagoPedimentoVinculado ",
/// edr.ClavePagoPedimentoVinculado);
// }
// if (edr.MontoIVAPedimento > 0) {
// nodoDocRelacionado.SetAttribute("MontoIVAPedimento", String.format("%1$.2f",
/// edr.MontoIVAPedimento));
// }
// if (edr.MontoRetencionISR > 0) {
// nodoDocRelacionado.SetAttribute("MontoRetencionIVA", String.format("%1$.2f",
/// edr.MontoRetencionIVA));
// }
// if (edr.MontoRetencionIVA > 0) {
// nodoDocRelacionado.SetAttribute("MontoRetencionIVA ", String.format("%1$.2f",
/// edr.MontoRetencionIVA));
// }
// if (edr.MontoRetencionOtrosImpuestos > 0) {
// nodoDocRelacionado.SetAttribute("MontoRetencionOtrosImpuestos",
/// String.format("%1$.2f", edr.MontoRetencionOtrosImpuestos));
// }
// if (!edr.NumeroPedimentoVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("NumeroPedimentoVinculado",
/// edr.NumeroPedimentoVinculado);
// }
// if (!edr.ClavePagoPedimentoVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("ClavePagoPedimentoVinculado ",
/// edr.ClavePagoPedimentoVinculado);
// }
// if (edr.MontoIVAPedimento > 0) {
// nodoDocRelacionado.SetAttribute("MontoIVAPedimento", String.format("%1$.2f",
/// edr.MontoIVAPedimento));
// }
// if (edr.OtrosImpuestosPagadosPedimento > 0) {
// nodoDocRelacionado.SetAttribute("OtrosImpuestosPagadosPedimento",
/// String.format("%1$.2f", edr.OtrosImpuestosPagadosPedimento));
// }
// if (!edr.FechaFolioFiscalVinculado.equals("")) {
// nodoDocRelacionado.SetAttribute("FechaFolioFiscalVinculado",
/// edr.FechaFolioFiscalVinculado);
// }
// if (!edr.Mes.equals("")) {
// nodoDocRelacionado.SetAttribute("Mes", edr.Mes);
// }
// if (edr.MontoTotalErogaciones > 0) {
// nodoDocRelacionado.SetAttribute("MontoTotalErogaciones",
/// String.format("%1$.2f", edr.MontoTotalErogaciones));
// }
// }
// return nodoDocRelacionado;
// }
//
// private static Element AgegarNodoActividades(ArrayList<Actividades>
/// actividades, XmlDocument documento) {
// if (actividades == null && actividades.size() == 0) {
// return null;
// }
// Element nodoActividades = documento.CreateElement("gceh", "Actividades",
/// NAMESPACE_GASTOSHIDROCARBUROS);
// for (Actividades a : actividades) {
// nodoActividades.SetAttribute("ActividadRelacionada", a.ActividadRelacionada);
// Element subActividades = ObtenerNodoSubActividades(a.SubActividades,
/// documento);
// if (nodoActividades != null) {
// nodoActividades.AppendChild(subActividades);
// }
// }
// return nodoActividades;
// }
//
// private static Element ObtenerNodoSubActividades(ArrayList<SubActividades>
/// subActividades, XmlDocument documento) {
// if (subActividades == null && subActividades.size() == 0) {
// return null;
// }
// Element nodoSubActividades = documento.CreateElement("gceh",
/// "SubActividades", NAMESPACE_GASTOSHIDROCARBUROS);
// for (SubActividades sa : subActividades) {
// nodoSubActividades.SetAttribute("SubActividadRelacionada",
/// sa.SubActividadRelacionada);
// Element tareas = ObtenerNodoTareas(sa.Tareas, documento);
// if (nodoSubActividades != null) {
// nodoSubActividades.AppendChild(tareas);
// }
// }
// return nodoSubActividades;
// }
//
// private static Element ObtenerNodoTareas(ArrayList<Tareas> tareas,
/// XmlDocument documento) {
// if (tareas == null && tareas.size() == 0) {
// return null;
// }
// Element nodoTareas = documento.CreateElement("gceh", "Tareas",
/// NAMESPACE_GASTOSHIDROCARBUROS);
// for (Tareas t : tareas) {
// if (!t.TareaRelacionada.equals("")) {
// nodoTareas.SetAttribute("TareaRelacionada", t.TareaRelacionada);
// }
// }
// return nodoTareas;
// }
//
// private static Element AgregarNodoCentroCostos(ArrayList<CentroCostos>
/// centroCostos, XmlDocument documento) {
// if (centroCostos == null && centroCostos.size() == 0) {
// return null;
// }
// Element nodoCentroCos = documento.CreateElement("gceh", "CentroCostos",
/// NAMESPACE_GASTOSHIDROCARBUROS);
// for (CentroCostos cc : centroCostos) {
// if (!cc.Campo.equals("")) {
// nodoCentroCos.SetAttribute("Campo", cc.Campo);
// }
// Element yacimientos = ObtenerNodoYacimientos(cc.Yacimientos, documento);
// if (nodoCentroCos != null) {
// nodoCentroCos.AppendChild(yacimientos);
// }
// }
// return nodoCentroCos;
// }
//
// private static Element ObtenerNodoYacimientos(ArrayList<Yacimientos>
/// yacimientos, XmlDocument documento) {
// if (yacimientos == null && yacimientos.size() == 0) {
// return null;
// }
// Element nodoYacimientos = documento.CreateElement("gceh", "Yacimientos",
/// NAMESPACE_GASTOSHIDROCARBUROS);
// for (Yacimientos y : yacimientos) {
// if (!y.Yacimiento.equals("")) {
// nodoYacimientos.SetAttribute("Yacimientos", y.Yacimiento);
// }
// Element pozos = ObtenerNodoPozos(y.Pozos, documento);
// if (nodoYacimientos != null) {
// nodoYacimientos.AppendChild(pozos);
// }
//
// }
// return nodoYacimientos;
// }
//
// private static Element ObtenerNodoPozos(ArrayList<Pozos> pozos, XmlDocument
/// documento) {
// if (pozos == null && pozos.size() == 0) {
// return null;
// }
// Element nodoPozos = documento.CreateElement("gceh", "Pozos",
/// NAMESPACE_GASTOSHIDROCARBUROS);
// for (Pozos p : pozos) {
// if (!p.Pozo.equals("")) {
// nodoPozos.SetAttribute("Pozo", p.Pozo);
// }
// }
// return nodoPozos;
// }
//
// private static void ObtenerCertificadoYNoCertificado(String rutaCertificado,
/// tangible.OutObject<String> Certificado, tangible.OutObject<String>
/// NoCertificado) {
// try {
// X509Certificate2 objCert = new X509Certificate2();
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: byte[] bRawData = ReadFile(rutaCertificado);
// byte[] bRawData = ReadFile(rutaCertificado);
// objCert.Import(bRawData);
// Certificado.outArgValue = Convert.ToBase64String(bRawData);
// NoCertificado.outArgValue = FormatearSerieCert(objCert.SerialNumber);
// } catch (java.lang.Exception e) {
// Certificado.outArgValue = "";
// NoCertificado.outArgValue = "";
// }
// }
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: private static byte[] ReadFile(string strArchivo)
//
// private static byte[] ReadFile(String strArchivo) {
// FileInputStream f = new FileInputStream(strArchivo);
// int size = (int) f.Length;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: byte[] data = new byte[size];
// byte[] data = new byte[size];
// size = f.read(data, 0, size);
// f.close();
// return data;
// }
//
// private static String FormatearSerieCert(String Serie) {
// String Resultado = "";
// int i;
// for (i = 1; i < Serie.length(); i += 2) {
// //Resultado = Serie.Substring(I, 1);
// Resultado = Resultado + Serie.substring(i, i + 1);
// }
// return Resultado;
// }
//
// private static String ObtenerSelloPorPFX(XmlDocument xml, String rutaPFX,
/// String passwordPFX) {
// X509Certificate2 privateCert = new X509Certificate2(rutaPFX, passwordPFX,
/// X509KeyStorageFlags.Exportable);
// RSACryptoServiceProvider privateKey = (RSACryptoServiceProvider)
/// privateCert.PrivateKey;
// RSACryptoServiceProvider privateKey1 = new
/// System.Security.Cryptography.RSACryptoServiceProvider();
// privateKey1.ImportParameters(privateKey.ExportParameters(true));
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: byte[] bytesFirmados =
/// privateKey1.SignData(System.Text.Encoding.UTF8.GetBytes(GetCadenaOriginal(xml.InnerXml)),
/// "SHA256");
// byte[] bytesFirmados =
/// privateKey1.SignData(GetCadenaOriginal(xml.InnerXml).getBytes(java.nio.charset.StandardCharsets.UTF_8),
/// "SHA256");
// return Convert.ToBase64String(bytesFirmados);
// }
//
// private static String ObtenerSelloPorCertificado(XmlDocument xml, String
/// rutaArchivoClavePrivada, String lPassword) {
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: byte[] ClavePrivada =
/// File.ReadAllBytes(rutaArchivoClavePrivada);
// byte[] ClavePrivada = File.ReadAllBytes(rutaArchivoClavePrivada);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: byte[] bytesFirmados = null;
// byte[] bytesFirmados = null;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
/// equivalent in Java:
//// ORIGINAL LINE: byte[] bCadenaOriginal = null;
// byte[] bCadenaOriginal = null;
// String CadenaOriginal = GetCadenaOriginal(xml.InnerXml);
//
// System.Security.SecureString lSecStr = new System.Security.SecureString();
// SHA256Managed sham = new SHA256Managed();
// //SHA1Managed sham = new SHA1Managed(); //version 3.2
// lSecStr.Clear();
//
// for (char c : lPassword.toCharArray()) {
// lSecStr.AppendChar(c);
// }
//
// RSACryptoServiceProvider lrsa =
/// opensslkey.DecodeEncryptedPrivateKeyInfo(ClavePrivada, lSecStr);
// bCadenaOriginal =
/// CadenaOriginal.getBytes(java.nio.charset.StandardCharsets.UTF_8);
// try {
// bytesFirmados =
/// lrsa.SignData(CadenaOriginal.getBytes(java.nio.charset.StandardCharsets.UTF_8),
/// sham);
//
// } catch (NullPointerException ex) {
// throw new NullPointerException("Clave privada incorrecta, revisa que la clave
/// que escribes corresponde a los sellos digitales cargados");
// }
// String sellodigital = Convert.ToBase64String(bytesFirmados);
// return sellodigital;
//
// }
//
// private static String GetCadenaOriginal(String xmlCFD) {
// String rutaXSLT = "\\XSLT\\cadenaoriginal.xslt";
// XslCompiledTransform xslt = new XslCompiledTransform();
// XmlDocument xmldoc = new XmlDocument();
// XPathNavigator navigator;
// StringWriter output = new StringWriter();
// xmldoc.LoadXml(xmlCFD);
// navigator = xmldoc.CreateNavigator();
// xslt.Load(Application.StartupPath + rutaXSLT);
// xslt.Transform(navigator, null, output);
// return output.toString();
// }
//
// }
