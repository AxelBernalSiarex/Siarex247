package com.siarex247.procesos;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.cumplimientoFiscal.HistorialPagos.HistoricoPagosProceso;

public class TaskManagerHistoricoPagos {
	
	private static TaskManagerHistoricoPagos  _instance = null;
	private static Timer timerListComplemento = null;
	private final int NUMERO_PROCESO = 14;

	
	public static synchronized TaskManagerHistoricoPagos instance() {
		if (_instance == null) {
			timerListComplemento = new Timer();
			_instance = new TaskManagerHistoricoPagos();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerComplemento = null;
	
	

	class TaskTimerComplementoHP extends TimerTask {
		public void run() {
			Date fechaActual = new Date();
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			String horaActual = formatTime.format(fechaActual);
			logger.info("Ejecutando proceso de Envio de Complementos del dia 1 a las : "+horaActual);
			HistoricoPagosProceso compleProceso = new HistoricoPagosProceso();
			ProcesoMonitorBean procMon = new ProcesoMonitorBean();
			compleProceso.monitoreaComplementoHistoricoP(1); // dia 1
        	String bandMonitoreo = procMon.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de notificacion de complemento ordenes......");
        		timerListComplemento.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerComplemento == null) {
			timerComplemento = new TaskTimerComplementoHP();
		}
		//(timerProceso,  5000);
		//timerListComplemento.scheduleAtFixedRate(timerComplemento, 0, 3600 * 1000);
		timerListComplemento.scheduleAtFixedRate(timerComplemento, 0, 120 * 1000);
		logger.info("Fin");
	}
    

    public void iniciarProceso(){
    	agregarTimerProceso();
    }
    
    public void iniciarProcesoJSP(){
    	enciendeBandera();
    	agregarTimerProceso();
    }
    
    
    public void enciendeBandera(){
    	ProcesoMonitorBean procMon = new ProcesoMonitorBean();
    	procMon.enciendeProceso(NUMERO_PROCESO);
    }
    
    
    
    public void terminaProceso(String bandera){
    	ProcesoMonitorBean procMon = new ProcesoMonitorBean();
    	procMon.terminaProceso(NUMERO_PROCESO);
    	timerListComplemento.cancel();
    	_instance = null;
    	timerListComplemento = null;
    	logger.info("Terminando proceso de complemento 01.....");
    }

    
  
    public static void main(String args[]){
    	TaskManagerHistoricoPagos t = new TaskManagerHistoricoPagos();
    	t.iniciarProceso();
    }
 
}