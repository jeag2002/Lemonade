package be.vsko.jss.common;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import be.vsko.jss.exception.ConnectionPoolException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class ConnectionManager {
	private static ComboPooledDataSource cpds = null;
	
	private static Logger log = Logger.getLogger(ConnectionManager.class);
	
	private ConnectionManager() {		
	}
	
	private static void createDataSource() {
		try {
			log.info("Creating the connection pool");
			
			//configure the pool
			cpds = new ComboPooledDataSource(); 
			cpds.setDriverClass(JSSConfigurationProperties.getInstance().getJdbcDriver()); 
			cpds.setJdbcUrl(JSSConfigurationProperties.getInstance().getJdbcConnection()); 
			cpds.setUser(JSSConfigurationProperties.getInstance().getJdbcUser()); 
			cpds.setPassword(JSSConfigurationProperties.getInstance().getJdbcPassword());
			
			cpds.setMinPoolSize(JSSConfigurationProperties.getInstance().getJdbcMinPoolSize()); 
			cpds.setAcquireIncrement(JSSConfigurationProperties.getInstance().getJdbcAcquireIncrement()); 
			cpds.setMaxPoolSize(JSSConfigurationProperties.getInstance().getJdbcMaxPoolSize());
			cpds.setMaxStatements(JSSConfigurationProperties.getInstance().getJdbcMaxStatement());
			
		}
		catch(Exception e) {
			throw new ConnectionPoolException(e);
		}

	}
	
	/**
	 * Closes the connection pool. 
	 */
	public static void close() {
		if(cpds != null) {
			log.warn("Closing the connection pool, this should be done only when shuting down the application");
			cpds.close();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		if(cpds == null) {
			//this will happen only once
			createDataSource();
		}
		//System.out.println("begin " + Thread.currentThread().getId());
		return cpds.getConnection();
	}
}
