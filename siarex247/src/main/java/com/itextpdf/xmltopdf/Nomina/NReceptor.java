/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Nomina;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class NReceptor {
        private String _curp = "";
        private String _numSeguridadSocial = ""; // Condicional
        private LocalDate _fechaInicioRelLaboral = LocalDate.now(); // Condicional
        private String _antiguedad = ""; // Condicional
        private String _tipoContrato = "";
        private String _sindicalizado = ""; // Opcional
        private String _tipoJornada = ""; // Condicional
        private String _tipoRegimen = "";
        private String _numEmpleado = "";
        private String _departamento = ""; // Opcional
        private String _puesto = ""; // Opcional
        private String _riesgoPuesto = ""; // Opcional
        private String _periodicidadPago = "";
        private String _banco = ""; // Condicional
        private String _cuentaBancaria = ""; // Condicional
        private double _salarioBaseCotApor = 0; // Opcional
        private double _salarioDiarioIntegrado = 0; // Opcional
        private String _claveEntFed = "";
        private ArrayList<NSubContratacion> _subContratacion; // Condicional

        public final String getCurp() {
                return _curp;
        }

        /**
         * @param value Requerido.
         * @see Atributo requerido para expresar la CURP del receptor del comprobante de
         *      nómina.
         */
        public final void setCurp(String value) {
                _curp = value;
        }

        public final String getNumSeguridadSocial() {
                return _numSeguridadSocial;
        }

        /**
         * @param value Opcional
         * @see Atributo condicional para expresar el número de seguridad social del
         *      trabajador. Se debe ingresar cuando se cuente con él, o se esté obligado
         *      conforme a otras disposiciones distintas a las fiscales.
         */
        public final void setNumSeguridadSocial(String value) {
                _numSeguridadSocial = value;
        }

        public final LocalDate getFechaInicioRelLaboral() {
                return _fechaInicioRelLaboral;
        }

        /**
         * @param value Opcional
         * @see Atributo condicional para expresar la fecha de inicio de la relación
         *      laboral entre el empleador y el empleado. Se expresa en la forma
         *      aaaa-mm-dd, de acuerdo con la especificación ISO 8601. Se debe ingresar
         *      cuando se cuente con él, o se esté obligado conforme a otras
         *      disposiciones distintas a las fiscales.
         */
        public final void setFechaInicioRelLaboral(LocalDate value) {
                _fechaInicioRelLaboral = value;
        }

        public final String getAntiguedad() {
                return _antiguedad;
        }

        /**
         * @param value Opcional
         * @see Atributo condicional para expresar la fecha de inicio de la relación
         *      laboral entre el empleador y el empleado. Se expresa en la forma
         *      aaaa-mm-dd, de acuerdo con la especificación ISO 8601. Se debe ingresar
         *      cuando se cuente con él, o se esté obligado conforme a otras
         *      disposiciones distintas a las fiscales.
         */
        public final void setAntiguedad(String value) {
                _antiguedad = value;
        }

        public final String getTipoContrato() {
                return _tipoContrato;
        }

        /**
         * @param value Requerido
         * @see Atributo requerido para expresar el tipo de contrato que tiene el
         *      trabajador.
         */
        public final void setTipoContrato(String value) {
                _tipoContrato = value;
        }

        public final String getSindicalizado() {
                return _sindicalizado;
        }

        /**
         * @param value Opcional
         * @see Atributo opcional para indicar si el trabajador está asociado a un
         *      sindicato. Si se omite se asume que no está asociado a algún sindicato
         */
        public final void setSindicalizado(String value) {
                _sindicalizado = value;
        }

        public final String getTipoJornada() {
                return _tipoJornada;
        }

        /**
         * @param value Opcional
         * @see Atributo condicional para expresar el tipo de jornada que cubre el
         *      trabajador. Se debe ingresar cuando se esté obligado conforme a otras
         *      disposiciones distintas a las fiscales.
         */
        public final void setTipoJornada(String value) {
                _tipoJornada = value;
        }

        public final String getTipoRegimen() {
                return _tipoRegimen;
        }

        /**
         * @param value Requerido
         * @see Atributo requerido para la expresión de la clave del régimen por el cual
         *      se tiene contratado al trabajador
         */
        public final void setTipoRegimen(String value) {
                _tipoRegimen = value;
        }

        public final String getNumEmpleado() {
                return _numEmpleado;
        }

        /**
         * @param value Requerido
         * @see Atributo requerido para expresar el número de empleado de 1 a 15
         *      posiciones.
         */
        public final void setNumEmpleado(String value) {
                _numEmpleado = value;
        }

        public final String getDepartamento() {
                return _departamento;
        }

        /**
         * @param value Opcional
         * @see Atributo opcional para la expresión del departamento o área a la que
         *      pertenece el trabajador <para>
         */
        public final void setDepartamento(String value) {
                _departamento = value;
        }

        public final String getPuesto() {
                return _puesto;
        }

        /**
         * @param value Opcional
         * @see Atributo opcional para la expresión del puesto asignado al empleado o
         *      actividad que realiza.<para>
         */
        public final void setPuesto(String value) {
                _puesto = value;
        }

        public final String getRiesgoPuesto() {
                return _riesgoPuesto;
        }

        /**
         * @param value Opcional
         * @see Atributo opcional para expresar la clave conforme a la Clase en que
         *      deben inscribirse los patrones, de acuerdo con las actividades que
         *      desempeñan sus trabajadores, según lo previsto en el artículo 196 del
         *      Reglamento en Materia de Afiliación Clasificación de Empresas,
         *      Recaudación y Fiscalización, o conforme con la normatividad del
         *      Instituto de Seguridad Social del trabajador. Se debe ingresar cuando se
         *      cuente con él, o se esté obligado conforme a otras disposiciones
         *      distintas a las fiscales
         */
        public final void setRiesgoPuesto(String value) {
                _riesgoPuesto = value;
        }

        public final String getPeriodicidadPago() {
                return _periodicidadPago;
        }

        /**
         * @param value Requerido
         * @see Atributo requerido para la forma en que se establece el pago del
         *      salario.
         */
        public final void setPeriodicidadPago(String value) {
                _periodicidadPago = value;
        }

        public final String getBanco() {
                return _banco;
        }

        /**
         * @param value Opcional
         * @see Atributo condicional para la expresión de la clave del Banco conforme al
         *      catálogo, donde se realiza el depósito de nómina.
         */
        public final void setBanco(String value) {
                _banco = value;
        }

        public final String getCuentaBancaria() {
                return _cuentaBancaria;
        }

        /**
         * @param value Opcional
         * @see Atributo condicional para la expresión de la cuenta bancaria a 11
         *      posiciones o número de teléfono celular a 10 posiciones o número de
         *      tarjeta de crédito, débito o servicios a 15 ó 16 posiciones o la CLABE a
         *      18 posiciones o número de monedero electrónico, donde se realiza el
         *      depósito de nómina.
         */
        public final void setCuentaBancaria(String value) {
                _cuentaBancaria = value;
        }

        public final double getSalarioBaseCotApor() {
                return _salarioBaseCotApor;
        }

        /**
         * @param value Opcional
         * @see Atributo opcional para expresar la retribución otorgada al trabajador,
         *      que se integra por los pagos hechos en efectivo por cuota diaria,
         *      gratificaciones, percepciones, alimentación, habitación, primas,
         *      comisiones, prestaciones en especie y cualquiera otra cantidad o
         *      prestación que se entregue al trabajador por su trabajo, sin considerar
         *      los conceptos que se excluyen de conformidad con el Artículo 27 de la
         *      Ley del Seguro Social, o la integración de los pagos conforme la
         *      normatividad del Instituto de Seguridad Social del trabajador. (Se
         *      emplea para pagar las cuotas y aportaciones de Seguridad Social). Se
         *      debe ingresar cuando se esté obligado conforme a otras disposiciones
         *      distintas a las fiscales.
         */
        public final void setSalarioBaseCotApor(double value) {
                _salarioBaseCotApor = value;
        }

        public final double getSalarioDiarioIntegrado() {
                return _salarioDiarioIntegrado;
        }

        /**
         * @param value Opcional
         * @see Atributo opcional para expresar el salario que se integra con los pagos
         *      hechos en efectivo por cuota diaria, gratificaciones, percepciones,
         *      habitación, primas, comisiones, prestaciones en especie y cualquier otra
         *      cantidad o prestación que se entregue al trabajador por su trabajo, de
         *      conformidad con el Art. 84 de la Ley Federal del Trabajo. (Se utiliza
         *      para el cálculo de las indemnizaciones). Se debe ingresar cuando se esté
         *      obligado conforme a otras disposiciones distintas a las fiscales.
         */
        public final void setSalarioDiarioIntegrado(double value) {
                _salarioDiarioIntegrado = value;
        }

        public final String getClaveEntFed() {
                return _claveEntFed;
        }

        /**
         * @param value Requerido
         * @see Atributo requerido para expresar la clave de la entidad federativa en
         *      donde el receptor del recibo prestó el servicio.
         */
        public final void setClaveEntFed(String value) {
                _claveEntFed = value;
        }

        public final ArrayList<NSubContratacion> getSubContratacion() {
                return _subContratacion;
        }

        /**
         * @param value Opcional
         * @see Nodo condicional para expresar la lista de las personas que los
         *      subcontrataron
         */
        public final void setSubContratacion(ArrayList<NSubContratacion> value) {
                _subContratacion = value;
        }

}
