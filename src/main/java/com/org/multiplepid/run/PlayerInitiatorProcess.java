package com.org.multiplepid.run;

import com.org.AutoReplyIgnoreMessage;
import com.org.AutoReplySameWithCounter;
import com.org.multiplepid.TcpMessagingPlatformClient;
import com.org.multiplepid.TcpPlayerDevice;

public class PlayerInitiatorProcess {

  private static final String LOCAL_HOST = "localhost";

  public static void main(String[] args) throws Exception {

    int playerPort = 9052;
    int platformPort = 9049;

    String playerName = "Initiator";
    String playerId = "Initiator_ID";

    var platformClient = new TcpMessagingPlatformClient(LOCAL_HOST, platformPort);

    TcpPlayerDevice playerDevice =
        new TcpPlayerDevice(playerPort, playerId, playerName, platformClient, new AutoReplyIgnoreMessage());

    new Thread(playerDevice::launchAndListen).start();
    Thread.sleep(2000);
    System.out.println("Player " + playerName + " is running on port " + playerPort + " and is ready to play!");

    platformClient.registerPlayer(playerId, playerName, LOCAL_HOST, playerPort);
    System.out.println("Player is registered");

    Thread.sleep(5000);

    String anotherPlayerId = "Another_Player_ID";

    playerDevice.sendMessage("Hello", anotherPlayerId);
    playerDevice.sendMessage("Hi", anotherPlayerId);
    playerDevice.sendMessage("Here we go", anotherPlayerId);
    playerDevice.sendMessage("Thanks", anotherPlayerId);
    playerDevice.sendMessage("Danke", anotherPlayerId);
    playerDevice.sendMessage("How are you", anotherPlayerId);
    playerDevice.sendMessage("Lets go", anotherPlayerId);
    playerDevice.sendMessage("It is nice", anotherPlayerId);
    playerDevice.sendMessage("Perfect", anotherPlayerId);
    playerDevice.sendMessage("Actually not", anotherPlayerId);
    //Thread.sleep(2000);
    playerDevice.sendMessage("Bye!", anotherPlayerId);

    System.out.println("Player 'Initiator', just sent the very last message to 'Another Player'");
  }
}
