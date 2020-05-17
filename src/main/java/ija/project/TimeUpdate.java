package ija.project;

import java.time.LocalTime;

/** 
 * Interface for classes that are being updated based on timer    
* 
* @author Samuel Stuchly xstuch06
* @author Samuel Spisak xspisa02
*/
public interface TimeUpdate {
    void update(LocalTime time);

    void newTime(LocalTime time);

//    void reloadSchedule(LocalTime time);
}
