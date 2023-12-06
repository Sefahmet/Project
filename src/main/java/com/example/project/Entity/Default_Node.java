package com.example.project.Entity;



public class Default_Node {

        private Integer id;
        private Long osmid;
        private Double east;
        private Double north;
        private Integer streetCount;
        private Double elevation;

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
        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Long getOsmid() {
                return osmid;
        }

        public void setOsmid(Long osmid) {
                this.osmid = osmid;
        }

        public Double getEast() {
                return east;
        }

        public void setEast(Double east) {
                this.east = east;
        }

        public Double getNorth() {
                return north;
        }

        public void setNorth(Double north) {
                this.north = north;
        }

        public Integer getStreetCount() {
                return streetCount;
        }

        public void setStreetCount(Integer streetCount) {
                this.streetCount = streetCount;
        }

        public Double getElevation() {
                return elevation;
        }

        public void setElevation(Double elevation) {
                this.elevation = elevation;
        }

}
