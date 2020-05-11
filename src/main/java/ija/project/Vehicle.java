package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lineID", scope = Vehicle.class)
@JsonDeserialize(converter = Vehicle.CallConstructor.class)
public class Vehicle implements Drawable, TimeUpdate {
    private Line line;
    private Coordinate position;
    private double speed;
    private double distance = 0;

    private Schedule schedule;
    

    private LocalTime startTime;
    private Coordinate startPosition;
    private Path path;



    private boolean inBetweenRounds = true;
    private int timeBetweenRounds = 10; // 10 seconds
    private int secondsPassed;

    @JsonIgnore
    private List<Shape> gui;

    private Vehicle(){
    }

    public Vehicle(Line line, double speed) {
        this.line = line;
        this.speed = speed;
        this.path = line.getPath();
        this.schedule = new Schedule(this);
        fillSchedule();
        setGui();
    }

    /**
     * Updates time for vehicle:
     *  - sets new distance driven based on current vehicle speed
     *  - this distance is used to update vehicle position
     * **/
    @Override
    public void update(LocalTime time) {
        if (!inBetweenRounds){
            distance += speed;
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

    private void setGui() {
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
    private void fillSchedule(){
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
                System.out.println(e);
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

    public Coordinate getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    public Path getPath() {
        return path;
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
                ", position=" + position +
                ", speed=" + speed +
                ", path=" + path +
                '}';
    }
}
