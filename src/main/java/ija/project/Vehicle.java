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

 @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "busId", scope = Vehicle.class)
 @JsonDeserialize(converter = Vehicle.CallConstructor.class)
public class Vehicle implements Drawable, TimeUpdate {
    private String busId;
    private Line line;
    @JsonIgnore
    private Coordinate position;
    //private double speed;
    @JsonIgnore
    private double distance = 0;
    @JsonIgnore
    private Schedule schedule;
    
    @JsonIgnore
    private LocalTime startTime;
    @JsonIgnore
    private Coordinate startPosition;
    @JsonIgnore
    private Path path;


    @JsonIgnore
    private boolean inBetweenRounds = true;
    @JsonIgnore
    private int timeBetweenRounds = 10; // 10 seconds
    @JsonIgnore
    private int secondsPassed;

    @JsonIgnore
    private List<Shape> gui;

    private Vehicle(){
    }

    public Vehicle(String busId,Line line) {
        this.busId = busId;
        this.line = line;
        //this.speed = speed;
        this.path = line.getPath();
        //this.schedule = new Schedule(this);
       // System.out.println(this);
        //fillSchedule();
        //setGui();
    
    }

    /**
     * @param schedule the schedule to set
     */
    public void setSchedule() {
        this.schedule = new Schedule(this);
    }

    /**
     * Updates time for vehicle:
     *  - sets new distance driven based on current vehicle speed
     *  - this distance is used to update vehicle position
     * **/
    @Override
    public void update(LocalTime time) {
        double speed = 2;
        if (!inBetweenRounds){
            // TODO: should be changed to update based on speed on current street 
            distance += speed; // 2 is speed , will ba changed to speed from street 
            System.out.println(String.format("path len: %f, distance: %f, time %s", path.getPathLength(), distance,time));
            if(distance > path.getPathLength()){
                // stop at last Stop 
                Coordinate coordinates = path.getlastCoordinateOfPath();
                moveGui(coordinates);
                position = coordinates;
                //wait for couple minutes
                endRound();
                System.out.println("IM ON THE END");
                return;
            }
            Coordinate coordinates = path.getNextPosition(distance);
            moveGui(coordinates);
            position = coordinates;
        }
        else{
            System.out.println("IM WAITING because im finished");
            secondsPassed++;
            if(secondsPassed >= timeBetweenRounds ){
                startRound();
            }
            
        }
    }

    private void moveGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - position.getY()) + shape.getTranslateY());
        }
    }

    public void setGui() {
        this.gui = new ArrayList<Shape>();
        this.gui.add(new Circle(position.getX(), position.getY(), 10, Color.BLUE));
    }


    private void endRound(){
        inBetweenRounds = true;
        secondsPassed = 0;
    }

    private void startRound(){
        inBetweenRounds = false;
        distance = 0; 
    
    }
    // TODO  :  add if it is on break or not, based on time as well 
    // computes position of vehicle based on time of day 
    private void computePositionByTime(LocalTime time){
        long diffInSeconds = java.time.Duration.between(time, startTime).getSeconds();
        int distance = 0;
        for(int i = 0;i < diffInSeconds;i++){
            try{
                Street currentStreet = line.getStreetByCoord(startPosition);
                int curSpeed = currentStreet.getStreetSpeed();
                distance += curSpeed;
                position = path.getNextPosition(distance); 
                
            }
            catch(Exception e){
                System.out.println(e);
            }
       }
    }

    // go through entire path and calculate arrive time for every stop
    public void fillSchedule(){
        int distance = 0;
        long timeCount = 0;
        while(distance < path.getPathLength()){
            try{
                Street currentStreet = line.getStreetByCoord(startPosition);
                
                int curSpeed = currentStreet.getStreetSpeed();
                int tmpDistance = distance;
                for (int i = 0;i<curSpeed;i++){
                    position = path.getNextPosition(tmpDistance);
                    Stop currentStop = line.getStopFromCoords(position);
                    LocalTime time = startTime;
                    time.plusSeconds(timeCount);
                    if ( currentStop != null){
                        schedule.updateStopsList(currentStop);
                        schedule.updateTimesList(time);
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
    }



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

    // public double getSpeed() {
    //     return speed;
    // }
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
