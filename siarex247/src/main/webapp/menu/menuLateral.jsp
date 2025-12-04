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


<%

 
	SiarexSession sessionSAI = ObtenerSession.getSession(request);

	if ("".equalsIgnoreCase(sessionSAI.getEsquemaEmpresa())){
%>
		<script type="text/javascript">
			window.open('/login/logOut.jsp','_self');
		</script>
<%		
	}
	
%>

		<%	
							MenusAction menusAction = new MenusAction();	
							ArrayList<MenusForm> datosMenusPrincipales = menusAction.menuPrincipal2(request);
							ArrayList<MenusForm> listaMenuOpciones = null;
							
							MenusForm menusForm = null;
							MenusForm menusOpcionesForm = null;
							
							int x = 0, y = 0;
							
							boolean isPrimero = true;
							String primerUrl = "";
							String nombrePrimerMenu = "";
							
											
		%>
									
						<div class="navbar-vertical-content scrollbar">
                                  <ul class="navbar-nav flex-column mb-3" id="navbarVerticalNav">
                                          
							<%
							String activeMenu = null;
							String activeMenuPrincipal = null;
							String primerURL = "";
							String show = "";
	
							
							for(x = 0; x < datosMenusPrincipales.size(); x++){
									menusForm =  datosMenusPrincipales.get(x);
									listaMenuOpciones = menusAction.menuOpciones2(menusForm.getClaveMenu(), request);
									if (y == 0) {
										activeMenuPrincipal = "active";
										show = "show";
									} else {
										activeMenuPrincipal = "";
										show = "";
									}
									
%>
										<li class="nav-item" id= "menuID">
                                                <a class="nav-link dropdown-indicator linkPrincipal" href="#<%=menusForm.getNombreCorto() %>" role="button"
                                                      data-bs-toggle="collapse" aria-expanded="false"
                                                      aria-controls="<%=menusForm.getNombreCorto() %>">
                                                      <div class="d-flex align-items-center">
                                                      		<span class="nav-link-icon">
                                                            	<span class="<%=menusForm.getImagen() %>"></span>
                                                            </span>
                                                            <span class="nav-link-text ps-1"><%=menusForm.getDescripcion() %></span>
                                                      </div>
                                                </a>
                                                <ul class="nav collapse <%=show %> submenu" id="<%=menusForm.getNombreCorto() %>">
<%
												for(y = 0; y < listaMenuOpciones.size(); y++){
													menusOpcionesForm = listaMenuOpciones.get(y);
													if ("".equals(primerUrl)){
									    				primerUrl = menusOpcionesForm.getUrl();
									    			}
													
													if ( isPrimero) {
														activeMenu = "active";
														isPrimero = false;
													} else {
														activeMenu = "";
													}
													
%>                                                
                                                
                                                      <li class="nav-item"><a class="nav-link link <%=activeMenu %>" href="javascript:mostrarMenuOpcion('<%=menusOpcionesForm.getUrl() %>');">
                                                                  <div class="d-flex align-items-center"><span
                                                                              class="nav-link-text ps-1"><i
                                                                                    class="fas fa-circle mx-2 fs--2"></i><%=menusOpcionesForm.getDescripcion() %> </span>
                                                                  </div>
                                                            </a>
                                                      </li>
<%
												}
%>          									</ul>                                      
                                          </li>

<%												

											}
%>                                                      
                                    </ul>

                              </div>
                                              


<script type="text/javascript">
    
var BTN_ACEPTAR_MENU = null;
var BTN_CANCELAR_MENU = null;
var BTN_CERRAR_MENU = null;
var BTN_BUSCAR_MENU = null;
var BTN_PROCESAR_MENU = null;
var BTN_REGRESAR_MENU = null;
var BTN_ACTUALIZAR_MENU = null;
var BTN_IMPORTAR_MENU = null;
var BTN_NUEVO_MENU = null;
var BTN_GUARDAR_MENU = null;
var BTN_EDITAR_MENU = null;
var BTN_ELIMINAR_MENU = null;
var BTN_VER_MENU = null;
var BTN_VERSIONAR_CALENDRIO = null;
var BTN_REFRESCAR = null;
var BTN_VALIDAR = null;
var BTN_ASIGNAR = null;
var BOTON_MODIFICAR_CONFIGURACION = null;
var BOTON_AGREGAR_VALIDACIONES = null;
var BOTON_CARGAR_PEDIMENTOS = null;
var BTN_LIMPIAR = null;

var MSG_OPERACION_EXITOSA_MENU = null;
var MSG_REGISTRO_ELIMINADO_MENU = null;
var MSG_ERROR_OPERACION_MENU = null;
var MSG_SOLICITUD_EXITOSA_MENU = null;
var MSG_ALTA_EXITOSA_MENU = null;


