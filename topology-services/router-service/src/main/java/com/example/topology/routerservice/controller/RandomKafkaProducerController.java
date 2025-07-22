package com.example.topology.routerservice.controller;

import com.example.system.data.Data;
import com.example.system.data.DataEnrichedWithSession;
import com.example.system.kafka.KafkaTopicConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/random")
public class RandomKafkaProducerController {

    @Autowired
    ReplyingKafkaTemplate<String, Data, DataEnrichedWithSession> replyingKafkaTemplate;

    @PostMapping("/{id}")
    public ResponseEntity<DataEnrichedWithSession> send(@PathVariable String id) throws ExecutionException, InterruptedException, TimeoutException {

        ProducerRecord<String, Data> record = new ProducerRecord<>(KafkaTopicConst.DATA_TOPIC, new Data(id));
        //record.headers().add(KafkaHeaders.REPLY_TOPIC, KafkaTopicConst.DATA_SESSION_ENRICHED_REPLY_TOPIC.getBytes());

        RequestReplyFuture<String, Data, DataEnrichedWithSession> future =
                replyingKafkaTemplate.sendAndReceive(record);

        // Wait for the reply (timeout = 10s)
        ConsumerRecord<String, DataEnrichedWithSession> response = future.get(100, TimeUnit.SECONDS);


        return ResponseEntity.ok(response.value());
    }


}
