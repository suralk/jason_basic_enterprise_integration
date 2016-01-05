package externalSysCon;

//import utilities.*;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;  
import java.io.InputStreamReader;  

import javax.naming.*;  

<<<<<<< HEAD
import utilities.ConfigurationPropertyManager;
//import javax.jms.*;
=======
import access_utilities.ConfigurationPropertyManager;
>>>>>>> d8d32d27e7a36b4fdbcfcb9604c5d1b783988a48

public class SMTPConnector extends DefaultInternalAction{

	public SMTPConnector() {
		// TODO Auto-generated constructor stub
	}
	public Object execute( TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		ConfigurationPropertyManager configManager = ConfigurationPropertyManager.getConfigurationPropertyManager();
		
		final String email_address = configManager.getPropValues("gmail_address");		
		final String password = configManager.getPropValues("gmail_pw");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email_address,password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email_address));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(password));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"+ "\n\n No spam to my email, please!");

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}		return null;
	}

}