var LABEL_SELECCIONE_MES_MENU = null;
var LABEL_ENERO_MENU = null;
var LABEL_FEBRERO_MENU = null;
var LABEL_MARZO_MENU = null;
var LABEL_ABRIL_MENU = null;
var LABEL_MAYO_MENU = null;
var LABEL_JUNIO_MENU = null;
var LABEL_JULIO_MENU = null;
var LABEL_AGOSTO_MENU = null;
var LABEL_SEPTIEMBRE_MENU = null;
var LABEL_OCTUBRE_MENU = null;
var LABEL_NOVIEMBRE_MENU = null;
var LABEL_DICIEMBRE_MENU = null;
var LABEL_SI = null;
var LABEL_NO = null;

$(document).ready(function() {
	
	calcularEtiquetasBotonesSiarex();
	calcularEtiquetasMensajeGeneralesSiarex();
	try{
		$('#menuID .submenu a').click(function(){
		    $('.link').removeClass("active");  
		    $(this).addClass("active");  
		});  
		
		
		$('#menuID a').click(function(){
		    $('.linkPrincipal').removeClass("active");  
		    $(this).addClass("active");  
		}); 
		
	}catch(e){
		alert('menu()_'+e);
	}
	
	$("#mostrarContenido").load('<%=primerUrl%>');
	
});


function calcularEtiquetasBotonesSiarex(){
	$.ajax({
		url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
		type : 'POST', 
		data : {
			nombrePantalla : 'PAN_BTN_GENERALES'
		},
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			   
			} else {
				BTN_BUSCAR_MENU 	= data.BTN1;
				BTN_IMPORTAR_MENU 	= data.BTN3;
				BTN_ELIMINAR_MENU	= data.BTN6;
				BTN_VALIDAR 		= data.BTN7;
				BOTON_MODIFICAR_CONFIGURACION = data.BTN8;
				BTN_GUARDAR_MENU	= data.BTN9;
				BTN_CERRAR_MENU 	= data.BTN10;
				BTN_PROCESAR_MENU 	= data.BTN11;
				BTN_CANCELAR_MENU 	= data.BTN13;
				BTN_ACEPTAR_MENU 	= data.BTN14;
				BTN_REGRESAR_MENU 	= data.BTN15;
				BTN_ASIGNAR 		= data.BTN16;
				BTN_ACTUALIZAR_MENU = data.BTN17;
				BTN_EDITAR_MENU		= data.BTN21;
				BTN_REFRESCAR 		= data.BTN24;
				BOTON_CARGAR_PEDIMENTOS = data.BTN25;
				BTN_NUEVO_MENU		= data.BTN26;
				BTN_VER_MENU		= data.BTN27;
				BOTON_AGREGAR_VALIDACIONES		= data.BTN28;
				BTN_LIMPIAR 		= data.BTN29;
				
				
			}
			
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('calcularEtiquetasBotones()_1_'+thrownError);
		}
	});	
	
}

function calcularEtiquetasMensajeGeneralesSiarex(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'PAN_MSG_GENERALES'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					MSG_OPERACION_EXITOSA_MENU	= data.MSG1;
					MSG_SOLICITUD_EXITOSA_MENU	= data.MSG2;
					MSG_ERROR_OPERACION_MENU	= data.MSG3;
					MSG_REGISTRO_ELIMINADO_MENU	= data.MSG22;
					MSG_ALTA_EXITOSA_MENU      	= data.MSG4;
					
					
					LABEL_SELECCIONE_MES_MENU = data.LABEL1;
					LABEL_ENERO_MENU = data.LABEL_ENERO;
					LABEL_FEBRERO_MENU = data.LABEL_FEBRERO;
					LABEL_MARZO_MENU = data.LABEL_MARZO;
					LABEL_ABRIL_MENU = data.LABEL_ABRIL;
					LABEL_MAYO_MENU = data.LABEL_MAYO;
					LABEL_JUNIO_MENU = data.LABEL_JUNIO;
					LABEL_JULIO_MENU = data.LABEL_JULIO;
					LABEL_AGOSTO_MENU = data.LABEL_AGOSTO;
					LABEL_SEPTIEMBRE_MENU = data.LABEL_SEPTIEMBRE;
					LABEL_OCTUBRE_MENU = data.LABEL_OCTUBRE;
					LABEL_NOVIEMBRE_MENU = data.LABEL_NOVIEMBRE;
					LABEL_DICIEMBRE_MENU = data.LABEL_DICIEMBRE;
					LABEL_SI = data.LABEL_SI;
					LABEL_NO = data.LABEL_NO;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasBotones()_1_'+thrownError);
			}
		});	
		
}

</script>