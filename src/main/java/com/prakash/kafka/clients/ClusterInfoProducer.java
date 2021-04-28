package com.prakash.kafka.clients;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClusterInfoProducer {

    private static final Logger logger
            = LoggerFactory.getLogger(ClusterInfoProducer.class);

    public static void main(String[] args) {
        logger.info("Starting kafka producer");
        Properties producerPros = new Properties();
        producerPros.put("bootstrap.servers", "localhost:9092");
        producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerPros.put("value.serializer", "com.prakash.kafka.clients.ClusterInfoSerializer");
        producerPros.put("acks", "all");

        Producer<String, ClusterInfo> producer = new KafkaProducer<>(producerPros);
       String topic="test1";
        String clusterName="dev-app";
        ClusterInfo devApp=new ClusterInfo();
        devApp.setId(1);
        devApp.setKafkaUrl("app-dev.test.prakash.com:9092");
        devApp.setZkUrl("app-dev.test.prakash.com:9092");
        devApp.setName("app-dev.test.prakash.com:9092");
        devApp.setNodes(4);
        ProducerRecord<String,ClusterInfo> message=new ProducerRecord<>(topic,clusterName,devApp);
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
