package com.prakash.kafka.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ClusterInfoSerializer implements Serializer<ClusterInfo> {
    private static final Logger logger
            = LoggerFactory.getLogger(ClusterInfoSerializer.class);
    private  ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map map, boolean b) {
        logger.debug("ClusterInfoSerializer initialized");
    }

    @Override
    public byte[] serialize(String s, ClusterInfo clusterInfo) {

        logger.debug("serializing {}",clusterInfo);
        byte[] clusterInfoInBytes = null;
        try {
            clusterInfoInBytes=objectMapper.writeValueAsString(clusterInfo).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return clusterInfoInBytes;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, ClusterInfo clusterInfo) {
        logger.debug("serializing {}",clusterInfo);
        byte[] clusterInfoInBytes = null;
        try {
            clusterInfoInBytes=objectMapper.writeValueAsString(clusterInfo).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return clusterInfoInBytes;
    }

    @Override
    public void close() {

    }
}
