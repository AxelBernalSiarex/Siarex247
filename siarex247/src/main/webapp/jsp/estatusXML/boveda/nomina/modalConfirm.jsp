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



<form id="frmConfirmEmail" class="was-validated" novalidate>
	
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="BOVEDA_NOMINA_ETQ31">Introduzca una dirección de correo electrónico</h5>
	   				
				</div>
				<div class="p-4 pb-0">
					<div class="mb-2 row">
						<label class="col-sm-12 col-form-label" for="" id="BOVEDA_NOMINA_ETQ32">La cantidad permitida para descargar los XML excede, favor de introducir un correo electrónico en el cual le haremos llegar la información requerida </label>						
					</div>
				    <div class="mb-2 row">
						<div class="col-sm-12">
	   						<input id="emailNotificacion" name="emailNotificacion" placeholder="" class="form-control" type="email" required />
	 					</div>
					</div>

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="BOVEDA_NOMINA_BTN_ProcesarConfir" class="btn btn-primary">Procesar</button>
				<button class="btn btn-secondary" id="BOVEDA_NOMINA_BTN_CancelarConfir" type="button" data-bs-dismiss="modal" id="">Cancelar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#frmConfirmEmail").on('submit', function (event) {
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
	   $('#frmConfirmEmail').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   procesaDescargarCFDI();
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
	   	   	   
	
	   calcularEtiquetasBovedaNominaConfirModal();
	   	   
	});
   
   

	 function calcularEtiquetasBovedaNominaConfirModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'BOVEDA_NOMINA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("BOVEDA_NOMINA_ETQ31").innerHTML = data.ETQ31;
						document.getElementById("BOVEDA_NOMINA_ETQ32").innerHTML = data.ETQ32;
						document.getElementById("BOVEDA_NOMINA_BTN_ProcesarConfir").innerHTML = BTN_PROCESAR_MENU;
						document.getElementById("BOVEDA_NOMINA_BTN_CancelarConfir").innerHTML = BTN_CANCELAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasBovedaNominaConfirModal()_1_'+thrownError);
				}
			});	
		}
	 
	 
	 function procesaDescargarCFDI(){
		  try{
		    // 1) UUIDs seleccionados desde la DataTable (si existe)
		    var dataSelect = (window.tablaDetalleNomina && tablaDetalleNomina.rows)
		                      ? tablaDetalleNomina.rows('.selected').data()
		                      : null;

		    var uuids = [];
		    if (dataSelect && dataSelect.length){
		      $.each(dataSelect, function(_, row){
		        if (row && row.uuid) uuids.push(String(row.uuid).trim());
		      });
		    }
		    // únicos y en formato "u1;u2;u3"
		    uuids = Array.from(new Set(uuids.filter(Boolean)));
		    if (uuids.length > 50000){
		      Swal.fire({ title: MSG_ERROR_OPERACION_MENU, html:'<p>'+LABEL_BOVEDA_TEXT4+'</p>', icon:'info' });
		      return;
		    }
		    var idRegistro = uuids.join(';'); // <-- nombre correcto

		    // 2) Filtros base (si hay filtros en thead, úsalos; si no, usa helpers globales)
		    var rfc          = ($('#rfcFilterInputN').length ? $('#rfcFilterInputN').val() : '') || (window.obtenerRFC_Nomina ? obtenerRFC_Nomina() : '');
		    var razonSocial  = ($('#razonFilterInputN').length ? $('#razonFilterInputN').val() : '') || (window.obtenerRazon_Nomina ? obtenerRazon_Nomina() : '');
		    var serie        = ($('#serieFilterInputN').length ? $('#serieFilterInputN').val() : '') || (window.obtenerSerie_Nomina ? obtenerSerie_Nomina() : '');
		    var uuid         = ($('#uuidFilterInputN').length ? $('#uuidFilterInputN').val() : '') || (window.obtenerUUID_Nomina ? obtenerUUID_Nomina() : '');
		    var folio        = ($('#folioFilter1N').length ? $('#folioFilter1N').val() : '') || (window.obtenerFolio_Nomina ? obtenerFolio_Nomina() : '');
		    var fechaInicial = ($('#dateFilter1N').length ? $('#dateFilter1N').val() : '') || (window.obtenerFechaIni_Nomina ? obtenerFechaIni_Nomina() : '');
		    var fechaFinal   = ($('#dateFilter2N').length ? $('#dateFilter2N').val() : '') || (window.obtenerFechaFin_Nomina ? obtenerFechaFin_Nomina() : '');

		    // 3) Operadores y valores DX-like
		    var rfcOperator    = ($('#rfcOperatorN').val()   || 'contains').toLowerCase();
		    var razonOperator  = ($('#razonOperatorN').val() || 'contains').toLowerCase();
		    var serieOperator  = ($('#serieOperatorN').val() || 'contains').toLowerCase();
		    var uuidOperator   = ($('#uuidOperatorN').val()  || 'contains').toLowerCase();

		    var dateOperator   = ($('#dateOperatorN').val()  || 'eq').toLowerCase();
		    var dateV1         = $('#dateFilter1N').val() || '';
		    var dateV2         = $('#dateFilter2N').val() || '';

		    var folioOperator  = ($('#folioOperatorN').val() || 'eq').toLowerCase();
		    var folioV1        = $('#folioFilter1N').val() || '';
		    var folioV2        = $('#folioFilter2N').val() || '';

		    var totalOperator  = ($('#totalOperatorN').val() || 'eq').toLowerCase();
		    var totalV1        = $('#totalFilter1N').val() || '';
		    var totalV2        = $('#totalFilter2N').val() || '';

		    var subOperator    = ($('#subOperatorN').val() || 'eq').toLowerCase();
		    var subV1          = $('#subFilter1N').val() || '';
		    var subV2          = $('#subFilter2N').val() || '';

		    var descOperator   = ($('#descOperatorN').val() || 'eq').toLowerCase();
		    var descV1         = $('#descFilter1N').val() || '';
		    var descV2         = $('#descFilter2N').val() || '';

		    var percOperator   = ($('#percOperatorN').val() || 'eq').toLowerCase();
		    var percV1         = $('#percFilter1N').val() || '';
		    var percV2         = $('#percFilter2N').val() || '';

		    var dedOperator    = ($('#dedOperatorN').val() || 'eq').toLowerCase();
		    var dedV1          = $('#dedFilter1N').val() || '';
		    var dedV2          = $('#dedFilter2N').val() || '';

		    // 4) Construye FormData a partir del form del modal (email, etc.)
		    var frm = document.getElementById("frmConfirmEmail");
		    if (!frm){
		      alert('No se encontró el formulario frmConfirmEmail en el modal.');
		      return;
		    }
		    var formData = new FormData(frm);

		    // Básicos
		    formData.append("fechaInicial", fechaInicial);
		    formData.append("fechaFinal",   fechaFinal);
		    formData.append("rfc",          rfc);
		    formData.append("razonSocial",  razonSocial);
		    formData.append("uuid",         uuid);
		    formData.append("serie",        serie);
		    formData.append("folio",        folio);
		    formData.append("idRegistro",   idRegistro); // <-- clave

		    // Operadores/valores
		    formData.append("rfcOperator",   rfcOperator);
		    formData.append("razonOperator", razonOperator);
		    formData.append("serieOperator", serieOperator);
		    formData.append("uuidOperator",  uuidOperator);

		    formData.append("dateOperator",  dateOperator);
		    formData.append("dateV1",        dateV1);
		    formData.append("dateV2",        dateV2);

		    formData.append("folioOperator", folioOperator);
		    formData.append("folioV1",       folioV1);
		    formData.append("folioV2",       folioV2);

		    formData.append("totalOperator", totalOperator);
		    formData.append("totalV1",       totalV1);
		    formData.append("totalV2",       totalV2);

		    formData.append("subOperator",   subOperator);
		    formData.append("subV1",         subV1);
		    formData.append("subV2",         subV2);

		    formData.append("descOperator",  descOperator);
		    formData.append("descV1",        descV1);
		    formData.append("descV2",        descV2);

		    formData.append("percOperator",  percOperator);
		    formData.append("percV1",        percV1);
		    formData.append("percV2",        percV2);

		    formData.append("dedOperator",   dedOperator);
		    formData.append("dedV1",         dedV1);
		    formData.append("dedV2",         dedV2);

		    // 5) POST al Action
		    $.ajax({
		      url:  '/siarex247/cumplimientoFiscal/boveda/nomina/procesaDescargarCFDI.action',
		      type: 'POST',
		      dataType: 'json',
		      data: formData,
		      cache: false,
		      contentType: false,
		      processData: false
		    }).done(function(data){
		      if (data && data.codError == '000'){
		        Swal.fire({
		          icon: 'success',
		          title: MSG_OPERACION_EXITOSA_MENU,
		          html: '<p>'+data.mensajeError+' </p>',
		          confirmButtonText: BTN_ACEPTAR_MENU
		        }).then(function(){
		          $('#myModalNotifica_Nomina').modal('hide');
		        });
		      } else {
		        Swal.fire({ icon:'error', title:'¡Error en Operación!', text:(data && data.mensajeError) ? data.mensajeError : 'Error desconocido' });
		      }
		    }).fail(function(xhr, st, err){
		      alert('procesaDescargarCFDI() error: ' + (err || st));
		    });

		  } catch(e){
		    alert('procesaDescargarCFDI()_' + e);
		  }
		}

	
 </script>
</html>