<%@page import="com.siarex247.cumplimientoFiscal.Boveda.BovedaAction"%>
<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XML Historial Pagos</title>
<link rel="icon" type="image/png" sizes="32x32" href="/theme-falcon/assets/img/icons/ico.png">
<%
    String uuid = request.getParameter("f");
    String mostrarDocumento = "";

    try {
        BovedaAction bovedaAction = new BovedaAction();
        mostrarDocumento = bovedaAction.generaXML(uuid, request);
    } catch (Exception e) {
        Utils.imprimeLog("mostrarXMLHistorial.jsp", e);
    }

    if (mostrarDocumento == null || "".equals(mostrarDocumento.trim())) {
        mostrarDocumento = "about:blank";
    }
%>
<script type="text/javascript">
   function abrirXML(){
       try{
           var url = "<%=mostrarDocumento%>";
           if (url && url !== 'about:blank'){
               // Abrimos directamente el XML generado en /files/...
               window.location.href = url;
           } else {
               alert("No se encontró el XML para el UUID indicado o hubo un error al generarlo.");
           }
       }catch(e){
           alert('abrirXML()_'+e);
       }
   }
</script>
</head>
<body onload="abrirXML();">
</body>
</html>
