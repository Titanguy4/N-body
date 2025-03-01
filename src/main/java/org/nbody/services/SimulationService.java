package org.nbody.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.nbody.models.Body;
import org.nbody.models.Vector2D;

import java.util.List;

@ApplicationScoped
public class SimulationService {

    private final NBodyService nBodyService;

    private static final double G = 6.67430e-11;

    @Channel("bodies-out")
    Emitter<String> bodyEmitter;

    public SimulationService(NBodyService nBodyService) {
        this.nBodyService = nBodyService;
    }

    @Scheduled(every = "1s")
    public void updateSimulation(){
        List<Body> bodies = nBodyService.getAllBodies();
        double deltaTime = 1.0;

        for(Body body : bodies){
            Vector2D force = new Vector2D(0, 0);

            for(Body other : bodies){
                if(!body.equals(other)){
                    Vector2D coordinateVector = other.getPosition().subtract(body.getPosition());
                    double distance = coordinateVector.distance();

                    if(distance != 0){
                        double forceValue = G * body.getMass() * other.getMass() / (distance * distance);
                        Vector2D forceVector = coordinateVector.normalize().multiplyByScalar(forceValue);
                        force.add(forceVector);
                    }
                }
            }
            Vector2D acceleration = force.multiplyByScalar(1/body.getMass());
            body.setAcceleration(acceleration);
            body.getVelocity().add(acceleration.multiplyByScalar(deltaTime)); // change the velocity of the body
            body.getPosition().add(body.getVelocity().multiplyByScalar(deltaTime)); // change the position of the body
        }
        String jsonBodies = convertBodiesToJson(bodies);
        System.out.println("MQTT message : " + jsonBodies);
        bodyEmitter.send(jsonBodies);
    }

    private String convertBodiesToJson(List<Body> bodies) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(bodies);
        } catch (JsonProcessingException e){
            e.printStackTrace();
            return "[]";
        }
    }
}
