package com.example.project.Controller;


import com.example.project.Service.WeightedShortestPath;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/Effortless")
public class WeightedShortestPathController {
    /*private final Class<WeightedShortestPath> weightedShortestPath = WeightedShortestPath.class;*/


    @GetMapping("/Coordinates")
    public ResponseEntity<List<Coordinate>> shortestPathGetter(@RequestParam double lat1,
                                                               @RequestParam double lon1,
                                                               @RequestParam double lat2,
                                                               @RequestParam double lon2,
                                                               @RequestParam double wLength,
                                                               @RequestParam double wSlope,
                                                               @RequestParam double wMaxSpeed,
                                                               @RequestParam double wTurnLeft,
                                                               @RequestParam double wGreenary) throws IOException {

        Coordinate p1 = new Coordinate(lat1, lon1);
        Coordinate p2 = new Coordinate(lat2, lon2);
        List<Coordinate> coordinates = WeightedShortestPath.shortestPathService(lat1, lon1, lat2, lon2,
                wLength, wSlope, wMaxSpeed, wTurnLeft,wGreenary);


        coordinates.add(0,p1);
        coordinates.add(p2);



        if (coordinates!=null){
            return new ResponseEntity(coordinates, HttpStatus.OK);
        }else{
            return new ResponseEntity("CoordinatesCouldntFind", HttpStatus.BAD_REQUEST);
        }

        }
/*
    @GetMapping("/CoordinatesTest")
    public ResponseEntity<List<Coordinate>> shortestPathGetterTest() {
        Double lat1 = 7.1078218;
        Double lon1 = 50.7314816;
        Double lat2 =  7.1002560;
        Double lon2 = 50.7271996;
        Coordinate p1 = new Coordinate(lat1, lon1);
        Coordinate p2 = new Coordinate(lat2, lon2);
        List<Coordinate> coordinates = WeightedShortestPath.shortestPathService(lat1, lon1, lat2, lon2,
                0, 0, 0, 0,0);


        coordinates.add(0,p1);
        coordinates.add(p2);



        if (coordinates!=null){
            return new ResponseEntity(coordinates, HttpStatus.OK);
        }else{
            return new ResponseEntity("CoordinatesCouldntFind", HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/osmids")
    public ResponseEntity<List<String>> osmidsGetter(@Valid @RequestParam double lat1,
                                                               @Valid @RequestParam double lon1,
                                                               @Valid @RequestParam double lat2,
                                                               @Valid @RequestParam double lon2,
                                                               @Valid @RequestParam double wLength,
                                                               @Valid @RequestParam double wSlope,
                                                               @Valid @RequestParam double wMaxSpeed,
                                                               @Valid @RequestParam double wTurnLeft,
                                                               @Valid @RequestParam double wGreenary) throws IOException {

        Coordinate p1 = new Coordinate(lat1, lon1);
        Coordinate p2 = new Coordinate(lat2, lon2);
        List<String> coordinates = WeightedShortestPath.shortestPathServiceOSMID(lat1, lon1, lat2, lon2,
                wLength, wSlope, wMaxSpeed, wTurnLeft,wGreenary);




        if (coordinates!=null){
            return new ResponseEntity(coordinates, HttpStatus.OK);
        }else{
            return new ResponseEntity("CoordinatesCouldn'tFind", HttpStatus.BAD_REQUEST);
        }

    }*/
    }
