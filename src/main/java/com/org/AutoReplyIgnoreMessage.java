package com.org;

/**
 * Represents a function that accepts a message and return nothing.
 */
public class AutoReplyIgnoreMessage implements AutoReply{
  @Override public String replyWithPayload(Message incomingMessage) {
    return null;
  }
}
