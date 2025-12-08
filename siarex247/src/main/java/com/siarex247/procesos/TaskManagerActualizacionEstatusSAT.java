package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.cumplimientoFiscal.DescargaSAT.ActualizarEstatusCancelarSAT;

public class TaskManagerActualizacionEstatusSAT {
	
	private static TaskManagerActualizacionEstatusSAT  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 13;
	public static synchronized TaskManagerActualizacionEstatusSAT instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerActualizacionEstatusSAT();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerProceso = null;
	
	
    
	class TaskTimerProceso extends TimerTask {
		public void run() {
			// logger.info("\n Sensando Proceso de Lista Negra ............... \n");
			ProcesoMonitorBean procBean = new ProcesoMonitorBean();
			ActualizarEstatusCancelarSAT actualizaBean = new ActualizarEstatusCancelarSAT();
			actualizaBean.monitoreaCorreo();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de actualizacion de estatus en el SAT ......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("\n\n Iniciando proceso de actualizacion de estatus en el SAT  ......\n");
		//(timerProceso,  5000);
		timerList.scheduleAtFixedRate(timerProceso, 0, 120 * 1000);
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
    	ProcesoMonitorBean procBean = new ProcesoMonitorBean();
    	procBean.enciendeProceso(NUMERO_PROCESO);
    }
    
    
    public void terminaProceso(String bandera){
    	ProcesoMonitorBean procBean = new ProcesoMonitorBean();
    	procBean.terminaProceso(NUMERO_PROCESO);
    	timerList.cancel();
    	_instance = null;
    	logger.info("Terminando proceso de actualizacion de estatus en el SAT................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerActualizacionEstatusSAT t = new TaskManagerActualizacionEstatusSAT();
    	t.iniciarProceso();
    }
 
}