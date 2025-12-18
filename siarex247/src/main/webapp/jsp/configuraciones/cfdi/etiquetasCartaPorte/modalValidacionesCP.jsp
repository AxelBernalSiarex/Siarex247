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
    
	<script src="/siarex247/jsp/configuraciones/cfdi/etiquetasCartaPorte/etiquetasXMLVal.js"></script>
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
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
      <div class="modal-content position-relative">
        <div class="modal-body p-0">
          <div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
            <h5 class="mb-1" id="CONF_CFDI_CARTAP_ETQ19"><%=title %></h5>
          </div>
          <div class="p-4 pb-0">

            <div class="mb-2 row">
             
              <div class="col-sm-6">
                <input id="txtDatoValidar" name="txtDatoValidar" class="form-control" type="text" placeholder="Introduzca el Dato a Validar" />
              </div>

   
              <div class="col-sm-3">
              <!-- 
                  <button class="btn btn-primary rounded-pill me-1 mb-1" type="button"> Agregar <span class="fas fa-check" data-fa-transform="shrink-3"></span> </button>
               -->
                  
				 <div class="col-sm-2">
					<div class="position-relative" id="emoji-button" onclick="guardarDatosVal();">
						<div class="btn btn-info" data-picmo='{"position":"bottom-start"}'><span class="fas fa-plus"></span></div>
			  		</div>
				</div>
                  
                  
              </div>
    
            </div>

            <div class="mb-2 row">
              <div class="tab-content">
					
                <div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
                    
                  <div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalleValidaciones"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_CARTAP_ETQ20">Datos a Validar</th>
							   	<th class="sort pe-1 align-middle white-space-nowrap" ></th>
							</tr>
							
						</thead>
					</table>
				  </div>
                </div>
              </div> <!-- tab-content -->
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="CONF_CFDI_CARTAP_Boton_CerrarValidacion"> Cerrar </button>
        </div>
      </div>
    </div>

<script type="text/javascript">

$(document).ready(function() {
	calcularEtiquetasConfCartaPorteValModal();
});



$("#txtDatoValidar").keypress(function (e){
	var code = (e.keyCode ? e.keyCode : e.which);
	if(code == 13) 
	{
		guardarDatosVal();
   	}
}); 

$('#txtDatoValidar').focus();




function calcularEtiquetasConfCartaPorteValModal(){
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
					
					document.getElementById("CONF_CFDI_CARTAP_ETQ19").innerHTML = data.ETQ19 + ' "' + '<%=etiqueta%>' + '"';
					document.getElementById("CONF_CFDI_CARTAP_ETQ20").innerHTML = data.ETQ20;
					
					document.getElementById("CONF_CFDI_CARTAP_Boton_CerrarValidacion").innerHTML = BTN_CERRAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfEtiquetasModal()_1_'+thrownError);
			}
		});	
	}
	
</script>


</html>
