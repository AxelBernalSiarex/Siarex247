<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
 long t = System.currentTimeMillis();
%>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        .contenido {
            display: block;
            width: 100%;
            word-wrap: break-word;
            font-size: 1rem;
            height: 30px;
        }
    </style>
    
    <script src="/theme-falcon/vendors/echarts/echarts.min.js"></script>
    <script src="/theme-falcon/assets/js/echarts-example.js"></script>
    <script src="/theme-falcon/vendors/dayjs/dayjs.min.js"></script>
    
    <script src="/siarex247/jsp/dashboard/monitor360/graficaMonitor.js?t=<%=t%>"></script>
    <link href="/theme-falcon/assets/cssV21/user-rtl.min.css" rel="stylesheet" id="user-style-rtl">
    <link href="/theme-falcon/assets/cssV21/user.min.css" rel="stylesheet" id="user-style-default">
	<link href="/theme-falcon/assets/cssV21/user-rtl.min.css" rel="stylesheet" id="user-style-rtl">
	<link href="/theme-falcon/assets/cssV21/user.min.css" rel="stylesheet" id="user-style-default">

	<link href="/theme-falcon/assets/cssV21/theme.css" rel="stylesheet" id="style-default">
	<link href="/theme-falcon/assets/cssV21/user-rtl.css" rel="stylesheet" id="user-style-rtl">
	<link href="/theme-falcon/assets/cssV21/user.css" rel="stylesheet" id="user-style-default">
    
    
</head>


