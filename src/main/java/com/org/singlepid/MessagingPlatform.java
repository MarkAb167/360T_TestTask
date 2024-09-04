package com.org.singlepid;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import com.org.AutoReply;
import com.org.Message;
import com.org.ShutdownCondition;

/**
 * Implementation of the Messaging Platform which routes messages between different Players.
 * This class registers, stores players and routes messaging asynchronously.
 */
public class MessagingPlatform {
  public static final Random RANDOM = new Random();
  private final Map<String, Player> players = new ConcurrentHashMap<>();
  private final BlockingQueue<Message> messageInboundQueue = new ArrayBlockingQueue<>(100);
  private final AtomicBoolean shutdownFlag = new AtomicBoolean(false);
  private final Thread processorThread;
  private final ShutdownCondition shutdownCondition;


  public MessagingPlatform(ShutdownCondition shutdownCondition) {
    processorThread = new Thread(this::internalMessageProcessing);
    processorThread.start();
    this.shutdownCondition = shutdownCondition;
  }

  /**
   * Method puts incoming messages into ArrayBlockingQueue that provides FIFO functionality for working with asynchronously
   * @param message
   */
  public void publish(Message message) {
    if (!isAlive()) {
      System.err.println(
          "Player: " + message.getSourcePlayerID() + " tried to send message after system shutdown. Message: " + message.getPayload());
      return;
    }
    try {
      messageInboundQueue.put(message);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Failed to enqueue message: " + message  + " err: " + e.getMessage());
    }
  }

  public Player registerPlayer(String playerName, AutoReply autoReply) {
    String id = playerName.replace(" ", "") + RANDOM.nextInt(10000);
    Player player = new Player(id, playerName, this, autoReply);
    players.put(id, player);
    System.out.println("Player registered: " + playerName + " (id: " + id + ")");
    return player;
  }

  /**
   * While shutDown flag is closed, the process Thread takes messages from blocking queue, handles and then sends the message back
   */
  private void internalMessageProcessing() {
    while (isAlive()) {
      try {
        Message message = messageInboundQueue.take();

        // in case we were waiting for next message, while system got shut down
        if (!isAlive()) {
          return;
        }
        if (shutdownCondition.shouldShutdown(message)) {
          shutdown();
          return;
        }
        Player destinationPlayer = players.get(message.getDestinationPlayerID());
        if (destinationPlayer != null) {
          System.out.println("Routing message \"" + message.getPayload() + "\" from " + message.getSourcePlayerID() + " to "
              + message.getDestinationPlayerID());
          destinationPlayer.receiveMessage(message);
        } else {
          System.out.println("Player with id " + message.getDestinationPlayerID() + " not found.");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Message processing interrupted: " + e.getMessage());
        break;
      }
    }
  }

  public void shutdown() {
    if (!isAlive()) {
      return;
    }
    shutdownFlag.set(true);
    if (processorThread != null) {
      processorThread.interrupt();
    }
    System.out.println("System is shutting down...");
    players.values().forEach(Player::disconnectAndStop);
  }

  public boolean isAlive() {
    return !shutdownFlag.get();
  }
}
