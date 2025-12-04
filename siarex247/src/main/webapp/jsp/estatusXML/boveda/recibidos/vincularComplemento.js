var tiempoLog = true;
var tablaDetalleVincular = null;

$(document).ready(function () {
  try {
    tablaDetalleVincular = $('#tablaDetalleVincular').DataTable({
      paging: true,
      retrieve: true,
      pageLength: 10,
      lengthChange: false,
      dom:
        "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" +
        "<'table-responsive scrollbar'tr>" +
        "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
      ordering: true,
      serverSide: false,
      fixedHeader: true,
      orderCellsTop: true,
      info: true,
      select: false,
      stateSave: false,
      order: [[0, 'asc']],
      buttons: [
        {
          extend: 'collection',
          autoClose: true,
          text:
            '<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
          className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
          buttons: [
            {
              extend: 'excel',
              text: 'Exportar Excel',
              title: 'Detalle Vincular',
              exportOptions: { modifier: { selected: null, page: 'all' } },
              filename: 'detalleVincular'
            }
          ]
        }
      ],
      language: {
        processing: 'Procesando...',
        zeroRecords: 'No se encontraron resultados',
        emptyTable: 'Ningún dato disponible en esta tabla',
        info: 'Mostrando _START_ al _END_ de _TOTAL_ registros',
        infoEmpty: 'No hay registros disponibles',
        infoFiltered: '(filtrado de un total de _MAX_ registros)',
        search: 'Buscar:',
        infoThousands: ',',
        loadingRecords: 'Cargando...',
        oPaginate: {
          sFirst: 'Primero',
          sLast: 'Último',
          sNext: "<span class='fa fa-chevron-right fa-w-10'></span>",
          sPrevious: "<span class='fa fa-chevron-left fa-w-10'></span>"
        }
      },
      ajax: {
        url: '/siarex247/cumplimientoFiscal/boveda/recibidos/mensajeComplementos.action',
        type: 'POST',
        data: {
          iden: function () {
            return obtenerIDENTIFICADOR();
          }
        }
      },
      aoColumns: [
        { mData: 'UUID', sClass: 'alinearCentro' },
        { mData: 'ESTATUS' },
        { mData: 'MENSAJE' }
      ],
      initComplete: function () {
        var btns = $('.dt-button');
        btns.removeClass('dt-button');

        var btnsSubMenu = $('.dtb-b2');
        btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');
      },
      drawCallback: function () {}
    });
  } catch (e) {
    alert('usuarios()_' + e);
  }

  tablaDetalleVincular.on('draw', function () {
    $('[data-toggle="tooltip"]').tooltip();
  });
});

/* ================== Helpers ================== */
function obtenerIDENTIFICADOR() {
  var IDEN = $('#IDEN').val();
  return IDEN;
}

function cierraVentana() {
  tiempoLog = false;
}

function monitorConsola() {
  try {
    if (tiempoLog) {
      setTimeout(timerConsola, 2000);
    }
  } catch (e) {
    alert('monitorConsola(): ' + e);
  }
}

function timerConsola() {
  try {
    $('#tablaDetalleVincular').DataTable().ajax.reload(null, false);
    calcularProcesados();
  } catch (e) {
    alert('timerConsola(): ' + e);
  }
}

function calcularProcesados() {
  try {
    var iden = obtenerIDENTIFICADOR();
    $.ajax({
      url: '/siarex247/cumplimientoFiscal/boveda/recibidos/calcularProcesados.action',
      type: 'POST',
      data: { iden: iden },
      dataType: 'json',
      success: function (data) {
        var totReg = $('#totalXML').val();
        var totalVinculados = data.TOT_OK;
        var totalError = data.TOT_NOK;
        $('#totalVinculados').val(totalVinculados);
        $('#totalError').val(totalError);

        if (totReg > totalVinculados + totalError) {
          if (tiempoLog) monitorConsola();
        }
      },
      error: function (xhr, ajaxOptions, thrownError) {
        alert('calcularProcesados()_' + thrownError);
      }
    });
  } catch (e) {
    alert('calcularProcesados()_' + e);
  }
}

