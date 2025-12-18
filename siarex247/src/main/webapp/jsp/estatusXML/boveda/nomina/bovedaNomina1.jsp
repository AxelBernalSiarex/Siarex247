<%@page import="com.siarex247.utils.Utils"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html charset="utf-8">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

 	   <script src='/siarex247/jsp/estatusXML/boveda/nomina/bovedaNomina.js?v=<%=Utils.VERSION%>'></script>
 
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
			#tablaDetalleNomina thead tr.filters th{ position:relative; overflow:visible; }
			
			
			/* Ancho recomendado para SERIE (columna id=BOVEDA_NOMINA_ETQ18) */
			#tablaDetalleNomina th#BOVEDA_NOMINA_ETQ18, #tablaDetalleNomina td:nth-child(4){ min-width: 180px; }
			
			
			.d-none { display:none; }
			</style>
			
				
			<style>
			  /* deja salir el menÃÂº desde el thead */
			  #tablaDetalleNomina thead tr.filters th { position: relative; overflow: visible !important; }
			  table.dataTable thead th, table.dataTable thead td { overflow: visible !important; }
			  .dx-like-menu { z-index: 20000; }
			  /* si usas FixedHeader */
			  .dtfh-floatingparent thead th { overflow: visible !important; }
			</style>
			
			<style>
  #tablaDetalleNomina thead .dx-like-menu {
    position: absolute;
    z-index: 1090; /* mayor que tooltips/modals si hace falta */
  }
</style>
 	   
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="BOVEDA_NOMINA_ETQ7">Fecha de Ultima Actualizaci&oacute;n : </h5>
			</div>
			<div class="col-auto d-flex">
			   
				<div class="dropdown font-sans-serif" style="position: relative; z-index: 100000;">
					<button class="btn btn-outline-secondary btn-sm btnClr" type="button" id="btnLimpiar_Nomina" onclick="limpiarNomina();">
						<span class="fas fa-broom me-1"></span>
						<span class="d-none d-sm-inline-block">Limpiar</span>
					</button>
					<button class="btn btn-falcon-success btn-sm" type="button" id="btnRefrescar_NominaCFDI">
						<span class="fab fa-firefox-browser me-1"></span>
						<span class="d-none d-sm-inline-block">Actualizar</span>
					</button>
					<button class="btn btn-outline-secondary btn-sm " type="button" id="btnExport_Nomina" onclick="exportExcelNomina();">
						<span class="fas fa-external-link-alt me-1"></span>
						<span class="d-none d-sm-inline-block">Export</span>
					</button>
					
					<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
						<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="BOVEDA_NOMINA_ETQ1">Opciones</span>
					</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:abreModal_Nomina('nuevo');" id="BOVEDA_NOMINA_ETQ2">Cargar XML</a>

					<div class="dropdown dropend">
						<a class="dropdown-item dropdown-toggle" href="#" id="dropdown-itmes" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Convertir XML a Excel</a>
						
						<div class="dropdown-menu" aria-labelledby="dropdown-itmes">
							<a class="dropdown-item" href="javascript:reporteResumen();" id="BOVEDA_NOMINA_ETQ3">Resumen de XML Nomina</a>
							<a class="dropdown-item" href="javascript:reporteDetalle();" id="BOVEDA_NOMINA_ETQ4">Detalle de XML Nomina</a>
							<a class="dropdown-item" href="javascript:reporteNomina();" id="BOVEDA_NOMINA_ETQ100">Reporte Nomina XML</a>
						</div>
						
						
					</div>
					
					<a class="dropdown-item" href="javascript:consultarTotales();" id="BOVEDA_NOMINA_ETQ5">Descargar CFDI</a>
					<div class="dropdown-divider"></div>
					<a class="dropdown-item text-danger" href="javascript:eliminaBovedaNomina('MULTIPLE');" id="BOVEDA_NOMINA_ETQ6">Eliminar Registro</a>
				</div>
			  </div>
			</div>
		</div>
	</div>
	
	<div class="card-header">
	
	
		<div class="mb-2 row">
				<label class="col-sm-2 col-form-label" for="fechaInicial_Nomina_Filtro" id="BOVEDA_NOMINA_ETQ10" >Fecha Inicio Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Nomina_Filtro" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>

				<label class="col-sm-2 col-form-label" for="fechaFinal_Nomina_Filtro" id="BOVEDA_NOMINA_ETQ13">Fecha Final Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Nomina_Filtro" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
	              
	              
        </div>
        
	</div>
		
	  
	</div>

