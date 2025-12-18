

<%@page import="java.util.HashMap"%>
<%@page import="com.siarex247.procesos.ProcesoMonitorBean"%>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Monitoreo Siarex</title>
<style type="text/css">

	 .GREEN{
	    width: 50%; 
	    height: 100px; 
	    background-color: green; 
	    color: white; 
	    font-weight: bold;
	    cursor: pointer;
	 }
 
	 .RED{
	    width: 50%; 
	    height: 100px; 
	    background-color: red; 
	    color: white; 
	    font-weight: bold;
	    cursor: pointer;  
	 }
</style>

<%
 ProcesoMonitorBean procBean = new ProcesoMonitorBean();
 HashMap mapaProceso = procBean.getBanderas();
 String usuarioHTTP = request.getRemoteUser();
 
 
%>
<%if (usuarioHTTP.equalsIgnoreCase("lupillo19801@gmail.com") || usuarioHTTP.equalsIgnoreCase("mario.tjbc@gmail.com") || usuarioHTTP.equalsIgnoreCase("admin.siarex@siarexdev.com") || usuarioHTTP.equalsIgnoreCase("DSBUMAJTJ")
		|| usuarioHTTP.equalsIgnoreCase("admin.siarex@siarex.com")){ %>
<script type="text/javascript">
  function getClase(idButton){
	  return document.getElementById(idButton).className;
  }
  
  function iniciarProcesoCorreos(){
	  if (getClase('btnProc1') == 'GREEN'){
		  window.open('terminaProcesoCorreos.jsp','ocultoFrame');
		  document.getElementById('btnProc1').className = 'RED';
	  }else{
		  window.open('iniciaProcesoCorreos.jsp','ocultoFrame');
		  document.getElementById('btnProc1').className = 'GREEN';
		  
	  }
	  
  }
  
  function iniciarProcesoTareas(){
	  if (getClase('btnProc11') == 'GREEN'){
		  window.open('terminaProcesoTareas.jsp','ocultoFrame');
		  document.getElementById('btnProc11').className = 'RED';
	  }else{
		  window.open('iniciaProcesoTareas.jsp','ocultoFrame');
		  document.getElementById('btnProc11').className = 'GREEN';
		  
	  }
	  
  }
  
  
  function iniciarProcesoDescargaCFDI(){
	  if (getClase('btnProc9') == 'GREEN'){
		  window.open('terminaProcesoDescarga.jsp','ocultoFrame');
		  document.getElementById('btnProc9').className = 'RED';
	  }else{
		  window.open('iniciaProcesoDescarga.jsp','ocultoFrame');
		  document.getElementById('btnProc9').className = 'GREEN';
		  
	  }
  }
  
  
  function iniciarProcesoListaNegra(){
	  if (getClase('btnProc5') == 'GREEN'){
		  window.open('terminaProcesoListaNegra.jsp','ocultoFrame');
		  document.getElementById('btnProc5').className = 'RED';
	  }else{
		  window.open('iniciaProcesoListaNegra.jsp','ocultoFrame');
		  document.getElementById('btnProc5').className = 'GREEN';
		  
	  }
  }
  
  
  function iniciarProcesoTimbrado(){
	  try{
		  if (getClase('btnProc6') == 'GREEN'){
			  window.open('terminaProcesoTimbrado.jsp','ocultoFrame');
			  document.getElementById('btnProc6').className = 'RED';
		  }else{
			  window.open('iniciaProcesoTimbrado.jsp','ocultoFrame');
			  document.getElementById('btnProc6').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarProcesoTimbrado()_'+e);
	  }
  }
  
  function iniciarProcesoValidacionXML(){
	  try{
		  if (getClase('btnProc2') == 'GREEN'){
			  window.open('terminaValidacionFisicos.jsp','ocultoFrame');
			  document.getElementById('btnProc2').className = 'RED';
		  }else{
			  window.open('iniciaProcesoValidacionFisicos.jsp','ocultoFrame');
			  document.getElementById('btnProc2').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarProcesoValidacionXML()_'+e);
	  }
  }
  
  function iniciarProcesoLocal(){
	  try{
		  if (getClase('btnProc2') == 'GREEN'){
			  window.open('terminaProcesoLocal.jsp','ocultoFrame');
			  document.getElementById('btnProc2').className = 'RED';
		  }else{
			  window.open('iniciaProcesoLocal.jsp','ocultoFrame');
			  document.getElementById('btnProc2').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarProcesoLocal()_'+e);
	  }
  }
  
  
  
  function iniciarProcesoVincular(){
	  try{
		  if (getClase('btnProc3') == 'GREEN'){
			  window.open('terminaVincularBoveda.jsp','ocultoFrame');
			  document.getElementById('btnProc3').className = 'RED';
		  }else{
			  window.open('iniciaProcesoVincularBoveda.jsp','ocultoFrame');
			  document.getElementById('btnProc3').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarProcesoVincular()_'+e);
	  }
  }
  
  
  function iniciarAlertaConciliacion(){
	  try{
		  if (getClase('btnProc7') == 'GREEN'){
			  window.open('terminaAlertaConciliacion.jsp','ocultoFrame');
			  document.getElementById('btnProc7').className = 'RED';
		  }else{
			  window.open('iniciaProcesoAlertaConciliacion.jsp','ocultoFrame');
			  document.getElementById('btnProc7').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarAlertaConciliacion()_'+e);
	  }
  }
  
  
  function iniciarActualizarCancelados(){
	  try{
		  if (getClase('btnProc13') == 'GREEN'){
			  window.open('terminaActualizacionEstatus.jsp','ocultoFrame');
			  document.getElementById('btnProc13').className = 'RED';
		  }else{
			  window.open('iniciaProcesoActualizacionEstatus.jsp','ocultoFrame');
			  document.getElementById('btnProc13').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarActualizarCancelados()_'+e);
	  }
  }
  
  
  
  function iniciarReporteNomina(){
	  try{
		  if (getClase('btnProc12') == 'GREEN'){
			  window.open('terminaProcesoReporteNomina.jsp','ocultoFrame');
			  document.getElementById('btnProc12').className = 'RED';
		  }else{
			  window.open('iniciaProcesoReporteNomina.jsp','ocultoFrame');
			  document.getElementById('btnProc12').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarReporteNomina()_'+e);
	  }
  }
  
  
  function iniciarEliminarArchivos(){
	  try{
		  if (getClase('btnProc4') == 'GREEN'){
			  window.open('terminaProcesoDepurador.jsp','ocultoFrame');
			  document.getElementById('btnProc4').className = 'RED';
		  }else{
			  window.open('iniciaProcesoDepurador.jsp','ocultoFrame');
			  document.getElementById('btnProc4').className = 'GREEN';
			  
		  }
	  }catch(e){
		  alert('iniciarEliminarArchivos()_'+e);
	  }
  }
  

  function iniciarComplemento1(){
      if (getClase('btnProc9') == 'GREEN'){
    	  document.getElementById('btnProc9').className = 'RED';
          window.open('terminarComple1.jsp','ocultoFrame');
      }else{
    	  document.getElementById('btnProc9').className = 'GREEN';
          window.open('iniciarComplemento1.jsp','ocultoFrame');
          
      }

  }
  
  function iniciarComplementoHP(){
      if (getClase('btnProc14') == 'GREEN'){
    	  document.getElementById('btnProc14').className = 'RED';
          window.open('terminarHistoricoPagos.jsp','ocultoFrame');
      }else{
    	  document.getElementById('btnProc14').className = 'GREEN';
          window.open('iniciarHistoricoPagos.jsp','ocultoFrame');
          
      }

  }
  
  function muestraMensaje(mensaje){
	  document.getElementById('txtMensaje').value = mensaje;
  }
  
  
  function verificaBanderas(){
	  try{
		  debugger;
		  var btnProc1 = '<%=mapaProceso.get("PROC_1")%>';
		  var btnProc2 = '<%=mapaProceso.get("PROC_2")%>';
		  var btnProc3 = '<%=mapaProceso.get("PROC_3")%>';
		  var btnProc4 = '<%=mapaProceso.get("PROC_4")%>';
		  var btnProc5 = '<%=mapaProceso.get("PROC_5")%>';
		  var bandBtn6 = '<%=mapaProceso.get("PROC_6")%>';
		  var bandBtn7 = '<%=mapaProceso.get("PROC_7")%>';
		  var bandBtn11 = '<%=mapaProceso.get("PROC_11")%>';
		  var bandBtn9 = '<%=mapaProceso.get("PROC_9")%>';
		  
		  var bandBtn12 = '<%=mapaProceso.get("PROC_12")%>';
		  var bandBtn13 = '<%=mapaProceso.get("PROC_13")%>';
		  var bandBtn14 = '<%=mapaProceso.get("PROC_14")%>';
		  
		  if (btnProc1 == 'S'){
			  document.getElementById('btnProc1').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc1').className = 'GREEN';
		  }
		  
		  if (btnProc2 == 'S'){
			  document.getElementById('btnProc2').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc2').className = 'GREEN';
		  }
		  
		  if (btnProc3 == 'S'){
			  document.getElementById('btnProc3').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc3').className = 'GREEN';
		  }
		  
		  if (btnProc4 == 'S'){
			  document.getElementById('btnProc4').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc4').className = 'GREEN';
		  }
		  
		  if (btnProc5 == 'S'){
			  document.getElementById('btnProc5').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc5').className = 'GREEN';
		  }
		  
		  if (bandBtn7 == 'S'){
			  document.getElementById('btnProc7').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc7').className = 'GREEN';
		  }
		  
		  
		  if (bandBtn11 == 'S'){
			  document.getElementById('btnProc11').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc11').className = 'GREEN';
		  }
		  
		  if (bandBtn9 == 'S'){
			  document.getElementById('btnProc9').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc9').className = 'GREEN';
		  }
		  
		  
		  if (bandBtn6 == 'S'){
			  document.getElementById('btnProc6').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc6').className = 'GREEN';
		  }
		  
		  if (bandBtn12 == 'S'){
			  document.getElementById('btnProc12').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc12').className = 'GREEN';
		  }
		  
		  if (bandBtn13 == 'S'){
			  document.getElementById('btnProc13').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc13').className = 'GREEN';
		  }
		  if (bandBtn14 == 'S'){
			  document.getElementById('btnProc14').className = 'RED';			  
		  }else{
			  document.getElementById('btnProc14').className = 'GREEN';
		  }
		  

	  }catch(e){
		  alert('verificaBanderas()_'+e);
	  }
  }
  </script>
</head>

<body onload="verificaBanderas();">
 <form action="" name="frmProceso">
  <center>
   <table width="80%" height="500" cellpadding="0" cellspacing="0" border="1">
       <tr class="tablaTh">
           <td width="100%" height="30" colspan="5" align="center" style="font-family: Arial; font-size: 24px; color: navy; font-weight: bold;"> 
                Monitor de Procesos SIAREX
            </td>
       </tr>
       <tr>
          <td width="15%" align="center">&nbsp;
              <input type="button" name="btnProc1" id="btnProc1" value="PROCESO DE CORREOS" onclick="iniciarProcesoCorreos();" class="GREEN">
          </td>    
          <td width="25%" align="center">
              <input type="button" name="btnProc11" id="btnProc11" value="PROCESO DE TAREAS" onclick="iniciarProcesoTareas();" class="GREEN">
          </td>
          
          <td align="center">
              <input type="button" name="btnProc9" id="btnProc9" value="COMPLEMENTO DIA 1" onclick="iniciarComplemento1();" class="GREEN">
          </td>
          
           <td align="center">
              <input type="button" name="btnProc14" id="btnProc14" value="HISTORICO DE PAGO" onclick="iniciarComplementoHP();" class="GREEN">
          </td>
          
           <!-- 
	          <td width="15%" align="center">
	             <input type="button" name="btnProc9" id="btnProc9" value="DESCARGA CFDI" onclick="iniciarProcesoDescargaCFDI();" class="GREEN" style="visibility: hidden;">
	          </td>
             -->
           
       </tr> 
       
       <tr>
          <td width="15%" align="center">&nbsp;
          <!-- 
              <input type="button" name="btnProc10" id="btnProc10" value="DESCARGA METADATA" onclick="iniciarProcesoMetaData();" class="GREEN">
            -->
               
          </td>    
          <td width="25%" align="center">&nbsp;
             <input type="button" name="btnProc5" id="btnProc5" value="LISTA NEGRA" onclick="iniciarProcesoListaNegra();" class="GREEN">
          </td>
          <td width="25%" align="center">
             <input type="button" name="btnProc6" id="btnProc6" value="DESCARGA CFDI TIMBRADO" onclick="iniciarProcesoTimbrado();" class="GREEN">
          </td>
          <td align="center">&nbsp;
              
          </td>
       </tr> 
       
       <tr>
       
         <!--
          
          <td width="15%" align="center">&nbsp;
             <input type="button" name="btnProc2" id="btnProc2" value="VALIDACION DE XML FISICOS" onclick="iniciarProcesoValidacionXML();" style="visibility: hidden;" class="GREEN">
          </td>
          
         -->
         
         
           <td width="25%" align="center">&nbsp;
             <input type="button" name="btnProc2" id="btnProc2" value="DESCARGA MASIVA LOCAL" onclick="iniciarProcesoLocal();" class="GREEN">
          </td>
               
          <td width="25%" align="center">&nbsp;
             <input type="button" name="btnProc3" id="btnProc3" value="VINCULAR BOVEDA" onclick="iniciarProcesoVincular();" class="GREEN">
          </td>
          <td width="25%" align="center">
             <input type="button" name="btnProc7" id="btnProc7" value="ALERTAS CONCILIACION BOVEDA" onclick="iniciarAlertaConciliacion();" class="GREEN">
          </td>
          <td align="center">&nbsp;
              
          </td>
          
       </tr> 
       <tr>
          <td width="15%" align="center">&nbsp;
             <input type="button" name="btnProc12" id="btnProc12" value="REPORTE DE NOMINA" onclick="iniciarReporteNomina();"  class="GREEN">
          </td>    
          <td width="25%" align="center">&nbsp;
             <input type="button" name="btnProc4" id="btnProc4" value="ELIMINAR ARCHIVOS FISICOS" onclick="iniciarEliminarArchivos();"  class="GREEN">
          </td>
          <td width="25%" align="center">
             <input type="button" name="btnProc13" id="btnProc13" value="ACTUALIZAR CANCELADOS SAT" onclick="iniciarActualizarCancelados();"  class="GREEN">
          </td>
          <td align="center">&nbsp;
              
          </td>
          
       </tr> 
       
       
        <iframe name="ocultoFrame" style="visibility: hidden; width: 0px; height: 0px; " marginheight="0" marginwidth="0" frameborder="0"></iframe>
   </table>
  </center> 
 </form>
</body>
<%} %>
</html>