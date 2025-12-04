package com.siarex247.procesos;
 
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.estadisticas.reporteNomina.ProcesoReporteNomina;

public class TaskManagerReporteNomina {
	
	private static TaskManagerReporteNomina  _instance = null;
	private static Timer timerList = null;
	
	private final int NUMERO_PROCESO = 12;
	
	
	public static synchronized TaskManagerReporteNomina instance() {
		if (_instance == null) {
			timerList = new Timer();
			_instance = new TaskManagerReporteNomina();
		}
		return _instance;
	}
	
	
	private static final Logger logger = Logger.getLogger(String.class);
	private TimerTask timerProceso = null;
	
	
    
	class TaskTimerProceso extends TimerTask {
		public void run() {
			// logger.info("\n Sensando Proceso de Descarga Masiva de CFDI ............... \n");
			ProcesoMonitorBean procBean = new ProcesoMonitorBean();
			ProcesoReporteNomina repoProceso = new ProcesoReporteNomina();
			repoProceso.monitoreaCorreo();
        	String bandMonitoreo = procBean.revisaMonitoreo(NUMERO_PROCESO);
        	if ("S".equalsIgnoreCase(bandMonitoreo) ){
        		logger.info("Finalizando el proceso de Reporte de Nomina......");
        		timerList.cancel();
        	}
		}
	}
    
    
    void agregarTimerProceso() {
		if (timerProceso == null) {
			timerProceso = new TaskTimerProceso();
		}
		logger.info("Iniciando proceso de Reporte de Nomina  ......");
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
    	logger.info("Terminando proceso de Descarga Masiva de CFDI ................");
    }

    
  
    public static void main(String args[]){
    	TaskManagerReporteNomina t = new TaskManagerReporteNomina();
    	t.iniciarProceso();
    }
 
}