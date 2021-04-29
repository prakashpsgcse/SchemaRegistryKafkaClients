package com.prakash.kafka.clients;

import com.prakash.schema.avro.ClusterInfo;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class AvroConsumer {
    private static final Logger logger
            = LoggerFactory.getLogger(AvroConsumer.class);
    public static void main(String[] args) {
        logger.info("Starting kafka consumer");
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "teuyxxylo");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                "http://localhost:8081");
        KafkaConsumer<String, ClusterInfo> consumer = new KafkaConsumer<>(props);
        String topic="prakash";
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, ClusterInfo> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, ClusterInfo> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
