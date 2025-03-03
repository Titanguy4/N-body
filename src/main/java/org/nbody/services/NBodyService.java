package org.nbody.services;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.nbody.models.Body;
import org.nbody.models.BodyType;
import org.nbody.models.Vector2D;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Slf4j
public class NBodyService {

    private final List<Body> bodies = new ArrayList<>();

    private final MQTTService mqttService;

    public NBodyService(MQTTService mqttService){
        this.mqttService = mqttService;
    }

    public void addBody(Body body){
        body.setVelocity(new Vector2D(0, 0));
        body.setAcceleration(new Vector2D(0, 0));
        body.setId(bodies.size());
        bodies.add(body);
    }

    public List<Body> getAllBodies(){
        return bodies;
    }

    public void deleteBody(int index){
        log.info("Delete Body");
        if(index >= 0 && index <= bodies.size()){
            bodies.remove(index - 1);
        }
        log.debug(bodies.toString());
    }

    public void changeBody(Body updatedBody) {
        for(Body body : this.bodies){
            if(body.getId() == updatedBody.getId()){
                body.setType(updatedBody.getType());
                body.setMass(updatedBody.getMass());
                body.setPosition(updatedBody.getPosition());
            }
        }
    }

    public void deleteAllBodies(){
        this.bodies.clear();
    }

    private void publishBodies() {
        String jsonBodies = bodies.toString();
        mqttService.sendBodies(jsonBodies);
    }

    public void setSystemSolar() {
        this.deleteAllBodies();
        bodies.add(new Body(1, new BodyType("PLANET"), 1.989e30, new Vector2D(0, 0), new Vector2D(0, 0), new Vector2D(0, 0)));
        bodies.add(new Body(2, new BodyType("PLANET"), 3.285e23, new Vector2D(57.9e9, 0), new Vector2D(0, 47400), new Vector2D(0, 0)));
        bodies.add(new Body(3, new BodyType("PLANET"), 4.867e24, new Vector2D(108.2e9, 0), new Vector2D(0, 35020), new Vector2D(0, 0)));
        bodies.add(new Body(4, new BodyType("PLANET"), 5.972e24, new Vector2D(149.6e9, 0), new Vector2D(0, 29783), new Vector2D(0, 0)));
        bodies.add(new Body(5, new BodyType("PLANET"), 6.39e23, new Vector2D(227.9e9, 0), new Vector2D(0, 24077), new Vector2D(0, 0)));
        bodies.add(new Body(6, new BodyType("PLANET"), 1.898e27, new Vector2D(778.5e9, 0), new Vector2D(0, 13070), new Vector2D(0, 0)));
        bodies.add(new Body(7, new BodyType("PLANET"), 5.683e26, new Vector2D(1433.5e9, 0), new Vector2D(0, 9690), new Vector2D(0, 0)));
        bodies.add(new Body(8, new BodyType("PLANET"), 8.681e25, new Vector2D(2872.5e9, 0), new Vector2D(0, 6810), new Vector2D(0, 0)));
        bodies.add(new Body(9, new BodyType("PLANET"), 1.024e26, new Vector2D(4495.1e9, 0), new Vector2D(0, 5430), new Vector2D(0, 0)));
    }

    public void setTwoBodies(){
        this.deleteAllBodies();
        bodies.add(new Body(1, new BodyType("PLANET"), 200000000, new Vector2D(0, 0), new Vector2D(0, 0), new Vector2D(0, 0)));
        bodies.add(new Body(2, new BodyType("PLANET"), 200000000, new Vector2D(1, 0), new Vector2D(0, 0), new Vector2D(0, 0)));
    }

    public void setThreeBodies(){
        this.deleteAllBodies();
        bodies.add(new Body(1, new BodyType("PLANET"), 200000000, new Vector2D(0, 0), new Vector2D(0, 0), new Vector2D(0, 0)));
        bodies.add(new Body(2, new BodyType("PLANET"), 200000000, new Vector2D(2, 0), new Vector2D(0, 0), new Vector2D(0, 0)));
        bodies.add(new Body(3, new BodyType("PLANET"), 200000000, new Vector2D(-2, 2), new Vector2D(0, 0), new Vector2D(0, 0)));
    }
}
