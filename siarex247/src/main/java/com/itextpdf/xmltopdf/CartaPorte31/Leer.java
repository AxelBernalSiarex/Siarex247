package com.itextpdf.xmltopdf.CartaPorte31;

import java.util.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.time.*;

/**
 *
 * @author frack
 */

public class Leer {

    public static CartaPorte31 ObtenerNodoComplementoCartaPorte(Element documento) {
        NodeList listaCartaPorte = documento.getElementsByTagName("cartaporte31:CartaPorte");
        if (listaCartaPorte.getLength() == 0) {
            return null;
        }
        CartaPorte31 cartaPorte = new CartaPorte31();
        Element nodoCartaPorte = (Element) listaCartaPorte.item(0);
        if (!cartaPorte.getVersion().equals("3.1")) {
            return null;
        }
        if (nodoCartaPorte.hasAttribute("Version")) {
            cartaPorte.setVersion(nodoCartaPorte.getAttribute("Version"));
        }
        if (nodoCartaPorte.hasAttribute("idCCP")) {
            cartaPorte.setIdCCP(nodoCartaPorte.getAttribute("idCCP"));
        }

        if (nodoCartaPorte.hasAttribute("TranspInternac")) {
            cartaPorte.setTranspInternac(nodoCartaPorte.getAttribute("TranspInternac"));
        }
        if (nodoCartaPorte.hasAttribute("RegimenAduanero")) {
            cartaPorte.setRegimenAduanero(nodoCartaPorte.getAttribute("RegimenAduanero"));
        }
        if (nodoCartaPorte.hasAttribute("EntradaSalidaMerc")) {
            cartaPorte.setEntradaSalidaMerc(nodoCartaPorte.getAttribute("EntradaSalidaMerc"));
        }
        if (nodoCartaPorte.hasAttribute("PaisOrigenDestino")) {
            cartaPorte.setPaisOrigenDestino(nodoCartaPorte.getAttribute("PaisOrigenDestino"));
        }
        if (nodoCartaPorte.hasAttribute("ViaEntradaSalida")) {
            cartaPorte.setViaEntradaSalida(nodoCartaPorte.getAttribute("ViaEntradaSalida"));
        }
        if (nodoCartaPorte.hasAttribute("TotalDistRec")) {
            cartaPorte.setTotalDistRec(Double.parseDouble(nodoCartaPorte.getAttribute("TotalDistRec")));
        }
        if (nodoCartaPorte.hasAttribute("UbicacionPoloOrigen")) {
            cartaPorte.setUbicacionPoloOrigen(nodoCartaPorte.getAttribute("UbicacionPoloOrigen"));
        }
        if (nodoCartaPorte.hasAttribute("UbicacionPoloDestino")) {
            cartaPorte.setUbicacionPoloDestino(nodoCartaPorte.getAttribute("UbicacionPoloDestino"));
        }
        if (nodoCartaPorte.hasAttribute("RegistroISTMO")) {
            cartaPorte.setRegistroISTMO(nodoCartaPorte.getAttribute("RegistroISTMO"));
        }
        
        cartaPorte.setUbicaciones(ObtenerUbicaciones(nodoCartaPorte));
        cartaPorte.setMercancias(ObtenerMercancias(nodoCartaPorte));
        cartaPorte.setFiguraTransporte(ObtenerFiguraTransporte(nodoCartaPorte));
        return cartaPorte;
    }

    private static Ubicaciones ObtenerUbicaciones(Element nodoCartaPorte) {
        Ubicaciones ubicaciones = new Ubicaciones();
        NodeList listaUbicaciones = nodoCartaPorte.getElementsByTagName("cartaporte31:Ubicaciones");
        if (listaUbicaciones.getLength() == 0) {
            return null;
        }
        Element nodoUbicaciones = (Element) listaUbicaciones.item(0) instanceof Element
                ? (Element) listaUbicaciones.item(0)
                : null;
        ubicaciones.setUbicacion(ObtenerUbicacion(nodoUbicaciones));
        return ubicaciones;
    }

    private static ArrayList<Ubicacion> ObtenerUbicacion(Element nodoUbicaciones) {
        ArrayList<Ubicacion> listaUbicacion = new ArrayList<>();
        NodeList nListaUbicacion = nodoUbicaciones.getElementsByTagName("cartaporte31:Ubicacion");
        for (int i = 0; i < nListaUbicacion.getLength(); i++) {
            Element item = (Element) nListaUbicacion.item(i);
            Ubicacion ubicacion = new Ubicacion();
            if (item.hasAttribute("TipoUbicacion")) {
                ubicacion.setTipoUbicacion(item.getAttribute("TipoUbicacion"));
            }
            if (item.hasAttribute("IDUbicacion")) {
                ubicacion.setIDUbicacion(item.getAttribute("IDUbicacion"));
            }
            if (item.hasAttribute("RFCRemitenteDestinatario")) {
                ubicacion.setRFCRemitenteDestinatario(item.getAttribute("RFCRemitenteDestinatario"));
            }
            if (item.hasAttribute("NombreRemitenteDestinatario")) {
                ubicacion.setNombreRemitenteDestinatario(item.getAttribute("NombreRemitenteDestinatario"));
            }
            if (item.hasAttribute("NumRegIdTrib")) {
                ubicacion.setNumRegIdTrib(item.getAttribute("NumRegIdTrib"));
            }
            if (item.hasAttribute("ResidenciaFiscal")) {
                ubicacion.setResidenciaFiscal(item.getAttribute("ResidenciaFiscal"));
            }
            if (item.hasAttribute("NumEstacion")) {
                ubicacion.setNumEstacion(item.getAttribute("NumEstacion"));
            }
            if (item.hasAttribute("NombreEstacion")) {
                ubicacion.setNombreEstacion(item.getAttribute("NombreEstacion"));
            }
            if (item.hasAttribute("NavegacionTrafico")) {
                ubicacion.setNavegacionTrafico(item.getAttribute("NavegacionTrafico"));
            }
            if (item.hasAttribute("FechaHoraSalidaLlegada")) {
                ubicacion.setFechaHoraSalidaLlegada(LocalDateTime.parse(item.getAttribute("FechaHoraSalidaLlegada")));
            }
            if (item.hasAttribute("TipoEstacion")) {
                ubicacion.setTipoEstacion(item.getAttribute("TipoEstacion"));
            }
            if (item.hasAttribute("DistanciaRecorrida")) {
                ubicacion.setDistanciaRecorrida(Double.parseDouble(item.getAttribute("DistanciaRecorrida")));
            }
            ubicacion.setDomicilio(ObtenerDomicilio(item));
            listaUbicacion.add(ubicacion);
        }
        return listaUbicacion;

    }

