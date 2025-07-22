package com.example.sessionhandlerservice.kafkastream;

import com.example.system.data.DataEnrichedWithSession;
import com.example.system.data.UserSession;
import com.example.system.kafka.KafkaTopicConst;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.UUID;

@Configuration
@EnableKafkaStreams
public class EnrichedDataConsumer {

  private static final Logger log = LoggerFactory.getLogger(EnrichedDataConsumer.class);

  @Autowired KafkaTemplate<String, UserSession> kafkaTemplate;

  @Bean
  public KStream<String, DataEnrichedWithSession> listenToData(StreamsBuilder builder) {

    KStream<String, DataEnrichedWithSession> inputStream =
        builder.stream(
            KafkaTopicConst.MISSING_SESSION_DATA_TOPIC,
            Consumed.with(Serdes.String(), new JsonSerde<>(DataEnrichedWithSession.class)));

    KStream<String, DataEnrichedWithSession> modifiedStream =
        inputStream.mapValues(this::addNewSessionToDto);

    modifiedStream.to(
            KafkaTopicConst.DATA_ENRICHED_WITH_SESSION_TOPIC,
        Produced.with(Serdes.String(), new JsonSerde<>(DataEnrichedWithSession.class)));

    return inputStream;
  }

  private DataEnrichedWithSession addNewSessionToDto(DataEnrichedWithSession data) {
    UserSession session =
        new UserSession(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    DataEnrichedWithSession newData = new DataEnrichedWithSession(data.data(), session);
    kafkaTemplate.send(
        KafkaTopicConst.USER_SESSION_ENRICHMENT_TOPIC, (String) data.data().requestData(), session);

    return newData;
  }
}
