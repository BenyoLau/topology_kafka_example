package com.example.usersessiondataworkflow.kafka;

import com.example.system.data.DataEnrichedWithSession;
import com.example.system.kafka.KafkaTopicConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class DataWorkflow {

    private static final Logger log = LoggerFactory.getLogger(DataWorkflow.class);

    @Autowired
    KafkaTemplate<String, DataEnrichedWithSession> kafkaTemplate;

    @KafkaListener(topics = KafkaTopicConst.DATA_ENRICHED_WITH_SESSION_TOPIC, groupId = KafkaTopicConst.DATA_ENRICHED_WITH_SESSION_TOPIC)
    public void handleWorkflow(DataEnrichedWithSession dataEnrichedWithSession, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.debug("Handling workflow for [{}]", dataEnrichedWithSession);
        Message<DataEnrichedWithSession> message = MessageBuilder
                .withPayload(dataEnrichedWithSession)
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicConst.DATA_SESSION_ENRICHED_REPLY_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

}

