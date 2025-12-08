<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>


	 
	      <th></th>
			    <th>
			      <!-- Aquí el filtro de RFC tipo DevExtreme -->
			      <div class="dx-like-filter" style="position:relative;">
				  <span class="op-btn" id="rfcOpBtn" title="Operador">
					  <span class="op-label"><i class="fas fa-search"></i></span>
					</span>
					<input type="hidden" id="rfcGridFilter" value="">
				  <input type="text" id="rfcFilterInput" placeholder="Filtrar RFC..." />
				  <div class="dx-like-menu" id="rfcOpMenu" role="menu">
				    <ul>
				      <li data-op="contains">&#128269; Contiene</li>
				      <li data-op="notContains">&#128683; No contiene</li>
				      <li data-op="startsWith">&#8676; Empieza con</li>
				      <li data-op="endsWith">&#8677; Termina con</li>
				      <li data-op="equals">= Igual</li>
				      <li data-op="notEquals">&ne; Distinto</li>
				      <li data-op="reset">&#128260; Reset</li>
				    </ul>
				  </div>
				</div>
			    </th>
			    <th>
			    <!-- col 2: Razón social -->
			    <div class="dx-like-filter" style="position:relative;">
			      <span class="op-btn" id="razonOpBtn"><span class="op-label"><i class="fas fa-search"></i></span></span>
			      <input type="text" id="razonFilterInput" placeholder="Filtrar razón social..."/>
			      <div class="dx-like-menu" id="razonOpMenu" role="menu">
			        <ul>
			          <li data-op="contains">&#128269; Contiene</li>
			          <li data-op="notContains">&#128683; No contiene</li>
			          <li data-op="startsWith">&#8676; Empieza con</li>
			          <li data-op="endsWith">&#8677; Termina con</li>
			          <li data-op="equals">= Igual</li>
			          <li data-op="notEquals">&ne; Distinto</li>
			          <li data-op="reset">&#128260; Reset</li>
			        </ul>
			      </div>
			    </div>
			  </th>
			    <!-- col 3: Serie -->
				  <th>
				    <div class="dx-like-filter" style="position:relative;">
				      <span class="op-btn" id="serieOpBtn"><span class="op-label"><i class="fas fa-search"></i></span></span>
				      <input type="text" id="serieFilterInput" placeholder="Filtrar serie..."/>
				      <div class="dx-like-menu" id="serieOpMenu" role="menu">
				        <ul>
				          <li data-op="contains">&#128269; Contiene</li>
				          <li data-op="notContains">&#128683; No contiene</li>
				          <li data-op="startsWith">&#8676; Empieza con</li>
				          <li data-op="endsWith">&#8677; Termina con</li>
				          <li data-op="equals">= Igual</li>
				          <li data-op="notEquals">&ne; Distinto</li>
				          <li data-op="reset">&#128260; Reset</li>
				        </ul>
				      </div>
				    </div>
				  </th>
			     <!-- col 4: Tipo de comprobante -->
				  <th>
				    <div class="dx-like-filter" style="position:relative;">
				      <span class="op-btn" id="tipoOpBtn"><span class="op-label">=</span></span>
				      <select id="tipoFilterInput" class="form-select form-select-sm">
				        <option value="">Todos</option>
				        <option value="I">I - Ingreso</option>
				        <option value="P">P - Pago</option>
				        <option value="E">E - Egreso</option>
				        <option value="T">T - Traslado</option>
				      </select>
				      <div class="dx-like-menu" id="tipoOpMenu" role="menu">
				        <ul>
				          <li data-op="equals">= Igual</li>
				          <li data-op="notEquals">&ne; Distinto</li>
				          <!-- Si quieres permitir LIKE en tipo, agrega los otros 4, pero suele ser catálogo -->
				          <li data-op="reset">&#128260; Reset</li>
				        </ul>
				      </div>
				    </div>
				  </th>
			    <!-- FOLIO (numérico) -->
				<th>
				  <div class="dx-like-filter" style="position:relative;">
				    <span class="op-btn" id="folioOpBtn"><span class="op-label">=</span></span>
				    <input type="number" step="any" id="folioFilter1" placeholder="Folio...">
				    <input type="number" step="any" id="folioFilter2" placeholder="y..." class="d-none" />
				    <div class="dx-like-menu" id="folioOpMenu" role="menu">
				      <ul>
				        <li data-op="eq">= Igual</li>
				        <li data-op="ne">≠ No igual</li>
				        <li data-op="lt">&lt; Menor que</li>
				        <li data-op="gt">&gt; Mayor que</li>
				        <li data-op="le">≤ Menor o igual</li>
				        <li data-op="ge">≥ Mayor o igual</li>
				        <li data-op="between">↦ Entre</li>
				        <li data-op="reset">🔄 Reset</li>
				      </ul>
				    </div>
				  </div>
				  <input type="hidden" id="folioOperator" value="eq">
				</th>
			    <!-- TOTAL -->
				<th>
				  <div class="dx-like-filter" style="position:relative;">
				    <span class="op-btn" id="totalOpBtn"><span class="op-label">=</span></span>
				    <input type="number" step="any" id="totalFilter1" placeholder="Total...">
				    <input type="number" step="any" id="totalFilter2" placeholder="y..." class="d-none" />
				    <div class="dx-like-menu" id="totalOpMenu" role="menu">
				      <ul>
				        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
				        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
				        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
				        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
				      </ul>
				    </div>
				  </div>
				  <input type="hidden" id="totalOperator" value="eq">
				</th>
			   <!-- SUBTOTAL -->
				<th>
				  <div class="dx-like-filter" style="position:relative;">
				    <span class="op-btn" id="subOpBtn"><span class="op-label">=</span></span>
				    <input type="number" step="any" id="subFilter1" placeholder="Sub-total...">
				    <input type="number" step="any" id="subFilter2" placeholder="y..." class="d-none" />
				    <div class="dx-like-menu" id="subOpMenu" role="menu">
				      <ul>
				        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
				        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
				        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
				        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
				      </ul>
				    </div>
				  </div>
				  <input type="hidden" id="subOperator" value="eq">
				</th>
												    
			    <!-- IVA -->
				<th>
				  <div class="dx-like-filter" style="position:relative;">
				    <span class="op-btn" id="ivaOpBtn"><span class="op-label">=</span></span>
				    <input type="number" step="any" id="ivaFilter1" placeholder="IVA...">
				    <input type="number" step="any" id="ivaFilter2" placeholder="y..." class="d-none" />
				    <div class="dx-like-menu" id="ivaOpMenu" role="menu">
				      <ul>
				        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
				        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
				        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
				        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
				      </ul>
				    </div>
				  </div>
				  <input type="hidden" id="ivaOperator" value="eq">
				</th>
			    <!-- IVA RET -->
					<th>
					  <div class="dx-like-filter" style="position:relative;">
					    <span class="op-btn" id="ivaRetOpBtn"><span class="op-label">=</span></span>
					    <input type="number" step="any" id="ivaRetFilter1" placeholder="IVA Ret...">
					    <input type="number" step="any" id="ivaRetFilter2" placeholder="y..." class="d-none" />
					    <div class="dx-like-menu" id="ivaRetOpMenu" role="menu">
					      <ul>
					        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
					        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
					        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
					        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
					      </ul>
					    </div>
					  </div>
					  <input type="hidden" id="ivaRetOperator" value="eq">
					</th>
			    <!-- ISR RET -->
				<th>
				  <div class="dx-like-filter" style="position:relative;">
				    <span class="op-btn" id="isrOpBtn"><span class="op-label">=</span></span>
				    <input type="number" step="any" id="isrFilter1" placeholder="ISR Ret...">
				    <input type="number" step="any" id="isrFilter2" placeholder="y..." class="d-none" />
				    <div class="dx-like-menu" id="isrOpMenu" role="menu">
				      <ul>
				        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
				        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
				        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
				        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
				      </ul>
				    </div>
				  </div>
				  <input type="hidden" id="isrOperator" value="eq">
				</th>
			    <!-- IMP. LOCALES -->
				<th>
				  <div class="dx-like-filter" style="position:relative;">
				    <span class="op-btn" id="impLocOpBtn"><span class="op-label">=</span></span>
				    <input type="number" step="any" id="impLocFilter1" placeholder="Imp. locales...">
				    <input type="number" step="any" id="impLocFilter2" placeholder="y..." class="d-none" />
				    <div class="dx-like-menu" id="impLocOpMenu" role="menu">
				      <ul>
				        <li data-op="eq">= Igual</li><li data-op="ne">≠ No igual</li>
				        <li data-op="lt">&lt; Menor que</li><li data-op="gt">&gt; Mayor que</li>
				        <li data-op="le">≤ Menor o igual</li><li data-op="ge">≥ Mayor o igual</li>
				        <li data-op="between">↦ Entre</li><li data-op="reset">🔄 Reset</li>
				      </ul>
				    </div>
				  </div>
				  <input type="hidden" id="impLocOperator" value="eq">
				</th>
				
			    <th></th>
			      <th></th>
			      <!-- col 14: UUID (ajusta índice según tu tabla) -->
					  <th>
					    <div class="dx-like-filter" style="position:relative;">
					      <span class="op-btn" id="uuidOpBtn"><span class="op-label"><i class="fas fa-search"></i></span></span>
					      <input type="text" id="uuidFilterInput" placeholder="Filtrar UUID..."/>
					      <div class="dx-like-menu" id="uuidOpMenu" role="menu">
					        <ul>
					          <li data-op="contains">&#128269; Contiene</li>
					          <li data-op="notContains">&#128683; No contiene</li>
					          <li data-op="startsWith">&#8676; Empieza con</li>
					          <li data-op="endsWith">&#8677; Termina con</li>
					          <li data-op="equals">= Igual</li>
					          <li data-op="notEquals">&ne; Distinto</li>
					          <li data-op="reset">&#128260; Reset</li>
					        </ul>
					      </div>
					    </div>
					  </th>
			<!-- Filtro FECHA FACTURA -->
			<th>
			  <div class="dx-like-filter" style="position:relative; min-width: 260px;">
			    <span class="op-btn" id="dateOpBtn"><span class="op-label">=</span></span>
			
			    <input type="date" id="dateFilter1" />
			    <input type="date" id="dateFilter2" style="display:none; margin-left:6px;" />
			
			    <div class="dx-like-menu" id="dateOpMenu" role="menu">
			      <ul>
			        <li data-op="eq">= Igual</li>
			        <li data-op="ne">≠ No igual</li>
			        <li data-op="lt">&lt; Menor que</li>
			        <li data-op="gt">&gt; Mayor que</li>
			        <li data-op="le">≤ Menor o igual</li>
			        <li data-op="ge">≥ Mayor o igual</li>
			        <li data-op="bt">⟲ Entre</li>
			        <li data-op="reset">🔄 Reset</li>
			      </ul>
			    </div>
			  </div>
			
			  <!-- hidden para enviar al backend -->
			  <input type="hidden" id="dateOperator" value="eq">
			</th>
		 </tr> 

	<script>
			// ===================== TEXTOS: RFC, Razón, Serie, Tipo, UUID =====================
			function initDxLikeFilter({btnId, menuId, inputId, hiddenOpId, targetInput}) {
			  const $btn=$(btnId), $menu=$(menuId), $input=$(inputId), $hidden=$(hiddenOpId), $label=$btn.find('.op-label');
			  if(!$btn.length||!$menu.length||!$input.length||!$hidden.length) return;
			
			  if(!$hidden.val()) $hidden.val('contains');
			
			  // abrir/cerrar menú
			  $btn.on('click',e=>{ e.stopPropagation(); $menu.toggleClass('show'); });
			  $(document).on('click',()=> $menu.removeClass('show'));
			
			  function short(t){ return (t||'').trim().split(/\s+/)[0]; }
			
			  // elegir operador (NO dispara consulta)
			  $menu.on('click','li',function(){
			    const op=String($(this).data('op')||'').toLowerCase();
			    if(op==='reset'){
			      $hidden.val(hiddenOpId.includes('tipo')?'equals':'contains');
			      $label.html('<i class="fas fa-search"></i>');
			      $input.val('');
			    }else{
			      $hidden.val(op);
			      $label.text(short($(this).text()));
			    }
			    $menu.removeClass('show');
			    // 🔕 nada de refrescar aquí
			  });
			
			  // Solo ENTER aplica el filtro
			  function aplicar(){
			    if(targetInput) $(targetInput).val(($input.val()||'').trim());
			    if(typeof refrescarBoveda==='function') refrescarBoveda();
			  }
			  $input.on('keydown',e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicar(); }});
			}
			
			// ===================== NUMÉRICOS: Folio, Total, Sub, IVA, IVA Ret, ISR, ImpLocales =====================
			function initNumericDxFilter({btnId, menuId, v1Id, v2Id, opHiddenId}) {
			  const $btn=$(btnId), $menu=$(menuId), $v1=$(v1Id), $v2=$(v2Id), $op=$(opHiddenId), $label=$btn.find('.op-label');
			  if(!$btn.length||!$menu.length||!$v1.length||!$op.length) return;
			
			  if(!$op.val()) $op.val('eq'); // default
			  const symbol = {eq:'=', ne:'≠', lt:'<', gt:'>', le:'≤', ge:'≥', between:'↦'};
			
			  function toggleSecond(){
			    const isBetween = ($op.val()||'').toLowerCase()==='between';
			    if(isBetween){ $v2.removeClass('d-none'); } else { $v2.addClass('d-none'); $v2.val(''); }
			  }
			
			  $btn.on('click',e=>{ e.stopPropagation(); $menu.toggleClass('show'); });
			  $(document).on('click',()=> $menu.removeClass('show'));
			
			  // elegir operador (NO refresca)
			  $menu.on('click','li',function(){
			    const op = String($(this).data('op')||'eq').toLowerCase();
			    if(op==='reset'){
			      $op.val('eq'); $label.text('='); $v1.val(''); $v2.val('');
			    }else{
			      $op.val(op); $label.text(symbol[op]||'=');
			    }
			    $menu.removeClass('show');
			    toggleSecond();
			    // 🔕 nada de refrescar aquí
			  });
			
			  // Solo ENTER aplica
			  const aplicar = ()=>{ if(typeof refrescarBoveda==='function') refrescarBoveda(); };
			  $v1.on('keydown',e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicar(); }});
			  $v2.on('keydown',e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicar(); }});
			
			  toggleSecond();
			}
			
			// ===================== FECHA con operador =====================
			function initDxLikeDateFilter({btnId, menuId, input1Id, input2Id, hiddenOpId}) {
			  const $btn=$(btnId), $menu=$(menuId), $d1=$(input1Id), $d2=$(input2Id), $hidden=$(hiddenOpId), $label=$btn.find('.op-label');
			  if(!$btn.length||!$menu.length||!$d1.length||!$hidden.length) return;
			
			  if(!$hidden.val()) $hidden.val('eq');
			  const sym = {eq:'=', ne:'≠', lt:'<', gt:'>', le:'≤', ge:'≥', bt:'↔'};
			
			  function showSecond(show){ if(show){ $d2.show(); } else { $d2.hide().val(''); } }
			
			  $btn.on('click', e=>{ e.stopPropagation(); $menu.toggleClass('show'); });
			  $(document).on('click', ()=> $menu.removeClass('show'));
			
			  // elegir operador (NO refresca)
			  $menu.on('click','li',function(){
			    const op = String($(this).data('op')||'').toLowerCase();
			    if(op==='reset'){
			      $hidden.val('eq'); $label.text('=');
			      $d1.val(''); showSecond(false);
			    }else{
			      $hidden.val(op);
			      $label.text(sym[op]||op);
			      showSecond(op==='bt');
			    }
			    $menu.removeClass('show');
			    // 🔕 nada de refrescar aquí
			  });
			
			  // Solo ENTER aplica
			  const aplicarFecha = ()=>{ if(typeof refrescarBoveda==='function') refrescarBoveda(); };
			  $d1.on('keydown', e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicarFecha(); }});
			  $d2.on('keydown', e=>{ if(e.key==='Enter'){ e.preventDefault(); aplicarFecha(); }});
			}
			
			// ===================== INIT de todos los filtros =====================
			$(function(){
			  // Textos
			  initDxLikeFilter({ btnId:'#rfcOpBtn',   menuId:'#rfcOpMenu',   inputId:'#rfcFilterInput',   hiddenOpId:'#rfcOperator',   targetInput:'#rfc_Recibidos' });
			  initDxLikeFilter({ btnId:'#razonOpBtn', menuId:'#razonOpMenu', inputId:'#razonFilterInput', hiddenOpId:'#razonOperator', targetInput:'#razonSocial_Recibidos' });
			  initDxLikeFilter({ btnId:'#serieOpBtn', menuId:'#serieOpMenu', inputId:'#serieFilterInput', hiddenOpId:'#serieOperator', targetInput:'#serie_Recibidos' });
			  initDxLikeFilter({ btnId:'#tipoOpBtn',  menuId:'#tipoOpMenu',  inputId:'#tipoFilterInput',  hiddenOpId:'#tipoOperator',  targetInput:'#tipoComprobante_Recibidos' });
			  initDxLikeFilter({ btnId:'#uuidOpBtn',  menuId:'#uuidOpMenu',  inputId:'#uuidFilterInput',  hiddenOpId:'#uuidOperator',  targetInput:'#uuid_Recibidos' });
			
			  // Numéricos
			  initNumericDxFilter({ btnId:'#folioOpBtn',   menuId:'#folioOpMenu',   v1Id:'#folioFilter1',   v2Id:'#folioFilter2',   opHiddenId:'#folioOperator' });
			  initNumericDxFilter({ btnId:'#totalOpBtn',   menuId:'#totalOpMenu',   v1Id:'#totalFilter1',   v2Id:'#totalFilter2',   opHiddenId:'#totalOperator' });
			  initNumericDxFilter({ btnId:'#subOpBtn',     menuId:'#subOpMenu',     v1Id:'#subFilter1',     v2Id:'#subFilter2',     opHiddenId:'#subOperator' });
			  initNumericDxFilter({ btnId:'#ivaOpBtn',     menuId:'#ivaOpMenu',     v1Id:'#ivaFilter1',     v2Id:'#ivaFilter2',     opHiddenId:'#ivaOperator' });
			  initNumericDxFilter({ btnId:'#ivaRetOpBtn',  menuId:'#ivaRetOpMenu',  v1Id:'#ivaRetFilter1',  v2Id:'#ivaRetFilter2',  opHiddenId:'#ivaRetOperator' });
			  initNumericDxFilter({ btnId:'#isrOpBtn',     menuId:'#isrOpMenu',     v1Id:'#isrFilter1',     v2Id:'#isrFilter2',     opHiddenId:'#isrOperator' });
			  initNumericDxFilter({ btnId:'#impLocOpBtn',  menuId:'#impLocOpMenu',  v1Id:'#impLocFilter1',  v2Id:'#impLocFilter2',  opHiddenId:'#impLocOperator' });
			
			  // Fecha
			  initDxLikeDateFilter({
			    btnId:'#dateOpBtn', menuId:'#dateOpMenu',
			    input1Id:'#dateFilter1', input2Id:'#dateFilter2',
			    hiddenOpId:'#dateOperator'
			  });
			
			  // ENTER global por si algún control no dispara keydown (selects, etc.)
			  $(document).on('keydown', '#tablaDetalle thead tr.filters :input, #tablaDetalle thead tr.filters select', function(e){
			    if(e.key==='Enter'){
			      e.preventDefault();
			      if(typeof refrescarBoveda==='function') refrescarBoveda();
			    }
			  });
			});
			</script>
			
						<script>
			// ===== Bóveda guard + diagnóstico =====
			(function(){
			  // Evita que este bloque se ejecute más de una vez (si el JS se carga duplicado)
			  if (window.__bovedaBooted) {
			    console.warn('⚠️ boveda bootstrap ya corría, evitando reinit');
			    return;
			  }
			  window.__bovedaBooted = true;
			
			  // ===== Contadores de diagnóstico =====
			  window.__rb_hits = 0;     // veces que alguien invoca refrescarBoveda()
			  window.__dt_req  = 0;     // peticiones reales del DataTable
			
			  function dt(){
			    return $.fn.DataTable.isDataTable('#tablaDetalle') ? $('#tablaDetalle').DataTable() : null;
			  }
			
			  // Cuenta cada request que DataTables hace al servidor
			  $('#tablaDetalle').off('preXhr.rb').on('preXhr.rb', function(){
			    console.log('📡 DT request#', ++window.__dt_req);
			  });
			
			  // ===== ÚNICA versión de refrescarBoveda (con debounce) =====
			  const DEBOUNCE_MS = 150;
			  let __rb_timer = null;
			
			  window.refrescarBoveda = function(reason){
			    console.log('refrescarBoveda hit#', ++window.__rb_hits, reason||'');
			    clearTimeout(__rb_timer);
			    __rb_timer = setTimeout(function(){
			      const t = dt();
			      if (t) t.ajax.reload(null, false); // no cambia de página
			    }, DEBOUNCE_MS);
			  };
			
			  // ===== Bind de ENTER en filtros: una sola vez y con namespace =====
			  const sel = '#tablaDetalle thead tr.filters :input, #tablaDetalle thead tr.filters select';
			  $(document).off('keydown.dxfilters').on('keydown.dxfilters', sel, function(e){
			    if (e.key === 'Enter'){
			      e.preventDefault();
			      window.refrescarBoveda('enter');
			    }
			  });
			
			  // ===== Limpieza de posibles binds globales duplicados =====
			  // (muchas veces se repite este handler de "click fuera" para cerrar menús)
			  $(document).off('click.dxmenu'); // si tus menús usan este namespace, reúsalo al re-bindear
			
			  // ===== TIP: al inicializar cada filtro, usa .off().on() con namespace =====
			  // Ejemplo dentro de initDxLikeFilter:
			  //   const ns = (btnId||'').replace('#','');
			  //   $btn.off('click.'+ns).on('click.'+ns, handler);
			  //   $(document).off('click.'+ns).on('click.'+ns, closeHandler);
			  //
			  // y para inputs:
			  //   $input.off('keydown.apply'+ns).on('keydown.apply'+ns, e=>{ ... });
			
			})();
	</script>
						

		 
</html>