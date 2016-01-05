package externalSysCon;

import java.util.Properties;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import access_utilities.ConfigurationPropertyManager;


public class IMAPConnector extends DefaultInternalAction{

	public IMAPConnector() {
		// TODO Auto-generated constructor stub
	}
	public Object execute(TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		Folder folder = null;
	    Store store = null;
	    ConfigurationPropertyManager configManager = ConfigurationPropertyManager.getConfigurationPropertyManager();
	    final String email_address = configManager.getPropValues("gmail_address");		
		final String password = configManager.getPropValues("gmail_pw");

	    
	    try {	    	
	      Properties props = System.getProperties();
	      props.setProperty("mail.store.protocol", "imaps");

	      //Session session = Session.getDefaultInstance(props, null);
	      Session session = Session.getInstance(props, new GMailAuthenticator(email_address, password));
	      store = session.getStore("imaps");
	      store.connect("imap.gmail.com",email_address,password);
	      folder = store.getFolder("Inbox");
	      folder.open(Folder.READ_WRITE);
	      Message messages[] = folder.getMessages();
	      System.out.println("No of Messages : " + folder.getMessageCount());
	    }
	    catch(Exception e){
	    	System.out.println(e.toString());
	    }
	    finally {
	        if (folder != null) { folder.close(true); }
	    }
		return null;
	}

}