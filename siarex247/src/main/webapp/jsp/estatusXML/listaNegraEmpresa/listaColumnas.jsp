<%@page import="com.siarex247.utils.Utils"%>
<%@page import="com.siarex247.session.ObtenerSession"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.siarex247.configuraciones.ListaNegra.ListaNegraBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.siarex247.session.SiarexSession"%>
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



<title>Insert title here</title>
</head>



<%

SiarexSession sessionSIA = ObtenerSession.getSession(request);

Date fechaActual = new Date();
SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
String fechaHoy = formatDate.format(fechaActual);

//int anioListaNegra = Integer.parseInt(fechaHoy.substring(0, 4));
int anioListaNegra = Utils.noNuloINT(request.getParameter("annioConsulta"));
if (anioListaNegra == 0){
	anioListaNegra = Integer.parseInt(fechaHoy.substring(0, 4));
}

// anioListaNegra = 2021;
ListaNegraBean listaNegra = new ListaNegraBean();
ArrayList<String> detColumnas = listaNegra.consultaColumnas(sessionSIA.getEsquemaEmpresa(), anioListaNegra);



%>
<script language="javascript">

	var columnas = [];
	columnas.push({mData: 'RFC', "sClass": "alinearCentro"});
	columnas.push({mData: 'RAZON_SOCIAL'});
	columnas.push({mData: 'SUPUESTO'});
	columnas.push({mData: 'NOMBRE_ARTICULO'});
	

</script>

					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900" >
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap" id="LISTA_NEGRA_ETQ9">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="LISTA_NEGRA_ETQ10">Razon Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="LISTA_NEGRA_ETQ11">Supuesto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="LISTA_NEGRA_ETQ12">Nombre del Articulo</th>
								<%
                                  int contColumnas = 1;
                                  for (int x = 0; x < detColumnas.size(); x++){
                                  
                                  %>
                                      <script language="javascript">
                                      	columnas.push({mData: 'DISPONIBLE_<%=contColumnas %>'});
                                      </script>
                                      <th class="sort pe-1 align-middle white-space-nowrap"><%=detColumnas.get(x) %></th>
                                  <%
                                  	contColumnas++;
                                  } %>
							</tr>
						</thead>
					</table>
					

<script type="text/javascript">
$(document).ready(function() {
	
	
	var tablaDetalle = null;
		$('#anioListaNegra').val(<%=anioListaNegra%>);
		
		$(document).ready(function() {
			try {
				tablaDetalle = $('#tablaDetalle').DataTable( {
					paging      : true,
					retrieve: true,
					pageLength  : 50,
					lengthChange: false,
//					dom: 'Blfrtip',
//					dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
					dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
					ordering    : true,
					serverSide	: true,
					fixedHeader : true,
					orderCellsTop: true,
					info		: true,
					select      : false,
					stateSave	: false, 
					order       : [ [ 0, 'asc' ] ],	
					
					buttons: [
						/*
						{ 	text: '<div id="btnFilter_Visor">  <span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1"> <a class="me-1 mb-1 btn-sm btnClr" href="#filtrosBusquedaVisor" data-bs-toggle="collapse" aria-expanded="false" aria-controls="filtrosBusquedaVisor"> Filtros </a>       </span> </div>',
		                	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
		                    action: function ( e, dt, node, config ) {
		                    	
		                    },
		                }
						*/
					],
					language : {
						processing:     "Procesando...",
						zeroRecords:    "No se encontraron resultados",
						emptyTable:     "Ningún dato disponible en esta tabla",
						info:           "Mostrando _START_ al _END_ de _TOTAL_ registros",
						infoEmpty:      "No hay registros disponibles",
						infoFiltered:   "(filtrado de un total de _MAX_ registros)",
						infoPostFix:    "",
						search:         "Buscar:",
						url:            "",
						infoThousands:  ",",
						loadingRecords: "Cargando...",
						oPaginate: {
					           sFirst : "Primero",
					           sLast  : "Último",
					           sNext  : "<span class='fa fa-chevron-right fa-w-10'></span>",
					           sPrevious : "<span class='fa fa-chevron-left fa-w-10'></span>"
					   	}
					},
					ajax : {
						url: '/siarex247/cumplimientoFiscal/listaNegraEmpresa/detalleListaNegra.action',
						beforeSend: function( xhr ) {
		        			$('#overSeccion').css({display:'block'});
		        		},
		        		complete: function(jqXHR, textStatus){
				    		  $('#overSeccion').css({display:'none'});
					    },
						data : {
							razonSocial: function() { return obtenerRS(); },
							rfcListaNegra: function() { return obtenerRLN(); },
							idSupuesto: function() { return obtenerIDS(); },
							idNombreArticulo: function() { return obtenerIDA(); },
							anioListaNegra: function() { return obtenerALN(); },
							tipoFactura : function() { return obtenerTipoFactura(); }
						},
						type: 'POST'
					},
					aoColumns : columnas,
					initComplete: function () {
			        	  var btns = $('.dt-button');
			              btns.removeClass('dt-button');
			              
			              var btnsSubMenu = $('.dtb-b2');
			              btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');
			              
			           },
					  drawCallback: function () {
						  
					  }
				});
				
			} catch(e) {
				alert('usuarios()_'+e);
			};
			
			tablaDetalle.on( 'draw', function () {
				 $('[data-toggle="tooltip"]').tooltip();
			} );
				
		}); 
		
		// Aqui se agrega los filtros del encabezado
		$('#tablaDetalle thead tr:eq(1) th').each( function (i) {
			 var title = $(this).text();
			 if (i == 19){
			 } else {
				 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
			 }
		});
		
		$('#tablaDetalle thead tr:eq(1) th').on('keyup', "input",function () {
			filtraDatos($(this).parent().index(), this.value);
		});
			
	 
});

function filtraDatos(columna, texto) {
	tablaDetalle
		.column(columna)
        .search(texto)
        .draw();
}




</script>



				
						
</html>