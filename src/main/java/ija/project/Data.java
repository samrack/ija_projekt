package ija.project;

import java.util.List;

public class Data {
    private List<Coordinate> coordinates;
    private List<Vehicle> vehicles;
    private List<Stop> stops;
    private List<Street> streets;
    private List<Line> lines;

    private Data(){
    }

//    public Data(List<Coordinate> coordinates, List<Vehicle> vehicles, List<Stop> stops, List<Street> streets) {
//        this.coordinates = coordinates;
//        this.vehicles = vehicles;
//        this.stops = stops;
//        this.streets = streets;
//    }

    public Data(List<Coordinate> coordinates, List<Vehicle> vehicles, List<Stop> stops, List<Street> streets,List<Line> lines) {
        this.coordinates = coordinates;
        this.vehicles = vehicles;
        this.stops = stops;
        this.streets = streets;
        this.lines = lines;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return "Data{" +
                "coordinates=" + coordinates +
                ", vehicles=" + vehicles +
                ", stops=" + stops +
                ", streets" + streets +
                ", lines" + lines +
                '}';
    }
}
