package com.org;

/**
 * Represents a function that accepts a message and produces a reply message.
 * In response only payload is expected, since reply assumes that it will be sent back to source player
 * Payload can be replaced in future with some typed class
 */
@FunctionalInterface
public interface AutoReply {
  String replyWithPayload(Message incomingMessage);
}
