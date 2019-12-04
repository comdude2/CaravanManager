package net.comdude2.apps.caravanmanager.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import net.comdude2.apps.caravanmanager.util.Log;

import com.mysql.jdbc.Connection;

public class DatabaseConnector {
	
	private String URL = null;
	private String username = null;
	private String password = null;
	private Connection connection = null;
	private boolean allow = false;
	private Log log = null;
	
	//jdbc:mysql://localhost:3306/   <-- Url needs to be like that
	public DatabaseConnector(String URL, Log log){
		this.URL = URL;
		allow = true;
		this.log = log;
	}
	
	public void setupConnection(String username, String password){
		if (allow){
			this.username = username;
			this.password = password;
		}else{
			
		}
	}
	
	public void connect() throws ConnectionException, SQLException, IllegalStateException, Exception{
		if (allow){
			if ((this.URL != null) && (this.username != null) && (this.password != null)){
				//Connect
				try {
					connection = (Connection) DriverManager.getConnection(this.URL, username, password);
			    	log.debug("Database connected!");
				} catch (SQLException e) {
					throw new IllegalStateException("Cannot connect the database!", e);
				}
			}else{
				throw new ConnectionException("One of the connection fields were null!");
			}
		}else{
			
		}
	}
	
	public Connection getConnection(){
		if (allow){
			return connection;
		}else{
			
			return null;
		}
	}
	
	public void disconnect(){
		if (allow){
			if (connection != null){
				try {
					if (!connection.isClosed()){
						log.debug("Disconnecting database: " + URL);
						connection.close();
						log.debug("Database Disconnected.");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			
		}
	}
	
	public static void loadJdbcDriver(Log log){
		log.debug("Loading driver...");
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    log.debug("Driver loaded!");
		} catch (ClassNotFoundException e) {
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
	}
	
}
