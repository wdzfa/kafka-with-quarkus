package org.demo.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log; // Using Quarkus's built-in logger
import jakarta.enterprise.context.ApplicationScoped; // Correct Jakarta EE annotation
import org.apache.kafka.clients.producer.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

@ApplicationScoped
public class UserProducer {
    private static final String TOPIC = "random-user";
    private final ObjectMapper mapper = new ObjectMapper();

    public void publishUsers() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://randomuser.me/api/?results=30"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode results = mapper.readTree(response.body()).get("results");

            for (JsonNode user : results) {
                String jsonUser = user.toString();
                producer.send(new ProducerRecord<>(TOPIC, null, jsonUser), (metadata, exception) -> {
                    if (exception == null) {
                        Log.info("Successfully published user to Kafka: " + jsonUser + " (Topic: " + metadata.topic() + ", Partition: " + metadata.partition() + ", Offset: " + metadata.offset() + ")");
                    } else {
                        Log.error("Failed to publish user to Kafka: " + jsonUser, exception);
                    }
                });
            }
            producer.flush();
            System.out.println("✅ Published all users to Kafka");
        } catch (IOException | InterruptedException e) {
            Log.error("Error fetching or publishing users", e);
            throw new RuntimeException("Error fetching or publishing users", e);
        }
    }
}
