<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
	
	<script src='/siarex247/jsp/estatusXML/exportarXML/exportarXML.js'></script>    
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="EXPORTARXML_TITLE1">Exportar XML</h5>
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>

	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">

		<form id="frmExportXML" class="was-validated" novalidate>
		   <div id="overSeccionCA" class="overlay" style="display: none;text-align: right;">
				<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
			</div>
			
			<div class="p-2 pb-0">
				<div class="mb-2 row">
					<label class="col-sm-2 col-form-label" for="tipoExportacion" id="EXPORTARXML_ETQ1">Tipo de Exportacion</label>
					<div class="col-sm-4">
						<div class="form-group">
							<select class="form-select" id="tipoExportacion" name="tipoExportacion" onchange="cambiarModo(this.value);">
								 <option value="TEXTO" id="EXPORTARXML_ETQ2">Por Archivo de Texto</option>
                                 <option value="RFC" id="EXPORTARXML_ETQ3">Por RFC</option>
                                 <option value="XML" id="EXPORTARXML_ETQ4">Por XML</option>
                                 
							</select>
						</div>
					</div>
				</div>
				<div id="divBusquedaRFC">
				<!-- 
				     <div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="rfc" id="EXPORTARXML_ETQ5">RFC</label>
						<div class="col-sm-4">
							<input id="rfcProveedor" name="rfcProveedor" class="form-control" type="text" maxlength="15"/>
						</div>
					</div>
				-->
				
					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="rfcProveedor" id="EXPORTARXML_ETQ5">RFC</label>
						<div class="col-sm-4">
							<div class="form-group">
							<select class="form-select" id="rfcProveedor" name="rfcProveedor">
							    <option value="">Seleccione una opci&oacute;n ...</option>
							</select>
							</div>
						</div>
					</div>
								
				
				
				     <div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="fechaInicial" id="EXPORTARXML_ETQ6">Fecha Inicio</label>
		            	<div class="col-sm-4">
		        	       <div class="form-group">
		    	             <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
			               </div>
			            </div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="fechaInicial" id="EXPORTARXML_ETQ7">Fecha Final</label>
		            	<div class="col-sm-4">
		        	       <div class="form-group">
		    	             <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
			               </div>
			            </div>
					</div>
					
				
				</div>
				
				<div class="mb-2 row" id="divBusquedaXML">
					<label class="col-sm-2 col-form-label" for="filesXML" id="EXPORTARXML_ETQ8">Archivos XML </label>
					<div class="col-sm-4">
						<input id="filesXML" name="filesXML" class="form-control" type="file" accept="text/xml" multiple/>
					</div>
				</div>
				
				
				<div class="mb-2 row" id="divBusquedaTxt">
					<label class="col-sm-2 col-form-label" for="fileTXT" id="EXPORTARXML_ETQ9">Archivo TXT </label>
					<div class="col-sm-4">
						<input id="fileTXT" name="fileTXT" class="form-control" type="file" accept="text/plain"/>
					</div>
				</div>

				<div class="mb-2 row">
					<label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ10">Agrupar en Folder</label>
					<div class="col-sm-3">
						<div class="form-check form-switch">
							<input class="form-check-input" id="agruparFacturasSW" type="checkbox"   onchange="habilitarAgrupacion();" />
						</div>
					</div>
				</div>

				<div class="mb-2 row">
					<label class="col-sm-2 col-form-label" for="modoAgrupar" id="EXPORTARXML_ETQ11">Modo de Agrupaci&oacute;n</label>
					<div class="col-sm-4">
						<div class="form-group">
							<select class="form-select" id="modoAgrupar" name="modoAgrupar" disabled="disabled">
								 <option value="NONE" selected=""></option>
                                 <option value="1" id="EXPORTARXML_ETQ12">PROVEEDOR</option>
                                 <option value="2" id="EXPORTARXML_ETQ13">TIPO DE MONEDA</option>
                                 <option value="3" id="EXPORTARXML_ETQ14">TIPO DE MONEDA Y PROVEEDOR</option>
                                 <option value="4" id="EXPORTARXML_ETQ15">PROVEEDOR Y TIPO DE MONEDA</option>
							</select>
						</div>
					</div>
				</div>



				<div class="mb-2 row">
					<label class="col-sm-2 col-form-label" for="CORREO_RESPONSABLE" id="EXPORTARXML_ETQ16">Correo de notificaci&oacute;n al responsable</label>
					<div class="col-sm-4">
						<input id="CORREO_RESPONSABLE" name="correoResponsable" class="form-control" type="email" required />
					</div>
				</div>


				<div class="mb-2 row">
					<label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ17">Descargar Facturas </label>
					<div class="col-sm-1">
						<div class="form-check form-switch">
							<input class="form-check-input" id="descargarFacturas" name="descargarFacturas" type="checkbox"  />
						</div>
					</div>
					
					<label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ18">Validar Facturas en SAT</label>
					<div class="col-sm-1">
						<div class="form-check form-switch">
							<input class="form-check-input" id="validarSAT" name="validarSAT" type="checkbox"  />
						</div>
					</div>
					
				</div>

				<div class="mb-2 row">
				    <label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ19">Descargar Complemento </label>
					<div class="col-sm-1">
						<div class="form-check form-switch">
							<input class="form-check-input" id="descargarComplemento" name="descargarComplemento" type="checkbox"  />
						</div>
					</div>
					
					<label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ20">Validar Complemento en SAT</label>
					<div class="col-sm-1">
						<div class="form-check form-switch">
							<input class="form-check-input" id="complementoSAT" name="complementoSAT" type="checkbox"  />
						</div>
					</div>
				</div>
				
				
				<div class="mb-2 row">
				    <label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ21">Descargar Nota de Cr&eacute;dito </label>
					<div class="col-sm-1">
						<div class="form-check form-switch">
							<input class="form-check-input" id="descargarNotaCredito" name="descargarNotaCredito" type="checkbox"  />
						</div>
					</div>
					
					<label class="col-sm-2 col-form-label" for="" id="EXPORTARXML_ETQ22">Validar Nota de Cr&eacute;dito en SAT</label>
					<div class="col-sm-1">
						<div class="form-check form-switch">
							<input class="form-check-input" id="notaCreditoSAT" name="notaCreditoSAT" type="checkbox"  />
						</div>
					</div>
				</div>
				

				<div class="col-md-6 text-center">
					<button type="submit" id="btnProcesar_XML" class="btn btn-primary">Procesar</button>
				</div>

			</div>

		</form>

	</div>
