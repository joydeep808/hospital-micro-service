package com.example.auth_service.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
public class Producer {

  @Value("${spring.kafka.bootstrap}")
  private  String kafkaServerString;

  public Map<String , Object> producer(){
    HashMap<String , Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerString);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return props;
  }


    @Bean
    protected ProducerFactory<String , Object> producerFactory(){
      return new DefaultKafkaProducerFactory<>(producer());
    }
    @Bean
    protected <T> KafkaTemplate <String , T> kafkaTemplate(
      ProducerFactory<String , T > producerFactory
    ){
      return new KafkaTemplate<>(producerFactory);
    }



  
}
