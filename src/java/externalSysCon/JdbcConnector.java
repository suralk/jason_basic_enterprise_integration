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
		final String password = configManager.getPropValues("jdbc_pw");
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/test?"+"user=root&password="+password);
	}
	
	public Object execute(TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		
		if(args[0].toString().equals("get_users"))
		{
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from userinfo.users");
			ListTerm lt = new ListTermImpl();
			
			while (resultSet.next()) {
				String name = resultSet.getString("user");			
				StringTerm st = new StringTermImpl(name);
				lt.add(st);
		    }
			return un.unifies(lt,args[1]);
		}
		if(args[0].toString().equals("get_rules"))
		{
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from userinfo.rules");
			ListTerm lt = new ListTermImpl();
			
			while (resultSet.next()) {
				String name = resultSet.getString("rule");			
				StringTerm st = new StringTermImpl(name);
				lt.add(st);
		    }
			return un.unifies(lt,args[1]);
		}
			return null;
	}

}
