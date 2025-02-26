package org.nbody.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Vector2D {
    private double x;
    private double y;

    public void add(Vector2D otherVector){
        this.x += otherVector.x;
        this.y += otherVector.y;
    }

    public void multiplyByScalar(double scalar){
        this.x *= scalar;
        this.y *= scalar;
    }
}
