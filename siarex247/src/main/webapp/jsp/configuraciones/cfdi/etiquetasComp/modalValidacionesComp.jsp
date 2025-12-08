<%@page import="com.siarex247.utils.Utils"%>
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
    
    
	<script src="/siarex247/jsp/configuraciones/cfdi/etiquetasComp/etiquetasXMLCompVal.js"></script>
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
    
<%
    
 	// etiqueta='+etiqueta+'&version
 	String etiqueta = Utils.noNuloNormal(request.getParameter("etiqueta"));
	String version = Utils.noNuloNormal(request.getParameter("version"));
	String title = "Agregar Valores a la Etiqueta \""+ etiqueta+"\"";
%>    
    
  </head>

  	<input type="hidden" name="hdnEtiqueta_Validaciones" id="hdnEtiqueta_Validaciones" value="<%=etiqueta%>">
	<input type="hidden" name="hdnVersion_Validaciones" id="hdnVersion_Validaciones" value="<%=version%>">
    <input type="hidden" name="idagregar" id="idagregar" value="0" />
    
      
        <div class="modal-body p-0">
          <div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
            <h5 class="mb-1" id="CONF_CFDI_ETIQUETAS_ETQ16" ><%=title %></h5>
          </div>
          <div class="p-4 pb-0">
				<div class="col-sm-12 text-end">
					<div class="form-group text-end" style="margin-bottom: 10px;">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarEtiquetaComp();" id="btnRefrescar_Etiqueta" ><span class="fab fa-firefox-browser me-1"></span> <span id="CONF_CFDI_ETIQUETAS_COMP_BTN100"> Refrescar </span>  </button>
					</div>
				</div>
				
            <div class="mb-2 row">
              <div class="tab-content" style="height: 20em;">
					
                <div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
                    
                  <div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalleValidaciones"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap w-75" id="CONF_CFDI_ETIQUETAS_ETQ17">Datos a Validar</th>
							   	<th class="sort pe-1 align-middle white-space-nowrap" ></th>
							   	
							   	
							</tr>
							<tr class="forFilters">
								<th></th>
								<th></th>
							</tr>
						</thead>
					</table>
				  </div>
                </div>
              </div> <!-- tab-content -->
            </div>
          </div>
        </div>
      


<script type="text/javascript">

$(document).ready(function() {
	calcularEtiquetasConfEtiquetasValidacionModal();
});


$("#txtDatoValidar").keypress(function (e){
	var code = (e.keyCode ? e.keyCode : e.which);
	if(code == 13) 
	{
		guardarDatosVal();
   	}
}); 

$('#txtDatoValidar').focus();



function calcularEtiquetasConfEtiquetasValidacionModal(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'CONF_CFDI_ETIQUETAS'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("CONF_CFDI_ETIQUETAS_ETQ16").innerHTML = data.ETQ16 + ' "' + '<%=etiqueta%>' + '"';
					document.getElementById("CONF_CFDI_ETIQUETAS_ETQ17").innerHTML = data.ETQ12;
					
					// document.getElementById("CONF_CFDI_ETIQUETAS_Boton_CerrarValidacion").innerHTML = BTN_CERRAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfEtiquetasModal()_1_'+thrownError);
			}
		});	
	}
	
</script>


</html>
