function mostrarGraficaEgresos(mes = null) {
  const annio = $('#cmbAnnio').val();
  const tipoMoneda = $('#cmbTipoMoneda').val();
  const contribuyente = $('#cmbContribuyentes').val() || '';

  const params = { annio, tipoMoneda, contribuyente };
  if (mes !== null) params.mes = mes;

  $.getJSON('/siarex247/visor/monitor360/calcularGraficaPorTipoComprobante.action', params, function (data) {
    const chartDom = document.querySelector('.echart-grafica-egresos');
    if (!chartDom) return console.warn("⚠️ Contenedor no encontrado");

    const myChart = echarts.init(chartDom);
    myChart.clear();

    const meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

    const leyendas = {
      I: 'Factura (Ingreso)',
      E: 'Nota de crédito',
      N: 'Recibo de nómina',
      T: 'Traslado / Carta porte',
      P: 'Pago',
      X: 'Recibo Honorarios',
      Y: 'Recibo Arrendamientos'
    };

    const colores = ['#2c7be5', '#00d27a', '#f5803e', '#e63757', '#39afd1', '#6f42c1', '#ffc107'];
    const series = [];
    const legendNames = [];

    let idx = 0;
    for (const tipo in leyendas) {
      const nombreVisible = leyendas[tipo];
      legendNames.push(nombreVisible);
	  
      const valores = data.data[tipo] || new Array(12).fill(0); // Si no hay datos, llena con 0s

	  //alert('nombreVisible===>'+nombreVisible);
	  
      series.push({
        name: nombreVisible,
        type: 'bar',
        data: valores,
        barGap: 0,
        itemStyle: {
          color: colores[idx % colores.length],
          barBorderRadius: [3, 3, 0, 0]
        }
      });

      idx++;
    }

    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'cross', crossStyle: { color: '#aaa' } },
        backgroundColor: '#fff',
        borderColor: '#ccc',
        borderWidth: 1,
        textStyle: { color: '#000' }
      },
      toolbox: {
        top: 0,
        feature: {
          magicType: { show: true, type: ['bar', 'line'] },
          restore: { show: true },
          saveAsImage: { show: true, name: "egresos" }
        },
        iconStyle: { borderColor: '#666' }
      },
      legend: {
        top: 10,
        data: legendNames,
        textStyle: { color: '#666' }
      },
      xAxis: {
        type: 'category',
        data: meses,
        axisLabel: { color: '#666' },
        axisLine: { show: true, lineStyle: { color: '#ccc' } }
      },
      yAxis: {
        type: 'value',
        min: 0,
        max: data.numeroMayor * 1.1,
        interval: data.intervalo,
        axisLabel: { color: '#666', formatter: '{value}' },
        splitLine: { lineStyle: { color: '#eee' } }
      },
      grid: {
        left: 5,
        right: 5,
        top: '23%',
        bottom: 5,
        containLabel: true
      },
      series: series
    };

    myChart.setOption(option);
  });
}
