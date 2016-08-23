package externalSysCon;

import jason.asSyntax.*;
import jason.asSemantics.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import access_utilities.ConfigurationPropertyManager;

public class JdbcConnector extends DefaultInternalAction{

	private Connection connect = null;
    private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public JdbcConnector() throws Exception {
		ConfigurationPropertyManager configManager = ConfigurationPropertyManager.getConfigurationPropertyManager();	    		
		//final String password = configManager.getPropValues("jdbc_pw");
		//Class.forName("com.mysql.jdbc.Driver"); // This is an expression, not a statement!
		//String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String dbName = "basic_enterprise_integration";
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
		// String connectionURL = "jdbc:mysql://localhost/test?"+"user=root&password="+password;
		connect = DriverManager.getConnection(connectionURL);
	}
	
	public Object execute(TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		
		if(args[0].toString().equals("get_users"))
		{
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from USERS"); // NOTE: was userinfo.users for H2 database
			ListTerm lt = new ListTermImpl();
			
			while (resultSet.next()) {
				String name = resultSet.getString("username"); // NOTE: was "user" for H2 database			
				StringTerm st = new StringTermImpl(name);
				lt.add(st);
		    }
			return un.unifies(lt,args[1]);
		}
		if(args[0].toString().equals("get_rules"))
		{
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from RULES"); // Note: was userinfo.rules for H2 database
			ListTerm lt = new ListTermImpl();
			
			while (resultSet.next()) {
				String rule = resultSet.getString("rule");			
				StringTerm st = new StringTermImpl(rule);
				lt.add(st);
		    }
			return un.unifies(lt,args[1]);
		}
			return null;
	}

}