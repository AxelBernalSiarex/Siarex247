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
    
        <link rel="stylesheet" href="/theme-falcon/css/searchBuilder.dataTables-custom.css"  />
	<link rel="stylesheet" href="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/css/searchBuilder.bootstrap5.css"  />
	<link rel="stylesheet" href="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/css/dataTables.dateTime.min.css"  />
    
    <script src="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/js/dataTables.searchBuilder.min.js"></script> 	
   <script src="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/js/searchBuilder.bootstrap.js"></script> 	
   <script src="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/js/dataTables.dateTime.min.js"></script>
   <script src="/siarex247/satJS/catalogos.js"></script>
   
    
	<script src="/siarex247/jsp/catalogos/proveedores/proveedores.js"></script>
    <style type="text/css">
        .colorTR {
          color: red;
        }
    </style>
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CAT_PROVEEDORES_TITLE1">Catálogo de Proveedores</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
                <button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
                	<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="CAT_PROVEEDORES_ETQ1">Opciones</span>
                </button>
                <div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu" style="">
                	<a class="dropdown-item" href="javascript:exportarCertificados();" id="CAT_PROVEEDORES_ETQ2">Exportar Certificados</a>
                  	<a class="dropdown-item" href="javascript:abreModal('validarIMSS', 0);" id="CAT_PROVEEDORES_ETQ3">Validar Certificados IMSS</a>
                  	<a class="dropdown-item" href="javascript:abreModal('validarSAT', 0);" id="CAT_PROVEEDORES_ETQ4">Validar Certificados SAT</a>
                  	<a class="dropdown-item" href="javascript:abreModal('enviarAcceso', 0);" id="CAT_PROVEEDORES_ETQ5">Enviar Acceso</a>
                  	<div class="dropdown-divider"></div>
                  	<a class="dropdown-item text-danger" href="javascript:eliminarCertificados();" id="CAT_PROVEEDORES_ETQ6">Eliminar Certificados</a>
                  	
                </div>
              </div>
			</div>
		</div>
	</div>
	
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">

			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
								
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaProveedores"class="table table-sm mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								
								<th class="no-sort pe-1 align-middle data-table-row-action" id="CAT_PROVEEDORES_ETQ7">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ8">Clave del Registro</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ9">Id Proveedor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ10">Razon Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ11">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ12">Delegacion</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ13">Ciudad</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ14">Estado</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ15">Telefono</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ16">Extencion</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ17">Nombre del Contacto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ18">Tipo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ19">Anexo24</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ20">Cert. IMSS</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ21">Cert. SAT</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ22">Banco</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ23">Sucursal</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ24">Nombre de Sucursal</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ25">Numero de Cuenta</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ26">Cuenta Clabe</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ27">Moneda</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ28">Aba</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ29">Concepto de Servicio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ30">Forma de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_PROVEEDORES_ETQ31">Fecha de Alta</th>
								
								
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


<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalEstatus" tabindex="-1" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalValidar" tabindex="-1" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalEnviar" tabindex="-1"  data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" role="dialog" aria-hidden="true"></div>


    <form action="/siarex247/jsp/catalogos/proveedores/mostrarCertificado.jsp" name="frmAbrirFactura" id="frmAbrirFactura" target="_blank" method="post">
       <input type="hidden" name="t" value="" id="tipoArchivo">
       <input type="hidden" name="p" value="" id="claveRegistroCertificado">
       <input type="hidden" name="rfc" value="" id="rfcProveedor">
    </form>



    <form action="/siarex247/excel/exportCertificados.action" name="frmExportarCertificados" id="frmExportarCertificados" target="zipFile" method="post">
       <input type="hidden" name="idProveedores" value="" id="idProveedores">
    </form>




<script type="text/javascript">

var TITLE_NEW_CATALOGO = null;
var TITLE_EDIT_CATALOGO = null;
var TITLE_VIEW_CATALOGO = null;
var TITLE_DELETE_CATALOGO = null;

