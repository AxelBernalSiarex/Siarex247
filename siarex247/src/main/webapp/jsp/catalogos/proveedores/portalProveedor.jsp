<%@page import="com.siarex247.utils.Utils"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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

    <!-- Estilos Falcon (heredan del layout principal) -->

    <script src="/theme-falcon/repse247/lib/jquery/jquery.min.js"></script>
    <script src="/siarex247/jsp/catalogos/portalproveedores/portalProveedor.js"></script>

    <style>
        .campoLabel { color:#828282; font-weight:bold; }
        .accordion-button { font-weight:bold; }
    </style>
</head>

<div class="card mb-3" style="box-shadow:none;">

    <div class="card-header">
        <h5 class="mb-0">Portal del Proveedor</h5>
    </div>

    <div class="card-body bg-light pt-0">

        <!-- PESTAÑAS -->
        <ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabsPortalProveedor" role="tablist">

            <li class="nav-item text-nowrap" role="presentation">
                <a class="nav-link active" data-bs-toggle="tab" href="#infoPrincipal" role="tab">
                    <span class="fas fa-address-card text-600"></span>
                    <span style="padding-left:8px;">Información Principal</span>
                </a>
            </li>

            <li class="nav-item text-nowrap" role="presentation">
                <a class="nav-link" data-bs-toggle="tab" href="#infoAdicional" role="tab">
                    <span class="fas fa-list text-600"></span>
                    <span style="padding-left:8px;">Datos Adicionales</span>
                </a>
            </li>
            
                 <li class="nav-item text-nowrap" role="presentation">
                <a class="nav-link" data-bs-toggle="tab" href="#certificadosSAT" role="tab">
                    <span class="fas fa-key text-600"></span>
                    <span style="padding-left:8px;">Certificados SAT</span>
                </a>
            </li>

        </ul>

        <div class="tab-content mt-3">

            <!-- TAB 1: INFORMACIÓN PRINCIPAL -->
            <div class="tab-pane fade show active" id="infoPrincipal" role="tabpanel">

                <div class="accordion" id="accPrincipal">

                    <!-- DATOS GENERALES -->
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button bg-200" type="button" data-bs-toggle="collapse"
                                data-bs-target="#dg" aria-expanded="true">
                                Datos Generales
                            </button>
                        </h2>

                        <div id="dg" class="accordion-collapse collapse show">
                            <div class="accordion-body">

                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">Id Proveedor</label>
                                    <div class="col-sm-4">
                                        <input id="idProveedor" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Nombre del Contacto</label>
                                    <div class="col-sm-4">
                                        <input id="nombreContacto" class="form-control" readonly />
                                    </div>
                                </div>

                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">Razón Social</label>
                                    <div class="col-sm-4">
                                        <input id="razonSocial" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Nacionalidad Proveedor</label>
                                    <div class="col-sm-4">
                                        <input id="nacionalidad" class="form-control" readonly />
                                    </div>
                                </div>

                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">RFC</label>
                                    <div class="col-sm-4">
                                        <input id="rfc" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Teléfono</label>
                                    <div class="col-sm-4">
                                        <input id="telefono" class="form-control" readonly />
                                    </div>
                                </div>

                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">Correo</label>
                                    <div class="col-sm-4">
                                        <input id="email" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Estado</label>
                                    <div class="col-sm-4">
                                        <input id="estado" class="form-control" readonly />
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                </div>

            </div>

            <!-- TAB 2: DATOS ADICIONALES -->
            <div class="tab-pane fade" id="infoAdicional" role="tabpanel">

                <div class="accordion" id="accAdicional">

                    <!-- DATOS ADICIONALES -->
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button bg-200" type="button" data-bs-toggle="collapse"
                                data-bs-target="#da" aria-expanded="true">
                                Datos Adicionales
                            </button>
                        </h2>

                        <div id="da" class="accordion-collapse collapse show">
                            <div class="accordion-body">

                                <div class="mb-3 row">

						    <!-- Confirmar Monto -->
						    <label class="col-sm-2 col-form-label campoLabel">Confirmar Monto</label>
						    <div class="col-sm-4">
						        <input id="confirmarMonto" class="form-control" readonly />
						    </div>
						
						    <!-- Aplicar Anexo 24 SAT -->
						    <label class="col-sm-2 col-form-label campoLabel">Aplicar Anexo 24 SAT</label>
						    <div class="col-sm-4">
						        <div class="form-check form-switch">
						            <input class="form-check-input" id="anexo24" type="checkbox" disabled />
						        </div>
						    </div>
						
						</div>


                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">Calle</label>
                                    <div class="col-sm-4">
                                        <input id="calle" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Núm. Exterior</label>
                                    <div class="col-sm-4">
                                        <input id="numeroExt" class="form-control" readonly />
                                    </div>
                                </div>

                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">Colonia</label>
                                    <div class="col-sm-4">
                                        <input id="colonia" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Código Postal</label>
                                    <div class="col-sm-4">
                                        <input id="codigoPostal" class="form-control" readonly />
                                    </div>
                                </div>

                                <div class="mb-3 row">
                                    <label class="col-sm-2 col-form-label campoLabel">Delegación</label>
                                    <div class="col-sm-4">
                                        <input id="delegacion" class="form-control" readonly />
                                    </div>

                                    <label class="col-sm-2 col-form-label campoLabel">Ciudad</label>
                                    <div class="col-sm-4">
                                        <input id="ciudad" class="form-control" readonly />
                                    </div>
                                </div>

                                <hr/>

                         <h6><b>Correos</b></h6>

<table class="table table-sm" style="margin-bottom:0px;">
    <thead>
        <tr>
            <th>Correo</th>
            <th class="text-center">Pagos</th>
            <th class="text-center">Órdenes de Compra</th>
        </tr>
    </thead>

    <tbody>
        <tr>
            <td><input id="email1" class="form-control" readonly /></td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail1" type="checkbox" disabled />
                </div>
            </td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail2" type="checkbox" disabled />
                </div>
            </td>
        </tr>

        <tr>
            <td><input id="email2" class="form-control" readonly /></td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail3" type="checkbox" disabled />
                </div>
            </td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail4" type="checkbox" disabled />
                </div>
            </td>
        </tr>

        <tr>
            <td><input id="email3" class="form-control" readonly /></td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail5" type="checkbox" disabled />
                </div>
            </td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail6" type="checkbox" disabled />
                </div>
            </td>
        </tr>

        <tr>
            <td><input id="email4" class="form-control" readonly /></td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail7" type="checkbox" disabled />
                </div>
            </td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail8" type="checkbox" disabled />
                </div>
            </td>
        </tr>

        <tr>
            <td><input id="email5" class="form-control" readonly /></td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail9" type="checkbox" disabled />
                </div>
            </td>
            <td class="text-center">
                <div class="form-check form-switch">
                    <input class="form-check-input" id="tipoEmail10" type="checkbox" disabled />
                </div>
            </td>
        </tr>

    </tbody>
</table>



                            </div>
                        </div>

                    </div> <!-- accordion-item -->

                </div> <!-- accordion -->

            </div> <!-- tab-pane -->
            
             <!-- ******************************************************************** -->
            <!-- ⭐ TAB 3: CERTIFICADOS SAT ⭐ -->
            <!-- ******************************************************************** -->
            <div class="tab-pane fade" id="certificadosSAT" role="tabpanel">

                <div class="accordion">

                    <div class="accordion-item">

                        <h2 class="accordion-header">
                            <button class="accordion-button bg-200" data-bs-toggle="collapse"
                                    data-bs-target="#certSAT">
                                Certificados SAT (E-firma)
                            </button>
                        </h2>

                        <div id="certSAT" class="accordion-collapse collapse show">
                            <div class="accordion-body">

                                <form id="frmCertificadosSAT" enctype="multipart/form-data">

                                  <div class="row mb-3">
								    <label class="col-sm-3 campoLabel">¿Tiene Certificado?</label>
								
								    <div class="col-sm-3">
								        <div class="form-check form-switch">
								            <input class="form-check-input" id="TIENE_CERTIFICADO" type="checkbox" disabled />
								        </div>
								    </div>
								</div>


                                    <div class="row mb-3">
                                        <label class="col-sm-3 campoLabel">Archivo .CER</label>
                                        <div class="col-sm-5">
                                            <input id="fileCER" name="fileCER" type="file"
                                                   class="form-control" accept=".cer" required />
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 campoLabel">Archivo .KEY</label>
                                        <div class="col-sm-5">
                                            <input id="llavePrivada" name="llavePrivada" type="file"
                                                   class="form-control" accept=".key" required />
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 campoLabel">Contraseña E-firma</label>
                                        <div class="col-sm-5">
                                            <input id="pwdSat" name="pwdSat" type="password"
                                                   class="form-control" maxlength="100" required />
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 campoLabel">Número de Certificado</label>
                                        <div class="col-sm-5">
                                            <input id="numeroCertificado" name="numeroCertificado"
                                                   class="form-control" maxlength="50" required />
                                        </div>
                                    </div>
                                    
                                    <div class="p-4 pb-0">
									<div class="mb-2 row">
										<label class="col-sm-6 col-form-label" for="pwdSat" style="color: navy;">
											Estimado cliente le recordamos que la información que usted ingresa, se guardará en nuestros servidores de manera
											encriptada por protocolo de seguridad, por lo que la información que usted ingrese no podrá ser visualizada ni compartida
											de manera interna o externa.
										</label>
										
									</div>
								</div>	
                                    

                                    <div class="text-center mt-3">
                                        <button type="submit" class="btn btn-primary">Guardar Certificados</button>
                                    </div>

                                </form>

                            </div>
                        </div>

                    </div> <!-- accordion-item -->

                </div>

            </div> <!-- tab certificados -->

        </div> <!-- tab-content -->

    </div> <!-- card-body -->

</div> <!-- card -->

</html>
