package ija.project;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Main controller, controls things like timer and street speed
 * 
 * @author Samuel Stuchly xstuch06
 * @author Samuel Spisak xspisa02
 */
public class MainController {

    @FXML
    private Pane content;

    @FXML
    private Pane timeline;

    @FXML
    private TextField timeScale;
    @FXML
    private TextField timeSetHours;
    @FXML
    private TextField timeSetMinutes;

    @FXML
    private TextField textStreetName;

    @FXML
    private Text timeField;

    private Timer timer;
    private LocalTime time = LocalTime.now();
    private float scale = 1;

    private List<Drawable> elements = new ArrayList<>();
    private List<TimeUpdate> updates = new ArrayList<>();
    private List<Street> streetsList = new ArrayList<>();
    private List<ija.project.Line> allLines = new ArrayList<>();

    private Itinerary itinerary;
    public Vehicle activeVehicle;

    public Text clickedStreetName;
    private Boolean redAlreadySet = false;

    private Text redStreet;
    private Set<Text> greenSet = new HashSet<>();

    public Boolean canSetByPass = false;

    public Boolean alreadySetByPass = false;

    private int highlightedLinesCounter;
    private ByPass byPass;

    @FXML
    private void onNewBypass() {
        if (!alreadySetByPass) {
            canSetByPass = true;
            resetStreetTextHighlight();
        }

    }

    @FXML
    private void onConfirmBypass() {
        if (!redAlreadySet) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ByPass cannot be formed , no red street !");
            alert.showAndWait();
            return;
        }
        Street replacedStreet = getStreetByName(redStreet.getText());
        List<Street> replacmenetStreets = new ArrayList<>();

        for (Text streetText : greenSet) {
            Street tmp = getStreetByName(streetText.getText());
            replacmenetStreets.add(tmp);
        }

        if (byPass != null) {
            for (ija.project.Line line : byPass.getAffectedLines()) {
                for (int i = 0; i < byPass.getReplacmenetStreets().size(); i++) {
                    line.getStreetsList().remove(line.getStreetsList().size() - 1);
                }
            }
        }

        byPass = new ByPass(replacedStreet, replacmenetStreets, allLines);

        if (!byPass.activate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ByPass cannot be formed with selected streets !");
            alert.showAndWait();
            return;
        }

        updateVehiclesWithByPass(false);

