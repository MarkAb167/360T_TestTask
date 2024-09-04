package com.org;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a message that can be sent between players
 * Serializable and used in both inner-process and in communication via TCP
 */
public class Message implements Serializable {

  @Serial private static final long serialVersionUID = 10L;

  private final String payload;
  private final String sourcePlayerID;
  private final String destinationPlayerID;
  private final int replyCount;

  public Message(String payload, String sourcePlayerID, String destinationPlayerID) {
    this(payload, sourcePlayerID, destinationPlayerID, 0);
  }

  public Message(String payload, String sourcePlayerID, String destinationPlayerID, int replyCount) {
    this.payload = payload;
    this.sourcePlayerID = sourcePlayerID;
    this.destinationPlayerID = destinationPlayerID;
    this.replyCount = replyCount;
  }

  public String getPayload() {
    return payload;
  }

  public String getSourcePlayerID() {
    return sourcePlayerID;
  }

  public String getDestinationPlayerID() {
    return destinationPlayerID;
  }

  public int getReplyCount() {
    return replyCount;
  }

  @Override public String toString() {
    return "Message{" +
        "payload='" + payload + '\'' +
        ", sourcePlayerID='" + sourcePlayerID + '\'' +
        ", destinationPlayerID='" + destinationPlayerID + '\'' +
        ", replyCount=" + replyCount +
        '}';
  }


}
