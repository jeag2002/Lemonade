package be.vsko.jss.exception;

import org.apache.log4j.Logger;

public class JSSException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(JSSException.class);
	
	public JSSException() {
		
	}
	
	public JSSException(Throwable e) {
		log.error("There was an error in the application");
		log.error(e.getMessage());
		log.error(e.getCause());
		logException(e);
	}
	
	public JSSException(Throwable e, String message) {
		log.error("There was an error in the application");
		log.error(message);
		log.error(e.getMessage());
		log.error(e.getCause());
		logException(e);
	}
	
	public JSSException(String message) {
		log.error(message);		
	}
	
	private void logException(Throwable e) {
		
		for (StackTraceElement element: e.getStackTrace()) {
			log.error(element.toString());
		}
	}
}
