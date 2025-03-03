package org.nbody;

import io.quarkus.test.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nbody.models.Body;
import org.nbody.models.BodyType;
import org.nbody.models.Vector2D;
import org.nbody.services.MQTTService;
import org.nbody.services.NBodyService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class NbodyServiceTest {

    @Mock
    MQTTService mqttService;

    private NBodyService nBodyService;

    @BeforeEach
    public void setUp(){
        nBodyService = new NBodyService(mqttService);
    }

    @Test
    public void addBody(){
        Body body = new Body(1, new BodyType("PLANET"), 100.0, new Vector2D(20, 0), new Vector2D(10, 0), new Vector2D(0, 0));
        nBodyService.addBody(body);
        assertEquals(100.0, nBodyService.getAllBodies().get(0).getMass());
    }

    @Test
    public void addNullBody(){
        nBodyService.addBody(null);
        assertThrows(IllegalArgumentException.class, () -> nBodyService.addBody(null));
    }
}
