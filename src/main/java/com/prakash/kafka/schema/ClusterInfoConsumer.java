package com.prakash.kafka.schema;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ClusterInfoConsumer {
    private static final Logger logger
            = LoggerFactory.getLogger(ClusterInfoConsumer.class);
    public static void main(String[] args) {
        logger.info("Starting kafka consumer");
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "test1124411");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "com.prakash.kafka.schema.ClusterInfoDeSerializer");
        KafkaConsumer<String, com.prakash.kafka.schema.ClusterInfo> consumer = new KafkaConsumer<>(props);
        String topic="test1";
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, com.prakash.kafka.schema.ClusterInfo> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, com.prakash.kafka.schema.ClusterInfo> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
