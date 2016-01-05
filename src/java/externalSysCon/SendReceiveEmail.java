package externalSysCon;

import java.util.Properties;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;

import access_utilities.ConfigurationPropertyManager;


public class SendReceiveEmail extends DefaultInternalAction{

	public SendReceiveEmail() {
		// TODO Auto-generated constructor stub
	}
	private static HashMap<Integer, Message> received_emails = new HashMap<Integer, Message>();
	private static int map_index=0;
	public Object execute(TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		if(args[0].isNumeric())
		{
			int i = Integer.parseInt(args[0].toString());
			if(i==1)
				return un.unifies((StringTerm)receiveMail(), args[1]);
		
		}
		return null;
	}
	
	private static String updateEmailrepository(Message[] messages) throws Exception{		
		StringBuffer message_info = new StringBuffer();
		for(int i=0;i<messages.length; i++){
			map_index++;
			received_emails.put(map_index, messages[i]);
			message_info.append("#map_index:");
			message_info.append(map_index);
			message_info.append("#subject:");
			message_info.append(messages[i].getSubject());
			message_info.append("sender:");
			message_info.append(messages[i].getFrom());
			message_info.append("\n");
		}
		return message_info.toString();
	}
	
	private Object receiveMail() throws Exception{
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
	      //Message messages[] = folder.getMessages();
	      // search for all "unseen" messages
	      Flags seen = new Flags(Flags.Flag.SEEN);
	      FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
	      Message messages[] = folder.search(unseenFlagTerm);
	      
	      for(int i=0;i<messages.length; i++){
	    	  folder.getMessage(messages[i].getMessageNumber());//this will mark the retrieved messages as READ	    	  
	      }
	      String s = updateEmailrepository(messages);
	      //System.out.println(s);
	      StringTerm st = new StringTermImpl(s);
	      return st;
	      
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
