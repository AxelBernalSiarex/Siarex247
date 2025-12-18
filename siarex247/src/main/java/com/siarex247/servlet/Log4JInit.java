package com.siarex247.servlet;
 
import java.io.File;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Log4JInit extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 4958986768344673617L;
	private static final Logger logger = Logger.getLogger(String.class);
    
	
    public static void configuraLog(){
    	//ConsoleAppender appender = new VtaConsoleAppender(new PatternLayout("confCampanas.MS%p_%l_%p : %m%n"), ConsoleAppender.SYSTEM_ERR);
    	ConsoleAppender appender = new ConsoleAppender(new PatternLayout("siarex247.MS%p_%l_%p : %m%n"), ConsoleAppender.SYSTEM_ERR);
    	appender.setName("CONSOLA");
    	BasicConfigurator.configure(appender);
    	logger.setLevel(Level.DEBUG);
    }
    
    public static void printTrace() {
    	new Throwable().printStackTrace();
	}

    
    private void reconfigura(){
    	String configFile = null;
    	try {
            configuraLog();
            
           configFile = getServletContext().getRealPath("/WEB-INF/log4j.xml");
          // configFile = "/home/pandorax/public_html/siarex/WEB-INF/log4j.xml";
            
    		if(new File(configFile).exists()){
    			DOMConfigurator.configure(configFile);
    		}else{
    			logger.error("No se encuentro el archivo de propiedades para log4j en -> "+configFile);
    		}
		} catch (Exception e) {
			logger.error(e);
			logger.error("Archivo de configuraci�n: "+ configFile);
		}
    }

    /* (sin Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	if("SI".equals(request.getParameter("reiniciar"))){
    		reconfigura();
    	}
    }

    /* (sin Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	//sin uso
    }
    
	/* (sin Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		reconfigura();
	}
}

