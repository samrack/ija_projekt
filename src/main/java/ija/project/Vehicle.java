package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "busId", scope = Vehicle.class)
// @JsonDeserialize(converter = Vehicle.CallConstructor.class)
public class Vehicle implements Drawable, TimeUpdate {
    private String busId;
    private Line line;
    @JsonIgnore
    private Coordinate position;
    // private double speed;
    @JsonIgnore
    private double distance = 0;
    @JsonIgnore
    private Schedule schedule;

    @JsonIgnore
    private LocalTime startTime;
    @JsonIgnore
    private int startingMinute;

    @JsonIgnore
    private Coordinate startPosition;
    @JsonIgnore
    private Path path;

    @JsonIgnore
    private boolean inBetweenRounds = true;

    // @JsonIgnore
    // private boolean firstRound = true;

    @JsonIgnore
    private int timeBetweenRounds = 60 * 5; // 5 minutes
    @JsonIgnore
    private int secondsPassed;
    @JsonIgnore
    private long oneRideLength; // in seconds

    @JsonIgnore
    private List<Shape> gui;

    private Vehicle() {
    }

    public Vehicle(String busId, Line line, int startingMinute) {
        this.busId = busId;
        this.line = line;
        this.path = line.getPath();
        this.schedule = new Schedule(this);
        this.startPosition = path.getPath().get(0);
        this.position = startPosition;
        this.startingMinute = startingMinute;
        fillSchedule(composeStartignTime());
        setGui();

    }

    private LocalTime composeStartignTime() {
        return LocalTime.of(LocalTime.now().getHour(), startingMinute, 0);
    }

    /*
     * TODO : 
     * timer - osetrit time set pre buses ktore su na prelome hodiny
     * 
     * 
     */


    @Override
     public void reloadSchedule(LocalTime time) {
         fillSchedule(time);         
     }

    // redraws all vehicles to the place where they should be
    @Override
    public void newTime(LocalTime time) {
        System.out.println("ATTENTION PROGRAMMER NEWTIME HAS BEEN TRIGGERED with time : " + time);
        Coordinate positionInTime;

        inBetweenRounds = true;
        // fillSchedule(time);
        // positionInTime = new Coordinate(300, 100);
        positionInTime = computePositionByTime(time);
        //System.out.println("Positonbytime = " + positionInTime);
        //System.out.println("start positon = " + startPosition);
        //System.out.println("Positon = " + position);
        // distance = 0;
        // position = startPosition;
        moveGui(startPosition);
        position = startPosition;
        // System.out.println("Positon = " + position);
        moveGui(positionInTime);
        //System.out.println("Positon = " + position);
        position = positionInTime;
        //System.out.println("Positon po = " + position);
        //System.out.println("DISTANCE = " + distance);
        // vehicle is still in start or already at the end
        if (positionInTime.equals(startPosition) || distance >= path.getPathLength()) {

        } else {
            inBetweenRounds = false;
        }

    }