    private static Domicilio ObtenerDomicilio(Element nodoUbicacion) {
        Domicilio domicilio = new Domicilio();
        NodeList listaDomicilios = nodoUbicacion.getElementsByTagName("cartaporte31:Domicilio");
        if (listaDomicilios.getLength() == 0) {
            return null;
        }
        Element nodoDomicilio = (Element) listaDomicilios.item(0);
        if (nodoDomicilio.hasAttribute("Calle")) {
            domicilio.setCalle(nodoDomicilio.getAttribute("Calle"));
        }
        if (nodoDomicilio.hasAttribute("NumeroExterior")) {
            domicilio.setNumeroExterior(nodoDomicilio.getAttribute("NumeroExterior"));
        }
        if (nodoDomicilio.hasAttribute("NumeroInterior")) {
            domicilio.setNumeroInterior(nodoDomicilio.getAttribute("NumeroInterior"));
        }
        if (nodoDomicilio.hasAttribute("Colonia")) {
            domicilio.setColonia(nodoDomicilio.getAttribute("Colonia"));
        }
        if (nodoDomicilio.hasAttribute("Localidad")) {
            domicilio.setLocalidad(nodoDomicilio.getAttribute("Localidad"));
        }
        if (nodoDomicilio.hasAttribute("Referencia")) {
            domicilio.setReferencia(nodoDomicilio.getAttribute("Referencia"));
        }
        if (nodoDomicilio.hasAttribute("Municipio")) {
            domicilio.setMunicipio(nodoDomicilio.getAttribute("Municipio"));
        }
        if (nodoDomicilio.hasAttribute("Estado")) {
            domicilio.setEstado(nodoDomicilio.getAttribute("Estado"));
        }
        if (nodoDomicilio.hasAttribute("Pais")) {
            domicilio.setPais(nodoDomicilio.getAttribute("Pais"));
        }
        if (nodoDomicilio.hasAttribute("CodigoPostal")) {
            domicilio.setCodigoPostal(nodoDomicilio.getAttribute("CodigoPostal"));
        }
        return domicilio;
    }

    private static Mercancias ObtenerMercancias(Element nodoCartaPorte) {
        Mercancias mercancias = new Mercancias();
        NodeList listaMercancias = nodoCartaPorte.getElementsByTagName("cartaporte31:Mercancias");
        if (listaMercancias.getLength() == 0) {
            return null;
        }
        Element nodoMercancias = (Element) listaMercancias.item(0);
        if (nodoMercancias.hasAttribute("PesoBrutoTotal")) {
            mercancias.setPesoBrutoTotal(Double.parseDouble(nodoMercancias.getAttribute("PesoBrutoTotal")));
        }
        if (nodoMercancias.hasAttribute("UnidadPeso")) {
            mercancias.setUnidadPeso(nodoMercancias.getAttribute("UnidadPeso"));
        }
        if (nodoMercancias.hasAttribute("PesoNetoTotal")) {
            mercancias.setPesoNetoTotal(Double.parseDouble(nodoMercancias.getAttribute("PesoNetoTotal")));
        }
        if (nodoMercancias.hasAttribute("NumTotalMercancias")) {
            mercancias.setNumTotalMercancias(Integer.parseInt(nodoMercancias.getAttribute("NumTotalMercancias")));
        }
        if (nodoMercancias.hasAttribute("CargoPorTasacion")) {
            mercancias.setCargoPorTasacion(Double.parseDouble(nodoMercancias.getAttribute("CargoPorTasacion")));
        }
        if (nodoMercancias.hasAttribute("LogisticaInversaRecoleccionDevolucion")) {
            mercancias.setLogisticaInversaRecoleccionDevolucion(
                    nodoMercancias.getAttribute("LogisticaInversaRecoleccionDevolucion"));
        }
        mercancias.setMercancia(ObtenerMercancia(nodoMercancias));
        mercancias.setAutotransporte(ObtenerAutoTransporte(nodoMercancias));
        mercancias.setTransporteMaritimo(ObtenerTransporteMaritimo(nodoMercancias));
        mercancias.setTransporteAereo(ObtenerTransporteAereo(nodoMercancias));
        mercancias.setTransporteFerroviario(ObtenerTransporteFerroviario(nodoMercancias));
        return mercancias;
    }