<!-- 	
	<div class="card-header">
	  <div class="mb-2 row">
	     <label class="col-sm-2 col-form-label" for="fechaInicial_Nomina" id="BOVEDA_NOMINA_ETQ9">Fecha Inicio Factura</label>
          <div class="col-sm-2">
            <div class="form-group">
              <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Nomina" name="fechaInicial" type="text" placeholder="yyyy/mm/dd" data-options='{"disableMobile":true}' readonly="readonly"/>
            </div>
          </div>
          
          <label class="col-sm-2 col-form-label" for="fechaFinal_Nomina" style="text-align: right;" id="BOVEDA_NOMINA_ETQ10">Fecha Final Factura</label>
           <div class="col-sm-2">
             <div class="form-group">
               <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Nomina" name="fechaFinal" type="text" placeholder="yyyy/mm/dd" data-options='{"disableMobile":true}' readonly="readonly" />
             </div>
           </div>
	              
             <div class="col-sm-2">
				<div class="form-group">
			  	 <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="validarFechas_Nomina();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span> <span id="BOVEDA_NOMINA_ETQ11"> Refrescar </span> </button>
				</div>
			</div>
				
          
	  </div>
	</div>
-->

 <div class="collapse" id="filtrosBusquedaVisor">	
 
 <!-- 
 	<input type="hidden" name="razonSocial" id="razonSocial_Nomina" value="">
  -->	
	<div class="card-header">
	
	
		<div class="mb-2 row">
                <label class="col-sm-1 col-form-label" for="rfc_Nomina" id="BOVEDA_NOMINA_ETQ8">RFC Receptor</label>
                
                <div class="col-sm-2">
					<div class="form-group">
					   <input id="rfc_Nomina" name="rfc" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				
                <label class="col-sm-2 col-form-label" for="razonSocial_Nomina" id="BOVEDA_NOMINA_ETQ9">Raz&oacute;n Social Receptor</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="razonSocial_Nomina" name="razonSocial" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				  
				 
				 
				<label class="col-sm-2 col-form-label" for="fechaInicial_Nomina" id="BOVEDA_NOMINA_ETQ10_BK" >Fecha Factura Inicio</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Nomina" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
	              
        </div>
        
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="uuid_Nomina" id="BOVEDA_NOMINA_ETQ11">UUID</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="uuid_Nomina" name="uuid" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<label class="col-sm-2 col-form-label" for="folio_Nomina" id="BOVEDA_NOMINA_ETQ14">Folio</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="folio_Nomina" name="folio" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
	             <label class="col-sm-2 col-form-label" for="fechaFinal_Nomina" id="BOVEDA_NOMINA_ETQ13_BK">Fecha Factura Final</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Nomina" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>

					
        </div>
        
        <div class="mb-2 row">

				<label class="col-sm-1 col-form-label" for="serie_Nomina" id="BOVEDA_NOMINA_ETQ12">Serie</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="serie_Nomina" name="serie" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarBovedaNomina();" id="btnRefrescar_Nomina_respaldo" ><span class="fab fa-firefox-browser me-1"></span> <span id="BOVEDA_NOMINA_BTN_Refrescar"> Refrescar </span>  </button>
					   <button class="btn btn-falcon-secondary btn-sm mb-2 mb-sm-0" type="button" onclick="limpiarNomina();" id="btnLimipiar_Nomina" ><span class="fas fa-broom me-1"></span> <span id="BOVEDA_NOMINA_BTN_Limpiar"> Limpiar </span>  </button>
					</div>
				</div>
        </div>
	</div>
 </div>	
		
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important; " >
		<div class="tab-content" style="overflow: auto;">
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<div id="overSeccion_Boveda_Nomina" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					 <input type="hidden" id="rfcOperatorN"   value="contains" />
					  <input type="hidden" id="razonOperatorN" value="contains" />
					  <input type="hidden" id="serieOperatorN" value="contains" />
					  <input type="hidden" id="uuidOperatorN"  value="contains" />
					
					  <input type="hidden" id="folioOperatorN" value="eq" />
					  <input type="hidden" id="totalOperatorN" value="eq" />
					  <input type="hidden" id="subOperatorN"   value="eq" />
					  <input type="hidden" id="descOperatorN"  value="eq" />
					  <input type="hidden" id="percOperatorN"  value="eq" />
					  <input type="hidden" id="dedOperatorN"   value="eq" />
					
					  <input type="hidden" id="dateOperatorN"  value="eq" />
					<table id="tablaDetalleNomina"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ15">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ16">RFC Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ17">Raz&oacute;n Social Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ18">Serie</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ19">Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ20">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ21">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ22">Descuento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ23">Total Percepciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ24">Total Deducciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ25">XML</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ26">PDF</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ27">UUID</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BOVEDA_NOMINA_ETQ28">Fecha Factura</th>
								
							</tr>
