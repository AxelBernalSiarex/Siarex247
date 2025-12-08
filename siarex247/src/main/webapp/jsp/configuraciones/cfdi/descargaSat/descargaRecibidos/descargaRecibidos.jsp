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

<script src='/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaRecibidos/descargaRecibidos.js?v=<%=Utils.VERSION%>' ></script>
	


<style>
  /* Filtros compactos (igual que en Emitidos) */
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

  /* Deja salir el menÃÂÃÂº en thead + FixedHeader */
  #tablaDetalle thead tr.filters th{position:relative;overflow:visible!important;}
  table.dataTable thead th,table.dataTable thead td{overflow:visible!important;}
  .dtfh-floatingparent thead 
[cite_start]th{overflow:visible!important;} /* [cite: 18] */
  #tablaDetalle thead .dx-like-menu{z-index:1090;}

  /* --- REGLAS AÃÂÃÂADIDAS PARA ANCHO DE COLUMNAS --- */
  #tablaDetalle #CONF_DESCARGA_SAT_ETQ22 { /* Pac Emisor */
    width: 120px !important;
    min-width: 120px !important;
  }
  
  #tablaDetalle #CONF_DESCARGA_SAT_ETQ27 { /* Estatus */
    width: 150px !important;
    min-width: 150px !important;
  }
  
  #tablaDetalle #CONF_DESCARGA_SAT_ETQ29 { /* Existe Boveda */
    width: 120px !important;
    min-width: 120px !important;
  }

</style>


	
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE1">Descarga Masiva de XML Recibidos</h5>
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>

  <div class="card-header">
	
	
		<div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="fechaInicial_Filtro" id="CONF_DESCARGA_SAT_ETQ3" >Fecha Inicio Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicialDescarga" name="fechaInicialDescarga" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>

				<label class="col-sm-1 col-form-label" for="fechaFinal_Filtro" id="CONF_DESCARGA_SAT_ETQ14">Fecha Final Factura</label>
	              <div class="col-sm-1">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinalDescarga" name="fechaFinalDescarga" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>

	              
        </div>
        
        
	</div>
	
