package ija.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    // Possible TODO : set Scale to class variable so then it is consistent after
    // timeset or sltreet slow, it might be better not to do it tho.

    @FXML
    private Pane content;

    @FXML
    private TextField timeScale;
    @FXML
    private TextField timeSetHours;
    @FXML
    private TextField timeSetMinutes;
    @FXML
    private TextField timeSetSeconds;

    @FXML
    private TextField textStreetName;

    private Timer timer;
    private LocalTime time = LocalTime.now();

    private List<Drawable> elements = new ArrayList<>();
    private List<TimeUpdate> updates = new ArrayList<>();

    private List<Street> streetsList = new ArrayList<>();

    /*
     * Checks if input street exists on map and if it does, slows its speed to
     * SLOWED_SPEED
     */
    @FXML
    private void onSlowStreet() {
        String streetName = (textStreetName.getText());
        for (Street street : streetsList) {
            if (street.getStreetName() == streetName) {
                street.setStreetSpeed(Street.SLOWED_SPEED);
                //System.out.println("street " + streetName + " slowed");
                timer.cancel();
                for (TimeUpdate update : updates) {
                    update.reloadSchedule(time);
                }
                startTimer(1);
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Street name doest match any of the streets !");
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
            if (street.getStreetName() == streetName) {
                street.setStreetSpeed(Street.DEFAULT_SPEED);
                //System.out.println("street " + streetName + " back to default speed");

                timer.cancel();
                for (TimeUpdate update : updates) {
                    update.reloadSchedule(time);
                }
                startTimer(1);
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
            float scale = Float.parseFloat(timeScale.getText());
            if (scale <= 0) {
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
            System.out.println("VOLAL SOM ONNEWSETTIME");
            int hours = Integer.parseInt(timeSetHours.getText());

            int minutes = Integer.parseInt(timeSetMinutes.getText());
            int seconds = Integer.parseInt(timeSetSeconds.getText());

            timer.cancel();
            time = LocalTime.of(hours, minutes, seconds);
            for (TimeUpdate update : updates) {
                update.newTime(time);
            }
            startTimer(1);
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
     * Timer that runs TimerTask at fixed rate that can be changes by scale
     * 
     * @param scale
     */
    public void startTimer(double scale) {
       
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        time = time.plusSeconds(1);

                        for (TimeUpdate update : updates) {
                            update.update(time);
                        }
                    }
                });
            }

        }, 0, (long) (1000 / scale));

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

}
