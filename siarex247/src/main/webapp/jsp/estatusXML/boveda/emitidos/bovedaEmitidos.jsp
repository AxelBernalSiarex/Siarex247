
<%@page import="com.siarex247.utils.Utils"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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

  <meta name="viewport" content="width=device-width, initial-scale=1">
 	   <script src='/siarex247/jsp/estatusXML/boveda/emitidos/bovedaEmitidos.js?v=<%=Utils.VERSION%>'></script>
 	   
 	   <style>
			/* Filtros compactos */
			.dx-like-filter { display:flex; align-items:center; gap:6px; position: relative; }
			.dx-like-filter .op-btn{ min-width:28px; height:28px; padding:0 6px; display:inline-flex; align-items:center; justify-content:center; cursor:pointer; border:1px solid #d0d5dd; border-radius:4px; background:#fff; line-height:1; }
			.dx-like-filter .op-label{ font-size:12px; line-height:1; font-weight:600; }
			.dx-like-filter input{ height:28px; line-height:28px; padding:0 8px; border:1px solid #d0d5dd; border-radius:4px; width:100%; }
			.dx-like-filter input[type="number"] { width: 90px; }
			.dx-like-menu{ position:absolute; z-index:1000; background:#fff; border:1px solid #e5e7eb; border-radius:6px; box-shadow:0 6px 20px rgba(0,0,0,.08); min-width:180px; display:none; top:32px; left:0; }
			.dx-like-menu.show{display:block;}
			.dx-like-menu ul{list-style:none; margin:0; padding:4px;}
			.dx-like-menu li{padding:6px 10px; cursor:pointer; border-radius:4px; white-space:nowrap;}
			.dx-like-menu li:hover{background:#f3f4f6;}
			
			
			/* Alcance especÃÂ­fico a la tabla de Emitidos */
			#tablaDetalleEmitidos thead tr.filters th{ position:relative; overflow:visible; }
			
			
			/* Ancho recomendado para SERIE (columna id=BOVEDA_EMITIDOS_ETQ18) */
			#tablaDetalleEmitidos th#BOVEDA_EMITIDOS_ETQ18, #tablaDetalleEmitidos td:nth-child(4){ min-width: 180px; }
			
			
			.d-none { display:none; }
			</style>
			
				
		<style>
			  /* deja salir el menÃÂº desde el thead */
			  #tablaDetalleEmitidos thead tr.filters th { position: relative; overflow: visible !important; }
			  table.dataTable thead th, table.dataTable thead td { overflow: visible !important; }
			  .dx-like-menu { z-index: 20000; }
			  /* si usas FixedHeader */
			  .dtfh-floatingparent thead th { overflow: visible !important; }
		</style>
			
		<style>
  			#tablaDetalleEmitidos thead .dx-like-menu {
    			position: absolute;
    			z-index: 1090; /* mayor que tooltips/modals si hace falta */
  			}
		</style>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="BOVEDA_EMITIDOS_ETQ6">Fecha de Ultima Actualizaci&oacute;n : </h5>
			</div>
			<div class="col-auto d-flex">
			   
				<div class="dropdown font-sans-serif">
					<button class="btn btn-outline-secondary btn-sm btnClr" type="button" id="btnLimpiar_Emitidos" onclick="limpiarEmitidos();">
						<span class="fas fa-broom me-1"></span>
						<span class="d-none d-sm-inline-block">Limpiar</span>
					</button>
					
					<button class="btn btn-falcon-success btn-sm" type="button" id="btnRefrescar_Emitidos" >
						<span class="fab fa-firefox-browser me-1"></span>
						<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR_EMITIDOS">Actualizar</span>
					</button>				
					
					<button class="btn btn-outline-secondary btn-sm " type="button" id="btnExport" onclick="exportExcelEmitidos();">
						<span class="fas fa-external-link-alt me-1"></span>
						<span class="d-none d-sm-inline-block">Export</span>
					</button>
					
					<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
						<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="BOVEDA_EMITIDOS_ETQ1">Opciones</span>
					</button>
					<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
						<a class="dropdown-item" href="javascript:abreModal_Emitidos('nuevo');" id="BOVEDA_EMITIDOS_ETQ2">Cargar XML</a>
						<a class="dropdown-item" href="javascript:convXMLAExcelEmitidos();" id="BOVEDA_EMITIDOS_ETQ3">Convertir XML a Excel</a>
						<a class="dropdown-item" href="javascript:consultarTotalesEmitidos();" id="BOVEDA_EMITIDOS_ETQ4">Descargar CFDI</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item text-danger" href="javascript:eliminaBovedaEmitidos('MULTIPLE');" id="BOVEDA_EMITIDOS_ETQ5">Eliminar Registro</a>
					</div>
			  </div>
			</div>
		</div>
	</div>

<!-- 		
	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="BOVEDA_EMITIDOS_ETQ6">Fecha de Ultima Actualizaci&oacute;n : </h5>
			</div>
			<div class="col-auto d-flex">
			   
				<div class="dropdown font-sans-serif">
				    <button class="btn btn-outline-secondary btn-sm btnClr" type="button" id="btnLimpiar_Emitidos" onclick="limpiarEmitidos();">
						<span class="fas fa-broom me-1"></span>
						<span class="d-none d-sm-inline-block">Limpiar</span>
					</button>
					<button class="btn btn-falcon-success btn-sm" type="button" id="btnRefrescar_Emitidos">
						<span class="fab fa-firefox-browser me-1"></span>
						<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR">Actualizar</span>
					</button>
					<button class="btn btn-outline-secondary btn-sm " type="button" id="btnExport_Emitidos" onclick="exportExcelEmitidos();">
						<span class="fas fa-external-link-alt me-1"></span>
						<span class="d-none d-sm-inline-block">Export</span>
					</button>
					
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="BOVEDA_EMITIDOS_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:abreModal_Emitidos('nuevo');" id="BOVEDA_EMITIDOS_ETQ2">Cargar XML</a>
					<a class="dropdown-item" href="javascript:convXMLAExcelEmitidos();" id="BOVEDA_EMITIDOS_ETQ3">Convertir XML a Excel</a>
					<a class="dropdown-item" href="javascript:consultarTotalesEmitidos();" id="BOVEDA_EMITIDOS_ETQ4">Descargar CFDI</a>
					<div class="dropdown-divider"></div>
					<a class="dropdown-item text-danger" href="javascript:eliminaBovedaEmitidos('MULTIPLE');" id="BOVEDA_EMITIDOS_ETQ5">Eliminar Registro</a>
				</div>
			  </div>
			</div>
		</div>
	</div>
	 -->

	<div class="card-header">
		<div class="mb-2 row">
				<label class="col-sm-2 col-form-label" for="fechaInicial_Emitidos_Filtro" id="BOVEDA_EMITIDOS_ETQ9" >Fecha Inicio Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Emitidos_Filtro" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
				<label class="col-sm-2 col-form-label" for="fechaFinal_Emitidos_Filtro" id="BOVEDA_EMITIDOS_ETQ12">Fecha Final Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Emitidos_Filtro" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
	              
        </div>
        
        <!-- 
        <div class="mb-2 row">
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarBoveda();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span> <span id="BOVEDA_ETQ11"> Refrescar </span>  </button>
					</div>
				</div>
        </div>
         -->
	</div>
	
 <div class="collapse" id="filtrosBusquedaVisor">
	<div class="card-header">
	
	
		<div class="mb-2 row">
                <label class="col-sm-1 col-form-label" for="rfc_Emitidos" id="BOVEDA_EMITIDOS_ETQ7">RFC Receptor</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="rfc_Emitidos" name="rfc" class="form-control" type="text"  value="" maxlength="15"  />
					</div>
				</div>
				<label class="col-sm-2 col-form-label" for="razonSocial_Emitidos" id="BOVEDA_EMITIDOS_ETQ8">Raz&oacute;n Social Receptor</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="razonSocial_Emitidos" name="razonSocial" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="fechaInicial_Emitidos" id="BOVEDA_EMITIDOS_ETQ9_BK" >Fecha Factura Inicio</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Emitidos" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
        </div>
        
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="uuid_Emitidos" id="BOVEDA_EMITIDOS_ETQ10">UUID</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="uuid_Emitidos" name="uuid" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="tipoComprobante_Emitidos" id="BOVEDA_EMITIDOS_ETQ11">Tipo de Comprobante </label>
				<div class="col-sm-3">
					<div class="form-group">
					   <select id="tipoComprobante_Emitidos" class="form-select"> 
					  		 <option value="ALL"style="text-align: center;"> Todos </option>
					   		 <option value="I"  style="text-align: center;"> I - Ingreso</option>
                        	 <option value="P"  style="text-align: center;"> P - Pago</option>
                        	 <option value="E"  style="text-align: center;"> E - Egresos</option>
					   		 <option value="T"  style="text-align: center;"> T - Translado</option>
					   </select>
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="fechaFinal_Emitidos" id="BOVEDA_EMITIDOS_ETQ12_BK">Fecha Factura Final</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Emitidos" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
					
        </div>
        
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="serie_Emitidos" id="BOVEDA_EMITIDOS_ETQ13">Serie</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="serie_Emitidos" name="serie" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="folio_Emitidos" id="BOVEDA_EMITIDOS_ETQ14">Folio</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="folio_Emitidos" name="folio" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarBovedaEmitidos();" id="btnRefrescar_Emitidos_Respaldo" ><span class="fab fa-firefox-browser me-1"></span> <span id="BOVEDA_EMITIDOS_BTN_Refrescar"> Refrescar </span>  </button>
					   <button class="btn btn-falcon-secondary btn-sm mb-2 mb-sm-0" type="button" onclick="limpiarEmitidos();" id="btnLimipiar_Emitidos" ><span class="fas fa-broom me-1"></span> <span id="BOVEDA_EMITIDOS_BTN_Limipiar"> Limpiar </span>  </button>
					</div>
				</div>
        </div>
	</div>
 </div>	

	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		<div class="tab-content" style="overflow: auto;">
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<div id="overSeccion_Boveda_Emitidos" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<input type="hidden" id="rfcOperatorE" value="contains" />
					<input type="hidden" id="razonOperatorE" value="contains" />
					<input type="hidden" id="serieOperatorE" value="contains" />
					<input type="hidden" id="tipoOperatorE" value="equals" />
					<input type="hidden" id="uuidOperatorE" value="contains" />
					
					
					<input type="hidden" id="folioOperatorE" value="eq" />
					<input type="hidden" id="totalOperatorE" value="eq" />
					<input type="hidden" id="subOperatorE" value="eq" />
					<input type="hidden" id="retOperatorE" value="eq" />
					<input type="hidden" id="trasOperatorE" value="eq" />
					
					
					<input type="hidden" id="dateOperatorE" value="eq" />
					<table id="tablaDetalleEmitidos"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ15">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ16">RFC Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ17">Raz&oacute;n Social Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ18">Serie</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ19">Tipo de Comprobante</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ20">Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ21">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ22">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ23">Total Retenciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ24">Total Traslado</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ25">XML</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ26">PDF</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ27">UUID</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_EMITIDOS_ETQ28">Fecha Factura</th>
								
							</tr>
							<tr class="filters">
							  <!-- col 1: Acciones -->
							  <th></th>
							
							  <!-- col 2: RFC -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="rfcOpBtnE" title="Operador"><span class="op-label"><i class="fas fa-search"></i></span></span>
							      <input type="hidden" id="rfcGridFilterE" value="" />
							      <input type="text" id="rfcFilterInputE" placeholder="Filtrar RFC..." />
							      <div class="dx-like-menu" id="rfcOpMenuE" role="menu">
							        <ul>
							          <li data-op="contains">⊚ Contiene</li>
							          <li data-op="notContains">⊘ No contiene</li>
							          <li data-op="startsWith">&#8676; Empieza con</li>
							          <li data-op="endsWith">&#8677; Termina con</li>
							          <li data-op="equals">= Igual</li>
							          <li data-op="notEquals">&ne; Distinto</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 3: RazÃÂ³n Social -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="razonOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
							      <input type="text" id="razonFilterInputE" placeholder="Filtrar razón social..." />
							      <div class="dx-like-menu" id="razonOpMenuE" role="menu">
							        <ul>
							          <li data-op="contains">⊚ Contiene</li>
							          <li data-op="notContains">⊘ No contiene</li>
							          <li data-op="startsWith">&#8676; Empieza con</li>
							          <li data-op="endsWith">&#8677; Termina con</li>
							          <li data-op="equals">= Igual</li>
							          <li data-op="notEquals">&ne; Distinto</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 4: Serie -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="serieOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
							      <input type="text" id="serieFilterInputE" placeholder="Filtrar serie..." />
							      <div class="dx-like-menu" id="serieOpMenuE" role="menu">
							        <ul>
							          <li data-op="contains">⊚ Contiene</li>
							          <li data-op="notContains">⊘ No contiene</li>
							          <li data-op="startsWith">&#8676; Empieza con</li>
							          <li data-op="endsWith">&#8677; Termina con</li>
							          <li data-op="equals">= Igual</li>
							          <li data-op="notEquals">&ne; Distinto</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 5: Tipo de Comprobante -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="tipoOpBtnE"><span class="op-label">=</span></span>
							      <select id="tipoFilterInputE" class="form-select form-select-sm">
							        <option value="ALL">Todos</option>
							        <option value="I">I - Ingreso</option>
							        <option value="P">P - Pago</option>
							        <option value="E">E - Egreso</option>
							        <option value="T">T - Traslado</option>
							      </select>
							      <input type="hidden" id="tipoOpMenuE" value="contains">
							    </div>
							  </th>
							
							  <!-- col 6: Folio (numÃÂ©rico) -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="folioOpBtnE"><span class="op-label">=</span></span>
							      <input type="number" step="any" id="folioFilter1E" placeholder="Folio..." />
							      <input type="number" step="any" id="folioFilter2E" placeholder="y..." class="d-none" />
							      <div class="dx-like-menu" id="folioOpMenuE" role="menu">
							        <ul>
							          <li data-op="eq">= Igual</li>
							          <li data-op="ne">&ne; No igual</li>
							          <li data-op="lt">&lt; Menor que</li>
							          <li data-op="gt">&gt; Mayor que</li>
							          <li data-op="le">&le; Menor o igual</li>
							          <li data-op="ge">&ge; Mayor o igual</li>
							          <li data-op="between">&#8646; Entre</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 7: Total (numÃÂ©rico) -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="totalOpBtnE"><span class="op-label">=</span></span>
							      <input type="number" step="any" id="totalFilter1E" placeholder="Total..." />
							      <input type="number" step="any" id="totalFilter2E" placeholder="y..." class="d-none" />
							      <div class="dx-like-menu" id="totalOpMenuE" role="menu">
							        <ul>
							          <li data-op="eq">= Igual</li>
							          <li data-op="ne">&ne; No igual</li>
							          <li data-op="lt">&lt; Menor que</li>
							          <li data-op="gt">&gt; Mayor que</li>
							          <li data-op="le">&le; Menor o igual</li>
							          <li data-op="ge">&ge; Mayor o igual</li>
							          <li data-op="between">&#8646; Entre</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 8: Sub-Total (numÃÂ©rico) -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="subOpBtnE"><span class="op-label">=</span></span>
							      <input type="number" step="any" id="subFilter1E" placeholder="Sub-total..." />
							      <input type="number" step="any" id="subFilter2E" placeholder="y..." class="d-none" />
							      <div class="dx-like-menu" id="subOpMenuE" role="menu">
							        <ul>
							          <li data-op="eq">= Igual</li>
							          <li data-op="ne">&ne; No igual</li>
							          <li data-op="lt">&lt; Menor que</li>
							          <li data-op="gt">&gt; Mayor que</li>
							          <li data-op="le">&le; Menor o igual</li>
							          <li data-op="ge">&ge; Mayor o igual</li>
							          <li data-op="between">&#8646; Entre</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 9: Total Retenciones (numÃÂ©rico) -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="retOpBtnE"><span class="op-label">=</span></span>
							      <input type="number" step="any" id="retFilter1E" placeholder="Retenciones..." />
							      <input type="number" step="any" id="retFilter2E" placeholder="y..." class="d-none" />
							      <div class="dx-like-menu" id="retOpMenuE" role="menu">
							        <ul>
							          <li data-op="eq">= Igual</li>
							          <li data-op="ne">&ne; No igual</li>
							          <li data-op="lt">&lt; Menor que</li>
							          <li data-op="gt">&gt; Mayor que</li>
							          <li data-op="le">&le; Menor o igual</li>
							          <li data-op="ge">&ge; Mayor o igual</li>
							          <li data-op="between">&#8646; Entre</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 10: Total Traslado (numÃÂ©rico) -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="trasOpBtnE"><span class="op-label">=</span></span>
							      <input type="number" step="any" id="trasFilter1E" placeholder="Traslado..." />
							      <input type="number" step="any" id="trasFilter2E" placeholder="y..." class="d-none" />
							      <div class="dx-like-menu" id="trasOpMenuE" role="menu">
							        <ul>
							          <li data-op="eq">= Igual</li>
							          <li data-op="ne">&ne; No igual</li>
							          <li data-op="lt">&lt; Menor que</li>
							          <li data-op="gt">&gt; Mayor que</li>
							          <li data-op="le">&le; Menor o igual</li>
							          <li data-op="ge">&ge; Mayor o igual</li>
							          <li data-op="between">&#8646; Entre</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 11: XML -->
							  <th></th>
							  <!-- col 12: PDF -->
							  <th></th>
							
							  <!-- col 13: UUID -->
							  <th>
							    <div class="dx-like-filter">
							      <span class="op-btn" id="uuidOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
							      <input type="text" id="uuidFilterInputE" placeholder="Filtrar UUID..." />
							      <div class="dx-like-menu" id="uuidOpMenuE" role="menu">
							        <ul>
							          <li data-op="contains">⊚ Contiene</li>
							          <li data-op="notContains">⊘ No contiene</li>
							          <li data-op="startsWith">&#8676; Empieza con</li>
							          <li data-op="endsWith">&#8677; Termina con</li>
							          <li data-op="equals">= Igual</li>
							          <li data-op="notEquals">&ne; Distinto</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							  </th>
							
							  <!-- col 14: Fecha Factura 
							  <th>
							    <div class="dx-like-filter" style="min-width:260px;">
							      <span class="op-btn" id="dateOpBtnE"><span class="op-label">=</span></span>
							      <input type="date" id="dateFilter1E" />
							      <input type="date" id="dateFilter2E" class="d-none" />
							      <div class="dx-like-menu" id="dateOpMenuE" role="menu">
							        <ul>
							          <li data-op="eq">= Igual</li>
							          <li data-op="ne">&ne; No igual</li>
							          <li data-op="lt">&lt; Menor que</li>
							          <li data-op="gt">&gt; Mayor que</li>
							          <li data-op="le">&le; Menor o igual</li>
							          <li data-op="ge">&ge; Mayor o igual</li>
							          <li data-op="bt">&#8634; Entre</li>
							          <li data-op="reset">⟲ Reset</li>
							        </ul>
							      </div>
							    </div>
							    <input type="hidden" id="dateOperatorE" value="eq">
							  </th>-->
							  <th>
								  <input type="hidden" id="dateOperatorE" value="eq">
								  <input type="hidden" id="dateFilter1E">
								  <input type="hidden" id="dateFilter2E">
								  <!-- opcional, placeholder por si algún JS toca el label -->
								  <span id="dateOpBtnE" class="d-none"><span class="op-label">=</span></span>
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<form action="/siarex247/jsp/estatusXML/boveda/emitidos/mostrarBovedaEmitidos.jsp" name="frmBovedaEmitidos" id="frmBovedaEmitidos" target="_blank" method="post">
   <input type="hidden" name="f" value="" id="idRegistroP_Emitidos">
   <input type="hidden" name="t" value="" id="tipoArchivoP_Emitidos">
</form>


<!-- 
<form action="/siarex247/excel/descargarBovedaEmitidos.action" name="frmBovedaEmitidosZIP" id="frmBovedaEmitidosZIP" target="_blank" method="post">
   <input type="hidden" name="rfc" value="" id="rfcZIP_Emitidos">
   <input type="hidden" name="razonSocial" value="" id="razonSocialZIP_Emitidos">
   <input type="hidden" name="folio" value="" id="folioZIP_Emitidos">
   <input type="hidden" name="serie" value="" id="serieZIP_Emitidos">
   <input type="hidden" name="fechaInicial" value="" id="fechaInicialZIP_Emitidos">
   <input type="hidden" name="fechaFinal" value="" id="fechaFinalZIP_Emitidos">
   <input type="hidden" name="tipoComprobante" value="" id="tipoComprobanteZIP_Emitidos">
   <input type="hidden" name="uuid" value="" id="uuidBovedaZIP_Emitidos">
   <input type="hidden" name="idRegistro" value="" id="idRegistroZIP_Emitidos">
</form>
 -->
 
<form action="/siarex247/excel/exportXMLaEXCELEmitidos.action" name="frmBovedaXMLExcelEmitidos" id="frmBovedaXMLExcelEmitidos" target="_blank" method="post">
   <input type="hidden" name="rfc" value="" id="rfcXMLExcel_Emitidos">
   <input type="hidden" name="razonSocial" value="" id="razonSocialXMLExcel_Emitidos">
   <input type="hidden" name="folio" value="" id="folioXMLExcel_Emitidos">
   <input type="hidden" name="serie" value="" id="serieXMLExcel_Emitidos">
   <input type="hidden" name="fechaInicial" value="" id="fechaInicial_ConvXML_Emitidos">
   <input type="hidden" name="fechaFinal" value="" id="fechaFinal_ConvXML_Emitidos">
   <input type="hidden" name="tipoComprobante" value="" id="tipoComprobanteXMLExcel_Emitidos">
   <input type="hidden" name="uuid" value="" id="uuidBovedaXMLExcel_Emitidos">
   <input type="hidden" name="bandSelecciono" value="" id="bandSeleccionoXMLExcel_Emitidos">
   <input type="hidden" name="idRegistro" value="" id="idRegistroXMLExcel_Emitidos">
   <input type="hidden" name="bandFiltros" value="false" id="bandFiltros_Emitidos">
   
</form>



<form action="/siarex247/excel/exportExcelBovedaEmitidos.action"
      name="frmExportarDetalleExcel_Emitidos"
      id="frmExportarDetalleExcel_Emitidos"
      target="_blank"
      method="post">

  <!-- BÃ¡sicos -->
  <input type="hidden" name="rfc"             id="rfc_Emitidos_Exportar">
  <input type="hidden" name="razonSocial"     id="razonSocial_Emitidos_Exportar">
  <input type="hidden" name="folio"           id="folio_Emitidos_Exportar">
  <input type="hidden" name="serie"           id="serie_Emitidos_Exportar"><!-- â corregido el id -->
  <input type="hidden" name="fechaInicial"    id="fechaInicial_Emitidos_Exportar">
  <input type="hidden" name="fechaFinal"      id="fechaFinal_Emitidos_Exportar">
  <input type="hidden" name="tipoComprobante" id="tipoComprobante_Emitidos_Exportar">
  <input type="hidden" name="uuid"            id="uuid_Emitidos_Exportar">

  <!-- Operadores/valores (opcionales, el Action los puede ignorar sin problema) -->
  <input type="hidden" name="rfcOperator"     id="rfcOperator_Emitidos_Exportar">
  <input type="hidden" name="razonOperator"   id="razonOperator_Emitidos_Exportar">
  <input type="hidden" name="serieOperator"   id="serieOperator_Emitidos_Exportar">
  <input type="hidden" name="tipoOperator"    id="tipoOperator_Emitidos_Exportar">
  <input type="hidden" name="uuidOperator"    id="uuidOperator_Emitidos_Exportar">

  <input type="hidden" name="dateOperator"    id="dateOperator_Emitidos_Exportar">
  <input type="hidden" name="dateV1"          id="dateV1_Emitidos_Exportar">
  <input type="hidden" name="dateV2"          id="dateV2_Emitidos_Exportar">

  <input type="hidden" name="folioOperator"   id="folioOperator_Emitidos_Exportar">
  <input type="hidden" name="folioV1"         id="folioV1_Emitidos_Exportar">
  <input type="hidden" name="folioV2"         id="folioV2_Emitidos_Exportar">

  <input type="hidden" name="totalOperator"   id="totalOperator_Emitidos_Exportar">
  <input type="hidden" name="totalV1"         id="totalV1_Emitidos_Exportar">
  <input type="hidden" name="totalV2"         id="totalV2_Emitidos_Exportar">

  <input type="hidden" name="subOperator"     id="subOperator_Emitidos_Exportar">
  <input type="hidden" name="subV1"           id="subV1_Emitidos_Exportar">
  <input type="hidden" name="subV2"           id="subV2_Emitidos_Exportar">

  <input type="hidden" name="ivaOperator"     id="ivaOperator_Emitidos_Exportar">
  <input type="hidden" name="ivaV1"           id="ivaV1_Emitidos_Exportar">
  <input type="hidden" name="ivaV2"           id="ivaV2_Emitidos_Exportar">

  <input type="hidden" name="ivaRetOperator"  id="ivaRetOperator_Emitidos_Exportar">
  <input type="hidden" name="ivaRetV1"        id="ivaRetV1_Emitidos_Exportar">
  <input type="hidden" name="ivaRetV2"        id="ivaRetV2_Emitidos_Exportar">
</form>



   <div class="modal fade bd-example-modal-lg" id="myModalDetalle_Emitidos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
   <div class="modal fade" id="myModalNotifica_Emitidos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
   <div class="modal fade" id="modalDescargaCFDI_Emitidos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>



<script type="text/javascript">
    var LABEL_BOVEDA_EMITIDOS_ETQ8 = null;
    
	$(document).ready(function() {
		
		$("#myModalDetalle_Emitidos").load('/siarex247/jsp/estatusXML/boveda/emitidos/modalBovedaEmitidos.jsp');
		$("#myModalNotifica_Emitidos").load('/siarex247/jsp/estatusXML/boveda/emitidos/modalConfirm.jsp');
		$("#modalDescargaCFDI_Emitidos").load('/siarex247/jsp/estatusXML/boveda/emitidos/modalDescargaCFDIEmitidos.jsp');
		
		$('#tipoComprobante_Emitidos').select2({
			theme: 'bootstrap-5'
		});
		$('#tipoComprobante_Emitidos').val('ALL'); 		// Selecciona primer valor del combo
		$('#tipoComprobante_Emitidos').trigger('change');  // Refresca el combo
		
		
		$('#btnRefrescar_Emitidos').on('click', function(e) {
			validarFechas_Emitidos();
			  /*
			if (typeof window.__copiarTheadAGlobalesE === 'function') window.__copiarTheadAGlobalesE();
			  const $btn = $(this);
			  const html0 = $btn.html();

			  // Desactiva el botón y muestra el spinner
			  $btn.prop('disabled', true)
			      .html('<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Refrescando…');

			  // __syncFilterToGlobalE($(this).closest('.dx-like-filter'));
			  // Recarga el DataTable
			  tablaDetalleEmitidos.page('first').draw('page');
			  tablaDetalleEmitidos.ajax.reload(function() {
			    $btn.prop('disabled', false).html(html0);
			  }, false);
			 */ 
		});
		/*
		
		flatpickr(fechaInicial_Emitidos_Filtro, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "Y-m-d",
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
		
		
		flatpickr(fechaFinal_Emitidos_Filtro, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "Y-m-d",
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
		
		*/
		
		obtenerFechasFiltroEmitidos();
		
		 // consultarFechaEmitidos();
		calcularEtiquetasBovedaEmitidos();
	});
	
	
	function obtenerFechasMinimaEmitidos(fechaInicial, fechaFinal){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/emitidos/consultarFechaMinima.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					const calendarioIniEmitidos = flatpickr("#fechaInicial_Emitidos_Filtro", { 
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
					calendarioIniEmitidos.setDate(fechaInicial);
					
					const calendarioFinEmitidos = flatpickr("#fechaFinal_Emitidos_Filtro", { 
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
					
					
					calendarioFinEmitidos.setDate(fechaFinal);  
					
					 $('#fechaInicial_Emitidos_Filtro').val(fechaInicial);
					 $('#fechaFinal_Emitidos_Filtro').val(fechaFinal);
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasMinimaEmitidos()_'+thrownError);
			}
		});	
				
	}
	

	 function calcularEtiquetasBovedaEmitidos(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'BOVEDA_EMITIDOS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
					//  document.getElementById("BOVEDA_EMITIDOS_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("BOVEDA_EMITIDOS_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("BOVEDA_EMITIDOS_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("BOVEDA_EMITIDOS_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("BOVEDA_EMITIDOS_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("BOVEDA_EMITIDOS_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("BOVEDA_EMITIDOS_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("BOVEDA_EMITIDOS_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("BOVEDA_EMITIDOS_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("BOVEDA_EMITIDOS_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("BOVEDA_EMITIDOS_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("BOVEDA_EMITIDOS_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("BOVEDA_EMITIDOS_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("BOVEDA_EMITIDOS_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("BOVEDA_EMITIDOS_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("BOVEDA_EMITIDOS_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("BOVEDA_EMITIDOS_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("BOVEDA_EMITIDOS_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("BOVEDA_EMITIDOS_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("BOVEDA_EMITIDOS_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("BOVEDA_EMITIDOS_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("BOVEDA_EMITIDOS_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("BOVEDA_EMITIDOS_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("BOVEDA_EMITIDOS_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("BOVEDA_EMITIDOS_ETQ24").innerHTML = data.ETQ24;
						document.getElementById("BOVEDA_EMITIDOS_ETQ25").innerHTML = data.ETQ25;
						document.getElementById("BOVEDA_EMITIDOS_ETQ26").innerHTML = data.ETQ26;
						document.getElementById("BOVEDA_EMITIDOS_ETQ27").innerHTML = data.ETQ27;
						document.getElementById("BOVEDA_EMITIDOS_ETQ28").innerHTML = data.ETQ28;
						
						document.getElementById("BOVEDA_EMITIDOS_BTN_Refrescar").innerHTML = BTN_REFRESCAR;
						document.getElementById("BOVEDA_EMITIDOS_BTN_Limipiar").innerHTML = BTN_LIMPIAR;
						
						LABEL_BOVEDA_EMITIDOS_ETQ8 = data.ETQ6;
						// LABEL_BOVEDA_EMITIDOS_TEXT1 = data.TEXT1;
						// TITLE_DELETE_CATALOGO = data.TEXT2;
						// LABEL_BOVEDA_EMITIDOS_TEXT3 = data.TEXT3;
						// LABEL_BOVEDA_EMITIDOS_TEXT4 = data.TEXT4;
						// LABEL_BOVEDA_EMITIDOS_TEXT5 = data.TEXT5;
						
						consultarFechaEmitidos();
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasBoveda()_1_'+thrownError);
				}
			});	
		}
	 
	 $('#tipoFilterInputE').on('change', function(e) {
		 const v = ($(this).val()||'').trim();
		  $('#tipoOpMenuE').val(v && v!=='ALL' ? 'equals' : 'contains');
		  // UI del botÃÂÃÂ³n: "=" cuando hay valor, lupa cuando TODOS
		  $('#tipoOpBtnE .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
		 // window.__bvdRec_syncThead();
		  validarFechas_Emitidos();
			
	});
	 
</script>

		<script>
				(function(){

				  // ====== Fecha con operador (Emitidos) ======
				  function initDxLikeDateFilterE({btnId, menuId, input1Id, input2Id, hiddenOpId}){
				    const $btn    = $(btnId),
				          $menu   = $(menuId),
				          $d1     = $(input1Id),
				          $d2     = $(input2Id),
				          $hidden = $(hiddenOpId),
				          $label  = $btn.find('.op-label');
				
				    if(!$btn.length || !$menu.length || !$d1.length || !$hidden.length) return;
				
				    if(!$hidden.val()) $hidden.val('eq');
				
				    const sym = {eq:'=', ne:'≠', lt:'<', gt:'>', le:'≤', ge:'≥', bt:'↔'};
				
				    function showSecond(show){
				      if(show){ $d2.removeClass('d-none'); }
				      else { $d2.addClass('d-none').val(''); }
				    }
				
				    // abrir/cerrar menÃÂº
				    $btn.on('click', e => { e.stopPropagation(); $menu.toggleClass('show'); });
				    $(document).on('click.emitidosmenu', () => $menu.removeClass('show'));
				
				    // seleccionar operador (no refresca aquÃÂ­)
				    $menu.on('click','li',function(){
				      const op = String($(this).data('op')||'').toLowerCase();
				      if(op === 'reset'){
				        $hidden.val('eq');
				        $label.text('=');
				        $d1.val('');
				        showSecond(false);
				      } else {
				        $hidden.val(op);
				        $label.text(sym[op] || '=');
				        showSecond(op === 'bt');
				      }
				      $menu.removeClass('show');
				    });
				
				    // ENTER aplica (refresca tabla)
				    // const aplicarFecha = ()=>{ if(typeof refrescarBovedaEmitidos === 'function') refrescarBovedaEmitidos('date-enter'); };
				    const ns = '.dxenterE';
				    $d1.off('keydown'+ns).on('keydown'+ns, e => { if(e.key==='Enter'){ e.preventDefault(); aplicarFecha(); }});
				    $d2.off('keydown'+ns).on('keydown'+ns, e => { if(e.key==='Enter'){ e.preventDefault(); aplicarFecha(); }});
				  }
				
				  // ====== Bind de todos los filtros (Emitidos) ======
				  $(function(){
				    // Textos
				    initDxLikeFilterE({ btnId:'#rfcOpBtnE',   menuId:'#rfcOpMenuE',   inputId:'#rfcFilterInputE',   hiddenOpId:'#rfcOperatorE',   targetInput:'#rfc_Emitidos' });
				    initDxLikeFilterE({ btnId:'#razonOpBtnE', menuId:'#razonOpMenuE', inputId:'#razonFilterInputE', hiddenOpId:'#razonOperatorE', targetInput:'#razonSocial_Emitidos' });
				    initDxLikeFilterE({ btnId:'#serieOpBtnE', menuId:'#serieOpMenuE', inputId:'#serieFilterInputE', hiddenOpId:'#serieOperatorE',  targetInput:'#serie_Emitidos' });
				    initDxLikeFilterE({ btnId:'#tipoOpBtnE',  menuId:'#tipoOpMenuE',  inputId:'#tipoFilterInputE',  hiddenOpId:'#tipoOperatorE',   targetInput:'#tipoComprobante_Emitidos' });
				    initDxLikeFilterE({ btnId:'#uuidOpBtnE',  menuId:'#uuidOpMenuE',  inputId:'#uuidFilterInputE',  hiddenOpId:'#uuidOperatorE',   targetInput:'#uuid_Emitidos' });
				
				    // NumÃÂ©ricos
				    initNumericDxFilterE({ btnId:'#folioOpBtnE', menuId:'#folioOpMenuE', v1Id:'#folioFilter1E', v2Id:'#folioFilter2E', opHiddenId:'#folioOperatorE' });
				    initNumericDxFilterE({ btnId:'#totalOpBtnE', menuId:'#totalOpMenuE', v1Id:'#totalFilter1E', v2Id:'#totalFilter2E', opHiddenId:'#totalOperatorE' });
				    initNumericDxFilterE({ btnId:'#subOpBtnE',   menuId:'#subOpMenuE',   v1Id:'#subFilter1E',   v2Id:'#subFilter2E',   opHiddenId:'#subOperatorE' });
				    initNumericDxFilterE({ btnId:'#retOpBtnE',   menuId:'#retOpMenuE',   v1Id:'#retFilter1E',   v2Id:'#retFilter2E',   opHiddenId:'#retOperatorE' });
				    initNumericDxFilterE({ btnId:'#trasOpBtnE',  menuId:'#trasOpMenuE',  v1Id:'#trasFilter1E',  v2Id:'#trasFilter2E',  opHiddenId:'#trasOperatorE' });
				
				    // Fecha
				    initDxLikeDateFilterE({ btnId:'#dateOpBtnE', menuId:'#dateOpMenuE', input1Id:'#dateFilter1E', input2Id:'#dateFilter2E', hiddenOpId:'#dateOperatorE' });
				
				  });
				
				})();
		</script>
				

</html>