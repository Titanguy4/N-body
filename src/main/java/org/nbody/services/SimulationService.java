package org.nbody.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.nbody.models.Body;
import org.nbody.models.Vector2D;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class SimulationService {

    private final NBodyService nBodyService;
    private final MQTTService mqttService;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private static final double G = 6.67430e-11;
    private static final long UPDATE_INTERVAL_MS = 17;

    private boolean running = true;

    public SimulationService(NBodyService nBodyService, MQTTService mqttService) {
        this.nBodyService = nBodyService;
        this.mqttService = mqttService;
        startSimulation();
    }

    void startSimulation() {
        executorService.scheduleAtFixedRate(() -> {
            if (running) {
                updateSimulation();
            }
        }, 0, UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    public void updateSimulation() {
        List<Body> bodies = nBodyService.getAllBodies();
        double deltaTime = 1.0;

        for (Body body : bodies) {
            Vector2D force = new Vector2D(0, 0);

            for (Body other : bodies) {
                if (!body.equals(other)) {
                    Vector2D coordinateVector = other.getPosition().subtract(body.getPosition());
                    double distance = coordinateVector.distance();

                    if (distance != 0) {
                        double forceValue = G * body.getMass() * other.getMass() / (distance * distance);
                        Vector2D forceVector = coordinateVector.normalize().multiplyByScalar(forceValue);
                        force.add(forceVector);
                    }
                }
            }
            Vector2D acceleration = force.multiplyByScalar(1 / body.getMass());
            body.setAcceleration(acceleration);
            body.getVelocity().add(acceleration.multiplyByScalar(deltaTime));
            body.getPosition().add(body.getVelocity().multiplyByScalar(deltaTime));
        }

        String jsonBodies = convertBodiesToJson(bodies);
        mqttService.sendBodies(jsonBodies);
    }

    private String convertBodiesToJson(List<Body> bodies) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(bodies);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public void pauseSimulation() {
        running = false;
    }

    public void resumeSimulation() {
        running = true;
    }
}