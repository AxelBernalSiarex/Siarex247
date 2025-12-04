
<%@page import="com.siarex247.catalogos.ConstanciaSituacion.ConstanciaSituacionAction"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache"); %>
	
	    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">


<link rel="icon" type="image/png" sizes="32x32" href="/theme-falcon/assets/img/icons/ico.png">


<%

	ConstanciaSituacionAction constAcion = new ConstanciaSituacionAction();
	String mostrarDocumento = constAcion.mostrarDocumentos(request);
     
     
  %>
<script type="text/javascript">
   function abrirDocumento(){
	   try{
		   window.open('<%=mostrarDocumento%>', 'frmDocumentos');   
	   }catch(e){
		   alert('abrirDocumento()_'+e);
	   }
   }
</script>



</head>

<body onload="abrirDocumento();">
  <iframe name="frmDocumentos" style="width: 100%; height: 1500px;" 
	        frameborder="0" marginheight="0" marginwidth="0" src="" >
	  </iframe>
	  
</body>

</html>