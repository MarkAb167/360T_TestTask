package com.org.multiplepid;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.org.multiplepid.dto.RegistrationDto;
import com.org.Message;
import com.org.ShutdownCondition;

/**
 * Implementation of Messaging Platform for routing messages between different players.
 * Implements TcpServer
 */
public class TcpMessagingPlatform extends TcpServer {
  private final Map<String, TcpPlayerClient> playersClients = new ConcurrentHashMap<>();
  private final ShutdownCondition shutdownCondition;


  public TcpMessagingPlatform(int port, ShutdownCondition shutdownCondition) throws IOException {
    super(port);
    this.shutdownCondition = shutdownCondition;
  }

  @Override
  protected String getServerPublicName() {
    return "Messaging Platform";
  }


  /**
   * Implementation of the TcpServer's abstract method.
   * Method is responsible for routing incoming messages.
   * Depends on incoming request's object type, method could send the message back or make a registration of new player at the platform
   * @param request - is an object, but actually it could be an implementation of different classes (Message, ShutdownCommand)
   * @return
   * @throws IOException
   */
  @Override
  protected Object receiveEvent(Object request) throws IOException {
    if (Objects.requireNonNull(request) instanceof Message message) {
      System.out.println("Routing message \"" + message.getPayload() + "\" from " + message.getSourcePlayerID() + " to "
          + message.getDestinationPlayerID());
      TcpPlayerClient destinationPlayer = playersClients.get(message.getDestinationPlayerID());
      if (destinationPlayer == null) {
        System.out.println("Destination player '" + message.getDestinationPlayerID() + "' IS NOT FOUND.");
        return null;
      }
      destinationPlayer.sendMessage(message);
      if (shutdownCondition.shouldShutdown(message)) {
        System.out.println("Shutdown condition is met!");
        shutdown();
        return null;
      }
      return "OK";
    } else if (request instanceof RegistrationDto registrationDto) {
      playersClients.put(registrationDto.id(),
          new TcpPlayerClient(registrationDto.host(), registrationDto.port(), registrationDto.id(), registrationDto.name()));
      System.out.println("Player '" + registrationDto.name() + "' (id: " + registrationDto.id() + ") is registered.");
      return registrationDto.id();
    }
    throw new IllegalStateException("Unexpected value: " + request);
  }

  /**
   * shut down command
   * invokes shutdown command for every registered player stored at the platform system
   */
  public void shutdown() {
    if (shutdownFlag.get()) {
      return;
    }
    super.shutdown();
    playersClients.values().forEach(tcpPlayerClient -> {
      System.out.println("Shutting down player '" + tcpPlayerClient.getName() + "' (id: " + tcpPlayerClient.getId() + ")");
      tcpPlayerClient.shutdown();
      System.out.println("Player '" + tcpPlayerClient.getName() + "' (id: " + tcpPlayerClient.getId() + ") is shutdown.");
    });
  }
}
