package com.org.multiplepid;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.org.multiplepid.dto.ShutdownCommand;

/**
 * Simplest implementation of a TCP server, to extend and reduce code duplications
 */
public abstract class TcpServer {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    protected final AtomicBoolean shutdownFlag = new AtomicBoolean(false);

    protected TcpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Method to initiate listening on the server socket. Each device listens to the port and handles requests only until the shutDown flag is inactive.
     * When the server socket accepts a connection, the program passes a handling process to executorService and the service processes it asynchronously.
     */
    @SuppressWarnings("java:S112")
    public void launchAndListen() {
        System.out.println("Server '" + getServerPublicName() + "' started on port: " + serverSocket.getLocalPort());

        while (!shutdownFlag.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(() -> {
                    if (shutdownFlag.get()) {
                        return;
                    }
                    try {
                        handleClient(clientSocket);
                    } catch (EOFException | SocketException e) {
                        System.out.println("The server tried to send message to the person but now it is impossible because of the system is shutdown");
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("method: launchAndListen : " +
                                this.getClass().getSimpleName() + "(" + getServerPublicName() + "): Client connection error: " + e.getMessage());
                    }
                });
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    protected abstract String getServerPublicName();

    /**
     *
     * @param clientSocket - TCP connection client socket
     * @throws IOException
     * @throws ClassNotFoundException
     *
     * Method is responsible for creating a TCP connection between current server and client (listening and writing)
     */
    protected void handleClient(Socket clientSocket) throws IOException, ClassNotFoundException {
        try (clientSocket;
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            Object request;
            while ((request = in.readObject()) != null) {


                System.out.println(this.getClass().getSimpleName() + "(" + this.getServerPublicName() + "): Received message: " + request);

                Object acknowledgementResponse = this.receiveEvent(request);
                if (acknowledgementResponse instanceof ShutdownCommand) {
                    break;
                }
                if (!clientSocket.isClosed()) {
                    out.writeObject(acknowledgementResponse);
                    out.flush();
                }

                System.out.println(
                        this.getClass().getSimpleName() + "(" + this.getServerPublicName() + "): Sent message: " + acknowledgementResponse);
            }
        }
    }

    /**
     *
     * @param request - is an object, but actually it could be an implementation of different classes (Message, ShutdownCommand)
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected abstract Object receiveEvent(Object request) throws IOException, ClassNotFoundException;

    /**
     * Method is responsible for changing of shutDown flag state, stopping executor processes and closing the main server socket.
     */
    @SuppressWarnings("java:S112")
    public void shutdown() {
        if (shutdownFlag.get()) {
            return;
        }
        System.out.println("System \"" + this.getServerPublicName() + "\" is shutting down...");
        shutdownFlag.set(true);
        executorService.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
