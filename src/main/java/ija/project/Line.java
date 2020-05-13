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




@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "yamlId", scope = Line.class)
public class Line {
    private String yamlId;
    private String id;
    private Path path;
    private List<Stop> stopsList;
    private List<Street> streetsList;
    private Color lineColor;

    private Line(){}

    public Line(String id,Path path, List<Stop> stopsList, List<Street> streetList) {
        this.id = id;
        this.path = path;
        this.stopsList = stopsList;
        this.streetsList = streetList;

    }


    public void setLineColor(Color color){
        this.lineColor = color;
    }

    public Color getLineColor(){
        return lineColor;
    }

    public String getYamlId(){
        return yamlId;
    }

    public String getId(){
        return id;
    }

    public Path getPath(){
        return path;
    }

    public List<Stop> getStopsList(){
        return stopsList;
    }

    public List<Street> getStreetsList() {
        return streetsList;
    }

    public Street getStreetByCoord(Coordinate coord) throws Exception {
        //System.out.println(streetsList.size());
        for (Street street : streetsList) {
            //System.out.println(street);
        
            if (street.isCoordOnStreet(coord)){
                //System.out.println("FOUND STREET ");
                return street;
            }
        }
        System.out.println("UZ BOLI TRI ");
        throw new Exception("Coord not on any street in Line !");
    }

    public Stop getStopFromCoords(Coordinate coords){
        for(Stop stop : stopsList){
            if (stop.getCoordinate().equals(coords)){
                //System.out.println("FOUND STOP ");
                return stop;
            }
        }//System.out.println("getStopfromCoords returned null");
        return null;
    }




    
    // public boolean addStop​(Stop stop) {
    //     if (StopsList.isEmpty()) {
    //         streetsStopsList.add(new SimpleImmutableEntry<>(stop.getStreet(), stop));
    //         return true;
    //     } else {
    //         Street lastStreet = streetsStopsList.get(streetsStopsList.size() - 1).getKey();
    //         if (stop.getStreet().follows​(lastStreet)) {
    //             streetsStopsList.add(new SimpleImmutableEntry<>(stop.getStreet(), stop));
    //             return true;
    //         } else {
    //             return false;
    //         }
    //     }
    // }

    
    // public boolean addStreet​(Street street) {
    //     if (streetsStopsList.isEmpty()){
    //         return false;
    //     }
    //     else {
    //         Street lastStreet = streetsStopsList.get(streetsStopsList.size() - 1).getKey();
    //         if (street.follows​(lastStreet)) {
    //             streetsStopsList.add(new SimpleImmutableEntry<>(street, null));
    //             return true;
    //         } else {
    //             return false;
    //         }
    //     }
    // }



}