<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <script src="/siarex247/jsp/visor/tableroVisor.js?v=<%=Utils.VERSION%>"></script>
  <script src="/siarex247/jsp/visor/listaProveedoresUSD.js"></script>
  <script src="/siarex247/jsp/visor/listaProveedoresMXN.js"></script>
  <script src="/siarex247/jsp/visor/listaOrdenes.js"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

  <div class="card-header">
    <div class="row flex-between-end">
      <div class="col-auto align-self-center">
        <h5 class="mb-0" data-anchor="data-anchor">Visor</h5>
      </div>
    </div>
  </div>

  <div class="card-body bg-light pt-0" style="padding-top: 10px !important;">


    <div class="row g-3 mb-3">

      <div class="col-sm-12 col-md-4">
        <div class="card overflow-hidden" style="min-width: 12rem">
          <div class="bg-holder bg-card"
            style="background-image:url(/theme-falcon/assets/img/icons/spot-illustrations/corner-1.png);">
          </div>
          <!--/.bg-holder-->

          <div class="card-body position-relative">
            <h6 id="VISOR_ETQ1">Total de Ordenes</h6>
            <div class="display-4 fs-4 mb-2 fw-normal font-sans-serif text-warning"
              data-countup='{"endValue":58.386,"decimalPlaces":2,"suffix":"k"}' id="totalOrdenes"> 0 </div>
              <a id="VISOR_ETQ2" class="fw-semi-bold fs--1 text-nowrap" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp');"> Ver Ordenes<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
          </div>
        </div>
      </div>

      <div class="col-sm-12 col-md-4">
        <div class="card overflow-hidden" style="min-width: 12rem">
          <div class="bg-holder bg-card"
            style="background-image:url(/theme-falcon/assets/img/icons/spot-illustrations/corner-2.png);">
          </div>
          <!--/.bg-holder-->

          <div class="card-body position-relative">
            <h6>USD</h6>
            <div class="display-4 fs-4 mb-2 fw-normal font-sans-serif text-info"
              data-countup='{"endValue":23.434,"decimalPlaces":2,"suffix":"k"}' id="totalOrdenesUSD">0</div><a id="VISOR_ETQ3"
              class="fw-semi-bold fs--1 text-nowrap" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?tipoMoneda=USD');" >Ver Ordenes<span
                class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
          </div>
        </div>
      </div>

      <div class="col-sm-12 col-md-4">
        <div class="card overflow-hidden" style="min-width: 12rem">
          <div class="bg-holder bg-card"
            style="background-image:url(/theme-falcon/assets/img/icons/spot-illustrations/corner-3.png);">
          </div>
          <!--/.bg-holder-->

          <div class="card-body position-relative">
            <h6>MXN</h6>
            <div class="display-4 fs-4 mb-2 fw-normal font-sans-serif" data-countup='{"endValue":43594,"prefix":"$"}' id="totalOrdenesMXN">0
            </div><a id="VISOR_ETQ4" class="fw-semi-bold fs--1 text-nowrap" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?tipoMoneda=MXN');">Ver Ordenes<span
                class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
          </div>
        </div>
      </div>

    </div>

    <div class="row g-3 mb-3">

      <div class="col-lg-6 pe-lg-2 mb-3">
        <div class="card h-lg-100 overflow-hidden">
          <div class="card-header bg-light">
            <div class="row align-items-center">
              <div class="col">
                <h6 class="mb-0" id="VISOR_ETQ5">Estatus Ordenes de Compra</h6>
              </div>
            </div>
          </div>
          <div class="card-body p-0">
          
			<div class="row g-0 align-items-center py-2 position-relative">
              <div class="col ps-x1 py-1 position-static">
                <div class="d-flex align-items-center">
                <!-- 
                  <div class="avatar avatar-xl me-3">
                    <div class="avatar-name rounded-circle bg-danger-subtle text-dark"><span class="fs-0 text-danger">A5</span></div>
                  </div>
                  --> 
                  <div class="flex-1">
                    <h6 class="mb-0 d-flex align-items-center"><a id="VISOR_ETQ10" class="text-800 stretched-link" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=A5');">Servicio no recibido y sin factura</a>
                    	<span class="badge rounded-pill ms-2 bg-200 text-primary" id="porcentajeA5">0%</span>
                    </h6>
                  </div>
                </div>
              </div>
              <div class="col py-1">
                <div class="row flex-end-center g-0">
                  <div class="col-auto pe-2">
                    <div class="fs--1 fw-semi-bold" id="totalEstatusA5">0</div>
                  </div>
                  <div class="col-5 pe-x1 ps-2" id="divProgressBarA5">
                  <!-- 
                    <div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="70"
                      aria-valuemin="0" aria-valuemax="100">
                      <div class="progress-bar rounded-pill" style="width: 70%"></div>
                    </div>
                    -->
                    
                  </div>
                </div>
              </div>
            </div>          
          
            <div class="row g-0 align-items-center py-2 position-relative border-bottom border-200">
              <div class="col ps-x1 py-1 position-static">
                <div class="d-flex align-items-center">
                <!-- 
                  <div class="avatar avatar-xl me-3">
                    <div class="avatar-name rounded-circle bg-primary-subtle text-dark"><span
                        class="fs-0 text-primary">A1</span></div>
                  </div>
                  --> 
                  <div class="flex-1">
                    <h6 class="mb-0 d-flex align-items-center"><a id="VISOR_ETQ6" class="text-800 stretched-link"
                        href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=A1');">Servicio no recibido y con factura</a>
                        	<span class="badge rounded-pill ms-2 bg-200 text-primary" id="porcentajeA1">0%</span>
                    </h6>
                  </div>
                </div>
              </div>
              <div class="col py-1">
                <div class="row flex-end-center g-0">
                  <div class="col-auto pe-2">
                    <div class="fs--1 fw-semi-bold" id="totalEstatusA1">0</div>
                  </div>
                  <div class="col-5 pe-x1 ps-2" id="divProgressBarA1">
                    <!-- 
                    <div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="3" aria-valuemin="0" aria-valuemax="100">
                      <div class="progress-bar rounded-pill" style="width: 21.43%"></div>
                    </div>
                    --> 
                  </div>
                </div>
              </div>
            </div>
            <div class="row g-0 align-items-center py-2 position-relative border-bottom border-200">
              <div class="col ps-x1 py-1 position-static">
                <div class="d-flex align-items-center">
                <!-- 
                  <div class="avatar avatar-xl me-3">
                    <div class="avatar-name rounded-circle bg-success-subtle text-dark"><span
                        class="fs-0 text-success">A2</span></div>
                  </div>
                  --> 
                  <div class="flex-1">
                    <h6 class="mb-0 d-flex align-items-center"><a id="VISOR_ETQ7" class="text-800 stretched-link" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=A2');">Servicio recibido y sin factura</a>
                    <span class="badge rounded-pill ms-2 bg-200 text-primary" id="porcentajeA2">0%</span>
                    </h6>
                  </div>
                </div>
              </div>
              <div class="col py-1">
                <div class="row flex-end-center g-0">
                  <div class="col-auto pe-2">
                    <div class="fs--1 fw-semi-bold" id="totalEstatusA2">0</div>
                  </div>
                  <div class="col-5 pe-x1 ps-2" id="divProgressBarA2">
                    <!-- 
                    <div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="79" aria-valuemin="0" aria-valuemax="100">
                      <div class="progress-bar rounded-pill" style="width: 79%"></div>
                    </div>
                     -->
                  </div>
                </div>
              </div>
            </div>
            <div class="row g-0 align-items-center py-2 position-relative border-bottom border-200">
              <div class="col ps-x1 py-1 position-static">
                <div class="d-flex align-items-center">
                <!-- 
                  <div class="avatar avatar-xl me-3">
                    <div class="avatar-name rounded-circle bg-info-subtle text-dark"><span
                        class="fs-0 text-info">A3</span></div>
                  </div>
                  --> 
                  <div class="flex-1">
                    <h6 class="mb-0 d-flex align-items-center"><a id="VISOR_ETQ8"  class="text-800 stretched-link" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=A3');">Factura lista para pago</a>
                    <span class="badge rounded-pill ms-2 bg-200 text-primary" id="porcentajeA3">0%</span></h6>
                  </div>
                </div>
              </div>
              <div class="col py-1">
                <div class="row flex-end-center g-0">
                  <div class="col-auto pe-2">
                    <div class="fs--1 fw-semi-bold" id="totalEstatusA3">0</div>
                  </div>
                  <div class="col-5 pe-x1 ps-2" id="divProgressBarA3">
                  
                  <!-- 
                    <div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="90"
                      aria-valuemin="0" aria-valuemax="100">
                      <div class="progress-bar rounded-pill" style="width: 90%"></div>
                    </div>
                  -->
                   
                  </div>
                </div>
              </div>
            </div>
            <div class="row g-0 align-items-center py-2 position-relative border-bottom border-200">
              <div class="col ps-x1 py-1 position-static">
                <div class="d-flex align-items-center">
                <!-- 
                  <div class="avatar avatar-xl me-3">
                    <div class="avatar-name rounded-circle bg-warning-subtle text-dark"><span
                        class="fs-0 text-warning">A4</span></div>
                  </div>
                  --> 
                  <div class="flex-1">
                    <h6 class="mb-0 d-flex align-items-center"><a id="VISOR_ETQ9" class="text-800 stretched-link" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=A4');">Factura pagada sin complemento de pago</a>
                    <span class="badge rounded-pill ms-2 bg-200 text-primary" id="porcentajeA4">0%</span></h6>
                  </div>
                </div>
              </div>
              <div class="col py-1">
                <div class="row flex-end-center g-0">
                  <div class="col-auto pe-2">
                    <div class="fs--1 fw-semi-bold" id="totalEstatusA4">0</div>
                  </div>
                  <div class="col-5 pe-x1 ps-2" id="divProgressBarA4">
                  
                  <!-- 
                    <div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="40"
                      aria-valuemin="0" aria-valuemax="100">
                      <div class="progress-bar rounded-pill" style="width: 40%"></div>
                    </div>
                   -->  
                  </div>
                </div>
              </div>
            </div>
            
            <div class="row g-0 align-items-center py-2 position-relative">
              <div class="col ps-x1 py-1 position-static">
                <div class="d-flex align-items-center">
                <!-- 
                  <div class="avatar avatar-xl me-3">
                    <div class="avatar-name rounded-circle bg-danger-subtle text-dark"><span class="fs-0 text-danger">A6</span></div>
                  </div>
                  --> 
                  <div class="flex-1">
                    <h6 class="mb-0 d-flex align-items-center"><a id="VISOR_ETQ105" class="text-800 stretched-link" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=A6');">Factura pagada con complemento de pago</a>
                    	<span class="badge rounded-pill ms-2 bg-200 text-primary" id="porcentajeA6">0%</span>
                    </h6>
                  </div>
                </div>
              </div>
              <div class="col py-1">
                <div class="row flex-end-center g-0">
                  <div class="col-auto pe-2">
                    <div class="fs--1 fw-semi-bold" id="totalEstatusA6">0</div>
                  </div>
                  <div class="col-5 pe-x1 ps-2" id="divProgressBarA6">
                  <!-- 
                    <div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="70"
                      aria-valuemin="0" aria-valuemax="100">
                      <div class="progress-bar rounded-pill" style="width: 70%"></div>
                    </div>
                    -->
                    
                  </div>
                </div>
              </div>
            </div>
            
          </div>
          <div class="card-footer bg-light p-0">
            <!--  Eliminar Pillo 
          	<a class="btn btn-sm btn-link d-block w-100 py-2"  href="javascript:openModal()">Ver todos los estatus<svg class="svg-inline--fa fa-chevron-right fa-w-10 ms-1 fs--2" aria-hidden="true"
                focusable="false" data-prefix="fas" data-icon="chevron-right" role="img"
                xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512" data-fa-i2svg="">
                <path fill="currentColor"
                  d="M285.476 272.971L91.132 467.314c-9.373 9.373-24.569 9.373-33.941 0l-22.667-22.667c-9.357-9.357-9.375-24.522-.04-33.901L188.505 256 34.484 101.255c-9.335-9.379-9.317-24.544.04-33.901l22.667-22.667c9.373-9.373 24.569-9.373 33.941 0L285.475 239.03c9.373 9.372 9.373 24.568.001 33.941z">
                </path>
              </svg>
            </a>
            -->
          </div>
        </div>
      </div>


      <div class="col-lg-6 pe-lg-2 mb-3">
        <div class="card h-lg-100 overflow-hidden">
          <div class="card-header bg-light">
            <div class="row align-items-center">
              <div class="col">
                <h6 class="mb-0" id="VISOR_ETQ11">Totales de Ordenes de Compra</h6>
              </div>
            </div>
          </div>
          <div class="card py-3 mb-3">
            <div class="card-body py-3">
              <div class="row g-0">
                <div class="col-6 col-md-4 border-200 border-bottom border-end pb-4">
                  <h6 class="pb-1 text-700 fs--2" id="VISOR_ETQ12">Ordenes de Compra </h6>
                  <p class="font-sans-serif lh-1 mb-1 fs-2" id="totalOrdenesClasificacion">0 </p>
                  <div class="d-flex align-items-center">
                    <div class="row g-0 my-3">
                    <!--  Eliminar Pillo 
                      <a class="fw-semi-bold fs--2 text-nowrap" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp');">Ver Ordenes<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
                     -->  
                    </div>
                  </div>
                  
                </div>
                
                <div class="col-6 col-md-4 border-200 border-bottom border-end-md pb-4 ps-3">
                  <h6 class="pb-1 text-700 fs--2" id="VISOR_ETQ13">Facturas </h6>
                  <p class="font-sans-serif lh-1 mb-1 fs-2" id="totalFacturas">0 </p>
                  <div class="d-flex align-items-center">
                    <div class="row g-0 my-3">
                    <!--  Eliminar Pillo 
                      <a class="fw-semi-bold fs--2 text-nowrap" href="javascript:mostrarOpcion('/siarex247/jsp/visor/detalleVisor.jsp?estatusOrden=FACTURAS');">Ver Facturas<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
                      -->
                    </div>
                  </div>
                </div>

                <div
                  class="col-6 col-md-4 border-200 border-bottom border-end border-end-md-0 pb-4 pt-4 pt-md-0 ps-md-3">
                   <h6 class="pb-1 text-700 fs--2" id="VISOR_ETQ14">Complementos de Pago </h6>
                  <p class="font-sans-serif lh-1 mb-1 fs-2" id="totalComplemento" >0 </p>
                  <div class="d-flex align-items-center">
                    <div class="row g-0 my-3">
                    <!--  Eliminar Pillo 
                      <a class="fw-semi-bold fs--2 text-nowrap" href="javascript:mostrarOpcion('/theme-falcon/siarexApp/configuracion/visor/dashboard/visorordenes/detalleVisor.html');">Ver Complementos<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
                      -->
                    </div>
                  </div>
                </div>

                <div
                  class="col-6 col-md-4 border-200 border-bottom border-bottom-md-0 border-end-md pt-4 pb-md-0 ps-3 ps-md-0">
                   <h6 class="pb-1 text-700 fs--2" id="VISOR_ETQ15">Notas de Credito </h6>
                  <p class="font-sans-serif lh-1 mb-1 fs-2" id="totalNotaCredito">0 </p>
                  <div class="d-flex align-items-center">
                    <div class="row g-0 my-3">
                    <!--  Eliminar Pillo 
                      <a class="fw-semi-bold fs--2 text-nowrap" href="javascript:mostrarOpcion('/theme-falcon/siarexApp/configuracion/visor/dashboard/visorordenes/detalleVisor.html');">Ver Notas de Credito<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
                      -->
                    </div>
                  </div>
                </div>

                <div class="col-6 col-md-4 border-200 border-bottom-md-0 border-end pt-4 pb-md-0 ps-md-3">
                   <h6 class="pb-1 text-700 fs--2" id="VISOR_ETQ16">Proveedores </h6>
                  <p class="font-sans-serif lh-1 mb-1 fs-2" id="totalProveedores">0 </p>
                  <div class="d-flex align-items-center">
                    <div class="row g-0 my-3">
                    <!--  Eliminar Pillo 
                      <a class="fw-semi-bold fs--2 text-nowrap" href="javascript:mostrarOpcion('/theme-falcon/siarexApp/configuracion/visor/dashboard/visorordenes/detalleVisor.html');">Ver Proveedores<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
                     --> 
                    </div>
                  </div>
                </div>

                <div class="col-6 col-md-4 pb-0 pt-4 ps-3">
                   <h6 class="pb-1 text-700 fs--2"> </h6>
                  <p class="font-sans-serif lh-1 mb-1 fs-2"></p>
                  <div class="d-flex align-items-center">
                    <div class="row g-0 my-3">
                      <!-- 
                      	<a class="fw-semi-bold fs--2 text-nowrap" href="javascript:mostrarOpcion('/theme-falcon/siarexApp/configuracion/visor/dashboard/visorordenes/detalleVisor.html');">Ver Complementos<span class="fas fa-angle-right ms-1" data-fa-transform="down-1"></span></a>
                       -->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-footer bg-light p-0">
          </div>
        </div>
      </div>

    </div>

    <div class="row g-0">
      <div id="mostrarOrden" style="width: 100%;  overflow-x:hidden;"></div>
    </div>
    <div class="row g-0">
      <div id="mostrarVisor" style="width: 100%;  overflow-x:hidden;"></div>
    </div>

  </div> <!-- card-body -->

  <div class="card-body pt-0" style="padding-top: 10px !important;">
    <div class="card-header p-0 scrollbar">
      <ul class="nav nav-tabs border-0 top-courses-tab flex-nowrap" role="tablist">
        <li class="nav-item" role="presentation"><a class="nav-link p-x1 mb-0 active" role="tab" id="popularPaid-tab"
            data-bs-toggle="tab" href="#popularPaid" aria-controls="popularPaid" aria-selected="false">
            <div class="d-flex gap-1 py-1 pe-3">
              <div class="d-flex flex-column flex-between-center">
              </div>
              <div class="ms-2">
                <h6 class="mb-1 text-700 fs--2 text-nowrap"></h6>
                <h5 class="mb-0 lh-1">USD</h5>
              </div>
            </div>
          </a></li>
        <li class="nav-item" role="presentation"><a class="nav-link p-x1 mb-0 false" role="tab" id="popularFree-tab"
            data-bs-toggle="tab" href="#popularFree" aria-controls="popularFree" aria-selected="true">
            <div class="d-flex gap-1 py-1 pe-3">
              <div class="d-flex flex-column flex-between-center">
              </div>
              <div class="ms-2">
                <h6 class="mb-1 text-700 fs--2 text-nowrap"></h6>
                <h5 class="mb-0 lh-1">MXN</h5>
              </div>
            </div>
          </a></li>
      </ul>
    </div>
    <div class="card-body p-0">
      <div class="tab-content">
        <div class="tab-pane active" id="popularPaid" role="tabpanel" aria-labelledby="popularPaid-tab">
          <div class="z-1" id="popularPaidCourses"
            data-list='{"valueNames":["title","name","published","enrolled","price"],"page":4}'>
            <div class="px-0 py-0">
              <div class="table-responsive scrollbar">
                <table id="tablaDetalleUSD" class="table table-sm table-striped fs--1 mb-0 overflow-hidden">
                  <thead class="bg-200 text-900">
                    <tr class="font-sans-serif">
                      <th class="fw-medium sort pe-1 align-middle" style="width: 30%; text-align: center;" id="VISOR_ETQ17">Razón Social</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 15%; text-align: center;" id="VISOR_ETQ18">RFC</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ19" >Monto Total</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ20">Monto Facturado</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ21">Monto Pagado</th>
                      <!-- 
                      <th class="fw-medium no-sort pe-1 align-middle data-table-row-action"></th>
                       -->
                    </tr>
                    <tr class="forFilters">
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						
					</tr>
							
                  </thead>
                  
                </table>
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane" id="popularFree" role="tabpanel" aria-labelledby="popularFree-tab">
          <div class="z-1" id="popularFreeCourses"
            data-list='{"valueNames":["title","name","published","enrolled","price"],"page":4}'>
            <div class="px-0 py-0">
              <div class="table-responsive scrollbar">
                <table id="tablaDetalleMXN" class="table table-sm table-striped fs--1 mb-0 overflow-hidden">
                  <thead class="bg-200 text-900">
                    <tr class="font-sans-serif">
                      <th class="fw-medium sort pe-1 align-middle" style="width: 30%; text-align: center;" id="VISOR_ETQ22">Razón Social</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 15%; text-align: center;" id="VISOR_ETQ23">RFC</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ24">Monto Total</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ25">Monto Facturado</th>
                      <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ26">Monto Pagado</th>
                      <!-- 
                      <th class="fw-medium no-sort pe-1 align-middle data-table-row-action"></th>
                       -->
                    </tr>
                    <tr class="forFilters">
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
                  </thead>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="card-footer bg-light py-2">
      <div class="row flex-between-center g-0">
        <div class="col-auto">
      
        </div>
      </div>
    </div>
  </div>

  <div class="card-body pt-0" style="padding-top: 10px !important;">
    <div class="card-header">
      <div class="row flex-between-center">
        <div class="col-6 col-sm-auto d-flex align-items-center pe-0">
          <h5 class="fs-0 mb-0 text-nowrap py-2 py-xl-0" id="VISOR_ETQ27">Ordenes con último movimiento </h5>
        </div>
      </div>
    </div>
    <div class="card-body px-0 pt-0">

      <div class="table-responsive scrollbar">
        <table id="tablaOrdenes" class="table table-sm table-striped fs--1 mb-0 overflow-hidden">
          <thead class="bg-200 text-900">
            <tr class="font-sans-serif">
              <th class="fw-medium sort pe-1 align-middle" style="width: 40%; text-align: center;" id="VISOR_ETQ28">Razón Social</th>
              <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ29">Orden de Compra</th>
              <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ30">Tipo Moneda</th>
              <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ31">Monto</th>
              <th class="fw-medium sort pe-1 align-middle" style="width: 20%; text-align: center;" id="VISOR_ETQ32">Estatus de pago</th>
              <th class="fw-medium sort pe-1 align-middle" style="width: 10%; text-align: center;" id="VISOR_ETQ33">Estado</th>
              <!-- 
              <th class="fw-medium no-sort pe-1 align-middle data-table-row-action" style="width: 10%;"></th>
               -->
            </tr>
            
             <tr class="forFilters">
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
          </thead>
        </table>
      </div>
    </div>
  </div>