    private static ArrayList<Mercancia> ObtenerMercancia(Element nodoMercancias) {
        ArrayList<Mercancia> listaMercancia = new ArrayList<>();
        NodeList nListaMercancia = nodoMercancias.getElementsByTagName("cartaporte31:Mercancia");
        for (int i = 0; i < nListaMercancia.getLength(); i++) {
            Element item = (Element) nListaMercancia.item(i);
            Mercancia mercancia = new Mercancia();
            if (item.hasAttribute("BienesTransp")) {
                mercancia.setBienesTransp(item.getAttribute("BienesTransp"));
            }
            if (item.hasAttribute("ClaveSTCC")) {
                mercancia.setClaveSTCC(item.getAttribute("ClaveSTCC"));
            }
            if (item.hasAttribute("Descripcion")) {
                mercancia.setDescripcion(item.getAttribute("Descripcion"));
            }
            if (item.hasAttribute("Cantidad")) {
                mercancia.setCantidad(Double.parseDouble(item.getAttribute("Cantidad")));
            }
            if (item.hasAttribute("ClaveUnidad")) {
                mercancia.setClaveUnidad(item.getAttribute("ClaveUnidad"));
            }
            if (item.hasAttribute("Unidad")) {
                mercancia.setUnidad(item.getAttribute("Unidad"));
            }
            if (item.hasAttribute("Dimensiones")) {
                mercancia.setDimensiones(item.getAttribute("Dimensiones"));
            }
            if (item.hasAttribute("MaterialPeligroso")) {
                mercancia.setMaterialPeligroso(item.getAttribute("MaterialPeligroso"));
            }
            if (item.hasAttribute("CveMaterialPeligroso")) {
                mercancia.setCveMaterialPeligroso(item.getAttribute("CveMaterialPeligroso"));
            }
            if (item.hasAttribute("Embalaje")) {
                mercancia.setEmbalaje(item.getAttribute("Embalaje"));
            }
            if (item.hasAttribute("DescripEmbalaje")) {
                mercancia.setDescripEmbalaje(item.getAttribute("DescripEmbalaje"));
            }
            if (item.hasAttribute("SectorCOFEPRIS")) {
                mercancia.setSectorCOFEPRIS(item.getAttribute("SectorCOFEPRIS"));
            }
            if (item.hasAttribute("NombreIngredienteActivo")) {
                mercancia.setNombreIngredienteActivo(item.getAttribute("NombreIngredienteActivo"));
            }
            if (item.hasAttribute("NomQuimico")) {
                mercancia.setNomQuimico(item.getAttribute("NomQuimico"));
            }
            if (item.hasAttribute("DenominacionGenericaProd")) {
                mercancia.setDenominacionGenericaProd(item.getAttribute("DenominacionGenericaProd"));
            }
            if (item.hasAttribute("DenominacionDistintivaProd")) {
                mercancia.setDenominacionDistintivaProd(item.getAttribute("DenominacionDistintivaProd"));
            }
            if (item.hasAttribute("Fabricante")) {
                mercancia.setFabricante(item.getAttribute("Fabricante"));
            }
            if (item.hasAttribute("FechaCaducidad")) {
                mercancia.setFechaCaducidad(LocalDate.parse(item.getAttribute("FechaCaducidad")).atStartOfDay());
            }
            if (item.hasAttribute("LoteMedicamento")) {
                mercancia.setLoteMedicamento(item.getAttribute("LoteMedicamento"));
            }
            if (item.hasAttribute("FormaFarmaceutica")) {
                mercancia.setFormaFarmaceutica(item.getAttribute("FormaFarmaceutica"));
            }
            if (item.hasAttribute("CondicionesEspTransp")) {
                mercancia.setCondicionesEspTransp(item.getAttribute("CondicionesEspTransp"));
            }
            if (item.hasAttribute("RegistroSanitarioFolioAutorizacion")) {
                mercancia
                        .setRegistroSanitarioFolioAutorizacion(item.getAttribute("RegistroSanitarioFolioAutorizacion"));
            }
            if (item.hasAttribute("PermisoImportacion")) {
                mercancia.setPermisoImportacion(item.getAttribute("PermisoImportacion"));
            }
            if (item.hasAttribute("FolioImpoVUCEM")) {
                mercancia.setFolioImpoVUCEM(item.getAttribute("FolioImpoVUCEM"));
            }
            if (item.hasAttribute("NumCAS")) {
                mercancia.setNumCAS(item.getAttribute("NumCAS"));
            }
            if (item.hasAttribute("RazonSocialEmpImp")) {
                mercancia.setRazonSocialEmpImp(item.getAttribute("RazonSocialEmpImp"));
            }
            if (item.hasAttribute("NumRegSanPlagCOFEPRIS")) {
                mercancia.setNumRegSanPlagCOFEPRIS(item.getAttribute("NumRegSanPlagCOFEPRIS"));
            }
            if (item.hasAttribute("DatosFabricante")) {
                mercancia.setDatosFabricante(item.getAttribute("DatosFabricante"));
            }
            if (item.hasAttribute("DatosFormulador")) {
                mercancia.setDatosFormulador(item.getAttribute("DatosFormulador"));
            }
            if (item.hasAttribute("DatosMaquilador")) {
                mercancia.setDatosMaquilador(item.getAttribute("DatosMaquilador"));
            }
            if (item.hasAttribute("UsoAutorizado")) {
                mercancia.setUsoAutorizado(item.getAttribute("UsoAutorizado"));
            }
            if (item.hasAttribute("PesoEnKg")) {
                mercancia.setPesoEnKg(Double.parseDouble(item.getAttribute("PesoEnKg")));
            }
            if (item.hasAttribute("ValorMercancia")) {
                mercancia.setValorMercancia(Double.parseDouble(item.getAttribute("ValorMercancia")));
            }
            if (item.hasAttribute("Moneda")) {
                mercancia.setMoneda(item.getAttribute("Moneda"));
            }
            if (item.hasAttribute("FraccionArancelaria")) {
                mercancia.setFraccionArancelaria(item.getAttribute("FraccionArancelaria"));
            }
            if (item.hasAttribute("UUIDComercioExt")) {
                mercancia.setUUIDComercioExt(item.getAttribute("UUIDComercioExt"));
            }
            if (item.hasAttribute("TipoMateria")) {
                mercancia.setTipoMateria(item.getAttribute("TipoMateria"));
            }
            if (item.hasAttribute("DescripcionMateria")) {
                mercancia.setDescripcionMateria(item.getAttribute("DescripcionMateria"));
            }
            mercancia.setDocumentacionAduanera(ObtenerDocumentacionAduanera(item));
            mercancia.setGuiasIdentificacion(ObtenerGuiasIdentificacion(item));
            mercancia.setCantidadTransporta(ObtenerCantidadTransporta(item));
            mercancia.setDetalleMercancia(ObtenerDetalleMercancia(item));
            listaMercancia.add(mercancia);
        }
        return listaMercancia;
    }

