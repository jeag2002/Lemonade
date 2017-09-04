package be.vsko.jss.common;

import java.io.IOException;
import java.util.Properties;

import be.vsko.jss.exception.ResourceException;

public class JSSConfigurationProperties {	
		
	private Properties properties;
	private static JSSConfigurationProperties instance = null;
	private static Boolean useJunitProperties = false;
	
	private JSSConfigurationProperties() {
		try {
			properties = new Properties();			
			
			if(useJunitProperties)
				properties.load(getClass().getClassLoader().getResourceAsStream("configuration-junit.properties"));
			else
				properties.load(getClass().getClassLoader().getResourceAsStream("configuration.properties"));
		}
		catch(IOException e) {
			throw new ResourceException(e);
		}
	}
	
	public static JSSConfigurationProperties getInstance() {
		if(instance == null)
			instance = new JSSConfigurationProperties();
		
		return instance;
	}
	
	public String getJdbcDriver() {
		return properties.getProperty("jdbc.driver");
	}
	
	public String getJdbcConnection() {
		return properties.getProperty("jdbc.connection");				
	}
	
	public String getJdbcUser() {
		return properties.getProperty("jdbc.user");
	}
	
	public String getJdbcPassword() {
		return properties.getProperty("jdbc.password");
	}
	
	public int getJdbcMinPoolSize() {
		return Integer.parseInt(properties.getProperty("jdbc.minPoolSize"));
	}
	
	public int getJdbcMaxPoolSize() {
		return Integer.parseInt(properties.getProperty("jdbc.maxPoolSize"));
	}
	
	public int getJdbcAcquireIncrement() {
		return Integer.parseInt(properties.getProperty("jdbc.acquireIncrement"));
	}
	
	public int getJdbcMaxStatement() {
		return Integer.parseInt(properties.getProperty("jdbc.maxStatement"));
	}
	
	public String getVersion() {
		return properties.getProperty("version");
	}
	
	public String getModelPackage() {
		return properties.getProperty("modelPackage");
	}

	public static Boolean getUseJunitProperties() {
		return useJunitProperties;
	}

	public static void useJunitProperties() {
		useJunitProperties = true;
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

}
