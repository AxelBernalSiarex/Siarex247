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

  <script src="/siarex247/jsp/catalogos/constanciaFiscal/constanciaFiscal.js?v=<%=Utils.VERSION%>"></script>
  <script src="/siarex247/satJS/catalogos.js"></script>
  
  
</head>



<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">
   
   <div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CAT_CONSTANCIA_SITUACION_TITLE1">Boveda de Constancias de Situación Fiscal</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
                <button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
                	<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="CAT_CONSTANCIA_SITUACION_ETQ23">Opciones</span>
                </button>
                <div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu" style="">
                	<a class="dropdown-item" href="javascript:abreModal('cfdi',0,0);" id="CAT_CONSTANCIA_SITUACION_ETQ24">Cargar Cedulas</a>
                </div>
              </div>
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important; text-align: " >
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900" style="">
							<tr>
								 <th class="no-sort pe-1 align-middle data-table-row-action" id="CAT_CONSTANCIA_SITUACION_ETQ5">Acciones</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ3">Cédula Fiscal</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ1">RFC</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ2">Razón Social</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ26">Régimen Capital</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ11"> Nombre</th>
                                 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ12"> Apellido Paterno</th>
                                 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ13"> Apellido Materno</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ17"> Fecha Inicio de Operaciones</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ15"> Situación del contribuyente</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ27"> Fecha Ultimo Cambio de Situación</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ28"> Entidad Federativa</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ29"> Municipio o Delegación</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ30"> Colonia</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ31"> Tipo de Vialidad</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ32"> Nombre de la Vialidad</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ33" > Número Exterior</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ34"> Número Interior</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ4">Código Postal</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ18"> Correo electrónico</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ35"> Regimen Fiscal</th>
								 <th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CONSTANCIA_SITUACION_ETQ36"> Fecha de Alta</th>
                                 
							</tr>
							<tr class="forFilters">
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div> <!-- tab-content -->
	</div> <!-- card-body -->
</div><!-- card mb-3 -->

<div class="modal fade" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade" id="myModalCargaCFDI" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>

    <form id="frmExportarCedula" name="frmExportarCedula" class="easyui-form"  method="post"  action="/siarex247/jsp/catalogos/constanciaFiscal/mostrarCedulaFiscal.jsp" target="_blank">
      <input type="hidden" name="idRegistro" value="0" id="idRegistro_PDF">
      <input type="hidden" name="cedulaFiscal" value="0" id="cedulaFiscal_PDF">
   </form>
   

<iframe name="framePDF" style="width: 0px; height: 0px; visibility: hidden;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<script type="text/javascript">

  var TITLE_NEW_CATALOGO = null;
  var TITLE_EDIT_CATALOGO = null;
  var TITLE_VIEW_CATALOGO = null;
  var TITLE_DELETE_CATALOGO = null;
  
  var LABEL_CONSTANCIAS = null;
  var TITLE_CARGA_EXITOSA = null;
  var MENSAJE_CARGA_EXITOSA = null;
  
  
	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/catalogos/constanciaFiscal/modalConstancia.jsp');
		$("#myModalCargaCFDI").load('/siarex247/jsp/catalogos/constanciaFiscal/modalCargar.jsp');
		calcularEtiquetasCatalogoConstancias();
	});
	

	 function calcularEtiquetasCatalogoConstancias(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_CONSTANCIA_SITUACION'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_CONSTANCIA_SITUACION_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ13").innerHTML = data.ETQ13;
						// document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ15").innerHTML = data.ETQ15;
						// document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ24").innerHTML = data.ETQ24;
						
						
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ26").innerHTML = data.ETQ26;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ27").innerHTML = data.ETQ27;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ28").innerHTML = data.ETQ28;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ29").innerHTML = data.ETQ29;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ30").innerHTML = data.ETQ30;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ31").innerHTML = data.ETQ31;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ32").innerHTML = data.ETQ32;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ33").innerHTML = data.ETQ33;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ34").innerHTML = data.ETQ34;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ35").innerHTML = data.ETQ35;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ36").innerHTML = data.ETQ36;
						
						
						TITLE_NEW_CATALOGO = data.ETQ6;
						TITLE_EDIT_CATALOGO = data.ETQ7;
						TITLE_VIEW_CATALOGO = data.ETQ8;
						LABEL_CONSTANCIAS = data.ETQ9;
						TITLE_DELETE_CATALOGO = data.ETQ10;
						TITLE_CARGA_EXITOSA = data.ETQ21;
						MENSAJE_CARGA_EXITOSA = data.ETQ22;
						  
						// document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCatalogoConstancias()_1_'+thrownError);
				}
			});	
		}
	 
</script>