<div class="row mb-3">
    <div class="col">
        <div class="card bg-100 shadow-none border">
            <div class="row gx-0 flex-between-center">
                <div class="col-sm-auto d-flex align-items-center"><img class="ms-n2"
                        src="/theme-falcon/assets/img/illustrations/crm-bar-chart.png" alt="" width="90">
                    <div>
                        <h6 class="text-primary fs-10 mb-0">Monitor</h6>
                        <h4 class="text-primary fw-bold mb-0">CFDI 360 - <span
                                class="text-info fw-medium">Resumén</span></h4>
                    </div><img class="ms-n4 d-md-none d-lg-block" src="/theme-falcon/assets/img/illustrations/crm-line-chart.png"
                        alt="" width="150">
                </div>
            </div>
        </div>

        <div class="card-body bg-body-tertiary mt-4">

				<div class="card-header">
					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="cmbAnnio"> Año :</label>
						<div class="col-sm-2" style="z-index: 0;">
							<div class="form-group">
							<select class="form-select" id="cmbAnnio" name="annio" onchange="muestraGrafica();">
							
							</select>
							</div>
						</div>
						
						<label class="col-sm-2 col-form-label" for="cmbMes">Mes :</label>
						<div class="col-sm-3" style="z-index: 0;">
							<div class="form-group">
							<select class="form-select" id="cmbMes" name="mes" onchange="muestraGrafica();">
								<option value="">Seleccione un Mes ...</option>
                                <option value="01">Enero</option>
                                <option value="02">Febrero</option>
                                <option value="03">Marzo</option>
                                <option value="04">Abril</option>
                                <option value="05">Mayo</option>
                                <option value="06">Junio</option>
                                <option value="07">Julio</option>
                                <option value="08">Agosto</option>
                                <option value="09">Septiembre</option>
                                <option value="10">Octubre</option>
                                <option value="11">Noviembre</option>
                                <option value="12">Diciembre</option>
							</select>
							</div>
						</div>
						
						<label class="col-sm-1 col-form-label" for="cmbTipo">Tipo :</label>
						<div class="col-sm-2" style="z-index: 0;">
							<div class="form-group">
							<select class="form-select" id="cmbTipo" name="tipo" onchange="muestraGrafica(); cargaContribuyentes();">
							 	<option value="">Seleccione un Tipo ...</option>
                                <option value="1">Emitidos</option>
                                <option value="2">Recibidos</option>
							</select>
							</div>
						</div>
					</div>
					
					
					<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="cmbContribuyentes">Contribuyente :</label>
							<div class="col-sm-2" style="z-index: 0;">
								<div class="form-group">
								<select class="form-select" id="cmbContribuyentes" name="rfcContribuyentes" onchange="muestraGrafica();">
								</select>
								</div>
							</div>
							
							<label class="col-sm-2 col-form-label" for="cmbTipoMoneda">Tipo de Moneda :</label>
							<div class="col-sm-3" style="z-index: 0;">
								<div class="form-group">
								<select class="form-select" id="cmbTipoMoneda" name="tipoMoneda" onchange="muestraGrafica();">
                                	<option value="MXN">MXN</option>
                                	<option value="USD">USD</option>
								</select>
								</div>
							</div>
					</div>
				</div>



            <div class="row g-3 mb-4">
                <div class="col">
                    <div class="row justify-content-md-center">
 						<div id="overSeccion_Totales" class="overlay" style="display: none;text-align: left;">
								<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
						</div>
									
                        <div class="col-md-3 col-sm-12 mb-3">
                            <div class="card h-md-100" style="cursor: pointer;" onclick="abrirDetalle('UNIVERSO');">
                                <div class="card-header pb-0">
                                    <span class="badge badge bg-primary contenido  ">Universo XML</span>
                                </div>
                                <div class="card-body d-flex flex-column justify-content-end">
                                    <div class="row justify-content-center">
                                        <div class="col-auto align-self-end">
                                            <div class="fs-6 fw-normal font-sans-serif text-500 lh-1 mb-1" id="totalUniverso" >0</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-3 col-sm-12 mb-3">
                            <div class="card h-md-100" style="cursor: pointer;"  onclick="abrirDetalle('METADATA');">
                                <div class="card-header pb-0">
                                    <span class="badge badge bg-primary contenido ">% XML vs Metadata</span>
                                </div>
                                <div class="card-body d-flex flex-column justify-content-end">
                                    <div class="row justify-content-center">
                                        <div class="col-auto align-self-end">
                                            <div class="fs-6 fw-normal font-sans-serif text-500 lh-1 mb-1" id="porcentajeMetadata" >0 %</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-2 col-sm-12 mb-3">
                            <div class="card h-md-100" style="cursor: pointer;"  onclick="abrirDetalle('LISTA_NEGRA');">
                                <div class="card-header pb-0">
                                    <span class="badge badge bg-primary contenido ">RFC en LN 69 y 69B</span>
                                </div>
                                <div class="card-body d-flex flex-column justify-content-end">
                                    <div class="row justify-content-center">
                                        <div class="col-auto align-self-end">
                                            <div class="fs-6 fw-normal font-sans-serif text-500 lh-1 mb-1" id="totalListaNegra">0</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-2 col-sm-12 mb-3">
                            <div class="card h-md-100" style="cursor: pointer;" onclick="abrirDetalle('CANCELADOS');">
                                <div class="card-header pb-0">
                                    <span class="badge badge bg-primary contenido ">% UUID Cancelados</span>
                                </div>
                                <div class="card-body d-flex flex-column justify-content-end">
                                    <div class="row justify-content-center">
                                        <div class="col-auto align-self-end">
                                            <div class="fs-6 fw-normal font-sans-serif text-500 lh-1 mb-1" id="porcentajeCancelados" >0 %</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-2 col-sm-12 mb-3">
                            <div class="card h-md-100">
                                <div class="card-header pb-0">
                                    <span class="badge badge bg-primary contenido ">% PPD / CRP</span>
                                </div>
                                <div class="card-body d-flex flex-column justify-content-end">
                                    <div class="row justify-content-center">
                                        <div class="col-auto align-self-end">
                                            <div class="fs-6 fw-normal font-sans-serif text-500 lh-1 mb-1" id="porcentajeComplementos">0 %</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

           <div class="col-lg-12">
              <div class="card h-100">
                <div class="card-header">
                  <div class="row flex-between-end">
                    <div class="col-auto align-self-center">
                      <h5 class="mb-0" data-anchor="data-anchor">Monitor 360</h5>
                      <div id="overSeccion_Grafica" class="overlay" style="display: none;text-align: left;">
								<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
						</div>
                    </div>
                  </div>
                </div>
                <div class="card-body bg-light">
                  <div class="tab-content">
                    <div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-09f8c910-2305-4eba-9e2f-aa6feb2c92e2" id="dom-09f8c910-2305-4eba-9e2f-aa6feb2c92e2">
                      <!-- Find the JS file for the following chart at: src/js/charts/echarts/examples/bar-line-mixed-chart.js-->
                      <!-- If you are not using gulp based workflow, you can find the transpiled code at: public/assets/js/echarts-example.js-->
                      <div class="echart-bar-line-chart-exampleMonitor360" style="min-height: 350px;" data-echart-responsive="true"></div>
                    </div>
                    
                  </div>
                </div>
              </div>
            </div>


            <div class="container my-4">
                <div class="row">
                    <!-- Card principal -->
                    <div class="col-12">
                        <div class="card shadow">
                            <div class="card-body">
                                
                                <div class="row text-center">
	                                <div id="overSeccion_Ingresos" class="overlay" style="display: none;text-align: right;">
										<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
									</div>
									
                                    <!-- Primera columna: Ingresos y Egresos -->
                                    <div class="col-md-3 mb-3 mb-md-0">
                                        <div class="d-flex flex-column align-items-center">
                                            <div class="bg-primary text-white p-2 rounded-pill mb-2"
                                                style="width: 100px;">Ingresos</div>
                                            <div class="text-white p-2 rounded-pill"
                                                style="width: 100px; background-color:#27BCFD;cursor: pointer;"
                                               onclick="activarTabGraficaEgresos()">
                                                Egresos</div>
                                        </div>
                                    </div>
                                    <!-- Segunda columna: Subtotal -->
                                    <div class="col-md-3 mb-3 mb-md-0">
                                        <div class="d-flex flex-column align-items-left">
                                            <p class="fw-bold mb-1">Subtotal</p>
                                            <h4 class="text-primary fw-bold" id="subTotalIngresos">$ 0 </h4> <!-- Subtotal Ingresos -->
                                            <h4 class="text-secondary fw-bold" id="subTotalEgresos">$ 0 </h4> <!-- Subtotal Egresos -->
                                        </div>
                                    </div>
                                    <!-- Tercera columna: Impuestos y Retenciones -->
                                    <div class="col-md-3 mb-3 mb-md-0">
                                        <div class="d-flex flex-column align-items-left">
                                            <p class="fw-bold mb-1">Impuestos y Retenciones</p>
                                            <h4 class="text-primary fw-bold" id="retencionesIngresos">$ 0</h4> <!-- Impuestos -->
                                            <h4 class="text-secondary fw-bold" id="retencionesEgresos">$ 0</h4> <!-- Retenciones -->
                                        </div>
                                    </div>
                                    <!-- Cuarta columna: Total -->
                                    <div class="col-md-3">
                                        <div class="d-flex flex-column align-items-left">
                                            <p class="fw-bold mb-1">Total </p>
                                            <h4 class="text-primary fw-bold" id="totalIngresos"></h4> <!-- Total ingresos -->
                                            <h4 class="text-secondary fw-bold" id="totalEgresos">$ 0</h4> <!-- Total egresos -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<script type="text/javascript">


