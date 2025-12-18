<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


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

 		<script src='/siarex247/jsp/estatusXML/boveda/recibidos/boveda.js?v=<%=Utils.VERSION%>'></script>
 		 <!-- estilos para filtros tipo DevExtreme -->
			<style>
			/* Filtros compactos */
			.dx-like-filter { display:flex; align-items:center; gap:6px; position: relative; z-index: 1000; }
			.dx-like-filter .op-btn{ min-width:28px; height:28px; padding:0 6px; display:inline-flex; align-items:center; justify-content:center; cursor:pointer; border:1px solid #d0d5dd; border-radius:4px; background:#fff; line-height:1; position: relative; z-index: 1000; }
			.dx-like-filter .op-label{ font-size:12px; line-height:1; font-weight:600; position: relative; z-index: 1000;}
			.dx-like-filter input{ height:28px; line-height:28px; padding:0 8px; border:1px solid #d0d5dd; border-radius:4px; width:100%; position: relative; z-index: 1000; }
			.dx-like-filter input[type="number"] { width: 90px; position: relative; z-index: 1000;}
			.dx-like-menu{ position: relative; z-index: 1000; background:#fff; border:1px solid #e5e7eb; border-radius:6px; box-shadow:0 6px 20px rgba(0,0,0,.08); min-width:180px; display:none; top:32px; left:0; z-index: 10; }
			.dx-like-menu.show{display:block; position: relative; z-index: 1000; }
			.dx-like-menu ul{list-style:none; margin:0; padding:4px; position: relative; z-index: 1000; }
			.dx-like-menu li{padding:6px 10px; cursor:pointer; border-radius:4px; white-space:nowrap; position: relative; z-index: 1000; }
			.dx-like-menu li:hover{background:#f3f4f6; z-index: 10; }
			
			
			/* Alcance especifico a la tabla de Emitidos */
			#tablaDetalleBoveda thead tr.filters th{ position:relative; overflow:visible; }
			
			
			/* Ancho recomendado para SERIE (columna id=BOVEDA_NOMINA_ETQ18) */
			#tablaDetalleBoveda th#BOVEDA_NOMINA_ETQ18, #tablaDetalleBoveda td:nth-child(4){ min-width: 180px; }
			
			
			.d-none { display:none; }
			</style>
			
				
			<style>
			  /* deja salir el menÃÂº desde el thead */
			  #tablaDetalleBoveda thead tr.filters th { position: relative; overflow: visible !important; }
			  table.dataTable thead th, table.dataTable thead td { overflow: visible !important; }
			  .dx-like-menu { z-index: 20000; }
			  /* si usas FixedHeader */
			  .dtfh-floatingparent thead th { overflow: visible !important; }
			</style>
			
			<style>
				  #tablaDetalleBoveda thead .dx-like-menu {
				    position: absolute;
				    z-index: 1090; /* mayor que tooltips/modals si hace falta */
				  }
				</style>
			
			<style>
				  /* SERIE más ancha */
				  #tablaDetalleBoveda th#BOVEDA_ETQ14,
				   #tablaDetalleBoveda th#BOVEDA_ETQ12,
				  #tablaDetalleBoveda td:nth-child(4){
				    min-width: 180px;   /* ajusta a tu gusto */
				  }
		</style>
			
 
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="BOVEDA_ETQ8">Fecha de Ultima Actualizaci&oacute;n : </h5>
			</div>
			<div class="col-auto d-flex">
			   
				<div class="dropdown font-sans-serif">
					<button class="btn btn-outline-secondary btn-sm btnClr" type="button" id="btnLimpiar" onclick="limpiarRecibidos();">
						<span class="fas fa-broom me-1"></span>
						<span class="d-none d-sm-inline-block">Limpiar</span>
					</button>
					<button class="btn btn-falcon-success btn-sm" type="button" id="btnRefrescar">
						<span class="fab fa-firefox-browser me-1"></span>
						<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR">Actualizar</span>
					</button>
					<button class="btn btn-outline-secondary btn-sm " type="button" id="btnExport" onclick="exportExcelRecibidos();">
						<span class="fas fa-external-link-alt me-1"></span>
						<span class="d-none d-sm-inline-block">Export</span>
					</button>
					
					<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
						<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="BOVEDA_ETQ1">Opciones</span>
					</button>
					<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu" style="z-index: 10000;">
						<a class="dropdown-item" href="javascript:abreModal('nuevo');" id="BOVEDA_ETQ2">Cargar XML</a>
						<a class="dropdown-item" href="javascript:vincularComplementos();" id="BOVEDA_ETQ3">Vincular Complementos</a>
						<a class="dropdown-item" href="javascript:vincularComplementosBoveda();" id="BOVEDA_ETQ4">Vincular Complementos Boveda</a>
						<!-- 
						<a class="dropdown-item" href="javascript:abreModal('descargacfdi');">Descargar CFDI</a>
						 -->
						<a class="dropdown-item" href="javascript:convXMLAExcel();" id="BOVEDA_ETQ5">Convertir XML a Excel</a>
						<a class="dropdown-item" href="javascript:consultarTotalesRecibidos();" id="BOVEDA_ETQ6">Descargar CFDI</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item text-danger" href="javascript:eliminaBovedaMenu();" id="BOVEDA_ETQ7">Eliminar Registro</a>
					</div>
			  </div>
			</div>
		</div>
	</div>
	

	<div class="card-header">
	
	
		<div class="mb-2 row">
				<label class="col-sm-2 col-form-label" for="fechaInicial_Filtro" id="BOVEDA_ETQ9" >Fecha Inicio Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Filtro" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>

				<label class="col-sm-2 col-form-label" for="fechaFinal_Filtro" id="BOVEDA_ETQ10">Fecha Final Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Filtro" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>

	              
        </div>
        
        
	</div>
		
 	
 <div class="collapse" id="filtrosBusquedaVisor">
 
	<div class="card-header">
	
	
		<div class="mb-2 row">
                <label class="col-sm-1 col-form-label" for="rfc" id="BOVEDA_ETQ49">RFC Emisor</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="rfc_Recibidos" name="rfc" class="form-control" type="text"  value="" maxlength="15"  />
					</div>
				</div>
				<label class="col-sm-2 col-form-label" for="razonSocial" id="BOVEDA_ETQ50">Raz&oacute;n Social Emisor</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="razonSocial_Recibidos" name="razonSocial" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="fechaInicial" id="" >Fecha Factura Inicio</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Recibidos" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
        </div>
        
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="uuid" id="BOVEDA_ETQ51">UUID</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="uuid_Recibidos" name="uuid" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="tipoComprobante" id="BOVEDA_ETQ52">Tipo de Comprobante </label>
				<div class="col-sm-3">
					<div class="form-group">
					   <select id="tipoComprobante_Recibidos" class="form-select"> 
					  		 <option value="ALL"  style="text-align: center;"> Todos </option>
					   		 <option value="I"  style="text-align: center;"> I - Ingreso</option>
                        	 <option value="P"  style="text-align: center;"> P - Pago</option>
                        	 <option value="E"  style="text-align: center;"> E - Egresos</option>
					   		 <option value="T"  style="text-align: center;"> T - Translado</option>
					   </select>
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="fechaFinal" id="">Fecha Factura Final</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Recibidos" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
					
        </div>
        
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="serie_Recibidos" id="BOVEDA_ETQ53">Serie</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="serie_Recibidos" name="serie" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="folio_Recibidos" id="BOVEDA_ETQ54">Folio</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="folio_Recibidos" name="folio" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarBoveda();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span> <span id="BOVEDA_ETQ11"> Refrescar </span>  </button>
					   <button class="btn btn-falcon-secondary btn-sm mb-2 mb-sm-0" type="button" onclick="limpiarRecibidos();" id="btnLimipiar_Recibidos" ><span class="fas fa-broom me-1"></span> <span id="BOVEDA_BTN_LIMPIAR"> Limpiar </span>  </button>
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
 </div>
 
 					

	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		<div class="tab-content" style="overflow: auto;">
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<div id="overSeccion_Boveda_Recibidos" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
				
				
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<!-- Ponlo arriba de la tabla, por ejemplo justo antes del <table> -->
						<input type="hidden" id="rfcOperator" value="contains">
						<!-- hidden operators -->
						<input type="hidden" id="razonOperator" value="contains">
						<input type="hidden" id="serieOperator" value="contains">
						<input type="hidden" id="tipoOperator" value="equals">   <!-- recomendado equals por catálogo -->
						<input type="hidden" id="uuidOperator" value="contains">
						<div id="bovedaRecContainer">
					<table id="tablaDetalleBoveda"class="table mb-0 data-table fs--1">
					
						<thead class="bg-200 text-900">
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ27">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ12">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ13">Raz&oacute;n Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ14">Serie</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ15">Tipo de Comprobante</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ16">Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ17">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ18">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ19">Iva</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ20">Iva Ret</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ21">Isr Ret</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ22">Imp. Locales</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ23">XML</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ24">PDF</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ25">UUID</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_ETQ26">Fecha Factura</th>
								
							</tr>
							
									<tr class="filters">
						    <th></th>
								    <th>
								      <!-- Aquí el filtro de RFC tipo DevExtreme -->
								      <div class="dx-like-filter" style="position:relative;">
									  <span class="op-btn" id="rfcOpBtn" title="Operador">
										  <span class="op-label"><i class="fas fa-search"></i></span>
										</span>
										<input type="hidden" id="rfcGridFilter" value="">
									  <input type="text" id="rfcFilterInput" placeholder="Filtrar RFC..." />
									  <div class="dx-like-menu" id="rfcOpMenu" role="menu">
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
								    <th>
								    <!-- col 2: Razón social -->
								    <div class="dx-like-filter" style="position:relative;">
								      <span class="op-btn" id="razonOpBtn"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <input type="text" id="razonFilterInput" placeholder="Filtrar razón social..."/>
								      <div class="dx-like-menu" id="razonOpMenu" role="menu">
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
								    <!-- col 3: Serie -->
									  <th>
									    <div class="dx-like-filter" style="position:relative;">
									      <span class="op-btn" id="serieOpBtn"><span class="op-label"><i class="fas fa-search"></i></span></span>
									      <input type="text" id="serieFilterInput" placeholder="Filtrar serie..."/>
									      <div class="dx-like-menu" id="serieOpMenu" role="menu">
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
								     <!-- col 4: Tipo de comprobante -->
									  <th>
									    <div class="dx-like-filter" style="position:relative;">
									      <span class="op-btn" id="tipoOpBtn"><span class="op-label">=</span></span>
									      <select id="tipoFilterInput" class="form-select form-select-sm">
									        <option value="">Todos</option>
									        <option value="I">I - Ingreso</option>
									        <option value="P">P - Pago</option>
									        <option value="E">E - Egreso</option>
									        <option value="T">T - Traslado</option>
									      </select>
									      	<input type="hidden" id="tipoOpMenu" value="contains">
									    </div>
									  </th>
								    <!-- FOLIO (numérico) -->
									<th>
									  <div class="dx-like-filter" style="position:relative;">
									    <span class="op-btn" id="folioOpBtn"><span class="op-label">=</span></span>
									    <input type="number" step="any" id="folioFilter1" placeholder="Folio...">
									    <input type="number" step="any" id="folioFilter2" placeholder="y..." class="d-none" />
									    <div class="dx-like-menu" id="folioOpMenu" role="menu">
									      <ul>
									        <li data-op="eq">= Igual</li>
									        <li data-op="ne">≠ No igual</li>
									        <li data-op="lt">&lt; Menor que</li>
									        <li data-op="gt">&gt; Mayor que</li>
									        <li data-op="le">≤ Menor o igual</li>
									        <li data-op="ge">≥ Mayor o igual</li>
									        <li data-op="between">↦ Entre</li>
									        <li data-op="reset">🔄 Reset</li>
									      </ul>
									    </div>
									  </div>
									  <input type="hidden" id="folioOperator" value="eq">
									</th>
								    <!-- TOTAL -->
									<th>
									  <div class="dx-like-filter" style="position:relative;">
									    <span class="op-btn" id="totalOpBtn"><span class="op-label">=</span></span>
									    <input type="number" step="any" id="totalFilter1" placeholder="Total...">
									    <input type="number" step="any" id="totalFilter2" placeholder="y..." class="d-none" />
									    <div class="dx-like-menu" id="totalOpMenu" role="menu">
									      <ul>
									        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
									        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
									        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
									        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
									      </ul>
									    </div>
									  </div>
									  <input type="hidden" id="totalOperator" value="eq">
									</th>
								   <!-- SUBTOTAL -->
									<th>
									  <div class="dx-like-filter" style="position:relative;">
									    <span class="op-btn" id="subOpBtn"><span class="op-label">=</span></span>
									    <input type="number" step="any" id="subFilter1" placeholder="Sub-total...">
									    <input type="number" step="any" id="subFilter2" placeholder="y..." class="d-none" />
									    <div class="dx-like-menu" id="subOpMenu" role="menu">
									      <ul>
									        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
									        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
									        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
									        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
									      </ul>
									    </div>
									  </div>
									  <input type="hidden" id="subOperator" value="eq">
									</th>
																	    
								    <!-- IVA -->
									<th>
									  <div class="dx-like-filter" style="position:relative;">
									    <span class="op-btn" id="ivaOpBtn"><span class="op-label">=</span></span>
									    <input type="number" step="any" id="ivaFilter1" placeholder="IVA...">
									    <input type="number" step="any" id="ivaFilter2" placeholder="y..." class="d-none" />
									    <div class="dx-like-menu" id="ivaOpMenu" role="menu">
									      <ul>
									        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
									        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
									        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
									        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
									      </ul>
									    </div>
									  </div>
									  <input type="hidden" id="ivaOperator" value="eq">
									</th>
								    <!-- IVA RET -->
										<th>
										  <div class="dx-like-filter" style="position:relative;">
										    <span class="op-btn" id="ivaRetOpBtn"><span class="op-label">=</span></span>
										    <input type="number" step="any" id="ivaRetFilter1" placeholder="IVA Ret...">
										    <input type="number" step="any" id="ivaRetFilter2" placeholder="y..." class="d-none" />
										    <div class="dx-like-menu" id="ivaRetOpMenu" role="menu">
										      <ul>
										        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
										        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
										        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
										        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
										      </ul>
										    </div>
										  </div>
										  <input type="hidden" id="ivaRetOperator" value="eq">
										</th>
								    <!-- ISR RET -->
									<th>
									  <div class="dx-like-filter" style="position:relative;">
									    <span class="op-btn" id="isrOpBtn"><span class="op-label">=</span></span>
									    <input type="number" step="any" id="isrFilter1" placeholder="ISR Ret...">
									    <input type="number" step="any" id="isrFilter2" placeholder="y..." class="d-none" />
									    <div class="dx-like-menu" id="isrOpMenu" role="menu">
									      <ul>
									        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
									        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
									        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
									        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
									      </ul>
									    </div>
									  </div>
									  <input type="hidden" id="isrOperator" value="eq">
									</th>
								    <!-- IMP. LOCALES -->
									<th>
									  <div class="dx-like-filter" style="position:relative;">
									    <span class="op-btn" id="impLocOpBtn"><span class="op-label">=</span></span>
									    <input type="number" step="any" id="impLocFilter1" placeholder="Imp. locales...">
									    <input type="number" step="any" id="impLocFilter2" placeholder="y..." class="d-none" />
									    <div class="dx-like-menu" id="impLocOpMenu" role="menu">
									      <ul>
									        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
									        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
									        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
									        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
									      </ul>
									    </div>
									  </div>
									  <input type="hidden" id="impLocOperator" value="eq">
									</th>
									
								    <th></th>
								      <th></th>
								      <!-- col 14: UUID (ajusta índice según tu tabla) -->
										  <th>
										    <div class="dx-like-filter" style="position:relative;">
										      <span class="op-btn" id="uuidOpBtn"><span class="op-label"><i class="fas fa-search"></i></span></span>
										      <input type="text" id="uuidFilterInput" placeholder="Filtrar UUID..."/>
										      <div class="dx-like-menu" id="uuidOpMenu" role="menu">
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
								<!-- Filtro FECHA FACTURA
								<th>
								  <div class="dx-like-filter" style="position:relative; min-width: 260px;">
								    <span class="op-btn" id="dateOpBtn"><span class="op-label">=</span></span>
								
								    <input type="date" id="dateFilter1" />
								    <input type="date" id="dateFilter2" style="display:none; margin-left:6px;" />
								
								    <div class="dx-like-menu" id="dateOpMenu" role="menu">
								      <ul>
								        <li data-op="eq">= Igual</li>
								        <li data-op="ne">≠ No igual</li>
								        <li data-op="lt">&lt; Menor que</li>
								        <li data-op="gt">&gt; Mayor que</li>
								        <li data-op="le">≤ Menor o igual</li>
								        <li data-op="ge">≥ Mayor o igual</li>
								        <li data-op="bt">⟲ Entre</li>
								        <li data-op="reset">🔄 Reset</li>
								      </ul>
								    </div>
								  </div>
								
								  <!-- hidden para enviar al backend 
								  <input type="hidden" id="dateOperator" value="eq">
								</th> --> 
								<!-- Fecha Factura (controlado por #CRMDateRangeR) -->
								<th>
								  <input type="hidden" id="dateOperator" value="eq">
								  <input type="hidden" id="dateFilter1">
								  <input type="hidden" id="dateFilter2">
								  <!-- opcional, placeholder por si algún JS toca el label -->
								  <span id="dateOpBtn" class="d-none"><span class="op-label">=</span></span>
								</th>

							  
						</thead>
					</table>
					
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->



<form action="/siarex247/jsp/estatusXML/boveda/recibidos/mostrarBoveda.jsp" name="frmBoveda" id="frmBoveda" target="_blank" method="post">
   <input type="hidden" name="f" value="" id="idRegistroP">
   <input type="hidden" name="t" value="" id="tipoArchivoP">
</form>

<form action="/siarex247/excel/descargarBoveda.action" name="frmBovedaZIP" id="frmBovedaZIP" target="_blank" method="post">
   <input type="hidden" name="rfc" value="" id="rfcZIP">
   <input type="hidden" name="razonSocial" value="" id="razonSocialZIP">
   <input type="hidden" name="folio" value="" id="folioZIP">
   <input type="hidden" name="serie" value="" id="serieZIP">
   <input type="hidden" name="fechaInicial" value="" id="fechaInicialZIP">
   <input type="hidden" name="fechaFinal" value="" id="fechaFinalZIP">
   <input type="hidden" name="tipoComprobante" value="" id="tipoComprobanteZIP">
   <input type="hidden" name="uuid" value="" id="uuidBovedaZIP">
   <input type="hidden" name="bandSelecciono" value="" id="bandSeleccionoZIP">
   <input type="hidden" name="idRegistro" value="" id="idRegistroZIP">
</form>

<form action="/siarex247/excel/exportXMLaEXCEL.action" name="frmBovedaXMLExcel" id="frmBovedaXMLExcel" target="_blank" method="post">
   <input type="hidden" name="rfc" value="" id="rfcXMLExcel">
   <input type="hidden" name="razonSocial" value="" id="razonSocialXMLExcel">
   <input type="hidden" name="folio" value="" id="folioXMLExcel">
   <input type="hidden" name="serie" value="" id="serieXMLExcel">
   <input type="hidden" name="fechaInicial" value="" id="fechaInicial_ConvXML">
   <input type="hidden" name="fechaFinal" value="" id="fechaFinal_ConvXML">
   <input type="hidden" name="tipoComprobante" value="" id="tipoComprobanteXMLExcel">
   <input type="hidden" name="uuid" value="" id="uuidBovedaXMLExcel">
   <input type="hidden" name="bandSelecciono" value="" id="bandSeleccionoXMLExcel">
   <input type="hidden" name="idRegistro" value="" id="idRegistroXMLExcel">
   <input type="hidden" name="bandFiltros" value="false" id="bandFiltros">
   
</form>


<form action="/siarex247/excel/exportExcelBovedaXML.action"
      name="frmExportarDetalleExcel"
      id="frmExportarDetalleExcel"
      target="_blank"
      method="post">

  <!-- Básicos (tus mismos IDs/names) -->
  <input type="hidden" name="rfc"             id="rfc_Exportar">
  <input type="hidden" name="razonSocial"     id="razonSocial_Exportar">
  <input type="hidden" name="folio"           id="folio_Exportar">
  <input type="hidden" name="serie"           id="serie_Exportar">
  <input type="hidden" name="fechaInicial"    id="fechaInicial_Exportar">
  <input type="hidden" name="fechaFinal"      id="fechaFinal_Exportar">
  <input type="hidden" name="tipoComprobante" id="tipoComprobante_Exportar">
  <input type="hidden" name="uuid"            id="uuid_Exportar">

  <!-- (Opcional) Operadores DX-like; si tu Action no los usa, puedes dejarlos -->
  <input type="hidden" name="rfcOperator"     id="rfcOperator_Exportar">
  <input type="hidden" name="razonOperator"   id="razonOperator_Exportar">
  <input type="hidden" name="serieOperator"   id="serieOperator_Exportar">
  <input type="hidden" name="tipoOperator"    id="tipoOperator_Exportar">
  <input type="hidden" name="uuidOperator"    id="uuidOperator_Exportar">

  <input type="hidden" name="dateOperator"    id="dateOperator_Exportar">
  <input type="hidden" name="dateV1"          id="dateV1_Exportar">
  <input type="hidden" name="dateV2"          id="dateV2_Exportar">

  <input type="hidden" name="folioOperator"   id="folioOperator_Exportar">
  <input type="hidden" name="folioV1"         id="folioV1_Exportar">
  <input type="hidden" name="folioV2"         id="folioV2_Exportar">

  <input type="hidden" name="totalOperator"   id="totalOperator_Exportar">
  <input type="hidden" name="totalV1"         id="totalV1_Exportar">
  <input type="hidden" name="totalV2"         id="totalV2_Exportar">

  <input type="hidden" name="subOperator"     id="subOperator_Exportar">
  <input type="hidden" name="subV1"           id="subV1_Exportar">
  <input type="hidden" name="subV2"           id="subV2_Exportar">

  <input type="hidden" name="ivaOperator"     id="ivaOperator_Exportar">
  <input type="hidden" name="ivaV1"           id="ivaV1_Exportar">
  <input type="hidden" name="ivaV2"           id="ivaV2_Exportar">

  <input type="hidden" name="ivaRetOperator"  id="ivaRetOperator_Exportar">
  <input type="hidden" name="ivaRetV1"        id="ivaRetV1_Exportar">
  <input type="hidden" name="ivaRetV2"        id="ivaRetV2_Exportar">

  <input type="hidden" name="isrOperator"     id="isrOperator_Exportar">
  <input type="hidden" name="isrV1"           id="isrV1_Exportar">
  <input type="hidden" name="isrV2"           id="isrV2_Exportar">

  <input type="hidden" name="impLocOperator"  id="impLocOperator_Exportar">
  <input type="hidden" name="impLocV1"        id="impLocV1_Exportar">
  <input type="hidden" name="impLocV2"        id="impLocV2_Exportar">
</form>



 <div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
 <div class="modal fade bd-example-modal-lg" id="myModalVincular" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
 <div class="modal fade bd-example-modal-lg" id="myModalVincularComplemento" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
 <div class="modal fade" id="myModalNotifica_Recibidos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
 <div class="modal fade" id="modalDescargaCFDI" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>


<script type="text/javascript">
    var LABEL_BOVEDA_ETQ8 = null;
    var LABEL_BOVEDA_TEXT3 = null;
    var LABEL_BOVEDA_TEXT4 = null;
    var LABEL_BOVEDA_TEXT5 = null;
    var LABEL_BOVEDA_TEXT6 = null;
    
    var LABEL_BOVEDA_TEXT1 = null;
    var TITLE_DELETE_CATALOGO = null;
    
	$(document).ready(function() {
		
		$("#myModalDetalle").load('/siarex247/jsp/estatusXML/boveda/recibidos/modalBoveda.jsp');
		$("#myModalVincular").load('/siarex247/jsp/estatusXML/boveda/recibidos/modalVinComplementos.jsp');
		$("#myModalVincularComplemento").load('/siarex247/jsp/estatusXML/boveda/recibidos/modalVinComplementosBoveda.jsp');
		$("#myModalNotifica_Recibidos").load('/siarex247/jsp/estatusXML/boveda/recibidos/modalConfirm.jsp');
		$("#modalDescargaCFDI").load('/siarex247/jsp/estatusXML/boveda/recibidos/modalDescargaCFDI.jsp');
		
		$('#btnRefrescar').on('click', function(e) {
			validarFechas();
			
		});
		
		
		obtenerFechasFiltro();
		calcularEtiquetasBoveda();
		
	});
	
	
	
	function obtenerFechasMinima(fechaInicial, fechaFinal){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/consultarFechaMinima.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					const calendarioIni = flatpickr("#fechaInicial_Filtro", { 
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
					
					const calendarioFin = flatpickr("#fechaFinal_Filtro", { 
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
					
					 $('#fechaInicial_Filtro').val(fechaInicial);
					 $('#fechaFinal_Filtro').val(fechaFinal);
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasFiltro()_'+thrownError);
			}
		});	
				
	}
	
	

	 function calcularEtiquetasBoveda(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'BOVEDA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						//document.getElementById("BOVEDA_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("BOVEDA_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("BOVEDA_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("BOVEDA_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("BOVEDA_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("BOVEDA_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("BOVEDA_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("BOVEDA_ETQ7").innerHTML = data.ETQ7;
						// document.getElementById("BOVEDA_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("BOVEDA_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("BOVEDA_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("BOVEDA_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("BOVEDA_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("BOVEDA_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("BOVEDA_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("BOVEDA_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("BOVEDA_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("BOVEDA_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("BOVEDA_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("BOVEDA_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("BOVEDA_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("BOVEDA_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("BOVEDA_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("BOVEDA_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("BOVEDA_ETQ24").innerHTML = data.ETQ24;
						document.getElementById("BOVEDA_ETQ25").innerHTML = data.ETQ25;
						document.getElementById("BOVEDA_ETQ26").innerHTML = data.ETQ26;
						document.getElementById("BOVEDA_ETQ27").innerHTML = data.ETQ27;
						
						document.getElementById("BOVEDA_ETQ49").innerHTML = data.ETQ12;
						document.getElementById("BOVEDA_ETQ50").innerHTML = data.ETQ13;
						document.getElementById("BOVEDA_ETQ51").innerHTML = data.ETQ25;
						document.getElementById("BOVEDA_ETQ52").innerHTML = data.ETQ15;
						document.getElementById("BOVEDA_ETQ53").innerHTML = data.ETQ14;
						document.getElementById("BOVEDA_ETQ54").innerHTML = data.ETQ16;
						
						document.getElementById("BOVEDA_BTN_LIMPIAR").innerHTML = BTN_LIMPIAR;
						
						LABEL_BOVEDA_ETQ8 = data.ETQ8;
						LABEL_BOVEDA_TEXT1 = data.TEXT1;
						TITLE_DELETE_CATALOGO = data.TEXT2;
						LABEL_BOVEDA_TEXT3 = data.TEXT3;
						LABEL_BOVEDA_TEXT4 = data.TEXT4;
						LABEL_BOVEDA_TEXT5 = data.TEXT5;
						
						consultarFecha();
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasBoveda()_1_'+thrownError);
				}
			});	
		}
	 
	 $('#tipoFilterInput').on('change', function(e) {
		 const v = ($(this).val()||'').trim();
		  $('#tipoOpMenu').val(v && v!=='ALL' ? 'equals' : 'contains');
		  // UI del botÃÂÃÂ³n: "=" cuando hay valor, lupa cuando TODOS
		  $('#tipoOpBtn .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
		 // window.__bvdRec_syncThead();
		  validarFechas();
			
	});

</script>

	<script>
	
			// ===================== TEXTOS: RFC, Razón, Serie, Tipo, UUID =====================
			function initDxLikeFilter({btnId, menuId, inputId, hiddenOpId, targetInput}) {
			  const $btn=$(btnId), $menu=$(menuId), $input=$(inputId), $hidden=$(hiddenOpId), $label=$btn.find('.op-label');
			  if(!$btn.length||!$menu.length||!$input.length||!$hidden.length) return;
			
			  if(!$hidden.val()) $hidden.val('contains');
			
			  // abrir/cerrar menú
			  $btn.on('click',e=>{ e.stopPropagation(); $menu.toggleClass('show'); });
			  $(document).on('click',()=> $menu.removeClass('show'));
			
			  function short(t){ return (t||'').trim().split(/\s+/)[0]; }
			
			  // elegir operador (NO dispara consulta)
			  $menu.on('click','li',function(){
			    const op=String($(this).data('op')||'').toLowerCase();
			    if(op==='reset'){
			      $hidden.val(hiddenOpId.includes('tipo')?'equals':'contains');
			      $label.html('<i class="fas fa-search"></i>');
			      $input.val('');
			    }else{
			      $hidden.val(op);
			      $label.text(short($(this).text()));
			    }
			    $menu.removeClass('show');
			    // 🔕 nada de refrescar aquí
			  });
			
			  // Solo ENTER aplica el filtro
			  function aplicar(){
				// tablaDetalleBoveda.page('first').draw('page');
			    //if(targetInput) $(targetInput).val(($input.val()||'').trim());
			    //if(typeof refrescarBoveda==='function') refrescarBoveda();
				// refrescarBoveda();
				validarFechas();
			  }
			  //$input.on('keydown',e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicar(); }});
			  const ns = '.dxenter';
			  $input.off('keydown' + ns).on('keydown' + ns, function (e) {
			    if (e.key === 'Enter') { e.preventDefault(); aplicar(); }
			  });
			}
			
			// ===================== NUMÉRICOS: Folio, Total, Sub, IVA, IVA Ret, ISR, ImpLocales =====================
			function initNumericDxFilter({btnId, menuId, v1Id, v2Id, opHiddenId}) {
			  const $btn=$(btnId), $menu=$(menuId), $v1=$(v1Id), $v2=$(v2Id), $op=$(opHiddenId), $label=$btn.find('.op-label');
			  if(!$btn.length||!$menu.length||!$v1.length||!$op.length) return;
			
			  if(!$op.val()) $op.val('eq'); // default
			  const symbol = {eq:'=', ne:'≠', lt:'<', gt:'>', le:'≤', ge:'≥', between:'↦'};
			
			  function toggleSecond(){
			    const isBetween = ($op.val()||'').toLowerCase()==='between';
			    if(isBetween){ $v2.removeClass('d-none'); } else { $v2.addClass('d-none'); $v2.val(''); }
			  }
			
			  $btn.on('click',e=>{ e.stopPropagation(); $menu.toggleClass('show'); });
			  $(document).on('click',()=> $menu.removeClass('show'));
			
			  // elegir operador (NO refresca)
			  $menu.on('click','li',function(){
			    const op = String($(this).data('op')||'eq').toLowerCase();
			    if(op==='reset'){
			      $op.val('eq'); $label.text('='); $v1.val(''); $v2.val('');
			    }else{
			      $op.val(op); $label.text(symbol[op]||'=');
			    }
			    $menu.removeClass('show');
			    toggleSecond();
			    // 🔕 nada de refrescar aquí
			  });
			
			  // Solo ENTER aplica
			  // const aplicar = ()=>{ if(typeof refrescarBoveda==='function') {  alert('***1111****'); refrescarBoveda(); } };
			  
			//  $v1.on('keydown',e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicar(); }});
			//  $v2.on('keydown',e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicar(); }});
			const ns = '.dxenter';
			$v1.off('keydown' + ns).on('keydown' + ns, function (e) {
			  if (e.key === 'Enter') { e.preventDefault(); validarFechas(); }
			});
			$v2.off('keydown' + ns).on('keydown' + ns, function (e) {
			  if (e.key === 'Enter') { e.preventDefault();  validarFechas();}
			});
			
			  toggleSecond();
			}
			
			// ===================== FECHA con operador =====================
			function initDxLikeDateFilter({btnId, menuId, input1Id, input2Id, hiddenOpId}) {
			  const $btn=$(btnId), $menu=$(menuId), $d1=$(input1Id), $d2=$(input2Id), $hidden=$(hiddenOpId), $label=$btn.find('.op-label');
			  if(!$btn.length||!$menu.length||!$d1.length||!$hidden.length) return;
			
			  if(!$hidden.val()) $hidden.val('eq');
			  const sym = {eq:'=', ne:'≠', lt:'<', gt:'>', le:'≤', ge:'≥', bt:'↔'};
			
			  function showSecond(show){ if(show){ $d2.show(); } else { $d2.hide().val(''); } }
			
			  $btn.on('click', e=>{ e.stopPropagation(); $menu.toggleClass('show'); });
			  $(document).on('click', ()=> $menu.removeClass('show'));
			
			  // elegir operador (NO refresca)
			  $menu.on('click','li',function(){
			    const op = String($(this).data('op')||'').toLowerCase();
			    if(op==='reset'){
			      $hidden.val('eq'); $label.text('=');
			      $d1.val(''); showSecond(false);
			    }else{
			      $hidden.val(op);
			      $label.text(sym[op]||op);
			      showSecond(op==='bt');
			    }
			    $menu.removeClass('show');
			    // 🔕 nada de refrescar aquí
			  });
			
			  // Solo ENTER aplica
			  const aplicarFecha = ()=>{ if(typeof refrescarBoveda==='function') { validarFechas(); }};
			  $d1.on('keydown', e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicarFecha(); }});
			  $d2.on('keydown', e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicarFecha(); }});
			}
			
			// ===================== INIT de todos los filtros =====================
			$(function(){
			  // Textos
			  initDxLikeFilter({ btnId:'#rfcOpBtn',   menuId:'#rfcOpMenu',   inputId:'#rfcFilterInput',   hiddenOpId:'#rfcOperator',   targetInput:'#rfc_Recibidos' });
			  initDxLikeFilter({ btnId:'#razonOpBtn', menuId:'#razonOpMenu', inputId:'#razonFilterInput', hiddenOpId:'#razonOperator', targetInput:'#razonSocial_Recibidos' });
			  initDxLikeFilter({ btnId:'#serieOpBtn', menuId:'#serieOpMenu', inputId:'#serieFilterInput', hiddenOpId:'#serieOperator', targetInput:'#serie_Recibidos' });
			  initDxLikeFilter({ btnId:'#tipoOpBtn',  menuId:'#tipoOpMenu',  inputId:'#tipoFilterInput',  hiddenOpId:'#tipoOperator',  targetInput:'#tipoComprobante_Recibidos' });
			  initDxLikeFilter({ btnId:'#uuidOpBtn',  menuId:'#uuidOpMenu',  inputId:'#uuidFilterInput',  hiddenOpId:'#uuidOperator',  targetInput:'#uuid_Recibidos' });
			
			  // Numéricos
			  initNumericDxFilter({ btnId:'#folioOpBtn',   menuId:'#folioOpMenu',   v1Id:'#folioFilter1',   v2Id:'#folioFilter2',   opHiddenId:'#folioOperator' });
			  initNumericDxFilter({ btnId:'#totalOpBtn',   menuId:'#totalOpMenu',   v1Id:'#totalFilter1',   v2Id:'#totalFilter2',   opHiddenId:'#totalOperator' });
			  initNumericDxFilter({ btnId:'#subOpBtn',     menuId:'#subOpMenu',     v1Id:'#subFilter1',     v2Id:'#subFilter2',     opHiddenId:'#subOperator' });
			  initNumericDxFilter({ btnId:'#ivaOpBtn',     menuId:'#ivaOpMenu',     v1Id:'#ivaFilter1',     v2Id:'#ivaFilter2',     opHiddenId:'#ivaOperator' });
			  initNumericDxFilter({ btnId:'#ivaRetOpBtn',  menuId:'#ivaRetOpMenu',  v1Id:'#ivaRetFilter1',  v2Id:'#ivaRetFilter2',  opHiddenId:'#ivaRetOperator' });
			  initNumericDxFilter({ btnId:'#isrOpBtn',     menuId:'#isrOpMenu',     v1Id:'#isrFilter1',     v2Id:'#isrFilter2',     opHiddenId:'#isrOperator' });
			  initNumericDxFilter({ btnId:'#impLocOpBtn',  menuId:'#impLocOpMenu',  v1Id:'#impLocFilter1',  v2Id:'#impLocFilter2',  opHiddenId:'#impLocOperator' });
			
			  // Fecha
			  initDxLikeDateFilter({
			    btnId:'#dateOpBtn', menuId:'#dateOpMenu',
			    input1Id:'#dateFilter1', input2Id:'#dateFilter2',
			    hiddenOpId:'#dateOperator'
			  });
			
			});
			
			</script>
			
</html>