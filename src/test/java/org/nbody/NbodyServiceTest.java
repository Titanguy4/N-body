package org.nbody;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nbody.models.Body;
import org.nbody.models.BodyType;
import org.nbody.models.Vector2D;
import org.nbody.services.MQTTService;
import org.nbody.services.NBodyService;

import static org.junit.jupiter.api.Assertions.*;


public class NbodyServiceTest {

    @Mock
    MQTTService mqttService;

    private NBodyService nBodyService;

    @BeforeEach
    public void setUp(){
        nBodyService = new NBodyService(mqttService);
        nBodyService.setThreeBodies();
    }

    @Test
    public void addBody(){
        Body body = new Body(1, new BodyType("PLANET"), 100.0, new Vector2D(20, 0), new Vector2D(10, 0), new Vector2D(0, 0));
        nBodyService.addBody(body);
        assertEquals(100.0, nBodyService.getAllBodies().get(3).getMass(), "AddBody failed");
    }

    @Test
    public void addNullBody(){
        assertThrows(IllegalArgumentException.class, () -> nBodyService.addBody(null), "addNullBody failed");
    }

    @Test
    public void deleteBody(){
        nBodyService.deleteBody(1);
        assertEquals(2, nBodyService.getAllBodies().size(), "deleteBody failed");
    }

    @Test
    public void deleteBodyWithNegativeIndex(){
        nBodyService.deleteBody(-1);
        assertEquals(3, nBodyService.getAllBodies().size(), "deleteBodyWithNegativeIndex failed");
    }

    @Test
    public void deleteBodyWithOutOfRange(){
        nBodyService.deleteBody(nBodyService.getAllBodies().size() + 100);
        assertEquals(3, nBodyService.getAllBodies().size(), "deleteBodyWithOutOfRange failed");
    }

    @Test
    void deleteAllBodies() {
        nBodyService.deleteAllBodies();
        assertTrue(nBodyService.getAllBodies().isEmpty(), "No delete all bodies");
    }
}
