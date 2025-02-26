package org.nbody.models;

import lombok.Data;

import java.util.List;

@Data
public class Body {
    private int id;
    private BodyType type;
    private double mass;
    private Vector2D position;
    private Vector2D speed;
    private Vector2D acceleration;
}