$(document).ready(function () {
	$('#cmbAnnio').select2({
		theme: 'bootstrap-5'
	});
	$('#cmbMes').select2({
		theme: 'bootstrap-5'
	});
	$('#cmbTipo').select2({
		theme: 'bootstrap-5'
	});
	$('#cmbContribuyentes').select2({
		theme: 'bootstrap-5'
	});
	
	$('#cmbTipoMoneda').select2({
		theme: 'bootstrap-5'
	});
	
	
	cargaAnnios();
	// cargaContribuyentes();
	
});


function cargaAnnios() {
	try {
		var bandPrimero = true;
		$('#cmbAnnio').empty();
		$.ajax({
           url:  '/siarex247/visor/monitor360/obtenerAnnios.action',
           type: 'POST',
            dataType : 'json',
		    success: function(data){
		    	$('#cmbAnnio').empty();
		    	$.each(data.data, function(key, text) {
			    	$('#cmbAnnio').append($('<option></option>').attr('value', text.annio).text(text.desAnnio));
			    	if (bandPrimero){
			    		muestraGrafica();
			    		bandPrimero = false;
			    	}
			    	
			    	
		      	});
		    }
		});
	}catch(e) {
		alert("cargaAnnios()_"+e);
	} 
}

  

function cargaContribuyentes() {
	try {
		var bandPrimero = true;
		var tipo = $('#cmbTipo').val();
		var tipoMoneda = $('#cmbTipoMoneda').val();
		$('#cmbContribuyentes').empty();
		$.ajax({
           url:  '/siarex247/visor/monitor360/obtenerContribuyentes.action',
           data : {
        	   tipo : tipo,
        	   tipoMoneda : tipoMoneda
           },
           type: 'POST',
            dataType : 'json',
		    success: function(data){
		    	$('#cmbContribuyentes').empty();
		    	$.each(data.data, function(key, text) {
			    	$('#cmbContribuyentes').append($('<option></option>').attr('value', text.rfc).text(text.razonSocial));
		      	});
		    }
		});
	}catch(e) {
		alert("cargaContribuyentes()_"+e);
	} 
}

  
function calcularCabecero(){
	var annio = $('#cmbAnnio').val();
	var mes = $('#cmbMes').val();
	var tipo = $('#cmbTipo').val();
	var tipoMoneda = $('#cmbTipoMoneda').val();
	
	var contribuyente = $('#cmbContribuyentes').val();
	if (contribuyente == null ){
		contribuyente = '';
	}
	
	$.ajax({
		url  : '/siarex247/visor/monitor360/calcularCabecero.action',
		type : 'POST', 
		data : {
			annio : annio,
			mes   : mes,
			tipo  : tipo,
			contribuyente : contribuyente,
			tipoMoneda : tipoMoneda
		},
		beforeSend: function( xhr ) {
			$('#overSeccion_Totales').css({display:'block'});
		},
		complete: function(jqXHR, textStatus){
  		  $('#overSeccion_Totales').css({display:'none'});
	    },
	    
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			   
			} else {
				
				document.getElementById("totalUniverso").innerHTML = data.totalUniverso;
				document.getElementById("porcentajeMetadata").innerHTML = data.porcentajeMetadata;
				document.getElementById("porcentajeCancelados").innerHTML = data.porcentajeCancelados;
				document.getElementById("porcentajeComplementos").innerHTML = data.porcentajeComplementos;
				document.getElementById("totalListaNegra").innerHTML = data.totalListaNegra;
				
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('calcularCabecero()_'+thrownError);
		}
	});	
	
}