</div><!-- card mb-3 -->


</html>


<script type="text/javascript">
	$(document).ready(function() {
		// $("#myModalOrdenes").load('/theme-falcon/siarexApp/configuracion/visor/dashboard/modalordenes.html');
		calcularEtiquetasVisor();
	});


	function calcularEtiquetasVisor(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'VISOR'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					document.getElementById("VISOR_ETQ1").innerHTML = data.ETQ1;
					document.getElementById("VISOR_ETQ2").innerHTML = data.ETQ2;
					document.getElementById("VISOR_ETQ3").innerHTML = data.ETQ2;
					document.getElementById("VISOR_ETQ4").innerHTML = data.ETQ2;
					document.getElementById("VISOR_ETQ5").innerHTML = data.ETQ5;
					document.getElementById("VISOR_ETQ6").innerHTML = data.ETQ6;
					document.getElementById("VISOR_ETQ7").innerHTML = data.ETQ7;
					document.getElementById("VISOR_ETQ8").innerHTML = data.ETQ8;
					document.getElementById("VISOR_ETQ9").innerHTML = data.ETQ9;
					document.getElementById("VISOR_ETQ10").innerHTML = data.ETQ10;
					document.getElementById("VISOR_ETQ11").innerHTML = data.ETQ11;
					document.getElementById("VISOR_ETQ12").innerHTML = data.ETQ12;
					document.getElementById("VISOR_ETQ13").innerHTML = data.ETQ13;
					document.getElementById("VISOR_ETQ14").innerHTML = data.ETQ14;
					document.getElementById("VISOR_ETQ15").innerHTML = data.ETQ15;
					document.getElementById("VISOR_ETQ16").innerHTML = data.ETQ16;
					document.getElementById("VISOR_ETQ17").innerHTML = data.ETQ17;
					document.getElementById("VISOR_ETQ18").innerHTML = data.ETQ18;
					document.getElementById("VISOR_ETQ19").innerHTML = data.ETQ19;
					document.getElementById("VISOR_ETQ20").innerHTML = data.ETQ20;
					document.getElementById("VISOR_ETQ21").innerHTML = data.ETQ21;
					document.getElementById("VISOR_ETQ22").innerHTML = data.ETQ17;
					document.getElementById("VISOR_ETQ23").innerHTML = data.ETQ18;
					document.getElementById("VISOR_ETQ24").innerHTML = data.ETQ19;
					document.getElementById("VISOR_ETQ25").innerHTML = data.ETQ20;
					document.getElementById("VISOR_ETQ26").innerHTML = data.ETQ21;
					document.getElementById("VISOR_ETQ27").innerHTML = data.ETQ27;
					document.getElementById("VISOR_ETQ28").innerHTML = data.ETQ28;
					document.getElementById("VISOR_ETQ29").innerHTML = data.ETQ29;
					document.getElementById("VISOR_ETQ30").innerHTML = data.ETQ30;
					document.getElementById("VISOR_ETQ31").innerHTML = data.ETQ31;
					document.getElementById("VISOR_ETQ32").innerHTML = data.ETQ32;
					document.getElementById("VISOR_ETQ33").innerHTML = data.ETQ33;
					document.getElementById("VISOR_ETQ105").innerHTML = data.ETQ105;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
</script>

