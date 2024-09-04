package com.org;

/**
 * Represents a function that accepts a message and return a same message with counter.
 */
public class AutoReplySameWithCounter implements AutoReply {
  public static final String DELIMITER = "_";

  @Override public String replyWithPayload(Message incomingMessage) {
    if (incomingMessage.getPayload().contains(DELIMITER)) {
      return incomingMessage.getPayload().split(DELIMITER)[0] + DELIMITER + incomingMessage.getReplyCount();
    } else {
      return incomingMessage.getPayload() + DELIMITER + incomingMessage.getReplyCount();
    }
  }
}