        canSetByPass = false;
        alreadySetByPass = true;
    }

    @FXML
    private void onCancelByPass() {
        if (alreadySetByPass) {
            byPass.deactivate();

            updateVehiclesWithByPass(true);

            resetStreetTextHighlight();
            alreadySetByPass = false;
        }
    }

    /**
     * @param deactivateValue
     */
    private void updateVehiclesWithByPass(boolean deactivateValue) {
        List<Vehicle> vList = new ArrayList<>();
        for (Drawable elem : elements) {
            if (elem instanceof Vehicle) {
                vList.add((Vehicle) elem);
            }
        }

        for (ija.project.Line line : byPass.getAffectedLines()) {
            for (Vehicle v : vList) {
                if (line.getId().equals(v.getLine().getId())) {
                    checkIfCanUpdate(v, line);
                    v.setDeactivate(deactivateValue);
                    v.setUpdatedLine(line);
                    v.setReplacedStreetIndex(byPass.getIndex());
                    v.setReplacedStreetSize(byPass.getReplacmenetStreets().size());

                }
            }
        }
    }

    /**
     * @param v
     * @param line
     */
    private void checkIfCanUpdate(Vehicle v, ija.project.Line line) {
        Street currentStreet = getStreetFromCoord(v.getPosition());
        if ((currentStreet.getStreetName() == redStreet.getText()) && !v.getInBetweenRounds()) {
            v.setCanUpdate(false);
            v.setOnRedWhenActivate(true);
            v.getStopDistances().clear();
            v.setStopDistances(v.getOriginalPath());
            return;
        } else if (isOnGreen(currentStreet) && !v.getInBetweenRounds()) {
            v.setCanUpdate(true);
            v.getStopDistances().clear();
            v.setStopDistances(line.getPath());
            return;
        } else {
            v.setCanUpdate(true);
            v.getStopDistances().clear();
            v.setStopDistances(line.getPath());
        }
    }

    /**
     * @param coord
     * @return Street
     */
    private Street getStreetFromCoord(Coordinate coord) {
        for (Street street : streetsList) {

            if (street.isCoordOnStreet(coord)) {

                return street;
            }
        }
        return null;
    }

    /**
     * @param curStreet
     * @return Boolean
     */
    private Boolean isOnGreen(Street curStreet) {
        List<Street> replacmenetStreets = new ArrayList<>();
        for (Text streetText : greenSet) {
            Street tmp = getStreetByName(streetText.getText());
            replacmenetStreets.add(tmp);
        }
        for (Street s : replacmenetStreets) {
            if (curStreet.getStreetName() == s.getStreetName()) {
                return true;
            }
        }
        return false;
    }

    private void resetStreetTextHighlight() {
        if (redStreet != null) {
            redStreet.setFill(Color.BLACK);
            redStreet = null;
        }
        if (!greenSet.isEmpty()) {
            for (Text streetText : greenSet) {
                streetText.setFill(Color.BLACK);
            }
            greenSet.clear();
        }
        redAlreadySet = false;

    }

    /*
     * Checks if input street exists on map and if it does, slows its speed to
     * SLOWED_SPEED
     */
    @FXML
    private void onSlowStreet() {
        String streetName = (textStreetName.getText());
        for (Street street : streetsList) {
            if (street.getStreetName().equals(streetName)) {
                if (street.getStreetSpeed() > 1) {

                    street.setStreetSpeed(street.getStreetSpeed() - 2);
                    if (itinerary != null) {
                        setItinerary(activeVehicle);
                    }
                    return;

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Traffic on the street cannot get any slower!");
                    alert.showAndWait();
                    return;

                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.ERROR, "Street name doest match any of the streets!");
        alert.showAndWait();
    }

    /*
     * Checks if input street exists on map and if it does, sets its speed to
     * DEFAULT_SPEED
     */
    @FXML
    private void onSpeedUpStreet() {
        String streetName = (textStreetName.getText());
        for (Street street : streetsList) {
            if (street.getStreetName().equals(streetName)) {

                street.setStreetSpeed(Street.DEFAULT_SPEED);
                if (itinerary != null) {
                    setItinerary(activeVehicle);
                }
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Street name doest match any of the streets !");
        alert.showAndWait();

    }

    /*
     * Speeds up or slows down passage of time
     */
    @FXML
    private void onTimeScaleChange() {
        try {
            scale = Float.parseFloat(timeScale.getText());
            if (scale <= 0 || scale > 1000) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid time scale value");
                alert.showAndWait();
                return;
            }
            timer.cancel();
            startTimer(scale);

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid time scale value");
            alert.showAndWait();
        }
    }

    /*
     * Sets time to time input by user
     */
    @FXML
    private void onNewTimeSet() {
        try {

            int hours = Integer.parseInt(timeSetHours.getText());
            int minutes = Integer.parseInt(timeSetMinutes.getText());

            timer.cancel();
            time = LocalTime.of(hours, minutes, 0);

            timeline.getChildren().clear();
            unsetHighligtedLine();
            activeVehicle = null;
            itinerary = null;

            for (TimeUpdate update : updates) {
                if (update instanceof Vehicle) {

                    if (((Vehicle) update).getStartingMinute()
                            + ((int) ((Vehicle) update).getOneRideLength() / 60) > (60 + time.getMinute())) {
                        ((Vehicle) update).reloadSchedule(LocalTime.of(hours - 1, minutes, 0));
                    } else {
                        ((Vehicle) update).reloadSchedule(time);
                    }
                }
                update.newTime(time);
            }
            startTimer(scale);
        } catch (Exception ex) {
            System.out.println(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * @param event
     */
    @FXML
    private void onScroll(ScrollEvent event) {
        event.consume();
        double zoom = event.getDeltaY() > 0 ? 1.1 : 0.9;
        content.setScaleX(zoom * content.getScaleX());
        content.setScaleY(zoom * content.getScaleY());
        content.layout();
    }

    /**
     * @param elements
     */
    public void setElements(List<Drawable> elements) {
        this.elements = elements;
        for (Drawable elem : elements) {
            content.getChildren().addAll(elem.getGUI());
            if (elem instanceof TimeUpdate) {
                updates.add((TimeUpdate) elem);
            }
        }
    }

    /**
     * @param vehicle
     */
    public void setItinerary(Vehicle vehicle) {
        this.itinerary = new Itinerary(vehicle, time);
        timeline.getChildren().clear();
        timeline.getChildren().addAll(itinerary.getGUI());
    }

    public void unsetHighligtedLine() {
        if (activeVehicle != null) {

            int from = content.getChildren().size() - highlightedLinesCounter;
            int to = content.getChildren().size();

            content.getChildren().remove(from, to);
        }
    }

    public void setHighlightedLine() {
        ija.project.Line activeLine = activeVehicle.getLine();
        highlightedLinesCounter = 0;
        for (Street street : activeLine.getStreetsList()) {
            if (byPass != null) {
                if (alreadySetByPass) {
                    if (byPass.getAffectedLines().contains(activeLine)) {
                        if (street.equals(activeLine.getStreetsList().get(activeLine.getStreetsList().size() - 1))) {
                            break;
                        }
                    }
                } else {
                    if (byPass.getAffectedLines().contains(activeLine)) {
                        if (street.equals(activeLine.getStreetsList()
                                .get(activeLine.getStreetsList().size() - byPass.getReplacmenetStreets().size()))) {
                            break;
                        }
                    }
                }
            }
            Line l = new Line(street.getBegin().getX(), street.getBegin().getY(), street.getEnd().getX(),
                    street.getEnd().getY());
            l.setStrokeWidth(3);
            content.getChildren().add(l);
            highlightedLinesCounter++;

        }
    }

    public void setHighlightStreetName() {
        if (clickedStreetName.getFill() == Color.BLACK && !redAlreadySet) {
            clickedStreetName.setFill(Color.RED);
            redStreet = clickedStreetName;
            redAlreadySet = true;

        } else if (clickedStreetName.getFill() == Color.BLACK && redAlreadySet) {
            clickedStreetName.setFill(Color.GREEN);
            greenSet.add(clickedStreetName);
        } else if (clickedStreetName.getFill() == Color.RED) {
            clickedStreetName.setFill(Color.BLACK);
            redStreet = null;
            redAlreadySet = false;
        } else if (clickedStreetName.getFill() == Color.GREEN) {
            clickedStreetName.setFill(Color.BLACK);
            greenSet.remove(clickedStreetName);
        }

    }

    /**
     * Get street by street name from selected text
     * 
     * @param name
     * @return Street
     */
    private Street getStreetByName(String name) {
        for (Street street : streetsList) {
            if (street.getStreetName().equals(name)) {
                return street;
            }
        }
        return null;
    }

    /**
     * Timer that runs TimerTask at fixed rate that can be changes by scale
     * 
     * @param scaleValue
     */
    public void startTimer(double scaleValue) {

        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        time = time.plusSeconds(1);
                        timeField.setText(time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

                        for (TimeUpdate update : updates) {
                            update.update(time);
                            if (itinerary != null) {
                                itinerary.update(time);
                            }
                        }
                    }
                });
            }

        }, 0, (long) (1000 / scaleValue));

    }

    /**
     * @return Timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * @param list
     */
    public void setStreetsList(List<Street> list) {
        this.streetsList = list;
    }

    /**
     * @param list
     */
    public void setAllLines(List<ija.project.Line> list) {
        this.allLines = list;
    }

}
