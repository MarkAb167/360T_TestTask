package com.org.multiplepid;

import com.org.Message;

import java.io.*;
import java.net.*;

/**
 * Simplest implementation of a TCP client, to extend and reduce code duplications
 */
public abstract class TcpClient {
  private final Socket socket;
  private final ObjectOutputStream out;
  private final ObjectInputStream in;

  /**
   * Constructor just at the start creates a socket connection for identified server port
   * @param host - localhost
   * @param port
   */
  @SuppressWarnings("java:S112")
  protected TcpClient(String host, int port) {
    try {
      System.out.println("Trying to connect to " + host + ":" + port + "...");
      socket = new Socket(host, port);
      System.out.println("Connection established to " + host + ":" + port);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   *Sending the message as Object to connected server port
   * @param message
   * @throws IOException
   *
   */
  protected void tcpSendRequest(Object message) throws IOException {
    if (!socket.isClosed()){
      out.writeObject(message);
      out.flush();
    } else {
      if (message instanceof Message){
        Message msg = (Message) message;
        System.out.println("Player " + msg.getSourcePlayerID() + " couldn't send messages any more");
      }
    }
  }


  /**
   * Receiving the response and casting it
   * In case actually to String
   * @param responseClazz
   * @return
   * @param <T>
   * @throws IOException
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("unchecked")
  protected <T> T tcpReadResponse(Class<T> responseClazz) throws IOException, ClassNotFoundException {
    if (!socket.isClosed()){
      return responseClazz.cast(in.readObject());
    }
    return null;
  }

  /**
   * closing of socket connection
   */
  @SuppressWarnings("java:S112")
  public void shutdown() {
    try{
      out.close();
      in.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
