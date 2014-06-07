
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JWindow;

public class Main{

	private static final ScheduledExecutorService worker = 
			  Executors.newSingleThreadScheduledExecutor();
	private static JWindow splash;
	
	public static void main(String[] args) throws Exception {
		
		worker.schedule(new Runnable(){
			@Override
			public void run() {
				try {
					loadingEnded();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}, 5, TimeUnit.SECONDS);
		
	}

	private static void loadingEnded() throws Exception{
		String externalIP=getExternalIp();
		String internalIP=getInternalIp();
		splash.dispose();
		
		ProcessScreen ps=new ProcessScreen();
	}
	
	public static String getInternalIp() throws Exception {
		InetAddress thisIp = InetAddress.getLocalHost();
		return thisIp.getHostAddress().toString();
	}
	
	

	public static String getExternalIp() throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		InputStream is = null;
		try {
			SocketAddress addr = new InetSocketAddress("172.21.241.100",
					Integer.valueOf("8080"));
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

			is = whatismyip.openConnection(proxy).getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
