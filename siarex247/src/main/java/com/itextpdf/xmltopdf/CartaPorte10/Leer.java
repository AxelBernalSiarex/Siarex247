/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte10;

/**
 *
 * @author frack
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.time.*;

/**
 *
 * @author frack
 */
public class Leer {

    public static CartaPorte10 ObtenerNodoComplementoCartaPorte(Element documento) {
        CartaPorte10 cartaPorte = new CartaPorte10();
        NodeList listaCartaPorte = documento.getElementsByTagName("cartaporte:CartaPorte");
        if (listaCartaPorte.getLength() == 0) {
            return null;
        }
        Element nodoCartaPorte = (Element) listaCartaPorte.item(0);
        if (nodoCartaPorte.hasAttribute("Version")) {
            cartaPorte.setVersion(nodoCartaPorte.getAttribute("Version"));
        }
        if (!cartaPorte.getVersion().equals("1.0")) {
            return null;
        }
        cartaPorte.setUbicaciones(ObtenerUbicaciones(nodoCartaPorte));
        cartaPorte.setMercancias(ObtenerMercancias(nodoCartaPorte));
        cartaPorte.setFiguraTransporte(ObtenerFiguraTransporte(nodoCartaPorte));
        if (nodoCartaPorte.hasAttribute("TranspInternac")) {
            cartaPorte.setTranspInternac(nodoCartaPorte.getAttribute("TranspInternac"));
        }
        if (nodoCartaPorte.hasAttribute("EntradaSalidaMerc")) {
            cartaPorte.setEntradaSalidaMerc(nodoCartaPorte.getAttribute("EntradaSalidaMerc"));
        }
        if (nodoCartaPorte.hasAttribute("ViaEntradaSalida")) {
            cartaPorte.setViaEntradaSalida(nodoCartaPorte.getAttribute("ViaEntradaSalida"));
        }
        if (nodoCartaPorte.hasAttribute("TotalDistRec")) {
            cartaPorte.setTotalDistRec(Double.parseDouble(nodoCartaPorte.getAttribute("TotalDistRec")));
        }
        return cartaPorte;
    }

    private static Ubicaciones ObtenerUbicaciones(Element nodoCartaPorte) {
        Ubicaciones ubicaciones = new Ubicaciones();
        NodeList listaUbicaciones = nodoCartaPorte.getElementsByTagName("cartaporte:Ubicaciones");
        if (listaUbicaciones.getLength() == 0) {
            return null;
        }
        Element nodoUbicaciones = (Element) listaUbicaciones.item(0);
        ubicaciones.setUbicacion(ObtenerUbicacion(nodoUbicaciones));
        return ubicaciones;
    }

    private static ArrayList<Ubicacion> ObtenerUbicacion(Element nodoUbicaciones) {
        ArrayList<Ubicacion> listaUbicacion = new ArrayList<>();
        NodeList nListaUbicacion = nodoUbicaciones.getElementsByTagName("cartaporte:Ubicacion");
        for (int i = 0; i < nListaUbicacion.getLength(); i++) {
            Element item = (Element) nListaUbicacion.item(i);
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setOrigen(ObtenerOrigen(item));
            ubicacion.setDestino(ObtenerDestino(item));
            ubicacion.setDomicilio(ObtenerDomicilio(item));
            if (item.hasAttribute("TipoEstacion")) {
                ubicacion.setTipoEstacion(item.getAttribute("TipoEstacion"));
            }
            if (item.hasAttribute("DistanciaRecorrida")) {
                ubicacion.setDistanciaRecorrida(Double.parseDouble(item.getAttribute("DistanciaRecorrida")));
            }
            listaUbicacion.add(ubicacion);
        }
        return listaUbicacion;

    }

