<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <script src="/siarex247/jsp/configuraciones/sistema/alertaComplemento/alertaComplemento.js"></script>
     
     <link rel="stylesheet" href="/siarex247/assets/summernote/summernote-lite.css">
     <link rel="stylesheet" href="/siarex247/assets/summernote/summernote-bs5.min.css">
     <script src="/siarex247/assets/summernote/summernote-lite.js"></script>
     
     <!--
     <script src="/siarex247/js/html2pdf.bundle.js" defer></script>
     -->
     
     
        
</head>

<form id="form-AlarmaComplementos" class="was-validated" accept-charset="UTF-8" novalidate>
	<div class="p-2">
	
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="fechaPago_Complemento">Apartir de la Fecha de Pago</label>
			<div class="col-sm-4">
				<input class="form-control datetimepicker flatpickr-input active" id="fechaPago_Complemento"
											name="fechaPago" type="text" placeholder="dd/mm/yyyy"
											data-options='{"disableMobile":true}' readonly/>
			</div>
		</div>

		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="subject_Complemento">Subject</label>
			<div class="col-sm-4">
				<input id="txtSubject_Complemento" name="subject" class="form-control" type="text" required />
			</div>
		</div>

		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtMsjError_Complemento">Mensaje de Error</label>
			<div class="col-sm-6">
			<!-- 
				<textarea class="form-control" id="txtMsjError_Complemento" name="mensajeError" rows="6" required></textarea>
			 -->	
				<textarea id="txtMsjError_Complemento" name="mensajeError" required></textarea>
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario1_Complemento">Destinatario 1</label>
			<div class="col-sm-4">
				<input id="txtDestinatario1_Complemento" name="destinatario1" class="form-control" type="email" placeholder="Correo destinatario 1" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario2_Complemento">Destinatario 2</label>
			<div class="col-sm-4">
				<input id="txtDestinatario2_Complemento" name="destinatario2" class="form-control" type="email" placeholder="Correo destinatario 2" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario3_Complemento">Destinatario 3</label>
			<div class="col-sm-4">
				<input id="txtDestinatario3_Complemento" name="destinatario3" class="form-control" type="email" placeholder="Correo destinatario 3" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario4_Complemento">Destinatario 4</label>
			<div class="col-sm-4">
				<input id="txtDestinatario4_Complemento" name="destinatario4" class="form-control" type="email" placeholder="Correo destinatario 4" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario5_Complemento">Destinatario 5</label>
			<div class="col-sm-4">
				<input id="txtDestinatario5_Complemento" name="destinatario5" class="form-control" type="email" placeholder="Correo destinatario 5" />
			</div>
		</div>
				
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="swtAdjuntarArchivo_Complemento">Activar Proceso</label>
			<div class="col-sm-4">
				<div class="form-check form-switch">
					<label class="form-check-label d-inline-block me-1" for="swtActivarProceso_Complemento" id="swtActivarProcesoLbl_Complemento" >NO</label>
					<input class="form-check-input" id="swtActivarProceso_Complemento" type="checkbox"/>
				</div>
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="diasProcesar_Complemento">D&iacute;as a Procesar</label>
			<div class="col-sm-4">
				<input id="diasProcesar_Complemento" name="diasProcesar" class="form-control" type="text" placeholder="Introduzca los dias a procesar separados por comas 1,5,7,10,15" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtEspacio"></label>
			<div class="col-sm-4">
				<button type="submit" id="submit" class="btn btn-primary">Guardar</button>
			</div>
		</div>
		
	</div>
			
</form>

<script type="text/javascript">
	$(document).ready(function() {
		
		 $('#txtMsjError_Complemento').summernote({
		        placeholder: "Escribir el contenido del mensaje de error...",
		        height: 200,
		      
		        toolbar: [
		        	['style', ['style']],
		            ['font', ['bold', 'underline', 'clear']],
		            ['color', ['color']],
		            ['para', ['ul', 'ol', 'paragraph']],
		            ['table', ['table']],
		            ['insert', ['picture']]
				  ]
		   });
		 
		 
		 flatpickr(fechaPago_Complemento, {
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

		 
				
		$("#form-AlarmaComplementos").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-AlarmaComplementos').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           keyUp: true,
           submitHandler: function(form) {

				//Aqui va el proceso para guardar
				guardarComplementos();
				/*Swal.fire({
  					position: 'top-end',
  					icon: 'success',
  					title: 'El registro se guardÃ³ con Ã©xito !',
  					showConfirmButton: false,
  					timer: 1500
				})*/
        	   
			},
           errorPlacement: function (error, e) {
           },
           highlight: function (e) {
           },
           success: function (e) {
           }, rules:  {
               select: {required: true}
           }, messages: {
               select: {required: 'error'}
           }
       }).resetForm(); 
	   
	   iniciaTabAlarmaComplemento();
	   consultaAlarmaComplemento();
	   	   
	   
	   $('#swtActivarProceso_Complemento').change(function () {
           if ($(this).is(":checked")) {
               $('#swtActivarProcesoLbl_Complemento').html("SI");               
           } else {
        	   $('#swtActivarProcesoLbl_Complemento').html("NO");
           }
       });
	   /* ----------------------------------------------*/  
	   	   
	});
	
	
</script>

</html>