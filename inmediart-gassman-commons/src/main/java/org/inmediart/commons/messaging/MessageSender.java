package org.inmediart.commons.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

public abstract class MessageSender<T> {
    @Value("${gassman.instance.id}")
    protected String instanceId;
    @Value("${gassman.instance.botName}")
    protected String botName;

    protected void sendMessage(MessageChannel userOrderChannel, T payload) {
        Message<GassmanMessage<T>> msg = MessageBuilder.withPayload(new GassmanMessage<>(payload,instanceId,botName)).build();
        userOrderChannel.send(msg);
    }
}
