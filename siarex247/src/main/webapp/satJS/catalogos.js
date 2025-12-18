


function cargaMetodoPago(claveMetodo) {
	try {
		$('#metodoPago').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboMetodoPago.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#metodoPago').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveMetodo == text.clave){
			    		$('#metodoPago').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#metodoPago').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaMetodoPago()_"+e);
	} 
}

function cargaFormaPago(claveFormaPago) {
	try {
		$('#formaPago').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboFormaPago.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#formaPago').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveFormaPago == text.clave){
			    		$('#formaPago').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#formaPago').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaFormaPago()_"+e);
	} 
}



function cargaRegimenFiscal(claveRegimenFiscal) {
	try {
		$('#regimenFiscal').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboRegimenFiscal.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#regimenFiscal').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRegimenFiscal == text.clave){
			    		$('#regimenFiscal').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#regimenFiscal').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRegimenFiscal()_"+e);
	} 
}



function cargaRegimenFiscalPatronal(claveRegimenFiscalPatronal) {
	try {
		$('#regimenFiscalPatronal').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboRegimenFiscal.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#regimenFiscalPatronal').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRegimenFiscalPatronal == text.clave){
			    		$('#regimenFiscalPatronal').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#regimenFiscalPatronal').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRegimenFiscalPatronal()_"+e);
	} 
}



function cargaUsoCFDI(claveUsoCFDI) {
	try {
		$('#usoCFDI').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboUsoCFDI.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#usoCFDI').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveUsoCFDI == text.clave){
			    		$('#usoCFDI').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#usoCFDI').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaUsoCFDI()_"+e);
	} 
}



function cargaTipoMoneda(claveTipoMoneda) {
	try {
		$('#tipoMoneda').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTipoMoneda.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#tipoMoneda').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveTipoMoneda == text.clave){
			    		$('#tipoMoneda').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#tipoMoneda').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaTipoMoneda()_"+e);
	} 
}



function cargaExportacion(claveExportacion) {
	try {
		$('#exportacion').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboExportacion.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#exportacion').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveExportacion == text.clave){
			    		$('#exportacion').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#exportacion').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaExportacion()_"+e);
	} 
}


function cargaClaveProducto(claveProducto) {
	try {
		$('#claveProducto').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboClaveProducto.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#claveProducto').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveProducto == text.clave){
			    		$('#claveProducto').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#claveProducto').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaClaveProducto()_"+e);
	} 
}



function cargaClaveUnidad(claveUnidad) {
	try {
		$('#claveUnidad').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboClaveUnidad.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#claveUnidad').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveUnidad == text.clave){
			    		$('#claveUnidad').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#claveUnidad').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaClaveUnidad()_"+e);
	} 
}



function cargaObjetoImpuesto(claveObjectoImpuesto) {
	try {
		$('#objectoImpuesto').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboObjetoImpuesto.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#objectoImpuesto').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveObjectoImpuesto == text.clave){
			    		$('#objectoImpuesto').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#objectoImpuesto').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaObjetoImpuesto()_"+e);
	} 
}



function cargaTrasladoImpuesto(claveTransladoImpuesto) {
	try {
		$('#transladoImpuesto').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboImpuestos.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#transladoImpuesto').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveTransladoImpuesto == text.clave){
			    		$('#transladoImpuesto').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#transladoImpuesto').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaTrasladoImpuesto()_"+e);
	} 
}



function cargaTrasladoFactor(claveTransladoFactor) {
	try {
		$('#transladoFactor').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTipoFactor.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#transladoFactor').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveTransladoFactor == text.clave){
			    		$('#transladoFactor').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#transladoFactor').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaTrasladoFactor()_"+e);
	} 
}



function cargaTrasladoTasa(claveImpuesto, claveTransladoTasa) {
	try {
		$('#transladoTasa').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTasaCuota.action',
           data : {
        	   claveImpuesto : claveImpuesto
           },
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#transladoTasa').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveTransladoTasa == text.clave){
			    		$('#transladoTasa').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#transladoTasa').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaTrasladoTasa()_"+e);
	} 
}


function cargaRetencionImpuesto1(claveRetencionImpuesto1) {
	try {
		$('#retencionImpuesto1').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboImpuestos.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#retencionImpuesto1').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRetencionImpuesto1 == text.clave){
			    		$('#retencionImpuesto1').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#retencionImpuesto1').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRetencionImpuesto1()_"+e);
	} 
}

function cargaRetencionFactor1(claveRetencionFactor1) {
	try {
		$('#retencionFactor1').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTipoFactor.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#retencionFactor1').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRetencionFactor1 == text.clave){
			    		$('#retencionFactor1').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#retencionFactor1').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRetencionFactor1()_"+e);
	} 
}


function cargaRetencionTasa1(claveImpuesto, claveRetencionTasa1) {
	try {
		$('#retencionTasa1').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTasaCuota.action',
           data : {
        	   claveImpuesto : claveImpuesto
           },
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#retencionTasa1').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRetencionTasa1 == text.clave){
			    		$('#retencionTasa1').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#retencionTasa1').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRetencionTasa1()_"+e);
	} 
}

function cargaRetencionImpuesto2(claveRetencionImpuesto2) {
	try {
		$('#retencionImpuesto2').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboImpuestos.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#retencionImpuesto2').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRetencionImpuesto2 == text.clave){
			    		$('#retencionImpuesto2').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#retencionImpuesto2').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRetencionImpuesto2()_"+e);
	} 
}

function cargaRetencionFactor2(claveRetencionFactor2) {
	try {
		$('#retencionFactor2').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTipoFactor.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#retencionFactor2').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRetencionFactor2 == text.clave){
			    		$('#retencionFactor2').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#retencionFactor2').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRetencionFactor2()_"+e);
	} 
}


function cargaRetencionTasa2(claveImpuesto, claveRetencionTasa2) {
	try {
		$('#retencionTasa2').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboTasaCuota.action',
           data : {
        	   claveImpuesto : claveImpuesto
           },
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#retencionTasa2').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveRetencionTasa2 == text.clave){
			    		$('#retencionTasa2').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#retencionTasa2').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaRetencionTasa2()_"+e);
	} 
}



function cargaEstados(claveEstado) {
	try {
		$('#estado').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboEstados.action',
           data : null,
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#estado').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveEstado == text.clave){
			    		$('#estado').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#estado').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaEstados()_"+e);
	} 
}


function cargaCiudad(claveEstado, claveCiudad) {
	try {
		$('#ciudad').empty();
		$.ajax({
           url:  '/siarexSAT/catalogos/sat/comboCiudades.action',
           data : {
        	   claveEstado : claveEstado
           },
           type: 'POST',
           dataType : 'json',
		   success: function(data){
		    	$('#ciudad').empty();
		    	$.each(data.data, function(key, text) {
			    	if (claveCiudad == text.clave){
			    		$('#ciudad').append($('<option></option>').attr('selected', 'selected').attr('value', text.clave).text(text.descripcion));
			    	}else{
			    		$('#ciudad').append($('<option></option>').attr('value', text.clave).text(text.descripcion));
			    	}
		    		
		      	});
		    }
		});
	}catch(e) {
		alert("cargaCiudad()_"+e);
	} 
}
