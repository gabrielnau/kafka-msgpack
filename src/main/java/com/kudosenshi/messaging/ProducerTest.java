package com.kudosenshi.messaging;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.msgpack.MessagePack;

public class ProducerTest extends Thread {

    private final KafkaProducer<String, byte[]> producer;
    private final String topic;
    private final Boolean isAsync;

    public ProducerTest(String topic, Boolean isAsync) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        props.put("client.id", "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        producer = new KafkaProducer<String, byte[]>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    public void run() {
        String messageId = UUID.randomUUID().toString();
        String messageStr = "Message_" + messageId;
        long startTime = System.currentTimeMillis();

        MyMessage src = new MyMessage();
        src.name = "msgpack";
        src.version = "0.1";

        MessagePack msgpack = new MessagePack();
        byte[] bytes = null;
        try {
            bytes = msgpack.write(src);

            if (isAsync) {
                producer.send(new ProducerRecord<String, byte[]>(topic, String.valueOf(messageId), bytes),
                              new MessageCallback(startTime, messageId, messageStr));
            } else {
                producer.send(new ProducerRecord<String, byte[]>(topic, String.valueOf(messageId), bytes)).get();
                System.out.println("Sent message: (" + messageId + ", " + messageStr + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ProducerTest producer = new ProducerTest("jps-messages-v1", false);
        producer.start();
    }
}

class MessageCallback implements Callback {
    private long startTime;
    private String key;
    private String message;

    public MessageCallback(long startTime, String key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            String.format("message(%s, %s) sent to partition(%s), offset(%s) in % ms",
                          key,
                          message,
                          metadata.partition(),
                          metadata.offset(),
                          elapsedTime);

        } else {
            exception.printStackTrace();
        }
    }
}
