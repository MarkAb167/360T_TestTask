package com.org;


/**
 * Returns decision based on massage whether message system should be shutdown or not.
 * At them moment it's only based on Message content, but it can be extended to include other platform properties
 */
public interface ShutdownCondition {
  boolean shouldShutdown(Message message);
}
