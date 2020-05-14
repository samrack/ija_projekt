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

public class Vehicle implements Drawable, TimeUpdate {
    private String busId;

    private Line line;

    private Coordinate position;

    private double distance = 0;

    private Schedule schedule;

    private LocalTime startTime;

    private int startingMinute;

    private Coordinate startPosition;

    private Path path;

    private boolean inBetweenRounds = true;

    private boolean isOnStop;

    private int stopTimer = 0;

    private int secondsPassed;

    private long oneRideLength; // in seconds

    private List<Shape> gui;

    static int timeCounter = 0;

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

    /**
     * @return LocalTime
     */
    private LocalTime composeStartignTime() {
        return LocalTime.of(LocalTime.now().getHour(), startingMinute, 0);
    }

    /**
     * Refills schedule for current time 
     * 
     * @param time
     */
    @Override
    public void reloadSchedule(LocalTime time) {
        fillSchedule(time);
    }

    /**
     * Redraws all vehicles to the place where they should be
     * 
     * @param time
     */
    @Override
    public void newTime(LocalTime time) {
        System.out.println("ATTENTION PROGRAMMER NEWTIME HAS BEEN TRIGGERED with time : " + time);
        Coordinate positionInTime;

        inBetweenRounds = true;

        positionInTime = computePositionByTime(time);

        moveGui(startPosition);
        position = startPosition;

        moveGui(positionInTime);

        position = positionInTime;

        if (positionInTime.equals(startPosition) || distance >= path.getPathLength()) {

        } else {
            inBetweenRounds = false;
        }

    }

