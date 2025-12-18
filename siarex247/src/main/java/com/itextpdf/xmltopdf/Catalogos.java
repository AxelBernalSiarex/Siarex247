package com.itextpdf.xmltopdf;

public final class Catalogos {
    // <editor-fold defaultstate="collapsed" desc="Catalogos SAT">
    public static String ObtenerRegimenFiscal(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "601":
                    return "General de Ley Personas Morales";
                case "603":
                    return "Personas Morales con Fines no Lucrativos";
                case "605":
                    return "Sueldos y Salarios e Ingresos Asimilados a Salarios";
                case "606":
                    return "Arrendamiento";
                case "607":
                    return "Régimen de Enajenación o Adquisición de Bienes";
                case "608":
                    return "Demás ingresos";
                case "609":
                    return "Consolidación";
                case "610":
                    return "Residentes en el Extranjero sin Establecimiento Permanente en México";
                case "611":
                    return "Ingresos por Dividendos (socios y accionistas)";
                case "612":
                    return "Personas Físicas con Actividades Empresariales y Profesionales";
                case "614":
                    return "Ingresos por intereses";
                case "615":
                    return "Régimen de los ingresos por obtención de premios";
                case "616":
                    return "Sin obligaciones fiscales";
                case "620":
                    return "Sociedades Cooperativas de Producción que optan por diferir sus ingresos";
                case "621":
                    return "Incorporación Fiscal";
                case "622":
                    return "Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras";
                case "623":
                    return "Opcional para Grupos de Sociedades";
                case "624":
                    return "Coordinados";
                case "625":
                    return "Régimen de las Actividades Empresariales con ingresos a través de Plataformas Tecnológicas";
                case "626":
                    return "Régimen Simplificado de Confianza";
                case "628":
                    return "Hidrocarburos";
                case "629":
                    return "De los Regímenes Fiscales Preferentes y de las Empresas Multinacionales";
                case "630":
                    return "Enajenación de acciones en bolsa de valores";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerTipoNomina(String tipoNomina) {
        tipoNomina = tipoNomina.toUpperCase();
        if (null == tipoNomina) {
            return "-";
        } else {
            switch (tipoNomina) {
                case "O":
                    return "Nómina ordinaria";
                case "E":
                    return "Nómina extraordinaria";
                default:
                    return "-";
            }
        }
    }

    public static String ObtenerFormaPago(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "01":
                    return "Efectivo";
                case "02":
                    return "Cheque nominativo";
                case "03":
                    return "Transferencia electrónica de fondos";
                case "04":
                    return "Tarjeta de crédito";
                case "05":
                    return "Monedero electrónico";
                case "06":
                    return "Dinero electrónico";
                case "08":
                    return "Vales de despensa";
                case "12":
                    return "Dación en pago";
                case "13":
                    return "Pago por subrogación";
                case "14":
                    return "Pago por consignación";
                case "15":
                    return "Condonación";
                case "17":
                    return "Compensación";
                case "23":
                    return "Novación";
                case "24":
                    return "Confusión";
                case "25":
                    return "Remisión de deuda";
                case "26":
                    return "Prescripción o caducidad";
                case "27":
                    return "A satisfacción del acreedor";
                case "28":
                    return "Tarjeta de débito";
                case "29":
                    return "Tarjeta de servicios";
                case "30":
                    return "Aplicación de anticipos";
                case "99":
                    return "Por definir";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerImpuesto(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "001":
                    return "ISR";
                case "002":
                    return "IVA";
                case "003":
                    return "IEPS";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerMetodoPago(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "PUE":
                    return "Pago en una sola exhibición";
                case "PPD":
                    return "Pago en parcialidades o diferido";
                default:
                    return " ";
            }
        }
    }

    // public static String ObtenerPais(String clave) {
    // if (null == clave) {
    // return " ";
    // } else {
    // switch (clave) {
    // case "MEX":
    // return "México";
    // case "USA":
    // return "Estados Unidos";
    // default:
    // return " ";
    // }
    // }
    // }

    public static String ObtenerTipoComprobante(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "I":
                    return "Ingreso";
                case "E":
                    return "Egreso";
                case "T":
                    return "Traslado";
                case "N":
                    return "Nómina";
                case "P":
                    return "Pago";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerUsoCFDI(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "G01":
                    return "Adquisición de mercancias";
                case "G02":
                    return "Devoluciones, descuentos o bonificaciones";
                case "G03":
                    return "Gastos en general";
                case "I01":
                    return "Construcciones";
                case "I02":
                    return "Mobilario y equipo de oficina por inversiones";
                case "I03":
                    return "Equipo de transporte";
                case "I04":
                    return "Equipo de computo y accesorios";
                case "I05":
                    return "Dados, troqueles, moldes, matrices y herramental";
                case "I06":
                    return "Comunicaciones telefónicas";
                case "I07":
                    return "Comunicaciones satelitales";
                case "I08":
                    return "Otra maquinaria y equipo";
                case "D01":
                    return "Honorarios médicos, dentales y gastos hospitalarios.";
                case "D02":
                    return "Gastos médicos por incapacidad o discapacidad";
                case "D03":
                    return "Gastos funerales.";
                case "D04":
                    return "Donativos.";
                case "D05":
                    return "Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación).";
                case "D06":
                    return "Aportaciones voluntarias al SAR.";
                case "D07":
                    return "Primas por seguros de gastos médicos.";
                case "D08":
                    return "Gastos de transportación escolar obligatoria.";
                case "D09":
                    return "Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones.";
                case "D10":
                    return "Pagos por servicios educativos (colegiaturas)";
                case "P01":
                    return "Por definir";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerUnidad(String clave) {
        clave = clave.toUpperCase();
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "H87":
                    return "Pieza";
                case "LTR":
                    return "Litro";
                case "KGM":
                    return "Kilogramo";
                case "E48":
                    return "Unidad de servicio";
                case "EA":
                    return "Elemento";
                case "PR":
                    return "Par";
                case "ACT":
                    return "Actividad";
                case "LUB":
                    return "Tonelada métrica, aceite lubricante";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerMoneda(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "MXN":
                    return "Peso Mexicano";
                case "EUR":
                    return "Euro";
                case "USD":
                    return "Dolar americano";
                case "XXX":
                    return "Los códigos asignados para las transacciones en que intervenga ninguna moneda";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerTipoRelacion(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "01":
                    return "Nota de crédito de los documentos relacionados";
                case "02":
                    return "Nota de débito de los documentos relacionados";
                case "03":
                    return "Devolución de mercancía sobre facturas o traslados previos";
                case "04":
                    return "Sustitución de los CFDI previos";
                case "05":
                    return "Traslados de mercancias facturados previamente";
                case "06":
                    return "Factura generada por los traslados previos";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerTipoJornada(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "01":
                    return "Diurna";
                case "02":
                    return "Nocturna";
                case "03":
                    return "Mixta";
                case "04":
                    return "Por hora";
                case "05":
                    return "Reducida";
                case "06":
                    return "Continuada";
                case "07":
                    return "Partida";
                case "08":
                    return "Por turnos";
                case "99":
                    return "OtraJornada";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoRegimen(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "02":
                    return "Sueldos";
                case "07":
                    return "Asimilados Miembros consejos";
                case "08":
                    return "Asimilados comisionistas";
                case "09":
                    return "Asimilados Honorarios";
                case "10":
                    return "Asimilados acciones";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerTipoJornada1(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                // DIURNA
                case "01":
                    return "8";
                // NOCTURTO
                case "02":
                    return "7";
                // MIXTO
                case "03":
                    return "7.3";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerPeriodicidadPago(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "02":
                    return "Semanal ";
                case "03":
                    return "Catorcenal ";
                case "04":
                    return "Quincenal ";
                case "05":
                    return "Mensual ";
                case "10":
                    return "Decenal ";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerRiesgoPuesto(String riesgoPuesto) {
        if (null == riesgoPuesto) {
            return " ";
        } else {
            switch (riesgoPuesto) {
                case "1":
                    return "Clase I";
                case "2":
                    return "Clase II";
                case "3":
                    return "Clase II";
                case "4":
                    return "Clase II";
                case "5":
                    return "Clase II";
                default:
                    return " ";
            }
        }

    }

    public static String ObtenerTipoContrato(String clave) {
        if (null == clave) {
            return " ";
        } else {
            switch (clave) {
                case "01":
                    return "Contrato de trabajo por tiempo indeterminado";
                case "02":
                    return "Contrato de trabajo para obra determinada";
                case "03":
                    return "Contrato de trabajo por tiempo determinado";
                case "04":
                    return "Contrato de trabajo por temporada";
                case "05":
                    return "Contrato de trabajo sujeto a prueba";
                case "06":
                    return "Contrato de trabajo con capacitación inicial";
                case "07":
                    return "Modalidad de contratación por pago de hora laborada";
                case "08":
                    return "Modalidad de trabajo por comisión laboral";
                case "09":
                    return "Modalidades de contratación donde no existe relación de trabajo";
                case "10":
                    return "Jubilación, pensión, retiro.";
                case "99":
                    return "Otro contrato";
                default:
                    return " ";
            }
        }
    }

    public static String ObtenerTipoPercepcion(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "001":
                    return "Sueldos, Salarios  Rayas y Jornales";
                case "002":
                    return "Gratificación Anual (Aguinaldo)";
                case "003":
                    return "Participación de los Trabajadores en las Utilidades PTU";
                case "004":
                    return "Reembolso de Gastos Médicos Dentales y Hospitalarios";
                case "005":
                    return "Fondo de Ahorro";
                case "006":
                    return "Caja de ahorro";
                case "009":
                    return "Contribuciones a Cargo del Trabajador Pagadas por el Patrón";
                case "010":
                    return "Premios por puntualidad";
                case "011":
                    return "Prima de Seguro de vida";
                case "012":
                    return "Seguro de Gastos Médicos Mayores";
                case "013":
                    return "Cuotas Sindicales Pagadas por el Patrón";
                case "014":
                    return "Subsidios por incapacidad";
                case "015":
                    return "Becas para trabajadores y/o hijos";
                case "019":
                    return "Horas extra";
                case "020":
                    return "Prima dominical";
                case "021":
                    return "Prima vacacional";
                case "022":
                    return "Prima por antigüedad";
                case "023":
                    return "Pagos por separación";
                case "024":
                    return "Seguro de retiro";
                case "025":
                    return "Indemnizaciones";
                case "026":
                    return "Reembolso por funeral";
                case "027":
                    return "Cuotas de seguridad social pagadas por el patrón";
                case "028":
                    return "Comisiones";
                case "029":
                    return "Vales de despensa";
                case "030":
                    return "Vales de restaurante";
                case "031":
                    return "Vales de gasolina";
                case "032":
                    return "Vales de ropa";
                case "033":
                    return "Ayuda para renta";
                case "034":
                    return "Ayuda para artículos escolares";
                case "035":
                    return "Ayuda para anteojos";
                case "036":
                    return "Ayuda para transporte";
                case "037":
                    return "Ayuda para gastos de funeral";
                case "038":
                    return "Otros ingresos por salarios";
                case "039":
                    return "Jubilaciones, pensiones o haberes de retiro";
                case "044":
                    return "Jubilaciones, pensiones o haberes de retiro en parcialidades";
                case "045":
                    return "Ingresos en acciones o títulos valor que representan bienes";
                case "046":
                    return "Ingresos asimilados a salarios";
                case "047":
                    return "Alimentación";
                case "048":
                    return "Habitación";
                case "049":
                    return "Premios por asistencia";
                case "050":
                    return "Viáticos";
                case "051":
                    return "Pagos por gratificaciones, primas, compensaciones, recompensas u otros a extrabajadores derivados de jubilación en parcialidades";
                case "052":
                    return "Pagos que se realicen a extrabajadores que obtengan una jubilación en parcialidades derivados de la ejecución de resoluciones judicial o de un laudo";
                case "053":
                    return "Pagos que se realicen a extrabajadores que obtengan una jubilación en una sola exhibición derivados de la ejecución de resoluciones judicial o de un laudo";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoDeduccion(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "001":
                    return "Seguridad social";
                case "002":
                    return "ISR";
                case "003":
                    return "Aportaciones a retiro, cesantía en edad avanzada y vejez.";
                case "004":
                    return "Otros";
                case "005":
                    return "Aportaciones a Fondo de vivienda";
                case "006":
                    return "Descuento por incapacidad";
                case "007":
                    return "Pensión alimenticia";
                case "008":
                    return "Renta";
                case "009":
                    return "Préstamos provenientes del Fondo Nacional de la Vivienda para los Trabajadores";
                case "010":
                    return "Pago por crédito de vivienda";
                case "011":
                    return "Pago de abonos INFONACOT";
                case "012":
                    return "Anticipo de salarios";
                case "013":
                    return "Pagos hechos con exceso al trabajador";
                case "014":
                    return "Errores";
                case "015":
                    return "Pérdidas";
                case "016":
                    return "Averías";
                case "017":
                    return "Adquisición de artículos producidos por la empresa o establecimiento";
                case "018":
                    return "Cuotas para la constitución y fomento de sociedades cooperativas y de cajas de ahorro";
                case "019":
                    return "Cuotas sindicales";
                case "020":
                    return "Ausencia (Ausentismo)";
                case "021":
                    return "Cuotas obrero patronales";
                case "022":
                    return "Impuestos Locales";
                case "023":
                    return "Aportaciones voluntarias";
                case "024":
                    return "Ajuste en Gratificación Anual (Aguinaldo) Exento";
                case "025":
                    return "Ajuste en Gratificación Anual (Aguinaldo) Gravado";
                case "026":
                    return "Ajuste en Participación de los Trabajadores en las Utilidades PTU Exento";
                case "027":
                    return "Ajuste en Participación de los Trabajadores en las Utilidades PTU Gravado";
                case "028":
                    return "Ajuste en Reembolso de Gastos Médicos Dentales y Hospitalarios Exento";
                case "029":
                    return "Ajuste en Fondo de ahorro Exento";
                case "030":
                    return "Ajuste en Caja de ahorro Exento";
                case "031":
                    return "Ajuste en Contribuciones a Cargo del Trabajador Pagadas por el Patrón Exento";
                case "032":
                    return "Ajuste en Premios por puntualidad Gravado";
                case "033":
                    return "Ajuste en Prima de Seguro de vida Exento";
                case "034":
                    return "Ajuste en Seguro de Gastos Médicos Mayores Exento";
                case "035":
                    return "Ajuste en Cuotas Sindicales Pagadas por el Patrón Exento";
                case "036":
                    return "Ajuste en Subsidios por incapacidad Exento";
                case "037":
                    return "Ajuste en Becas para trabajadores y/o hijos Exento";
                case "038":
                    return "Ajuste en Horas extra Exento";
                case "039":
                    return "Ajuste en Horas extra Gravado";
                case "040":
                    return "Ajuste en Prima dominical Exento";
                case "041":
                    return "Ajuste en Prima dominical Gravado";
                case "042":
                    return "Ajuste en Prima vacacional Exento";
                case "043":
                    return "Ajuste en Prima vacacional Gravado";
                case "044":
                    return "Ajuste en Prima por antigüedad Exento";
                case "045":
                    return "Ajuste en Prima por antigüedad Gravado";
                case "046":
                    return "Ajuste en Pagos por separación Exento";
                case "047":
                    return "Ajuste en Pagos por separación Gravado";
                case "048":
                    return "Ajuste en Seguro de retiro Exento";
                case "049":
                    return "Ajuste en Indemnizaciones Exento";
                case "050":
                    return "Ajuste en Indemnizaciones Gravado";
                case "051":
                    return "Ajuste en Reembolso por funeral Exento";
                case "052":
                    return "Ajuste en Cuotas de seguridad social pagadas por el patrón Exento";
                case "053":
                    return "Ajuste en Comisiones Gravado";
                case "054":
                    return "Ajuste en Vales de despensa Exento";
                case "055":
                    return "Ajuste en Vales de restaurante Exento";
                case "056":
                    return "Ajuste en Vales de gasolina Exento";
                case "057":
                    return "Ajuste en Vales de ropa Exento";
                case "058":
                    return "Ajuste en Ayuda para renta Exento";
                case "059":
                    return "Ajuste en Ayuda para artículos escolares Exento";
                case "060":
                    return "Ajuste en Ayuda para anteojos Exento";
                case "061":
                    return "Ajuste en Ayuda para transporte Exento";
                case "062":
                    return "Ajuste en Ayuda para gastos de funeral Exento";
                case "063":
                    return "Ajuste en Otros ingresos por salarios Exento";
                case "064":
                    return "Ajuste en Otros ingresos por salarios Gravado";
                case "065":
                    return "Ajuste en Jubilaciones, pensiones o haberes de retiro en una sola exhibición Exento ";
                case "066":
                    return "Ajuste en Jubilaciones, pensiones o haberes de retiro en una sola exhibición Gravado";
                case "067":
                    return "Ajuste en Pagos por separación Acumulable";
                case "068":
                    return "Ajuste en Pagos por separación No acumulable";
                case "069":
                    return "Ajuste en Jubilaciones, pensiones o haberes de retiro en parcialidades Exento";
                case "070":
                    return "Ajuste en Jubilaciones, pensiones o haberes de retiro en parcialidades Gravado";
                case "071":
                    return "Ajuste en Subsidio para el empleo (efectivamente entregado al trabajador)";
                case "072":
                    return "Ajuste en Ingresos en acciones o títulos valor que representan bienes Exento";
                case "073":
                    return "Ajuste en Ingresos en acciones o títulos valor que representan bienes Gravado";
                case "074":
                    return "Ajuste en Alimentación Exento";
                case "075":
                    return "Ajuste en Alimentación Gravado";
                case "076":
                    return "Ajuste en Habitación Exento";
                case "077":
                    return "Ajuste en Habitación Gravado";
                case "078":
                    return "Ajuste en Premios por asistencia";
                case "079":
                    return "Ajuste en Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.";
                case "080":
                    return "Ajuste en Viáticos gravados";
                case "081":
                    return "Ajuste en Viáticos (entregados al trabajador)";
                case "082":
                    return "Ajuste en Fondo de ahorro Gravado";
                case "083":
                    return "Ajuste en Caja de ahorro Gravado";
                case "084":
                    return "Ajuste en Prima de Seguro de vida Gravado";
                case "085":
                    return "Ajuste en Seguro de Gastos Médicos Mayores Gravado";
                case "086":
                    return "Ajuste en Subsidios por incapacidad Gravado";
                case "087":
                    return "Ajuste en Becas para trabajadores y/o hijos Gravado";
                case "088":
                    return "Ajuste en Seguro de retiro Gravado";
                case "089":
                    return "Ajuste en Vales de despensa Gravado";
                case "090":
                    return "Ajuste en Vales de restaurante Gravado";
                case "091":
                    return "Ajuste en Vales de gasolina Gravado";
                case "092":
                    return "Ajuste en Vales de ropa Gravado";
                case "093":
                    return "Ajuste en Ayuda para renta Gravado";
                case "094":
                    return "Ajuste en Ayuda para artículos escolares Gravado";
                case "095":
                    return "Ajuste en Ayuda para anteojos Gravado";
                case "096":
                    return "Ajuste en Ayuda para transporte Gravado";
                case "097":
                    return "Ajuste en Ayuda para gastos de funeral Gravado";
                case "098":
                    return "Ajuste a ingresos asimilados a salarios gravados";
                case "099":
                    return "Ajuste a ingresos por sueldos y salarios gravados";
                case "100":
                    return "Ajuste en Viáticos exentos";
                case "101":
                    return "ISR Retenido de ejercicio anterior";
                case "102":
                    return "Ajuste a pagos por gratificaciones, primas, compensaciones, recompensas u otros a extrabajadores derivados de jubilación en parcialidades, gravados";
                case "103":
                    return "Ajuste a pagos que se realicen a extrabajadores que obtengan una jubilación en parcialidades derivados de la ejecución de una resolución judicial o de un laudo gravados";
                case "104":
                    return "Ajuste a pagos que se realicen a extrabajadores que obtengan una jubilación en parcialidades derivados de la ejecución de una resolución judicial o de un laudo exentos";
                case "105":
                    return "Ajuste a pagos que se realicen a extrabajadores que obtengan una jubilación en una sola exhibición derivados de la ejecución de una resolución judicial o de un laudo gravados";
                case "106":
                    return "Ajuste a pagos que se realicen a extrabajadores que obtengan una jubilación en una sola exhibición derivados de la ejecución de una resolución judicial o de un laudo exentos";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoOtroPago(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "0001":
                    return "Reintegro de ISR pagado en exceso (siempre que no haya sido enterado al SAT).";
                case "002":
                    return "Subsidio para el empleo (efectivamente entregado al trabajador).";
                case "003":
                    return "Viáticos (entregados al trabajador).";
                case "004":
                    return "Aplicación de saldo a favor por compensación anual.";
                case "005":
                    return "Reintegro de ISR retenido en exceso de ejercicio anterior (siempre que no haya sido enterado al SAT).";
                case "999":
                    return "Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerEstatusUUID(String clave) {
        if (null == clave) {
            return "";
        } else {
            switch (clave) {
                case "201":
                    return "Cancelación aceptada";
                case "202":
                    return "Cancelacion no aceptada";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoHoras(Integer tipoHora) {
        if (null == tipoHora) {
            return "";
        } else {
            switch (tipoHora) {
                case 1:
                    return "Dobles";
                case 2:
                    return "Triples";
                case 3:
                    return "Simples";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerClaveTransporte(String tipoHora) {
        if (null == tipoHora) {
            return "";
        } else {
            switch (tipoHora) {
                case "01":
                    return "Autotransporte";
                case "02":
                    return "Transporte Marítimo";
                case "03":
                    return "Transporte Aéreo";
                case "04":
                    return "Transporte Ferroviario";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoEstacion(String tipoEstacion) {
        if (null == tipoEstacion) {
            return "";
        } else {
            switch (tipoEstacion) {
                case "01":
                    return "Origen Nacional";
                case "02":
                    return "Intermedia";
                case "03":
                    return "Destino Final Nacional";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerConfigVehicular(String configVehicular) {
        if (null == configVehicular) {
            return "";
        } else {
            switch (configVehicular) {
                case "VL":
                    return "Vehículo ligero de carga (2 llantas en el eje delantero y 2 llantas en el eje trasero)";
                case "C2":
                    return "Camión Unitario (2 llantas en el eje delantero y 4 llantas en el eje trasero)";
                case "C3":
                    return "Camión Unitario (2 llantas en el eje delantero y 6 o 8 llantas en los dos ejes traseros)";
                case "C2R2":
                    return "Camión-Remolque (6 llantas en el camión y 8 llantas en remolque)";
                case "C3R2":
                    return "Camión-Remolque (10 llantas en el camión y 8 llantas en remolque)";
                case "C2R3":
                    return "Camión-Remolque (6 llantas en el camión y 12 llantas en remolque)";
                case "C3R3":
                    return "Camión-Remolque (10 llantas en el camión y 12 llantas en remolque)";
                case "T2S1":
                    return "Tractocamión Articulado (6 llantas en el tractocamión, 4 llantas en el semirremolque)";
                case "T2S2":
                    return "Tractocamión Articulado (6 llantas en el tractocamión, 8 llantas en el semirremolque)";
                case "T2S3":
                    return "Tractocamión Articulado (6 llantas en el tractocamión, 12 llantas en el semirremolque)";
                case "T3S1":
                    return "Tractocamión Articulado (10 llantas en el tractocamión, 4 llantas en el semirremolque)";
                case "T3S2":
                    return "Tractocamión Articulado (10 llantas en el tractocamión, 8 llantas en el semirremolque)";
                case "T3S3":
                    return "Tractocamión Articulado (10 llantas en el tractocamión, 12 llantas en el semirremolque)";
                case "T2S1R2":
                    return "Tractocamión Semirremolque-Remolque (6 llantas en el tractocamión, 4 llantas en el semirremolque y 8 llantas en el remolque)";
                case "T2S2R2":
                    return "Tractocamión Semirremolque-Remolque (6 llantas en el tractocamión, 8 llantas en el semirremolque y 8 llantas en el remolque)";
                case "T2S1R3":
                    return "Tractocamión Semirremolque-Remolque (6 llantas en el tractocamión, 4 llantas en el semirremolque y 12 llantas en el remolque)";
                case "T3S1R2":
                    return "Tractocamión Semirremolque-Remolque (10 llantas en el tractocamión, 4 llantas en el semirremolque y 8 llantas en el remolque)";
                case "T3S1R3":
                    return "Tractocamión Semirremolque-Remolque (10 llantas en el tractocamión, 4 llantas en el semirremolque y 12 llantas en el remolque)";
                case "T3S2R2":
                    return "Tractocamión Semirremolque-Remolque (10 llantas en el tractocamión, 8 llantas en el semirremolque y 8 llantas en el remolque)";
                case "T3S2R3":
                    return "Tractocamión Semirremolque-Remolque (10 llantas en el tractocamión, 8 llantas en el semirremolque y 12 llantas en el remolque)";
                case "T3S2R4":
                    return "Tractocamión Semirremolque-Remolque (10 llantas en el tractocamión, 8 llantas en el semirremolque y 16 llantas en el remolque)";
                case "T2S2S2":
                    return "Tractocamión Semirremolque-Semirremolque (6 llantas en el tractocamión, 8 llantas en el semirremolque delantero y 8 llantas en el semirremolque trasero)";
                case "T3S2S2":
                    return "Tractocamión Semirremolque-Semirremolque (10 llantas en el tractocamión, 8 llantas en el semirremolque delantero y 8 llantas en el semirremolque trasero)";
                case "T3S3S2":
                    return "Tractocamión Semirremolque-Semirremolque (10 llantas en el tractocamión, 12 llantas en el semirremolque delantero y 8 llantas en el semirremolque trasero)";
                case "OTROEVGP":
                    return "Especializado de carga Voluminosa y/o Gran Peso";
                case "OTROSG":
                    return "Servicio de Grúas";
                case "GPLUTA":
                    return "Grúa de Pluma Tipo A";
                case "GPLUTB":
                    return "Grúa de Pluma Tipo B";
                case "GPLUTC":
                    return "Grúa de Pluma Tipo C";
                case "GPLUTD":
                    return "Grúa de Pluma Tipo D";
                case "GPLATA":
                    return "Grúa de Plataforma Tipo A";
                case "GPLATB":
                    return "Grúa de Plataforma Tipo B";
                case "GPLATC":
                    return "Grúa de Plataforma Tipo C";
                case "GPLATD":
                    return "Grúa de Plataforma Tipo D";

                default:
                    return "";
            }
        }
    }

    public static String ObtenerSubTipoRem(String tipoEstacion) {
        if (null == tipoEstacion) {
            return "";
        } else {
            switch (tipoEstacion) {
                case "CTR001":
                    return "Caballete";
                case "CTR002":
                    return "Caja";
                case "CTR003":
                    return "Caja Abierta";
                case "CTR004":
                    return "Caja Cerrada";
                case "CTR005":
                    return "Caja De Recolección Con Cargador Frontal";
                case "CTR006":
                    return "Caja Refrigerada";
                case "CTR007":
                    return "Caja Seca";
                case "CTR008":
                    return "Caja Transferencia";
                case "CTR009":
                    return "Cama Baja o Cuello Ganso";
                case "CTR010":
                    return "Chasis Portacontenedor";
                case "CTR011":
                    return "Convencional De Chasis";
                case "CTR012":
                    return "Equipo Especial";
                case "CTR013":
                    return "Estacas";
                case "CTR014":
                    return "Góndola Madrina";
                case "CTR015":
                    return "Grúa Industrial";
                case "CTR016":
                    return "Grúa ";
                case "CTR017":
                    return "Integral";
                case "CTR018":
                    return "Jaula";
                case "CTR019":
                    return "Media Redila";
                case "CTR020":
                    return "Pallet o Celdillas";
                case "CTR021":
                    return "Plataforma";
                case "CTR022":
                    return "Plataforma Con Grúa";
                case "CTR023":
                    return "Plataforma Encortinada";
                case "CTR024":
                    return "Redilas";
                case "CTR025":
                    return "Refrigerador";
                case "CTR026":
                    return "Revolvedora";
                case "CTR027":
                    return "Semicaja";
                case "CTR028":
                    return "Tanque";
                case "CTR029":
                    return "Tolva";
                case "CTR031":
                    return "Volteo";
                case "CTR032":
                    return "Volteo Desmontable";

                default:
                    return "";
            }
        }
    }

    public static String ObtenerConfigMaritima(String tipoEstacion) {
        if (null == tipoEstacion) {
            return "";
        } else {
            switch (tipoEstacion) {
                case "B01":
                    return "Abastecedor";
                case "B02":
                    return "Barcaza";
                case "B03":
                    return "Granelero";
                case "B04":
                    return "Porta Contenedor";
                case "B05":
                    return "Draga";
                case "B06":
                    return "Pesquero";
                case "B07":
                    return "Carga General";
                case "B08":
                    return "Quimiqueros";
                case "B09":
                    return "Transbordadores";
                case "B10":
                    return "Carga RoRo";
                case "B11":
                    return "Investigación";
                case "B12":
                    return "Tanquero";
                case "B13":
                    return "Gasero";
                case "B14":
                    return "Remolcador";
                case "B15":
                    return "Extraordinaria especialización";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoPermiso(String tipoPermiso) {
        if (null == tipoPermiso) {
            return "";
        } else {
            switch (tipoPermiso) {
                case "TPAF01":
                    return "Autotransporte Federal de carga general.";
                case "TPAF02":
                    return "Transporte privado de carga.";
                case "TPAF03":
                    return "Autotransporte Federal de Carga Especializada de materiales y residuos peligrosos.";
                case "TPAF04":
                    return "Transporte de automóviles sin rodar en vehículo tipo góndola.";
                case "TPAF05":
                    return "Transporte de carga de gran peso y/o volumen de hasta 90 toneladas.";
                case "TPAF06":
                    return "Transporte de carga especializada de gran peso y/o volumen de más 90 toneladas.";
                case "TPAF07":
                    return "Transporte Privado de materiales y residuos peligrosos.";
                case "TPAF08":
                    return "Autotransporte internacional de carga de largo recorrido.";
                case "TPAF09":
                    return "Autotransporte internacional de carga especializada de materiales y residuos peligrosos de largo recorrido.";
                case "TPAF10":
                    return "Autotransporte Federal de Carga General cuyo ámbito de aplicación comprende la franja fronteriza con Estados Unidos.";
                case "TPAF11":
                    return "Autotransporte Federal de Carga Especializada cuyo ámbito de aplicación comprende la franja fronteriza con Estados Unidos.";
                case "TPAF12":
                    return "Servicio auxiliar de arrastre en las vías generales de comunicación.";
                case "TPAF13":
                    return "Servicio auxiliar de servicios de arrastre, arrastre y salvamento, y depósito de vehículos en las vías generales de comunicación.";
                case "TPAF14":
                    return "Servicio de paquetería y mensajería en las vías generales de comunicación.";
                case "TPAF15":
                    return "Transporte especial para el tránsito de grúas industriales con peso máximo de 90 toneladas.";
                case "TPAF16":
                    return "Servicio federal para empresas arrendadoras servicio público federal.";
                case "TPAF17":
                    return "Empresas trasladistas de vehículos nuevos.";
                case "TPAF18":
                    return "Empresas fabricantes o distribuidoras de vehículos nuevos.";
                case "TPAF19":
                    return "Autorización expresa para circular en los caminos y puentes de jurisdicción federal con configuraciones de tractocamión doblemente articulado.";
                case "TPAF20":
                    return "Autotransporte Federal de Carga Especializada de fondos y valores.";
                case "TPTM01":
                    return "Permiso temporal para navegación de cabotaje";
                case "TPTA01":
                    return "Concesión y/o autorización para el servicio regular nacional y/o internacional para empresas mexicanas";
                case "TPTA02":
                    return "Permiso para el servicio aéreo regular de empresas extranjeras";
                case "TPTA03":
                    return "Permiso para el servicio nacional e internacional no regular de fletamento";
                case "TPTA04":
                    return "Permiso para el servicio nacional e internacional no regular de taxi aéreo";
                case "TPXX00":
                    return "Permiso no contemplado en el catálogo.";

                default:
                    return "";
            }
        }

    }

    public static String ObtenerTipoCarga(String tipoCarga) {
        if (null == tipoCarga) {
            return "";
        } else {
            switch (tipoCarga) {
                case "CGS":
                    return "Carga General Suelta";
                case "CGC":
                    return "Carga General Contenerizada";
                case "GMN":
                    return "Gran Mineral";
                case "GAG":
                    return "Granel Agrícola";
                case "OFL":
                    return "Otros Fluidos";
                case "PYD":
                    return "Petróleo y Derivados";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerContenedorMaritimo(String contenedorMaritimo) {
        if (null == contenedorMaritimo) {
            return "";
        } else {
            switch (contenedorMaritimo) {
                case "CM001":
                    return "Contenedores refrigerados de 20FT";
                case "CM002":
                    return "Contenedores refrigerados de 40FT";
                case "CM003":
                    return "Contenedores estándar de 8FT";
                case "CM004":
                    return "Contenedores estándar de 10FT";
                case "CM005":
                    return "Contenedores estándar de 20FT";
                case "CM006":
                    return "Contenedores estándar de 40FT";
                case "CM007":
                    return "Contenedores Open Side";
                case "CM008":
                    return "Contenedor Isotanque";
                case "CM009":
                    return "Contenedor flat racks";
                default:
                    return "";
            }
        }

    }

    public static String ObtenerTipoServicio(String tipoServicio) {
        if (null == tipoServicio) {
            return "";
        } else {
            switch (tipoServicio) {
                case "TS01":
                    return "Carros Ferroviarios";
                case "TS02":
                    return "Carros Ferroviarios intermodal";
                case "TS03":
                    return "Tren unitario de carros ferroviarios";
                case "TS04":
                    return "Tren unitario Intermodal";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoCarro(String tipoCarro) {
        if (null == tipoCarro) {
            return "";
        } else {
            switch (tipoCarro) {
                case "TC01":
                    return "Furgón";
                case "TC02":
                    return "Góndola";
                case "TC03":
                    return "Tolva";
                case "TC04":
                    return "Tanque";
                case "TC05":
                    return "Plataforma Intermodal";
                case "TC06":
                    return "Plataforma de Uso General ";
                case "TC07":
                    return "Plataforma Automotriz";
                case "TC08":
                    return "Locomotora";
                case "TC09":
                    return "Carro Especial";
                case "TC10":
                    return "Pasajeros";
                case "TC11":
                    return "Mantenimiento de Vía";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerTipoContenedor(String tipoContenedor) {
        if (null == tipoContenedor) {
            return "";
        } else {
            switch (tipoContenedor) {
                case "TC01":
                    return "20'";
                case "TC02":
                    return "40'";
                case "TC03":
                    return "45'";
                case "TC04":
                    return "48'";
                case "TC05":
                    return "53'";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerObjetoImp(String objetoImp) {
        if (null == objetoImp) {
            return "";
        } else {
            switch (objetoImp) {
                case "01":
                    return "No objeto de impuesto.";
                case "02":
                    return "Sí objeto de impuesto.";
                case "03":
                    return "Sí objeto del impuesto y no obligado al desglose.";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerExportacion(String exportacion) {
        if (null == exportacion) {
            return "";
        } else {
            switch (exportacion) {
                case "01":
                    return "No aplica";
                case "02":
                    return "Definitiva";
                case "03":
                    return "Temporal";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerPeriodicidad(String periodicidad) {
        if (null == periodicidad) {
            return "";
        } else {
            switch (periodicidad) {
                case "01":
                    return "Diario";
                case "02":
                    return "Semanal";
                case "03":
                    return "Quincenal";
                case "04":
                    return "Mensual";
                case "05":
                    return "Bimestral";
                default:
                    return "";
            }
        }
    }

    public static String ObtenerMeses(String meses) {
        if (null == meses) {
            return "";
        } else {
            switch (meses) {
                case "01":
                    return "Enero";
                case "02":
                    return "Febrero";
                case "03":
                    return "Marzo";
                case "04":
                    return "Abril";
                case "05":
                    return "Mayo";
                case "06":
                    return "Junio";
                case "07":
                    return "Julio";
                case "08":
                    return "Agosto";
                case "09":
                    return "Septiembre";
                case "10":
                    return "Octubre";
                case "11":
                    return "Noviembre";
                case "12":
                    return "Diciembre";
                case "13":
                    return "Enero-Febrero";
                case "14":
                    return "Marzo-Abril";
                case "15":
                    return "Mayo-Junio";
                case "16":
                    return "Julio-Agosto";
                case "17":
                    return "Septiembre-Octubre";
                case "18":
                    return "Noviembre-Diciembre";
                default:
                    return "";
            }
        }
    }

    // </editor-fold>
}
