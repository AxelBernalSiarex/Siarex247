<%@page import="java.util.HashMap"%>
<%@page import="com.siarex247.session.ObtenerSession"%>
<%@page import="com.siarex247.session.SiarexSession"%>
<%@page import="com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean"%>
<%@page import="com.siarex247.seguridad.Usuarios.UsuariosForm"%>
<%@page import="com.siarex247.seguridad.Usuarios.UsuariosAction"%>
<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

<!-- 
	<link rel="stylesheet" href="/theme-falcon/css/searchBuilder.dataTables-custom.css"  />
	<link rel="stylesheet" href="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/css/searchBuilder.bootstrap5.css"  />
	<link rel="stylesheet" href="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/css/dataTables.dateTime.min.css"  />
    
   	   <script src="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/js/dataTables.searchBuilder.min.js"></script> 	
	   <script src="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/js/searchBuilder.bootstrap.js"></script> 	
	   <script src="/theme-falcon/assets/datatable/SearchBuilder-1.3.4/js/dataTables.dateTime.min.js"></script>
 -->
 
   	<script src="/siarex247/jsp/visor/detalleVisor.js"></script>
	<script src="/siarex247/jsp/visor/exportarEliminar/exportarEliminar.js"></script>
	<script src="/siarex247/jsp/visor/facturas/cargarFacturas.js"></script>
	

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
                    <a class="dropdown-item" href="javascript:exportarFacturas();" id="VISOR_OPCION12" >Exportar Facturas</a>
                    <a class="dropdown-item" href="javascript:exportarlayOut();" id="VISOR_OPCION13" >Exportar LayOut</a>
                    <a class="dropdown-item" href="javascript:exportarPlantilla();" id="VISOR_OPCION14" >Plantillas</a>
                    
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
				<div class="card-body bg-body-tertiary">
				<!-- 
					<a class="mb-4 d-block d-flex align-items-center" href="#filtrosBusquedaVisor" data-bs-toggle="collapse" aria-expanded="false" aria-controls="filtrosBusquedaVisor">
						<span class="fas fa-plus"></span>
						<span class="ms-3" id="VISOR_ETQ100">Filtros de Busqueda</span>
					</a>
				 -->
				 	
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
						          
						          
							     <label class="col-sm-1 col-form-label" for="fechaInicial" id="VISOR_ETQ34" >Fecha Inicio</label>
						          <div class="col-sm-2">
						            <div class="form-group">
						              <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial" name="fechaInicial" type="text" placeholder="yyyy/mm/dd" data-options='{"disableMobile":true}' readonly="readonly"/>
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
						          <label class="col-sm-1 col-form-label" for="fechaFinal" id="VISOR_ETQ35">Fecha Final</label>
						           <div class="col-sm-2">
						             <div class="form-group">
						               <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal" name="fechaFinal" type="text" placeholder="yyyy/mm/dd" data-options='{"disableMobile":true}' readonly="readonly" />
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
							<!--  
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
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
							-->
						</thead>
					</table>
				   </form>	
				</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<%if (usuariosForm.getIdPerfil() != 4 ){ %>
    <form id="frmExportarPlantilla" name="frmExportarPlantilla" class="easyui-form"  method="post" 
    	action="/siarex247/excel/exportarPlantilla.action" target="frmOcultoSiarex">
      	<input type="hidden" name="foliosExportar" id="foliosExportar_Exportar_Plantilla" value="0">
      	<input type="hidden" name="tipoMoneda" id="tipoMoneda_Exportar_Plantilla" value="0">
      	<input type="hidden" name="estatusOrden" id="estatusOrden_Exportar_Plantilla" value="0">
      	<input type="hidden" name="rfc" id="rfc_Exportar_Plantilla" value="0">
      	<input type="hidden" name="razonSocial" id="razonSocial_Exportar_Plantilla" value="0">
      	<input type="hidden" name="uuid" id="uuid_Exportar_Plantilla" value="0">
      	<input type="hidden" name="folioEmpresa" id="folioEmpresa_Exportar_Plantilla" value="0">
      	<input type="hidden" name="serieFolio" id="serieFolio_Exportar_Plantilla" value="0">
      	<input type="hidden" name="fechaInicial" id="fechaInicial_Exportar_Plantilla" value="0">
      	<input type="hidden" name="fechaFinal" id="fechaFinal_Exportar_Plantilla" value="0">
      	
   </form>

    <form id="frmExportarFacturas" name="frmExportarFacturas" class="easyui-form"  method="post" 
    	action="/siarex247/excel/exportarFacturas.action" target="frmOcultoSiarex">
      	<input type="hidden" name="foliosExportar" id="foliosExportar_Facturas" value="0">
      	<input type="hidden" name="tipoMoneda" id="tipoMoneda_Facturas" value="0">
      	<input type="hidden" name="estatusOrden" id="estatusOrden_Facturas" value="0">
      	<input type="hidden" name="rfc" id="rfc_Facturas" value="0">
      	<input type="hidden" name="razonSocial" id="razonSocial_Facturas" value="0">
      	<input type="hidden" name="uuid" id="uuid_Facturas" value="0">
      	<input type="hidden" name="folioEmpresa" id="folioEmpresa_Facturas" value="0">
      	<input type="hidden" name="serieFolio" id="serieFolio_Facturas" value="0">
      	<input type="hidden" name="fechaInicial" id="fechaInicial_Facturas" value="0">
      	<input type="hidden" name="fechaFinal" id="fechaFinal_Facturas" value="0">
      	
   </form>
   
    <form id="frmexportarLayOut" name="frmexportarLayOut" class="easyui-form"  method="post" 
    	action="/siarex247/excel/exportarLayOut.action" target="frmOcultoSiarex">
      	<input type="hidden" name="foliosExportar" id="foliosExportar_LayOut" value="0">
      	<input type="hidden" name="tipoMoneda" id="tipoMoneda_LayOut" value="0">
      	<input type="hidden" name="estatusOrden" id="estatusOrden_LayOut" value="0">
      	<input type="hidden" name="rfc" id="rfc_LayOut" value="0">
      	<input type="hidden" name="razonSocial" id="razonSocial_LayOut" value="0">
      	<input type="hidden" name="uuid" id="uuid_LayOut" value="0">
      	<input type="hidden" name="folioEmpresa" id="folioEmpresa_LayOut" value="0">
      	<input type="hidden" name="serieFolio" id="serieFolio_LayOut" value="0">
      	<input type="hidden" name="fechaInicial" id="fechaInicial_LayOut" value="0">
      	<input type="hidden" name="fechaFinal" id="fechaFinal_LayOut" value="0">
      	
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
		 
		
		// obtenerFechasFiltro();
		calcularEtiquetasDetalleVisor();
	});
	
	
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

</html>