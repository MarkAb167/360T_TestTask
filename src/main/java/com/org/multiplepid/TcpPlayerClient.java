package com.org.multiplepid;

import static java.lang.Thread.sleep;
import java.io.IOException;
import com.org.Message;
import com.org.multiplepid.dto.ShutdownCommand;

/**
 * Simple Player client,that has Player address to send message, and id & name to identify player.
 */
public class TcpPlayerClient extends TcpClient {

  private final String id;
  private final String name;

  public TcpPlayerClient(String host, int port, String id, String name) {
    super(host, port);
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }


  public void sendMessage(Message message) throws IOException {
    super.tcpSendRequest(message);
  }

  @SuppressWarnings("java:S112")
  @Override
  public void shutdown() {
    try {
      tcpSendRequest(new ShutdownCommand());
      sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
