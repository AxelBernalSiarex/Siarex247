package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.procesos.validacionFisicos.ValidacionFisicosXML;


public class TaskManagerValidarFisicaXML {
	
	private static TaskManagerValidarFisicaXML  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 2;
	
	
	public static synchronized TaskManagerValidarFisicaXML instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerValidarFisicaXML();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerProceso = null;
	
	
    
	class TaskTimerProceso extends TimerTask {
		public void run() {
			ProcesoMonitorBean procBean = new ProcesoMonitorBean();
			ValidacionFisicosXML valFisicos = new ValidacionFisicosXML();
			valFisicos.monitoreaCorreo();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de Validacion XML Fisicos......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("\n\n Iniciando proceso de Validacion XML Fisicos  ......\n");
		//(timerProceso,  5000);
		timerList.scheduleAtFixedRate(timerProceso, 0, 86400 * 1000);
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
    	logger.info("Terminando proceso de Validacion XML Fisicos................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerValidarFisicaXML t = new TaskManagerValidarFisicaXML();
    	t.iniciarProceso();
    }
 
}