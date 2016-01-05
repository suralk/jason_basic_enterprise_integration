package externalSysCon;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

public class ZookeeperConnector extends DefaultInternalAction{

	
	public Object execute( TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {
		if(args[0].isLiteral())		
		{
			System.out.println(args[0].toString());
			ZooKeeperManager zkm = new ZooKeeperManager(args[0].toString());
			zkm.run();
		
		}
		
		return null;
	}	

}
