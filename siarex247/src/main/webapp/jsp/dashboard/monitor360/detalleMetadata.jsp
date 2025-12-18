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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<script src="/siarex247/jsp/dashboard/monitor360/detalleMetadata.js"></script>


<%

String tipoConsulta = Utils.noNulo(request.getParameter("tipoConsulta"));
int annio = Utils.noNuloINT(request.getParameter("annio"));
String mes = Utils.noNulo(request.getParameter("mes"));
String tipo = Utils.noNulo(request.getParameter("tipo"));
String contribuyente = Utils.noNulo(request.getParameter("contribuyente"));
String tipoMoneda = Utils.noNulo(request.getParameter("tipoMoneda"));

%>

</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">
	<input type="hidden" name="TIPOCONSULTA_DETALLE" id="TIPOCONSULTA_DETALLE" value="<%=tipoConsulta %>">
	<input type="hidden" name="ANNIO_DETALLE" id="ANNIO_DETALLE" value="<%=annio %>">
	<input type="hidden" name="MES_DETALLE" id="MES_DETALLE" value="<%=mes %>">
	<input type="hidden" name="TIPO_DETALLE" id="TIPO_DETALLE" value="<%=tipo %>">
	<input type="hidden" name="CONTRIBUYENTE_DETALLE" id="CONTRIBUYENTE_DETALLE" value="<%=contribuyente %>">
	<input type="hidden" name="TIPOMONEDA_DETALLE" id="TIPOMONEDA_DETALLE" value="<%=tipoMoneda %>">
	
	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
			    <% if ("UNIVERSO".equalsIgnoreCase(tipoConsulta)){ %>
					<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE1">Resumen Universo UUID </h5>
				<%}else if ("CANCELADOS".equalsIgnoreCase(tipoConsulta)) { %>
					<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE1">Resumen Cancelados UUID </h5>
				<%} %>	
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ30">Tipo de Moneda</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ27">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ28">Fecha Cancelaci&oacute;n</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_DESCARGA_SAT_ETQ29">Existe Boveda</th>
							</tr>
							<tr class="forFilters">
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
							</tr>
							
						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

</html>