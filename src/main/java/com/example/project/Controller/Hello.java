package com.example.project.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Hello {
    @GetMapping("/")
    public ResponseEntity<String> helloVisiters(){
        String response = "This API is created for the Master Project.<br>" +
                "We produce an effortless path for a cyclist according to your preferences.<br>"+
                "The front-end side will be here soon.<br>"+
                "You might be seeing a set of meaningless coordinates right now.<br>"+
                "But we expect this to become a useful app soon.<br>"+
                "If you want to see some examples, you can visit:<br>" +
                "<a href=\"http://localhost:8080/masterproject/Effortless/Coordinates?lat1=50.71821596667237&lon1=7.122297496060757&lat2=50.711788588547996&lon2=7.137928122501666&wLength=1&wSlope=1&wMaxSpeed=1&wTurnLeft=1&wGreenary=1\">this address</a> and also you can change weights and coordinates. Make sure the coordinates must be in Bonn.";

        return new ResponseEntity(response, HttpStatus.OK);
    }

}
