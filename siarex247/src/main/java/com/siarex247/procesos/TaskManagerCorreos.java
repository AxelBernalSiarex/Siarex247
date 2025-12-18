package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class TaskManagerCorreos {
	
	private static TaskManagerCorreos  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 1;
	public static synchronized TaskManagerCorreos instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerCorreos();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerProceso = null;
	
	
/*    @Override

    public void run() {
        System.out.println("Timer task started at:"+new Date());
        completeTask(logger);
        System.out.println("Timer task finished at:"+new Date());
    }
*/ 
    
	class TaskTimerProceso extends TimerTask {
		public void run() {
			// logger.info("\n Sensando Proceso de Verificacion de Correos............... \n");
			ProcesoMonitorBean procBean = new ProcesoMonitorBean();
			LeeCorreo leeCorreo = new LeeCorreo();
			leeCorreo.monitoreaCorreo();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de Verificacion de Correos......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("\n\n Iniciando proceso de ejecucion de Verificacion de Correos.......\n");
		//(timerProceso,  5000);
		timerList.scheduleAtFixedRate(timerProceso, 0, 60 * 1000);
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
    	logger.info("Terminando proceso de Verificacion de Correos................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerCorreos t = new TaskManagerCorreos();
    	t.iniciarProceso();
    }
 
}