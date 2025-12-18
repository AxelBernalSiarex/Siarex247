


 function muestraGrafica(){
	calcularCabecero();
	calcularIngresos();
	$('#overSeccion_Grafica').css({display:'block'});
	docReady(echartsBarLineChartInitMonitor360);
		
 }
 

 
 function activarTabGraficaEgresos(mes = null) {
   const tab = document.querySelector('a[href="#graficaEgresos"]');
   if (tab) {
     new bootstrap.Tab(tab).show();     // Cambia a la pestaña
     mostrarGraficaEgresos(mes);        // Pasa el mes si viene desde clic
   } else {
     console.warn("❌ No se encontró la pestaña de egresos");
   }
 }





 var echartsBarLineChartInitMonitor360 = function echartsBarLineChartInitMonitor360() {
		
		var annio = $('#cmbAnnio').val();
		var mes = $('#cmbMes').val();
		var tipo = $('#cmbTipo').val();
		var tipoMoneda = $('#cmbTipoMoneda').val();
		
		var contribuyente = $('#cmbContribuyentes').val();
		if (contribuyente == null ){
			contribuyente = '';
		}
		
		setTimeout(function() {
			$.getJSON( '/siarex247/visor/monitor360/calcularGrafica.action?annio='+annio+'&mes='+mes+'&tipo='+tipo+'&contribuyente='+contribuyente+'&tipoMoneda='+tipoMoneda,
	            function(data) {
				 var $barLineChartEl = document.querySelector('.echart-bar-line-chart-exampleMonitor360');

				  if ($barLineChartEl) {
					
				    // Get options from data attribute
				    var userOptions = utils.getData($barLineChartEl, 'options');
				    var chart = window.echarts.init($barLineChartEl);
				    var months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

				    var getDefaultOptions = function getDefaultOptions() {
				      return {
				        tooltip: {
				          trigger: 'axis',
				          axisPointer: {
				            type: 'cross',
				            crossStyle: {
				              color: utils.getGrays()['500']
				            },
				            label: {
				              show: true,
				              backgroundColor: utils.getGrays()['600'],
				              color: utils.getGrays()['100']
				            }
				          },
				          padding: [7, 10],
				          backgroundColor: utils.getGrays()['100'],
				          borderColor: utils.getGrays()['300'],
				          textStyle: {
				            color: utils.getColors().dark
				          },
				          borderWidth: 1,
				          transitionDuration: 0,
				          // formatter: tooltipFormatter
				        },
						
						
				        toolbox: {
				          top: 0,
				          feature: {
				            dataView: {
				              show: false
				            },
				            magicType: {
				              show: true,
				              type: ['line', 'bar']
				            },
				            restore: {
				              show: true
				            },
				            saveAsImage: {
				              show: true,
				              name : "monitor360"
				            }
				          },
				          iconStyle: {
				            borderColor: utils.getGrays()['700'],
				            borderWidth: 1
				          },
				          emphasis: {
				            iconStyle: {
				              textFill: utils.getGrays()['600']
				            }
				          }
				        },
				        legend: {
				          top: 10,
				          //data: ['Evaporation', 'Precipitation', 'Average temperature'],
				          data: ['Ingresos', 'Egresos'],
						  /*formatter: function (name) {
						      return 'Series: ' + name;
						  },*/
						  textStyle: {
				            color: utils.getGrays()['600']
				          }
				        },
						
				        xAxis: [{
				          type: 'category',
				          data: months,
				          axisLabel: {
				            color: utils.getGrays()['600'],
				          },
				          axisPointer: {
				            type: 'shadow'
				          },
				          axisLine: {
				            show: true,
				            lineStyle: {
				              color: utils.getGrays()['300']
				            }
				          }
				        }],
				        yAxis: [{
				          type: 'value',
				          min: 0,
				          max: data.numeroMayor, // numeroMayor
				          interval: data.intervalo,
						  axisLabel: {
				            color: utils.getGrays()['600'],
				            formatter: '{value}'
				          },
				          splitLine: {
				            show: true,
				            lineStyle: {
				              color: utils.getGrays()['200']
				            }
				          }
				        }],
				        series: [{
				          name: 'Ingresos',
				          type: 'bar',
				          data: data.arrIngresos,
				          itemStyle: {
				            color: utils.getColor('primary'),
				            barBorderRadius: [3, 3, 0, 0]
				          }
				        }, {
				          name: 'Egresos',
				          type: 'bar',
				          data: data.arrEgresos,
						  
				          itemStyle: {
				            color: utils.getColor('info'),
				            barBorderRadius: [3, 3, 0, 0],
				          }
				        }],
				        grid: {
				          right: 5,
				          left: 5,
				          bottom: 5,
				          top: '23%',
				          containLabel: true
				        }
				      };
				    };
				    echartSetOption(chart, userOptions, getDefaultOptions);
				    $('#overSeccion_Grafica').css({display:'none'});
					
					// ✅ Detectar clic en toda el área de la gráfica
					chart.on('click', function (params) {
					  if (params.seriesName === 'Egresos') {
					    const meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
					    const mesIndex = meses.indexOf(params.name) + 1; // 1 a 12
					    console.log("📅 Clic en mes:", mesIndex);

					    activarTabGraficaEgresos(mesIndex); // 👉 le pasamos el mes clickeado
					  }
					});

				    
				  }
				
				}
			);
			
			
		}, 500);
		
	};
	