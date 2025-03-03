package org.nbody;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nbody.models.Body;
import org.nbody.models.BodyType;
import org.nbody.models.Vector2D;
import org.nbody.services.MQTTService;
import org.nbody.services.NBodyService;
import org.nbody.services.SimulationService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationTest {

    @Mock
    private NBodyService nBodyService;

    @Mock
    private MQTTService mqttService;

    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        simulationService = new SimulationService(nBodyService, mqttService);
    }

    @Test
    void updateSimulationWithDifferentBodies() {
        Body body1 = new Body(1, new BodyType("PLANET"), 100.0, new Vector2D(20, 0), new Vector2D(10, 0), new Vector2D(0, 0));
        Body body2 = new Body(2, new BodyType("PLANET"), 50.0, new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(0, 0));
        List<Body> bodies = Arrays.asList(body1, body2);
        when(nBodyService.getAllBodies()).thenReturn(bodies);

        simulationService.updateSimulation();

        double epsilon = 1e-2;
        assertAll("positions",
            () -> assertEquals(29.99, bodies.get(0).getPosition().getX(), epsilon),
            () -> assertEquals(0, bodies.get(0).getPosition().getY(), epsilon),
            () -> assertEquals(10.00, bodies.get(1).getPosition().getX(), epsilon),
            () -> assertEquals(0, bodies.get(1).getPosition().getY(), epsilon)
        );

        assertAll("velocities",
            () -> assertEquals(9.99, bodies.get(0).getVelocity().getX(), epsilon),
            () -> assertEquals(0, bodies.get(0).getVelocity().getY(), epsilon),
            () -> assertEquals(10.00, bodies.get(1).getVelocity().getX(), epsilon),
            () -> assertEquals(0, bodies.get(1).getVelocity().getY(), epsilon)
        );

        assertAll("accelerations",
            () -> assertEquals(0, bodies.get(0).getAcceleration().getX(), epsilon),
            () -> assertEquals(0, bodies.get(0).getAcceleration().getY(), epsilon),
            () -> assertEquals(0, bodies.get(1).getAcceleration().getX(), epsilon),
            () -> assertEquals(0, bodies.get(1).getAcceleration().getY(), epsilon)
        );

        verify(mqttService, times(1)).sendBodies(anyString());
    }

    @Test
    void updateSimulationBodiesSamePosition(){
        Body body1 = new Body(1, new BodyType("PLANET"), 100.0, new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(0, 0));
        Body body2 = new Body(2, new BodyType("PLANET"), 50.0, new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(0, 0));
        List<Body> bodies = Arrays.asList(body1, body2);
        when(nBodyService.getAllBodies()).thenReturn(bodies);

        simulationService.updateSimulation();

        double epsilon = 1e-12;
        assertAll("positions",
            () -> assertEquals(10.00, bodies.get(0).getPosition().getX(), epsilon),
            () -> assertEquals(0, bodies.get(0).getPosition().getY(), epsilon),
            () -> assertEquals(10.000000000066, bodies.get(1).getPosition().getX(), epsilon),
            () -> assertEquals(0, bodies.get(1).getPosition().getY(), epsilon)
        );

        assertAll("velocities",
            () -> assertEquals(10.00, bodies.get(0).getVelocity().getX(), epsilon),
            () -> assertEquals(0, bodies.get(0).getVelocity().getY(), epsilon),
            () -> assertEquals(10.000000000066, bodies.get(1).getVelocity().getX(), epsilon),
            () -> assertEquals(0, bodies.get(1).getVelocity().getY(), epsilon)
        );

        assertAll("accelerations",
            () -> assertEquals(0, bodies.get(0).getAcceleration().getX(), epsilon),
            () -> assertEquals(0, bodies.get(0).getAcceleration().getY(), epsilon),
            () -> assertEquals(6.6743E-11, bodies.get(1).getAcceleration().getX(), epsilon),
            () -> assertEquals(0, bodies.get(1).getAcceleration().getY(), epsilon)
        );

        verify(mqttService, times(1)).sendBodies(anyString());
    }
}
