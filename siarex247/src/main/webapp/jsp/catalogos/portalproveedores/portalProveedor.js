$(document).ready(function () {

    cargarDatosProveedor();
    cargarCertificadosProveedor();

    // Guardar certificado SAT
    $("#frmCertificadosSAT").on("submit", function (e) {
        e.preventDefault();
        guardarCertificadosProveedor();
    });

});


/* ============================================================
   CARGAR DATOS PRINCIPALES DEL PROVEEDOR
   ============================================================ */
function cargarDatosProveedor() {

    $.ajax({
        url: '/siarex247/catalogos/proveedores/portalProveedor.action',
        type: 'POST',
        dataType: 'json',

        success: function (data) {

            if (data.codError) {
                alert(data.mensaje);
                return;
            }

            // ===== DATOS GENERALES =====
            $("#idProveedor").val(data.idProveedor);
            $("#razonSocial").val(data.razonSocial);
            $("#rfc").val(data.rfc);
            $("#nombreContacto").val(data.nombreContacto);
            $("#telefono").val(data.telefono);
            $("#email").val(data.email);
            $("#nacionalidad").val(data.nacionalidad);
            $("#estado").val(data.estado);

            // ===== DOMICILIO =====
            $("#calle").val(data.calle);
            $("#colonia").val(data.colonia);
            $("#numeroExt").val(data.numeroExt);
            $("#numeroInt").val(data.numeroInt);
            $("#codigoPostal").val(data.codigoPostal);
            $("#delegacion").val(data.delegacion);
            $("#ciudad").val(data.ciudad);

            $("#confirmarMonto").val(
                data.confirmarMonto == "0" ? "Sub-Total" :
                data.confirmarMonto == "1" ? "Total" : ""
            );

            $("#anexo24").prop("checked", data.anexo24 == "S");

            // ===== CORREOS =====
            $("#email1").val(data.email1);
            $("#email2").val(data.email2);
            $("#email3").val(data.email3);
            $("#email4").val(data.email4);
            $("#email5").val(data.email5);

            // ===== SWITCHES =====
            $("#tipoEmail1").prop("checked", data.tipoEmail1 == "S");
            $("#tipoEmail2").prop("checked", data.tipoEmail2 == "S");
            $("#tipoEmail3").prop("checked", data.tipoEmail3 == "S");
            $("#tipoEmail4").prop("checked", data.tipoEmail4 == "S");
            $("#tipoEmail5").prop("checked", data.tipoEmail5 == "S");
            $("#tipoEmail6").prop("checked", data.tipoEmail6 == "S");
            $("#tipoEmail7").prop("checked", data.tipoEmail7 == "S");
            $("#tipoEmail8").prop("checked", data.tipoEmail8 == "S");
            $("#tipoEmail9").prop("checked", data.tipoEmail9 == "S");
            $("#tipoEmail10").prop("checked", data.tipoEmail10 == "S");

        },

        error: function (xhr, status, error) {
            alert("Error al cargar datos del proveedor: " + error);
        }
    });
}



/* ============================================================
   CARGAR INFORMACIÓN DEL CERTIFICADO SAT DEL PROVEEDOR
   ============================================================ */
function cargarCertificadosProveedor() {

    $.ajax({
        url: '/siarex247/catalogos/proveedores/obtenerCertificadosSAT.action',
        type: 'POST',
        dataType: 'json',

        success: function (data) {

            $("#TIENE_CERTIFICADO").prop("checked", data.tieneCertificado == "S");

            // Campos nuevos: válido desde / válido hasta
            $("#validoDesde").val(data.validoDesde || "");
            $("#validoHasta").val(data.validoHasta || "");

            // Evaluar vigencia
            evaluarVigenciaCertificado(data.validoHasta);

        },

        error: function (xhr, status, error) {
            console.error("Error cargarCertificadosProveedor(): ", error);
        }
    });

}



/* ============================================================
   EVALUAR VIGENCIA DEL CERTIFICADO Y MOSTRAR MENSAJE
   ============================================================ */
   function evaluarVigenciaCertificado(fechaVigencia) {

       let divAviso = $("#avisoVencimiento");
       divAviso.hide().html("");

       if (!fechaVigencia || fechaVigencia.trim() === "") {
           return;
       }

       let fechaFin = new Date(fechaVigencia);
       let hoy = new Date();

       let diffMs = fechaFin - hoy;
       let diasRestantes = Math.ceil(diffMs / (1000 * 60 * 60 * 24));

       // Si ya venció
       if (diasRestantes < 0) {
           divAviso
               .html("⚠️ <b style='color:#b30000;'>EL CERTIFICADO SAT HA EXPIRADO</b>")
               .css({
                   "background": "#ffd6d6",
                   "border": "1px solid #b30000",
                   "color": "#b30000"
               })
               .show();
           return;
       }

       // NUEVO: Solo mostrar si faltan 90 días o menos
       if (diasRestantes <= 90) {

           divAviso
               .html(
                   "⚠️ <b style='color:#b30000;'>FALTAN " + diasRestantes +
                   " DÍAS PARA QUE SU CERTIFICADO VENZA.</b>"
               )
               .css({
                   "background": "#ffd6d6",
                   "border": "1px solid #b30000",
                   "color": "#b30000",
                   "text-transform": "uppercase"
               })
               .show();
       }
   }




/* ============================================================
   GUARDAR CERTIFICADOS (.CER, .KEY, PASSWORD, NÚMERO)
   ============================================================ */
function guardarCertificadosProveedor() {

    let formData = new FormData(document.getElementById("frmCertificadosSAT"));

    $.ajax({
        url: '/siarex247/catalogos/proveedores/guardarCertificadosSAT.action',
        type: 'POST',
        data: formData,
        dataType: 'json',
        cache: false,
        contentType: false,
        processData: false,

        success: function (data) {

            if (data.codError == "000") {

                Swal.fire({
                    icon: "success",
                    title: "Certificados guardados",
                    text: data.mensaje
                });

                cargarCertificadosProveedor();
                $("#frmCertificadosSAT")[0].reset();

            } else {

                Swal.fire({
                    icon: "error",
                    title: "Error al guardar",
                    text: data.mensaje
                });
            }
        },

        error: function (xhr, status, error) {

            Swal.fire({
                icon: "error",
                title: "Error inesperado",
                text: error
            });
        }
    });

}
