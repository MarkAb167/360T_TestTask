package com.org.multiplepid;

import java.io.IOException;

import com.org.multiplepid.dto.RegistrationDto;
import com.org.Message;

/**
 * Implementation of Messaging Platform client for communication with the platform server
 * Implements TcpClient
 */
public class TcpMessagingPlatformClient extends TcpClient {

  public TcpMessagingPlatformClient(String host, int port) {
    super(host, port);
  }

  public void sendMessageToPlayer(Message message) throws IOException {
    if(message == null){
      return;
    }
    super.tcpSendRequest(message);
  }

  /**
   * Creates a new Registration dto and sends new request to MessagingPlatform server for registration new player
   * @param id
   * @param name
   * @param host
   * @param port
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public String registerPlayer(String id, String name, String host, int port) throws IOException, ClassNotFoundException {
    System.out.println("Sending registration request from " + name + " to " + host + ":" + port);
    super.tcpSendRequest(new RegistrationDto(id, name, host, port));
    String registeredPlayerId = super.tcpReadResponse(String.class);
    if (!id.equals(registeredPlayerId)) {
      throw new IllegalStateException("Wrong id in response");
    }
    return registeredPlayerId;
  }
}
