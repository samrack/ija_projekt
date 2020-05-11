package ija.project;

import java.time.LocalTime;
import java.util.List;

public class Schedule {
   private  Vehicle bus;
   private List<Stop> stopsList;
   private List<LocalTime> timesList;

    public Schedule(Vehicle bus){
        this.bus = bus;
    }

    public void updateStopsList(Stop stop){
        stopsList.add(stop);
    }

    public void updateTimesList(LocalTime time){
        timesList.add(time);
    }

    public List<Stop> getStopsList(Stop stop){
        return stopsList;
    }

    public List<LocalTime> getTimesList(LocalTime time){
        return timesList;
    }

    
}