<!-- Filtros -->
					   <tr class="filters">
  <!-- 1: Acciones -->
  <th></th>

  <!-- 2: RFC Receptor (texto) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="rfcOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
						      <input type="text" id="rfcFilterInputN" placeholder="Filtrar RFC..." />
						      <div class="dx-like-menu" id="rfcOpMenuN">
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
						
						  <!-- 3: RazÃ³n Social Receptor (texto) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="razonOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
						      <input type="text" id="razonFilterInputN" placeholder="Filtrar razón social..." />
						      <div class="dx-like-menu" id="razonOpMenuN">
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
						
						  <!-- 4: Serie (texto) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="serieOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
						      <input type="text" id="serieFilterInputN" placeholder="Filtrar serie..." />
						      <div class="dx-like-menu" id="serieOpMenuN">
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
						
						  <!-- 5: Folio (numÃ©rico) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="folioOpBtnN"><span class="op-label">=</span></span>
						      <input type="number" step="any" id="folioFilter1N" placeholder="Folio..." />
						      <input type="number" step="any" id="folioFilter2N" placeholder="y..." class="d-none" />
						      <div class="dx-like-menu" id="folioOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="between">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						  </th>
						
						  <!-- 6: Total (numÃ©rico) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="totalOpBtnN"><span class="op-label">=</span></span>
						      <input type="number" step="any" id="totalFilter1N" placeholder="Total..." />
						      <input type="number" step="any" id="totalFilter2N" placeholder="y..." class="d-none" />
						      <div class="dx-like-menu" id="totalOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="between">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						  </th>
						
						  <!-- 7: Sub-Total (numÃ©rico) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="subOpBtnN"><span class="op-label">=</span></span>
						      <input type="number" step="any" id="subFilter1N" placeholder="Sub-total..." />
						      <input type="number" step="any" id="subFilter2N" placeholder="y..." class="d-none" />
						      <div class="dx-like-menu" id="subOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="between">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						  </th>
						
						  <!-- 8: Descuento (numÃ©rico) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="descOpBtnN"><span class="op-label">=</span></span>
						      <input type="number" step="any" id="descFilter1N" placeholder="Descuento..." />
						      <input type="number" step="any" id="descFilter2N" placeholder="y..." class="d-none" />
						      <div class="dx-like-menu" id="descOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="between">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						  </th>
						
						  <!-- 9: Total Percepciones (numÃ©rico) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="percOpBtnN"><span class="op-label">=</span></span>
						      <input type="number" step="any" id="percFilter1N" placeholder="Percepciones..." />
						      <input type="number" step="any" id="percFilter2N" placeholder="y..." class="d-none" />
						      <div class="dx-like-menu" id="percOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="between">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						  </th>
						
						  <!-- 10: Total Deducciones (numÃ©rico) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="dedOpBtnN"><span class="op-label">=</span></span>
						      <input type="number" step="any" id="dedFilter1N" placeholder="Deducciones..." />
						      <input type="number" step="any" id="dedFilter2N" placeholder="y..." class="d-none" />
						      <div class="dx-like-menu" id="dedOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="between">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						  </th>
						
						  <!-- 11: XML -->
						  <th></th>
						  <!-- 12: PDF -->
						  <th></th>
						
						  <!-- 13: UUID (texto) -->
						  <th>
						    <div class="dx-like-filter">
						      <span class="op-btn" id="uuidOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
						      <input type="text" id="uuidFilterInputN" placeholder="Filtrar UUID..." />
						      <div class="dx-like-menu" id="uuidOpMenuN">
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
						
						  <!-- 14: Fecha Factura (fecha con operador
						  <th>
						    <div class="dx-like-filter" style="min-width:260px;">
						      <span class="op-btn" id="dateOpBtnN"><span class="op-label">=</span></span>
						      <input type="date" id="dateFilter1N" />
						      <input type="date" id="dateFilter2N" class="d-none" />
						      <div class="dx-like-menu" id="dateOpMenuN">
						        <ul>
						          <li data-op="eq">= Igual</li>
						          <li data-op="ne">&ne; No igual</li>
						          <li data-op="lt">&lt; Menor que</li>
						          <li data-op="gt">&gt; Mayor que</li>
						          <li data-op="le">&le; Menor o igual</li>
						          <li data-op="ge">&ge; Mayor o igual</li>
						          <li data-op="bt">&#8596; Entre</li>
						          <li data-op="reset">⟲ Reset</li>
						        </ul>
						      </div>
						    </div>
						    <input type="hidden" id="dateOperatorN" value="eq">
						  </th>) -->
						  
						  
						  
						  
						  		<th>
								  <input type="hidden" id="dateOperatorN" value="eq">
								  <input type="hidden" id="dateFilter1N">
								  <input type="hidden" id="dateFilter2N">
								  <!-- opcional, placeholder por si algún JS toca el label -->
								  <span id="dateOpBtn" class="d-none"><span class="op-label">=</span></span>
								</th>
						</tr>

						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<form action="/siarex247/jsp/estatusXML/boveda/nomina/mostrarBovedaNomina.jsp" name="frmBovedaNomina" id="frmBovedaNomina" target="_blank" method="post">
   <input type="hidden" name="f" value="" id="idRegistroP_Nomina">
   <input type="hidden" name="t" value="" id="tipoArchivoP_Nomina">
