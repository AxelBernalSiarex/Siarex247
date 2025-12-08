
<%@page import="com.siarex247.utils.Utils"%>
<%@page import="com.siarex247.visor.VisorOrdenes.VisorDocumentos"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache"); %>
	
	    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" type="image/png" sizes="32x32" href="/theme-falcon/assets/img/icons/ico.png">


<%

	String tipoDocumento = Utils.noNulo(request.getParameter("tipoDocumento")); 
	int posXML = tipoDocumento.indexOf("XML_"); 
	VisorDocumentos visorBean = new VisorDocumentos();

	String mostrarDocumento = visorBean.mostrarDocumentos(request);
     
     
  %>
<script type="text/javascript">
   function abrirDocumento(posXML){
	   try{
		   if (posXML >= 0 ){
			   window.open('<%=mostrarDocumento%>', '_self');   
		   }else{
			   window.open('<%=mostrarDocumento%>', 'frmDocumentos');   
		   }
	   }catch(e){
		   alert('abrirDocumento()_'+e);
	   }
   }
</script>



</head>

<body onload="abrirDocumento(<%=posXML%>);">
  <iframe name="frmDocumentos" style="width: 100%; height: 1500px;" 
	        frameborder="0" marginheight="0" marginwidth="0" src="" >
	  </iframe>
	  
  <table id="demo"></table>
</body>


</html>