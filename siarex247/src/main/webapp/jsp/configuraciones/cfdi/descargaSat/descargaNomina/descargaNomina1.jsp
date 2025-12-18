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

	<script src="/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaNomina/descargaNomina.js?v=<%=Utils.VERSION%>"></script>
	<style>
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
  #tablaDetalleNomina thead tr.filters th{position:relative;overflow:visible!important;}
  table.dataTable thead th,table.dataTable thead td{overflow:visible!important;}
  .dtfh-floatingparent thead th{overflow:visible!important;}
  #tablaDetalleNomina thead .dx-like-menu{z-index:1090;}
  
 #tablaDetalleNomina #CONF_DESCARGA_SAT_ETQ76 { width: 120px !important; min-width: 120px !important; } /* PAC */
  #tablaDetalleNomina #CONF_DESCARGA_SAT_ETQ81 { width: 150px !important; min-width: 150px !important; } /* Estatus */
  #tablaDetalleNomina #CONF_DESCARGA_SAT_ETQ83 { width: 150px !important; min-width: 150px !important; } /* BÃÂ³veda */
  }
</style>
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE4">Descarga Masiva de XML Nomina</h5>
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>
	


	<div class="card-header">
	
	
		<div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="fechaInicial_Filtro" id="CONF_DESCARGA_SAT_ETQ63" >Fecha Inicio</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Nomina" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>

				<label class="col-sm-1 col-form-label" for="fechaFinal_Filtro" id="CONF_DESCARGA_SAT_ETQ68">Fecha Final</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Nomina" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>

	              
        </div>
        
        
	</div>
		
 <div class="collapse" id="filtrosBusquedaVisor">	
	<div class="card-header">
	
	
		<div class="mb-2 row">
                <label class="col-sm-1 col-form-label" for="rfcReceptor_Nomina" id="CONF_DESCARGA_SAT_ETQ61">RFC Receptor</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="rfcReceptor_Nomina" name="rfcReceptor" class="form-control" type="text"  value="" maxlength="15"  />
					</div>
				</div>
				<label class="col-sm-2 col-form-label" for="razonSocialReceptor_Nomina" id="CONF_DESCARGA_SAT_ETQ62">Raz&oacute;n Social Receptor</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="razonSocialReceptor_Nomina" name="razonSocialEmisor" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				
				<label class="col-sm-1 col-form-label" for="fechaInicial_Nomina" id="CONF_DESCARGA_SAT_ETQ63_BK">Fecha Inicio</label>
	              <div class="col-sm-2">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicial_Nomina_BK" name="fechaInicial" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
        </div>
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="existeBoveda_Nomina" id="CONF_DESCARGA_SAT_ETQ64">Existe Boveda </label>
				<div class="col-sm-2">
					<div class="form-group">
					   <select id="existeBoveda_Nomina" class="form-select"> 
					     		 <option value="ALL"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ65"> Todos </option>
                        		<option value="S"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ66"> SI </option>
                        		<option value="N"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ67"> NO </option>
					   </select>
					</div>
				</div>
				
				 <label class="col-sm-2 col-form-label" for="estatusCFDI_Nomina" id="CONF_DESCARGA_SAT_EstatusNomina">Estatus </label>
				<div class="col-sm-3">
					<div class="form-group">
					   <select id="estatusCFDI_Nomina" class="form-select"> 
					     	<option value="ALL"  style="text-align: center;"> Todos </option>
                        	<option value="VIGENTE"  style="text-align: center;"> Vigentes </option>
                        	<option value="CANCELADO"  style="text-align: center;"> Cancelados </option>
					   </select>
					</div>
				</div>
				
				<label class="col-sm-1 col-form-label" for="fechaFinal_Nomina" id="CONF_DESCARGA_SAT_ETQ68_BK">Fecha Final</label>
	              <div class="col-sm-2">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinal_Nomina_BK" name="fechaFinal" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
					
        </div>
        <div class="mb-2 row">
        	  
          		<label class="col-sm-1 col-form-label" for="uuidDescarga_Nomina" id="CONF_DESCARGA_SAT_ETQ69">UUID</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="uuidDescarga_Nomina" name="uuidDescarga" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescarNomina();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span>  <span id="CONF_DESCARGA_SAT_ETQ70" > Refrescar </span> </button>
					</div>
				</div>
        </div>
	</div>
 </div>	
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
	
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<div id="overSeccion_Nomina" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
				
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<input type="hidden" id="uuidOperatorN"    value="contains">
				<input type="hidden" id="rfcRecOperatorN"  value="contains">
				<input type="hidden" id="nomRecOperatorN"  value="contains">
				
				<!-- y si usas tambiÃÂ©n emisor/pac/estatus/bÃÂ³veda: -->
				<input type="hidden" id="rfcEmiOperatorN"  value="contains">
				<input type="hidden" id="nomEmiOperatorN"  value="contains">
				<input type="hidden" id="pacOperatorN"     value="contains">
				<input type="hidden" id="efectoOperatorN"  value="contains">
				<input type="hidden" id="estatusOperatorN" value="contains">
				<input type="hidden" id="bovedaOperatorN"  value="contains">
				
				<!-- num/fechas -->
				<input type="hidden" id="montoOperatorN"   value="eq">
				<input type="hidden" id="emiDateOperatorN" value="eq">
				<input type="hidden" id="cerDateOperatorN" value="eq">
				<input type="hidden" id="canDateOperatorN" value="eq">
				
					<table id="tablaDetalleNomina"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<!-- <th class="no-sort pe-1 align-middle data-table-row-action">Sel</th> -->
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ71">UUID</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ72">RFC del Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ73">Raz&oacute;n Social Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ74">RFC Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ75">Raz&oacute;n Social Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ76">Pac Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ77">Fecha Emision</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ78">Fecha Certificaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ79">Monto</th>
								<!--  <th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ80">Efecto Comprobante</th>-->
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ81">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ82">Fecha Cancelaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ83">Existe Boveda</th>
							</tr>
							<!-- Fila de filtros (usar como <tr class="filters"> debajo de los headers) -->
								<tr class="filters">
								  <!-- 1: UUID -->
								  <th>
								    <div class="dx-like-filter">
								      <span class="op-btn" id="uuidOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <input type="text" id="uuidFilterInputN" placeholder="Filtrar UUID...">
								      <div class="dx-like-menu" id="uuidOpMenuN">
								        <ul>
								          <li data-op="contains">&#128269; Contiene</li>
								          <li data-op="notContains">&#128683; No contiene</li>
								          <li data-op="startsWith">&#8676; Empieza con</li>
								          <li data-op="endsWith">&#8677; Termina con</li>
								          <li data-op="equals">= Igual</li>
								          <li data-op="notEquals">&ne; Distinto</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="uuidOperatorN" value="contains">
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
								      <span class="op-btn" id="rfcRecOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <input type="text" id="rfcRecFilterInputN" placeholder="RFC receptor...">
								      <div class="dx-like-menu" id="rfcRecOpMenuN">
								        <ul>
								          <li data-op="contains">&#128269; Contiene</li>
								          <li data-op="notContains">&#128683; No contiene</li>
								          <li data-op="startsWith">&#8676; Empieza con</li>
								          <li data-op="endsWith">&#8677; Termina con</li>
								          <li data-op="equals">= Igual</li>
								          <li data-op="notEquals">&ne; Distinto</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="rfcRecOperatorN" value="contains">
								  </th>
								
								  <!-- 5: Nombre Receptor -->
								  <th>
								    <div class="dx-like-filter">
								      <span class="op-btn" id="nomRecOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <input type="text" id="nomRecFilterInputN" placeholder="Nombre receptor...">
								      <div class="dx-like-menu" id="nomRecOpMenuN">
								        <ul>
								          <li data-op="contains">&#128269; Contiene</li>
								          <li data-op="notContains">&#128683; No contiene</li>
								          <li data-op="startsWith">&#8676; Empieza con</li>
								          <li data-op="endsWith">&#8677; Termina con</li>
								          <li data-op="equals">= Igual</li>
								          <li data-op="notEquals">&ne; Distinto</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="nomRecOperatorN" value="contains">
								  </th>
								
								  <!-- 6: RFC PAC -->
								  <th>
								    <div class="dx-like-filter">
								      <span class="op-btn" id="pacOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <input type="text" id="pacFilterInputN" placeholder="RFC PAC...">
								      <div class="dx-like-menu" id="pacOpMenuN">
								        <ul>
								          <li data-op="contains">&#128269; Contiene</li>
								          <li data-op="notContains">&#128683; No contiene</li>
								          <li data-op="startsWith">&#8676; Empieza con</li>
								          <li data-op="endsWith">&#8677; Termina con</li>
								          <li data-op="equals">= Igual</li>
								          <li data-op="notEquals">&ne; Distinto</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="pacOperatorN" value="contains">
								  </th>
								
								  <!-- 7: Fecha EmisiÃÂ³n
								  <th>
								    <div class="dx-like-filter" style="min-width:260px;">
								      <span class="op-btn" id="emiDateOpBtnN"><span class="op-label">=</span></span>
								      <input type="date" id="emiDateFilter1N">
								      <input type="date" id="emiDateFilter2N" class="d-none">
								      <div class="dx-like-menu" id="emiDateOpMenuN">
								        <ul>
								          <li data-op="eq">= Igual</li>
								          <li data-op="ne">&ne; No igual</li>
								          <li data-op="lt">&lt; Menor que</li>
								          <li data-op="gt">&gt; Mayor que</li>
								          <li data-op="le">&le; Menor o igual</li>
								          <li data-op="ge">&ge; Mayor o igual</li>
								          <li data-op="bt">&#8596; Entre</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="emiDateOperatorN" value="eq">
								  </th> -->
								  
								      							 <th>
																  <input type="hidden" id="emiDateOperatorN" value="eq">
																  <input type="hidden" id="emiDateFilter1N">
																  <input type="hidden" id="emiDateFilter2N">
																  <!-- opcional, placeholder por si algÃºn JS toca el label -->
																  <span id="emiDateOpBtnN" class="d-none"><span class="op-label">=</span></span>
																</th>
								
								  <!-- 8: Fecha CertificaciÃÂ³n -->
								  <th>
								    <div class="dx-like-filter" style="min-width:260px;">
								      <span class="op-btn" id="cerDateOpBtnN"><span class="op-label">=</span></span>
								      <input type="date" id="cerDateFilter1N">
								      <input type="date" id="cerDateFilter2N" class="d-none">
								      <div class="dx-like-menu" id="cerDateOpMenuN">
								        <ul>
								          <li data-op="eq">= Igual</li>
								          <li data-op="ne">&ne; No igual</li>
								          <li data-op="lt">&lt; Menor que</li>
								          <li data-op="gt">&gt; Mayor que</li>
								          <li data-op="le">&le; Menor o igual</li>
								          <li data-op="ge">&ge; Mayor o igual</li>
								          <li data-op="bt">&#8596; Entre</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="cerDateOperatorN" value="eq">
								  </th>
								
								  <!-- 9: Monto -->
								  <th>
								    <div class="dx-like-filter">
								      <span class="op-btn" id="montoOpBtnN"><span class="op-label">=</span></span>
								      <input type="number" step="any" id="montoFilter1N" placeholder="Monto...">
								      <input type="number" step="any" id="montoFilter2N" placeholder="y..." class="d-none">
								      <div class="dx-like-menu" id="montoOpMenuN">
								        <ul>
								          <li data-op="eq">= Igual</li>
								          <li data-op="ne">&ne; No igual</li>
								          <li data-op="lt">&lt; Menor que</li>
								          <li data-op="gt">&gt; Mayor que</li>
								          <li data-op="le">&le; Menor o igual</li>
								          <li data-op="ge">&ge; Mayor o igual</li>
								          <li data-op="between">&#8596; Entre</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="montoOperatorN" value="eq">
								  </th>
								
								
								
								
								  <!-- 11: Estatus -->
								  <th>
								    <div class="dx-like-filter">
								      <span class="op-btn" id="estatusOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <select id="estatusFilterN" class="form-select form-select-sm">
								        <option value="ALL">TODOS</option>
								        <option value="VIGENTE">VIGENTE</option>
								        <option value="CANCELADO">CANCELADO</option>
								      </select>
								      <input type="hidden" id="estatusOperatorN" value="contains">
								    </div>
								  </th>
								
								  <!-- 12: Fecha CancelaciÃÂ³n -->
								  <th>
								    <div class="dx-like-filter" style="min-width:260px;">
								      <span class="op-btn" id="canDateOpBtnN"><span class="op-label">=</span></span>
								      <input type="date" id="canDateFilter1N">
								      <input type="date" id="canDateFilter2N" class="d-none">
								      <div class="dx-like-menu" id="canDateOpMenuN">
								        <ul>
								          <li data-op="eq">= Igual</li>
								          <li data-op="ne">&ne; No igual</li>
								          <li data-op="lt">&lt; Menor que</li>
								          <li data-op="gt">&gt; Mayor que</li>
								          <li data-op="le">&le; Menor o igual</li>
								          <li data-op="ge">&ge; Mayor o igual</li>
								          <li data-op="bt">&#8596; Entre</li>
								          <li data-op="reset">&#128260; Reset</li>
								        </ul>
								      </div>
								    </div>
								    <input type="hidden" id="canDateOperatorN" value="eq">
								  </th>
								
								  <!-- 13: Existe en BÃÂ³veda -->
								  <th>
								    <div class="dx-like-filter">
								      <span class="op-btn" id="bovedaOpBtnN"><span class="op-label"><i class="fas fa-search"></i></span></span>
								      <select id="bovedaFilterN" class="form-select form-select-sm">
								        <option value="ALL">TODOS</option>
								        <option value="S">S</option>
								        <option value="N">N</option>
								      </select>
								      <input type="hidden" id="bovedaOperatorN" value="contains">
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


