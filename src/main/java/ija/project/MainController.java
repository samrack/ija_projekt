package ija.project;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
    private TextField timeScale;
    @FXML
    private TextField timeSetHours;
    @FXML
    private TextField timeSetMinutes;
    @FXML
    private TextField timeSetSeconds;

    @FXML
    private TextField textStreetName;


    @FXML
    private Text timeField;

    @FXML
    private Pane timeline;

    private Timer timer;
    private LocalTime time = LocalTime.now();
    private float scale = 1;

    private List<Drawable> elements = new ArrayList<>();
    private List<TimeUpdate> updates = new ArrayList<>();

    private List<Street> streetsList = new ArrayList<>();

    private String selectedId = "unactive";



    /*
     * Checks if input street exists on map and if it does, slows its speed to
     * SLOWED_SPEED
     */
    @FXML
    private void onSlowStreet() {
        String streetName = (textStreetName.getText());
        for (Street street : streetsList) {
            if (street.getStreetName().equals(streetName)) {
                if(street.getStreetSpeed() > 1) {

                    //testovanie
//                    street.setStreetSpeed(Street.DEFAULT_SPEED);
//                    street.setStreetSpeed(1);
                    //\testovanie

                    street.setStreetSpeed(street.getStreetSpeed() - 2);
//                    timer.cancel();
//                    for (TimeUpdate update : updates) {
//                        update.reloadSchedule(time);
//                    }
//                    startTimer(scale);
                    for (Street streetTmp : streetsList) {
                        System.out.println(streetTmp.toString());
                    }
                    return;

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Traffic on the street is as slow as possible!");
                    alert.showAndWait();
                    return;

                }
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
            if (street.getStreetName().equals(streetName)) {
                street.setStreetSpeed(Street.DEFAULT_SPEED);
                System.out.println("street " + streetName + " back to default speed");

//                timer.cancel();
//                for (TimeUpdate update : updates) {
//                    update.reloadSchedule(time);
//                }
//                startTimer(scale);
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
            if (scale <= 0 || scale >= 1000) {
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

//    EventHandler<javafx.scene.input.MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent event) {
//            System.out.println("clicked on circle");
//        }
//    };

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

                        //zoznam aktivnych spojov (max 2)
                        List<String> activeBuses = new ArrayList<>();
                        String newActiveId = "unactive";

                        //prechadzam elementy vozidiel
                        for (TimeUpdate update : updates) {
                            if(update instanceof Vehicle) {
                                Boolean isActive = ((Vehicle) update).isActive();
                                String busId = ((Vehicle) update).getBusId();

                                //Ak najdem aktivny spoj, vlozim do zoznamu aktivnych IDciek
                                if(isActive) {
                                    activeBuses.add(busId);
                                }
                            }
                        }

                        if(activeBuses.size() == 1) {
                            newActiveId = activeBuses.get(0);
                        }

                        if(activeBuses.size() == 2) {
                            if (selectedId.equals(activeBuses.get(0))) {
                                newActiveId = activeBuses.get(1);
                            } else {
                                newActiveId = activeBuses.get(0);
                            }
                        }

                        selectedId = newActiveId;

                        //Update vehicles and timelines
                        for (TimeUpdate update : updates) {

                            //Ak je newActiveId "unactive", deaktivujem vsetky timelines
                            //Inak deaktivujem vsetky okrem aktivneho, ktory ma zhodne id s newActiveId.
                            if (update instanceof Vehicle) {
                                Boolean isActive = ((Vehicle) update).isActive();
                                String currentId = ((Vehicle) update).getBusId();
                                if (!newActiveId.equals(currentId)) {
                                    ((Vehicle) update).deactivate();
                                }

                                update.update(time);
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

}
