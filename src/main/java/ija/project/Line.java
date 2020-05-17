package ija.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javafx.scene.paint.Color;
/** 
 * Represents Line, which defines stops and streets that vehicle goes through    
* 
* @author Samuel Stuchly xstuch06
* @author Samuel Spisak xspisa02
*/
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "yamlId", scope = Line.class)
public class Line {
    private String yamlId;
    private String id;
    private Path path;
    private List<Stop> stopsList;
    private List<Street> streetsList;
    private Color lineColor;

    private Line() {
    }

    public Line(String id, Path path, List<Stop> stopsList, List<Street> streetList) {
        this.id = id;
        this.path = path;
        this.stopsList = stopsList;
        this.streetsList = streetList;

    }

    /**
     * @param color
     */
    public void setLineColor(Color color) {
        this.lineColor = color;
    }

    /**
     * @return Color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * @return String
     */
    public String getYamlId() {
        return yamlId;
    }

    /**
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * @return Path
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return List<Stop>
     */
    public List<Stop> getStopsList() {
        return stopsList;
    }

    /**
     * @return List<Street>
     */
    public List<Street> getStreetsList() {
        return streetsList;
    }

    /**
     * Get street the coordinate is on. Street is always a straight line
     * 
     * @param coord
     * @return Street
     * @throws Exception
     */
    public Street getStreetByCoord(Coordinate coord) throws Exception {

        for (Street street : streetsList) {

            if (street.isCoordOnStreet(coord)) {

                return street;
            }
        }
        throw new Exception("Coord not on any street in Line !");
    }

    /**
     * Check if stop lies on the given coordinates 
     * 
     * @param coords
     * @return Stop if found, else null
     */
    public Stop getStopFromCoords(Coordinate coords) {
        for (Stop stop : stopsList) {
            if (stop.getCoordinate().equals(coords)) {

                return stop;
            }
        }
        return null;
    }

}