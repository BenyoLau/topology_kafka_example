package com.example.system.kafka;

public interface KafkaTopicConst {

  String DATA_TOPIC = "data";
  String USER_SESSION_ENRICHMENT_TOPIC = "user-session-enrichment";
  String DATA_ENRICHED_WITH_SESSION_TOPIC = "data-enriched-with-session";
  String MISSING_SESSION_DATA_TOPIC = "missing-session-data";
  String DATA_SESSION_ENRICHED_REPLY_TOPIC = "data-session-enriched-reply";
}