    /**
     * Calculate position of vehicle on the map at the set time 
     * 
     * @param time 
     * @return Coordinate - position of vehicle
     */
    private Coordinate computePositionByTime(LocalTime time) {
        // vehicle started before end of hour and hasnt finished round yet
        if ((startingMinute + ((int) oneRideLength / 60)) > (60 + time.getMinute())) {
            int untilHour = (60 - startingMinute);

            long secondsOnRoad = (untilHour + time.getMinute()) * 60;
            Coordinate tmp2Position = startPosition;
            System.out.println("START POSITION IN SPECIA IS = " + tmp2Position);
            int tmp2Distance = 0;

            for (int i = 0; i < secondsOnRoad; i++) {
                try {
                    Street currentStreet = line.getStreetByCoord(tmp2Position);
                    int curSpeed = currentStreet.getStreetSpeed();
                    tmp2Distance += curSpeed;
                    tmp2Position = path.getNextPosition(tmp2Distance);

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
        else if (time.getMinute() >= startingMinute + (int) (oneRideLength / 60)) {
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

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            distance = tmpDistance;

            return tmpPosition;
        }
    }


    /**
     * Updates time for vehicle:
     * - sets new distance driven based on current vehicle speed 
     * - this distance is used to update vehicle position
     * 
     * @param LocalTime time recieved from Timer in MainController
     **/
    @Override
    public void update(LocalTime time) {

        timeCounter++;
        double speed;
        Street currentStreet = null;
        if (isOnStop) {
            if (stopTimer == 40) {
                isOnStop = false;
                stopTimer = 0;
            } else {
                stopTimer++;
            }
        } else if (!inBetweenRounds) {

            try {
                currentStreet = line.getStreetByCoord(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            speed = currentStreet.getStreetSpeed();
            distance += speed;

            if (distance > path.getPathLength()) {
                // stop at last Stop
                Coordinate coordinates = path.getlastCoordinateOfPath();
                moveGui(coordinates);
                position = coordinates;
                // wait for couple minutes
                endRound();

                return;
            }
            Coordinate coordinates = path.getNextPosition(distance);

            moveGui(coordinates);
            position = coordinates;

            for (LocalTime myTime : schedule.getTimesList()) {
                if (myTime.getHour() == time.getHour() && myTime.getMinute() == time.getMinute()
                        && myTime.getSecond() == time.getSecond()) {
                    isOnStop = true;
                    break;
                }
            }

        }
        // if vehicle is still late to start it will skip and go next hour, based on
        // real life :)
        else {
            if (time.getMinute() == startingMinute) {

                System.out.println("bus" + busId + " starting again at time " + time);
                startRound(time);

            }
        }
    }

    /**
     * End a round of vehicle on the Line
     */
    private void endRound() {
        inBetweenRounds = true;
        secondsPassed = 0;
    }

    /**
     * Start a round of vehicle on the Line
     * 
     * @param time
     */
    private void startRound(LocalTime time) {
        fillSchedule(time);
        distance = 0;
        resetGui(startPosition);
        position = startPosition;
        inBetweenRounds = false;
        // schedule.printOutSchedule();

    }

    /**
     * Go through entire path and calculate arrive time for every stop and fill
     * schedule with it
     * 
     * @param begTime start time for vehicle at first station
     */

    public void fillSchedule(LocalTime begTime) {
        int distance = 0;
        long timeCount = 0;

        startTime = begTime;
        List<Stop> stoplist = new ArrayList<>();
        List<LocalTime> timeslist = new ArrayList<>();

        while (distance <= path.getPathLength()) {
            try {
                Street currentStreet = line.getStreetByCoord(startPosition);

                int curSpeed = currentStreet.getStreetSpeed();

                int tmpDistance = distance;

                for (int i = 0; i < curSpeed; i++) {
                    position = path.getNextPosition(tmpDistance + i);
                    Stop currentStop = line.getStopFromCoords(position);

                    LocalTime time = startTime;

                    time = time.plusSeconds(timeCount);

                    if (currentStop != null) {
                        if (time != startTime) {
                            timeCount += 40;
                        }

                        stoplist.add(currentStop);
                        timeslist.add(time);

                    }
                }
                distance += curSpeed;
                position = path.getNextPosition(distance);
                timeCount++;

            } catch (Exception e) {
                System.out.println(e + " FILLSCHEDULE Error");
                break;
            }
        }

        schedule.setStopList(stoplist);
        schedule.setTimesList(timeslist);
        oneRideLength = calculateOneRide(timeslist);

    }

    /**
     * Calculates duration of one ride through while line in seconds
     * 
     * @param timesList
     * @return long duration in seconds
     */
    private long calculateOneRide(List<LocalTime> timesList) {
        long duration = java.time.Duration.between(timesList.get(timesList.size() - 1), timesList.get(0)).getSeconds();
        return Math.abs(duration);
    }

    /**
     * Resets vehicle circle to its starting position
     * 
     * @param coordinate
     */
    private void resetGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()));
            shape.setTranslateY((coordinate.getY() - position.getY()));

        }

    }

    /**
     * Move vehicle circle to the coordinate
     * 
     * @param coordinate
     */
    private void moveGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - position.getY()) + shape.getTranslateY());

        }
    }

    public void setGui() {
        this.gui = new ArrayList<Shape>();
        this.gui.add(new Circle(position.getX(), position.getY(), 10, line.getLineColor()));

    }

    /**
     * @return List<Shape>
     */
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    /**
     * @return Line
     */
    public Line getLine() {
        return line;
    }

    /**
     * @return Coordinate
     */
    public Coordinate getPosition() {
        return position;
    }

    /**
     * @return Path
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return String
     */
    public String getBusId() {
        return busId;
    }

    /**
     * @return double
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return Schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * @return LocalTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return Coordinate
     */
    public Coordinate getStartPosition() {
        return startPosition;
    }

    /**
     * @return boolean
     */
    public boolean getInBetweenRounds() {
        return inBetweenRounds;
    }

    /**
     * @return int
     */
    public int getSecondsPassed() {
        return secondsPassed;
    }

    /**
     * @return long
     */
    public long getOneRideLength() {
        return oneRideLength;
    } // in seconds

    static class CallConstructor extends StdConverter<Vehicle, Vehicle> {

        @Override
        public Vehicle convert(Vehicle value) {
            value.setGui();
            return value;
        }
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Vehicle{" + "line='" + line + '\'' + '}';
    }
}
