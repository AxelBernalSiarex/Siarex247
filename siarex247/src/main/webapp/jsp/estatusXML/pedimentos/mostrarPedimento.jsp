
<%@page import="com.siarex247.cumplimientoFiscal.Pedimentos.PedimentosAction"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" type="image/png" sizes="32x32" href="/theme-falcon/assets/img/icons/ico.png">
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">



<%

    String numPedimento = request.getParameter("f");
	String tipoRegistro = request.getParameter("t");
     
    String mostrarDocumento = new PedimentosAction().generaPDF(numPedimento, request);
    
%>
<script type="text/javascript">
   function abrirPDF(tipoRegistro){
	   try{
		   if (tipoRegistro == 'XML'){
			   window.open('<%=mostrarDocumento%>', '_self');   
		   }
	   }
	   catch(e){
		   alert('abrirPDF(): ' + e);
	   }
   }
</script>


</head>
	<body onload="abrirPDF('<%=tipoRegistro%>');">
	  <iframe name="frameOrdenes" style="width: 100%; height: 1500px;" frameborder="0" marginheight="0" marginwidth="0" src="<%=mostrarDocumento%>"></iframe>
	</body>
	
</html>