<!-- ====== FORM oculto para Excel (Nomina) ====== -->
<form name="frmDescargaXMLExcelNomina"
      action="/siarex247/excel/exportExcelDescargaNomina.action"
      method="post" target="_blank">

  <!-- ya existentes (Nomina) -->
  <input type="hidden" name="rfcReceptor"            id="rfcReceptorXLS_Nomina">
  <input type="hidden" name="razonSocialReceptor"    id="razonSocialReceptorXLS_Nomina">
  <input type="hidden" name="existeBovedaDescarga"   id="existeBovedaDescargaXLS_Nomina">
  <input type="hidden" name="tipoComprobanteDescarga" id="tipoComprobanteDescargaXLS_Nomina">
  <input type="hidden" name="fechaInicialDescarga"   id="fechaInicialDescargaXLS_Nomina">
  <input type="hidden" name="fechaFinalDescarga"     id="fechaFinalDescargaXLS_Nomina">
  <input type="hidden" name="uuidDescarga"           id="uuidDescargaXLS_Nomina">
  <input type="hidden" name="estatusCFDI"            id="estatusCFDIXLS_Nomina">

  <!-- TEXTOS Ã¢ÂÂ operadores -->
  <input type="hidden" name="uuidOperator"   id="uuidOperatorXLS_Nomina">
  <input type="hidden" name="rfcEmiOperator" id="rfcEmiOperatorXLS_Nomina">
  <input type="hidden" name="nomEmiOperator" id="nomEmiOperatorXLS_Nomina">
  <input type="hidden" name="rfcRecOperator" id="rfcRecOperatorXLS_Nomina">
  <input type="hidden" name="nomRecOperator" id="nomRecOperatorXLS_Nomina">
  <input type="hidden" name="pacOperator"    id="pacOperatorXLS_Nomina">
  <input type="hidden" name="efectoOperator" id="efectoOperatorXLS_Nomina">
  <input type="hidden" name="estatusOperator" id="estatusOperatorXLS_Nomina">
  <input type="hidden" name="bovedaOperator"  id="bovedaOperatorXLS_Nomina">

  <!-- NUMÃÂRICO Ã¢ÂÂ monto -->
  <input type="hidden" name="montoV1"       id="montoV1XLS_Nomina">
  <input type="hidden" name="montoV2"       id="montoV2XLS_Nomina">
  <input type="hidden" name="montoOperator" id="montoOperatorXLS_Nomina">

  <!-- FECHAS Ã¢ÂÂ emisiÃÂ³n/certificaciÃÂ³n/cancelaciÃÂ³n -->
  <input type="hidden" name="emiDateOperator" id="emiDateOperatorXLS_Nomina">
  <input type="hidden" name="emiDateV1"       id="emiDateV1XLS_Nomina">
  <input type="hidden" name="emiDateV2"       id="emiDateV2XLS_Nomina">

  <input type="hidden" name="cerDateOperator" id="cerDateOperatorXLS_Nomina">
  <input type="hidden" name="cerDateV1"       id="cerDateV1XLS_Nomina">
  <input type="hidden" name="cerDateV2"       id="cerDateV2XLS_Nomina">

  <input type="hidden" name="canDateOperator" id="canDateOperatorXLS_Nomina">
  <input type="hidden" name="canDateV1"       id="canDateV1XLS_Nomina">
  <input type="hidden" name="canDateV2"       id="canDateV2XLS_Nomina">