    // TODO : add if it is on break or not, based on time as well
    // computes position of vehicle based on time of day
    private Coordinate computePositionByTime(LocalTime time) {
        // vehicle started before end of hour and hasnt finished round yet
        if((startingMinute + ((int)oneRideLength / 60)) > (60 + time.getMinute()) ){
            int untilHour = (60 - startingMinute);

            long secondsOnRoad = (untilHour + time.getMinute()) * 60;
            Coordinate tmp2Position = startPosition;
            System.out.println("START POSITION IN SPECIA IS = "+ tmp2Position);
            int tmp2Distance = 0;

            for (int i = 0; i < secondsOnRoad; i++) {
                try {
                    Street currentStreet = line.getStreetByCoord(tmp2Position);
                    int curSpeed = currentStreet.getStreetSpeed();
                    tmp2Distance += curSpeed;
                    tmp2Position = path.getNextPosition(tmp2Distance);
                    //System.out.println("TMP_DISTANCE v compiute = " + tmpDistance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            distance = tmp2Distance;
            System.out.println("SOM V SPECIAL CASE = ");
            return tmp2Position;
        }
        // vehicle hasnt started route yet so it is in its starting station
        else if (time.getMinute() < startingMinute) {
            return startPosition;
        }
        // vehicle finished route
        else if (time.getMinute() >= startingMinute + (int)(oneRideLength / 60)) {
            return startPosition;
        }
        // vehicle is on the way
        else {
            long secondsOnRoad = (time.getMinute() - startingMinute) * 60;
            Coordinate tmpPosition = startPosition;
            int tmpDistance = 0;

            for (int i = 0; i < secondsOnRoad; i++) {
                try {
                    Street currentStreet = line.getStreetByCoord(tmpPosition);
                    int curSpeed = currentStreet.getStreetSpeed();
                    tmpDistance += curSpeed;
                    tmpPosition = path.getNextPosition(tmpDistance);
                    //System.out.println("TMP_DISTANCE v compiute = " + tmpDistance);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            distance = tmpDistance;
            //System.out.println("DISTANCE v compiute = " + distance);
            return tmpPosition;
        }
    }

    // /**
    // * @param schedule the schedule to set
    // */
    // public void setSchedule() {
    // this.schedule = new Schedule(this);
    // }
    static int timeCounter = 0;

    /**
     * Updates time for vehicle: - sets new distance driven based on current vehicle
     * speed - this distance is used to update vehicle position
     **/
    @Override
    public void update(LocalTime time) {
        // if (timeCounter % 30 == 0){
        // System.out.println(String.format("TIME = %s ",time));
        // }
        // System.out.println(timeCounter);
        timeCounter++;
        double speed ;
        Street currentStreet = null;
        if (!inBetweenRounds) {
            // TODO: should be changed to update based on speed on current street
            try {
                currentStreet = line.getStreetByCoord(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            speed = currentStreet.getStreetSpeed();
            distance += speed; // 2 is speed , will ba changed to speed from street 
            //System.out.println(String.format("path len: %f, distance: %f, time %s positon %s", path.getPathLength(), distance,time, position));
            if(distance > path.getPathLength()){
                // stop at last Stop 
                Coordinate coordinates = path.getlastCoordinateOfPath();
                moveGui(coordinates);
                position = coordinates;
                //wait for couple minutes
                endRound();
                //System.out.println("bus " + busId + " ON THE END at time " + time);
                return;
            }
            Coordinate coordinates = path.getNextPosition(distance);
            //System.out.println(coordinates);
            moveGui(coordinates);
            position = coordinates;
        }
        // if vehicle is still late to start it will skip and go next hour, based on real life :) 
        else {
            if (time.getMinute() == startingMinute){
                //firstRound = false;
                System.out.println("bus"+busId+ " starting again at time " + time );
                startRound(time);
                
            }
        }
    }

    private void endRound(){
        inBetweenRounds = true;
        secondsPassed = 0;
    }

    private void startRound(LocalTime time){
        fillSchedule(time);
        distance = 0;
        resetGui(startPosition);
        position = startPosition;
        
        inBetweenRounds = false;
        
        schedule.printOutSchedule();
    
    }
   

    // go through entire path and calculate arrive time for every stop
    public void fillSchedule(LocalTime begTime){
        int distance = 0;
        long timeCount = 0;
        //startTime = LocalTime.now().plusMinutes(5+7).plusSeconds(30);
        startTime = begTime;
        List<Stop> stoplist = new ArrayList<>();
        List<LocalTime> timeslist = new ArrayList<>();
        System.out.println("Streetslist lenghts " + line.getStreetsList().size()+ ". LIne id is " + line.getId());
        System.out.println("path legnth: " + path.getPathLength());
        while(distance <= path.getPathLength()){
            try{
                //Street currentStreet = line.getStreetByCoord(startPosition);
                
                //int curSpeed = currentStreet.getStreetSpeed();
                int curSpeed = 2;
                int tmpDistance = distance;
                
                for (int i = 0; i < curSpeed; i++){
                    position = path.getNextPosition(tmpDistance + i);
                    Stop currentStop = line.getStopFromCoords(position);
                    
                    LocalTime time = startTime;
                    //System.out.println(timeCount);                    
                    time = time.plusSeconds(timeCount);
                    //System.out.println(time);
                    
                    if ( currentStop != null){
                        stoplist.add(currentStop);
                        timeslist.add(time);
                        //System.out.println("ADDING STOP ");
                    }
                }
                distance += curSpeed;
                position = path.getNextPosition(distance); 
                timeCount++;
                
            }
            catch(Exception e){
                System.out.println(e + " FILLSCHEDULE Error");
                break;
            }
        }
       // System.out.println("FILL SHCEDULE WHILE LOOP DONE");
        
        schedule.setStopList(stoplist);
        schedule.setTimesList(timeslist);
        oneRideLength = calculateOneRide(timeslist);
        System.out.println("one ride by "+ busId+" takes "+ oneRideLength/60 + " minutes");
        //schedule.printOutSchedule();
    
    }
    
    private long calculateOneRide(List<LocalTime> timesList){
         long duration =  java.time.Duration.between(timesList.get(timesList.size()-1),timesList.get(0)).getSeconds();
         return Math.abs(duration);
    }



    // =========== gui =========== // 

    private void resetGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()));
            shape.setTranslateY((coordinate.getY() - position.getY()));
            // System.out.println("=== reset ===");
            // System.out.println( shape.getTranslateX());
            // System.out.println( shape.getTranslateY());
            
        }
    
    }

    private void moveGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - position.getY()) + shape.getTranslateY());
            // System.out.println("=== move ===");
            // System.out.println( shape.getTranslateX());
            // System.out.println( shape.getTranslateY());
           
        }
    }

    public void setGui() {
        this.gui = new ArrayList<Shape>();
        this.gui.add(new Circle(position.getX(), position.getY(), 10, line.getLineColor()));
      
    }
    
    // =========== Getters =========== // 

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    public Line getLine() {
        return line;
    }
    @JsonIgnore
    public Coordinate getPosition() {
        return position;
    }

    @JsonIgnore
    public Path getPath() {
        return path;
    }

    public String getBusId() {
        return busId;
    }
   
    @JsonIgnore
    public double getDistance(){
        return distance;
    }
    @JsonIgnore
    public Schedule getSchedule(){
        return schedule;
    }
    
    @JsonIgnore
    public LocalTime getStartTime(){
        return startTime;
    }
    @JsonIgnore
    public Coordinate getStartPosition(){
        return startPosition;
    }

    @JsonIgnore
    public boolean getInBetweenRounds(){
        return inBetweenRounds;
    }
    @JsonIgnore
    public int getTmeBetweenRounds(){
        return timeBetweenRounds;
    } 
    @JsonIgnore
    public int getSecondsPassed(){
        return secondsPassed;
    }
    @JsonIgnore
    public long getOneRideLength(){
        return oneRideLength;
    } // in seconds
    


    static class CallConstructor extends StdConverter<Vehicle, Vehicle> {

        @Override
        public Vehicle convert (Vehicle value) {
            value.setGui();
            return value;
        }
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "line='" + line + '\'' +
                //", position=" + position +
                //", speed=" + speed +
               // ", path=" + path +
                '}';
    }
}
