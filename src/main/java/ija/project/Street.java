package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import jdk.nashorn.internal.ir.ReturnNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** 
 * Represents a street on a map
* 
* @author Samuel Stuchly xstuch06
* @author Samuel Spisak xspisa02
*/
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "streetName", scope = Street.class)
public class Street implements Drawable {
    

    static final public int DEFAULT_SPEED = 5;
    static final public int SLOWED_SPEED = 1;

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



//    public boolean isCoordOnStreet(Coordinate coord) {
//
//        BigDecimal bigCoordX = new BigDecimal(String.valueOf(coord.getX()));
//        BigDecimal bigCoordY = new BigDecimal(String.valueOf(coord.getY()));
//        int intCoordX = bigCoordX.intValue();
//        int intCoordY = bigCoordY.intValue();
//
//        double coordX = intCoordX;
//        double coordY = intCoordY;
//
//        double dxc = coordX - begin.getX();
//        double dyc = coordY - begin.getY();
//
//        double dxl = end.getX() - begin.getX();
//        double dyl = end.getY() - begin.getY();
//
//        if (dxc * dyl - dyc * dxl == 0) {
//
//            if (Math.abs(dxl) >= Math.abs(dyl)) {
//                return dxl > 0 ?
//                        begin.getX() <= coordX && coordX <= end.getX() :
//                        end.getX() <= coordX && coordX <= begin.getX();
//            } else {
//                return dyl > 0 ?
//                        begin.getY() <= coordY && coordY <= end.getY() :
//                        end.getY() <= coordY && coordY <= begin.getY();
//            }
//
//        } else return false;
//    }

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

        if(dxc * dyl - dyc * dxl == 0) {

            if(Math.abs(dxl) >= Math.abs(dyl)) {
                return dxl > 0 ?
                        begin.getX() <= coord.getX() && coord.getX() <= end.getX() :
                        end.getX() <= coord.getX() && coord.getX() <= begin.getX();
            } else {
                return dyl > 0 ?
                        begin.getY() <= coord.getY() && coord.getY() <= end.getY() :
                        end.getY() <= coord.getY() && coord.getY() <= begin.getY();
            }

        } else return false;
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

    /**
     * @param speed
     */
    public void setStreetSpeed(int speed) {
        this.speed = speed;
    }

}