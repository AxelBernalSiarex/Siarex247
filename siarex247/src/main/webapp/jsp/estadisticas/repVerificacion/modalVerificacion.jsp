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
    
	<script>
      var isRTL = JSON.parse(localStorage.getItem('isRTL'));
      if (isRTL) {
        var linkDefault = document.getElementById('style-default');
        var userLinkDefault = document.getElementById('user-style-default');
        linkDefault.setAttribute('disabled', true);
        userLinkDefault.setAttribute('disabled', true);
        document.querySelector('html').setAttribute('dir', 'rtl');
      } else {
        var linkRTL = document.getElementById('style-rtl');
        var userLinkRTL = document.getElementById('user-style-rtl');
        linkRTL.setAttribute('disabled', true);
        userLinkRTL.setAttribute('disabled', true);
      }
    </script>
	
</head>



<form id="form-Reporte" class="was-validated" novalidate>
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="REPORTE_SAT_ETQ15" >Nuevo Reporte de Verificaci&oacute;n </h5>
				</div>
				<div class="p-4 pb-0">

					<div class="mb-2 row">
						<label class="col-sm-5 col-form-label" for="descripcion" id="REPORTE_SAT_Etiqueta_Descripcion">Descripci&oacute;n</label>
						<div class="col-sm-7">
	   						<input id="descripcion" name="descripcion" class="form-control" type="text"  required />
	 					</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-5 col-form-label" for="" id="REPORTE_SAT_ETQ16">Validar Factura en SAT</label>
							<div class="col-sm-7">
								<div class="form-check form-switch">
									<label class="form-check-label d-inline-block me-1" for="validarFactura" id="validarFacturaLbl">SI</label>
									<input class="form-check-input" id="validarFactura" name="validarFactura" type="checkbox" checked="" />
								</div>
							</div>
					</div>


					<div class="mb-2 row">
						<label class="col-sm-5 col-form-label" for="" id="REPORTE_SAT_ETQ17">Validar Complemento en SAT</label>
							<div class="col-sm-7">
								<div class="form-check form-switch">
									<label class="form-check-label d-inline-block me-1" for="validarComplemento" id="validarComplementoLbl">SI</label>
									<input class="form-check-input" id="validarComplemento" name="validarComplemento" type="checkbox" checked="" />
								</div>
							</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-5 col-form-label" for="" id="REPORTE_SAT_ETQ18">Validar Nota de Credito en SAT</label>
							<div class="col-sm-7">
								<div class="form-check form-switch">
									<label class="form-check-label d-inline-block me-1" for="validarNota" id="validarNotaLbl">SI</label>
									<input class="form-check-input" id="validarNota" name="validarNota" type="checkbox" checked="" />
								</div>
							</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-5 col-form-label" for="" id="REPORTE_SAT_ETQ19">Seleccionar datos por</label>
							<div class="col-sm-7">
					              <div class="form-check form-check-inline">
					                <input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="inlineRadio1" name="tipoFecha" value="ORD" type="radio" style="cursor: pointer;" />
					                <label class="form-check-label" for="radiotabla" id="REPORTE_SAT_ETQ20">Fecha de la Orden</label>
					              </div>
					              <div class="form-check form-check-inline">
					                <input class="form-check-input rounded-circle form-check-line-through form-check-input-info" id="inlineRadio2" name="tipoFecha" value="FAC"  type="radio" value="option2" style="cursor: pointer;" />
				               		 <label class="form-check-label" for="radiotabla" id="REPORTE_SAT_ETQ21">Fecha de la Factura</label>
					              </div>
					              <div class="form-check form-check-inline">
					                <input class="form-check-input rounded-circle form-check-line-through form-check-input-warning" id="inlineRadio3" name="tipoFecha" value="PAG" type="radio" style="cursor: pointer;" />
				                	<label class="form-check-label" for="radioxml" id="REPORTE_SAT_ETQ22">Fecha de Pago</label>
					              </div>
								
							</div>
					</div>
					
		            <div class="mb-2 row">
		              <label class="col-sm-5 col-form-label" for="fecIni" id="REPORTE_SAT_ETQ23">Fecha Inicial Reporte</label>
		              <div class="col-sm-7">
		                <div class="form-group">
		                  <input class="form-control datetimepicker flatpickr-input active" id="fecIni" name="fecIni" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
		                </div>
		              </div>
		            </div>
					
					<div class="mb-2 row">
		              <label class="col-sm-5 col-form-label" for="fecFin" id="REPORTE_SAT_ETQ24">Fecha Final Reporte</label>
		              <div class="col-sm-7">
		                <div class="form-group">
		                  <input class="form-control datetimepicker flatpickr-input active" id="fecFin" name="fecFin" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
		                </div>
		              </div>
		            </div>

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="REPORTE_SAT_Boton_Cerrar">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   flatpickr(fecIni, {
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
	   	          longhand: ['Enero', 'Febrero', 'Мarzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
	   
	   
	   flatpickr(fecFin, {
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
	   	          longhand: ['Enero', 'Febrero', 'Мarzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
	   
	   
	   $("#form-Reporte").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   var elements = document.querySelectorAll('[data-input-mask]');
	    elements.forEach(function (item) {
	      var userOptions = utils.getData(item, 'input-mask');
	      var defaultOptions = {
	        showMaskOnFocus: false,
	        showMaskOnHover: false,
	        jitMasking: true
	      };

	      var maskOptions = window._.merge(defaultOptions, userOptions);

	      var inputmask = new window.Inputmask(_objectSpread({}, maskOptions)).mask(item);
	      return inputmask;
	    });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Reporte').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   guardarCatalogo();
			},
           errorPlacement: function (error, e) {
        	   e.parents('.form-group').append(error);
           },
           highlight: function (e) {
               $(e).closest('.form-group').removeClass('has-success has-error').addClass('has-error');
           },
           success: function (e) {
               e.closest('.form-group').removeClass('has-success has-error').addClass('has-success');
           }, rules:  {
               select: {required: true}
           }, messages: {
               select: {required: 'error'}
           }
       }).resetForm(); 
	   	   	   
	   calcularEtiquetasReporteVerificacionModal();
	});
   
   

	 function calcularEtiquetasReporteVerificacionModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'REPORTE_SAT'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("REPORTE_SAT_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("REPORTE_SAT_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("REPORTE_SAT_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("REPORTE_SAT_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("REPORTE_SAT_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("REPORTE_SAT_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("REPORTE_SAT_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("REPORTE_SAT_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("REPORTE_SAT_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("REPORTE_SAT_ETQ24").innerHTML = data.ETQ24;
						
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("REPORTE_SAT_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasReporteVerificacionModal()_1_'+thrownError);
				}
			});	
		}
	
 </script>
</html>