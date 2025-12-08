<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <script src="/siarex247/jsp/configuraciones/sistema/alertaConciliacion/alertaConciliacion.js"></script>
     <link rel="stylesheet" href="/siarex247/assets/summernote/summernote-lite.css">
     <link rel="stylesheet" href="/siarex247/assets/summernote/summernote-bs5.min.css">
     <script src="/siarex247/assets/summernote/summernote-lite.js"></script>
    <!-- 
     <script src="/siarex247/js/html2pdf.bundle.js" defer></script>
 	-->
        
</head>

<form id="form-AlarmaCompras" class="was-validated" accept-charset="UTF-8"  novalidate>
	<div class="p-2">
	
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="diaEjecucion">Procesar</label>
			<div class="col-sm-3">
				<div class="form-group">
				<select class="form-select" id="diaEjecucion" name="diaEjecucion" required>
				    <option value="">Seleccione una opci�n ...</option>
				</select>
				</div>
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="subject">Subject</label>
			<div class="col-sm-4">
				<input id="txtSubject" name="subject" class="form-control" type="text" required />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtMsjError">Mensaje de Error</label>
			<div class="col-sm-6">
				<textarea id="txtMsjError" name="mensajeError" required></textarea>
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario1">Destinatario 1</label>
			<div class="col-sm-4">
				<input id="txtDestinatario1" name="destinatario1" class="form-control" type="email" placeholder="Correo destinatario 1" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario2">Destinatario 2</label>
			<div class="col-sm-4">
				<input id="txtDestinatario2" name="destinatario2" class="form-control" type="email" placeholder="Correo destinatario 2" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario3">Destinatario 3</label>
			<div class="col-sm-4">
				<input id="txtDestinatario3" name="destinatario3" class="form-control" type="email" placeholder="Correo destinatario 3" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario4">Destinatario 4</label>
			<div class="col-sm-4">
				<input id="txtDestinatario4" name="destinatario4" class="form-control" type="email" placeholder="Correo destinatario 4" />
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="txtDestinatario5">Destinatario 5</label>
			<div class="col-sm-4">
				<input id="txtDestinatario5" name="destinatario5" class="form-control" type="email" placeholder="Correo destinatario 5" />
			</div>
		</div>
				
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="swtAdjuntarArchivo">Adjuntar Archivo</label>
			<div class="col-sm-4">
				<div class="form-check form-switch">
					<label class="form-check-label d-inline-block me-1" for="swtAdjuntarArchivo" id="swtAdjuntarArchivoLbl" >NO</label>
					<input class="form-check-input" id="swtAdjuntarArchivo"  type="checkbox"/>
				</div>
			</div>
		</div>
		
		<div class="mb-2 row">
			<label class="col-sm-2 col-form-label" for="swtAdjuntarArchivo">Activar Proceso</label>
			<div class="col-sm-4">
				<div class="form-check form-switch">
					<label class="form-check-label d-inline-block me-1" for="swtActivarProceso" id="swtActivarProcesoLbl" >NO</label>
					<input class="form-check-input" id="swtActivarProceso" type="checkbox"/>
				</div>
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
		
		 $('#txtMsjError').summernote({
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

		 
		$("#form-AlarmaCompras").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-AlarmaCompras').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           keyUp: true,
           submitHandler: function(form) {

				//Aqui va el proceso para guardar
				guardarConfiguracion();
				/*Swal.fire({
  					position: 'top-end',
  					icon: 'success',
  					title: 'El registro se guardó con éxito !',
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
	   
	   iniciaTabAlarmaCompras();
	   consultaConfiguracion();
	   	   
	   $('#cmbPrcesarCompras').on('change', function() { 
		   $(this).trigger('blur');
		   
	   });
	   	   
	   $('#swtAdjuntarArchivo').change(function () {
           if ($(this).is(":checked")) {
               $('#swtAdjuntarArchivoLbl').html("SI");               
           } else {
        	   $('#swtAdjuntarArchivoLbl').html("NO");
           }
       });
	   
	   $('#swtActivarProceso').change(function () {
           if ($(this).is(":checked")) {
               $('#swtActivarProcesoLbl').html("SI");               
           } else {
        	   $('#swtActivarProcesoLbl').html("NO");
           }
       });
	   /* ----------------------------------------------*/  
	   	   
	});
	
	
</script>

</html>