package ija.project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    @FXML
    private Pane content;

    @FXML
    private TextField timeScale;

    private Timer timer;
    private LocalTime time = LocalTime.now();

    private List<Drawable> elements = new ArrayList<>();
    private List<TimeUpdate> updates = new ArrayList<>();

    @FXML
    private void onTimeScaleChange() {
        try {
            float scale = Float.parseFloat(timeScale.getText());
            if(scale <= 0) {
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
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                for(TimeUpdate update : updates) {
                    update.update(time);
                }
            }
        }, 0, (long) (1000 / scale));

    }

    public Timer getTimer() {
        return timer;
    }
}
