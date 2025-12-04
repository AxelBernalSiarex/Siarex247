<%@page import="com.siarex247.seguridad.Usuarios.UsuariosForm"%>
<%@page import="com.siarex247.seguridad.Usuarios.UsuariosAction"%>
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
    
	<%
	
		UsuariosAction usuariosAction = new  UsuariosAction();
		UsuariosForm userForm = usuariosAction.consultaPermisosJSP(request);
		
	
	%>
    
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE2">Descarga Masiva</h5>
			</div>
			<div class="col-auto d-flex">
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="card overflow-hidden">
			
				<div class="card-header p-0 scrollbar-overlay border-bottom">
					<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabBovedaRecibidos" data-bs-toggle="tab" href="#bovedaRecibidos" role="tab" aria-controls="bovedaRecibidos" aria-selected="true">
								<span class="fas fa-file-invoice text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_DESCARGA_SAT_ETQ59" >Recibidos</h6>
							</a>
						</li>
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabBovedaEmitidos" data-bs-toggle="tab" href="#bovedaEmitidos" role="tab" aria-controls="bovedaEmitidos" aria-selected="false">
								<span class="far fa-calendar-alt icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_DESCARGA_SAT_ETQ60">Emitidos</h6>
                      		</a>
                      	</li>
                      	
              <%if ("007".equalsIgnoreCase(userForm.getNombreCortoPerfil()) || "009".equalsIgnoreCase(userForm.getNombreCortoPerfil()) || "ADM".equalsIgnoreCase(userForm.getNombreCortoPerfil())) { %>
              		                     	 
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabBovedaNomina" data-bs-toggle="tab" href="#bovedaNomina" role="tab" aria-controls="bovedaNomina" aria-selected="false">
								<span class="fas fa-money-check-alt icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" >Nomina</h6>
                      		</a>
                      	</li>
                       
                <%} %>         	  
                      	
					</ul>
				</div>
                
                <div class="tab-content">
                	
                	<div class="card-body bg-light tab-pane active" id="bovedaRecibidos" role="tabpanel" aria-labelledby="tabBovedaRecibidos">
                		<h6 class="mb-0 text-700">Recibidos</h6>
                	</div>
                	
                	 <div class="card-body bg-light tab-pane" id="bovedaEmitidos" role="tabpanel" aria-labelledby="tabBovedaEmitidos">
                		<h6 class="mb-0 text-700">Emitidos</h6>
                	</div>
                	  
                	<div class="card-body bg-light tab-pane" id="bovedaNomina" role="tabpanel" aria-labelledby="tabBovedaNomina">
                		<h6 class="mb-0 text-700">Nomina</h6>
                	</div>
                	  
                </div>
			</div>
		</div> <!-- tab-content -->
	</div> <!-- card-body -->
</div><!-- card mb-3 -->

<script type="text/javascript">

//alarmaUsuario.html

	$(document).ready(function() {
		$("#bovedaRecibidos").load('/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaRecibidos/descargaRecibidos.jsp');

		$(".nav-tabs a").click(function(){
		    $(this).tab('show');
		});
				
		$('.nav-tabs a').on('shown.bs.tab', function(event){
			var target = event.target || event.srcElement;
			var id = target.id
			if (id == "tabBovedaRecibidos") {
				$("#bovedaRecibidos").load('/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaRecibidos/descargaRecibidos.jsp');
			} else if (id == "tabBovedaEmitidos"){
				$("#bovedaEmitidos").load('/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaEmitidos/descargaEmitidos.jsp');
			} else if (id == "tabBovedaNomina"){
				<%if ("007".equalsIgnoreCase(userForm.getNombreCortoPerfil()) || "009".equalsIgnoreCase(userForm.getNombreCortoPerfil()) || "ADM".equalsIgnoreCase(userForm.getNombreCortoPerfil())) { %>       
					$("#bovedaNomina").load('/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaNomina/descargaNomina1.jsp');
				<%}%>
			} 
		});
		
		
		calcularEtiquetasDescargaMasiva();
				
	});
	
	

	 function calcularEtiquetasDescargaMasiva(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_DESCARGA_SAT'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_DESCARGA_SAT_TITLE2").innerHTML = data.TITLE2;
						document.getElementById("CONF_DESCARGA_SAT_ETQ59").innerHTML = data.ETQ59;
						document.getElementById("CONF_DESCARGA_SAT_ETQ60").innerHTML = data.ETQ60;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasDescargaMasiva()_1_'+thrownError);
				}
			});	
		}
	 
</script>

</html>