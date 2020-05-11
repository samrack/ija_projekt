package ija.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;



public class Line {
    private String id;
    private Path path;
    private List<Stop> stopsList;

// maybe nanic 
    private List<Street> streetsList;


    public Line(String id,Path path, List<Stop> stopsList) {
        this.id = id;
        this.path = path;
        this.stopsList = stopsList;
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


    public Street getStreetByCoord(Coordinate coord) throws Exception {
        for (Street street : streetsList) {
            if (street.isCoordOnStreet(coord)){
                return street;
            }
        }
        throw new Exception("Coord not on any street in Line !");
    }

    public Stop getStopFromCoords(Coordinate coords){
        for(Stop stop : stopsList){
            if (stop.getCoordinate().equals(coords)){
                return stop;
            }
        }
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