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

	<script src="/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaEmitidos/descargaEmitidos.js?v=<%=Utils.VERSION%>"></script>
	
	<style>
  /* Filtros compactos */
  .dx-like-filter{display:flex;align-items:center;gap:6px;position:relative;}
  .dx-like-filter .op-btn{min-width:28px;height:28px;padding:0 6px;display:inline-flex;align-items:center;justify-content:center;cursor:pointer;border:1px solid #d0d5dd;border-radius:4px;background:#fff;line-height:1;}
  .dx-like-filter .op-label{font-size:12px;line-height:1;font-weight:600;}
  .dx-like-filter input{height:28px;line-height:28px;padding:0 8px;border:1px solid #d0d5dd;border-radius:4px;width:100%;}
  .dx-like-filter input[type="number"]{width:90px;}
  .dx-like-menu{position:absolute;z-index:1000;background:#fff;border:1px solid #e5e7eb;border-radius:6px;box-shadow:0 6px 20px rgba(0,0,0,.08);min-width:180px;display:none;top:32px;left:0;}
  .dx-like-menu.show{display:block;}
  .dx-like-menu ul{list-style:none;margin:0;padding:4px;}
  .dx-like-menu li{padding:6px 10px;cursor:pointer;border-radius:4px;white-space:nowrap;}
  .dx-like-menu li:hover{background:#f3f4f6;}

  /* thead + FixedHeader: permitir el menú */
  #tablaDetalleEmitidos_Descarga thead tr.filters th{position:relative;overflow:visible!important;}
  table.dataTable thead th,table.dataTable thead td{overflow:visible!important;}
  .dtfh-floatingparent thead th{overflow:visible!important;}
  #tablaDetalleEmitidos_Descarga thead .dx-like-menu{z-index:1090;}

  /* Anchos de columnas (ajusta ids si tu thead usa otros) */
  #tablaDetalleEmitidos_Descarga #CONF_DESCARGA_SAT_ETQ51 { width: 120px !important; min-width: 120px !important; } /* PAC */
  #tablaDetalleEmitidos_Descarga #CONF_DESCARGA_SAT_ETQ56 { width: 150px !important; min-width: 150px !important; } /* Estatus */
  #tablaDetalleEmitidos_Descarga #CONF_DESCARGA_SAT_ETQ58 { width: 150px !important; min-width: 150px !important; } /* Bóveda */
  
</style>
	
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE3">Descarga Masiva de XML Emitidos</h5>
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>
	
	




	  <div class="card-header">
	
	
		<div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="fechaInicial_Filtro" id="CONF_DESCARGA_SAT_ETQ32" >Fecha Inicio Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Emitidos" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>

				<label class="col-sm-1 col-form-label" for="fechaFinal_Filtro" id="CONF_DESCARGA_SAT_ETQ43">Fecha Final Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Emitidos" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>

	              
        </div>
        
        
	</div>
	
	
	
<div class="collapse" id="filtrosBusquedaVisor">	
	<div class="card-header">
	
	
	 
	
	
		<div class="mb-2 row">
                <label class="col-sm-1 col-form-label" for="rfcReceptor_Emitidos" id="CONF_DESCARGA_SAT_ETQ30">RFC Receptor</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="rfcReceptor_Emitidos" name="rfcReceptor" class="form-control" type="text"  value="" maxlength="15"  />
					</div>
				</div>
				<label class="col-sm-2 col-form-label" for="razonSocialReceptor_Emitidos" id="CONF_DESCARGA_SAT_ETQ31">Raz&oacute;n Social Receptor</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="razonSocialReceptor_Emitidos" name="razonSocialEmisor" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				
				<label class="col-sm-1 col-form-label" for="fechaInicial_Emitidos" id="CONF_DESCARGA_SAT_ETQ32_BK">Fecha Inicio</label>
	              <div class="col-sm-2">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Emitidos_BK" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
        </div>
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="existeBoveda_Emitidos" id="CONF_DESCARGA_SAT_ETQ33">Existe Boveda </label>
				<div class="col-sm-2">
					<div class="form-group">
					   <select id="existeBoveda_Emitidos" class="form-select"> 
					     		 <option value="ALL"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ34"> Todos </option>
                        		<option value="S"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ35"> SI </option>
                        		<option value="N"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ36"> NO </option>
					   </select>
					</div>
				</div>
				
        		<label class="col-sm-2 col-form-label" for="tipoComprobante_Emitidos" id="CONF_DESCARGA_SAT_ETQ37">Tipo de Comprobante </label>
				<div class="col-sm-3">
					<div class="form-group">
					   <select id="tipoComprobante_Emitidos" class="form-select"> 
					     		<option value="ALL"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ38"> Todos </option>
                        		<option value="I"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ39"> I - Ingreso</option>
                        		<option value="P"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ40"> P - Pago</option>
                        		<option value="E"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ41"> E - Egresos</option>
                        		<option value="T"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ42"> T - Translado</option>
					   </select>
					</div>
				</div>
				
				<label class="col-sm-1 col-form-label" for="fechaFinal_Emitidos" id="CONF_DESCARGA_SAT_ETQ43_BK">Fecha Final</label>
	              <div class="col-sm-2">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Emitidos_BK" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
					
        </div>
        <div class="mb-2 row">
        	   <label class="col-sm-1 col-form-label" for="estatusCFDI_Emitidos" id="CONF_DESCARGA_SAT_EstatusEmitidos">Estatus </label>
				<div class="col-sm-2">
					<div class="form-group">
					   <select id="estatusCFDI_Emitidos" class="form-select"> 
					     	<option value="ALL"  style="text-align: center;"> Todos </option>
                        	<option value="VIGENTE"  style="text-align: center;"> Vigentes </option>
                        	<option value="CANCELADO"  style="text-align: center;"> Cancelados </option>
					   </select>
					</div>
				</div>
          		<label class="col-sm-2 col-form-label" for="uuidDescarga_Emitidos" id="CONF_DESCARGA_SAT_ETQ44">UUID</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="uuidDescarga_Emitidos" name="uuidDescarga" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarEmitidos();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span>  <span id="CONF_DESCARGA_SAT_ETQ45" > Refrescar </span> </button>
					</div>
				</div>
        </div>
	</div>
 </div>	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<div id="overSeccion_Emitidos" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalleEmitidos_Descarga"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<!-- <th class="no-sort pe-1 align-middle data-table-row-action">Sel</th> -->
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ46">UUID</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ47">RFC del Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ48">Raz&oacute;n Social Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ49">RFC Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ50">Raz&oacute;n Social Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ51">Pac Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ52">Fecha Emision</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ53">Fecha Certificaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ54">Monto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ55">Efecto Comprobante</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ56">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ57">Fecha Cancelaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ58">Existe Boveda</th>
							</tr>
							<tr class="filters">
  <!-- 1: UUID -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="uuidOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <input type="text" id="uuidFilterInputE" placeholder="Filtrar UUID..." />
      <div class="dx-like-menu" id="uuidOpMenuE">
        <ul>
          <li data-op="contains">🔎 Contiene</li><li data-op="notContains">🚫 No contiene</li>
          <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
          <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
          <li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="uuidOperatorE" value="contains">
  </th>

  <!-- 2: RFC Emisor -->
  <th>
   
  </th>

  <!-- 3: Nombre Emisor -->
  <th>
    
  </th>

  <!-- 4: RFC Receptor -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="rfcRecOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <input type="text" id="rfcRecFilterInputE" placeholder="RFC receptor..." />
      <div class="dx-like-menu" id="rfcRecOpMenuE">
        <ul>
          <li data-op="contains">🔎 Contiene</li><li data-op="notContains">🚫 No contiene</li>
          <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
          <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
          <li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="rfcRecOperatorE" value="contains">
  </th>

  <!-- 5: Nombre Receptor -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="nomRecOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <input type="text" id="nomRecFilterInputE" placeholder="Nombre receptor..." />
      <div class="dx-like-menu" id="nomRecOpMenuE">
        <ul>
          <li data-op="contains">🔎 Contiene</li><li data-op="notContains">🚫 No contiene</li>
          <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
          <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
          <li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="nomRecOperatorE" value="contains">
  </th>

  <!-- 6: RFC PAC -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="pacOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <input type="text" id="pacFilterInputE" placeholder="RFC PAC..." />
      <div class="dx-like-menu" id="pacOpMenuE">
        <ul>
          <li data-op="contains">🔎 Contiene</li><li data-op="notContains">🚫 No contiene</li>
          <li data-op="startsWith">↤ Empieza con</li><li data-op="endsWith">↦ Termina con</li>
          <li data-op="equals">= Igual</li><li data-op="notEquals">≠ Distinto</li>
          <li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="pacOperatorE" value="contains">
  </th>

  <!-- 7: Fecha Emisión
  <th>
    <div class="dx-like-filter" style="min-width:260px;">
      <span class="op-btn" id="emiDateOpBtnE"><span class="op-label">=</span></span>
      <input type="date" id="emiDateFilter1E" />
      <input type="date" id="emiDateFilter2E" class="d-none" />
      <div class="dx-like-menu" id="emiDateOpMenuE">
        <ul>
          <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
          <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
          <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
          <li data-op="bt">↔ Entre</li><li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="emiDateOperatorE" value="eq">
  </th> -->
  
     						     <th>
								  <input type="hidden" id="emiDateOperatorE" value="eq">
								  <input type="hidden" id="emiDateFilter1E">
								  <input type="hidden" id="emiDateFilter2E">
								  <!-- opcional, placeholder por si algún JS toca el label -->
								  <span id="emiDateOpBtnE" class="d-none"><span class="op-label">=</span></span>
								</th>

  <!-- 8: Fecha Certificación -->
  <th>
    <div class="dx-like-filter" style="min-width:260px;">
      <span class="op-btn" id="cerDateOpBtnE"><span class="op-label">=</span></span>
      <input type="date" id="cerDateFilter1E" />
      <input type="date" id="cerDateFilter2E" class="d-none" />
      <div class="dx-like-menu" id="cerDateOpMenuE">
        <ul>
          <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
          <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
          <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
          <li data-op="bt">↔ Entre</li><li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="cerDateOperatorE" value="eq">
  </th>

  <!-- 9: Monto -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="montoOpBtnE"><span class="op-label">=</span></span>
      <input type="number" step="any" id="montoFilter1E" placeholder="Monto..." />
      <input type="number" step="any" id="montoFilter2E" placeholder="y..." class="d-none" />
      <div class="dx-like-menu" id="montoOpMenuE">
        <ul>
          <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
          <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
          <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
          <li data-op="between">↔ Entre</li><li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="montoOperatorE" value="eq">
  </th>

  <!-- 10: Efecto (I,P,T,E) -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="efectoOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <select id="efectoFilterInputE" class="form-select form-select-sm">
        <option value="">TODOS</option>
        <option value="I">I</option><option value="P">P</option>
        <option value="T">T</option><option value="E">E</option>
      </select>
      <input type="hidden" id="efectoOperatorE" value="contains">
    </div>
  </th>

  <!-- 11: Estatus -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="estatusOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <select id="estatusFilterE" class="form-select form-select-sm">
        <option value="ALL">TODOS</option>
        <option value="VIGENTE">VIGENTE</option>
        <option value="CANCELADO">CANCELADO</option>
      </select>
      <input type="hidden" id="estatusOperatorE" value="contains">
    </div>
  </th>

  <!-- 12: Fecha Cancelación -->
  <th>
    <div class="dx-like-filter" style="min-width:260px;">
      <span class="op-btn" id="canDateOpBtnE"><span class="op-label">=</span></span>
      <input type="date" id="canDateFilter1E" />
      <input type="date" id="canDateFilter2E" class="d-none" />
      <div class="dx-like-menu" id="canDateOpMenuE">
        <ul>
          <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
          <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
          <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
          <li data-op="bt">↔ Entre</li><li data-op="reset">🔄 Reset</li>
        </ul>
      </div>
    </div>
    <input type="hidden" id="canDateOperatorE" value="eq">
  </th>

  <!-- 13: En Bóveda -->
  <th>
    <div class="dx-like-filter">
      <span class="op-btn" id="bovedaOpBtnE"><span class="op-label"><i class="fas fa-search"></i></span></span>
      <select id="bovedaFilterE" class="form-select form-select-sm">
        <option value="ALL">TODOS</option>
        <option value="S">S</option>
        <option value="N">N</option>
      </select>
      <input type="hidden" id="bovedaOperatorE" value="contains">
    </div>
  </th>
</tr>
							
						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->


<form name="frmDescargaXMLExcelEmitidos"
      action="/siarex247/excel/exportExcelDescargaEmitidos.action"
      method="post" target="_blank">

  <!-- Básicos -->
  <input type="hidden" name="rfcReceptor"               id="rfcReceptorXLS_Emitidos">
  <input type="hidden" name="razonSocialReceptor"       id="razonSocialReceptorXLS_Emitidos">
  <input type="hidden" name="existeBovedaDescarga"      id="existeBovedaDescargaXLS_Emitidos">
  <input type="hidden" name="tipoComprobanteDescarga"   id="tipoComprobanteDescargaXLS_Emitidos">
  <input type="hidden" name="fechaInicialDescarga"      id="fechaInicialDescargaXLS_Emitidos">
  <input type="hidden" name="fechaFinalDescarga"        id="fechaFinalDescargaXLS_Emitidos">
  <input type="hidden" name="uuidDescarga"              id="uuidDescargaXLS_Emitidos">
  <input type="hidden" name="estatusCFDI"               id="estatusCFDIXLS_Emitidos">

  <!-- TEXTOS – operadores -->
  <input type="hidden" name="uuidOperator"    id="uuidOperatorXLS_Emitidos">
  <input type="hidden" name="rfcEmiOperator"  id="rfcEmiOperatorXLS_Emitidos">
  <input type="hidden" name="nomEmiOperator"  id="nomEmiOperatorXLS_Emitidos">
  <input type="hidden" name="rfcRecOperator"  id="rfcRecOperatorXLS_Emitidos">
  <input type="hidden" name="nomRecOperator"  id="nomRecOperatorXLS_Emitidos">
  <input type="hidden" name="pacOperator"     id="pacOperatorXLS_Emitidos">
  <input type="hidden" name="efectoOperator"  id="efectoOperatorXLS_Emitidos">
  <input type="hidden" name="estatusOperator" id="estatusOperatorXLS_Emitidos">
  <input type="hidden" name="bovedaOperator"  id="bovedaOperatorXLS_Emitidos">

  <!-- NUMÉRICO – monto -->
  <input type="hidden" name="montoV1"         id="montoV1XLS_Emitidos">
  <input type="hidden" name="montoV2"         id="montoV2XLS_Emitidos">
  <input type="hidden" name="montoOperator"   id="montoOperatorXLS_Emitidos">

  <!-- FECHAS – emisión/certificación/cancelación -->
  <input type="hidden" name="emiDateOperator" id="emiDateOperatorXLS_Emitidos">
  <input type="hidden" name="emiDateV1"       id="emiDateV1XLS_Emitidos">
  <input type="hidden" name="emiDateV2"       id="emiDateV2XLS_Emitidos">

  <input type="hidden" name="cerDateOperator" id="cerDateOperatorXLS_Emitidos">
  <input type="hidden" name="cerDateV1"       id="cerDateV1XLS_Emitidos">
  <input type="hidden" name="cerDateV2"       id="cerDateV2XLS_Emitidos">

  <input type="hidden" name="canDateOperator" id="canDateOperatorXLS_Emitidos">
  <input type="hidden" name="canDateV1"       id="canDateV1XLS_Emitidos">
  <input type="hidden" name="canDateV2"       id="canDateV2XLS_Emitidos">
</form>


<script type="text/javascript">
$(document).ready(function() {
	/*
	$('#tipoComprobante_Emitidos').select2({
		theme: 'bootstrap-5'
	});
	$('#tipoComprobante_Emitidos').val('ALL'); // Selecciona primer valor del combo
	$('#tipoComprobante_Emitidos').trigger('change'); //Refresca el combo
	
	$('#existeBoveda_Emitidos').select2({
		theme: 'bootstrap-5'
	});
	$('#existeBoveda_Emitidos').val('ALL'); // Selecciona primer valor del combo
	$('#existeBoveda_Emitidos').trigger('change'); //Refresca el combo
	
	$('#estatusCFDI_Emitidos').select2({
		theme: 'bootstrap-5'
	});
	$('#estatusCFDI_Emitidos').val('ALL'); // Selecciona primer valor del combo
	$('#estatusCFDI_Emitidos').trigger('change'); //Refresca el combo
	
	
	flatpickr(fechaInicial_Emitidos, {
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
 	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
 	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
 	        },
 	      },
 	    }); 
  	 
  	 flatpickr(fechaFinal_Emitidos, {
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
  	 
  	obtenerFechasFiltroDescargaEmitidos();
  	calcularEtiquetasConfDescargaRecibidos();
});


function obtenerFechasMinimaDescargaEmitidos(fechaInicial, fechaFinal){
	$.ajax({
		url  : '/siarex247/cumplimientoFiscal/descargaSAT/emitidos/consultarFechaMinimaEmitidos.action',
		type : 'POST', 
		data : null,
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			   
			} else {
				const calendarioIni = flatpickr("#fechaInicial_Emitidos", { 
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
				
				const calendarioFin = flatpickr("#fechaFinal_Emitidos", { 
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
				
				 $('#fechaInicial_Emitidos').val(fechaInicial);
				 $('#fechaFinal_Emitidos').val(fechaFinal);
				
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('obtenerFechasMinimaDescarga()_'+thrownError);
		}
	});	
			
}


function calcularEtiquetasConfDescargaRecibidos(){
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
					
					document.getElementById("CONF_DESCARGA_SAT_TITLE3").innerHTML = data.TITLE3;
					document.getElementById("CONF_DESCARGA_SAT_ETQ30").innerHTML = data.ETQ20;
					document.getElementById("CONF_DESCARGA_SAT_ETQ31").innerHTML = data.ETQ21;
					document.getElementById("CONF_DESCARGA_SAT_ETQ32").innerHTML = data.ETQ3;
					document.getElementById("CONF_DESCARGA_SAT_ETQ33").innerHTML = data.ETQ4;
					document.getElementById("CONF_DESCARGA_SAT_ETQ34").innerHTML = data.ETQ5;
					document.getElementById("CONF_DESCARGA_SAT_ETQ35").innerHTML = data.ETQ6;
					document.getElementById("CONF_DESCARGA_SAT_ETQ36").innerHTML = data.ETQ7;
					document.getElementById("CONF_DESCARGA_SAT_ETQ37").innerHTML = data.ETQ8;
					document.getElementById("CONF_DESCARGA_SAT_ETQ38").innerHTML = data.ETQ9;
					document.getElementById("CONF_DESCARGA_SAT_ETQ39").innerHTML = data.ETQ10;
					document.getElementById("CONF_DESCARGA_SAT_ETQ40").innerHTML = data.ETQ11;
					document.getElementById("CONF_DESCARGA_SAT_ETQ41").innerHTML = data.ETQ12;
					document.getElementById("CONF_DESCARGA_SAT_ETQ42").innerHTML = data.ETQ13;
					document.getElementById("CONF_DESCARGA_SAT_ETQ43").innerHTML = data.ETQ14;
					document.getElementById("CONF_DESCARGA_SAT_ETQ44").innerHTML = data.ETQ15;
					document.getElementById("CONF_DESCARGA_SAT_ETQ45").innerHTML = data.ETQ16;
					document.getElementById("CONF_DESCARGA_SAT_ETQ46").innerHTML = data.ETQ17;
					document.getElementById("CONF_DESCARGA_SAT_ETQ47").innerHTML = data.ETQ18;
					document.getElementById("CONF_DESCARGA_SAT_ETQ48").innerHTML = data.ETQ19;
					document.getElementById("CONF_DESCARGA_SAT_ETQ49").innerHTML = data.ETQ20;
					document.getElementById("CONF_DESCARGA_SAT_ETQ50").innerHTML = data.ETQ21;
					document.getElementById("CONF_DESCARGA_SAT_ETQ51").innerHTML = data.ETQ22;
					document.getElementById("CONF_DESCARGA_SAT_ETQ52").innerHTML = data.ETQ23;
					document.getElementById("CONF_DESCARGA_SAT_ETQ53").innerHTML = data.ETQ24;
					document.getElementById("CONF_DESCARGA_SAT_ETQ54").innerHTML = data.ETQ25;
					document.getElementById("CONF_DESCARGA_SAT_ETQ55").innerHTML = data.ETQ26;
					document.getElementById("CONF_DESCARGA_SAT_ETQ56").innerHTML = data.ETQ27;
					document.getElementById("CONF_DESCARGA_SAT_ETQ57").innerHTML = data.ETQ28;
					document.getElementById("CONF_DESCARGA_SAT_ETQ58").innerHTML = data.ETQ29;
					document.getElementById("CONF_DESCARGA_SAT_EstatusEmitidos").innerHTML = data.ETQ27;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
</script>

<script>
// Copia select del thead a los “globales” que viajan en AJAX (Emitidos)
function __copiarTheadAGlobalesE(){
  const est = ($('#estatusFilterE').val() || '').trim();
  $('#estatusCFDI_Emitidos').val(est === 'ALL' ? '' : est);

  const bov = ($('#bovedaFilterE').val() || '').trim();
  $('#existeBoveda_Emitidos').val(bov === 'ALL' ? '' : bov);
}

// Cambios en ESTATUS => set operador y refresca
$(document).on('change','#efectoFilterInputE', function(){
  const v = ($(this).val()||'').trim();
  $('#efectoOperatorE').val(v && v!=='ALL' ? 'equals' : 'contains');
  // UI del botón: "=" cuando hay valor, lupa cuando TODOS
  $('#efectoOpBtnE .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
  __copiarTheadAGlobalesE();
  validarFechasEmitidos();
  //if (typeof refrescarEmitidos==='function') refrescarEmitidos('change-estatus');
});


//Cambios en ESTATUS => set operador y refresca
$(document).on('change','#estatusFilterE', function(){
  const v = ($(this).val()||'').trim();
  $('#estatusOperatorE').val(v && v!=='ALL' ? 'equals' : 'contains');
  // UI del botón: "=" cuando hay valor, lupa cuando TODOS
  $('#estatusOpBtnE .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
  __copiarTheadAGlobalesE();
  validarFechasEmitidos();
  //if (typeof refrescarEmitidos==='function') refrescarEmitidos('change-estatus');
});


// Cambios en BÓVEDA => set operador y refresca
$(document).on('change','#bovedaFilterE', function(){
  const v = ($(this).val()||'').trim();
  $('#bovedaOperatorE').val(v && v!=='ALL' ? 'equals' : 'contains');
  $('#bovedaOpBtnE .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
  __copiarTheadAGlobalesE();
  validarFechasEmitidos();
  //if (typeof refrescarEmitidos==='function') refrescarEmitidos('change-boveda');
});

// DEBUG opcional para ver qué viaja (quítalo en prod)
window.DEBUG_EMI = true;
if (window.DEBUG_EMI) {
  $(document).on('change', '#estatusFilterE,#bovedaFilterE', function(){
	  /*
    console.log('[DBG/EMITIDOS] estatus=', $('#estatusCFDI_Emitidos').val(),
                ' op=', $('#estatusOperatorE').val(),
                ' | boveda=', $('#existeBoveda_Emitidos').val(),
                ' op=', $('#bovedaOperatorE').val()); */
  });
}
</script>



</html>