var LABEL_PROVEEDORES = null;
var LABEL_ELIMINAR_CERTIFICADOS = null; // ¿Estas seguro de eliminar los certificados ?
var MENSAJE_ELIMINAR_CERTIFICADOS = null; // Los certificados se han eliminado satisfactoriamente.
var MENSAJE_ENVIO_INFORMACION = null; // Los certificados se han eliminado satisfactoriamente.
var MENSAJE_ENVIO_INFORMACION = null; // Los certificados se han eliminado satisfactoriamente.
var MENSAJE_FINALIZO_CERTIFICADOS = null; // Finalizó proceso validación de certificados

var VALIDACION_ETQ108 = null; // Debe seleccionar al menos un registro para continuar



	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/catalogos/proveedores/modalProveedores.jsp');  
		$("#myModalEstatus").load('/siarex247/jsp/catalogos/proveedores/modalEstatus.jsp');
		$("#myModalValidar").load('/siarex247/jsp/catalogos/proveedores/modalValidar.jsp');
		$("#myModalEnviar").load('/siarex247/jsp/catalogos/proveedores/modalAcceso.jsp');
		
		calcularEtiquetasCatalogoProveedores();
	});
	
	

	 function calcularEtiquetasCatalogoProveedores(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_PROVEEDORES'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_PROVEEDORES_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CAT_PROVEEDORES_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CAT_PROVEEDORES_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CAT_PROVEEDORES_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CAT_PROVEEDORES_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CAT_PROVEEDORES_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CAT_PROVEEDORES_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CAT_PROVEEDORES_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CAT_PROVEEDORES_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("CAT_PROVEEDORES_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("CAT_PROVEEDORES_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CAT_PROVEEDORES_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CAT_PROVEEDORES_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CAT_PROVEEDORES_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CAT_PROVEEDORES_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CAT_PROVEEDORES_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CAT_PROVEEDORES_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CAT_PROVEEDORES_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CAT_PROVEEDORES_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CAT_PROVEEDORES_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CAT_PROVEEDORES_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("CAT_PROVEEDORES_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("CAT_PROVEEDORES_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("CAT_PROVEEDORES_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("CAT_PROVEEDORES_ETQ24").innerHTML = data.ETQ24;
						document.getElementById("CAT_PROVEEDORES_ETQ25").innerHTML = data.ETQ25;
						document.getElementById("CAT_PROVEEDORES_ETQ26").innerHTML = data.ETQ26;
						document.getElementById("CAT_PROVEEDORES_ETQ27").innerHTML = data.ETQ27;
						document.getElementById("CAT_PROVEEDORES_ETQ28").innerHTML = data.ETQ28;
						document.getElementById("CAT_PROVEEDORES_ETQ29").innerHTML = data.ETQ29;
						document.getElementById("CAT_PROVEEDORES_ETQ30").innerHTML = data.ETQ30;
						document.getElementById("CAT_PROVEEDORES_ETQ31").innerHTML = data.ETQ31;
						
						
						
						
						TITLE_NEW_CATALOGO = data.ETQ104;
						TITLE_EDIT_CATALOGO = data.ETQ105;
						TITLE_VIEW_CATALOGO = data.ETQ106;
						LABEL_PROVEEDORES = data.ETQ10;
						TITLE_DELETE_CATALOGO = data.ETQ107;
						VALIDACION_ETQ108 = data.ETQ108;
						LABEL_ELIMINAR_CERTIFICADOS = data.ETQ109;
						MENSAJE_ELIMINAR_CERTIFICADOS = data.ETQ110;
						VALIDACION_ETQ111 = data.ETQ111;
						MENSAJE_ENVIO_INFORMACION = data.ETQ112;
						MENSAJE_FINALIZO_CERTIFICADOS = data.ETQ123;
						
						document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
</script>

</html>