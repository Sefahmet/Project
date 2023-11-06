package com.example.project.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
public class Rectangle {
    @Getter @Setter private double xmin;
    @Getter @Setter private double ymin;
    @Getter @Setter private double xmax;
    @Getter @Setter private double ymax;
}
