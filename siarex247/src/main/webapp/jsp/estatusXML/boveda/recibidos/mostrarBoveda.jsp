
<%@page import="com.siarex247.cumplimientoFiscal.Boveda.BovedaAction"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" type="image/png" sizes="32x32" href="/theme-falcon/assets/img/icons/ico.png">
<%

    String idRegistro = request.getParameter("f");
	String tipoRegistro = request.getParameter("t");
     
    BovedaAction bovedaAction = new BovedaAction();
    String mostrarDocumento = null;
    if ("PDF".equalsIgnoreCase(tipoRegistro)){
    	mostrarDocumento = bovedaAction.generaPDF(idRegistro, request);	
    }else{
    	mostrarDocumento = bovedaAction.generaXML(idRegistro, request);
    }
    
%>
<script type="text/javascript">
   function abrirDocumento(tipoRegistro){
	   try{
		   if (tipoRegistro == 'XML'){
			   window.open('<%=mostrarDocumento%>', '_self');   
		   }
	   }catch(e){
		   alert('abrirDocumento()_'+e);
	   }
   }
</script>


</head>
	<body onload="abrirDocumento('<%=tipoRegistro%>');">
	  <iframe name="frameOrdenes" style="width: 100%; height: 1500px;" 
	        frameborder="0" marginheight="0" marginwidth="0" src="<%=mostrarDocumento%>">
	  </iframe>
	</body>
	
</html>