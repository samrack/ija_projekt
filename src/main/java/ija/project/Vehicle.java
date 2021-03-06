package ija.project;

import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a vehicle
 * 
 * @author Samuel Stuchly xstuch06
 * @author Samuel Spisak xspisa02
 */
public class Vehicle implements Drawable, TimeUpdate {
    private String busId;

    private Line line;
    private Path path;
    private Path originalPath;
    private Schedule schedule;

    private Coordinate position;
    private double distance = 0;
    private Coordinate startPosition;

    private LocalTime startTime;
    private int startingMinute;
    private int hourStarted;

    private boolean inBetweenRounds = true;
    public boolean isOnStop;
    private int stopTimer = 0;
    private int secondsPassed;
    private long oneRideLength; // in seconds

    static int timeCounter = 0;

    private List<Shape> gui;

    private List<Stop> stoplist = new ArrayList<>();
    private List<LocalTime> timeslist = new ArrayList<>();
    private List<Integer> delaysList = new ArrayList<>();
    private List<Double> stopDistances = new ArrayList<>();

    public EventHandler<MouseEvent> handler;

    private Line updatedLine;
    private Boolean updateReady = false;
    private Boolean canUpdate = false;
    private int replacedStreetIndex = 0;
    private int replacedStreetSize = 0;
    private boolean isDeactivate;
    private boolean onRedWhenActivate;

    private Vehicle() {
    }

    public Vehicle(String busId, Line line, int startingMinute, EventHandler<MouseEvent> handler) {
        this.busId = busId;
        this.line = line;
        this.path = line.getPath();
        this.originalPath = new Path(this.path.getPath());
        this.schedule = new Schedule(this);
        this.startPosition = path.getPath().get(0);
        this.position = startPosition;
        this.startingMinute = startingMinute;
        this.handler = handler;
        fillSchedule(composeStartingTime());
        setStopDistances(this.path);
        setGui();

    }

    private void updateLineAndPath() {

        this.line = updatedLine;
        this.path = updatedLine.getPath();
        updateReady = false;
        canUpdate = false;

    }

    /**
     * @param updatedline
     */
    public void setUpdatedLine(Line updatedline) {
        this.updatedLine = updatedline;
        updateReady = true;
    }

    /**
     * @return Line
     */
    public Line getUpdatedLine() {
        return updatedLine;
    }

    /**
     * @param value
     */
    public void setCanUpdate(Boolean value) {
        this.canUpdate = value;
    }

    /**
     * @return LocalTime
     */
    private LocalTime composeStartingTime() {
        return LocalTime.of(LocalTime.now().getHour(), startingMinute, 0);
    }

    /**
     * Redraws all vehicles to the place where they should be
     * 
     * @param time
     */
    @Override
    public void newTime(LocalTime time) {
        Coordinate positionInTime;

        inBetweenRounds = true;

        positionInTime = computePositionByTime(time);

        if (!positionInTime.equals(startPosition)) {
            for (Stop stop : this.getLine().getStopsList()) {
                if (stop.getCoordinate().equals(positionInTime)) {
                    isOnStop = true;
                    break;
                }
            }
        }

        resetGui();
        ;
        position = startPosition;

        moveGui(positionInTime);

        position = positionInTime;

        inBetweenRounds = positionInTime.equals(startPosition) || distance >= path.getPathLength();

    }

