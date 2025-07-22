package com.example.sessionhandlerservice.kafkastream;

import com.example.system.data.Data;
import com.example.system.data.DataEnrichedWithSession;
import com.example.system.data.UserSession;
import com.example.system.kafka.KafkaTopicConst;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Map;

@Configuration
@EnableKafkaStreams
public class SessionHandlerDataTopic {

  private static final Logger log = LoggerFactory.getLogger(SessionHandlerDataTopic.class);

  @Bean
  public KStream<String, Data> dataStream(StreamsBuilder builder) {
    log.debug("Creating KStream from data stream");
    // 1. Create enrichment table
    KTable<String, UserSession> enrichTable =
        builder.table(
            KafkaTopicConst.USER_SESSION_ENRICHMENT_TOPIC,
            Consumed.with(Serdes.String(), new JsonSerde<>(UserSession.class)));

    // 2. Main stream from 'data' topic
    KStream<String, Data> dataStream =
        builder.stream(
                KafkaTopicConst.DATA_TOPIC,
                Consumed.with(Serdes.String(), new JsonSerde<>(Data.class)))
            .selectKey((k, v) -> (String) v.requestData());

    // 3. Enrich with join
    KStream<String, DataEnrichedWithSession> enriched =
        dataStream.leftJoin(enrichTable, DataEnrichedWithSession::new);

    // 4. Publish enriched output
    enriched
        .split()
        .branch(
            (key, value) -> value.userSession() != null,
            Branched.withConsumer(ks -> ks.to(KafkaTopicConst.DATA_ENRICHED_WITH_SESSION_TOPIC)))
        .defaultBranch(
            Branched.withConsumer(ks -> ks.to(KafkaTopicConst.MISSING_SESSION_DATA_TOPIC)));

    return dataStream;
  }
}