    private static Origen ObtenerOrigen(Element nodoUbicacion) {
        NodeList listaUbicacion = nodoUbicacion.getElementsByTagName("cartaporte:Origen");
        if (listaUbicacion.getLength() == 0) {
            return null;
        }
        Origen origen = new Origen();
        Element nUbicacion = (Element) listaUbicacion.item(0);
        if (nUbicacion.hasAttribute("IDOrigen")) {
            origen.setIDOrigen(nUbicacion.getAttribute("IDOrigen"));
        }
        if (nUbicacion.hasAttribute("RFCRemitente")) {
            origen.setRFCRemitente(nUbicacion.getAttribute("RFCRemitente"));
        }
        if (nUbicacion.hasAttribute("NombreRemitente")) {
            origen.setNombreRemitente(nUbicacion.getAttribute("NombreRemitente"));
        }
        if (nUbicacion.hasAttribute("NumRegIdTrib")) {
            origen.setNumRegIdTrib(nUbicacion.getAttribute("NumRegIdTrib"));
        }
        if (nUbicacion.hasAttribute("ResidenciaFiscal")) {
            origen.setResidenciaFiscal(nUbicacion.getAttribute("ResidenciaFiscal"));
        }
        if (nUbicacion.hasAttribute("NumEstacion")) {
            origen.setNumEstacion(nUbicacion.getAttribute("NumEstacion"));
        }
        if (nUbicacion.hasAttribute("NombreEstacion")) {
            origen.setNombreEstacion(nUbicacion.getAttribute("NombreEstacion"));
        }
        if (nUbicacion.hasAttribute("NavegacionTrafico")) {
            origen.setNavegacionTrafico(nUbicacion.getAttribute("NavegacionTrafico"));
        }
        if (nUbicacion.hasAttribute("FechaHoraSalida")) {
            origen.setFechaHoraSalida(LocalDateTime.parse(nUbicacion.getAttribute("FechaHoraSalida")));
        }
        return origen;
    }

    private static Destino ObtenerDestino(Element nodoUbicacion) {

        NodeList listaDestino = nodoUbicacion.getElementsByTagName("cartaporte:Destino");
        if (listaDestino.getLength() == 0) {
            return null;
        }
        Destino destino = new Destino();
        Element nDestino = (Element) listaDestino.item(0);
        if (nDestino.hasAttribute("IDDestino")) {
            destino.setIDDestino(nDestino.getAttribute("IDDestino"));
        }
        if (nDestino.hasAttribute("RFCDestinatario")) {
            destino.setRFCDestinatario(nDestino.getAttribute("setRFCDestinatario"));
        }
        if (nDestino.hasAttribute("NombreDestinatario")) {
            destino.setNombreDestinatario(nDestino.getAttribute("NombreDestinatario"));
        }
        if (nDestino.hasAttribute("NumRegIdTrib")) {
            destino.setNumRegIdTrib(nDestino.getAttribute("NumRegIdTrib"));
        }
        if (nDestino.hasAttribute("ResidenciaFiscal")) {
            destino.setResidenciaFiscal(nDestino.getAttribute("ResidenciaFiscal"));
        }
        if (nDestino.hasAttribute("NumEstacion")) {
            destino.setNumEstacion(nDestino.getAttribute("NumEstacion"));
        }
        if (nDestino.hasAttribute("NombreEstacion")) {
            destino.setNombreEstacion(nDestino.getAttribute("NombreEstacion"));
        }
        if (nDestino.hasAttribute("NavegacionTrafico")) {
            destino.setNavegacionTrafico(nDestino.getAttribute("NavegacionTrafico"));
        }
        if (nDestino.hasAttribute("FechaHoraProgLlegada")) {
            destino.setFechaHoraProgLlegada(LocalDateTime.parse(nDestino.getAttribute("FechaHoraProgLlegada")));
        }
        return destino;
    }

    private static Domicilio ObtenerDomicilio(Element nodoUbicacion) {
        Domicilio domicilio = new Domicilio();
        NodeList listaDomicilios = nodoUbicacion.getElementsByTagName("cartaporte:Domicilio");
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
        NodeList listaMercancias = nodoCartaPorte.getElementsByTagName("cartaporte:Mercancias");
        if (listaMercancias.getLength() == 0) {
            return null;
        }
        Element nodoMercancias = (Element) listaMercancias.item(0);
        mercancias.setMercancia(ObtenerMercancia(nodoMercancias));
        mercancias.setAutotransporteFederal(ObtenerAutoTransporteFederal(nodoMercancias));
        mercancias.setTransporteMaritimo(ObtenerTransporteMaritimo(nodoMercancias));
        mercancias.setTransporteAereo(ObtenerTransporteAereo(nodoMercancias));
        mercancias.setTransporteFerroviario(ObtenerTransporteFerroviario(nodoMercancias));
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
        return mercancias;
    }

