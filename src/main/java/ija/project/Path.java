package ija.project;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Path {

    private List<Coordinate> path;

    private Path(){}

    public Path(List<Coordinate> path) { 
        this.path = path;
    }

    /**
     * @return distance between two coordinates
     **/
    private double getCoordinatesDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }

    /**
     * @return coordinate on new position based on distance driven
     * **/
    public Coordinate getNextPosition(double distance) {
        double currentLength = 0;
        double nextLength = 0;

        Coordinate a = null;
        Coordinate b = null;

        for(int i = 0; i < path.size() - 1; i++) {
            a = path.get(i);
            b = path.get(i + 1);
            nextLength = getCoordinatesDistance(a, b);

            if(currentLength + nextLength >= distance) {
                break;
            }
            currentLength += nextLength;
        }
        if(a == null || b == null) return null;

        double completed = (distance - currentLength) / nextLength;
        return Coordinate.create(a.getX() + (b.getX() - a.getX()) * completed, a.getY() + (b.getY() - a.getY()) * completed);
    }

    public List<Coordinate> getPath() {
        return path;
    }

    /**
     * @return total length of the path
     * **/
    @JsonIgnore
    public double getPathLength () {
        double length = 0;

        for(int i = 0; i < path.size() - 1; i++) {
            length += getCoordinatesDistance(path.get(i), path.get(i+1));
        }
        return length;
    }

    /*Returns last coordinate of path a.k.a last Stop */
    public Coordinate getlastCoordinateOfPath(){
        return path.get(path.size() - 1);
    }
}
