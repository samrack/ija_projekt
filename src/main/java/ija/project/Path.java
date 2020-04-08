package ija.project;

import java.util.List;

public class Path {
    private List<Coordinate> path;

    public Path(List<Coordinate> path) {
        this.path = path;
    }

    private double getCoordinatesDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }

    public Coordinate getCoordinateByDistance(double distance) {
        double length = 0;
        double driveLength = 0;

        Coordinate a = null;
        Coordinate b = null;

        for(int i = 0; i < path.size() - 1; i++) {
            a = path.get(i);
            b = path.get(i + 1);
            driveLength = getCoordinatesDistance(a, b);


            if(length + driveLength >= distance) {
                break;
            }
            length += driveLength;
        }
        if(a == null || b == null) return null;

        double completed = (distance - length) / driveLength;
        return Coordinate.create(a.getX() + (b.getX() - a.getX()) * completed, a.getY() + (b.getY() - a.getY()) * completed);
    }

    public double getPathLength () {
        double length = 0;

        for(int i = 0; i < path.size() - 1; i++) {
            length += getCoordinatesDistance(path.get(i), path.get(i+1));
        }
        return length;
    }
}
