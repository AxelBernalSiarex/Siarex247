
$(document).ready(function() {
	
	obtenerTotal();
	obtenerTotalXMoneda('USD');
	obtenerTotalXMoneda('MXN');
	obtenerTotalEstatus();
	obtenerTotalClasificacion();
	obtenerTotalFacturas();
	obtenerTotalComplemento();
	obtenerTotalNotaCredito();
	obtenerTotalProveedores();
});


	function obtenerTotal(){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalOrdenes.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("totalOrdenes").innerHTML = data.desTotalOrdenes;
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotal()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotal()_'+e);
		}
		
    }


	
	function obtenerTotalXMoneda(tipoMoneda){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalMoneda.action',
				type : 'POST', 
				data : {
					tipoMoneda : tipoMoneda
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						if (tipoMoneda == 'USD'){
							document.getElementById("totalOrdenesUSD").innerHTML = data.totalXmoneda;	
						}else{
							document.getElementById("totalOrdenesMXN").innerHTML = data.totalXmoneda;
						}
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalXMoneda()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalXMoneda()_'+e);
		}
		
    }
	
	
	
	function obtenerTotalXMoneda(tipoMoneda){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalMoneda.action',
				type : 'POST', 
				data : {
					tipoMoneda : tipoMoneda
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						if (tipoMoneda == 'USD'){
							document.getElementById("totalOrdenesUSD").innerHTML = data.totalXmoneda;	
						}else{
							document.getElementById("totalOrdenesMXN").innerHTML = data.totalXmoneda;
						}
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalXMoneda()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalXMoneda()_'+e);
		}
		
    }
	
	
	
	function obtenerTotalEstatus(tipoMoneda){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalEstatus.action',
				type : 'POST', 
				data : {
					tipoMoneda : tipoMoneda
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						// Valores de A1
						try{
							document.getElementById("porcentajeA1").innerHTML = data.A1.porcentajeEstatus;
							document.getElementById("totalEstatusA1").innerHTML = data.A1.desTotalEstatus;
							var rowElementA1 = '<div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="'+data.A1.desTotalEstatus+'" aria-valuemin="0" aria-valuemax="100">';
								rowElementA1+= '<div class="progress-bar rounded-pill" style="width: '+data.A1.porcentajeEstatus+'"></div>';
								rowElementA1+= '</div>';
							document.getElementById("divProgressBarA1").innerHTML = rowElementA1;	
						}catch(e){
							e = null;
						}
						
						
						// Valores de A2
						try{
							document.getElementById("porcentajeA2").innerHTML = data.A2.porcentajeEstatus;
							document.getElementById("totalEstatusA2").innerHTML = data.A2.desTotalEstatus;
							var rowElementA2 = '<div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="'+data.A2.desTotalEstatus+'" aria-valuemin="0" aria-valuemax="100">';
								rowElementA2+= '<div class="progress-bar rounded-pill" style="width: '+data.A2.porcentajeEstatus+'"></div>';
								rowElementA2+= '</div>';
							document.getElementById("divProgressBarA2").innerHTML = rowElementA2;	
						}catch(e){
							e = null;
						}
						
						
						
						// Valores de A3
						try{
							document.getElementById("porcentajeA3").innerHTML = data.A3.porcentajeEstatus;
							document.getElementById("totalEstatusA3").innerHTML = data.A3.desTotalEstatus;
							var rowElementA3 = '<div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="'+data.A3.desTotalEstatus+'" aria-valuemin="0" aria-valuemax="100">';
								rowElementA3+= '<div class="progress-bar rounded-pill" style="width: '+data.A3.porcentajeEstatus+'"></div>';
								rowElementA3+= '</div>';
							document.getElementById("divProgressBarA3").innerHTML = rowElementA3;	
						}catch(e){
							e = null;
						}
						
						
						
						// Valores de A4
						try{
							document.getElementById("porcentajeA4").innerHTML = data.A4.porcentajeEstatus;
							document.getElementById("totalEstatusA4").innerHTML = data.A4.desTotalEstatus;
							var rowElementA4 = '<div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="'+data.A4.desTotalEstatus+'" aria-valuemin="0" aria-valuemax="100">';
								rowElementA4+= '<div class="progress-bar rounded-pill" style="width: '+data.A4.porcentajeEstatus+'"></div>';
								rowElementA4+= '</div>';
							document.getElementById("divProgressBarA4").innerHTML = rowElementA4;	
						}catch(e){
							e = null;
						}
						
						
						
						// Valores de A5
						try{
							document.getElementById("porcentajeA5").innerHTML = data.A5.porcentajeEstatus;
							document.getElementById("totalEstatusA5").innerHTML = data.A5.desTotalEstatus;
							var rowElementA5 = '<div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="'+data.A5.desTotalEstatus+'" aria-valuemin="0" aria-valuemax="100">';
								rowElementA5+= '<div class="progress-bar rounded-pill" style="width: '+data.A5.porcentajeEstatus+'"></div>';
								rowElementA5+= '</div>';
							document.getElementById("divProgressBarA5").innerHTML = rowElementA5;	
						}catch(e){
							e = null;
						}
						
						// Valores de A6
						try{
							document.getElementById("porcentajeA6").innerHTML = data.A6.porcentajeEstatus;
							document.getElementById("totalEstatusA6").innerHTML = data.A6.desTotalEstatus;
							var rowElementA5 = '<div class="progress bg-200 me-2" style="height: 5px;" role="progressbar" aria-valuenow="'+data.A6.desTotalEstatus+'" aria-valuemin="0" aria-valuemax="100">';
								rowElementA5+= '<div class="progress-bar rounded-pill" style="width: '+data.A6.porcentajeEstatus+'"></div>';
								rowElementA5+= '</div>';
							document.getElementById("divProgressBarA6").innerHTML = rowElementA6;	
						}catch(e){
							e = null;
						}
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalEstatus()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalEstatus()_'+e);
		}
    }
	
	
	
	function obtenerTotalClasificacion(){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalOrdenes.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("totalOrdenesClasificacion").innerHTML = data.desTotalOrdenes;
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalClasificacion()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalClasificacion()_'+e);
		}
		
    }
	
	
	function obtenerTotalFacturas(){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalFacturas.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("totalFacturas").innerHTML = data.desTotalFacturas;
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalFacturas()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalFacturas()_'+e);
		}
		
    }
	
	
	function obtenerTotalComplemento(){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalComplemento.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("totalComplemento").innerHTML = data.desTotalComplementos;
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalComplemento()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalComplemento()_'+e);
		}
		
    }
	
	
	function obtenerTotalNotaCredito(){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalNotaCredito.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("totalNotaCredito").innerHTML = data.desTotalNotaCredito;
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalNotaCredito()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalNotaCredito()_'+e);
		}
		
    }
	
	
	
	function obtenerTotalProveedores(){
		try{
			$.ajax({
				url  : '/siarex247/visor/dashboard/obtenerTotalProveedores.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("totalProveedores").innerHTML = data.desTotalProveedores;
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotalProveedores()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotalProveedores()_'+e);
		}
		
    }
	