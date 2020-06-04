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
        System.out.println(coord);
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

// ==========================

    public void updateLine(Path updatedPath, List<Street> updatedStreetList){
        this.path = updatedPath;
        this.streetsList = updatedStreetList;
        System.out.println("UPDATE LINE WAS CALLED");
        System.out.println("LINE NOW is " + updatedStreetList);
    }

        // Posuvaj po celom oboch zoznamoch a postavaj novy zoznam s obchadkov pre vsetky tri path streets a stops a updatni Line. 
        // potom sa zavola update este na vehicle kde sa prenho vsetko updatne a nasrtavy sa tak nova trasa aj schdeule aj itinerar a vsetko. 
        // Treba este nejak pozbierat ale ten novy zoznam , (mozme szbierat podla ulic, podla zastavok, co je asi lepsie. alebo podla suradnic rovno.)
        // Treba urobit button ktory aktualizuje trasu s obchadzkou +  nejaky button ktory zapne zapinanie obchadzky, alebo mozno len natukame nove ulice 
        // Moze byt nahrada ulice jednou alebo viac ulicami. ktore mozu byt zadane aj podla mena napriklad alebo naklikane postupne a ulozene. A ulica sa da pridat
        // len ak navazaje na predchadzajucu v zozname 
        // V tom pripade mozeme mat dve buttons, jednu na vytvaranie obchadzky kedze nevieme pauznut nas timer, a druhy button bude na uz realne uploadnutie obchadzky. 



// ==========================

}