<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
 long t = System.currentTimeMillis();
%>

<script src="/siarex247/jsp/dashboard/monitor360/graficaEgresos.js?t=<%=t%>"></script>
<div class="card-body bg-light tab-pane" id="graficaEgresos" role="tabpanel" aria-labelledby="tabGraficaEgresos">
  <h5 class="mb-0" data-anchor="data-anchor" id="x">Gráfica Egreso</h5>
  <h6 class="mb-3 text-700">Gráfica de Egresos por Tipo de Comprobante</h6>
  <div id="contenedorGraficaEgresos" style="min-height: 0px;"></div>
</div>
<div class="echart-grafica-egresos" style="height: 400px;"></div>

