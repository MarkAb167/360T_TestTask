package com.org.multiplepid;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.org.multiplepid.dto.ShutdownCommand;
import com.org.AutoReply;
import com.org.Message;

/**
 * Implementation of Player Device
 * Implements TcpServer
 */
public class TcpPlayerDevice extends TcpServer {
  private final String id;
  private final String name;
  private final TcpMessagingPlatformClient tcpMessagingPlatformClient;
  private final AutoReply autoReply;

  private final AtomicInteger replyCounter = new AtomicInteger(0);

  public TcpPlayerDevice(int port, String id, String name, TcpMessagingPlatformClient platformClient, AutoReply autoReply)
      throws IOException {
    super(port);
    this.tcpMessagingPlatformClient = platformClient;
    this.id = id;
    this.name = name;
    this.autoReply = autoReply;
  }


  @Override protected String getServerPublicName() {
    return name + "'s Device";
  }

  /**
   * @param request - is an object, but actually it could be an implementation of different classes (Message, ShutdownCommand)
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  @Override
  protected Object receiveEvent(Object request) throws IOException {
    if (request instanceof Message message) {
      String payload = autoReply.replyWithPayload(message);

      if (payload != null){
        Message replyMessage = new Message(payload, message.getDestinationPlayerID(),
                message.getSourcePlayerID(), replyCounter.incrementAndGet());
        System.out.println(
                "Sending reply message: " + message.getPayload() + " to player: " + message.getSourcePlayerID() + " with reply count: "
                        + message.getReplyCount());
        tcpMessagingPlatformClient.sendMessageToPlayer(replyMessage);
      }else {
        System.out.println("Player " + message.getDestinationPlayerID() + "received reply from player " + message.getSourcePlayerID());
      }
      return ((Message) request).getSourcePlayerID();
    } else if (request instanceof ShutdownCommand) {
      this.shutdown();
      return request;
    } else {
      throw new IllegalArgumentException("Message must be of type Message");
    }
  }

  /**
   * sending message to the Player through the Messaging Platform
   * @param payload
   * @param destinationPlayerId
   * @throws IOException
   */
  public void sendMessage(String payload, String destinationPlayerId) throws IOException {
    tcpMessagingPlatformClient.sendMessageToPlayer(new Message(payload, id, destinationPlayerId, 0));
  }

  @Override
  public void shutdown() {
    tcpMessagingPlatformClient.shutdown();
    super.shutdown();
  }
}
