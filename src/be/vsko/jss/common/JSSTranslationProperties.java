package be.vsko.jss.common;

import java.io.IOException;
import java.util.Properties;

import be.vsko.jss.exception.ResourceException;

public class JSSTranslationProperties {	
		
	private Properties properties;
	private static JSSTranslationProperties instance = null;
	
	private JSSTranslationProperties() {
		try {
			properties = new Properties();			
			
			properties.load(getClass().getClassLoader().getResourceAsStream("translation.properties"));
		}
		catch(IOException e) {
			throw new ResourceException(e);
		}
	}
	
	public static JSSTranslationProperties getInstance() {
		if(instance == null)
			instance = new JSSTranslationProperties();
		
		return instance;
	}
	
	public String getMessage(String key) {
		
		String value = properties.getProperty(key); 
		return value!=null ? value : ("-"+key+"- key not found");
	}

}
