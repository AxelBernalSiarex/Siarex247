<%@page import="com.siarex247.menus.MenusForm"%>
<%@page import="com.siarex247.menus.MenusAction"%>
<%@page import="com.siarex247.session.ObtenerSession"%>
<%@page import="com.siarex247.session.SiarexSession"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

<head>
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">

</head>


<div class="navbar-vertical-content scrollbar">
	<ul class="navbar-nav flex-column mb-3" id="navbarVerticalNav">


	  <li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#visor" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="visor">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-folder"></span></span>
            		<span class="nav-link-text ps-1">Visor</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="visor">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/visor/indexVisor.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Visor</span>
                    	</div>
					</a>
				</li>
			</ul>

		</li>
		
		

		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#correos" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="correos">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-envelope"></span></span>
            		<span class="nav-link-text ps-1">Envio de Correos</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="correos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/envioCorreos/confTareas/tareas.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Configurador de Tareas</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="correos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/envioCorreos/bitacora/bitacora.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Bitácora de Confidencialidad</span>
                    	</div>
					</a>
				</li>		
			</ul>
		</li>
		
		
		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#catalogos" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="catalogos">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-folder"></span></span>
            		<span class="nav-link-text ps-1">Catalogos</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="catalogos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/catalogos/proveedores/proveedores.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Proveedores</span>
                    	</div>
					</a>
				</li>
			</ul>
			
			<ul class="nav collapse submenu" id="catalogos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/catalogos/proveedoresExternos/proveedoresExternos.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Proveedores Externos</span>
                    	</div>
					</a>
				</li>
			</ul>
			
			
			<ul class="nav collapse submenu" id="catalogos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/catalogos/empleados/empleados.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Empleados</span>
                    	</div>
					</a>
				</li>
			</ul>
			
			<ul class="nav collapse submenu" id="catalogos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/catalogos/centroCostos/centroCostos.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Centros de Costos</span>
                    	</div>
					</a>
				</li>
			</ul>
			<ul class="nav collapse submenu" id="catalogos">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/catalogos/puestos/puestos.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Puestos</span>
                    	</div>
					</a>
				</li>
			</ul>
			
		</li>


		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#cargas" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="cargas">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-cloud-upload-alt"></span></span>
            		<span class="nav-link-text ps-1">Cargas</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="cargas">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/cargas/ordenes/ordenes.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Carga de Ordenes</span>
                    	</div>
					</a>
				</li>		
			</ul>

		



		</li>
		

		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#configcfdi" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="configcfdi">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-cog"></span></span>
            		<span class="nav-link-text ps-1">Configuraciones CFDI</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="configcfdi">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/etiquetas/etiquetas.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Configuración de Etiquetas</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/etiquetasComp/etiquetasComp.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Configuración Complemento</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/etiquetasCartaPorte/etiquetasCartaPorte.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Etiquetas Carta Porte</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/confClaveUsoCFDI/confClaveUsoCFDI.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Configuración clave y uso de CFDI</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/confClaveUsoCP/confClaveUsoCP.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Configuración Clave y Uso de Carta Porte</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/regimenFiscal/regimenFiscal.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Configuración Regimen Fiscal</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/listaNegra/listaNegra.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Lista Negra SAT</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/formatos/formatos.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Envio de Formatos</span>
                    	</div>
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/cfdi/descargaSat/descargaSat.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2 icoSubMenu1"></i>Descarga SAT</span>
                    	</div>
					</a>
				</li>
		
			</ul>
		</li>

		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#configsist" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="configsist">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-cogs"></span></span>
            		<span class="nav-link-text ps-1">Configuraciones Sistema</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="configsist">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/configuraciones/sistema/indexSistema.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Configuraciones de Siarex</span>
                    	</div>
					</a>
				</li>		
			</ul>
		</li>

		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#estadisticas" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="estadisticas">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-chart-bar"></span></span>
            		<span class="nav-link-text ps-1">Estadisticas</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="estadisticas">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estadisticas/repVerificacion/repVerificacion.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Reporte de Verificacion SAT</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="estadisticas">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estadisticas/repCartaPorte/repCartaPorte.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Estadisticas Carta Porte</span>
                    	</div>
					</a>
				</li>		
			</ul>
		</li>
	
		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#statusxml" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="statusxml">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="far fa-file-code"></span></span>
            		<span class="nav-link-text ps-1">Status XML</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/validarXML/validarXML.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Validar XML</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/exportarXML/exportarXML.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Exportar XML</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/boveda/boveda.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Boveda</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/conciliacion/conciliacion.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Conciliación</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/conciliacionBoveda/conciliacionBoveda.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Conciliación Boveda</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/conciliacionCP/conciliacionCP.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Conciliación Carta Porte</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/descartar/descartar.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Descartar</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/pedimentos/pedimentos.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Pedimentos</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="statusxml">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/estatusXML/listaNegra/listaNegra.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Lista Negra del SAT</span>
                    	</div>
					</a>
				</li>		
			</ul>
		</li>
		
		
		
		<li class="nav-item" id= "menuID">
			<a class="nav-link dropdown-indicator linkPrincipal" href="#seguridad" role="button" data-bs-toggle="collapse" aria-expanded="false" aria-controls="seguridad">
            	<div class="d-flex align-items-center">
            		<span class="nav-link-icon"><span class="fas fa-lock"></span></span>
            		<span class="nav-link-text ps-1">Seguridad</span>
				</div>
			</a>
			<ul class="nav collapse submenu" id="seguridad">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/seguridad/perfiles/perfiles.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Perfiles</span>
                    	</div>
					</a>
				</li>		
			</ul>
			<ul class="nav collapse submenu" id="seguridad">
				<li class="nav-item">
					<a class="nav-link" href="javascript:mostrarOpcion('/siarex247/jsp/seguridad/usuarios/usuarios.jsp');">
                    	<div class="d-flex align-items-center"><span class="nav-link-text ps-1">
                    		<i class="fas fa-circle mx-2 fs--2"></i>Usuarios</span>
                    	</div>
					</a>
				</li>		
			</ul>
		</li>

	
	
	</ul>
</div>

<script type="text/javascript">
    
$(document).ready(function() {
	
	try{
		$('#menuID .submenu a').click(function(){
		    $('.nav-link').removeClass("active");  
		    $(this).addClass("active");  
		});  
		
		
		$('#menuID a').click(function(){
		    $('.linkPrincipal').removeClass("active");  
		    $(this).addClass("active");  
		}); 
		
	}catch(e){
		alert('menu()_'+e);
	}
	
	$("#mostrarContenido").load('/siarex247/jsp/visor/indexVisor.jsp');
	
});

</script>
</html>