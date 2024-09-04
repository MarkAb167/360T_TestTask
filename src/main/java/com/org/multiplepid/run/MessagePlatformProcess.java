package com.org.multiplepid.run;

import com.org.ShutdownCondition;
import com.org.multiplepid.TcpMessagingPlatform;

public class MessagePlatformProcess {

  public static void main(String[] args) throws Exception {
    int platformPort = 9049;

    ShutdownCondition shutdownCondition = message -> message.getReplyCount() == 10;

    TcpMessagingPlatform tcpMessagingPlatform = new TcpMessagingPlatform(platformPort, shutdownCondition);
    tcpMessagingPlatform.launchAndListen();
  }
}
