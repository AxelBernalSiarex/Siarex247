package com.siarex247.procesos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siarex247.descargaMasivaSat.monitorDescargaMasivaSat;



public class TaskManagerDescargaMetaData {

    private static TaskManagerDescargaMetaData _instance = null;
    private static Timer timerListMetaData = null;

    private final int NUMERO_PROCESO = 16;

    public static synchronized TaskManagerDescargaMetaData instance() {
        if (_instance == null) {
            timerListMetaData = new Timer();
            _instance = new TaskManagerDescargaMetaData();
        }
        return _instance;
    }

    private static final Logger logger = Logger.getLogger(String.class);
    private TimerTask timerMetaData = null;

    class TaskTimerMetaData extends TimerTask {
        public void run() {

            Date fechaActual = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            String horaActual = formatTime.format(fechaActual);

            try {
              //  logger.info("Ejecutando Descarga de MetaData a las: " + horaActual);

                
                monitorDescargaMasivaSat monitor = new monitorDescargaMasivaSat();
                monitor.monitorDescargaMasivaSat(1);

                ProcesoMonitorBean procMon = new ProcesoMonitorBean();
                String bandMonitoreo = procMon.revisaMonitoreo(NUMERO_PROCESO);

                if ("S".equalsIgnoreCase(bandMonitoreo)) {
                    logger.info("Finalizando el proceso de Descarga de MetaData......");
                    timerListMetaData.cancel();
                }

            } catch (Exception e) {
                logger.error("Error ejecutando Descarga de MetaData a las " + horaActual, e);
            }
        }
    }

    void agregarTimerProceso() {
        if (timerMetaData == null) {
            timerMetaData = new TaskTimerMetaData();
        }
        timerListMetaData.scheduleAtFixedRate(timerMetaData, 0, 60 * 1000);
        logger.info("Fin");
    }

    public void iniciarProceso() { agregarTimerProceso(); }

    public void iniciarProcesoJSP() {
        enciendeBandera();
        agregarTimerProceso();
    }

    public void enciendeBandera() {
        ProcesoMonitorBean procMon = new ProcesoMonitorBean();
        procMon.enciendeProceso(NUMERO_PROCESO);
    }

    public void terminaProceso(String bandera) {
        ProcesoMonitorBean procMon = new ProcesoMonitorBean();
        procMon.terminaProceso(NUMERO_PROCESO);

        timerListMetaData.cancel();
        _instance = null;
        timerListMetaData = null;

        logger.info("Terminando proceso de Descarga de MetaData.....");
    }
}
