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

<form id="frmFormatosMEX" class="was-validated" novalidate>
  <input type="hidden" name="tipoProveedor" id="tipoProveedorMEX" value="MEX">
  
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content position-relative">
      <div class="modal-body p-0">
        <div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
          <h5 class="mb-1" id="CONF_ENVIO_FORMATOS_ETQ18">Instrucciones Predeterminadas MEX</h5>
        </div>

        <div class="p-4 pb-0">
        
          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="descripcionMEX" id="CONF_ENVIO_FORMATOS_ETQ19">Descripción </label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="descripcionMEX" name="descripcion" class="form-control" type="text" required />
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="subjectCorreoMEX" id="CONF_ENVIO_FORMATOS_ETQ20">Asunto del Correo </label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="subjectCorreoMEX" name="subjectCorreo" class="form-control" type="text" required />
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="cuerpoCorreoMEX" id="CONF_ENVIO_FORMATOS_ETQ21">Cuerpo del Correo </label>
            <div class="col-sm-9">
              <div class="form-group">
                <textarea class="form-control" id="cuerpoCorreoMEX" name="cuerpoCorreo" rows="3" required></textarea>
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="copiaParaMEX" id="CONF_ENVIO_FORMATOS_ETQ22">Copia Para </label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="copiaParaMEX" name="copiaPara" class="form-control" type="text" required />
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="fileFormatoMEX" id="CONF_ENVIO_FORMATOS_ETQ23">Seleccione el Archivo PDF</label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="fileFormatoMEX" name="fileFormato" class="form-control" type="file" accept="application/pdf" />
              </div>
            </div>
          </div>

          <div class="mb-2 row">
            <label class="col-sm-3 col-form-label" for="fileFormatoXLS" id="CONF_ENVIO_FORMATOS_ETQ24">Seleccione el Archivo Excel</label>
            <div class="col-sm-9">
              <div class="form-group">
                <input id="fileFormatoXLS" name="fileFormatoXLS" class="form-control" type="file" />
              </div>
            </div>
          </div>

        </div>
      </div>
      <div class="modal-footer">
        <button type="submit" id="btnSometerMEX" class="btn btn-primary"> Guardar </button>
        <button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="CONF_ENVIO_FORMATOS_Boton_CerrarMEX"> Cerrar </button>
      </div>
    </div>
  </div>
</form>


<script type="text/javascript">
  $(document).ready(function () {



    $("#frmFormatosMEX").on("submit", function (event) {
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
    $("#frmFormatosMEX")
      .validate({
        ignore: "input[type=hidden]",
        focusInvalid: false,
        errorClass: "help-block animation-pullUp",
        errorElement: "div",
        keyUp: true,
        submitHandler: function (form) {
        	guardaFormatoMEX();
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

    calcularEtiquetasConfEnvioCorreosMEXModal();
  });
  

	 function calcularEtiquetasConfEnvioCorreosMEXModal(){
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
						
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("CONF_ENVIO_FORMATOS_ETQ24").innerHTML = data.ETQ24;
						
						document.getElementById("btnSometerMEX").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CONF_ENVIO_FORMATOS_Boton_CerrarMEX").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
</script>

</html>