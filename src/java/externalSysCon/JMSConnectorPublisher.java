package externalSysCon;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class JMSConnectorPublisher  extends DefaultInternalAction{
	private String topicName = "news.sport";

	 private String initialContextFactory = "org.apache.activemq"+".jndi.ActiveMQInitialContextFactory";
	 private String connectionString = "tcp://localhost:61616";
	 
	 public Object execute( TransitionSystem ts, Unifier un,	Term[] args ) throws Exception {
		 publishWithTopicLookup();
		 return null;
	 }
	 public void publishWithTopicLookup() {
		 Properties properties = new Properties();
		 TopicConnection topicConnection = null;
		 properties.put("java.naming.factory.initial", initialContextFactory);
		 properties.put("connectionfactory.QueueConnectionFactory",connectionString);
		 properties.put("topic." + topicName, topicName);

		 try {
			 InitialContext ctx = new InitialContext(properties);
			 TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup("QueueConnectionFactory");
			 topicConnection = topicConnectionFactory.createTopicConnection();

		 try {
			 TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		    // create or use the topic
		    System.out.println("Use the Topic " + topicName);
		    Topic topic = (Topic) ctx.lookup(topicName);
		    javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);

		    String msg = "Hi, I am Test Message";
		    TextMessage textMessage = topicSession.createTextMessage(msg);

		    topicPublisher.publish(textMessage);
		    System.out.println("Publishing message " +textMessage);
		    topicPublisher.close();
		    topicSession.close();

		    Thread.sleep(20);
		   } catch (InterruptedException e) {
		    e.printStackTrace();
		   }

		  } catch (JMSException e) {
		   throw new RuntimeException("Error in JMS operations", e);
		  } catch (NamingException e) {
		   throw new RuntimeException("Error in initial context lookup", e);
		  }
	}
}