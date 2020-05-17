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

    // Function for testing to see the schedule
    public void printOutSchedule() {
        int numberOfStops = stopsList.size();
        System.out.println(numberOfStops);
        for (int i = 0; i < numberOfStops; i++) {
            System.out.println("BUS " + bus.getBusId() + " will be on stop " + stopsList.get(i).getStopId()
                    + " at time : " + String.format("%s", timesList.get(i)));
        }
    }

    /* KAZDA LINKA MA STARTTIMES : 00 06 12 18 24 20 36 42 48 54 */

}