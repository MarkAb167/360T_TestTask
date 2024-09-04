package com.org.singlepid.run;

import com.org.AutoReplyIgnoreMessage;
import com.org.AutoReplySameWithCounter;
import com.org.singlepid.MessagingPlatform;
import com.org.singlepid.Player;

public class SingleProcess {
    public static void main(String[] args) {

        MessagingPlatform messagingPlatform = new MessagingPlatform(message -> message.getReplyCount() == 10);
        Player initiator = messagingPlatform.registerPlayer("Initiator", new AutoReplyIgnoreMessage());
        Player secondOne = messagingPlatform.registerPlayer("SecondOne", new AutoReplySameWithCounter());
        initiator.sendMessage("Hello1", secondOne.getId(), 0);
        initiator.sendMessage("Hello2", secondOne.getId(), 0);
        initiator.sendMessage("Hello3", secondOne.getId(), 0);
        initiator.sendMessage("Hello4", secondOne.getId(), 0);
        initiator.sendMessage("Hello5", secondOne.getId(), 0);
        initiator.sendMessage("Hello6", secondOne.getId(), 0);
        initiator.sendMessage("Hello7", secondOne.getId(), 0);
        initiator.sendMessage("Hello8", secondOne.getId(), 0);
        initiator.sendMessage("Hello9", secondOne.getId(), 0);
        initiator.sendMessage("Hello10", secondOne.getId(), 0);
        initiator.sendMessage("Hello11", secondOne.getId(), 0);
        initiator.sendMessage("Hello12", secondOne.getId(), 0);
        initiator.sendMessage("Hello13", secondOne.getId(), 0);
        initiator.sendMessage("Hello14", secondOne.getId(), 0);
        initiator.sendMessage("Hello15", secondOne.getId(), 0);
        initiator.sendMessage("Hello16", secondOne.getId(), 0);
    }
}