/* ================== Filtros del THEAD ==================
   Toma operadores/valores tal y como quedaron en el thead.
   (Coincide con lo que envías en DataTable y otras acciones)
========================================================== */
function getFiltroPayloadRecibidos() {
  const v = (s) => (($(s).val() || '') + '').trim();
  return {
    // texto
    rfcOperator: (v('#rfcOperator') || 'contains').toLowerCase(),
    razonOperator: (v('#razonOperator') || 'contains').toLowerCase(),
    serieOperator: (v('#serieOperator') || 'contains').toLowerCase(),
    tipoOperator: (v('#tipoOperator') || 'equals').toLowerCase(),
    uuidOperator: (v('#uuidOperator') || 'contains').toLowerCase(),
    // fecha
    dateOperator: v('#dateOperator') || 'eq',
    dateV1: v('#dateFilter1'),
    dateV2: v('#dateFilter2'),
    // numéricos
    folioOperator: v('#folioOperator') || 'eq',
    folioV1: v('#folioFilter1'),
    folioV2: v('#folioFilter2'),

    totalOperator: v('#totalOperator') || 'eq',
    totalV1: v('#totalFilter1'),
    totalV2: v('#totalFilter2'),

    subOperator: v('#subOperator') || 'eq',
    subV1: v('#subFilter1'),
    subV2: v('#subFilter2'),

    ivaOperator: v('#ivaOperator') || 'eq',
    ivaV1: v('#ivaFilter1'),
    ivaV2: v('#ivaFilter2'),

    ivaRetOperator: v('#ivaRetOperator') || 'eq',
    ivaRetV1: v('#ivaRetFilter1'),
    ivaRetV2: v('#ivaRetFilter2'),

    isrOperator: v('#isrOperator') || 'eq',
    isrV1: v('#isrFilter1'),
    isrV2: v('#isrFilter2'),

    impLocOperator: v('#impLocOperator') || 'eq',
    impLocV1: v('#impLocFilter1'),
    impLocV2: v('#impLocFilter2')
  };
}

/* ================== Acción principal ================== */
function procesaVincular() {
  try {
    // (opcional) sincroniza THEAD -> inputs globales si existe tu helper
    if (typeof __rb_syncTheadToGlobal === 'function') {
      __rb_syncTheadToGlobal();
    }

    // lee valores base
    const fechaInicial = obtenerFechaIni_Recibidos();
    const fechaFinal = obtenerFechaFin_Recibidos();
    const rfc = obtenerRFC_Recibidos();
    const razonSocial = obtenerRazon_Recibidos();
    const uuid = obtenerUUID_Recibidos();
    // const tipoComprobante = obtenerComprobante_Recibidos(); // no se usa aquí
    const serie = obtenerSerie_Recibidos();
    const folio = obtenerFolio_Recibidos();

    $('#btnVincularComplemento').hide();

    $.ajax({
      url: '/siarex247/cumplimientoFiscal/boveda/recibidos/vicularComplementos.action',
      type: 'POST',
      data: $.extend(
        {
          rfc: rfc,
          razonSocial: razonSocial,
          folio: folio,
          serie: serie,
          fechaInicial: fechaInicial,
          fechaFinal: fechaFinal,
          uuid: uuid
        },
        getFiltroPayloadRecibidos() // 👈 añade operadores/rangos
      ),
      dataType: 'json',
      success: function (data) {
        if (data.ESTATUS == 'OK') {
          Swal.fire({
            title: '¡Operación Exitosa!',
            text:
              'Proceso de Vincular complementos fue iniciado satisfactoriamente, por favor revise sus estatus de los totales.',
            showCancelButton: false,
            confirmButtonText: 'Aceptar',
            icon: 'success'
          }).then(function () {
            $('#IDEN').val(data.IDEN);
            monitorConsola();
          });
        } else {
          Swal.fire({
            title: '¡Operación No Existosa!',
            html: '<p>' + data.mensajeError + '</p>',
            icon: 'error'
          });
        }
      },
      error: function (xhr, ajaxOptions, thrownError) {
        alert('procesaVincular()_' + thrownError);
        // re-mostrar el botón por si algo falló
        $('#btnVincularComplemento').show();
      }
    });
  } catch (e) {
    alert('procesaVincular()_' + e);
    $('#btnVincularComplemento').show();
  }
}
