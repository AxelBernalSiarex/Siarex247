<!DOCTYPE html>
<html>

    <%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
  
  
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
	
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


<form id="frmConfXML" class="was-validated" novalidate>
	<input type="hidden" name="claveRegistro" id="idRegistro_Catalogo" value="0" >
	<input type="hidden" name="remplazarCertificacion" id="remplazarCertificacion" value="INICIO" >
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="CONF_CFDI_ETIQUETAS_Modal_ETQ11">Editar Configuraci&oacute;n a Etiquetas del XML</h5>
				</div>
				<div class="p-4 pb-3">
				
					<div class="card overflow-hidden">
			
						<div class="card-header p-0 scrollbar-overlay border-bottom">
							<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
								<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabInfoPrincipal" data-bs-toggle="tab" href="#infoPrincipal" role="tab" aria-controls="infoPrincipal" aria-selected="true">
										<span class="fas fa-address-card text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_CFDI_ETIQUETAS_Modal_ETQ21">Configuraci&oacute;n de Etiqueta</h6>
									</a>
								</li>
								
							</ul>
						</div>
		                
		                <div class="tab-content">
		                	
							<!-- Informacion Principal -->
		                	<div class="card-body bg-light tab-pane active" id="infoPrincipal" role="tabpanel" aria-labelledby="tabInfoPrincipal">
								
								<div class="accordion" id="accordion1">
									
									<div class="accordion-item"><!-- accordion-item Datos Generales -->
    									<h2 class="accordion-header" id="heading1">
      										<button class="accordion-button bg-200" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="true" aria-controls="collapse1"  id="CONF_CFDI_ETIQUETAS_Modal_ETQ19">Configuraci&oacute;n General</button>
    									</h2>
    									<div class="accordion-collapse collapse show" id="collapse1" aria-labelledby="heading1" data-bs-parent="#accordion1">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
									              <label class="col-sm-3 col-form-label" for="etiqueta" id="CONF_CFDI_ETIQUETAS_Modal_ETQ12">Etiqueta del XML </label>
									              <div class="col-sm-9">
									                <input id="etiqueta" name="etiqueta" class="form-control" type="text" readonly="readonly" />
									              </div>
									            </div>
												<div class="mb-2 row">
									              <label class="col-sm-3 col-form-label" for="fechaInicial" id="CONF_CFDI_ETIQUETAS_Modal_Etiqueta_FechaInicio">Fecha de Inicio</label>
									              <div class="col-sm-9">
									                <div class="form-group">
									                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
									                </div>
									              </div>
									            </div>
									
									            <div class="mb-2 row">
									              <label class="col-sm-3 col-form-label" for="fechaFinal" id="CONF_CFDI_ETIQUETAS_Modal_Etiqueta_FechaFinal">Fecha de Final</label>
									              <div class="col-sm-9">
									                <div class="form-group">
									                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
									                </div>
									              </div>
									            </div>
									
									            <div class="mb-2 row">
													<label class="col-sm-3 col-form-label" for="txtAsunto" id="CONF_CFDI_ETIQUETAS_Modal_ETQ13">Activar Configuraci&oacute;n </label>
									              <div class="col-sm-9">
													<div class="form-check form-switch">
														<label class="form-check-label d-inline-block me-1" for="activadaSW" id="activadaSWLbl" >SI</label>
														<input class="form-check-input" id="activadaSW" name="activadaSW" type="checkbox" checked="" />
													  </div>
									              </div>
									            </div>
									
									            <div class="mb-2 row">
									              <label class="col-sm-3 col-form-label" for="subject" id="CONF_CFDI_ETIQUETAS_Modal_ETQ14">Asunto </label>
									              <div class="col-sm-9">
									                <input id="subject" name="subject"  class="form-control" type="text" required />
									              </div>
									            </div>
									            
									            <div class="mb-2 row">
									              <label class="col-sm-3 col-form-label" for="mensajeError" id="CONF_CFDI_ETIQUETAS_Modal_ETQ15">Mensaje de Error</label>
									              <div class="col-sm-9">
									                <textarea class="form-control" id="mensajeError" name="mensajeError" rows="3" required ></textarea>
									              </div>
									            </div>
									            
      										</div>
    									</div>
  									</div> 
  									
  									
  									<div class="accordion-item"> 
										<h2 class="accordion-header" id="heading2">
											<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse2" aria-expanded="true" aria-controls="collapse2"  id="CONF_CFDI_ETIQUETAS_Modal_ETQ20">Datos a Validar</button>
										</h2>
										<div class="accordion-collapse collapse" id="collapse2" aria-labelledby="heading2" data-bs-parent="#accordion1">
											<div class="accordion-body">
												<div id="modalValidaciones" style="width: 100%; height: 100%; overflow: hidden;"></div>
												
											
											</div>
										</div>
									</div><!-- accordion-item Datos de la Cuenta -->
									
								</div><!-- accordion -->

		                	</div><!-- Informacion Principal -->
		                </div>
					
					</div>
				
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" type="button" id="CONF_CFDI_ETIQUETAS_Modal_Boton_Cerrar" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>

  <script type="text/javascript">
    $(document).ready(function () {
    	
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
    	 
    	 
      $("#activadaSW").change(function () {
        if ($(this).is(":checked")) {
          $("#activadaSWLbl").html("SI");
        } else {
          $("#activadaSWLbl").html("NO");
        }
      });

      $("#frmConfXML").on("submit", function (event) {
        $(this).addClass("was-validated");
      });

      var elements = document.querySelectorAll("[data-input-mask]");
      elements.forEach(function (item) {
        var userOptions = utils.getData(item, "input-mask");
        var defaultOptions = {
          showMaskOnFocus: false,
          showMaskOnHover: false,
          jitMasking: true,
        };

        var maskOptions = window._.merge(defaultOptions, userOptions);

        var inputmask = new window.Inputmask(
          _objectSpread({}, maskOptions)
        ).mask(item);
        return inputmask;
      });


      
      $("#frmConfXML")
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

      calcularEtiquetasConfEtiquetasModal();
    });
    
    

	 function calcularEtiquetasConfEtiquetasModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_CFDI_COMPLEMENT'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_Etiqueta_FechaInicio").innerHTML = data.ETQ4;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_Etiqueta_FechaFinal").innerHTML = data.ETQ5;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_ETQ21").innerHTML = data.ETQ21;
						
						
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CONF_CFDI_ETIQUETAS_Modal_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfEtiquetasModal()_1_'+thrownError);
				}
			});	
		}
	 
  </script>
</html>