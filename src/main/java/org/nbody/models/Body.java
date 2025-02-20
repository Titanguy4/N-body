package org.nbody.models;

import lombok.Data;

import java.util.List;

@Data
public class Body {
    private int id;
    private BodyType type;
    private List<Integer[]> position;
    private Double acceleration;
    private Double speed;

}
