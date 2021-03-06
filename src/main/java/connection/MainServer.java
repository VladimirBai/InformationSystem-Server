package connection;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {

    private static final Logger logger = Logger.getLogger(MainServer.class);

    public static void main(String[] args) {
        logger.info("Server started");
        System.out.println("Server started");
        try {
            ServerSocket serverSocket = new ServerSocket(0066);
            ExecutorService executor = Executors.newCachedThreadPool();
            Thread mainThread = Thread.currentThread();
            while (!mainThread.isInterrupted()) {
                executor.submit(new Server(serverSocket.accept()));
            }
            executor.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
