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
    private TextField slowedStreetName;


    private Timer timer;
    private LocalTime time = LocalTime.now();

    private List<Drawable> elements = new ArrayList<>();
    private List<TimeUpdate> updates = new ArrayList<>();

    private List<Street> streetsList = new ArrayList<>();



    @FXML
    private void onSlowStreet() {
        String streetName = (slowedStreetName.getText());
        for (Street street : streetsList) {
            if(street.getStreetName() == streetName){
                street.setStreetSpeed(Street.SLOWED_SPEED);
                System.out.println("street " + streetName + " slowed");
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Street name doest match any of the streets !");
            alert.showAndWait();

        
    }

    @FXML
    private void onSpeedUpStreet() {
        String streetName = (slowedStreetName.getText());
        for (Street street : streetsList) {
            if(street.getStreetName() == streetName){
                street.setStreetSpeed(Street.DEFAULT_SPEED);
                System.out.println("street " + streetName + " back to default speed");
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Street name doest match any of the streets !");
            alert.showAndWait();
        
    }


    @FXML
    private void onTimeScaleChange() {
        try {
            System.out.println("VOLAL SOM ontimescalechange");
            float scale = Float.parseFloat(timeScale.getText());
            if(scale <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid time scale value");
                alert.showAndWait();
                return;
            }
            timer.cancel();
            startTimer(scale);
            //System.out.println("TIMER STARTED");
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid time scale value");
            alert.showAndWait();
        }
    }

    @FXML
    private void onNewTimeSet() {
        // treba tri okna na text plus jeden button na potvdenie 
        try {
            System.out.println("VOLAL SOM ONNEWSETTIME");
            int hours = Integer.parseInt(timeSetHours.getText());

            int minutes = Integer.parseInt(timeSetMinutes.getText());
            int seconds = Integer.parseInt(timeSetSeconds.getText());
            
            timer.cancel();
            time = LocalTime.of(hours, minutes, seconds);
            for(TimeUpdate update : updates) {
                update.newTime(time);
            }
            startTimer(1);
        } catch (Exception ex) {
            System.out.println(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
       

    }

    @FXML
    private void onScroll (ScrollEvent event) {
        event.consume();
        double zoom = event.getDeltaY() > 0 ? 1.1 : 0.9;
        content.setScaleX(zoom * content.getScaleX());
        content.setScaleY(zoom * content.getScaleY());
        content.layout();
    }

    public void setElements(List<Drawable> elements) {
        this.elements = elements;
        for (Drawable elem : elements) {
            content.getChildren().addAll(elem.getGUI());
            if(elem instanceof TimeUpdate) {
                updates.add((TimeUpdate) elem);
            }
        }
    }

    public void startTimer(double scale) {
        System.out.println("TIMER STARTED");
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    
                    time = time.plusSeconds(1);
                    //System.out.println("TIME = " + time);
                    for(TimeUpdate update : updates) {
                        update.update(time);
                    }
                }
                });
            }
        
        }, 0, (long) (1000 / scale));

        


    }

    public Timer getTimer() {
        return timer;
    }


    public void setStreetsList(List<Street> list){
        this.streetsList = list;
    }

}
