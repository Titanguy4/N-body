package org.nbody.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2D {
    private double x;
    private double y;

    public void add(Vector2D otherVector){
        this.x += otherVector.x;
        this.y += otherVector.y;
    }

    public Vector2D multiplyByScalar(double scalar){
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2D subtract(Vector2D otherVector){
        return new Vector2D(this.x - otherVector.x, this.y - otherVector.y);
    }

    public double distance(){
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize(){
        double distance = this.distance();
        return distance == 0 ? new Vector2D(0, 0) : new Vector2D(x/distance, y/distance);
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

}
