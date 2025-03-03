package org.nbody.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.nbody.models.Body;
import org.nbody.services.NBodyService;
import org.nbody.services.SimulationService;

import java.io.IOException;

@ApplicationScoped
public class NBodyController {

    private final NBodyService nbodyService;
    private final SimulationService simulationService;

    private final ObjectMapper objectMapper;

    public NBodyController(NBodyService nbodyService, SimulationService simulationService, ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.nbodyService = nbodyService;
        this.simulationService = simulationService;
    }

    @Incoming("simulation-event-add")
    public void addBody(String bodyJson){
        try{
            Body body = objectMapper.readValue(bodyJson, Body.class);
            nbodyService.addBody(body);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Incoming("simulation-event-move")
    public void moveBody(String bodyJson){
        try{
            Body body = objectMapper.readValue(bodyJson, Body.class);
            nbodyService.changeBody(body);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Incoming("simulation-event-delete")
    public void deleteBody(String bodyJson){
        try{
            Body body = objectMapper.readValue(bodyJson, Body.class);
            nbodyService.deleteBody(body.getId());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Incoming("simulation-event-pause")
    public void pauseSimulation(String message){
        if(message.equals("pause")){
            simulationService.pauseSimulation();
        } else if (message.equals("resume")){
            simulationService.resumeSimulation();
        }
    }

    @Incoming("simulation-preset")
    public void presetSimulation(String message){
        switch (message) {
            case "two-bodies" -> nbodyService.setTwoBodies();
            case "three-bodies" -> nbodyService.setThreeBodies();
            default -> nbodyService.setSystemSolar();
        }
    }


}