</form>

<script type="text/javascript">
$(document).ready(function() {
	
	/*
	$('#existeBoveda_Nomina').select2({
		theme: 'bootstrap-5'
	});
	$('#existeBoveda_Nomina').val('ALL'); // Selecciona primer valor del combo
	$('#existeBoveda_Nomina').trigger('change'); //Refresca el combo
	
	$('#estatusCFDI_Nomina').select2({
		theme: 'bootstrap-5'
	});
	$('#estatusCFDI_Nomina').val('ALL'); // Selecciona primer valor del combo
	$('#estatusCFDI_Nomina').trigger('change'); //Refresca el combo
	
	
	flatpickr(fechaInicial_Nomina, {
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
 	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'OÃÂct', 'Nov', 'Dic'],
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
 	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'],         
 	        }, 
 	        months: {
 	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
 	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
 	        },
 	      },
 	    }); 
  	 	*/
  	 	obtenerFechasFiltroDescargaNomina();
  	// calcularEtiquetasConfDescargaNomina();
});



function obtenerFechasMinimaDescargaNomina(fechaInicial, fechaFinal){
	$.ajax({
		url  : '/siarex247/cumplimientoFiscal/descargaSAT/nomina/consultarFechaMinimaNomina.action',
		type : 'POST', 
		data : null,
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			   
			} else {
				const calendarioIni = flatpickr("#fechaInicial_Nomina", { 
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
				
				const calendarioFin = flatpickr("#fechaFinal_Nomina", { 
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
				
				 $('#fechaInicial_Nomina').val(fechaInicial);
				 $('#fechaFinal_Nomina').val(fechaFinal);
				
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('obtenerFechasFiltro()_'+thrownError);
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
					document.getElementById("CONF_DESCARGA_SAT_EstatusNomina").innerHTML = data.ETQ27;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
</script>

<script>
// Copia select del thead a los Ã¢ÂÂglobalesÃ¢ÂÂ que viajan en AJAX (NÃÂMINA)
function __copiarTheadAGlobalesN(){
  const est = ($('#estatusFilterN').val() || '').trim();
  $('#estatusCFDI_Nomina').val(est === 'ALL' ? '' : est);

  const bov = ($('#bovedaFilterN').val() || '').trim();
  $('#existeBoveda_Nomina').val(bov === 'ALL' ? '' : bov);
}

// Cambios en ESTATUS => set operador y refresca
/*
$(document).on('change','#estatusFilterN', function(){
	alert('consultando filtro......');
  const v = ($(this).val()||'').trim();
  $('#estatusOperatorN').val(v && v!=='ALL' ? 'equals' : 'contains');
  $('#estatusOpBtnN .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
  __copiarTheadAGlobalesN();
  if (typeof refrescarNomina==='function') refrescarNomina('change-estatus');
});
*/
// Cambios en BÃÂVEDA => set operador y refresca
/*
$(document).on('change','#bovedaFilterN', function(){
  const v = ($(this).val()||'').trim();
  $('#bovedaOperatorN').val(v && v!=='ALL' ? 'equals' : 'contains');
  $('#bovedaOpBtnN .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
  __copiarTheadAGlobalesN();
  if (typeof refrescarNomina==='function') refrescarNomina('change-boveda');
});
*/

// Efecto de comprobante (Nomina): select + auto-refresh
/*
$(document).on('change', '#efectoFilterInputN', function(){
  const v = ($(this).val() || '').trim();
  if (v === '') {
    $('#efectoOperatorN').val('contains');
    $('#tipoComprobanteDescarga_Nomina').val('');
    $('#efectoOpBtnN .op-label').html('<i class="fas fa-search"></i>');
  } else {
    $('#efectoOperatorN').val('equals');
    $('#tipoComprobanteDescarga_Nomina').val(v);
    $('#efectoOpBtnN .op-label').html('=');
  }
  if (typeof __copiarTheadAGlobalesN === 'function') __copiarTheadAGlobalesN();
  if (typeof refrescarNomina === 'function') refrescarNomina('change-efecto');
});
*/


/*
$(document).on('change', '#tablaDetalleNomina thead tr.filters select, .dtfh-floatingparent thead tr.filters select', function(){
  const id = this.id || '';
  if (id === 'bovedaFilterN') {
    const v = ($(this).val()||'').trim();
    $('#bovedaOperatorN').val(v ? 'equals' : 'contains');
  }
  if (id === 'pacFilterInputN') {
    const v = ($(this).val()||'').trim();
    $('#pacOperatorN').val(v ? 'equals' : 'contains');
  }
  if (typeof __copiarTheadAGlobalesN === 'function') __copiarTheadAGlobalesN();
  if (typeof refrescarNomina === 'function') refrescarNomina('change-select');
});

*/

/*
window.DEBUG_NOM = false;
if (window.DEBUG_NOM) {
  $(document).on('change', '#estatusFilterN,#bovedaFilterN', function(){
  });
}

  if (needSecond) {
	  // muestra segundo campo (ya lo haces)
	  // y fÃÂ³calo
	  setTimeout(function(){
	    $f.find('#montoFilter2N,#emiDateFilter2N,#cerDateFilter2N,#canDateFilter2N')
	      .filter(':visible')
	      .first()
	      .focus();
	  }, 0);
	}
	*/
	
</script>



</html>