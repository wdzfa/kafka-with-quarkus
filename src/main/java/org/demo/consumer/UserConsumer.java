package org.demo.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.demo.dto.UserDto;
import org.demo.util.AgeUtils;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.demo.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class UserConsumer {

    private static final Logger LOGGER = Logger.getLogger(UserConsumer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    DataSource dataSource;

    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository = new UserRepository(dataSource);
    }

    @Incoming("random-user")
    @Blocking
    public void consume(String message) {
        try {
            JsonNode user = mapper.readTree(message);
            UserDto userDTO = mapToUserDTO(user);
            userRepository.save(userDTO);
            LOGGER.info("Inserted user: " + userDTO.getFullName());
        } catch (Exception e) {
            LOGGER.error("Failed to process user record", e);
        }
    }

    private UserDto mapToUserDTO(JsonNode user) {
        UserDto userDTO = new UserDto();
        userDTO.setUuid(UUID.fromString(user.get("login").get("uuid").asText()));
        userDTO.setFullName(
                user.get("name").get("title").asText() + " " +
                        user.get("name").get("first").asText() + " " +
                        user.get("name").get("last").asText()
        );
        userDTO.setUsername(user.get("login").get("username").asText());
        userDTO.setEmail(user.get("email").asText().toLowerCase());
        userDTO.setGender(capitalize(user.get("gender").asText()));
        userDTO.setAge(user.get("dob").get("age").asInt());
        userDTO.setAgeCategory(AgeUtils.getAgeCategory(userDTO.getAge()));
        userDTO.setAdult(userDTO.getAge() >= 18);
        userDTO.setDob(Timestamp.from(Instant.parse(user.get("dob").get("date").asText())));
        userDTO.setRegistered(Timestamp.from(Instant.parse(user.get("registered").get("date").asText())));
        JsonNode location = user.get("location");
        userDTO.setCountry(location.get("country").asText());
        userDTO.setCity(location.get("city").asText());
        userDTO.setStreetAddress(location.get("street").get("number").asText() + " " +
                location.get("street").get("name").asText());
        userDTO.setLatitude(location.get("coordinates").get("latitude").asDouble());
        userDTO.setLongitude(location.get("coordinates").get("longitude").asDouble());
        userDTO.setTimezone(location.get("timezone").get("description").asText());
        userDTO.setPhone(user.get("phone").asText());
        userDTO.setCell(user.get("cell").asText());
        userDTO.setProfilePicUrl(user.get("picture").get("large").asText());
        return userDTO;
    }

    private String capitalize(String input) {
        return input == null || input.isEmpty()
                ? input
                : input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}