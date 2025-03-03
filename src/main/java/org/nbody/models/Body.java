package org.nbody.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class Body {
    private int id;
    private BodyType type;
    private double mass;
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;
}
