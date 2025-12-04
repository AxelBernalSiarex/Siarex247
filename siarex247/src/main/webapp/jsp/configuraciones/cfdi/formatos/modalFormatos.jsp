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

<form id="frmFormatos" class="was-validated" novalidate>
  <input type="hidden" name="idRegistro" id="idRegistro_Catalogo" value="0" >
  <input type="hidden" name="tipoProveedor" id="tipoProveedor" value="ALL">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content position-relative">
      <div class="modal-body p-0">
        <div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
          <h5 class="mb-1" id="modal-title-catalogo">title</h5>
        </div>

        <div class="p-4 pb-0">
        
          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="descripcion" id="CONF_ENVIO_FORMATOS_Etiqueta_Descripcion">Descripción </label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="descripcion" name="descripcion" class="form-control validarHabilitar" type="text" required />
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="subjectCorreo" id="CONF_ENVIO_FORMATOS_ETQ13">Asunto del Correo </label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="subjectCorreo" name="subjectCorreo" class="form-control validarHabilitar" type="text" required />
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="cuerpoCorreo" id="CONF_ENVIO_FORMATOS_ETQ14">Cuerpo del Correo </label>
            <div class="col-sm-9">
              <div class="form-group">
                <textarea class="form-control validarHabilitar" id="cuerpoCorreo" name="cuerpoCorreo" rows="3" required></textarea>
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="fileFormato" id="CONF_ENVIO_FORMATOS_ETQ15">Seleccione el Archivo </label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="fileFormato" name="fileFormato" class="form-control validarHabilitar" type="file" accept="application/pdf" />
              </div>
            </div>
          </div>

        </div>
      </div>
      <div class="modal-footer">
        <button type="submit" id="btnSometer" class="btn btn-primary"> Guardar </button>
        <button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="CONF_ENVIO_FORMATOS_Boton_Cerrar"> Cerrar </button>
      </div>
    </div>
  </div>
</form>


<script type="text/javascript">
  $(document).ready(function () {

    $("#frmFormatos").on("submit", function (event) {
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

    /* Necesario para validacion del form y del Select2
   -----------------------------------------------------*/
    $("#frmFormatos")
      .validate({
        ignore: "input[type=hidden]",
        focusInvalid: false,
        errorClass: "help-block animation-pullUp",
        errorElement: "div",
        keyUp: true,
        submitHandler: function (form) {
        	 var idRegistro = $('#idRegistro_Catalogo').val();
			   if (idRegistro == 0){
				   guardaFormato();
			   }
			   else{
				   modificaFormato();
			   }
        	
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

    calcularEtiquetasConfEnvioFormatosModal();
  });
  

	 function calcularEtiquetasConfEnvioFormatosModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_ENVIO_FORMATOS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ15").innerHTML = data.ETQ15;
						
						document.getElementById("btnSometer").innerHTML =BTN_GUARDAR_MENU;
						document.getElementById("CONF_ENVIO_FORMATOS_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfEnvioFormatosModal()_1_'+thrownError);
				}
			});	
		}
	 
	 
</script>

</html>