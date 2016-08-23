package externalSysCon;

import java.util.Properties;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;


public class ZooKeeperManager implements Runnable{

	private Properties prop;
	protected static String HOST;
    protected static ZooKeeper zk;
    protected static final byte[] NO_DATA = new byte[0];
    protected static final Integer INITIAL_VERSION = 0;
    private String containerId;
    
	public ZooKeeperManager(String agent) throws Exception{
		/*HOST = "127.0.0.1:2181";
		zk = new ZooKeeper(HOST, 3000, new ZooKeeperWatcher());
		dm = new DataMonitor(zk, znode, null, this);
		if (zk.exists("/containers/container", false) == null) 
			containerId =  zk.create("/agents/"+agent, NO_DATA, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(containerId);*/
	}
	public void run() {
	/*	try {
            synchronized (this) {
                while (!zk.) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }*/
		
	}
	protected class ZooKeeperWatcher implements Watcher {
	       //@Override
	        public void process(WatchedEvent event) {
	    	   System.out.println(": "+event.getType());
	        }
		 }

}