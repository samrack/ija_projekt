package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Coordinate {

    private String name;
    private double x;
    private double y;

    private Coordinate() {
    }

    public Coordinate(double x, double y) {

        this.x = x < 0 ? 0 : x;
        this.y = y < 0 ? 0 : y;
    }

    
    /** 
     * @param x
     * @param y
     * @return Coordinate
     */
    public static Coordinate create(double x, double y) {

        if (x < 0 || y < 0)
            return null;
        return new Coordinate(x, y);
    }

    
    /** 
     * Calculate difference between coordinate on X axis
     * 
     * @param c
     * @return double
     */
    public double diffX(Coordinate c) {

        return this.x - c.getX();
    }

    
    /** 
     * Calculate difference between coordinate on Y axis
     * 
     * @param c
     * @return double
     */
    public double diffY(Coordinate c) {

        return this.y - c.getY();
    }

    
    /** 
     * @return double
     */
    public double getX() {

        return x;
    }

    
    /** 
     * @return double
     */
    public double getY() {

        return y;
    }

    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }

    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "Coordinate{" + "x=" + x + ", y=" + y + '}';
    }
}
