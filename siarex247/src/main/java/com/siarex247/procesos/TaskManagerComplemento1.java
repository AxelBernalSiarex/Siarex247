package com.siarex247.procesos;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.validaciones.ComplementoProcesosBean;

public class TaskManagerComplemento1 {
	
	private static TaskManagerComplemento1  _instance = null;
	private static Timer timerListComplemento = null;
	private final int NUMERO_PROCESO = 9;

	
	public static synchronized TaskManagerComplemento1 instance() {
		if (_instance == null) {
			timerListComplemento = new Timer();
			_instance = new TaskManagerComplemento1();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerComplemento = null;
	
	

	class TaskTimerComplemento1 extends TimerTask {
		public void run() {
			Date fechaActual = new Date();
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			String horaActual = formatTime.format(fechaActual);
			logger.info("Ejecutando proceso de Envio de Complementos del dia 1 a las : "+horaActual);
			ComplementoProcesosBean compleProceso = new ComplementoProcesosBean();
			ProcesoMonitorBean procMon = new ProcesoMonitorBean();
			compleProceso.monitoreaComplemento(1); // dia 1
        	String bandMonitoreo = procMon.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de notificacion de complemento ordenes......");
        		timerListComplemento.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerComplemento == null) {
			timerComplemento = new TaskTimerComplemento1();
		}
		//(timerProceso,  5000);
		//timerListComplemento.scheduleAtFixedRate(timerComplemento, 0, 3600 * 1000);
		timerListComplemento.scheduleAtFixedRate(timerComplemento, 0, 10800 * 1000);
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
    	TaskManagerComplemento1 t = new TaskManagerComplemento1();
    	t.iniciarProceso();
    }
 
}