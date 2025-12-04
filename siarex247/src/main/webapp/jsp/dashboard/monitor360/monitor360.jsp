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
    
	
    
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor">Dashboard SIAREX</h5>
			</div>
			<div class="col-auto d-flex">
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="card overflow-hidden">
			
				<div class="card-header p-0 scrollbar-overlay border-bottom">
					<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabGrafica360" data-bs-toggle="tab" href="#grafica360" role="tab" aria-controls="grafica360" aria-selected="true">
								<span class="fas fa-chart-bar text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" >Grafica Monitor360</h6>
							</a>
						</li>
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabDetalle360" data-bs-toggle="tab" href="#detalle360" role="tab" aria-controls="detalle360" aria-selected="false">
								<span class="far fa-calendar-alt icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" >Detalle Monitor360</h6>
                      		</a>
                      	</li>
                      	<li class="nav-item text-nowrap" role="presentation">
						  <a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" 
						     id="tabGraficaEgresos" 
						     data-bs-toggle="tab" 
						     href="#graficaEgresos" 
						     role="tab" 
						     aria-controls="graficaEgresos" 
						     aria-selected="false">
						    <span class="fas fa-chart-pie text-600"></span>
						    <h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" >Grafica Egresos</h6>
						  </a>
						</li>
                      	
                      	
					</ul>
				</div>
                
                <div class="tab-content">
                	
                	<div class="card-body bg-light tab-pane active" id="grafica360" role="tabpanel" aria-labelledby="tabGrafica360">
                		<h6 class="mb-0 text-700">Grafica Monitor360</h6>
                	</div>
                	
                	 <div class="card-body bg-light tab-pane" id="detalle360" role="tabpanel" aria-labelledby="tabDetalle360">
                		<h6 class="mb-0 text-700">Detalle Monitor360</h6>
                	</div>
                	<div class="card-body bg-light tab-pane" id="graficaEgresos" role="tabpanel" aria-labelledby="tabGraficaEgresos">
					  <h6 class="mb-3 text-700">Gráfica de Egresos por Tipo de Comprobante</h6>
					  <div id="contenedorGraficaEgresos" style="min-height: 350px;"></div>
					</div>
                	
                	
                	
                </div>
			
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<script type="text/javascript">

//alarmaUsuario.html

	$(document).ready(function() {
		$("#grafica360").load('/siarex247/jsp/dashboard/monitor360/graficaMonitor.jsp');
		$("#detalle360").load('/siarex247/jsp/dashboard/monitor360/detalleMetadata.jsp');
		$("#graficaEgresos").load('/siarex247/jsp/dashboard/monitor360/graficaEgresos.jsp');

		
		
		$(".nav-tabs a").click(function(){
		     $(this).tab('show');
		   // alert('cambiar tab==>'+$(this));
		   
		});
				
		$('.nav-tabs a').on('shown.bs.tab', function(event){
			var target = event.target || event.srcElement;
			var id = target.id
			
			/*
			if (id == "tabConGrafica360") {
				$("#grafica360").load('/siarex247/jsp/dashboard/monitor360/graficaMonitor.jsp');
			} else if (id == "tabDetalle360"){
				$("#detalle360").load('/siarex247/jsp/dashboard/monitor360/detalleMonitor.jsp');
			}
			*/
		});
		
		
				
	});

	 
</script>

</html>