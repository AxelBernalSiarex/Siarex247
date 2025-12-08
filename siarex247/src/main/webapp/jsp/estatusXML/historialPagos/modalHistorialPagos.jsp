<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
<html>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">


<form id="frmHistorialPagos" class="was-validated" novalidate>
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			
            <div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="HISTORIAL_PAGOS_MODAL_TITULO">
						Cargar Historial
	   				</h5>
				</div>
                
                <div class="p-4 pb-0">
					<div id="overSeccionImportar" class="overlay" style="display: none;text-align: right;">
						<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
					</div>
					
					<div class="mb-2 row">
						<label class="col-sm-3 col-form-label" for="fileCargaHistorial">Seleccione el Archivo</label>
						<div class="col-sm-9">
	   						<input class="form-control" id="fileCargaHistorial" name="fileCargaHistorial" type="file" required="required" accept=".txt,.csv" />
                            <div class="invalid-feedback">Seleccione un archivo válido.</div>
						</div>
					</div>

                    <div class="mb-2 row">
						<div class="col-sm-12">
							<div class="alert alert-info border-2 d-flex align-items-center" role="alert">
								<div class="bg-info me-3 icon-item"><span class="fas fa-info-circle text-white fs-3"></span></div>
								<p class="mb-0 flex-1">
                                    <strong>Formato:</strong> RFC(EMPRESA)|RFC(PROOVEDOR)|FECHA(YYYY-MM-DD)|UUID|MONEDA|TOTAL<br>
                                </p>
							</div>
						</div>
					</div>

				</div>
			</div>

			<div class="modal-footer">	
				<button type="submit" id="submitImport" class="btn btn-primary">Procesar </button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal">
					Cancelar
				</button>
			</div>
            
		</div>
	</div>
</form>

<script type="text/javascript">
	 
	 $(document).ready(function() {
	 	  
		  $("#frmHistorialPagos").on('submit', function (event) {
			   $(this).addClass('was-validated');
			 });
		   
		   /* Necesario para validacion del form y del Select2
		   -----------------------------------------------------*/
		   $('#frmHistorialPagos').validate({
			   ignore: 'input[type=hidden]',
			   focusInvalid: false,
	           errorClass: 'help-block animation-pullUp', errorElement: 'div',
	           keyUp: true,
	           submitHandler: function(form) {
	        	   cargarHistorialPagos();
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
 </script>
 
 </html>