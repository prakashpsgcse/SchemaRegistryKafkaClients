./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties


When we use Custom serializer if we add new field when we produce client deserilization will fail in object mapper
When we use Avro to read Not Avro we get **Unknown magic byte** (schemaid is missing)

1.Lets add field to producer without default
    When we add new field without default value , SR will reject this schems
12:43:59.774 [main] DEBUG i.c.k.s.c.r.RestService - Sending POST with input {"schema":"{\"type\":\"record\",\"name\":\"ClusterInfo\",\"namespace\":\"com.prakash.schema.avro\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Name of the cluster\"},{\"name\":\"kafkaUrl\",\"type\":\"string\",\"doc\":\"kafka url\",\"default\":\"localhost:9092\"},{\"name\":\"zkUrl\",\"type\":\"string\",\"doc\":\"zookeeper url\",\"default\":\"localhost:2181\"},{\"name\":\"id\",\"type\":\"int\",\"doc\":\"dummy id\",\"default\":\"1\"},{\"name\":\"kafkaNodes\",\"type\":\"int\",\"doc\":\"dummy id\"}]}"} to http://localhost:8081/subjects/prakash-value/versions
Exception in thread "main" org.apache.kafka.common.errors.SerializationException: Error registering Avro schema: {"type":"record","name":"ClusterInfo","namespace":"com.prakash.schema.avro","fields":[{"name":"name","type":"string","doc":"Name of the cluster"},{"name":"kafkaUrl","type":"string","doc":"kafka url","default":"localhost:9092"},{"name":"zkUrl","type":"string","doc":"zookeeper url","default":"localhost:2181"},{"name":"id","type":"int","doc":"dummy id","default":"1"},{"name":"kafkaNodes","type":"int","doc":"dummy id"}]}
Caused by: io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException: Schema being registered is incompatible with an earlier schema for subject "prakash-value"; error code: 409

2. Lets try with adding default value
   
    With default we are able to producer 

3. lets run the consumer which will use OLD schema (without kafkaNodes)
   It consumes both old and New ???? 
       yes .Its is able to get both data 

offset = 3, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1}
offset = 4, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1}
offset = 5, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5}
offset = 6, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5}


4. Consumer is new (it is using the latest schema) now when it consumes from begining
     ?????\
    Its able to process .O/P is same as previous (old records its not using default value)
   offset = 4, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1}
   offset = 5, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5}
   offset = 6, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5
   
Lets try with first AVRO we created (Transtive)





Exception in thread "main" org.apache.kafka.common.errors.SerializationException: Error deserializing key/value for partition test1-0 at offset 10. If needed, please seek past the record to continue consumption.
Caused by: org.apache.kafka.common.errors.SerializationException: Error deserializing Avro message for id -1
Caused by: org.apache.kafka.common.errors.SerializationException: Unknown magic byte!

NoW Changed to FORWARD
```curl -X PUT \
http://localhost:8081/config/prakash-value \
-H 'cache-control: no-cache' \
-H 'content-type: application/vnd.schemaregistry.v1+json' \
-H 'postman-token: de7c57b2-593b-1df2-c7b4-ad76d51d8b38' \
-d '{"compatibility": "FORWARD"}'```

Lets add new field without default
   added zkNodes without fefault : ACCEPTED 
   
 Now lets run old consumer from begining 
 
   Its able to process all 3 formatrs but new getZooNodes() is not available 
   offset = 4, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1}
offset = 5, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5}
offset = 6, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5}
offset = 7, key = dev-app, value = {"name": "test", "kafkaUrl": "test:9092", "zkUrl": "test:2181", "id": 1, "kafkaNodes": 5, "zooNodes": 5}

#Schema Registry compatibility levels