<div class="collapse" id="filtrosBusquedaVisor">	
	<div class="card-header">
	
	
		<div class="mb-2 row">
                <label class="col-sm-1 col-form-label" for="rfcEmisor" id="CONF_DESCARGA_SAT_ETQ1">RFC Emisor</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="rfcEmisor" name="rfcEmisor" class="form-control" type="text"  value="" maxlength="15"  />
					</div>
				</div>
				<label class="col-sm-2 col-form-label" for="razonSocialEmisor" id="CONF_DESCARGA_SAT_ETQ2">Raz&oacute;n Social Emisor</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="razonSocialEmisor" name="razonSocialEmisor" class="form-control" type="text"  value="" maxlength="300"  />
					</div>
				</div>
				
				<label class="col-sm-1 col-form-label" for="fechaInicialDescarga" id="CONF_DESCARGA_SAT_ETQ3_BK">Fecha Inicio</label>
	              <div class="col-sm-2">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicialDescarga_BK" name="fechaInicialDescarga" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
	                </div>
	              </div>
        </div>
        <div class="mb-2 row">
				<label class="col-sm-1 col-form-label" for="existeBovedaDescarga" id="CONF_DESCARGA_SAT_ETQ4">Existe Boveda </label>
				<div class="col-sm-2">
					<div class="form-group">
					   <select id="existeBovedaDescarga" class="form-select"> 
					     		 <option value="ALL"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ5"> Todos </option>
                        		<option value="S"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ6"> SI </option>
                        		<option value="N"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ7"> NO </option>
					   </select>
					</div>
				</div>
				
        		<label class="col-sm-2 col-form-label" for="tipoComprobanteDescarga" id="CONF_DESCARGA_SAT_ETQ8">Tipo de Comprobante </label>
				<div class="col-sm-3">
					<div class="form-group">
					   <select id="tipoComprobanteDescarga" class="form-select"> 
					     		<option value="ALL"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ9"> Todos </option>
                        		<option value="I"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ10"> I - Ingreso</option>
                        		<option value="P"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ11"> P - Pago</option>
                        		<option value="E"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ12"> E - Egresos</option>
                        		<option value="T"  style="text-align: center;" id="CONF_DESCARGA_SAT_ETQ13"> T - Translado</option>
					   </select>
					</div>
				</div>
				
				<label class="col-sm-1 col-form-label" for="fechaFinalDescarga" id="CONF_DESCARGA_SAT_ETQ14_BK">Fecha Final</label>
	              <div class="col-sm-2">
	                <div class="form-group">
	                  <input class="form-control datetimepicker flatpickr-input active" id="fechaFinalDescarga_BK" name="fechaFinalDescarga" type="text" placeholder="dd/mm/yyyy" data-options='{"disableMobile":true}' readonly="readonly" />
	                </div>
	              </div>
					
        </div>
        <div class="mb-2 row">
        	   <label class="col-sm-1 col-form-label" for="estatusCFDI" id="CONF_DESCARGA_SAT_Estatus">Estatus </label>
				<div class="col-sm-2">
					<div class="form-group">
					   <select id="estatusCFDI" class="form-select"> 
					     		 <option value="ALL"  style="text-align: center;"> Todos </option>
                        		<option value="VIGENTE"  style="text-align: center;"> Vigentes </option>
                        		<option value="CANCELADO"  style="text-align: center;"> Cancelados </option>
					   </select>
					</div>
				</div>
          		<label class="col-sm-2 col-form-label" for="uuidDescarga" id="CONF_DESCARGA_SAT_ETQ15">UUID</label>
				<div class="col-sm-3">
					<div class="form-group">
					   <input id="uuidDescarga" name="uuidDescarga" class="form-control" type="text"  value="" maxlength="100"  />
					</div>
				</div>
				
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescar();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span>  <span id="CONF_DESCARGA_SAT_ETQ16" > Refrescar </span> </button>
					</div>
				</div>
        </div>
	</div>
 </div>	
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				<div id="overSeccion_Descarga_Recibidos" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">

					
					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<!-- <th class="no-sort pe-1 align-middle data-table-row-action">Sel</th> -->
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ17">UUID</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ18">RFC del Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ19">Raz&oacute;n Social Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ20">RFC Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ21">Raz&oacute;n Social Receptor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ22">Pac Emisor</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ23">Fecha Emision</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ24">Fecha Certificaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ25">Monto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ26">Efecto Comprobante</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ27">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ28">Fecha Cancelaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ29">Existe Boveda</th>
							</tr>
							<!-- Filtros -->
							  <tr class="filters">
							    <!-- 1: UUID (texto) -->
							    <th>
							      <div class="dx-like-filter">
							        <span class="op-btn" id="uuidOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
							        <input type="text" id="uuidFilterInputR" placeholder="Filtrar UUID..." />
							        <div class="dx-like-menu" id="uuidOpMenuR">
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
							      <input type="hidden" id="uuidOperatorR" value="contains">
							    </th>
							
							    <!-- 2: RFC Emisor (texto) -->
							    <th>
							      <div class="dx-like-filter">
							        <span class="op-btn" id="rfcEmiOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
							        <input type="text" id="rfcEmiFilterInputR" placeholder="RFC emisor..." />
							        <div class="dx-like-menu" id="rfcEmiOpMenuR">
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
							      <input type="hidden" id="rfcEmiOperatorR" value="contains">
							    </th>
							
							    <!-- 3: Nombre Emisor (texto) -->
							    <th>
							      <div class="dx-like-filter">
							        <span class="op-btn" id="nomEmiOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
							        <input type="text" id="nomEmiFilterInputR" placeholder="Nombre emisor..." />
							        <div class="dx-like-menu" id="nomEmiOpMenuR">
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
							      <input type="hidden" id="nomEmiOperatorR" value="contains">
							    </th>
							
							    <!-- 4: RFC Receptor (texto) -->
							    <th>
							      
							    </th>
							
							    <!-- 5: Nombre Receptor (texto) -->
							    <th>
							     
							    </th>
							
							    <!-- 6: RFC PAC (texto) -->
							    <th>
							      <div class="dx-like-filter">
							        <span class="op-btn" id="pacOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
							        <input type="text" id="pacFilterInputR" placeholder="RFC PAC..." />
							        <div class="dx-like-menu" id="pacOpMenuR">
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
							      <input type="hidden" id="pacOperatorR" value="contains">
							    </th>
							
							    <!-- 7: Fecha EmisiÃÂÃÂ³n (fecha con operador) 
							    <th>
							      <div class="dx-like-filter" style="min-width:260px;">
							        <span class="op-btn" id="emiDateOpBtnR"><span class="op-label">=</span></span>
							        <input type="date" id="emiDateFilter1R" />
							        <input type="date" id="emiDateFilter2R" class="d-none" />
							        <div class="dx-like-menu" id="emiDateOpMenuR">
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
							      <input type="hidden" id="emiDateOperatorR" value="eq">
							    </th>-->
							    
							          <th>
								  <input type="hidden" id="emiDateOperatorR" value="eq">
								  <input type="hidden" id="emiDateFilter1R">
								  <input type="hidden" id="emiDateFilter2R">
								  <!-- opcional, placeholder por si algÃÂºn JS toca el label -->
								  <span id="emiDateOpBtnR" class="d-none"><span class="op-label">=</span></span>
								</th>
							
							    <!-- 8: Fecha CertificaciÃÂÃÂ³n (fecha con operador) -->
							    <th>
							      <div class="dx-like-filter" style="min-width:260px;">
							        <span class="op-btn" id="cerDateOpBtnR"><span class="op-label">=</span></span>
							        <input type="date" id="cerDateFilter1R" />
							        <input type="date" id="cerDateFilter2R" class="d-none" />
							        <div class="dx-like-menu" id="cerDateOpMenuR">
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
							      <input type="hidden" id="cerDateOperatorR" value="eq">
							    </th>
							
							    <!-- 9: Monto (numÃÂÃÂ©rico con operador) -->
							    <th>
							      <div class="dx-like-filter">
							        <span class="op-btn" id="montoOpBtnR"><span class="op-label">=</span></span>
							        <input type="number" step="any" id="montoFilter1R" placeholder="Monto..." />
							        <input type="number" step="any" id="montoFilter2R" placeholder="y..." class="d-none" />
							        <div class="dx-like-menu" id="montoOpMenuR">
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
							      <input type="hidden" id="montoOperatorR" value="eq">
							    </th>
							
							    <!-- 10: Efecto (texto/selector; aquÃÂÃÂ­ lo dejo texto) -->
							    <th>
								  <div class="dx-like-filter">
								    <span class="op-btn" id="efectoOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
								    <select id="efectoFilterInputR" class="form-select form-select-sm">
								      <option value="">TODOS</option>
								      <option value="I">I</option>
								      <option value="P">P</option>
								      <option value="T">T</option>
								      <option value="E">E</option>
								    </select>
								    <input type="hidden" id="efectoOperatorR" value="contains">
								  </div>
								</th>

							
							    <!-- 11: Estatus (texto; si tienes <select>, ÃÂÃÂºsalo) -->
							    <th>
								  <div class="dx-like-filter">
								    <span class="op-btn" id="estatusOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
								    <select id="estatusFilterR" class="form-select form-select-sm">
								      <option value="ALL">TODOS</option>
								      <option value="VIGENTE">VIGENTE</option>
								      <option value="CANCELADO">CANCELADO</option>
								    </select>
								    <input type="hidden" id="estatusOperatorR" value="contains">
								  </div>
								</th>
							
							    <!-- 12: Fecha CancelaciÃÂÃÂ³n (fecha con operador) -->
							    <th>
							      <div class="dx-like-filter" style="min-width:260px;">
							        <span class="op-btn" id="canDateOpBtnR"><span class="op-label">=</span></span>
							        <input type="date" id="canDateFilter1R" />
							        <input type="date" id="canDateFilter2R" class="d-none" />
							        <div class="dx-like-menu" id="canDateOpMenuR">
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
							      <input type="hidden" id="canDateOperatorR" value="eq">
							    </th>
							
							    <!-- 13: En BÃÂÃÂ³veda (texto/selector; lo dejo texto simple) -->
						<!-- EXISTE EN BÃÂÃÂVEDA -->
						<th id="BOVEDA_EMITIDOS_ETQ18">
						  <div class="dx-like-filter">
						    <span class="op-btn" id="bovedaOpBtnR"><span class="op-label"><i class="fas fa-search"></i></span></span>
						    <select id="bovedaFilterR" class="form-select form-select-sm">
						      <option value="ALL">TODOS</option>
						      <option value="S">S</option>
						      <option value="N">N</option>
						    </select>
						    <input type="hidden" id="bovedaOperatorR" value="contains">
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


