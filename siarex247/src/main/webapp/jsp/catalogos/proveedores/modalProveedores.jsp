<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">   
    
	<script>
      var isRTL = JSON.parse(localStorage.getItem('isRTL'));
      if (isRTL) {
        var linkDefault = document.getElementById('style-default');
        var userLinkDefault = document.getElementById('user-style-default');
        linkDefault.setAttribute('disabled', true);
        userLinkDefault.setAttribute('disabled', true);
        document.querySelector('html').setAttribute('dir', 'rtl');
      } else {
        var linkRTL = document.getElementById('style-rtl');
        var userLinkRTL = document.getElementById('user-style-rtl');
        linkRTL.setAttribute('disabled', true);
        userLinkRTL.setAttribute('disabled', true);
      }
    </script>
	
</head>


<form id="frmProveedores" class="was-validated" novalidate>
	<input type="hidden" name="claveRegistro" id="idRegistro_Catalogo" value="0" >
	<input type="hidden" name="remplazarCertificacion" id="remplazarCertificacion" value="INICIO" >
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="modal-title-catalogo">Proveedor</h5>
				</div>
				<div class="p-4 pb-3">
				
					<div class="card overflow-hidden">
			
						<div class="card-header p-0 scrollbar-overlay border-bottom">
							<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
								<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabInfoPrincipal" data-bs-toggle="tab" href="#infoPrincipal" role="tab" aria-controls="infoPrincipal" aria-selected="true">
										<span class="fas fa-address-card text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_PROVEEDORES_ETQ32">Información Principal</h6>
									</a>
								</li>
								<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabDatosBancarios" data-bs-toggle="tab" href="#datosBancarios" role="tab" aria-controls="datosBancarios" aria-selected="false">
										<span class="fas fa-landmark icon text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_PROVEEDORES_ETQ33">Datos Bancarios</h6>
		                      		</a>
		                      	</li>
		                      	<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabCalificacionProveedor" data-bs-toggle="tab" href="#calificacionProveedor" role="tab" aria-controls="calificacionProveedor" aria-selected="false">
										<span class="fas fa-certificate icon text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_PROVEEDORES_ETQ34">Certificados del Proveedor</h6>
		                      		</a>
		                      	</li>
		                      	
		                      	<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabInformacionAcceso" data-bs-toggle="tab" href="#informacionAcceso" role="tab" aria-controls="informacionAcceso" aria-selected="false">
										<span class="fas fa-key icon text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_PROVEEDORES_ETQ35">Información de Acceso</h6>
		                      		</a>
		                      	</li>
							</ul>
						</div>
		                
		                <div class="tab-content">
		                	
							<!-- Informacion Principal -->
		                	<div class="card-body bg-light tab-pane active" id="infoPrincipal" role="tabpanel" aria-labelledby="tabInfoPrincipal">
								
								<div class="accordion" id="accordion1">
									
									<div class="accordion-item"><!-- accordion-item Datos Generales -->
    									<h2 class="accordion-header" id="heading1">
      										<button class="accordion-button bg-200" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="true" aria-controls="collapse1" id="CAT_PROVEEDORES_ETQ36">Datos Generales</button>
    									</h2>
    									<div class="accordion-collapse collapse show" id="collapse1" aria-labelledby="heading1" data-bs-parent="#accordion1">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="idProveedor" id="CAT_PROVEEDORES_Etiqueta_IdProveedor">Id. Proveedor</label>
													<div class="col-sm-4">
								   						<input id="idProveedor" name="idProveedor" class="form-control validarHabilitar validarHabilitar" type="text" maxlength="10"  required />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="nombreContacto" id="CAT_PROVEEDORES_Etiqueta_NombreContacto">Nombre Contacto</label>
													<div class="col-sm-4">
								   						<input id="nombreContacto" name="nombreContacto" class="form-control validarHabilitar" type="text"  maxlength="100" required />
								 					</div>
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="razonSocial" id="CAT_PROVEEDORES_Etiqueta_RazonSocial">Razón Social</label>
													<div class="col-sm-4">
								   						<input id="razonSocial" name="razonSocial" class="form-control validarHabilitar" type="text" maxlength="200" required/>
								 					</div>
								 					
								 					<label class="col-sm-2 col-form-label" for="tipoProveedor" id="CAT_PROVEEDORES_ETQ38">Nacionalidad Proveedor</label>
													<div class="col-sm-4">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="tipoProveedor" name="tipoProveedor" onchange="asignarValorRFC(this.value); cargaEstados('')" required>
														    
														</select>
														</div>
													</div>
								 					
												</div>			                		
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="rfc" id="CAT_PROVEEDORES_Etiqueta_RFC">RFC</label>
													<div class="col-sm-4">
								   						<input id="rfc" name="rfc" class="form-control validarHabilitar" type="text" minlength="12" maxlength="13" required />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="telefono" id="CAT_PROVEEDORES_Etiqueta_Telefono">Teléfono</label>
													<div class="col-sm-4">
														<input class="form-control validarHabilitar" id="telefono" name="telefono" data-input-mask='{"mask":"(999) 999-9999"}' placeholder="(XXX) XXX-XXXX" type="tel" maxlength="13" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="email" id="CAT_PROVEEDORES_ETQ37">Email </label>
													<div class="col-sm-4">
								   						<input id="email" name="email" class="form-control validarHabilitar" type="email" required />
								 					</div>
								 					
								 					<label class="col-sm-2 col-form-label" for="estado" id="CAT_PROVEEDORES_Etiqueta_Estado">Estado</label>
													<div class="col-sm-4">
								   						<div class="form-group">
															<select class="form-select" id="estado" name="estado" onchange="cargaCiudad(this.value, '');" >
														
															</select>
														</div>
								 					</div>
								 					
								 					
												</div>
												
      										</div>
    									</div>
  									</div> <!-- accordion-item Datos Generales -->
  									
  									
  									<div class="accordion-item"> <!-- accordion-item Datos de la Cuenta -->
										<h2 class="accordion-header" id="heading2">
											<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse2" aria-expanded="true" aria-controls="collapse2" id="CAT_PROVEEDORES_ETQ39">Datos de la Cuenta</button>
										</h2>
										<div class="accordion-collapse collapse" id="collapse2" aria-labelledby="heading2" data-bs-parent="#accordion1">
											<div class="accordion-body">
											
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="razonProveedor" id="CAT_PROVEEDORES_ETQ40">Tipo de Proveedor</label>
													<div class="col-sm-3">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="razonProveedor" name="razonProveedor">
														    
														</select>
														</div>
													</div>
													
													<label class="col-sm-2 col-form-label" for="formaPago" id="CAT_PROVEEDORES_EtiquetaFormaPago">Forma Pago</label>
													<div class="col-sm-5">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="formaPago" name="formaPago" onchange="seleccionarForma(this.value);">
														   
														</select>
														</div>
													</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="conServicio" id="CAT_PROVEEDORES_ConceptoServicio">Concepto Servicio</label>
													<div class="col-sm-3">
								   						<input id="conServicio" name="conServicio" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-2 col-form-label" for="pagoDolares" id="CAT_PROVEEDORES_ETQ41">Dolares</label>
													<div class="col-sm-5">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="pagoDolares" name="pagoDolares" >
														    
														</select>
														</div>
													</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroCuentaProveedor" id="CAT_PROVEEDORES_NumeroCuentaPesos">Número Cuenta</label>
													<div class="col-sm-3">
								   						<input id="numeroCuentaProveedor" name="numeroCuentaProveedor" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="pagoPesos" id="CAT_PROVEEDORES_ETQ42">Pesos</label>
													<div class="col-sm-5">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="pagoPesos" name="pagoPesos">
														    
														</select>
														</div>
													</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for=""></label>
													<div class="col-sm-3">
								   						
								 					</div>
								 					
													<label class="col-sm-2 col-form-label" for="centroCostos" id="CAT_PROVEEDORES_ETQ43">Centro de Costos</label>
													<div class="col-sm-5">
								   						<div class="form-group">
															<select class="form-select" id="centroCostos" name="centroCostos">
														
															</select>
														</div>
								 					</div>
								 					
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-4 col-form-label" for="bandDescuento" id="CAT_PROVEEDORES_ETQ44">Omitir Descuento</label>
													<div class="col-sm-8">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="bandDescuento" name="bandDescuento" type="checkbox"/>
														</div>
								 					</div>
								 					
												</div>
												
												<div class="mb-2 row">
								 					<label class="col-sm-4 col-form-label" for="notComUsuario" id="CAT_PROVEEDORES_ETQ45">Notificar Orden de Compra a Usuario</label>
													<div class="col-sm-8">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="notComUsuario" name="notComUsuario" type="checkbox"/>
														</div>
								 					</div>
								 					<label class="col-sm-4 col-form-label" for="notPagoUsuario" id="CAT_PROVEEDORES_ETQ46">Notificar Orden de Pago a Usuario</label>
													<div class="col-sm-8">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="notPagoUsuario" name="notPagoUsuario" type="checkbox"/>
														</div>
								 					</div>
												</div>
											
											</div>
										</div>
									</div><!-- accordion-item Datos de la Cuenta -->
									
									<div class="accordion-item"><!-- accordion-item Datos Adicionales -->
    									<h2 class="accordion-header" id="heading3">
      										<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse3" aria-expanded="true" aria-controls="collapse3" id="CAT_PROVEEDORES_ETQ47">Datos Adicionales</button>
    									</h2>
    									<div class="accordion-collapse collapse" id="collapse3" aria-labelledby="heading3" data-bs-parent="#accordion1">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
      												<label class="col-sm-2 col-form-label" for="tipoConfirmacion" id="CAT_PROVEEDORES_ETQ48">Confirmar Monto</label>
													<div class="col-sm-4">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="tipoConfirmacion" name="tipoConfirmacion">
														    
														</select>
														</div>
													</div>
													<label class="col-sm-3 col-form-label" for="anexo24" id="CAT_PROVEEDORES_ETQ49">Aplicar Anexo 24 SAT</label>
													<div class="col-sm-2">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="anexo24" name="anexo24" type="checkbox"/>
														</div>
								 					</div>
												</div>
      											
      											<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="calle" id="CAT_PROVEEDORES_ETQ50">Calle</label>
													<div class="col-sm-4">
								   						<input id="calle" name="calle" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="numeroExt" id="CAT_PROVEEDORES_ETQ51">Núm. Exterior</label>
													<div class="col-sm-1">
								   						<input id="numeroExt" name="numeroExt" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="numeroInt" id="CAT_PROVEEDORES_ETQ52">Núm. Interior</label>
													<div class="col-sm-1">
								   						<input id="numeroInt" name="numeroInt" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="colonia" id="CAT_PROVEEDORES_ETQ53">Colonia</label>
													<div class="col-sm-4">
								   						<input id="colonia" name="colonia" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="codigoPostal" id="CAT_PROVEEDORES_ETQ54">Código Postal</label>
													<div class="col-sm-4">
								   						<input id="codigoPostal" name="codigoPostal" class="form-control validarHabilitar" type="number" />
								 					</div>
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="delegacion" id="CAT_PROVEEDORES_EtiquetaDelegacion">Delegación</label>
													<div class="col-sm-4">
								   						<input id="delegacion" name="delegacion" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="ciudad" id="CAT_PROVEEDORES_Etiqueta_Ciudad">Ciudad</label>
													<div class="col-sm-4">
								   						<div class="form-group">
															<select class="form-select" id="ciudad" name="ciudad" >
														
															</select>
														</div>
								 					</div>	
												</div>
												
												<div class="mb-2 row">
													<div class="col-sm-12">
													
														<table class="table table-sm"  style="margin-bottom: 0px !important;">
															<thead>
																<tr>
																	<th id="CAT_PROVEEDORES_Etiqueta_Correo">Email</th>
																	<th class="text-center" id="CAT_PROVEEDORES_ETQ55">Pagos</th>
																	<th class="text-center" id="CAT_PROVEEDORES_ETQ56">Ordenes de Compra</th>
																</tr>
															</thead>
															<tbody>
																<tr>
																	<td>
																		<input id="email1" name="email1" type="email" class="form-control validarHabilitar" placeholder="Email 1" maxlength="100">
																		<div class="invalid-feedback" id="CAT_PROVEEDORES_ETQ57">Ingrese Email 1</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail1" name="tipoEmail1" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail2" name="tipoEmail2" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																</tr>
														
																<tr>
																	<td>
																		<input id="email2" name="email2" type="email" class="form-control validarHabilitar" placeholder="Email 2" maxlength="100">
																		<div class="invalid-feedback" id="CAT_PROVEEDORES_ETQ58">Ingrese Email 2</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail3" name="tipoEmail3" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail4" name="tipoEmail4" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																</tr>
														
																<tr>
																	<td>
																		<input id="email3" name="email3" type="email" class="form-control validarHabilitar" placeholder="Email 3" maxlength="100">
																		<div class="invalid-feedback" id="CAT_PROVEEDORES_ETQ59">Ingrese Email 3</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail5" name="tipoEmail5" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail6" name="tipoEmail6" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																</tr>
														
																<tr>
																	<td>
																		<input id="email4" name="email4" type="email" class="form-control validarHabilitar" placeholder="Email 4" maxlength="100">
																		<div class="invalid-feedback" id="CAT_PROVEEDORES_ETQ60">Ingrese Email 4</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail7" name="tipoEmail7" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail8" name="tipoEmail8" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																</tr>
														
														
																<tr>
																	<td>
																		<input id="email5" name="email5" type="email" class="form-control validarHabilitar" placeholder="Email 5" maxlength="100">
																		<div class="invalid-feedback" id="CAT_PROVEEDORES_ETQ61">Ingrese Email 5</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail9" name="tipoEmail9" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																	<td class="text-center">
																		<div class="switchToggle">																			
																			<div class="form-check form-switch">
													   							<input class="form-check-input validarHabilitar" id="tipoEmail10" name="tipoEmail10" type="checkbox"/>
																			</div>
																		</div>
																	</td>
																</tr>
														
															</tbody>
														</table>
													
								 					</div>
												</div>	
      										
      										</div>
    									</div>
  									</div><!-- accordion-item Datos Adicionales -->
  									
  									<div class="accordion-item"><!-- accordion-item Configuración de Factura -->
  										<h2 class="accordion-header" id="heading4">
  											<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse4" aria-expanded="true" aria-controls="collapse4" id="CAT_PROVEEDORES_ETQ62">Configuración de Factura</button>
  										</h2>
    									<div class="accordion-collapse collapse" id="collapse4" aria-labelledby="heading4" data-bs-parent="#accordion1">
    										<div class="accordion-body">
    										
    											<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="limiteTolerancia" id="CAT_PROVEEDORES_ETQ63">Límite de Tolerancia</label>
													<div class="col-sm-4">
								   						<input id="limiteTolerancia" name="limiteTolerancia" class="form-control validarHabilitar" type="number" value="0" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="limiteComplemento" id="CAT_PROVEEDORES_ETQ64">Límite Tolerancia Complemento</label>
													<div class="col-sm-4">
								   						<input id="limiteComplemento" name="limiteComplemento" class="form-control validarHabilitar" type="number" value="0" />
								 					</div>
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="AMERICANOS_SERIE" id="CAT_PROVEEDORES_ETQ65">Asignar Serie</label>
													<div class="col-sm-4">
								   						<input id="AMERICANOS_SERIE" name="AMERICANOS_SERIE" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					<label class="col-sm-2 col-form-label" for="AMERICANOS_FOLIO" id="CAT_PROVEEDORES_ETQ66">Asignar Folio Apartir</label>
													<div class="col-sm-4">
								   						<input id="AMERICANOS_FOLIO" name="AMERICANOS_FOLIO" class="form-control validarHabilitar" type="number" />
								 					</div>
												</div>	
    											
    										
    										
    											<div class="mb-2 row">
								 					<label class="col-sm-2 col-form-label" for="regimenFiscal" id="CAT_PROVEEDORES_ETQ67">Régimen Fiscal</label>
													<div class="col-sm-4">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="regimenFiscal" name="regimenFiscal">
														   
														</select>
														</div>
													</div>
								 					
								 					<label class="col-sm-2 col-form-label" for="numRegistro" id="CAT_PROVEEDORES_ETQ68">Número de Registro</label>
													<div class="col-sm-4">
								   						<input id="numRegistro" name="numRegistro" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
												    <label class="col-sm-3 col-form-label" for="PERMITIR_ACCESO_GENERADOR" id="CAT_PROVEEDORES_ETQ69">Acceso Generador de Factura</label>
													<div class="col-sm-2">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="PERMITIR_ACCESO_GENERADOR" name="PERMITIR_ACCESO_GENERADOR" type="checkbox"/>
														</div>
								 					</div>
												</div>
												
												<div class="mb-2 row">      												
													<label class="col-sm-3 col-form-label" for="bandImss" id="CAT_PROVEEDORES_ETQ70">Omitir Certificaciones IMSS</label>
													<div class="col-sm-2">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="bandImss" name="bandImss" type="checkbox"/>
														</div>
								 					</div>
												</div>
												
												<div class="mb-2 row">      												
								 					<label class="col-sm-3 col-form-label" for="bandSat" id="CAT_PROVEEDORES_ETQ71">Omitir Certificaciones SAT</label>
								 					<div class="col-sm-2">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="bandSat" name="bandSat" type="checkbox"/>
														</div>
								 					</div>
												</div>
												
												<div class="mb-2 row">      												
								 					<label class="col-sm-3 col-form-label" for="bandCartaPorte" id="CAT_PROVEEDORES_ETQ72">Aplicar Carta Porte</label>
								 					<div class="col-sm-2">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="bandCartaPorte" name="bandCartaPorte" type="checkbox"/>
														</div>
								 					</div>
												</div>
												
												<div class="mb-2 row">      												
								 					<label class="col-sm-3 col-form-label" for="bandInstrucciones" id="CAT_PROVEEDORES_ETQ73">Enviar Instrucciones</label>
								 					<div class="col-sm-2">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="bandInstrucciones" name="bandInstrucciones" type="checkbox"/>
														</div>
								 					</div>
												</div>
												
    										
    										</div>
    									</div>
    								</div><!-- accordion-item Configuración de Factura -->
								
								</div><!-- accordion -->

		                	</div><!-- Informacion Principal -->
		                	
		                	
		                	<!-- Datos Bancarios -->
		                	<div class="card-body bg-light tab-pane" id="datosBancarios" role="tabpanel" aria-labelledby="tabDatosBancarios">
		                		
								<div class="accordion" id="accordion2">
									
									<div class="accordion-item"><!-- accordion-item Datos de Banco en Pesos -->
    									<h2 class="accordion-header" id="heading5">
      										<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse5" aria-expanded="true" aria-controls="collapse5" id="CAT_PROVEEDORES_ETQ74">Datos del Banco en Pesos</button>
    									</h2>
    									<div class="accordion-collapse collapse" id="collapse5" aria-labelledby="heading5" data-bs-parent="#accordion2">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="banco" id="CAT_PROVEEDORES_Etiqueta_BancoPesos">Banco</label>
													<div class="col-sm-10">
								   						<input id="banco" name="banco" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="sucursal" id="CAT_PROVEEDORES_Etiqueta_SucursalPesos">Sucursal</label>
													<div class="col-sm-4">
								   						<input id="sucursal" name="sucursal" class="form-control validarHabilitar" type="text" />
								 					</div>
													<label class="col-sm-2 col-form-label" for="nombreSucursal" id="CAT_PROVEEDORES_Etiqueta_NombreSucursalPesos">Nombre Sucursal</label>
													<div class="col-sm-4">
								   						<input id="nombreSucursal" name="nombreSucursal" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
												</div>			                		
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroCuenta" id="CAT_PROVEEDORES_Etiqueta_NumeroCuentaPesos">Núm. de Cuenta</label>
													<div class="col-sm-4">
														<input class="form-control validarHabilitar" id="numeroCuenta" name="numeroCuenta" data-input-mask='{"mask":"9999 9999 9999 9999"}' placeholder="XXXX XXXX XXXX XXXX" type="text" />
								 					</div>
								 					
								 					
													<label class="col-sm-2 col-form-label" for="cuentaClabe" id="CAT_PROVEEDORES_Etiqueta_CuentaClabePesos">Cuenta Clabe</label>
													<div class="col-sm-4">
														<input id="cuentaClabe" name="cuentaClabe" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroConvenio" id="CAT_PROVEEDORES_ETQ75">Número Convenio</label>
													<div class="col-sm-4">
														<input id="numeroConvenio" name="numeroConvenio" class="form-control validarHabilitar" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="moneda" id="CAT_PROVEEDORES_Etiqueta_MonedaPesos">Moneda</label>
													<div class="col-sm-4">
														<input id="moneda" name="moneda" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
      										</div>
    									</div>
  									</div> <!-- accordion-item Datos de Banco en Pesos -->
  									
  									<div class="accordion-item"> <!-- accordion-item Datos de Banco en Dolares -->
										<h2 class="accordion-header" id="heading6">
      										<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse6" aria-expanded="true" aria-controls="collapse6" id="CAT_PROVEEDORES_ETQ76">Datos del Banco en Dolares</button>
    									</h2>
    									<div class="accordion-collapse collapse" id="collapse6" aria-labelledby="heading6" data-bs-parent="#accordion2">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="bancoDollar" id="CAT_PROVEEDORES_ETQ77">Banco</label>
													<div class="col-sm-10">
								   						<input id="bancoDollar" name="bancoDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="sucursalDollar" id="CAT_PROVEEDORES_ETQ78">Sucursal</label>
													<div class="col-sm-4">
								   						<input id="sucursalDollar" name="sucursalDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
													<label class="col-sm-2 col-form-label" for="nombreSucursalDollar" id="CAT_PROVEEDORES_ETQ79">Nombre Sucursal</label>
													<div class="col-sm-4">
								   						<input id="nombreSucursalDollar" name="nombreSucursalDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
												</div>			                		
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroCuentaDollar" id="CAT_PROVEEDORES_ETQ80">Núm. de Cuenta</label>
													<div class="col-sm-4">
														<input class="form-control validarHabilitar" id="numeroCuentaDollar" name="numeroCuentaDollar" data-input-mask='{"mask":"9999 9999 9999 9999"}' placeholder="XXXX XXXX XXXX XXXX" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="cuentaClabeDollar" id="CAT_PROVEEDORES_ETQ81">Cuenta Clabe</label>
													<div class="col-sm-4">
														<input id="cuentaClabeDollar" name="cuentaClabeDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroConvenioDollar" id="CAT_PROVEEDORES_ETQ82">Número Convenio</label>
													<div class="col-sm-4">
														<input id="numeroConvenioDollar" name="numeroConvenioDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="monedaDollar" id="CAT_PROVEEDORES_ETQ83">Moneda</label>
													<div class="col-sm-4">
														<input id="monedaDollar" name="monedaDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="abaDollar" id="CAT_PROVEEDORES_ETQ84">Aba</label>
													<div class="col-sm-4">
														<input id="abaDollar" name="abaDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="switfCodeDollar" id="CAT_PROVEEDORES_ETQ85">Swift Code</label>
													<div class="col-sm-4">
														<input id="switfCodeDollar" name="switfCodeDollar" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
      										</div>
    									</div>
									</div><!-- accordion-item Datos de Banco en Dolares -->
									
									<div class="accordion-item"><!-- accordion-item Datos Otro Banco -->
    									<h2 class="accordion-header" id="heading7">
      										<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse7" aria-expanded="true" aria-controls="collapse7" id="CAT_PROVEEDORES_ETQ86">Datos de Otro Banco</button>
    									</h2>
    									<div class="accordion-collapse collapse" id="collapse7" aria-labelledby="heading7" data-bs-parent="#accordion2">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="bancoOtro" id="CAT_PROVEEDORES_ETQ87">Banco</label>
													<div class="col-sm-10">
								   						<input id="bancoOtro" name="bancoOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
												</div>	
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="sucursalOtro" id="CAT_PROVEEDORES_ETQ88">Sucursal</label>
													<div class="col-sm-4">
								   						<input id="sucursalOtro" name="sucursalOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
													<label class="col-sm-2 col-form-label" for="nombreSucursalOtro" id="CAT_PROVEEDORES_ETQ89">Nombre Sucursal</label>
													<div class="col-sm-4">
								   						<input id="nombreSucursalOtro" name="nombreSucursalOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
												</div>			                		
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroCuentaOtro" id="CAT_PROVEEDORES_ETQ90">Núm. de Cuenta</label>
													<div class="col-sm-4">
														<input class="form-control validarHabilitar" id="numeroCuentaOtro" name="numeroCuentaOtro" data-input-mask='{"mask":"9999 9999 9999 9999"}' placeholder="XXXX XXXX XXXX XXXX" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="cuentaClabeOtro" id="CAT_PROVEEDORES_ETQ91">Cuenta Clabe</label>
													<div class="col-sm-4">
														<input id="cuentaClabeOtro" name="cuentaClabeOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numeroConvenioOtro" id="CAT_PROVEEDORES_ETQ92">Número Convenio</label>
													<div class="col-sm-4">
														<input id="numeroConvenioOtro" name="numeroConvenioOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="monedaOtro" id="CAT_PROVEEDORES_ETQ93">Moneda</label>
													<div class="col-sm-4">
														<input id="monedaOtro" name="monedaOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="abaOtro" id="CAT_PROVEEDORES_ETQ94">Aba</label>
													<div class="col-sm-4">
														<input id="abaOtro" name="abaOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
													
													<label class="col-sm-2 col-form-label" for="switfCodeOtro" id="CAT_PROVEEDORES_ETQ95">Swift Code</label>
													<div class="col-sm-4">
														<input id="switfCodeOtro" name="switfCodeOtro" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
      										
      										</div>
    									</div>
  									</div><!-- accordion-item Datos Otro Banco -->
  									
								</div><!-- accordion -->
		                		
		                	</div><!-- Datos Bancarios -->
		                	
		                	
		                	<!-- Calificacion del Proveedor -->
		                	<div class="card-body bg-light tab-pane" id="calificacionProveedor" role="tabpanel" aria-labelledby="tabCalificacionProveedor">
		                		
		                		
		                		<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="filesIMSS" id="CAT_PROVEEDORES_ETQ96">Importar Certificación IMSS</label>
									<div class="col-sm-9">
										<input class="form-control validarHabilitar form-control validarHabilitar-sm" id="filesIMSS" name="filesIMSS" type="file" accept="application/pdf"/>
								 	</div>
								</div>
								
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="filesSAT" id="CAT_PROVEEDORES_ETQ97">Importar Certificación SAT</label>
									<div class="col-sm-9">
										<input class="form-control validarHabilitar form-control validarHabilitar-sm" id="filesSAT" name="filesSAT" type="file" accept="application/pdf" />
								 	</div>
								</div>
								
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="filesConfidencialidad" id="CAT_PROVEEDORES_ETQ98">Importar Contrato de Confidencialidad</label>
									<div class="col-sm-9">
										<input class="form-control validarHabilitar form-control validarHabilitar-sm" id="filesConfidencialidad" name="filesConfidencialidad" type="file" accept="application/pdf" />
								 	</div>
								</div>
								
								
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="txtCertificaIMSS" id="CAT_PROVEEDORES_ETQ99">Certificación del IMSS</label>
									<div class="col-sm-4">
										<input id="txtCertificaIMSS" name="txtCertificaIMSS" class="form-control validarHabilitar" type="text" value="" />
				 					</div>
								</div>
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="txtCertificaSAT" id="CAT_PROVEEDORES_ETQ100">Certificación del SAT</label>
									<div class="col-sm-4">
										<input id="txtCertificaSAT" name="txtCertificaSAT" class="form-control validarHabilitar" type="text" value="" />
				 					</div>
								</div>
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="txtConfidencial" id="CAT_PROVEEDORES_ETQ101">Contrato de Confidencialidad</label>
									<div class="col-sm-4">
										<input id="txtConfidencial" name="txtConfidencial" class="form-control validarHabilitar" type="text" value="" />
				 					</div>
								</div>
								
		                	</div><!-- Calificacion del Proveedor -->
		                	
		                	<div class="card-body bg-light tab-pane" id="informacionAcceso" role="tabpanel" aria-labelledby="tabInformacionAcceso">
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="usrAcceso" id="CAT_PROVEEDORES_ETQ102">Email de Acceso</label>
									<div class="col-sm-8">
										<input id="usrAcceso" name="usrAcceso" class="form-control validarHabilitar" type="email" />
				 					</div>
								</div>
								
								<div class="mb-2 row">
								    <label class="col-sm-3 col-form-label" for="chkAcceso" id="CAT_PROVEEDORES_ETQ103">Permitir acceso</label>
									<div class="col-sm-2">
									   <div class="switchToggle">																			
										<div class="form-check form-switch">
				   							<input class="form-check-input validarHabilitar" id="permitirAcceso" name="permitirAcceso" type="checkbox"/>
										</div>
									  </div>
									</div>
								</div>
								
		                	</div> <!-- informacionAcceso -->
		                	
		                	
		                </div>
					
					</div>
				
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" type="button" id="CAT_PROVEEDORES_Boton_Cerrar" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>


 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#frmProveedores").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmProveedores').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   var idRegistro = $('#idRegistro_Catalogo').val();
        	   if (idRegistro == 0){
				   guardarCatalogo();
			   }else{
				   actualizarCatalogo();
			   }
        	   
			},
           errorPlacement: function (error, e) {
        	   e.parents('.form-group').append(error);
           },
           highlight: function (element) {
               $(element).closest('.form-group').removeClass('has-success has-error').addClass('has-error');
           },
           success: function (e) {
               e.closest('.form-group').removeClass('has-success has-error').addClass('has-success');
           }, rules:  {
                select: {required: true},
        	    rfc : {
	               required: true,
	               minlength : 10,
                   maxlength: 13
	            }
           
           }, messages: {
                select: {required: 'error'},
               /*rfc : {
            	   required: "Please enter a username",
            	   minlength: "Username must be at least 5 characters long"
	            }*/
               
           }
       }).resetForm(); 
	   	   	   
	   
	   $('#razonProveedor').on('change', function() { 
		   $(this).trigger('blur');
	   });
	   
	   $('#tipoProveedor').on('change', function() { 
		   $(this).trigger('blur');
	   });
	   
	   $('#formaPago').on('change', function() { 
		   $(this).trigger('blur');
	   });
	   	   
	   $('#pagoDolares').on('change', function() { 
		   $(this).trigger('blur');
	   });
	   
	   $('#pagoPesos').on('change', function() { 
		   $(this).trigger('blur');
	   });
	   
	   $('#tipoConfirmacion').on('change', function() { 
		   $(this).trigger('blur');
	   });
	   
	   $('#regimenFiscal').on('change', function() { 
		   $(this).trigger('blur');
	   });

	   
	   calcularEtiquetasCatalogoProveedoresModal();
	   	   
	});
	
   

	 function calcularEtiquetasCatalogoProveedoresModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_PROVEEDORES'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						
						document.getElementById("CAT_PROVEEDORES_ETQ32").innerHTML = data.ETQ32;
						document.getElementById("CAT_PROVEEDORES_ETQ33").innerHTML = data.ETQ33;
						document.getElementById("CAT_PROVEEDORES_ETQ34").innerHTML = data.ETQ34;
						document.getElementById("CAT_PROVEEDORES_ETQ35").innerHTML = data.ETQ35;
						document.getElementById("CAT_PROVEEDORES_ETQ36").innerHTML = data.ETQ36;
						document.getElementById("CAT_PROVEEDORES_ETQ37").innerHTML = data.ETQ37;
						document.getElementById("CAT_PROVEEDORES_ETQ38").innerHTML = data.ETQ38;
						document.getElementById("CAT_PROVEEDORES_ETQ39").innerHTML = data.ETQ39;
						document.getElementById("CAT_PROVEEDORES_ETQ40").innerHTML = data.ETQ40;
						document.getElementById("CAT_PROVEEDORES_ETQ41").innerHTML = data.ETQ41;
						document.getElementById("CAT_PROVEEDORES_ETQ42").innerHTML = data.ETQ42;
						document.getElementById("CAT_PROVEEDORES_ETQ43").innerHTML = data.ETQ43;
						document.getElementById("CAT_PROVEEDORES_ETQ44").innerHTML = data.ETQ44;
						document.getElementById("CAT_PROVEEDORES_ETQ45").innerHTML = data.ETQ45;
						document.getElementById("CAT_PROVEEDORES_ETQ46").innerHTML = data.ETQ46;
						document.getElementById("CAT_PROVEEDORES_ETQ47").innerHTML = data.ETQ47;
						document.getElementById("CAT_PROVEEDORES_ETQ48").innerHTML = data.ETQ48;
						document.getElementById("CAT_PROVEEDORES_ETQ49").innerHTML = data.ETQ49;
						document.getElementById("CAT_PROVEEDORES_ETQ50").innerHTML = data.ETQ50;
						document.getElementById("CAT_PROVEEDORES_ETQ51").innerHTML = data.ETQ51;
						document.getElementById("CAT_PROVEEDORES_ETQ52").innerHTML = data.ETQ52;
						document.getElementById("CAT_PROVEEDORES_ETQ53").innerHTML = data.ETQ53;
						document.getElementById("CAT_PROVEEDORES_ETQ54").innerHTML = data.ETQ54;
						document.getElementById("CAT_PROVEEDORES_ETQ55").innerHTML = data.ETQ55;
						document.getElementById("CAT_PROVEEDORES_ETQ56").innerHTML = data.ETQ56;
						document.getElementById("CAT_PROVEEDORES_ETQ57").innerHTML = data.ETQ57;
						document.getElementById("CAT_PROVEEDORES_ETQ58").innerHTML = data.ETQ58;
						document.getElementById("CAT_PROVEEDORES_ETQ59").innerHTML = data.ETQ59;
						document.getElementById("CAT_PROVEEDORES_ETQ60").innerHTML = data.ETQ60;
						document.getElementById("CAT_PROVEEDORES_ETQ61").innerHTML = data.ETQ61;
						document.getElementById("CAT_PROVEEDORES_ETQ62").innerHTML = data.ETQ62;
						document.getElementById("CAT_PROVEEDORES_ETQ63").innerHTML = data.ETQ63;
						document.getElementById("CAT_PROVEEDORES_ETQ64").innerHTML = data.ETQ64;
						document.getElementById("CAT_PROVEEDORES_ETQ65").innerHTML = data.ETQ65;
						document.getElementById("CAT_PROVEEDORES_ETQ66").innerHTML = data.ETQ66;
						document.getElementById("CAT_PROVEEDORES_ETQ67").innerHTML = data.ETQ67;
						document.getElementById("CAT_PROVEEDORES_ETQ68").innerHTML = data.ETQ68;
						document.getElementById("CAT_PROVEEDORES_ETQ69").innerHTML = data.ETQ69;
						document.getElementById("CAT_PROVEEDORES_ETQ70").innerHTML = data.ETQ70;
						document.getElementById("CAT_PROVEEDORES_ETQ71").innerHTML = data.ETQ71;
						document.getElementById("CAT_PROVEEDORES_ETQ72").innerHTML = data.ETQ72;
						document.getElementById("CAT_PROVEEDORES_ETQ73").innerHTML = data.ETQ73;
						document.getElementById("CAT_PROVEEDORES_ETQ74").innerHTML = data.ETQ74;
						document.getElementById("CAT_PROVEEDORES_ETQ75").innerHTML = data.ETQ75;
						document.getElementById("CAT_PROVEEDORES_ETQ76").innerHTML = data.ETQ76;
						document.getElementById("CAT_PROVEEDORES_ETQ77").innerHTML = data.ETQ77;
						document.getElementById("CAT_PROVEEDORES_ETQ78").innerHTML = data.ETQ78;
						document.getElementById("CAT_PROVEEDORES_ETQ79").innerHTML = data.ETQ79;
						document.getElementById("CAT_PROVEEDORES_ETQ80").innerHTML = data.ETQ80;
						document.getElementById("CAT_PROVEEDORES_ETQ81").innerHTML = data.ETQ81;
						document.getElementById("CAT_PROVEEDORES_ETQ82").innerHTML = data.ETQ82;
						document.getElementById("CAT_PROVEEDORES_ETQ83").innerHTML = data.ETQ83;
						document.getElementById("CAT_PROVEEDORES_ETQ84").innerHTML = data.ETQ84;
						document.getElementById("CAT_PROVEEDORES_ETQ85").innerHTML = data.ETQ85;
						document.getElementById("CAT_PROVEEDORES_ETQ86").innerHTML = data.ETQ86;
						document.getElementById("CAT_PROVEEDORES_ETQ87").innerHTML = data.ETQ87;
						document.getElementById("CAT_PROVEEDORES_ETQ88").innerHTML = data.ETQ88;
						document.getElementById("CAT_PROVEEDORES_ETQ89").innerHTML = data.ETQ89;
						document.getElementById("CAT_PROVEEDORES_ETQ90").innerHTML = data.ETQ90;
						document.getElementById("CAT_PROVEEDORES_ETQ91").innerHTML = data.ETQ91;
						document.getElementById("CAT_PROVEEDORES_ETQ92").innerHTML = data.ETQ92;
						document.getElementById("CAT_PROVEEDORES_ETQ93").innerHTML = data.ETQ93;
						document.getElementById("CAT_PROVEEDORES_ETQ94").innerHTML = data.ETQ94;
						document.getElementById("CAT_PROVEEDORES_ETQ95").innerHTML = data.ETQ95;
						document.getElementById("CAT_PROVEEDORES_ETQ96").innerHTML = data.ETQ96;
						document.getElementById("CAT_PROVEEDORES_ETQ97").innerHTML = data.ETQ97;
						document.getElementById("CAT_PROVEEDORES_ETQ98").innerHTML = data.ETQ98;
						document.getElementById("CAT_PROVEEDORES_ETQ99").innerHTML = data.ETQ99;
						document.getElementById("CAT_PROVEEDORES_ETQ100").innerHTML = data.ETQ100;
						document.getElementById("CAT_PROVEEDORES_ETQ101").innerHTML = data.ETQ101;
						document.getElementById("CAT_PROVEEDORES_ETQ102").innerHTML = data.ETQ102;
						document.getElementById("CAT_PROVEEDORES_ETQ103").innerHTML = data.ETQ103;
						
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_IdProveedor").innerHTML = data.ETQ9;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_NombreContacto").innerHTML = data.ETQ17;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_RazonSocial").innerHTML = data.ETQ10;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_Estado").innerHTML = data.ETQ14;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_RFC").innerHTML = data.ETQ11;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_Telefono").innerHTML = data.ETQ15;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_Ciudad").innerHTML = data.ETQ13;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_Correo").innerHTML = data.ETQ37;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_BancoPesos").innerHTML = data.ETQ22;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_SucursalPesos").innerHTML = data.ETQ23;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_NombreSucursalPesos").innerHTML = data.ETQ24;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_NumeroCuentaPesos").innerHTML = data.ETQ25;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_CuentaClabePesos").innerHTML = data.ETQ26;
						 document.getElementById("CAT_PROVEEDORES_Etiqueta_MonedaPesos").innerHTML = data.ETQ27;
						 document.getElementById("CAT_PROVEEDORES_EtiquetaFormaPago").innerHTML = data.ETQ30;
						 
						 
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CAT_PROVEEDORES_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
   
   function cargaNacionalidad(nacionalidad) {
 		try {
 			
 			$('#tipoProveedor').empty();
 			
 			if (nacionalidad == ''){
 				$('#tipoProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', '').text('Seleccione una Opción'));
 				$('#tipoProveedor').append($('<option></option>').attr('value', 'MEX').text('México'));
 				$('#tipoProveedor').append($('<option></option>').attr('value', 'USA').text('Estados Unidos'));
 			}else if (nacionalidad == 'MEX'){
 				$('#tipoProveedor').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
 				$('#tipoProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', 'MEX').text('México'));
 				$('#tipoProveedor').append($('<option></option>').attr('value', 'USA').text('Estados Unidos'));
 			}else if (nacionalidad == 'USA'){
 				$('#tipoProveedor').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
 				$('#tipoProveedor').append($('<option></option>').attr('value', 'MEX').text('México'));
 				$('#tipoProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', 'USA').text('Estados Unidos'));
 			}
 		}catch(e) {
 			alert("cargaNacionalidad()_"+e);
 		} 
 	}
   
   function cargaRazon(razonProveedor) {
		try {
			$('#razonProveedor').empty();
			
			if (razonProveedor == ''){
				$('#razonProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', '').text('Seleccione una Opción'));
				$('#razonProveedor').append($('<option></option>').attr('value', 'FIS').text('Persona Fisica'));
				$('#razonProveedor').append($('<option></option>').attr('value', 'MOR').text('Persona Moral'));
			}else if (razonProveedor == 'FIS'){
				$('#razonProveedor').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
				$('#razonProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', 'FIS').text('Persona Fisica'));
				$('#razonProveedor').append($('<option></option>').attr('value', 'MOR').text('Persona Moral'));
			}else if (razonProveedor == 'MOR'){
				$('#razonProveedor').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
				$('#razonProveedor').append($('<option></option>').attr('value', 'FIS').text('Persona Fisica'));
				$('#razonProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', 'MOR').text('Persona Moral'));
			}
		}catch(e) {
			alert("cargaRazon()_"+e);
		} 
	}

   
   function seleccionarForma(valorForma){
	 try {
		 $('#pagoDolares').empty();
		 $('#pagoPesos').empty();
		 
		 if (valorForma == ''){
			 $('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'NON').text('Seleccione una Opción'));
			 $('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'NON').text('Seleccione una Opción'));
		 }else if (valorForma == 'WIR'){
			 $('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'WIR').text('Transferencia'));
			 $('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'WIR').text('Transferencia'));
		 }else if (valorForma == 'CHK'){
			 $('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'CHK').text('Cheque'));
			 $('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'CHK').text('Cheque'));
			 
		 }else if (valorForma == 'AMB'){
				$('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'CHK').text('Cheque'));

				$('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				
		 }
	 }  catch(e){
		 alert('seleccionarForma()_'+e);
	 }
   }
   
   function cargaFormaPago(formaPago) {
		try {
			$('#formaPago').empty();
			if (formaPago == ''){
				$('#formaPago').append($('<option></option>').attr('selected', 'selected').attr('value', '').text('Seleccione una Opción'));
				$('#formaPago').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#formaPago').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				$('#formaPago').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (formaPago == 'WIR'){
				$('#formaPago').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
				$('#formaPago').append($('<option></option>').attr('selected', 'selected').attr('value', 'WIR').text('Transferencia'));
				$('#formaPago').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				$('#formaPago').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (formaPago == 'CHK'){
				$('#formaPago').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
				$('#formaPago').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#formaPago').append($('<option></option>').attr('selected', 'selected').attr('value', 'CHK').text('Cheque'));
				$('#formaPago').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (formaPago == 'AMB'){
				$('#formaPago').append($('<option></option>').attr('value', '').text('Seleccione una Opción'));
				$('#formaPago').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#formaPago').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				$('#formaPago').append($('<option></option>').attr('selected', 'selected').attr('value', 'AMB').text('Transferencia y Cheque'));
			}
		}catch(e) {
			alert("cargaFormaPago()_"+e);
		} 
	}

   
   function cargaPagoDolares(pagoDolares) {
		try {
			$('#pagoDolares').empty();
			if (pagoDolares == 'NON'){
				$('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				//$('#pagoDolares').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (pagoDolares == 'WIR'){
				$('#pagoDolares').append($('<option></option>').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'WIR').text('Transferencia'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				//$('#pagoDolares').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (pagoDolares == 'CHK'){
				$('#pagoDolares').append($('<option></option>').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'CHK').text('Cheque'));
				// $('#pagoDolares').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}/*else if (pagoDolares == 'AMB'){
				$('#pagoDolares').append($('<option></option>').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoDolares').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				$('#pagoDolares').append($('<option></option>').attr('selected', 'selected').attr('value', 'AMB').text('Transferencia y Cheque'));
			}*/
		}catch(e) {
			alert("cargaPagoDolares()_"+e);
		} 
	}
   
   
   function cargaPagoPesos(pagoPesos) {
		try {
			
			$('#pagoPesos').empty();
			if (pagoPesos == 'NON'){
				$('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				//$('#pagoPesos').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (pagoPesos == 'WIR'){
				$('#pagoPesos').append($('<option></option>').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'WIR').text('Transferencia'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				//$('#pagoPesos').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}else if (pagoPesos == 'CHK'){
				$('#pagoPesos').append($('<option></option>').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'CHK').text('Cheque'));
				//$('#pagoPesos').append($('<option></option>').attr('value', 'AMB').text('Transferencia y Cheque'));
			}/*else if (pagoPesos == 'AMB'){
				$('#pagoPesos').append($('<option></option>').attr('value', 'NON').text('Seleccione una Opción'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'WIR').text('Transferencia'));
				$('#pagoPesos').append($('<option></option>').attr('value', 'CHK').text('Cheque'));
				$('#pagoPesos').append($('<option></option>').attr('selected', 'selected').attr('value', 'AMB').text('Transferencia y Cheque'));
			}*/
		}catch(e) {
			alert("cargapagoPesos()_"+e);
		} 
	}
   
   
   function cargaTipoConfirmacion(tipoConfirmacion) {
		try {
			$('#tipoConfirmacion').empty();
			if (tipoConfirmacion == '0'){
				$('#tipoConfirmacion').append($('<option></option>').attr('selected', 'selected').attr('value', '0').text('Sub-Total'));
				$('#tipoConfirmacion').append($('<option></option>').attr('value', '1').text('Total'));
			}else if (tipoConfirmacion == '1'){
				$('#tipoConfirmacion').append($('<option></option>').attr('value', '0').text('Sub-Total'));
				$('#tipoConfirmacion').append($('<option></option>').attr('selected', 'selected').attr('value', '1').text('Total'));
			}else {
				$('#tipoConfirmacion').append($('<option></option>').attr('selected', 'selected').attr('value', '0').text('Sub-Total'));
				$('#tipoConfirmacion').append($('<option></option>').attr('value', '1').text('Total'));
			}
		}catch(e) {
			alert("cargaTipoConfirmacion()_"+e);
		} 
	}
   

   function cargaRegimenFisca(idRegimen) {
  		try {
  			$('#regimenFiscal').empty();
  			$.ajax({
  	           url:  '/siarex247/configuracion/regimenFiscal/comboClavesRegimen.action?accion=2',
  	           type: 'POST',
  	            dataType : 'json',
  			    success: function(data){
  			    	$('#regimenFiscal').empty();
  			    	$.each(data.data, function(key, text) {
  				    	if (idRegimen == text.claveRegimen){
  				    		$('#regimenFiscal').append($('<option></option>').attr('selected', 'selected').attr('value', text.claveRegimen).text(text.descripcion));
  				    	}else{
  				    		$('#regimenFiscal').append($('<option></option>').attr('value', text.claveRegimen).text(text.descripcion));
  				    	}
  			    		
  			      	});
  			    }
  			});
  		}catch(e) {
  			alert("cargaRegimenFisca()_"+e);
  		} 
  	}
   
   
   function cargaCentros(idCentro) {
		try {
			$('#centroCostos').empty();
			$.ajax({
				url:  '/siarex247/catalogos/centroCostos/comboCentrosCostos.action',
	           data : null,
	           type: 'POST',
	            dataType : 'json',
			    success: function(data){
			    	$('#centroCostos').empty();
			    	$.each(data.data, function(key, text) {
			    		if (idCentro == text.idCentroCosto){
			    			$('#centroCostos').append($('<option></option>').attr('selected', 'selected').attr('value', text.idCentroCosto).text(text.departamento));
			    		}else{
			    			$('#centroCostos').append($('<option></option>').attr('value', text.idCentroCosto).text(text.departamento));	
			    		}
			    		
			      	});
			    }
			});
		}catch(e) {
			alert("cargaCentros()_"+e);
		} 
	}
   
   
   function asignarValorRFC(valorTipo){
	   if (valorTipo == 'USA'){
		   $('#rfc').val('INTERNACIONAL');
		   $( "#rfc" ).prop( "readonly", true );
	   }else{
		   var rfc = $('#rfc').val('');
		   if (rfc == 'INTERNACIONAL'){
			   $('#rfc').val('');
		   }
		   $( "#rfc" ).prop( "readonly", false );
	   }
   }
   
 </script>
</html>