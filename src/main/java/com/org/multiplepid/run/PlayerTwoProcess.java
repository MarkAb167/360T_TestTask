package com.org.multiplepid.run;

import com.org.AutoReplySameWithCounter;
import com.org.multiplepid.TcpMessagingPlatformClient;
import com.org.multiplepid.TcpPlayerDevice;

public class PlayerTwoProcess {

  private static final String LOCAL_HOST = "localhost";
  public static void main(String[] args) throws Exception {

    int playerPort = 9051;
    int platformPort = 9049;

    String playerName = "Another Player";
    String playerId = "Another_Player_ID";

    var platformClient = new TcpMessagingPlatformClient(LOCAL_HOST, platformPort);

    TcpPlayerDevice playerDevice =
        new TcpPlayerDevice(playerPort, playerId, playerName, platformClient,  new AutoReplySameWithCounter());

    new Thread(playerDevice::launchAndListen).start();

    Thread.sleep(2000);
    System.out.println("Player " + playerName + " is running on port " + playerPort+ " and is ready to play!");

    platformClient.registerPlayer(playerId, playerName, LOCAL_HOST, playerPort);

    System.out.println("Player is registered");
  }
}