<form name="frmDescargaXMLExcelRecibidos"
      action="/siarex247/excel/exportExcelDescargaSAT.action"
      method="post" target="_blank">

  <!-- ocultos que ya tenÃÂÃÂ­as (bÃÂÃÂ¡sicos) -->
  <input type="hidden" name="rfcEmisor"               id="rfcEmisorXLS_Recibidos">
  <input type="hidden" name="razonSocialEmisor"       id="razonSocialEmisorXLS_Recibidos">
  <input type="hidden" name="existeBovedaDescarga"    id="existeBovedaDescargaXLS_Recibidos">
  <input type="hidden" name="tipoComprobanteDescarga" id="tipoComprobanteDescargaXLS_Recibidos">
  <input type="hidden" name="fechaInicialDescarga"    id="fechaInicialDescargaXLS_Recibidos">
  <input type="hidden" name="fechaFinalDescarga"      id="fechaFinalDescargaXLS_Recibidos">
  <input type="hidden" name="uuidDescarga"            id="uuidDescargaXLS_Recibidos">
  <input type="hidden" name="estatusCFDI"             id="estatusCFDIXLS_Recibidos">

  <!-- TEXTOS ÃÂ¢ÃÂÃÂ operadores -->
  <input type="hidden" name="uuidOperator"    id="uuidOperatorXLS_Recibidos">
  <input type="hidden" name="rfcEmiOperator"  id="rfcEmiOperatorXLS_Recibidos">
  <input type="hidden" name="nomEmiOperator"  id="nomEmiOperatorXLS_Recibidos">
  <input type="hidden" name="rfcRecOperator"  id="rfcRecOperatorXLS_Recibidos">
  <input type="hidden" name="nomRecOperator"  id="nomRecOperatorXLS_Recibidos">
  <input type="hidden" name="pacOperator"     id="pacOperatorXLS_Recibidos">
  <input type="hidden" name="efectoOperator"  id="efectoOperatorXLS_Recibidos">
  <input type="hidden" name="estatusOperator" id="estatusOperatorXLS_Recibidos">
  <input type="hidden" name="bovedaOperator"  id="bovedaOperatorXLS_Recibidos">

  <!-- NUMÃÂÃÂRICO ÃÂ¢ÃÂÃÂ monto -->
  <input type="hidden" name="montoV1"         id="montoV1XLS_Recibidos">
  <input type="hidden" name="montoV2"         id="montoV2XLS_Recibidos">
  <input type="hidden" name="montoOperator"   id="montoOperatorXLS_Recibidos">

  <!-- FECHAS ÃÂ¢ÃÂÃÂ emisiÃÂÃÂ³n -->
  <input type="hidden" name="emiDateOperator" id="emiDateOperatorXLS_Recibidos">
  <input type="hidden" name="emiDateV1"       id="emiDateV1XLS_Recibidos">
  <input type="hidden" name="emiDateV2"       id="emiDateV2XLS_Recibidos">

  <!-- FECHAS ÃÂ¢ÃÂÃÂ certificaciÃÂÃÂ³n -->
  <input type="hidden" name="cerDateOperator" id="cerDateOperatorXLS_Recibidos">
  <input type="hidden" name="cerDateV1"       id="cerDateV1XLS_Recibidos">
  <input type="hidden" name="cerDateV2"       id="cerDateV2XLS_Recibidos">

  <!-- FECHAS ÃÂ¢ÃÂÃÂ cancelaciÃÂÃÂ³n -->
  <input type="hidden" name="canDateOperator" id="canDateOperatorXLS_Recibidos">
  <input type="hidden" name="canDateV1"       id="canDateV1XLS_Recibidos">
  <input type="hidden" name="canDateV2"       id="canDateV2XLS_Recibidos">

