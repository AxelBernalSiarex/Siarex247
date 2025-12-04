
<%@page import="com.siarex247.utils.Utils"%>
<%@page import="com.siarex247.catalogos.Proveedores.ProveedoresAction"%>
<%@page import="com.siarex247.session.ObtenerSession"%>
<%@page import="com.siarex247.session.SiarexSession"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="/siarex/js/jquery.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
table,th,td {
  border : 1px solid black;
  border-collapse: collapse;
}
th,td {
  padding: 5px;
}
</style>
<%
	SiarexSession sessionSIA = ObtenerSession.getSession(request);

int tipoAcceso = Utils.noNuloINT(request.getParameter("t"));
String rfcProveedor = Utils.noNulo(request.getParameter("rfc"));

     String nombreRepositorio = sessionSIA.getEsquemaEmpresa();
     int idProveedor =  Utils.noNuloINT(request.getParameter("p"));
     
     String nombreArchivo = null;
     String mostrarFactura = null;
     
     ProveedoresAction provAction = new ProveedoresAction();
     
             if (tipoAcceso == 1){
            	 nombreArchivo = "RFC"+rfcProveedor +"_" + "Cert_IMSS.pdf";
             }else if (tipoAcceso == 2) {
            	 nombreArchivo = "RFC"+rfcProveedor +"_" + "Cert_SAT.pdf";
             }
             mostrarFactura = provAction.mostrarCertificado(request, nombreArchivo, nombreRepositorio, idProveedor);
         
         if (nombreArchivo == null){
             nombreArchivo = "";
         }
%>
<script type="text/javascript">
   function abrirXML(tipoAcceso){
	   try{
		     document.getElementById('frameOrdenes').src = '<%=mostrarFactura%>';
	   }catch(e){
		   alert('abrirXML()_'+e);
	   }
   }
</script>




</head>
<body onload="abrirXML(<%=tipoAcceso%>);">
  <iframe name="frameOrdenes" style="width: 100%; height: 1500px;" id="frameOrdenes"
        frameborder="0" marginheight="0" marginwidth="0" src="">
  </iframe>
  <table id="demo"></table>
</body>

<script type="text/javascript">

	
</script>
</html>