</div>


<script type="text/javascript">

var MENSAJE_EXITOSO = null;
	$(document).ready(function () {

		
		$( "#divBusquedaRFC" ).hide();
		$( "#divBusquedaXML" ).hide();
		
		$('#modoAgrupar').select2({
			theme: "bootstrap-5",
		});
		
		$('#tipoExportacion').select2({
			theme: "bootstrap-5",
		});

		$("#frmExportXML").on("submit", function (event) {
			$(this).addClass("was-validated");
		});

		
		
   	 flatpickr(fechaInicial, {
  	      minDate: '1920-01-01', 
  	      //dateFormat : "d-m-Y", 
  	      dateFormat : "Y-m-d",
  	      locale: {
  	        firstDayOfWeek: 1,
  	        weekdays: {
  	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
  	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],         
  	        }, 
  	        months: {
  	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
  	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  	        },
  	      },
  	    }); 

   	flatpickr(fechaFinal, {
	      minDate: '1920-01-01', 
	      //dateFormat : "d-m-Y", 
	      dateFormat : "Y-m-d",
	      locale: {
	        firstDayOfWeek: 1,
	        weekdays: {
	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],         
	        }, 
	        months: {
	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	        },
	      },
	    }); 

   	
		/* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
		$("#frmExportXML")
			.validate({
				ignore: "input[type=hidden]",
				focusInvalid: false,
				errorClass: "help-block animation-pullUp",
				errorElement: "div",
				keyUp: true,
				submitHandler: function (form) {
					guardarDatos();
				},
				errorPlacement: function (error, e) {
					e.parents(".form-group").append(error);
				},
				highlight: function (e) {
					$(e)
						.closest(".form-group")
						.removeClass("has-success has-error")
						.addClass("has-error");
				},
				success: function (e) {
					e.closest(".form-group")
						.removeClass("has-success has-error")
						.addClass("has-success");
				},
				rules: {
					select: { required: true },
				},
				messages: {
					select: { required: "error" },
				},
			})
			.resetForm();
		
		
		iniciaFormCatalogo();
		asignarCorreo();
		calcularEtiquetasExportarXML();
		
	});
	
	

	 function calcularEtiquetasExportarXML(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'EXPORTARXML'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("EXPORTARXML_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("EXPORTARXML_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("EXPORTARXML_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("EXPORTARXML_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("EXPORTARXML_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("EXPORTARXML_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("EXPORTARXML_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("EXPORTARXML_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("EXPORTARXML_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("EXPORTARXML_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("EXPORTARXML_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("EXPORTARXML_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("EXPORTARXML_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("EXPORTARXML_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("EXPORTARXML_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("EXPORTARXML_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("EXPORTARXML_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("EXPORTARXML_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("EXPORTARXML_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("EXPORTARXML_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("EXPORTARXML_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("EXPORTARXML_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("EXPORTARXML_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("btnProcesar_XML").innerHTML = BTN_PROCESAR_MENU;
						
						
						MENSAJE_EXITOSO = data.ETQ23;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasExportarXML()_1_'+thrownError);
				}
			});	
		}
	 
	 
	 function cargaProveedores() {
			try {
				$('#rfcProveedor').empty();
				$.ajax({
		           url:  '/siarex247/cumplimientoFiscal/exportarXML/comboProveedores.action',
		           type: 'POST',
		            dataType : 'json',
				    success: function(data){
				    	$('#rfcProveedor').empty();
				    	$.each(data.data, function(key, text) {
					    	$('#rfcProveedor').append($('<option></option>').attr('value', text.rfc).text(text.razonSocial));
				      	});
				    }
				});
			}catch(e) {
				alert("cargaProveedores()_"+e);
			} 
		}
	 
</script>




</html>