    private static ArrayList<Mercancia> ObtenerMercancia(Element nodoMercancias) {
        ArrayList<Mercancia> listaMercancia = new ArrayList<>();
        NodeList nListaMercancia = nodoMercancias.getElementsByTagName("cartaporte:Mercancia");
        for (int i = 0; i < nListaMercancia.getLength(); i++) {
            Element item = (Element) nListaMercancia.item(i);
            Mercancia mercancia = new Mercancia();
            mercancia.setCantidadTransporta(ObtenerCantidadTransporta(item));
            mercancia.setDetalleMercancia(ObtenerDetalleMercancia(item));
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
            listaMercancia.add(mercancia);
        }
        return listaMercancia;
    }

    private static ArrayList<CantidadTransporta> ObtenerCantidadTransporta(Element nodoMercancia) {
        ArrayList<CantidadTransporta> listaCantidadTransporta = new ArrayList<>();
        NodeList nlistaCantidadTransporta = nodoMercancia.getElementsByTagName("cartaporte:CantidadTransporta");
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
        NodeList listaDetalleMercancias = nodoMercancia.getElementsByTagName("cartaporte:DetalleMercancia");
        if (listaDetalleMercancias.getLength() == 0) {
            return null;
        }
        Element nodoDetalleMercancia = (Element) listaDetalleMercancias.item(0);
        if (nodoDetalleMercancia.hasAttribute("UnidadPeso")) {
            detalleMercancias.setUnidadPeso(String.valueOf(nodoDetalleMercancia.hasAttribute("UnidadPeso")));
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

    private static AutotransporteFederal ObtenerAutoTransporteFederal(Element nodoMercancias) {
        AutotransporteFederal autoTransporte = new AutotransporteFederal();
        NodeList listaAutotransporte = nodoMercancias.getElementsByTagName("cartaporte:AutotransporteFederal");
        if (listaAutotransporte.getLength() == 0) {
            return null;
        }
        Element nodoAutotransporte = (Element) listaAutotransporte.item(0);
        autoTransporte.setIdentificacionVehicular(ObtenerIdentificacionVehicular(nodoAutotransporte));
        autoTransporte.setRemolques(ObtenerRemolques(nodoAutotransporte));
        if (nodoAutotransporte.hasAttribute("PermSCT")) {
            autoTransporte.setPermSCT(nodoAutotransporte.getAttribute("PermSCT"));
        }
        if (nodoAutotransporte.hasAttribute("NumPermisoSCT")) {
            autoTransporte.setNumPermisoSCT(nodoAutotransporte.getAttribute("NumPermisoSCT"));
        }
        if (nodoAutotransporte.hasAttribute("NombreAseg")) {
            autoTransporte.setNombreAseg(nodoAutotransporte.getAttribute("NombreAseg"));
        }
        if (nodoAutotransporte.hasAttribute("NumPolizaSeguro")) {
            autoTransporte.setNumPolizaSeguro(nodoAutotransporte.getAttribute("NumPolizaSeguro"));
        }
        return autoTransporte;
    }

    private static IdentificacionVehicular ObtenerIdentificacionVehicular(Element nodoAutotransporteFederal) {
        IdentificacionVehicular identificacionVehicular = new IdentificacionVehicular();
        NodeList listaIdentificacionVehicular = nodoAutotransporteFederal
                .getElementsByTagName("cartaporte:IdentificacionVehicular");
        if (listaIdentificacionVehicular.getLength() == 0) {
            return null;
        }
        Element nodoIdentificacionVehicular = (Element) listaIdentificacionVehicular.item(0);
        if (nodoIdentificacionVehicular.hasAttribute("ConfigVehicular")) {
            identificacionVehicular.setConfigVehicular(nodoIdentificacionVehicular.getAttribute("ConfigVehicular"));
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

    private static Remolques ObtenerRemolques(Element nodoAutotransporteFederal) {
        Remolques remolques = new Remolques();
        NodeList listaRemolques = nodoAutotransporteFederal.getElementsByTagName("cartaporte:Remolques");
        if (listaRemolques.getLength() == 0) {
            return null;
        }
        Element nodoRemolques = (Element) listaRemolques.item(0);
        remolques.setRemolque(ObtenerRemolque(nodoRemolques));
        return remolques;
    }

    private static ArrayList<Remolque> ObtenerRemolque(Element nodoRemolques) {
        ArrayList<Remolque> listaRemolque = new ArrayList<>();
        NodeList nListaRemolque = nodoRemolques.getElementsByTagName("cartaporte:Remolque");
        for (int i = 0; i < nListaRemolque.getLength(); i++) {
            Element item = (Element) nListaRemolque.item(i);
            Remolque remolque = new Remolque();
            if (item.hasAttribute("SubTipoRem")) {
                remolque.setSubTipoRem(item.getAttribute("SubTipoRem"));
            }
            if (item.hasAttribute("Placa")) {
                remolque.setPlaca(item.getAttribute("Placa"));
            }
            listaRemolque.add(remolque);
        }
        return listaRemolque;
    }

    private static TransporteMaritimo ObtenerTransporteMaritimo(Element nodoMercancias) {

        TransporteMaritimo transporteMaritimo = new TransporteMaritimo();
        NodeList listaTransporteMaritimo = nodoMercancias.getElementsByTagName("cartaporte:TransporteMaritimo");
        if (listaTransporteMaritimo.getLength() == 0) {
            return null;
        }
        Element nodoTransporteMaritimo = (Element) listaTransporteMaritimo.item(0);
        transporteMaritimo.setContenedor(ObtenerContenedor(nodoTransporteMaritimo));
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
            transporteMaritimo.setUnidadesDeArqBruto(
                    Double.parseDouble(nodoTransporteMaritimo.getAttribute("UnidadesDeArqBruto")));
        }
        if (nodoTransporteMaritimo.hasAttribute("TipoCarga")) {
            transporteMaritimo.setTipoCarga(nodoTransporteMaritimo.getAttribute("TipoCarga"));
        }
        if (nodoTransporteMaritimo.hasAttribute("NumCertITC")) {
            transporteMaritimo.setNumCertITC(nodoTransporteMaritimo.getAttribute("NumCertITC"));
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
        return transporteMaritimo;
    }

    private static ArrayList<Contenedor> ObtenerContenedor(Element nodoTransporteMaritimo) {
        ArrayList<Contenedor> listaContenedor = new ArrayList<>();
        NodeList nListaContenedor = nodoTransporteMaritimo.getElementsByTagName("cartaporte:Contenedor");
        for (int i = 0; i < nListaContenedor.getLength(); i++) {
            Element item = (Element) nListaContenedor.item(i);
            Contenedor contenedor = new Contenedor();
            if (item.hasAttribute("MatriculaContenedor")) {
                contenedor.setMatriculaContenedor(item.getAttribute("MatriculaContenedor"));
            }
            if (item.hasAttribute("TipoContenedor")) {
                contenedor.setTipoContenedor(item.getAttribute("TipoContenedor"));
            }
            if (item.hasAttribute("NumPrecinto")) {
                contenedor.setNumPrecinto(item.getAttribute("NumPrecinto"));
            }
            listaContenedor.add(contenedor);
        }
        return listaContenedor;
    }

    private static TransporteAereo ObtenerTransporteAereo(Element nodoMercancias) {
        TransporteAereo transporteAereo = new TransporteAereo();
        NodeList listaTransporteAereo = nodoMercancias.getElementsByTagName("cartaporte:TransporteAereo");
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
        if (nodoTransporteAereo.hasAttribute("RFCTransportista")) {
            transporteAereo.setRFCTransportista(nodoTransporteAereo.getAttribute("RFCTransportista"));
        }
        if (nodoTransporteAereo.hasAttribute("CodigoTransportista")) {
            transporteAereo.setCodigoTransportista(nodoTransporteAereo.getAttribute("CodigoTransportista"));
        }
        if (nodoTransporteAereo.hasAttribute("NumRegIdTribTranspor")) {
            transporteAereo.setNumRegIdTribTranspor(nodoTransporteAereo.getAttribute("NumRegIdTribTranspor"));
        }
        if (nodoTransporteAereo.hasAttribute("ResidenciaFiscalTranspor")) {
            transporteAereo.setResidenciaFiscalTranspor(nodoTransporteAereo.getAttribute("ResidenciaFiscalTranspor"));
        }
        if (nodoTransporteAereo.hasAttribute("NombreTransportista")) {
            transporteAereo.setNombreTransportista(nodoTransporteAereo.getAttribute("NombreTransportista"));
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
        NodeList listaTransporteFerroviario = nodoMercancias.getElementsByTagName("cartaporte:TransporteFerroviario");
        if (listaTransporteFerroviario.getLength() == 0) {
            return null;
        }
        Element nodoTransporteFerroviario = (Element) listaTransporteFerroviario.item(0);
        transporteFerroviario.setDerechosDePaso(ObtenerDerechosDePaso(nodoTransporteFerroviario));
        transporteFerroviario.setCarro(ObtenerCarro(nodoTransporteFerroviario));
        if (nodoTransporteFerroviario.hasAttribute("TipoDeServicio")) {
            transporteFerroviario.setTipoDeServicio(nodoTransporteFerroviario.getAttribute("TipoDeServicio"));
        }
        if (nodoTransporteFerroviario.hasAttribute("NombreAseg")) {
            transporteFerroviario.setNombreAseg(nodoTransporteFerroviario.getAttribute("NombreAseg"));
        }
        if (nodoTransporteFerroviario.hasAttribute("NumPolizaSeguro")) {
            transporteFerroviario.setNumPolizaSeguro(nodoTransporteFerroviario.getAttribute("NumPolizaSeguro"));
        }
        if (nodoTransporteFerroviario.hasAttribute("Concesionario")) {
            transporteFerroviario.setConcesionario(nodoTransporteFerroviario.getAttribute("Concesionario"));
        }

        return transporteFerroviario;
    }

    private static ArrayList<DerechosDePaso> ObtenerDerechosDePaso(Element nodoTransporteFerroviario) {

        ArrayList<DerechosDePaso> listaDerechosDePaso = new ArrayList<>();
        NodeList nlistaDerechosDePaso = nodoTransporteFerroviario.getElementsByTagName("cartaporte:DerechosDePaso");
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
        NodeList nlistaCarro = nodoTransporteFerroviario.getElementsByTagName("cartaporte:Carro");
        for (int i = 0; i < nlistaCarro.getLength(); i++) {
            Element item = (Element) nlistaCarro.item(i);
            Carro carro = new Carro();
            carro.setContenedor(ObtenerContenedorCarro(item));
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
            listaCarro.add(carro);
        }
        return listaCarro;
    }

    private static ArrayList<CarroContenedor> ObtenerContenedorCarro(Element nodoCarro) {

        ArrayList<CarroContenedor> listaContenedor = new ArrayList<>();
        NodeList nlistaContenedor = nodoCarro.getElementsByTagName("cartaporte:Contenedor");
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
        NodeList listaFiguraTransporte = nodoCartaPorte.getElementsByTagName("cartaporte:FiguraTransporte");
        if (listaFiguraTransporte.getLength() == 0) {
            return null;
        }
        Element nodoFiguraTransporte = (Element) listaFiguraTransporte.item(0);
        figuraTransporte.setOperadores(ObtenerOperadores(nodoFiguraTransporte));
        figuraTransporte.setPropietario(ObtenerPropietario(nodoFiguraTransporte));
        figuraTransporte.setArrendatario(ObtenerArrendatario(nodoFiguraTransporte));
        figuraTransporte.setNotificado(ObtenerNotificado(nodoFiguraTransporte));
        if (nodoFiguraTransporte.hasAttribute("CveTransporte")) {
            figuraTransporte.setCveTransporte(nodoFiguraTransporte.getAttribute("CveTransporte"));
        }
        return figuraTransporte;
    }

    private static ArrayList<Operadores> ObtenerOperadores(Element nodoFiguraTransporte) {

        ArrayList<Operadores> listaOperadores = new ArrayList<>();
        NodeList nlistaOperadores = nodoFiguraTransporte.getElementsByTagName("cartaporte:Operadores");
        if (nlistaOperadores.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < nlistaOperadores.getLength(); i++) {
            Element nodoOperadores = (Element) nlistaOperadores.item(i);
            Operadores operadores = new Operadores();
            operadores.setOperador(ObtenerOperador(nodoOperadores));
            listaOperadores.add(operadores);
        }
        return listaOperadores;
    }

    private static ArrayList<Operador> ObtenerOperador(Element nodoOperadores) {
        ArrayList<Operador> listaOperador = new ArrayList<>();
        NodeList nlistaOperador = nodoOperadores.getElementsByTagName("cartaporte:Operador");
        for (int i = 0; i < nlistaOperador.getLength(); i++) {
            Element item = (Element) nlistaOperador.item(i);
            Operador operador = new Operador();
            operador.setDomicilio(ObtenerDomicilio(item));
            if (item.hasAttribute("RFCOperador")) {
                operador.setRFCOperador(item.getAttribute("RFCOperador"));
            }
            if (item.hasAttribute("NumLicencia")) {
                operador.setNumLicencia(item.getAttribute("NumLicencia"));
            }
            if (item.hasAttribute("NombreOperador")) {
                operador.setNombreOperador(item.getAttribute("NombreOperador"));
            }
            if (item.hasAttribute("NumRegIdTribOperador")) {
                operador.setNumRegIdTribOperador(item.getAttribute("NumRegIdTribOperador"));
            }
            if (item.hasAttribute("ResidenciaFiscalOperador")) {
                operador.setResidenciaFiscalOperador(item.getAttribute("ResidenciaFiscalOperador"));
            }
            listaOperador.add(operador);
        }
        return listaOperador;
    }

    private static ArrayList<Propietario> ObtenerPropietario(Element nodoFiguraTransporte) {
        ArrayList<Propietario> listaPropietario = new ArrayList<>();
        NodeList nlistaPropietario = nodoFiguraTransporte.getElementsByTagName("cartaporte:Propietario");
        for (int i = 0; i < nlistaPropietario.getLength(); i++) {
            Element item = (Element) nlistaPropietario.item(i);
            Propietario propietario = new Propietario();
            propietario.setDomicilio(ObtenerDomicilio(item));
            if (item.hasAttribute("RFCPropietario")) {
                propietario.setRFCPropietario(item.getAttribute("RFCPropietario"));
            }
            if (item.hasAttribute("NombrePropietario")) {
                propietario.setNombrePropietario(item.getAttribute("NombrePropietario"));
            }
            if (item.hasAttribute("NumRegIdTribPropietario")) {
                propietario.setNumRegIdTribPropietario(item.getAttribute("NumRegIdTribPropietario"));
            }
            if (item.hasAttribute("ResidenciaFiscalPropietario")) {
                propietario.setResidenciaFiscalPropietario(item.getAttribute("ResidenciaFiscalPropietario"));
            }
            listaPropietario.add(propietario);
        }
        return listaPropietario;
    }

    private static ArrayList<Arrendatario> ObtenerArrendatario(Element nodoFiguraTransporte) {
        ArrayList<Arrendatario> listaArrendatario = new ArrayList<>();
        NodeList nlistaArrendatario = nodoFiguraTransporte.getElementsByTagName("cartaporte:Arrendatario");
        for (int i = 0; i < nlistaArrendatario.getLength(); i++) {
            Element item = (Element) nlistaArrendatario.item(i);
            Arrendatario arrendatario = new Arrendatario();
            arrendatario.setDomicilio(ObtenerDomicilio(item));
            if (item.hasAttribute("RFCArrendatario")) {
                arrendatario.setRFCArrendatario(item.getAttribute("RFCArrendatario"));
            }
            if (item.hasAttribute("NombreArrendatario")) {
                arrendatario.setNombreArrendatario(item.getAttribute("NombreArrendatario"));
            }
            if (item.hasAttribute("NumRegIdTribArrendatario")) {
                arrendatario.setNumRegIdTribArrendatario(item.getAttribute("NumRegIdTribArrendatario"));
            }
            if (item.hasAttribute("ResidenciaFiscalArrendatario")) {
                arrendatario.setResidenciaFiscalArrendatario(item.getAttribute("ResidenciaFiscalArrendatario"));
            }
            listaArrendatario.add(arrendatario);
        }
        return listaArrendatario;
    }

    private static ArrayList<Notificado> ObtenerNotificado(Element nodoFiguraTransporte) {
        ArrayList<Notificado> listaNotificado = new ArrayList<>();
        NodeList nlistaNotificado = nodoFiguraTransporte.getElementsByTagName("cartaporte:Notificado");
        for (int i = 0; i < nlistaNotificado.getLength(); i++) {
            Element item = (Element) nlistaNotificado.item(i);
            Notificado notificado = new Notificado();
            notificado.setDomicilio(ObtenerDomicilio(item));
            if (item.hasAttribute("RFCNotificado")) {
                notificado.setRFCNotificado(item.getAttribute("RFCNotificado"));
            }
            if (item.hasAttribute("NombreNotificado")) {
                notificado.setNombreNotificado(item.getAttribute("NombreNotificado"));
            }
            if (item.hasAttribute("NumRegIdTribNotificado")) {
                notificado.setNumRegIdTribNotificado(item.getAttribute("NumRegIdTribNotificado"));
            }
            if (item.hasAttribute("ResidenciaFiscalNotificado")) {
                notificado.setResidenciaFiscalNotificado(item.getAttribute("ResidenciaFiscalNotificado"));
            }
            listaNotificado.add(notificado);
        }
        return listaNotificado;
    }

}
