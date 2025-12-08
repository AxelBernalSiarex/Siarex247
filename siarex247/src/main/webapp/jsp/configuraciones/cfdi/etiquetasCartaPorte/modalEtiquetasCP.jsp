<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<html>
  <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

    <!-- Validation js -->
    <script src="/theme-falcon/js/validate.js"></script>

    <script>
      var isRTL = JSON.parse(localStorage.getItem("isRTL"));
      if (isRTL) {
        var linkDefault = document.getElementById("style-default");
        var userLinkDefault = document.getElementById("user-style-default");
        linkDefault.setAttribute("disabled", true);
        userLinkDefault.setAttribute("disabled", true);
        document.querySelector("html").setAttribute("dir", "rtl");
      } else {
        var linkRTL = document.getElementById("style-rtl");
        var userLinkRTL = document.getElementById("user-style-rtl");
        linkRTL.setAttribute("disabled", true);
        userLinkRTL.setAttribute("disabled", true);
      }
    </script>
  </head>

  <form id="frmConfXML" class="was-validated" novalidate>
    <input type="hidden" name="claveRegistro" id="idRegistro_Catalogo" value="0" >
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
      <div class="modal-content position-relative">
        <div class="modal-body p-0">
          <div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
            <h5 class="mb-1" id="CONF_CFDI_CARTAP_ETQ13">Editar Configuraci&oacute;n Etiquetas Carta Porte</h5>
          </div>
          <div class="p-4 pb-0">
            <div class="mb-2 row">
              <label class="col-sm-3 col-form-label" for="etiqueta" id="CONF_CFDI_CARTAP_ETQ14">Etiqueta del XML </label>
              <div class="col-sm-9">
                <input id="etiqueta" name="etiqueta" class="form-control" type="text" required
                />
              </div>
            </div>

            <div class="mb-2 row">
              <label class="col-sm-3 col-form-label" for="fechaInicial" id="CONF_CFDI_CARTAP_Etiqueta_FechaInicio">Fecha de Inicio</label>
              <div class="col-sm-9">
                <div class="form-group">
                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
                </div>
              </div>
            </div>

            <div class="mb-2 row">
              <label class="col-sm-3 col-form-label" for="fechaFinal" id="CONF_CFDI_CARTAP_Etiqueta_FechaFinal">Fecha de Final</label>
              <div class="col-sm-9">
                <div class="form-group">
                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
                </div>
              </div>
            </div>

            <div class="mb-2 row">
				<label class="col-sm-3 col-form-label" for="activadaSW" id="CONF_CFDI_CARTAP_ETQ15">Activar Configuraci&oacute;n </label>
              <div class="col-sm-9">
				<div class="form-check form-switch">
					<label class="form-check-label d-inline-block me-1" for="activadaSW" id="activadaSWLbl" >SI</label>
					<input class="form-check-input" id="activadaSW" name="activadaSW" type="checkbox" checked="" />
				  </div>
              </div>
            </div>

			<div class="mb-2 row">
				<label class="col-sm-3 col-form-label" for="valEtiquetaSW" id="CONF_CFDI_CARTAP_ETQ16">Validar Valor Etiqueta </label>
              <div class="col-sm-9">
				<div class="form-check form-switch">
					<label class="form-check-label d-inline-block me-1" for="valEtiquetaSW" id="valEtiquetaSWLbl" >SI</label>
					<input class="form-check-input" id="valEtiquetaSW" name="valEtiquetaSW" type="checkbox" checked="" />
				  </div>
              </div>
            </div>
            
            <div class="mb-2 row">
              <label class="col-sm-3 col-form-label" for="subject" id="CONF_CFDI_CARTAP_ETQ17">Asunto </label>
              <div class="col-sm-9">
                <input id="subject" name="subject"  class="form-control" type="text" required />
              </div>
            </div>

            <div class="mb-2 row">
              <label class="col-sm-3 col-form-label" for="mensajeError" id="CONF_CFDI_CARTAP_ETQ18">Mensaje de Error</label>
              <div class="col-sm-9">
                <textarea class="form-control" id="mensajeError" name="mensajeError" rows="3" required ></textarea>
              </div>
            </div>
          </div>
        </div>
        
        <div class="modal-footer">
          <button type="submit" id="btnSometer" class="btn btn-primary"> Guardar </button>
          <button class="btn btn-secondary" id="CONF_CFDI_CARTAP_Boton_Cerrar" type="button" data-bs-dismiss="modal">Cerrar</button>
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
      
      $("#valEtiquetaSW").change(function () {
          if ($(this).is(":checked")) {
            $("#valEtiquetaSWLbl").html("SI");
          } else {
            $("#valEtiquetaSWLbl").html("NO");
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

      calcularEtiquetasConfCartaPorteModal();
    });
    
    

	 function calcularEtiquetasConfCartaPorteModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_CFDI_CARTAP'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_CFDI_CARTAP_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CONF_CFDI_CARTAP_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CONF_CFDI_CARTAP_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CONF_CFDI_CARTAP_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CONF_CFDI_CARTAP_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CONF_CFDI_CARTAP_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CONF_CFDI_CARTAP_Etiqueta_FechaInicio").innerHTML = data.ETQ4;
						document.getElementById("CONF_CFDI_CARTAP_Etiqueta_FechaFinal").innerHTML = data.ETQ5;
						
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CONF_CFDI_CARTAP_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfEtiquetasModal()_1_'+thrownError);
				}
			});	
		}
	 
  </script>
</html>