</form>

<form action="/siarex247/excel/descargarBovedaNominaZIP.action" name="frmBovedaNominaZIP" id="frmBovedaNominaZIP" target="_blank" method="post">
   <input type="hidden" name="rfc" value="" id="rfcZIP_Nomina">
   <input type="hidden" name="razonSocial" value="" id="razonSocialZIP_Nomina">
   <input type="hidden" name="folio" value="" id="folioZIP_Nomina">
   <input type="hidden" name="serie" value="" id="serieZIP_Nomina">
   <input type="hidden" name="fechaInicial" value="" id="fechaInicialZIP_Nomina">
   <input type="hidden" name="fechaFinal" value="" id="fechaFinalZIP_Nomina">
   <input type="hidden" name="uuid" value="" id="uuidBovedaZIP_Nomina">
   <input type="hidden" name="idRegistro" value="" id="idRegistroZIP_Nomina">
</form>



<form action="/siarex247/excel/exportExcelBovedaNomina.action"
      name="frmExportarDetalleExcel_Nomina"
      id="frmExportarDetalleExcel_Nomina"
      target="_blank"
      method="post">

  <!-- Básicos -->
  <input type="hidden" name="rfc"          id="rfc_Nomina_Exportar">
  <input type="hidden" name="razonSocial"  id="razonSocial_Nomina_Exportar">
  <input type="hidden" name="folio"        id="folio_Nomina_Exportar">
  <input type="hidden" name="serie"        id="serie_Nomina_Exportar">
  <input type="hidden" name="fechaInicial" id="fechaInicial_Nomina_Exportar">
  <input type="hidden" name="fechaFinal"   id="fechaFinal_Nomina_Exportar">
  <input type="hidden" name="uuid"         id="uuid_Nomina_Exportar">

  <!-- Operadores DX-like (texto) -->
  <input type="hidden" name="rfcOperator"    id="rfcOperator_Nomina_Exportar">
  <input type="hidden" name="razonOperator"  id="razonOperator_Nomina_Exportar">
  <input type="hidden" name="serieOperator"  id="serieOperator_Nomina_Exportar">
  <input type="hidden" name="uuidOperator"   id="uuidOperator_Nomina_Exportar">

  <!-- Fecha -->
  <input type="hidden" name="dateOperator"   id="dateOperator_Nomina_Exportar">
  <input type="hidden" name="dateV1"         id="dateV1_Nomina_Exportar">
  <input type="hidden" name="dateV2"         id="dateV2_Nomina_Exportar">

  <!-- Numéricos -->
  <input type="hidden" name="folioOperator"  id="folioOperator_Nomina_Exportar">
  <input type="hidden" name="folioV1"        id="folioV1_Nomina_Exportar">
  <input type="hidden" name="folioV2"        id="folioV2_Nomina_Exportar">

  <input type="hidden" name="totalOperator"  id="totalOperator_Nomina_Exportar">
  <input type="hidden" name="totalV1"        id="totalV1_Nomina_Exportar">
  <input type="hidden" name="totalV2"        id="totalV2_Nomina_Exportar">

  <input type="hidden" name="subOperator"    id="subOperator_Nomina_Exportar">
  <input type="hidden" name="subV1"          id="subV1_Nomina_Exportar">
  <input type="hidden" name="subV2"          id="subV2_Nomina_Exportar">

  <input type="hidden" name="descOperator"   id="descOperator_Nomina_Exportar">
  <input type="hidden" name="descV1"         id="descV1_Nomina_Exportar">
  <input type="hidden" name="descV2"         id="descV2_Nomina_Exportar">

  <input type="hidden" name="percOperator"   id="percOperator_Nomina_Exportar">
  <input type="hidden" name="percV1"         id="percV1_Nomina_Exportar">
  <input type="hidden" name="percV2"         id="percV2_Nomina_Exportar">

  <input type="hidden" name="dedOperator"    id="dedOperator_Nomina_Exportar">
  <input type="hidden" name="dedV1"          id="dedV1_Nomina_Exportar">
  <input type="hidden" name="dedV2"          id="dedV2_Nomina_Exportar">
</form>







