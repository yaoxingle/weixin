package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

public class JettyStart {
	public static void main(String[] args) {
        final Server jettyServer = new Server(); 
        try {
            SocketConnector conn = new SocketConnector();
            String portVariable = System.getProperty("jetty.port");
            int port = parsePort(portVariable);
            if (port <= 0)
                port = 80;
            conn.setPort(port);
            conn.setAcceptQueueSize(100);
            jettyServer.setConnectors(new Connector[] { conn });
            
            WebAppContext wah = new WebAppContext();
            wah.setContextPath("/app");
            wah.setWar("src/main/webapp");
            wah.setTempDirectory(new File("target/work"));
            
            HandlerCollection hc =new HandlerCollection();
            hc.setHandlers(new org.mortbay.jetty.Handler[]{wah});
            jettyServer.setHandler(hc);
            
            //Use this to set a limit on the number of threads used to respond requests
            QueuedThreadPool tp = new QueuedThreadPool();
            tp.setMinThreads(50);
            tp.setMaxThreads(50);
            conn.setThreadPool(tp);
            jettyServer.start();
            jettyServer.join();  
            /*
             * Reads from System.in looking for the string "stop\n" in order to gracefully terminate
             * the jetty server and shut down the JVM. This way we can invoke the shutdown hooks
             * while debugging in eclipse. Can't catch CTRL-C to emulate SIGINT as the eclipse
             * console is not propagating that event
             */
            Thread stopThread = new Thread() {
                @Override
                public void run() {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String line;
                    try {
                        while (true) {
                            line = reader.readLine();
                            if ("stop".equals(line)) {
                                jettyServer.stop();
                                System.exit(0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            };
            stopThread.setDaemon(true);
            stopThread.run();
        } catch (Exception e) {
        	e.printStackTrace();

            if (jettyServer != null) {
                try {
                    jettyServer.stop();
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        }
    }

    private static int parsePort(String portVariable) {
        if (portVariable == null)
            return -1;
        try {
            return Integer.valueOf(portVariable).intValue();
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