</form>



<script type="text/javascript">
$(document).ready(function() {
	/*
	$('#tipoComprobanteDescarga').select2({
		theme: 'bootstrap-5'
	});
	$('#tipoComprobanteDescarga').val('ALL'); // Selecciona primer valor del combo
	$('#tipoComprobanteDescarga').trigger('change'); //Refresca el combo
	
	$('#existeBovedaDescarga').select2({
		theme: 'bootstrap-5'
	});
	$('#existeBovedaDescarga').val('ALL'); // Selecciona primer valor del combo
	$('#existeBovedaDescarga').trigger('change'); //Refresca el combo
	
	$('#estatusCFDI').select2({
		theme: 'bootstrap-5'
	});
	$('#estatusCFDI').val('ALL'); // Selecciona primer valor del combo
	$('#estatusCFDI').trigger('change'); //Refresca el combo
	*/
	
	/*
	flatpickr(fechaInicialDescarga, {
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
 	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'ÃÂÃÂÃÂÃÂct', 'Nov', 'Dic'],
 	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
 	        },
 	      },
 	    }); 
  	 
  	 flatpickr(fechaFinalDescarga, {
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
 	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'ÃÂÃÂÃÂÃÂct', 'Nov', 'Dic'],
 	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
 	        },
 	      },
 	    }); 
  	 	*/
  	 obtenerFechasFiltroDescarga();
  	calcularEtiquetasConfDescargaSAT();
});


 function obtenerFechasMinimaDescarga(fechaInicial, fechaFinal){
	$.ajax({
		url  : '/siarex247/cumplimientoFiscal/descargaSAT/recibidos/consultarFechaMinima.action',
		type : 'POST', 
		data : null,
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			   
			} else {
				const calendarioIni = flatpickr("#fechaInicialDescarga", { 
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
				
				const calendarioFin = flatpickr("#fechaFinalDescarga", { 
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
				
				 $('#fechaInicialDescarga').val(fechaInicial);
				 $('#fechaFinalDescarga').val(fechaFinal);
				
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('obtenerFechasFiltro()_'+thrownError);
		}
	});	
			
}

function calcularEtiquetasConfDescargaSAT(){
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
					
					document.getElementById("CONF_DESCARGA_SAT_TITLE1").innerHTML = data.TITLE1;
					document.getElementById("CONF_DESCARGA_SAT_ETQ1").innerHTML = data.ETQ1;
					document.getElementById("CONF_DESCARGA_SAT_ETQ2").innerHTML = data.ETQ2;
					document.getElementById("CONF_DESCARGA_SAT_ETQ3").innerHTML = data.ETQ3;
					document.getElementById("CONF_DESCARGA_SAT_ETQ4").innerHTML = data.ETQ4;
					document.getElementById("CONF_DESCARGA_SAT_ETQ5").innerHTML = data.ETQ5;
					document.getElementById("CONF_DESCARGA_SAT_ETQ6").innerHTML = data.ETQ6;
					document.getElementById("CONF_DESCARGA_SAT_ETQ7").innerHTML = data.ETQ7;
					document.getElementById("CONF_DESCARGA_SAT_ETQ8").innerHTML = data.ETQ8;
					document.getElementById("CONF_DESCARGA_SAT_ETQ9").innerHTML = data.ETQ9;
					document.getElementById("CONF_DESCARGA_SAT_ETQ10").innerHTML = data.ETQ10;
					document.getElementById("CONF_DESCARGA_SAT_ETQ11").innerHTML = data.ETQ11;
					document.getElementById("CONF_DESCARGA_SAT_ETQ12").innerHTML = data.ETQ12;
					document.getElementById("CONF_DESCARGA_SAT_ETQ13").innerHTML = data.ETQ13;
					document.getElementById("CONF_DESCARGA_SAT_ETQ14").innerHTML = data.ETQ14;
					document.getElementById("CONF_DESCARGA_SAT_ETQ15").innerHTML = data.ETQ15;
					document.getElementById("CONF_DESCARGA_SAT_ETQ16").innerHTML = data.ETQ16;
					document.getElementById("CONF_DESCARGA_SAT_ETQ17").innerHTML = data.ETQ17;
					document.getElementById("CONF_DESCARGA_SAT_ETQ18").innerHTML = data.ETQ18;
					document.getElementById("CONF_DESCARGA_SAT_ETQ19").innerHTML = data.ETQ19;
					document.getElementById("CONF_DESCARGA_SAT_ETQ20").innerHTML = data.ETQ20;
					document.getElementById("CONF_DESCARGA_SAT_ETQ21").innerHTML = data.ETQ21;
					document.getElementById("CONF_DESCARGA_SAT_ETQ22").innerHTML = data.ETQ22;
					document.getElementById("CONF_DESCARGA_SAT_ETQ23").innerHTML = data.ETQ23;
					document.getElementById("CONF_DESCARGA_SAT_ETQ24").innerHTML = data.ETQ24;
					document.getElementById("CONF_DESCARGA_SAT_ETQ25").innerHTML = data.ETQ25;
					document.getElementById("CONF_DESCARGA_SAT_ETQ26").innerHTML = data.ETQ26;
					document.getElementById("CONF_DESCARGA_SAT_ETQ27").innerHTML = data.ETQ27;
					document.getElementById("CONF_DESCARGA_SAT_ETQ28").innerHTML = data.ETQ28;
					document.getElementById("CONF_DESCARGA_SAT_ETQ29").innerHTML = data.ETQ29;
					document.getElementById("CONF_DESCARGA_SAT_Estatus").innerHTML = data.ETQ27;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
</script>

<script>
	// Copia select del thead a los ÃÂ¢ÃÂÃÂglobalesÃÂ¢ÃÂÃÂ que viajan en AJAX
	function __copiarTheadAGlobalesR(){
	  const est = ($('#estatusFilterR').val() || '').trim();
	  $('#estatusCFDI').val(est === 'ALL' ? '' : est);
	
	  const bov = ($('#bovedaFilterR').val() || '').trim();
	  $('#existeBovedaDescarga').val(bov === 'ALL' ? '' : bov);
	}
	
	// Cambios en ESTATUS => set operador y refresca
	$(document).on('change','#estatusFilterR', function(){
	  const v = ($(this).val()||'').trim();
	  $('#estatusOperatorR').val(v && v!=='ALL' ? 'equals' : 'contains');
	  // UI del botÃÂÃÂ³n: "=" cuando hay valor, lupa cuando TODOS
	  $('#estatusOpBtnR .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
	  __copiarTheadAGlobalesR();
	  validarFechas();
	 // if (typeof refrescarRecibidos==='function') validarFechas();
	});
	

	// Cambios en ESTATUS => set operador y refresca
	$(document).on('change','#efectoFilterInputR', function(){
	  const v = ($(this).val()||'').trim();
	  $('#efectoOperatorR').val(v && v!=='ALL' ? 'equals' : 'contains');
	  // UI del botÃÂÃÂ³n: "=" cuando hay valor, lupa cuando TODOS
	  $('#efectoOpBtnR .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
	  __copiarTheadAGlobalesR();
	  validarFechas();
	 // if (typeof refrescarRecibidos==='function') validarFechas();
	});
	
	
	
	$(document).on('change','#bovedaFilterR', function(){
	  const v = ($(this).val()||'').trim();
	  $('#bovedaOperatorR').val(v && v!=='ALL' ? 'equals' : 'contains');
	  $('#bovedaOpBtnR .op-label').html(v && v!=='ALL' ? '=' : '<i class="fas fa-search"></i>');
	  __copiarTheadAGlobalesR();
	  validarFechas();
	 // if (typeof refrescarRecibidos==='function') validarFechas();
	});
	
</script>

</html>