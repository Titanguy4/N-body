package org.nbody.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduler;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import org.nbody.models.Body;
import org.nbody.models.Vector2D;

import java.util.List;

@ApplicationScoped
public class SimulationService {

    private final NBodyService nBodyService;
    private final MQTTService mqttService;

    @Inject
    Scheduler scheduler;

    private static final double G = 6.67430e-11;



    public SimulationService(NBodyService nBodyService, MQTTService mqttService) {
        this.nBodyService = nBodyService;
        this.mqttService = mqttService;
    }

    @Scheduled(every = "1s", identity = "simulation")
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
        mqttService.sendBodies(jsonBodies);
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

    public void pauseSimulation() {
        scheduler.pause("simulation");
    }

    public void resumeSimulation(){
        scheduler.resume("simulation");
    }
}