    private static ArrayList<DocumentacionAduanera> ObtenerDocumentacionAduanera(Element nodoMercancia) {
        ArrayList<DocumentacionAduanera> listaDocumentacionAduanera = new ArrayList<>();
        NodeList nListaPedimentos = nodoMercancia.getElementsByTagName("cartaporte31:DocumentacionAduanera");
        for (int i = 0; i < nListaPedimentos.getLength(); i++) {
            Element item = (Element) nListaPedimentos.item(i);
            DocumentacionAduanera pedimentos = new DocumentacionAduanera();
            if (item.hasAttribute("TipoDocumento")) {
                pedimentos.setTipoDocumento(item.getAttribute("TipoDocumento"));
            }
            if (item.hasAttribute("NumPedimento")) {
                pedimentos.setNumPedimento(item.getAttribute("NumPedimento"));
            }
            if (item.hasAttribute("IdentDocAduanero")) {
                pedimentos.setIdentDocAduanero(item.getAttribute("IdentDocAduanero"));
            }
            if (item.hasAttribute("RFCImpo")) {
                pedimentos.setRFCImpo(item.getAttribute("RFCImpo"));
            }
            listaDocumentacionAduanera.add(pedimentos);
        }
        return listaDocumentacionAduanera;
    }

    private static ArrayList<GuiasIdentificacion> ObtenerGuiasIdentificacion(Element nodoMercancia) {
        ArrayList<GuiasIdentificacion> listaGuiasIdentificacion = new ArrayList<>();
        NodeList nListaGuiasIdentificacion = nodoMercancia.getElementsByTagName("cartaporte31:GuiasIdentificacion");
        for (int i = 0; i < nListaGuiasIdentificacion.getLength(); i++) {
            Element item = (Element) nListaGuiasIdentificacion.item(i);
            GuiasIdentificacion guiasIdentificacion = new GuiasIdentificacion();
            if (item.hasAttribute("DescripGuiaIdentificacion")) {
                guiasIdentificacion.setDescripcionGuiaIdentificacion(item.getAttribute("DescripGuiaIdentificacion"));
            }
            if (item.hasAttribute("NumeroGuiaIdentificacion")) {
                guiasIdentificacion.setNumeroGuiaIdentificacion(item.getAttribute("NumeroGuiaIdentificacion"));
            }
            if (item.hasAttribute("PesoGuiaIdentificacion")) {
                guiasIdentificacion
                        .setPesoGuiaIdentificacion(Double.parseDouble(item.getAttribute("PesoGuiaIdentificacion")));
            }
            listaGuiasIdentificacion.add(guiasIdentificacion);
        }
        return listaGuiasIdentificacion;
    }

    private static ArrayList<CantidadTransporta> ObtenerCantidadTransporta(Element nodoMercancia) {
        ArrayList<CantidadTransporta> listaCantidadTransporta = new ArrayList<>();
        NodeList nlistaCantidadTransporta = nodoMercancia.getElementsByTagName("cartaporte31:CantidadTransporta");
        for (int i = 0; i < nlistaCantidadTransporta.getLength(); i++) {
            Element item = (Element) nlistaCantidadTransporta.item(i);
            CantidadTransporta cantidadTransporta = new CantidadTransporta();
            if (item.hasAttribute("Cantidad")) {
                cantidadTransporta.setCantidad(Double.parseDouble(item.getAttribute("Cantidad")));
            }
            if (item.hasAttribute("IDOrigen")) {
                cantidadTransporta.setIDOrigen(item.getAttribute("IDOrigen"));
            }
            if (item.hasAttribute("IDDestino")) {
                cantidadTransporta.setIDDestino(item.getAttribute("IDDestino"));
            }
            if (item.hasAttribute("CvesTransporte")) {
                cantidadTransporta.setCvesTransporte(item.getAttribute("CvesTransporte"));
            }
            listaCantidadTransporta.add(cantidadTransporta);
        }
        return listaCantidadTransporta;
    }

    private static DetalleMercancia ObtenerDetalleMercancia(Element nodoMercancia) {
        DetalleMercancia detalleMercancias = new DetalleMercancia();
        NodeList listaDetalleMercancias = nodoMercancia.getElementsByTagName("cartaporte31:DetalleMercancia");
        if (listaDetalleMercancias.getLength() == 0) {
            return null;
        }
        Element nodoDetalleMercancia = (Element) listaDetalleMercancias.item(0);
        if (nodoDetalleMercancia.hasAttribute("UnidadPesoMerc")) {
            detalleMercancias.setUnidadPesoMerc(String.valueOf(nodoDetalleMercancia.hasAttribute("UnidadPesoMerc")));
        }
        if (nodoDetalleMercancia.hasAttribute("PesoBruto")) {
            detalleMercancias.setPesoBruto(Double.parseDouble(nodoDetalleMercancia.getAttribute("PesoBruto")));
        }
        if (nodoDetalleMercancia.hasAttribute("PesoNeto")) {
            detalleMercancias.setPesoNeto(Double.parseDouble(nodoDetalleMercancia.getAttribute("PesoNeto")));
        }
        if (nodoDetalleMercancia.hasAttribute("PesoTara")) {
            detalleMercancias.setPesoTara(Double.parseDouble(nodoDetalleMercancia.getAttribute("PesoTara")));
        }
        if (nodoDetalleMercancia.hasAttribute("NumPiezas")) {
            detalleMercancias.setNumPiezas(Integer.parseInt(nodoDetalleMercancia.getAttribute("NumPiezas")));
        }
        return detalleMercancias;

    }

    private static Autotransporte ObtenerAutoTransporte(Element nodoMercancias) {
        Autotransporte autoTransporte = new Autotransporte();
        NodeList listaAutotransporte = nodoMercancias.getElementsByTagName("cartaporte31:Autotransporte");
        if (listaAutotransporte.getLength() == 0) {
            return null;
        }
        Element nodoAutotransporte = (Element) listaAutotransporte.item(0);
        if (nodoAutotransporte.hasAttribute("PermSCT")) {
            autoTransporte.setPermSCT(nodoAutotransporte.getAttribute("PermSCT"));
        }
        if (nodoAutotransporte.hasAttribute("NumPermisoSCT")) {
            autoTransporte.setNumPermisoSCT(nodoAutotransporte.getAttribute("NumPermisoSCT"));
        }
        autoTransporte.setIdentificacionVehicular(ObtenerIdentificacionVehicular(nodoAutotransporte));
        autoTransporte.setSeguros(ObtenerSeguros(nodoAutotransporte));
        autoTransporte.setRemolques(ObtenerRemolques(nodoAutotransporte));

        return autoTransporte;
    }

