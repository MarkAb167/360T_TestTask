package com.org.singlepid;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.org.AutoReply;
import com.org.Message;

/**
 * Implementation of the Player.
 * This class is responsible for processing incoming to the Player messages and if it's necessary sending back as reply.
 */

public class Player {
  private final String id;
  private final String name;
  private final MessagingPlatform messagingPlatform;
  private final BlockingQueue<Message> incomingMessageBuffer = new ArrayBlockingQueue<>(100);
  private final AutoReply autoReply;
  private final Thread playerThread;
  private final AtomicInteger replyCounter = new AtomicInteger(0);

  public Player(String id, String name, MessagingPlatform messagingPlatform, AutoReply autoReply) {
    this.id = id;
    this.name = name;
    this.messagingPlatform = messagingPlatform;
    this.autoReply = autoReply;

    if(autoReply != null) {
      playerThread = new Thread(this::handleMessageWithAutoReply);
      playerThread.start();
    } else {
      playerThread = null;
    }
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  /**
   * Cleaning of BlockingQueue and stopping of process thread
   */
  public void disconnectAndStop(){
    playerThread.interrupt();
    incomingMessageBuffer.clear();
    System.out.println("Player: " + name + " (id: " + id + ") disconnected and stopped.");
  }

  /**
   * Putting the message to the Blocking Queue
   * @param message
   */
  public void receiveMessage(Message message) {
    try {
      incomingMessageBuffer.put(message);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("(method receiveMessage) Failed to enqueue message: " + message + ", err : " + e.getMessage());
    }
  }

  /**
   * Sending message to another Player through Messaging Platform
   * @param payload
   * @param destinationPlayerID
   * @param currentReplyCount
   */
  public void sendMessage(String payload, String destinationPlayerID, int currentReplyCount) {
    Message message = new Message(payload, id, destinationPlayerID, currentReplyCount);
    messagingPlatform.publish(message);
  }

  /**
   * This method handles the incoming messages and sends reply back
   */
  private void handleMessageWithAutoReply() {
    while (messagingPlatform.isAlive() && autoReply != null) {
      try {
        Message message = incomingMessageBuffer.take();
        System.out.println("Message from " + message.getSourcePlayerID() + " is received by AutoReply " + name + " (id: " + id + "): " + message.getPayload());
        Message m = new Message(message.getPayload(), message.getSourcePlayerID(), message.getDestinationPlayerID(), replyCounter.incrementAndGet());
        String autoReplyPayload = autoReply.replyWithPayload(m);
        if (autoReplyPayload != null){
          sendMessage(autoReplyPayload, m.getSourcePlayerID(), m.getReplyCount());
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Player message processing interrupted: " + e.getMessage());
        break;
      }
    }
  }
}
