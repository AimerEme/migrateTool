package com.example.migrate.sync.kafka.producter;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, Object message) {
        String json = JSON.toJSONString(message);
        kafkaTemplate.send(topic, json);
    }
}