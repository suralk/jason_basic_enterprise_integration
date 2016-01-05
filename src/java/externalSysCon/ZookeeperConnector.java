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
import jason.asSyntax.Term;

public class ZookeeperConnector extends DefaultInternalAction{

	private Properties prop;
	protected static String HOST;
    protected static ZooKeeper zk;
    protected static final byte[] NO_DATA = new byte[0];
    protected static final Integer INITIAL_VERSION = 0;
    private String containerId;
    
	public ZookeeperConnector() {
		// TODO Auto-generated constructor stub
	}
	public Object execute( TransitionSystem ts,	Unifier un,	Term[] args ) throws Exception {		
		HOST = "127.0.0.1:2181";
		zk = new ZooKeeper(HOST, 3000, new ZooKeeperWatcher());
		
		if (zk.exists("/containers/container", false) == null) 
			containerId =  zk.create("/containers/container", NO_DATA, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(containerId);
		return null;
	}
	
	protected class ZooKeeperWatcher implements Watcher {
       @Override
        public void process(WatchedEvent event) {
        }
	 }

}