function calcularIngresos(){
	var annio = $('#cmbAnnio').val();
	var mes = $('#cmbMes').val();
	var tipo = $('#cmbTipo').val();
	var tipoMoneda = $('#cmbTipoMoneda').val();
	
	var contribuyente = $('#cmbContribuyentes').val();
	if (contribuyente == null ){
		contribuyente = '';
	}
	
	$.ajax({
		url  : '/siarex247/visor/monitor360/calcularIngresos.action',
		type : 'POST', 
		data : {
			annio : annio,
			mes   : mes,
			tipo  : tipo,
			contribuyente : contribuyente,
			tipoMoneda : tipoMoneda
		},
		beforeSend: function( xhr ) {
			$('#overSeccion_Ingresos').css({display:'block'});
		},
		complete: function(jqXHR, textStatus){
  		  $('#overSeccion_Ingresos').css({display:'none'});
	    },
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			   
			} else {
				
				document.getElementById("subTotalIngresos").innerHTML = data.subTotalIngreso;
				document.getElementById("subTotalEgresos").innerHTML = data.subTotaEngreso;
				document.getElementById("retencionesIngresos").innerHTML = data.impRetenidoIngreso;
				document.getElementById("retencionesEgresos").innerHTML = data.impRetenidoEgreso;
				document.getElementById("totalIngresos").innerHTML = data.totalIngreso;
				document.getElementById("totalEgresos").innerHTML = data.totalEgreso;

				
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('calcularIngresos()_'+thrownError);
		}
	});	
	
}

  function abrirDetalle(tipoConsulta){
	    var annio = $('#cmbAnnio').val();
		var mes = $('#cmbMes').val();
		var tipo = $('#cmbTipo').val();
		var tipoMoneda = $('#cmbTipoMoneda').val();
		
		var contribuyente = $('#cmbContribuyentes').val();
		if (contribuyente == null ){
			contribuyente = '';
		}
		try{
			
			// alert('abriendo detalle...');
			// $("#detalle360").click();
			// document.getElementById('detalle360').click();
			if (tipoConsulta == 'LISTA_NEGRA'){
				$("#detalle360").load('/siarex247/jsp/dashboard/monitor360/detalleListanegra.jsp?tipoConsulta='+tipoConsulta+'&annio='+annio+'&mes='+mes+'&tipo='+tipo+'&contribuyente='+contribuyente+'&tipoMoneda='+tipoMoneda);
			}else{
				$("#detalle360").load('/siarex247/jsp/dashboard/monitor360/detalleMetadata.jsp?tipoConsulta='+tipoConsulta+'&annio='+annio+'&mes='+mes+'&tipo='+tipo+'&contribuyente='+contribuyente+'&tipoMoneda='+tipoMoneda);	
			}
			
			
			$("#tabDetalle360").attr('aria-selected', 'true');
			$("#tabDetalle360").addClass("active");
			$("#detalle360").addClass("active");
		
			$("#tabGrafica360").attr('aria-selected', 'false');
			$("#tabGrafica360").removeClass("active");
			$("#grafica360").removeClass("active");
			

		}catch(e){
			alert('abrirDetalle()_'+e);
		}
  }
</script>

</html>