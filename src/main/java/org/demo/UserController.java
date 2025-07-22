package org.demo;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.demo.producer.UserProducer;

@Path("/users")
public class UserController {

    @Inject
    UserProducer userProducerService;

    @GET
    @Path("/publish")
    @Produces(MediaType.TEXT_PLAIN)
    public String publish() {
        userProducerService.publishUsers();
        return "Users published to Kafka!";
    }
}
