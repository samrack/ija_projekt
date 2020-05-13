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

    public List<Stop> getStopsList(Stop stop){
        return stopsList;
    }

    public List<LocalTime> getTimesList(LocalTime time){
        return timesList;
    }

    public void setStopList(List<Stop> stopList){
        this.stopsList = stopList; 
    }

    public void setTimesList(List<LocalTime> timeList ){
        this.timesList = timeList;
    }

   public void printOutSchedule(){
       int numberOfStops = stopsList.size();
       System.out.println(numberOfStops);
       for(int i = 0; i < numberOfStops; i++){
           System.out.println("BUS " + bus.getBusId() + " will be on stop " + stopsList.get(i).getStopId() + " at time : " + String.format("%s",timesList.get(i)));
       }
   }
    
   /* KAZDA LINKA MA STARTTIMES :  00 06 12 18 24 20 36 42 48 54  */ 


}