package org.nbody.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class MQTTService {

    @Channel("simulation")
    Emitter<String> bodyEmitter;

    public void sendBodies(String jsonBodies){
        bodyEmitter.send(jsonBodies);
    }
}