    private static IdentificacionVehicular ObtenerIdentificacionVehicular(Element nodoAutotransporteFederal) {
        IdentificacionVehicular identificacionVehicular = new IdentificacionVehicular();
        NodeList listaIdentificacionVehicular = nodoAutotransporteFederal
                .getElementsByTagName("cartaporte31:IdentificacionVehicular");
        if (listaIdentificacionVehicular.getLength() == 0) {
            return null;
        }
        Element nodoIdentificacionVehicular = (Element) listaIdentificacionVehicular.item(0);
        if (nodoIdentificacionVehicular.hasAttribute("ConfigVehicular")) {
            identificacionVehicular.setConfigVehicular(nodoIdentificacionVehicular.getAttribute("ConfigVehicular"));
        }
        if (nodoIdentificacionVehicular.hasAttribute("PesoBrutoVehicular")) {
            identificacionVehicular.setPesoBrutoVehicular(
                    Double.parseDouble(nodoIdentificacionVehicular.getAttribute("PesoBrutoVehicular")));
        }
        if (nodoIdentificacionVehicular.hasAttribute("PlacaVM")) {
            identificacionVehicular.setPlacaVM(nodoIdentificacionVehicular.getAttribute("PlacaVM"));
        }
        if (nodoIdentificacionVehicular.hasAttribute("AnioModeloVM")) {
            identificacionVehicular
                    .setAnioModeloVM(Integer.parseInt(nodoIdentificacionVehicular.getAttribute("AnioModeloVM")));
        }
        return identificacionVehicular;
    }

    private static Seguros ObtenerSeguros(Element nodoAutoTransporte) {
        Seguros seguros = new Seguros();
        NodeList listaSeguros = nodoAutoTransporte.getElementsByTagName("cartaporte31:Seguros");
        if (listaSeguros.getLength() > 0) {
            return null;
        }
        Element nodoSeguros = (Element) listaSeguros.item(0);
        if (nodoSeguros.hasAttribute("AseguraRespCivil")) {
            seguros.setAseguraRespCivil(nodoSeguros.getAttribute("AseguraRespCivil"));
        }
        if (nodoSeguros.hasAttribute("PolizaRespCivil")) {
            seguros.setPolizaRespCivil(nodoSeguros.getAttribute("PolizaRespCivil"));
        }
        if (nodoSeguros.hasAttribute("AseguraMedAmbiente")) {
            seguros.setAseguraMedAmbiente(nodoSeguros.getAttribute("AseguraMedAmbiente"));
        }
        if (nodoSeguros.hasAttribute("PolizaMedAmbiente")) {
            seguros.setPolizaMedAmbiente(nodoSeguros.getAttribute("PolizaMedAmbiente"));
        }
        if (nodoSeguros.hasAttribute("AseguraCarga")) {
            seguros.setAseguraCarga(nodoSeguros.getAttribute("AseguraCarga"));
        }
        if (nodoSeguros.hasAttribute("PolizaCarga")) {
            seguros.setPolizaCarga(nodoSeguros.getAttribute("PolizaCarga"));
        }
        if (nodoSeguros.hasAttribute("PrimaSeguro")) {
            seguros.setPrimaSeguro(Double.parseDouble(nodoSeguros.getAttribute("PrimaSeguro")));
        }
        return seguros;
    }

    private static Remolques ObtenerRemolques(Element nodoAutotransporteFederal) {
        Remolques remolques = new Remolques();
        NodeList listaRemolques = nodoAutotransporteFederal.getElementsByTagName("cartaporte31:Remolques");
        if (listaRemolques.getLength() == 0) {
            return null;
        }
        Element nodoRemolques = (Element) listaRemolques.item(0);
        remolques.setRemolque(ObtenerRemolque(nodoRemolques));
        return remolques;
    }

    private static ArrayList<Remolque> ObtenerRemolque(Element nodoRemolques) {
        ArrayList<Remolque> listaRemolque = new ArrayList<>();
        NodeList nListaRemolque = nodoRemolques.getElementsByTagName("cartaporte31:Remolque");
        for (int i = 0; i < nListaRemolque.getLength(); i++) {
            Element item = (Element) nListaRemolque.item(i);
            Remolque remolque = new Remolque();
            if (item.hasAttribute("Placa")) {
                remolque.setPlaca(item.getAttribute("Placa"));
            }
            if (item.hasAttribute("SubTipoRem")) {
                remolque.setSubTipoRem(item.getAttribute("SubTipoRem"));
            }
            listaRemolque.add(remolque);
        }
        return listaRemolque;
    }

