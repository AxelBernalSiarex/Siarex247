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
	<script src="/siarex247/jsp/seguridad/perfiles/perfiles.js?v=<%=Utils.VERSION%>"></script>

	
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor">Perfiles</h5>
			</div>
			<div class="col-auto d-flex">
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							 <tr>
							 	<th class="sort pe-1 align-middle white-space-nowrap">Acciones</th>
                                <th class="sort pe-1 align-middle white-space-nowrap">Id. Perfil</th>
								<th class="sort pe-1 align-middle white-space-nowrap">Nombre Corto</th>
								<th class="sort pe-1 align-middle white-space-nowrap">Descripci&oacute;n</th>
								
                              </tr>
							
                               <tr class="forFilters">
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

<script type="text/javascript">
	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/seguridad/perfiles/modalPerfiles.jsp');  
	});
</script>

</html>