package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Coordinate {

    private String name;
    private double x;
    private double y;

    private Coordinate(){}

    public Coordinate (double x, double y) {

        this.x = x < 0 ? 0 : x;
        this.y = y < 0 ? 0 : y;
    }

    public static Coordinate create (double x, double y) {

        if (x < 0 || y < 0) return null;
        return new Coordinate(x, y);
    }

    public double diffX(Coordinate c) {

        return this.x - c.x;
    }

    public double diffY(Coordinate c) {

        return this.y - c.y;
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
