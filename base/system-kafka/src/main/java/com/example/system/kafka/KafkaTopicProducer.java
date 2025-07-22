package com.example.system.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class KafkaTopicProducer {

  @Autowired KafkaAdmin kafkaAdmin;

  @PostConstruct
  public void postConstruct() {

    Class<?> clazz = KafkaTopicConst.class;
    Field[] fields = clazz.getDeclaredFields();

    List<NewTopic> topics = new ArrayList<>();
    for (Field field : fields) {
      try {
        // Since these are static fields, pass null as the instance
        Object value = field.get(null);

        topics.add(createTopic((String) value));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    kafkaAdmin.createOrModifyTopics(topics.toArray(new NewTopic[] {}));
  }

  private NewTopic createTopic(String topicName) {
    return new NewTopic(topicName, 1, (short) 1);
  }
}