<form action="/siarex247/excel/reporteResumen.action" name="frmNominaResumen" id="frmNominaResumen" target="_blank" method="post">
   <!-- Básicos -->
  <input type="hidden" id="rfc_Nomina_Resumen"          name="rfc"/>
  <input type="hidden" id="razonSocial_Nomina_Resumen"  name="razonSocial"/>
  <input type="hidden" id="folio_Nomina_Resumen"        name="folio"/>
  <input type="hidden" id="serie_Nomina_Resumen"        name="serie"/>
  <input type="hidden" id="fechaInicial_Nomina_Resumen" name="fechaInicial"/>
  <input type="hidden" id="fechaFinal_Nomina_Resumen"   name="fechaFinal"/>
  <input type="hidden" id="uuid_Nomina_Resumen"         name="uuid"/>
  <input type="hidden" id="idRegistroResumen_Nomina"    name="idRegistro"/>

  <!-- Operadores / valores -->
  <input type="hidden" id="rfcOperator_Nomina_Resumen"    name="rfcOperator"/>
  <input type="hidden" id="razonOperator_Nomina_Resumen"  name="razonOperator"/>
  <input type="hidden" id="serieOperator_Nomina_Resumen"  name="serieOperator"/>
  <input type="hidden" id="uuidOperator_Nomina_Resumen"   name="uuidOperator"/>

  <input type="hidden" id="dateOperator_Nomina_Resumen"   name="dateOperator"/>
  <input type="hidden" id="dateV1_Nomina_Resumen"         name="dateV1"/>
  <input type="hidden" id="dateV2_Nomina_Resumen"         name="dateV2"/>

  <input type="hidden" id="folioOperator_Nomina_Resumen"  name="folioOperator"/>
  <input type="hidden" id="folioV1_Nomina_Resumen"        name="folioV1"/>
  <input type="hidden" id="folioV2_Nomina_Resumen"        name="folioV2"/>

  <input type="hidden" id="totalOperator_Nomina_Resumen"  name="totalOperator"/>
  <input type="hidden" id="totalV1_Nomina_Resumen"        name="totalV1"/>
  <input type="hidden" id="totalV2_Nomina_Resumen"        name="totalV2"/>

  <input type="hidden" id="subOperator_Nomina_Resumen"    name="subOperator"/>
  <input type="hidden" id="subV1_Nomina_Resumen"          name="subV1"/>
  <input type="hidden" id="subV2_Nomina_Resumen"          name="subV2"/>

  <input type="hidden" id="descOperator_Nomina_Resumen"   name="descOperator"/>
  <input type="hidden" id="descV1_Nomina_Resumen"         name="descV1"/>
  <input type="hidden" id="descV2_Nomina_Resumen"         name="descV2"/>

  <input type="hidden" id="percOperator_Nomina_Resumen"   name="percOperator"/>
  <input type="hidden" id="percV1_Nomina_Resumen"         name="percV1"/>
  <input type="hidden" id="percV2_Nomina_Resumen"         name="percV2"/>

  <input type="hidden" id="dedOperator_Nomina_Resumen"    name="dedOperator"/>
  <input type="hidden" id="dedV1_Nomina_Resumen"          name="dedV1"/>
  <input type="hidden" id="dedV2_Nomina_Resumen"          name="dedV2"/>
   
</form>


<form action="/siarex247/excel/reporteDetalle.action" name="frmNominaDetalle" id="frmNominaDetalle" target="_blank" method="post">
 <!-- Básicos -->
  <input type="hidden" id="rfc_Nomina_Detalle"          name="rfc"/>
  <input type="hidden" id="razonSocial_Nomina_Detalle"  name="razonSocial"/>
  <input type="hidden" id="folio_Nomina_Detalle"        name="folio"/>
  <input type="hidden" id="serie_Nomina_Detalle"        name="serie"/>
  <input type="hidden" id="fechaInicial_Nomina_Detalle" name="fechaInicial"/>
  <input type="hidden" id="fechaFinal_Nomina_Detalle"   name="fechaFinal"/>
  <input type="hidden" id="uuid_Nomina_Detalle"         name="uuid"/>
  <input type="hidden" id="idRegistroDetalle_Nomina"    name="idRegistro"/>

  <!-- Operadores / valores -->
  <input type="hidden" id="rfcOperator_Nomina_Detalle"    name="rfcOperator"/>
  <input type="hidden" id="razonOperator_Nomina_Detalle"  name="razonOperator"/>
  <input type="hidden" id="serieOperator_Nomina_Detalle"  name="serieOperator"/>
  <input type="hidden" id="uuidOperator_Nomina_Detalle"   name="uuidOperator"/>

  <input type="hidden" id="dateOperator_Nomina_Detalle"   name="dateOperator"/>
  <input type="hidden" id="dateV1_Nomina_Detalle"         name="dateV1"/>
  <input type="hidden" id="dateV2_Nomina_Detalle"         name="dateV2"/>

  <input type="hidden" id="folioOperator_Nomina_Detalle"  name="folioOperator"/>
  <input type="hidden" id="folioV1_Nomina_Detalle"        name="folioV1"/>
  <input type="hidden" id="folioV2_Nomina_Detalle"        name="folioV2"/>

  <input type="hidden" id="totalOperator_Nomina_Detalle"  name="totalOperator"/>
  <input type="hidden" id="totalV1_Nomina_Detalle"        name="totalV1"/>
  <input type="hidden" id="totalV2_Nomina_Detalle"        name="totalV2"/>

  <input type="hidden" id="subOperator_Nomina_Detalle"    name="subOperator"/>
  <input type="hidden" id="subV1_Nomina_Detalle"          name="subV1"/>
  <input type="hidden" id="subV2_Nomina_Detalle"          name="subV2"/>

  <input type="hidden" id="descOperator_Nomina_Detalle"   name="descOperator"/>
  <input type="hidden" id="descV1_Nomina_Detalle"         name="descV1"/>
  <input type="hidden" id="descV2_Nomina_Detalle"         name="descV2"/>

  <input type="hidden" id="percOperator_Nomina_Detalle"   name="percOperator"/>
  <input type="hidden" id="percV1_Nomina_Detalle"         name="percV1"/>
  <input type="hidden" id="percV2_Nomina_Detalle"         name="percV2"/>

  <input type="hidden" id="dedOperator_Nomina_Detalle"    name="dedOperator"/>
  <input type="hidden" id="dedV1_Nomina_Detalle"          name="dedV1"/>
  <input type="hidden" id="dedV2_Nomina_Detalle"          name="dedV2"/>
   
