package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.procesos.vincularBoveda.VincularBovedaBean;


public class TaskManagerVincularBoveda {
	
	private static TaskManagerVincularBoveda  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 3;
	
	
	public static synchronized TaskManagerVincularBoveda instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerVincularBoveda();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerProceso = null;
	
	
    
	class TaskTimerProceso extends TimerTask {
		public void run() {
			ProcesoMonitorBean procBean = new ProcesoMonitorBean();
			VincularBovedaBean vincularBean = new VincularBovedaBean();
			vincularBean.monitoreaCorreo();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de Vincular Boveda......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("\n\n Iniciando proceso de Vincular Boveda ......\n");
		//(timerProceso,  5000);
		timerList.scheduleAtFixedRate(timerProceso, 0, 300 * 1000);
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
    	logger.info("Terminando proceso de Vincular Boveda................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerVincularBoveda t = new TaskManagerVincularBoveda();
    	t.iniciarProceso();
    }
 
}