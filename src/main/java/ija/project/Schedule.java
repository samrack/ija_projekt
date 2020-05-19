package ija.project;

import java.time.LocalTime;
import java.util.List;


/** 
 * Represents schedule for a one certain vehicle    
* 
* @author Samuel Stuchly xstuch06
* @author Samuel Spisak xspisa02
*/

public class Schedule {
    private Vehicle bus;
    private List<Stop> stopsList;
    private List<LocalTime> timesList;

    public Schedule(Vehicle bus) {
        this.bus = bus;
    }

    /**
     * @return List<Stop>
     */
    public List<Stop> getStopsList() {
        return stopsList;
    }

    /**
     * @return List<LocalTime>
     */
    public List<LocalTime> getTimesList() {
        return timesList;
    }

    /**
     * @param stopList
     */
    public void setStopList(List<Stop> stopList) {
        this.stopsList = stopList;
    }

    /**
     * @param timeList
     */
    public void setTimesList(List<LocalTime> timeList) {
        this.timesList = timeList;
    }

}