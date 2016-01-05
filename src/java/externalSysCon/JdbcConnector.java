package externalSysCon;

import jason.asSyntax.*;
import jason.asSemantics.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import utilities.ConfigurationPropertyManager;

public class JdbcConnector extends DefaultInternalAction{

	private Connection connect = null;
    private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	public JdbcConnector(){
		// TODO Auto-generated constructor stub
	}
	
	public Object execute(TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		
		ConfigurationPropertyManager configManager = ConfigurationPropertyManager.getConfigurationPropertyManager();	    		
		final String password = configManager.getPropValues("jdbc_pw");
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/test?"+"user=root&password="+password);
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from test.pet");
		while (resultSet.next()) {
			String name = resultSet.getString("name");			
			System.out.println(name);
	    }
			return null;
	}

}
