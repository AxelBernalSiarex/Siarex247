package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaMasivaLocal;


public class TaskManagerDescargaMasivaLocal {
	
	private static TaskManagerDescargaMasivaLocal  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 2;
	
	
	public static synchronized TaskManagerDescargaMasivaLocal instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerDescargaMasivaLocal();
		}
		return _instance;
	}
	
	private static final Logger logger = Logger.getLogger(String.class);
	//private TimerTask taskSiarex = null;
	private TimerTask timerProceso = null;
	
	
    
	class TaskTimerProceso extends TimerTask {
		public void run() {
			ProcesoMonitorBean procBean = new ProcesoMonitorBean();
			DescargaMasivaLocal descarga = new DescargaMasivaLocal();
			descarga.monitoreaCorreo();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de Decarga Masiva Local......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("\n\n Iniciando proceso de Decarga Masiva Local......");
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
    	logger.info("Terminando proceso de Decarga Masiva Local................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerDescargaMasivaLocal t = new TaskManagerDescargaMasivaLocal();
    	t.iniciarProceso();
    }
 
}