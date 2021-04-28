package com.prakash.kafka.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ClusterInfoDeSerializer implements Deserializer<ClusterInfo> {


    private static final Logger logger
            = LoggerFactory.getLogger(ClusterInfoDeSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map map, boolean b) {
        logger.debug("ClusterInfoDeSerializer initialized");
    }

    @Override
    public ClusterInfo deserialize(String s, byte[] bytes) {
        logger.debug("DeSerializing..... ");
        ClusterInfo info = null;
        try {
            info = objectMapper.readValue(bytes, ClusterInfo.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        logger.debug("DeSerialized clusterinfo  {}",info);
        return info;
    }

    @Override
    public ClusterInfo deserialize(String topic, Headers headers, byte[] data) {
        logger.debug("DeSerializing..... ");
        ClusterInfo info = null;
        try {
            String dateInText=new String(data);
            System.out.println("******************"+dateInText);
            info = objectMapper.readValue(data, ClusterInfo.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        logger.debug("DeSerialized clusterinfo  {}",info);
        return info;
    }

    @Override
    public void close() {

    }
}
