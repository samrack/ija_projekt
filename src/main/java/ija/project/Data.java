package ija.project;

import java.util.List;

public class Data {
    private List<Coordinate> coordinates;
    private Vehicle vehicle;
    //    private List<Vehicle> vehicles;

    private Data(){}

    public Data(List<Coordinate> coordinates, Vehicle vehicle) {
        this.coordinates = coordinates;
        this.vehicle = vehicle;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public String toString() {
        return "Data{" +
                "coordinates=" + coordinates +
                ", vehicle=" + vehicle +
                '}';
    }
}