    /**
     * Calculate position of vehicle on the map at the set time
     * 
     * @param time
     * @return Coordinate - position of vehicle
     */
    private Coordinate computePositionByTime(LocalTime time) {
        int secondsOnRoad;
        Coordinate tmpPosition = startPosition;
        int tmpDistance = 0;

        // vehicle started before end of hour and hasnt finished round yet
        if ((startingMinute + ((int) oneRideLength / 60)) > (60 + time.getMinute())) {

            int untilHour = (60 - startingMinute);
            secondsOnRoad = (untilHour + time.getMinute()) * 60;

        }
        // vehicle hasnt started route yet so it is in its starting station
        else if (time.getMinute() <= startingMinute) {
            return startPosition;
        }
        // vehicle finished route
        else if (time.getMinute() >= (startingMinute + (int) (oneRideLength / 60))) {
            return startPosition;
        }
        // vehicle is on the way
        else {
            secondsOnRoad = (time.getMinute() - startingMinute) * 60;
        }

        int stopEnteredSecond = 0;

        for (int i = 1; i <= secondsOnRoad; i++) {

            try {
                Street currentStreet = line.getStreetByCoord(tmpPosition);
                int curSpeed = currentStreet.getStreetSpeed();

                for (int j = 1; j <= curSpeed; j++) {

                    tmpDistance++;
                    tmpPosition = path.getNextPosition(tmpDistance);

                    for (Stop stop : stoplist) {
                        if (stop.getCoordinate().equals(tmpPosition)) {
                            stopEnteredSecond = i;
                            i += 40;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // set how many seconds are left to wait on stop in case vehicle's position is
        // on stop at entered time
        if (secondsOnRoad - stopEnteredSecond <= 40) {
            stopTimer = secondsOnRoad - stopEnteredSecond;
        }

        distance = tmpDistance;
        return tmpPosition;
    }

    /**
     * Updates time for vehicle: - sets new distance driven based on current vehicle
     * speed - this distance is used to update vehicle position
     * 
     * @param time recieved from Timer in MainController
     **/
    @Override
    public void update(LocalTime time) {

        if (updateReady && canUpdate) {

            Street currentStreet;
            double oldPathLength = path.getPathLength();

            try {
                currentStreet = line.getStreetByCoord(position);
                if (!isDeactivate) {
                    updateLineAndPath();
                }
                int index = line.getStreetsList().indexOf(currentStreet);
                if (isDeactivate) {
                    if (position.equals(startPosition)) {
                        isDeactivate = false;
                    }
                    if (index >= replacedStreetIndex + replacedStreetSize
                            && index < line.getStreetsList().size() - replacedStreetSize) {
                        updateLineAndPath();
                        //
                    }
                } else if (index >= replacedStreetIndex) {
                    double distanceDiff = path.getPathLength() - oldPathLength;
                    distance += distanceDiff;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

            if (distance + speed >= path.getPathLength()) {
                // stop at last Stop
                Coordinate coordinates = path.getlastCoordinateOfPath();
                moveGui(coordinates);
                position = coordinates;
                endRound();

                return;
            }

            Coordinate coordinates = position;

            for (int j = 1; j <= speed; j++) {
                distance++;
                coordinates = path.getNextPosition(distance);

                for (Stop stop : schedule.getStopsList()) {
                    if (stop.getCoordinate().equals(coordinates)) {
                        isOnStop = true;
                        moveGui(coordinates);
                        position = coordinates;

                        return;
                    }
                }
            }

            moveGui(coordinates);
            position = coordinates;
        }
        // if vehicle is still late to start it will skip and go next hour, based on
        // real life
        else {
            // updates line

            if (time.getMinute() == startingMinute) {
                if (updateReady) {
                    canUpdate = true;
                }
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
        position = startPosition;
    }

    /**
     * Start a round of vehicle on the Line
     * 
     * @param time
     */
    private void startRound(LocalTime time) {
        fillSchedule(time);
        distance = 0;
        position = startPosition;
        resetGui();
        inBetweenRounds = false;

    }

    /**
     * Go through entire path and calculate arrive time for every stop and fill
     * schedule with it
     * 
     * @param begTime start time for vehicle at first station
     */

    public void fillSchedule(LocalTime begTime) {
        hourStarted = begTime.getHour();
        int distance = 0;
        long timeCount = 0;

        startTime = begTime;
        Coordinate tmpPosition;

        stoplist.clear();
        timeslist.clear();

        boolean completed = false;

        while (distance <= originalPath.getPathLength()) {
            try {

                int speed = Street.DEFAULT_SPEED;
                int tmpDistance = distance;

                for (int i = 0; i < speed; i++) {
                    tmpPosition = originalPath.getNextPosition(tmpDistance + i);
                    Stop currentStop = line.getStopFromCoords(tmpPosition);

                    LocalTime time = startTime;

                    time = time.plusSeconds(timeCount);

                    if (currentStop != null) {
                        if (time != startTime) {
                            timeCount += 40;
                        }

                        stoplist.add(currentStop);
                        timeslist.add(time);

                        if (distance >= originalPath.getPathLength()) {
                            completed = true;
                            break;
                        }
                    }
                }

                if (completed) {
                    break;
                }
                distance += speed;
                timeCount++;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        schedule.setStopList(stoplist);
        schedule.setTimesList(timeslist);
        oneRideLength = calculateOneRide(timeslist);
    }

    /**
     * @param currentTime
     */
    public void fillDelaysList(LocalTime currentTime) {

        delaysList.clear();
        double distance = this.distance;
        long timeCount = 0;

        Coordinate tmpPosition = this.position;
        LocalTime time;
        int stopOrder = 0;
        boolean completed = false;

        // zastavkam ktore uz vozidlo preslo nedavam meskanie
        for (double stopDistance : stopDistances) {
            if (stopDistance <= this.distance) {
                delaysList.add(0);
                stopOrder++;
            }
        }

        while (distance <= path.getPathLength()) {
            try {
                Street currentStreet = line.getStreetByCoord(tmpPosition);
                double tmpDistance = distance;
                int speed = currentStreet.getStreetSpeed();

                for (int i = 1; i <= speed; i++) {
                    tmpPosition = path.getNextPosition(tmpDistance + i);
                    Stop currentStop = line.getStopFromCoords(tmpPosition);

                    if (isOnStop) {
                        time = currentTime.plusSeconds(40 - stopTimer);
                    } else {
                        time = currentTime;
                    }

                    time = time.plusSeconds(timeCount);

                    if (currentStop != null) {
                        Long delay = java.time.Duration.between(time, timeslist.get(stopOrder)).getSeconds();
                        int secondsDelay = Math.abs(delay.intValue());
                        delaysList.add(secondsDelay);
                        timeCount += 40;
                        stopOrder++;

                        if (distance >= path.getPathLength()) {
                            completed = true;
                            break;
                        }
                    }
                }

                if (completed) {
                    break;
                }

                distance += speed;
                timeCount++;

            } catch (Exception e) {

                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Refills schedule taking into consideration current time
     *
     * @param time
     */
    public void reloadSchedule(LocalTime time) {

        LocalTime begTime = LocalTime.of(time.getHour(), startingMinute, 0);
        fillSchedule(begTime);

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
     */
    private void resetGui() {
        for (Shape shape : gui) {
            shape.setTranslateX(0);
            shape.setTranslateY(0);
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
        this.gui.get(0).setOnMouseClicked(this.handler);

    }

    /**
     * @param path
     */
    public void setStopDistances(Path path) {

        for (int i = 0; i < schedule.getStopsList().size(); i++) {
            double stopDistance = 0;
            Coordinate stopCoordinate = startPosition;

            while (!stopCoordinate.equals(schedule.getStopsList().get(i).getCoordinate())) {
                stopDistance++;
                stopCoordinate = path.getNextPosition(stopDistance);
            }
            stopDistances.add(stopDistance);
        }
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
     * @return int
     */
    public int getStartingMinute() {
        return startingMinute;
    }

    /**
     * @return int
     */
    public int getHourStarted() {
        return hourStarted;
    }

    /**
     * @return Coordinate
     */
    public Coordinate getStartPosition() {
        return startPosition;
    }

    /**
     * @return List<Stop>
     */
    public List<Stop> getStoplist() {
        return stoplist;
    }

    /**
     * @return List<LocalTime>
     */
    public List<LocalTime> getTimeslist() {
        return timeslist;
    }

    /**
     * @return List<Integer>
     */
    public List<Integer> getDelaysList() {
        return delaysList;
    }

    /**
     * @param replacedStreetIndex
     */
    public void setReplacedStreetIndex(int replacedStreetIndex) {
        this.replacedStreetIndex = replacedStreetIndex;
    }

    /**
     * @param deactivate
     */
    public void setDeactivate(boolean deactivate) {
        isDeactivate = deactivate;
    }

    /**
     * @param replacedStreetSize
     */
    public void setReplacedStreetSize(int replacedStreetSize) {
        this.replacedStreetSize = replacedStreetSize;
    }

    /**
     * @param onRedWhenActivate
     */
    public void setOnRedWhenActivate(boolean onRedWhenActivate) {
        this.onRedWhenActivate = onRedWhenActivate;
    }

    /**
     * @return boolean
     */
    public boolean isOnRedWhenActivate() {
        return onRedWhenActivate;
    }

    /**
     * @return boolean
     */
    public boolean getInBetweenRounds() {
        return inBetweenRounds;
    }

    /**
     * @return Path
     */
    public Path getOriginalPath() {
        return originalPath;
    }

    /**
     * @return int
     */
    public int getSecondsPassed() {
        return secondsPassed;
    }

    /**
     * @return long length in seconds
     */
    public long getOneRideLength() {
        return oneRideLength;
    }

    /**
     * @return List<Double>
     */
    public List<Double> getStopDistances() {
        return stopDistances;
    }

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
        return "Vehicle{" + "line='" + line + '\'' + "handelr= " + handler + '}';
    }
}
