package com.example.topology.routerservice.controller;

import com.example.system.data.Data;
import com.example.system.data.DataEnrichedWithSession;
import com.example.system.kafka.KafkaTopicConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class ListenerConfig {

  @Bean
  public KafkaMessageListenerContainer<String, DataEnrichedWithSession> replyContainer(
          ConsumerFactory<String, DataEnrichedWithSession> consumerFactory) {
    ContainerProperties containerProps = new ContainerProperties(KafkaTopicConst.DATA_SESSION_ENRICHED_REPLY_TOPIC);
    containerProps.setGroupId(KafkaTopicConst.DATA_ENRICHED_WITH_SESSION_TOPIC);
    return new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
  }

  @Bean
  public ReplyingKafkaTemplate<String, Data, DataEnrichedWithSession> replyingKafkaTemplate(
      ProducerFactory<String, Data> pf,
      KafkaMessageListenerContainer<String, DataEnrichedWithSession> container) {
    return new ReplyingKafkaTemplate<>(pf, container);
  }
}
