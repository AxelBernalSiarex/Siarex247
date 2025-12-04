

<%@page import="com.siarex247.procesos.TaskManagerActualizacionEstatusSAT"%>
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
<title>Insert title here</title>
<%

String bandera = "S";
TaskManagerActualizacionEstatusSAT task = TaskManagerActualizacionEstatusSAT.instance();
  task.terminaProceso(bandera);
%>
</head>
<body onload="parent.muestraMensaje('El proceso se ha finalizado satisfactoriamente.');">

</body>
</html>