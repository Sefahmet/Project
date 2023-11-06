package com.example.project.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor

public class Point {
    @Getter @Setter double x;
    @Getter @Setter double y;
    @Getter @Setter double h;
}
