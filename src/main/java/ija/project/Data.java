package ija.project;

import java.util.List;

/**
 * Stores loaded data for map setup
 * 
 * @author Samuel Stuchly xstuch06
 * @author Samuel Spisak xspisa02
 */

public class Data {
    private List<Coordinate> coordinates;
    private List<Vehicle> vehicles;
    private List<Stop> stops;
    private List<Street> streets;
    private List<Line> lines;

    private Data() {
    }

    public Data(List<Coordinate> coordinates, List<Vehicle> vehicles, List<Stop> stops, List<Street> streets,
            List<Line> lines) {
        this.coordinates = coordinates;
        this.vehicles = vehicles;
        this.stops = stops;
        this.streets = streets;
        this.lines = lines;
    }

    /**
     * @return List<Coordinate>
     */
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    /**
     * @return List<Vehicle>
     */
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * @return List<Street>
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * @return List<Stop>
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * @return List<Line>
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Data{" + "coordinates=" + coordinates + ", vehicles=" + vehicles + ", stops=" + stops + ", streets"
                + streets + ", lines" + lines + '}';
    }
}
