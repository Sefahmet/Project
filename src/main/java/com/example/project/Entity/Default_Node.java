package com.example.project.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Default_Node {

        @Getter @Setter private Integer id;
        @Getter @Setter private Long osmid;
        @Getter @Setter private Double east;
        @Getter @Setter private Double north;
        @Getter @Setter private Integer streetCount;
        @Getter @Setter private Double elevation;

        public Default_Node(Integer id,Long osmid,Double east,Double north,Integer streetCount){
                this.id  =id;
                this.osmid  =osmid;
                this.east  =east;
                this.north  =north;
                this.streetCount  =streetCount;
        }
        @Override
        public String toString() {
            return id + " "+osmid+" "+ east+ " "+ north+ " "+ streetCount;
        }


}
