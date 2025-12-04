<%@page import="java.util.HashMap"%>
<%@page import="com.siarex247.session.ObtenerSession"%>
<%@page import="com.siarex247.session.SiarexSession"%>
<%@page import="com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean"%>
<%@page import="com.siarex247.seguridad.Usuarios.UsuariosForm"%>
<%@page import="com.siarex247.seguridad.Usuarios.UsuariosAction"%>
<%@page import="com.siarex247.utils.Utils"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
 
   	<script src="/siarex247/jsp/visor/detalleVisor.js?v=<%=Utils.VERSION%>"></script>
	<script src="/siarex247/jsp/visor/exportarEliminar/exportarEliminar.js?v=<%=Utils.VERSION%>"></script>
	<script src="/siarex247/jsp/visor/facturas/cargarFacturas.js?v=<%=Utils.VERSION%>"></script>
	
	<!-- ===== DetalleVisor: estilos filtros compactos ===== -->
<style>
  /* Filtros compactos — DX-like */
  .dx-like-filter{display:flex;align-items:center;gap:6px;position:relative;}
  .dx-like-filter .op-btn{min-width:28px;height:28px;padding:0 6px;display:inline-flex;align-items:center;justify-content:center;cursor:pointer;border:1px solid #d0d5dd;border-radius:4px;background:#fff;line-height:1;}
  .dx-like-filter .op-label{font-size:12px;line-height:1;font-weight:600;}
  .dx-like-filter input, .dx-like-filter select{height:28px;line-height:28px;padding:0 8px;border:1px solid #d0d5dd;border-radius:4px;width:100%;}
  .dx-like-filter input[type="number"]{width:100px;}
  .dx-like-menu{position:absolute;z-index:1000;background:#fff;border:1px solid #e5e7eb;border-radius:6px;box-shadow:0 6px 20px rgba(0,0,0,.08);min-width:180px;display:none;top:32px;left:0;}
  .dx-like-menu.show{display:block;}
  .dx-like-menu ul{list-style:none;margin:0;padding:4px;}
  .dx-like-menu li{padding:6px 10px;cursor:pointer;border-radius:4px;white-space:nowrap;}
  .dx-like-menu li:hover{background:#f3f4f6;}
  /* thead + FixedHeader */
  #tablaDetalleVisor thead tr.filters th{position:relative;overflow:visible!important;}
  table.dataTable thead th,table.dataTable thead td{overflow:visible!important;}
  .dtfh-floatingparent thead th{overflow:visible!important;}
  #tablaDetalleVisor thead .dx-like-menu{z-index:1090;}
  
  .dx-like-filter .dx-input { width:100%; height: calc(2.25rem); padding:.375rem .5rem; }
  
  /* COLUMNA TIPO DE MONEDA */
#tablaDetalleVisor th#VISOR_COL5, #tablaDetalleVisor td:nth-child(4){ min-width: 180px; }
  /* COLUMNA TIPO DE MONEDA */
#tablaDetalleVisor th#VISOR_COL7, #tablaDetalleVisor td:nth-child(4){ min-width: 150px; }
  /* COLUMNA ASIGNAR A  */
#tablaDetalleVisor th#VISOR_COL28, #tablaDetalleVisor td:nth-child(4){ min-width: 150px; }
  /* COLUMNA ESTADOS CFDI */
#tablaDetalleVisor th#VISOR_COL30, #tablaDetalleVisor td:nth-child(4){ min-width: 150px; }
#tablaDetalleVisor th#VISOR_COL32, #tablaDetalleVisor td:nth-child(4){ min-width: 150px; }
</style>

	
	

<style type="text/css">

  .redBorder {
  	color: red;
  }
  
  .colorBlack {
  	color: black;
  }

 .ocultarBoton{
    visibility: hidden;
    background-color: transparent;
 }
</style>


<style>
  /* Calendario por encima de headers/modales */
.flatpickr-calendar { z-index: 99999 !important; }
#rangeHintV.hint-show { transition: opacity .25s ease; opacity: 1; }
#rangeHintV.d-none { opacity: 0; }

</style>

    <%
    SiarexSession sessionSiarex = ObtenerSession.getSession(request);	
    
    String tipoMoneda = Utils.noNulo(request.getParameter("tipoMoneda"));
    String rfcProveedor = Utils.noNulo(request.getParameter("rfcProveedor"));
    String estatusOrden = Utils.noNulo(request.getParameter("estatusOrden"));
    
    
    UsuariosAction usaurioAction  = new UsuariosAction();
    UsuariosForm usuariosForm = usaurioAction.consultaPermisosJSP(request);
    
    ConfigAdicionalesBean configAdicinales = new ConfigAdicionalesBean();
    HashMap<String, String> mapaConfi =  configAdicinales.obtenerConfiguraciones(sessionSiarex.getEsquemaEmpresa());
    
    String PERMITIR_ACCESO_GENERADOR = Utils.noNulo(mapaConfi.get("PERMITIR_ACCESO_GENERADOR"));
    
    
    %>
</head>

 <input type="hidden" name="tipoMoneda" id="tipoMoneda_Visor" value="<%=tipoMoneda%>">
 
<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="VISOR_TITLE1">Visor de Ordenes de Compra</h5>
			</div>

			<div class="col-auto d-flex">
				<a class="btn btn-sm btn-falcon-default mx-2" href="javascript:mostrarOpcion('/siarex247/jsp/visor/indexVisor.jsp');" role="button"><i class="fas fa-undo me-2"></i> <span id="VISOR_MENU0">Visor Principal </span> </a>

<%if (usuariosForm.getIdPerfil() != 5 ){ %>				
				<div class="dropdown font-sans-serif" style="margin-right: 10px;">
                <button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
                	<span class="fas fa-cloud-upload-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="VISOR_MENU1">Cargar</span>
                </button>
                <div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu" style="">

	<%if ("N".equals(usuariosForm.getIsAmericano())){ %>                
                	<a id="menuCargar_Factura" class="dropdown-item" href="javascript:abreModal('cargarFactura', 0);" >Cargar Facturas</a>
	<%} %>

	<%if ("S".equals(usuariosForm.getIsAmericano()) || usuariosForm.getIdPerfil() != 4 ){ %>
                	<a id="menuCargar_FacturaAmericana" class="dropdown-item" href="javascript:abreModal('cargarFacturaAmericana', 0);">Cargar Factura Americana</a>
                	<%if ("S".equalsIgnoreCase(PERMITIR_ACCESO_GENERADOR)) {%>
                		<a id="menuCargar_GenerarFactura" class="dropdown-item" href="javascript:validaGeneraFactura();">Generar Factura</a>
                	<%} %>
                	<a id="menuCargar_AdjuntarLogo" class="dropdown-item" href="javascript:abrirPantallaLogo();">Adjuntar Logo</a>
	<%} %>

	<%if ("N".equals(usuariosForm.getIsAmericano())){ %>                	
                  	<a id="menuCargar_Complemento" class="dropdown-item" href="javascript:abreModal('cargarComplemento', 0);">Cargar Compelmentos</a>
					<a id="menuCargar_NotaCredito" class="dropdown-item" href="javascript:abreModal('cargarNota', 0);">Cargar Notas Creditos</a>
					<!-- 
					<a id="menuCargar_CartaPorte" class="dropdown-item" href="javascript:cargarCartaPorte();" >Cargar Carta Porte</a>
					 -->
	<%} %>

<!--   
					<a id="menuCargar_SolicitarLiberacion" class="dropdown-item" href="javascript:solicitarLiberacionPago();">Solicitar Liberaci&oacute;n de Pago</a>
 -->
  					
				</div>
	<%} %>
				
              </div>

<%if (usuariosForm.getIdPerfil() != 4 && usuariosForm.getIdPerfil() != 2 && usuariosForm.getIdPerfil() != 5 ){ %>
			 <div id="menuValidar_Completo" class="dropdown font-sans-serif" style="margin-right: 10px;">
                <button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
                	<span class="fas fa-check" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="VISOR_MENU2">Validar</span>
                </button>
                <div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu" style="">
					<a class="dropdown-item" href="javascript:validarClaves();" id="VISOR_OPCION8">Validar Claves CFDI</a>
					<a class="dropdown-item" href="javascript:consultarNotaCredito();" id="VISOR_OPCION9">Validar Notas Crédito</a>
					<!-- 
					<a class="dropdown-item" href="javascript:validarClavesCP();" id="VISOR_OPCION10">Validar Claves Carta Porte</a>
					 -->
					<a class="dropdown-item" href="javascript:consultarEstatusAmericanaValidacion();" id="VISOR_OPCION11">Validar Ordenes Americanas</a>
				</div>
              </div>
<%} %>                            
<%if (usuariosForm.getIdPerfil() != 4 && usuariosForm.getIdPerfil() != 5 ){ %>
              <div id="menuMovimientos_Completo" class="dropdown font-sans-serif">
                <button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
                	<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="VISOR_MENU3">Movimientos</span>
                </button>
                <div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu" style="">
                   <a class="dropdown-item" href="javascript:exportarFacturasDX();" id="VISOR_OPCION12">Exportar Facturas</a>
                    <a class="dropdown-item" href="javascript:exportarlayOutXD();" onclick="return exportarLayOut();" id="VISOR_OPCION13">Exportar LayOut</a>

                   <a class="dropdown-item" href="javascript:exportarPlantillaDX();" onclick="return exportarPlantillaDX();" id="VISOR_OPCION13">Exportar Plantillas</a>
                    
                    <%if (usuariosForm.getIdPerfil() != 2 || usuariosForm.getIdPerfil() != 5){ %>
						<a id="menuMovimientos_FechaRecepcion" class="dropdown-item" href="javascript:consultarFechaPago();">Modificar Fecha de Recepción de Pago</a>
						                    
	                    <div class="dropdown-divider"></div>
	                    <a id="menuMovimientos_EliminarOrdenes" class="dropdown-item text-danger" href="javascript:eliminarOrdenes();">Eliminar Ordenes de Compra</a>
						<a id="menuMovimientos_EliminarComplemento" class="dropdown-item text-danger" href="javascript:eliminarComplementoMenu();">Eliminar Complemento</a>
						<a id="menuMovimientos_EliminarNota" class="dropdown-item text-danger" href="javascript:eliminarNotaCreditoMenu();">Eliminar Nota Credito</a>
						<!-- 
						<a id="menuMovimientos_EliminarCartaPorte" class="dropdown-item text-danger" href="javascript:eliminarCartaPorte();">Eliminar Carta Porte</a>
						 -->
					<%} %>
									
				</div>
              </div>
<%} %>              
              
			</div>
			
		</div>
		
	</div>
	
	
	<div class="card-header">
		<div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="fechaInicial_Filtro" id="VISOR_ETQ34" >Fecha Inicio</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
	              
				<label class="col-sm-1 col-form-label" for="fechaFinal_Filtro" id="VISOR_ETQ35">Fecha Fin</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
					
        </div>
   </div>
	
	
	<div class="card-header">
					<div class="collapse" id="filtrosBusquedaVisor">
							<div class="mb-2 row">
								  <label class="col-sm-1 col-form-label" for="rfcFiltro_Visor" id="RFC_FILTRO_BUSQUEDA" >RFC</label>
						          <div class="col-sm-2">
						            <div class="form-group">
						              <input id="rfcFiltro_Visor" name="rfc" class="form-control" type="text" value="<%=rfcProveedor %>" maxlength="15">
						            </div>
						          </div>
						          <label class="col-sm-1 col-form-label" for="razonSocialFiltro_Visor"id="RAZON_SOCIAL_FILTRO_BUSQUEDA" >Razón Social</label>
						          <div class="col-sm-3">
						            <div class="form-group">
						              <input id="razonSocialFiltro_Visor" name="rfc" class="form-control" type="text" maxlength="200">
						            </div>
						          </div>
						          
						          
							     <label class="col-sm-1 col-form-label" for="fechaInicial" id="VISOR_ETQ34_BK" >Fecha Inicio</label>
						          <div class="col-sm-2">
						            <div class="form-group">
						              <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_BK" name="fechaInicial" type="text" placeholder="yyyy/mm/dd" data-options='{"disableMobile":true}' readonly="readonly"/>
						            </div>
						          </div>
						     </div>
						     <div class="mb-2 row">
						     	  <label class="col-sm-1 col-form-label" for="ordenCompra_Visor" id="ORDEN_COMPRA_FILTRO_BUSQUEDA" >No. Orden</label>
						          <div class="col-sm-2">
						            <div class="form-group">
						              <input id="ordenCompra_Visor" name="folioEmpresa" class="form-control" type="number" maxlength="20">
						            </div>
						          </div>
						          <label class="col-sm-1 col-form-label" for="uuidFiltro_Visor" id="UUID_FILTRO_BUSQUEDA" >UUID</label>
						          <div class="col-sm-3">
						            <div class="form-group">
						              <input id="uuidFiltro_Visor" name="uuid" class="form-control" type="text" maxlength="200">
						            </div>
						          </div>
						          <label class="col-sm-1 col-form-label" for="fechaFinal" id="VISOR_ETQ35_BK">Fecha Final</label>
						           <div class="col-sm-2">
						             <div class="form-group">
						               <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_BK" name="fechaFinal" type="text" placeholder="yyyy/mm/dd" data-options='{"disableMobile":true}' readonly="readonly" />
						             </div>
						           </div>
						 	</div>
						 	<div class="mb-2 row">
						 			<label class="col-sm-1 col-form-label" for="estatusOrden_Visor" id="ESTATUS_FILTRO_BUSQUEDA">Estatus</label>
									<div class="col-sm-2">
										<div class="form-group">
										<select class="form-select" id="estatusOrden_Visor" name="estatusOrden" required>
										
										    <option value="">Seleccione una opción ...</option>
										    
										    <option value="A1" <%if ("A1".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ6"> A1 - Servicio no recibido y con factura </label> </option>
										    <option value="A2" <%if ("A2".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ7"> A2 - Servicio recibido y sin factura </label> </option>
										    <option value="A3" <%if ("A3".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ8"> A3 - Factura lista para pago </label> </option>
										    <option value="A4" <%if ("A4".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ9"> A4 - Factura pagada sin complemento de pago </label> </option>
										    <option value="A5" <%if ("A5".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ10"> A5 - Servicio no recibido y sin factura </label> </option>
										    <option value="A6" <%if ("A6".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ105"> A6 - Factura pagada con complemento de pago </label> </option>
										    <option value="A9" <%if ("A9".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ101"> A9 - Bajo Validación </label> </option>
										    <option value="A10" <%if ("A10".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ102"> A10 - Validar Claves de CFDI </label> </option>
										    <option value="A11" <%if ("A11".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> <label id="VISOR_ETQ103"> A11 - Bajo Validacion Nota de Crédito </label> </option>
										    <!-- 
										    	<option value="A12" <%if ("A12".equalsIgnoreCase(estatusOrden)){ %> selected="selected" <% } %>> A12 - VALIDAR CLAVES CFDI CARTA PORTE</option>
										     -->
										    
										</select>
										</div>
									</div>
									 <label class="col-sm-1 col-form-label" for="serieFolio_Visor" id="SERIE_FILTRO_BUSQUEDA" >Serie/Folio</label>
							          <div class="col-sm-3">
							            <div class="form-group">
							              <input id="serieFolio_Visor" name="serieFolio" class="form-control" type="text" maxlength="20">
							            </div>
							          </div>
									
										<div class="col-sm-2">
										  <div class="form-group">
									  	    <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="validarFechas();" id="btnRefrescar_Visor" ><span class="fab fa-firefox-browser me-1"></span> <span id="VISOR_BTN_REFRESCAR"> Refrescar </span> </button>
									  	    <button class="btn btn-falcon-secondary btn-sm mb-2 mb-sm-0" type="button" onclick="limpiarVisor();" id="btnLimipiar_Visor" ><span class="fas fa-broom me-1"></span> <span id="BOVEDA_BTN_LIMPIAR"> Limpiar </span>  </button>
									  	    
									  	    <div id="overSeccion" class="overlay" style="display: none;text-align: right;">
											  <img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
										    </div>
										  </div>
									   </div>
								   
									
						 	</div>
                    
                    </div>
         </div> <!-- detalleXML -->	
	</div>
	
	
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				  <form id="frmTableVisor">
					<table id="tablaDetalleVisor"class="table table-sm mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<th class="no-sort pe-1 align-middle data-table-row-action" id="VISOR_COL1">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL2" >Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL3" >Orden de Compra</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL4" >Descripción</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL5" >Tipo Moneda</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL6" >Monto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL7" >Servicio Recibo ?</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL8" >Status de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL9" >Serie/Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL10" >Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL11" >Subtotal</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL12" >IVA</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL13" >IVA RET</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL14" >ISR RET</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL15" >IMP Locales</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL16" >XML</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL17" >PDF</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL18" >XML COMP.</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL19" >PDF COMP.</th>
								<!-- 
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL20" >XML CARTA PORTE</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL21" >PDF CARTA PORTE</th>
								 -->
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL22" >XML NC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL23" >PDF NC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL24" >TOTAL NC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL25" >Pago Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL26" >IVA RET NC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL27" >Fecha de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL28" >Asignar A</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL29" >Ultimo Movimiento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL30" >Estado CFDI</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL31" >Estatus en SAT</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL32" >UsoCFDI</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_COL33" >Clave de Producto Servicio</th>
							</tr>
					  <!-- ===== FILA DE FILTROS ===== -->
		<tr class="filters">
					      <!-- 1 Acciones: sin filtro -->
					      <th></th>
					
					      <!-- 2 Razón Social (texto) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="rsOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <input type="text" id="rsFilterV" placeholder="Razón social..." />
					          <div class="dx-like-menu" id="rsOpMenuV">
					            <ul>
					              <li data-op="contains">⊚ Contiene</li><li data-op="notContains">⊘ No contiene</li>
					              <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
					              <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
					              <li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="rsOperatorV" value="contains">
					      </th>
					
										<!-- 3 Orden de Compra (numérico) -->
						<th>
						  <div class="dx-like-filter">
						    <span class="op-btn" id="ocOpBtnV"><span class="op-label">=</span></span>
						    <input type="number" id="ocV1V" placeholder="Folio OC..." />
						    <input type="number" id="ocV2V" placeholder="hasta..." style="display:none" />
						    <div class="dx-like-menu" id="ocOpMenuV">
						      <ul>
						        <li data-op="eq">= Igual</li>
						        <li data-op="ne">≠ Distinto</li>
						        <li data-op="lt">&lt; Menor que</li>
						        <li data-op="le">≤ Menor o igual</li>
						        <li data-op="gt">&gt; Mayor que</li>
						        <li data-op="ge">≥ Mayor o igual</li>
						        <li data-op="bt">↔ Entre</li>
						        <li data-op="reset">⟲  Reset</li>
						      </ul>
						    </div>
						  </div>
						  <input type="hidden" id="ocOperatorV" value="eq">
						</th>

					
					      <!-- 4 Descripción (texto) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="descOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <input type="text" id="descFilterV" placeholder="Descripción..." />
					          <div class="dx-like-menu" id="descOpMenuV">
					            <ul>
					              <li data-op="contains">⊚ Contiene</li><li data-op="notContains">⊘ No contiene</li>
					              <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
					              <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
					              <li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="descOperatorV" value="contains">
					      </th>
					
					      <!-- 5 Tipo Moneda (select) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="monedaOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <select id="monedaFilterV" class="form-select form-select-sm">
					            <option value="ALL">TODAS</option>
					            <option value="MXN">MXN</option>
					            <option value="USD">USD</option>
					          </select>
					          <input type="hidden" id="monedaOperatorV" value="contains">
					        </div>
					      </th>
					
					      <!-- 6 Monto (num con operadores) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="montoOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="montoFilter1V" placeholder="Monto..." />
					          <input type="number" step="any" id="montoFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="montoOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="montoOperatorV" value="eq">
					      </th>
					
					      <!-- 7 Servicio Recibo ? (S/N) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="reciboOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <select id="reciboFilterV" class="form-select form-select-sm">
					            <option value="ALL">TODOS</option>
					            <option value="1">SI</option>
					            <option value="0">NO</option>
					          </select>
					          <input type="hidden" id="reciboOperatorV" value="contains">
					        </div>
					      </th>
					
					      <!-- 8 Status de Pago (A1..A11) -->
					      <th>
					        <div class="dx-like-filter" style="min-width:260px;">
					          <span class="op-btn" id="estatusPagoOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <select id="estatusPagoFilterV" class="form-select form-select-sm">
					            <option value="ALL">TODOS</option>
					            <option value="">Seleccione una opción ...</option>
					            <option value="A1">A1 - Servicio no recibido y con factura</option>
					            <option value="A2">A2 - Servicio recibido y sin factura</option>
					            <option value="A3">A3 - Factura lista para pago</option>
					            <option value="A4">A4 - Factura pagada sin complemento de pago</option>
					            <option value="A5">A5 - Servicio no recibido y sin factura</option>
					            <option value="A6">A6 - Factura pagada con complemento de pago</option>
					            <option value="A9">A9 - Bajo Validación</option>
					            <option value="A10">A10 - Validar Claves de CFDI</option>
					            <option value="A11">A11 - Bajo Validacion Nota de Crédito</option>
					          </select>
					          <input type="hidden" id="estatusPagoOperatorV" value="contains">
					        </div>
					      </th>
					
					      <!-- 9 Serie/Folio (texto) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="sfOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <input type="text" id="sfFilterV" placeholder="Serie / Folio..." />
					          <div class="dx-like-menu" id="sfOpMenuV">
					            <ul>
					              <li data-op="contains">⊚ Contiene</li><li data-op="notContains">⊘ No contiene</li>
					              <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
					              <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
					              <li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="sfOperatorV" value="contains">
					      </th>
					
					      <!-- 10 Total (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="totalOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="totalFilter1V" placeholder="Total..." />
					          <input type="number" step="any" id="totalFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="totalOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="totalOperatorV" value="eq">
					      </th>
					
					      <!-- 11 Subtotal (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="subtotalOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="subtotalFilter1V" placeholder="Subtotal..." />
					          <input type="number" step="any" id="subtotalFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="subtotalOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="subtotalOperatorV" value="eq">
					      </th>
					
					      <!-- 12 IVA (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="ivaOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="ivaFilter1V" placeholder="IVA..." />
					          <input type="number" step="any" id="ivaFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="ivaOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="ivaOperatorV" value="eq">
					      </th>
					
					      <!-- 13 IVA RET (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="ivaretOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="ivaretFilter1V" placeholder="IVA RET..." />
					          <input type="number" step="any" id="ivaretFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="ivaretOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="ivaretOperatorV" value="eq">
					      </th>
					
					      <!-- 14 ISR RET (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="isrretOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="isrretFilter1V" placeholder="ISR RET..." />
					          <input type="number" step="any" id="isrretFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="isrretOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="isrretOperatorV" value="eq">
					      </th>
					
					      <!-- 15 IMP Locales (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="implocOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="implocFilter1V" placeholder="Imp. locales..." />
					          <input type="number" step="any" id="implocFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="implocOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="implocOperatorV" value="eq">
					      </th>
					
					      <!-- 16-19 (XML/PDF/COMP): sin filtro -->
					      <th></th><th></th><th></th><th></th>
					
					      <!-- 22 XML NC: sin filtro -->
					      <th></th>
					
					      <!-- 23 PDF NC: sin filtro -->
					      <th></th>
					
					      <!-- 24 TOTAL NC (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="totalncOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="totalncFilter1V" placeholder="Total NC..." />
					          <input type="number" step="any" id="totalncFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="totalncOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="totalncOperatorV" value="eq">
					      </th>
					
					      <!-- 25 Pago Total (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="pagotOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="pagotFilter1V" placeholder="Pago Total..." />
					          <input type="number" step="any" id="pagotFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="pagotOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="pagotOperatorV" value="eq">
					      </th>
					
					      <!-- 26 IVA RET NC (num) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="ivaretncOpBtnV"><span class="op-label">=</span></span>
					          <input type="number" step="any" id="ivaretncFilter1V" placeholder="IVA RET NC..." />
					          <input type="number" step="any" id="ivaretncFilter2V" placeholder="y..." class="d-none" />
					          <div class="dx-like-menu" id="ivaretncOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor</li><li data-op="gt">&gt; Mayor</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="between">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="ivaretncOperatorV" value="eq">
					      </th>
					
					      <!-- 27 Fecha de Pago (fecha) -->
					      <th>
					        <div class="dx-like-filter" style="min-width:260px;">
					          <span class="op-btn" id="fechapagoOpBtnV"><span class="op-label">=</span></span>
					          <input type="date" id="fechapagoFilter1V" />
					          <input type="date" id="fechapagoFilter2V" class="d-none" />
					          <div class="dx-like-menu" id="fechapagoOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="bt">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="fechapagoOperatorV" value="eq">
					      </th>
					
					      <!-- 28 Asignar A (texto) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="asignarOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <input type="text" id="asignarFilterV" placeholder="Asignar a..." />
					          <div class="dx-like-menu" id="asignarOpMenuV">
					            <ul>
					              <li data-op="contains">⊚ Contiene</li><li data-op="notContains">⊘ No contiene</li>
					              <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
					              <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
					              <li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="asignarOperatorV" value="contains">
					      </th>
					
					      <!-- 29 Ultimo Movimiento (fecha) 
					      <th>
					        <div class="dx-like-filter" style="min-width:260px;">
					          <span class="op-btn" id="ultmovOpBtnV"><span class="op-label">=</span></span>
					          <input type="date" id="ultmovFilter1V" />
					          <input type="date" id="ultmovFilter2V" class="d-none" />
					          <div class="dx-like-menu" id="ultmovOpMenuV">
					            <ul>
					              <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					              <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
					              <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					              <li data-op="bt">↔ Entre</li><li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="ultmovOperatorV" value="eq">
					      </th>-->
					      
					      <th>
								  <input type="hidden" id="ultmovOperatorV" value="eq">
								  <input type="hidden" id="ultmovFilter1V">
								  <input type="hidden" id="ultmovFilter2V">
								  <!-- opcional, placeholder por si algún JS toca el label -->
								  <span id="ultmovOpBtnV" class="d-none"><span class="op-label">=</span></span>
								</th>
					      
					      
					
					     <!-- 30 Estado CFDI (combo: TODOS / VIGENTE / CANCELADO / NO ENCONTRADO) -->
				<!-- 30 Estado CFDI (combo: TODOS / VIGENTE / CANCELADO / NO ENCONTRADO) -->
					<th>
					  <div class="dx-like-filter">
					    <span class="op-btn" id="estadoCfdiOpBtnV">
					      <span class="op-label"><i class="fas fa-search"></i></span>
					    </span>
					
					    <!-- usa el mismo estilo que Estatus SAT -->
					    <select id="estadoCfdiFilterV" class="form-select form-select-sm">
					      <option value="ALL">TODOS</option>
					      <option value="VIGENTE">VIGENTE</option>
					      <option value="CANCELADO">CANCELADO</option>
					      <option value="NO ENCONTRADO">NO ENCONTRADO</option>
					    </select>
					
					    <!-- (opcional) deja el menú si quieres, pero no es necesario para combo -->
					    <div class="dx-like-menu" id="estadoCfdiOpMenuV" style="display:none">
					      <ul>
					        <li data-op="contains">⊚ Contiene</li>
					        <li data-op="equals">= Igual</li>
					      </ul>
					    </div>
					  </div>
					  <input type="hidden" id="estadoCfdiOperatorV" value="contains">
					</th>


					      <!-- 31 Estatus en SAT (S/N) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="estatusSatOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <select id="estatusSatFilterV" class="form-select form-select-sm">
					            <option value="ALL">TODOS</option>
					            <option value="S - Comprobante obtenido satisfactoriamente.">S - Comprobante obtenido satisfactoriamente</option>
					            <option value="N - 602: Comprobante no encontrado.">N - 602: Comprobante no encontrado</option>
					          </select>
					          <input type="hidden" id="estatusSatOperatorV" value="contains">
					        </div>
					      </th>
					
					     
					    <!-- 32 UsoCFDI (texto) -->
						<th>
						  <div class="dx-like-filter">
						    <span class="op-btn" id="usoCfdiOpBtnV">
						      <span class="op-label"><i class="fas fa-search"></i></span>
						    </span>
						
						    <!-- ahora es input de texto, mismo id -->
						    <input type="text" id="usoCfdiFilterV" placeholder="Uso CFDI..." />
						
						    <!-- menú de operadores para texto -->
						    <div class="dx-like-menu" id="usoCfdiOpMenuV">
						      <ul>
						        <li data-op="contains">⊚ Contiene</li>
						        <li data-op="notContains">⊘ No contiene</li>
						        <li data-op="startsWith">↤ Empieza con</li>
						        <li data-op="endsWith">↦ Termina con</li>
						        <li data-op="equals">= Igual</li>
						        <li data-op="notEquals">≠ Distinto</li>
						        <li data-op="reset">⟲ Reset</li>
						      </ul>
						    </div>
						  </div>
						  <input type="hidden" id="usoCfdiOperatorV" value="contains">
						</th>

					
					      <!-- 33 Clave de Producto Servicio (texto) -->
					      <th>
					        <div class="dx-like-filter">
					          <span class="op-btn" id="cpsOpBtnV"><span class="op-label"><i class="fas fa-search"></i></span></span>
					          <input type="text" id="cpsFilterV" placeholder="Clave Prod/Serv..." />
					          <div class="dx-like-menu" id="cpsOpMenuV">
					            <ul>
					              <li data-op="contains">⊚ Contiene</li><li data-op="notContains">⊘ No contiene</li>
					              <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
					              <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
					              <li data-op="reset">⟲  Reset</li>
					            </ul>
					          </div>
					        </div>
					        <input type="hidden" id="cpsOperatorV" value="contains">
					      </th>
							</tr>

							
						</thead>
					</table>
				   </form>	
				</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<%if (usuariosForm.getIdPerfil() != 4 ){ %>

  <form id="frmExportarPlantilla" name="frmExportarPlantilla" class="easyui-form"
      method="post" action="/siarex247/excel/exportarPlantilla.action" target="frmOcultoSiarex">

  <!-- selección explícita -->
  <input type="hidden" name="foliosExportar" id="plt_foliosExportar">

  <!-- ===== TEXTO/SELECTS (valor, operador) ===== -->
  <input type="hidden" name="razonSocial"       id="plt_razonSocial">
  <input type="hidden" name="rsOperator"        id="plt_rsOp">

  <!-- OC (numérico) -->
  <input type="hidden" name="ocOperator"        id="plt_ocOperator">
  <input type="hidden" name="ocV1"              id="plt_ocV1">
  <input type="hidden" name="ocV2"              id="plt_ocV2">

  <input type="hidden" name="descripcion"       id="plt_descripcion">
  <input type="hidden" name="descOperator"      id="plt_descOp">

  <input type="hidden" name="tipoMoneda"        id="plt_tipoMoneda">
  <input type="hidden" name="monedaOperator"    id="plt_monedaOp">

  <input type="hidden" name="servicioRecibo"    id="plt_servicioRecibo">
  <input type="hidden" name="reciboOperator"    id="plt_reciboOp">

  <input type="hidden" name="estatusPago"       id="plt_estatusPago">
  <input type="hidden" name="estatusPagoOperator" id="plt_estatusPagoOp">

  <input type="hidden" name="serieFolio"        id="plt_serieFolio">
  <input type="hidden" name="serieFolioOperator" id="plt_serieFolioOp">

  <input type="hidden" name="asignarA"          id="plt_asignarA">
  <input type="hidden" name="asignarOperator"   id="plt_asignarOp">

  <input type="hidden" name="estadoCfdi"        id="plt_estadoCfdi">
  <input type="hidden" name="estadoCfdiOperator" id="plt_estadoCfdiOp">

  <input type="hidden" name="estatusSat"        id="plt_estatusSat">
  <input type="hidden" name="estatusSatOperator" id="plt_estatusSatOp">

  <input type="hidden" name="usoCfdi"           id="plt_usoCfdi">
  <input type="hidden" name="usoCfdiOperator"   id="plt_usoCfdiOp">

  <input type="hidden" name="cps"               id="plt_cps">
  <input type="hidden" name="cpsOperator"       id="plt_cpsOp">

  <!-- ===== NUMÉRICOS (operator, v1, v2) ===== -->
  <input type="hidden" name="montoOperator"     id="plt_montoOp"><input type="hidden" name="montoV1" id="plt_montoV1"><input type="hidden" name="montoV2" id="plt_montoV2">
  <input type="hidden" name="totalOperator"     id="plt_totalOp"><input type="hidden" name="totalV1" id="plt_totalV1"><input type="hidden" name="totalV2" id="plt_totalV2">
  <input type="hidden" name="subtotalOperator"  id="plt_subtotalOp"><input type="hidden" name="subtotalV1" id="plt_subtotalV1"><input type="hidden" name="subtotalV2" id="plt_subtotalV2">
  <input type="hidden" name="ivaOperator"       id="plt_ivaOp"><input type="hidden" name="ivaV1" id="plt_ivaV1"><input type="hidden" name="ivaV2" id="plt_ivaV2">
  <input type="hidden" name="ivaretOperator"    id="plt_ivaretOp"><input type="hidden" name="ivaretV1" id="plt_ivaretV1"><input type="hidden" name="ivaretV2" id="plt_ivaretV2">
  <input type="hidden" name="isrretOperator"    id="plt_isrretOp"><input type="hidden" name="isrretV1" id="plt_isrretV1"><input type="hidden" name="isrretV2" id="plt_isrretV2">
  <input type="hidden" name="implocOperator"    id="plt_implocOp"><input type="hidden" name="implocV1" id="plt_implocV1"><input type="hidden" name="implocV2" id="plt_implocV2">
  <input type="hidden" name="totalncOperator"   id="plt_totalncOp"><input type="hidden" name="totalncV1" id="plt_totalncV1"><input type="hidden" name="totalncV2" id="plt_totalncV2">
  <input type="hidden" name="pagotOperator"     id="plt_pagotOp"><input type="hidden" name="pagotV1" id="plt_pagotV1"><input type="hidden" name="pagotV2" id="plt_pagotV2">
  <input type="hidden" name="ivaretncOperator"  id="plt_ivaretncOp"><input type="hidden" name="ivaretncV1" id="plt_ivaretncV1"><input type="hidden" name="ivaretncV2" id="plt_ivaretncV2">

  <!-- ===== FECHAS (operator, v1, v2) ===== -->
  <input type="hidden" name="fechapagoOperator" id="plt_fechapagoOp">
  <input type="hidden" name="fechapagoV1"       id="plt_fechapagoV1">
  <input type="hidden" name="fechapagoV2"       id="plt_fechapagoV2">

  <input type="hidden" name="ultmovOperator"    id="plt_ultmovOp">
  <input type="hidden" name="ultmovV1"          id="plt_ultmovV1">
  <input type="hidden" name="ultmovV2"          id="plt_ultmovV2">
</form>

  


	<form id="frmExportarFacturas" method="post"
	      action="/siarex247/excel/exportarFacturas.action" target="frmOcultoSiarex">
	
	  <!-- selección directa -->
	  <input type="hidden" name="foliosExportar" id="dx_foliosExportar">
	
	  <!-- ===== TEXTO / SELECTS (valor, operador) ===== -->
	  <input type="hidden" name="razonSocial"     id="dx_razonSocial">
	  <input type="hidden" name="rsOperator"      id="dx_rsOperator">
	
	  <!-- OC (numérico) -->
	  <input type="hidden" name="ocOperator"      id="dx_ocOperator">
	  <input type="hidden" name="ocV1"            id="dx_ocV1">
	  <input type="hidden" name="ocV2"            id="dx_ocV2">
	
	  <input type="hidden" name="descripcion"     id="dx_descripcion">
	  <input type="hidden" name="descOperator"    id="dx_descOperator">
	
	  <input type="hidden" name="tipoMoneda"      id="dx_tipoMoneda">
	  <input type="hidden" name="monedaOperator"  id="dx_monedaOperator">
	
	  <input type="hidden" name="servicioRecibo"  id="dx_servicioRecibo">
	  <input type="hidden" name="reciboOperator"  id="dx_reciboOperator">
	
	  <input type="hidden" name="estatusPago"     id="dx_estatusPago">
	  <input type="hidden" name="estatusPagoOperator" id="dx_estatusPagoOperator">
	
	  <input type="hidden" name="serieFolio"      id="dx_serieFolio">
	  <input type="hidden" name="serieFolioOperator" id="dx_serieFolioOperator">
	
	  <input type="hidden" name="asignarA"        id="dx_asignarA">
	  <input type="hidden" name="asignarOperator" id="dx_asignarOperator">
	
	  <input type="hidden" name="estadoCfdi"      id="dx_estadoCfdi">
	  <input type="hidden" name="estadoCfdiOperator" id="dx_estadoCfdiOperator">
	
	  <input type="hidden" name="estatusSat"      id="dx_estatusSat">
	  <input type="hidden" name="estatusSatOperator" id="dx_estatusSatOperator">
	
	  <input type="hidden" name="usoCfdi"         id="dx_usoCfdi">
	  <input type="hidden" name="usoCfdiOperator" id="dx_usoCfdiOperator">
	
	  <input type="hidden" name="cps"             id="dx_cps">
	  <input type="hidden" name="cpsOperator"     id="dx_cpsOperator">
	
	  <!-- ===== NUMÉRICOS (operator, v1, v2) ===== -->
	  <input type="hidden" name="montoOperator"    id="dx_montoOperator">
	  <input type="hidden" name="montoV1"          id="dx_montoV1">
	  <input type="hidden" name="montoV2"          id="dx_montoV2">
	
	  <input type="hidden" name="totalOperator"    id="dx_totalOperator">
	  <input type="hidden" name="totalV1"          id="dx_totalV1">
	  <input type="hidden" name="totalV2"          id="dx_totalV2">
	
	  <input type="hidden" name="subtotalOperator" id="dx_subtotalOperator">
	  <input type="hidden" name="subtotalV1"       id="dx_subtotalV1">
	  <input type="hidden" name="subtotalV2"       id="dx_subtotalV2">
	
	  <input type="hidden" name="ivaOperator"      id="dx_ivaOperator">
	  <input type="hidden" name="ivaV1"            id="dx_ivaV1">
	  <input type="hidden" name="ivaV2"            id="dx_ivaV2">
	
	  <input type="hidden" name="ivaretOperator"   id="dx_ivaretOperator">
	  <input type="hidden" name="ivaretV1"         id="dx_ivaretV1">
	  <input type="hidden" name="ivaretV2"         id="dx_ivaretV2">
	
	  <input type="hidden" name="isrretOperator"   id="dx_isrretOperator">
	  <input type="hidden" name="isrretV1"         id="dx_isrretV1">
	  <input type="hidden" name="isrretV2"         id="dx_isrretV2">
	
	  <input type="hidden" name="implocOperator"   id="dx_implocOperator">
	  <input type="hidden" name="implocV1"         id="dx_implocV1">
	  <input type="hidden" name="implocV2"         id="dx_implocV2">
	
	  <input type="hidden" name="totalncOperator"  id="dx_totalncOperator">
	  <input type="hidden" name="totalncV1"        id="dx_totalncV1">
	  <input type="hidden" name="totalncV2"        id="dx_totalncV2">
	
	  <input type="hidden" name="pagotOperator"    id="dx_pagotOperator">
	  <input type="hidden" name="pagotV1"          id="dx_pagotV1">
	  <input type="hidden" name="pagotV2"          id="dx_pagotV2">
	
	  <input type="hidden" name="ivaretncOperator" id="dx_ivaretncOperator">
	  <input type="hidden" name="ivaretncV1"       id="dx_ivaretncV1">
	  <input type="hidden" name="ivaretncV2"       id="dx_ivaretncV2">
	
	  <!-- ===== FECHAS (operator, v1, v2) ===== -->
	  <input type="hidden" name="fechapagoOperator" id="dx_fechapagoOperator">
	  <input type="hidden" name="fechapagoV1"       id="dx_fechapagoV1">
	  <input type="hidden" name="fechapagoV2"       id="dx_fechapagoV2">
	
	  <input type="hidden" name="ultmovOperator"    id="dx_ultmovOperator">
	  <input type="hidden" name="ultmovV1"          id="dx_ultmovV1">
	  <input type="hidden" name="ultmovV2"          id="dx_ultmovV2">
	</form>
	
	
	
	   
	<form id="frmexportarLayOut" method="post"
      action="/siarex247/excel/exportarLayOut.action"
      target="frmOcultoSiarex">

  <!-- selección explícita -->
  <input type="hidden" name="foliosExportar" id="dxlo_foliosExportar">

  <!-- ==== OC (numérico) ==== -->
  <input type="hidden" name="ocOperator" id="dxlo_ocOperator">
  <input type="hidden" name="ocV1"       id="dxlo_ocV1">
  <input type="hidden" name="ocV2"       id="dxlo_ocV2">

  <!-- ===== TEXTO / SELECTS (valor, operador) ===== -->
  <input type="hidden" name="razonSocial"     id="dxlo_razonSocial">
  <input type="hidden" name="rsOperator"      id="dxlo_rsOperator">

  <input type="hidden" name="descripcion"     id="dxlo_descripcion">
  <input type="hidden" name="descOperator"    id="dxlo_descOperator">

  <input type="hidden" name="tipoMoneda"      id="dxlo_tipoMoneda">
  <input type="hidden" name="monedaOperator"  id="dxlo_monedaOperator">

  <input type="hidden" name="servicioRecibo"  id="dxlo_servicioRecibo">
  <input type="hidden" name="reciboOperator"  id="dxlo_reciboOperator">

  <input type="hidden" name="estatusPago"     id="dxlo_estatusPago">
  <input type="hidden" name="estatusPagoOperator" id="dxlo_estatusPagoOperator">

  <input type="hidden" name="serieFolio"      id="dxlo_serieFolio">
  <input type="hidden" name="serieFolioOperator" id="dxlo_serieFolioOperator">

  <input type="hidden" name="asignarA"        id="dxlo_asignarA">
  <input type="hidden" name="asignarOperator" id="dxlo_asignarOperator">

  <input type="hidden" name="estadoCfdi"      id="dxlo_estadoCfdi">
  <input type="hidden" name="estadoCfdiOperator" id="dxlo_estadoCfdiOperator">

  <input type="hidden" name="estatusSat"      id="dxlo_estatusSat">
  <input type="hidden" name="estatusSatOperator" id="dxlo_estatusSatOperator">

  <input type="hidden" name="usoCfdi"         id="dxlo_usoCfdi">
  <input type="hidden" name="usoCfdiOperator" id="dxlo_usoCfdiOperator">

  <input type="hidden" name="cps"             id="dxlo_cps">
  <input type="hidden" name="cpsOperator"     id="dxlo_cpsOperator">

  <!-- ===== NUMÉRICOS (operator, v1, v2) ===== -->
  <input type="hidden" name="montoOperator"    id="dxlo_montoOperator">
  <input type="hidden" name="montoV1"          id="dxlo_montoV1">
  <input type="hidden" name="montoV2"          id="dxlo_montoV2">

  <input type="hidden" name="totalOperator"    id="dxlo_totalOperator">
  <input type="hidden" name="totalV1"          id="dxlo_totalV1">
  <input type="hidden" name="totalV2"          id="dxlo_totalV2">

  <input type="hidden" name="subtotalOperator" id="dxlo_subtotalOperator">
  <input type="hidden" name="subtotalV1"       id="dxlo_subtotalV1">
  <input type="hidden" name="subtotalV2"       id="dxlo_subtotalV2">

  <input type="hidden" name="ivaOperator"      id="dxlo_ivaOperator">
  <input type="hidden" name="ivaV1"            id="dxlo_ivaV1">
  <input type="hidden" name="ivaV2"            id="dxlo_ivaV2">

  <input type="hidden" name="ivaretOperator"   id="dxlo_ivaretOperator">
  <input type="hidden" name="ivaretV1"         id="dxlo_ivaretV1">
  <input type="hidden" name="ivaretV2"         id="dxlo_ivaretV2">

  <input type="hidden" name="isrretOperator"   id="dxlo_isrretOperator">
  <input type="hidden" name="isrretV1"         id="dxlo_isrretV1">
  <input type="hidden" name="isrretV2"         id="dxlo_isrretV2">

  <input type="hidden" name="implocOperator"   id="dxlo_implocOperator">
  <input type="hidden" name="implocV1"         id="dxlo_implocV1">
  <input type="hidden" name="implocV2"         id="dxlo_implocV2">

  <input type="hidden" name="totalncOperator"  id="dxlo_totalncOperator">
  <input type="hidden" name="totalncV1"        id="dxlo_totalncV1">
  <input type="hidden" name="totalncV2"        id="dxlo_totalncV2">

  <input type="hidden" name="pagotOperator"    id="dxlo_pagotOperator">
  <input type="hidden" name="pagotV1"          id="dxlo_pagotV1">
  <input type="hidden" name="pagotV2"          id="dxlo_pagotV2">

  <input type="hidden" name="ivaretncOperator" id="dxlo_ivaretncOperator">
  <input type="hidden" name="ivaretncV1"       id="dxlo_ivaretncV1">
  <input type="hidden" name="ivaretncV2"       id="dxlo_ivaretncV2">

  <!-- ===== FECHAS (operator, v1, v2) ===== -->
  <input type="hidden" name="fechapagoOperator" id="dxlo_fechapagoOperator">
  <input type="hidden" name="fechapagoV1"       id="dxlo_fechapagoV1">
  <input type="hidden" name="fechapagoV2"       id="dxlo_fechapagoV2">

  <input type="hidden" name="ultmovOperator"    id="dxlo_ultmovOperator">
  <input type="hidden" name="ultmovV1"          id="dxlo_ultmovV1">
  <input type="hidden" name="ultmovV2"          id="dxlo_ultmovV2">
</form>


   
<%} %>   

    <form id="frmMostrarDocumento" name="frmMostrarDocumento" class="easyui-form"  method="post" 
    	action="/siarex247/jsp/visor/mostrarDocumento.jsp" target="_blank">
      	<input type="hidden" name="tipoDocumento" id="tipoDocumento_MostrarDocumento" value="0">
      	<input type="hidden" name="folioOrden" id="folioOrden_MostrarDocumento" value="0">
   </form>


	
	<form action="/siarex247/excel/exportFacturasDetails.action" name="frmExportarDetalleExcel" id="frmExportarDetalleExcel" target="frmOcultoSiarex" method="post">
	   <input type="hidden" name="tipoMoneda" value="" id="tipoMoneda_Exportar">
	   <input type="hidden" name="estatusOrden" value="" id="estatusOrden_Exportar">
	   <input type="hidden" name="rfc" value="" id="rfc_Exportar">
	   <input type="hidden" name="razonSocial" value="" id="razonSocial_Exportar">
	   <input type="hidden" name="uuid" value="" id="uuid_Exportar">
	   <input type="hidden" name="folioEmpresa" value="" id="folioEmpresa_Exportar">
	   <input type="hidden" name="serieFolio" value="" id="serieFolio_Exportar">
	   <input type="hidden" name="fechaInicial" value="" id="fechaInicial_Exportar">
	   <input type="hidden" name="fechaFinal" value="" id="fechaFinal_Exportar">
	   
	</form>
	

	<iframe name="frmOcultoSiarex" id="frmOcultoSiarex" style="width: 0px; height: 0px; visibility: hidden;" marginheight="0" marginwidth="0" frameborder="0">
	</iframe>

<div class="modal fade" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true"></div>

<div class="modal fade bd-example-modal-lg" id="myModalCargarFactura" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalValidarCFDI" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalCargarComplemento" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalCargarNota" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalValidarNota" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalEliminarComplemento" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalEliminarNotaCredito" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalCargarFacturaAmericanos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalValidarFacturaAmericanos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalFechaRecepcion" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalCargarCartaPorte" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalValidarCP" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalCargarLogo" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>


<!-- 
<div class="modal fade" id="staticBackdrop" data-bs-keyboard="false" data-bs-backdrop="static" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
 -->
<script type="text/javascript">

  
	var MODAL_TITLE_NUEVO = null;
	var MODAL_TITLE_EDITAR = null;
	var MODAL_TITLE_VER = null;

	
	var QUESTION_ELIMINAR_ORDEN = null;
	var VISOR_LABEL_ORDEN_COMPRA = null;
	var VISOR_MSG1_ELIMINAR = null;
	
	var VISOR_TITLE16 = null; // ¿ Orden Multiple ?
	var VISOR_TITLE17 = null; // ¡Rango de fechas no valido!
	var VISOR_TITLE18 = null; // ¿Estas seguro de eliminar el complemento de pago con el folio UUID  ?
			
	var VISOR_MSG2 = null; // La orden de compra se ha eliminado satisfactoriamente.
	var VISOR_MSG3 = null;
	var VISOR_MSG4 = null; // La orden seleccionado no contiene un archivo .PDF
	var VISOR_MSG5 = null; // Es necesario seleccionar al menos un registro.
	var VISOR_MSG6 = null; // Es necesario seleccionar solo un registro.
	var VISOR_MSG7 = null; // Su solicitud de liberación de pago fue enviado satisfactoriamente.
	var VISOR_MSG8 = null; // Para realizar la solicitud de liberación de pago es necesario que la orden de compra no tenga Servicio Recibido
	var VISOR_MSG9 = null; // La factura se ha generado satisfactoriamente. 
	var VISOR_MSG10 = null; // Estatus de la Orden es incorrecta para eliminar
	var VISOR_MSG13 = null; // La orden de compra << FOLIO_EMPRESA >> no tiene asignado complemento de pago
	var VISOR_MSG14 = null; // El complemento de pago se ha eliminado satisfactoriamente.
	var VISOR_MSG15 = null; // El complemento de pago se ha asignado satisfactoriamente.
	var VISOR_MSG16 = null; // ¿Estas seguro de eliminar las Ordenes de Compra seleccionadas ?
	var VISOR_MSG17 = null; // Es necesario seleccionar un registro para eliminar carta porte.
	var VISOR_MSG18 = null; //  Debe seleccionar solo un registro para eliminar carta porte.
	var VISOR_MSG19 = null; //  ¿Estas seguro de eliminar la carta porte ?
	var VISOR_MSG20 = null; //  La Carta Porte se ha eliminado satisfactoriamente.
	var VISOR_MSG21 = null; //  Estatus de la Orden es incorrecta para validar
	var VISOR_MSG22 = null; //  Debe seleccionar un motivo de rechazo
	var VISOR_MSG23 = null; //  La orden de compra << FOLIO_EMPRESA >> no tiene asignado una nota de credito.
	var VISOR_MSG24 = null; //  ¿Estas seguro de eliminar la Nota de Crédito con el folio UUID  ?
	var VISOR_MSG25 = null; //   La Nota de Crédito se ha eliminado satisfactoriamente.
	var VISOR_MSG26 = null; // La nota de credito se ha asignado satisfactoriamente.  
	
	$(document).ready(function() {
		 $("#myModalDetalle").load('/siarex247/jsp/visor/modalVisor.jsp');
		 $("#myModalCargarFactura").load('/siarex247/jsp/visor/facturas/cargarFactura.jsp');
		 $("#myModalValidarCFDI").load('/siarex247/jsp/visor/validar/validarClavesCFDI.jsp');
		 $("#myModalCargarComplemento").load('/siarex247/jsp/visor/complementos/cargarComplemento.jsp');
		 $("#myModalCargarNota").load('/siarex247/jsp/visor/notaCredito/cargarNota.jsp');
		 $("#myModalValidarNota").load('/siarex247/jsp/visor/notaCredito/validarNotas.jsp');
		 $("#myModalEliminarComplemento").load('/siarex247/jsp/visor/complementos/eliminarComplemento.jsp');
		 $("#myModalEliminarNotaCredito").load('/siarex247/jsp/visor/notaCredito/eliminarNotaCredito.jsp');
		 $("#myModalCargarFacturaAmericanos").load('/siarex247/jsp/visor/facturas/cargarFacturaAmericanos.jsp');
		 $("#myModalValidarFacturaAmericanos").load('/siarex247/jsp/visor/facturas/validarAmericana.jsp');
		 $("#myModalFechaRecepcion").load('/siarex247/jsp/visor/fechaPago/fechaRecepcion.jsp');
		 $("#myModalCargarCartaPorte").load('/siarex247/jsp/visor/facturas/cargarCartaPorte.jsp');
		 $("#myModalValidarCP").load('/siarex247/jsp/visor/facturas/validarClavesCP.jsp');
		 $("#myModalCargarLogo").load('/siarex247/jsp/visor/cargarLogo.jsp');
		 

		 $('#estatusOrden_Visor').select2({
				theme: 'bootstrap-5'
			});
		 
		obtenerFechasFiltro();
		calcularEtiquetasDetalleVisor();
	});
	
	
	
	function obtenerFechasMinima(fechaInicial, fechaFinal){
		$.ajax({
			url  : '/siarex247/visor/tablero/consultarFechaMinima.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					const calendarioIni = flatpickr("#fechaInicial", { 
						dateFormat: "Y-m-d",
						minDate: data.fechaInicial,
				   	   	maxDate: data.fechaFinal,
				   	    locale: {
				   	        firstDayOfWeek: 1,
				   	        weekdays: {
				   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
				   	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'],         
				   	        }, 
				   	        months: {
				   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
				   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
				   	        },
				   	     },
					});
					
					const calendarioFin = flatpickr("#fechaFinal", { 
						dateFormat: "Y-m-d",
						minDate: data.fechaInicial,
				   	   	maxDate: data.fechaFinal,
				   	    locale: {
				   	        firstDayOfWeek: 1,
				   	        weekdays: {
				   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
				   	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'],         
				   	        }, 
				   	        months: {
				   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
				   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
				   	        },
				   	     },
					});
					
					calendarioIni.setDate(fechaInicial);
					calendarioFin.setDate(fechaFinal);  
					
					 $('#fechaInicial').val(fechaInicial);
					 $('#fechaFinal').val(fechaFinal);
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasFiltro()_'+thrownError);
			}
		});	
				
	}
	
	
	
	
	function calcularEtiquetasDetalleVisor(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'VISOR'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("VISOR_TITLE1").innerHTML = data.TITLE1;
					document.getElementById("VISOR_MENU0").innerHTML = data.MENU0;
					
					
					QUESTION_ELIMINAR_ORDEN = data.QUESTION1;
					VISOR_LABEL_ORDEN_COMPRA =  data.ETQ29;
					VISOR_MSG1_ELIMINAR = data.MSG1;
					
					VISOR_TITLE16 = data.TITLE16;
					VISOR_TITLE17 = data.TITLE17;
					VISOR_TITLE18 = data.TITLE18;
					
					VISOR_MSG2 = data.MSG2;
					VISOR_MSG3 = data.MSG3;
					VISOR_MSG4 = data.MSG4;
					VISOR_MSG5 = data.MSG5;
					VISOR_MSG6 = data.MSG6;
					VISOR_MSG7 = data.MSG7;
					VISOR_MSG8 = data.MSG8;
					VISOR_MSG9 = data.MSG9;
					VISOR_MSG10 = data.MSG10;
					VISOR_MSG13 = data.MSG13;
					VISOR_MSG14 = data.MSG14;
					VISOR_MSG15 = data.MSG15;
					VISOR_MSG16 = data.MSG16;
					VISOR_MSG17 = data.MSG17;
					VISOR_MSG18 = data.MSG18;
					VISOR_MSG19 = data.MSG19;
					VISOR_MSG20 = data.MSG20;
					VISOR_MSG21 = data.MSG21;
					VISOR_MSG22 = data.MSG22;
					VISOR_MSG23 = data.MSG23;
					VISOR_MSG24 = data.MSG24;
					VISOR_MSG25 = data.MSG25;
					VISOR_MSG26 = data.MSG26;
					
					
					try{
						document.getElementById("VISOR_MENU1").innerHTML = data.MENU1;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_Factura").innerHTML = data.OPCION1;	
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_Factura").innerHTML = data.OPCION1;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_FacturaAmericana").innerHTML = data.OPCION2;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_GenerarFactura").innerHTML = data.OPCION3;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_AdjuntarLogo").innerHTML = data.OPCION4;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_Complemento").innerHTML = data.OPCION5;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_NotaCredito").innerHTML = data.OPCION6;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuCargar_CartaPorte").innerHTML = data.OPCION7;
					}catch(e){
						e=null;
					}
					
					try{
						document.getElementById("VISOR_MENU2").innerHTML = data.MENU2;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION8").innerHTML = data.OPCION8;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION9").innerHTML = data.OPCION9;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION10").innerHTML = data.OPCION10;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION11").innerHTML = data.OPCION11;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_MENU3").innerHTML = data.MENU3;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION12").innerHTML = data.OPCION12;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION13").innerHTML = data.OPCION13;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("VISOR_OPCION14").innerHTML = data.OPCION14;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuMovimientos_FechaRecepcion").innerHTML = data.OPCION15;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuMovimientos_EliminarOrdenes").innerHTML = data.OPCION16;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuMovimientos_EliminarComplemento").innerHTML = data.OPCION17;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuMovimientos_EliminarNota").innerHTML = data.OPCION18;
					}catch(e){
						e=null;
					}
					try{
						document.getElementById("menuMovimientos_EliminarCartaPorte").innerHTML = data.OPCION19;
					}catch(e){
						e=null;
					}
					
					document.getElementById("VISOR_ETQ34").innerHTML = data.ETQ34;
					document.getElementById("VISOR_ETQ35").innerHTML = data.ETQ35;
					
					
					
					<%if (usuariosForm.getIdPerfil() == 4 ){ %>
						$('#btnNuevo_Visor').hide();
						var btnNuevo =  $('.validarNuevo');
	            	 	btnNuevo.addClass("ocultarBoton");
					<%}else {%>
						document.getElementById("btnNuevo_Visor").innerHTML = '<div id="btnNuevo_Visor">  <span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span> </div>';
					<%}%>
					
					document.getElementById("VISOR_COL1").innerHTML = data.COL1;
					document.getElementById("VISOR_COL2").innerHTML = data.COL2;
					document.getElementById("VISOR_COL3").innerHTML = data.COL3;
					document.getElementById("VISOR_COL4").innerHTML = data.COL4;
					document.getElementById("VISOR_COL5").innerHTML = data.COL5;
					document.getElementById("VISOR_COL6").innerHTML = data.COL6;
					document.getElementById("VISOR_COL7").innerHTML = data.COL7;
					document.getElementById("VISOR_COL8").innerHTML = data.COL8;
					document.getElementById("VISOR_COL9").innerHTML = data.COL9;
					document.getElementById("VISOR_COL10").innerHTML = data.COL10;
					document.getElementById("VISOR_COL11").innerHTML = data.COL11;
					document.getElementById("VISOR_COL12").innerHTML = data.COL12;
					document.getElementById("VISOR_COL13").innerHTML = data.COL13;
					document.getElementById("VISOR_COL14").innerHTML = data.COL14;
					document.getElementById("VISOR_COL15").innerHTML = data.COL15;
					document.getElementById("VISOR_COL16").innerHTML = data.COL16;
					document.getElementById("VISOR_COL17").innerHTML = data.COL17;
					document.getElementById("VISOR_COL18").innerHTML = data.COL18;
					document.getElementById("VISOR_COL19").innerHTML = data.COL19;
					//document.getElementById("VISOR_COL20").innerHTML = data.COL20;
					//document.getElementById("VISOR_COL21").innerHTML = data.COL21;
					document.getElementById("VISOR_COL22").innerHTML = data.COL22;
					document.getElementById("VISOR_COL23").innerHTML = data.COL23;
					document.getElementById("VISOR_COL24").innerHTML = data.COL24;
					document.getElementById("VISOR_COL25").innerHTML = data.COL25;
					document.getElementById("VISOR_COL26").innerHTML = data.COL26;
					document.getElementById("VISOR_COL27").innerHTML = data.COL27;
					document.getElementById("VISOR_COL28").innerHTML = data.COL28;
					document.getElementById("VISOR_COL29").innerHTML = data.COL29;
					document.getElementById("VISOR_COL30").innerHTML = data.COL30;
					document.getElementById("VISOR_COL31").innerHTML = data.COL31;
					document.getElementById("VISOR_COL32").innerHTML = data.COL32;
					document.getElementById("VISOR_COL33").innerHTML = data.COL33;
					
					//document.getElementById("VISOR_ETQ100").innerHTML = data.ETQ100;
					document.getElementById("RFC_FILTRO_BUSQUEDA").innerHTML = data.ETQ18;
					document.getElementById("RAZON_SOCIAL_FILTRO_BUSQUEDA").innerHTML = data.COL2;
					document.getElementById("ORDEN_COMPRA_FILTRO_BUSQUEDA").innerHTML = data.COL3;
					document.getElementById("ESTATUS_FILTRO_BUSQUEDA").innerHTML = data.COL8;
					document.getElementById("SERIE_FILTRO_BUSQUEDA").innerHTML = data.COL9;
					document.getElementById("VISOR_BTN_REFRESCAR").innerHTML = BTN_REFRESCAR;
					document.getElementById("BOVEDA_BTN_LIMPIAR").innerHTML = BTN_LIMPIAR;
					document.getElementById("VISOR_ETQ6").innerHTML = 'A1 - ' + data.ETQ6;
					document.getElementById("VISOR_ETQ7").innerHTML = 'A2 - ' + data.ETQ7;
					document.getElementById("VISOR_ETQ8").innerHTML = 'A3 - ' + data.ETQ8;
					document.getElementById("VISOR_ETQ9").innerHTML = 'A4 - ' + data.ETQ9;
					document.getElementById("VISOR_ETQ10").innerHTML = 'A5 - ' + data.ETQ10;
					document.getElementById("VISOR_ETQ105").innerHTML = 'A6 - ' + data.ETQ105;
					document.getElementById("VISOR_ETQ101").innerHTML = 'A9 - ' + data.ETQ101;
					document.getElementById("VISOR_ETQ102").innerHTML = 'A10 - ' + data.ETQ102;
					document.getElementById("VISOR_ETQ103").innerHTML = 'A11 - ' + data.ETQ103;
					
					
					MODAL_TITLE_NUEVO = data.TITLE2;
					MODAL_TITLE_EDITAR = data.TITLE3;
					MODAL_TITLE_VER = data.TITLE4;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
</script>



<script type="text/javascript">

/* =======================
 * DETALLE VISOR – Filtros DX-like
 * ======================= */
(function(){
  'use strict';
  const NS='VISOR_DX', TABLE='#tablaDetalleVisor';
  
    // ===== Helpers base =====
  const __isFromClone = (el)=>!!$(el).closest('.dtfh-floatingparent').length;
  const __hiddenForMenu = ($menu)=>('#'+( ($menu.attr('id')||'').replace('OpMenu','Operator') ));

  const __toggleSecondByFilter = ($f, show)=>{
    const $inps=$f.find('input[type=text],input[type=number],input[type=date]');
    const $second=$inps.eq(1);
    if (!$second.length) return;
    if (show){ $second.removeClass('d-none').show(); }
    else { $second.val('').addClass('d-none'); }
  };

  const __inputsHaveValue = ($f)=>{
    const $inps=$f.find('input[type=text],input[type=number],input[type=date]');
    const $i1=$inps.eq(0), $i2=$inps.eq(1);
    const op=($f.find('input[id$="OperatorV"]').val()||'').toLowerCase();
    const v1=($.trim($i1.val()||''));
    if (op==='bt'){
      const v2=($.trim($i2.val()||''));
      return v1!=='' && v2!==''; }
    return v1!==''; };

  // ===== Helpers original + clon (FixedHeader) =====
  function __bothMenusById(mid){
    const sel = [
      TABLE + ' thead .dx-like-menu#'+mid,
      '.dtfh-floatingparent thead .dx-like-menu#'+mid
    ].join(', ');
    return $(sel);
  }
  function __bothFiltersByMenuId(mid){
    const $menus = __bothMenusById(mid);
    const $filters = $menus.map(function(){ return $(this).closest('.dx-like-filter').get(0); });
    return $($filters);
  }
  function __setLabelBoth(mid, labelText){
    __bothFiltersByMenuId(mid).each(function(){ $(this).find('.op-label').text(labelText); });
  }
  function __setOperatorOriginal(mid, op){
    const hidSel = '#'+mid.replace('OpMenu','Operator');
    const sel = TABLE + ' thead ' + hidSel; // SOLO original
    $(sel).val(op);
  }
  function __toggleSecondBoth(mid, show){
    __bothFiltersByMenuId(mid).each(function(){ __toggleSecondByFilter($(this), show); });
  }
  function __clearInputsBoth(mid){
    __bothFiltersByMenuId(mid).each(function(){
      const $inps=$(this).find('input[type=text],input[type=number],input[type=date]');
      $inps.eq(0).val(''); $inps.eq(1).val('');
    });
  }
  function __closeMenusBoth(mid){ __bothMenusById(mid).removeClass('show'); }
  function __originalFilterByMenuId(mid){
    return $(TABLE + ' thead .dx-like-menu#'+mid).closest('.dx-like-filter');
  }

  // ===== Recarga coalescida =====
  let __busy=false, __queue=false, __deb=null;
  window.refrescarVisor_DX = function(reason){
    if (__deb) clearTimeout(__deb);
    __deb = setTimeout(function(){
      const dt = $.fn.dataTable.isDataTable(TABLE) ? $(TABLE).DataTable() : null;
      if (!dt) return;
      if (__busy){ __queue = true; return; }
      __busy = true;
      try{
        // UNA sola llamada: resetPaging = true mueve a la primera página sin draw extra
        dt.ajax.reload(function(){
          __busy = false;
          if (__queue){ __queue = false; window.refrescarVisor_DX('coalesced'); }
        }, true);
      }catch(e){
        __busy = false;
        console.error('[VISOR_DX] reload', e);
      }
    }, 120); // debounce corto para agrupar eventos simultáneos
  };


  // ===== Abrir/cerrar menús solo con .op-btn =====
  const FILTER_HEAD = TABLE+' thead .dx-like-filter, .dtfh-floatingparent thead .dx-like-filter';
  const FILTER_MENU = FILTER_HEAD+' .dx-like-menu';
  const FILTER_INP  = FILTER_HEAD+' input, '+FILTER_HEAD+' textarea, '+FILTER_HEAD+' select';
  const MENU_BUTTON = FILTER_HEAD+' .op-btn';

  // Bloquea apertura de menú desde el contenedor (salvo .op-btn o dentro del menú)
  document.addEventListener('click', function(ev){
    const t=ev.target, inF=!!t.closest('.dx-like-filter'), isBtn=!!t.closest('.op-btn'), inMenu=!!t.closest('.dx-like-menu');
    if (inF && !isBtn && !inMenu){ ev.stopPropagation(); }
  }, true);
  // Cierra menús al enfocar input/textarea/select
  document.addEventListener('focus', function(ev){
    const t=ev.target, inMenu=t && t.closest && t.closest('.dx-like-menu'), inF=t && t.closest && t.closest('.dx-like-filter');
    if (inF && !inMenu && !t.closest('.op-btn')){ __cerrarMenus(); ev.stopPropagation(); }
  }, true);

  $(document).on('click.'+NS, MENU_BUTTON, function(e){
    e.preventDefault(); e.stopPropagation();
    const $f=$(this).closest('.dx-like-filter'), $m=$f.find('.dx-like-menu');
    $(FILTER_MENU).not($m).removeClass('show');
    $m.toggleClass('show');
  });
  function __cerrarMenus(){ $(FILTER_MENU).removeClass('show'); }
  $(document).on('click.'+NS+'-close', __cerrarMenus);
  $(window).on('scroll.'+NS+' resize.'+NS, __cerrarMenus);
  $(TABLE).on('draw.dt.'+NS, __cerrarMenus);

  // ===== Seleccionar operador en menú (corrige label, set hidden) =====
  const MENU_ITEMS = TABLE+' thead .dx-like-menu li, .dtfh-floatingparent thead .dx-like-menu li';
  $(document).off('click.'+NS, MENU_ITEMS).on('click.'+NS, MENU_ITEMS, function(e){
    e.preventDefault(); e.stopPropagation();

    const $li=$(this);
    const rawOp = String($li.data('op')||'').toLowerCase();
    let   op    = (rawOp==='between') ? 'bt' : rawOp;

    const $menu=$li.closest('.dx-like-menu');
    const mid  =$menu.attr('id')||'';
    if (!mid) return;

    // token para el botón
    const firstToken=$.trim($li.text()).split(/\s+/)[0];
    let   label = firstToken;

    // === RESET → limpiar y llamar a limpiarVisor() ===
    if (rawOp === 'reset') {
      // visual básico por si tu limpiarVisor() solo resetea campos globales
      __clearInputsBoth(mid);
      __setLabelBoth(mid, '🔎');
      __setOperatorOriginal(mid, 'contains');
      __toggleSecondBoth(mid, false);
      __closeMenusBoth(mid);

      if (typeof window.limpiarVisor === 'function') {
        window.limpiarVisor();               // tu función debe limpiar y recargar
      } else {
        // Fallback mínimo: recarga con filtros vacíos
        window.refrescarVisor_DX('menu-reset');
      }
      return;
    }

    // “contains” → siempre mostrar lupa
    if (op==='contains') label='🔎';

    // 1) Cambia label en AMBAS cabeceras
    __setLabelBoth(mid, label);

    // 2) Escribe operador SOLO en thead original
    __setOperatorOriginal(mid, op);

    // 3) Mostrar/ocultar 2º input (Entre)
    __toggleSecondBoth(mid, op==='bt');

    // 4) Cierra menús en ambas cabeceras
    __closeMenusBoth(mid);

    // 5) Si hay valores suficientes, recarga; si no, guía foco
    const $filterOrig = __originalFilterByMenuId(mid);
    if (!$filterOrig.length) return;

    if (op==='bt'){
      const $inps=$filterOrig.find('input[type=text],input[type=number],input[type=date]');
      const $i1=$inps.eq(0), $i2=$inps.eq(1);
      const v1=($.trim($i1.val()||'')), v2=($.trim($i2.val()||''));
      const secondHidden = !$i2.is(':visible') || $i2.hasClass('d-none');
      if (secondHidden){ __toggleSecondByFilter($filterOrig,true); $i2.focus(); return; }
      if (!v2){ $i2.focus(); return; }
      if (v1 && v2){ window.refrescarVisor_DX('menu-op-between'); return; }
      if (!v1){ $i1.focus(); }
      return;
    }

    if (__inputsHaveValue($filterOrig)) window.refrescarVisor_DX('menu-op');
  });

  // ===== Selects AUTO (“=” cuando hay valor; lupa cuando ALL/vacío) =====
  function __wireSelectEq(sel, op, btn, reason){
	  // const events = 'input.' + NS + ' change.' + NS;
	  const events = 'change.' + NS;

	  // Quita cualquier handler previo para este selector con el mismo namespace
	    $(document).off(events, sel).on(events, sel, function(){
		    const v = $.trim($(this).val() || '');
		    const isEq = (v && v !== 'ALL');
		    $(op).val(isEq ? 'equals' : 'contains');
		    $(btn + ' .op-label').html(isEq ? '=' : '<i class="fas fa-search"></i>');
		    // window.refrescarVisor_DX(reason);
		    validarFechasoOrdenes();
		  });
	}
  __wireSelectEq('#monedaFilterV','#monedaOperatorV','#monedaOpBtnV','select-auto-moneda');
  __wireSelectEq('#reciboFilterV','#reciboOperatorV','#reciboOpBtnV','select-auto-recibo');
  __wireSelectEq('#estatusPagoFilterV','#estatusPagoOperatorV','#estatusPagoOpBtnV','select-auto-epago');
  __wireSelectEq('#estatusSatFilterV','#estatusSatOperatorV','#estatusSatOpBtnV','select-auto-sat');
//  __wireSelectEq('#usoCfdiFilterV','#usoCfdiOperatorV','#usoCfdiOpBtnV','select-auto-uso');
  __wireSelectEq('#estadoCfdiFilterV','#estadoCfdiOperatorV','#estadoCfdiOpBtnV','select-auto-estadoCfdi');

  // ===== Enter en inputs (texto/num/fecha) =====
  const INPUTS = TABLE+' thead input';
  $(document)
  .off('keydown.' + NS, INPUTS)           // 🔑 quita cualquier handler previo con ese namespace+selector
  .on('keydown.' + NS, INPUTS, function (e) {
    if (e.key !== 'Enter') return;
    e.preventDefault();

    const $f = $(this).closest('.dx-like-filter');
    const $inps = $f.find('input[type=text],input[type=number],input[type=date]');
    const $i1 = $inps.eq(0), $i2 = $inps.eq(1);
    const op = ($f.find('input[id$="OperatorV"]').val() || '').toLowerCase();
    const v1 = $.trim($i1.val() || ''), v2 = $.trim($i2.val() || '');

    if (op === 'bt') {
      const hiddenSecond = !$i2.is(':visible') || $i2.hasClass('d-none');
      if (hiddenSecond) { __toggleSecondByFilter($f, true); $i2.focus(); return; }
      if (!v2) { $i2.focus(); return; }
      if (v1 && v2) { window.refrescarVisor_DX('enter-between'); return; }
      if (!v1) { $i1.focus(); }
      return;
    }

    if (v1.length >= 1) {
      validarFechasoOrdenes();
      // window.refrescarVisor_DX('enter');
    }
  });
  

})();

</script>
<script>
/* ====== DetalleVisor: lógica de filtros (DX-like + FixedHeader) ====== */

/* Debounce para evitar ráfagas */
const __dv_debounce = (fn, wait=250) => { let t; return function(...a){ clearTimeout(t); t=setTimeout(()=>fn.apply(this,a), wait); }; };

/* Evitar dobles llamadas */
let __DV_REFRESH_IN_FLIGHT = false;
function __safeRefreshDV(reason){
  if (typeof refrescarDetalleVisor !== 'function') return;
  if (__DV_REFRESH_IN_FLIGHT) return;
  __DV_REFRESH_IN_FLIGHT = true;
  try { refrescarDetalleVisor(reason); }
  finally { setTimeout(()=>{ __DV_REFRESH_IN_FLIGHT = false; }, 0); }
}

/* ===== Helpers FixedHeader (opera en thead original + clon) ===== */
function __bothBySel(sel){
  return $([
    '#tablaDetalleVisor thead ' + sel,
    '.dtfh-floatingparent thead ' + sel
  ].join(', '));
}
function __toggleMenu(menuSel, show){
  __bothBySel(menuSel).toggleClass('show', !!show);
}
function __bothFiltersBySel(menuSel){
  return __bothBySel(menuSel).map(function(){ return $(this).closest('.dx-like-filter').get(0); });
}
function __setLabelBothBySel(menuSel, html){
  __bothFiltersBySel(menuSel).each(function(){ $(this).find('.op-label').html(html); });
}
function __setOperatorOriginalBySel(menuSel, op){
  const id  = (menuSel||'').replace(/^#/, '');      // ej. rsOpMenuV
  const hid = '#'+id.replace('OpMenu','Operator');  // ej. rsOperatorV
  $('#tablaDetalleVisor thead '+hid).val(op);
}
function __toggleSecondBothBySel(menuSel, show){
  __bothFiltersBySel(menuSel).each(function(){
    const $inps=$(this).find('input[type=text],input[type=number],input[type=date]');
    const $second=$inps.eq(1);
    if ($second.length){
      if (show){ $second.removeClass('d-none').show(); }
      else { $second.val('').addClass('d-none'); }
    }
  });
}
function __clearInputsBothBySel(menuSel){
  __bothFiltersBySel(menuSel).each(function(){
    const $inps=$(this).find('input[type=text],input[type=number],input[type=date]');
    $inps.eq(0).val('');
    if ($inps.eq(1).length) $inps.eq(1).val('');
  });
}

/* Cambiar operador (texto/num/fecha) — ACTUALIZA original + clon, operator solo en original */
function __bindOpMenu(menuSel, opHiddenSel, btnSel, extra){
  // Clic en opciones de menú
  $(document).off('click', menuSel + ' li').on('click', menuSel + ' li', function(e){
    e.preventDefault(); e.stopPropagation();

    let op = ($(this).data('op')||'').toString().toLowerCase();
    if (!op) return;
    if (op==='between') op='bt';

    // Etiqueta del botón (emoji/símbolo)
    let labelHtml = '<i class="fas fa-search"></i>'; // default
    switch(op){
      case 'contains':      labelHtml = '⊚'; break;
      case 'notcontains':   labelHtml = '⊘'; break;
      case 'startswith':    labelHtml = '↤';  break;
      case 'endswith':      labelHtml = '↦';  break;
      case 'equals':
      case 'eq':            labelHtml = '=';  break;
      case 'notequals':
      case 'ne':            labelHtml = '≠';  break;
      case 'lt':            labelHtml = '&lt;'; break;
      case 'gt':            labelHtml = '&gt;'; break;
      case 'le':            labelHtml = '≤';  break;
      case 'ge':            labelHtml = '≥';  break;
      case 'bt':            labelHtml = '↔';  break;
      case 'reset':
        // reset => volver a contains + limpiar inputs (ambas cabeceras)
        op = 'contains';
        labelHtml = '⊚';
        __clearInputsBothBySel(menuSel);
        break;
    }

    // 1) Cambia label en AMBAS cabeceras
    __setLabelBothBySel(menuSel, labelHtml);

    // 2) Escribe operador SOLO en thead original
    __setOperatorOriginalBySel(menuSel, op);

    // 3) Mostrar/ocultar segundo input si corresponde
    const showSecond = (op==='bt');
    __toggleSecondBothBySel(menuSel, showSecond);

    // 4) Respeta “extra” para inputs concretos
    if (extra && extra.toggleSecond){
      $(extra.secondSel).toggleClass('d-none', !showSecond);
      if (!showSecond) $(extra.secondSel).val('');
    }
    if (op==='contains' && extra){
      if (extra.firstSel)  $(extra.firstSel).val('');
      if (extra.secondSel) $(extra.secondSel).val('');
    }

    // 5) Copia a globales + recarga con debounce
    __copyAllToGlobalsV();
    __onFilterChangedDV('op-change');

    // 6) Cierra menús (original + clon)
    __toggleMenu(menuSel, false);
  });

  // Abrir/cerrar menú al click del botón
  $(document).off('click', btnSel).on('click', btnSel, function(e){
    e.preventDefault(); e.stopPropagation();
    const isOpen = __bothBySel(menuSel).first().hasClass('show');
    __toggleMenu(menuSel, !isOpen);
  });
}

/* Operador "=" cuando el select tiene valor != ALL; lupa si ALL */
function __toggleOperadorSelectV($select, $hiddenOp, $btn){
  const v = ($select.val() || '').trim();
  const isEq = (v && v!=='ALL');
  $hiddenOp.val(isEq ? 'equals' : 'contains');
  $btn.find('.op-label').html(isEq ? '=' : '<i class="fas fa-search"></i>');
}

/* Copia TODOS los filtros a los hidden/globales usados en AJAX (si los tienes) */
function __copyAllToGlobalsV(){
  // TEXT (value + operador)
  function setText(id, opId, gName){
    const v = ($(id).val()||'').trim(); $(gName).val(v);
    $(gName+'_op').val($(opId).val()||'contains');
  }
  // NUMBER/DATE con 1-2 inputs + operador
  function setRange(f1, f2, opId, gBase){
    const v1 = ($(f1).val()||'').trim(), v2 = ($(f2).val()||'').trim();
    $(gBase+'_v1').val(v1); $(gBase+'_v2').val(v2);
    $(gBase+'_op').val($(opId).val()||'eq');
  }
  // SELECT (normaliza ALL->'')
  function setSelect(sel, opId, gName){
    const v = ($(sel).val()||'').trim(); $(gName).val(v==='ALL' ? '' : v);
    $(gName+'_op').val($(opId).val()||'contains');
  }

  // 2 Razón social
  setText('#rsFilterV',   '#rsOperatorV',   '#rs_DV');
  // 3 OC
  setText('#ocFilterV',   '#ocOperatorV',   '#oc_DV');
  // 4 Desc
  setText('#descFilterV', '#descOperatorV', '#desc_DV');

  // 5 Moneda
  setSelect('#monedaFilterV','#monedaOperatorV','#moneda_DV');

  // 6 Monto
  setRange('#montoFilter1V','#montoFilter2V','#montoOperatorV','#monto_DV');

  // 7 Recibo (S/N)
  setSelect('#reciboFilterV','#reciboOperatorV','#recibo_DV');

  // 8 Estatus Pago
  setSelect('#estatusPagoFilterV','#estatusPagoOperatorV','#estatusPago_DV');

  // 9 Serie/Folio
  setText('#sfFilterV', '#sfOperatorV', '#sf_DV');

  // 10-15 num
  setRange('#totalFilter1V','#totalFilter2V','#totalOperatorV','#total_DV');
  setRange('#subtotalFilter1V','#subtotalFilter2V','#subtotalOperatorV','#subtotal_DV');
  setRange('#ivaFilter1V','#ivaFilter2V','#ivaOperatorV','#iva_DV');
  setRange('#ivaretFilter1V','#ivaretFilter2V','#ivaretOperatorV','#ivaret_DV');
  setRange('#isrretFilter1V','#isrretFilter2V','#isrretOperatorV','#isrret_DV');
  setRange('#implocFilter1V','#implocFilter2V','#implocOperatorV','#imploc_DV');

  // 24-26 num
  setRange('#totalncFilter1V','#totalncFilter2V','#totalncOperatorV','#totalnc_DV');
  setRange('#pagotFilter1V','#pagotFilter2V','#pagotOperatorV','#pagot_DV');
  setRange('#ivaretncFilter1V','#ivaretncFilter2V','#ivaretncOperatorV','#ivaretnc_DV');

  // 27 Fecha pago (date)
  setRange('#fechapagoFilter1V','#fechapagoFilter2V','#fechapagoOperatorV','#fechapago_DV');

  // 28 Asignar A (texto)
  setText('#asignarFilterV','#asignarOperatorV','#asignar_DV');

  // 29 Último movimiento (date)
  setRange('#ultmovFilter1V','#ultmovFilter2V','#ultmovOperatorV','#ultmov_DV');

  // 30 Estado CFDI (texto)
  setText('#estadoCfdiFilterV','#estadoCfdiOperatorV','#estadoCfdi_DV');

  // 31 Estatus SAT (S/N)
  setSelect('#estatusSatFilterV','#estatusSatOperatorV','#estatusSat_DV');

  // 32 UsoCFDI
  setSelect('#usoCfdiFilterV','#usoCfdiOperatorV','#usoCfdi_DV');

  // 33 CPS (texto)
  setText('#cpsFilterV','#cpsOperatorV','#cps_DV');
}

/* Disparador con debounce cuando cambian filtros */
const __onFilterChangedDV = __dv_debounce(function(reason){
  __copyAllToGlobalsV();
  __safeRefreshDV(reason);
}, 250);

/* ===== Bind de menús de operadores (texto) ===== */
__bindOpMenu('#rsOpMenuV',        '#rsOperatorV',        '#rsOpBtnV');
__bindOpMenu('#ocOpMenuV',        '#ocOperatorV',        '#ocOpBtnV');
__bindOpMenu('#descOpMenuV',      '#descOperatorV',      '#descOpBtnV');
__bindOpMenu('#sfOpMenuV',        '#sfOperatorV',        '#sfOpBtnV');
__bindOpMenu('#asignarOpMenuV',   '#asignarOperatorV',   '#asignarOpBtnV');
__bindOpMenu('#estadoCfdiOpMenuV','#estadoCfdiOperatorV','#estadoCfdiOpBtnV');
__bindOpMenu('#cpsOpMenuV',       '#cpsOperatorV',       '#cpsOpBtnV');

/* ===== Bind de menús (num) con 2º input cuando 'between' ===== */
__bindOpMenu('#montoOpMenuV',     '#montoOperatorV',     '#montoOpBtnV',     {toggleSecond:true, firstSel:'#montoFilter1V',     secondSel:'#montoFilter2V',     defaultOp:'eq'});
__bindOpMenu('#totalOpMenuV',     '#totalOperatorV',     '#totalOpBtnV',     {toggleSecond:true, firstSel:'#totalFilter1V',     secondSel:'#totalFilter2V',     defaultOp:'eq'});
__bindOpMenu('#subtotalOpMenuV',  '#subtotalOperatorV',  '#subtotalOpBtnV',  {toggleSecond:true, firstSel:'#subtotalFilter1V',  secondSel:'#subtotalFilter2V',  defaultOp:'eq'});
__bindOpMenu('#ivaOpMenuV',       '#ivaOperatorV',       '#ivaOpBtnV',       {toggleSecond:true, firstSel:'#ivaFilter1V',       secondSel:'#ivaFilter2V',       defaultOp:'eq'});
__bindOpMenu('#ivaretOpMenuV',    '#ivaretOperatorV',    '#ivaretOpBtnV',    {toggleSecond:true, firstSel:'#ivaretFilter1V',    secondSel:'#ivaretFilter2V',    defaultOp:'eq'});
__bindOpMenu('#isrretOpMenuV',    '#isrretOperatorV',    '#isrretOpBtnV',    {toggleSecond:true, firstSel:'#isrretFilter1V',    secondSel:'#isrretFilter2V',    defaultOp:'eq'});
__bindOpMenu('#implocOpMenuV',    '#implocOperatorV',    '#implocOpBtnV',    {toggleSecond:true, firstSel:'#implocFilter1V',    secondSel:'#implocFilter2V',    defaultOp:'eq'});
__bindOpMenu('#totalncOpMenuV',   '#totalncOperatorV',   '#totalncOpBtnV',   {toggleSecond:true, firstSel:'#totalncFilter1V',   secondSel:'#totalncFilter2V',   defaultOp:'eq'});
__bindOpMenu('#pagotOpMenuV',     '#pagotOperatorV',     '#pagotOpBtnV',     {toggleSecond:true, firstSel:'#pagotFilter1V',     secondSel:'#pagotFilter2V',     defaultOp:'eq'});
__bindOpMenu('#ivaretncOpMenuV',  '#ivaretncOperatorV',  '#ivaretncOpBtnV',  {toggleSecond:true, firstSel:'#ivaretncFilter1V',  secondSel:'#ivaretncFilter2V',  defaultOp:'eq'});

/* ===== Bind de menús (fecha) ===== */
__bindOpMenu('#fechapagoOpMenuV', '#fechapagoOperatorV', '#fechapagoOpBtnV', {toggleSecond:true, firstSel:'#fechapagoFilter1V', secondSel:'#fechapagoFilter2V', defaultOp:'eq'});
__bindOpMenu('#ultmovOpMenuV',    '#ultmovOperatorV',    '#ultmovOpBtnV',    {toggleSecond:true, firstSel:'#ultmovFilter1V',    secondSel:'#ultmovFilter2V',    defaultOp:'eq'});

/* ===== Selects con "=" cuando hay valor ===== */
 /*
function __wireSelectEq(sel, op, btn, reason){
  $(document).off('change', sel).on('change', sel, function(){
    __toggleOperadorSelectV($(sel), $(op), $(btn));
    __onFilterChangedDV(reason);
  });
}
*/
__wireSelectEq('#monedaFilterV',      '#monedaOperatorV',      '#monedaOpBtnV',      'change-moneda');
__wireSelectEq('#reciboFilterV',      '#reciboOperatorV',      '#reciboOpBtnV',      'change-recibo');
__wireSelectEq('#estatusPagoFilterV', '#estatusPagoOperatorV', '#estatusPagoOpBtnV', 'change-estatusPago');
__wireSelectEq('#estatusSatFilterV',  '#estatusSatOperatorV',  '#estatusSatOpBtnV',  'change-estatusSat');
__wireSelectEq('#usoCfdiFilterV',     '#usoCfdiOperatorV',     '#usoCfdiOpBtnV',     'change-usoCfdi');

/* ===== Inputs (texto/num/fecha) disparan con Enter o change ===== */
$(document).off('keyup change', '#rsFilterV,#ocFilterV,#descFilterV,#sfFilterV,#asignarFilterV,#estadoCfdiFilterV,#cpsFilterV')
.on('keyup change', '#rsFilterV,#ocFilterV,#descFilterV,#sfFilterV,#asignarFilterV,#estadoCfdiFilterV,#cpsFilterV', function(e){
  if (e.type==='change' || e.key==='Enter'){ __onFilterChangedDV('text-change'); }
});
$(document).off('keyup change', '#montoFilter1V,#montoFilter2V,#totalFilter1V,#totalFilter2V,#subtotalFilter1V,#subtotalFilter2V,#ivaFilter1V,#ivaFilter2V,#ivaretFilter1V,#ivaretFilter2V,#isrretFilter1V,#isrretFilter2V,#implocFilter1V,#implocFilter2V,#totalncFilter1V,#totalncFilter2V,#pagotFilter1V,#pagotFilter2V,#ivaretncFilter1V,#ivaretncFilter2V')
.on('keyup change', '#montoFilter1V,#montoFilter2V,#totalFilter1V,#totalFilter2V,#subtotalFilter1V,#subtotalFilter2V,#ivaFilter1V,#ivaFilter2V,#ivaretFilter1V,#ivaretFilter2V,#isrretFilter1V,#isrretFilter2V,#implocFilter1V,#implocFilter2V,#totalncFilter1V,#totalncFilter2V,#pagotFilter1V,#pagotFilter2V,#ivaretncFilter1V,#ivaretncFilter2V', function(e){
  if (e.type==='change' || e.key==='Enter'){ __onFilterChangedDV('number-change'); }
});
$(document).off('change keyup', '#fechapagoFilter1V,#fechapagoFilter2V,#ultmovFilter1V,#ultmovFilter2V')
.on('change keyup', '#fechapagoFilter1V,#fechapagoFilter2V,#ultmovFilter1V,#ultmovFilter2V', function(e){
  if (e.type==='change' || e.key==='Enter'){  __onFilterChangedDV('date-change'); }
});

</script>



</html>