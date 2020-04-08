package ija.project;

import java.util.Objects;

public class Coordinate {

    private double X;
    private double Y;

    public Coordinate (double X, double Y) {

        this.X = X < 0 ? 0 : X;
        this.Y = Y < 0 ? 0 : Y;
    }

    public static Coordinate create (double X, double Y) {

        if (X < 0 || Y < 0) return null;
        return new Coordinate(X, Y);
    }

    public double diffX(Coordinate c) {

        return this.X - c.X;
    }

    public double diffY(Coordinate c) {

        return this.Y - c.Y;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.X, X) == 0 &&
                Double.compare(that.Y, Y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }
}