</form>

<form action="/siarex247/excel/reporteNomina.action" name="frmNominaReporte" id="frmNominaReporte" target="_blank" method="post">
    <!-- Básicos -->
  <input type="hidden" id="rfc_Nomina_Reporte"          name="rfc"/>
  <input type="hidden" id="razonSocial_Nomina_Reporte"  name="razonSocial"/>
  <input type="hidden" id="folio_Nomina_Reporte"        name="folio"/>
  <input type="hidden" id="serie_Nomina_Reporte"        name="serie"/>
  <input type="hidden" id="fechaInicial_Nomina_Reporte" name="fechaInicial"/>
  <input type="hidden" id="fechaFinal_Nomina_Reporte"   name="fechaFinal"/>
  <input type="hidden" id="uuid_Nomina_Reporte"         name="uuid"/>
  <input type="hidden" id="idRegistroReporte_Nomina"    name="idRegistro"/>

  <!-- Operadores / valores -->
  <input type="hidden" id="rfcOperator_Nomina_Reporte"    name="rfcOperator"/>
  <input type="hidden" id="razonOperator_Nomina_Reporte"  name="razonOperator"/>
  <input type="hidden" id="serieOperator_Nomina_Reporte"  name="serieOperator"/>
  <input type="hidden" id="uuidOperator_Nomina_Reporte"   name="uuidOperator"/>

  <input type="hidden" id="dateOperator_Nomina_Reporte"   name="dateOperator"/>
  <input type="hidden" id="dateV1_Nomina_Reporte"         name="dateV1"/>
  <input type="hidden" id="dateV2_Nomina_Reporte"         name="dateV2"/>

  <input type="hidden" id="folioOperator_Nomina_Reporte"  name="folioOperator"/>
  <input type="hidden" id="folioV1_Nomina_Reporte"        name="folioV1"/>
  <input type="hidden" id="folioV2_Nomina_Reporte"        name="folioV2"/>

  <input type="hidden" id="totalOperator_Nomina_Reporte"  name="totalOperator"/>
  <input type="hidden" id="totalV1_Nomina_Reporte"        name="totalV1"/>
  <input type="hidden" id="totalV2_Nomina_Reporte"        name="totalV2"/>

  <input type="hidden" id="subOperator_Nomina_Reporte"    name="subOperator"/>
  <input type="hidden" id="subV1_Nomina_Reporte"          name="subV1"/>
  <input type="hidden" id="subV2_Nomina_Reporte"          name="subV2"/>

  <input type="hidden" id="descOperator_Nomina_Reporte"   name="descOperator"/>
  <input type="hidden" id="descV1_Nomina_Reporte"         name="descV1"/>
  <input type="hidden" id="descV2_Nomina_Reporte"         name="descV2"/>

  <input type="hidden" id="percOperator_Nomina_Reporte"   name="percOperator"/>
  <input type="hidden" id="percV1_Nomina_Reporte"         name="percV1"/>
  <input type="hidden" id="percV2_Nomina_Reporte"         name="percV2"/>

  <input type="hidden" id="dedOperator_Nomina_Reporte"    name="dedOperator"/>
  <input type="hidden" id="dedV1_Nomina_Reporte"          name="dedV1"/>
  <input type="hidden" id="dedV2_Nomina_Reporte"          name="dedV2"/>
