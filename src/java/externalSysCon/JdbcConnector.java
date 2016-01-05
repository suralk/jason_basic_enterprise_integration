package externalSysCon;

import jason.asSyntax.*;
import jason.asSemantics.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

<<<<<<< HEAD
import utilities.ConfigurationPropertyManager;
=======
import access_utilities.ConfigurationPropertyManager;
>>>>>>> d8d32d27e7a36b4fdbcfcb9604c5d1b783988a48

public class JdbcConnector extends DefaultInternalAction{

	private Connection connect = null;
    private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
<<<<<<< HEAD
	public JdbcConnector(){
		// TODO Auto-generated constructor stub
	}
	
	public Object execute(TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		
=======
	
	public JdbcConnector() throws Exception {
>>>>>>> d8d32d27e7a36b4fdbcfcb9604c5d1b783988a48
		ConfigurationPropertyManager configManager = ConfigurationPropertyManager.getConfigurationPropertyManager();	    		
		final String password = configManager.getPropValues("jdbc_pw");
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/test?"+"user=root&password="+password);
<<<<<<< HEAD
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from test.pet");
		while (resultSet.next()) {
			String name = resultSet.getString("name");			
			System.out.println(name);
	    }
=======
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
>>>>>>> d8d32d27e7a36b4fdbcfcb9604c5d1b783988a48
			return null;
	}

}
