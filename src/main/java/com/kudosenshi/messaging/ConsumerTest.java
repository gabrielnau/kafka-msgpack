package com.kudosenshi.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.msgpack.MessagePack;
import org.msgpack.unpacker.Unpacker;

public class ConsumerTest {
    public static void main(String[] args) throws IOException {

        Properties config = new Properties();
        config.put("zookeeper.connect", "localhost:2181");
        config.put("group.id", "default");
        config.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<String, byte[]>(config);) {
            consumer.subscribe(Arrays.asList("jps-messages-v1"));
            int timeouts = 0;

            MessagePack msgpack = new MessagePack();
            while (true) {
                ConsumerRecords<String, byte[]> records = consumer.poll(200);
                if (records.count() == 0) {
                    timeouts++;
                } else {
                    System.out.printf("Got %d records after %d timeouts\n", records.count(), timeouts);
                    for (ConsumerRecord<String, byte[]> record : records) {
                        switch (record.topic()) {
                            case "jps-messages-v1":
                                ByteArrayInputStream in = new ByteArrayInputStream(record.value());
                                Unpacker unpacker = msgpack.createUnpacker(in);
                                MyMessage message = unpacker.read(MyMessage.class);
                                System.out.println(message.name + " " + message.version);
                                for(String item: message.sample) {
                                    System.out.println(item);
                                }
                                break;
                            default:
                                throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
                        }
                    }
                    timeouts = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Consumer closed. ");
        }

    }
}