</form>


<div class="modal fade bd-example-modal-lg" id="myModalDetalle_Nomina" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade" id="myModalNotifica_Nomina" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>

<script type="text/javascript">
    var LABEL_BOVEDA_NOMINA_ETQ7 = null;
    
	$(document).ready(function() {
		
		$("#myModalDetalle_Nomina").load('/siarex247/jsp/estatusXML/boveda/nomina/modalBovedaNomina.jsp');
		$("#myModalNotifica_Nomina").load('/siarex247/jsp/estatusXML/boveda/nomina/modalConfirm.jsp');
		
		
		$('#btnRefrescar_NominaCFDI').on('click', function(e) {
			  
			/*
			  if (typeof window.__copiarTheadAGlobalesN === 'function') window.__copiarTheadAGlobalesN();
			  const $btn = $(this);
			  const html0 = $btn.html();

			  // Desactiva el botón y muestra el spinner
			  $btn.prop('disabled', true)
			      .html('<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Refrescando…');

			  // Recarga el DataTable
			  tablaDetalleNomina.page('first').draw('page');
			  tablaDetalleNomina.ajax.reload(function() {
			    $btn.prop('disabled', false).html(html0);
			  }, false);
			*/  
			validarFechas_Nomina();
		});
		
/*		
		flatpickr(fechaInicial_Nomina, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "Y-m-d",
	   	      locale: {
	   	        firstDayOfWeek: 1,
	   	        weekdays: {
	   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	   	          longhand: ['Domingo', 'Lunes', 'Martes', 'MiÃÂ©rcoles', 'Jueves', 'Viernes', 'SÃÂ¡bado'],         
	   	        }, 
	   	        months: {
	   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'ÃÂct', 'Nov', 'Dic'],
	   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
		
		
		flatpickr(fechaFinal_Nomina, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "Y-m-d",
	   	      locale: {
	   	        firstDayOfWeek: 1,
	   	        weekdays: {
	   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	   	          longhand: ['Domingo', 'Lunes', 'Martes', 'MiÃÂ©rcoles', 'Jueves', 'Viernes', 'SÃÂ¡bado'],         
	   	        }, 
	   	        months: {
	   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'ÃÂct', 'Nov', 'Dic'],
	   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
		*/
		
		obtenerFechasFiltroNomina();
		// cargaProveedoresNomina();
		calcularEtiquetasBovedaNomina();
	});


	function obtenerFechasMinimaNomina(fechaInicial, fechaFinal){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/nomina/consultarFechaMinima.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					const calendarioIni = flatpickr("#fechaInicial_Nomina_Filtro", { 
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
					
					const calendarioFin = flatpickr("#fechaFinal_Nomina_Filtro", { 
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
					
					 $('#fechaInicial_Nomina_Filtro').val(fechaInicial);
					 $('#fechaFinal_Nomina_Filtro').val(fechaFinal);
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasFiltro()_'+thrownError);
			}
		});	
				
	}
	
	
	
	(function($bs) {
		  const CLASS_NAME = 'has-child-dropdown-show';
		  $bs.Dropdown.prototype.toggle = function(_orginal) {
		    return function() {
		      document.querySelectorAll('.' + CLASS_NAME).forEach(function(e) {
		        e.classList.remove(CLASS_NAME);
		      });
		      let dd = this._element.closest('.dropdown').parentNode.closest('.dropdown');
		      for (; dd && dd !== document; dd = dd.parentNode.closest('.dropdown')) {
		        dd.classList.add(CLASS_NAME);
		      }
		      return _orginal.call(this);
		    }
		  }($bs.Dropdown.prototype.toggle);
		
		  document.querySelectorAll('.dropdown').forEach(function(dd) {
		    dd.addEventListener('hide.bs.dropdown', function(e) {
		      if (this.classList.contains(CLASS_NAME)) {
		        this.classList.remove(CLASS_NAME);
		        e.preventDefault();
		      }
		      e.stopPropagation(); // do not need pop in multi level mode
		    });
		  });

	  // for hover
	  document.querySelectorAll('.dropdown-hover, .dropdown-hover-all .dropdown').forEach(function(dd) {
	    dd.addEventListener('mouseenter', function(e) {
	      let toggle = e.target.querySelector(':scope>[data-bs-toggle="dropdown"]');
	      if (!toggle.classList.contains('show')) {
	        $bs.Dropdown.getOrCreateInstance(toggle).toggle();
	        dd.classList.add(CLASS_NAME);
	        $bs.Dropdown.clearMenus();
	      }
	    });
	    dd.addEventListener('mouseleave', function(e) {
	      let toggle = e.target.querySelector(':scope>[data-bs-toggle="dropdown"]');
	      if (toggle.classList.contains('show')) {
	        $bs.Dropdown.getOrCreateInstance(toggle).toggle();
	      }
	    });
	  });
	})(bootstrap);
	
	

	 function calcularEtiquetasBovedaNomina(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'BOVEDA_NOMINA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						//document.getElementById("BOVEDA_NOMINA_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("BOVEDA_NOMINA_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("BOVEDA_NOMINA_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("BOVEDA_NOMINA_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("BOVEDA_NOMINA_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("BOVEDA_NOMINA_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("BOVEDA_NOMINA_ETQ6").innerHTML = data.ETQ6;
						//document.getElementById("BOVEDA_NOMINA_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("BOVEDA_NOMINA_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("BOVEDA_NOMINA_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("BOVEDA_NOMINA_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("BOVEDA_NOMINA_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("BOVEDA_NOMINA_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("BOVEDA_NOMINA_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("BOVEDA_NOMINA_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("BOVEDA_NOMINA_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("BOVEDA_NOMINA_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("BOVEDA_NOMINA_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("BOVEDA_NOMINA_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("BOVEDA_NOMINA_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("BOVEDA_NOMINA_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("BOVEDA_NOMINA_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("BOVEDA_NOMINA_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("BOVEDA_NOMINA_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("BOVEDA_NOMINA_ETQ24").innerHTML = data.ETQ24;
						document.getElementById("BOVEDA_NOMINA_ETQ25").innerHTML = data.ETQ25;
						document.getElementById("BOVEDA_NOMINA_ETQ26").innerHTML = data.ETQ26;
						document.getElementById("BOVEDA_NOMINA_ETQ27").innerHTML = data.ETQ27;
						document.getElementById("BOVEDA_NOMINA_ETQ28").innerHTML = data.ETQ28;
						
						LABEL_BOVEDA_NOMINA_ETQ7 = data.ETQ7;
						
						document.getElementById("BOVEDA_NOMINA_BTN_Refrescar").innerHTML = BTN_REFRESCAR;
						document.getElementById("BOVEDA_NOMINA_BTN_Limpiar").innerHTML = BTN_LIMPIAR;
						
						consultarFechaNomina();
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasBoveda()_1_'+thrownError);
				}
			});	
		}
