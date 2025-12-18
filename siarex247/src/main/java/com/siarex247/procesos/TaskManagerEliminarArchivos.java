package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.utils.DepuradorArchivos;

public class TaskManagerEliminarArchivos {
	
	private static TaskManagerEliminarArchivos  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 4;
	public static synchronized TaskManagerEliminarArchivos instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerEliminarArchivos();
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
			DepuradorArchivos depuradorBean = new DepuradorArchivos();
			depuradorBean.monitorDepurador();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso eliminar archivos......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("\n\n Iniciando proceso eliminar archivos  ......\n");
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
    	logger.info("Terminando proceso eliminar archivos................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerEliminarArchivos t = new TaskManagerEliminarArchivos();
    	t.iniciarProceso();
    }
 
}