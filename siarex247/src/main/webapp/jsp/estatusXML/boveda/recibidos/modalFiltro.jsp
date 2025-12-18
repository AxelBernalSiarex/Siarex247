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
    
     <script src="/theme-falcon/js/validate.js"></script>
     <script src='/siarex247/js/filterFechas.js'></script>
     
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



<form id="form-Filter" class="" novalidate>
	<input type="hidden" name="filtrarPor" id="filtrarPor" value="">
	<input type="hidden" name="nextRow" id="nextRow" value="1">
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="modal-title-filter">Filtros Boveda</h5>
				</div>
				<div class="p-4 pb-0">
					<div class="mb-2 row">
						<a class="mb-4 d-block d-flex align-items-center" href="javascript:agregarSiguiente();" aria-expanded="false" aria-controls="experience-form1">
							<span class="circle-dashed">
								<span class="fas fa-plus"></span>
							</span>
							<span class="ms-3">Agregar nuevo filtro</span>
						</a>
					</div>
					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="idCondicion">Condicion</label>
						<div class="col-sm-3">
							<div class="form-check form-switch">
								<label class="form-check-label d-inline-block me-1" for="idCondicionLbl" id="idCondicionLbl" >AND</label>
								<input class="form-check-input" id="idCondicion" type="checkbox" checked="checked" />
							</div>
						</div>
					</div>
					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" >Filtrar por : </label>
						<div class="col-sm-3">
	   						<select class="form-select comboCondicion" id="cmbColumnas" name="cmbColumnas" onchange="mostrarCondiciones(this.value, 0);">
							    <option value="">Seleccione una opci&oacute;n ...</option>
							    <option value="RFC">RFC</option>
							    <option value="RAZON_SOCIAL">RAZON SOCIAL</option>
							    <option value="SERIE">SERIE</option>
							    <option value="TIPO_COMPROBANTE">TIPO DE COMPROBANTE</option>
							    <option value="FOLIO">FOLIO</option>
							    <option value="TOTAL">TOTAL</option>
							    <option value="SUB_TOTAL">SUB-TOTAL</option>
							    <option value="IVA">IVA</option>
							    <option value="IVA_RET">IVA RET</option>
							    <option value="ISR_RET">ISR RET</option>
							    <option value="IMP_LOCALES">IMP. LOCALES</option>
							    <option value="UUID">UUID</option>
							    <option value="FECHA_FACTURA">FECHA FACTURA</option>
							    
							</select>
	 					</div>
						<div class="col-sm-3">
	   						<select class="form-select" id="cmbOperador0" name="cmbOperador" onchange="activarValor(this.value, 0);"> </select>
	 					</div>
	 					
	 					<div class="col-sm-4" id="valoresInput0">
	   						<input id="idValor0" name="idValor" class="form-control" type="text" value="" disabled="disabled" />
	 					</div>
	 					
					</div>
					<div id="nextRow1"></div>
					
				</div>
			</div>
			<div class="modal-footer">	
				<button type="button" onclick="refrescarDatatable();" id="btnSometer" class="btn btn-primary">Procesar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Cancelar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $('#idCondicion').change(function () {
           if ($(this).is(":checked")) {
               $('#idCondicionLbl').html("AND");               
           } else {
        	   $('#idCondicionLbl').html("OR");
           }
       });
	   
	   
	   $("#form-Filter").on('submit', function (event) {
		   // $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Filter').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   
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
	   	   	   
	   
	});

   
   function agregarSiguiente(){
	   try {
		   var rowActual = parseInt( $('#nextRow').val());
		   var rowElement = '';
		   var nombreOperador = 'cmbOperador'+rowActual;
		   var nombreValor = 'valoresInput'+rowActual
		   
		   var comboColumnas = 'cmbColumnas'+rowActual;
		   
		   rowElement = rowElement + '<div class="mb-2 row">';
		   	rowElement = rowElement + '<label class="col-sm-2 col-form-label" >Filtrar por : </label>';
		   		rowElement = rowElement + '<div class="col-sm-3">';
		   			rowElement = rowElement + '<select class="form-select comboCondicion" id="'+comboColumnas+'" name="cmbColumnas" onchange="mostrarCondiciones(this.value,  \'' + rowActual + '\');"  required>';
		   				rowElement = rowElement + '<option value="">Seleccione una opci&oacute;n ...</option>';
		   				rowElement = rowElement + '<option value="RFC">RFC</option>';
		   				rowElement = rowElement + '<option value="RAZON_SOCIAL">RAZON SOCIAL</option>';
		   				rowElement = rowElement + '<option value="SERIE">SERIE</option>';
		   				rowElement = rowElement + '<option value="TIPO_COMPROBANTE">TIPO DE COMPROBANTE</option>';
		   				rowElement = rowElement + '<option value="FOLIO">FOLIO</option>';
		   				rowElement = rowElement + '<option value="TOTAL">TOTAL</option>';
		   				rowElement = rowElement + '<option value="SUB_TOTAL">SUB-TOTAL</option>';
		   				rowElement = rowElement + '<option value="IVA">IVA</option>';
		   				rowElement = rowElement + '<option value="IVA_RET">IVA RET</option>';
		   				rowElement = rowElement + '<option value="ISR_RET">ISR RET</option>';
		   				rowElement = rowElement + '<option value="IMP_LOCALES">IMP. LOCALES</option>';
		   				rowElement = rowElement + '<option value="UUID">UUID</option>';
		   				rowElement = rowElement + '<option value="FECHA_FACTURA">FECHA FACTURA</option>';
				    rowElement = rowElement + '</select>';
				rowElement = rowElement + '</div>';
				rowElement = rowElement + '<div class="col-sm-3">';
					rowElement = rowElement + '<select class="form-select" id="'+nombreOperador+'" name="cmbOperador" onchange="activarValor(this.value, \'' + rowActual + '\');" required> </select>';
				rowElement = rowElement + '</div>';
				
				rowElement = rowElement + '<div class="col-sm-4" id="'+nombreValor+'">';
					 rowElement = rowElement + '<input id="idValor0" name="idValor" class="form-control" type="text" value="" disabled="disabled" />';
				rowElement = rowElement + '</div>';
		   rowElement = rowElement + '</div>';
		   
		   var nextRow = rowActual + 1;
		   rowElement = rowElement  + '<div id="nextRow'+nextRow+'"></div>';
		   rowElement = rowElement  + '</div>';
		   
		   document.getElementById("nextRow"+rowActual).innerHTML = rowElement;
		   
		   $('#nextRow').val(nextRow);
		   

			comboColumnas = '#cmbColumnas'+rowActual;
		   $(comboColumnas).select2({
				dropdownParent: $('#myModalFilter .modal-body'),
				theme: 'bootstrap-5'
			});
		   
	   }catch(e){
		   alert('agregarSiguiente()_'+e);
	   }
   }
   
   
   function mostrarCondiciones(valorCondicion, idComboOperador) {
		try {
			

			var nombreCombo = '#cmbOperador'+idComboOperador; 
			$(nombreCombo).empty();

			$(nombreCombo).append($('<option></option>').attr('value', '').text('Seleccione una opcion'));
			
			if (valorCondicion == 'RFC' || valorCondicion == 'RAZON_SOCIAL' || valorCondicion == 'SERIE' || valorCondicion == 'TIPO_COMPROBANTE' || valorCondicion == 'FOLIO' || valorCondicion == 'UUID'){
				
				$(nombreCombo).append($('<option></option>').attr('value', 'IGUAL_A').text('Igual a'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'DIFERENTE_DE').text('Diferente de'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'INICIA_CON').text('Inicia con'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'NO_INICIA_CON').text('No Inicia con'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'CONTIENE').text('Contiene'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'NO_CONTIENE').text('No Contiene'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'TERMINA_CON').text('Termina con'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'NO_TERMINA_CON').text('No Termina con'));
			}else if (valorCondicion == 'TOTAL' || valorCondicion == 'SUB_TOTAL' || valorCondicion == 'IVA' || valorCondicion == 'IVA_RET' || valorCondicion == 'ISR_RET' || valorCondicion == 'IMP_LOCALES') {
				
				$(nombreCombo).append($('<option></option>').attr('value', 'IGUAL_A').text('Igual a'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'DIFERENTE_DE').text('Diferente de'));
				$(nombreCombo).append($('<option></option>').attr('value', 'MENOR_A').text('Menor a'));
				$(nombreCombo).append($('<option></option>').attr('value', 'MENOR_IGUAL_A').text('Menor o igual a'));
				$(nombreCombo).append($('<option></option>').attr('value', 'MAYOR_IGUAL_A').text('Mayor o igual a'));
				$(nombreCombo).append($('<option></option>').attr('value', 'MAYOR_A').text('Mayor a'));
				$(nombreCombo).append($('<option></option>').attr('value', 'ENTRE').text('Entre'));
				$(nombreCombo).append($('<option></option>').attr('value', 'NO_ENTRE').text('No Entre'));
				
			}else if (valorCondicion == 'FECHA_FACTURA'){
				
				$(nombreCombo).append($('<option></option>').attr('value', 'IGUAL_A_FECHA').text('Igual a'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'DIFERENTE_DE_FECHA').text('Diferente de'));	
				$(nombreCombo).append($('<option></option>').attr('value', 'ANTES_FECHA').text('Antes'));
				$(nombreCombo).append($('<option></option>').attr('value', 'DESPUES_FECHA').text('Despues'));
				$(nombreCombo).append($('<option></option>').attr('value', 'ENTRE_FECHA').text('Entre'));
				$(nombreCombo).append($('<option></option>').attr('value', 'NO_ENTRE_FECHA').text('No Entre'));
				
			}
			
			
			$(nombreCombo).select2({
				dropdownParent: $('#myModalFilter .modal-body'),
				theme: 'bootstrap-5'
			});

			addClassWarning();
		}catch(e) {
			alert("mostrarCondiciones()_"+e);
		} 
	}
	
   
   function addClassWarning(){
	    $( "#FILTER_RFC" ).removeClass( "text-warning" );
		$( "#FILTER_RAZON_SOCIAL" ).removeClass( "text-warning" );
		$( "#FILTER_SERIE" ).removeClass( "text-warning" );
		$( "#FILTER_TIPO_COMPROBANTE" ).removeClass( "text-warning" );
		$( "#FILTER_FOLIO" ).removeClass( "text-warning" );
		$( "#FILTER_TOTAL" ).removeClass( "text-warning" );
		$( "#FILTER_SUBTOTAL" ).removeClass( "text-warning" );
		$( "#FILTER_IVA" ).removeClass( "text-warning" );
		$( "#FILTER_IVARET" ).removeClass( "text-warning" );
		$( "#FILTER_ISRRET" ).removeClass( "text-warning" );
		$( "#FILTER_IMPLOCALES" ).removeClass( "text-warning" );
		$( "#FILTER_UUID" ).removeClass( "text-warning" );
		$( "#FILTER_FECHA_FACTURA" ).removeClass( "text-warning" );
		
	   var arrCondicion = document.getElementsByClassName("comboCondicion");
		for (var x = 0; x < arrCondicion.length; x++){
			if ('RFC' == arrCondicion[x].value){
				$( "#FILTER_RFC" ).addClass( "text-warning" );
			}else if ('RAZON_SOCIAL' == arrCondicion[x].value){
				$( "#FILTER_RAZON_SOCIAL" ).addClass( "text-warning" );
			}else if ('SERIE' == arrCondicion[x].value){
				$( "#FILTER_SERIE" ).addClass( "text-warning" );
			}else if ('TIPO_COMPROBANTE' == arrCondicion[x].value){
				$( "#FILTER_TIPO_COMPROBANTE" ).addClass( "text-warning" );
			}else if ('FOLIO' == arrCondicion[x].value){
				$( "#FILTER_FOLIO" ).addClass( "text-warning" );
			}else if ('TOTAL' == arrCondicion[x].value){
				$( "#FILTER_TOTAL" ).addClass( "text-warning" );
			}else if ('SUB_TOTAL' == arrCondicion[x].value){
				$( "#FILTER_SUBTOTAL" ).addClass( "text-warning" );
			}else if ('IVA' == arrCondicion[x].value){
				$( "#FILTER_IVA" ).addClass( "text-warning" );
			}else if ('IVA_RET' == arrCondicion[x].value){
				$( "#FILTER_IVARET" ).addClass( "text-warning" );
			}else if ('ISR_RET' == arrCondicion[x].value){
				$( "#FILTER_ISRRET" ).addClass( "text-warning" );
			}else if ('IMP_LOCALES' == arrCondicion[x].value){
				$( "#FILTER_IMPLOCALES" ).addClass( "text-warning" );
			}else if ('UUID' == arrCondicion[x].value){
				$( "#FILTER_UUID" ).addClass( "text-warning" );
			}else if ('FECHA_FACTURA' == arrCondicion[x].value){
				$( "#FILTER_FECHA_FACTURA" ).addClass( "text-warning" );
			}
		}
   }
   
   function activarValor(valorCombo, idOperador){
	   try{
		  
		   var rowElement = '';
		   var VALOR_INICIAL = 'VALOR_INICIAL'+idOperador;
		   var valoresInput =  'valoresInput'+idOperador;
		   if (valorCombo == ''){
			   rowElement = rowElement + '<input id="'+VALOR_INICIAL+'" name="VALOR_INICIAL" class="form-control" type="text" value="" disabled="disabled"/>';
			   document.getElementById(valoresInput).innerHTML = rowElement;
		   }else if (valorCombo == 'ENTRE' || valorCombo == 'NO_ENTRE'){
		   			
		   			var VALOR_FINAL = 'VALOR_FINAL'+idOperador;
   	 					rowElement = rowElement + '<div class="col-sm-8" id="inputValue0">';
   	 						rowElement = rowElement + '<input id="'+VALOR_INICIAL+'" name="VALOR_INICIAL" class="form-control" type="text" value="" />';
   		 					rowElement = rowElement + '<input id="'+VALOR_FINAL+'" name="VALOR_FINAL" class="form-control" type="text" value="" />';
   	 				rowElement = rowElement + '</div>';
   	 		 document.getElementById(valoresInput).innerHTML = rowElement;
		   }else if (valorCombo == 'IGUAL_A_FECHA' || valorCombo == 'DIFERENTE_DE_FECHA' || valorCombo == 'ANTES_FECHA' || valorCombo == 'DESPUES_FECHA'){
			   
			   // rowElement = rowElement + '<input class="form-control datetimepicker flatpickr-input active" id="'+VALOR_INICIAL+'" name="VALOR_INICIAL" type="text" placeholder="dd/mm/yyyy" readonly="readonly" />';
			   rowElement = rowElement + '<input class="form-control datetimepicker flatpickr-input active" id="'+VALOR_INICIAL+'" name="VALOR_INICIAL" type="text" placeholder="yyyy/mm/dd/" data-options=\'{"disableMobile":true}\' readonly="readonly" />';
			   document.getElementById(valoresInput).innerHTML = rowElement;
			
			   if (idOperador == 0){
				   formatFecha0();
			   }else if (idOperador == 1){
				   formatFecha1();
			   }else if (idOperador == 2){
				   formatFecha2();
			   }else if (idOperador == 3){
				   formatFecha3();
			   }else if (idOperador == 4){
				   formatFecha4();
			   }else if (idOperador == 5){
				   formatFecha5();
			   }else if (idOperador == 6){
				   formatFecha6();
			   }else if (idOperador == 7){
				   formatFecha7();
			   }else if (idOperador == 8){
				   formatFecha8();
			   }else if (idOperador == 9){
				   formatFecha9();
			   }else if (idOperador == 10){
				   formatFecha10();
			   }else if (idOperador == 11){
				   formatFecha11();
			   }else if (idOperador == 12){
				   formatFecha12();
			   }else if (idOperador == 13){
				   formatFecha13();
			   }else if (idOperador == 14){
				   formatFecha14();
			   }else if (idOperador == 15){
				   formatFecha15();
			   }
		   }else if (valorCombo == 'ENTRE_FECHA' || valorCombo == 'NO_ENTRE_FECHA' || valorCombo == 'ANTES_FECHA' || valorCombo == 'DESPUES_FECHA'){
			   var VALOR_FINAL = 'VALOR_FINAL'+idOperador;
			   rowElement = rowElement + '<div class="col-sm-8" id="inputValue0">';
			   		rowElement = rowElement + '<input class="form-control datetimepicker flatpickr-input active" id="'+VALOR_INICIAL+'" name="VALOR_INICIAL" type="text" placeholder="yyyy/mm/dd/" data-options=\'{"disableMobile":true}\' readonly="readonly" />';
			   		rowElement = rowElement + '<input class="form-control datetimepicker flatpickr-input active" id="'+VALOR_FINAL+'" name="VALOR_FINAL" type="text" placeholder="yyyy/mm/dd/" data-options=\'{"disableMobile":true}\' readonly="readonly" />';
			   	rowElement = rowElement + '</div>';
			   	
			   document.getElementById(valoresInput).innerHTML = rowElement;
			   
			   if (idOperador == 0){
				   formatFecha0();
				   formatFechaFinal0();
			   }else if (idOperador == 1){
				   formatFecha1();
				   formatFechaFinal1();
			   }else if (idOperador == 2){
				   formatFecha2();
				   formatFechaFinal2();
			   }else if (idOperador == 3){
				   formatFecha3();
				   formatFechaFinal3();
			   }else if (idOperador == 4){
				   formatFecha4();
				   formatFechaFinal4();
			   }else if (idOperador == 5){
				   formatFecha5();
				   formatFechaFinal5();
			   }else if (idOperador == 6){
				   formatFecha6();
				   formatFechaFinal6();
			   }else if (idOperador == 7){
				   formatFecha7();
				   formatFechaFinal7();
			   }else if (idOperador == 8){
				   formatFecha8();
				   formatFechaFinal8();
			   }else if (idOperador == 9){
				   formatFecha9();
				   formatFechaFinal9();
			   }else if (idOperador == 10){
				   formatFecha10();
				   formatFechaFinal0();
			   }else if (idOperador == 11){
				   formatFecha11();
				   formatFechaFinal1();
			   }else if (idOperador == 12){
				   formatFecha12();
				   formatFechaFinal2();
			   }else if (idOperador == 13){
				   formatFecha13();
				   formatFechaFinal3();
			   }else if (idOperador == 14){
				   formatFecha14();
				   formatFechaFinal4();
			   }else if (idOperador == 15){
				   formatFecha15();
				   formatFechaFinal5();
			   }
			   
		   }else {
			   
			   rowElement = rowElement + '<input id="'+VALOR_INICIAL+'" name="VALOR_INICIAL" class="form-control" type="text" value=""/>';
			   document.getElementById(valoresInput).innerHTML = rowElement;   
		   }

		   
		   
		   
	   }catch(e){
		   
	   }
   }
   
   
   
 </script>
</html>