    private static TransporteMaritimo ObtenerTransporteMaritimo(Element nodoMercancias) {
        TransporteMaritimo transporteMaritimo = new TransporteMaritimo();
        NodeList listaTransporteMaritimo = nodoMercancias.getElementsByTagName("cartaporte31:TransporteMaritimo");
        if (listaTransporteMaritimo.getLength() == 0) {
            return null;
        }
        Element nodoTransporteMaritimo = (Element) listaTransporteMaritimo.item(0);

        if (nodoTransporteMaritimo.hasAttribute("PermSCT")) {
            transporteMaritimo.setPermSCT(nodoTransporteMaritimo.getAttribute("PermSCT"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumPermisoSCT")) {
            transporteMaritimo.setNumPermisoSCT(nodoTransporteMaritimo.getAttribute("NumPermisoSCT"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NombreAseg")) {
            transporteMaritimo.setNombreAseg(nodoTransporteMaritimo.getAttribute("NombreAseg"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumPolizaSeguro")) {
            transporteMaritimo.setNumPolizaSeguro(nodoTransporteMaritimo.getAttribute("NumPolizaSeguro"));
        }
        if (nodoTransporteMaritimo.hasAttribute("TipoEmbarcacion")) {
            transporteMaritimo.setTipoEmbarcacion(nodoTransporteMaritimo.getAttribute("TipoEmbarcacion"));
        }
        if (nodoTransporteMaritimo.hasAttribute("Matricula")) {
            transporteMaritimo.setMatricula(nodoTransporteMaritimo.getAttribute("Matricula"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumeroOMI")) {
            transporteMaritimo.setNumeroOMI(nodoTransporteMaritimo.getAttribute("NumeroOMI"));
        }
        if (nodoTransporteMaritimo.hasAttribute("AnioEmbarcacion")) {
            transporteMaritimo
                    .setAnioEmbarcacion(Integer.parseInt(nodoTransporteMaritimo.getAttribute("AnioEmbarcacion")));
        }
        if (nodoTransporteMaritimo.hasAttribute("NombreEmbarc")) {
            transporteMaritimo.setNombreEmbarc(nodoTransporteMaritimo.getAttribute("NombreEmbarc"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NacionalidadEmbarc")) {
            transporteMaritimo.setNacionalidadEmbarc(nodoTransporteMaritimo.getAttribute("NacionalidadEmbarc"));
        }
        if (nodoTransporteMaritimo.hasAttribute("UnidadesDeArqBruto")) {
            transporteMaritimo
                    .setUnidadesDeArqBruto(
                            Double.parseDouble(nodoTransporteMaritimo.getAttribute("UnidadesDeArqBruto")));
        }
        if (nodoTransporteMaritimo.hasAttribute("TipoCarga")) {
            transporteMaritimo.setTipoCarga(nodoTransporteMaritimo.getAttribute("TipoCarga"));
        }

        if (nodoTransporteMaritimo.hasAttribute("Eslora")) {
            transporteMaritimo.setEslora(Double.parseDouble(nodoTransporteMaritimo.getAttribute("Eslora")));
        }
        if (nodoTransporteMaritimo.hasAttribute("Manga")) {
            transporteMaritimo.setManga(Double.parseDouble(nodoTransporteMaritimo.getAttribute("Manga")));
        }
        if (nodoTransporteMaritimo.hasAttribute("Calado")) {
            transporteMaritimo.setCalado(Double.parseDouble(nodoTransporteMaritimo.getAttribute("Calado")));
        }
        if (nodoTransporteMaritimo.hasAttribute("Puntal")) {
            transporteMaritimo.setPuntal(Double.parseDouble(nodoTransporteMaritimo.getAttribute("Puntal")));
        }
        if (nodoTransporteMaritimo.hasAttribute("LineaNaviera")) {
            transporteMaritimo.setLineaNaviera(nodoTransporteMaritimo.getAttribute("LineaNaviera"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NombreAgenteNaviero")) {
            transporteMaritimo.setNombreAgenteNaviero(nodoTransporteMaritimo.getAttribute("NombreAgenteNaviero"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumAutorizacionNaviero")) {
            transporteMaritimo.setNumAutorizacionNaviero(nodoTransporteMaritimo.getAttribute("NumAutorizacionNaviero"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumViaje")) {
            transporteMaritimo.setNumViaje(nodoTransporteMaritimo.getAttribute("NumViaje"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumConocEmbarc")) {
            transporteMaritimo.setNumConocEmbarc(nodoTransporteMaritimo.getAttribute("NumConocEmbarc"));
        }
        if (nodoTransporteMaritimo.hasAttribute("PermisoTempNavegacion")) {
            transporteMaritimo.setPermisoTempNavegacion(nodoTransporteMaritimo.getAttribute("PermisoTempNavegacion"));
        }
        transporteMaritimo.setContenedor(ObtenerContenedor(nodoTransporteMaritimo));
        transporteMaritimo.setRemolquesCCP(ObtenerRemolquesCPP(nodoTransporteMaritimo));
        return transporteMaritimo;
    }

    private static ArrayList<Contenedor> ObtenerContenedor(Element nodoTransporteMaritimo) {
        ArrayList<Contenedor> listaContenedor = new ArrayList<>();
        NodeList nListaContenedor = nodoTransporteMaritimo.getElementsByTagName("cartaporte31:Contenedor");
        for (int i = 0; i < nListaContenedor.getLength(); i++) {
            Element item = (Element) nListaContenedor.item(i);
            Contenedor contenedor = new Contenedor();
            if (item.hasAttribute("TipoContenedor")) {
                contenedor.setTipoContenedor(item.getAttribute("TipoContenedor"));
            }
            if (item.hasAttribute("MatriculaContenedor")) {
                contenedor.setMatriculaContenedor(item.getAttribute("MatriculaContenedor"));
            }

            if (item.hasAttribute("NumPrecinto")) {
                contenedor.setNumPrecinto(item.getAttribute("NumPrecinto"));
            }
            if (item.hasAttribute("IdCCPRelacionado")) {
                contenedor.setIdCCPRelacionado(item.getAttribute("IdCCPRelacionado"));
            }
            if (item.hasAttribute("PlacaVMCCP")) {
                contenedor.setPlacaVMCCP(item.getAttribute("PlacaVMCCP"));
            }
            if (item.hasAttribute("FechaCertificacionCCP")) {
                contenedor.setFechaCertificacionCCP(LocalDateTime.parse(item.getAttribute("FechaCertificacionCCP")));
            }
            listaContenedor.add(contenedor);
        }
        return listaContenedor;
    }

    private static RemolquesCCP ObtenerRemolquesCPP(Element nodoTransporteMaritimo) {
        RemolquesCCP remolques = new RemolquesCCP();
        NodeList listaRemolques = nodoTransporteMaritimo.getElementsByTagName("cartaporte31:RemolquesCPP");
        if (listaRemolques.getLength() == 0) {
            return null;
        }
        Element nodoRemolques = (Element) listaRemolques.item(0);
        remolques.setRemolqueCCP(ObtenerRemolqueCCP(nodoRemolques));
        return remolques;
    }

    private static ArrayList<RemolqueCCP> ObtenerRemolqueCCP(Element nodoRemolques) {
        ArrayList<RemolqueCCP> listaRemolqueCCP = new ArrayList<>();
        NodeList nListaRemolqueCPP = nodoRemolques.getElementsByTagName("cartaporte31:RemolqueCCP");
        for (int i = 0; i < nListaRemolqueCPP.getLength(); i++) {
            Element item = (Element) nListaRemolqueCPP.item(i);
            RemolqueCCP remolqueCPP = new RemolqueCCP();
            if (item.hasAttribute("SubTipoRemCCP")) {
                remolqueCPP.setSubTipoRemCCP(item.getAttribute("SubTipoRemCCP"));
            }
            if (item.hasAttribute("PlacaCCP")) {
                remolqueCPP.setPlacaCCP(item.getAttribute("PlacaCCP"));
            }
            listaRemolqueCCP.add(remolqueCPP);
        }
        return listaRemolqueCCP;
    }

    private static TransporteAereo ObtenerTransporteAereo(Element nodoMercancias) {
        TransporteAereo transporteAereo = new TransporteAereo();
        NodeList listaTransporteAereo = nodoMercancias.getElementsByTagName("cartaporte31:TransporteAereo");
        if (listaTransporteAereo.getLength() == 0) {
            return null;
        }
        Element nodoTransporteAereo = (Element) listaTransporteAereo.item(0);
        if (nodoTransporteAereo.hasAttribute("PermSCT")) {
            transporteAereo.setPermSCT(nodoTransporteAereo.getAttribute("PermSCT"));
        }
        if (nodoTransporteAereo.hasAttribute("NumPermisoSCT")) {
            transporteAereo.setNumPermisoSCT(nodoTransporteAereo.getAttribute("NumPermisoSCT"));
        }
        if (nodoTransporteAereo.hasAttribute("MatriculaAeronave")) {
            transporteAereo.setMatriculaAeronave(nodoTransporteAereo.getAttribute("MatriculaAeronave"));
        }
        if (nodoTransporteAereo.hasAttribute("NombreAseg")) {
            transporteAereo.setNombreAseg(nodoTransporteAereo.getAttribute("NombreAseg"));
        }
        if (nodoTransporteAereo.hasAttribute("NumPolizaSeguro")) {
            transporteAereo.setNumPolizaSeguro(nodoTransporteAereo.getAttribute("NumPolizaSeguro"));
        }
        if (nodoTransporteAereo.hasAttribute("NumeroGuia")) {
            transporteAereo.setNumeroGuia(nodoTransporteAereo.getAttribute("NumeroGuia"));
        }
        if (nodoTransporteAereo.hasAttribute("LugarContrato")) {
            transporteAereo.setLugarContrato(nodoTransporteAereo.getAttribute("LugarContrato"));
        }
        if (nodoTransporteAereo.hasAttribute("CodigoTransportista")) {
            transporteAereo.setCodigoTransportista(nodoTransporteAereo.getAttribute("CodigoTransportista"));
        }
        if (nodoTransporteAereo.hasAttribute("RFCEmbarcador")) {
            transporteAereo.setRFCEmbarcador(nodoTransporteAereo.getAttribute("RFCEmbarcador"));
        }
        if (nodoTransporteAereo.hasAttribute("NumRegIdTribEmbarc")) {
            transporteAereo.setNumRegIdTribEmbarc(nodoTransporteAereo.getAttribute("NumRegIdTribEmbarc"));
        }
        if (nodoTransporteAereo.hasAttribute("ResidenciaFiscalEmbarc")) {
            transporteAereo.setResidenciaFiscalEmbarc(nodoTransporteAereo.getAttribute("ResidenciaFiscalEmbarc"));
        }
        if (nodoTransporteAereo.hasAttribute("NombreEmbarcador")) {
            transporteAereo.setNombreEmbarcador(nodoTransporteAereo.getAttribute("NombreEmbarcador"));
        }
        return transporteAereo;
    }

    private static TransporteFerroviario ObtenerTransporteFerroviario(Element nodoMercancias) {
        TransporteFerroviario transporteFerroviario = new TransporteFerroviario();
        NodeList listaTransporteFerroviario = nodoMercancias.getElementsByTagName("cartaporte31:TransporteFerroviario");
        if (listaTransporteFerroviario.getLength() == 0) {
            return null;
        }
        Element nodoTransporteFerroviario = (Element) listaTransporteFerroviario.item(0);
        if (nodoTransporteFerroviario.hasAttribute("TipoDeServicio")) {
            transporteFerroviario.setTipoDeServicio(nodoTransporteFerroviario.getAttribute("TipoDeServicio"));
        }
        if (nodoTransporteFerroviario.hasAttribute("TipoDeTrafico")) {
            transporteFerroviario.setTipoDeTrafico(nodoTransporteFerroviario.getAttribute("TipoDeTrafico"));
        }
        if (nodoTransporteFerroviario.hasAttribute("NombreAseg")) {
            transporteFerroviario.setNombreAseg(nodoTransporteFerroviario.getAttribute("NombreAseg"));
        }
        if (nodoTransporteFerroviario.hasAttribute("NumPolizaSeguro")) {
            transporteFerroviario.setNumPolizaSeguro(nodoTransporteFerroviario.getAttribute("NumPolizaSeguro"));
        }
        transporteFerroviario.setDerechosDePaso(ObtenerDerechosDePaso(nodoTransporteFerroviario));
        transporteFerroviario.setCarro(ObtenerCarro(nodoTransporteFerroviario));
        return transporteFerroviario;
    }

    private static ArrayList<DerechosDePaso> ObtenerDerechosDePaso(Element nodoTransporteFerroviario) {
        ArrayList<DerechosDePaso> listaDerechosDePaso = new ArrayList<>();
        NodeList nlistaDerechosDePaso = nodoTransporteFerroviario.getElementsByTagName("cartaporte31:DerechosDePaso");
        for (int i = 0; i < nlistaDerechosDePaso.getLength(); i++) {
            Element item = (Element) nlistaDerechosDePaso.item(i);
            DerechosDePaso derechosDePaso = new DerechosDePaso();
            if (item.hasAttribute("TipoDerechoDePaso")) {
                derechosDePaso.setTipoDerechoDePaso(item.getAttribute("TipoDerechoDePaso"));
            }
            if (item.hasAttribute("KilometrajePagado")) {
                derechosDePaso.setKilometrajePagado(Double.parseDouble(item.getAttribute("KilometrajePagado")));
            }
            listaDerechosDePaso.add(derechosDePaso);
        }
        return listaDerechosDePaso;
    }

    private static ArrayList<Carro> ObtenerCarro(Element nodoTransporteFerroviario) {
        ArrayList<Carro> listaCarro = new ArrayList<>();
        NodeList nlistaCarro = nodoTransporteFerroviario.getElementsByTagName("cartaporte31:Carro");
        for (int i = 0; i < nlistaCarro.getLength(); i++) {
            Element item = (Element) nlistaCarro.item(i);
            Carro carro = new Carro();

            if (item.hasAttribute("TipoCarro")) {
                carro.setTipoCarro(item.getAttribute("TipoCarro"));
            }
            if (item.hasAttribute("MatriculaCarro")) {
                carro.setMatriculaCarro(item.getAttribute("MatriculaCarro"));
            }
            if (item.hasAttribute("GuiaCarro")) {
                carro.setGuiaCarro(item.getAttribute("GuiaCarro"));
            }
            if (item.hasAttribute("ToneladasNetasCarro")) {
                carro.setToneladasNetasCarro(Double.parseDouble(item.getAttribute("ToneladasNetasCarro")));
            }
            carro.setContenedor(ObtenerContenedorCarro(item));
        }
        return listaCarro;
    }

    private static ArrayList<CarroContenedor> ObtenerContenedorCarro(Element nodoCarro) {
        ArrayList<CarroContenedor> listaContenedor = new ArrayList<>();
        NodeList nlistaContenedor = nodoCarro.getElementsByTagName("cartaporte31:Contenedor");
        for (int i = 0; i < nlistaContenedor.getLength(); i++) {
            Element item = (Element) nlistaContenedor.item(i);
            CarroContenedor contenedor = new CarroContenedor();
            if (item.hasAttribute("TipoContenedor")) {
                contenedor.setTipoContenedor(item.getAttribute("TipoContenedor"));
            }
            if (item.hasAttribute("PesoContenedorVacio")) {
                contenedor.setPesoContenedorVacio(Double.parseDouble(item.getAttribute("PesoContenedorVacio")));
            }
            if (item.hasAttribute("PesoNetoMercancia")) {
                contenedor.setPesoNetoMercancia(Double.parseDouble(item.getAttribute("PesoNetoMercancia")));
            }
            listaContenedor.add(contenedor);
        }
        return listaContenedor;
    }

    private static FiguraTransporte ObtenerFiguraTransporte(Element nodoCartaPorte) {
        FiguraTransporte figuraTransporte = new FiguraTransporte();
        NodeList listaFiguraTransporte = nodoCartaPorte.getElementsByTagName("cartaporte31:FiguraTransporte");
        if (listaFiguraTransporte.getLength() == 0) {
            return null;
        }
        Element nodoFiguraTransporte = (Element) listaFiguraTransporte.item(0) instanceof Element
                ? (Element) listaFiguraTransporte.item(0)
                : null;
        figuraTransporte.setTiposFigura(ObtenerTiposFigura(nodoFiguraTransporte));
        return figuraTransporte;
    }

    private static ArrayList<TiposFigura> ObtenerTiposFigura(Element nodoFiguraTransporte) {
        ArrayList<TiposFigura> listaTiposFigura = new ArrayList<>();
        NodeList nListaTiposFigura = nodoFiguraTransporte.getElementsByTagName("cartaporte31:TiposFigura");
        for (int i = 0; i < nListaTiposFigura.getLength(); i++) {
            Element item = (Element) nListaTiposFigura.item(i);
            TiposFigura tiposFigura = new TiposFigura();
            if (item.hasAttribute("TipoFigura")) {
                tiposFigura.setTipoFigura(item.getAttribute("TipoFigura"));
            }
            if (item.hasAttribute("RFCFigura")) {
                tiposFigura.setRFCFigura(item.getAttribute("RFCFigura"));
            }
            if (item.hasAttribute("NumLicencia")) {
                tiposFigura.setNumLicencia(item.getAttribute("NumLicencia"));
            }
            if (item.hasAttribute("NombreFigura")) {
                tiposFigura.setNombreFigura(item.getAttribute("NombreFigura"));
            }
            if (item.hasAttribute("NumRegIdTribFigura")) {
                tiposFigura.setNumRegIdTribFigura(item.getAttribute("NumRegIdTribFigura"));
            }
            if (item.hasAttribute("ResidenciaFiscalFigura")) {
                tiposFigura.setResidenciaFiscalFigura(item.getAttribute("ResidenciaFiscalFigura"));
            }
            tiposFigura.setPartesTransporte(ObtenerPartesTransporte(item));
            tiposFigura.setDomicilio(ObtenerDomicilio(item));
            listaTiposFigura.add(tiposFigura);
        }
        return listaTiposFigura;
    }

    private static ArrayList<PartesTransporte> ObtenerPartesTransporte(Element nodoTiposFigura) {
        ArrayList<PartesTransporte> listaPartesTransporte = new ArrayList<>();
        NodeList nListaPartesTransporte = nodoTiposFigura.getElementsByTagName("cartaporte31:PartesTransporte");
        for (int i = 0; i < nListaPartesTransporte.getLength(); i++) {
            Element item = (Element) nListaPartesTransporte.item(i);
            PartesTransporte partesTransporte = new PartesTransporte();
            if (item.hasAttribute("ParteTransporte")) {
                partesTransporte.setParteTransporte(item.getAttribute("ParteTransporte"));
            }
            listaPartesTransporte.add(partesTransporte);
        }
        return listaPartesTransporte;
    }
}
