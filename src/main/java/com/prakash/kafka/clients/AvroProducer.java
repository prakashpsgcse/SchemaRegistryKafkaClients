package com.prakash.kafka.clients;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.prakash.schema.avro.ClusterInfo;

public class AvroProducer {
    private static final Logger logger
            = LoggerFactory.getLogger(AvroProducer.class);

    public static void main(String[] args) {
        logger.info("Starting kafka producer");
        Properties producerPros = new Properties();
        producerPros.put("bootstrap.servers", "localhost:9092");
        producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerPros.put("value.serializer", KafkaAvroSerializer.class.getName());
        producerPros.put("acks", "all");
        producerPros.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                "http://localhost:8081");

        Producer<String, ClusterInfo> producer = new KafkaProducer<>(producerPros);
        String topic="prakash";
        String clusterName="dev-app";


        ClusterInfo info=ClusterInfo.newBuilder().setId(1)
                .setKafkaUrl("test:9092")
                .setName("test")
                .setZkUrl("test:2181")
             //   .setKafkaNodes(5)
              //  .setZooNodes(5)
                .build();
        ProducerRecord<String,ClusterInfo> message=new ProducerRecord<>(topic,clusterName,info);
        Future<RecordMetadata> brokerResponse=producer.send(message);

        while(!brokerResponse.isDone()){
            logger.debug("Record is added to batch .Waiting for batch to picked by I/O thread to send and received ack from broker");
        }
        logger.info("ACK received from broker");
        try {
            logger.info("Message published to partition : {} and Offset: {} ",brokerResponse.get().offset(),brokerResponse.get().partition());
        } catch (InterruptedException| ExecutionException e) {
            logger.error("Not able to publish message to kafka {}",e.getMessage());
        }
        logger.debug("closig producer");
        producer.close();
    }
}
