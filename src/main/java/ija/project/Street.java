package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.*;

/** 
 * Represents a street on a map
* 
* @author Samuel Stuchly xstuch06
* @author Samuel Spisak xspisa02
*/
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "streetName", scope = Street.class)
public class Street implements Drawable {
    

    static final public int DEFAULT_SPEED = 5;

    private String streetName;

    private Coordinate begin;
    private Coordinate end;
    private List<Stop> stops = new ArrayList<>();

    private int speed = DEFAULT_SPEED;

    private Street() {
    }

    public Street(String name, List<Stop> stops, Coordinate begin, Coordinate end) {
        this.streetName = name;
        this.begin = begin;
        this.end = end;
        this.stops.addAll(stops);

    }

    /**
     * @return String
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * @return List<Stop>
     */
    public List<Stop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /**
     * @return Coordinate
     */
    public Coordinate getBegin() {
        return begin;
    }

    /**
     * @return Coordinate
     */
    public Coordinate getEnd() {
        return end;
    }

    /**
     * @return int
     */
    @JsonIgnore
    public int getStreetSpeed() {
        return speed;
    }

    /**
     * @param speed
     */
    public void setStreetSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Check if coordinate lies on the street
     *
     * @param coord
     * @return boolean
     */

    public boolean isCoordOnStreet(Coordinate coord) {
        double dxc = coord.getX() - begin.getX();
        double dyc = coord.getY() - begin.getY();

        double dxl = end.getX() - begin.getX();
        double dyl = end.getY() - begin.getY();

        if(Math.abs(dxc * dyl - dyc * dxl) > 1) {

            return false;

        } else {

            if(Math.abs(dxl) >= Math.abs(dyl)) {
                return dxl > 0 ?
                        begin.getX() <= coord.getX() && coord.getX() <= end.getX() :
                        end.getX() <= coord.getX() && coord.getX() <= begin.getX();
            } else {
                return dyl > 0 ?
                        begin.getY() <= coord.getY() && coord.getY() <= end.getY() :
                        end.getY() <= coord.getY() && coord.getY() <= begin.getY();
            }
        }
    }

    /**
     * @return List<Shape>
     */
    @JsonIgnore
    @Override
    public List<Shape> getGUI() {

        double minX = Math.min(begin.getX(), end.getX());
        double minY = Math.min(begin.getY(), end.getY());
        double maxX = Math.max(begin.getX(), end.getX());
        double maxY = Math.max(begin.getY(), end.getY());

        return Arrays.asList(

                new Text(minX + (Math.abs(minX - maxX)/2), minY + (Math.abs(minY - maxY)/2), streetName),
                new Line(this.getBegin().getX(), this.getBegin().getY(), this.getEnd().getX(), this.getEnd().getY()));
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Street{" + "streetName='" + streetName + "_CurrentSpeed=" + speed +'}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return streetName.equals(street.streetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetName);
    }
}