</script>

			<script>
			  $(function () {
			    // Texto
			    initDxLikeFilterN({ btnId:'#rfcOpBtnN',   menuId:'#rfcOpMenuN',   inputId:'#rfcFilterInputN',   hiddenOpId:'#rfcOperatorN' });
			    initDxLikeFilterN({ btnId:'#razonOpBtnN', menuId:'#razonOpMenuN', inputId:'#razonFilterInputN', hiddenOpId:'#razonOperatorN' });
			    initDxLikeFilterN({ btnId:'#serieOpBtnN', menuId:'#serieOpMenuN', inputId:'#serieFilterInputN', hiddenOpId:'#serieOperatorN' });
			    initDxLikeFilterN({ btnId:'#uuidOpBtnN',  menuId:'#uuidOpMenuN',  inputId:'#uuidFilterInputN',  hiddenOpId:'#uuidOperatorN' });
			
			    // NumÃ©ricos
			    initNumericDxFilterN({ btnId:'#folioOpBtnN', menuId:'#folioOpMenuN', v1Id:'#folioFilter1N', v2Id:'#folioFilter2N', opHiddenId:'#folioOperatorN' });
			    initNumericDxFilterN({ btnId:'#totalOpBtnN', menuId:'#totalOpMenuN', v1Id:'#totalFilter1N', v2Id:'#totalFilter2N', opHiddenId:'#totalOperatorN' });
			    initNumericDxFilterN({ btnId:'#subOpBtnN',   menuId:'#subOpMenuN',   v1Id:'#subFilter1N',   v2Id:'#subFilter2N',   opHiddenId:'#subOperatorN' });
			    initNumericDxFilterN({ btnId:'#descOpBtnN',  menuId:'#descOpMenuN',  v1Id:'#descFilter1N',  v2Id:'#descFilter2N',  opHiddenId:'#descOperatorN' });
			    initNumericDxFilterN({ btnId:'#percOpBtnN',  menuId:'#percOpMenuN',  v1Id:'#percFilter1N',  v2Id:'#percFilter2N',  opHiddenId:'#percOperatorN' });
			    initNumericDxFilterN({ btnId:'#dedOpBtnN',   menuId:'#dedOpMenuN',   v1Id:'#dedFilter1N',   v2Id:'#dedFilter2N',   opHiddenId:'#dedOperatorN' });
			
			    // Fecha
			    initDxLikeDateFilterN({ btnId:'#dateOpBtnN', menuId:'#dateOpMenuN', input1Id:'#dateFilter1N', input2Id:'#dateFilter2N', hiddenOpId:'#dateOperatorN' });
			  });
			</script>


			

</html>