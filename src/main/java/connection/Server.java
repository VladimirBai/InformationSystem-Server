package connection;

import controller.Message;
import controller.MessageHandler;
import model.Genre;
import model.Track;
import model.TrackXML;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

public class Server implements Runnable {

    private Socket socket;
    private static final Logger logger = Logger.getLogger(Server.class);
    private static final int TIMEOUT_VALUE = 300000000;

    public Server(Socket socket) {
        this.socket = socket;
        System.out.println("Client connected");
        logger.info("Client connected");
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream;
            MessageHandler messageHandler = new MessageHandler();
            while (!Thread.currentThread().isInterrupted()) {
                socket.setSoTimeout(TIMEOUT_VALUE);
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("message received");
                Message message = (Message) objectInputStream.readObject();
                message = messageHandler.handle(message);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(message);
                System.out.println("message sent");
            }
        } catch (SocketTimeoutException e) {
            logger.error("There is timeout on server");
            e.printStackTrace();
        } catch (SocketException e) {
            logger.error("SocketException in 'run'");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException in 'run'");
            System.out.println("IOException in 'run'");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException in 'run'");
            e.printStackTrace();
        }  finally {
            try {
                socket.close();
                logger.info("Client disconnected");
                System.out.println("Client disconnected");
            } catch (IOException e1) {
                logger.error("IOException while socket closing");
            }
        }
    }
}
